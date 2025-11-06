package org.yy.controller.qm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.fop.fonts.type1.PFBData;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.github.pagehelper.util.StringUtil;

import org.yy.entity.PageData;
import org.yy.service.km.QualityInspectionPlanInspectionDetailsService;
import org.yy.service.km.QualityInspectionPlanService;
import org.yy.service.qm.QualityInspectionPlanDetailExecuteService;
import org.yy.service.qm.QualityInspectionPlanExecuteSampleService;
import org.yy.service.qm.QualityInspectionPlanExecuteService;

/**
 * 说明：样本管理 作者：YuanYes QQ356703572 时间：2021-03-07 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/QualityInspectionPlanExecuteSample")
public class QualityInspectionPlanExecuteSampleController extends BaseController {

	@Autowired
	private QualityInspectionPlanExecuteSampleService QualityInspectionPlanExecuteSampleService;
	@Autowired
	private QualityInspectionPlanService QualityInspectionPlanService;
	@Autowired
	private QualityInspectionPlanInspectionDetailsService QualityInspectionPlanInspectionDetailsService;
	@Autowired 
	private QualityInspectionPlanDetailExecuteService QualityInspectionPlanDetailExecuteService;
	
	@Autowired
	private QualityInspectionPlanExecuteService QualityInspectionPlanExecuteService;
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
		pd.put("QualityInspectionPlanExecuteSample_ID", this.get32UUID()); // 主键
		pd.put("FState", "执行中");
		pd.put("SampleRegisterTime", Tools.date2Str(new Date()));
		QualityInspectionPlanExecuteSampleService.save(pd);
		PageData pData = new PageData();
		pData.put("QIPlanID", pd.getString("QIPlanID"));
		List<PageData> listAll = QualityInspectionPlanInspectionDetailsService.listAll(pData);
		for (PageData pageData : listAll) {
			pageData.put("QualityInspectionPlanExecuteSample_ID", pd.getString("QualityInspectionPlanExecuteSample_ID"));
			pageData.put("QualityInspectionPlanExecute_ID",  pd.getString("QualityInspectionPlanExecute_ID"));
			pageData.put("QualityInspectionPlanDetailExecute_ID", this.get32UUID());
			QualityInspectionPlanDetailExecuteService.save(pageData);
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
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		QualityInspectionPlanExecuteSampleService.delete(pd);
		List<PageData> listAll = QualityInspectionPlanDetailExecuteService.listAll(pd);
		for (PageData pageData : listAll) {
			QualityInspectionPlanDetailExecuteService.delete(pageData);
		}
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
		QualityInspectionPlanExecuteSampleService.edit(pd);
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
		List<PageData> varList = QualityInspectionPlanExecuteSampleService.list(page); // 列出QualityInspectionPlanExecuteSample列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获取左边指标
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getLeftIndicators")
	@ResponseBody
	public Object getLeftIndicators() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = QualityInspectionPlanExecuteSampleService.listAll(pd);
		// 样品总数
		BigDecimal SampleCount = new BigDecimal("0");
		// 合格数
		BigDecimal QualifiedCount = new BigDecimal("0");
		// 让步接收数
		BigDecimal CompromisedCount = new BigDecimal("0");
		// 不合格数
		BigDecimal DisqualifiedCount = new BigDecimal("0");
		// 合格率
		BigDecimal AcceptanceRate = new BigDecimal("0");

		for (PageData pageData : varList) {
			if (StringUtil.isNotEmpty(pageData.getString("SampleCount"))) {
				SampleCount = SampleCount.add(new BigDecimal(pageData.getString("SampleCount")));
			}
			if (StringUtil.isNotEmpty(pageData.getString("QualifiedCount"))) {
				QualifiedCount = QualifiedCount.add(new BigDecimal(pageData.getString("QualifiedCount")));
			}
			if (StringUtil.isNotEmpty(pageData.getString("CompromisedCount"))) {
				CompromisedCount = CompromisedCount.add(new BigDecimal(pageData.getString("CompromisedCount")));
			}
			if (StringUtil.isNotEmpty(pageData.getString("DisqualifiedCount"))) {
				DisqualifiedCount = DisqualifiedCount.add(new BigDecimal(pageData.getString("DisqualifiedCount")));
			}
		}
		if (QualifiedCount.intValue() != 0) {
			AcceptanceRate = (QualifiedCount.add(CompromisedCount)).divide(SampleCount, 4, RoundingMode.HALF_UP)
					.multiply(new BigDecimal("100"));
		}

		pd.put("SampleCount", SampleCount);
		pd.put("QualifiedCount", QualifiedCount);
		pd.put("CompromisedCount", CompromisedCount);
		pd.put("DisqualifiedCount", DisqualifiedCount);
		pd.put("AcceptanceRate", AcceptanceRate);
		map.put("pd", pd);
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
	public Object listAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pData = new PageData();
		
		PageData PlanExecute = QualityInspectionPlanExecuteService.findById(pd);
		String ExecutionStatus = PlanExecute.getString("ExecutionStatus");
		
		pData.put("QualityInspectionPlan_ID", pd.getString("QIPlanID"));
		PageData findById = QualityInspectionPlanService.findById(pData);
		String IsChemistry = "否";
		if ("是".equals(findById.getString("IsChemistry"))) {
			IsChemistry = "是";
		}
		List<PageData> varList = QualityInspectionPlanExecuteSampleService.listAll(pd); // 列出QualityInspectionPlanExecuteSample列表
		for (PageData pageData : varList) {
			pageData.put("IsChemistry", IsChemistry);
			pageData.put("TaskStatus", ExecutionStatus);
			if (StringUtil.isEmpty(pageData.getString("SampleCount"))) {
				pageData.put("SampleCount", 0);
			}
			if (StringUtil.isEmpty(pageData.getString("QualifiedCount"))) {
				pageData.put("QualifiedCount", 0);
				pageData.put("AcceptanceRate", "0");
			} else {
				pageData.put("AcceptanceRate",
						(new BigDecimal(pageData.getString("QualifiedCount"))
						.add(new BigDecimal(pageData.getString("CompromisedCount"))))
								.divide(new BigDecimal(pageData.getString("SampleCount")), 4, RoundingMode.HALF_UP)
								.multiply(new BigDecimal("100")));
			}
			if (StringUtil.isEmpty(pageData.getString("CompromisedCount"))) {
				pageData.put("CompromisedCount", 0);
			}
			if (StringUtil.isEmpty(pageData.getString("DisqualifiedCount"))) {
				pageData.put("DisqualifiedCount", 0);
			}
		}
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
		pd = this.getPageData();
		pd = QualityInspectionPlanExecuteSampleService.findById(pd); // 根据ID读取
		
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
			QualityInspectionPlanExecuteSampleService.deleteAll(ArrayDATA_IDS);
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
		titles.add("质检任务ID"); // 1
		titles.add("标签码"); // 2
		titles.add("抽样数量"); // 3
		titles.add("合格数量"); // 4
		titles.add("让步接收数量"); // 5
		titles.add("不合格数量"); // 6
		titles.add("样品登记时间"); // 7
		titles.add("状态"); // 8
		titles.add("检验方"); // 9
		titles.add("次号"); // 10
		dataMap.put("titles", titles);
		List<PageData> varOList = QualityInspectionPlanExecuteSampleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("QualityInspectionPlanExecute_ID")); // 1
			vpd.put("var2", varOList.get(i).getString("TagCode")); // 2
			vpd.put("var3", varOList.get(i).getString("SampleCount")); // 3
			vpd.put("var4", varOList.get(i).getString("QualifiedCount")); // 4
			vpd.put("var5", varOList.get(i).getString("CompromisedCount")); // 5
			vpd.put("var6", varOList.get(i).getString("DisqualifiedCount")); // 6
			vpd.put("var7", varOList.get(i).getString("SampleRegisterTime")); // 7
			vpd.put("var8", varOList.get(i).getString("FState")); // 8
			vpd.put("var9", varOList.get(i).getString("InspectionParty")); // 9
			vpd.put("var10", varOList.get(i).getString("PatchNum")); // 10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
