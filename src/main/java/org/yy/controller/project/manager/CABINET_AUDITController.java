package org.yy.controller.project.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.yy.service.project.manager.CABINET_AUDITService;
import org.yy.service.project.manager.Cabinet_AssemblyService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.DPROJECTService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

import com.github.pagehelper.util.StringUtil;

/**
 * 说明：柜体审核 作者：YuanYes QQ356703572 时间：2021-07-07 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/cabinet_audit")
public class CABINET_AUDITController extends BaseController {

	@Autowired
	private CABINET_AUDITService cabinet_auditService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;
	@Autowired
	private Cabinet_AssemblyService Cabinet_AssemblyService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private DPROJECTService dprojectService;
	@Autowired
	private StaffService StaffService;
	@Autowired
	private DPROJECTService projectService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("cabinet_audit:add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CABINET_AUDIT_ID", this.get32UUID()); // 主键
		cabinet_auditService.save(pd);
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
	@RequiresPermissions("cabinet_audit:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		cabinet_auditService.delete(pd);
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
	@RequiresPermissions("cabinet_audit:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		cabinet_auditService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 审核
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/audit")
	@ResponseBody
	public Object audit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FAudit_State = pd.getString("FAudit_State");
		PageData pdF = cabinet_auditService.findById(pd);
		String FTYPE = pd.getString("FTYPE");// 审核类型
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (FAudit_State.equals("通过")) {
			PageData old = Cabinet_Assembly_DetailService.findById(pd);

			/* Cabinet_Assembly_DetailService.edit(old); */

			if (FTYPE.equals("技术审核")) {
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
						"负责技术" + Jurisdiction.getName() + "一键结束了柜体" + cad.getString("Cabinet_No") + "的图纸和BOM，请注意查看");// 消息正文
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
				// 反写柜体审核状态

				cad.put("FStatus", "设计结束");
				cad.put("FAudit_State", FAudit_State);
				cad.put("FAudit_Bom_State", FAudit_State);
				cad.put("FAudit_Cabinet_State", FAudit_State);
				Cabinet_Assembly_DetailService.edit(cad);
			} else if (FTYPE.equals("设计冻结")) {
				String Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
				PageData caData = new PageData();
				caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
				PageData cad = Cabinet_Assembly_DetailService.findById(caData);

				String caID = cad.getString("Cabinet_Assembly_ID");
				PageData caParam = new PageData();
				caParam.put("Cabinet_Assembly_ID", caID);
				PageData ca = Cabinet_AssemblyService.findById(caParam);
				// 反写柜体审核状态
				cad.put("FStatus", "设计冻结");
				cad.put("FAudit_CabinetDJ_State", FAudit_State);
				cad.put("FIs_NeedDJ_Time", Tools.date2Str(new Date()));
				Cabinet_Assembly_DetailService.edit(cad);
			} else if (FTYPE.equals("异常结项审核")) {
				String PROJECT_ID = pd.getString("PROJECT_ID");
				PageData caData = new PageData();
				caData.put("PROJECT_ID", PROJECT_ID);
				PageData cad = projectService.findById(caData);
				cad.put("FEND_TIME", Tools.date2Str(new Date()));
				cad.put("FEND_MAN", Jurisdiction.getName());
				cad.put("FAudit_State", FAudit_State);
				cad.put("FEND_STATE", "结束");
				projectService.over(cad);
			} else if (FTYPE.equals("BOM完成审核")) {
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

				// 反写柜体审核状态
				cad.put("If_Bom_Done", "是");
				cad.put("Bom_Done_Time", Tools.date2Str(new Date()));
				cad.put("FAudit_Bom_State", FAudit_State);
				Cabinet_Assembly_DetailService.edit(cad);
			} else if (FTYPE.equals("图纸完成审核")) {
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
								+ Cabinet_Assembly_Detail_ID + "&NOTICE_TYPE=" + "ALL");// pdNotice.put("NOTICE_ID",
																						// this.get32UUID()); // 主键
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
				// 反写柜体审核状态
				cad.put("If_Drawings_Done", "是");
				cad.put("Drawings_Done_Time", Tools.date2Str(new Date()));
				cad.put("FAudit_Cabinet_State", FAudit_State);
				Cabinet_Assembly_DetailService.edit(cad);
			}
		} else {
			if (FTYPE.equals("技术审核")) {
				String Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
				PageData caData = new PageData();
				caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
				PageData cad = Cabinet_Assembly_DetailService.findById(caData);
				cad.put("FAudit_State", FAudit_State);
				cad.put("FAudit_Bom_State", FAudit_State);
				cad.put("FAudit_Cabinet_State", FAudit_State);
				Cabinet_Assembly_DetailService.edit(cad);
			} else if (FTYPE.equals("设计冻结")) {
				String Cabinet_Assembly_Detail_ID = pd.getString("Cabinet_Assembly_Detail_ID");
				PageData caData = new PageData();
				caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
				PageData cad = Cabinet_Assembly_DetailService.findById(caData);
				cad.put("FAudit_CabinetDJ_State", FAudit_State);
				Cabinet_Assembly_DetailService.edit(cad);
			} else if (FTYPE.equals("异常结项审核")) {
				String PROJECT_ID = pd.getString("PROJECT_ID");
				PageData caData = new PageData();
				caData.put("PROJECT_ID", PROJECT_ID);
				PageData cad = projectService.findById(caData);
				cad.put("FAudit_State", FAudit_State);
				projectService.edit(cad);
			}

		}

		pdF.put("FAUDIT_STATE", pd.getString("FAUDIT_STATE"));
		pdF.put("FAUDIT_MAN", Jurisdiction.getName()); //
		pdF.put("FAUDIT_TIME", DateUtil.date2Str(new Date())); //
		pdF.put("FAUDIT_FNOTE", pd.getString("FAUDIT_FNOTE"));
		cabinet_auditService.edit(pdF);
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
	// @RequiresPermissions("cabinet_audit:list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
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
		pd.put("Responsible_Technology_Name", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = cabinet_auditService.list(page); // 列出CABINET_AUDIT列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210930 异常结项审核
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listXM")
	// @RequiresPermissions("cabinet_audit:list")
	@ResponseBody
	public Object listXM(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件

		pd.put("Responsible_Technology_Name", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = cabinet_auditService.listXM(page); // 列出CABINET_AUDIT列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 柜体审核记录列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listSH")
	// @RequiresPermissions("cabinet_audit:list")
	@ResponseBody
	public Object listSH(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = cabinet_auditService.listSH(page); // 列出CABINET_AUDIT列表
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
	@RequiresPermissions("cabinet_audit:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = cabinet_auditService.findById(pd); // 根据ID读取
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
	@RequiresPermissions("cabinet_audit:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			cabinet_auditService.deleteAll(ArrayDATA_IDS);
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
		titles.add("柜体ID"); // 1
		titles.add("审核人"); // 2
		titles.add("审核时间"); // 3
		titles.add("审核结果"); // 4
		titles.add("审核描述"); // 5
		titles.add("创建人"); // 6
		titles.add("创建时间"); // 7
		dataMap.put("titles", titles);
		List<PageData> varOList = cabinet_auditService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CABINET_ASSEMBLY_DETAIL_ID")); // 1
			vpd.put("var2", varOList.get(i).getString("FAUDIT_MAN")); // 2
			vpd.put("var3", varOList.get(i).getString("FAUDIT_TIME")); // 3
			vpd.put("var4", varOList.get(i).getString("FAUDIT_STATE")); // 4
			vpd.put("var5", varOList.get(i).getString("FAUDIT_FNOTE")); // 5
			vpd.put("var6", varOList.get(i).getString("FCREATOR")); // 6
			vpd.put("var7", varOList.get(i).getString("FCTIME")); // 7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
