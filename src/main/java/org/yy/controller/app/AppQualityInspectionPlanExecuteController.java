package org.yy.controller.app;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.service.app.AppRegistrationService;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.QualityInspectionPlanInspectionDetailsService;
import org.yy.service.km.QualityInspectionPlanService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.qm.QualityInspectionPlanDetailExecuteService;
import org.yy.service.qm.QualityInspectionPlanExecuteSampleService;
import org.yy.service.qm.QualityInspectionPlanExecuteService;
import org.yy.util.DateUtil;
import org.yy.util.JpushClientUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

/**
 * 说明：质检任务 作者：范贺男 时间：2020-11-12
 */
@Controller
@RequestMapping("/appQualityInspectionPlanExecute")
public class AppQualityInspectionPlanExecuteController extends BaseController {
	@Autowired
	private ProcessWorkOrderExampleService processWorkOrderExampleService;
	@Autowired
	private QualityInspectionPlanExecuteService QualityInspectionPlanExecuteService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private QualityInspectionPlanInspectionDetailsService QualityInspectionPlanInspectionDetailsService;
	@Autowired
	private QualityInspectionPlanDetailExecuteService QualityInspectionPlanDetailExecuteService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ProcessWorkOrderExampleService ProcessWorkOrderExampleService;
	@Autowired
	private QualityInspectionPlanService QualityInspectionPlanService;
	@Autowired
	private CodingRulesService CodingRulesService;
	@Autowired
	private QualityInspectionPlanExecuteSampleService QualityInspectionPlanExecuteSampleService;
	@Autowired
	private AppRegistrationService AppRegistrationService;
	@Autowired
	private NoticeService noticeService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	// @RequiresPermissions("QualityInspectionPlanExecute:add")
	@ResponseBody
	public Object add(HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			PageData pdStaff = new PageData();
			pd = this.getPageData();
			String USERNAME = pd.getString("UserName");
			// 根据 USERNAME 获取 staff info
			PageData pd1 = new PageData();
			pd1.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pd1);
			pd.put("FNAME", staff.get("NAME")); // 获取当前登录人的中文名称
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
			if (null != FFTYPE && FFTYPE.equals("生产任务")) {
				String DEPARTMENT_ID = staff.getString("DEPARTMENT_ID");
				if (StringUtil.isNotEmpty(DEPARTMENT_ID)) {
					String depts = departmentService.getDEPARTMENT_IDSSS(DEPARTMENT_ID);
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
									+ findById.getString("FName") + "，产出物料为"
									+ findById.getString("ProcessIMaterielCode") + "的生产任务呼叫质检";
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
					pdNotice.put("FIssuedID", staff.get("NAME")); // 发布人
					pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
					pdNotice.put("TType", "消息推送");// 消息类型
					pdNotice.put("FContent",
							"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("FName")
									+ "，产出物料为" + findById.getString("ProcessIMaterielCode") + "的生产任务呼叫质检");// 消息正文
					pdNotice.put("FTitle", "生产任务质检");// 消息标题
					pdNotice.put("LinkIf", "no");// 是否跳转页面
					pdNotice.put("DataSources", "生产任务质检");// 数据来源
					pdNotice.put("Report_Key", "task");// 手机app
					pdNotice.put("Report_Value", "");// 手机app
					pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
					pdNotice.put("AccessURL",
							"../../../views/qm/QualityTask/Cabinet_QualityTask_list.html?NOTICE_TYPE=" + "ALL");//
					noticeService.save(pdNotice);
				}
			}
			map.put("data", "success");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
			map.put("result", errInfo);
			map.put("msg", e.getMessage());
			return map;
		}
	}

	@RequestMapping(value = "/getGX")
	@ResponseBody
	public Object getGX() throws Exception {
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = QualityInspectionPlanExecuteService.getGX(pd);
			return AppResult.success(varList, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
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
	public Object list(AppPage page, HttpServletResponse response) throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			String orderby = "RowNum";
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "asc";
			if ("desc".equals(pd.getString("sort"))) {
				sort = "desc";
			}

			pd.put("KEYWORDS4", pd.getString("STAFF_ID"));
			PageData staff = staffService.findById(pd);
			String DEPARTMENT_ID = staff.getString("DEPARTMENT_ID");
			if (StringUtil.isNotEmpty(DEPARTMENT_ID)) {
				pd.put("KEYWORDS5", DEPARTMENT_ID);
			}

			page.setPd(pd);

			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(QualityInspectionPlanExecuteService.AppList(page));
			varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	// @RequiresPermissions("QualityInspectionPlanExecute:edit")
	@ResponseBody
	public Object goEdit(HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = QualityInspectionPlanExecuteService.findById(pd); // 根据ID读取
			map.put("pd", pd);
			return AppResult.success(map, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 范贺男 显示全部部门列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listAllDep")
	@ResponseBody
	public Object listAll(HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = departmentService.listAll(pd);
			map.put("varList", varList);
			return AppResult.success(map, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 用户列表
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/staffList")
	@ResponseBody
	public Object staffList(AppPage page, HttpServletResponse response) throws Exception {
		try {
			List<PageData> varList = Lists.newArrayList();
			PageData pd = new PageData();
			pd = this.getPageData();
			page.setPd(pd);
			PageInfo<PageData> pageInfo = new PageInfo<>(staffService.AppList(page));
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

}
