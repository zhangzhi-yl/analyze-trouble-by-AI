package org.yy.controller.qm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.app.AppRegistrationService;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.QualityInspectionPlanInspectionDetailsService;
import org.yy.service.km.QualityInspectionPlanService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.StockListDetailService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.WC_StationService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.pp.WorkRecordService;
import org.yy.service.qm.O_RECORD_QUALITYTASKService;
import org.yy.service.qm.QualityInspectionPlanDetailExecuteService;
import org.yy.service.qm.QualityInspectionPlanExecuteSampleService;
import org.yy.service.qm.QualityInspectionPlanExecuteService;
import org.yy.util.DateUtil;
import org.yy.util.JpushClientUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

import com.github.pagehelper.util.StringUtil;

/**
 * 说明：质检任务 作者：范贺男 时间：2020-11-12
 */
@Controller
@RequestMapping("/QualityInspectionPlanExecute")
public class QualityInspectionPlanExecuteController extends BaseController {
	@Autowired
	private ProcessWorkOrderExampleService processWorkOrderExampleService;
	@Autowired
	private QualityInspectionPlanExecuteService QualityInspectionPlanExecuteService;

	@Autowired
	private QualityInspectionPlanDetailExecuteService QualityInspectionPlanDetailExecuteService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private ProcessWorkOrderExampleService ProcessWorkOrderExampleService;
	@Autowired
	private QualityInspectionPlanExecuteSampleService QualityInspectionPlanExecuteSampleService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private AppRegistrationService AppRegistrationService;
	@Autowired
	private StockListDetailService StockListDetailService;
	@Autowired
	private CodingRulesService CodingRulesService;
	@Autowired
	private QualityInspectionPlanInspectionDetailsService QualityInspectionPlanInspectionDetailsService;
	@Autowired
	private QualityInspectionPlanService QualityInspectionPlanService;
	@Autowired
	private WC_StationService wc_stationService;// 工位
	@Autowired
	private StaffService staffService;
	@Autowired
	private WorkRecordService WorkRecordService;
	@Autowired
	private O_RECORD_QUALITYTASKService o_record_qualitytaskService;

	/**
	 * 入库单生成质检任务
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addRKQM")
	@ResponseBody
	public Object addRKQM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdStaff = new PageData();
		Random random = new Random();
		int ends = random.nextInt(99);
		String.format("%02d", ends);
		pd = this.getPageData();
		List<PageData> varList = StockListDetailService.listAll(pd);// 入库单明细列表
		pd.put("QIType", "入厂检");
		pd.put("FFTYPE", "入厂检");
		pd.put("FFDEPT", "品控部");

		List<PageData> QIList = QualityInspectionPlanExecuteService.listQI(pd);// 入厂检方案列表
		for (int i = 0; i < varList.size(); i++) {
			String MATID = varList.get(i).get("FCode").toString();// 物料ID
			for (int j = 0; j < QIList.size(); j++) {
				pd.put("QIPlanID", QIList.get(j).get("QualityInspectionPlan_ID").toString());
				List<PageData> matList = QualityInspectionPlanExecuteService.listQIMAT(pd);// 质检方案下物料列表

				// 根据质检任务 质检方式值 计算出 需要抽检的总样品数
				String QIMethod = QIList.get(j).getString("QIMethod");
				String QIMethodValue = QIList.get(j).getString("QIMethodValue");
				Double FCount = new Double(varList.get(i).get("MaterialQuantity").toString());
				if ("全检".equals(QIMethod)) {

				}
				if ("比例抽检".equals(QIMethod)) {
					FCount = FCount * new Double(QIMethodValue) / 100;
				}
				if ("自定义抽检".equals(QIMethod)) {
					FCount = new Double(QIMethodValue);
				}

				for (int z = 0; z < matList.size(); z++) {
					String ApplicableMaterialID = matList.get(z).get("ApplicableMaterialID").toString();
					if (MATID.equals(ApplicableMaterialID)) {
						pd.put("FNAME", Jurisdiction.getName()); // 获取当前登录人的中文名称
						pdStaff = staffService.getStaffId(pd); // 根据人员的中文姓名获取人员ID
						pd.put("QualityInspectionPlanExecute_ID", this.get32UUID()); // 主键
						pd.put("GenerationType", "呼叫质检");// 生产类型为临时创建
						pd.put("FMakeBillsPersoID", pdStaff.get("STAFF_ID"));// 制单人
						pd.put("FMakeBillsTime", Tools.date2Str(new Date())); // 制单时间
						pd.put("DataSources", "入库单据");
						pd.put("TaskNum", "ZJRW" + Tools.date2Str(new Date(), "yyyyMMdd") + ends);// 任务单号
						pd.put("FOperator", "部门");
						pd.put("InspectorID", "33cbc85b1aee46fba02944d381dda6b7");// 部门ID
						pd.put("FType", "20dd7fd1a5ce4b7d8589c5c20abcfb22");// 质检类型
						pd.put("RequireEndTime", Tools.date2Str(new Date(), "yyyy-MM-dd"));// 要求完成时间
						pd.put("QIPosition", "入厂口");// 质检位置
						pd.put("MaterialID", MATID);// 物料
						pd.put("FPriorityID", "1");// 优先级
						pd.put("FCount", FCount);// 校验总数
						pd.put("RowNum", varList.get(i).get("RowNum").toString());
						pd.put("ExecutionStatus", "已下发");
						pd.put("GenerationType", "入库质检");
						pd.put("StockListInRel", varList.get(i).get("DocumentNo").toString());
						QualityInspectionPlanExecuteService.save(pd);

						// 添加一条样品 默认是 总数 自检 标签码自动生成
						PageData pdSample = new PageData();
						pdSample.put("QualityInspectionPlanExecuteSample_ID", this.get32UUID()); // 主键
						pdSample.put("QualityInspectionPlanExecute_ID",
								pd.getString("QualityInspectionPlanExecute_ID"));
						String sampleCode = String.valueOf(CodingRulesService.getRuleNumByRuleType("SAMPLE"));
						pdSample.put("TagCode", sampleCode);
						pdSample.put("SampleCount", FCount);
						pdSample.put("SampleRegisterTime", Tools.date2Str(new Date()));
						pdSample.put("InspectionParty", "自检");
						pdSample.put("FState", "执行中");
						QualityInspectionPlanExecuteSampleService.save(pdSample);
						PageData pData = new PageData();
						pData.put("QIPlanID", QIList.get(j).get("QualityInspectionPlan_ID").toString());
						List<PageData> listAll = QualityInspectionPlanInspectionDetailsService.listAll(pData);
						for (PageData pageData : listAll) {
							pageData.put("QualityInspectionPlanExecuteSample_ID",
									pdSample.getString("QualityInspectionPlanExecuteSample_ID"));
							pageData.put("QualityInspectionPlanExecute_ID",
									pdSample.getString("QualityInspectionPlanExecute_ID"));
							pageData.put("QualityInspectionPlanDetailExecute_ID", this.get32UUID());
							pageData.put("FinishOnce", 0);
							QualityInspectionPlanDetailExecuteService.save(pageData);
						}
						operationrecordService.add("", "质检任务", "添加", pd.getString("QualityInspectionPlanExecute_ID"),
								"", "");// 操作日志
						// 修改生产任务中是否生成质检任务字段
						PageData pdPP = new PageData();
						pdPP.put("ProcessWorkOrderExample_ID", pd.get("ProcessWorkOrderExample_ID"));
						pdPP.put("QITaskIF", "是");
						ProcessWorkOrderExampleService.EditQIIF(pdPP);
						// 修改生产任务中是否生成质检任务字段

						map.put("result", errInfo);
						// 入厂检质检-消息推送质检岗位
						String FFTYPE = pd.getString("FFTYPE");
						PageData pdDept = departmentService.findByName(pd);
						if (null != FFTYPE && FFTYPE.equals("入厂检") && pdDept != null) {

							String depts = departmentService.getDEPARTMENT_IDSSS(pdDept.getString("DEPARTMENT_ID"));
							pd.put("depts", depts);
							List<PageData> staffList = staffService.listAllByDept(pd);
							String ReceivingAuthority = ",";
							for (PageData pdStaffx : staffList) {
								ReceivingAuthority += pdStaffx.getString("USER_ID");
								ReceivingAuthority = ReceivingAuthority + ",";
								// 手机app-极光推送
								String UserName = pdStaffx.getString("USER_ID");
								pd.put("UserName", UserName);
								PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
								if (pdf != null) {
									String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
									String notification_title = "工作通知";
									String msg_title = "入厂检质检";
									String msg_content = "您有一条入厂检呼叫质检";
									if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title,
											msg_title, msg_content, "") == 1) {
										// errInfo="success";
									} else {
										// errInfo="error";
									}
								}
							}
							PageData pdNotice = new PageData();
							pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
							pdNotice.put("ReadPeople", ",");// 已读人默认空
							pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
							pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
							pdNotice.put("TType", "消息推送");// 消息类型
							pdNotice.put("FContent", "您有一条入厂检呼叫质检");// 消息正文
							pdNotice.put("FTitle", "入厂检质检");// 消息标题
							pdNotice.put("LinkIf", "no");// 是否跳转页面
							pdNotice.put("DataSources", "入厂检质检");// 数据来源
							pdNotice.put("Report_Key", "task");// 手机app
							pdNotice.put("Report_Value", "");// 手机app
							pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
							noticeService.save(pdNotice);

						}
					}
				}
			}
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 入库单生成质检任务
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addCKQM")
	@ResponseBody
	public Object addCKQM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdStaff = new PageData();
		Random random = new Random();
		int ends = random.nextInt(99);
		String.format("%02d", ends);
		pd = this.getPageData();
		List<PageData> varList = StockListDetailService.listAll(pd);// 出库单明细列表
		pd.put("QIType", "出厂检");
		pd.put("FFTYPE", "出厂检");
		pd.put("FFDEPT", "品控部");
		List<PageData> QIList = QualityInspectionPlanExecuteService.listQI(pd);// 出厂检方案列表
		for (int i = 0; i < varList.size(); i++) {
			String MATID = varList.get(i).get("FCode").toString();// 物料ID
			for (int j = 0; j < QIList.size(); j++) {
				pd.put("QIPlanID", QIList.get(j).get("QualityInspectionPlan_ID").toString());
				List<PageData> matList = QualityInspectionPlanExecuteService.listQIMAT(pd);// 质检方案下物料列表

				// 根据质检任务 质检方式值 计算出 需要抽检的总样品数
				String QIMethod = QIList.get(j).getString("QIMethod");
				String QIMethodValue = QIList.get(j).getString("QIMethodValue");
				Double FCount = new Double(varList.get(i).get("MaterialQuantity").toString());
				if ("全检".equals(QIMethod)) {

				}
				if ("比例抽检".equals(QIMethod)) {
					FCount = FCount * new Double(QIMethodValue) / 100;
				}
				if ("自定义抽检".equals(QIMethod)) {
					FCount = new Double(QIMethodValue);
				}

				for (int z = 0; z < matList.size(); z++) {
					String ApplicableMaterialID = matList.get(z).get("ApplicableMaterialID").toString();
					if (MATID.equals(ApplicableMaterialID)) {
						pd.put("FNAME", Jurisdiction.getName()); // 获取当前登录人的中文名称
						pdStaff = staffService.getStaffId(pd); // 根据人员的中文姓名获取人员ID
						pd.put("QualityInspectionPlanExecute_ID", this.get32UUID()); // 主键
						pd.put("GenerationType", "呼叫质检");// 生产类型为临时创建
						pd.put("FMakeBillsPersoID", pdStaff.get("STAFF_ID"));// 制单人
						pd.put("FMakeBillsTime", Tools.date2Str(new Date())); // 制单时间
						pd.put("DataSources", "出库单据");
						pd.put("TaskNum", "ZJRW" + Tools.date2Str(new Date(), "yyyyMMdd") + ends);// 任务单号
						pd.put("FOperator", "部门");
						pd.put("InspectorID", "33cbc85b1aee46fba02944d381dda6b7");// 部门ID
						pd.put("FType", "20dd7fd1a5ce4b7d8589c5c20abcfb22");// 质检类型
						pd.put("RequireEndTime", Tools.date2Str(new Date(), "yyyy-MM-dd"));// 要求完成时间
						pd.put("QIPosition", "出厂口");// 质检位置
						pd.put("MaterialID", MATID);// 物料
						pd.put("FPriorityID", "1");// 优先级
						pd.put("FCount", FCount);// 校验总数
						pd.put("RowNum", varList.get(i).get("RowNum").toString());
						pd.put("ExecutionStatus", "已下发");
						QualityInspectionPlanExecuteService.save(pd);

						// 添加一条样品 默认是 总数 自检 标签码自动生成
						PageData pdSample = new PageData();
						pdSample.put("QualityInspectionPlanExecuteSample_ID", this.get32UUID()); // 主键
						pdSample.put("QualityInspectionPlanExecute_ID",
								pd.getString("QualityInspectionPlanExecute_ID"));
						String sampleCode = String.valueOf(CodingRulesService.getRuleNumByRuleType("SAMPLE"));
						pdSample.put("TagCode", sampleCode);
						pdSample.put("SampleCount", FCount);
						pdSample.put("SampleRegisterTime", Tools.date2Str(new Date()));
						pdSample.put("InspectionParty", "自检");
						pdSample.put("FState", "执行中");
						QualityInspectionPlanExecuteSampleService.save(pdSample);
						PageData pData = new PageData();
						pData.put("QIPlanID", QIList.get(j).get("QualityInspectionPlan_ID").toString());
						List<PageData> listAll = QualityInspectionPlanInspectionDetailsService.listAll(pData);
						for (PageData pageData : listAll) {
							pageData.put("QualityInspectionPlanExecuteSample_ID",
									pdSample.getString("QualityInspectionPlanExecuteSample_ID"));
							pageData.put("QualityInspectionPlanExecute_ID",
									pdSample.getString("QualityInspectionPlanExecute_ID"));
							pageData.put("QualityInspectionPlanDetailExecute_ID", this.get32UUID());
							pageData.put("FinishOnce", 0);
							QualityInspectionPlanDetailExecuteService.save(pageData);
						}

						operationrecordService.add("", "质检任务", "添加", pd.getString("QualityInspectionPlanExecute_ID"),
								"", "");// 操作日志
						// 修改生产任务中是否生成质检任务字段
						PageData pdPP = new PageData();
						pdPP.put("ProcessWorkOrderExample_ID", pd.get("ProcessWorkOrderExample_ID"));
						pdPP.put("QITaskIF", "是");
						ProcessWorkOrderExampleService.EditQIIF(pdPP);
						// 修改生产任务中是否生成质检任务字段

						map.put("result", errInfo);
						// 出厂任务质检-消息推送质检岗位
						String FFTYPE = pd.getString("FFTYPE");
						PageData pdDept = departmentService.findByName(pd);
						if (null != FFTYPE && FFTYPE.equals("出厂检") && pdDept != null) {

							String depts = departmentService.getDEPARTMENT_IDSSS(pdDept.getString("DEPARTMENT_ID"));
							pd.put("depts", depts);
							List<PageData> staffList = staffService.listAllByDept(pd);
							String ReceivingAuthority = ",";
							for (PageData pdStaffx : staffList) {
								ReceivingAuthority += pdStaffx.getString("USER_ID");
								ReceivingAuthority = ReceivingAuthority + ",";
								// 手机app-极光推送
								String UserName = pdStaffx.getString("USER_ID");
								pd.put("UserName", UserName);
								PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
								if (pdf != null) {
									String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
									String notification_title = "工作通知";
									String msg_title = "出厂检质检";
									String msg_content = "您有一条出厂检呼叫质检";
									if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title,
											msg_title, msg_content, "") == 1) {
										// errInfo="success";
									} else {
										// errInfo="error";
									}
								}
							}
							PageData pdNotice = new PageData();
							pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
							pdNotice.put("ReadPeople", ",");// 已读人默认空
							pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
							pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
							pdNotice.put("TType", "消息推送");// 消息类型
							pdNotice.put("FContent", "您有一条出厂检呼叫质检");// 消息正文
							pdNotice.put("FTitle", "出厂检质检");// 消息标题
							pdNotice.put("LinkIf", "no");// 是否跳转页面
							pdNotice.put("DataSources", "出厂检质检");// 数据来源
							pdNotice.put("Report_Key", "task");// 手机app
							pdNotice.put("Report_Value", "");// 手机app
							pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
							noticeService.save(pdNotice);

						}
					}
				}
			}
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 保存 v2 管悦 2021-07-21 插入质检方案的质检项
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	// @RequiresPermissions("QualityInspectionPlanExecute:add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdStaff = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", Jurisdiction.getName()); // 获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd); // 根据人员的中文姓名获取人员ID
		pd.put("QualityInspectionPlanExecute_ID", this.get32UUID()); // 主键
		pd.put("ExecutionStatus", "待检");// 执行状态默认为创建
		pd.put("GenerationType", "临时创建");// 生产类型为临时创建
		pd.put("FMakeBillsPersoID", pdStaff.get("STAFF_ID"));// 制单人
		pd.put("FMakeBillsTime", Tools.date2Str(new Date())); // 制单时间
		QualityInspectionPlanExecuteService.save(pd);

		// 插入质检方案的质检项
		PageData pData = new PageData();
		pData.put("QIPlanID", pd.getString("QIPlanID"));
		List<PageData> listAll = QualityInspectionPlanInspectionDetailsService.listAll(pData);
		for (PageData pageData : listAll) {
			pageData.put("QualityInspectionPlanExecute_ID", pd.getString("QualityInspectionPlanExecute_ID"));
			pageData.put("QualityInspectionPlanDetailExecute_ID", this.get32UUID());
			QualityInspectionPlanDetailExecuteService.save(pageData);
		}

		operationrecordService.add("", "质检任务", "添加", pd.getString("QualityInspectionPlanExecute_ID"), "", "");// 操作日志

		map.put("result", errInfo);
		map.put("QualityInspectionPlanExecute_ID", pd.get("QualityInspectionPlanExecute_ID"));
		return map;
	}

	@RequestMapping(value = "/callAdd")
	@ResponseBody
	public Object callAdd() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			PageData pdStaff = new PageData();
			pd = this.getPageData();
			pd.put("FNAME", Jurisdiction.getName()); // 获取当前登录人的中文名称
			pdStaff = staffService.getStaffId(pd); // 根据人员的中文姓名获取人员ID
			pd.put("QualityInspectionPlanExecute_ID", this.get32UUID()); // 主键
			Random random = new Random();
			int ends = random.nextInt(99);
			String.format("%02d", ends);
			pd.put("TaskNum", "AppCallQM" + Tools.date2Str(new Date(), "yyyyMMddHHmmss") + "-" + ends);// 手机端呼叫质检，任务单号
																										// AppCallQM+年月日时分秒+两位随机数
			pd.put("ExecutionStatus", "已下发");// 执行状态默认为下发
			pd.put("DataSources", "生产执行");// 默认数据来源
			pd.put("GenerationType", "呼叫质检");// 生产类型
			pd.put("FOperator", "部门");// 默认下发部门
			pd.put("InspectorID", "33cbc85b1aee46fba02944d381dda6b7");// 默认下发给生产品控部
			pd.put("ExecutionStatus", "已下发");// 默认为已下发的状态
			pd.put("QIPosition", "生产");// 质检位置
			pd.put("FPriorityID", "1");// 优先级
			pd.put("FType", "20dd7fd1a5ce4b7d8589c5c20abcfb22");// 默认质检类型
			pd.put("RequireEndTime", Tools.date2Str(new Date(), "yyyy-MM-dd"));// 要求完成时间

			pd.put("FMakeBillsPersoID", pdStaff.getString("STAFF_ID"));// 制单人
			pd.put("FMakeBillsTime", Tools.date2Str(new Date())); // 制单时间

			// 根据质检任务 质检方式值 计算出 需要抽检的总样品数
			String QIPlanID = pd.getString("QIPlanID");
			if (StringUtil.isEmpty(QIPlanID)) {
				throw new RuntimeException("该生产任务没有配置质检方案");
			}
			PageData pdData = new PageData();
			pdData.put("QualityInspectionPlan_ID", QIPlanID);
			PageData QIPlan = QualityInspectionPlanService.findById(pdData);
			String QIMethod = QIPlan.getString("QIMethod");
			String QIMethodValue = QIPlan.getString("QIMethodValue");
			Double FCount = new Double(pd.get("MaterialQuantity").toString());
			if ("全检".equals(QIMethod)) {

			}
			if ("比例抽检".equals(QIMethod)) {
				FCount = FCount * new Double(QIMethodValue) / 100;
			}
			if ("自定义抽检".equals(QIMethod)) {
				FCount = new Double(QIMethodValue);
			}

			pd.put("FCount", FCount);
			QualityInspectionPlanExecuteService.save(pd);

			// 添加一条样品 默认是 总数 自检 标签码自动生成
			PageData pdSample = new PageData();
			pdSample.put("QualityInspectionPlanExecuteSample_ID", this.get32UUID()); // 主键
			pdSample.put("QualityInspectionPlanExecute_ID", pd.getString("QualityInspectionPlanExecute_ID"));
			String sampleCode = String.valueOf(CodingRulesService.getRuleNumByRuleType("SAMPLE"));
			pdSample.put("TagCode", sampleCode);
			pdSample.put("SampleCount", FCount);
			pdSample.put("SampleRegisterTime", Tools.date2Str(new Date()));
			pdSample.put("InspectionParty", "自检");
			pdSample.put("FState", "执行中");
			QualityInspectionPlanExecuteSampleService.save(pdSample);
			PageData pData = new PageData();
			pData.put("QIPlanID", pd.get("QIPlanID").toString());
			List<PageData> listAll = QualityInspectionPlanInspectionDetailsService.listAll(pData);
			for (PageData pageData : listAll) {
				pageData.put("QualityInspectionPlanExecuteSample_ID",
						pdSample.getString("QualityInspectionPlanExecuteSample_ID"));
				pageData.put("QualityInspectionPlanExecute_ID", pdSample.getString("QualityInspectionPlanExecute_ID"));
				pageData.put("QualityInspectionPlanDetailExecute_ID", this.get32UUID());
				pageData.put("FinishOnce", 0);
				QualityInspectionPlanDetailExecuteService.save(pageData);
			}

			operationrecordService.add("", "质检任务", "添加", pd.getString("QualityInspectionPlanExecute_ID"), "", "");// 操作日志
			// 修改生产任务中是否生成质检任务字段
			PageData pdPP = new PageData();
			pdPP.put("ProcessWorkOrderExample_ID", pd.get("ProcessWorkOrderExample_ID"));
			pdPP.put("QITaskIF", "是");
			ProcessWorkOrderExampleService.EditQIIF(pdPP);
			// 修改生产任务中是否生成质检任务字段

			map.put("result", errInfo);
			// 生产任务质检-消息推送质检岗位
			PageData findById = processWorkOrderExampleService.findById(pd);
			String FFTYPE = pd.getString("FFTYPE");
			PageData pdDept = departmentService.findByName(pd);
			if (null != FFTYPE && FFTYPE.equals("生产任务") && pdDept != null) {

				String depts = departmentService.getDEPARTMENT_IDSSS(pdDept.getString("DEPARTMENT_ID"));
				pd.put("depts", depts);
				List<PageData> staffList = staffService.listAllByDept(pd);
				String ReceivingAuthority = ",";
				for (PageData pdStaffx : staffList) {
					ReceivingAuthority += pdStaffx.getString("USER_ID");
					ReceivingAuthority = ReceivingAuthority + ",";
					// 手机app-极光推送
					String UserName = pdStaffx.getString("USER_ID");
					pd.put("UserName", UserName);
					PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
					if (pdf != null) {
						String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
						String notification_title = "工作通知";
						String msg_title = "生产任务质检";
						String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
								+ findById.getString("FName") + "，产出物料为" + findById.getString("ProcessIMaterielCode")
								+ "的生产任务呼叫质检";
						if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title, msg_title,
								msg_content, "") == 1) {
							// errInfo="success";
						} else {
							// errInfo="error";
						}
					}
				}
				PageData pdNotice = new PageData();
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("FName") + "，产出物料为"
								+ findById.getString("ProcessIMaterielCode") + "的生产任务呼叫质检");// 消息正文
				pdNotice.put("FTitle", "生产任务质检");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "生产任务质检");// 数据来源
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("Report_Value", "");// 手机app
				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("AccessURL",
						"../../../views/qm/QualityTask/Cabinet_QualityTask_list.html?NOTICE_TYPE=" + "ALL");//
				noticeService.save(pdNotice);
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
				String WXNR = "【生产任务质检提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
			}
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
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	// @RequiresPermissions("QualityInspectionPlanExecute:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();

		// 联级删除所有样品 联级删除所有 子详情
		List<PageData> sampleList = QualityInspectionPlanExecuteSampleService.listAll(pd);
		for (PageData sample : sampleList) {

			List<PageData> listAll = QualityInspectionPlanDetailExecuteService.listAll(sample);
			for (PageData pageData : listAll) {
				QualityInspectionPlanDetailExecuteService.delete(pageData);
			}
			QualityInspectionPlanExecuteSampleService.delete(sample);
		}
		QualityInspectionPlanExecuteService.delete(pd);
		operationrecordService.add("", "质检任务", "删除", pd.getString("QualityInspectionPlanExecute_ID"), "", "");// 操作日志
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
	// @RequiresPermissions("QualityInspectionPlanExecute:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FOperator = pd.get("FOperator").toString();
		if (FOperator == "部门" || FOperator.equals("部门")) {
			pd.put("FOperator", 1);
		} else {
			pd.put("FOperator", 2);
		}
		QualityInspectionPlanExecuteService.edit(pd);
		operationrecordService.add("", "质检任务", "修改", pd.getString("QualityInspectionPlanExecute_ID"), "", "");// 操作日志
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping(value = "/getGX")
	@ResponseBody
	public Object getGX() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = QualityInspectionPlanExecuteService.getGX(pd);
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
	@RequestMapping(value = "/list")
	// @RequiresPermissions("QualityInspectionPlanExecute:list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = QualityInspectionPlanExecuteService.list(page); // 列出QualityInspectionPlanExecute列表
		for (PageData pageData : varList) {
			PageData pdData = new PageData();
			pdData.put("QualityInspectionPlanExecute_ID", pageData.getString("QualityInspectionPlanExecute_ID"));
			List<PageData> varLists = QualityInspectionPlanExecuteSampleService.listAll(pdData);
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

			for (PageData pageData1 : varLists) {
				if (StringUtil.isNotEmpty(pageData1.getString("SampleCount"))) {
					SampleCount = SampleCount.add(new BigDecimal(pageData1.getString("SampleCount")));
				}
				if (StringUtil.isNotEmpty(pageData1.getString("QualifiedCount"))) {
					QualifiedCount = QualifiedCount.add(new BigDecimal(pageData1.getString("QualifiedCount")));
				}
				if (StringUtil.isNotEmpty(pageData1.getString("CompromisedCount"))) {
					CompromisedCount = CompromisedCount.add(new BigDecimal(pageData1.getString("CompromisedCount")));
				}
				if (StringUtil.isNotEmpty(pageData1.getString("DisqualifiedCount"))) {
					DisqualifiedCount = DisqualifiedCount.add(new BigDecimal(pageData1.getString("DisqualifiedCount")));
				}
			}
			if (QualifiedCount.intValue() != 0) {
				AcceptanceRate = (QualifiedCount.add(CompromisedCount)).divide(SampleCount, 4, RoundingMode.HALF_UP)
						.multiply(new BigDecimal("100"));
			}

			pageData.put("SampleCount", SampleCount);
			pageData.put("QualifiedCount", QualifiedCount);
			pageData.put("CompromisedCount", CompromisedCount);
			pageData.put("DisqualifiedCount", DisqualifiedCount);
			pageData.put("AcceptanceRate", AcceptanceRate);
		}
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
	@RequestMapping(value = "/listRun")
	// @RequiresPermissions("QualityInspectionPlanExecute:list")
	@ResponseBody
	public Object listRun(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = QualityInspectionPlanExecuteService.listRun(page); // 列出QualityInspectionPlanExecute列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 去修改页面获取数据 v2 管悦 2021-07-21 查工位信息
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	// @RequiresPermissions("QualityInspectionPlanExecute:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = QualityInspectionPlanExecuteService.findById(pd); // 根据ID读取
		PageData materialPageData = new PageData();
		materialPageData.put("MAT_BASIC_ID", pd.getString("MaterialID"));
		PageData mat_basic = mat_basicService.findById(materialPageData);
		pd.put("MAT_NAME", mat_basic.getString("MAT_NAME"));
		String stationname = "";
		PageData temp = new PageData();
		PageData spd = new PageData();
		if (pd.containsKey("FStation") && null != pd.getString("FStation")) {
			stationname = "";
			String[] sarr = pd.getString("FStation").split(",yl,");
			if (sarr.length > 1) {
				for (int j = 0; j < sarr.length; j++) {
					temp.put("WC_STATION_ID", sarr[j]);
					spd = wc_stationService.findById(temp);
					if (null != spd && spd.containsKey("FNAME")) {
						if (j > 0)
							stationname += ",yl,";
						stationname += spd.getString("FNAME");
					}
				}
			} else {
				temp.put("WC_STATION_ID", sarr[0]);
				spd = wc_stationService.findById(temp);
				if (null != spd && spd.containsKey("FNAME"))
					stationname = spd.getString("FNAME");
			}
			pd.put("stations", stationname);
		} else {
			pd.put("stations", "");
		}
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
	// @RequiresPermissions("QualityInspectionPlanExecute:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			QualityInspectionPlanExecuteService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		} else {
			errInfo = "error";
		}
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 质检任务下发 参数：质检任务ID QualityInspectionPlanExecute_ID 质检方案ID
	 */
	@RequestMapping(value = "/goLssue")
	@ResponseBody
	public Object goLssue() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		QualityInspectionPlanExecuteService.goLssue(pd);
		operationrecordService.add("", "质检任务", "下发", pd.getString("QualityInspectionPlanExecute_ID"), "", "");// 操作日志
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	// @RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("质检任务ID"); // 1
		titles.add("数据来源"); // 2
		titles.add("任务单号"); // 3
		titles.add("质检方案ID"); // 4
		titles.add("关联工单ID"); // 5
		titles.add("工序ID"); // 6
		titles.add("物料ID"); // 7
		titles.add("质检位置"); // 8
		titles.add("优先级"); // 9
		titles.add("制单人"); // 10
		titles.add("制单时间"); // 11
		titles.add("检验人员或部门"); // 12
		titles.add("开始时间"); // 13
		titles.add("结束时间"); // 14
		titles.add("判定结果"); // 15
		titles.add("类型"); // 16
		titles.add("执行状态"); // 17
		titles.add("生成类型"); // 18
		titles.add("描述"); // 19
		titles.add("下发给人或部门"); // 20
		titles.add("要求完成日期"); // 21
		titles.add("检验位置"); // 22
		titles.add("检验仓库"); // 23
		titles.add("检验仓位"); // 24
		dataMap.put("titles", titles);
		List<PageData> varOList = QualityInspectionPlanExecuteService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("QualityInspectionPlanExecute_ID")); // 1
			vpd.put("var2", varOList.get(i).getString("DataSources")); // 2
			vpd.put("var3", varOList.get(i).getString("TaskNum")); // 3
			vpd.put("var4", varOList.get(i).getString("QIPlanID")); // 4
			vpd.put("var5", varOList.get(i).getString("WorkOrderIDRel")); // 5
			vpd.put("var6", varOList.get(i).getString("WP_ID")); // 6
			vpd.put("var7", varOList.get(i).getString("MaterialID")); // 7
			vpd.put("var8", varOList.get(i).getString("QIPosition")); // 8
			vpd.put("var9", varOList.get(i).get("FPriorityID").toString()); // 9
			vpd.put("var10", varOList.get(i).getString("FMakeBillsPersoID")); // 10
			vpd.put("var11", varOList.get(i).getString("FMakeBillsTime")); // 11
			vpd.put("var12", varOList.get(i).getString("InspectorID")); // 12
			vpd.put("var13", varOList.get(i).getString("FBeginTime")); // 13
			vpd.put("var14", varOList.get(i).getString("FEndTime")); // 14
			vpd.put("var15", varOList.get(i).getString("JudgmentResults")); // 15
			vpd.put("var16", varOList.get(i).getString("FType")); // 16
			vpd.put("var17", varOList.get(i).getString("ExecutionStatus")); // 17
			vpd.put("var18", varOList.get(i).getString("GenerationType")); // 18
			vpd.put("var19", varOList.get(i).getString("FExplanation")); // 19
			vpd.put("var20", varOList.get(i).getString("FOperator")); // 20
			vpd.put("var21", varOList.get(i).getString("RequireEndTime")); // 21
			vpd.put("var22", varOList.get(i).getString("InspectionPosition")); // 22
			vpd.put("var23", varOList.get(i).getString("InspectionWarehouse")); // 23
			vpd.put("var24", varOList.get(i).getString("InspectionLocation")); // 24
			varList.add(vpd);
		}
		operationrecordService.add("", "质检任务", "导出", pd.getString("QualityInspectionPlanExecute_ID"), "", "");// 操作日志
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	@RequestMapping(value = "/getNowUser")
	@ResponseBody
	public Object getNowUser() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		PageData pdStaff = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", Jurisdiction.getName()); // 获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd); // 根据人员的中文姓名获取人员ID
		map.put("FNAME", Jurisdiction.getName());
		map.put("STAFF_ID", pdStaff.get("STAFF_ID"));
		return map;
	}

	/**
	 * 柜体质检任务列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listCabinet")
	@RequiresPermissions("CabinetQualityInspect:list")
	@ResponseBody
	public Object listCabinet(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = QualityInspectionPlanExecuteService.listCabinet(page); // 列出QualityInspectionPlanExecute列表
		String stationname = "";
		PageData temp = new PageData();
		PageData spd = new PageData();
		for (int i = 0; i < varList.size(); i++) {
			if (varList.get(i).containsKey("FStation") && null != varList.get(i).getString("FStation")) {
				stationname = "";
				String[] sarr = varList.get(i).getString("FStation").split(",yl,");
				if (sarr.length > 1) {
					for (int j = 0; j < sarr.length; j++) {
						temp.put("WC_STATION_ID", sarr[j]);
						spd = wc_stationService.findById(temp);
						if (null != spd && spd.containsKey("FNAME")) {
							if (j > 0)
								stationname += ",yl,";
							stationname += spd.getString("FNAME");
						}
					}
				} else {
					temp.put("WC_STATION_ID", sarr[0]);
					spd = wc_stationService.findById(temp);
					if (null != spd && spd.containsKey("FNAME"))
						stationname = spd.getString("FNAME");
				}
				varList.get(i).put("stations", stationname);
			} else {
				varList.get(i).put("stations", "");
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 质检完成 1、反写完成时间、执行状态 2、未计时结束的用当前时间自动结束 3、生产执行的工序任务自动完成
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/finish")
	// @RequiresPermissions("QualityInspectionPlanExecute:add")
	@ResponseBody
	public Object finish() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = QualityInspectionPlanExecuteService.findByIdx(pd);
		PageData pdOp = new PageData();
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = staffService.getStaffId(pdOp) == null ? ""
				: staffService.getStaffId(pdOp).getString("STAFF_ID");
		// 生产执行的工序任务自动完成
		PageData appProcessWorkOrderExampleDetailByPK = processWorkOrderExampleService.findById(pd);
		if (null != appProcessWorkOrderExampleDetailByPK) {
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
			appProcessWorkOrderExampleDetailByPK.put("FStatus", "结束");
			appProcessWorkOrderExampleDetailByPK.put("FRUN_STATE", "结束");
			appProcessWorkOrderExampleDetailByPK.put("FOPERATOR", STAFF_ID);
			appProcessWorkOrderExampleDetailByPK.put("EXECUTE_TIME", Tools.date2Str(new Date()));
			appProcessWorkOrderExampleDetailByPK.put("TIME_STAMP", System.currentTimeMillis());
			appProcessWorkOrderExampleDetailByPK.put("ActualEndTime", Tools.date2Str(new Date()));
			pd.put("EndTime", Tools.date2Str(new Date()));
			WorkRecordService.editEnd(pd);
			processWorkOrderExampleService.edit(appProcessWorkOrderExampleDetailByPK);
		}
		// 未计时结束的质检任务用当前时间自动结束
		pd.put("END_TIME", Tools.date2Str(new Date()));
		pd.put("ASSOCIATED_ID", pd.getString("QualityInspectionPlanExecute_ID"));
		o_record_qualitytaskService.editEnd(pd);
		// 反写完成时间、执行状态
		pd.put("FEndTime", Tools.date2Str(new Date()));
		pd.put("ExecutionStatus", "结束");
		pd.put("JudgmentResults", pd.getString("JudgmentResultsNew"));
		QualityInspectionPlanExecuteService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
}
