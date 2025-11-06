package org.yy.controller.km;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.km.QualityInspectionPlanInspectionDetailsService;
import org.yy.service.system.DictionariesService;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

/**
 * 说明：质检方案检验明细 作者：YuanYes QQ356703572 时间：2020-11-10 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/QualityInspectionPlanInspectionDetails")
public class QualityInspectionPlanInspectionDetailsController extends BaseController {

	@Autowired
	private QualityInspectionPlanInspectionDetailsService QualityInspectionPlanInspectionDetailsService;
	

	/**
	 * 根据前端传过来的参数生成公式值
	 */
	@RequestMapping("/formula")
	@ResponseBody
	public Object formula() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData result = new PageData();
		String SortKey = pd.getString("SortKey");
		String FormulaValue = pd.getString("FormulaValue");
		String QIItemName = pd.getString("QIItemName");
		String Category = pd.getString("Category");
		String StandardType = pd.getString("StandardType");
		String ToleranceType = pd.getString("ToleranceType");
		String FRetain = pd.getString("FRetain");
		String StandardValue = pd.getString("StandardValue");
		String UpperDeviation = pd.getString("UpperDeviation");
		String LowerDeviation = pd.getString("LowerDeviation");
		String FMinimum = pd.getString("FMinimum");
		String FMaximum = pd.getString("FMaximum");
		String FUnit = pd.getString("FUnit");
		String BreakdownOfBadReasons = pd.getString("BreakdownOfBadReasons");
		String BadnessReasonLevel = pd.getString("BadnessReasonLevel");

		if ("人工判断".equals(StandardType)) {
			FormulaValue = "人工判断";
			FRetain = "-";
		}
		if ("区间".equals(StandardType)) {
			FormulaValue = "合格标准: " + FMinimum + "~" + FMaximum + FUnit;
			FRetain = "保留" + FRetain + "位小数";
		}
		if ("允差".equals(StandardType)) {
			if ("数值".equals(ToleranceType)) {
				FormulaValue = "合格标准: " + StandardValue + "(" + UpperDeviation + "~" + LowerDeviation + ")" + FUnit;
				FRetain = "保留" + FRetain + "位小数";
			}
		}
		if ("≥".equals(StandardType) || "=".equals(StandardType) || "≤".equals(StandardType)
				|| "<".equals(StandardType) || ">".equals(StandardType)) {
			FormulaValue = "合格标准: " + StandardType + StandardValue + FUnit;
			FRetain = "保留" + FRetain + "位小数";
		}

		result.put("SortKey", SortKey);
		result.put("QIItemName", QIItemName);
		result.put("Category", Category);
		result.put("FormulaValue", FormulaValue);
		result.put("FRetain", FRetain);
		result.put("BreakdownOfBadReasons", BreakdownOfBadReasons);
		result.put("BadnessReasonLevel", BadnessReasonLevel);
		map.put("result", errInfo);
		map.put("data", result);
		return map;
	}

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
		pd.put("QualityInspectionPlanInspectionDetails_ID", this.get32UUID()); // 主键
		QualityInspectionPlanInspectionDetailsService.save(pd);
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
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		QualityInspectionPlanInspectionDetailsService.delete(pd);
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
		QualityInspectionPlanInspectionDetailsService.edit(pd);
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
		List<PageData> varList = QualityInspectionPlanInspectionDetailsService.list(page); // 列出QualityInspectionPlanInspectionDetails列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 质检方案明细列表(不带分页) 用于下拉框
	 * 
	 * @param QIPlanID
	 *            质检方案ID
	 * @throws Exception
	 *             作者：范贺男
	 */
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public Object listAll(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = QualityInspectionPlanInspectionDetailsService.listAll(pd); // 列出QualityInspectionPlanInspectionDetails列表
		map.put("varList", varList);
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
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData spd = new PageData();
		pd = this.getPageData();
		pd = QualityInspectionPlanInspectionDetailsService.findById(pd); // 根据ID读取
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
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			QualityInspectionPlanInspectionDetailsService.deleteAll(ArrayDATA_IDS);
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
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("排序值"); // 1
		titles.add("质检方案ID"); // 2
		titles.add("质检项ID"); // 3
		titles.add("标准类型"); // 4
		titles.add("允差类型"); // 5
		titles.add("保留"); // 6
		titles.add("标准值"); // 7
		titles.add("上偏差"); // 8
		titles.add("下偏差"); // 9
		titles.add("最小值"); // 10
		titles.add("最大值"); // 11
		titles.add("单位"); // 12
		titles.add("不良原因细分"); // 13
		titles.add("客户"); // 14
		titles.add("公式值"); // 15
		titles.add("备注"); // 16
		dataMap.put("titles", titles);
		List<PageData> varOList = QualityInspectionPlanInspectionDetailsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("SortKey")); // 1
			vpd.put("var2", varOList.get(i).getString("QIPlanID")); // 2
			vpd.put("var3", varOList.get(i).getString("QIItemID")); // 3
			vpd.put("var4", varOList.get(i).getString("StandardType")); // 4
			vpd.put("var5", varOList.get(i).getString("ToleranceType")); // 5
			vpd.put("var6", varOList.get(i).getString("FRetain")); // 6
			vpd.put("var7", varOList.get(i).getString("StandardValue")); // 7
			vpd.put("var8", varOList.get(i).getString("UpperDeviation")); // 8
			vpd.put("var9", varOList.get(i).getString("LowerDeviation")); // 9
			vpd.put("var10", varOList.get(i).get("FMinimum").toString()); // 10
			vpd.put("var11", varOList.get(i).get("FMaximum").toString()); // 11
			vpd.put("var12", varOList.get(i).getString("FUnit")); // 12
			vpd.put("var13", varOList.get(i).getString("BreakdownOfBadReasons")); // 13
			vpd.put("var14", varOList.get(i).getString("FCustomer")); // 14
			vpd.put("var15", varOList.get(i).getString("FormulaValue")); // 15
			vpd.put("var16", varOList.get(i).getString("FExplanation")); // 16
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}
	//手动排序，向上一条
	@RequestMapping(value="/upPX")
	@ResponseBody
	public Object upPXx() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		String result = "";
		PageData pd = new PageData();
		pd=this.getPageData();
		List<PageData> varList = QualityInspectionPlanInspectionDetailsService.listAll(pd);//获取质检项列表
		for (int i = 0; i < varList.size(); i++) {
			PageData pd1 = varList.get(i);
			if(Integer.parseInt(pd.get("SortKey").toString()) != 1){//如果为第一条，不能再向上排序返回error
				if(varList.get(i).get("SortKey") == pd.get("SortKey") || varList.get(i).get("SortKey").equals(pd.get("SortKey"))){//判断当前条为要修改的条
					pd.put("QualityInspectionPlanInspectionDetails_ID", varList.get(i).get("QualityInspectionPlanInspectionDetails_ID"));//当前条目的ID
					pd.put("SortKey", varList.get(i).get("SortKey"));//改之前的排序值，作为筛选条件
					pd.put("NewSortKey", Integer.parseInt(pd.get("SortKey").toString())-1);//新排序值=老排序值-1 
					QualityInspectionPlanInspectionDetailsService.editSortKey(pd);//修改排序值
					
					pd.put("QualityInspectionPlanInspectionDetails_ID", varList.get(i-1).get("QualityInspectionPlanInspectionDetails_ID"));//上一条的ID
					pd.put("SortKey", varList.get(i-1).get("SortKey"));//改之前的排序值，作为筛选条件
					pd.put("NewSortKey", varList.get(i).get("SortKey"));//新排序值=老排序值+1 
					QualityInspectionPlanInspectionDetailsService.editSortKey(pd);//修改排序值
					result = "success";
				}
			}else{
				if(Integer.parseInt(pd.get("SortKey").toString()) != 2 && Integer.parseInt(pd.get("SortKey").toString()) != 1){
					result="error";
				}
			}
		}
		map.put("result", result);
		return map;
	}
	//手动排序，向下一条
		@RequestMapping(value="/downPX")
		@ResponseBody
		public Object downPXx() throws Exception{
			Map<String, Object> map = new HashMap<String,Object>();
			String result = "";
			PageData pd = new PageData();
			PageData pdnew = new PageData();
			pd=this.getPageData();
			List<PageData> varList = QualityInspectionPlanInspectionDetailsService.listAll(pd);//获取质检项列表
			for (int i = 0; i < varList.size(); i++) {
				PageData pd1 = varList.get(i);
				if(Integer.parseInt(pd.get("SortKey").toString()) != varList.size()){//如果为最后一条，不能再向下排序返回error
					if(varList.get(i).get("SortKey") == pd.get("SortKey") || varList.get(i).get("SortKey").equals(pd.get("SortKey"))){//判断当前条为要修改的条
						pdnew.put("QualityInspectionPlanInspectionDetails_ID", varList.get(i).get("QualityInspectionPlanInspectionDetails_ID"));//当前条目的ID
						pdnew.put("SortKey", varList.get(i).get("SortKey"));//改之前的排序值，作为筛选条件
						pdnew.put("NewSortKey", Integer.parseInt(pd.get("SortKey").toString())+1);//新排序值=老排序值-1 
						QualityInspectionPlanInspectionDetailsService.editSortKey(pdnew);//修改排序值
						
						pdnew.put("QualityInspectionPlanInspectionDetails_ID", varList.get(i+1).get("QualityInspectionPlanInspectionDetails_ID"));//下一条的ID
						pdnew.put("SortKey", varList.get(i+1).get("SortKey"));//改之前的排序值，作为筛选条件
						pdnew.put("NewSortKey", varList.get(i).get("SortKey"));//新排序值=老排序值+1 
						QualityInspectionPlanInspectionDetailsService.editSortKey(pdnew);//修改排序值
						result = "success";
					}
				}else{
					if(Integer.parseInt(pd.get("SortKey").toString()) != varList.size()-1 && Integer.parseInt(pd.get("SortKey").toString()) != varList.size()){
						result="error";
					}
				}
			}
			map.put("result", result);
			return map;
		}
}
