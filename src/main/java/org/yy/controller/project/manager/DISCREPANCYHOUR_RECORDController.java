package org.yy.controller.project.manager;

import java.util.ArrayList;
import java.util.Date;
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
import org.yy.service.project.manager.DISCREPANCYHOUR_RECORDService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

/**
 * 说明：项目工时统计异常说明 作者：YuanYes QQ356703572 时间：2021-09-29 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/discrepancyhour_record")
public class DISCREPANCYHOUR_RECORDController extends BaseController {

	@Autowired
	private DISCREPANCYHOUR_RECORDService discrepancyhour_recordService;

	/**
	 * v1 管悦 20210929 生成差异记录
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
		List<PageData> varList = discrepancyhour_recordService.listAll(pd); // 依据ID获取已生成数据列表
		List<PageData> varListO = discrepancyhour_recordService.getAllListGXDB(pd); // 获取差异信息
		if (varList.size() > 0) { // 判断是否已经生成过记录
			discrepancyhour_recordService.delete(pd); // 已经生成的，再次生成时删除上一次生成的

		}
		if (varListO.size() > 0) { // 判断是否存在差异信息
			for (int i = 0; i < varListO.size(); i++) {
				PageData pdO = varListO.get(i);
				pdO.put("DISCREPANCYHOUR_RECORD_ID", this.get32UUID()); // 主键
				pdO.put("FFOUNDER", Jurisdiction.getName());
				pdO.put("FCREATE_TIME", Tools.date2Str(new Date()));

				pdO.put("PROJECT_ID", pd.get("PROJECT_ID")); // 项目ID
				discrepancyhour_recordService.save(pdO);
			}
		}
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
	@RequiresPermissions("discrepancyhour_record:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		discrepancyhour_recordService.delete(pd);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * v1 20210929 管悦 保存差异原因
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
		pd.put("FMODIFY_NAME", Jurisdiction.getName());
		pd.put("FMODIFY_TIME", Tools.date2Str(new Date()));
		discrepancyhour_recordService.edit(pd);
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
		List<PageData> varList = discrepancyhour_recordService.list(page); // 列出DISCREPANCYHOUR_RECORD列表
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
	@RequiresPermissions("discrepancyhour_record:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = discrepancyhour_recordService.findById(pd); // 根据ID读取
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
	@RequiresPermissions("discrepancyhour_record:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			discrepancyhour_recordService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备注2"); // 1
		titles.add("备注3"); // 2
		titles.add("备注4"); // 3
		titles.add("备注5"); // 4
		titles.add("备注6"); // 5
		titles.add("备注7"); // 6
		titles.add("备注8"); // 7
		titles.add("备注9"); // 8
		titles.add("备注10"); // 9
		titles.add("备注11"); // 10
		titles.add("备注12"); // 11
		titles.add("备注13"); // 12
		dataMap.put("titles", titles);
		List<PageData> varOList = discrepancyhour_recordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FTYPE")); // 1
			vpd.put("var2", varOList.get(i).getString("FPlanHours")); // 2
			vpd.put("var3", varOList.get(i).getString("FUseHours")); // 3
			vpd.put("var4", varOList.get(i).getString("PROJECT_NAME")); // 4
			vpd.put("var5", varOList.get(i).getString("PROJECT_NUMBER")); // 5
			vpd.put("var6", varOList.get(i).getString("CUSTOMER_NAME")); // 6
			vpd.put("var7", varOList.get(i).getString("PROJECT_ID")); // 7
			vpd.put("var8", varOList.get(i).getString("FCREATE_TIME")); // 8
			vpd.put("var9", varOList.get(i).getString("FFOUNDER")); // 9
			vpd.put("var10", varOList.get(i).getString("FMODIFY_TIME")); // 10
			vpd.put("var11", varOList.get(i).getString("FMODIFY_NAME")); // 11
			vpd.put("var12", varOList.get(i).getString("CAUSE_OF_DIFFERENCE")); // 12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
