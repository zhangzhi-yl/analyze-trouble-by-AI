package org.yy.controller.project.manager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.CodingRulesDetailService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.ProcessDefinitionService;
import org.yy.service.km.ProcessRouteService;
import org.yy.service.km.QualityInspectionPlanInspectionDetailsService;
import org.yy.service.km.SopStepService;
import org.yy.service.km.WORKINGPROCEDURESELFCHECKEXAMPLEService;
import org.yy.service.km.WORKINGPROCEDURESELFCHECKService;
import org.yy.service.km.WorkingProcedureService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mbase.MAT_DESIGNService;
import org.yy.service.mbase.MAT_LOGISTICService;
import org.yy.service.mbase.MAT_QUALITYService;
import org.yy.service.mbase.MAT_SPECService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.WH_LocationService;
import org.yy.service.mom.WH_WareHouseService;
import org.yy.service.pp.PlanningWorkOrderMasterService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessWorkOrderExample_SopStepService;
import org.yy.service.pp.PurchaseListService;
import org.yy.service.pp.PurchaseList_CommentsService;
import org.yy.service.pp.PurchaseMaterialDetailsService;
import org.yy.service.project.manager.CABINET_AUDITService;
import org.yy.service.project.manager.Cabinet_AssemblyService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.service.project.manager.Cabinet_BOM_changeMxService;
import org.yy.service.project.manager.Cabinet_BOM_changeService;
import org.yy.service.project.manager.ChangeService;
import org.yy.service.project.manager.DPROJECTService;
import org.yy.service.qm.QualityInspectionPlanDetailExecuteService;
import org.yy.service.qm.QualityInspectionPlanExecuteService;
import org.yy.service.system.DictionariesService;
import org.yy.service.system.UsersService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.util.StringUtil;

/**
 * 说明：装配详情表 作者：YuanYes QQ356703572 时间：2021-04-30 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/Cabinet_Assembly_Detail")
public class Cabinet_Assembly_DetailController extends BaseController {
	@Autowired
	private ProcessDefinitionService ProcessDefinitionService;// 工序定义serverce
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;
	@Autowired
	private WorkingProcedureService WorkingProcedureService;
	@Autowired
	private ProcessRouteService ProcessRouteService;
	@Autowired
	private Cabinet_BOMService Cabinet_BOMService;
	@Autowired
	private StaffService StaffService;
	@Autowired
	private CodingRulesService codingRulesService;
	@Autowired
	private AttachmentSetService AttachmentSetService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private MAT_DESIGNService mat_designService;// 设计资料
	@Autowired
	private MAT_LOGISTICService mat_logisticService;// 物料
	@Autowired
	private MAT_QUALITYService mat_qualityService;// 质量资料
	@Autowired
	private PlanningWorkOrderMasterService PlanningWorkOrderMasterService;
	@Autowired
	private MAT_SPECService MAT_SPECService;// 物料规格
	@Autowired
	private StaffService staffService;// 员工
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	@Autowired
	private SopStepService SopStepService;
	@Autowired
	private ProcessWorkOrderExample_SopStepService pwoeSopStepService;
	@Autowired
	private BYTEARRAYService BYTEARRAYService;
	@Autowired
	private StockService StockService;
	@Autowired
	private PurchaseListService PurchaseListService;
	@Autowired
	private PurchaseList_CommentsService PurchaseList_CommentsService;// 审核意见
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private PurchaseMaterialDetailsService purchaseMaterialDetailsService;
	@Autowired
	private WH_WareHouseService wh_warehouseService;
	@Autowired
	private WH_LocationService wh_locationService;
	@Autowired
	private Cabinet_AssemblyService Cabinet_AssemblyService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private DPROJECTService dprojectService;

	@Autowired
	private CABINET_AUDITService cabinet_auditService;
	@Autowired
	private WORKINGPROCEDURESELFCHECKEXAMPLEService workingprocedureselfcheckexampleService;

	@Autowired
	private WORKINGPROCEDURESELFCHECKService workingprocedureselfcheckService;
	@Autowired
	private QualityInspectionPlanExecuteService QualityInspectionPlanExecuteService;
	@Autowired
	private QualityInspectionPlanDetailExecuteService QualityInspectionPlanDetailExecuteService;
	@Autowired
	private QualityInspectionPlanInspectionDetailsService QualityInspectionPlanInspectionDetailsService;
	@Autowired
	private DictionariesService dictionariesService;
	@Autowired
	private CodingRulesService codingrulesService;

	@Autowired
	private CodingRulesDetailService codingRulesDetailService;
	@Autowired
	private Cabinet_BOM_changeService cabinet_bom_changeService;
	@Autowired
	private ChangeService ChangeService;
	@Autowired
	private Cabinet_BOM_changeMxService cabinet_bom_changemxService;
	@Autowired
	private UsersService usersService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			// 主键
			pd.put("Cabinet_Assembly_Detail_ID", this.get32UUID());
			pd.put("FIs_Need_Audit", "否");
			Cabinet_Assembly_DetailService.save(pd);
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
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			Cabinet_Assembly_DetailService.delete(pd);
			// 返回结果
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
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData findByNameParam = new PageData();
			findByNameParam.put("Cabinet_Aliases", pd.getString("Cabinet_Aliases"));
			findByNameParam.put("Cabinet_Edit_ID", pd.getString("Cabinet_Assembly_Detail_ID"));
			List<PageData> findByNameList = Cabinet_Assembly_DetailService.listAll(findByNameParam);

			/*
			 * if (!CollectionUtils.isEmpty(findByNameList)) { map.put("result", "failed");
			 * map.put("msg", "柜体别名重复"); return map; }
			 */
			Cabinet_Assembly_DetailService.edit(pd);
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
	 * v1 吴双双 20210607 根据技术负责人修改
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
			String STAFF_ID = pd.getString("STAFF_ID");// 负责人ID
			PageData findByAssemblyID = new PageData();
			findByAssemblyID.put("Cabinet_Assembly_ID", pd.getString("Cabinet_Assembly_ID"));// 柜体主键ID
			List<PageData> findByAssemblyIDList = Cabinet_Assembly_DetailService.findByAssemblyID(findByAssemblyID);// 根据主键获取柜体列表
			for (int i = 0; i < findByAssemblyIDList.size(); i++) {
				PageData pdPro = findByAssemblyIDList.get(i);
				PageData pddata = Cabinet_Assembly_DetailService.findById(pdPro);// 获取单个柜体信息
				String FStatus = pddata.getString("FStatus");
				if ("创建".equals(FStatus)) {
					String DetailID = pddata.getString("Cabinet_Assembly_Detail_ID");// 获取单个柜体的主键
					PageData pdParend = new PageData();
					pdParend.put("Cabinet_Assembly_Detail_ID", DetailID);
					pdParend.put("Responsible_Technology", STAFF_ID);
					Cabinet_Assembly_DetailService.editByUser(pdParend);// 根据主键和负责人ID，对柜体负责人进行修改
				}
			}
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
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			// 关键词检索条件
			String KEYWORDS = pd.getString("KEYWORDS");
			if (Tools.notEmpty(KEYWORDS)) {
				pd.put("KEYWORDS", KEYWORDS.trim());
			}
			page.setPd(pd);
			List<PageData> varList = Cabinet_Assembly_DetailService.list(page); // 列出Cabinet_Assembly_Detail列表
			map.put("varList", varList);
			map.put("page", page);
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
	 * 修改状态
	 * 
	 * @param
	 * @throws Exception
	 *//*
		 * @RequestMapping(value = "/editIFStatus")
		 * 
		 * @ResponseBody public Object editIFStatus() throws Exception { Map<String,
		 * Object> map = new HashMap<String, Object>(10); try { String errInfo =
		 * "success"; PageData pd = new PageData(); pd = this.getPageData(); String
		 * FIs_Need_Audit=pd.getString("FIs_Need_Audit"); PageData old =
		 * Cabinet_Assembly_DetailService.findById(pd); if
		 * (StringUtil.isNotEmpty(pd.getString("If_Bom_Done"))) { old.put("If_Bom_Done",
		 * pd.getString("If_Bom_Done")); old.put("Bom_Done_Time", Tools.date2Str(new
		 * Date())); // 判断 BOM 是否存在数据 不存在不让 List<PageData> listAll =
		 * Cabinet_BOMService.listAll(pd); if (CollectionUtils.isEmpty(listAll)) {
		 * map.put("result", "failed"); map.put("msg", "BOM数据不存在不能修改状态"); return map; }
		 * if(FIs_Need_Audit.equals("是")) {//需要技术负责人审核 old.put("FIs_Need_Audit", "是");
		 * cabinet_auditService.findById(pd); PageData pdAudit = new PageData();
		 * pdAudit.put("CABINET_AUDIT_ID", this.get32UUID()); //主键
		 * pdAudit.put("FAUDIT_STATE", "待审核"); pdAudit.put("FTYPE", "BOM完成审核");
		 * pdAudit.put("CABINET_ASSEMBLY_DETAIL_ID",
		 * pd.getString("Cabinet_Assembly_Detail_ID")); pdAudit.put("FCREATOR",
		 * Jurisdiction.getName()); // 发布人 pdAudit.put("FCTIME", DateUtil.date2Str(new
		 * Date())); // 发布时间 cabinet_auditService.save(pdAudit);
		 * old.put("FAudit_Bom_State", "待审核"); }else {
		 * 
		 * // 完成bom 提醒 String Cabinet_Assembly_Detail_ID =
		 * pd.getString("Cabinet_Assembly_Detail_ID"); PageData caData = new PageData();
		 * caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
		 * PageData cad = Cabinet_Assembly_DetailService.findById(caData);
		 * 
		 * String caID = cad.getString("Cabinet_Assembly_ID"); PageData caParam = new
		 * PageData(); caParam.put("Cabinet_Assembly_ID", caID);
		 * 
		 * PageData ca = Cabinet_AssemblyService.findById(caParam);
		 * 
		 * String PROJECT_ID = ca.getString("PROJECT_ID"); PageData projectParam = new
		 * PageData();
		 * 
		 * projectParam.put("PROJECT_ID", PROJECT_ID); PageData project =
		 * dprojectService.findById(projectParam);
		 * 
		 * String PPROJECT_MANAGER = project.getString("PPROJECT_MANAGER");
		 * 
		 * PageData staffParam = new PageData(); staffParam.put("FNAME",
		 * PPROJECT_MANAGER);
		 * 
		 * PageData staffInfo = StaffService.getStaffId(staffParam);
		 * 
		 * PageData pdNotice = new PageData(); // 跳转页面 pdNotice.put("AccessURL",
		 * "../../../views/projectManager/pro_plan/pro_plan_jssj.html");//
		 * pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键 pdNotice.put("ReadPeople",
		 * ",");// 已读人默认空 pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
		 * pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
		 * pdNotice.put("TType", "消息推送");// 消息类型 pdNotice.put("FContent", "负责技术" +
		 * Jurisdiction.getName() + "完成了柜体" + cad.getString("Cabinet_No") +
		 * "的BOM，请注意查看");// 消息正文 pdNotice.put("FTitle", "柜体BOM完成");// 消息标题
		 * pdNotice.put("LinkIf", "no");// 是否跳转页面 pdNotice.put("DataSources",
		 * "柜体BOM");// 数据来源 pdNotice.put("ReceivingAuthority", "," +
		 * staffInfo.getString("USER_ID") + ",");// 接收人 pdNotice.put("Report_Key",
		 * "changeBOM"); pdNotice.put("Report_Value", ""); noticeService.save(pdNotice);
		 * old.put("FAudit_Bom_State", "通过"); }
		 * 
		 * 
		 * } if (StringUtil.isNotEmpty(pd.getString("If_Drawings_Done"))) {
		 * old.put("If_Drawings_Done", pd.getString("If_Drawings_Done"));
		 * old.put("Drawings_Done_Time", Tools.date2Str(new Date())); // 判断 图纸是否上传 不存在不让
		 * PageData pData = new PageData(); pData.put("AssociationID",
		 * pd.getString("Cabinet_Assembly_Detail_ID")); List<PageData> findByAssId =
		 * AttachmentSetService.findByAssId(pData); if
		 * (CollectionUtils.isEmpty(findByAssId)) { map.put("result", "failed");
		 * map.put("msg", "图纸数据不存在不能修改状态"); return map; } if(FIs_Need_Audit.equals("是"))
		 * {//需要技术负责人审核 cabinet_auditService.findById(pd); PageData pdAudit = new
		 * PageData(); pdAudit.put("CABINET_AUDIT_ID", this.get32UUID()); //主键
		 * pdAudit.put("FAUDIT_STATE", "待审核"); pdAudit.put("FTYPE", "图纸完成审核");
		 * pdAudit.put("CABINET_ASSEMBLY_DETAIL_ID",
		 * pd.getString("Cabinet_Assembly_Detail_ID")); pdAudit.put("FCREATOR",
		 * Jurisdiction.getName()); // 发布人 pdAudit.put("FCTIME", DateUtil.date2Str(new
		 * Date())); // 发布时间 cabinet_auditService.save(pdAudit);
		 * old.put("FAudit_Cabinet_State", "待审核"); }else { //完成图纸 提醒 String
		 * Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
		 * PageData caData = new PageData(); caData.put("Cabinet_Assembly_Detail_ID",
		 * Cabinet_Assembly_Detail_ID); PageData cad =
		 * Cabinet_Assembly_DetailService.findById(caData);
		 * 
		 * String caID = cad.getString("Cabinet_Assembly_ID"); PageData caParam = new
		 * PageData(); caParam.put("Cabinet_Assembly_ID", caID);
		 * 
		 * PageData ca = Cabinet_AssemblyService.findById(caParam);
		 * 
		 * String PROJECT_ID = ca.getString("PROJECT_ID"); PageData projectParam = new
		 * PageData();
		 * 
		 * projectParam.put("PROJECT_ID", PROJECT_ID); PageData project =
		 * dprojectService.findById(projectParam);
		 * 
		 * String PPROJECT_MANAGER = project.getString("PPROJECT_MANAGER");
		 * 
		 * PageData staffParam = new PageData(); staffParam.put("FNAME",
		 * PPROJECT_MANAGER);
		 * 
		 * PageData staffInfo = StaffService.getStaffId(staffParam);
		 * 
		 * PageData pdNotice = new PageData(); // 跳转页面 pdNotice.put("AccessURL",
		 * "../../../views/projectManager/pro_plan/pro_plan_jssj.html");//
		 * pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键 pdNotice.put("ReadPeople",
		 * ",");// 已读人默认空 pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
		 * pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
		 * pdNotice.put("TType", "消息推送");// 消息类型 pdNotice.put("FContent", "负责技术" +
		 * Jurisdiction.getName() + "完成了柜体" + cad.getString("Cabinet_No") +
		 * "的图纸，请注意查看");// 消息正文 pdNotice.put("FTitle", "柜体图纸完成");// 消息标题
		 * pdNotice.put("LinkIf", "no");// 是否跳转页面 pdNotice.put("DataSources", "柜体图纸");//
		 * 数据来源 pdNotice.put("ReceivingAuthority", "," + staffInfo.getString("USER_ID")
		 * + ",");// 接收人 pdNotice.put("Report_Key", "changeDrawing");
		 * pdNotice.put("Report_Value", ""); noticeService.save(pdNotice);
		 * old.put("FAudit_Cabinet_State", "通过"); }
		 * 
		 * }
		 * 
		 * Cabinet_Assembly_DetailService.edit(old); map.put("result", errInfo); return
		 * map; } catch (Exception e) { e.printStackTrace(); map.put("result",
		 * "failed"); map.put("msg", e.getMessage()); return map; }
		 * 
		 * }
		 */
	/**
	 * 修改状态
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/editIFStatus")
	@ResponseBody
	public Object editIFStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String FIs_Need_Audit = pd.getString("FIs_Need_Audit");
			PageData old = Cabinet_Assembly_DetailService.findById(pd);
			if (StringUtil.isNotEmpty(pd.getString("If_Bom_Done"))) {
				old.put("If_Bom_Done", pd.getString("If_Bom_Done"));
				old.put("Bom_Done_Time", Tools.date2Str(new Date()));
				// 判断 BOM 是否存在数据 不存在不让
				pd.put("FFTYPE", "BOM");
				List<PageData> listAll = Cabinet_BOMService.listAll(pd);
				if (CollectionUtils.isEmpty(listAll)) {
					map.put("result", "failed");
					map.put("msg", "BOM数据不存在或者信息不完整，不能修改状态");
					return map;
				}

				// 完成bom 提醒
				String Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
				PageData caData = new PageData();
				caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
				PageData cad = Cabinet_Assembly_DetailService.findById(caData);

				String caID = cad.getString("Cabinet_Assembly_ID");
				PageData caParam = new PageData();
				caParam.put("Cabinet_Assembly_ID", caID);

				PageData ca = Cabinet_AssemblyService.findById(caParam);

				String PROJECT_ID = ca.getString("PROJECT_ID");
				PageData projectParam = new PageData();

				projectParam.put("PROJECT_ID", PROJECT_ID);
				PageData project = dprojectService.findById(projectParam);

				String PPROJECT_MANAGER = project.getString("PPROJECT_MANAGER");

				PageData staffParam = new PageData();
				staffParam.put("FNAME", PPROJECT_MANAGER);

				PageData staffInfo = StaffService.getStaffId(staffParam);

				PageData pdNotice = new PageData();
				// 跳转页面
				pdNotice.put("AccessURL",
						"../../../views/projectManager/pro_plan/pro_plan_jssj.html?Cabinet_Assembly_Detail_ID="
								+ Cabinet_Assembly_Detail_ID + "&NOTICE_TYPE=" + "ALL");//
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"负责技术" + Jurisdiction.getName() + "完成了柜体" + cad.getString("Cabinet_No") + "的BOM，请注意查看");// 消息正文
				pdNotice.put("FTitle", "柜体BOM完成");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "柜体BOM");// 数据来源
				pdNotice.put("ReceivingAuthority", "," + staffInfo.getString("USER_ID") + ",");// 接收人
				pdNotice.put("Report_Key", "changeBOM");
				pdNotice.put("Report_Value", "");
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
				String WXNR = "【柜体BOM完成】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
				old.put("FAudit_Bom_State", "通过");
			}

			if (StringUtil.isNotEmpty(pd.getString("If_Drawings_Done"))) {
				old.put("If_Drawings_Done", pd.getString("If_Drawings_Done"));
				old.put("Drawings_Done_Time", Tools.date2Str(new Date()));
				// 判断 图纸是否上传 不存在不让
				PageData pData = new PageData();
				pData.put("AssociationID", pd.getString("Cabinet_Assembly_Detail_ID"));
				List<PageData> findByAssId = AttachmentSetService.findByAssId(pData);
				if (CollectionUtils.isEmpty(findByAssId)) {
					map.put("result", "failed");
					map.put("msg", "图纸数据不存在不能修改状态");
					return map;
				}

				// 完成图纸 提醒
				String Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
				PageData caData = new PageData();
				caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
				PageData cad = Cabinet_Assembly_DetailService.findById(caData);

				String caID = cad.getString("Cabinet_Assembly_ID");
				PageData caParam = new PageData();
				caParam.put("Cabinet_Assembly_ID", caID);

				PageData ca = Cabinet_AssemblyService.findById(caParam);

				String PROJECT_ID = ca.getString("PROJECT_ID");
				PageData projectParam = new PageData();

				projectParam.put("PROJECT_ID", PROJECT_ID);
				PageData project = dprojectService.findById(projectParam);

				String PPROJECT_MANAGER = project.getString("PPROJECT_MANAGER");

				PageData staffParam = new PageData();
				staffParam.put("FNAME", PPROJECT_MANAGER);

				PageData staffInfo = StaffService.getStaffId(staffParam);

				PageData pdNotice = new PageData();
				// 跳转页面
				pdNotice.put("AccessURL",
						"../../../views/projectManager/pro_plan/pro_plan_jssj.html?Cabinet_Assembly_Detail_ID="
								+ Cabinet_Assembly_Detail_ID + "&NOTICE_TYPE=" + "ALL");//
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"负责技术" + Jurisdiction.getName() + "完成了柜体" + cad.getString("Cabinet_No") + "的图纸，请注意查看");// 消息正文
				pdNotice.put("FTitle", "柜体图纸完成");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "柜体图纸");// 数据来源
				pdNotice.put("ReceivingAuthority", "," + staffInfo.getString("USER_ID") + ",");// 接收人
				pdNotice.put("Report_Key", "changeDrawing");
				pdNotice.put("Report_Value", "");
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
				String WXNR = "【柜体图纸完成】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
				old.put("FAudit_Cabinet_State", "通过");
			}

			Cabinet_Assembly_DetailService.edit(old);
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
	 * 一键结束状态
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/oneKeyDone")
	@ResponseBody
	public Object oneKeyDone() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String FIs_Need_Audit = pd.getString("FIs_Need_Audit");
			// 判断 BOM 是否存在数据 不存在不让
			pd.put("FFTYPE", "BOM");
			List<PageData> listAll = Cabinet_BOMService.listAll(pd);
			if (CollectionUtils.isEmpty(listAll)) {
				map.put("result", "failed");
				map.put("msg", "BOM数据不存在或者信息不完整，不能修改状态");
				return map;
			}
			// 判断 图纸 是否上传 不存在不让
			PageData pData = new PageData();
			pData.put("AssociationID", pd.getString("Cabinet_Assembly_Detail_ID"));
			List<PageData> findByAssId = AttachmentSetService.findByAssId(pData);
			if (CollectionUtils.isEmpty(findByAssId)) {
				map.put("result", "failed");
				map.put("msg", "图纸数据不存在不能修改状态");
				return map;
			}

			if (FIs_Need_Audit.equals("是")) {// 需要技术负责人审核
				PageData pdAudit = new PageData();
				pdAudit.put("CABINET_AUDIT_ID", this.get32UUID()); // 主键
				pdAudit.put("FAUDIT_STATE", "待审核");
				pdAudit.put("FTYPE", "技术审核");
				pdAudit.put("CABINET_ASSEMBLY_DETAIL_ID", pd.getString("Cabinet_Assembly_Detail_ID"));
				pdAudit.put("FCREATOR", Jurisdiction.getName()); // 发布人
				pdAudit.put("FCTIME", DateUtil.date2Str(new Date())); // 发布时间
				cabinet_auditService.save(pdAudit);
				PageData old = Cabinet_Assembly_DetailService.findById(pd);
				old.put("FAudit_Bom_State", "待审核");
				old.put("FIs_Need_Audit", FIs_Need_Audit);
				old.put("FAudit_Cabinet_State", "待审核");
				old.put("FAudit_State", "待审核");
				old.put("If_Bom_Done", "是");
				old.put("If_Drawings_Done", "是");
				// old.put("FStatus", "设计结束");
				old.put("FIs_Need_Audit", "是");
				old.put("Bom_Done_Time", Tools.date2Str(new Date()));
				old.put("Drawings_Done_Time", Tools.date2Str(new Date()));
				Cabinet_Assembly_DetailService.edit(old);
			} else {
				PageData old = Cabinet_Assembly_DetailService.findById(pd);
				old.put("FAudit_Bom_State", "通过");
				old.put("FAudit_State", "直接通过");

				old.put("FAudit_Cabinet_State", "通过");
				old.put("If_Bom_Done", "是");
				old.put("If_Drawings_Done", "是");
				old.put("FStatus", "设计结束");
				old.put("FIs_Need_Audit", FIs_Need_Audit);
				old.put("Bom_Done_Time", Tools.date2Str(new Date()));
				old.put("Drawings_Done_Time", Tools.date2Str(new Date()));
				old.put("FEnd_Time", Tools.date2Str(new Date()));

				Cabinet_Assembly_DetailService.edit(old);

				// 完成图纸和BOM 提醒
				String Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
				PageData caData = new PageData();
				caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
				PageData cad = Cabinet_Assembly_DetailService.findById(caData);

				String caID = cad.getString("Cabinet_Assembly_ID");
				PageData caParam = new PageData();
				caParam.put("Cabinet_Assembly_ID", caID);

				PageData ca = Cabinet_AssemblyService.findById(caParam);

				String PROJECT_ID = ca.getString("PROJECT_ID");
				PageData projectParam = new PageData();

				projectParam.put("PROJECT_ID", PROJECT_ID);
				PageData project = dprojectService.findById(projectParam);

				String PPROJECT_MANAGER = project.getString("PPROJECT_MANAGER");

				PageData staffParam = new PageData();
				staffParam.put("FNAME", PPROJECT_MANAGER);

				PageData staffInfo = StaffService.getStaffId(staffParam);

				PageData pdNotice = new PageData();
				// 跳转页面
				pdNotice.put("AccessURL",
						"../../../views/projectManager/pro_plan/pro_plan_jssj.html?Cabinet_Assembly_Detail_ID="
								+ Cabinet_Assembly_Detail_ID + "&NOTICE_TYPE=" + "ALL");//
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						"负责技术" + Jurisdiction.getName() + "技术结束了柜体" + cad.getString("Cabinet_No") + "的图纸和BOM，请注意查看");// 消息正文
				pdNotice.put("FTitle", "柜体图纸BOM完成");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "柜体图纸BOM");// 数据来源
				pdNotice.put("ReceivingAuthority", "," + staffInfo.getString("USER_ID") + ",");// 接收人
				pdNotice.put("Report_Key", "changeDrawingBOM");
				pdNotice.put("Report_Value", "");
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
				String WXNR = "【柜体图纸BOM完成】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
				Cabinet_Assembly_DetailService.updateMx(pd);// 更新bom状态
			}

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
	 * v1 管悦 20210820 设计冻结
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/oneKeyDoneDJ")
	@ResponseBody
	public Object oneKeyDoneDJ() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String FIs_NeedDJ_Audit = pd.getString("FIs_NeedDJ_Audit");
			// 判断 BOM 是否存在数据 不存在不让
			pd.put("FFTYPE", "BOM");
			List<PageData> listAll = Cabinet_BOMService.listAll(pd);
			if (CollectionUtils.isEmpty(listAll)) {
				map.put("result", "failed");
				map.put("msg", "BOM数据不存在或者信息不完整，不能修改状态");
				return map;
			}
			// 判断 图纸 是否上传 不存在不让
			PageData pData = new PageData();
			pData.put("AssociationID", pd.getString("Cabinet_Assembly_Detail_ID"));
			List<PageData> findByAssId = AttachmentSetService.findByAssId(pData);
			if (CollectionUtils.isEmpty(findByAssId)) {
				map.put("result", "failed");
				map.put("msg", "图纸数据不存在不能修改状态");
				return map;
			}

			if (FIs_NeedDJ_Audit.equals("是")) {// 需要销售审核
				PageData pdAudit = new PageData();
				pdAudit.put("CABINET_AUDIT_ID", this.get32UUID()); // 主键
				pdAudit.put("FAUDIT_STATE", "待审核");
				pdAudit.put("FTYPE", "设计冻结");
				pdAudit.put("CABINET_ASSEMBLY_DETAIL_ID", pd.getString("Cabinet_Assembly_Detail_ID"));
				pdAudit.put("FCREATOR", Jurisdiction.getName()); // 发布人
				pdAudit.put("FCTIME", DateUtil.date2Str(new Date())); // 发布时间
				cabinet_auditService.save(pdAudit);
				PageData old = Cabinet_Assembly_DetailService.findById(pd);
				old.put("FIs_NeedDJ_Audit", FIs_NeedDJ_Audit);
				old.put("FAudit_CabinetDJ_State", "待审核");
				Cabinet_Assembly_DetailService.edit(old);
			} else {
				PageData old = Cabinet_Assembly_DetailService.findById(pd);
				old.put("FAudit_CabinetDJ_State", "直接通过");
				old.put("FStatus", "设计冻结");
				old.put("FIs_NeedDJ_Audit", FIs_NeedDJ_Audit);
				old.put("FIs_NeedDJ_Time", Tools.date2Str(new Date()));
				Cabinet_Assembly_DetailService.edit(old);
			}

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
	 * 技术视角列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listJssj")
	@ResponseBody
	public Object listJssj(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			// 关键词检索条件
			String KEYWORDS = pd.getString("KEYWORDS");
			if (Tools.notEmpty(KEYWORDS)) {
				pd.put("KEYWORDS", KEYWORDS.trim());
			}
			if (StringUtil.isNotEmpty(pd.getString("BTStatus"))) {
				if ("N-N".equals(pd.getString("BTStatus"))) {
					pd.put("If_Bom_Done", "否");
					pd.put("If_Drawings_Done", "否");
				}
				if ("Y-N".equals(pd.getString("BTStatus"))) {
					pd.put("If_Bom_Done", "是");
					pd.put("If_Drawings_Done", "否");
				}
				if ("N-Y".equals(pd.getString("BTStatus"))) {
					pd.put("If_Bom_Done", "否");
					pd.put("If_Drawings_Done", "是");
				}
				if ("Y-Y".equals(pd.getString("BTStatus"))) {
					pd.put("If_Bom_Done", "是");
					pd.put("If_Drawings_Done", "是");
				}
			}

			pd.put("CREATORMAN", Jurisdiction.getName());
			page.setPd(pd);
			String staffName = Jurisdiction.getName();

			List<PageData> varList = Cabinet_Assembly_DetailService.listJssj(page); // 列出Cabinet_Assembly_Detail列表
			for (PageData pageData : varList) {
				if (staffName.equals(pageData.getString("Responsible_Technology_Name"))) {
					pageData.put("showOpt", "Y");
				} else {
					pageData.put("showOpt", "N");
				}
			}

			map.put("varList", varList);
			map.put("page", page);
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
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			// 根据ID读取
			pd = Cabinet_Assembly_DetailService.findById(pd);
			map.put("pd", pd);
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
	 * 下发技术 V1吴双双 20210531下发提醒
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "dispatch")
	@ResponseBody
	public Object dispatch() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData findById = Cabinet_Assembly_DetailService.findById(pd); // 根据ID获取数据
			if (StringUtils.isEmpty(findById.getString("Responsible_Technology"))) {
				map.put("result", "failed");
				map.put("msg", "存在没有分配负责技术的柜体数据，请编辑柜体详情");
				return map;
			}
			if (findById.getString("If_Sync_Done") == null || findById.getString("If_Sync_Done").equals("否")) {
				map.put("result", "failed");
				map.put("msg", "存在没有产成品代码同步的柜体数据，请编辑柜体详情");
				return map;
			}
			findById.put("FStatus", "下发");
			findById.put("Distribute_Time", Tools.date2Str(new Date()));
			Cabinet_Assembly_DetailService.edit(findById);

			// 负责技术发送设计提醒
			String PPROJECT_MANAGER = findById.getString("Responsible_Technology_Name"); // 根据key值，获取接收人
			if (StringUtils.isEmpty(PPROJECT_MANAGER)) { // 若PPROJECT_MANAGER为空，则找不到接收人
				map.put("result", "error");
				return map;
			}
			PageData staffParam = new PageData();
			staffParam.put("FNAME", PPROJECT_MANAGER);

			PageData staffInfo = StaffService.getStaffId(staffParam); // 根据接收人，获取英文名称
			if (null == staffInfo) { // 若staffInfo为空，则找不到接收人
				map.put("result", "error");
				return map;
			}
			PageData pdNotice = new PageData();
			// 跳转页面
			pdNotice.put("AccessURL", "../../../views/projectManager/pro_plan/pro_plan_jssj.html");//
			pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
			pdNotice.put("ReadPeople", ",");// 已读人默认空
			pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
			pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
			pdNotice.put("TType", "消息推送");// 消息类型
			pdNotice.put("FContent", Jurisdiction.getName() + "下发了柜体" + findById.getString("Cabinet_No") + "的设计，请注意查看");// 消息正文
			pdNotice.put("FTitle", "柜体设计下发");// 消息标题
			pdNotice.put("LinkIf", "no");// 是否跳转页面
			pdNotice.put("DataSources", "柜体设计下发");// 数据来源
			pdNotice.put("ReceivingAuthority", "," + staffInfo.getString("USER_ID") + ",");// 接收人
			pdNotice.put("Report_Key", "changeDrawingBOM");
			pdNotice.put("Report_Value", "");
			noticeService.save(pdNotice);

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
	 * 下发车间
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/dispatchWorkShop")
	@ResponseBody
	public Object dispatchWorkShop() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {

			String staffName = Jurisdiction.getName();
			PageData pdData = new PageData();
			pdData.put("FNAME", staffName);
			PageData staffInfo = StaffService.getStaffId(pdData);

			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			String PMSDETAILBATCH = codingRulesService.getRuleNumByRuleType("PMSDETAILBATCH").toString();
			if (Tools.notEmpty(DATA_IDS)) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				List<PageData> subPlanParamList = Lists.newArrayList();

				for (String Cabinet_Assembly_Detail_ID : ArrayDATA_IDS) {
					PageData pData = new PageData();
					pData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
					PageData findById = Cabinet_Assembly_DetailService.findById(pData);
					findById.put("Load_Status", "是");
					findById.put("Load_Dispach_Time", Tools.date2Str(new Date()));
					// 更新当前详情 下发装配状态为是
					String PPROJECT_CODE = findById.getString("PPROJECT_CODE");
					String BatchNum = PPROJECT_CODE + PMSDETAILBATCH;
					// 更新批号
					findById.put("BatchNum", BatchNum);
					Cabinet_Assembly_DetailService.edit(findById);
					subPlanParamList.add(findById);
				}

				// 创建计划
				PageData masterPlanParam = new PageData();

				masterPlanParam.put("WorkOrderType", "面向库存");
				masterPlanParam.put("WorkOrderNum", codingRulesService.getRuleNumByRuleType("JHBH"));
				masterPlanParam.put("FSpecifications", "面");
				masterPlanParam.put("FPriorityID", "1");
				masterPlanParam.put("PlannerType", "用户");

				// 获取当前登录用户
				masterPlanParam.put("FPlannerID", staffInfo.getString("STAFF_ID"));
				masterPlanParam.put("FPlanner", staffInfo.getString("FNAME"));

				List<PageData> beginOrderList = subPlanParamList;
				List<PageData> endOrderList = subPlanParamList;

				// 先按照开始时间从小到大排序，获取最小的开始时间的数据
				Collections.sort(beginOrderList, new Comparator<PageData>() {
					@Override
					public int compare(PageData o1, PageData o2) {
						Date o1Date = Tools.str2Date(o1.getString("Estimate_Start_Date"));
						Date o2Date = Tools.str2Date(o2.getString("Estimate_Start_Date"));
						return o1Date.compareTo(o2Date);
					}
				});

				// 计算当前计划的开始时间和结束时间
				masterPlanParam.put("PlannedBeginTime", beginOrderList.get(0).getString("Estimate_Start_Date"));

				// 先按照结束时间从大到小排序，获取最大的结束时间的数据
				Collections.sort(endOrderList, new Comparator<PageData>() {
					@Override
					public int compare(PageData o1, PageData o2) {
						Date o1Date = Tools.str2Date(o1.getString("Project_End_Date"));
						Date o2Date = Tools.str2Date(o2.getString("Project_End_Date"));
						return o1Date.compareTo(o2Date);
					}
				});

				masterPlanParam.put("PlannedEndTime", endOrderList.get(0).getString("Project_End_Date"));

				// 计算当前总数量
				masterPlanParam.put("FCount", subPlanParamList.size());

				masterPlanParam.put("FExplanation", "柜体生产");
				masterPlanParam.put("TogetherCreate", "否");
				masterPlanParam.put("StandardType", "标准类型");
				masterPlanParam.put("OccupyCount", "0");
				masterPlanParam.put("ReferenceType", "全部生产");
				masterPlanParam.put("ReferenceCount", "0");
				masterPlanParam.put("OutputMaterial", "装配柜体");
				masterPlanParam.put("OutputMaterialID", "ee9be023786249a3a2e37421bed698db");

				// 创建计划
				String PlanningWorkOrderMaster_ID = this.addMasterPlan(masterPlanParam);

				// 创建工单，自动绑定工艺路线，自动绑定物料需求 自动绑定投入产出
				for (PageData subPlanParam : subPlanParamList) {
					PageData subPlan = new PageData();

					subPlan.put("MasterWorkOrder_ID", PlanningWorkOrderMaster_ID);
					subPlan.put("FCount", 1.00);

					String Cabinet_No = subPlanParam.getString("Cabinet_No");

					List<PageData> listByMatCode = mat_basicService.getListByMatCode(Cabinet_No);
					if (CollectionUtil.isEmpty(listByMatCode)) {
						continue;
					}
					String MAT_BASIC_ID = listByMatCode.get(0).getString("MAT_BASIC_ID");
					String MAT_CODE = listByMatCode.get(0).getString("MAT_CODE");
					String MAT_NAME = listByMatCode.get(0).getString("MAT_NAME");
					String MAT_SPECS = listByMatCode.get(0).getString("MAT_SPECS");

					subPlan.put("OutputMaterial", MAT_CODE + "/" + MAT_NAME);
					subPlan.put("OutputMaterialID", MAT_BASIC_ID);
					subPlan.put("FSpecifications", MAT_SPECS);
					subPlan.put("ProductionSupervisorType", "用户");
					// 获取当前登陆人
					// subPlan.put("ProductionSupervisorID", staffInfo.getString("STAFF_ID"));
					// subPlan.put("ProductionSupervisorName", staffInfo.getString("FNAME"));
					// subPlan.put("FPlannerID", staffInfo.getString("STAFF_ID"));
					// subPlan.put("FPlanner", staffInfo.getString("FNAME"));
					subPlan.put("PlannerType", "用户");
					subPlan.put("FPriorityID", "1");

					// 批号
					subPlan.put("BatchNumType", "自动输入");
					subPlan.put("BatchNum", subPlanParam.getString("BatchNum"));

					// 车间计划开始
					subPlan.put("PlannedBeginTime", subPlanParam.getString("Estimate_Start_Date"));

					// 车间计划结束
					subPlan.put("PlannedEndTime", subPlanParam.getString("Project_End_Date"));

					// 工单计划开始
					subPlan.put("WorkOrderBeginTime", subPlanParam.getString("Estimate_Start_Date"));

					// 工单计划结束
					subPlan.put("WorkOrderEndTime", subPlanParam.getString("Project_End_Date"));

					// 创建 子计划工单ID
					PageData pageData = new PageData();
					pageData.put("PlanningWorkOrderMaster_ID", PlanningWorkOrderMaster_ID);
					PageData masterPlanningWordOrder = PlanningWorkOrderMasterService.findById(pageData);
					if (null != masterPlanningWordOrder) {

						subPlan.put("StandardType", masterPlanningWordOrder.getString("StandardType"));
						subPlan.put("FCustomer", masterPlanningWordOrder.getString("FCustomer"));
						subPlan.put("WorkOrderType", masterPlanningWordOrder.getString("WorkOrderType"));
						subPlan.put("SalesOrderDetailID", masterPlanningWordOrder.getString("SalesOrderDetailID"));
						subPlan.put("MasterWorkOrderNum", masterPlanningWordOrder.getString("WorkOrderNum"));
						// 获取父级计划工单下有多少子计划工单，然后给子工单编号
						// 获取父级计划工单信息 填充子工单信息
						PageData pdOps = new PageData();
						pdOps.put("MasterWorkOrder_ID", PlanningWorkOrderMaster_ID);
						List<PageData> listByMasterIdAndMasterWorkOrderNum = PlanningWorkOrderService
								.listByMasterIdAndMasterWorkOrderNum(pdOps);
						int i = 1;
						if (CollectionUtil.isNotEmpty(listByMasterIdAndMasterWorkOrderNum)) {
							i += listByMasterIdAndMasterWorkOrderNum.size();
						}

						subPlan.put("WorkOrderNum", Cabinet_No);

					}
					subPlan.put("PlanningWorkOrder_ID", this.get32UUID());
					subPlan.put("FStatus", "创建");
					subPlan.put("ScheduleSchedule", "未排程");
					subPlan.put("DistributionProgress", "未下发");
					subPlan.put("WorkOrderCreateTime", Tools.date2Str(new Date()));

					String Cabinet_Type = subPlanParam.getString("Cabinet_Type");
					PageData pData = new PageData();
					pData.put("Cabinet_Type", Cabinet_Type);
					List<PageData> routerListAll = ProcessRouteService.listAll(pData);

					if (!CollectionUtils.isEmpty(routerListAll)) {
						String WorkOrderNum = subPlan.getString("WorkOrderNum");
						// 关联工艺路线
						subPlan.put("ProcessRoute_ID", routerListAll.get(0).getString("ProcessRoute_ID"));
						subPlan.put("ProcessRoute_Name", routerListAll.get(0).getString("FName"));

						String ProcessRoute_ID = routerListAll.get(0).getString("ProcessRoute_ID");

						// 新增 流程图
						PageData bytearrayParam = new PageData();
						bytearrayParam.put("PID", ProcessRoute_ID);
						PageData findByPID = BYTEARRAYService.findByPID(bytearrayParam);
						findByPID.put("PID", subPlan.getString("PlanningWorkOrder_ID"));
						findByPID.put("FTYPE", "工单实例BOM");
						findByPID.put("RESOURCE_FILE_NAME", "流程图JSON");
						findByPID.put("GE_BYTEARRAY_FLOW_ID", this.get32UUID());
						BYTEARRAYService.save(findByPID);
						PageData workingProduceParam = new PageData();
						workingProduceParam.put("ProcessRouteID", ProcessRoute_ID);
						List<PageData> WPList = WorkingProcedureService.listAll(workingProduceParam);
						for (int i = 0; i < WPList.size(); i++) {
							String ProcessWorkOrderExample_ID = this.get32UUID();
							PageData workingProduce = WPList.get(i);
							String FTestMethod = workingProduce.getString("FTestMethod");
							if (FTestMethod != null && FTestMethod.equals("自动生成")) {// 生成质检任务和质检项
								PageData pdStaff = new PageData();
								PageData pdMain = new PageData();
								pdMain.put("FNAME", Jurisdiction.getName()); // 获取当前登录人的中文名称
								pdStaff = staffService.getStaffId(pdMain); // 根据人员的中文姓名获取人员ID
								pdMain.put("QualityInspectionPlanExecute_ID", this.get32UUID()); // 主键
								pdMain.put("ExecutionStatus", "待检");// 执行状态默认为创建
								pdMain.put("GenerationType", "临时创建");// 生产类型为临时创建
								pdMain.put("FMakeBillsPersoID", pdStaff.get("STAFF_ID"));// 制单人
								pdMain.put("FMakeBillsTime", Tools.date2Str(new Date())); // 制单时间
								pdMain.put("DataSources", "自动创建");
								pdMain.put("TaskNum", getRuleNumByRuleType("ZJRW"));
								pdMain.put("QIPlanID", workingProduce.getString("QIPlanID"));
								pdMain.put("WorkOrderIDRel", subPlan.getString("PlanningWorkOrder_ID"));
								pdMain.put("WP_ID", workingProduce.getString("FName"));
								pdMain.put("MaterialID", MAT_BASIC_ID);
								pdMain.put("FPriorityID", "1");
								pdMain.put("InspectorID", "0392470639e442e3897f0d1f65cd7e38");
								PageData pdS = new PageData();
								pdS.put("NAME", workingProduce.getString("FName"));
								PageData pdDic = dictionariesService.findByDICTIONARIESId(pdS);
								if (pdDic == null) {
									pdS.put("DICTIONARIES_ID", this.get32UUID());
									pdS.put("NAME", workingProduce.getString("FName"));
									pdS.put("NAME_EN", "TYPE" + (6 + i));
									pdS.put("BIANMA", "TYPE" + (6 + i));
									pdS.put("ORDER_BY", "2");
									pdS.put("PARENT_ID", "11341e231e004ff890146017a872d6c3");
									pdS.put("YNDEL", "no");
									dictionariesService.save(pdS);
								} else {
									pdS.put("DICTIONARIES_ID", pdDic.getString("DICTIONARIES_ID"));
								}
								pdMain.put("FType", pdS.getString("DICTIONARIES_ID"));
								pdMain.put("FOperator", "部门");
								pdMain.put("RequireEndTime", Tools.date2Str(new Date(), "yyyy-MM-dd"));
								pdMain.put("FCount", "1");
								pdMain.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);
								pdMain.put("FStation", workingProduce.getString("FStation"));
								QualityInspectionPlanExecuteService.save(pdMain);
								PageData pDataxx = new PageData();// 插入质检方案的质检项
								pDataxx.put("QIPlanID", workingProduce.getString("QIPlanID"));
								List<PageData> listAll = QualityInspectionPlanInspectionDetailsService.listAll(pDataxx);
								for (PageData pageDatax : listAll) {
									pageDatax.put("QualityInspectionPlanExecute_ID",
											pdMain.getString("QualityInspectionPlanExecute_ID"));
									pageDatax.put("QualityInspectionPlanDetailExecute_ID", this.get32UUID());
									QualityInspectionPlanDetailExecuteService.save(pageDatax);
								}
								operationrecordService.add("", "质检任务", "添加",
										pdMain.getString("QualityInspectionPlanExecute_ID"), "", "");// 操作日志

							}
							// 实例化任务
							PageData ProcessWorkOrderExampleData = new PageData();

							ProcessWorkOrderExampleData.put("ProcessIMateriel", MAT_NAME);
							ProcessWorkOrderExampleData.put("ProcessIMaterielCode", MAT_CODE);
							ProcessWorkOrderExampleData.put("ProcessIMaterielID", MAT_BASIC_ID);

							if (Tools.notEmpty(WorkOrderNum)) {
								// 订单编号
								ProcessWorkOrderExampleData.put("OrderNum", pd.getString("OrderNum"));
								// 任务编号 默认 工单编号~00 两位数字
								String TaskNum = WorkOrderNum + "~" + String.format("%02d", i + 1);
								ProcessWorkOrderExampleData.put("TaskNum", TaskNum);
								// ProcessWorkOrderExampleData.put("TaskNum", i);
							}

							// 计划工单ID
							ProcessWorkOrderExampleData.put("PlanningWorkOrderID",
									subPlan.getString("PlanningWorkOrder_ID"));
							// （从参数获取） 批号
							ProcessWorkOrderExampleData.put("BatchNum", subPlan.getString("BatchNum"));

							// （从参数获取） 规格
							ProcessWorkOrderExampleData.put("FMSpecification", subPlan.getString("FSpecifications"));
							// （从参数获取） 计划开始时间
							ProcessWorkOrderExampleData.put("PlannedBeginTime", subPlan.getString("PlannedBeginTime"));
							// （从参数获取）计划结束时间
							ProcessWorkOrderExampleData.put("PlannedEndTime", subPlan.getString("PlannedEndTime"));
							// 工序ID
							ProcessWorkOrderExampleData.put("WP", workingProduce.getString("WP"));
							String ProcessDefinition_ID = workingProduce.getString("WP");
							if (null != ProcessDefinition_ID) {
								PageData WPData = new PageData();
								WPData.put("ProcessDefinition_ID", ProcessDefinition_ID);
								PageData ProcessDefinition = ProcessDefinitionService.findById(WPData);
								if (null != ProcessDefinition) {
									// 工序名称
									ProcessWorkOrderExampleData.put("WPName", ProcessDefinition.getString("FName"));
									// 工序编码
									ProcessWorkOrderExampleData.put("WPNum", ProcessDefinition.getString("FNum"));
									// 工序类型
									ProcessWorkOrderExampleData.put("WPType", ProcessDefinition.getString("WPType"));
								}
							}

							// 序号
							ProcessWorkOrderExampleData.put("SerialNum", workingProduce.getString("SerialNum"));
							// 工位
							ProcessWorkOrderExampleData.put("FStation", workingProduce.getString("FStation"));

							// 续接方式
							ProcessWorkOrderExampleData.put("ConnectionMode",
									workingProduce.getString("ConnectionMode"));
							// 单次扫码
							ProcessWorkOrderExampleData.put("SingleScan", workingProduce.getString("SingleScan"));
							// 一码到底
							ProcessWorkOrderExampleData.put("OneCodeEnd", workingProduce.getString("OneCodeEnd"));
							// 用料追溯关系
							ProcessWorkOrderExampleData.put("MaterialTraceability",
									workingProduce.getString("MaterialTraceability"));
							// 不合格投产
							ProcessWorkOrderExampleData.put("UnqualifiedProducts",
									workingProduce.getString("UnqualifiedProducts"));
							// 准备时间
							ProcessWorkOrderExampleData.put("PreparationTime",
									workingProduce.getString("PreparationTime"));
							// 准备时间单位
							ProcessWorkOrderExampleData.put("PreparationTimeUnit",
									workingProduce.getString("PreparationTimeUnit"));
							// 工序产出比例
							ProcessWorkOrderExampleData.put("WPOutputRatio", workingProduce.getString("WPOutputRatio"));
							// 产出是否冻结
							ProcessWorkOrderExampleData.put("FrozenIF", workingProduce.getString("FrozenIF"));
							// 生产描述
							ProcessWorkOrderExampleData.put("ProductionDesc",
									workingProduce.getString("ProductionDesc"));
							// 附件
							ProcessWorkOrderExampleData.put("FAttachment", workingProduce.getString("FAttachment"));
							// 创建人
							ProcessWorkOrderExampleData.put("FCreatePersonID",
									workingProduce.getString("FCreatePersonID"));
							// 创建日期
							ProcessWorkOrderExampleData.put("FCreateTime", workingProduce.getString("FCreateTime"));
							// 质检计划id
							ProcessWorkOrderExampleData.put("QIPlanID", workingProduce.getString("QIPlanID"));

							// 是否完成核对
							ProcessWorkOrderExampleData.put("CheckDoneIF", "否");
							// 是否完成称量
							ProcessWorkOrderExampleData.put("WeighingDoneIF", "否");
							// 设备id
							ProcessWorkOrderExampleData.put("EQM_BASE_ID", "");
							// 生产能力值
							ProcessWorkOrderExampleData.put("Throughput", workingProduce.getString("Throughput"));
							// 是否支持拆锅
							ProcessWorkOrderExampleData.put("WokIF", workingProduce.getString("WokIF"));
							// 是否支持称量
							ProcessWorkOrderExampleData.put("WeighingIF", workingProduce.getString("WeighingIF"));
							// 是否支持自检
							ProcessWorkOrderExampleData.put("EnableTest", workingProduce.getString("EnableTest"));
							ProcessWorkOrderExampleData.put("FRUN_STATE", "创建");
							ProcessWorkOrderExampleData.put("FIFUCCOPY", "未占用");

							// 状态 默认 计划
							ProcessWorkOrderExampleData.put("FStatus", "计划");
							ProcessWorkOrderExampleData.put("TaskType", "1");

							ProcessWorkOrderExampleData.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);

							ProcessWorkOrderExampleData.put("FCreatePersonID", staffInfo.getString("STAFF_ID")); // 创建人ID
							ProcessWorkOrderExampleData.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间

							ProcessWorkOrderExampleData.put("WorkHours", workingProduce.getString("WorkHours"));
							ProcessWorkOrderExampleData.put("WorkHoursUnit", workingProduce.getString("WorkHoursUnit"));
							if (null != ProcessWorkOrderExampleData.get("EnableTest")
									&& "是".equals(ProcessWorkOrderExampleData.get("EnableTest"))) {
								// 从中获取数据复制出来一份自检项定义 作为实例化数据
								ProcessWorkOrderExampleData.put("WORKINGPROCEDURE_ID",
										workingProduce.getString("WorkingProcedure_ID"));

								List<PageData> varListTest = workingprocedureselfcheckService
										.listAll(ProcessWorkOrderExampleData);
								for (PageData test : varListTest) {
									test.put("WORKINGPROCEDURESELFCHECKEXAMPLE_ID", this.get32UUID());
									test.put("WORKINGPROCEDUREEXAMPLE_ID", ProcessWorkOrderExample_ID);
									test.put("FISACCORD", "否");
									workingprocedureselfcheckexampleService.save(test);
								}
							}

							String EnableSOP = workingProduce.getString("EnableSOP");
							String SOP_ID = workingProduce.getString("SOP_ID");

							// 加入字段 是否启用SOP和SOP ID
							ProcessWorkOrderExampleData.put("EnableSOP", EnableSOP);
							ProcessWorkOrderExampleData.put("SOP_ID", SOP_ID);

							PlanningWorkOrderService.insertProcessWorkOrderExample(ProcessWorkOrderExampleData);

							if (Tools.notEmpty(SOP_ID)) {
								// 从SOP 步骤库中获取数据 复制出来一份 作为实例化数据
								PageData soPageData = new PageData();
								soPageData.put("SopSchemeTemplate_ID", SOP_ID);
								List<PageData> SopList = SopStepService.listAll(soPageData);
								for (PageData sop : SopList) {
									sop.put("PlanningWorkOrderID", subPlan.getString("PlanningWorkOrder_ID"));
									sop.put("ProcessWorkOrderExample_SopStep_ID", this.get32UUID());
									sop.put("ProcessWorkOrderExample_ID",
											ProcessWorkOrderExampleData.getString("ProcessWorkOrderExample_ID"));
									if (null != sop.getString("FAnnex_Name")
											&& !"".equals(sop.getString("FAnnex_Name"))) {
										sop.put("FAnnex_Name", sop.getString("FAnnex_Name"));
										sop.put("FANNEX_Path", sop.getString("FANNEX_Path"));
										// 附件集插入数据
										PageData pdFile = new PageData();
										pdFile.put("DataSources", "SOP实例");
										pdFile.put("AssociationIDTable", "PP_ProcessWorkOrderExample_SopStep");
										pdFile.put("AssociationID",
												sop.getString("ProcessWorkOrderExample_SopStep_ID"));
										pdFile.put("FName", sop.getString("FAnnex_Name"));
										pdFile.put("FUrl", sop.getString("FANNEX_Path"));
										pdFile.put("FExplanation", "");
										pdFile.put("FCreatePersonID", Jurisdiction.getUsername());
										pdFile.put("FCreateTime", Tools.date2Str(new Date()));
										attachmentsetService.check(pdFile);
									} else {
										sop.put("FAnnex_Name", "");
										sop.put("FANNEX_Path", "");
									}
									pwoeSopStepService.save(sop);
								}

							}

						}
					}
					subPlan.put("FPlannerID", staffInfo.getString("STAFF_ID"));
					subPlan.put("FPlanner", staffInfo.getString("FNAME"));
					/*
					 * subPlan.put("FPlannerID",""); subPlan.put("FPlanner", "");
					 */
					PlanningWorkOrderService.save(subPlan);

				}
				errInfo = "success";
			} else {
				errInfo = "error";
			}
			map.put("result", errInfo); // 返回结果
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * 同步物料数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/syncMat")
	@ResponseBody
	public Object syncMat() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");

			if (Tools.notEmpty(DATA_IDS)) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				for (String Cabinet_Assembly_Detail_ID : ArrayDATA_IDS) {
					PageData pData = new PageData();
					pData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
					PageData findById = Cabinet_Assembly_DetailService.findById(pData);

					// 别名必须填写
					if (StringUtils.isEmpty(findById.getString("Cabinet_Aliases"))) {
						map.put("result", "failed");
						map.put("msg", "柜体别名不能为空");
						return map;
					}

					findById.put("If_Sync_Done", "是");
					// 新插入物料信息表 设置默认值

					// 判断当前柜体编号是否存在？存在略过:不存在继续插入
					List<PageData> listByMatCode = mat_basicService.getListByMatCode(findById.getString("Cabinet_No"));
					if (!CollectionUtils.isEmpty(listByMatCode)) {
						continue;
					}

					PageData matInsertParam = new PageData();
					matInsertParam.put("MAT_CLASS_ID", "afafa0d2cf3e4f69ad197101552c3837");
					matInsertParam.put("MAT_CODE", findById.getString("Cabinet_No"));
					matInsertParam.put("MAT_NAME", findById.getString("Cabinet_Aliases"));
					matInsertParam.put("MAT_ATTRIBUTE", "e8a2897878cd49dcbaba908ca55e84f5");
					matInsertParam.put("MAT_MAIN_UNIT", "234eaab3f5824032bccfc78dea24e0e6");
					matInsertParam.put("MAT_AUXILIARY_UNIT", "234eaab3f5824032bccfc78dea24e0e6");
					matInsertParam.put("MAT_SPECS", "side");
					matInsertParam.put("MAT_STATE", "启用中");
					matInsertParam.put("ENABLEFBATCH", "不启用");
					matInsertParam.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
					matInsertParam.put("UNIQUE_CODE_WHETHER", "否");
					matInsertParam.put("MAT_SPECS_QTY", "1");
					matInsertParam.put("SAFE_STOCK", "不启用");
					matInsertParam.put("SAFE_STOCK_QTY", "0");
					matInsertParam.put("EARLY_WARN", "不启用");

					// 插入物料基础信息
					this.add2MatBasic(matInsertParam);

					// 更新当前详情 同步状态为是
					Cabinet_Assembly_DetailService.edit(findById);
				}
				errInfo = "success";
			} else {
				errInfo = "error";
			}
			map.put("result", errInfo); // 返回结果
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

	}

	private String addMasterPlan(PageData pd) throws Exception {

		pd.put("FStatus", "创建");
		pd.put("PlanningWorkOrderMaster_ID", this.get32UUID());
		pd.put("WorkOrderCreateTime", Tools.date2Str(new Date()));

		String WorkOrderNumParam = pd.getString("WorkOrderNum");
		PageData pDatas = new PageData();
		pDatas.put("WorkOrderNum", WorkOrderNumParam);
		List<PageData> workOrderNumList = PlanningWorkOrderMasterService.listAll(pDatas);
		if (CollectionUtil.isNotEmpty(workOrderNumList)) {
			throw new RuntimeException("已经存在该工单号的计划工单了");
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

		return pd.getString("PlanningWorkOrderMaster_ID");
	}

	private void add2MatBasic(PageData pd) throws Exception {
		Date date = new Date();// 时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 获取当前时间
		PageData pdLogistic = new PageData();// 物流资料
		PageData pdDesign = new PageData();// 设计资料
		PageData pdQuality = new PageData();// 质量资料
		PageData pdSpec = new PageData();// 物料规格
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid = staffService.getStaffId(staff).getString("STAFF_ID");
		pd.put("MAT_BASIC_ID", this.get32UUID()); // 主键
		pd.put("FCREATOR", Jurisdiction.getName());// 创建人
		pd.put("CREATE_TIME", dateFormat.format(date));// 创建时间
		mat_basicService.save(pd);
		pdLogistic.put("MAT_LOGISTIC_ID", this.get32UUID());// 物流资料ID
		pdLogistic.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));// 基础资料id
		pdLogistic.put("FCREATOR", Jurisdiction.getName());// 创建人
		pdLogistic.put("CREATE_TIME", dateFormat.format(date));// 创建时间
		mat_logisticService.save(pdLogistic);// 物流资料保存
		pdDesign.put("MAT_DESIGN_ID", this.get32UUID());// 设计资料ID
		pdDesign.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));// 基础资料id
		pdDesign.put("FVERSIONS", "1");// 设计资料版本
		pdDesign.put("FCREATOR", Jurisdiction.getName());// 创建人
		pdDesign.put("CREATE_TIME", dateFormat.format(date));// 创建时间
		mat_designService.save(pdDesign);// 设计资料保存
		pdQuality.put("MAT_QUALITY_ID", this.get32UUID());// 质量资料ID
		pdQuality.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));// 基础资料id
		pdQuality.put("FCREATOR", Jurisdiction.getName());// 创建人
		pdQuality.put("CREATE_TIME", dateFormat.format(date));// 创建时间
		mat_qualityService.save(pdQuality);// 质量资料保存
		pdSpec.put("MAT_SPEC_ID", this.get32UUID());// 物料规格ID
		pdSpec.put("MAT_BASIC_ID", pd.getString("MAT_BASIC_ID"));// 基础资料id
		pdSpec.put("UNIT_INFO_ID", pd.getString("MAT_AUXILIARY_UNIT"));// 物料定义的辅助单位id（规格单位id）
		pdSpec.put("MAT_SPEC_QTY", new BigDecimal(pd.get("MAT_SPECS_QTY").toString()));// 规格数量
		pdSpec.put("FCreatePersonID", staffid);
		pdSpec.put("FCreateTime", dateFormat.format(date));
		pdSpec.put("LastModifiedBy", staffid);
		pdSpec.put("LastModificationTime", dateFormat.format(date));
		MAT_SPECService.save(pdSpec);

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
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			if (Tools.notEmpty(DATA_IDS)) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				Cabinet_Assembly_DetailService.deleteAll(ArrayDATA_IDS);
				errInfo = "success";
			} else {
				errInfo = "error";
			}
			map.put("result", errInfo); // 返回结果
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

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
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("所属装配主表id"); // 1
			titles.add("柜体号"); // 2
			titles.add("柜体别名"); // 3
			titles.add("负责技术"); // 4
			titles.add("是否完成bom"); // 5
			titles.add("是否完成图纸"); // 6
			titles.add("状态"); // 7
			dataMap.put("titles", titles);
			List<PageData> varOList = Cabinet_Assembly_DetailService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for (int i = 0; i < varOList.size(); i++) {
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("Cabinet_Assembly_ID")); // 1
				vpd.put("var2", varOList.get(i).getString("Cabinet_No")); // 2
				vpd.put("var3", varOList.get(i).getString("Cabinet_Aliases")); // 3
				vpd.put("var4", varOList.get(i).getString("Responsible_Technology")); // 4
				vpd.put("var5", varOList.get(i).getString("If_Bom_Done")); // 5
				vpd.put("var6", varOList.get(i).getString("If_Drawings_Done")); // 6
				vpd.put("var7", varOList.get(i).getString("FStatus")); // 7
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv, dataMap);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return mv;
		}

	}

	/**
	 * 下推采购数据列表 v2 管悦 2021-07-13 采购数量不判断库存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/doPurchaseDataList")
	@ResponseBody
	public Object doPurchaseDataList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			List<PageData> varList = Lists.newArrayList();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			if (Tools.notEmpty(DATA_IDS)) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				varList = Cabinet_BOMService.listByCabinetAssemblyDetailIDS(ArrayDATA_IDS);

				PageData pData = new PageData();
				pData.put("Cabinet_Assembly_Detail_ID", ArrayDATA_IDS[0]);
				PageData findById = Cabinet_Assembly_DetailService.findById(pData);
				String PPROJECT_CODE = findById.getString("PPROJECT_CODE");

				for (PageData pageData : varList) {

					String MaterialID = pageData.getString("MAT_BASIC_ID");
					String FClass = pageData.getString("MAT_CLASS");
					String wareHouseType = "工厂库";
					PageData pg = new PageData();
					pg.put("ItemId", MaterialID);
					pg.put("FType", wareHouseType);
					pg.put("FClass", FClass);
					// 工厂库 公用 辅助属性
					pg.put("MaterialSProp", "80108fa272884119b09eab03a78d3cc5");
					pg.put("MaterialSPropKey", "4486905b9e47428985c36c9d55d21ad7");
					List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);
					BigDecimal BOM_COUNT = new BigDecimal(pageData.get("BOM_COUNT").toString());
					/*
					 * if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
					 * pageData.put("STOCK_ACT_COUNT", 0.00); pageData.put("PURCHASE_COUNT",
					 * BOM_COUNT); pageData.put("PPROJECT_CODE", PPROJECT_CODE); } else { BigDecimal
					 * ActualCountBig = new BigDecimal("0"); for (PageData
					 * actualCountByFTYPEAndItemIDData : actualCountByFTYPEAndItemID) {
					 * ActualCountBig = ActualCountBig .add(new
					 * BigDecimal(actualCountByFTYPEAndItemIDData.get("ActualCount").toString())); }
					 * pageData.put("STOCK_ACT_COUNT", ActualCountBig.toString());
					 * pageData.put("PURCHASE_COUNT",
					 * BOM_COUNT.subtract(ActualCountBig).doubleValue() <= 0 ? 0 :
					 * BOM_COUNT.subtract(ActualCountBig).doubleValue());
					 * pageData.put("PPROJECT_CODE", PPROJECT_CODE); }
					 */
					if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
						pageData.put("STOCK_ACT_COUNT", 0.00);
						pageData.put("PURCHASE_COUNT", BOM_COUNT);
						pageData.put("PPROJECT_CODE", PPROJECT_CODE);
					} else {
						BigDecimal ActualCountBig = new BigDecimal("0");
						for (PageData actualCountByFTYPEAndItemIDData : actualCountByFTYPEAndItemID) {
							ActualCountBig = ActualCountBig
									.add(new BigDecimal(actualCountByFTYPEAndItemIDData.get("ActualCount").toString()));
						}
						pageData.put("STOCK_ACT_COUNT", ActualCountBig.toString());
						pageData.put("PURCHASE_COUNT", BOM_COUNT);
						pageData.put("PPROJECT_CODE", PPROJECT_CODE);
					}

				}

				errInfo = "success";
			} else {
				errInfo = "error";
			}
			map.put("varList", varList);

			map.put("result", errInfo); // 返回结果
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * 下推采购数据列表 扣减可用
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/kjky")
	@ResponseBody
	public Object kjky() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			List<PageData> varList = Lists.newArrayList();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			if (Tools.notEmpty(DATA_IDS)) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				varList = Cabinet_BOMService.listByCabinetAssemblyDetailIDS(ArrayDATA_IDS);

				PageData pData = new PageData();
				pData.put("Cabinet_Assembly_Detail_ID", ArrayDATA_IDS[0]);
				PageData findById = Cabinet_Assembly_DetailService.findById(pData);
				String PPROJECT_CODE = findById.getString("PPROJECT_CODE");

				for (PageData pageData : varList) {

					String MaterialID = pageData.getString("MAT_BASIC_ID");
					String FClass = pageData.getString("MAT_CLASS");
					String wareHouseType = "工厂库";
					PageData pg = new PageData();
					pg.put("ItemId", MaterialID);
					pg.put("FType", wareHouseType);
					pg.put("FClass", FClass);
					// 工厂库 公用 辅助属性
					pg.put("MaterialSProp", "80108fa272884119b09eab03a78d3cc5");
					pg.put("MaterialSPropKey", "4486905b9e47428985c36c9d55d21ad7");
					List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);
					BigDecimal BOM_COUNT = new BigDecimal(pageData.get("BOM_COUNT").toString());
					if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
						pageData.put("STOCK_ACT_COUNT", 0.00);
						pageData.put("PURCHASE_COUNT", BOM_COUNT);
						pageData.put("PPROJECT_CODE", PPROJECT_CODE);
					} else {
						BigDecimal ActualCountBig = new BigDecimal("0");
						for (PageData actualCountByFTYPEAndItemIDData : actualCountByFTYPEAndItemID) {
							ActualCountBig = ActualCountBig
									.add(new BigDecimal(actualCountByFTYPEAndItemIDData.get("ActualCount").toString()));
						}
						pageData.put("STOCK_ACT_COUNT", ActualCountBig.toString());
						pageData.put("PURCHASE_COUNT", BOM_COUNT.subtract(ActualCountBig).doubleValue() <= 0 ? 0
								: BOM_COUNT.subtract(ActualCountBig).doubleValue());
						pageData.put("PPROJECT_CODE", PPROJECT_CODE);
					}
				}

				errInfo = "success";
			} else {
				errInfo = "error";
			}
			map.put("varList", varList);

			map.put("result", errInfo); // 返回结果
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * v1 管悦 2021-07-08 按项目查看物料汇总
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/kjky_XM")
	@ResponseBody
	public Object kjky_XM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			List<PageData> varList = Lists.newArrayList();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			if (Tools.notEmpty(DATA_IDS)) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				varList = Cabinet_BOMService.listByCabinetAssemblyDetailIDSXM(ArrayDATA_IDS);

				PageData pData = new PageData();
				pData.put("Cabinet_Assembly_Detail_ID", ArrayDATA_IDS[0]);
				PageData findById = Cabinet_Assembly_DetailService.findById(pData);
				String PPROJECT_CODE = findById.getString("PPROJECT_CODE");

				for (PageData pageData : varList) {

					String MaterialID = pageData.getString("MAT_BASIC_ID");
					String FClass = pageData.getString("MAT_CLASS");
					String wareHouseType = "工厂库";
					PageData pg = new PageData();
					pg.put("ItemId", MaterialID);
					pg.put("FType", wareHouseType);
					pg.put("FClass", FClass);
					// 工厂库 公用 辅助属性
					pg.put("MaterialSProp", "80108fa272884119b09eab03a78d3cc5");
					pg.put("MaterialSPropKey", "4486905b9e47428985c36c9d55d21ad7");
					List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);
					BigDecimal BOM_COUNT = new BigDecimal(pageData.get("BOM_COUNT").toString());
					if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
						pageData.put("STOCK_ACT_COUNT", 0.00);
						pageData.put("PURCHASE_COUNT", BOM_COUNT);
						pageData.put("PPROJECT_CODE", PPROJECT_CODE);
					} else {
						BigDecimal ActualCountBig = new BigDecimal("0");
						for (PageData actualCountByFTYPEAndItemIDData : actualCountByFTYPEAndItemID) {
							ActualCountBig = ActualCountBig
									.add(new BigDecimal(actualCountByFTYPEAndItemIDData.get("ActualCount").toString()));
						}
						pageData.put("STOCK_ACT_COUNT", ActualCountBig.toString());
						pageData.put("PURCHASE_COUNT", BOM_COUNT.subtract(ActualCountBig).doubleValue() <= 0 ? 0
								: BOM_COUNT.subtract(ActualCountBig).doubleValue());
						pageData.put("PPROJECT_CODE", PPROJECT_CODE);
					}
				}

				errInfo = "success";
			} else {
				errInfo = "error";
			}
			map.put("varList", varList);

			map.put("result", errInfo); // 返回结果
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}

	}

	/**
	 * v1 管悦 下推采购自动生成采购单
	 * 
	 * @param FFILEPATH:文件、清单信息
	 * @throws Exception
	 */
	@RequestMapping(value = "/PurchaseListAdd")
	@ResponseBody
	public Object PurchaseListAdd(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FNum", codingRulesService.getRuleNumByRuleType("CGQD"));
		try {
			String DATA_IDS = pd.getString("DATA_IDS");
			List<PageData> varList = Lists.newArrayList();
			String[] ArrayDATA_IDS = DATA_IDS.split(",");

			varList = Cabinet_BOMService.listByCabinetAssemblyDetailIDS(ArrayDATA_IDS);
			if (varList.size() == 0) {
				errInfo = "fail3";
			} else {
				// 单号验重
				PageData pdNum = PurchaseListService.getRepeatNum(pd);
				if (pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) > 0) {
					errInfo = "fail1";// 单号重复
				} else {
					pd.put("FNAME", Jurisdiction.getName());
					String staffID = staffService.getStaffId(pd).getString("STAFF_ID");
					String purchaseList_id = this.get32UUID();
					pd.put("PurchaseList_ID", purchaseList_id); // 主键
					pd.put("FMakeBillsPersoID", staffID);// 查询职员ID
					pd.put("FOperatorID", staffID);
					pd.put("FMakeBillsTime", Tools.date2Str(new Date()));
					pd.put("FStatus", "创建");
					pd.put("FCheckFlag", "N");
					pd.put("FCustomer", "");
					pd.put("ExpenseType", "");
					PurchaseListService.save(pd);
					// 添加审批意见
					PageData cpd = new PageData();
					cpd.put("PurchaseList_Comments_ID", this.get32UUID()); // 主键
					cpd.put("BillID", purchaseList_id); // 操作人
					cpd.put("FStatus", "创建 "); // 操作人
					cpd.put("FComments", "无"); // 操作人
					cpd.put("FOperator", staffID); // 操作人
					cpd.put("OperationTime", Tools.date2Str(new Date())); // 操作时间
					PurchaseList_CommentsService.save(cpd);
					// 上传附件
					DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					Calendar calendar = Calendar.getInstance();
					String dateName = df.format(calendar.getTime());
					String ffile = DateUtil.getDays();
					String FFILENAME = "";
					String FPFFILEPATH = "";
					if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
						String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
						String fileNamereal = pd.getString("FFILENAME").substring(0,
								pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
						FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal + dateName);// 执行上传
						FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
						// 附件集插入数据
						PageData pdFile = new PageData();
						pdFile.put("DataSources", "采购订单");
						pdFile.put("AssociationIDTable", "PP_PurchaseList");
						pdFile.put("AssociationID", pd.getString("PurchaseList_ID"));
						pdFile.put("FName", FFILENAME);
						pdFile.put("FUrl", FPFFILEPATH);
						pdFile.put("FExplanation", "");
						pdFile.put("FCreatePersonID", pd.getString("FMakeBillsPersoID"));
						pdFile.put("FCreateTime", Tools.date2Str(new Date()));
						attachmentsetService.check(pdFile);
					}
					map.put("PurchaseList_ID", purchaseList_id);

				}
				// 消息提醒项目经理、采购
				List<PageData> manVarList = Cabinet_Assembly_DetailService.listXMJLByIDS(ArrayDATA_IDS);
				pd.put("ROLE_NAME", "采购管理");
				List<PageData> staffList = usersService.listAllByRoleName(pd);
				String ReceivingAuthority = ",";
				for (PageData pdStaffx : manVarList) {
					ReceivingAuthority += pdStaffx.getString("PPROJECT_MANAGER");
					ReceivingAuthority = ReceivingAuthority + ",";
				}
				for (PageData pdStaffx : staffList) {
					ReceivingAuthority += pdStaffx.getString("USERNAME");
					ReceivingAuthority = ReceivingAuthority + ",";
				}
				if (manVarList.size() > 0) {
					// 消息推送
					PageData pdNotice = new PageData();

					// 跳转页面
					pdNotice.put("AccessURL", "../../../views/pp/PurchaseList/PurchaseList_list.html");//
					pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
					pdNotice.put("ReadPeople", ",");// 已读人默认空
					pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
					pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
					pdNotice.put("TType", "消息推送");// 消息类型
					pdNotice.put("FContent",
							"项目编号为" + manVarList.get(0).getString("PPROJECT_CODE") + "，项目名称为"
									+ manVarList.get(0).getString("PPROJECT_NAME") + "，采购编号为" + pd.getString("FNum")
									+ "的采购单已下发");// 消息正文
					pdNotice.put("FTitle", "采购任务提醒");// 消息标题
					pdNotice.put("LinkIf", "no");// 是否跳转页面
					pdNotice.put("DataSources", "采购任务提醒");// 数据来源
					pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
					pdNotice.put("Report_Key", "task");// 手机app
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
					String WXNR = "【采购任务提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
							+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
					// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
					// "0");
					weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
				}

				// 插入操作日志
				PageData pdOp = new PageData();
				pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
				pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
				pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));// 操作人
				pdOp.put("FunctionType", "");// 功能类型
				pdOp.put("FunctionItem", "采购订单");// 功能项
				pdOp.put("OperationType", "新增");// 操作类型
				pdOp.put("Fdescribe", "");// 描述
				pdOp.put("DeleteTagID", pd.get("PurchaseList_ID"));// 删改数据ID
				operationrecordService.save(pdOp);

				PageData pData = new PageData();
				pData.put("Cabinet_Assembly_Detail_ID", ArrayDATA_IDS[0]);
				PageData findById = Cabinet_Assembly_DetailService.findById(pData);
				String PPROJECT_CODE = findById.getString("PPROJECT_CODE");
//
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

				for (PageData pageData : varList) {

					String MaterialID = pageData.getString("MAT_BASIC_ID");
					String FClass = pageData.getString("MAT_CLASS");
					String wareHouseType = "工厂库";
					PageData pg = new PageData();
					pg.put("ItemId", MaterialID);
					pg.put("FType", wareHouseType);
					pg.put("FClass", FClass);
					// 工厂库 公用 辅助属性
					pg.put("MaterialSProp", "80108fa272884119b09eab03a78d3cc5");
					pg.put("MaterialSPropKey", "4486905b9e47428985c36c9d55d21ad7");
					List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);
					BigDecimal BOM_COUNT = new BigDecimal(pageData.get("BOM_COUNT").toString());
					if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
						pageData.put("STOCK_ACT_COUNT", 0.00);
						pageData.put("PURCHASE_COUNT", BOM_COUNT);
						pageData.put("PPROJECT_CODE", PPROJECT_CODE);
					} else {
						BigDecimal ActualCountBig = new BigDecimal("0");
						for (PageData actualCountByFTYPEAndItemIDData : actualCountByFTYPEAndItemID) {
							ActualCountBig = ActualCountBig
									.add(new BigDecimal(actualCountByFTYPEAndItemIDData.get("ActualCount").toString()));
						}
						pageData.put("STOCK_ACT_COUNT", ActualCountBig.toString());
						pageData.put("PURCHASE_COUNT", BOM_COUNT.subtract(ActualCountBig).doubleValue() <= 0 ? 0
								: BOM_COUNT.subtract(ActualCountBig).doubleValue());
						pageData.put("PPROJECT_CODE", PPROJECT_CODE);
					}
//
					String PURCHASE_COUNT_STR = pageData.get("PURCHASE_COUNT").toString();
					String BOM_COUNT_STR = pageData.get("BOM_COUNT").toString();
//
					BigDecimal PURCHASE_COUNT_BIG = new BigDecimal(PURCHASE_COUNT_STR);
					BigDecimal BOM_COUNT_BIG = new BigDecimal(BOM_COUNT_STR);
//
//				// 插入 辅助属性为 项目的 该 物料的库存 为 OCCUPY_BIG
//				// 增加库存
//
					String WareHouseId = "";
					String LocationId = "";
					List<PageData> wareHouseList = wh_warehouseService.wareHouseList(pg);
					if (CollectionUtil.isNotEmpty(wareHouseList)) {
						WareHouseId = wareHouseList.get(0).getString("WH_WAREHOUSE_ID");
					}
					PageData pData2 = new PageData();
					pData2.put("WH_STORAGEAREA_ID", WareHouseId);
					List<PageData> locationList = wh_locationService.locationList(pData2);
					if (CollectionUtil.isNotEmpty(locationList)) {
						LocationId = locationList.get(0).getString("WH_LOCATION_ID");
					}
//
					PageData inParam = new PageData();
					List<PageData> listByMatCode = mat_basicService.getListByMatCode(pageData.getString("MAT_CODE"));
					if (CollectionUtil.isNotEmpty(listByMatCode)) {
						inParam.put("FUnit", listByMatCode.get(0).getString("MAT_MAIN_UNIT"));// 单位
						inParam.put("FBatch", listByMatCode.get(0).getString("FBATCH"));// 批号
						inParam.put("ProductionBatch", listByMatCode.get(0).getString("FBATCH"));// 生产批号
					}
					inParam.put("Stock_ID", this.get32UUID());
					inParam.put("WarehouseID", WareHouseId);// 仓库id
					inParam.put("PositionID", LocationId);// 库位id
					inParam.put("ItemID", pageData.getString("MAT_BASIC_ID"));// 物料id
					inParam.put("StorageStatus", "工厂");// 存储状态(工厂,车间)
					inParam.put("QRCode", pageData.getString("MAT_CODE"));// 物料二维码
					inParam.put("OneThingCode", "");// 一物码
					inParam.put("MaterialSProp", "54e6b1a185764c32bff189907a571a6d");// 物料辅助属性
					inParam.put("MaterialSPropKey", MAT_AUXILIARYMX_ID);// 物料辅助属性值
					inParam.put("SpecificationDesc", pageData.getString("MAT_SPECS"));// 规格
					inParam.put("ActualCount", BOM_COUNT_BIG.subtract(PURCHASE_COUNT_BIG));// 实际数量
//
					inParam.put("UseCount", 0);// 使用数量
					inParam.put("UseIf", "NO");// 是否占用
					inParam.put("FLevel", 1);// 等级
//
					inParam.put("RunningState", "在用");// 运行状态
					inParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
					inParam.put("UsageTime", Tools.date2Str(new Date()));// 使用时间
					inParam.put("DateOfManufacture", Tools.date2Str(new Date()));// 生产日期
//
					inParam.put("LastModifiedTime", Tools.date2Str(new Date()));// 最后修改日期
					inParam.put("LastCountTime", Tools.date2Str(new Date()));// 上次盘点日期
					inParam.put("FStatus", "入库");// 状态
//
					StockService.inStock(inParam);
//	
//				// 占用数量
					BigDecimal OCCUPY_BIG = BOM_COUNT_BIG.subtract(PURCHASE_COUNT_BIG);
					List<PageData> stockListByFTYPEAndItemID = StockService.getStockListByFTYPEAndItemID(pg);
					for (int i = 0; i < stockListByFTYPEAndItemID.size(); i++) {
						PageData sData = stockListByFTYPEAndItemID.get(i);
						// 如果占用数量 为 0 则
						if (OCCUPY_BIG.doubleValue() == 0) {
							break;
						}
						BigDecimal remaind = new BigDecimal(sData.get("ActualCount").toString()).subtract(OCCUPY_BIG);

						if (remaind.doubleValue() > 0) {
							// 如果够减 则 更新当条的数量 为 剩余数量 ,OCCUPY_BIG 数量为 0
							OCCUPY_BIG = new BigDecimal("0");
							sData.put("ActualCount", remaind);
							StockService.edit(sData);

						} else {
							// 如果不够减 则 更新当前数量为 0 ,更新 OCCUPY_BIG 数量为 剩余
							OCCUPY_BIG = OCCUPY_BIG.subtract(new BigDecimal(sData.get("ActualCount").toString()));
							sData.put("ActualCount", 0);
							StockService.edit(sData);
						}
					}
				}

				Map<String, PageData> result1 = new HashMap<String, PageData>();
				for (PageData pageData : varList) {
					String MaterialID = pageData.get("MAT_BASIC_ID").toString();
					BigDecimal value = new BigDecimal((pageData.get("BOM_COUNT").toString()));
					if (result1.containsKey(MaterialID)) {
						BigDecimal bigDecimal = new BigDecimal(
								result1.get(MaterialID).get("PURCHASE_COUNT").toString());
						value = value.add(bigDecimal);
					}
					pageData.put("PURCHASE_COUNT", value);
					result1.put(MaterialID, pageData);
				}
				// System.out.println("合并后的数据："+JSON.toJSONString(result1));

				List<PageData> varListPurchase = Lists.newArrayList();
				Collection<PageData> values = result1.values();
				for (PageData pageData : values) {
					varListPurchase.add(pageData);
				}

				for (int i = 0; i < varListPurchase.size(); i++) {

					if (new BigDecimal(varListPurchase.get(i).get("PURCHASE_COUNT").toString()).doubleValue() == 0) {
						continue;
					}
					// 插入详情
					PageData purchaseMaterialDetail = new PageData();
					purchaseMaterialDetail.put("PurchaseMaterialDetails_ID", this.get32UUID());
					purchaseMaterialDetail.put("RowClose", "N");
					purchaseMaterialDetail.put("RowNum", i + 1);
					purchaseMaterialDetail.put("SourceOrderType", pd.getString("SourceOrderType"));
					purchaseMaterialDetail.put("PurchaseID", pd.getString("PurchaseList_ID"));
					purchaseMaterialDetail.put("MaterialID", varListPurchase.get(i).getString("MAT_BASIC_ID"));

					// 辅助属性为 项目的
					purchaseMaterialDetail.put("SProp", "54e6b1a185764c32bff189907a571a6d");
					purchaseMaterialDetail.put("SPropKey", MAT_AUXILIARYMX_ID);

					purchaseMaterialDetail.put("SpecificationsDesc", varListPurchase.get(i).getString("MAT_SPECS"));
					purchaseMaterialDetail.put("FCount", varListPurchase.get(i).get("PURCHASE_COUNT").toString());

					purchaseMaterialDetail.put("EstimatedTimeOfArrival", pd.getString("EstimatedTimeOfArrival"));

					purchaseMaterialDetail.put("Admission", "0.00");
					purchaseMaterialDetail.put("ReturnedMaterialsCount", "0.00");
					purchaseMaterialDetail.put("EnableEarlyWarningIF", "N");
					purchaseMaterialDetail.put("OccupationOfInventory", "N");
					purchaseMaterialDetail.put("Cabinet_No", varListPurchase.get(i).getString("Cabinet_No"));
					String staffName = Jurisdiction.getName();
					PageData pdData = new PageData();
					pdData.put("FNAME", staffName);
					PageData staffInfo = StaffService.getStaffId(pdData);
					if (null != staffInfo) {
						purchaseMaterialDetail.put("FCreator", staffInfo.getString("STAFF_ID"));
					}

					purchaseMaterialDetail.put("FCreatetime", Tools.date2Str(new Date()));
					purchaseMaterialDetail.put("FPushCount", "0.00");
					purchaseMaterialDetail.put("FAMOUNT", "0.00");

					purchaseMaterialDetailsService.save(purchaseMaterialDetail);

				}
				// 更新 详情信息 为 已下推采购
				for (String detailID : ArrayDATA_IDS) {
					PageData detailIDPageData = new PageData();
					detailIDPageData.put("Cabinet_Assembly_Detail_ID", detailID);
					PageData detail = Cabinet_Assembly_DetailService.findById(detailIDPageData);
					detail.put("If_Purchase_Done", "是");
					Cabinet_Assembly_DetailService.edit(detail);
					// 将所有 BOM下推状态为否的 都改为是
					PageData pdData = new PageData();
					pdData.put("Cabinet_Assembly_Detail_ID", detailID);
					List<PageData> listAll = Cabinet_BOMService.listAll(pdData);
					for (PageData pageData : listAll) {
						pageData.put("If_Purchase", "是");
						Cabinet_BOMService.edit(pageData);

					}
				}
				Cabinet_BOMService.editCGNUM(ArrayDATA_IDS);// 反写采购数量

				// 插入BOM变更记录

				for (String detailID : ArrayDATA_IDS) {
					PageData detailIDPageData = new PageData();
					detailIDPageData.put("Cabinet_Assembly_Detail_ID", detailID);
					PageData detail = Cabinet_Assembly_DetailService.findById(detailIDPageData);
					int FNUMCH = Integer.parseInt(detail.get("FNUMCH").toString());
					List<PageData> pdList = cabinet_bom_changeService.listAll(detailIDPageData);
					if (pdList.size() == 0) {// 第一次下推采购
						// 将本版本数据保存到临时表
						cabinet_bom_changeService.save(detailIDPageData);
					} else if (FNUMCH > 0) {
						pd.put("ROLE_NAME", "车间主任");
						List<PageData> staffList1 = usersService.listAllByRoleName(pd);
						String ReceivingAuthority1 = ",";
						for (PageData pdStaffx : staffList1) {
							ReceivingAuthority1 += pdStaffx.getString("USERNAME");
							ReceivingAuthority1 = ReceivingAuthority1 + ",";
						}

						pd.put("ROLE_NAME", "采购管理");
						List<PageData> staffList2 = usersService.listAllByRoleName(pd);
						for (PageData pdStaffx : staffList2) {
							ReceivingAuthority1 += pdStaffx.getString("USERNAME");
							ReceivingAuthority1 = ReceivingAuthority1 + ",";
						}
						String PROJECT_ID = detail.getString("PROJECT_ID");
						PageData projectParam = new PageData();
						projectParam.put("PROJECT_ID", PROJECT_ID);// 获取柜体信息主表数据中的PROJECT_ID字段
						PageData project = dprojectService.findById(projectParam);

						String PPROJECT_MANAGER = project.getString("PPROJECT_MANAGER");
						PageData staffParam = new PageData();
						staffParam.put("FNAME", PPROJECT_MANAGER);
						PageData staffInfo = StaffService.getStaffId(staffParam);
						if (staffInfo != null) {
							ReceivingAuthority1 += staffInfo.getString("USER_ID");
							ReceivingAuthority1 = ReceivingAuthority1 + ",";
						}
						String Responsible_Technology_Name = project.getString("Responsible_Technology_Name");
						if (Responsible_Technology_Name != null) {
							PageData staffParam1 = new PageData();
							staffParam1.put("FNAME", Responsible_Technology_Name);
							PageData staffInfo1 = StaffService.getStaffId(staffParam1);
							if (staffInfo1 != null) {
								ReceivingAuthority1 += staffInfo1.getString("USER_ID");
								ReceivingAuthority1 = ReceivingAuthority1 + ",";
							}
						}
						/*
						 * //消息提醒项目经理 List<PageData> staffList3 =
						 * Cabinet_Assembly_DetailService.listXMJLByIDS(ArrayDATA_IDS); for (PageData
						 * pdStaffx : staffList3) { ReceivingAuthority1 +=
						 * pdStaffx.getString("PPROJECT_MANAGER"); ReceivingAuthority1 =
						 * ReceivingAuthority1 + ","; } //消息提醒技术负责人 List<PageData> staffList4 =
						 * Cabinet_Assembly_DetailService.listJSFZRByIDS(ArrayDATA_IDS); for (PageData
						 * pdStaffx : staffList4) { ReceivingAuthority1 +=
						 * pdStaffx.getString("Responsible_Technology"); ReceivingAuthority1 =
						 * ReceivingAuthority1 + ","; }
						 */
						// 消息推送
						PageData pdNotice = new PageData();

						// 跳转页面
						pdNotice.put("AccessURL", "../../../views/projectManager/pro_plan/pro_plan_jssj.html");//
						pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
						pdNotice.put("ReadPeople", ",");// 已读人默认空
						pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
						pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
						pdNotice.put("TType", "消息推送");// 消息类型
						pdNotice.put("FContent",
								"项目编号为" + project.getString("PPROJECT_CODE") + "，项目名称为"
										+ project.getString("PPROJECT_NAME") + "，柜体编号为" + detail.getString("Cabinet_No")
										+ "的柜体有BOM变更，请及时查看");// 消息正文
						pdNotice.put("FTitle", "柜体BOM变更");// 消息标题
						pdNotice.put("LinkIf", "no");// 是否跳转页面
						pdNotice.put("DataSources", "柜体BOM变更");// 数据来源
						pdNotice.put("ReceivingAuthority", ReceivingAuthority1);// 接收人
						pdNotice.put("Report_Key", "task");// 手机app
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
						String WXNR = "【柜体BOM变更提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
								+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
						// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
						// "0");
						weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");

						// 插入变更记录
						PageData pdCH = new PageData();
						pdCH.put("Relation_ID", detail.getString("PROJECT_ID"));
						pdCH.put("FType", "BOM");
						pdCH.put("Change_Duty", "");
						pdCH.put("Change_Description", "BOM变更");
						pdCH.put("CC", Jurisdiction.getName());
						pdCH.put("Change_Date", Tools.date2Str(new Date()));
						pdCH.put("FRelation_ID", detail.getString("Cabinet_Assembly_Detail_ID"));
						pdCH.put("Change_ID", this.get32UUID()); // 主键
						ChangeService.save(pdCH);
						// 插入变更记录明细
						PageData pdCHMX = new PageData();
						pdCHMX.put("Cabinet_Assembly_Detail_ID", detailID);
						pdCHMX.put("Change_ID", pdCH.getString("Change_ID"));
						cabinet_bom_changemxService.save(pdCHMX);
						// 删除临时表数据
						cabinet_bom_changeService.delete(detailIDPageData);
						// 将本版本数据保存到临时表
						cabinet_bom_changeService.save(detailIDPageData);
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
		}

		map.put("result", errInfo);
		return map;
	}

	/**
	 * 下推柜体数据列表
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/getLoadCountByBeginEndTime")
	@ResponseBody
	public Object getLoadCountByBeginEndTime() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			List<PageData> beginEndTime = Cabinet_Assembly_DetailService.getBeginEndTime();
			List<String> timeList = Lists.newArrayList();
			List<Integer> yesList = Lists.newArrayList();
			List<Integer> noList = Lists.newArrayList();
			List<Integer> totalList = Lists.newArrayList();
			for (PageData pageData : beginEndTime) {
				pageData.put("Load_Status", "是");
				PageData loadYesCount = Cabinet_Assembly_DetailService.LoadCountByBeginEndTime(pageData);
				Integer yesNum = Integer.valueOf(loadYesCount.get("num").toString());
				yesList.add(yesNum);
				pageData.put("Load_Status", "否");
				PageData loadNoCount = Cabinet_Assembly_DetailService.LoadCountByBeginEndTime(pageData);
				Integer noNum = Integer.valueOf(loadNoCount.get("num").toString());
				noList.add(noNum);
				timeList.add(pageData.getString("beginB"));
				Integer totalNum = yesNum + noNum;
				totalList.add(totalNum);
			}

			map.put("yesList", yesList);
			map.put("noList", noList);
			map.put("timeList", timeList);
			map.put("totalList", totalList);
			map.put("result", "success");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "failed");
			map.put("msg", e.getMessage());
			return map;
		}
	}

	/**
	 * 根据规则类型获取编码值
	 * 
	 * @param
	 * @throws Exception
	 */
	public String getRuleNumByRuleType(String ruleType) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 根据规则类型 查询规则详情
		PageData pgData = new PageData();
		pgData.put("CODINGRULESTYPE", ruleType);

		List<PageData> dataByCodingRulesType = codingrulesService.getDataByCodingRulesType(pgData);
		if (CollectionUtils.isEmpty(dataByCodingRulesType)) {
			throw new RuntimeException("获取规则号失败，该类型数据不存在");
		}

		String returnCode = "";
		String CODINGRULEID = "";
		String ACQUISITIONTIME = "";
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (PageData pageData : dataByCodingRulesType) {
			// 根据详情类型 生成对应的编号
			CODINGRULEID = pageData.getString("CODINGRULES_ID");
			String CODINGRULESDETAILID = pageData.getString("CODINGRULESDETAIL_ID");
			String TERMOFVALIDITY = pageData.getString("TERMOFVALIDITY");
			String DETAILTYPE = pageData.getString("DETAILTYPE");
			String FLENGTH = pageData.getString("FLENGTH");
			String FFORMAT = pageData.getString("FFORMAT");
			String TSTEP = pageData.getString("TSTEP");
			String SETTINGVALUE = pageData.getString("SETTINGVALUE");
			String RESETPERIOD = pageData.getString("RESETPERIOD");
			// 进制问题需要确定
			/* String STREAMCODING = pageData.getString("STREAMCODING"); */
			ACQUISITIONTIME = pageData.getString("ACQUISITIONTIME");

			// 判断是否过期

			String termDateTime = TERMOFVALIDITY.substring(12, TERMOFVALIDITY.length());
			Date parse = null;
			parse = sdFormat.parse(termDateTime);
			long time = parse.getTime();
			long currentTimeMillis = new Date().getTime();
			if (time < currentTimeMillis) {
				throw new RuntimeException("该编码规则已过期");
			}

			// 常量类型直接返回当初的设置值
			if ("constant".equals(DETAILTYPE)) {
				returnCode += SETTINGVALUE;
			}

			// 日期类型直接返回当初的设置格式后 格式化时间返回
			if ("date".equals(DETAILTYPE)) {
				String code = "";
				String acqNow = sdFormat.format(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat(FFORMAT);
				try {
					code = sdf.format(sdFormat.parse(ACQUISITIONTIME));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ("everyday".equals(RESETPERIOD)) {
					if (!acqNow.equals(ACQUISITIONTIME)) {
						code = sdf.format(new Date());
					}
				}
				returnCode += code;
			}

			// 根据规则类型生成流水号
			if ("serial".equals(DETAILTYPE)) {
				String acqNow = sdFormat.format(new Date());
				// 如果每天重置，日期变更 重新设置位1开始
				if ("everyday".equals(RESETPERIOD)) {
					if (!acqNow.equals(ACQUISITIONTIME)) {
						SETTINGVALUE = "0";
					}
				}
				// 如果不重置，则接着前一天的时间继续编号
				else {
					acqNow = ACQUISITIONTIME;
				}

				// 自增 步长
				Integer getValueInc = Integer.valueOf(SETTINGVALUE) + Integer.valueOf(TSTEP);
				String formatNum = this.formatNum(getValueInc, Integer.valueOf(FLENGTH));

				// 回写数据
				PageData pgRuleDetail = new PageData();
				pgRuleDetail.put("CODINGRULESDETAIL_ID", CODINGRULESDETAILID);
				PageData findDetailById = codingRulesDetailService.findById(pgRuleDetail);
				findDetailById.put("SETTINGVALUE", formatNum);
				codingRulesDetailService.edit(findDetailById);
				// 返回結果
				returnCode += formatNum;
			}
		}
		// 更新数据
		PageData pgRule = new PageData();
		pgRule.put("CODINGRULES_ID", CODINGRULEID);
		PageData findById = codingrulesService.findById(pgRule);
		findById.put("GETVALUE", returnCode);
		findById.put("ACQUISITIONTIME", sdFormat.format(new Date()));
		codingrulesService.edit(findById);
		map.put("result", errInfo);
		map.put("pd", returnCode);
		return returnCode;
	}

	private String formatNum(int input, int FLENGTH) {
		// 大于1000时直接转换成字符串返回
		double pow = Math.pow(10.00, FLENGTH);
		if (input > pow - 1) {
			throw new RuntimeException("生成失败，编码号已经超过了" + FLENGTH + "位");
		}
		return String.format("%0" + FLENGTH + "d", input);
	}

	/**
	 * 项目看板-技术部待设计
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listXMJSSJ")
	@ResponseBody
	public Object listXMJSSJ(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			// 关键词检索条件
			String KEYWORDS = pd.getString("KEYWORDS");
			if (Tools.notEmpty(KEYWORDS)) {
				pd.put("KEYWORDS", KEYWORDS.trim());
			}
			if (StringUtil.isNotEmpty(pd.getString("BTStatus"))) {
				if ("N-N".equals(pd.getString("BTStatus"))) {
					pd.put("If_Bom_Done", "否");
					pd.put("If_Drawings_Done", "否");
				}
				if ("Y-N".equals(pd.getString("BTStatus"))) {
					pd.put("If_Bom_Done", "是");
					pd.put("If_Drawings_Done", "否");
				}
				if ("N-Y".equals(pd.getString("BTStatus"))) {
					pd.put("If_Bom_Done", "否");
					pd.put("If_Drawings_Done", "是");
				}
				if ("Y-Y".equals(pd.getString("BTStatus"))) {
					pd.put("If_Bom_Done", "是");
					pd.put("If_Drawings_Done", "是");
				}
			}

			pd.put("CREATORMAN", Jurisdiction.getName());
			page.setPd(pd);
			String staffName = Jurisdiction.getName();

			List<PageData> varList = Cabinet_Assembly_DetailService.listXMJSSJ(page); // 列出Cabinet_Assembly_Detail列表
			for (PageData pageData : varList) {
				if (staffName.equals(pageData.getString("Responsible_Technology_Name"))) {
					pageData.put("showOpt", "Y");
				} else {
					pageData.put("showOpt", "N");
				}
			}

			map.put("varList", varList);
			map.put("page", page);
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
	 * 下推采购
	 * 
	 * @param FFILEPATH:文件、清单信息
	 * @throws Exception
	 */
	@RequestMapping(value = "/PurchaseList/add")
	@ResponseBody
	public Object add(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			// 单号验重
			PageData pdNum = PurchaseListService.getRepeatNum(pd);
			if (pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) > 0) {
				errInfo = "fail1";// 单号重复
			} else {
				pd.put("FNAME", Jurisdiction.getName());
				String staffID = staffService.getStaffId(pd).getString("STAFF_ID");
				String purchaseList_id = this.get32UUID();
				pd.put("PurchaseList_ID", purchaseList_id); // 主键
				pd.put("FMakeBillsPersoID", staffID);// 查询职员ID

				pd.put("FMakeBillsTime", Tools.date2Str(new Date()));
				pd.put("FStatus", "创建");
				pd.put("FCheckFlag", "N");
				PurchaseListService.save(pd);
				// 添加审批意见
				PageData cpd = new PageData();
				cpd.put("PurchaseList_Comments_ID", this.get32UUID()); // 主键
				cpd.put("BillID", purchaseList_id); // 操作人
				cpd.put("FStatus", "创建 "); // 操作人
				cpd.put("FComments", "无"); // 操作人
				cpd.put("FOperator", staffID); // 操作人
				cpd.put("OperationTime", Tools.date2Str(new Date())); // 操作时间
				PurchaseList_CommentsService.save(cpd);
				// 上传附件
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar calendar = Calendar.getInstance();
				String dateName = df.format(calendar.getTime());
				String ffile = DateUtil.getDays();
				String FFILENAME = "";
				String FPFFILEPATH = "";
				if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
					String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
					String fileNamereal = pd.getString("FFILENAME").substring(0,
							pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
					FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal + dateName);// 执行上传
					FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
					// 附件集插入数据
					PageData pdFile = new PageData();
					pdFile.put("DataSources", "采购订单");
					pdFile.put("AssociationIDTable", "PP_PurchaseList");
					pdFile.put("AssociationID", pd.getString("PurchaseList_ID"));
					pdFile.put("FName", FFILENAME);
					pdFile.put("FUrl", FPFFILEPATH);
					pdFile.put("FExplanation", "");
					pdFile.put("FCreatePersonID", pd.getString("FMakeBillsPersoID"));
					pdFile.put("FCreateTime", Tools.date2Str(new Date()));
					attachmentsetService.check(pdFile);
				}
				map.put("PurchaseList_ID", purchaseList_id);

			}
			// 插入操作日志
			PageData pdOp = new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
			pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));// 操作人
			pdOp.put("FunctionType", "");// 功能类型
			pdOp.put("FunctionItem", "采购订单");// 功能项
			pdOp.put("OperationType", "新增");// 操作类型
			pdOp.put("Fdescribe", "");// 描述
			pdOp.put("DeleteTagID", pd.get("PurchaseList_ID"));// 删改数据ID
			operationrecordService.save(pdOp);

			String DATA_IDS = pd.getString("DATA_IDS");
			List<PageData> varList = Lists.newArrayList();
			String[] ArrayDATA_IDS = DATA_IDS.split(",");

			varList = Cabinet_BOMService.listByCabinetAssemblyDetailIDS(ArrayDATA_IDS);

			PageData pData = new PageData();
			pData.put("Cabinet_Assembly_Detail_ID", ArrayDATA_IDS[0]);
			PageData findById = Cabinet_Assembly_DetailService.findById(pData);
			String PPROJECT_CODE = findById.getString("PPROJECT_CODE");
//
			String MAT_AUXILIARYMX_ID = "";
			PageData pg1 = new PageData();
			pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
			pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
			List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
					.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
			if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
				MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0).getString("MAT_AUXILIARYMX_ID");
			}

			for (PageData pageData : varList) {

				String MaterialID = pageData.getString("MAT_BASIC_ID");
				String FClass = pageData.getString("MAT_CLASS");
				String wareHouseType = "工厂库";
				PageData pg = new PageData();
				pg.put("ItemId", MaterialID);
				pg.put("FType", wareHouseType);
				pg.put("FClass", FClass);
				// 工厂库 公用 辅助属性
				pg.put("MaterialSProp", "80108fa272884119b09eab03a78d3cc5");
				pg.put("MaterialSPropKey", "4486905b9e47428985c36c9d55d21ad7");
				List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);
				BigDecimal BOM_COUNT = new BigDecimal(pageData.get("BOM_COUNT").toString());
				if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
					pageData.put("STOCK_ACT_COUNT", 0.00);
					pageData.put("PURCHASE_COUNT", BOM_COUNT);
					pageData.put("PPROJECT_CODE", PPROJECT_CODE);
				} else {
					BigDecimal ActualCountBig = new BigDecimal("0");
					for (PageData actualCountByFTYPEAndItemIDData : actualCountByFTYPEAndItemID) {
						ActualCountBig = ActualCountBig
								.add(new BigDecimal(actualCountByFTYPEAndItemIDData.get("ActualCount").toString()));
					}
					pageData.put("STOCK_ACT_COUNT", ActualCountBig.toString());
					pageData.put("PURCHASE_COUNT", BOM_COUNT.subtract(ActualCountBig).doubleValue() <= 0 ? 0
							: BOM_COUNT.subtract(ActualCountBig).doubleValue());
					pageData.put("PPROJECT_CODE", PPROJECT_CODE);
				}
//
				String PURCHASE_COUNT_STR = pageData.get("PURCHASE_COUNT").toString();
				String BOM_COUNT_STR = pageData.get("BOM_COUNT").toString();
//
				BigDecimal PURCHASE_COUNT_BIG = new BigDecimal(PURCHASE_COUNT_STR);
				BigDecimal BOM_COUNT_BIG = new BigDecimal(BOM_COUNT_STR);
//
//				// 插入 辅助属性为 项目的 该 物料的库存 为 OCCUPY_BIG
//				// 增加库存
//
				String WareHouseId = "";
				String LocationId = "";
				List<PageData> wareHouseList = wh_warehouseService.wareHouseList(pg);
				if (CollectionUtil.isNotEmpty(wareHouseList)) {
					WareHouseId = wareHouseList.get(0).getString("WH_WAREHOUSE_ID");
				}
				PageData pData2 = new PageData();
				pData2.put("WH_STORAGEAREA_ID", WareHouseId);
				List<PageData> locationList = wh_locationService.locationList(pData2);
				if (CollectionUtil.isNotEmpty(locationList)) {
					LocationId = locationList.get(0).getString("WH_LOCATION_ID");
				}
//
				PageData inParam = new PageData();
				List<PageData> listByMatCode = mat_basicService.getListByMatCode(pageData.getString("MAT_CODE"));
				if (CollectionUtil.isNotEmpty(listByMatCode)) {
					inParam.put("FUnit", listByMatCode.get(0).getString("MAT_MAIN_UNIT"));// 单位
					inParam.put("FBatch", listByMatCode.get(0).getString("FBATCH"));// 批号
					inParam.put("ProductionBatch", listByMatCode.get(0).getString("FBATCH"));// 生产批号
				}
				inParam.put("Stock_ID", this.get32UUID());
				inParam.put("WarehouseID", WareHouseId);// 仓库id
				inParam.put("PositionID", LocationId);// 库位id
				inParam.put("ItemID", pageData.getString("MAT_BASIC_ID"));// 物料id
				inParam.put("StorageStatus", "工厂");// 存储状态(工厂,车间)
				inParam.put("QRCode", pageData.getString("MAT_CODE"));// 物料二维码
				inParam.put("OneThingCode", "");// 一物码
				inParam.put("MaterialSProp", "54e6b1a185764c32bff189907a571a6d");// 物料辅助属性
				inParam.put("MaterialSPropKey", MAT_AUXILIARYMX_ID);// 物料辅助属性值
				inParam.put("SpecificationDesc", pageData.getString("MAT_SPECS"));// 规格
				inParam.put("ActualCount", BOM_COUNT_BIG.subtract(PURCHASE_COUNT_BIG));// 实际数量
//
				inParam.put("UseCount", 0);// 使用数量
				inParam.put("UseIf", "NO");// 是否占用
				inParam.put("FLevel", 1);// 等级
//
				inParam.put("RunningState", "在用");// 运行状态
				inParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
				inParam.put("UsageTime", Tools.date2Str(new Date()));// 使用时间
				inParam.put("DateOfManufacture", Tools.date2Str(new Date()));// 生产日期
//
				inParam.put("LastModifiedTime", Tools.date2Str(new Date()));// 最后修改日期
				inParam.put("LastCountTime", Tools.date2Str(new Date()));// 上次盘点日期
				inParam.put("FStatus", "入库");// 状态
//
				StockService.inStock(inParam);
//	
//				// 占用数量
				BigDecimal OCCUPY_BIG = BOM_COUNT_BIG.subtract(PURCHASE_COUNT_BIG);
				List<PageData> stockListByFTYPEAndItemID = StockService.getStockListByFTYPEAndItemID(pg);
				for (int i = 0; i < stockListByFTYPEAndItemID.size(); i++) {
					PageData sData = stockListByFTYPEAndItemID.get(i);
					// 如果占用数量 为 0 则
					if (OCCUPY_BIG.doubleValue() == 0) {
						break;
					}
					BigDecimal remaind = new BigDecimal(sData.get("ActualCount").toString()).subtract(OCCUPY_BIG);

					if (remaind.doubleValue() > 0) {
						// 如果够减 则 更新当条的数量 为 剩余数量 ,OCCUPY_BIG 数量为 0
						OCCUPY_BIG = new BigDecimal("0");
						sData.put("ActualCount", remaind);
						StockService.edit(sData);

					} else {
						// 如果不够减 则 更新当前数量为 0 ,更新 OCCUPY_BIG 数量为 剩余
						OCCUPY_BIG = OCCUPY_BIG.subtract(new BigDecimal(sData.get("ActualCount").toString()));
						sData.put("ActualCount", 0);
						StockService.edit(sData);
					}
				}
			}

			Map<String, PageData> result1 = new HashMap<String, PageData>();
			for (PageData pageData : varList) {
				String MaterialID = pageData.get("MAT_BASIC_ID").toString();
				BigDecimal value = new BigDecimal((pageData.get("BOM_COUNT").toString()));
				if (result1.containsKey(MaterialID)) {
					BigDecimal bigDecimal = new BigDecimal(result1.get(MaterialID).get("BOM_COUNT").toString());
					value = value.add(bigDecimal);
				}
				pageData.put("PURCHASE_COUNT", value);
				result1.put(MaterialID, pageData);
			}
			System.out.println("合并后的数据：" + JSON.toJSONString(result1));

			List<PageData> varListPurchase = Lists.newArrayList();
			Collection<PageData> values = result1.values();
			for (PageData pageData : values) {
				varListPurchase.add(pageData);
			}

			for (int i = 0; i < varListPurchase.size(); i++) {

				if (new BigDecimal(varListPurchase.get(i).get("PURCHASE_COUNT").toString()).doubleValue() == 0) {
					continue;
				}
				// 插入详情
				PageData purchaseMaterialDetail = new PageData();
				purchaseMaterialDetail.put("PurchaseMaterialDetails_ID", this.get32UUID());
				purchaseMaterialDetail.put("RowClose", "N");
				purchaseMaterialDetail.put("RowNum", i + 1);
				purchaseMaterialDetail.put("SourceOrderType", pd.getString("SourceOrderType"));
				purchaseMaterialDetail.put("PurchaseID", pd.getString("PurchaseList_ID"));
				purchaseMaterialDetail.put("MaterialID", varListPurchase.get(i).getString("MAT_BASIC_ID"));

				// 辅助属性为 项目的
				purchaseMaterialDetail.put("SProp", "54e6b1a185764c32bff189907a571a6d");
				purchaseMaterialDetail.put("SPropKey", MAT_AUXILIARYMX_ID);

				purchaseMaterialDetail.put("SpecificationsDesc", varListPurchase.get(i).getString("MAT_SPECS"));
				purchaseMaterialDetail.put("FCount", varListPurchase.get(i).get("PURCHASE_COUNT").toString());

				purchaseMaterialDetail.put("EstimatedTimeOfArrival", pd.getString("EstimatedTimeOfArrival"));

				purchaseMaterialDetail.put("Admission", "0.00");
				purchaseMaterialDetail.put("ReturnedMaterialsCount", "0.00");
				purchaseMaterialDetail.put("EnableEarlyWarningIF", "N");
				purchaseMaterialDetail.put("OccupationOfInventory", "N");
				purchaseMaterialDetail.put("Cabinet_No", varListPurchase.get(i).getString("Cabinet_No"));
				String staffName = Jurisdiction.getName();
				PageData pdData = new PageData();
				pdData.put("FNAME", staffName);
				PageData staffInfo = StaffService.getStaffId(pdData);
				if (null != staffInfo) {
					purchaseMaterialDetail.put("FCreator", staffInfo.getString("STAFF_ID"));
				}

				purchaseMaterialDetail.put("FCreatetime", Tools.date2Str(new Date()));
				purchaseMaterialDetail.put("FPushCount", "0.00");
				purchaseMaterialDetail.put("FAMOUNT", "0.00");

				purchaseMaterialDetailsService.save(purchaseMaterialDetail);

			}
			// 更新 详情信息 为 已下推采购
			for (String detailID : ArrayDATA_IDS) {
				PageData detailIDPageData = new PageData();
				detailIDPageData.put("Cabinet_Assembly_Detail_ID", detailID);
				PageData detail = Cabinet_Assembly_DetailService.findById(detailIDPageData);
				detail.put("If_Purchase_Done", "是");
				Cabinet_Assembly_DetailService.edit(detail);
				// 将所有 BOM下推状态为否的 都改为是
				PageData pdData = new PageData();
				pdData.put("Cabinet_Assembly_Detail_ID", detailID);
				List<PageData> listAll = Cabinet_BOMService.listAll(pdData);
				for (PageData pageData : listAll) {
					pageData.put("If_Purchase", "是");
					Cabinet_BOMService.edit(pageData);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20320906 实际甘特图列表-排程时间
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listAllPC")
	@ResponseBody
	public Object listAllPC() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			// 关键词检索条件
			String KEYWORDS = pd.getString("KEYWORDS");
			if (Tools.notEmpty(KEYWORDS)) {
				pd.put("KEYWORDS", KEYWORDS.trim());
			}
			List<PageData> varList = Cabinet_Assembly_DetailService.listAllPC(pd); // 列出Cabinet_Assembly_Detail列表
			map.put("varList", varList);
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
