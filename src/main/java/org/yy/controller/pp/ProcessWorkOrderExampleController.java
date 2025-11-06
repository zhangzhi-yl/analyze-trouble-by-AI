package org.yy.controller.pp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.app.AppRegistrationService;
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
import org.yy.service.km.QualityInspectionPlanService;
import org.yy.service.km.SopStepService;
import org.yy.service.km.WorkingProcedureExampleService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_AUXILIARYService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mbase.MAT_SPECService;
import org.yy.service.mm.MAKECODEService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.Unit_InfoService;
import org.yy.service.mom.WC_StationService;
import org.yy.service.mom.WH_LocationService;
import org.yy.service.mom.WH_WareHouseService;
import org.yy.service.pp.PlanningWorkOrderMasterService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.pp.ProcessWorkOrderExample_SopStepService;
import org.yy.service.pp.SALESORDERDETAILService;
import org.yy.service.pp.SALESORDERService;
import org.yy.service.pp.WorkRecordService;
import org.yy.service.pp.WorkorderProcessIOExampleService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.util.DateUtil;
import org.yy.util.JpushClientUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.util.StringUtil;

/**
 * 任务controller
 * 
 * @author chen
 *
 */
@Controller
@RequestMapping("/ProcessWorkOrderExample")
public class ProcessWorkOrderExampleController extends BaseController {
	@Autowired
	private MAT_SPECService MAT_SPECService;
	@Autowired
	private WC_StationService WC_StationService;
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private StaffService StaffService;
	@Autowired
	private ProcessWorkOrderExampleService ProcessWorkOrderExampleService;
	@Autowired
	private StockService StockService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	@Autowired
	private BYTEARRAYService BYTEARRAYService;
	@Autowired
	private NEWPLAN_BOMService NEWPLAN_BOMService;
	@Autowired
	private QualityInspectionPlanService QIPlanService;
	@Autowired
	private WorkorderProcessIOExampleService WorkorderProcessIOExampleService;
	@Autowired
	private ProcessWorkOrderExample_SopStepService pwoeSopStepService;
	@Autowired
	private SopStepService SopStepService;
	@Autowired
	private PlanningWorkOrderService planningWorkOrderService;
	@Autowired
	private WorkorderProcessIOExampleService workorderProcessIOExampleService;
	@Autowired
	private MaterialConsumeService MaterialConsumeService;
	@Autowired
	private ProcessWorkOrderExampleService processWorkOrderExampleService;
	@Autowired
	private StockService stockService;
	@Autowired
	private WC_StationService stationService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private Unit_InfoService Unit_InfoService;
	@Autowired
	private MAKECODEService MAKECODEService;
	@Autowired
	private MAT_AUXILIARYMxService MAT_AUXILIARYMxService;
	@Autowired
	private MAT_BASICService matBasicService;
	@Autowired
	private WorkRecordService WorkRecordService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private AppRegistrationService AppRegistrationService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;
	@Autowired
	private Cabinet_BOMService Cabinet_BOMService;

	/**
	 * 删除节点和线
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteBySubPlanIdAndNodeId")
	@ResponseBody
	public Object deleteBySubPlanIdAndNodeId() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		NEWPLAN_BOMService.deleteBySubPlanIdAndNodeId(pd);

		// 根据subplanid 和 nodeid 获取当前任务 删除该任务
		pd.put("PlanningWorkOrderID", pd.getString("SUBPLAN_ID"));
		List<PageData> taskListAll = ProcessWorkOrderExampleService.listAll(pd);
		for (PageData task : taskListAll) {
			ProcessWorkOrderExampleService.delete(task);
		}
		map.put("result", errInfo);// 返回结果
		return map;
	}

	/**
	 * 修改生产任务状态
	 * 
	 * @param response
	 * @param FStatus
	 * @param ProcessWorkOrderExample_ID
	 * @return
	 */
	@RequestMapping("/changeProcessWorkOrderExampleStatus")
	@ResponseBody
	public Object changeProcessWorkOrderExampleStatus() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		String msg = "";
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData subPlan = planningWorkOrderService.findById(pd);
			if (null == subPlan) {
				// throw new RuntimeException("所属子计划工单为空");
				errInfo = "error";
				map.put("result", "error");
				map.put("msg", "所属子计划工单为空");
				return map;
			}

			if ("暂停".equals(subPlan.getString("FStatus"))) {
				// throw new RuntimeException("该任务所属计划被暂停，不能执行");
				errInfo = "error";
				map.put("result", "error");
				map.put("msg", "该任务所属计划被暂停，不能执行");
				return map;
			}
			PageData appProcessWorkOrderExampleDetailByPK = processWorkOrderExampleService.findById(pd);
			if (null == appProcessWorkOrderExampleDetailByPK) {
				errInfo = "error";
				msg = "任务没找到！";
				map.put("result", errInfo);// 返回结果
				map.put("msg", msg);
				return map;
			}
			String status = pd.getString("FStatus");

			if (status == "结束" || status.equals("结束")) {
				appProcessWorkOrderExampleDetailByPK.put("ProcessWorkOrderExample_ID",
						pd.get("ProcessWorkOrderExample_ID"));
				if (appProcessWorkOrderExampleDetailByPK.get("WPType") == "投入"
						|| appProcessWorkOrderExampleDetailByPK.get("WPType").equals("投入")) {
					appProcessWorkOrderExampleDetailByPK.put("FType", "消耗");
					appProcessWorkOrderExampleDetailByPK.put("WPTYPE", "投入");
				} else {
					appProcessWorkOrderExampleDetailByPK.put("FType", "产出");
					appProcessWorkOrderExampleDetailByPK.put("WPTYPE", "产出");
				}
//				List<PageData> InputAndOutput = WorkorderProcessIOExampleService
//						.getOutput(appProcessWorkOrderExampleDetailByPK);
//				if (InputAndOutput.size() < 1) {
//					errInfo = "error";
//					msg = "该任务还没有设置产出，请确认后再进行操作";
//					map.put("result", errInfo);// 返回结果
//					map.put("msg", msg);
//					return map;
//				}
			}

			PageData pdOp = new PageData();
			String name = Jurisdiction.getName();
			pdOp.put("FNAME", name);
			String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
			if (Tools.notEmpty(status)) {
				appProcessWorkOrderExampleDetailByPK.put("FStatus", status);
				appProcessWorkOrderExampleDetailByPK.put("FRUN_STATE", status);
				appProcessWorkOrderExampleDetailByPK.put("FOPERATOR", STAFF_ID);
				appProcessWorkOrderExampleDetailByPK.put("EXECUTE_TIME", Tools.date2Str(new Date()));
				appProcessWorkOrderExampleDetailByPK.put("TIME_STAMP", System.currentTimeMillis());

				if ("执行中".equals(status)) {
					appProcessWorkOrderExampleDetailByPK.put("ActualBeginTime", Tools.date2Str(new Date()));
				}
				if ("结束".equals(status)) {
					appProcessWorkOrderExampleDetailByPK.put("ActualEndTime", Tools.date2Str(new Date()));
					pd.put("EndTime", Tools.date2Str(new Date()));
					WorkRecordService.editEnd(pd);
				}

				processWorkOrderExampleService.edit(appProcessWorkOrderExampleDetailByPK);
				// 暂停时通知工单负责的生产主管
				if ("暂停".equals(status) && null != appProcessWorkOrderExampleDetailByPK.getString("FSC_NO")
						&& !"".equals(appProcessWorkOrderExampleDetailByPK.getString("FSC_NO"))) {
					PageData pdNotice = new PageData();
					pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
					
					
					// 跳转页面
				    pdNotice.put("AccessURL", "../../../views/pp/PlanningWorkOrder/PlanningWorkOrder_list.html");// 
					pdNotice.put("ReadPeople", ",");// 已读人默认空
					pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
					pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
					pdNotice.put("TType", "消息推送");// 消息类型
					pdNotice.put("FContent", "工单编号为" + appProcessWorkOrderExampleDetailByPK.getString("WorkOrderNum")
							+ "，工序为" + appProcessWorkOrderExampleDetailByPK.getString("WPName") + "，产出物料为"
							+ appProcessWorkOrderExampleDetailByPK.getString("ProcessIMaterielCode") + "的生产任务已暂停");// 消息正文
					pdNotice.put("FTitle", "生产任务暂停");// 消息标题
					pdNotice.put("LinkIf", "no");// 是否跳转页面
					pdNotice.put("DataSources", "生产任务暂停");// 数据来源
					String ReceivingAuthority = ",";
					ReceivingAuthority += appProcessWorkOrderExampleDetailByPK.getString("FSC_NO");
					ReceivingAuthority = ReceivingAuthority + ",";
					pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
					pdNotice.put("Report_Key", "task");// 手机app
					pdNotice.put("Report_Value", "");// 手机app
					noticeService.save(pdNotice);

					// 手机app-极光推送
					String UserName = appProcessWorkOrderExampleDetailByPK.getString("FSC_NO");
					pd.put("UserName", UserName);
					PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
					if (pdf != null) {
						String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
						String notification_title = "工作通知";
						String msg_title = "生产任务暂停";
						String msg_content = "工单编号为" + appProcessWorkOrderExampleDetailByPK.getString("WorkOrderNum")
								+ "，工序为" + appProcessWorkOrderExampleDetailByPK.getString("WPName") + "，产出物料为"
								+ appProcessWorkOrderExampleDetailByPK.getString("ProcessIMaterielCode") + "的生产任务已暂停";
						if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title, msg_title,
								msg_content, "") == 1) {
							// errInfo="success";
						} else {
							// errInfo="error";
						}
					}
				}
			}

			map.put("result", errInfo);// 返回结果
			return map;

		} catch (Exception e) {
			map.put("result", errInfo);// 返回结果
			return map;
		}
	}

	/**
	 * 根据子工单和node节点获取数据
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTaskBySubPlanIdAndNodeId")
	@ResponseBody
	public Object getTaskBySubPlanIdAndNodeId() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PlanningWorkOrderID", pd.getString("SUBPLAN_ID"));
		// 根据子工单和node节点获取数据
		List<PageData> taskListAll = ProcessWorkOrderExampleService.listAll(pd);
		if (CollectionUtil.isEmpty(taskListAll)) {
			errInfo = "failed";
		} else {
			map.put("pd", taskListAll.get(0));
		}
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

		PageData newplan_bom_line = new PageData();
		String NODE_ID = pd.getString("NODE_ID");
		String NODE_NAME = pd.getString("NODE_NAME");
		String BEGIN_NODE = pd.getString("BEGIN_NODE");
		String END_NODE = pd.getString("END_NODE");

		PageData pData = new PageData();
		pData.put("PlanningWorkOrder_ID", pd.getString("PlanningWorkOrderID"));
		PageData subPlan = PlanningWorkOrderService.findById(pData);
		String MasterWorkOrder_ID = subPlan.getString("MasterWorkOrder_ID");

		// 在流程节点表中加入连线
		newplan_bom_line.put("NEWPLAN_BOM_FLOW", this.get32UUID()); // 主键
		newplan_bom_line.put("NODE_ID", NODE_ID);
		newplan_bom_line.put("NODE_NAME", NODE_NAME);
		newplan_bom_line.put("NODE_KIND", "line");
		newplan_bom_line.put("NODE_TYPE", "sl");
		newplan_bom_line.put("PHASE_TYPE", "");
		newplan_bom_line.put("JUMP_CONDITION", "");
		newplan_bom_line.put("BEGIN_NODE", BEGIN_NODE);
		newplan_bom_line.put("END_NODE", END_NODE);
		newplan_bom_line.put("FCREATOR", STAFF_ID);
		newplan_bom_line.put("CREATE_TIME", Tools.date2Str(new Date()));
		newplan_bom_line.put("LAST_MODIFIER", STAFF_ID);
		newplan_bom_line.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		newplan_bom_line.put("FDES", "");
		newplan_bom_line.put("EXECUTE_STATE", "计划");
		newplan_bom_line.put("SUBPLAN_ID", pd.getString("PlanningWorkOrderID"));
		newplan_bom_line.put("MASTERPLAN_ID", MasterWorkOrder_ID);
		newplan_bom_line.put("RUN_O_ID", MasterWorkOrder_ID);
		NEWPLAN_BOMService.save(newplan_bom_line);
		return map;
	}

	@RequestMapping("/add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrderID"));

		PageData pData = new PageData();
		pData.put("PlanningWorkOrder_ID", pd.getString("PlanningWorkOrderID"));
		PageData subPlan = PlanningWorkOrderService.findById(pData);

		PageData listData = new PageData();
		listData.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrderID"));
		List<PageData> listAll = ProcessWorkOrderExampleService.listAll(listData);
		int i = 0;
		if (CollectionUtil.isNotEmpty(listAll)) {
			i = listAll.size();
		}

		if (null != subPlan) {
			PageData pdOp = new PageData();
			String name = Jurisdiction.getName();
			pdOp.put("FNAME", name);
			String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
			String BatchNum = subPlan.getString("BatchNum");

			String MasterWorkOrder_ID = subPlan.getString("MasterWorkOrder_ID");
			PageData pData2 = new PageData();
			pData2.put("PlanningWorkOrderMaster_ID", MasterWorkOrder_ID);

			if (i > 0) {
				Object object = listAll.get(i - 1).get("SerialNum");
				i = Integer.valueOf(String.valueOf(object));
			}
			// 添加任务号码
			String TaskNum = subPlan.getString("WorkOrderNum") + "~" + String.format("%02d", i + 1);
			pd.put("TaskNum", TaskNum);
			pd.put("SerialNum", i + 1);
			pd.put("OrderNum", subPlan.getString("OrderNum"));
			pd.put("BatchNum", BatchNum);
			pd.put("FCreatePersonID", STAFF_ID);
			pd.put("FCreateTime", Tools.date2Str(new Date()));
			pd.put("PlannedBeginTime", Tools.date2Str(new Date()));
			pd.put("PlannedEndTime", Tools.date2Str(new Date()));
			pd.put("FStatus", "未执行");
			pd.put("NODE_ID", pd.getString("NODE_ID"));
			pd.put("TaskType", "1");
			pd.put("ProcessWorkOrderExample_ID", this.get32UUID());
			ProcessWorkOrderExampleService.add(pd);

			// 插入节点
			PageData newplan_bom_node = new PageData();
			newplan_bom_node.put("NEWPLAN_BOM_FLOW", this.get32UUID()); // 主键
			newplan_bom_node.put("NODE_ID", pd.getString("NODE_ID"));
			newplan_bom_node.put("NODE_NAME", TaskNum);
			newplan_bom_node.put("NODE_KIND", "node");
			newplan_bom_node.put("NODE_TYPE", "ask round");
			newplan_bom_node.put("PHASE_TYPE", "");
			newplan_bom_node.put("JUMP_CONDITION", "");
			newplan_bom_node.put("BEGIN_NODE", "");
			newplan_bom_node.put("END_NODE", "");
			newplan_bom_node.put("FCREATOR", STAFF_ID);
			newplan_bom_node.put("CREATE_TIME", Tools.date2Str(new Date()));
			newplan_bom_node.put("LAST_MODIFIER", STAFF_ID);
			newplan_bom_node.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			newplan_bom_node.put("FDES", "");
			newplan_bom_node.put("EXECUTE_STATE", "未执行");
			newplan_bom_node.put("SUBPLAN_ID", pd.getString("PlanningWorkOrderID"));
			newplan_bom_node.put("MASTERPLAN_ID", MasterWorkOrder_ID);
			newplan_bom_node.put("RUN_O_ID", MasterWorkOrder_ID);
			NEWPLAN_BOMService.save(newplan_bom_node);

		}
		map.put("result", errInfo);// 返回结果ad
		return map;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 更新
		String ProcessWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
		PageData pData = new PageData();
		pData.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);
		PageData taskInfo = ProcessWorkOrderExampleService.findById(pData);
		if (null != taskInfo) {
			Tools.mapCopy(pd, taskInfo);
			ProcessWorkOrderExampleService.edit(taskInfo);
			pwoeSopStepService.deleteByTaskID(ProcessWorkOrderExample_ID);

			String SOP_ID = taskInfo.getString("SOP_ID");
			if (Tools.notEmpty(SOP_ID)) {
				PageData soPageData = new PageData();
				soPageData.put("SopSchemeTemplate_ID", SOP_ID);
				List<PageData> SopList = SopStepService.listAll(soPageData);
				for (PageData sop : SopList) {
					sop.put("PlanningWorkOrderID", taskInfo.getString("PlanningWorkOrderID"));
					sop.put("ProcessWorkOrderExample_SopStep_ID", this.get32UUID());
					sop.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);
					pwoeSopStepService.save(sop);
				}
			}
		}

		map.put("result", errInfo);
		return map;
	}

	@RequestMapping("/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 更新
		String ProcessWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
		PageData pData = new PageData();
		pData.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);
		PageData taskInfo = ProcessWorkOrderExampleService.findById(pData);
		if (null != taskInfo) {
			String QIPlanID = taskInfo.getString("QIPlanID");
			PageData qiPageData = new PageData();
			qiPageData.put("QualityInspectionPlan_ID", QIPlanID);
			PageData qi = QIPlanService.findById(qiPageData);
			// 关联质检名称
			if (null != qi) {
				taskInfo.put("QIPlanName", qi.getString("FName"));
			}
			if (null == taskInfo.getString("ExecutorID")) {
				taskInfo.put("ExecutorID", "");
				taskInfo.put("ExecutorName", "");
			}
			map.put("pd", taskInfo);
		}
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping("/listPageByPlanningWorkOrderID")
	@ResponseBody
	public Object listPageByPlanningWorkOrderID(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrderID"));
		page.setPd(pd);
		List<PageData> varList = ProcessWorkOrderExampleService.list(page);
		for (PageData pageData : varList) {
			String QIPlanID = pageData.getString("QIPlanID");
			PageData qiPageData = new PageData();
			qiPageData.put("QualityInspectionPlan_ID", QIPlanID);
			PageData qi = QIPlanService.findById(qiPageData);
			// 关联质检名称
			if (null != qi) {
				pageData.put("QIPlanName", qi.getString("FName"));
			}
			if (null == pageData.getString("ExecutorID")) {
				pageData.put("ExecutorID", "");
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping("/listByPlanningWorkOrderID")
	@ResponseBody
	public Object listByPlanningWorkOrderID() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrderID"));

		List<PageData> varList = ProcessWorkOrderExampleService.listAll(pd);

		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 创建临时任务
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addCurrentTask")
	@ResponseBody
	public Object addCurrentTask() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrderID"));
		// 获取子计划工单
		PageData pData = new PageData();
		pData.put("PlanningWorkOrder_ID", pd.getString("PlanningWorkOrderID"));
		PageData subPlan = PlanningWorkOrderService.findById(pData);

		PageData listData = new PageData();
		listData.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrderID"));
		List<PageData> listAll = ProcessWorkOrderExampleService.listAll(listData);
		int i = 0;
		if (CollectionUtil.isNotEmpty(listAll)) {
			i = listAll.size();
		}
		// 添加任务号
		if (null != subPlan) {
			PageData pdOp = new PageData();
			String name = Jurisdiction.getName();
			pdOp.put("FNAME", name);
			String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
			String BatchNum = subPlan.getString("BatchNum");

			String MasterWorkOrder_ID = subPlan.getString("MasterWorkOrder_ID");
			PageData pData2 = new PageData();
			pData2.put("PlanningWorkOrderMaster_ID", MasterWorkOrder_ID);

			if (i > 0) {
				Object object = listAll.get(i - 1).get("SerialNum");
				i = Integer.valueOf(String.valueOf(object));
			}
			// 添加任务号码
			String TaskNum = subPlan.getString("WorkOrderNum") + "~" + String.format("%02d", i + 1);
			pd.put("TaskNum", TaskNum);
			pd.put("SerialNum", i + 1);
			pd.put("OrderNum", subPlan.getString("OrderNum"));
			pd.put("BatchNum", BatchNum);
			pd.put("FCreatePersonID", STAFF_ID);
			pd.put("FCreateTime", Tools.date2Str(new Date()));
			pd.put("PlannedBeginTime", Tools.date2Str(new Date()));
			pd.put("PlannedEndTime", Tools.date2Str(new Date()));
			pd.put("FStatus", "待执行");
			pd.put("NODE_ID", pd.getString("NODE_ID"));
			// 添加临时任务
			pd.put("TaskType", "3");
			pd.put("ProcessWorkOrderExample_ID", this.get32UUID());
			ProcessWorkOrderExampleService.add(pd);
		}
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ProcessWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
		// 删除信息
		ProcessWorkOrderExampleService.delete(pd);
		// 删除关联的投入产出
		List<PageData> listByProcessWorkOrderExampleID = WorkorderProcessIOExampleService
				.listByProcessWorkOrderExampleID(pd.getString("ProcessWorkOrderExample_ID"));
		for (PageData pageData : listByProcessWorkOrderExampleID) {
			WorkorderProcessIOExampleService.delete(pageData);
		}
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping("/findByteArrayByPID")
	@ResponseBody
	public Object findByteArrayByPID() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdOp = new PageData();
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = StaffService.getStaffId(pdOp).getString("STAFF_ID");
		// 根据主计划ID获取流程图JSON
		PageData pdjson = new PageData();
		pdjson.put("PID", pd.getString("PlanningWorkOrderID"));
		pdjson.put("FTYPE", "工单实例BOM");
		PageData findByPID = BYTEARRAYService.findByPID(pdjson);
		if (null != findByPID) {
			map.put("byteArray", findByPID.getString("TEXT_BYTE_STREAM"));
		} else {
			String byteArrayJson = "" + "{\"title\":\"流程图\","
					+ "\"nodes\":{\"1607049105657\":{\"name\":\"开始\",\"left\":230,\"top\":60,\"type\":\"start round\",\"width\":26,\"height\":26,\"alt\":true},"
					+ "\"1607049107889\":{\"name\":\"结束\",\"left\":230,\"top\":530,\"type\":\"end round\",\"width\":26,\"height\":26,\"alt\":true}},"
					+ "\"lines\":{},\"areas\":{},\"initNum\":1}";
			// 没有？创建一个byteArray
			pdjson.put("GE_BYTEARRAY_FLOW_ID", this.get32UUID());
			pdjson.put("RESOURCE_FILE_NAME", "工艺路线");
			pdjson.put("TEXT_BYTE_STREAM", byteArrayJson);
			pdjson.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			pdjson.put("LAST_MODIFIER", STAFF_ID);
			BYTEARRAYService.save(pdjson);
			map.put("byteArray", byteArrayJson);

			String PlanningWorkOrderID = pd.getString("PlanningWorkOrderID");
			PageData pData = new PageData();
			pData.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
			PageData subPlan = PlanningWorkOrderService.findById(pData);
			String MASTERPLAN_ID = "";
			if (null != subPlan) {
				MASTERPLAN_ID = subPlan.getString("MasterWorkOrder_ID");
			}

			// 保存 固定开始结束节点数据
			PageData newplan_bom_begin = new PageData();
			newplan_bom_begin.put("NEWPLAN_BOM_FLOW", this.get32UUID()); // 主键
			newplan_bom_begin.put("NODE_ID", "1607049105657");
			newplan_bom_begin.put("NODE_NAME", "开始");
			newplan_bom_begin.put("NODE_KIND", "node");
			newplan_bom_begin.put("NODE_TYPE", "start round");
			newplan_bom_begin.put("PHASE_TYPE", "");
			newplan_bom_begin.put("JUMP_CONDITION", "");
			newplan_bom_begin.put("BEGIN_NODE", "");
			newplan_bom_begin.put("END_NODE", "");
			newplan_bom_begin.put("FCREATOR", STAFF_ID);
			newplan_bom_begin.put("CREATE_TIME", Tools.date2Str(new Date()));
			newplan_bom_begin.put("LAST_MODIFIER", STAFF_ID);
			newplan_bom_begin.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			newplan_bom_begin.put("FDES", "");
			newplan_bom_begin.put("EXECUTE_STATE", "未执行");
			newplan_bom_begin.put("SUBPLAN_ID", pd.getString("PlanningWorkOrderID"));
			newplan_bom_begin.put("MASTERPLAN_ID", MASTERPLAN_ID);
			newplan_bom_begin.put("RUN_O_ID", MASTERPLAN_ID);
			NEWPLAN_BOMService.save(newplan_bom_begin);

			PageData newplan_bom_end = new PageData();
			newplan_bom_end.put("NEWPLAN_BOM_FLOW", this.get32UUID()); // 主键
			newplan_bom_end.put("NODE_ID", "1607049107889");
			newplan_bom_end.put("NODE_NAME", "结束");
			newplan_bom_end.put("NODE_KIND", "node");
			newplan_bom_end.put("NODE_TYPE", "end round");
			newplan_bom_end.put("PHASE_TYPE", "");
			newplan_bom_end.put("JUMP_CONDITION", "");
			newplan_bom_end.put("BEGIN_NODE", "");
			newplan_bom_end.put("END_NODE", "");
			newplan_bom_end.put("FCREATOR", STAFF_ID);
			newplan_bom_end.put("CREATE_TIME", Tools.date2Str(new Date()));
			newplan_bom_end.put("LAST_MODIFIER", STAFF_ID);
			newplan_bom_end.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			newplan_bom_end.put("FDES", "");
			newplan_bom_end.put("EXECUTE_STATE", "未执行");
			newplan_bom_end.put("SUBPLAN_ID", pd.getString("PlanningWorkOrderID"));
			newplan_bom_end.put("MASTERPLAN_ID", MASTERPLAN_ID);
			newplan_bom_end.put("RUN_O_ID", MASTERPLAN_ID);
			NEWPLAN_BOMService.save(newplan_bom_end);

		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 扫码验证是否是该任务的物料
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/scanMaterialx")
	@ResponseBody
	public Object scanMaterialx() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			if (Tools.isEmpty(code)) {
				map.put("result", "error");
				map.put("msg", "二维码无法识别");
				return map;
			}

			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				map.put("result", "error");
				map.put("msg", "工位无法识别");
				return map;
			}

			String oneThingsCode = "";
			String qrCode = "";
			String materialId = "";
			Double count = 0.0;
			String processWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
			PageData pwoeData = new PageData();
			pwoeData.put("ProcessWorkOrderExample_ID", processWorkOrderExample_ID);

			PageData findById = processWorkOrderExampleService.findById(pwoeData);
			if (null == findById) {
				map.put("result", "error");
				map.put("msg", "任务不存在");
				return map;
			}
			// 验证锅次
			if ("是".equals(findById.getString("WokIF"))) {
				String WokNumParam = findById.getString("WokNum");
				String[] splits = code.split(",YL,");
				List<String> codeSplitLists = Lists.newArrayList(splits);
				String WokNum = codeSplitLists.get(codeSplitLists.size() - 1);
				if (Tools.isEmpty(WokNum)) {
					map.put("result", "error");
					map.put("msg", "锅次号不存在");
					return map;
				}
				if (!WokNum.equals(WokNumParam)) {
					map.put("result", "error");
					map.put("msg", "该物料不是本锅次的物料");
					return map;
				}
			}

			// 唯一码 带出 物料码 和 序列号
			if ("W".equals(code.substring(0, 1))) {
				// 根据唯一码去库存中查找物料叫啥
				String[] split = code.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					oneThingsCode = codeSplitList.get(1);
					count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
					PageData pData = new PageData();
					// 一物码
					pData.put("OneThingCode", oneThingsCode);
					List<PageData> listAll = stockService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						materialId = listAll.get(0).getString("ItemID");
					}
					pd.put("MaterialCode", qrCode);
					pd.put("OneThingCode", oneThingsCode);
				}
			}
			// 类型码 带出 物料码
			if ("L".equals(code.substring(0, 1))) {
				// 根据类型码去库存中查找物料叫啥
				String[] split = code.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					qrCode = codeSplitList.get(1);
					count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
					PageData pData = new PageData();
					// 类型码
					pData.put("QRCode", qrCode);
					List<PageData> listAll = stockService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						materialId = listAll.get(0).getString("ItemID");
					}
					pd.put("MaterialCode", qrCode);
					pd.put("OneThingCode", oneThingsCode);
				}
			}
			pd.put("MaterialID", materialId);

			// 查投入产出实例表 看该物料是否在当前任务下 允许投入
			PageData inputParam = new PageData();

			inputParam.put("processWorkOrderExample_ID", processWorkOrderExample_ID);
			inputParam.put("TType", "投入");
			List<String> basicIDs = Lists.newArrayList();
			basicIDs.add(materialId);
			inputParam.put("array", basicIDs);
			List<PageData> wpioeList = workorderProcessIOExampleService.listAll(inputParam);
			if (CollectionUtil.isEmpty(wpioeList)) {
				map.put("result", "error");
				map.put("msg", "该物料不适用于该任务");
				return map;
			}
			// 单位带过来
			PageData materialParam = new PageData();
			materialParam.put("MAT_BASIC_ID", materialId);
			PageData mat = matBasicService.findById(materialParam);
			String MaterialName = mat.getString("MAT_NAME");
			pd.put("MaterialName", MaterialName);
			pd.put("TType", "投入");
			String MAT_MAIN_UNIT = mat.getString("MAT_MAIN_UNIT");
			materialParam.put("UNIT_INFO_ID", MAT_MAIN_UNIT);
			PageData unitInfo = Unit_InfoService.findById(materialParam);
			if (null == unitInfo) {
				pd.put("UNIT", "默认单位");
			} else {
				pd.put("UNIT", unitInfo.getString("FNAME"));
			}
			pd.put("ProcessIMateriel", pd.getString("MaterialCode") + "|" + pd.getString("MaterialName"));
			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			String stockSum = "0";
			double stockSumDouble = 0.0;
			if (null != stationInfo) {
				String WareHouseId = stationInfo.getString("ConsumptionWarehouse");
				// 消耗仓库 根据仓库获取该物料的 库存 如果库存不足，提示
				PageData pdKC = new PageData();
				pdKC.put("WarehouseID", WareHouseId);
				pdKC.put("ItemID", materialId);
				PageData pdStockSum = StockService.getSum(pdKC);// 即时库存

				if (null == pdStockSum) {
					map.put("result", "error");
					map.put("msg", "库存不足");
					return map;
				}

				if (null != pdStockSum) {

					if ("0".equals(String.valueOf(pdStockSum.get("stockSum")))) {
						map.put("result", "error");
						map.put("msg", "库存不足");
						return map;
					}

					// 获取即时库存
					stockSum = pdStockSum.get("stockSum").toString();
					stockSumDouble = Double.valueOf(stockSum);
				}
			}
			pd.put("StockSumDouble", stockSumDouble);
			pd.put("count", count);

			// ============================begin=================================//

			if (CollectionUtil.isNotEmpty(wpioeList)) {
				PageData pageData = wpioeList.get(0);
				pd.put("TType", pageData.getString("TType"));
				pd.put("ByProduct", pageData.getString("ByProduct"));
				String PlannedQuantityStr = String.valueOf(pageData.get("PlannedQuantity"));
				BigDecimal PlannedQuantityBigDec = new BigDecimal(PlannedQuantityStr);
				pd.put("PlannedQuantity", PlannedQuantityBigDec);

				// 根据物料消耗 表 获取当前 消耗了多少 做百分比
				PageData materialConsumeParam = new PageData();
				materialConsumeParam.put("ConsumptionDocumentID", pageData.getString("WorkorderProcessIOExample_ID"));
				materialConsumeParam.put("FType", "消耗");
				materialConsumeParam.put("MaterialID", pageData.getString("MaterialID"));
				materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
				List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
				if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
					BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
					for (PageData pageDatas : MaterialConsumeList) {
						BigDecimal i = new BigDecimal(String.valueOf(pageDatas.get("ConsumptionQuantity")));
						ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
					}
					pd.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
					BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDec, 4,
							BigDecimal.ROUND_HALF_DOWN);
					BigDecimal multiply = divide.multiply(new BigDecimal("100"));
					BigDecimal subtract = PlannedQuantityBigDec.subtract(ConsumptionQuantityBigDec);
					if (subtract.doubleValue() < 0) {
						pd.put("Subtract", 0);
					} else {
						pd.put("Subtract", subtract.doubleValue());
					}
					// 计算投产百分比
					if (multiply.doubleValue() > 100.00) {
						pd.put("InputPercent", 100);
					} else {
						String percent = new DecimalFormat("#.00").format(multiply);
						String index0 = percent.substring(0, 1);
						if (".".equals(index0)) {
							percent = "0".concat(percent);
						}
						pd.put("InputPercent", Double.valueOf(percent));
					}
				} else {
					pd.put("ConsumptionQuantity", 0);
					pd.put("InputPercent", 0);
					pd.put("Subtract", 0);
				}
			} else {
				pd.put("ConsumptionQuantity", 0);
				pd.put("InputPercent", 0);
				pd.put("Subtract", 0);
			}

			// ============================end===================================//

			map.put("result", "success");
			map.put("pd", pd);
			map.put("msg", "可以进行扫码投产,返回物料即时库存");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e);
			return map;
		}
	}

	/**
	 * 添加投入产出 非标
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insertIOData")
	@ResponseBody
	public Object insertIOData() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("WorkorderProcessIOExample_ID", this.get32UUID());
		pd.put("FormulaQuantity", 0.00);
		pd.put("PlannedQuantity", 0.00);
		pd.put("FStatus", "N");
		pd.put("FExplanation", "");

		// 添加非标产品的任务
		WorkorderProcessIOExampleService.save(pd);
		map.put("result", errInfo);
		PageData pData = new PageData();
		pData.put("processWorkOrderExample_ID", pd.getString("ProcessWorkOrderExampleID"));
		pData.put("TType", pd.getString("TType"));
		List<PageData> listAll = WorkorderProcessIOExampleService.listAll(pData);
		if (CollectionUtil.isEmpty(listAll)) {
			pd.put("SerialNum", 1);
		} else {
			pd.put("SerialNum", listAll.size());
		}
		map.put("pd", pd);
		return map;
	}

	/**
	 * 编辑投入产出 非标
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editIOData")
	@ResponseBody
	public Object editIOData() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FormulaQuantity", pd.get("PlannedQuantity"));
		// 编辑投入产出 如果是产出更新任务表的产出物料字段
		if ("产出".equals(pd.getString("TType"))) {
			String ProcessWorkOrderExample_ID = pd.getString("ProcessWorkOrderExampleID");

			if ("主产出".equals(pd.getString("ProcessIType"))) {
				String WorkorderProcessIOExample_ID = pd.getString("WorkorderProcessIOExample_ID");
				PageData pageData = new PageData();
				pageData.put("WorkorderProcessIOExample_ID", WorkorderProcessIOExample_ID);
				PageData findById = WorkorderProcessIOExampleService.findById(pageData);
				if (null != findById) {
					if (!"主产出".equals(findById.getString("ProcessIType"))) {
						PageData pData1 = new PageData();
						pData1.put("processWorkOrderExample_ID", ProcessWorkOrderExample_ID);
						pData1.put("TType", pd.getString("TType"));
						pData1.put("ProcessIType", "主产出");
						List<PageData> listAll1 = WorkorderProcessIOExampleService.listAll(pData1);
						if (listAll1.size() > 0) {
							map.put("result", "fail1");
							map.put("msg", "该任务只能有一个主产出");
							return map;
						}
					}
				}
			}
			PageData pData = new PageData();
			pData.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);
			PageData task = ProcessWorkOrderExampleService.findById(pData);
			if (null != task) {
				if (null != pd.getString("MaterialID")) {
					task.put("ProcessIMaterielID", pd.getString("MaterialID"));
					PageData matData = new PageData();
					matData.put("MAT_BASIC_ID", pd.getString("MaterialID"));
					String MAT_SPACS = "";
					String ProcessIMateriel = "";
					String ProcessIMaterielCode = "";
					PageData MAT = mat_basicService.findById(matData);
					if (null != MAT) {
						MAT_SPACS = MAT.getString("MAT_SPACS");
						ProcessIMateriel = MAT.getString("MAT_NAME");
						ProcessIMaterielCode = MAT.getString("MAT_CODE");
					}
					task.put("ProcessIMateriel", ProcessIMateriel);
					task.put("ProcessIMaterielCode", ProcessIMaterielCode);
					task.put("FMSpecification", MAT_SPACS);
					ProcessWorkOrderExampleService.edit(task);
				}
			}
		}
		WorkorderProcessIOExampleService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 任务详情
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/ProcessWorkOrderExampleDetailByPK")
	@ResponseBody
	public Object ProcessWorkOrderExampleDetailByPK() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String processWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
			PageData appProcessWorkOrderExampleDetailByPK = planningWorkOrderService
					.appProcessWorkOrderExampleDetailByPK(pd);
			if (null == appProcessWorkOrderExampleDetailByPK) {
				appProcessWorkOrderExampleDetailByPK = new PageData();
			}
			String PlanningWorkOrderID = appProcessWorkOrderExampleDetailByPK.getString("PlanningWorkOrderID");

			if (Tools.isEmpty(PlanningWorkOrderID)) {
				// throw new RuntimeException("所属子计划工单为空");
				errInfo = "error";
				map.put("result", "error");
				map.put("msg", "所属子计划工单为空");
				return map;
			}
			pd.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
			PageData subPlan = planningWorkOrderService.findById(pd);
			if (null == subPlan) {
				// throw new RuntimeException("所属子计划工单为空");
				errInfo = "error";
				map.put("result", "error");
				map.put("msg", "所属子计划工单为空");
				return map;
			}

			if ("暂停".equals(subPlan.getString("FStatus"))) {
				// throw new RuntimeException("该任务所属计划被暂停，不能执行");
				errInfo = "error";
				map.put("result", "error");
				map.put("msg", "该任务所属计划被暂停，不能执行");
				return map;
			}

			List<PageData> listByProcessWorkOrderExampleID = workorderProcessIOExampleService
					.listByProcessWorkOrderExampleID(processWorkOrderExample_ID);
			appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", 0);
			appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 0);
			appProcessWorkOrderExampleDetailByPK.put("PlannedQuantity", 0);
			// 按投入产出类型 获取不同的列表
			if (CollectionUtil.isNotEmpty(listByProcessWorkOrderExampleID)) {
				for (PageData pageData : listByProcessWorkOrderExampleID) {
					if ("产出".equals(pageData.getString("TType"))) {
						String PlannedQuantityStr = String.valueOf(pageData.get("PlannedQuantity"));
						BigDecimal PlannedQuantityBigDec = new BigDecimal(PlannedQuantityStr);

						// 根据物料消耗 表 获取当前生产了多少 做百分比
						PageData materialConsumeParam = new PageData();
						materialConsumeParam.put("ConsumptionDocumentID",
								pageData.getString("WorkorderProcessIOExample_ID"));
						materialConsumeParam.put("FType", "产出");
						materialConsumeParam.put("MaterialID", pageData.getString("MaterialID"));
						materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
						List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
						if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
							BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
							for (PageData pageDatas : MaterialConsumeList) {
								BigDecimal i = new BigDecimal(String.valueOf(pageDatas.get("ConsumptionQuantity")));
								ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
							}
							appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
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
								appProcessWorkOrderExampleDetailByPK.put("TaskPercent", Double.valueOf(percent));
							}
						} else {

							appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", 0);

							appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 0);

						}
						appProcessWorkOrderExampleDetailByPK.put("PlannedQuantity", PlannedQuantityBigDec);
					}
				}

			}
			map.put("pd", appProcessWorkOrderExampleDetailByPK);
			map.put("result", errInfo);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
			map.put("result", errInfo);
			map.put("msg", e);
			return map;
		}
	}

	/**
	 * 获取投入产出列表 非标
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getIOList")
	@ResponseBody
	public Object getIOList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> listAll = WorkorderProcessIOExampleService.listAll(pd);
		map.put("varList", listAll);
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping("/IOExamplelistPage")
	@ResponseBody
	public Object IOExamplelistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();

		PageData pwoe = processWorkOrderExampleService.findById(pd);
		List<PageData> varList = Lists.newArrayList();
		if ("投入".equals(pd.getString("TType"))) {

			String Cabinet_No = pwoe.getString("ProcessIMaterielCode");
			PageData pdCabNo = new PageData();
			pdCabNo.put("Cabinet_No", Cabinet_No);
			List<PageData> listAll = Cabinet_Assembly_DetailService.listAll(pdCabNo);

			if (CollectionUtil.isNotEmpty(listAll)) {
				String detailId = listAll.get(0).getString("Cabinet_Assembly_Detail_ID");
				PageData bomParam = new PageData();
				bomParam.put("Cabinet_Assembly_Detail_ID", detailId);
				bomParam.put("MAT_CODE", pd.getString("KEYWORDS"));
				page.setPd(bomParam);
				List<PageData> bomList = Cabinet_BOMService.listAllGroupByMat(bomParam);
				if (CollectionUtil.isNotEmpty(bomList)) {
					for (PageData bomData : bomList) {
						String IFOneCode = "否";
						PageData ioData = new PageData();
						ioData.put("MAT_NAME", bomData.getString("MAT_NAME"));
						ioData.put("MaterialName", bomData.getString("MAT_NAME"));
						ioData.put("MAT_CODE", bomData.getString("MAT_CODE"));
						ioData.put("MaterialNum", bomData.getString("MAT_CODE"));
						ioData.put("FormulaQuantity", bomData.get("BOM_COUNT").toString());
						ioData.put("MAT_SPECS", bomData.getString("MAT_SPECS"));
						ioData.put("UNIT_FNAME", bomData.getString("MAT_MAIN_UNIT"));
						ioData.put("FStatus", "Y");
						ioData.put("FExplanation", bomData.getString("MAT_BRAND"));
						ioData.put("PlannedQuantity", bomData.get("BOM_COUNT").toString());
						ioData.put("TType", "投入");
						ioData.put("MaterialID", bomData.getString("MAT_BASIC_ID"));
						ioData.put("CY_COUNT", bomData.get("CY_COUNT").toString());

						PageData matParam = new PageData();
						matParam.put("MAT_BASIC_ID", bomData.getString("MAT_BASIC_ID"));
						PageData findById = mat_basicService.findById(matParam);
						if (null != findById) {
							String UNIQUE_CODE_WHETHER = findById.getString("UNIQUE_CODE_WHETHER");
							if (StringUtil.isNotEmpty(UNIQUE_CODE_WHETHER)) {
								IFOneCode = UNIQUE_CODE_WHETHER;
								if ("是".equals(UNIQUE_CODE_WHETHER)) {
									ioData.put("FormulaQuantity", "1");
									ioData.put("PlannedQuantity", "1");
								}
							}
							ioData.put("fUnit", findById.getString("MAT_MAIN_UNIT"));
							ioData.put("MMAT_AUXILIARY_ID", findById.getString("MAT_AUXILIARY_ID"));
						}

						PageData mcParam = new PageData();
						mcParam.put("TType", pd.getString("TType"));
						mcParam.put("ConsumptionDocumentID", pd.getString("ProcessWorkOrderExample_ID"));
						mcParam.put("MaterialID", bomData.getString("MAT_BASIC_ID"));
						List<PageData> mcList = MaterialConsumeService.listAll(mcParam);
						BigDecimal ConsumptionQuantityBig = new BigDecimal("0");
						for (PageData pageData : mcList) {
							ConsumptionQuantityBig = ConsumptionQuantityBig
									.add(new BigDecimal(pageData.get("ConsumptionQuantity").toString()));
						}
						ioData.put("ConsumptionQuantity", ConsumptionQuantityBig.toString());

						ioData.put("IFOneCode", IFOneCode);
						varList.add(ioData);
					}
				}
			}
		}else{
			String IFOneCode = "否";
			PageData ioData = new PageData();
			ioData.put("MAT_NAME", pwoe.getString("ProcessIMateriel"));
			ioData.put("MaterialName", pwoe.getString("ProcessIMateriel"));
			ioData.put("MAT_CODE", pwoe.getString("ProcessIMaterielCode"));
			ioData.put("MaterialNum", pwoe.getString("ProcessIMaterielCode"));
			ioData.put("FormulaQuantity", "1");
		
			ioData.put("PlannedQuantity", "1");
			ioData.put("TType", "投入");
			ioData.put("MaterialID", pwoe.getString("ProcessIMaterielID"));

			PageData matParam = new PageData();
			matParam.put("MAT_BASIC_ID", pwoe.getString("ProcessIMaterielID"));
			PageData findById = mat_basicService.findById(matParam);
			if (null != findById) {
				String UNIQUE_CODE_WHETHER = findById.getString("UNIQUE_CODE_WHETHER");
				if (StringUtil.isNotEmpty(UNIQUE_CODE_WHETHER)) {
					IFOneCode = UNIQUE_CODE_WHETHER;
					if ("是".equals(UNIQUE_CODE_WHETHER)) {
						ioData.put("FormulaQuantity", "1");
						ioData.put("PlannedQuantity", "1");
					}
				}
				ioData.put("fUnit", findById.getString("MAT_MAIN_UNIT"));
				ioData.put("MAT_SPECS", findById.getString("MAT_SPECS"));
				ioData.put("UNIT_FNAME", findById.getString("FUNITNAME"));
				ioData.put("FStatus", "Y");
				ioData.put("FExplanation", findById.getString("MAT_BRAND"));
				ioData.put("MMAT_AUXILIARY_ID", findById.getString("MAT_AUXILIARY_ID"));
			}

			PageData mcParam = new PageData();
			mcParam.put("TType", pd.getString("TType"));
			mcParam.put("ConsumptionDocumentID", pd.getString("ProcessWorkOrderExample_ID"));
			mcParam.put("MaterialID", pwoe.getString("ProcessIMaterielID"));
			List<PageData> mcList = MaterialConsumeService.listAll(mcParam);
			BigDecimal ConsumptionQuantityBig = new BigDecimal("0");
			for (PageData pageData : mcList) {
				ConsumptionQuantityBig = ConsumptionQuantityBig
						.add(new BigDecimal(pageData.get("ConsumptionQuantity").toString()));
			}
			ioData.put("ConsumptionQuantity", ConsumptionQuantityBig.toString());

			ioData.put("IFOneCode", IFOneCode);
			varList.add(ioData);
			page.setCurrentPage(1);
			page.setCurrentResult(1);
			page.setTotalPage(1);
			page.setShowCount(1);
			
		}
		System.out.println(page.getCurrentPage());
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteIOData")
	@ResponseBody
	public Object deleteIOData() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WorkorderProcessIOExampleService.delete(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 配方核对任务列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/recipeCheckListPage")
	@ResponseBody
	public Object recipeCheckListPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// pd.put("PlanningWorkOrderID", pd.getString("PlanningWorkOrderID"));
		page.setPd(pd);
		List<PageData> varList = ProcessWorkOrderExampleService.recipeChecklistPage(page);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 配方核对任务-投入产出实例列表 根据工艺工单工序实例id查询投入产出实例列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findIOExampleByProcessWorkOrderExampleID")
	@ResponseBody
	public Object findIOExampleByProcessWorkOrderExampleID(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> recipeCheckIOList = WorkorderProcessIOExampleService
				.findIOExampleByProcessWorkOrderExampleID(pd);
		map.put("varList", recipeCheckIOList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 编辑是否核对
	 * 
	 * @param ProcessWorkOrderExample_ID
	 * @param CheckDoneIF
	 *            是、否
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editCheckDoneIF")
	@ResponseBody
	public Object editCheckDoneIF() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if ("是".equals(pd.getString("CheckDoneIF"))) {
			pd.put("CheckDoneIF", "是");
		} else {
			pd.put("CheckDoneIF", "否");
		}
		pd.put("Collator", Jurisdiction.getName());
		pd.put("ProofTime", Tools.date2Str(new Date()));
		ProcessWorkOrderExampleService.editCheckDoneIF(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 配方核对任务-投入产出实例列表 调整投入产出实例数量及描述
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editPlannedQuantityAndFRemarks")
	@ResponseBody
	public Object editPlannedQuantityAndFRemarks(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WorkorderProcessIOExampleService.editPlannedQuantityAndFRemarks(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 称重称量任务列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/weighinglistPage")
	@ResponseBody
	public Object weighinglistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = ProcessWorkOrderExampleService.weighinglistPage(page);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 编辑是否完成称量
	 * 
	 * @param ProcessWorkOrderExample_ID
	 * @param WeighingDoneIF
	 *            是、否
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editWeighingDoneIF")
	@ResponseBody
	public Object editWeighingDoneIF() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if ("是".equals(pd.getString("WeighingDoneIF"))) {
			pd.put("WeighingDoneIF", "是");
		} else {
			pd.put("WeighingDoneIF", "否");
		}
		ProcessWorkOrderExampleService.editWeighingDoneIF(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 编辑任务指定设备
	 * 
	 * @param ProcessWorkOrderExample_ID
	 * @param EQM_BASE_ID
	 *            设备id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editProcessWorkOrderExampleEqm")
	@ResponseBody
	public Object editProcessWorkOrderExampleEqm() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProcessWorkOrderExampleService.editProcessWorkOrderExampleEqm(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获取秤称量重量/秤清零
	 * 
	 * @param ProcessWorkOrderExample_ID
	 * @param WeighingDoneIF
	 *            是、否
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/weigh")
	@ResponseBody
	public Object weigh() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String type = pd.getString("type");
		String ClinkIP = pd.getString("ClinkIP");
		String ClinkPort = pd.getString("ClinkPort");
		String msgvalue = "";
		try {
			// String info="weigh"; //重量
			// String info="zero"; //清零
			// String info="tpreset80"; //设置皮重
			// String info="tare"; //获取皮重
			String monitorIp = ClinkIP;
			int monitorPort = Integer.parseInt(ClinkPort);
			// 1.建立客户端socket连接，指定服务器位置及端口
			Socket socket = new Socket(monitorIp, monitorPort);
			// 2.得到socket读写流
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			// 输入流
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			pw.write(type);
			pw.flush();
			socket.shutdownOutput();
			// 接收服务器的相应
			String reply = null;
			while (!((reply = br.readLine()) == null)) {
				msgvalue = reply;
				break;
			}
			// 4.关闭资源
			br.close();
			is.close();
			pw.close();
			os.close();
			socket.close();
			errInfo = "success";
			String num = getNumbers(msgvalue);
			String unit = splitNotNumber(msgvalue, num);
			map.put("msg", msgvalue);
			map.put("num", num);
			map.put("unit", unit);
		} catch (Exception e) {
			errInfo = "error";
			map.put("msg", "未知错误，请联系管理员！");
		} finally {
			map.put("result", errInfo);
		}
		return map;
	}

	public static String splitNotNumber(String content, String regex) {
		if (Tools.isEmpty(regex) || Tools.isEmpty(content)) {
			return "";
		}
		String[] arr = content.split(regex);
		return arr[1].trim();
	}

	public static String getNumbers(String content) {
		// 需要取整数和小数的字符串
		String str = content;
		// 控制正则表达式的匹配行为的参数(小数)
		Pattern p = Pattern.compile("(\\d+\\.\\d+)");
		// Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence
		// input)方法得到该类的实例.
		Matcher m = p.matcher(str);
		// m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
		if (m.find()) {
			// 如果有相匹配的,则判断是否为null操作
			// group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
			str = m.group(1) == null ? "0" : m.group(1);
		} else {
			// 如果匹配不到小数，就进行整数匹配
			p = Pattern.compile("(\\d+)");
			m = p.matcher(str);
			if (m.find()) {
				// 如果有整数相匹配
				str = m.group(1) == null ? "0" : m.group(1);
			} else {
				// 如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
				str = "0";
			}
		}
		return str;
	}

	/**
	 * 插入物料消耗表(不需要识别二维码的方法)
	 * 
	 * @param response
	 *            UserName code count WorkstationID
	 * @return
	 */
	@RequestMapping("/insertMaterialConsumeNoQRCode")
	@ResponseBody
	public Object insertMaterialConsumeNoQRCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			
			String ProcessWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
			PageData pdData = new PageData();
			pdData.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);
			PageData pwoe = ProcessWorkOrderExampleService.findById(pdData);
			String MAT_AUXILIARYMX_ID = "";
			if(null!=pwoe){
				String Cabinet_No = pwoe.getString("ProcessIMaterielCode");
				pdData.put("Cabinet_No", Cabinet_No);
				List<PageData> listAll = Cabinet_Assembly_DetailService.listAll(pdData);
				if(CollectionUtil.isNotEmpty(listAll)){
					String PPROJECT_CODE = listAll.get(0).getString("PPROJECT_CODE");
					PageData pg1 = new PageData();
					pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
					pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
					List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE =
							mat_auxiliarymxService.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
					if(CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)){
						MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0).getString("MAT_AUXILIARYMX_ID");
					}
				}
			}
			
			String USERNAME = Jurisdiction.getUsername();
			// 工位id
			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "工位参数没找到!");
				return map;
			}

			String materialId = pd.getString("MAT_BASIC_ID");
			String materialName = pd.getString("MAT_NAME");

			String count = pd.getString("count");

			String oneThingsCode = pd.getString("oneThingsCode");

			String qrCode = pd.getString("MAT_CODE");

			String materialSProp = "2540539e45324232a50bde60ac2951d3";

			String materialSPropKey = "";
			String specificationDesc = pd.getString("MAT_SPECS");
			String fUnit = pd.getString("fUnit");
			String fBatch = "";
			PageData pd1111 = WC_StationService.findById(pd);
			// 如果不存在一物码 则就是 类型码的物料则
			if (StringUtil.isEmpty(oneThingsCode)) {
				PageData pData = new PageData();
				// 类型码
				pData.put("QRCode", qrCode);
				if (null != pd1111) {
					pData.put("PositionID", pd1111.get("ConsumptionLocation"));
					pData.put("inputMat", "inputMat");
				}
				if(StringUtil.isNotEmpty(MAT_AUXILIARYMX_ID)){
					pData.put("MaterialSPropKey", MAT_AUXILIARYMX_ID);
				}
				List<PageData> listAll = stockService.listAll(pData);
				if (CollectionUtil.isNotEmpty(listAll)) {
					materialName = listAll.get(0).getString("MAT_NAME");
					materialSProp = listAll.get(0).getString("MaterialSProp") == null ? materialSProp
							: listAll.get(0).getString("MaterialSProp");
					materialSPropKey = listAll.get(0).getString("MaterialSPropKey") == null
							? "4486905b9e47428985c36c9d55d21ad7" : listAll.get(0).getString("MaterialSPropKey");
					materialId = listAll.get(0).getString("ItemID");
					if (Tools.isEmpty(materialId)) {
						errInfo = "error";
						map.put("result", errInfo);
						//map.put("msg", "该物料在库存中不存在");
						map.put("msg", "库存不足");
						return map;
					}
					specificationDesc = listAll.get(0).get("SpecificationDesc").toString();
					fUnit = listAll.get(0).getString("FUnit");
					fBatch = listAll.get(0).getString("FBatch");
				} else {
					errInfo = "error";
					map.put("result", errInfo);
					//map.put("msg", "该物料在库存中不存在");
					map.put("msg", "库存不足");
					return map;
				}
			} else {
				PageData pData = new PageData();
				// 一物码
				pData.put("OneThingCode", oneThingsCode);
				if (null != pd1111) {
					pData.put("PositionID", pd1111.get("ConsumptionLocation"));
					pData.put("inputMat", "inputMat");
				}
				List<PageData> listAll = stockService.listAll(pData);
				if (CollectionUtil.isNotEmpty(listAll)) {
					qrCode = listAll.get(0).getString("QRCode");
					materialName = listAll.get(0).getString("MAT_NAME");
					materialSProp = listAll.get(0).getString("MaterialSProp");
					materialSPropKey = listAll.get(0).getString("MaterialSPropKey");
					materialId = listAll.get(0).getString("ItemID");
					if (Tools.isEmpty(materialId)) {
						errInfo = "error";
						map.put("result", errInfo);
						//map.put("msg", "该物料在库存中不存在");
						map.put("msg", "库存不足");
						return map;
					}
					specificationDesc = listAll.get(0).get("SpecificationDesc").toString();
					fUnit = listAll.get(0).getString("FUnit");
					fBatch = listAll.get(0).getString("FBatch");
				} else {
					errInfo = "error";
					map.put("result", errInfo);
					//map.put("msg", "该物料在库存中不存在");
					map.put("msg", "库存不足");
					return map;
				}
			}

			PageData inputData = new PageData();

			// 根据 USERNAME 获取 staff info
			PageData pd1 = new PageData();
			pd1.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pd1);

			PageData findById = processWorkOrderExampleService.findById(pd);
			String BatchNum = findById.getString("BatchNum");
			String PlanningWorkOrderID = findById.getString("PlanningWorkOrderID");

			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			String stockSum = "0";
			if (null != stationInfo) {
				String WareHouseId = stationInfo.getString("ConsumptionWarehouse");
				// 消耗仓库 根据仓库获取该物料的 库存 如果库存不足，提示
				PageData pdKC = new PageData();
				pdKC.put("WarehouseID", WareHouseId);
				pdKC.put("ItemID", materialId);
				pdKC.put("MaterialSPropKey", MAT_AUXILIARYMX_ID);
				PageData pdStockSum = StockService.getSum(pdKC);// 即时库存

				if (null == pdStockSum) {
					errInfo = "error";
					map.put("result", errInfo);
					map.put("msg", "库存不足");
					return map;
				}

				if (null != pdStockSum) {

					if ("0.00".equals(pdStockSum.get("stockSum").toString())) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "库存不足");
						return map;

					}

					// 获取即时库存
					stockSum = pdStockSum.get("stockSum").toString();
					double stockSumDouble = Double.valueOf(stockSum);
					double countDouble = Double.valueOf(count);
					// 如果需求数量 大于了 即时库存数量 提示库存不足
					if (countDouble > stockSumDouble) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "库存不足");
						return map;
					}

					// 扣减库存
					PageData outParam = new PageData();
					outParam.put("num", countDouble);// 出库数量
					outParam.put("WarehouseID", WareHouseId);// 仓库id
					outParam.put("ItemID", materialId);// 物料id
					outParam.put("MaterialSPropKey", materialSPropKey);// 辅助属性值id
					StockService.outStock(outParam);

					// 插入到消耗产出表
					PageData materialConsume = new PageData();
					materialConsume.put("MaterialConsume_ID", this.get32UUID());
					materialConsume.put("ConsumptionDocumentID", pd.getString("ProcessWorkOrderExample_ID"));

					materialConsume.put("FType", "消耗");
					materialConsume.put("DataSources", "PP_ProcessWorkOrderExample");
					materialConsume.put("MaterialID", materialId);
					materialConsume.put("MaterialName", materialName);
					materialConsume.put("MaterialBarCode", qrCode);
					materialConsume.put("MaterialBatchNum", fBatch);
					materialConsume.put("SpecificationsAndModels", count + " " + specificationDesc);
					materialConsume.put("MaterialSProp", materialSProp);
					materialConsume.put("MaterialSPropKey", materialSPropKey);
					materialConsume.put("FUnit", fUnit);
					materialConsume.put("ConsumptionQuantity", count);
					materialConsume.put("WorkOrderRel", PlanningWorkOrderID);
					materialConsume.put("FOperatorID", staff.getString("STAFF_ID"));
					materialConsume.put("FOperateTime", Tools.date2Str(new Date()));
					materialConsume.put("BatchNum", BatchNum);
					materialConsume.put("OneThingCode", oneThingsCode);
					MaterialConsumeService.save(materialConsume);
					inputData.put("ProcessIMateriel", qrCode + "|" + materialName);
					// 返回冒泡信息
					PageData materialConsumeParam = new PageData();
					materialConsumeParam.put("ConsumptionDocumentID", pd.getString("ProcessWorkOrderExample_ID"));
					materialConsumeParam.put("FType", "消耗");
					materialConsumeParam.put("MaterialID", materialId);
					materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
					List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
					if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
						BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
						for (PageData pageData : MaterialConsumeList) {
							BigDecimal i = new BigDecimal(String.valueOf(pageData.get("ConsumptionQuantity")));
							ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
						}
						inputData.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
						inputData.put("PlannedQuantity", pd.get("PlannedQuantity"));
						BigDecimal PlannedQuantityBigDecimal = new BigDecimal(
								String.valueOf(pd.get("PlannedQuantity")));
						BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDecimal, 4,
								BigDecimal.ROUND_HALF_DOWN);
						BigDecimal multiply = divide.multiply(new BigDecimal("100"));
						if (multiply.doubleValue() > 100.0) {
							multiply = new BigDecimal("100");
						}
						BigDecimal subtract = PlannedQuantityBigDecimal.subtract(ConsumptionQuantityBigDec);
						if (subtract.doubleValue() < 0) {
							inputData.put("Subtract", 0);
						} else {
							inputData.put("Subtract", subtract.doubleValue());
						}
						inputData.put("InputPercent", multiply.doubleValue());
					} else {
						inputData.put("ConsumptionQuantity", 0);
						inputData.put("PlannedQuantity", 0);
						inputData.put("InputPercent", 0);
						inputData.put("Subtract", 0);
					}

				}
			}
			errInfo = "success";
			map.put("result", errInfo);
			map.put("msg", "物料消耗成功");
			return map;

		} catch (Exception e) {
			errInfo = "error";
			map.put("result", errInfo);
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * 插入物料消耗表
	 * 
	 * @param response
	 *            UserName code count WorkstationID
	 * @return
	 */
	@RequestMapping("/insertMaterialConsume")
	@ResponseBody
	public Object insertMaterialConsume() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String USERNAME = Jurisdiction.getUsername();
			// 工位id
			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "工位参数没找到!");
				return map;
			}

			/*
			 * 1、根据二维码 和数量 新增消耗记录 2、影响车间库库存
			 * 3、如果该物料在物料投产表内，将其加载到当前操作位置（返回总体需求量和已投产数据）
			 */
			String code = pd.getString("code");
			if (Tools.isEmpty(code)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "二维码无法识别");
				return map;
			}
			String count = pd.getString("count");

			String materialName = "";
			String oneThingsCode = "";
			String qrCode = "";
			String materialSProp = "2540539e45324232a50bde60ac2951d3";
			String materialSPropKey = "";
			String materialId = "";
			String specificationDesc = "";

			String fUnit = "";
			String fBatch = "";
			PageData inputData = new PageData();
			// 唯一码 带出 物料码 和 序列号
			if ("W".equals(code.substring(0, 1))) {
				// 根据唯一码去库存中查找物料叫啥
				String[] split = code.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					oneThingsCode = codeSplitList.get(1);
					PageData pData = new PageData();
					// 一物码
					pData.put("OneThingCode", oneThingsCode);
					List<PageData> listAll = stockService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						qrCode = listAll.get(0).getString("QRCode");
						materialName = listAll.get(0).getString("MAT_NAME");
						materialSProp = listAll.get(0).getString("MaterialSProp");
						materialSPropKey = listAll.get(0).getString("MaterialSPropKey");
						materialId = listAll.get(0).getString("ItemID");
						if (Tools.isEmpty(materialId)) {
							errInfo = "error";
							map.put("result", errInfo);
							map.put("msg", "该物料在库存中不存在");
							return map;
						}
						specificationDesc = listAll.get(0).get("SpecificationDesc").toString();
						fUnit = listAll.get(0).getString("FUnit");
						fBatch = listAll.get(0).getString("FBatch");
					}
				}
			}
			// 类型码 带出 物料码 和 序列号
			if ("L".equals(code.substring(0, 1))) {
				// 根据类型码去库存中查找物料叫啥
				String[] split = code.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					qrCode = codeSplitList.get(1);
					PageData pData = new PageData();
					// 类型码
					pData.put("QRCode", qrCode);
					List<PageData> listAll = stockService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						materialName = listAll.get(0).getString("MAT_NAME");
						materialSProp = listAll.get(0).getString("MaterialSProp");
						materialSPropKey = listAll.get(0).getString("MaterialSPropKey");
						materialId = listAll.get(0).getString("ItemID");
						if (Tools.isEmpty(materialId)) {
							errInfo = "error";
							map.put("result", errInfo);
							map.put("msg", "该物料在库存中不存在");
							return map;
						}
						specificationDesc = listAll.get(0).get("SpecificationDesc").toString();
						fUnit = listAll.get(0).getString("FUnit");
						fBatch = listAll.get(0).getString("FBatch");
					}
				}

			}
			if (Tools.isEmpty(materialId)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "该物料在库存中不存在");
				return map;
			}
			// 根据 USERNAME 获取 staff info
			PageData pd1 = new PageData();
			pd1.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pd1);

			PageData findById = processWorkOrderExampleService.findById(pd);
			String BatchNum = findById.getString("BatchNum");
			String PlanningWorkOrderID = findById.getString("PlanningWorkOrderID");

			// 查投入产出实例表 看该物料是否在当前任务下 允许投入
			PageData wpioeParam = new PageData();
			wpioeParam.put("processWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
			List<String> array = Lists.newArrayList();
			array.add(materialId);
			wpioeParam.put("array", array);
			wpioeParam.put("TType", "投入");
			List<PageData> wpioeList = workorderProcessIOExampleService.listAll(wpioeParam);
			if (CollectionUtil.isEmpty(wpioeList)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "该物料不适用于该任务");
				return map;
			}

			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			String stockSum = "0";
			if (null != stationInfo) {
				String WareHouseId = stationInfo.getString("ConsumptionWarehouse");
				// 消耗仓库 根据仓库获取该物料的 库存 如果库存不足，提示
				PageData pdKC = new PageData();
				pdKC.put("WarehouseID", WareHouseId);
				pdKC.put("ItemID", materialId);
				PageData pdStockSum = StockService.getSum(pdKC);// 即时库存

				if (null == pdStockSum) {
					errInfo = "error";
					map.put("result", errInfo);
					map.put("msg", "库存不足");
					return map;
				}

				if (null != pdStockSum) {

					if ("0.00".equals(pdStockSum.get("stockSum").toString())) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "库存不足");
						return map;

					}

					// 获取即时库存
					stockSum = pdStockSum.get("stockSum").toString();
					double stockSumDouble = Double.valueOf(stockSum);
					double countDouble = Double.valueOf(count);
					// 如果需求数量 大于了 即时库存数量 提示库存不足
					if (countDouble > stockSumDouble) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "库存不足");
						return map;
					}

					// 扣减库存
					PageData outParam = new PageData();
					outParam.put("num", countDouble);// 出库数量
					outParam.put("WarehouseID", WareHouseId);// 仓库id
					outParam.put("ItemID", materialId);// 物料id
					outParam.put("MaterialSPropKey", materialSPropKey);// 辅助属性值id
					StockService.outStock(outParam);
					String WorkorderProcessIOExample_ID = wpioeList.get(0).getString("WorkorderProcessIOExample_ID");

					// 插入到消耗产出表
					PageData materialConsume = new PageData();
					materialConsume.put("MaterialConsume_ID", this.get32UUID());
					materialConsume.put("ConsumptionDocumentID", WorkorderProcessIOExample_ID);
					materialConsume.put("FType", "消耗");
					materialConsume.put("DataSources", "PP_WorkorderProcessIOExample");
					materialConsume.put("MaterialID", materialId);
					materialConsume.put("MaterialName", materialName);
					materialConsume.put("MaterialBarCode", qrCode);
					materialConsume.put("MaterialBatchNum", fBatch);
					materialConsume.put("SpecificationsAndModels", count + " " + specificationDesc);
					materialConsume.put("MaterialSProp", materialSProp);
					materialConsume.put("MaterialSPropKey", materialSPropKey);
					materialConsume.put("FUnit", fUnit);
					materialConsume.put("ConsumptionQuantity", count);
					materialConsume.put("WorkOrderRel", PlanningWorkOrderID);
					materialConsume.put("FOperatorID", staff.getString("STAFF_ID"));
					materialConsume.put("FOperateTime", Tools.date2Str(new Date()));
					materialConsume.put("BatchNum", BatchNum);
					materialConsume.put("OneThingCode", oneThingsCode);
					MaterialConsumeService.save(materialConsume);
					inputData.put("ProcessIMateriel", qrCode + "|" + materialName);
					// 返回冒泡信息
					PageData materialConsumeParam = new PageData();
					materialConsumeParam.put("ConsumptionDocumentID", WorkorderProcessIOExample_ID);
					materialConsumeParam.put("FType", "消耗");
					materialConsumeParam.put("MaterialID", materialId);
					materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
					List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
					if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
						BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
						for (PageData pageData : MaterialConsumeList) {
							BigDecimal i = new BigDecimal(String.valueOf(pageData.get("ConsumptionQuantity")));
							ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
						}
						inputData.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
						inputData.put("PlannedQuantity", wpioeList.get(0).get("PlannedQuantity"));
						BigDecimal PlannedQuantityBigDecimal = new BigDecimal(
								String.valueOf(wpioeList.get(0).get("PlannedQuantity")));
						BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDecimal, 4,
								BigDecimal.ROUND_HALF_DOWN);
						BigDecimal multiply = divide.multiply(new BigDecimal("100"));
						if (multiply.doubleValue() > 100.0) {
							multiply = new BigDecimal("100");
						}
						BigDecimal subtract = PlannedQuantityBigDecimal.subtract(ConsumptionQuantityBigDec);
						if (subtract.doubleValue() < 0) {
							inputData.put("Subtract", 0);
						} else {
							inputData.put("Subtract", subtract.doubleValue());
						}
						inputData.put("InputPercent", multiply.doubleValue());
					} else {
						inputData.put("ConsumptionQuantity", 0);
						inputData.put("PlannedQuantity", 0);
						inputData.put("InputPercent", 0);
						inputData.put("Subtract", 0);
					}

				}
			}
			errInfo = "success";
			map.put("result", errInfo);
			map.put("msg", "物料消耗成功");
			return map;

		} catch (Exception e) {
			errInfo = "error";
			map.put("result", errInfo);
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * 根据物料id获取该物料在该任务的投产列表
	 * 
	 * @param page
	 * @param response
	 * @return
	 */
	@RequestMapping("getConsumeListByMaterialId")
	@ResponseBody
	public Object getConsumeListByMaterialId(Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pData = new PageData();
			// 任务id 查 投入产出

			pData.put("ConsumptionDocumentID", pd.getString("ProcessWorkOrderExample_ID"));
			pData.put("MaterialID", pd.getString("MaterialID"));
			pData.put("FType", pd.getString("FType"));
			page.setPd(pData);
			// 查询投产列表
			List<PageData> varList = MaterialConsumeService.list(page);
			for (PageData pageData : varList) {
				String FOperateTimeStr = pageData.getString("FOperateTime");
				FOperateTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Tools.str2Date(FOperateTimeStr));
				pageData.put("FOperateTime", FOperateTimeStr);
				// 处理操作人 和 拼接 物料名称和code
				String FOperatorID = pageData.getString("FOperatorID");
				PageData pdData = new PageData();
				pdData.put("STAFF_ID", FOperatorID);
				PageData staffInfo = staffService.findById(pdData);
				String NAME = staffInfo.getString("NAME");
				pageData.put("FOperatorName", NAME);
				pageData.put("ProcessIMateriel",
						pageData.getString("MaterialBarCode") + "|" + pageData.getString("MaterialName"));

				String FUnitID = pageData.getString("FUnit");
				PageData pData2 = new PageData();
				pData2.put("UNIT_INFO_ID", FUnitID);
				PageData unitInfo = Unit_InfoService.findById(pData2);
				pageData.put("FUnit", unitInfo.getString("FNAME"));

			}
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", errInfo);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
			map.put("msg", e.getMessage());
			map.put("result", errInfo);
			return map;
		}
	}

	/***
	 * 插入物料产出
	 * 
	 * @param MAT_BASIC_ID
	 *            WC_STATION_ID MAT_NAME MAT_CODE MAT_NAME MAT_SPECS SPropKeyID
	 *            MAT_AUX_UNIT_ID MAT_SPECS_QTY PlanningWorkOrderID SerialNum
	 *            Count
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insertMaterialOutput")
	@ResponseBody
	public Object insertMaterialOutput() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			// 查投入产出实例表 看该物料是否在当前任务下 允许投入
			String materialId = pd.getString("MAT_BASIC_ID");
	
			// 工位id
			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "工位参数没找到!");
				return map;
			}
			PageData findById = processWorkOrderExampleService.findById(pd);
			String BatchNum = findById.getString("BatchNum");
		
			Object materialName = pd.getString("MAT_NAME");
			Object qrCode = pd.getString("MAT_CODE");
			Object fBatch = pd.getString("MAT_NAME");
			Object specificationDesc = pd.getString("MAT_SPECS");
			Object materialSProp = "2540539e45324232a50bde60ac2951d3";
			Object materialSPropKey = pd.getString("SPropKeyID");
			Object fUnit = pd.getString("MAT_AUX_UNIT_ID");
			String MAT_SPECS_QTY = pd.getString("ConsumptionQuantity");
			Object PlanningWorkOrderID = pd.getString("PlanningWorkOrderID");
			Object oneThingsCode = pd.getString("SerialNum");
			Integer count = Integer.valueOf(pd.getString("Count"));
			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			PageData pageData = new PageData();
			pageData.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
			PageData pwo = planningWorkOrderService.findById(pageData);
			if (null == pwo) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "子计划工单没找到!");
				return map;
			}
			PageData pdOp = new PageData();
			String name = pd.getString("userName");
			pdOp.put("FNAME", name);
			pdOp = StaffService.getStaffId(pdOp);
			if (pdOp == null) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "操作人没有找到，请关闭产出页面重新扫人员码！");
				return map;
			}
			String STAFF_ID = pdOp.getString("STAFF_ID");
			if (null != stationInfo) {
				for (int i = 0; i < count; i++) {
					// 插入到产出表
					PageData materialConsume = new PageData();
					materialConsume.put("MaterialConsume_ID", this.get32UUID());
					materialConsume.put("ConsumptionDocumentID", pd.getString("ProcessWorkOrderExample_ID"));
					materialConsume.put("FType", "产出");
					materialConsume.put("DataSources", "PP_ProcessWorkOrderExample");
					materialConsume.put("MaterialID", materialId);
					materialConsume.put("MaterialName", materialName);
					materialConsume.put("MaterialBarCode", qrCode);
					materialConsume.put("MaterialBatchNum", fBatch);
					materialConsume.put("SpecificationsAndModels", specificationDesc);
					materialConsume.put("MaterialSProp", materialSProp);
					materialConsume.put("MaterialSPropKey", materialSPropKey);
					materialConsume.put("FUnit", fUnit);
					materialConsume.put("ConsumptionQuantity", Double.valueOf(MAT_SPECS_QTY));
					materialConsume.put("WorkOrderRel", PlanningWorkOrderID);
					materialConsume.put("FOperatorID", STAFF_ID);
					materialConsume.put("FOperateTime", Tools.date2Str(new Date()));
					materialConsume.put("BatchNum", BatchNum);
					materialConsume.put("OneThingCode", oneThingsCode);
					materialConsume.put("OutType", "正常");
					MaterialConsumeService.save(materialConsume);
					// 根据工位id获取绑定的仓库和库位 产出物料加库存
					String WareHouseId = stationInfo.getString("ProduceWarehouse");
					if (Tools.isEmpty(WareHouseId)) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "工位没有绑定默认仓库!");
						return map;
					}
					String LocationId = stationInfo.getString("ProduceLocation");
					if (Tools.isEmpty(LocationId)) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "工位没有绑定默认仓库!");
						return map;
					}
					// 增加库存
					PageData inParam = new PageData();
					inParam.put("Stock_ID", this.get32UUID());
					inParam.put("WarehouseID", WareHouseId);// 仓库id
					inParam.put("PositionID", LocationId);// 库位id
					inParam.put("ItemID", materialId);// 物料id
					inParam.put("StorageStatus", "工厂");// 存储状态(工厂,车间)
					inParam.put("QRCode", qrCode);// 物料二维码
					inParam.put("OneThingCode", oneThingsCode);// 一物码
					inParam.put("MaterialSProp", materialSProp);// 物料辅助属性
					inParam.put("MaterialSPropKey", materialSPropKey);// 物料辅助属性值
					inParam.put("SpecificationDesc", specificationDesc);// 规格
					inParam.put("ActualCount", MAT_SPECS_QTY);// 实际数量
					inParam.put("FBatch", BatchNum);// 批号
					inParam.put("FUnit", fUnit);// 单位
					inParam.put("ProductionBatch", BatchNum);// 生产批号

					inParam.put("UseCount", 0);// 使用数量
					inParam.put("UseIf", "NO");// 是否占用
					inParam.put("FLevel", 1);// 等级

					inParam.put("RunningState", "在用");// 运行状态
					inParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
					inParam.put("UsageTime", Tools.date2Str(new Date()));// 使用时间
					inParam.put("DateOfManufacture", Tools.date2Str(new Date()));// 生产日期

					inParam.put("LastModifiedTime", Tools.date2Str(new Date()));// 最后修改日期
					inParam.put("LastCountTime", Tools.date2Str(new Date()));// 上次盘点日期
					inParam.put("FStatus", "入库");// 状态

					inParam.put("ProjectNum", pwo.getString("WorkOrderNum"));// 计划工单编号
					inParam.put("CustomerID", pwo.getString("FCustomer"));// 客户

					StockService.inStock(inParam);
				}
			}
			// 消息提醒
			String FFTYPE = pd.getString("FFTYPE");
			if (null != FFTYPE && FFTYPE.equals("生产任务")) {
				PageData pdNotice = new PageData();
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				// 跳转页面
			    pdNotice.put("AccessURL", "../../../views/pp/PlanningWorkOrder/PlanningWorkOrder_list.html");// 
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"工单编号为" + findById.getString("WorkOrderNum") + "，产出物料为"
								+ findById.getString("ProcessIMaterielCode") + "的生产任务已产出");// 消息正文
				pdNotice.put("FTitle", "生产任务产出");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "生产任务产出");// 数据来源
				String ReceivingAuthority = ",";
				ReceivingAuthority += findById.getString("FSC_NO");
				ReceivingAuthority = ReceivingAuthority + ",";
				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("Report_Value", "");// 手机app
				noticeService.save(pdNotice);
				SendWeChatMessageMes weChat = new SendWeChatMessageMes();
				 PageData pd3 = new PageData();
				 //String Name = Jurisdiction.getName();
				 String Name = "管悦";
				 pd3.put("NAME", Name);
				// PageData pd2 = usersService.getPhone(pd3);
				 String phone = "";
				/* if(null!=pd2){
					 phone = pd2.getString("phone");
				 }else{
					 phone="";
				 }*/
				 String WXNR="【生产任务产出】\r\n"
							+ "发布人："+Jurisdiction.getName()+"\r\n"
							+ "发布时间："+Tools.date2Str(new Date())+"\r\n"
							+ "消息内容："+pdNotice.get("FContent")
							;
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR, "0");
				 weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
				// 手机app-极光推送
				String UserName = findById.getString("FSC_NO");
				pd.put("UserName", UserName);
				PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
				if (pdf != null) {
					String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
					String notification_title = "工作通知";
					String msg_title = "生产任务产出";
					String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
							+ findById.getString("FName") + "，产出物料为" + findById.getString("ProcessIMaterielCode")
							+ "的生产任务已产出";
					if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title, msg_title, msg_content,
							"") == 1) {
						// errInfo="success";
					} else {
						// errInfo="error";
					}
				}
			}
			map.put("result", errInfo);
			map.put("msg", "产出提交成功!");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
			map.put("result", errInfo);
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * 产出打码获取物料信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/outMatDetail")
	@ResponseBody
	public Object outMatDetail() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pdCode = new PageData();
			BigDecimal FCode = new BigDecimal(0);
			PageData pdMx = new PageData();

			// 物料id
			String MAT_BASIC_ID = pd.getString("MAT_BASIC_ID");
			if (Tools.isEmpty(MAT_BASIC_ID)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "物料id为空");
				return map;
			}
			pdMx.put("MAT_BASIC_ID", MAT_BASIC_ID);

			// 子计划工单id
			String PlanningWorkOrderID = pd.getString("PlanningWorkOrderID");
			if (Tools.isEmpty(PlanningWorkOrderID)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "子计划工单id为空");
				return map;
			}
			pdMx.put("PlanningWorkOrderID", PlanningWorkOrderID);

			PageData pdData = new PageData();
			pdData.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
			PageData subPlan = planningWorkOrderService.findById(pdData);
			String SPropKeyName = subPlan.getString("MasterWorkOrderNum");
			pdData.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
			pdData.put("MAT_AUXILIARYMX_CODE", SPropKeyName);

			// 获取MasterWorkOrder WorkOrderNum 根据号码 和 SPROP 获取 auxMX Id 返回前台
			List<PageData> MAT_AUXILIARYMX_INFO = MAT_AUXILIARYMxService
					.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pdData);
			if (CollectionUtil.isNotEmpty(MAT_AUXILIARYMX_INFO)) {
				pdMx.put("SPropKeyID", MAT_AUXILIARYMX_INFO.get(0).get("MAT_AUXILIARYMX_ID"));
			}
			PageData matParam = new PageData();
			matParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
			pd = matBasicService.findById(matParam);

			if (null == pd) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "物料不存在");
				return map;
			}

			String Ftype = "类型码";
			String ForOne = "否";
			if ("是".equals(pd.getString("UNIQUE_CODE_WHETHER"))) {
				Ftype = "唯一码";
				ForOne = "是";
			}
			pdMx.put("ForOne", ForOne);

			pd.put("Ftype", Ftype);

			// 类型码唯一码处理
			if ("类型码".equals(Ftype)) {
				pdCode = MAKECODEService.getCode(pd);
				String encode = "L,YL," + pd.getString("MAT_CODE") + ",YL," + pd.get("MAT_SPECS_QTY") + ",YL,"
						+ SPropKeyName;

				pdMx.put("Encode", encode);

				pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
				pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

				PageData unitMainParam = new PageData();
				unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
				PageData unitMain = Unit_InfoService.findById(unitMainParam);
				String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
				pdMx.put("MAT_SPECS_QTY", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT);

				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				String MAT_AUX_UNIT = "默认包装";
				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
				}
				pdMx.put("MAT_AUX_UNIT_ID", pd.get("MAT_MAIN_UNIT"));
				pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
				pdMx.put("SPropKey", SPropKeyName);
				pdMx.put("MakeTime", Tools.date2Str(new Date()));

				// 类型码 0
				pdMx.put("SerialNum", FCode);

			}
			if ("唯一码".equals(Ftype)) {
				pdCode = MAKECODEService.getCode(pd);
				FCode = new BigDecimal(pdCode.get("FCode").toString());

				FCode = FCode.add(new BigDecimal(1));
				String encode = "W,YL," + FCode + ",YL," + pd.get("MAT_SPECS_QTY") + ",YL," + SPropKeyName;
				pdMx.put("Encode", encode);

				pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
				pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

				PageData unitMainParam = new PageData();
				unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
				PageData unitMain = Unit_InfoService.findById(unitMainParam);
				String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
				pdMx.put("MAT_SPECS_QTY", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT);

				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				String MAT_AUX_UNIT = "默认包装";
				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
				}
				pdMx.put("MAT_AUX_UNIT_ID", pd.get("MAT_MAIN_UNIT"));
				pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
				pdMx.put("SPropKey", SPropKeyName);
				pdMx.put("MakeTime", Tools.date2Str(new Date()));

				// 唯一码有 序列号
				pdMx.put("SerialNum", FCode);

			}

			matParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
			// 根据物料id获取物料基础信息
			pd = matBasicService.findById(matParam);
			String MAT_AUX_UNIT = "默认包装";
			// 获取主单位名称
			PageData unitMainParam = new PageData();
			unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
			PageData unitMain = Unit_InfoService.findById(unitMainParam);

			String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
			PageData matspecParam = new PageData();
			matspecParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
			List<PageData> listAll = MAT_SPECService.listAll(matspecParam);
			List<String> matSpecList = Lists.newArrayList();
			// 根据物料id获取所有辅单位信息 添加其他规格
			for (int i = 0; i < listAll.size(); i++) {
				String MAT_SPEC_QTY = listAll.get(i).get("MAT_SPEC_QTY").toString();
				String UNIT_INFO_ID = listAll.get(i).getString("UNIT_INFO_ID");
				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", UNIT_INFO_ID);
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
					String matSpec = "";
					// 拼接返回值
					matSpec += MAT_SPEC_QTY + " " + MAT_MAIN_UNIT + "/" + MAT_AUX_UNIT;
					matSpecList.add(matSpec);
				}
			}
			pdMx.put("matSpecList", matSpecList);
			pd.put("FCode", FCode);
			MAKECODEService.editCode(pd);
			map.put("result", errInfo);
			map.put("pdMx", pdMx);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
			map.put("result", errInfo);
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * 任务关联计划列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/workOrderExamplelistPage")
	@ResponseBody
	public Object workOrderExamplelistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = ProcessWorkOrderExampleService.workOrderExamplelistPage(page);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 扫码验证是否是该任务的物料
	 * v1 管悦 2021-07-09
	 * @param response
	 * @return
	 */
	@RequestMapping("/scanMaterial")
	@ResponseBody
	public Object scanMaterial() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			if (Tools.isEmpty(code)) {
				map.put("result", "error");
				map.put("msg", "二维码无法识别");
				return map;
			}

			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				map.put("result", "error");
				map.put("msg", "工位无法识别");
				return map;
			}
			PageData pwoe = processWorkOrderExampleService.findById(pd);
			PageData bomParam = new PageData();
			bomParam.put("Cabinet_Assembly_Detail_ID", pd.getString("Cabinet_Assembly_Detail_ID"));
			bomParam.put("MAT_CODE", code);
			List<PageData> bomList = Cabinet_BOMService.listAllGroupByMat(bomParam);
			if (CollectionUtil.isEmpty(bomList)) {
				map.put("result", "error");
				map.put("msg", "该物料不适用于该任务");
				return map;
			}
			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			String stockSum = "0";
			if (null != stationInfo) {
			
			if (CollectionUtil.isNotEmpty(bomList)) {
				PageData pageData = bomList.get(0);
				String WareHouseId = stationInfo.getString("ConsumptionWarehouse");
				// 消耗仓库 根据仓库获取该物料的 库存 如果库存不足，提示
				PageData pdKC = new PageData();
				pdKC.put("WarehouseID", WareHouseId);
				pdKC.put("ItemID", pageData.get("MAT_BASIC_ID"));
				PageData pdStockSum = StockService.getSum(pdKC);// 即时库存

				if (null != pdStockSum) {
					stockSum = pdStockSum.get("stockSum").toString();
				}
				pd.put("StockSumDouble", stockSum);
				pd.put("MaterialCode", pageData.get("MAT_CODE"));
				pd.put("MaterialName", pageData.get("MAT_NAME"));
				pd.put("PlannedQuantity", pageData.get("BOM_COUNT").toString());
				pd.put("UNIT", pageData.get("MAT_MAIN_UNIT"));
				pd.put("stockSumDouble", stockSum);
				PageData mcParam = new PageData();
				mcParam.put("TType", pd.getString("TType"));
				mcParam.put("ConsumptionDocumentID", pd.getString("ProcessWorkOrderExample_ID"));
				mcParam.put("MaterialID", pwoe.getString("ProcessIMaterielID"));
				List<PageData> mcList = MaterialConsumeService.listAll(mcParam);
				BigDecimal ConsumptionQuantityBig = new BigDecimal("0");
				for (PageData pageDatax : mcList) {
					ConsumptionQuantityBig = ConsumptionQuantityBig
							.add(new BigDecimal(pageDatax.get("ConsumptionQuantity").toString()));
				}
				pd.put("ConsumptionQuantity", ConsumptionQuantityBig.toString());
				pd.put("MAT_BASIC_ID", pageData.get("MAT_BASIC_ID"));
				PageData matParam = new PageData();
				matParam.put("MAT_BASIC_ID",  pageData.get("MAT_BASIC_ID"));
				PageData findById = mat_basicService.findById(matParam);
				if (null != findById) {
					pd.put("fUnit", findById.getString("MAT_MAIN_UNIT"));
					pd.put("MMAT_AUXILIARY_ID", findById.getString("MAT_AUXILIARY_ID"));
				}

			} else {
				
			}

			// ============================end===================================//

			map.put("result", "success");
			map.put("pd", pd);
			map.put("msg", "可以进行扫码投产,返回物料即时库存");
			return map;
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e);
			return map;
		}
		return map;
	}
}
