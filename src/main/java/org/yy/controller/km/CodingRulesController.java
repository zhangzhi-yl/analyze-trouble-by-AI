package org.yy.controller.km;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.km.CodingRulesDetailService;
import org.yy.service.km.CodingRulesService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

/**
 * 说明：编码规则 作者：YuanYes QQ356703572 时间：2020-11-05 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/codingrules")
public class CodingRulesController extends BaseController {

	@Autowired
	private CodingRulesService codingrulesService;

	@Autowired
	private CodingRulesDetailService codingRulesDetailService;
	/**
	 * 根据规则类型获取编码值
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRuleNumByRuleType")
	@ResponseBody
	public Object getRuleNumByRuleType() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String ruleType = pd.getString("CODINGRULESTYPE");
		// 根据规则类型 查询规则详情
		PageData pgData = new PageData();
		pgData.put("CODINGRULESTYPE", ruleType);

		List<PageData> dataByCodingRulesType = codingrulesService.getDataByCodingRulesType(pgData);
		if (CollectionUtils.isEmpty(dataByCodingRulesType)) {
			throw new RuntimeException("获取规则号失败，该类型数据不存在");
		}

		String returnCode = "";
		String CODINGRULEID = "";
		String ACQUISITIONTIME = "";
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (PageData pageData : dataByCodingRulesType) {
			// 根据详情类型 生成对应的编号
			CODINGRULEID = pageData.getString("CODINGRULES_ID");
			String CODINGRULESDETAILID = pageData.getString("CODINGRULESDETAIL_ID");
			String TERMOFVALIDITY = pageData.getString("TERMOFVALIDITY");
			String DETAILTYPE = pageData.getString("DETAILTYPE");
			String FLENGTH = pageData.getString("FLENGTH");
			String FFORMAT = pageData.getString("FFORMAT");
			String TSTEP = pageData.getString("TSTEP");
			String SETTINGVALUE = pageData.getString("SETTINGVALUE");
			String RESETPERIOD = pageData.getString("RESETPERIOD");
			// 进制问题需要确定
			/* String STREAMCODING = pageData.getString("STREAMCODING"); */
			ACQUISITIONTIME = pageData.getString("ACQUISITIONTIME");

			// 判断是否过期

			String termDateTime = TERMOFVALIDITY.substring(12, TERMOFVALIDITY.length());
			Date parse = null;
			parse = sdFormat.parse(termDateTime);
			long time = parse.getTime();
			long currentTimeMillis = new Date().getTime();
			if (time < currentTimeMillis) {
				throw new RuntimeException("该编码规则已过期");
			}

			// 常量类型直接返回当初的设置值
			if ("constant".equals(DETAILTYPE)) {
				returnCode += SETTINGVALUE;
			}

			// 日期类型直接返回当初的设置格式后 格式化时间返回
			if ("date".equals(DETAILTYPE)) {
				String code = "";
				String acqNow = sdFormat.format(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat(FFORMAT);
				try {
					code = sdf.format(sdFormat.parse(ACQUISITIONTIME));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ("everyday".equals(RESETPERIOD)) {
					if (!acqNow.equals(ACQUISITIONTIME)) {
						code = sdf.format(new Date());
					}
				}
				returnCode += code;
			}

			// 根据规则类型生成流水号
			if ("serial".equals(DETAILTYPE)) {
				String acqNow = sdFormat.format(new Date());
				// 如果每天重置，日期变更 重新设置位1开始
				if ("everyday".equals(RESETPERIOD)) {
					if (!acqNow.equals(ACQUISITIONTIME)) {
						SETTINGVALUE = "0";
					}
				}
				// 如果不重置，则接着前一天的时间继续编号
				else {
					acqNow = ACQUISITIONTIME;
				}

				// 自增 步长
				Integer getValueInc = Integer.valueOf(SETTINGVALUE) + Integer.valueOf(TSTEP);
				String formatNum = this.formatNum(getValueInc, Integer.valueOf(FLENGTH));

				// 回写数据
				PageData pgRuleDetail = new PageData();
				pgRuleDetail.put("CODINGRULESDETAIL_ID", CODINGRULESDETAILID);
				PageData findDetailById = codingRulesDetailService.findById(pgRuleDetail);
				findDetailById.put("SETTINGVALUE", formatNum);
				codingRulesDetailService.edit(findDetailById);
				// 返回結果
				returnCode += formatNum;
			}
		}
		// 更新数据
		PageData pgRule = new PageData();
		pgRule.put("CODINGRULES_ID", CODINGRULEID);
		PageData findById = codingrulesService.findById(pgRule);
		findById.put("GETVALUE", returnCode);
		findById.put("ACQUISITIONTIME", sdFormat.format(new Date()));
		codingrulesService.edit(findById);
		map.put("result", errInfo);
		map.put("FNAME", Jurisdiction.getName());
		map.put("FDATE", Tools.date2Str(new Date(),"yyyy-MM-dd"));
		map.put("pd", returnCode);
		return map;
	}

	private String formatNum(int input, int FLENGTH) {
		// 大于1000时直接转换成字符串返回
		double pow = Math.pow(10.00, FLENGTH);
		if (input > pow - 1) {
			throw new RuntimeException("生成失败，编码号已经超过了" + FLENGTH + "位");
		}
		return String.format("%0" + FLENGTH + "d", input);
	}

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("codingrules:add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CODINGRULES_ID", this.get32UUID()); // 主键
		codingrulesService.save(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@RequiresPermissions("codingrules:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		codingrulesService.delete(pd);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	@RequiresPermissions("codingrules:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		codingrulesService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	@RequiresPermissions("codingrules:list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = codingrulesService.list(page); // 列出CodingRules列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	@RequiresPermissions("codingrules:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = codingrulesService.findById(pd); // 根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 批量删除
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAll")
	@RequiresPermissions("codingrules:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			codingrulesService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		} else {
			errInfo = "error";
		}
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("编码类型"); // 1
		titles.add("编码名称"); // 2
		titles.add("适用范围"); // 3
		titles.add("编码规则描述"); // 4
		titles.add("状态"); // 5
		titles.add("有效期"); // 6
		titles.add("初始值"); // 7
		titles.add("获取值"); // 8
		titles.add("获取时间"); // 9
		dataMap.put("titles", titles);
		List<PageData> varOList = codingrulesService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CODINGRULESTYPE")); // 1
			vpd.put("var2", varOList.get(i).getString("CODINGRULESNAME")); // 2
			vpd.put("var3", varOList.get(i).getString("SCOPEOFAPPLICATION")); // 3
			vpd.put("var4", varOList.get(i).getString("CODINGRULESDESCRIPTION")); // 4
			vpd.put("var5", varOList.get(i).getString("FSTATUS")); // 5
			vpd.put("var6", varOList.get(i).getString("TERMOFVALIDITY")); // 6
			vpd.put("var7", varOList.get(i).getString("INITIALKEY")); // 7
			vpd.put("var8", varOList.get(i).getString("GETVALUE")); // 8
			vpd.put("var9", varOList.get(i).getString("ACQUISITIONTIME")); // 9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
