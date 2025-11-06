package org.yy.controller.km;

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
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.app.AppRegistrationService;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.ExceptionService;
import org.yy.service.mdm.EQM_BASEService;
import org.yy.service.mm.ExceptionHandlingService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.project.manager.PROTEAMService;
import org.yy.util.DateUtil;
import org.yy.util.JpushClientUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

/**
 * 说明：异常 作者：范贺男 时间：2020-11-09
 */
@Controller
@RequestMapping("/Exception")
public class ExceptionController extends BaseController {
	@Autowired
	private ProcessWorkOrderExampleService ProcessWorkOrderExampleService;
	@Autowired
	private ExceptionService ExceptionService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ExceptionHandlingService ExceptionHandlingService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private AppRegistrationService AppRegistrationService;

	@Autowired
	private EQM_BASEService eqm_baseService;
	@Autowired
	private PROTEAMService proteamService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	// @RequiresPermissions("Exception:add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdStaff = new PageData();
		pd = this.getPageData();
		pd.put("Exception_ID", this.get32UUID()); // 主键
		pd.put("FNAME", Jurisdiction.getName()); // 获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd); // 根据人员的中文姓名获取人员ID
		pd.put("FIssuedID", pdStaff.get("STAFF_ID")); // 制单人 STAFF_ID
		pd.put("ReleaseTime", Tools.date2Str(new Date())); // 制单时间
		pd.put("IssueType", "未下发");
		pd.put("FIs_Read", "未读");
		ExceptionService.save(pd);
		operationrecordService.add("", "异常", "添加", pd.getString("Exception_ID"), "", "");// 操作日志
		map.put("result", errInfo);
		map.put("Exception_ID", pd.get("Exception_ID"));
		ExceptionService.goLssue(pd);
		try {
			PageData pdHandling = new PageData();
			// 下发异常时，同时新增一条异常处理记录
			pdHandling.put("ExceptionHandling_ID", this.get32UUID());// 主键ID
			pdHandling.put("Exception_ID", pd.get("Exception_ID"));// 异常ID
			String InitOperatorID = pd.get("InitOperatorID").toString();
			if (InitOperatorID == "部门" || InitOperatorID.equals("部门")) {
				pdHandling.put("InitOperatorID", 1);// 移交给部门
			} else {
				pdHandling.put("InitOperatorID", 2);// 移交给人
			}
			pdHandling.put("WaitingOperatorID", pd.get("ExceptionPendingParty"));// 待处理人或部门
			pdHandling.put("IfTurnOver", 0);// 是否移交处理 默认0 不移交
			pdHandling.put("Reorder", 1);// 排序字段，自动生成时为1，每移交一次+1
			pdHandling.put("DisposeType", "待处理");// 处理判别，初始默认待处理
			pdHandling.put("FExplanation", "");
			pdHandling.put("FOperator", pd.get("InitOperatorID"));// 移交给人
			ExceptionHandlingService.save(pdHandling);
			operationrecordService.add("", "异常", "下发", pd.getString("Exception_ID"), "", "");// 操作日志
		} catch (Exception e) {
			errInfo = "failure";//
		}
		// 生产任务异常-消息提醒
		String FFTYPE = pd.getString("FFTYPE");
		if (null != FFTYPE && FFTYPE.equals("生产任务")) {

			PageData findById = ProcessWorkOrderExampleService.findById(pd);
			PageData pdXM = new PageData();
			pdXM.put("EPROJECT_ID", findById.getString("PROJECT_ID"));
			pdXM.put("FPTISINNER", "项目经理");
			List<PageData> pdTeamList = proteamService.listAll(pdXM);
			int InitOperatorID = Integer.parseInt(pd.getString("InitOperatorID"));
			if (InitOperatorID == 1) {// 部门
				String depts = departmentService.getDEPARTMENT_IDSSS(pd.getString("ExceptionPendingParty"));
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
						String msg_title = "生产任务异常";
						String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
								+ findById.getString("WPName") + "，产出物料为" + findById.getString("ProcessIMaterielCode")
								+ "的生产任务上报异常";
						if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title, msg_title,
								msg_content, "") == 1) {
							// errInfo="success";
						} else {
							// errInfo="error";
						}
					}
				}
				for (PageData pdStaffx : pdTeamList) {
					ReceivingAuthority += pdStaffx.getString("FPTSTAFFID");
					ReceivingAuthority = ReceivingAuthority + ",";
				}
				PageData pdNotice = new PageData();
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("WPName") + "，产出物料为"
								+ findById.getString("ProcessIMaterielCode") + "的生产任务上报异常");// 消息正文
				pdNotice.put("FTitle", "生产任务异常");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "生产任务异常");// 数据来源
				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("AccessURL", "../../../views/pp/exceptionDispose/exceptionDispose_list.html?NOTICE_TYPE="
						+ "ALL" + "&Exception_ID=" + pd.getString("Exception_ID"));// 手机app//
				// 手机app
				pdNotice.put("Report_Value", "");// 手机app
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
				String WXNR = "【生产任务异常提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
			} else {
				pd.put("STAFF_ID", pd.getString("ExceptionPendingParty"));
				PageData staffPD = staffService.findById(pd);
				PageData pdNotice = new PageData();
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("WPName") + "，产出物料为"
								+ findById.getString("ProcessIMaterielCode") + "的生产任务上报异常");// 消息正文
				pdNotice.put("FTitle", "生产任务异常");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("DataSources", "生产任务异常");// 数据来源
				String ReceivingAuthority = ",";
				ReceivingAuthority += staffPD.getString("USER_ID");
				ReceivingAuthority = ReceivingAuthority + ",";
				for (PageData pdStaffx : pdTeamList) {
					ReceivingAuthority += pdStaffx.getString("FPTSTAFFID");
					ReceivingAuthority = ReceivingAuthority + ",";
				}

				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("Report_Value", "");// 手机app
				pdNotice.put("AccessURL", "../../../views/pp/exceptionDispose/exceptionDispose_list.html?NOTICE_TYPE="
						+ "ALL" + "&Exception_ID=" + pd.getString("Exception_ID"));// 手机app//
				// 手机app
				noticeService.save(pdNotice);
				SendWeChatMessageMes weChat = new SendWeChatMessageMes();
				String WXNR = "【生产任务异常提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
				// 手机app-极光推送
				String UserName = staffPD.getString("USER_ID");
				pd.put("UserName", UserName);
				PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
				if (pdf != null) {
					String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
					String notification_title = "工作通知";
					String msg_title = "生产任务异常";
					String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
							+ findById.getString("WPName") + "，产出物料为" + findById.getString("ProcessIMaterielCode")
							+ "的生产任务上报异常";
					if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title, msg_title, msg_content,
							"") == 1) {
						// errInfo="success";
					} else {
						// errInfo="error";
					}
				}
			}

		} else {
			int InitOperatorID = Integer.parseInt(pd.getString("InitOperatorID"));
			PageData pdEQM = eqm_baseService.findById(pd);
			if (InitOperatorID == 1) {// 部门
				String depts = departmentService.getDEPARTMENT_IDSSS(pd.getString("ExceptionPendingParty"));
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
						String msg_title = "设备异常";
						String msg_content = "设备名称为" + pdEQM.getString("FNAME") + "的设备上报异常";
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
				pdNotice.put("FContent", "设备名称为" + pdEQM.getString("FNAME") + "的设备上报异常");// 消息正文
				pdNotice.put("FTitle", "设备异常");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "设备异常");// 数据来源
				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("AccessURL", "../../../views/pp/exceptionDispose/exceptionDispose_list.html?NOTICE_TYPE="
						+ "ALL" + "&Exception_ID=" + pd.getString("Exception_ID"));// 手机app//
				// 手机app
				pdNotice.put("Report_Value", "");// 手机app
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
				String WXNR = "【设备异常提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
			} else {
				pd.put("STAFF_ID", pd.getString("ExceptionPendingParty"));
				PageData staffPD = staffService.findById(pd);
				PageData pdNotice = new PageData();
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent", "设备名称为" + pdEQM.getString("FNAME") + "的设备上报异常");// 消息正文
				pdNotice.put("FTitle", "设备异常");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("DataSources", "设备异常");// 数据来源
				String ReceivingAuthority = ",";
				ReceivingAuthority += staffPD.getString("USER_ID");
				ReceivingAuthority = ReceivingAuthority + ",";
				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("Report_Value", "");// 手机app
				pdNotice.put("AccessURL", "../../../views/pp/exceptionDispose/exceptionDispose_list.html?NOTICE_TYPE="
						+ "ALL" + "&Exception_ID=" + pd.getString("Exception_ID"));// 手机app//
				// 手机app
				noticeService.save(pdNotice);
				SendWeChatMessageMes weChat = new SendWeChatMessageMes();
				String WXNR = "【设备异常提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
			}

		}
		return map;
	}

	/**
	 * 下发异常
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goLssueAll")
	@ResponseBody
	public Object goLssue() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdHandling = new PageData();
		pd = this.getPageData();
		PageData pdStaff = new PageData();
		pd.put("Exception_ID", this.get32UUID()); // 主键
		pd.put("FNAME", Jurisdiction.getName()); // 获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd); // 根据人员的中文姓名获取人员ID
		pd.put("FIssuedID", pdStaff.get("STAFF_ID")); // 制单人 STAFF_ID
		pd.put("ReleaseTime", Tools.date2Str(new Date())); // 制单时间
		pd.put("IssueType", "未下发");
		ExceptionService.save(pd);
		operationrecordService.add("", "异常", "添加", pd.getString("Exception_ID"), "", "");// 操作日志
		map.put("result", errInfo);
		map.put("Exception_ID", pd.get("Exception_ID"));
		// 生产任务异常-消息提醒
		String FFTYPE = pd.getString("FFTYPE");
		if (null != FFTYPE && FFTYPE.equals("生产任务")) {
			PageData findById = ProcessWorkOrderExampleService.findById(pd);
			int InitOperatorID = Integer.parseInt(pd.getString("InitOperatorID"));
			if (InitOperatorID == 1) {// 部门
				String depts = departmentService.getDEPARTMENT_IDSSS(pd.getString("ExceptionPendingParty"));
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
						String msg_title = "生产任务异常";
						String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
								+ findById.getString("WPName") + "，产出物料为" + findById.getString("ProcessIMaterielCode")
								+ "的生产任务上报异常";
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
						"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("WPName") + "，产出物料为"
								+ findById.getString("ProcessIMaterielCode") + "的生产任务上报异常");// 消息正文
				pdNotice.put("FTitle", "生产任务异常");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "生产任务异常");// 数据来源
				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("Report_Value", "");// 手机app
				pdNotice.put("AccessURL",
						"../../../views/pp/exceptionDispose/exceptionDispose_list.html?NOTICE_TYPE=" + "ALL");// 手机app//
																												// 手机app

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
				String WXNR = "【生产任务异常提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
			} else {
				pd.put("STAFF_ID", pd.getString("ExceptionPendingParty"));
				PageData staffPD = staffService.findById(pd);
				PageData pdNotice = new PageData();
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("WPName") + "，产出物料为"
								+ findById.getString("ProcessIMaterielCode") + "的生产任务上报异常");// 消息正文
				pdNotice.put("FTitle", "生产任务异常");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("Report_Key", "task");// 手机app
				pdNotice.put("DataSources", "生产任务异常");// 数据来源
				String ReceivingAuthority = ",";
				ReceivingAuthority += staffPD.getString("USER_ID");
				ReceivingAuthority = ReceivingAuthority + ",";
				pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
				pdNotice.put("Report_Value", "");// 手机app
				pdNotice.put("AccessURL",
						"../../../views/pp/exceptionDispose/exceptionDispose_list.html?NOTICE_TYPE=" + "ALL");// 手机app//
																												// 手机app

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
				String WXNR = "【生产任务异常提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
				// 手机app-极光推送
				String UserName = staffPD.getString("USER_ID");
				pd.put("UserName", UserName);
				PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
				if (pdf != null) {
					String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
					String notification_title = "工作通知";
					String msg_title = "生产任务异常";
					String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
							+ findById.getString("WPName") + "，产出物料为" + findById.getString("ProcessIMaterielCode")
							+ "的生产任务上报异常";
					if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title, msg_title, msg_content,
							"") == 1) {
						// errInfo="success";
					} else {
						// errInfo="error";
					}
				}
			}

		}

		// 下发
		ExceptionService.goLssue(pd);
		try { // 下发异常时，同时新增一条异常处理记录
			pdHandling.put("ExceptionHandling_ID", this.get32UUID());// 主键ID
			pdHandling.put("Exception_ID", pd.get("Exception_ID"));// 异常ID
			String InitOperatorID = pd.get("InitOperatorID").toString();
			if (InitOperatorID == "部门" || InitOperatorID.equals("部门")) {
				pdHandling.put("InitOperatorID", 1);// 移交给部门
			} else {
				pdHandling.put("InitOperatorID", 2);// 移交给人
			}
			pdHandling.put("WaitingOperatorID", pd.get("ExceptionPendingParty"));// 待处理人或部门
			pdHandling.put("IfTurnOver", 0);// 是否移交处理 默认0 不移交
			pdHandling.put("Reorder", 1);// 排序字段，自动生成时为1，每移交一次+1
			pdHandling.put("DisposeType", "待处理");// 处理判别，初始默认待处理
			pdHandling.put("FOperator", pd.get("InitOperatorID"));// 移交给人
			ExceptionHandlingService.save(pdHandling);
			operationrecordService.add("", "异常", "下发", pd.getString("Exception_ID"), "", "");// 操作日志
		} catch (Exception e) {
			errInfo = "failure";//
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 保存并下发
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goLssue")
	@ResponseBody
	public Object goLssueAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdHandling = new PageData();
		pd = this.getPageData();
		ExceptionService.goLssue(pd);
		try { // 下发异常时，同时新增一条异常处理记录
			pdHandling.put("ExceptionHandling_ID", this.get32UUID());// 主键ID
			pdHandling.put("Exception_ID", pd.get("Exception_ID"));// 异常ID
			String InitOperatorID = pd.get("InitOperatorID").toString();
			if (InitOperatorID == "部门" || InitOperatorID.equals("部门")) {
				pdHandling.put("InitOperatorID", 1);// 移交给部门
			} else {
				pdHandling.put("InitOperatorID", 2);// 移交给人
			}
			pdHandling.put("WaitingOperatorID", pd.get("ExceptionPendingParty"));// 待处理人或部门
			pdHandling.put("IfTurnOver", 0);// 是否移交处理 默认0 不移交
			pdHandling.put("Reorder", 1);// 排序字段，自动生成时为1，每移交一次+1
			pdHandling.put("DisposeType", "待处理");// 处理判别，初始默认待处理
			pdHandling.put("FOperator", pd.get("InitOperatorID"));// 移交给人
			ExceptionHandlingService.save(pdHandling);
			operationrecordService.add("", "异常", "下发", pd.getString("Exception_ID"), "", "");// 操作日志
		} catch (Exception e) {
			errInfo = "failure";//
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
	// @RequiresPermissions("Exception:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ExceptionService.delete(pd);
		operationrecordService.add("", "异常", "删除", pd.getString("Exception_ID"), "", "");// 操作日志
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
	// @RequiresPermissions("Exception:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ExceptionService.edit(pd);
		operationrecordService.add("", "异常", "修改", pd.getString("Exception_ID"), "", "");// 操作日志
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
	// @RequiresPermissions("Exception:list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		pd.put("FNAME", Jurisdiction.getName());
		PageData pdS = staffService.getStaffId(pd);
		String STAFF_ID = "";
		if (pdS != null) {
			STAFF_ID = pdS.getString("STAFF_ID");
		}
		pd.put("USERNAME", Jurisdiction.getName());
		List<PageData> varList = ExceptionService.list(page); // 列出Exception列表
		for (int i = 1; i < varList.size(); i++) {
			PageData pdVar = varList.get(i);
			String InitOperatorID = pdVar.getString("InitOperatorID");
			String InitOperatorID1 = pdVar.getString("InitOperatorID1");
			if (InitOperatorID.equals("部门")) {
				String DEPARTMENT_ID = pdVar.getString("ExceptionPendingParty");
				String depts = departmentService.getDEPARTMENT_IDSSS(DEPARTMENT_ID);
				pd.put("depts", depts);
				List<PageData> staffList = staffService.listAllByDept(pd);
				if (staffList.size() > 0) {
					pdVar.put("IsXF", "1");
				} else {
					pdVar.put("IsXF", "0");
				}

			} else if (InitOperatorID.equals("人")) {
				if (STAFF_ID.equals(pdVar.getString("ExceptionPendingParty"))) {
					pdVar.put("IsXF", "1");
				} else {
					pdVar.put("IsXF", "0");
				}
			}

			if (InitOperatorID1.equals("部门")) {
				String deptID = pdVar.getString("deptID");
				String deptss = departmentService.getDEPARTMENT_IDSSS(deptID);
				pd.put("depts", deptss);
				List<PageData> staffLists = staffService.listAllByDept(pd);
				if (staffLists.size() > 0) {
					pdVar.put("IsDeal", "1");
				} else {
					pdVar.put("IsDeal", "0");
					varList.remove(i);
				}
			} else if (InitOperatorID1.equals("人")) {
				int FNUM1 = Integer.parseInt(pdVar.get("FNUM1").toString());
				if (FNUM1 > 0) {
					pdVar.put("IsDeal", "1");
				} else {
					pdVar.put("IsDeal", "0");
				}
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210928 项目异常记录
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listXM")
	// @RequiresPermissions("Exception:list")
	@ResponseBody
	public Object listXM(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		pd.put("FNAME", Jurisdiction.getName());
		PageData pdS = staffService.getStaffId(pd);
		String STAFF_ID = "";
		if (pdS != null) {
			STAFF_ID = pdS.getString("STAFF_ID");
		}
		pd.put("USERNAME", Jurisdiction.getName());
		List<PageData> varList = ExceptionService.listXM(page); // 列出Exception列表
		for (int i = 1; i < varList.size(); i++) {
			PageData pdVar = varList.get(i);
			String InitOperatorID = pdVar.getString("InitOperatorID");
			String InitOperatorID1 = pdVar.getString("InitOperatorID1");
			if (InitOperatorID.equals("部门")) {
				String DEPARTMENT_ID = pdVar.getString("ExceptionPendingParty");
				String depts = departmentService.getDEPARTMENT_IDSSS(DEPARTMENT_ID);
				pd.put("depts", depts);
				List<PageData> staffList = staffService.listAllByDept(pd);
				if (staffList.size() > 0) {
					pdVar.put("IsXF", "1");
				} else {
					pdVar.put("IsXF", "0");
				}

			} else if (InitOperatorID.equals("人")) {
				if (STAFF_ID.equals(pdVar.getString("ExceptionPendingParty"))) {
					pdVar.put("IsXF", "1");
				} else {
					pdVar.put("IsXF", "0");
				}
			}

			if (InitOperatorID1.equals("部门")) {
				String deptID = pdVar.getString("deptID");
				String deptss = departmentService.getDEPARTMENT_IDSSS(deptID);
				pd.put("depts", deptss);
				List<PageData> staffLists = staffService.listAllByDept(pd);
				if (staffLists.size() > 0) {
					pdVar.put("IsDeal", "1");
				} else {
					pdVar.put("IsDeal", "0");
					varList.remove(i);
				}
			} else if (InitOperatorID1.equals("人")) {
				int FNUM1 = Integer.parseInt(pdVar.get("FNUM1").toString());
				if (FNUM1 > 0) {
					pdVar.put("IsDeal", "1");
				} else {
					pdVar.put("IsDeal", "0");
				}
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 设备列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getEQMList")
	@ResponseBody
	public Object getEQMList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = ExceptionService.getEQMList(pd); // 列出Exception列表
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
	// @RequiresPermissions("Exception:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ExceptionService.findById(pd); // 根据ID读取
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
	// @RequiresPermissions("Exception:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ExceptionService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		} else {
			errInfo = "error";
		}
		operationrecordService.add("", "异常", "批量删除", DATA_IDS, "", "");// 操作日志
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
	// @RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("优先级"); // 1
		titles.add("数据来源"); // 2
		titles.add("等级"); // 3
		titles.add("异常待处理人"); // 4
		titles.add("异常待处理部门"); // 5
		titles.add("异常定义"); // 6
		titles.add("异常类型"); // 7
		titles.add("异常描述"); // 8
		titles.add("发布人"); // 9
		titles.add("发布时间"); // 10
		titles.add("要求处理时间"); // 11
		titles.add("是否监听"); // 12
		titles.add("临近触发时间"); // 13
		titles.add("监听次数"); // 14
		titles.add("处理类型"); // 15
		titles.add("移交给人，或部门"); // 16
		titles.add("执行状态"); // 17
		titles.add("排序值"); // 18
		titles.add("是否移交处理"); // 19
		titles.add("移交时间"); // 20
		titles.add("处理判别"); // 21
		titles.add("处理描述"); // 22
		titles.add("处理人"); // 23
		titles.add("处理时间"); // 24
		titles.add("处理状态"); // 25
		dataMap.put("titles", titles);
		List<PageData> varOList = ExceptionService.listAllHandling(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("FPriorityID").toString()); // 1
			vpd.put("var2", varOList.get(i).get("FDateSource").toString()); // 2
			vpd.put("var3", varOList.get(i).get("FLevel").toString()); // 3
			vpd.put("var4", varOList.get(i).get("NAMEStaff").toString()); // 4
			vpd.put("var5", varOList.get(i).get("NAMEDept").toString()); // 5
			vpd.put("var6", varOList.get(i).get("ExceptionDefinition").toString()); // 6
			vpd.put("var7", varOList.get(i).get("NAMEExcept").toString()); // 7
			vpd.put("var8", varOList.get(i).get("FExplanation").toString()); // 8
			vpd.put("var9", varOList.get(i).get("NAMERel").toString()); // 9
			vpd.put("var10", varOList.get(i).get("ReleaseTime").toString()); // 10
			vpd.put("var11", varOList.get(i).get("ProcessingTimeRequired").toString()); // 11
			vpd.put("var12", varOList.get(i).get("IfMonitorProcessing").toString()); // 12
			vpd.put("var13", varOList.get(i).get("NearTriggerMonitoringTime").toString()); // 13
			vpd.put("var14", varOList.get(i).get("MonitoringTimes").toString()); // 14
			vpd.put("var15", varOList.get(i).get("DealType").toString()); // 15
			vpd.put("var16", varOList.get(i).get("InitOperatorID").toString()); // 16
			vpd.put("var17", varOList.get(i).get("IssueType").toString()); // 17
			vpd.put("var18", varOList.get(i).get("Reorder").toString()); // 18
			vpd.put("var19", varOList.get(i).get("IfTurnOver").toString()); // 19
			vpd.put("var20", varOList.get(i).get("TurnOverTime").toString()); // 20
			vpd.put("var21", varOList.get(i).get("DisposeType").toString()); // 21
			vpd.put("var22", varOList.get(i).get("FExplanationMX").toString()); // 22
			vpd.put("var23", varOList.get(i).get("FOperatorID").toString()); // 23
			vpd.put("var24", varOList.get(i).get("FTime").toString()); // 24
			vpd.put("var25", varOList.get(i).get("FStatus").toString()); // 25
			varList.add(vpd);
		}
		operationrecordService.add("", "异常", "导出", pd.getString("Exception_ID"), "", "");// 操作日志
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 异常处理记录列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/datalistPageHandling")
	// @RequiresPermissions("Exception:list")
	@ResponseBody
	public Object datalistPageHandling(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = ExceptionService.datalistPageHandling(page); // 列出Exception列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 异常记录查看(单条)
	 */
	@RequestMapping(value = "/getHandling")
	// @RequiresPermissions("Exception:list")
	@ResponseBody
	public Object getHandling() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ExceptionService.getHandling(pd); // 列出Exception列表
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 生产看板-异常任务
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listSC")
	// @RequiresPermissions("Exception:list")
	@ResponseBody
	public Object listSC(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		pd.put("FNAME", Jurisdiction.getName());
		PageData pdS = staffService.getStaffId(pd);
		String STAFF_ID = "";
		if (pdS != null) {
			STAFF_ID = pdS.getString("STAFF_ID");
		}
		pd.put("USERNAME", Jurisdiction.getName());
		List<PageData> varList = ExceptionService.listSC(page); // 列出Exception列表
		for (PageData pdVar : varList) {
			String InitOperatorID = pdVar.getString("InitOperatorID");
			String InitOperatorID1 = pdVar.getString("InitOperatorID1");
			if (InitOperatorID.equals("部门")) {
				String DEPARTMENT_ID = pdVar.getString("ExceptionPendingParty");
				String depts = departmentService.getDEPARTMENT_IDSSS(DEPARTMENT_ID);
				pd.put("depts", depts);
				List<PageData> staffList = staffService.listAllByDept(pd);
				if (staffList.size() > 0) {
					pdVar.put("IsXF", "1");
				} else {
					pdVar.put("IsXF", "0");
				}

			} else if (InitOperatorID.equals("人")) {
				if (STAFF_ID.equals(pdVar.getString("ExceptionPendingParty"))) {
					pdVar.put("IsXF", "1");
				} else {
					pdVar.put("IsXF", "0");
				}
			}

			if (InitOperatorID1.equals("部门")) {
				String deptID = pdVar.getString("deptID");
				String deptss = departmentService.getDEPARTMENT_IDSSS(deptID);
				pd.put("depts", deptss);
				List<PageData> staffLists = staffService.listAllByDept(pd);
				if (staffLists.size() > 0) {
					pdVar.put("IsDeal", "1");
				} else {
					pdVar.put("IsDeal", "0");
				}
			} else if (InitOperatorID1.equals("人")) {
				int FNUM1 = Integer.parseInt(pdVar.get("FNUM1").toString());
				if (FNUM1 > 0) {
					pdVar.put("IsDeal", "1");
				} else {
					pdVar.put("IsDeal", "0");
				}
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210923 设置已读
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setRead")
	@ResponseBody
	public Object setRead() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FReadMan", Jurisdiction.getName());
		pd.put("FReadTime", Tools.date2Str(new Date()));
		pd.put("FIs_Read", "已读");
		ExceptionService.setRead(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 2021026 同类异常处理记录：已完成并且异常类型相同的异常处理记录
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
		PageData pdE = ExceptionService.findById(pd);
		pdE.put("USERNAME", Jurisdiction.getName());
		page.setPd(pdE);
		List<PageData> varList = ExceptionService.listHis(page); // 列出Exception列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
}
