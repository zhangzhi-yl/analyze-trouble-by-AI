package org.yy.controller.project.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mbase.MAT_DESIGNService;
import org.yy.service.mbase.MAT_LOGISTICService;
import org.yy.service.mbase.MAT_QUALITYService;
import org.yy.service.mbase.MAT_SPECService;
import org.yy.service.project.manager.Cabinet_AssemblyService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.service.project.manager.DPROJECTService;
import org.yy.service.project.manager.Pro_PlanService;
import org.yy.service.system.UsersService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

import com.alibaba.druid.util.StringUtils;
import com.beust.jcommander.internal.Lists;

/**
 * 说明：装配表 作者：YuanYes QQ356703572 时间：2021-04-30 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/Cabinet_Assembly")
public class Cabinet_AssemblyController extends BaseController {

	@Autowired
	private Cabinet_AssemblyService cabinetAssemblyService;
	@Autowired
	private Cabinet_Assembly_DetailService cabinetAssemblyDetailService;
	@Autowired
	private Pro_PlanService proPlanService;
	@Autowired
	private DPROJECTService dprojectService;
	@Autowired
	private CodingRulesService codingRulesService;
	@Autowired
	private Cabinet_BOMService cabinet_BOMService;

	@Autowired
	private NoticeService noticeService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private MAT_DESIGNService mat_designService;// 设计资料
	@Autowired
	private MAT_LOGISTICService mat_logisticService;// 物料
	@Autowired
	private MAT_QUALITYService mat_qualityService;// 质量资料
	@Autowired
	private StaffService StaffService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private MAT_SPECService MAT_SPECService;// 物料规格

	@Autowired
	private DPROJECTService projectService;
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
			pd.put("Cabinet_Assembly_ID", this.get32UUID()); // 主键
			pd.put("FCTIME", Tools.date2Str(new Date()));
			pd.put("FCREATOR", Jurisdiction.getName()); // 主键
			PageData pdPro = projectService.findById(pd);
			if (pdPro != null) {
				pd.put("Technical_Officer", pdPro.getString("Responsible_Technology"));
				pd.put("Project_End_Date", pdPro.getString("PDELIVERY_DATE"));
			}
			cabinetAssemblyService.save(pd);
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
			List<PageData> listAll = cabinetAssemblyDetailService.listAll(pd);
			List<PageData> remaindDetailList = Lists.newArrayList();

			for (PageData pageData : listAll) {
				if ("否".equals(pageData.getString("Load_Status"))) {
					String Cabinet_Assembly_Detail_ID = pageData.getString("Cabinet_Assembly_Detail_ID");
					PageData detailParam = new PageData();
					detailParam.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
					List<PageData> bomList = cabinet_BOMService.listAll(detailParam);
					for (PageData bom : bomList) {
						cabinet_BOMService.delete(bom);
					}
					cabinetAssemblyDetailService.delete(pageData);
				} else {
					remaindDetailList.add(pageData);
					continue;
				}
			}

			if (CollectionUtil.isNotEmpty(remaindDetailList)) {
				map.put("result", "failed");
				map.put("msg", "不允许删除，已经下发车间共" + remaindDetailList.size() + "条柜体数据，");
				return map;
			}
			cabinetAssemblyService.delete(pd);

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
			cabinetAssemblyService.edit(pd);
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
	 * 一键生成柜体 v2 管悦 20210802 自动产成品代码同步
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/generateCabinet")
	@ResponseBody
	public Object generateCabinet() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> listByAssemblyID = cabinetAssemblyDetailService.listAll(pd);
			int num = 0;
			PageData findById = cabinetAssemblyService.findById(pd);
			if (null == findById) {
				map.put("result", "failed");
				map.put("msg", "主信息为空");
				return map;
			}
			if (null != findById) {

				String numStr = findById.getString("Cabinet_Count");
				if (StringUtils.isEmpty(numStr)) {
					map.put("result", "failed");
					map.put("msg", "主信息柜体数量为空，不能一键生成，请编辑");
					return map;
				}
				if (CollectionUtil.isNotEmpty(listByAssemblyID)) {
					if (listByAssemblyID.size() >= Integer.valueOf(findById.getString("Cabinet_Count"))) {
						map.put("result", "failed");
						map.put("msg", "柜体数量已经达到标准，不能超过规定数量");
						return map;
					}
					numStr = String.valueOf(Integer.valueOf(numStr) - listByAssemblyID.size());
				}
				num = Integer.valueOf(numStr);
			}

			if (0 == num) {
				map.put("result", "failed");
				map.put("msg", "主信息柜体数量为0，不能一键生成，请编辑");
				return map;
			}

			String PRO_PLAN_ID = findById.getString("PRO_PLAN_ID");
			PageData proPlanParam = new PageData();
			proPlanParam.put("PRO_PLAN_ID", PRO_PLAN_ID);
			PageData proPlan = proPlanService.findById(proPlanParam);

			/*
			 * if (null == proPlan) { map.put("result", "failed"); map.put("msg", "项目为空");
			 * return map; }
			 */

			// String PROJECT_ID = proPlan.getString("PROJECT_ID");
			String PROJECT_ID = findById.getString("PRO_PLAN_ID");
			PageData dtProjectParam = new PageData();
			dtProjectParam.put("PROJECT_ID", PROJECT_ID);
			PageData dtProject = dprojectService.findById(dtProjectParam);

			if (null == dtProject) {
				map.put("result", "failed");
				map.put("msg", "计划为空");
				return map;
			}

			String PPROJECT_CODE = "";
			String code = dtProject.getString("PPROJECT_CODE");
			if (!StringUtils.isEmpty(code)) {
				PPROJECT_CODE = code;
			}

			String days = DateUtil.getDays().substring(2, 6);
			// 柜体编号为 项目号 + 日期 + 5位流水号
			for (int i = 0; i < num; i++) {
				String Cabinet_No = new StringBuilder().append(PPROJECT_CODE).append(days)
						.append(codingRulesService.getRuleNumByRuleType("PMSDETAIL")).toString();
				PageData detail = new PageData();
				detail.put("Cabinet_Assembly_Detail_ID", this.get32UUID());
				detail.put("Cabinet_Assembly_ID", pd.getString("Cabinet_Assembly_ID"));
				detail.put("Cabinet_No", Cabinet_No);
				detail.put("Cabinet_Aliases", findById.getString("Cabinet_Type") + "-" + Cabinet_No);
				detail.put("If_Bom_Done", "否");
				detail.put("If_Drawings_Done", "否");
				detail.put("If_Sync_Done", "否");
				detail.put("If_Purchase_Done", "否");
				detail.put("FStatus", "创建");
				detail.put("Load_Status", "否");
				detail.put("PROJECT_ID", PROJECT_ID);
				detail.put("PRO_PLAN_ID", PRO_PLAN_ID);
				detail.put("FDESIGNHOUR", 0); // 设计工时
				detail.put("FDIVISIONHOUR", 0); // 分料工时
				detail.put("FASSEMBLYHOUR", 0); // 装配工时
				detail.put("FWIRINGHOUR", 0); // 接线工时
				detail.put("FEXAMINATIONHOUR", 0); // 上电工时
				detail.put("FFINALINSPECTIONHOUR", 0); // 终检工时
				detail.put("FPACKINGHOUR", 0); // 包装工时
				detail.put("TOTALHOUR", 0); // 总工时
				detail.put("Responsible_Technology", findById.getString("Technical_Officer"));
				cabinetAssemblyDetailService.save(detail);
				// 产成品代码同步
				PageData pData = new PageData();
				pData.put("Cabinet_Assembly_Detail_ID", detail.getString("Cabinet_Assembly_Detail_ID"));
				PageData findByIdx = Cabinet_Assembly_DetailService.findById(pData);

				// 别名必须填写
				if (StringUtils.isEmpty(findByIdx.getString("Cabinet_Aliases"))) {
					map.put("result", "failed");
					map.put("msg", "柜体别名不能为空");
					return map;
				}

				findByIdx.put("If_Sync_Done", "是");
				// 新插入物料信息表 设置默认值

				// 判断当前柜体编号是否存在？存在略过:不存在继续插入
				List<PageData> listByMatCode = mat_basicService.getListByMatCode(findByIdx.getString("Cabinet_No"));
				if (!CollectionUtils.isEmpty(listByMatCode)) {
					continue;
				}

				PageData matInsertParam = new PageData();
				matInsertParam.put("MAT_CLASS_ID", "afafa0d2cf3e4f69ad197101552c3837");
				matInsertParam.put("MAT_CODE", findByIdx.getString("Cabinet_No"));
				matInsertParam.put("MAT_NAME", findByIdx.getString("Cabinet_Aliases"));
				matInsertParam.put("MAT_ATTRIBUTE", "e8a2897878cd49dcbaba908ca55e84f5");
				matInsertParam.put("MAT_MAIN_UNIT", "234eaab3f5824032bccfc78dea24e0e6");
				matInsertParam.put("MAT_AUXILIARY_UNIT", "234eaab3f5824032bccfc78dea24e0e6");
				matInsertParam.put("MAT_SPECS", "");
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
				Cabinet_Assembly_DetailService.edit(findByIdx);

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
	 * 添加一个柜体
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/generateOneCabinet")
	@ResponseBody
	public Object generateOneCabinet() throws Exception {
		Map<String, Object> map = new HashMap<>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			int num = 0;
			PageData findById = cabinetAssemblyService.findById(pd);
			if (null != findById) {

				String numStr = findById.getString("Cabinet_Count");
				if (StringUtils.isEmpty(numStr)) {
					map.put("result", "failed");
					map.put("msg", "主信息柜体数量为空，不能一键生成，请编辑");
					return map;
				}
				num = 1;
			}
			List<PageData> listByAssemblyID = cabinetAssemblyDetailService.listAll(pd);
			// 获取柜体数量，如果已经生成柜体的 不让重新生成，需都删除后重新下发，已下发的不能重新生成

			if (CollectionUtil.isNotEmpty(listByAssemblyID)) {
				if (listByAssemblyID.size() >= Integer.valueOf(findById.getString("Cabinet_Count"))) {
					map.put("result", "failed");
					map.put("msg", "柜体数量已经达到标准，不能超过规定数量");
					return map;
				}
			}

			if (0 == num) {
				map.put("result", "failed");
				map.put("msg", "主信息柜体数量为0，不能一键生成，请编辑");
				return map;
			}

			String PRO_PLAN_ID = findById.getString("PRO_PLAN_ID");
			PageData proPlanParam = new PageData();
			proPlanParam.put("PRO_PLAN_ID", PRO_PLAN_ID);
			PageData proPlan = proPlanService.findById(proPlanParam);
			/*
			 * if (null == proPlan) { map.put("result", "failed"); map.put("msg", "项目为空");
			 * return map; }
			 */

			// String PROJECT_ID = proPlan.getString("PROJECT_ID");
			String PROJECT_ID = findById.getString("PRO_PLAN_ID");
			PageData dtProjectParam = new PageData();
			dtProjectParam.put("PROJECT_ID", PROJECT_ID);
			PageData dtProject = dprojectService.findById(dtProjectParam);

			if (null == dtProject) {
				map.put("result", "failed");
				map.put("msg", "计划为空");
				return map;
			}

			String PPROJECT_CODE = "";
			String code = dtProject.getString("PPROJECT_CODE");
			if (!StringUtils.isEmpty(code)) {
				PPROJECT_CODE = code;
			}

			String days = DateUtil.getDays().substring(2, 6);
			// 柜体编号为 项目号 + 日期 + 5位流水号
			for (int i = 0; i < num; i++) {
				String Cabinet_No = PPROJECT_CODE + days + codingRulesService.getRuleNumByRuleType("PMSDETAIL");
				PageData detail = new PageData();
				detail.put("Cabinet_Assembly_Detail_ID", this.get32UUID());
				detail.put("Cabinet_Assembly_ID", pd.getString("Cabinet_Assembly_ID"));
				detail.put("Cabinet_No", Cabinet_No);
				detail.put("Cabinet_Aliases", findById.getString("Cabinet_Type") + "-" + Cabinet_No);
				detail.put("If_Bom_Done", "否");
				detail.put("If_Drawings_Done", "否");
				detail.put("If_Sync_Done", "否");
				detail.put("If_Purchase_Done", "否");
				detail.put("FStatus", "创建");
				detail.put("Load_Status", "否");
				detail.put("PROJECT_ID", PROJECT_ID);
				detail.put("PRO_PLAN_ID", PRO_PLAN_ID);
				cabinetAssemblyDetailService.save(detail);
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
	 * 一键下发 V1吴双双 20210531下发提醒
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/onKeyDispatch")
	@ResponseBody
	public Object onKeyDispatch() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> listAll = cabinetAssemblyDetailService.listAll(pd);
			PageData pdMain = cabinetAssemblyService.findById(pd);
			if (StringUtils.isEmpty(pdMain.getString("Estimate_Start_Date"))) {
				map.put("result", "failed");
				map.put("msg", "存在没有计划开始日期的柜体数据，请编辑柜体详情");
				return map;
			}
			if (StringUtils.isEmpty(pdMain.getString("Project_End_Date"))) {
				map.put("result", "failed");
				map.put("msg", "存在没有计划结束日期的柜体数据，请编辑柜体详情");
				return map;
			}
			if (CollectionUtil.isEmpty(listAll)) {
				map.put("result", "failed");
				map.put("msg", "没有符合下发条件的数据");
				return map;
			}
			for (PageData pageData : listAll) {
				if (StringUtils.isEmpty(pageData.getString("Responsible_Technology"))) {
					map.put("result", "failed");
					map.put("msg", "存在没有分配负责技术的柜体数据，请编辑柜体详情");
					return map;
				}
				if (pageData.getString("If_Sync_Done") == null || pageData.getString("If_Sync_Done").equals("否")) {
					map.put("result", "failed");
					map.put("msg", "存在没有产成品代码同步的柜体数据，请编辑柜体详情");
					return map;
				}
				pageData.put("FStatus", "下发");
				pageData.put("Distribute_Time", Tools.date2Str(new Date()));
				cabinetAssemblyDetailService.edit(pageData);

				// 发送技术设计提醒
				PageData findById = cabinetAssemblyDetailService.findById(pageData);
				String PPROJECT_MANAGER = findById.getString("Responsible_Technology");// 根据key值，获取接收人主键
				if (StringUtils.isEmpty(PPROJECT_MANAGER)) { // 若PPROJECT_MANAGER为空，则找不到接收人
					map.put("result", "error");
					return map;
				}

				PageData staffParam = new PageData();
				staffParam.put("STAFF_ID", PPROJECT_MANAGER);

				PageData staffInfo = StaffService.findById(staffParam);// 根据主键，获取接收人数据
				if (null == staffInfo) { // 若staffInfo为空，则找不到接收人
					map.put("result", "error");
					return map;
				}

				PageData pdNotice = new PageData();
				pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
				// 跳转页面
				pdNotice.put("AccessURL",
						"../../../views/projectManager/pro_plan/pro_plan_jssj.html?Cabinet_Assembly_Detail_ID="
								+ pageData.getString("Cabinet_Assembly_Detail_ID"));//
				pdNotice.put("ReadPeople", ",");// 已读人默认空
				pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice.put("TType", "消息推送");// 消息类型
				pdNotice.put("FContent",
						Jurisdiction.getName() + "下发了柜体" + findById.getString("Cabinet_No") + "的设计，请注意查看");// 消息正文
				pdNotice.put("FTitle", "设计任务提醒");// 消息标题
				pdNotice.put("LinkIf", "no");// 是否跳转页面
				pdNotice.put("DataSources", "设计任务提醒");// 数据来源
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
				String WXNR = "【设计任务提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");

				// weChat.sendWeChatMsgText("15004641758|13163419783|ZhaoWenTianXia|joker",
				// "@all", "1000014", WXNR, "0");
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
			String KEYWORDS = pd.getString("KEYWORDS"); // 关键词检索条件
			if (Tools.notEmpty(KEYWORDS)) {
				pd.put("KEYWORDS", KEYWORDS.trim());
			}
			page.setPd(pd);
			List<PageData> varList = cabinetAssemblyService.list(page); // 列出Cabinet_Assembly列表
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
			pd = cabinetAssemblyService.findById(pd); // 根据ID读取
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
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				cabinetAssemblyService.deleteAll(ArrayDATA_IDS);
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
			titles.add("柜体类型"); // 1
			titles.add("柜体数量"); // 2
			titles.add("缓冲期"); // 3
			titles.add("预计开始时间"); // 4
			titles.add("预计结束时间"); // 5
			titles.add("技术负责人"); // 6
			titles.add("计划id"); // 7
			dataMap.put("titles", titles);
			List<PageData> varOList = cabinetAssemblyService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for (int i = 0; i < varOList.size(); i++) {
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("Cabinet_Type")); // 1
				vpd.put("var2", varOList.get(i).getString("Cabinet_Count")); // 2
				vpd.put("var3", varOList.get(i).getString("Buffer_Period")); // 3
				vpd.put("var4", varOList.get(i).getString("Estimate_Start_Date")); // 4
				vpd.put("var5", varOList.get(i).getString("Project_End_Date")); // 5
				vpd.put("var6", varOList.get(i).getString("Technical_Officer")); // 6
				vpd.put("var7", varOList.get(i).getString("PRO_PLAN_ID")); // 7
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
}
