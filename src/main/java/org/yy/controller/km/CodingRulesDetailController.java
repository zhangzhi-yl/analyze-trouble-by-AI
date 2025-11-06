package org.yy.controller.km;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

/**
 * 说明：自定义编码详情 作者：YuanYes QQ356703572 时间：2020-11-06 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/codingrulesdetail")
public class CodingRulesDetailController extends BaseController {

	@Autowired
	private CodingRulesDetailService codingrulesdetailService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CODINGRULESDETAIL_ID", this.get32UUID()); // 主键
		codingrulesdetailService.save(pd);
		map.put("result", errInfo);
		map.put("id", pd.get("CODINGRULESDETAIL_ID"));
		return map;
	}

	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		codingrulesdetailService.delete(pd);
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
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		codingrulesdetailService.edit(pd);
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
		List<PageData> varList = codingrulesdetailService.list(page); // 列出CodingRulesDetail列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public Object list() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		List<PageData> varList = codingrulesdetailService.listAll(this.getPageData()); // 列出CodingRulesDetail列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping(value = "/listByIds")
	@ResponseBody
	public Object listByIds() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo;
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			List<PageData> varList = codingrulesdetailService.listByIds(ArrayDATA_IDS);
			errInfo = "success";
			map.put("varList", varList);
			map.put("result", errInfo);
		} else {
			errInfo = "error";
		}
		return map;
	}

	/**
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	@RequiresPermissions("codingrulesdetail:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = codingrulesdetailService.findById(pd); // 根据ID读取
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
	@RequiresPermissions("codingrulesdetail:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			codingrulesdetailService.deleteAll(ArrayDATA_IDS);
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
		titles.add("所属编码规则ID"); // 1
		titles.add("明细类型"); // 2
		titles.add("长度"); // 3
		titles.add("格式"); // 4
		titles.add("设置值"); // 5
		titles.add("起始值"); // 6
		titles.add("步长"); // 7
		titles.add("重置周期"); // 8
		titles.add("流水编码"); // 9
		dataMap.put("titles", titles);
		List<PageData> varOList = codingrulesdetailService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CODERULEID")); // 1
			vpd.put("var2", varOList.get(i).getString("DETAILTYPE")); // 2
			vpd.put("var3", varOList.get(i).getString("FLENGTH")); // 3
			vpd.put("var4", varOList.get(i).getString("FFORMAT")); // 4
			vpd.put("var5", varOList.get(i).getString("SETTINGVALUE")); // 5
			vpd.put("var6", varOList.get(i).getString("STARTINGVALUE")); // 6
			vpd.put("var7", varOList.get(i).getString("TSTEP")); // 7
			vpd.put("var8", varOList.get(i).getString("RESETPERIOD")); // 8
			vpd.put("var9", varOList.get(i).getString("STREAMCODING")); // 9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
