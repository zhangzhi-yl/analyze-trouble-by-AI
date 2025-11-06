package org.yy.controller.mm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.CallMaterialDetailsFLService;
import org.yy.service.mm.CallMaterialDetailsService;
import org.yy.service.mm.CallMaterialFLService;
import org.yy.service.mm.CallMaterialOperateService;
import org.yy.service.mm.CallMaterialService;
import org.yy.service.mm.StockService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.pp.WorkRecordService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.util.StringUtil;

/**
 * 说明：分料任务 作者：YuanYes QQ356703572 时间：2021-08-23 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/callmaterialfl")
public class CallMaterialFLController extends BaseController {

	@Autowired
	private CallMaterialFLService callmaterialflService;
	@Autowired
	private CallMaterialDetailsFLService callmaterialdetailsflService;
	@Autowired
	private StockService StockService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private CodingRulesService CodingRulesService;
	@Autowired
	private CallMaterialService CallMaterialService;
	@Autowired
	private CallMaterialOperateService CallMaterialOperateService;
	@Autowired
	private CallMaterialDetailsService CallMaterialDetailsService;
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;
	@Autowired
	private Cabinet_BOMService Cabinet_BOMService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private ProcessWorkOrderExampleService processWorkOrderExampleService;
	@Autowired
	private WorkRecordService WorkRecordService;
	@Autowired
	private StaffService StaffService;

	/**
	 * /**保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	// @RequiresPermissions("callmaterialfl:add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CallMaterialFL_ID", this.get32UUID()); // 主键
		callmaterialflService.save(pd);
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
	// @RequiresPermissions("callmaterialfl:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		callmaterialflService.delete(pd);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * v1 管悦 20210823 分料完成
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/finish")
//	@RequiresPermissions("callmaterialfl:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdX = callmaterialflService.findById(pd);
		List<PageData> listAll = callmaterialdetailsflService.listAll(pdX);
		for (PageData detail : listAll) {

			if (null == detail.get("QuantityCount") || StringUtil.isEmpty(detail.get("QuantityCount").toString())
					|| 0 == Double.parseDouble(detail.get("QuantityCount").toString())) {
				map.put("result", "exception");
				map.put("msg", "有单据没有填写实际数量");
				return map;
			}
		}

		pdX.put("FState", pd.get("FState"));
		pdX.put("FMan1", Jurisdiction.getName());
		pdX.put("FTime1", Tools.date2Str(new Date()));
		callmaterialflService.edit(pdX);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210824 车间接收
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/InStock")
//	@RequiresPermissions("callmaterialfl:edit")
	@ResponseBody
	public Object InStock() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdX = callmaterialflService.findById(pd);
		List<PageData> listAll = callmaterialdetailsflService.listAll(pdX);
		for (PageData detail : listAll) {

			if (StringUtil.isEmpty(detail.getString("TargetWarehouse"))
					|| StringUtil.isEmpty(detail.getString("TargetPosition"))) {
				map.put("result", "exception");
				map.put("msg", "有单据没有选择接收仓库或仓位");
				return map;
			} else if (StringUtil.isEmpty(detail.get("QuantityCount").toString())
					|| StringUtil.isEmpty(detail.get("QuantityCount").toString())) {
				map.put("result", "exception");
				map.put("msg", "有单据没有填写实际数量");
				return map;
			}
		}

		// 直接入到车间库
		// 加库存
		pdX.put("FInMan", Jurisdiction.getName());
		pdX.put("FInTime", Tools.date2Str(new Date()));

		for (PageData detail : listAll) {
			String UnitID = mat_basicService.getUnitID(detail);
			PageData inParam = new PageData();
			inParam.put("Stock_ID", this.get32UUID());

			inParam.put("WarehouseID", detail.getString("TargetWarehouseID"));// 仓库id
			inParam.put("PositionID", detail.getString("TargetPositionID"));// 库位id
			inParam.put("ItemID", detail.getString("Material_ID"));// 物料id
			inParam.put("StorageStatus", "车间");// 存储状态(工厂,车间)
			inParam.put("QRCode", detail.getString("MaterialCode"));// 物料二维码
			inParam.put("MaterialSPropKey", detail.getString("MAT_AUXILIARYMX_ID"));// 物料辅助属性值
			inParam.put("SpecificationDesc", detail.getString("Specification"));// 规格
			inParam.put("ActualCount", detail.get("QuantityCount").toString());// 实际数量
			inParam.put("FBatch", detail.getString("BatchNum"));// 批号
			inParam.put("ProductionBatch", detail.getString("BatchNum"));// 生产批号
			inParam.put("FUnit", UnitID);// 单位
			inParam.put("UseCount", 0);// 使用数量
			inParam.put("UseIf", "NO");// 是否占用
			inParam.put("FLevel", 1);// 等级

			inParam.put("RunningState", "在用");// 运行状态
			inParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
			inParam.put("FCreateTime", Jurisdiction.getUserId());// 创建人
			inParam.put("UsageTime", Tools.date2Str(new Date()));// 使用时间
			inParam.put("DateOfManufacture", Tools.date2Str(new Date()));// 生产日期

			inParam.put("LastModifiedTime", Tools.date2Str(new Date()));// 最后修改日期
			inParam.put("LastCountTime", Tools.date2Str(new Date()));// 上次盘点日期
			inParam.put("FStatus", "入库");// 状态

			inParam.put("ProjectNum", pdX.getString("PlanningWorkOrderNum"));// 计划工单编号

			StockService.inStock(inParam);
		}

		pdX.put("FState", pd.get("FState"));
		pdX.put("FInMan", Jurisdiction.getName());
		pdX.put("FInTime", Tools.date2Str(new Date()));
		pdX.put("FIsYL", pdX.get("FIsYLNew"));
		if (pdX.get("FIsYLNew").equals("否")) {
			pd.put("WPName", "分料");
			PageData pdOp = new PageData();
			String name = Jurisdiction.getName();
			pdOp.put("FNAME", name);
			String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");

			pd.put("ProcessIMaterielCode", pdX.getString("PlanningWorkOrderNum"));
			pd.put("WPName", "分料");
			PageData appProcessWorkOrderExampleDetailByPK = processWorkOrderExampleService.findByWP(pd);
			/*
			 * appProcessWorkOrderExampleDetailByPK.put("ProcessWorkOrderExample_ID",
			 * pd.get("ProcessWorkOrderExample_ID"));
			 */
			if (appProcessWorkOrderExampleDetailByPK.get("WPType") == "投入"
					|| appProcessWorkOrderExampleDetailByPK.get("WPType").equals("投入")) {
				appProcessWorkOrderExampleDetailByPK.put("FType", "消耗");
				appProcessWorkOrderExampleDetailByPK.put("WPTYPE", "投入");
			} else {
				appProcessWorkOrderExampleDetailByPK.put("FType", "产出");
				appProcessWorkOrderExampleDetailByPK.put("WPTYPE", "产出");
			}
			appProcessWorkOrderExampleDetailByPK.put("FStatus", "结束");
			appProcessWorkOrderExampleDetailByPK.put("FRUN_STATE", "结束");
			appProcessWorkOrderExampleDetailByPK.put("FOPERATOR", STAFF_ID);
			appProcessWorkOrderExampleDetailByPK.put("EXECUTE_TIME", Tools.date2Str(new Date()));
			appProcessWorkOrderExampleDetailByPK.put("TIME_STAMP", System.currentTimeMillis());
			appProcessWorkOrderExampleDetailByPK.put("ActualEndTime", Tools.date2Str(new Date()));
			pd.put("EndTime", Tools.date2Str(new Date()));
			pd.put("ProcessWorkOrderExample_ID",
					appProcessWorkOrderExampleDetailByPK.get("ProcessWorkOrderExample_ID"));
			WorkRecordService.editEnd(pd);
			processWorkOrderExampleService.edit(appProcessWorkOrderExampleDetailByPK);
		}
		callmaterialflService.edit(pdX);

		PageData pdC = CallMaterialService.findById(pdX);
		pdC.put("FState", "车间入库");
		pdC.put("FInMan", Jurisdiction.getName());
		pdC.put("FInTime", Tools.date2Str(new Date()));
		CallMaterialService.edit(pdC);
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
	@RequiresPermissions("callmaterialfl:list")
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
		List<PageData> varList = callmaterialflService.list(page); // 列出CallMaterialFL列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210824 历史分料列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listHis")
	@ResponseBody
	public Object listHis(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = callmaterialflService.listHis(page); // 列出CallMaterialFL列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210824 车间入库列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listCJ")
	@ResponseBody
	public Object listCJ(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = callmaterialflService.listCJ(page); // 列出CallMaterialFL列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210825 车间入库历史列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listCJHis")
	@ResponseBody
	public Object listCJHis(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = callmaterialflService.listCJHis(page); // 列出CallMaterialFL列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210825 车间入库未请料列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listCJHisWL")
	@ResponseBody
	public Object listCJHisWL(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = callmaterialflService.listCJHisWL(page); // 列出CallMaterialFL列表
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
	// @RequiresPermissions("callmaterialfl:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = callmaterialflService.findById(pd); // 根据ID读取
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
	// @RequiresPermissions("callmaterialfl:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			callmaterialflService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备注1"); // 1
		titles.add("备注2"); // 2
		titles.add("备注3"); // 3
		titles.add("备注4"); // 4
		titles.add("备注5"); // 5
		titles.add("备注6"); // 6
		titles.add("备注7"); // 7
		titles.add("备注8"); // 8
		titles.add("备注9"); // 9
		titles.add("备注10"); // 10
		titles.add("主计划单号"); // 11
		titles.add("关联ID"); // 12
		titles.add("备注13"); // 13
		titles.add("备注14"); // 14
		titles.add("备注15"); // 15
		titles.add("备注16"); // 16
		titles.add("备注17"); // 17
		titles.add("备注18"); // 18
		titles.add("备注19"); // 19
		titles.add("备注20"); // 20
		titles.add("分料完成时间"); // 21
		titles.add("分料完成人"); // 22
		dataMap.put("titles", titles);
		List<PageData> varOList = callmaterialflService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CallMaterial_ID")); // 1
			vpd.put("var2", varOList.get(i).getString("DocumentNum")); // 2
			vpd.put("var3", varOList.get(i).getString("NeedUseTime")); // 3
			vpd.put("var4", varOList.get(i).getString("SaleOrderNum")); // 4
			vpd.put("var5", varOList.get(i).getString("PlanningWorkOrderNum")); // 5
			vpd.put("var6", varOList.get(i).getString("TaskNum")); // 6
			vpd.put("var7", varOList.get(i).getString("WokNum")); // 7
			vpd.put("var8", varOList.get(i).getString("MakePersonID")); // 8
			vpd.put("var9", varOList.get(i).getString("MakeTime")); // 9
			vpd.put("var10", varOList.get(i).getString("FState")); // 10
			vpd.put("var11", varOList.get(i).getString("PlanningWorkOrderMasterNum")); // 11
			vpd.put("var12", varOList.get(i).getString("Associated_ID")); // 12
			vpd.put("var13", varOList.get(i).getString("FCallTime")); // 13
			vpd.put("var14", varOList.get(i).getString("FCallMan")); // 14
			vpd.put("var15", varOList.get(i).getString("FOutTime")); // 15
			vpd.put("var16", varOList.get(i).getString("FOutMan")); // 16
			vpd.put("var17", varOList.get(i).getString("FInTime")); // 17
			vpd.put("var18", varOList.get(i).getString("FInMan")); // 18
			vpd.put("var19", varOList.get(i).getString("FRecieveTime")); // 19
			vpd.put("var20", varOList.get(i).getString("FRecieveMan")); // 20
			vpd.put("var21", varOList.get(i).getString("FTime1")); // 21
			vpd.put("var22", varOList.get(i).getString("FMan1")); // 22
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * v1 管悦 20210825 生成叫料任务
	 * 
	 * @param PlanningWorkOrderID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/xiafa")
	public Object xiafa() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = this.getPageData();
		pd.put("FFTYPE", "生产任务");
		String IDS = pd.getString("IDS");
		String PlanningWorkOrderNumStrs = "";
		String[] strs = IDS.split(",yl,");

		PageData pdCX = new PageData();
		if (errInfo.equals("success")) {
			// 创建库房叫料单
			for (String param : strs) {
				PageData pdOrx = new PageData();
				String CallMaterialFL_ID = param;
				pdOrx.put("CallMaterialFL_ID", CallMaterialFL_ID);
				PageData subPlan = callmaterialflService.findById(pdOrx);
				PlanningWorkOrderNumStrs += "," + subPlan.getString("PlanningWorkOrderNum");
			}
			// 创建库房叫料单
			PlanningWorkOrderNumStrs = PlanningWorkOrderNumStrs.substring(1);

			// 创建叫料申请

			pdCX.put("CallMaterial_ID", this.get32UUID()); // 主键
			pdCX.put("MakePersonID", Jurisdiction.getName());
			pdCX.put("MakeTime", Tools.date2Str(new Date()));
			pdCX.put("DocumentNum", (String) CodingRulesService.getRuleNumByRuleType("JLSQ"));
			pdCX.put("FState", "叫料");
			pdCX.put("FCallTime", Tools.date2Str(new Date()));
			pdCX.put("FCallMan", Jurisdiction.getName());
			pdCX.put("MakeTime", Tools.date2Str(new Date()));
			pdCX.put("PlanningWorkOrderNum", PlanningWorkOrderNumStrs);
			pdCX.put("NeedUseTime", Tools.date2Str(new Date()));
			CallMaterialService.save(pdCX);

			// 加入分料单明细
			PageData operateX = new PageData();
			operateX.put("CallMaterialOperate_ID", this.get32UUID());
			operateX.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
			operateX.put("OperatePerson", Jurisdiction.getName());
			operateX.put("OperateTime", Tools.date2Str(new Date()));
			operateX.put("FState", "叫料");
			CallMaterialOperateService.save(operateX);

			if (StringUtil.isNotEmpty(IDS)) {
				List<String> CallMaterialList = CollectionUtils.asList(IDS.split(",yl,"));
				List<PageData> allDetailList = Lists.newArrayList();
				for (String num : CallMaterialList) {
					PageData pdOrx = new PageData();
					String CallMaterialFL_ID = num;
					pdOrx.put("CallMaterialFL_ID", CallMaterialFL_ID);
					PageData subPlan = callmaterialflService.findById(pdOrx);
					String PlanningWorkOrderNum = subPlan.getString("PlanningWorkOrderNum");

					String mat_id = "";
					PageData pdData = new PageData();
					pdData.put("WorkOrderNum", PlanningWorkOrderNum);
					List<PageData> listAll = PlanningWorkOrderService.listAll(pdData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						mat_id = listAll.get(0).getString("OutputMaterialID");
						PageData matParam = new PageData();
						matParam.put("MAT_BASIC_ID", mat_id);
						PageData MAT_INFO = mat_basicService.findById(matParam);
						if (null != MAT_INFO) {
							PageData cadparam = new PageData();
							cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
							List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
							if (CollectionUtil.isNotEmpty(cadList)) {
								PageData bomParam = new PageData();
								bomParam.put("FFTYPE", "BOM");

								bomParam.put("CallMaterialFL_ID", CallMaterialFL_ID);
								List<PageData> cadBomList = callmaterialdetailsflService.listAllQL(bomParam);

								String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
								String MAT_AUXILIARYMX_ID = "";
								PageData pg1 = new PageData();
								pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
								pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
								List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
										.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
								if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
									MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
											.getString("MAT_AUXILIARYMX_ID");
								}

								for (PageData bom : cadBomList) {
									PageData cmData = new PageData();
									cmData.put("CallMaterialDetails_ID", this.get32UUID()); // 主键
									cmData.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
									cmData.put("TType", "计划生成");
									cmData.put("OperatePersion", Jurisdiction.getName());
									cmData.put("OperateTime", Tools.date2Str(new Date()));

									cmData.put("Material_ID", bom.getString("Material_ID"));
									cmData.put("MaterialName", bom.getString("MaterialName"));

									cmData.put("Specification", bom.getString("Specification"));
									cmData.put("DemandNum", bom.get("DiffNum").toString());
									cmData.put("QuantityCount", bom.get("DiffNum").toString());

									cmData.put("MaterialCode", bom.getString("MaterialCode"));
									cmData.put("FUnitName", bom.getString("FUnitName"));

									cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
									cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

									allDetailList.add(cmData);

								}
							}
						}
					}
				}

				Map<String, PageData> result2 = new HashMap<String, PageData>();
				for (PageData cmData : allDetailList) {
					String Material_ID = cmData.get("Material_ID").toString();
					BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
					if (result2.containsKey(Material_ID)) {
						BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
						value = value.add(temp);
						result2.get(Material_ID).put("DemandNum", value);
						result2.get(Material_ID).put("QuantityCount", value);
						continue;
					}
					result2.put(Material_ID, cmData);
				}

				for (PageData cmDetailData : result2.values()) {
					CallMaterialDetailsService.save(cmDetailData);
				}
				PageData pdNotice1 = new PageData();

				// 跳转页面
				pdNotice1.put("AccessURL", "../../../views/mm/callmaterialfl/callmaterialfl_editAll.html");//
				pdNotice1.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice1.put("ReadPeople", ",");// 已读人默认空
				pdNotice1.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice1.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice1.put("TType", "消息推送");// 消息类型
				pdNotice1.put("FContent", "工单编号为" + PlanningWorkOrderNumStrs + "的计划工单即将用料，请关注（查看备货任务）");// 消息正文
				pdNotice1.put("FTitle", "分料任务");// 消息标题
				pdNotice1.put("LinkIf", "no");// 是否跳转页面
				pdNotice1.put("DataSources", "分料任务");// 数据来源
				pdNotice1.put("ReceivingAuthority", ",FLZ,KF,");// 接收人
				pdNotice1.put("Report_Key", "task");// 手机app
				pdNotice1.put("Report_Value", "");// 手机app
				noticeService.save(pdNotice1);
				SendWeChatMessageMes weChat = new SendWeChatMessageMes();
				PageData pd3 = new PageData();
				// String Name = Jurisdiction.getName();
				String Name = "管悦";
				pd3.put("NAME", Name);
				// PageData pd2 = usersService.getPhone(pd3);
				String phone = "";
				/*
				 * if(null!=pd2){ phone = pd2.getString("phone"); }else{ phone=""; }
				 */
				String WXNR = "【分料任务】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice1.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
			}

		}

		if (errInfo.equals("success")) {
			for (String CallMaterialFL_ID : strs) {
				pd.put("CallMaterialFL_ID", CallMaterialFL_ID);
				PageData subPlan = callmaterialflService.findById(pd);
				if (null != subPlan) {
					PageData po = new PageData();
					// 消息推送通知库房，备货通知：哪个订单即将用料，请关注（查看备货任务）
					PageData pdNotice1 = new PageData();

					// 跳转页面
					pdNotice1.put("AccessURL", "../../../views/mm/CallMaterial/cangku/CallMaterial_list.html");//
					pdNotice1.put("NOTICE_ID", this.get32UUID()); // 主键
					pdNotice1.put("ReadPeople", ",");// 已读人默认空
					pdNotice1.put("FIssuedID", Jurisdiction.getName()); // 发布人
					pdNotice1.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
					pdNotice1.put("TType", "消息推送");// 消息类型
					pdNotice1.put("FContent",
							"工单编号为" + subPlan.getString("PlanningWorkOrderNum") + "的计划工单待分料，请关注（查看备货任务）");// 消息正文
					pdNotice1.put("FTitle", "备货通知");// 消息标题
					pdNotice1.put("LinkIf", "no");// 是否跳转页面
					pdNotice1.put("DataSources", "备货通知");// 数据来源
					pdNotice1.put("ReceivingAuthority", ",KF,");// 接收人
					pdNotice1.put("Report_Key", "task");// 手机app
					pdNotice1.put("Report_Value", "");// 手机app
					noticeService.save(pdNotice1);

					SendWeChatMessageMes weChat = new SendWeChatMessageMes();
					PageData pd3 = new PageData();
					// String Name = Jurisdiction.getName();
					String Name = "管悦";
					pd3.put("NAME", Name);
					// PageData pd2 = usersService.getPhone(pd3);
					String phone = "";
					/*
					 * if(null!=pd2){ phone = pd2.getString("phone"); }else{ phone=""; }
					 */
					String WXNR = "【备货通知】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
							+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice1.get("FContent");
					// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
					// "0");
					weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");

					// 创建叫料申请
					PageData pdC = new PageData();
					pdC.put("CallMaterialFL_ID", this.get32UUID()); // 主键
					pdC.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
					pdC.put("MakeTime", Tools.date2Str(new Date()));
					pdC.put("DocumentNum", (String) CodingRulesService.getRuleNumByRuleType("JLSQ"));
					pdC.put("FState", "待分料");
					pdC.put("FCallTime", Tools.date2Str(new Date()));
					pdC.put("FCallMan", Jurisdiction.getName());
					pdC.put("MakeTime", Tools.date2Str(new Date()));
					pdC.put("PlanningWorkOrderNum", subPlan.getString("PlanningWorkOrderNum"));
					pdC.put("NeedUseTime", Tools.date2Str(new Date()));
					pdC.put("FIsYL", "是");
					callmaterialflService.save(pdC);

					// 加入操作记录
					PageData operate = new PageData();
					operate.put("CallMaterialOperate_ID", this.get32UUID());
					operate.put("CallMaterial_ID", pdC.getString("CallMaterial_ID"));
					operate.put("OperatePerson", Jurisdiction.getName());
					operate.put("OperateTime", Tools.date2Str(new Date()));
					operate.put("FState", "叫料任务");
					CallMaterialOperateService.save(operate);

					String PlanningWorkOrderNumStr = subPlan.getString("PlanningWorkOrderNum");
					if (StringUtil.isNotEmpty(PlanningWorkOrderNumStr)) {
						List<String> PlanningWorkOrderNumList = CollectionUtils
								.asList(PlanningWorkOrderNumStr.split(","));
						List<PageData> allDetailList = Lists.newArrayList();
						for (String num : PlanningWorkOrderNumList) {
							String mat_id = "";
							PageData pdData = new PageData();
							pdData.put("WorkOrderNum", num);
							List<PageData> listAll = PlanningWorkOrderService.listAll(pdData);
							if (CollectionUtil.isNotEmpty(listAll)) {
								mat_id = listAll.get(0).getString("OutputMaterialID");
								PageData matParam = new PageData();
								matParam.put("MAT_BASIC_ID", mat_id);
								PageData MAT_INFO = mat_basicService.findById(matParam);
								if (null != MAT_INFO) {
									PageData cadparam = new PageData();
									cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
									List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
									if (CollectionUtil.isNotEmpty(cadList)) {
										PageData bomParam = new PageData();
										bomParam.put("FFTYPE", "BOM");

										bomParam.put("CallMaterialFL_ID", CallMaterialFL_ID);
										List<PageData> cadBomList = callmaterialdetailsflService.listAllQL(bomParam);

										String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
										String MAT_AUXILIARYMX_ID = "";
										PageData pg1 = new PageData();
										pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
										pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
										List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
												.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
										if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
											MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
													.getString("MAT_AUXILIARYMX_ID");
										}

										for (PageData bom : cadBomList) {
											PageData cmData = new PageData();
											cmData.put("CallMaterialDetailsFL_ID", this.get32UUID()); // 主键
											cmData.put("CallMaterial_ID", pdC.getString("CallMaterial_ID"));
											cmData.put("CallMaterialFL_ID", pdC.getString("CallMaterialFL_ID"));
											cmData.put("TType", "计划生成");
											cmData.put("OperatePersion", Jurisdiction.getName());
											cmData.put("OperateTime", Tools.date2Str(new Date()));

											cmData.put("Material_ID", bom.getString("Material_ID"));
											cmData.put("MaterialName", bom.getString("MaterialName"));

											cmData.put("Specification", bom.getString("Specification"));
											cmData.put("DemandNum", bom.get("DiffNum").toString());
											cmData.put("QuantityCount", bom.get("DiffNum").toString());

											cmData.put("MaterialCode", bom.getString("MaterialCode"));
											cmData.put("FUnitName", bom.getString("FUnitName"));

											cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
											cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

											allDetailList.add(cmData);

										}
									}
								}
							}
						}

						Map<String, PageData> result2 = new HashMap<String, PageData>();
						for (PageData cmData : allDetailList) {
							String Material_ID = cmData.get("Material_ID").toString();
							BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
							if (result2.containsKey(Material_ID)) {
								BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
								value = value.add(temp);
								result2.get(Material_ID).put("DemandNum", value);
								result2.get(Material_ID).put("QuantityCount", value);
								continue;
							}
							result2.put(Material_ID, cmData);
						}

						for (PageData cmDetailData : result2.values()) {
							callmaterialdetailsflService.save(cmDetailData);
						}

						// 反写是否有余料
						subPlan.put("FIsYL", "否");
						subPlan.put("CallMaterialFL_ID", CallMaterialFL_ID);
						callmaterialflService.edit(subPlan);
					}
				}
			}
		}
		map.put("result", errInfo);
		return map;
	}

}
