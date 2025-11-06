package org.yy.controller.pp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.app.AppRegistrationService;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.flow.MASTER_PLANService;
import org.yy.service.flow.NEWPLAN_BOMService;
import org.yy.service.flow.NEW_BOMService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.InputOutputService;
import org.yy.service.km.KMCustomerService;
import org.yy.service.km.MaterialConsumeService;
import org.yy.service.km.ProcessDefinitionService;
import org.yy.service.km.ProductionBOMService;
import org.yy.service.km.SopStepService;
import org.yy.service.km.WorkingProcedureExampleService;
import org.yy.service.km.WorkingProcedureService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_AUXILIARYService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.WC_StationService;
import org.yy.service.mom.WH_LocationService;
import org.yy.service.mom.WH_WareHouseService;
import org.yy.service.mom.WorkStationPersonRelService;
import org.yy.service.pp.PlanningWorkOrderMasterService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.pp.ProcessWorkOrderExample_SopStepService;
import org.yy.service.pp.SALESORDERDETAILService;
import org.yy.service.pp.SALESORDERService;
import org.yy.service.pp.WorkorderProcessIOExampleService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.service.run.SecondStageService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileUpload;
import org.yy.util.JpushClientUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.beust.jcommander.internal.Sets;
import com.github.pagehelper.util.StringUtil;

/**
 * 说明：计划工单 作者：YuanYes QQ356703572 时间：2020-11-11 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PlanningWorkOrder")
public class PlanningWorkOrderController extends BaseController {
	@Autowired
	private WC_StationService WC_StationService;
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private PlanningWorkOrderMasterService PlanningWorkOrderMasterService;
	@Autowired
	private CodingRulesService CodingRulesService;
	@Autowired
	private StaffService StaffService;
	@Autowired
	private AttachmentSetService AttachmentSetService;
	@Autowired
	private MaterialRequirementService MaterialRequirementService;
	@Autowired
	private WorkingProcedureExampleService WorkingProcedureExampleService;
	@Autowired
	private InputOutputService inputOutputService;
	@Autowired
	private ProductionBOMService productionBOMService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private ProcessWorkOrderExampleService ProcessWorkOrderExampleService;
	@Autowired
	private StockService StockService;
	@Autowired
	private SALESORDERService salesorderService;
	@Autowired
	private SALESORDERDETAILService salesorderdetailService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	@Autowired
	private MAT_AUXILIARYService mat_auxiliaryService;
	@Autowired
	private ProcessDefinitionService ProcessDefinitionService;// 工序定义serverce
	@Autowired
	private MASTER_PLANService MASTER_PLANService;
	@Autowired
	private KMCustomerService CustomerService;
	@Autowired
	private BYTEARRAYService BYTEARRAYService;
	@Autowired
	private NEW_BOMService NEW_BOMService;
	@Autowired
	private NEWPLAN_BOMService NEWPLAN_BOMService;
	@Autowired
	private WH_LocationService WH_LocationService;
	@Autowired
	private WH_WareHouseService WH_WareHouseService;
	@Autowired
	private SecondStageService SecondStageService;
	@Autowired
	private WorkorderProcessIOExampleService WorkorderProcessIOExampleService;
	@Autowired
	private ProcessWorkOrderExample_SopStepService pwoeSopStepService;
	@Autowired
	private SopStepService SopStepService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PlanningWorkOrderService planningWorkOrderService;
	@Autowired
	private MaterialConsumeService MaterialConsumeService;
	@Autowired
	private ProcessWorkOrderExampleService processWorkOrderExampleService;
	@Autowired
	private WorkStationPersonRelService WorkStationPersonRelService;
	@Autowired
	private WorkorderProcessIOExampleService workorderProcessIOExampleService;
	@Autowired
	private WorkingProcedureService WorkingProcedureService;
	@Autowired
	private WorkStationPersonRelService workstationpersonrelService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private AppRegistrationService AppRegistrationService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;
	@Autowired
	private Cabinet_BOMService Cabinet_BOMService;
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 删除节点和线
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteByMasterPlanIdAndNodeId")
	@ResponseBody
	public Object deleteByMasterPlanIdAndNodeId() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MASTER_PLANService.deleteByMasterPlanIdAndNodeId(pd);
		map.put("result", errInfo);// 返回结果
		return map;
	}

	/**
	 * 增加线
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addLine")
	@ResponseBody
	public Object addLine() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdOp = new PageData();
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
		map.put("result", errInfo);
		PageData masterPlan = new PageData();
		String NODE_ID = pd.getString("NODE_ID");
		String NODE_NAME = pd.getString("NODE_NAME");
		String NODE_TYPE = pd.getString("NODE_TYPE");
		String BEGIN_NODE = pd.getString("BEGIN_NODE");
		String END_NODE = pd.getString("END_NODE");
		masterPlan.put("MASTER_PLAN_FLOW_ID", this.get32UUID()); // 主键
		masterPlan.put("NODE_ID", NODE_ID);
		masterPlan.put("NODE_NAME", NODE_NAME);
		masterPlan.put("NODE_KIND", "line");
		masterPlan.put("NODE_TYPE", NODE_TYPE);
		masterPlan.put("PHASE_TYPE", "");
		masterPlan.put("JUMP_CONDITION", "");
		masterPlan.put("BEGIN_NODE", BEGIN_NODE);
		masterPlan.put("END_NODE", END_NODE);
		masterPlan.put("FCREATOR", STAFF_ID);
		masterPlan.put("CREATE_TIME", Tools.date2Str(new Date()));
		masterPlan.put("LAST_MODIFIER", STAFF_ID);
		masterPlan.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		masterPlan.put("FDES", "");
		masterPlan.put("MASTERPLAN_ID", pd.getString("PlanningWorkOrderMaster_ID"));
		masterPlan.put("EXECUTE_STATE", "未执行");
		MASTER_PLANService.save(masterPlan);
		return map;
	}

	/**
	 * 根据 masterID 计划节点 获取当前子计划工单 数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPlanByNodeIdAndMasterPlanId")
	@ResponseBody
	public Object getPlanByNodeIdAndMasterPlanId() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String MasterWorkOrder_ID = pd.getString("MasterWorkOrder_ID");
		String NODE_ID = pd.getString("NODE_ID");
		pd = PlanningWorkOrderService.getPlanByNodeIdAndMasterPlanId(MasterWorkOrder_ID, NODE_ID);
		map.put("pd", pd);
		map.put("result", errInfo);
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
		String MasterWorkOrder_ID = pd.getString("MasterWorkOrder_ID");
		if (Tools.isEmpty(MasterWorkOrder_ID)) {
			throw new RuntimeException("父级ID没传!");
		}

		// 先创建 节点文件 然后 获取 NODE_ID 赋值给子计划工单
		PageData masterPlan = new PageData();
		String node_id = pd.getString("NODE_ID");
		String node_name = pd.getString("NODE_NAME");
		PageData pdOp = new PageData();
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
		masterPlan.put("MASTER_PLAN_FLOW_ID", this.get32UUID()); // 主键
		masterPlan.put("NODE_ID", node_id);
		masterPlan.put("NODE_NAME", node_name);
		masterPlan.put("NODE_KIND", "node");
		masterPlan.put("NODE_TYPE", "task");
		masterPlan.put("PHASE_TYPE", "");
		masterPlan.put("JUMP_CONDITION", "");
		masterPlan.put("BEGIN_NODE", "");
		masterPlan.put("END_NODE", "");
		masterPlan.put("FCREATOR", STAFF_ID);
		masterPlan.put("CREATE_TIME", Tools.date2Str(new Date()));
		masterPlan.put("LAST_MODIFIER", STAFF_ID);
		masterPlan.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		masterPlan.put("FDES", "");
		masterPlan.put("EXECUTE_STATE", "未执行");
		masterPlan.put("MASTERPLAN_ID", pd.getString("MasterWorkOrder_ID"));
		MASTER_PLANService.save(masterPlan);

		// 创建 子计划工单ID
		PageData pageData = new PageData();
		pageData.put("PlanningWorkOrderMaster_ID", MasterWorkOrder_ID);
		PageData masterPlanningWordOrder = PlanningWorkOrderMasterService.findById(pageData);
		if (null != masterPlanningWordOrder) {
			pd.put("FCount", 0.00);
			pd.put("StandardType", masterPlanningWordOrder.getString("StandardType"));
			pd.put("FCustomer", masterPlanningWordOrder.getString("FCustomer"));
			pd.put("WorkOrderType", masterPlanningWordOrder.getString("WorkOrderType"));
			pd.put("SalesOrderDetailID", masterPlanningWordOrder.getString("SalesOrderDetailID"));
			pd.put("MasterWorkOrderNum", masterPlanningWordOrder.getString("WorkOrderNum"));
			// 获取父级计划工单下有多少子计划工单，然后给子工单编号
			String PlanningWorkOrderMaster_ID = MasterWorkOrder_ID;
			// 获取父级计划工单信息 填充子工单信息
			PageData pdOps = new PageData();
			pdOps.put("MasterWorkOrder_ID", PlanningWorkOrderMaster_ID);
			List<PageData> listByMasterIdAndMasterWorkOrderNum = PlanningWorkOrderService
					.listByMasterIdAndMasterWorkOrderNum(pdOps);
			int i = 1;
			if (CollectionUtil.isNotEmpty(listByMasterIdAndMasterWorkOrderNum)) {
				i += listByMasterIdAndMasterWorkOrderNum.size();
			}

			// 子任务编号 默认 工单编号~001 三位数字
			String WorkOrderNum = masterPlanningWordOrder.getString("WorkOrderNum") + "~" + String.format("%03d", i);
			pd.put("WorkOrderNum", WorkOrderNum);

		}
		pd.put("PlanningWorkOrder_ID", this.get32UUID());
		pd.put("FStatus", "创建");
		pd.put("ScheduleSchedule", "未排程");
		pd.put("DistributionProgress", "未下发");
		pd.put("WorkOrderCreateTime", Tools.date2Str(new Date()));

		PlanningWorkOrderService.save(pd);
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}

	/**
	 * 从成品库获取当前成品的库存
	 * 
	 * @param ItemId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStockByItemId")
	@ResponseBody
	public Object getStockByItemId(String ItemId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd.put("ItemId", ItemId);
		pd.put("FClass", "产成品");
		pd.put("FType", "成品库");
		List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pd);
		if (CollectionUtil.isNotEmpty(actualCountByFTYPEAndItemID)) {
			PageData actualCountByFTYPEAndItemIDData = actualCountByFTYPEAndItemID.get(0);
			String ActualCountStr = String.valueOf(actualCountByFTYPEAndItemIDData.get("ActualCount"));
			Double ActualCount = Double.valueOf(ActualCountStr);
			map.put("data", ActualCount);
		} else {
			map.put("data", 0);
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据计划工单id获取当前物料是否齐套
	 * 
	 * @param PlanningWorkOrder_ID
	 * @throws Exception
	 */
	private PageData ifCompleteSetOfMaterialsByPlanningWorkOrderID(String PlanningWorkOrder_ID) throws Exception {
		if (Tools.isEmpty(PlanningWorkOrder_ID)) {
			throw new RuntimeException("PlanningWorkOrder_ID参数不存在");
		}
		PageData pgParam = new PageData();
		pgParam.put("PlanningWorkOrder_ID", PlanningWorkOrder_ID);
		List<PageData> materialRequirementList = MaterialRequirementService.getMaterialRequirementList(pgParam);
		if (CollectionUtil.isEmpty(materialRequirementList)) {
			PageData pageData = new PageData();
			pageData.put("ifAllComplete", "Y");
			pageData.put("PurchaseList", Lists.newArrayList());
			return pageData;
		}

		/**
		 * 查看物料需求单中物料需求数量 和 当前车间库中存在的物料数量进行对比 如果不够，则提示不够，并且提示什么物料 需要采购多少个
		 * 
		 * 返回类型 ： ifAllComplete 整个计划工单是否都齐套了 PurchaseList 需要采购的对象 {
		 * "ifAllComplete":"Y"/"N",
		 * "PurchaseList":[{"MaterialID":"xxxxxx","Purchase":"5"},
		 * {"MaterialID":"xxxxxx","Purchase":"8"}] }
		 * 
		 */
		String ifAllComplete = "Y";
		List<Map<String, Object>> resultMapList = Lists.newArrayList();
		for (int i = 0; i < materialRequirementList.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			PageData mrData = materialRequirementList.get(i);
			String MaterialID = mrData.getString("MaterialID");
			if (null == mrData.get("DemandCount")) {
				continue;
			}

			String DemandCountStr = String.valueOf(mrData.get("DemandCount"));
			Double DemandCount = Double.valueOf(DemandCountStr);
			String wareHouseType = "工厂库";
			PageData pg = new PageData();
			pg.put("ItemId", MaterialID);
			pg.put("FType", wareHouseType);
			List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);
			// 如果该物料在工厂库没有 ，则下推采购单
			if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
				map.put("MaterialID", MaterialID);
				map.put("Purchase", DemandCount);
				resultMapList.add(map);
			}
			// 如果该物料在工厂库有 ，但是小于需求数量，则下推采购单。否则返回齐套
			if (CollectionUtil.isNotEmpty(actualCountByFTYPEAndItemID)) {
				PageData actualCountByFTYPEAndItemIDData = actualCountByFTYPEAndItemID.get(0);
				String ActualCountStr = String.valueOf(actualCountByFTYPEAndItemIDData.get("ActualCount"));
				Double ActualCount = Double.valueOf(ActualCountStr);
				if (ActualCount < DemandCount) {
					map.put("MaterialID", MaterialID);
					map.put("Purchase", DemandCount - ActualCount);
					resultMapList.add(map);
				}
			}
		}
		// 如果有需要采购的物料的 物料齐套标志为 N
		if (CollectionUtil.isNotEmpty(resultMapList)) {
			ifAllComplete = "N";
		}
		PageData pageData = new PageData();
		pageData.put("ifAllComplete", ifAllComplete);
		pageData.put("PurchaseList", resultMapList);
		return pageData;
	}

	// 插入物料需求单
	private void InsertMaterialRequirements(PageData pd, String STAFF_ID, String PlanningWorkOrder_ID,
			String MAT_AUXILIARYMX_ID) throws Exception {
		if ("Y".equals(pd.getString("CreateMRIF"))) {
			List<PageData> MaterialRequirements = JSON.parseArray(pd.getString("MaterialRequirement"), PageData.class);
			if (CollectionUtil.isEmpty(MaterialRequirements)) {
				return;
			}
			// 根据计划工单id，删除物料需求单
			PlanningWorkOrderService.deleteMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrder_ID);

			// 创建物料需求单操作 从前端传过来的 物料需求单数据
			for (PageData pageData2 : MaterialRequirements) {
				if (!"投入".equals(pageData2.getString("TType"))) {
					continue;
				}
				String MaterialRequirement_ID = this.get32UUID();
				// 主键
				pageData2.put("MaterialRequirement_ID", MaterialRequirement_ID);
				pageData2.put("PlanningWorkOrderID", PlanningWorkOrder_ID);
				String WLXQ = (String) CodingRulesService.getRuleNumByRuleType("WLXQ");
				pageData2.put("MRCode", WLXQ);
				// 根据计划工单获取 行号
				String RowNum = MaterialRequirementService.getRowNumByPlanningWorkOrderID(PlanningWorkOrder_ID);
				// 行号
				pageData2.put("RowNum", RowNum);
				// 行关闭 默认不关闭 N
				pageData2.put("RowClose", "N");
				pageData2.put("PushDownPurchaseIF", "N");
				pageData2.put("PushDownTransferIF", "N");
				// 需求时间
				pageData2.put("DemandTime", Tools.date2Str(new Date()));
				// 日期批次
				pageData2.put("DateBatch", Tools.date2Str(new Date(), "yyyy-MM-dd"));
				pageData2.put("FMakeBillsPersoID", STAFF_ID);
				pageData2.put("FMakeBillsTime", Tools.date2Str(new Date()));
				// 计划工单编码
				pageData2.put("WorkOrderNum", pd.getString("WorkOrderNum"));
				// 批号
				pageData2.put("BatchNum", pd.getString("BatchNum"));
				// 保存物料需求单数据 物料辅助属性
				pageData2.put("SProp", "2540539e45324232a50bde60ac2951d3");
				pageData2.put("SPropKey", MAT_AUXILIARYMX_ID);

				String MaterialNum = pageData2.getString("MaterialNum");
				List<PageData> listByMatCode = mat_basicService.getListByMatCode(MaterialNum);
				if (CollectionUtil.isNotEmpty(listByMatCode)) {
					pageData2.put("MaterialID", listByMatCode.get(0).get("MAT_BASIC_ID"));
				}

				if (null != pageData2.get("DemandCount")) {
					MaterialRequirementService.save(pageData2);
				}
			}
			// 创建物料申请单时 保存计划工单的转移单状态
			pd.put("CreateMRStatus", "已创建");
			PlanningWorkOrderService.saveCreateMRStatus(pd);
			// 查询是否物料齐套
			PageData ifCompleteSet = ifCompleteSetOfMaterialsByPlanningWorkOrderID(PlanningWorkOrder_ID);
			// 没齐套 需要采购 在物料需求单中补充采购数量
			if ("N".equals(ifCompleteSet.getString("ifAllComplete"))) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> PurchaseList = (List<Map<String, Object>>) ifCompleteSet.get("PurchaseList");
				for (Map<String, Object> map2 : PurchaseList) {
					String MaterialID = String.valueOf(map2.get("MaterialID"));
					PageData pdOp1 = new PageData();
					pdOp1.put("PlanningWorkOrderID", PlanningWorkOrder_ID);
					pdOp1.put("MaterialID", MaterialID);
					List<PageData> materialRequirementList = MaterialRequirementService
							.getMaterialRequirementList(pdOp1);
					if (CollectionUtil.isEmpty(materialRequirementList)) {
						continue;
					}
					PageData materialRequirement = materialRequirementList.get(0);
					// 更改需求单的采购数量
					materialRequirement.put("PurchaseCount", map2.get("Purchase"));
					MaterialRequirementService.edit(materialRequirement);
				}
			}
		}
	}

	/**
	 * 删除
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PlanningWorkOrderID = pd.getString("PlanningWorkOrder_ID");
		if (Tools.isEmpty(PlanningWorkOrderID)) {
			throw new RuntimeException("主键为空");
		}

		// 根据子计划工单删除所有节点
		NEWPLAN_BOMService.deleteBySubPlanID(PlanningWorkOrderID);

		// 删除子计划工单里的流程 JSON 重建
		PageData deleteParam = new PageData();
		deleteParam.put("PID", PlanningWorkOrderID);
		deleteParam.put("FTYPE", "工单实例BOM");
		BYTEARRAYService.deleteByPidAndFTYPE(deleteParam);

		PageData pDatas = new PageData();
		pDatas.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
		PageData pdx = PlanningWorkOrderService.findById(pDatas);
		if (null != pdx) {
			// 删除附件
			PageData pData = new PageData();
			pData.put("AssociationID", PlanningWorkOrderID);
			attachmentsetService.delete(pd);
			// 删除辅助属性
			pData.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
			pData.put("MAT_AUXILIARYMX_CODE", pdx.getString("WorkOrderNum"));
			mat_auxiliarymxService.deleteByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pData);
		}
		pd.put("NODE_ID", pdx.getString("NODE_ID"));
		pd.put("MASTERPLAN_ID", pdx.getString("MasterWorkOrder_ID"));
		// 删除流程节点信息
		MASTER_PLANService.deleteByMasterPlanIdAndNodeId(pd);
		// 根据计划工单id，删除物料需求单
		PlanningWorkOrderService.deleteWorkorderProcessIOExampleByPlanningWorkOrderID(PlanningWorkOrderID);
		// 根据工艺工单工序实例id，删除工单工序投入产出实例
		PlanningWorkOrderService.deleteMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrderID);
		// 根据子计划工单id 删除所有SOP实例
		PlanningWorkOrderService.deleteProcessWorkOrderExampleSopStepByPlanningWorkOrderID(PlanningWorkOrderID);

		// 根据计划工单id，删除工艺工单工序实例
		PlanningWorkOrderService.deleteProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);

		// 删除主表信息
		PlanningWorkOrderService.delete(pd);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 去修改页面获取数据
	 * 
	 * v1 陈春光 2021-05-28 合并同物料的投入产出数据
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

		String PlanningWorkOrderID = pd.getString("PlanningWorkOrder_ID");
		if (Tools.isEmpty(PlanningWorkOrderID)) {
			throw new RuntimeException("参数为空");
		}
		pd = PlanningWorkOrderService.findById(pd); // 根据ID读取
		if (null == pd) {
			throw new RuntimeException("根据ID读取子计划工单为空");
		}
		// 计划员 和生产主管 拆分( ,yl, )变为数组传给前端
		String FPlannerID = Tools.notEmpty(pd.getString("FPlannerID")) ? pd.getString("FPlannerID") : "";
		String[] FPlannerIDsplit = FPlannerID.split(",yl,");
		if ("".equals(FPlannerIDsplit[0])) {
			pd.put("FPlannerID", Lists.newArrayList());
		} else {
			pd.put("FPlannerID", Lists.newArrayList(FPlannerIDsplit));
		}

		String FPlanner = Tools.notEmpty(pd.getString("FPlanner")) ? pd.getString("FPlanner") : "";
		String[] FPlannersplit = FPlanner.split(",yl,");
		if ("".equals(FPlannersplit[0])) {
			pd.put("FPlanner", Lists.newArrayList());
		} else {
			pd.put("FPlanner", Lists.newArrayList(FPlannersplit));
		}

		String ProductionSupervisorID = Tools.notEmpty(pd.getString("ProductionSupervisorID"))
				? pd.getString("ProductionSupervisorID")
				: "";
		String[] ProductionSupervisorIDsplit = ProductionSupervisorID.split(",yl,");
		if ("".equals(ProductionSupervisorIDsplit[0])) {
			pd.put("ProductionSupervisorID", Lists.newArrayList());
		} else {
			pd.put("ProductionSupervisorID", Lists.newArrayList(ProductionSupervisorIDsplit));
		}

		String ProductionSupervisorName = Tools.notEmpty(pd.getString("ProductionSupervisorName"))
				? pd.getString("ProductionSupervisorName")
				: "";
		String[] ProductionSupervisorNamesplit = ProductionSupervisorName.split(",yl,");
		if ("".equals(ProductionSupervisorNamesplit[0])) {
			pd.put("ProductionSupervisorName", Lists.newArrayList());
		} else {
			pd.put("ProductionSupervisorName", Lists.newArrayList(ProductionSupervisorNamesplit));
		}

		String SalesOrderDetailID = pd.getString("SalesOrderDetailID");
		if (Tools.notEmpty(SalesOrderDetailID)) {
			PageData param = new PageData();
			param.put("SalesOrderDetail_ID", SalesOrderDetailID);
			PageData orderDetail = salesorderdetailService.findById(param);
			if (null != orderDetail) {
				String SalesOrder_ID = orderDetail.getString("SalesOrderID");
				param.put("SalesOrder_ID", SalesOrder_ID);
				PageData order = salesorderService.findById(param);
				if (null != order) {
					pd.put("OrderNum", order.getString("OrderNum"));
				}
			}
		}

		String showChangeBOMButton = "N";

		// 根据主键获取上传的文件
		PageData pageData = new PageData();
		pageData.put("AssociationID", PlanningWorkOrderID);

		List<PageData> ioList = Lists.newArrayList();
		String Cabinet_No = pd.getString("OutputMaterial").split("/")[0];
		String Cabinet_Name = pd.getString("OutputMaterial").split("/")[1];
		if (!StringUtils.isEmpty(Cabinet_No)) {
			PageData pdCabNo = new PageData();
			pdCabNo.put("Cabinet_No", Cabinet_No);
			// 根据柜体编号获取柜体详情信息
			List<PageData> listAll = Cabinet_Assembly_DetailService.listAll(pdCabNo);
			if (CollectionUtil.isNotEmpty(listAll)) {
				String detailId = listAll.get(0).getString("Cabinet_Assembly_Detail_ID");

				// 如果存在变更的BOM。则返回前台变更按钮可展示
				PageData bomChangeParam = new PageData();
				bomChangeParam.put("Cabinet_Assembly_Detail_ID", detailId);
				bomChangeParam.put("FSTATE", "变更");

				// 如果存在变更则将展示按钮设置为Y
				List<PageData> listAllChangeData = Cabinet_BOMService.listAll(bomChangeParam);
				if (CollectionUtil.isNotEmpty(listAllChangeData)) {
					if ("变更".equals(listAllChangeData.get(0).getString("FSTATE"))) {
						showChangeBOMButton = "Y";
					}
				}
				// 转化为投入产出记录返回前台
				PageData bomParam = new PageData();
				bomParam.put("Cabinet_Assembly_Detail_ID", detailId);
				List<PageData> bomList = Cabinet_BOMService.listAllGroupByMat(bomParam);
				if (CollectionUtil.isNotEmpty(bomList)) {
					for (PageData bomData : bomList) {
						PageData ioData = new PageData();
						if (bomData != null) {
							ioData.put("MAT_NAME", bomData.getString("MAT_NAME"));
							ioData.put("MaterialName", bomData.getString("MAT_NAME"));
							ioData.put("MAT_CODE", bomData.getString("MAT_CODE"));
							ioData.put("MaterialNum", bomData.getString("MAT_CODE"));
							ioData.put("FormulaQuantity", bomData.get("BOM_COUNT"));
							ioData.put("MAT_SPECS", bomData.getString("MAT_SPECS"));
							ioData.put("PlannedQuantity", bomData.get("BOM_COUNT"));
							ioData.put("TType", "投入");
							ioList.add(ioData);
						}
					}
				}
			}
		}
		String MAT_SPECSs = "";
		List<PageData> listByMatCode = mat_basicService.getListByMatCode(Cabinet_No);
		if (CollectionUtil.isNotEmpty(listByMatCode)) {
			MAT_SPECSs = listByMatCode.get(0).getString("MAT_SPECS");
		}
		PageData ioData = new PageData();
		ioData.put("TType", "产出");
		ioData.put("MAT_NAME", Cabinet_Name);
		ioData.put("MAT_CODE", Cabinet_No);

		ioData.put("MaterialName", Cabinet_Name);
		ioData.put("MaterialNum", Cabinet_No);

		ioData.put("FormulaQuantity", pd.get("FCount"));
		ioData.put("MAT_SPECS", MAT_SPECSs);
		ioData.put("PlannedQuantity", pd.get("FCount"));
		ioList.add(ioData);

		map.put("pd", pd);

		// 查询生产任务
		List<PageData> listProcessWorkOrderExampleByPlanningWorkOrderID = PlanningWorkOrderService
				.getListProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);
		List<PageData> processWorkOrderList = Lists.newArrayList();
		List<PageData> WPStatusShowList = Lists.newArrayList();
		if (CollectionUtil.isEmpty(listProcessWorkOrderExampleByPlanningWorkOrderID)) {
			map.put("processWorkOrderList", Lists.newArrayList());
		} else {

			for (PageData l : listProcessWorkOrderExampleByPlanningWorkOrderID) {
				PageData WPStatusShow = new PageData();
				PageData processWorkOrder = new PageData();
				processWorkOrder.put("WorkOrderNum", pd.getString("WorkOrderNum"));
				processWorkOrder.put("ProcessWorkOrderExample_ID",
						Tools.isEmpty(l.getString("ProcessWorkOrderExample_ID"))
								? l.getString("ProcessWorkOrderExample_ID")
								: "");
				processWorkOrder.put("TaskNum", Tools.notEmpty(l.getString("TaskNum")) ? l.getString("TaskNum") : "");
				processWorkOrder.put("FStatus", Tools.notEmpty(l.getString("FStatus")) ? l.getString("FStatus") : "");
				processWorkOrder.put("ConsumptionQuantity",
						Tools.notEmpty(l.get("ConsumptionQuantity").toString())
								? l.get("ConsumptionQuantity").toString()
								: 0);
				processWorkOrder.put("PlannedBeginTime",
						Tools.notEmpty(l.getString("PlannedBeginTime")) ? l.getString("PlannedBeginTime") : "");
				processWorkOrder.put("PlannedEndTime",
						Tools.notEmpty(l.getString("PlannedEndTime")) ? l.getString("PlannedEndTime") : "");
				processWorkOrder.put("ActualBeginTime",
						Tools.notEmpty(l.getString("ActualBeginTime")) ? l.getString("ActualBeginTime") : "");
				processWorkOrder.put("ActualEndTime",
						Tools.notEmpty(l.getString("ActualEndTime")) ? l.getString("ActualEndTime") : "");
				PageData pData = new PageData();
				pData.put("ProcessDefinition_ID", l.getString("WP"));
				String WPName = l.getString("WPName");
				if (null != WPName) {
					WPStatusShow.put("WPName", WPName);
					processWorkOrder.put("WPName", WPName);
					WPStatusShowList.add(WPStatusShow);
				}

				String FStation = Tools.notEmpty(l.getString("FStation")) ? l.getString("FStation") : "";
				if (!"".equals(FStation)) {

					processWorkOrderList.add(processWorkOrder);
				}
			}
			;
			map.put("processWorkOrderList", processWorkOrderList);
		}
		// 拼接当前计划工单的状态数据
		Map<String, Object> workOrderStatusShow = Maps.newHashMap();
		workOrderStatusShow.put("OutputMaterial", pd.getString("OutputMaterial"));
		workOrderStatusShow.put("FSpecifications", pd.getString("FSpecifications"));
		workOrderStatusShow.put("FCount", String.valueOf(pd.get("FCount")));

		String IFAPS = "";
		String IFIssued = "";
		String IFProduce = "";
		if ("已排程".equals(pd.getString("ScheduleSchedule"))) {
			IFAPS = "Y";
		} else {
			IFAPS = "N";
		}
		if ("已下发".equals(pd.getString("DistributionProgress"))) {
			IFIssued = "Y";
		} else {
			IFIssued = "N";
		}
		if ("执行中".equals(pd.getString("FStatus")) || "结束".equals(pd.getString("FStatus"))) {
			IFProduce = "Y";
		} else {
			IFProduce = "N";
		}
		workOrderStatusShow.put("IFAPS", IFAPS);
		workOrderStatusShow.put("IFIssued", IFIssued);
		workOrderStatusShow.put("IFProduce", IFProduce);
		for (PageData p : WPStatusShowList) {
			p.put("IFAPS", IFAPS);
			p.put("IFIssued", IFIssued);
			p.put("IFProduce", IFProduce);
		}
		;

		// 根据子计划工单查询 工艺路线json

		// 根据主计划ID获取流程图JSON
		PageData pdjson = new PageData();
		pdjson.put("PID", PlanningWorkOrderID);
		pdjson.put("FTYPE", "工单实例BOM");
		PageData findByPID = BYTEARRAYService.findByPID(pdjson);
		if (null != findByPID) {
			map.put("byteArray", findByPID);
		}

		// 返回结果集
		map.put("workOrderStatusShow", workOrderStatusShow);
		map.put("showChangeBOMButton", showChangeBOMButton);
		map.put("WPStatusShowList", WPStatusShowList);
		map.put("ioList", ioList);

		map.put("result", errInfo);
		return map;
	}

	/**
	 * 手动编辑物料需求单
	 * 
	 * 参数：{ "PlanningWorkOrder_ID":"PlanningWorkOrder_ID",
	 * "MaterialRequirement":JSON.stringify([ { 物料需求的属性 }]) }
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editMR")
	@ResponseBody
	public Object editMR() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();

		String PlanningWorkOrderID = pd.get("PlanningWorkOrder_ID").toString();
		// 根据计划工单id，删除物料需求单
		PlanningWorkOrderService.deleteMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrderID);
		List<PageData> MaterialRequirements = JSON.parseArray(this.getPageData().getString("MaterialRequirement"),
				PageData.class);
		// 创建物料需求单操作 从前端传过来的 物料需求单数据
		for (PageData pageData2 : MaterialRequirements) {
			String MaterialRequirement_ID = this.get32UUID();
			// 主键
			pageData2.put("MaterialRequirement_ID", MaterialRequirement_ID);
			String WLXQ = (String) CodingRulesService.getRuleNumByRuleType("WLXQ");
			pageData2.put("MRCode", WLXQ);
			pageData2.put("PlanningWorkOrderID", PlanningWorkOrderID);
			// 根据计划工单获取 行号
			String RowNum = MaterialRequirementService.getRowNumByPlanningWorkOrderID(PlanningWorkOrderID);
			// 行号
			pageData2.put("RowNum", RowNum);
			// 行关闭 默认不关闭 N
			pageData2.put("RowClose", "N");
			pageData2.put("PurchaseCount", 0.0);
			// 需求时间
			pageData2.put("DemandTime", Tools.date2Str(new Date()));
			// 日期批次
			pageData2.put("DateBatch", Tools.date2Str(new Date(), "yyyy-MM-dd"));
			PageData pdOp = new PageData();
			String name = Jurisdiction.getName();
			pdOp.put("FNAME", name);
			String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
			pageData2.put("FMakeBillsPersoID", STAFF_ID);
			pageData2.put("FMakeBillsTime", Tools.date2Str(new Date()));
			MaterialRequirementService.save(pageData2);
		}
		// 创建物料申请单时 保存计划工单的转移单状态
		pd.put("CreateMRStatus", "已创建");
		pd.put("CreateMRIF", "Y");
		PlanningWorkOrderService.saveCreateMRStatus(pd);
		// 查询是否物料齐套
		PageData ifCompleteSet = ifCompleteSetOfMaterialsByPlanningWorkOrderID(PlanningWorkOrderID);
		// 没齐套 需要采购 在物料需求单中补充采购数量
		if ("N".equals(ifCompleteSet.getString("ifAllComplete"))) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> PurchaseList = (List<Map<String, Object>>) ifCompleteSet.get("PurchaseList");
			for (Map<String, Object> map2 : PurchaseList) {
				String MaterialID = String.valueOf(map2.get("MaterialID"));
				PageData pdOp1 = new PageData();
				pdOp1.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
				pdOp1.put("MaterialID", MaterialID);
				List<PageData> materialRequirementList = MaterialRequirementService.getMaterialRequirementList(pdOp1);
				if (CollectionUtil.isEmpty(materialRequirementList)) {
					continue;
				}
				PageData materialRequirement = materialRequirementList.get(0);
				// 更改需求单的采购数量
				materialRequirement.put("PurchaseCount", map2.get("Purchase"));
				MaterialRequirementService.edit(materialRequirement);
			}
		}
		map.put("result", errInfo);
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
		// 先跟主键查一下数据
		PageData ex = PlanningWorkOrderService.findById(pd);

		// 如果工单 已排程 待下发 已下发状态不让修改
		String ScheduleSchedule = ex.getString("ScheduleSchedule");
		if ("已排程".equals(ScheduleSchedule)) {
			throw new RuntimeException("该工单已排程，禁止修改");
		} else {
			pd.put("ScheduleSchedule", "未排程");
		}
		String DistributionProgress = ex.getString("DistributionProgress");
		if ("待下发".equals(DistributionProgress)) {
			throw new RuntimeException("该工单待下发，禁止修改");
		}
		if ("已下发".equals(DistributionProgress)) {
			throw new RuntimeException("该工单已下发，禁止修改");
		} else {
			pd.put("DistributionProgress", "未下发");
		}
		pd.put("WorkOrderCreateTime", ex.getString("WorkOrderCreateTime"));
		// // 删除所有附属信息
		// String PlanningWorkOrderID = pd.getString("PlanningWorkOrder_ID");
		// // 根据计划工单id，删除工艺工单工序实例
		// PlanningWorkOrderService.deleteProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);
		// // 根据子计划工单id 删除所有SOP实例
		// PlanningWorkOrderService.deleteProcessWorkOrderExampleSopStepByPlanningWorkOrderID(PlanningWorkOrderID);
		//
		// // 根据工艺工单工序实例id，物料需求单
		// PlanningWorkOrderService.deleteMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrderID);
		// // 根据计划工单id，删除工单工序投入产出实例
		// PlanningWorkOrderService.deleteWorkorderProcessIOExampleByPlanningWorkOrderID(PlanningWorkOrderID);

		String PlanningWorkOrderMaster_ID = pd.getString("PlanningWorkOrderMaster_ID");
		// 获取父级计划工单信息 填充子工单信息
		PageData pdOp = new PageData();
		pdOp.put("PlanningWorkOrderMaster_ID", PlanningWorkOrderMaster_ID);
		PageData planningWorkOrderMaster = PlanningWorkOrderMasterService.findById(pdOp);
		if (null != planningWorkOrderMaster) {
			pd.put("FCustomer", planningWorkOrderMaster.getString("FCustomer"));
			pd.put("WorkOrderType", planningWorkOrderMaster.getString("WorkOrderType"));
			pd.put("SalesOrderDetailID", planningWorkOrderMaster.getString("SalesOrderDetailID"));
			pd.put("PlannerType", planningWorkOrderMaster.getString("PlannerType"));
			pd.put("StandardType", planningWorkOrderMaster.getString("StandardType"));
			pd.put("MasterWorkOrder_ID", PlanningWorkOrderMaster_ID);
			pd.put("MasterWorkOrderNum", planningWorkOrderMaster.getString("WorkOrderNum"));
			pd.put("OrderNum", planningWorkOrderMaster.getString("OrderNum"));

			// 保存计划工单信息
			// 存一下BOMID
			pd.put("ProcessTypeKey", pd.getString("ProductionBOM_ID"));
			PlanningWorkOrderService.edit(pd);
		}
		// 从系统中获取staffID
		// String name = Jurisdiction.getName();
		// pdOp.put("FNAME", name);
		// String STAFF_ID =
		// StaffService.getStaffId(pdOp).getString("STAFF_ID");
		// // 文件归档 文件先上传 然后返回 URL 和名字 跟参数一块带过来
		// String FileName = pd.getString("FileName");
		// String FileUrl = pd.getString("FileUrl");
		// if (!Tools.isEmpty(FileUrl) && !Tools.isEmpty(FileName)) {
		// Map<String, Object> resultMap = this.attachment(FileUrl, FileName,
		// pd.getString("PlanningWorkOrder_ID"),
		// "附件");
		// if ("error".equals(resultMap.get("result"))) {
		// throw new RuntimeException(String.valueOf(resultMap.get("msg")));
		// }
		// }

		// BOM id 也是从参数传过来的
		// String ProductionBOM_ID = pd.getString("ProductionBOM_ID");
		// if (Tools.isEmpty(ProductionBOM_ID)) {
		// throw new RuntimeException("未选取bom");
		// }
		// PageData findByIdParam = new PageData();
		// // 根据BOMId 查询 数量
		// findByIdParam.put("ProductionBOM_ID", ProductionBOM_ID);
		// PageData bomData = productionBOMService.findById(findByIdParam);
		// String bomCount = String.valueOf(bomData.get("FCount"));
		// Double bomCountDouble = new Double(bomCount);

		// // 计划工单ID 是从刚才保存的实体中获取的
		// String PlanningWorkOrder_ID = pd.getString("PlanningWorkOrder_ID");
		//
		// // 根据BOMID获取BOM编码后，获取工艺工序实例
		// List<PageData> workingProcedureExampleListByBOMId =
		// WorkingProcedureExampleService
		// .getWorkingProcedureExampleListByBOMId(ProductionBOM_ID);
		//
		// // 插入工艺工单工序实例和工单工序投入产出实例
		// InsertDataToDB(pd, STAFF_ID, bomCountDouble,
		// pd.getString("WorkOrderNum"), PlanningWorkOrder_ID,
		// workingProcedureExampleListByBOMId);

		// PageData mat_auxiliarymxParam = new PageData();
		// mat_auxiliarymxParam.put("MAT_AUXILIARY_ID",
		// "2540539e45324232a50bde60ac2951d3");
		// mat_auxiliarymxParam.put("MAT_AUXILIARYMX_CODE",
		// pd.getString("MasterWorkOrderNum"));
		// List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE =
		// mat_auxiliarymxService
		// .getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(mat_auxiliarymxParam);
		// String aux_mx_id = "";
		// if
		// (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE))
		// {
		// aux_mx_id =
		// byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0).getString("MAT_AUXILIARYMX_ID");
		// }

		// // 插入物料需求单
		// InsertMaterialRequirements(pd, STAFF_ID, PlanningWorkOrder_ID,
		// aux_mx_id);
		// String WorkOrderType = pd.getString("WorkOrderType");
		// if (WorkOrderType != null && WorkOrderType.equals("面向销售订单")) {
		// salesorderService.calFProductionQuantity(pd);// 反写销售订单投产数量
		// salesorderService.calFStatus(pd);// 反写销售订单状态
		// }

		// // 根据子计划工单删除所有节点
		// NEWPLAN_BOMService.deleteBySubPlanID(PlanningWorkOrderID);
		//
		// // 删除子计划工单里的流程 JSON 重建
		// PageData deleteParam = new PageData();
		// deleteParam.put("PID", PlanningWorkOrderID);
		// deleteParam.put("FTYPE", "工单实例BOM");
		// BYTEARRAYService.deleteByPidAndFTYPE(deleteParam);
		//
		// // 获取 BOM 下的工艺路线 JSON 给子工单复制一份
		// PageData pageData = new PageData();
		// pageData.put("PID", ProductionBOM_ID);
		// pageData.put("FTYPE", "生产BOM");
		// PageData bytearray = BYTEARRAYService.findByPID(pageData);
		//
		// bytearray.put("PID", PlanningWorkOrderID);
		// bytearray.put("FTYPE", "工单实例BOM");
		// bytearray.put("GE_BYTEARRAY_FLOW_ID", this.get32UUID());
		// BYTEARRAYService.save(bytearray);
		//
		// // 获取 BOM 下的关联节点信息 给子工单复制一份
		// PageData bomParam = new PageData();
		// bomParam.put("ProductionBOM_ID", ProductionBOM_ID);
		// List<PageData> NEW_BOMlistAll = NEW_BOMService.listAll(bomParam);
		// for (PageData newbom : NEW_BOMlistAll) {
		// newbom.put("SUBPLAN_ID", PlanningWorkOrderID);
		// newbom.put("MASTERPLAN_ID", PlanningWorkOrderMaster_ID);
		// newbom.put("NEWPLAN_BOM_FLOW", this.get32UUID());
		// newbom.put("EXECUTE_STATE", "未执行");
		// PageData pData = new PageData();
		// pData.put("NODE_ID", pd.getString("NODE_ID"));
		// List<PageData> listAll = MASTER_PLANService.listAll(pData);
		// if (CollectionUtil.isNotEmpty(listAll)) {
		// newbom.put("MASTER_PLAN_FLOW_ID",
		// listAll.get(0).getString("MASTER_PLAN_FLOW_ID"));
		// newbom.put("RUN_O_ID",
		// listAll.get(0).getString("MASTER_PLAN_FLOW_ID"));
		// }
		// NEWPLAN_BOMService.save(newbom);
		// }

		map.put("result", errInfo);
		return map;

	}

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/editNoStandard")
	@ResponseBody
	public Object editNoStandard() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 先跟主键查一下数据
		PageData ex = PlanningWorkOrderService.findById(pd);

		// 如果工单 已排程 待下发 已下发状态不让修改
		String ScheduleSchedule = ex.getString("ScheduleSchedule");
		if ("已排程".equals(ScheduleSchedule)) {
			throw new RuntimeException("该工单已排程，禁止修改");
		} else {
			pd.put("ScheduleSchedule", "未排程");
		}
		String DistributionProgress = ex.getString("DistributionProgress");
		if ("待下发".equals(DistributionProgress)) {
			throw new RuntimeException("该工单待下发，禁止修改");
		}
		if ("已下发".equals(DistributionProgress)) {
			throw new RuntimeException("该工单已下发，禁止修改");
		} else {
			pd.put("DistributionProgress", "未下发");
		}
		pd.put("WorkOrderCreateTime", ex.getString("WorkOrderCreateTime"));
		// 删除所有附属信息
		String PlanningWorkOrderID = pd.getString("PlanningWorkOrder_ID");

		// 根据工艺工单工序实例id，物料需求单
		PlanningWorkOrderService.deleteMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrderID);

		String PlanningWorkOrderMaster_ID = pd.getString("PlanningWorkOrderMaster_ID");
		// 获取父级计划工单信息 填充子工单信息
		PageData pdOp = new PageData();
		pdOp.put("PlanningWorkOrderMaster_ID", PlanningWorkOrderMaster_ID);
		PageData planningWorkOrderMaster = PlanningWorkOrderMasterService.findById(pdOp);
		if (null != planningWorkOrderMaster) {
			pd.put("FCustomer", planningWorkOrderMaster.getString("FCustomer"));
			pd.put("WorkOrderType", planningWorkOrderMaster.getString("WorkOrderType"));
			pd.put("SalesOrderDetailID", planningWorkOrderMaster.getString("SalesOrderDetailID"));
			pd.put("PlannerType", planningWorkOrderMaster.getString("PlannerType"));
			pd.put("StandardType", planningWorkOrderMaster.getString("StandardType"));
			pd.put("MasterWorkOrder_ID", PlanningWorkOrderMaster_ID);
			pd.put("MasterWorkOrderNum", planningWorkOrderMaster.getString("WorkOrderNum"));
			pd.put("OrderNum", planningWorkOrderMaster.getString("OrderNum"));

			// 保存计划工单信息
			PlanningWorkOrderService.edit(pd);
		}
		// 从系统中获取staffID
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
		// 文件归档 文件先上传 然后返回 URL 和名字 跟参数一块带过来
		String FileName = pd.getString("FileName");
		String FileUrl = pd.getString("FileUrl");
		if (!Tools.isEmpty(FileUrl) && !Tools.isEmpty(FileName)) {
			Map<String, Object> resultMap = this.attachment(FileUrl, FileName, pd.getString("PlanningWorkOrder_ID"),
					"附件");
			if ("error".equals(resultMap.get("result"))) {
				throw new RuntimeException(String.valueOf(resultMap.get("msg")));
			}
		}

		// 计划工单ID 是从刚才保存的实体中获取的
		String PlanningWorkOrder_ID = pd.getString("PlanningWorkOrder_ID");

		PageData mat_auxiliarymxParam = new PageData();
		mat_auxiliarymxParam.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
		mat_auxiliarymxParam.put("MAT_AUXILIARYMX_CODE", pd.getString("MasterWorkOrderNum"));
		List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
				.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(mat_auxiliarymxParam);
		String aux_mx_id = "";
		if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
			aux_mx_id = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0).getString("MAT_AUXILIARYMX_ID");
		}

		// 插入物料需求单
		InsertMaterialRequirements(pd, STAFF_ID, PlanningWorkOrder_ID, aux_mx_id);
		String WorkOrderType = pd.getString("WorkOrderType");
		if (WorkOrderType != null && WorkOrderType.equals("面向销售订单")) {
			salesorderService.calFProductionQuantity(pd);// 反写销售订单投产数量
			salesorderService.calFStatus(pd);// 反写销售订单状态
		}

		map.put("result", errInfo);
		return map;

	}

	/**
	 * 根据条件获取生产任务
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ProcessWorkOrderExampleList")
	@ResponseBody
	public Object ProcessWorkOrderExampleList(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 模糊查询条件
		// 订单号
		String OrderNum = pd.getString("OrderNum");
		if (Tools.notEmpty(OrderNum)) {
			pd.put("OrderNum", OrderNum.trim());
		}
		// 批号
		String BatchNum = pd.getString("BatchNum");
		if (Tools.notEmpty(BatchNum)) {
			pd.put("BatchNum", BatchNum.trim());
		}
		// 任务编号
		String TaskNum = pd.getString("TaskNum");
		if (Tools.notEmpty(TaskNum)) {
			pd.put("TaskNum", TaskNum.trim());
		}
		// 工序
		String WP = pd.getString("WP");
		if (Tools.notEmpty(WP)) {
			pd.put("WP", WP.trim());
		}
		// 产出物料
		String ProcessIMateriel = pd.getString("ProcessIMateriel");
		if (Tools.notEmpty(ProcessIMateriel)) {
			pd.put("ProcessIMateriel", ProcessIMateriel.trim());
		}
		// 产出物料编码
		String ProcessIMaterielCode = pd.getString("ProcessIMaterielCode");
		if (Tools.notEmpty(ProcessIMaterielCode)) {
			pd.put("ProcessIMaterielCode", ProcessIMaterielCode.trim());
		}
		// 工位
		String FStation = pd.getString("FStation");
		if (Tools.notEmpty(FStation)) {
			pd.put("FStation", FStation.trim());
		}
		// 执行人
		String ExecutorID = pd.getString("ExecutorID");
		if (Tools.notEmpty(ExecutorID)) {
			pd.put("ExecutorID", ExecutorID.trim());
		}
		// 计划开始时间段-开始时间
		String PlannedBeginTimeBegin = pd.getString("PlannedBeginTimeBegin");
		if (Tools.notEmpty(PlannedBeginTimeBegin)) {
			pd.put("PlannedBeginTimeBegin", PlannedBeginTimeBegin.trim());
		}
		// 计划开始时间段-结束时间
		String PlannedBeginTimeEnd = pd.getString("PlannedBeginTimeEnd");
		if (Tools.notEmpty(PlannedBeginTimeEnd)) {
			pd.put("PlannedBeginTimeEnd", PlannedBeginTimeEnd.trim());
		}
		// 计划结束时间段-开始时间
		String PlannedEndTimeBegin = pd.getString("PlannedEndTimeBegin");
		if (Tools.notEmpty(PlannedEndTimeBegin)) {
			pd.put("PlannedEndTimeBegin", PlannedEndTimeBegin.trim());
		}
		// 计划结束时间段-结束时间
		String PlannedEndTimeEnd = pd.getString("PlannedEndTimeEnd");
		if (Tools.notEmpty(PlannedEndTimeEnd)) {
			pd.put("PlannedEndTimeEnd", PlannedEndTimeEnd.trim());
		}
		// 任务状态
		String FStatus = pd.getString("FStatus");
		if (Tools.notEmpty(FStatus)) {
			pd.put("FStatus", FStatus.trim());
		}
		// 实际开始时间段-开始时间
		String ActualBeginTimeBegin = pd.getString("ActualBeginTimeBegin");
		if (Tools.notEmpty(ActualBeginTimeBegin)) {
			pd.put("ActualBeginTimeBegin", ActualBeginTimeBegin.trim());
		}
		// 实际开始时间段-结束时间
		String ActualBeginTimeEnd = pd.getString("ActualBeginTimeEnd");
		if (Tools.notEmpty(ActualBeginTimeEnd)) {
			pd.put("ActualBeginTimeEnd", ActualBeginTimeEnd.trim());
		}
		// 实际结束时间段-开始时间
		String ActualEndTimeBegin = pd.getString("ActualEndTimeBegin");
		if (Tools.notEmpty(ActualEndTimeBegin)) {
			pd.put("ActualEndTimeBegin", ActualEndTimeBegin.trim());
		}
		// 实际结束时间段-结束时间
		String ActualEndTimeEnd = pd.getString("ActualEndTimeEnd");
		if (Tools.notEmpty(ActualEndTimeEnd)) {
			pd.put("ActualEndTimeEnd", ActualEndTimeEnd.trim());
		}

		// 计划工单优先级
		String FPriorityID = pd.getString("FPriorityID");
		if (Tools.notEmpty(FPriorityID)) {
			pd.put("FPriorityID", FPriorityID.trim());
		}

		// 任务类型 (1 生产任务、2 返工任务)
		String TaskType = pd.getString("TaskType");
		if (Tools.notEmpty(TaskType)) {
			pd.put("TaskType", TaskType.trim());
		}

		// 计划工单下发进度
		String DistributionProgress = pd.getString("DistributionProgress");
		if (Tools.notEmpty(DistributionProgress)) {
			pd.put("DistributionProgress", DistributionProgress.trim());
		}
		page.setPd(pd);
		List<PageData> varList = ProcessWorkOrderExampleService.list(page);
		for (PageData pageData : varList) {
			if (null == pageData) {
				continue;
			}
			if ("1".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "生产任务");
			} else if ("2".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "返工任务");
			}

			String WPID = pageData.getString("WP");
			PageData pData = new PageData();
			pData.put("ProcessDefinition_ID", WPID);
			String WPName = pageData.getString("WPName");
			if (null != WPName) {
				pageData.put("WP", WPName);
			}
			String station = "";
			String FStationIDS = pageData.getString("FStation");
			if (Tools.isEmpty(FStationIDS)) {
				continue;
			}
			String[] FStationIDSsplit = FStationIDS.split(",yl,");
			if (!"".equals(FStationIDSsplit[0])) {
				for (String stationId : FStationIDSsplit) {
					PageData stationData = new PageData();
					stationData.put("WC_STATION_ID", stationId);
					PageData WC_STATION = WC_StationService.findById(stationData);
					if (null != WC_STATION) {
						station += "," + WC_STATION.getString("FNAME");
					}
				}
			}
			station = station.substring(1, station.length());
			pageData.put("FStation", station);
			// 执行人 多个
			String ExecutorIDParam = Tools.notEmpty(pageData.getString("ExecutorID")) ? pageData.getString("ExecutorID")
					: "";
			if (!"".equals(ExecutorIDParam)) {
				List<String> ExecutorIDList = Lists.newArrayList(ExecutorIDParam.split(",yl,"));
				if (CollectionUtil.isNotEmpty(ExecutorIDList)) {
					String ExecutorID1 = "";
					for (String e : ExecutorIDList) {
						PageData pageData1 = new PageData();
						pageData1.put("STAFF_ID", e);
						try {
							PageData staffInfo = StaffService.findById(pageData1);
							if (null != staffInfo) {
								ExecutorID1 += ',' + (staffInfo.getString("NAME"));
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if (Tools.notEmpty(ExecutorID1)) {
						pageData.put("ExecutorID", ExecutorID1.subSequence(1, ExecutorID1.length()));
					}

				}
			}
			String FCreatePersonID = pageData.getString("FCreatePersonID");
			if (Tools.notEmpty(FCreatePersonID)) {
				PageData pageData1 = new PageData();
				pageData1.put("STAFF_ID", FCreatePersonID);
				try {
					PageData staffInfo = StaffService.findById(pageData1);
					if (null != staffInfo) {
						FCreatePersonID = (staffInfo.getString("NAME"));
						pageData.put("FCreatePersonID", FCreatePersonID);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 模糊查询条件
		// 订单号
		String OrderNum = pd.getString("OrderNum");
		if (Tools.notEmpty(OrderNum)) {
			pd.put("OrderNum", OrderNum.trim());
		}
		// 工单类型
		String WorkOrderType = pd.getString("WorkOrderType");
		if (Tools.notEmpty(WorkOrderType)) {
			pd.put("WorkOrderType", WorkOrderType.trim());
		}
		// 计划工单号
		String WorkOrderNum = pd.getString("WorkOrderNum");
		if (Tools.notEmpty(WorkOrderNum)) {
			pd.put("WorkOrderNum", WorkOrderNum.trim());
		}
		// 主计划工单id
		String MasterWorkOrder_ID = pd.getString("MasterWorkOrder_ID");
		if (Tools.notEmpty(MasterWorkOrder_ID)) {
			pd.put("MasterWorkOrder_ID", MasterWorkOrder_ID.trim());
		}

		List<PageData> varList = PlanningWorkOrderService.listAll(pd); // 列出PlanningWorkOrder列表
		for (PageData pds : varList) {

			// 计划员 和生产主管 拆分( ,yl, )变为数组传给前端
			String FPlannerID = Tools.notEmpty(pds.getString("FPlannerID")) ? pds.getString("FPlannerID") : "";
			pds.put("FPlannerID", Lists.newArrayList(FPlannerID.split(",yl,")));

			String FPlanner = Tools.notEmpty(pds.getString("FPlanner")) ? pds.getString("FPlanner") : "";
			pds.put("FPlanner", Lists.newArrayList(FPlanner.split(",yl,")));

			String ProductionSupervisorID = Tools.notEmpty(pds.getString("ProductionSupervisorID"))
					? pds.getString("ProductionSupervisorID")
					: "";
			pds.put("ProductionSupervisorID", Lists.newArrayList(ProductionSupervisorID.split(",yl,")));

			String ProductionSupervisorName = Tools.notEmpty(pds.getString("ProductionSupervisorName"))
					? pds.getString("ProductionSupervisorName")
					: "";
			pds.put("ProductionSupervisorName", Lists.newArrayList(ProductionSupervisorName.split(",yl,")));
			String FCustomer = Tools.notEmpty(pds.getString("FCustomer")) ? pds.getString("FCustomer") : "";
			if (!"".equals(FCustomer)) {
				PageData pdcustomer = new PageData();
				pdcustomer.put("Customer_ID", FCustomer);
				PageData Customer = CustomerService.findById(pdcustomer);
				if (null != Customer) {
					FCustomer = Customer.getString("FName");
					pds.put("FCustomer", FCustomer);
				}
			}

			pd.put("PlanningWorkOrder_ID", pds.get("PlanningWorkOrder_ID"));
			List<PageData> taskListAll = ProcessWorkOrderExampleService.listAll(pd);
			int taskSize = 0;
			for (int i = 0; i < taskListAll.size(); i++) {
				if (taskListAll.get(i).get("FStatus").toString() == "结束"
						|| taskListAll.get(i).get("FStatus").toString().equals("结束")) {
					taskSize++;
				}
			}
			if (taskSize == taskListAll.size() && taskSize != 0) {
				PlanningWorkOrderService.editStatus(pd);
			}
		}
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 工单列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listGD")
	@ResponseBody
	public Object listGD(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 模糊查询条件
		// 订单号
		String OrderNum = pd.getString("OrderNum");
		if (Tools.notEmpty(OrderNum)) {
			pd.put("OrderNum", OrderNum.trim());
		}
		// 工单类型
		String WorkOrderType = pd.getString("WorkOrderType");
		if (Tools.notEmpty(WorkOrderType)) {
			pd.put("WorkOrderType", WorkOrderType.trim());
		}
		// 计划工单号
		String WorkOrderNum = pd.getString("WorkOrderNum");
		if (Tools.notEmpty(WorkOrderNum)) {
			pd.put("WorkOrderNum", WorkOrderNum.trim());
		}
		// 主计划工单id
		String MasterWorkOrder_ID = pd.getString("MasterWorkOrder_ID");
		if (Tools.notEmpty(MasterWorkOrder_ID)) {
			pd.put("MasterWorkOrder_ID", MasterWorkOrder_ID.trim());
		}
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.list(page); // 列出PlanningWorkOrder列表
		for (PageData pds : varList) {

			// 计划员 和生产主管 拆分( ,yl, )变为数组传给前端
			String FPlannerID = Tools.notEmpty(pds.getString("FPlannerID")) ? pds.getString("FPlannerID") : "";
			pds.put("FPlannerID", Lists.newArrayList(FPlannerID.split(",yl,")));

			String FPlanner = Tools.notEmpty(pds.getString("FPlanner")) ? pds.getString("FPlanner") : "";
			pds.put("FPlanner", Lists.newArrayList(FPlanner.split(",yl,")));

			String ProductionSupervisorID = Tools.notEmpty(pds.getString("ProductionSupervisorID"))
					? pds.getString("ProductionSupervisorID")
					: "";
			pds.put("ProductionSupervisorID", Lists.newArrayList(ProductionSupervisorID.split(",yl,")));

			String ProductionSupervisorName = Tools.notEmpty(pds.getString("ProductionSupervisorName"))
					? pds.getString("ProductionSupervisorName")
					: "";
			pds.put("ProductionSupervisorName", Lists.newArrayList(ProductionSupervisorName.split(",yl,")));
			String FCustomer = Tools.notEmpty(pds.getString("FCustomer")) ? pds.getString("FCustomer") : "";
			if (!"".equals(FCustomer)) {
				PageData pdcustomer = new PageData();
				pdcustomer.put("Customer_ID", FCustomer);
				PageData Customer = CustomerService.findById(pdcustomer);
				if (null != Customer) {
					FCustomer = Customer.getString("FName");
					pds.put("FCustomer", FCustomer);
				}
			}

			pd.put("PlanningWorkOrder_ID", pds.get("PlanningWorkOrder_ID"));
			List<PageData> taskListAll = ProcessWorkOrderExampleService.listAll(pd);
			int taskSize = 0;
			for (int i = 0; i < taskListAll.size(); i++) {
				if (taskListAll.get(i).get("FStatus").toString() == "结束"
						|| taskListAll.get(i).get("FStatus").toString().equals("结束")) {
					taskSize++;
				}
			}
			if (taskSize == taskListAll.size() && taskSize != 0) {
				PlanningWorkOrderService.editStatus(pd);
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 项目看板-待生产
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listXMGD")
	@ResponseBody
	public Object listXMGD(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 模糊查询条件
		// 订单号
		String OrderNum = pd.getString("OrderNum");
		if (Tools.notEmpty(OrderNum)) {
			pd.put("OrderNum", OrderNum.trim());
		}
		// 工单类型
		String WorkOrderType = pd.getString("WorkOrderType");
		if (Tools.notEmpty(WorkOrderType)) {
			pd.put("WorkOrderType", WorkOrderType.trim());
		}
		// 计划工单号
		String WorkOrderNum = pd.getString("WorkOrderNum");
		if (Tools.notEmpty(WorkOrderNum)) {
			pd.put("WorkOrderNum", WorkOrderNum.trim());
		}
		// 主计划工单id
		String MasterWorkOrder_ID = pd.getString("MasterWorkOrder_ID");
		if (Tools.notEmpty(MasterWorkOrder_ID)) {
			pd.put("MasterWorkOrder_ID", MasterWorkOrder_ID.trim());
		}
		pd.put("CREATORMAN", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listXMGD(page); // 列出PlanningWorkOrder列表
		for (PageData pds : varList) {

			// 计划员 和生产主管 拆分( ,yl, )变为数组传给前端
			String FPlannerID = Tools.notEmpty(pds.getString("FPlannerID")) ? pds.getString("FPlannerID") : "";
			pds.put("FPlannerID", Lists.newArrayList(FPlannerID.split(",yl,")));

			String FPlanner = Tools.notEmpty(pds.getString("FPlanner")) ? pds.getString("FPlanner") : "";
			pds.put("FPlanner", Lists.newArrayList(FPlanner.split(",yl,")));

			String ProductionSupervisorID = Tools.notEmpty(pds.getString("ProductionSupervisorID"))
					? pds.getString("ProductionSupervisorID")
					: "";
			pds.put("ProductionSupervisorID", Lists.newArrayList(ProductionSupervisorID.split(",yl,")));

			String ProductionSupervisorName = Tools.notEmpty(pds.getString("ProductionSupervisorName"))
					? pds.getString("ProductionSupervisorName")
					: "";
			pds.put("ProductionSupervisorName", Lists.newArrayList(ProductionSupervisorName.split(",yl,")));
			String FCustomer = Tools.notEmpty(pds.getString("FCustomer")) ? pds.getString("FCustomer") : "";
			if (!"".equals(FCustomer)) {
				PageData pdcustomer = new PageData();
				pdcustomer.put("Customer_ID", FCustomer);
				PageData Customer = CustomerService.findById(pdcustomer);
				if (null != Customer) {
					FCustomer = Customer.getString("FName");
					pds.put("FCustomer", FCustomer);
				}
			}

			pd.put("PlanningWorkOrder_ID", pds.get("PlanningWorkOrder_ID"));
			List<PageData> taskListAll = ProcessWorkOrderExampleService.listAll(pd);
			int taskSize = 0;
			for (int i = 0; i < taskListAll.size(); i++) {
				if (taskListAll.get(i).get("FStatus").toString() == "结束"
						|| taskListAll.get(i).get("FStatus").toString().equals("结束")) {
					taskSize++;
				}
			}
			if (taskSize == taskListAll.size() && taskSize != 0) {
				PlanningWorkOrderService.editStatus(pd);
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据登录员工获取 任务列表 TaskType = 1 生产任务 TaskType = 2 返工任务
	 * 
	 * @param page
	 * @param response
	 * @return
	 */
	@RequestMapping("/listByStaffId")
	@ResponseBody
	public Object listByStaffId(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			List<PageData> varList = Lists.newArrayList();
			pd = this.getPageData();
			String USERNAME = Jurisdiction.getUsername();
			// 获取数据
			if (null != pd) {
				if (Tools.notEmpty(USERNAME)) {
					PageData staffParam = new PageData();
					staffParam.put("USERNAME", USERNAME);
					// 根据 user name 获取 staff id
					PageData staff = staffService.findById(staffParam);
					if (null != staff) {
						// 根据staff id 获取 生产任务 状态为未执行的
						String STAFF_ID = staff.getString("STAFF_ID");
						pd.put("STAFF_ID", STAFF_ID);
						// 默认 生产任务 如果传了 task type 2 为 返工任务
						String TaskType = "1";
						TaskType = Tools.notEmpty(pd.getString("TaskType")) ? pd.getString("TaskType") : TaskType;
						pd.put("TaskType", TaskType);

						PageData stationPersonRel = new PageData();
						stationPersonRel.put("PersonId", STAFF_ID);
						List<PageData> WorkStationPersonRelList = WorkStationPersonRelService.listAll(stationPersonRel);
						List<String> stationIDList = Lists.newArrayList();
						for (PageData WorkStationPersonRel : WorkStationPersonRelList) {
							stationIDList.add(WorkStationPersonRel.getString("WorkstationID"));
						}
						if (CollectionUtils.isEmpty(stationIDList)) {
							map.put("varList", varList);
							map.put("result", errInfo);
							map.put("page", page);
							return map;
						}
						String item = String.join("','", stationIDList);
						pd.put("item", "('" + item + "')");
						// pd.put("stationIDList", stationIDList);
						page.setPd(pd);

						varList = planningWorkOrderService.taskdatalistPage(page);

						for (PageData appProcessWorkOrderExampleDetailByPK : varList) {

							String ProcessIMaterielCode = appProcessWorkOrderExampleDetailByPK
									.getString("ProcessIMaterielCode");

							String yjbs = "正常范围";
							if (StringUtil.isNotEmpty(ProcessIMaterielCode)) {

								PageData tocmesParam = new PageData();
								tocmesParam.put("ProcessIMaterielCode", ProcessIMaterielCode);
								List<PageData> mesTocStatus = Cabinet_Assembly_DetailService.mesTocStatus(tocmesParam);
								if (CollectionUtil.isNotEmpty(mesTocStatus)) {
									yjbs = mesTocStatus.get(0).getString("预警标识");
								}
							}
							appProcessWorkOrderExampleDetailByPK.put("yjbs", yjbs);

							if (Tools.isEmpty(appProcessWorkOrderExampleDetailByPK.getString("OriginTaskID"))) {
								appProcessWorkOrderExampleDetailByPK.put("OriginTaskNum", "");
							} else {
								PageData pData = new PageData();
								pData.put("ProcessWorkOrderExample_ID",
										appProcessWorkOrderExampleDetailByPK.getString("OriginTaskID"));
								PageData orginPwoe = processWorkOrderExampleService.findById(pData);
								appProcessWorkOrderExampleDetailByPK.put("OriginTaskNum",
										orginPwoe.getString("TaskNum"));
							}
							if (Tools.isEmpty(appProcessWorkOrderExampleDetailByPK.getString("ActualBeginTime"))) {
								appProcessWorkOrderExampleDetailByPK.put("ActualBeginTime", "");
							}
							if (Tools.isEmpty(appProcessWorkOrderExampleDetailByPK.getString("ActualEndTime"))) {
								appProcessWorkOrderExampleDetailByPK.put("ActualEndTime", "");
							}
							if (Tools.isEmpty(appProcessWorkOrderExampleDetailByPK.getString("ProcessIMateriel"))) {
								appProcessWorkOrderExampleDetailByPK.put("ProcessIMateriel", "");
							} else {
								appProcessWorkOrderExampleDetailByPK.put("ProcessIMateriel",
										appProcessWorkOrderExampleDetailByPK.getString("ProcessIMaterielCode") + "|"
												+ appProcessWorkOrderExampleDetailByPK.getString("ProcessIMateriel"));
							}

							appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", 0);
							appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 0);
							appProcessWorkOrderExampleDetailByPK.put("PlannedQuantity", 0);
							List<PageData> listByProcessWorkOrderExampleID = workorderProcessIOExampleService
									.listByProcessWorkOrderExampleID(appProcessWorkOrderExampleDetailByPK
											.getString("ProcessWorkOrderExample_ID"));
							// 筛选出产出类型的数据
							List<PageData> collect = listByProcessWorkOrderExampleID.stream()
									.filter(t -> "产出".equals(t.getString("TType"))).collect(Collectors.toList());
							if (CollectionUtil.isNotEmpty(collect)) {
								PageData pageData = collect.get(0);
								String PlannedQuantityStr = String.valueOf(pageData.get("PlannedQuantity"));
								BigDecimal PlannedQuantityBigDec = new BigDecimal(PlannedQuantityStr);
								// 根据物料消耗 表 获取当前生产了多少 消耗了多少 做百分比
								PageData materialConsumeParam = new PageData();
								materialConsumeParam.put("ConsumptionDocumentID",
										pageData.getString("ProcessWorkOrderExampleID"));
								materialConsumeParam.put("FType", pageData.getString("TType"));
								materialConsumeParam.put("MaterialID", pageData.getString("MaterialID"));
								materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
								List<PageData> MaterialConsumeList = MaterialConsumeService
										.listAll(materialConsumeParam);
								if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
									String ConsumptionQuantityStr = String
											.valueOf(MaterialConsumeList.get(0).get("ConsumptionQuantity"));
									BigDecimal ConsumptionQuantityBigDec = new BigDecimal(ConsumptionQuantityStr);
									appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity",
											ConsumptionQuantityBigDec);
									BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDec, 4,
											BigDecimal.ROUND_HALF_DOWN);
									BigDecimal multiply = divide.multiply(new BigDecimal("100"));
									if (multiply.doubleValue() > 100.00) {
										appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 100);
									} else {
										String percent = new DecimalFormat("#.00").format(multiply);
										String index0 = percent.substring(0, 1);
										if (".".equals(index0)) {
											percent = "0".concat(percent);
										}
										// 计算完成百分比
										appProcessWorkOrderExampleDetailByPK.put("TaskPercent",
												Double.valueOf(percent));
									}
								} else {

									appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", 0);

									appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 0);

								}
								appProcessWorkOrderExampleDetailByPK.put("PlannedQuantity", PlannedQuantityBigDec);
							}
						}
					}
				}
			}
			map.put("varList", varList);
			map.put("result", errInfo);
			map.put("page", page);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
			map.put("result", errInfo);
			map.put("page", page);
			return map;
		}
	}

	/**
	 * 列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage")
	@ResponseBody
	public Object listPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 模糊查询条件
		// 订单号
		String OrderNum = pd.getString("OrderNum");
		if (Tools.notEmpty(OrderNum)) {
			pd.put("OrderNum", OrderNum.trim());
		}
		// 工单类型
		String WorkOrderType = pd.getString("WorkOrderType");
		if (Tools.notEmpty(WorkOrderType)) {
			pd.put("WorkOrderType", WorkOrderType.trim());
		}
		// 计划工单号
		String WorkOrderNum = pd.getString("WorkOrderNum");
		if (Tools.notEmpty(WorkOrderNum)) {
			pd.put("WorkOrderNum", WorkOrderNum.trim());
		}
		// 主计划工单id
		String MasterWorkOrder_ID = pd.getString("MasterWorkOrder_ID");
		if (Tools.notEmpty(MasterWorkOrder_ID)) {
			pd.put("MasterWorkOrder_ID", MasterWorkOrder_ID.trim());
		}
		// 主计划工单id
		String WorkOrderNumMaster = pd.getString("WorkOrderNumMaster");
		if (Tools.notEmpty(WorkOrderNumMaster)) {
			pd.put("WorkOrderNumMaster", WorkOrderNumMaster.trim());
		}
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.list(page); // 列出PlanningWorkOrder列表
		for (PageData pds : varList) {

			// 计划员 和生产主管 拆分( ,yl, )变为数组传给前端
			String FPlannerID = Tools.notEmpty(pds.getString("FPlannerID")) ? pds.getString("FPlannerID") : "";
			pds.put("FPlannerID", Lists.newArrayList(FPlannerID.split(",yl,")));

			String FPlanner = Tools.notEmpty(pds.getString("FPlanner")) ? pds.getString("FPlanner") : "";
			pds.put("FPlanner", Lists.newArrayList(FPlanner.split(",yl,")));

			String ProductionSupervisorID = Tools.notEmpty(pds.getString("ProductionSupervisorID"))
					? pds.getString("ProductionSupervisorID")
					: "";
			pds.put("ProductionSupervisorID", Lists.newArrayList(ProductionSupervisorID.split(",yl,")));

			String ProductionSupervisorName = Tools.notEmpty(pds.getString("ProductionSupervisorName"))
					? pds.getString("ProductionSupervisorName")
					: "";
			pds.put("ProductionSupervisorName", Lists.newArrayList(ProductionSupervisorName.split(",yl,")));
			String FCustomer = Tools.notEmpty(pds.getString("FCustomer")) ? pds.getString("FCustomer") : "";
			if (!"".equals(FCustomer)) {
				PageData pdcustomer = new PageData();
				pdcustomer.put("Customer_ID", FCustomer);
				PageData Customer = CustomerService.findById(pdcustomer);
				if (null != Customer) {
					FCustomer = Customer.getString("FName");
					pds.put("FCustomer", FCustomer);
				}
			}
		}
		map.put("page", page);
		map.put("varList", varList);
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
			for (String PlanningWorkOrderID : ArrayDATA_IDS) {

				// 根据子计划工单删除所有节点
				NEWPLAN_BOMService.deleteBySubPlanID(PlanningWorkOrderID);

				// 删除子计划工单里的流程 JSON 重建
				PageData deleteParam = new PageData();
				deleteParam.put("PID", PlanningWorkOrderID);
				deleteParam.put("FTYPE", "工单实例BOM");
				BYTEARRAYService.deleteByPidAndFTYPE(deleteParam);

				// 根据计划工单id，物料需求单
				PlanningWorkOrderService.deleteWorkorderProcessIOExampleByPlanningWorkOrderID(PlanningWorkOrderID);
				// 根据工艺工单工序实例id，删除工单工序投入产出实例
				PlanningWorkOrderService.deleteMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrderID);
				// 根据子计划工单id 删除所有SOP实例
				PlanningWorkOrderService.deleteProcessWorkOrderExampleSopStepByPlanningWorkOrderID(PlanningWorkOrderID);

				// 根据计划工单id，删除工艺工单工序实例
				PlanningWorkOrderService.deleteProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);
				// 删除附件
				PageData pData = new PageData();
				pData.put("AssociationID", PlanningWorkOrderID);
				attachmentsetService.delete(pd);
				// 删除辅助属性
				pData.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
				PageData pdx = PlanningWorkOrderService.findById(pData);
				pData.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
				pData.put("MAT_AUXILIARYMX_CODE", pdx.getString("WorkOrderNum"));
				mat_auxiliarymxService.deleteByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pData);
			}
			PlanningWorkOrderService.deleteAll(ArrayDATA_IDS);
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
		titles.add("客户"); // 1
		titles.add("工单类型"); // 2
		titles.add("销售订单明细ID"); // 3
		titles.add("工单编号"); // 4
		titles.add("产出物料"); // 5
		titles.add("规格"); // 6
		titles.add("数量"); // 7
		titles.add("批号类型"); // 8
		titles.add("批号号码"); // 9
		titles.add("优先级"); // 10
		titles.add("计划开始时间"); // 11
		titles.add("计划结束时间"); // 12
		titles.add("计划员类型"); // 13
		titles.add("计划员ID"); // 14
		titles.add("生产主管类型"); // 15
		titles.add("生产主管名称"); // 16
		titles.add("工艺类型"); // 17
		titles.add("工艺名称"); // 18
		titles.add("备注"); // 19
		titles.add("状态"); // 20
		titles.add("排程进度(未排程,已排程)"); // 21
		titles.add("下发进度(未下发,待下发,已下发)"); // 22
		titles.add("工单创建时间"); // 23
		titles.add("工单计划开始时间"); // 24
		titles.add("工单计划结束时间"); // 25
		titles.add("计划员"); // 26
		titles.add("是否创建物流需求单"); // 27
		titles.add("创建物料需求单状态"); // 28
		titles.add("创建时间"); // 29
		dataMap.put("titles", titles);
		List<PageData> varOList = PlanningWorkOrderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FCustomer")); // 1
			vpd.put("var2", varOList.get(i).getString("WorkOrderType")); // 2
			vpd.put("var3", varOList.get(i).getString("SalesOrderDetailID")); // 3
			vpd.put("var4", varOList.get(i).getString("WorkOrderNum")); // 4
			vpd.put("var5", varOList.get(i).getString("OutputMaterial")); // 5
			vpd.put("var6", varOList.get(i).getString("FSpecifications")); // 6
			vpd.put("var7", varOList.get(i).get("FCount").toString()); // 7
			vpd.put("var8", varOList.get(i).getString("BatchNumType")); // 8
			vpd.put("var9", varOList.get(i).getString("BatchNum")); // 9
			vpd.put("var10", varOList.get(i).get("FPriorityID").toString()); // 10
			vpd.put("var11", varOList.get(i).getString("PlannedBeginTime")); // 11
			vpd.put("var12", varOList.get(i).getString("PlannedEndTime")); // 12
			vpd.put("var13", varOList.get(i).getString("PlannerType")); // 13
			vpd.put("var14", varOList.get(i).getString("FPlannerID")); // 14
			vpd.put("var15", varOList.get(i).getString("ProductionSupervisorType")); // 15
			vpd.put("var16", varOList.get(i).getString("ProductionSupervisorID")); // 16
			vpd.put("var17", varOList.get(i).getString("ProcessType")); // 17
			vpd.put("var18", varOList.get(i).getString("ProcessName")); // 18
			vpd.put("var19", varOList.get(i).getString("FExplanation")); // 19
			vpd.put("var20", varOList.get(i).getString("FStatus")); // 20
			vpd.put("var21", varOList.get(i).getString("ScheduleSchedule")); // 21
			vpd.put("var22", varOList.get(i).getString("DistributionProgress")); // 22
			vpd.put("var23", varOList.get(i).getString("WorkOrderCreateTime")); // 23
			vpd.put("var24", varOList.get(i).getString("WorkOrderPlanBeginTime")); // 24
			vpd.put("var25", varOList.get(i).getString("WorkOrderPlanEndTime")); // 25
			vpd.put("var26", varOList.get(i).getString("FPlanner")); // 26
			vpd.put("var27", varOList.get(i).getString("CreateMRIF")); // 27
			vpd.put("var28", varOList.get(i).getString("CreateMRStatus")); // 28
			vpd.put("var29", varOList.get(i).getString("FCreateTime")); // 29
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 修改状态
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeStatus")
	@ResponseBody
	public Object changeStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PlanningWorkOrderService.changeStatus(pd);

		if ("暂停".equals(pd.getString("FStatus")) || "执行中".equals(pd.getString("FStatus"))) {
			PageData pageData = new PageData();
			pageData.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrder_ID"));
			List<PageData> listAll = processWorkOrderExampleService.listAll(pageData);
			for (PageData pwoe : listAll) {
				pwoe.put("FStatus", pd.getString("FStatus"));
				processWorkOrderExampleService.edit(pwoe);
			}
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 上传文件
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = file.getOriginalFilename();// 文件上传路径
			String FFILENAME = FileUpload.fileUp(file, filePath, fileNamereal + dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
			map.put("FName", FFILENAME);
			map.put("FUrl", FPFFILEPATH);
		}
		return map;
	}

	// 调用接口 文档归档操作
	private Map<String, Object> attachment(String FUrl, String FName, String AssociationID, String FExplanation)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd1 = new PageData();
		PageData pdOp = new PageData();
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");// 操作人
		boolean flag = false;
		pd1.put("FUrl", FUrl); // 附件路径
		pd1.put("FName", FName); // 附件名称
		pd1.put("DataSources", "计划工单"); // 数据源
		pd1.put("AssociationIDTable", "PP_PlanningWorkOrder"); // 数据源表
		pd1.put("AssociationID", AssociationID); // 数据源ID
		pd1.put("FExplanation", FExplanation); // 备注
		pd1.put("FCreatePersonID", STAFF_ID); // 创建人ID
		pd1.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间
		flag = AttachmentSetService.check(pd1);
		if (flag == true) {
			map.put("result", errInfo);
		} else {
			errInfo = "error";
			String msg = "请选择上传文件！";
			map.put("result", errInfo);
			map.put("msg", msg);
		}
		return map;
	}

	/**
	 * 创建物料转移单时 保存计划工单的转移单状态
	 */
	@RequestMapping(value = "/saveCreateMRStatus")
	@ResponseBody
	public Object saveCreateMRStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PlanningWorkOrderService.saveCreateMRStatus(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 插入工艺工单实例表数据
	 */
	@RequestMapping(value = "/insertProcessWorkOrderExample")
	@ResponseBody
	public Object insertProcessWorkOrderExample() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ProcessWorkOrderExample_ID", this.get32UUID()); // 主键
		PlanningWorkOrderService.insertProcessWorkOrderExample(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 插入产出实例表数据
	 */
	@RequestMapping(value = "/insertWorkorderProcessIOExample")
	@ResponseBody
	public Object insertWorkorderProcessIOExample() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("WorkorderProcessIOExample_ID", this.get32UUID()); // 主键
		PlanningWorkOrderService.insertWorkorderProcessIOExample(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 更新计划工单生产进度
	 */
	@RequestMapping(value = "/updateWorkOrderDistributionProgress")
	@ResponseBody
	public Object updateWorkOrderDistributionProgress() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PlanningWorkOrderService.updateWorkOrderDistributionProgress(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 复制计划工单
	 */
	@RequestMapping(value = "/copyWorkOrder")
	@ResponseBody
	public Object copyWorkOrder() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String WorkOrderNum = String.valueOf(CodingRulesService.getRuleNumByRuleType("JHGDBH"));
		pd.put("WorkOrderNum", WorkOrderNum);
		String BatchNum = String.valueOf(CodingRulesService.getRuleNumByRuleType("JHGDPH"));
		pd.put("BatchNum", BatchNum);
		this.copyWorkOrder(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据计划工单的生产数量 和 工艺路线的 投入产出量 计算总的 投入和产出
	 */
	@RequestMapping(value = "/getAllInputOutput")
	@ResponseBody
	public Object getAllInputOutput() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		Double count = Double.valueOf(pd.getString("count"));
		String ProductionBOM_ID = pd.getString("ProductionBOM_ID");
		String WokType = pd.getString("WokType");
		if (null == count) {
			map.put("dataList", Lists.newArrayList());
			map.put("result", errInfo);
			return map;
		}
		if (Tools.isEmpty(ProductionBOM_ID)) {
			throw new RuntimeException("BOM ID为空");
		}
		List<PageData> dataList = inputOutputService.calculateInputOutputListByBomIdAndCount(ProductionBOM_ID, count);

		// 不拆锅计算
		if (Tools.isEmpty(WokType) || "0".equals(WokType)) {
			map.put("dataList", dataList);
		}
		BigDecimal countBig = new BigDecimal(count);
		// 正常拆锅计算
		if ("1".equals(WokType)) {
			PageData bomParam = new PageData();
			bomParam.put("ProductionBOM_ID", ProductionBOM_ID);
			PageData findByIdBom = productionBOMService.findById(bomParam);
			List<PageData> resultList = Lists.newArrayList();
			if (null != findByIdBom) {
				String ProcessRouteRel = findByIdBom.getString("ProcessRouteRel");
				if (Tools.notEmpty(ProcessRouteRel)) {
					PageData wPageData = new PageData();
					wPageData.put("ProcessRouteID", ProcessRouteRel);
					List<PageData> WorkingProcedureList = WorkingProcedureService.listAll(wPageData);

					// 工序列表，其中每个工序配置了产能
					for (PageData WorkingProcedure : WorkingProcedureList) {
						// 工序
						String WP = WorkingProcedure.getString("WP");
						// 是否支持拆锅
						String WokIF = WorkingProcedure.getString("WokIF");
						// 产能
						String Throughput = WorkingProcedure.getString("Throughput");
						BigDecimal[] devide = null;
						if ("是".equals(WokIF) && Tools.notEmpty(Throughput)) {
							// 用该产出需求量 / 产能算锅次 然后根据锅次正常拆分 返回
							devide = new BigDecimal(count).divideAndRemainder(new BigDecimal(Throughput));
						}

						List<BigDecimal> devideList = Lists.newArrayList();
						for (int i = 0; i < devide[0].intValue(); i++) {
							devideList.add(new BigDecimal(Throughput));
						}
						if (0 != devide[1].intValue()) {
							devideList.add(devide[1]);
						}

						for (BigDecimal wokValue : devideList) {
							for (PageData InputOutput : dataList) {
								if (InputOutput.getString("WP").equals(WP)) {
									BigDecimal DemandCountOrigin = new BigDecimal(InputOutput.getString("DemandCount"));
									BigDecimal DemandCountBig = wokValue.divide(countBig, 8, RoundingMode.HALF_UP)
											.multiply(DemandCountOrigin).setScale(2, BigDecimal.ROUND_HALF_UP);
									PageData result = new PageData();
									Tools.mapCopy(InputOutput, result);
									result.put("DemandCount", DemandCountBig.toString());
									resultList.add(result);
								}
							}
						}

					}
				}
			}
			map.put("dataList", resultList);
		}
		// 均匀锅次计算
		if ("2".equals(WokType)) {

			PageData bomParam = new PageData();
			bomParam.put("ProductionBOM_ID", ProductionBOM_ID);
			PageData findByIdBom = productionBOMService.findById(bomParam);
			List<PageData> resultList = Lists.newArrayList();
			if (null != findByIdBom) {
				String ProcessRouteRel = findByIdBom.getString("ProcessRouteRel");

				if (Tools.notEmpty(ProcessRouteRel)) {
					PageData wPageData = new PageData();
					wPageData.put("ProcessRouteID", ProcessRouteRel);
					List<PageData> WorkingProcedureList = WorkingProcedureService.listAll(wPageData);

					// 工序列表，其中每个工序配置了产能
					for (PageData WorkingProcedure : WorkingProcedureList) {
						// 工序
						String WP = WorkingProcedure.getString("WP");
						// 是否支持拆锅
						String WokIF = WorkingProcedure.getString("WokIF");
						// 产能
						String Throughput = WorkingProcedure.getString("Throughput");
						if ("是".equals(WokIF) && Tools.notEmpty(Throughput)) {
							// 用该产出需求量 / 产能算锅次 然后根据锅次正常拆分 返回
							BigDecimal[] devide = new BigDecimal(count).divideAndRemainder(new BigDecimal(Throughput));

							List<BigDecimal> devideList = Lists.newArrayList();
							for (int i = 0; i < devide[0].intValue(); i++) {
								devideList.add(new BigDecimal(Throughput));
							}
							if (0 != devide[1].intValue()) {
								devideList.add(devide[1]);
							}

							for (int i = 0; i < devideList.size(); i++) {
								for (PageData InputOutput : dataList) {
									if (InputOutput.getString("WP").equals(WP)) {
										BigDecimal DemandCountOrigin = new BigDecimal(
												InputOutput.getString("DemandCount"));
										// 均分
										BigDecimal DemandCountBig = DemandCountOrigin
												.divide(new BigDecimal(devideList.size()), 8, RoundingMode.HALF_UP);

										PageData result = new PageData();
										Tools.mapCopy(InputOutput, result);
										result.put("DemandCount",
												DemandCountBig.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

										resultList.add(result);
									}
								}
							}
						}
					}
				}
			}
			map.put("dataList", resultList);

		}
		// 去尾均匀计算
		if ("3".equals(WokType)) {
			PageData bomParam = new PageData();
			bomParam.put("ProductionBOM_ID", ProductionBOM_ID);
			PageData findByIdBom = productionBOMService.findById(bomParam);
			List<PageData> resultList = Lists.newArrayList();
			if (null != findByIdBom) {
				String ProcessRouteRel = findByIdBom.getString("ProcessRouteRel");
				if (Tools.notEmpty(ProcessRouteRel)) {
					PageData wPageData = new PageData();
					wPageData.put("ProcessRouteID", ProcessRouteRel);
					List<PageData> WorkingProcedureList = WorkingProcedureService.listAll(wPageData);

					// 工序列表，其中每个工序配置了产能
					for (PageData WorkingProcedure : WorkingProcedureList) {
						// 工序
						String WP = WorkingProcedure.getString("WP");
						// 是否支持拆锅
						String WokIF = WorkingProcedure.getString("WokIF");
						// 产能
						String Throughput = WorkingProcedure.getString("Throughput");
						if ("是".equals(WokIF) && Tools.notEmpty(Throughput)) {
							// 用该产出需求量 / 产能算锅次 然后根据锅次正常拆分 返回
							BigDecimal[] devide = new BigDecimal(count).divideAndRemainder(new BigDecimal(Throughput));

							// 去尾均匀 不将余数添加到list中
							List<BigDecimal> devideList = Lists.newArrayList();
							for (int i = 0; i < devide[0].intValue(); i++) {
								devideList.add(new BigDecimal(Throughput));
							}

							for (int i = 0; i < devideList.size(); i++) {
								for (PageData InputOutput : dataList) {
									if (InputOutput.getString("WP").equals(WP)) {
										BigDecimal DemandCountOrigin = new BigDecimal(
												InputOutput.getString("DemandCount"));
										// 均分
										BigDecimal DemandCountBig = DemandCountOrigin
												.divide(new BigDecimal(devideList.size()), 8, RoundingMode.HALF_UP);
										PageData result = new PageData();
										Tools.mapCopy(InputOutput, result);
										result.put("DemandCount",
												DemandCountBig.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
										resultList.add(result);
									}
								}
							}
						}
					}
				}
			}
			map.put("dataList", resultList);
		}
		map.put("result", errInfo);
		return map;

	}

	/**
	 * 获取编号列表-可搜索-前100条
	 * 
	 * @author 管悦
	 * @date 2020-11-16
	 * @throws Exception
	 */
	@RequestMapping(value = "/getWorkOrderNumList")
	@ResponseBody
	public Object getWorkOrderNumList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); //
		if (Tools.notEmpty(KEYWORDS)) {
			pd.put("KEYWORDS", KEYWORDS.trim());
		}
		List<PageData> varList = PlanningWorkOrderService.getWorkOrderNumList(pd);
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 陈春光 20210603 获取批号列表
	 * 
	 * @author 管悦
	 * @date 2020-11-16
	 * @throws Exception
	 */
	@RequestMapping(value = "/getWorkOrderBatchList")
	@ResponseBody
	public Object getWorkOrderBatchList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); //
		if (Tools.notEmpty(KEYWORDS)) {
			pd.put("KEYWORDS", KEYWORDS.trim());
		}
		List<PageData> varList = PlanningWorkOrderService.getWorkOrderNumList(pd);

		Set<PageData> batchSet = Sets.newHashSet();
		for (PageData pageData : varList) {
			PageData pData = new PageData();
			pData.put("value", pageData.getString("BatchNum"));
			pData.put("BatchNum", pageData.getString("BatchNum"));
			batchSet.add(pData);
		}
		map.put("varList", batchSet);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据计划工单id获取物料需求单列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getMaterialRequirementListByPlanningWorkOrderId")
	@ResponseBody
	public Object getMaterialRequirementListByPlanningWorkOrderId() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if (Tools.isEmpty(pd.getString("PlanningWorkOrder_ID"))) {
			throw new RuntimeException("参数错误");
		}

		PageData pwo = PlanningWorkOrderService.findById(pd);
		PageData mat_auxiliarymxParam = new PageData();
		mat_auxiliarymxParam.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
		mat_auxiliarymxParam.put("MAT_AUXILIARYMX_CODE", pwo.getString("MasterWorkOrderNum"));
		List<PageData> mat_auxiliarymxs = mat_auxiliarymxService.listAll(mat_auxiliarymxParam);
		String spropID = "";
		if (CollectionUtil.isNotEmpty(mat_auxiliarymxs)) {
			spropID = mat_auxiliarymxs.get(0).getString("MAT_AUXILIARYMX_ID");
		}

		List<PageData> varList = MaterialRequirementService.getMaterialRequirementList(pd);
		List<PageData> bomList = Lists.newArrayList();
		String Cabinet_No = pwo.getString("OutputMaterial").split("/")[0];
		if (!StringUtils.isEmpty(Cabinet_No)) {
			PageData pdCabNo = new PageData();
			pdCabNo.put("Cabinet_No", Cabinet_No);
			List<PageData> listAll = Cabinet_Assembly_DetailService.listAll(pdCabNo);
			if (CollectionUtil.isNotEmpty(listAll)) {
				String detailId = listAll.get(0).getString("Cabinet_Assembly_Detail_ID");
				PageData bomParam = new PageData();
				bomParam.put("Cabinet_Assembly_Detail_ID", detailId);
				bomList = Cabinet_BOMService.listAll(bomParam);
			}
		}
		if (CollectionUtil.isEmpty(varList)) {
			if (CollectionUtil.isNotEmpty(bomList)) {
				for (PageData bomData : bomList) {
					// 从系统中获取staffID
					PageData pdOp = new PageData();
					String name = Jurisdiction.getName();
					pdOp.put("FNAME", name);
					String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
					PageData pdData = new PageData();
					// 行关闭 默认不关闭 N
					pdData.put("RowClose", "N");
					pdData.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrder_ID"));
					//
					pdData.put("SProp", "2540539e45324232a50bde60ac2951d3");
					pdData.put("SPropKey", spropID);

					pdData.put("DemandCount", bomData.get("BOM_COUNT"));
					pdData.put("IssuedQuantity", "");
					pdData.put("DeliveryWarehouse", "");
					pdData.put("DeliveryPosition", "");
					pdData.put("TargetWarehouse", "");
					pdData.put("TargetPosition", "");
					// 需求时间
					pdData.put("DemandTime", Tools.date2Str(new Date()));
					pdData.put("WorkOrderNum", pwo.getString("WorkOrderNum"));
					// 日期批次
					pdData.put("DateBatch", Tools.date2Str(new Date(), "yyyy-MM-dd"));

					pdData.put("TType", "投入");
					pdData.put("FStatus", "");
					pdData.put("OccupationOfInventory", "");
					pdData.put("TargetPosition", "");
					pdData.put("FMakeBillsPersoID", STAFF_ID);
					pdData.put("FMakeBillsTime", Tools.date2Str(new Date()));
					pdData.put("PushDownTransferIF", "");
					pdData.put("PushDownPurchaseIF", "");
					;
					pdData.put("MaterialID", bomData.getString("MAT_BASIC_ID"));

					pdData.put("MaterialNum", bomData.getString("MAT_CODE"));
					pdData.put("MaterialName", bomData.getString("MAT_NAME"));

					pdData.put("PurchaseCount", "");
					pdData.put("BatchNum", pwo.getString("BatchNum"));
					varList.add(pdData);
				}
			}

		} else {
			if (CollectionUtil.isNotEmpty(bomList)) {
				if (varList.size() != bomList.size()) {
					for (PageData pageData : varList) {
						MaterialRequirementService.delete(pageData);
					}
					varList = Lists.newArrayList();
					for (PageData bomData : bomList) {
						// 从系统中获取staffID
						PageData pdOp = new PageData();
						String name = Jurisdiction.getName();
						pdOp.put("FNAME", name);
						String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
						PageData pdData = new PageData();
						// 行关闭 默认不关闭 N
						pdData.put("RowClose", "N");
						pdData.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrder_ID"));
						//
						pdData.put("SProp", "2540539e45324232a50bde60ac2951d3");
						pdData.put("SPropKey", spropID);

						pdData.put("DemandCount", bomData.get("BOM_COUNT"));
						pdData.put("IssuedQuantity", "");
						pdData.put("DeliveryWarehouse", "");
						pdData.put("DeliveryPosition", "");
						pdData.put("TargetWarehouse", "");
						pdData.put("TargetPosition", "");
						// 需求时间
						pdData.put("DemandTime", Tools.date2Str(new Date()));
						pdData.put("WorkOrderNum", pwo.getString("WorkOrderNum"));
						// 日期批次
						pdData.put("DateBatch", Tools.date2Str(new Date(), "yyyy-MM-dd"));

						pdData.put("TType", "投入");
						pdData.put("FStatus", "");
						pdData.put("OccupationOfInventory", "");
						pdData.put("TargetPosition", "");
						pdData.put("FMakeBillsPersoID", STAFF_ID);
						pdData.put("FMakeBillsTime", Tools.date2Str(new Date()));
						pdData.put("PushDownTransferIF", "");
						pdData.put("PushDownPurchaseIF", "");
						;
						pdData.put("MaterialID", bomData.getString("MAT_BASIC_ID"));

						pdData.put("MaterialNum", bomData.getString("MAT_CODE"));
						pdData.put("MaterialName", bomData.getString("MAT_NAME"));

						pdData.put("PurchaseCount", "");
						pdData.put("BatchNum", pwo.getString("BatchNum"));
						varList.add(pdData);
					}

				} else {
					for (int i = 0; i < bomList.size(); i++) {
						PageData bomData = bomList.get(i);
						PageData mrData = varList.get(i);
						if (bomData.getString("MAT_CODE").equals(mrData.getString("MaterialNum"))) {
							if (new BigDecimal(bomData.get("BOM_COUNT").toString())
									.doubleValue() != (new BigDecimal(mrData.get("DemandCount").toString()))
											.doubleValue()) {
								MaterialRequirementService.delete(mrData);
								varList.remove(i);
								// 从系统中获取staffID
								PageData pdOp = new PageData();
								String name = Jurisdiction.getName();
								pdOp.put("FNAME", name);
								String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
								PageData pdData = new PageData();
								// 行关闭 默认不关闭 N
								pdData.put("RowClose", "N");
								pdData.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrder_ID"));
								//
								pdData.put("SProp", "2540539e45324232a50bde60ac2951d3");
								pdData.put("SPropKey", spropID);

								pdData.put("DemandCount", bomData.get("BOM_COUNT"));
								pdData.put("IssuedQuantity", "");
								pdData.put("DeliveryWarehouse", "");
								pdData.put("DeliveryPosition", "");
								pdData.put("TargetWarehouse", "");
								pdData.put("TargetPosition", "");
								// 需求时间
								pdData.put("DemandTime", Tools.date2Str(new Date()));
								pdData.put("WorkOrderNum", pwo.getString("WorkOrderNum"));
								// 日期批次
								pdData.put("DateBatch", Tools.date2Str(new Date(), "yyyy-MM-dd"));

								pdData.put("TType", "投入");
								pdData.put("FStatus", "");
								pdData.put("OccupationOfInventory", "");
								pdData.put("TargetPosition", "");
								pdData.put("FMakeBillsPersoID", STAFF_ID);
								pdData.put("FMakeBillsTime", Tools.date2Str(new Date()));
								pdData.put("PushDownTransferIF", "");
								pdData.put("PushDownPurchaseIF", "");
								;
								pdData.put("MaterialID", bomData.getString("MAT_BASIC_ID"));

								pdData.put("MaterialNum", bomData.getString("MAT_CODE"));
								pdData.put("MaterialName", bomData.getString("MAT_NAME"));

								pdData.put("PurchaseCount", "");
								pdData.put("BatchNum", pwo.getString("BatchNum"));
								varList.add(i, pdData);
							}

						} else {
							MaterialRequirementService.delete(mrData);
							varList.remove(i);
							// 从系统中获取staffID
							PageData pdOp = new PageData();
							String name = Jurisdiction.getName();
							pdOp.put("FNAME", name);
							String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
							PageData pdData = new PageData();
							// 行关闭 默认不关闭 N
							pdData.put("RowClose", "N");
							pdData.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrder_ID"));
							//
							pdData.put("SProp", "2540539e45324232a50bde60ac2951d3");
							pdData.put("SPropKey", spropID);

							pdData.put("DemandCount", bomData.get("BOM_COUNT"));
							pdData.put("IssuedQuantity", "");
							pdData.put("DeliveryWarehouse", "");
							pdData.put("DeliveryPosition", "");
							pdData.put("TargetWarehouse", "");
							pdData.put("TargetPosition", "");
							// 需求时间
							pdData.put("DemandTime", Tools.date2Str(new Date()));
							pdData.put("WorkOrderNum", pwo.getString("WorkOrderNum"));
							// 日期批次
							pdData.put("DateBatch", Tools.date2Str(new Date(), "yyyy-MM-dd"));

							pdData.put("TType", "投入");
							pdData.put("FStatus", "");
							pdData.put("OccupationOfInventory", "");
							pdData.put("TargetPosition", "");
							pdData.put("FMakeBillsPersoID", STAFF_ID);
							pdData.put("FMakeBillsTime", Tools.date2Str(new Date()));
							pdData.put("PushDownTransferIF", "");
							pdData.put("PushDownPurchaseIF", "");
							;
							pdData.put("MaterialID", bomData.getString("MAT_BASIC_ID"));

							pdData.put("MaterialNum", bomData.getString("MAT_CODE"));
							pdData.put("MaterialName", bomData.getString("MAT_NAME"));

							pdData.put("PurchaseCount", "");
							pdData.put("BatchNum", pwo.getString("BatchNum"));
							varList.add(i, pdData);
						}

					}
				}

			}
		}
		for (PageData d : varList) {

			PageData pData = new PageData();
			pData.put("ProcessDefinition_ID", d.getString("WP"));
			String WPName = d.getString("WPName");
			if (null != WPName) {
				d.put("WPName", WPName);
			}
			String MaterialNum = d.getString("MaterialNum");
			List<PageData> listByMatCode = mat_basicService.getListByMatCode(MaterialNum);
			if (CollectionUtil.isNotEmpty(listByMatCode)) {
				d.put("MaterialID", listByMatCode.get(0).get("MAT_BASIC_ID"));
			}

			String MaterialID = d.getString("MaterialID");
			PageData pdData = new PageData();
			pdData.put("MAT_BASIC_ID", MaterialID);
			pdData = mat_basicService.findById(pdData);
			if (null != pdData) {
				if (Tools.notEmpty(pdData.getString("MAT_SPECS"))) {
					d.put("MAT_SPECS", pdData.getString("MAT_SPECS"));
				} else {
					d.put("MAT_SPECS", "");
				}
			}
			PageData pdKC = new PageData();
			pdKC.put("WarehouseID", d.getString("DeliveryWarehouse"));
			pdKC.put("ItemID", d.getString("MaterialID"));

			if (null != d.getString("SPropKey")) {
				pdKC.put("MaterialSPropKey", d.getString("SPropKey"));
				PageData pdStockSum = StockService.getSum(pdKC);
				if (pdStockSum != null) {
					d.put("stockSum", pdStockSum.get("stockSum").toString());
				} else {
					d.put("stockSum", "0");
				}
			}

			if (Tools.notEmpty(d.getString("DeliveryWarehouse"))) {
				PageData warehouseParam = new PageData();
				warehouseParam.put("WH_WAREHOUSE_ID", d.getString("DeliveryWarehouse"));
				PageData DeliveryWarehouse = WH_WareHouseService.findById(warehouseParam);
				if (null != DeliveryWarehouse) {
					d.put("DeliveryWarehouseName", DeliveryWarehouse.getString("FNAME"));
				} else {
					d.put("DeliveryWarehouseName", "");
				}
			} else {
				d.put("DeliveryWarehouseName", "");
				d.put("DeliveryWarehouse", "");
			}

			if (Tools.notEmpty(d.getString("TargetWarehouse"))) {
				PageData warehouseParam = new PageData();
				warehouseParam.put("WH_WAREHOUSE_ID", d.getString("TargetWarehouse"));
				PageData DeliveryWarehouse = WH_WareHouseService.findById(warehouseParam);
				if (null != DeliveryWarehouse) {
					d.put("TargetWarehouseName", DeliveryWarehouse.getString("FNAME"));
				} else {
					d.put("TargetWarehouseName", "");
				}
			} else {
				d.put("TargetWarehouseName", "");
				d.put("TargetWarehouse", "");
			}

			if (Tools.notEmpty(d.getString("DeliveryPosition"))) {
				PageData locationParam = new PageData();
				locationParam.put("WH_LOCATION_ID", d.getString("DeliveryPosition"));
				PageData TargetPosition = WH_LocationService.findById(locationParam);
				if (null != TargetPosition) {
					d.put("DeliveryPositionName", TargetPosition.getString("FNAME"));
				} else {
					d.put("DeliveryPositionName", "");
				}
			} else {
				d.put("DeliveryPositionName", "");
				d.put("DeliveryPosition", "");
			}

			if (Tools.notEmpty(d.getString("TargetPosition"))) {
				PageData locationParam = new PageData();
				locationParam.put("WH_LOCATION_ID", d.getString("TargetPosition"));
				PageData TargetPosition = WH_LocationService.findById(locationParam);
				if (null != TargetPosition) {
					d.put("TargetPositionName", TargetPosition.getString("FNAME"));
				} else {
					d.put("TargetPositionName", "");
				}
			} else {
				d.put("TargetPositionName", "");
				d.put("TargetPosition", "");
			}

			if (Tools.notEmpty(d.getString("SProp"))) {
				PageData spropParam = new PageData();
				spropParam.put("MAT_AUXILIARY_ID", d.getString("SProp"));
				PageData SProp = mat_auxiliaryService.findById(spropParam);
				if (null != SProp) {
					d.put("SPropName", SProp.getString("MAT_AUXILIARY_NAME"));
				} else {
					d.put("SPropName", "");
				}
			} else {
				d.put("SPropName", "");
			}
			if (Tools.notEmpty(d.getString("SPropKey"))) {
				// 根据code获取 物料辅助属性值name
				PageData pageData = new PageData();
				pageData.put("MAT_AUXILIARYMX_ID", d.getString("SPropKey"));

				PageData mx = mat_auxiliarymxService.findById(pageData);
				if (null != mx) {
					d.put("SPropKeyName", mx.getString("MAT_AUXILIARYMX_NAME"));
				} else {
					d.put("SPropKeyName", "");
				}
			} else {
				d.put("SPropKeyName", "");
			}

		}
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据物料需求获取实时库存
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStockSum")
	@ResponseBody
	public Object getStockSum() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData d = new PageData();
		d = this.getPageData();
		String MaterialNum = d.getString("MaterialNum");
		List<PageData> listByMatCode = mat_basicService.getListByMatCode(MaterialNum);
		if (CollectionUtil.isNotEmpty(listByMatCode)) {
			PageData pdKC = new PageData();
			pdKC.put("WarehouseID", d.getString("DeliveryWarehouse"));
			pdKC.put("ItemID", listByMatCode.get(0).getString("MAT_BASIC_ID"));
			pdKC.put("MaterialSPropKey", d.getString("SPropKey"));
			PageData pdStockSum = StockService.getSum(pdKC);// 即时库存
			String stockSum = "";
			if (pdStockSum != null) {
				stockSum = pdStockSum.get("stockSum").toString();
			} else {
				stockSum = "0";
			}
			map.put("stockSum", stockSum);
			map.put("result", errInfo);
			return map;
		}
		map.put("stockSum", "0");
		map.put("result", errInfo);
		return map;

	}

	/**
	 * 计划工单明细-物料列表-采购订单添加明细列表
	 * 
	 * @author s
	 * @date 2021-2-22
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPurchaseMat")
	@ResponseBody
	public Object listPurchaseMat() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum"); // 销售订单编号
		if (Tools.notEmpty(KEYWORDS_OrderNum)) {
			pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		}
		pd = this.getPageData();
		String KEYWORDS_WorkOrderNum = pd.getString("KEYWORDS_WorkOrderNum"); // 计划工单编号
		if (Tools.notEmpty(KEYWORDS_WorkOrderNum)) {
			pd.put("KEYWORDS_WorkOrderNum", KEYWORDS_WorkOrderNum.trim());
		}
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum"); // 物料编号
		if (Tools.notEmpty(KEYWORDS_MaterialNum)) {
			pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		}
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName"); // 物料名称
		if (Tools.notEmpty(KEYWORDS_MaterialName)) {
			pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		}
		List<PageData> varList = PlanningWorkOrderService.listPurchaseMat(pd);
		for (int i = 0; i < varList.size(); i++) {
			PageData pdKC = new PageData();
			pdKC.put("WarehouseID", varList.get(i).getString("DeliveryWarehouse"));
			pdKC.put("ItemID", varList.get(i).getString("MaterialID"));
			pdKC.put("MaterialSPropKey", varList.get(i).getString("SPropKey"));
			PageData pdStockSum = StockService.getSum(pdKC);// 即时库存
			if (Tools.notEmpty(pdKC.getString("WarehouseID")) && Tools.notEmpty(pdKC.getString("ItemID"))) {
				if (pdStockSum != null) {
					varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
				} else {
					varList.get(i).put("stockSum", "0");
				}
			} else {
				varList.get(i).put("stockSum", "0");
			}
		}
		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", StaffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "采购订单明细-计划工单列表");// 功能项
		pdOp.put("OperationType", "查询");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", pd.getString("PurchaseID"));// 删改数据ID
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 计划工单明细-物料列表
	 * 
	 * @author 管悦
	 * @date 2020-11-19
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllMat")
	@ResponseBody
	public Object listAllMat() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum"); // 销售订单编号
		if (Tools.notEmpty(KEYWORDS_OrderNum)) {
			pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		}
		pd = this.getPageData();
		String KEYWORDS_WorkOrderNum = pd.getString("KEYWORDS_WorkOrderNum"); // 计划工单编号
		if (Tools.notEmpty(KEYWORDS_WorkOrderNum)) {
			pd.put("KEYWORDS_WorkOrderNum", KEYWORDS_WorkOrderNum.trim());
		}
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum"); // 物料编号
		if (Tools.notEmpty(KEYWORDS_MaterialNum)) {
			pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		}
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName"); // 物料名称
		if (Tools.notEmpty(KEYWORDS_MaterialName)) {
			pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		}
		List<PageData> varList = PlanningWorkOrderService.listAllMat(pd);
		for (int i = 0; i < varList.size(); i++) {
			PageData pdKC = new PageData();
			pdKC.put("WarehouseID", varList.get(i).getString("DeliveryWarehouse"));
			pdKC.put("ItemID", varList.get(i).getString("MaterialID"));
			pdKC.put("MaterialSPropKey", varList.get(i).getString("SPropKey"));
			PageData pdStockSum = StockService.getSum(pdKC);// 即时库存
			if (Tools.notEmpty(pdKC.getString("WarehouseID")) && Tools.notEmpty(pdKC.getString("ItemID"))) {
				if (pdStockSum != null) {
					varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
				} else {
					varList.get(i).put("stockSum", "0");
				}
			} else {
				varList.get(i).put("stockSum", "0");
			}
		}
		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", StaffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID

		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "采购订单明细-计划工单列表");// 功能项
		pdOp.put("OperationType", "查询");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", pd.getString("PurchaseID"));// 删改数据ID
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	// 复制计划工单
	private void copyWorkOrder(PageData pg) throws Exception {
		// 根据计划工单id获取信息
		PageData findById = PlanningWorkOrderService.findById(pg);
		if (null == findById) {
			throw new RuntimeException("计划工单数据不存在");
		}
		// 计划工单源 id
		String originDataId = findById.getString("PlanningWorkOrder_ID");
		// 复制一份并 赋值 新id
		String PlanningWorkOrder_ID = UuidUtil.get32UUID();
		// 重新赋值id 作为新数据复制一份
		findById.put("PlanningWorkOrder_ID", PlanningWorkOrder_ID);
		findById.put("WorkOrderNum", pg.getString("WorkOrderNum"));
		findById.put("BatchNum", pg.getString("BatchNum"));
		findById.put("WorkOrderCreateTime", Tools.date2Str(new Date()));
		// 保存到数据库中
		PlanningWorkOrderService.save(findById);

		// 根据源id 查询 工艺工单工序实例 也需要复制一份
		List<PageData> listProcessWorkOrderExampleByPlanningWorkOrderID = PlanningWorkOrderService
				.getListProcessWorkOrderExampleByPlanningWorkOrderID(originDataId);
		if (CollectionUtil.isNotEmpty(listProcessWorkOrderExampleByPlanningWorkOrderID)) {
			String OrderNum = pg.getString("OrderNum");
			PageData po = new PageData();
			po.put("OrderNum", OrderNum);
			List<PageData> result = ProcessWorkOrderExampleService.listAll(po);
			PageData lastData = result.get(result.size() - 1);
			Integer SerialNum = Integer.valueOf(String.valueOf(lastData.get("SerialNum")));
			for (int i = 0; i < listProcessWorkOrderExampleByPlanningWorkOrderID.size(); i++) {
				PageData pageData = listProcessWorkOrderExampleByPlanningWorkOrderID.get(i);
				// 创建要复制单子id 更新关联id
				String ProcessWorkOrderExample_ID = UuidUtil.get32UUID();
				pageData.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);
				pageData.put("PlanningWorkOrderID", PlanningWorkOrder_ID);
				pageData.put("BatchNum", pg.getString("BatchNum"));
				pageData.put("SerialNum", String.valueOf(SerialNum + i + 1));
				pageData.put("TaskNum", pageData.getString("OrderNum") + "~"
						+ String.format("%03d", Integer.valueOf(SerialNum + i + 1)));
				findById.put("FCreateTime", Tools.date2Str(new Date()));
				// 复制单子 插入工艺工单工序实例
				PlanningWorkOrderService.insertProcessWorkOrderExample(pageData);
				List<PageData> listByProcessWorkOrderExampleID = PlanningWorkOrderService
						.getListWorkorderProcessIOExampleByPlanningWorkOrderID(originDataId);
				for (PageData pageData2 : listByProcessWorkOrderExampleID) {
					// 创建要复制单子id 更新关联id
					pageData2.put("WorkorderProcessIOExample_ID", UuidUtil.get32UUID());
					pageData2.put("ProcessWorkOrderExampleID", ProcessWorkOrderExample_ID);
					// 复制单子 插入物料需求单
					PlanningWorkOrderService.insertWorkorderProcessIOExample(pageData2);
				}
			}

		}

		// 根据源id 查询 物料需求单 也需要复制一份
		List<PageData> listMaterialRequirementByPlanningWorkOrderID = PlanningWorkOrderService
				.getListMaterialRequirementByPlanningWorkOrderID(originDataId);
		for (PageData pageData : listMaterialRequirementByPlanningWorkOrderID) {
			// 复制出一份 将物料需求单 新增 赋值主键
			pageData.put("MaterialRequirement_ID", UuidUtil.get32UUID());
			// 关联新的计划工单
			pageData.put("PlanningWorkOrderID", PlanningWorkOrder_ID);
			pageData.put("BatchNum", pg.getString("BatchNum"));
			// 保存
			MaterialRequirementService.save(pageData);
		}
	}

	/**
	 * 返工任务创建
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/create/remade")
	@ResponseBody
	public Object createRemade() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String OriginTaskID = pd.getString("OriginTaskID");
		PageData orginParam = new PageData();
		orginParam.put("ProcessWorkOrderExample_ID", OriginTaskID);
		PageData originTask = ProcessWorkOrderExampleService.findById(orginParam);
		if (null != originTask) {
			// 将原来的任务复制一份，将状态改为 未执行，类型改为返工。加上源任务ID。赋值初始化参数
			originTask.put("OriginTaskID", OriginTaskID);
			originTask.put("TaskType", "2");
			originTask.put("ProcessWorkOrderExample_ID", this.get32UUID());
			originTask.put("EXECUTE_TIME", "");
			originTask.put("NODE_ID", "");
			originTask.put("FRUN_STATE", "");
			originTask.put("ActualBeginTime", "");
			originTask.put("ActualEndTime", "");
			originTask.put("FStatus", "待执行");
			originTask.put("FCreateTime", Tools.date2Str(new Date()));
			PageData pageData = new PageData();
			pageData.put("PlanningWorkOrderID", originTask.get("PlanningWorkOrderID"));
			List<PageData> listAll = ProcessWorkOrderExampleService.listAll(pageData);
			int size = listAll.size();
			originTask.put("SerialNum", String.valueOf((size + 1)));
			String TaskNum = originTask.getString("TaskNum");
			TaskNum = TaskNum.substring(0, TaskNum.length() - 3) + "~" + String.format("%02d", size + 1);
			originTask.put("TaskNum", TaskNum);
			PlanningWorkOrderService.insertProcessWorkOrderExample(originTask);
			// 复制 投入产出数据
			List<PageData> listByProcessWorkOrderExampleID = WorkorderProcessIOExampleService
					.listByProcessWorkOrderExampleID(OriginTaskID);
			if (CollectionUtil.isNotEmpty(listAll)) {
				for (int i = 0; i < listByProcessWorkOrderExampleID.size(); i++) {
					PageData ioData = listByProcessWorkOrderExampleID.get(i);
					ioData.put("WorkorderProcessIOExample_ID", this.get32UUID());
					ioData.put("ProcessWorkOrderExampleID", originTask.getString("ProcessWorkOrderExample_ID"));
					WorkorderProcessIOExampleService.save(ioData);
					if ("投入".equals(ioData.getString("TType"))) {
						String MaterialID = ioData.getString("MaterialID");
						BigDecimal PlannedQuantity = new BigDecimal(String.valueOf(ioData.get("PlannedQuantity")));
						PageData materialData = new PageData();
						materialData.put("MAT_BASIC_ID", MaterialID);
						String MaterialNum = "";
						String MaterialName = "";
						String SProp = "80108fa272884119b09eab03a78d3cc5";
						String SPropKey = "PUB";
						String subPlanningWorkOrderId = originTask.getString("PlanningWorkOrderID");
						PageData subData = new PageData();
						subData.put("PlanningWorkOrder_ID", subPlanningWorkOrderId);
						PageData subPlan = PlanningWorkOrderService.findById(subData);

						if (null != subPlan) {
							// 获取计划工单号对应的 物料辅助属性值
							PageData mat_auxiliarymxParam = new PageData();
							mat_auxiliarymxParam.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
							mat_auxiliarymxParam.put("MAT_AUXILIARYMX_CODE", subPlan.getString("MasterWorkOrderNum"));
							List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
									.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(mat_auxiliarymxParam);
							if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
								SPropKey = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
										.getString("MAT_AUXILIARYMX_ID");
								SProp = "2540539e45324232a50bde60ac2951d3";
							}
						}
						BigDecimal IssuedQuantity = new BigDecimal("0");
						String DemandTime = Tools.date2Str(new Date());
						String WorkOrderNum = subPlan.getString("WorkOrderNum");
						String DateBatch = Tools.date2Str(new Date(), "yyyy-MM-dd");
						String TType = "投入";
						String FStatus = "创建";
						String OccupationOfInventory = "";
						String FMakeBillsPersoID = "";
						PageData pdOp = new PageData();
						String name = Jurisdiction.getName();
						pdOp.put("FNAME", name);
						FMakeBillsPersoID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
						String FMakeBillsTime = Tools.date2Str(new Date());
						String PushDownTransferIF = "N";
						String PushDownPurchaseIF = "N";
						BigDecimal PurchaseCount = new BigDecimal("0");
						String BatchNum = subPlan.getString("BatchNum");
						String MRCode = CodingRulesService.getRuleNumByRuleType("WLXQ").toString();
						;
						String DeliveryPosition = "";
						String TargetWarehouse = "";
						PageData matBasic = mat_basicService.findById(materialData);
						if (null != matBasic) {
							MaterialNum = matBasic.getString("MAT_CODE");
							MaterialName = matBasic.getString("MAT_NAME");
						}
						// 插入物料需求单数据
						PageData mrData = new PageData();
						mrData.put("MaterialRequirement_ID", this.get32UUID());
						mrData.put("RowNum", i + 1);
						mrData.put("RowClose", "N");
						mrData.put("PlanningWorkOrderID", subPlanningWorkOrderId);
						mrData.put("MaterialNum", MaterialNum);
						mrData.put("MaterialName", MaterialName);
						mrData.put("SProp", SProp);
						mrData.put("SPropKey", SPropKey);
						mrData.put("DemandCount", PlannedQuantity.doubleValue());
						mrData.put("IssuedQuantity", IssuedQuantity);
						mrData.put("DemandTime", DemandTime);
						mrData.put("WorkOrderNum", WorkOrderNum);
						mrData.put("DateBatch", DateBatch);
						mrData.put("TType", TType);
						mrData.put("FStatus", FStatus);
						mrData.put("OccupationOfInventory", OccupationOfInventory);
						mrData.put("FMakeBillsPersoID", FMakeBillsPersoID);
						mrData.put("FMakeBillsTime", FMakeBillsTime);
						mrData.put("PushDownTransferIF", PushDownTransferIF);
						mrData.put("PushDownPurchaseIF", PushDownPurchaseIF);
						mrData.put("PurchaseCount", PurchaseCount);
						mrData.put("BatchNum", BatchNum);
						mrData.put("MRCode", MRCode);
						mrData.put("DeliveryPosition", DeliveryPosition);
						mrData.put("TargetWarehouse", TargetWarehouse);
						MaterialRequirementService.save(mrData);
					}
				}
			}
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据计划工单id获取投入产出列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getIOListByPlanningWorkOrderID")
	@ResponseBody
	public Object getIOListByPlanningWorkOrderID() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> listWorkorderProcessIOExampleByPlanningWorkOrderID = PlanningWorkOrderService
				.getListWorkorderProcessIOExampleByPlanningWorkOrderID(pd.getString("PlanningWorkOrder_ID"));
		List<PageData> ioList = Lists.newArrayList();
		// 根据计划工单id获取投入产出数据列表
		for (PageData io : listWorkorderProcessIOExampleByPlanningWorkOrderID) {
			PageData ioData = new PageData();
			String FormulaQuantity = String.valueOf(io.get("FormulaQuantity"));
			String PlannedQuantity = String.valueOf(io.get("PlannedQuantity"));
			String MaterialID = String.valueOf(io.get("MaterialID"));
			String SubstituteMaterialID = String.valueOf(io.get("SubstituteMaterialID"));
			ioData.put("MAT_BASIC_ID", MaterialID);
			try {
				PageData findById = mat_basicService.findById(ioData);
				String MAT_NAME = String.valueOf(findById.get("MAT_NAME"));
				String MAT_SPECS = String.valueOf(findById.get("MAT_SPECS"));
				String MAT_CODE = String.valueOf(findById.get("MAT_CODE"));
				ioData.put("MAT_NAME", MAT_NAME);
				ioData.put("MAT_CODE", MAT_CODE);
				if (Tools.notEmpty(SubstituteMaterialID)) {
					ioData.put("MAT_BASIC_ID", SubstituteMaterialID);
					findById = mat_basicService.findById(ioData);
					String SUB_MAT_NAME = String.valueOf(findById.get("MAT_NAME"));
					ioData.put("SUB_MAT_NAME", SUB_MAT_NAME);
				}
				// 带出物料规格
				ioData.put("TType", io.getString("TType"));
				ioData.put("MAT_SPECS", MAT_SPECS);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String ProcessWorkOrderExampleID = String.valueOf(io.get("ProcessWorkOrderExampleID"));
			PageData pageData1 = new PageData();
			pageData1.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExampleID);
			// 带出工序信息
			PageData findById = ProcessWorkOrderExampleService.findById(pageData1);
			String WP = findById.getString("WP");
			ioData.put("WP", WP);
			String WPName = findById.getString("WPName");
			ioData.put("WPName", WPName);
			ioData.put("FormulaQuantity", FormulaQuantity);
			ioData.put("PlannedQuantity", PlannedQuantity);
			ioData.put("WorkOrderNum", pd.getString("WorkOrderNum"));
			ioData.put("BatchNum", pd.getString("BatchNum"));
			ioList.add(ioData);
		}
		// 返回数据
		map.put("ioList", ioList);
		map.put("result", errInfo);
		return map;
	}

	/*
	 * 下发
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/issue")
	@ResponseBody
	public Object issue() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData subPlan = PlanningWorkOrderService.findById(pd);
		subPlan.put("FStatus", "执行中");
		subPlan.put("DistributionProgress", "已下发");
		subPlan.put("ScheduleSchedule", "已排程");
		subPlan.put("ActualBeginTime", Tools.date2Str(new Date()));
		PlanningWorkOrderService.edit(subPlan);
		// PageData pageData2 = new PageData();
		// pageData2.put("MASTERPLAN_ID",
		// subPlan.getString("MasterWorkOrder_ID"));
		// pageData2.put("SUBPLAN_ID",
		// subPlan.getString("PlanningWorkOrder_ID"));
		// List<PageData> findStartTask =
		// SecondStageService.findStartTaskNoStandard(pageData2);
		// for (PageData taskFlow : findStartTask) {
		PageData po = new PageData();
		// po.put("NODE_ID", taskFlow.getString("NODE_ID"));
		po.put("PlanningWorkOrderID", subPlan.getString("PlanningWorkOrder_ID"));
		List<PageData> pwoeList = ProcessWorkOrderExampleService.listAll(po);
		for (PageData task : pwoeList) {
			task.put("FStatus", "待执行");
			ProcessWorkOrderExampleService.edit(task);

			// 生产工单下发-消息推送工位人员
			PageData findById = processWorkOrderExampleService.findById(task);
			String FFTYPE = pd.getString("FFTYPE");
			if (null != FFTYPE && FFTYPE.equals("生产任务")) {
				List<PageData> staffList = workstationpersonrelService.listAllByName(findById);
				String ReceivingAuthority = ",";
				for (PageData pdStaffx : staffList) {
					ReceivingAuthority += pdStaffx.getString("staffno");
					ReceivingAuthority = ReceivingAuthority + ",";
					// 手机app-极光推送
					String UserName = pdStaffx.getString("staffno");
					pd.put("UserName", UserName);
					PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
					if (pdf != null) {
						String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
						String notification_title = "工作通知";
						String msg_title = "生产任务下发";
						String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
								+ findById.getString("WPName") + "，产出物料为" + findById.getString("ProcessIMaterielCode")
								+ "的生产任务已下发";
						if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title, msg_title,
								msg_content, "") == 1) {
							// errInfo="success";
						} else {
							// errInfo="error";
						}
					}

				}
				// 消息推送
				PageData pdNotice = new PageData();

				// 跳转页面
				pdNotice.put("AccessURL", "../../../views/pp/taskRun/run_task_listAll.html");//
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("WPName") + "，产出物料为"
								+ findById.getString("ProcessIMaterielCode") + "的生产任务已下发");// 消息正文
				pdNotice.put("FTitle", "生产任务下发");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "生产任务下发");// 数据来源
				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("Report_Value", "");// 手机app
				noticeService.save(pdNotice);
			}

			// }
			// taskFlow.put("EXECUTE_STATE", "执行中");
			// NEWPLAN_BOMService.edit(taskFlow);

		}

		pd = this.getPageData();
		map.put("result", errInfo);
		return map;

	}

	/**
	 * 查看所有锅次任务列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getWoklistPage")
	@ResponseBody
	public Object getWokListPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = ProcessWorkOrderExampleService.getWoklistPage(page);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 图纸列表
	 * 
	 * @return v1吴双双 2021-05-27
	 * @throws Exception
	 */
	@RequestMapping(value = "/attachList")
	@ResponseBody
	public Object AttachList() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		List<PageData> attachList = Lists.newArrayList(); // 空
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String WorkOrderNum = pd.getString("WorkOrderNum");// 获取柜体编号
			if (StringUtil.isEmpty(WorkOrderNum)) { // 如果柜体编号为空，则没有数据
				map.put("attachList", attachList);
				map.put("pd", pd);
				map.put("result", errInfo);
				return map;
			}
			// System.out.println(WorkOrderNum);
			// 1、根据柜体编号查询柜体数据，获取主键
			PageData cadParam = new PageData();
			cadParam.put("Cabinet_No", WorkOrderNum);
			List<PageData> listAll = Cabinet_Assembly_DetailService.listAll(cadParam);// 根据柜体编号查询柜体数据
			if (CollectionUtils.isEmpty(listAll)) { // 如果柜体数据为空，则没有数据
				map.put("attachList", attachList);
				map.put("pd", pd);
				map.put("result", errInfo);
				return map;
			}
			PageData cadInfo = listAll.get(0);// 柜体编号唯一，所以只有一条数据
			String Cabinet_Assembly_Detail_ID = cadInfo.getString("Cabinet_Assembly_Detail_ID");// 获取主键
			// System.out.println(Cabinet_Assembly_Detail_ID);
			// 2、根据主键去附件表获取和柜体数据表主键相同且状态为使用的附件数据
			PageData assetParam = new PageData();
			assetParam.put("AssociationID", Cabinet_Assembly_Detail_ID);// 主键
			assetParam.put("FStatus", "使用");// 状态
			List<PageData> attachmentsetList = attachmentsetService.listAll(assetParam);// 根据主键去附件表获取和柜体数据表
			if (CollectionUtils.isEmpty(attachmentsetList)) { // 没有和柜体数据表主键相同且状态为使用的附件数据
				map.put("attachList", attachList);
				map.put("pd", pd);
				map.put("result", errInfo);
				return map;
			}
			map.put("attachList", attachmentsetList);
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("attachList", attachList);
			map.put("msg", e.getMessage());
			map.put("result", "error");
			return map;
		}
	}

	/**
	 * 根据柜体编号查询柜体数据，获取主键
	 * 
	 * @return v1吴双双 2021-05-27
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCadIDByWorkOrderNum")
	@ResponseBody
	public Object getCadIDByWorkOrderNum() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		List<PageData> attachList = Lists.newArrayList();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String WorkOrderNum = pd.getString("WorkOrderNum");// 获取柜体编号
			if (StringUtil.isEmpty(WorkOrderNum)) {// 如果柜体数据为空，则没有数据
				map.put("AssociationID", "");
				map.put("pd", pd);
				map.put("result", errInfo);
				return map;
			}
			// System.out.println(WorkOrderNum);
			// 根据柜体编号查询柜体数据，获取主键
			PageData cadParam = new PageData();
			cadParam.put("Cabinet_No", WorkOrderNum);
			List<PageData> listAll = Cabinet_Assembly_DetailService.listAll(cadParam);// 根据柜体编号查询柜体数据
			if (CollectionUtils.isEmpty(listAll)) {
				map.put("attachList", attachList);
				map.put("pd", pd);
				map.put("result", errInfo);
				return map;
			}
			PageData cadInfo = listAll.get(0);// 柜体编号唯一，所以只有一条数据
			String Cabinet_Assembly_Detail_ID = cadInfo.getString("Cabinet_Assembly_Detail_ID");// 获取主键
			// System.out.println(Cabinet_Assembly_Detail_ID);
			map.put("AssociationID", Cabinet_Assembly_Detail_ID);
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("AssociationID", "");
			map.put("msg", e.getMessage());
			map.put("result", "error");
			return map;
		}
	}

	/**
	 * 生产看板-工序列表，入参：工序名称；未结束的任务
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllGX")
	@ResponseBody
	public Object listAllGX(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllGX(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 生产看板-工序列表角标，入参：工序名称；未结束的任务
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllGXNUM")
	@ResponseBody
	public Object listAllGXNUM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		PageData pd = new PageData();
		pd = this.getPageData();
		String BeginTime = pd.getString("BeginTime");
		String EndTime = pd.getString("EndTime");
		if (BeginTime == null || "".equals(BeginTime)) {
			// 获取前月的第一天
			Calendar cal_1 = Calendar.getInstance();// 获取当前日期
			cal_1.add(Calendar.MONTH, 0);
			cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			BeginTime = format.format(cal_1.getTime());
		}
		if (EndTime == null || "".equals(EndTime)) {
			// 获取前月的第一天
			// 获取前月的最后一天
			Calendar ca = Calendar.getInstance();
			ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
			EndTime = format.format(ca.getTime());
		}
		pd.put("EndTime", EndTime);
		pd.put("BeginTime", BeginTime);
		pd.put("FFDEPT", "生产部");
		PageData pdDept = departmentService.findByName(pd);
		if (pdDept != null) {
			String depts = departmentService.getDEPARTMENT_IDSSS(pdDept.getString("DEPARTMENT_ID"));
			pd.put("depts", depts);
		} else {
			pd.put("depts", "('')");
		}
		PageData pdNum = PlanningWorkOrderService.getAllNum(pd);// 工序执行状态数
		List<PageData> varList = PlanningWorkOrderService.listAllGXNUM(pd);// 工序列表角标
		List<PageData> varListBT = PlanningWorkOrderService.listAllBT(pd);// toc饼图
		map.put("varList", varList);
		map.put("varListBT", varListBT);
		map.put("pdNum", pdNum);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 项目看板-项目预警列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllXM")
	@ResponseBody
	public Object listAllXM(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllXM(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 项目看板-历史记录列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllXMHis")
	@ResponseBody
	public Object listAllXMHis(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllXMHis(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 项目看板-数字和饼图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllXMNUM")
	@ResponseBody
	public Object listAllXMNUM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData pdNum = PlanningWorkOrderService.getAllXMNUM(pd);// 数字
		List<PageData> varListBT = PlanningWorkOrderService.listAllBTXM(pd);// toc饼图
		map.put("varListBT", varListBT);
		map.put("pdNum", pdNum);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 设计看板-数字和饼图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllSJNUM")
	@ResponseBody
	public Object listAllSJNUM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData pdNum = PlanningWorkOrderService.listAllSJNUM(pd);// 数字
		List<PageData> varListBT = PlanningWorkOrderService.listAllBTSJ(pd);// toc饼图
		List<PageData> varListBT1 = PlanningWorkOrderService.listAllBTSJX(pd);// toc饼图
		map.put("varListBT", varListBT);
		map.put("varListBT1", varListBT1);
		map.put("pdNum", pdNum);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 设计看板-柜体预警列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllSJ")
	@ResponseBody
	public Object listAllSJ(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllSJ(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 设计看板-历史记录列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllSJHis")
	@ResponseBody
	public Object listAllSJHis(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllSJHis(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 检验看板-数字和饼图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllJYNUM")
	@ResponseBody
	public Object listAllJYNUM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData pdNum = PlanningWorkOrderService.listAllJYNUM(pd);// 数字
		map.put("pdNum", pdNum);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 检验看板-今日任务列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllJY")
	@ResponseBody
	public Object listAllJY(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllJY(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 检验看板-历史记录列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllJYHis")
	@ResponseBody
	public Object listAllJYHis(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllJYHis(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 装配看板-项目预警列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllZP")
	@ResponseBody
	public Object listAllZP(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData stationPersonRel = new PageData();
		PageData staffParam = new PageData();
		String USERNAME = Jurisdiction.getUsername();
		staffParam.put("USERNAME", USERNAME);
		PageData staff = staffService.findById(staffParam);
		if (null != staff) {
			// 根据staff id 获取 生产任务 状态为未执行的
			String STAFF_ID = staff.getString("STAFF_ID");
			stationPersonRel.put("PersonId", STAFF_ID);
			List<PageData> WorkStationPersonRelList = WorkStationPersonRelService.listAll(stationPersonRel);
			List<String> stationIDList = Lists.newArrayList();
			for (PageData WorkStationPersonRel : WorkStationPersonRelList) {
				stationIDList.add(WorkStationPersonRel.getString("WorkstationID"));
			}
			if (stationIDList.size() > 0) {
				String item = String.join("','", stationIDList);
				pd.put("item", "('" + item + "')");
			} else {
				pd.put("item", "('')");
			}
		} else {
			pd.put("item", "('')");
		}

		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllZP(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 装配看板-待执行、执行中列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllZPRUN")
	@ResponseBody
	public Object listAllZPRUN(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData stationPersonRel = new PageData();
		PageData staffParam = new PageData();
		String USERNAME = Jurisdiction.getUsername();
		staffParam.put("USERNAME", USERNAME);
		PageData staff = staffService.findById(staffParam);
		if (null != staff) {
			// 根据staff id 获取 生产任务 状态为未执行的
			String STAFF_ID = staff.getString("STAFF_ID");
			stationPersonRel.put("PersonId", STAFF_ID);
			List<PageData> WorkStationPersonRelList = WorkStationPersonRelService.listAll(stationPersonRel);
			List<String> stationIDList = Lists.newArrayList();
			for (PageData WorkStationPersonRel : WorkStationPersonRelList) {
				stationIDList.add(WorkStationPersonRel.getString("WorkstationID"));
			}
			if (stationIDList.size() > 0) {
				String item = String.join("','", stationIDList);
				pd.put("item", "('" + item + "')");
			} else {
				pd.put("item", "('')");
			}
		} else {
			pd.put("item", "('')");
		}

		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllZPRUN(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 装配看板-历史记录列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllZPHIS")
	@ResponseBody
	public Object listAllZPHIS(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData stationPersonRel = new PageData();
		PageData staffParam = new PageData();
		String USERNAME = Jurisdiction.getUsername();
		staffParam.put("USERNAME", USERNAME);
		PageData staff = staffService.findById(staffParam);
		if (null != staff) {
			// 根据staff id 获取 生产任务 状态为未执行的
			String STAFF_ID = staff.getString("STAFF_ID");
			stationPersonRel.put("PersonId", STAFF_ID);
			List<PageData> WorkStationPersonRelList = WorkStationPersonRelService.listAll(stationPersonRel);
			List<String> stationIDList = Lists.newArrayList();
			for (PageData WorkStationPersonRel : WorkStationPersonRelList) {
				stationIDList.add(WorkStationPersonRel.getString("WorkstationID"));
			}
			if (stationIDList.size() > 0) {
				String item = String.join("','", stationIDList);
				pd.put("item", "('" + item + "')");
			} else {
				pd.put("item", "('')");
			}
		} else {
			pd.put("item", "('')");
		}

		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllZPHIS(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 装配看板-数字和饼图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllZPNUM")
	@ResponseBody
	public Object listAllZPNUM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData stationPersonRel = new PageData();
		PageData staffParam = new PageData();
		String USERNAME = Jurisdiction.getUsername();
		staffParam.put("USERNAME", USERNAME);
		PageData staff = staffService.findById(staffParam);
		if (null != staff) {
			// 根据staff id 获取 生产任务 状态为未执行的
			String STAFF_ID = staff.getString("STAFF_ID");
			stationPersonRel.put("PersonId", STAFF_ID);
			List<PageData> WorkStationPersonRelList = WorkStationPersonRelService.listAll(stationPersonRel);
			List<String> stationIDList = Lists.newArrayList();
			for (PageData WorkStationPersonRel : WorkStationPersonRelList) {
				stationIDList.add(WorkStationPersonRel.getString("WorkstationID"));
			}
			if (stationIDList.size() > 0) {
				String item = String.join("','", stationIDList);
				pd.put("item", "('" + item + "')");
			} else {
				pd.put("item", "('')");
			}
		} else {
			pd.put("item", "('')");
		}

		PageData pdNum = PlanningWorkOrderService.listAllZPNUM(pd);// 数字
		List<PageData> varListBT = PlanningWorkOrderService.listAllBTZP(pd);// toc饼图
		map.put("varListBT", varListBT);
		map.put("pdNum", pdNum);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 采购看板-数字和饼图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllCGNUM")
	@ResponseBody
	public Object listAllCGNUM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdNum = PlanningWorkOrderService.listAllCGNUM(pd);// 工序执行状态数
		List<PageData> varListBT = PlanningWorkOrderService.listAllBTCG(pd);// toc饼图
		map.put("varListBT", varListBT);
		map.put("pdNum", pdNum);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 采购看板-柜体预警
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllCG")
	@ResponseBody
	public Object listAllCG(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllCG(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 采购看板-历史记录
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllCGHis")
	@ResponseBody
	public Object listAllCGHis(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllCGHis(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 库房看板-数字
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllKFNUM")
	@ResponseBody
	public Object listAllKFNUM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdNum = PlanningWorkOrderService.listAllKFNUM(pd);
		map.put("pdNum", pdNum);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 库房看板-请料列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllKF")
	@ResponseBody
	public Object listAllKF(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllKF(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 库房看板-齐套请料列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllKF1")
	@ResponseBody
	public Object listAllKF1(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllKF1(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 库房看板-不齐请料列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllKF2")
	@ResponseBody
	public Object listAllKF2(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllKF2(page);
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 生产看板-人员工时列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllGS")
	@ResponseBody
	public Object listAllGS(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String BeginTime = pd.getString("BeginTime");
		String EndTime = pd.getString("EndTime");
		if (BeginTime == null || "".equals(BeginTime)) {
			// 获取前月的第一天
			Calendar cal_1 = Calendar.getInstance();// 获取当前日期
			cal_1.add(Calendar.MONTH, 0);
			cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			BeginTime = format.format(cal_1.getTime());
		}
		if (EndTime == null || "".equals(EndTime)) {
			// 获取前月的第一天
			// 获取前月的最后一天
			Calendar ca = Calendar.getInstance();
			ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
			EndTime = format.format(ca.getTime());
		}
		pd.put("FFDEPT", "生产部");
		pd.put("EndTime", EndTime);
		pd.put("BeginTime", BeginTime);
		PageData pdDept = departmentService.findByName(pd);
		if (pdDept != null) {
			String depts = departmentService.getDEPARTMENT_IDSSS(pdDept.getString("DEPARTMENT_ID"));
			pd.put("depts", depts);
		} else {
			pd.put("depts", "('')");
		}
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listAllGS(page);
		List<PageData> varListx = PlanningWorkOrderService.listAllGXNUM(pd);// 工序列表角标
		map.put("varList", varList);
		map.put("result", errInfo);
		map.put("varListx", varListx);
		map.put("page", page);
		return map;
	}

	/**
	 * 生产看板-工单进度
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listSCPage")
	@ResponseBody
	public Object listSCPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 模糊查询条件
		// 订单号
		String OrderNum = pd.getString("OrderNum");
		if (Tools.notEmpty(OrderNum)) {
			pd.put("OrderNum", OrderNum.trim());
		}
		// 工单类型
		String WorkOrderType = pd.getString("WorkOrderType");
		if (Tools.notEmpty(WorkOrderType)) {
			pd.put("WorkOrderType", WorkOrderType.trim());
		}
		// 计划工单号
		String WorkOrderNum = pd.getString("WorkOrderNum");
		if (Tools.notEmpty(WorkOrderNum)) {
			pd.put("WorkOrderNum", WorkOrderNum.trim());
		}
		// 主计划工单id
		String MasterWorkOrder_ID = pd.getString("MasterWorkOrder_ID");
		if (Tools.notEmpty(MasterWorkOrder_ID)) {
			pd.put("MasterWorkOrder_ID", MasterWorkOrder_ID.trim());
		}
		// 主计划工单id
		String WorkOrderNumMaster = pd.getString("WorkOrderNumMaster");
		if (Tools.notEmpty(WorkOrderNumMaster)) {
			pd.put("WorkOrderNumMaster", WorkOrderNumMaster.trim());
		}
		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderService.listSCPage(page); // 列出PlanningWorkOrder列表
		for (PageData pds : varList) {

			// 计划员 和生产主管 拆分( ,yl, )变为数组传给前端
			String FPlannerID = Tools.notEmpty(pds.getString("FPlannerID")) ? pds.getString("FPlannerID") : "";
			pds.put("FPlannerID", Lists.newArrayList(FPlannerID.split(",yl,")));

			String FPlanner = Tools.notEmpty(pds.getString("FPlanner")) ? pds.getString("FPlanner") : "";
			pds.put("FPlanner", Lists.newArrayList(FPlanner.split(",yl,")));

			String ProductionSupervisorID = Tools.notEmpty(pds.getString("ProductionSupervisorID"))
					? pds.getString("ProductionSupervisorID")
					: "";
			pds.put("ProductionSupervisorID", Lists.newArrayList(ProductionSupervisorID.split(",yl,")));

			String ProductionSupervisorName = Tools.notEmpty(pds.getString("ProductionSupervisorName"))
					? pds.getString("ProductionSupervisorName")
					: "";
			pds.put("ProductionSupervisorName", Lists.newArrayList(ProductionSupervisorName.split(",yl,")));
			String FCustomer = Tools.notEmpty(pds.getString("FCustomer")) ? pds.getString("FCustomer") : "";
			if (!"".equals(FCustomer)) {
				PageData pdcustomer = new PageData();
				pdcustomer.put("Customer_ID", FCustomer);
				PageData Customer = CustomerService.findById(pdcustomer);
				if (null != Customer) {
					FCustomer = Customer.getString("FName");
					pds.put("FCustomer", FCustomer);
				}
			}
		}
		map.put("page", page);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 生产看板-返修任务
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ProcessWorkOrderExampleListSC")
	@ResponseBody
	public Object ProcessWorkOrderExampleListSC(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 模糊查询条件
		// 订单号
		String OrderNum = pd.getString("OrderNum");
		if (Tools.notEmpty(OrderNum)) {
			pd.put("OrderNum", OrderNum.trim());
		}
		// 批号
		String BatchNum = pd.getString("BatchNum");
		if (Tools.notEmpty(BatchNum)) {
			pd.put("BatchNum", BatchNum.trim());
		}
		// 任务编号
		String TaskNum = pd.getString("TaskNum");
		if (Tools.notEmpty(TaskNum)) {
			pd.put("TaskNum", TaskNum.trim());
		}
		// 工序
		String WP = pd.getString("WP");
		if (Tools.notEmpty(WP)) {
			pd.put("WP", WP.trim());
		}
		// 产出物料
		String ProcessIMateriel = pd.getString("ProcessIMateriel");
		if (Tools.notEmpty(ProcessIMateriel)) {
			pd.put("ProcessIMateriel", ProcessIMateriel.trim());
		}
		// 产出物料编码
		String ProcessIMaterielCode = pd.getString("ProcessIMaterielCode");
		if (Tools.notEmpty(ProcessIMaterielCode)) {
			pd.put("ProcessIMaterielCode", ProcessIMaterielCode.trim());
		}
		// 工位
		String FStation = pd.getString("FStation");
		if (Tools.notEmpty(FStation)) {
			pd.put("FStation", FStation.trim());
		}
		// 执行人
		String ExecutorID = pd.getString("ExecutorID");
		if (Tools.notEmpty(ExecutorID)) {
			pd.put("ExecutorID", ExecutorID.trim());
		}
		// 计划开始时间段-开始时间
		String PlannedBeginTimeBegin = pd.getString("PlannedBeginTimeBegin");
		if (Tools.notEmpty(PlannedBeginTimeBegin)) {
			pd.put("PlannedBeginTimeBegin", PlannedBeginTimeBegin.trim());
		}
		// 计划开始时间段-结束时间
		String PlannedBeginTimeEnd = pd.getString("PlannedBeginTimeEnd");
		if (Tools.notEmpty(PlannedBeginTimeEnd)) {
			pd.put("PlannedBeginTimeEnd", PlannedBeginTimeEnd.trim());
		}
		// 计划结束时间段-开始时间
		String PlannedEndTimeBegin = pd.getString("PlannedEndTimeBegin");
		if (Tools.notEmpty(PlannedEndTimeBegin)) {
			pd.put("PlannedEndTimeBegin", PlannedEndTimeBegin.trim());
		}
		// 计划结束时间段-结束时间
		String PlannedEndTimeEnd = pd.getString("PlannedEndTimeEnd");
		if (Tools.notEmpty(PlannedEndTimeEnd)) {
			pd.put("PlannedEndTimeEnd", PlannedEndTimeEnd.trim());
		}
		// 任务状态
		String FStatus = pd.getString("FStatus");
		if (Tools.notEmpty(FStatus)) {
			pd.put("FStatus", FStatus.trim());
		}
		// 实际开始时间段-开始时间
		String ActualBeginTimeBegin = pd.getString("ActualBeginTimeBegin");
		if (Tools.notEmpty(ActualBeginTimeBegin)) {
			pd.put("ActualBeginTimeBegin", ActualBeginTimeBegin.trim());
		}
		// 实际开始时间段-结束时间
		String ActualBeginTimeEnd = pd.getString("ActualBeginTimeEnd");
		if (Tools.notEmpty(ActualBeginTimeEnd)) {
			pd.put("ActualBeginTimeEnd", ActualBeginTimeEnd.trim());
		}
		// 实际结束时间段-开始时间
		String ActualEndTimeBegin = pd.getString("ActualEndTimeBegin");
		if (Tools.notEmpty(ActualEndTimeBegin)) {
			pd.put("ActualEndTimeBegin", ActualEndTimeBegin.trim());
		}
		// 实际结束时间段-结束时间
		String ActualEndTimeEnd = pd.getString("ActualEndTimeEnd");
		if (Tools.notEmpty(ActualEndTimeEnd)) {
			pd.put("ActualEndTimeEnd", ActualEndTimeEnd.trim());
		}

		// 计划工单优先级
		String FPriorityID = pd.getString("FPriorityID");
		if (Tools.notEmpty(FPriorityID)) {
			pd.put("FPriorityID", FPriorityID.trim());
		}

		// 任务类型 (1 生产任务、2 返工任务)
		String TaskType = pd.getString("TaskType");
		if (Tools.notEmpty(TaskType)) {
			pd.put("TaskType", TaskType.trim());
		}

		// 计划工单下发进度
		String DistributionProgress = pd.getString("DistributionProgress");
		if (Tools.notEmpty(DistributionProgress)) {
			pd.put("DistributionProgress", DistributionProgress.trim());
		}
		page.setPd(pd);
		List<PageData> varList = ProcessWorkOrderExampleService.listSC(page);
		for (PageData pageData : varList) {
			if (null == pageData) {
				continue;
			}
			if ("1".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "生产任务");
			} else if ("2".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "返工任务");
			}

			String WPID = pageData.getString("WP");
			PageData pData = new PageData();
			pData.put("ProcessDefinition_ID", WPID);
			String WPName = pageData.getString("WPName");
			if (null != WPName) {
				pageData.put("WP", WPName);
			}
			String station = "";
			String FStationIDS = pageData.getString("FStation");
			if (Tools.isEmpty(FStationIDS)) {
				continue;
			}
			String[] FStationIDSsplit = FStationIDS.split(",yl,");
			if (!"".equals(FStationIDSsplit[0])) {
				for (String stationId : FStationIDSsplit) {
					PageData stationData = new PageData();
					stationData.put("WC_STATION_ID", stationId);
					PageData WC_STATION = WC_StationService.findById(stationData);
					if (null != WC_STATION) {
						station += "," + WC_STATION.getString("FNAME");
					}
				}
			}
			station = station.substring(1, station.length());
			pageData.put("FStation", station);
			// 执行人 多个
			String ExecutorIDParam = Tools.notEmpty(pageData.getString("ExecutorID")) ? pageData.getString("ExecutorID")
					: "";
			if (!"".equals(ExecutorIDParam)) {
				List<String> ExecutorIDList = Lists.newArrayList(ExecutorIDParam.split(",yl,"));
				if (CollectionUtil.isNotEmpty(ExecutorIDList)) {
					String ExecutorID1 = "";
					for (String e : ExecutorIDList) {
						PageData pageData1 = new PageData();
						pageData1.put("STAFF_ID", e);
						try {
							PageData staffInfo = StaffService.findById(pageData1);
							if (null != staffInfo) {
								ExecutorID1 += ',' + (staffInfo.getString("NAME"));
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if (Tools.notEmpty(ExecutorID1)) {
						pageData.put("ExecutorID", ExecutorID1.subSequence(1, ExecutorID1.length()));
					}

				}
			}
			String FCreatePersonID = pageData.getString("FCreatePersonID");
			if (Tools.notEmpty(FCreatePersonID)) {
				PageData pageData1 = new PageData();
				pageData1.put("STAFF_ID", FCreatePersonID);
				try {
					PageData staffInfo = StaffService.findById(pageData1);
					if (null != staffInfo) {
						FCreatePersonID = (staffInfo.getString("NAME"));
						pageData.put("FCreatePersonID", FCreatePersonID);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210803 生产排程一键修改计划员
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/editByUser")
	@ResponseBody
	public Object editByUser() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PlanningWorkOrderService.editByUser(pd);
			map.put("result", errInfo);
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * v1 管悦 20210825 生产排程-柜体单条修改排产时间
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/editTime")
	@ResponseBody
	public Object editTime() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pdX = PlanningWorkOrderService.findById(pd);
			pdX.put("PlannedBeginTime", pd.getString("PlannedBeginTime"));
			pdX.put("PlannedEndTime", pd.getString("PlannedEndTime"));
			pdX.put("FPlanner", Jurisdiction.getName());
			pdX.put("FPlannerID", Jurisdiction.getName());
			pdX.put("progress", pd.getString("progress"));
			PlanningWorkOrderService.edit(pdX);
			map.put("result", errInfo);
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * v1 管悦 20210908 生产排程-工序单条修改排产时间
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/editTimeGX")
	@ResponseBody
	public Object editTimeGX() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pdX = processWorkOrderExampleService.findById(pd);
			pdX.put("PlannedBeginTime", pd.getString("PlannedBeginTime"));
			pdX.put("PlannedEndTime", pd.getString("PlannedEndTime"));
			pdX.put("FPlanner", Jurisdiction.getName());
			pdX.put("FPlannerID", Jurisdiction.getName());
			pdX.put("progress", pd.getString("progress"));
			processWorkOrderExampleService.edit(pdX);
			map.put("result", errInfo);
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

	}
}
