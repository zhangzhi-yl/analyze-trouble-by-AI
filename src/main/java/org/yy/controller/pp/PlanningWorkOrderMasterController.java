package org.yy.controller.pp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.flow.MASTER_PLANService;
import org.yy.service.flow.NEWPLAN_BOMService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.KMCustomerService;
import org.yy.service.km.ProcessDefinitionService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_AUXILIARYService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.WH_LocationService;
import org.yy.service.mom.WH_WareHouseService;
import org.yy.service.pp.PlanningWorkOrderMasterLockService;
import org.yy.service.pp.PlanningWorkOrderMasterService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.pp.SALESORDERDETAILService;
import org.yy.service.run.SecondStageService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.util.StringUtil;

/**
 * 说明：计划工单_主 作者：YuanYes QQ356703572 时间：2020-12-02 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PlanningWorkOrderMaster")
public class PlanningWorkOrderMasterController extends BaseController {
	@Autowired
	private NEWPLAN_BOMService NEWPLAN_BOMService;
	@Autowired
	private PlanningWorkOrderMasterService PlanningWorkOrderMasterService;
	@Autowired
	private BYTEARRAYService BYTEARRAYService;
	@Autowired
	private StaffService StaffService;
	@Autowired
	private MASTER_PLANService MASTER_PLANService;
	@Autowired
	private ProcessWorkOrderExampleService ProcessWorkOrderExampleService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private KMCustomerService CustomerService;
	@Autowired
	private SALESORDERDETAILService salesorderdetailService;
	@Autowired
	private MaterialRequirementService MaterialRequirementService;
	@Autowired
	private StockService StockService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private MAT_AUXILIARYService mat_auxiliaryService;
	// @Autowired
	// private ProcessDefinitionService ProcessDefinitionService;// 工序定义serverce
	@Autowired
	private WH_LocationService WH_LocationService;
	@Autowired
	private WH_WareHouseService WH_WareHouseService;
	@Autowired
	private SecondStageService SecondStageService;
	@Autowired
	private PlanningWorkOrderMasterLockService PlanningWorkOrderMasterLockService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/master-add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FStatus", "创建");
		pd.put("PlanningWorkOrderMaster_ID", this.get32UUID());
		pd.put("WorkOrderCreateTime", Tools.date2Str(new Date()));

		String WorkOrderNumParam = pd.getString("WorkOrderNum");
		PageData pDatas = new PageData();
		pDatas.put("WorkOrderNum", WorkOrderNumParam);
		List<PageData> workOrderNumList = PlanningWorkOrderMasterService.listAll(pDatas);
		if (CollectionUtil.isNotEmpty(workOrderNumList)) {
			map.put("result", "failed");
			map.put("msg", "已经存在该工单号的计划工单了");
			return map;
		}

		// 保存 主计划工单
		PlanningWorkOrderMasterService.save(pd);

		// 物料辅助属性为 planNO 的插入
		PageData auxData = new PageData();
		auxData.put("MAT_AUXILIARYMX_ID", this.get32UUID());
		auxData.put("MAT_AUXILIARYMX_CODE", pd.getString("WorkOrderNum"));
		auxData.put("MAT_AUXILIARYMX_NAME", pd.getString("WorkOrderNum") + "/计划工单编码");
		auxData.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
		mat_auxiliarymxService.save(auxData);

		// 占用库存 锁库
		if (StringUtil.isNotEmpty(pd.getString("OccupyCount"))) {
			if (0 != Integer.valueOf(pd.getString("OccupyCount"))) {
				PageData masterLock = new PageData();
				masterLock.put("DataSource", "主计划");
				masterLock.put("ConsumptionDocumentID", pd.getString("PlanningWorkOrderMaster_ID"));

				List<PageData> oldMasterLockList = PlanningWorkOrderMasterLockService.listAll(masterLock);
				for (PageData pageData : oldMasterLockList) {
					// 删除原来的数据
					PlanningWorkOrderMasterLockService.delete(pageData);
				}

				masterLock.put("PlanningWorkOrderMasterLock_ID", this.get32UUID());
				masterLock.put("MaterialID", pd.getString("OutputMaterialID"));
				masterLock.put("MaterialName", pd.getString("OutputMaterial").split("/")[0]);
				masterLock.put("MaterialCode", pd.getString("OutputMaterial").split("/")[1]);

				masterLock.put("SProp", auxData.getString("MAT_AUXILIARYMX_ID"));
				masterLock.put("LockCount", pd.getString("OccupyCount"));

				masterLock.put("LockOprator", Jurisdiction.getName());
				masterLock.put("LockTime", Tools.date2Str(new Date()));
				PlanningWorkOrderMasterLockService.save(masterLock);
			}
		}
		// 添加流程图JSON数据
		PageData pdOp = new PageData();

		String byteArrayJson = "" + "{\"title\":\"流程图\","
				+ "\"nodes\":{\"1607049105657\":{\"name\":\"开始\",\"left\":230,\"top\":60,\"type\":\"start round\",\"width\":26,\"height\":26,\"alt\":true},"
				+ "\"1607049107889\":{\"name\":\"结束\",\"left\":230,\"top\":530,\"type\":\"end round\",\"width\":26,\"height\":26,\"alt\":true}},"
				+ "\"lines\":{},\"areas\":{},\"initNum\":101}";

		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");

		if ("是".equals(pd.getString("TogetherCreate"))) {
			String nodeID = String.valueOf(System.currentTimeMillis());
			byteArrayJson = "" + "{\"title\":\"流程图\","
					+ "\"nodes\":{\"1607049105657\":{\"name\":\"开始\",\"left\":230,\"top\":60,\"type\":\"start round\",\"width\":26,\"height\":26,\"alt\":true},"
					+ "\"1607049107889\":{\"name\":\"结束\",\"left\":230,\"top\":530,\"type\":\"end round\",\"width\":26,\"height\":26,\"alt\":true},"
					+ "\"" + nodeID + "\":{\"name\":\"" + pd.getString("OutputMaterial")
					+ "\",\"left\":241,\"top\":268,\"type\":\"task round\",\"width\":26,\"height\":26,\"alt\":true}},"
					+ "\"lines\":{\"1609119686506\":{\"type\":\"sl\",\"from\":\"1607049105657\",\"to\":\"" + nodeID
					+ "\",\"name\":\"\",\"alt\":true}," + "\"1609119688338\":{\"type\":\"sl\",\"from\":\"" + nodeID
					+ "\",\"to\":\"1607049107889\",\"name\":\"\",\"alt\":true}}," + "\"areas\":{},\"initNum\":104}";

			PageData master_plan_node = new PageData();
			master_plan_node.put("MASTER_PLAN_FLOW_ID", this.get32UUID()); // 主键
			master_plan_node.put("NODE_ID", nodeID);
			master_plan_node.put("NODE_NAME", "node_101");
			master_plan_node.put("NODE_KIND", "node");
			master_plan_node.put("NODE_TYPE", "task");
			master_plan_node.put("PHASE_TYPE", "");
			master_plan_node.put("JUMP_CONDITION", "");
			master_plan_node.put("BEGIN_NODE", "");
			master_plan_node.put("END_NODE", "");
			master_plan_node.put("FCREATOR", STAFF_ID);
			master_plan_node.put("CREATE_TIME", Tools.date2Str(new Date()));
			master_plan_node.put("LAST_MODIFIER", STAFF_ID);
			master_plan_node.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			master_plan_node.put("FDES", "");
			master_plan_node.put("EXECUTE_STATE", "未执行");
			master_plan_node.put("MASTERPLAN_ID", pd.getString("PlanningWorkOrderMaster_ID"));
			MASTER_PLANService.save(master_plan_node);

			PageData master_plan_line1 = new PageData();
			master_plan_line1.put("MASTER_PLAN_FLOW_ID", this.get32UUID()); // 主键
			master_plan_line1.put("NODE_ID", System.currentTimeMillis());
			master_plan_line1.put("NODE_NAME", "");
			master_plan_line1.put("NODE_KIND", "line");
			master_plan_line1.put("NODE_TYPE", "sl");
			master_plan_line1.put("PHASE_TYPE", "");
			master_plan_line1.put("JUMP_CONDITION", "");
			master_plan_line1.put("BEGIN_NODE", "1607049105657");
			master_plan_line1.put("END_NODE", nodeID);
			master_plan_line1.put("FCREATOR", STAFF_ID);
			master_plan_line1.put("CREATE_TIME", Tools.date2Str(new Date()));
			master_plan_line1.put("LAST_MODIFIER", STAFF_ID);
			master_plan_line1.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			master_plan_line1.put("FDES", "");
			master_plan_line1.put("EXECUTE_STATE", "未执行");
			master_plan_line1.put("MASTERPLAN_ID", pd.getString("PlanningWorkOrderMaster_ID"));
			MASTER_PLANService.save(master_plan_line1);

			PageData master_plan_line2 = new PageData();
			master_plan_line2.put("MASTER_PLAN_FLOW_ID", this.get32UUID()); // 主键
			master_plan_line2.put("NODE_ID", System.currentTimeMillis());
			master_plan_line2.put("NODE_NAME", "");
			master_plan_line2.put("NODE_KIND", "line");
			master_plan_line2.put("NODE_TYPE", "sl");
			master_plan_line2.put("PHASE_TYPE", "");
			master_plan_line2.put("JUMP_CONDITION", "");
			master_plan_line2.put("BEGIN_NODE", nodeID);
			master_plan_line2.put("END_NODE", "1607049107889");
			master_plan_line2.put("FCREATOR", STAFF_ID);
			master_plan_line2.put("CREATE_TIME", Tools.date2Str(new Date()));
			master_plan_line2.put("LAST_MODIFIER", STAFF_ID);
			master_plan_line2.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			master_plan_line2.put("FDES", "");
			master_plan_line2.put("EXECUTE_STATE", "未执行");
			master_plan_line2.put("MASTERPLAN_ID", pd.getString("PlanningWorkOrderMaster_ID"));
			MASTER_PLANService.save(master_plan_line2);

			// 生成子计划工单信息
			// 创建 子计划工单ID
			PageData pageData = new PageData();
			String MasterWorkOrder_ID = pd.getString("PlanningWorkOrderMaster_ID");
			pageData.put("PlanningWorkOrderMaster_ID", MasterWorkOrder_ID);
			PageData masterPlanningWordOrder = PlanningWorkOrderMasterService.findById(pageData);
			if (null != masterPlanningWordOrder) {
				pd.put("MasterWorkOrder_ID", MasterWorkOrder_ID);
				pd.put("NODE_ID", nodeID);
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
				String WorkOrderNum = masterPlanningWordOrder.getString("WorkOrderNum") + "~"
						+ String.format("%03d", i);
				pd.put("WorkOrderNum", WorkOrderNum);
			}
			pd.put("PlanningWorkOrder_ID", this.get32UUID());
			pd.put("FStatus", "创建");
			pd.put("ScheduleSchedule", "未排程");
			pd.put("DistributionProgress", "未下发");
			pd.put("WorkOrderCreateTime", Tools.date2Str(new Date()));

			PlanningWorkOrderService.save(pd);

		}

		PageData pdjson = new PageData();
		pdjson.put("GE_BYTEARRAY_FLOW_ID", this.get32UUID()); // 主键
		pdjson.put("PID", pd.getString("PlanningWorkOrderMaster_ID"));
		pdjson.put("FTYPE", "计划工单");
		pdjson.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pdjson.put("LAST_MODIFIER", STAFF_ID);
		pdjson.put("TEXT_BYTE_STREAM", byteArrayJson);
		pdjson.put("RESOURCE_FILE_NAME", "流程图JSON");
		// 保存JSON
		BYTEARRAYService.save(pdjson);

		// 保存 固定开始结束节点数据
		PageData master_plan_begin = new PageData();
		master_plan_begin.put("MASTER_PLAN_FLOW_ID", this.get32UUID()); // 主键
		master_plan_begin.put("NODE_ID", "1607049105657");
		master_plan_begin.put("NODE_NAME", "开始");
		master_plan_begin.put("NODE_KIND", "node");
		master_plan_begin.put("NODE_TYPE", "start round");
		master_plan_begin.put("PHASE_TYPE", "");
		master_plan_begin.put("JUMP_CONDITION", "");
		master_plan_begin.put("BEGIN_NODE", "");
		master_plan_begin.put("END_NODE", "");
		master_plan_begin.put("FCREATOR", STAFF_ID);
		master_plan_begin.put("CREATE_TIME", Tools.date2Str(new Date()));
		master_plan_begin.put("LAST_MODIFIER", STAFF_ID);
		master_plan_begin.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		master_plan_begin.put("FDES", "");
		master_plan_begin.put("EXECUTE_STATE", "未执行");
		master_plan_begin.put("MASTERPLAN_ID", pd.getString("PlanningWorkOrderMaster_ID"));
		MASTER_PLANService.save(master_plan_begin);

		PageData master_plan_end = new PageData();
		master_plan_end.put("MASTER_PLAN_FLOW_ID", this.get32UUID()); // 主键
		master_plan_end.put("NODE_ID", "1607049107889");
		master_plan_end.put("NODE_NAME", "结束");
		master_plan_end.put("NODE_KIND", "node");
		master_plan_end.put("NODE_TYPE", "end round");
		master_plan_end.put("PHASE_TYPE", "");
		master_plan_end.put("JUMP_CONDITION", "");
		master_plan_end.put("BEGIN_NODE", "");
		master_plan_end.put("END_NODE", "");
		master_plan_end.put("FCREATOR", STAFF_ID);
		master_plan_end.put("CREATE_TIME", Tools.date2Str(new Date()));
		master_plan_end.put("LAST_MODIFIER", STAFF_ID);
		master_plan_end.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		master_plan_end.put("FDES", "");
		master_plan_end.put("EXECUTE_STATE", "未执行");
		master_plan_end.put("MASTERPLAN_ID", pd.getString("PlanningWorkOrderMaster_ID"));
		MASTER_PLANService.save(master_plan_end);

		// 回写销售订单已下推数量
		Double FCount = 0.0;
		String SalesOrderDetailID = pd.getString("SalesOrderDetailID");
		if (Tools.notEmpty(SalesOrderDetailID)) {
			PageData pData = new PageData();
			pData.put("SalesOrderDetailID", SalesOrderDetailID);
			List<PageData> listAll = PlanningWorkOrderMasterService.listAll(pData);
			if (CollectionUtil.isNotEmpty(listAll)) {
				for (PageData pageData : listAll) {
					Object fo = pageData.get("FCount");
					if (null != fo) {
						Double valueOf = Double.valueOf(String.valueOf(fo));
						FCount += valueOf;
					}
				}
			}
		}
		PageData pData = new PageData();
		pData.put("SalesOrderDetail_ID", SalesOrderDetailID);
		PageData SALESORDERDetail = salesorderdetailService.findById(pData);
		if (null != SALESORDERDetail) {
			Object fo = SALESORDERDetail.get("FPushCount");
			Double valueOf = Double.valueOf(String.valueOf(fo));
			FCount += valueOf;
			SALESORDERDetail.put("FPushCount", FCount);
			salesorderdetailService.edit(SALESORDERDetail);
		}

		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/master-delete")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData masterPlan = PlanningWorkOrderMasterService.findById(pd);
		pd.put("MasterWorkOrder_ID", pd.getString("PlanningWorkOrderMaster_ID"));
		PageData mat_auxiliarymxParam = new PageData();
		mat_auxiliarymxParam.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
		mat_auxiliarymxParam.put("MAT_AUXILIARYMX_CODE", masterPlan.getString("WorkOrderNum"));
		List<PageData> listAll2 = mat_auxiliarymxService.listAll(mat_auxiliarymxParam);
		if (CollectionUtil.isNotEmpty(listAll2)) {
			for (PageData pageData : listAll2) {
				mat_auxiliarymxService.delete(pageData);
			}
		}

		// 删除节点
		List<PageData> listAllSubPlan = PlanningWorkOrderService.listAll(pd);
		if (CollectionUtil.isNotEmpty(listAllSubPlan)) {
			for (PageData subplan : listAllSubPlan) {
				this.deleteSubPlanningWorkOrder(subplan.getString("PlanningWorkOrder_ID"));
			}
		}
		PlanningWorkOrderMasterService.delete(pd);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/master-edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();

		// 根据 辅助属性值 和 辅助属性值id 获取主键 删除原来的信息 重新创建
		PageData mat_auxiliarymxParam = new PageData();
		mat_auxiliarymxParam.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
		mat_auxiliarymxParam.put("MAT_AUXILIARYMX_CODE", pd.getString("WorkOrderNum"));
		List<PageData> listAll2 = mat_auxiliarymxService.listAll(mat_auxiliarymxParam);
		if (CollectionUtil.isNotEmpty(listAll2)) {
			for (PageData pageData : listAll2) {
				mat_auxiliarymxService.delete(pageData);
			}
		}

		// 物料辅助属性为 planNO 的插入
		PageData auxData = new PageData();
		auxData.put("MAT_AUXILIARYMX_ID", this.get32UUID());
		auxData.put("MAT_AUXILIARYMX_CODE", pd.getString("WorkOrderNum"));
		auxData.put("MAT_AUXILIARYMX_NAME", pd.getString("WorkOrderNum") + "/计划工单编码");
		auxData.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
		mat_auxiliarymxService.save(auxData);

		// 占用库存 锁库
		if (StringUtil.isNotEmpty(pd.getString("OccupyCount"))) {
			if (0 != Integer.valueOf(pd.getString("OccupyCount"))) {
				PageData masterLock = new PageData();
				masterLock.put("DataSource", "主计划");
				masterLock.put("ConsumptionDocumentID", pd.getString("PlanningWorkOrderMaster_ID"));

				List<PageData> oldMasterLockList = PlanningWorkOrderMasterLockService.listAll(masterLock);
				for (PageData pageData : oldMasterLockList) {
					// 删除原来的数据
					PlanningWorkOrderMasterLockService.delete(pageData);
				}

				masterLock.put("PlanningWorkOrderMasterLock_ID", this.get32UUID());
				masterLock.put("MaterialID", pd.getString("OutputMaterialID"));
				masterLock.put("MaterialName", pd.getString("OutputMaterial").split("/")[0]);
				masterLock.put("MaterialCode", pd.getString("OutputMaterial").split("/")[1]);

				masterLock.put("SProp", auxData.getString("MAT_AUXILIARYMX_ID"));
				masterLock.put("LockCount", pd.getString("OccupyCount"));

				masterLock.put("LockOprator", Jurisdiction.getName());
				masterLock.put("LockTime", Tools.date2Str(new Date()));
				PlanningWorkOrderMasterLockService.save(masterLock);
			}
		}

		PlanningWorkOrderMasterService.edit(pd);
		map.put("result", errInfo);
		return map;
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
		PageData pdOps = new PageData();
		pdOps.put("MasterWorkOrder_ID", pd.getString("PlanningWorkOrderMaster_ID"));
		if ("暂停".equals(pd.getString("FStatus"))) {
			List<PageData> listByMasterIdAndMasterWorkOrderNum = PlanningWorkOrderService
					.listByMasterIdAndMasterWorkOrderNum(pdOps);
			for (PageData subPlan : listByMasterIdAndMasterWorkOrderNum) {
				// 修改子计划工单 执行状态
				subPlan.put("FStatus", "暂停");
				PlanningWorkOrderService.edit(subPlan);
			}
		}
		if ("结束".equals(pd.getString("FStatus"))) {
			List<PageData> listByMasterIdAndMasterWorkOrderNum = PlanningWorkOrderService
					.listByMasterIdAndMasterWorkOrderNum(pdOps);
			for (PageData subPlan : listByMasterIdAndMasterWorkOrderNum) {
				// 修改子计划工单 执行状态
				subPlan.put("FStatus", "结束");
				PlanningWorkOrderService.edit(subPlan);
			}
		}
		if ("执行中".equals(pd.getString("FStatus"))) {
			List<PageData> listByMasterIdAndMasterWorkOrderNum = PlanningWorkOrderService
					.listByMasterIdAndMasterWorkOrderNum(pdOps);
			for (PageData subPlan : listByMasterIdAndMasterWorkOrderNum) {
				// 修改子计划工单 执行状态
				subPlan.put("FStatus", "执行中");
				PlanningWorkOrderService.edit(subPlan);
			}
		}
		// 修改主计划工单 执行状态
		PlanningWorkOrderMasterService.changeStatus(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
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
		String MasterWorkOrder_ID = pd.getString("PlanningWorkOrderMaster_ID");
		pd.put("MASTERPLAN_ID", MasterWorkOrder_ID);
		pd.put("MasterWorkOrder_ID", MasterWorkOrder_ID);
		List<PageData> listAll = PlanningWorkOrderService.listAll(pd);
		for (PageData pageData : listAll) {
			if ("未排程".equals(pageData.getString("ScheduleSchedule"))) {
				map.put("result", "failed");
				map.put("msg", "存在未排程的任务,请先去排程,再下发");
				return map;
			}
		}
		PageData masterPlan = PlanningWorkOrderMasterService.findById(pd);

		List<PageData> findStartSubPlan = SecondStageService.findStartSubPlan(pd);

		for (PageData pageData : findStartSubPlan) {
			String NODE_ID = pageData.getString("NODE_ID");
			PageData subPlan = PlanningWorkOrderService.getPlanByNodeIdAndMasterPlanId(MasterWorkOrder_ID, NODE_ID);
			subPlan.put("FStatus", "执行中");
			subPlan.put("DistributionProgress", "已下发");
			subPlan.put("ActualBeginTime", Tools.date2Str(new Date()));
			PlanningWorkOrderService.edit(subPlan);

			pageData.put("EXECUTE_STATE", "执行中");
			MASTER_PLANService.edit(pageData);
			PageData pageData2 = new PageData();
			pageData2.put("MASTERPLAN_ID", MasterWorkOrder_ID);
			pageData2.put("SUBPLAN_ID", subPlan.getString("PlanningWorkOrder_ID"));
			List<PageData> findStartTask = SecondStageService.findStartTask(pageData2);
			for (PageData taskFlow : findStartTask) {
				PageData po = new PageData();
				po.put("NODE_ID", taskFlow.getString("NODE_ID"));
				po.put("PlanningWorkOrderID", subPlan.getString("PlanningWorkOrder_ID"));
				List<PageData> pwoeList = ProcessWorkOrderExampleService.listAll(po);
				for (PageData task : pwoeList) {
					task.put("FStatus", "待执行");
					ProcessWorkOrderExampleService.edit(task);
				}

				taskFlow.put("EXECUTE_STATE", "执行中");
				NEWPLAN_BOMService.edit(taskFlow);
			}
		}
		masterPlan.put("FStatus", "执行中");
		masterPlan.put("ActualBeginTime", Tools.date2Str(new Date()));
		PlanningWorkOrderMasterService.edit(masterPlan);

		map.put("result", errInfo);
		return map;

	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/master-listAll")
	@ResponseBody
	public Object listAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件

		if (StringUtil.isNotEmpty(pd.getString("OrderNumS"))) {
			List<String> MasterNums = CollectionUtils.asList(pd.getString("OrderNumS").split(","));
			pd.put("OrderNumS", MasterNums);
		} else {
			pd.put("OrderNumS", null);
		}

		List<PageData> varList = PlanningWorkOrderMasterService.listAll(pd); // 列出PlanningWorkOrderMaster列表
		for (PageData pds : varList) {
			// 计划员 拆分( ,yl, )变为数组传给前端
			String FPlannerID = Tools.notEmpty(pds.getString("FPlannerID")) ? pds.getString("FPlannerID") : "";
			pds.put("FPlannerID", Lists.newArrayList(FPlannerID.split(",yl,")));

			String FPlanner = Tools.notEmpty(pds.getString("FPlanner")) ? pds.getString("FPlanner") : "";
			pds.put("FPlanner", Lists.newArrayList(FPlanner.split(",yl,")));

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
			pds.put("MaterialID", pds.getString("OutputMaterialID"));
			pds.put("DataSource", "主计划");
			pds.put("ConsumptionDocumentID", pds.getString("PlanningWorkOrderMaster_ID"));
			Integer OccupyCount = PlanningWorkOrderMasterLockService.getLockNumByMaterialId(pds);
			pds.put("OccupyCount", OccupyCount);
		}
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/master-list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
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

		page.setPd(pd);
		List<PageData> varList = PlanningWorkOrderMasterService.list(page); // 列出PlanningWorkOrderMaster列表
		for (PageData pds : varList) {
			// 计划员 拆分( ,yl, )变为数组传给前端
			String FPlannerID = Tools.notEmpty(pds.getString("FPlannerID")) ? pds.getString("FPlannerID") : "";
			pds.put("FPlannerID", Lists.newArrayList(FPlannerID.split(",yl,")));

			String FPlanner = Tools.notEmpty(pds.getString("FPlanner")) ? pds.getString("FPlanner") : "";
			pds.put("FPlanner", Lists.newArrayList(FPlanner.split(",yl,")));

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
			pds.put("MaterialID", pds.getString("OutputMaterialID"));
			pds.put("DataSource", "主计划");
			pds.put("ConsumptionDocumentID", pds.getString("PlanningWorkOrderMaster_ID"));
			Integer OccupyCount = PlanningWorkOrderMasterLockService.getLockNumByMaterialId(pds);
			pds.put("OccupyCount", OccupyCount);

			pd.put("PlanningWorkOrderMaster_ID", pds.get("PlanningWorkOrderMaster_ID"));
			List<PageData> varListMx = PlanningWorkOrderService.listAll(pd); // 列出PlanningWorkOrder列表
			int MxSize = varListMx.size();// 获取明细数量
			int RuningSize = 0;
			int FinishSize = 0;
			for (PageData pdMx : varListMx) {
				if (pdMx.get("FStatus").toString() == "结束" || pdMx.get("FStatus").toString().equals("结束")) {
					RuningSize++;
				}
				if (pdMx.get("FStatus").toString() == "执行中" || pdMx.get("FStatus").toString().equals("执行中")) {
					FinishSize++;
				}
			}
			if (RuningSize != 0) {
				pds.put("FStatus", "结束");
			}
			if (FinishSize != 0) {
				pds.put("FStatus", "执行中");
			}

		}
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
	@RequestMapping(value = "/master-goEdit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PlanningWorkOrderMasterService.findById(pd); // 根据ID读取

		// 根据主计划ID获取流程图JSON
		PageData pdjson = new PageData();
		pdjson.put("PID", pd.getString("PlanningWorkOrderMaster_ID"));
		pdjson.put("FTYPE", "计划工单");
		PageData findByPID = BYTEARRAYService.findByPID(pdjson);
		if (null != findByPID) {
			map.put("byteArray", findByPID);
		}
		String FCustomer = Tools.notEmpty(pd.getString("FCustomer")) ? pd.getString("FCustomer") : "";
		if (!"".equals(FCustomer)) {
			PageData pdcustomer = new PageData();
			pdcustomer.put("Customer_ID", FCustomer);
			PageData Customer = CustomerService.findById(pdcustomer);
			if (null != Customer) {
				FCustomer = Customer.getString("FName");
				pd.put("FCustomer", FCustomer);
			}
		}

		String FPlanner = Tools.notEmpty(pd.getString("FPlanner")) ? pd.getString("FPlanner") : "";
		if (!"".equals(FPlanner)) {
			String[] FPlanners = FPlanner.split(",yl,");
			List<String> FPlannerRes = Lists.newArrayList();
			for (String planner : FPlanners) {
				FPlannerRes.add(planner);
			}
			pd.put("FPlanner", FPlannerRes);
		}
		String FPlannerID = Tools.notEmpty(pd.getString("FPlannerID")) ? pd.getString("FPlannerID") : "";
		if (!"".equals(FPlannerID)) {
			String[] FPlannerIDs = FPlannerID.split(",yl,");
			List<String> FPlannerIDRes = Lists.newArrayList();
			for (String fPlannerID : FPlannerIDs) {
				FPlannerIDRes.add(fPlannerID);
			}
			pd.put("FPlannerID", FPlannerIDRes);
		}
		pd.put("MaterialID", pd.getString("OutputMaterialID"));
		pd.put("DataSource", "主计划");
		pd.put("ConsumptionDocumentID", pd.getString("PlanningWorkOrderMaster_ID"));
		Integer OccupyCount = PlanningWorkOrderMasterLockService.getLockNumByMaterialId(pd);
		pd.put("OccupyCount", OccupyCount);
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
	@RequestMapping(value = "/master-deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			for (String masterPlanId : ArrayDATA_IDS) {
				PageData pData = new PageData();
				pData.put("PlanningWorkOrderMaster_ID", masterPlanId);
				pData.put("MasterWorkOrder_ID", masterPlanId);
				PlanningWorkOrderMasterService.delete(pData);

				PageData masterPlan = MASTER_PLANService.findById(pData);
				if (null != masterPlan) {
					// 根据 辅助属性值 和 辅助属性值id 获取主键 删除原来的信息 重新创建
					PageData mat_auxiliarymxParam = new PageData();
					mat_auxiliarymxParam.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
					mat_auxiliarymxParam.put("MAT_AUXILIARYMX_CODE", masterPlan.getString("WorkOrderNum"));
					List<PageData> listAll2 = mat_auxiliarymxService.listAll(mat_auxiliarymxParam);
					if (CollectionUtil.isNotEmpty(listAll2)) {
						for (PageData pageData : listAll2) {
							mat_auxiliarymxService.delete(pageData);
						}
					}
				}

				List<PageData> listAllSubPlan = PlanningWorkOrderService.listAll(pData);
				if (CollectionUtil.isNotEmpty(listAllSubPlan)) {
					for (PageData subplan : listAllSubPlan) {
						this.deleteSubPlanningWorkOrder(subplan.getString("PlanningWorkOrder_ID"));
					}
				}
			}
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
		titles.add("备注14"); // 13
		titles.add("备注15"); // 14
		dataMap.put("titles", titles);
		List<PageData> varOList = PlanningWorkOrderMasterService.listAll(pd);
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
			vpd.put("var8", varOList.get(i).get("FPriorityID").toString()); // 8
			vpd.put("var9", varOList.get(i).getString("PlannedBeginTime")); // 9
			vpd.put("var10", varOList.get(i).getString("PlannedEndTime")); // 10
			vpd.put("var11", varOList.get(i).getString("FExplanation")); // 11
			vpd.put("var12", varOList.get(i).getString("FStatus")); // 12
			vpd.put("var13", varOList.get(i).getString("WorkOrderCreateTime")); // 13
			vpd.put("var14", varOList.get(i).getString("FPlanner")); // 14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/*
	 * 删除子计划工单
	 */
	private void deleteSubPlanningWorkOrder(String PlanningWorkOrderID) throws Exception {
		// 根据计划工单id，删除物料需求单
		PlanningWorkOrderService.deleteWorkorderProcessIOExampleByPlanningWorkOrderID(PlanningWorkOrderID);
		// 根据工艺工单工序实例id，删除工单工序投入产出实例
		PlanningWorkOrderService.deleteMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrderID);

		// 根据子计划工单id 删除所有SOP实例
		PlanningWorkOrderService.deleteProcessWorkOrderExampleSopStepByPlanningWorkOrderID(PlanningWorkOrderID);

		// 根据计划工单id，删除工艺工单工序实例
		PlanningWorkOrderService.deleteProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);
		// 删除主表信息
		PageData pd = new PageData();
		pd.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
		PlanningWorkOrderService.delete(pd);
		PageData pdx = PlanningWorkOrderService.findById(pd);
		if (null != pdx) {
			// 删除附件
			PageData pData = new PageData();
			pData.put("AssociationID", PlanningWorkOrderID);
			attachmentsetService.delete(pd);
			pd.put("NODE_ID", pdx.getString("NODE_ID"));
			pd.put("MASTERPLAN_ID", pdx.getString("MasterWorkOrder_ID"));
			// 删除流程节点信息
			MASTER_PLANService.deleteByMasterPlanIdAndNodeId(pd);
		}

	}

	/**
	 * 是否可创建主计划工单
	 */
	@RequestMapping(value = "/master-ableCreateMasterPlan")
	@ResponseBody
	public Object ableCreateMasterPlan() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();

		String SalesOrderDetailID = pd.getString("SalesOrderDetailID");
		Boolean able = true;
		if (Tools.notEmpty(SalesOrderDetailID)) {
			able = PlanningWorkOrderMasterService.ableCreateMasterPlan(SalesOrderDetailID);
		}
		map.put("pd", able);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 总物料需求单-汇总
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTotalMRByMasterPlanningWorkOrderID")
	@ResponseBody
	public Object getTotalMRByMasterPlanningWorkOrderID(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varlist = MaterialRequirementService.getTotalMRByMasterPlanningWorkOrderIDlistPage(page);
		map.put("varlist", varlist);
		map.put("result", errInfo);
		map.put("page", page);
		return map;
	}

	/**
	 * 总物料需求单-明细
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTotalMRDetailByMasterPlanningWorkOrderID")
	@ResponseBody
	public Object getTotalMRDetailByMasterPlanningWorkOrderID(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varlist = MaterialRequirementService.getTotalMRDetailByMasterPlanningWorkOrderID(page);
		for (PageData d : varlist) {
			// 根据id获取各个字段的名称
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
		map.put("varlist", varlist);
		map.put("result", errInfo);
		map.put("page", page);
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

		List<PageData> varList = PlanningWorkOrderMasterService.getWorkOrderNumList(pd);
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
