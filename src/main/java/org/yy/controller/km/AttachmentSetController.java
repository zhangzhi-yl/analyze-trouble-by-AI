package org.yy.controller.km;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.CollectionUtil;
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
import org.yy.service.km.AttachmentSetService;
import org.yy.service.project.manager.Cabinet_AssemblyService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.DPROJECTService;
import org.yy.service.system.UsersService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

import com.github.pagehelper.util.StringUtil;

/**
 * 说明：附件集 作者：YuanYes QQ356703572 时间：2020-11-06 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/attachmentset")
public class AttachmentSetController extends BaseController {

	@Autowired
	private AttachmentSetService attachmentsetService;

	@Autowired
	private StaffService staffService;
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
	private UsersService usersService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ATTACHMENTSET_ID", this.get32UUID()); // 主键
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		pd = this.getPageData();
		PageData pd1 = new PageData();
		PageData pdOp = new PageData();
		String FFILENAME = "";
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = staffService.getStaffId(pdOp).getString("STAFF_ID");// 操作人
		if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal + dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
			String FExtension = FFILENAME.split("\\.")[1]; // 获取文件扩展名
			pd1.put("AttachmentSet_ID", this.get32UUID());
			pd1.put("FUrl", FPFFILEPATH); // 附件路径
			pd1.put("FName", pd.getString("FName")); // 附件名称
			pd1.put("DataSources", pd.getString("DataSources")); // 数据源
			// pd1.put("AssociationIDTable", pd.getString("AssociationIDTable")); //数据源表
			pd1.put("AssociationID", pd.getString("AssociationID")); // 数据源ID
			pd1.put("FExplanation", pd.getString("FExplanation")); // 备注
			pd1.put("FStatus", "使用");
			pd1.put("TType", pd.getString("TType"));
			pd1.put("FExtension", FExtension);
			pd1.put("FCreatePersonID", STAFF_ID); // 创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间
		}
		pd1.put("FAudit_State", "否");
		attachmentsetService.save(pd1);
		map.put("result", errInfo);
		map.put("pd", pd1);
		return map;
	}

	/**
	 * 变更记录表保存
	 * 
	 * @param v1 吴双双 2021-05-28
	 *           将attachmentset_change.html传入的AssociationID和FUrl，以及页面输入的信息保存到变更记录表中
	 *           v2 陈春光 2021-05-28 变更图纸后给项目经理发图纸 v3 20210830 管悦
	 *           变更提醒：车间主任、项目经理、采购、技术负责人
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeadd")
	@ResponseBody
	public Object changeadd() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String Relation_ID = pd.getString("Relation_ID");

			pd.put("Change_ID", this.get32UUID()); // 变更记录表主键
			pd.put("Change_Date", Tools.date2Str(new Date()));// 变更时间
			pd.put("Relation_ID", pd.getString("PROJECT_ID"));
			pd.put("FRelation_ID", Relation_ID);
			attachmentsetService.changesave(pd);

			// 图纸变更
			String Cabinet_Assembly_Detail_ID = Relation_ID;// 根据关联ID，获取柜体信息从表主键
			if (StringUtil.isEmpty(Cabinet_Assembly_Detail_ID)) { // 若主键为空，则没有数据
				map.put("result", "error");
				return map;
			}
			PageData caData = new PageData();
			caData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
			PageData cad = Cabinet_Assembly_DetailService.findById(caData);// 根据主键，获取数据

			String caID = cad.getString("Cabinet_Assembly_ID"); // 获取数据中的柜体信息主表主键
			if (StringUtil.isEmpty(caID)) { // 若主键为空，则没有数据
				map.put("result", "error");
				return map;
			}
			PageData caParam = new PageData();
			caParam.put("Cabinet_Assembly_ID", caID);

			PageData ca = Cabinet_AssemblyService.findById(caParam); // 根据主键，获取柜体信息主表数据

			String PROJECT_ID = ca.getString("PROJECT_ID");
			PageData projectParam = new PageData();

			projectParam.put("PROJECT_ID", PROJECT_ID);// 获取柜体信息主表数据中的PROJECT_ID字段
			PageData project = dprojectService.findById(projectParam);

			if (null == project) { // 若项目为空，则返回失败
				map.put("result", "error");
				return map;
			}
			String ReceivingAuthority = ",";

			String PPROJECT_MANAGER = project.getString("PPROJECT_MANAGER");
			PageData staffParam = new PageData();
			staffParam.put("FNAME", PPROJECT_MANAGER);
			PageData staffInfo = StaffService.getStaffId(staffParam);
			if (staffInfo != null) {
				ReceivingAuthority += staffInfo.getString("USER_ID");
				ReceivingAuthority = ReceivingAuthority + ",";
			}
			String Responsible_Technology_Name = project.getString("Responsible_Technology_Name");
			if (Responsible_Technology_Name != null) {

				PageData staffParam1 = new PageData();
				staffParam1.put("FNAME", Responsible_Technology_Name);
				PageData staffInfo1 = StaffService.getStaffId(staffParam1);
				if (staffInfo1 != null) {
					ReceivingAuthority += staffInfo1.getString("USER_ID");
					ReceivingAuthority = ReceivingAuthority + ",";
				}
			}
			pd.put("ROLE_NAME", "车间主任");
			List<PageData> staffList1 = usersService.listAllByRoleName(pd);
			for (PageData pdStaffx : staffList1) {
				ReceivingAuthority += pdStaffx.getString("USERNAME");
				ReceivingAuthority = ReceivingAuthority + ",";
			}

			pd.put("ROLE_NAME", "采购管理");
			List<PageData> staffList2 = usersService.listAllByRoleName(pd);
			for (PageData pdStaffx : staffList2) {
				ReceivingAuthority += pdStaffx.getString("USERNAME");
				ReceivingAuthority = ReceivingAuthority + ",";
			}

			PageData pdNotice = new PageData();
			pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
			// 跳转页面
			pdNotice.put("AccessURL",
					"../../../views/projectManager/pro_plan/pro_plan_jssj.html?Cabinet_Assembly_Detail_ID="
							+ Cabinet_Assembly_Detail_ID + "&NOTICE_TYPE=" + "ALL");//
			pdNotice.put("ReadPeople", ",");// 已读人默认空
			pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
			pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
			pdNotice.put("TType", "消息推送");// 消息类型
			pdNotice.put("FContent", "负责技术" + Jurisdiction.getName() + "变更了柜体" + cad.getString("Cabinet_No") + "的一条"
					+ pd.getString("FType") + "信息，变更理由为" + pd.getString("Change_Duty") + "请注意查看");// 消息正文
			pdNotice.put("FTitle", "柜体图纸变更");// 消息标题
			pdNotice.put("LinkIf", "no");// 是否跳转页面
			pdNotice.put("DataSources", "柜体图纸变更");// 数据来源
			pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
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
			String WXNR = "【柜体图纸变更提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
					+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
			// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
			// "0");
			weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");

			map.put("result", errInfo);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "error");
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
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		attachmentsetService.delete(pd);
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
		attachmentsetService.edit(pd);
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
		List<PageData> varList = attachmentsetService.list(page); // 列出AttachmentSet列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 吴双双 20210607 版本号获取
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getvers")
	@ResponseBody
	public Object getvers() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData vers = attachmentsetService.findByAId(pd); // 根据AssociationID，查出AssociationID下的图纸个数
			map.put("vers", vers);
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
	 * v1 陈春光 2021-05-17 根据柜体详情id获取图纸列表，判断数据权限，是否可以操作并且做变更
	 * 
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listQR")
	@ResponseBody
	public Object listQR(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = attachmentsetService.listQR(page); // 列出AttachmentSet列表

		page.getPd().put("showChange", "N");
		String staffName = Jurisdiction.getName();

		// 柜体详情没有
		String Cabinet_Assembly_Detail_ID = pd.getString("AssociationID");
		if (StringUtil.isEmpty(Cabinet_Assembly_Detail_ID)) {
			page.getPd().put("showOpt", "N");
		}

		PageData pdData = new PageData();
		pdData.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
		PageData findById = Cabinet_Assembly_DetailService.findById(pdData);
		if (null == findById) {
			page.getPd().put("showOpt", "N");
		} else {
			// 柜体详情存在
			String If_Drawings_Done = findById.getString("If_Drawings_Done");
			if (StringUtil.isNotEmpty(If_Drawings_Done)) {
				// 图纸未完成
				if ("否".equals(If_Drawings_Done)) {
					page.getPd().put("showOpt", "Y");
					String Responsible_Technology_Name = findById.getString("Responsible_Technology_Name");
					if (!staffName.equals(Responsible_Technology_Name)) {
						page.getPd().put("showOpt", "N");
					}

				} else {
					// 图纸完成
					page.getPd().put("showOpt", "N");
					String Responsible_Technology_Name = findById.getString("Responsible_Technology_Name");
					// 我是当前负责技术的
					if (staffName.equals(Responsible_Technology_Name)) {
						// 不可进行增加修改操作 但是能进行变更
						page.getPd().put("showChange", "Y");
					}
				}
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
	@RequestMapping(value = "/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = attachmentsetService.findById(pd); // 根据ID读取
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
			attachmentsetService.deleteAll(ArrayDATA_IDS);
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
		titles.add("数据来源"); // 1
		titles.add("关联ID表"); // 2
		titles.add("关联ID"); // 3
		titles.add("名称"); // 4
		titles.add("路径"); // 5
		titles.add("扩展名"); // 6
		titles.add("类型"); // 7
		titles.add("状态(使用,归档)"); // 8
		titles.add("附件"); // 9
		titles.add("备注"); // 10
		titles.add("创建人"); // 11
		titles.add("创建时间"); // 12
		dataMap.put("titles", titles);
		List<PageData> varOList = attachmentsetService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("DATASOURCES")); // 1
			vpd.put("var2", varOList.get(i).getString("ASSOCIATIONIDTABLE")); // 2
			vpd.put("var3", varOList.get(i).getString("ASSOCIATIONID")); // 3
			vpd.put("var4", varOList.get(i).getString("FNAME")); // 4
			vpd.put("var5", varOList.get(i).getString("FURL")); // 5
			vpd.put("var6", varOList.get(i).getString("FEXTENSION")); // 6
			vpd.put("var7", varOList.get(i).getString("TTYPE")); // 7
			vpd.put("var8", varOList.get(i).getString("FSTATUS")); // 8
			vpd.put("var9", varOList.get(i).getString("FATTACHMENT")); // 9
			vpd.put("var10", varOList.get(i).getString("FEXPLANATION")); // 10
			vpd.put("var11", varOList.get(i).getString("FCREATEPERSONID")); // 11
			vpd.put("var12", varOList.get(i).getString("FCREATETIME")); // 12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 下载
	 * 
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/Dowland")
	public void downExcel(HttpServletResponse response) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> findByAssId = attachmentsetService.findByAssId(pd); // 通过关联ID获取数据
		if (CollectionUtil.isNotEmpty(findByAssId)) {
			pd = findByAssId.get(0);
			String FFILEPATH = pd.getString("FUrl"); // 文件路径
			String FFILENAME = pd.getString("FName"); // 文件名称
			String FExtension = pd.getString("FExtension");
			FFILENAME = FFILENAME + '.' + FExtension;
			FileDownload.fileDownload(response, PathUtil.getProjectpath() + FFILEPATH, FFILENAME);
		}
	}

	/**
	 * 下载（AttachmentSet_ID）
	 * 
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/Downland")
	public void Downland(HttpServletResponse response) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = attachmentsetService.findById(pd); // 通过关联ID获取数据
		String FFILEPATH = pd.getString("FUrl"); // 文件路径
		String FFILENAME = pd.getString("FName"); // 文件名称
		String FExtension = pd.getString("FExtension");
		FFILENAME = FFILENAME + '.' + FExtension;
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + FFILEPATH, FFILENAME);
	}

}
