package org.yy.controller.project.manager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mbase.MAT_DESIGNService;
import org.yy.service.mbase.MAT_LOGISTICService;
import org.yy.service.mbase.MAT_QUALITYService;
import org.yy.service.mbase.MAT_SPECService;
import org.yy.service.project.manager.CABINET_AUDITService;
import org.yy.service.project.manager.Cabinet_AssemblyService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.service.project.manager.ChangeService;
import org.yy.service.project.manager.DPROJECTService;
import org.yy.service.project.manager.DTPROJECTFILEService;
import org.yy.service.project.manager.EQUIPMENTService;
import org.yy.service.project.manager.PRESALEPLANONEService;
import org.yy.service.project.manager.PRESALEPLANService;
import org.yy.service.project.manager.PRESALEPLANTWOService;
import org.yy.service.project.manager.PROTEAMService;
import org.yy.service.system.UsersService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

/**
 * 说明：项目 作者：YuanYes QQ356703572 时间：2020-09-01 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/dproject")
public class DPROJECTController extends BaseController {

	@Autowired
	private DPROJECTService projectService;
	@Autowired
	private EQUIPMENTService equipmentService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	@Autowired
	private ChangeService ChangeService;
	// 售前方案一级
	@Autowired
	private PRESALEPLANONEService presaleplanoneService;
	// 售前方案二级
	@Autowired
	private PRESALEPLANTWOService presaleplantwoService;
	// 柜体类型汇总表
	@Autowired
	private Cabinet_AssemblyService cabinetAssemblyService;
	// 柜体数据
	@Autowired
	private Cabinet_Assembly_DetailService cabinetAssemblyDetailService;
	@Autowired
	private CodingRulesService codingRulesService;
	// BOM表
	@Autowired
	private Cabinet_BOMService Cabinet_BOMService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private MAT_DESIGNService mat_designService;// 设计资料
	@Autowired
	private MAT_LOGISTICService mat_logisticService;// 物料
	@Autowired
	private MAT_QUALITYService mat_qualityService;// 质量资料
	@Autowired
	private MAT_SPECService MAT_SPECService;// 物料规格.
	@Autowired
	private PROTEAMService proteamService;
	@Autowired
	private PRESALEPLANService presaleplanService;
	@Autowired
	private DTPROJECTFILEService dtprojectfileService;

	@Autowired
	private CABINET_AUDITService cabinet_auditService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 * @版本:v.2 @修改时间:2021-08-26 @修改内容:1.增加选择售前方案功能，售前方案选择后，保存时查询售前方案一级明细柜体数量(按照类型),保存到柜体类型汇总表(PMS_Cabinet_Assembly)中;
	 *         2.依据售前方案ID查询一级柜体明细,在按照一级柜体明细查询二级明细的工时数据，将一级明细的柜体名称、二级明细的工时数据保存到柜体表(PMS_Cabinet_Assembly_Detail)中;
	 *         3.依据售前方案ID查询一级柜体明细,在按照一级柜体明细查询二级明细的BOM数据，将二级明细BOM保存到BOM表(PMS_Cabinet_BOM)中。
	 *         v3 管悦 20210830 增加产成品代码同步
	 * @版本:v.4 @修改时间:2021-09-07 @修改内容:增加获取柜体类型下的假话结束日期，计划开始日期(计算得出) v4 管悦 20210916
	 *         如选择立项申请则反写立项申请立项状态，立项申请附件带出来，立项申请必须上传双章合同附件
	 */
	@RequestMapping(value = "/add")
	// @RequiresPermissions("dproject:add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PRESALEPLAN_ID = pd.getString("PRESALEPLAN_ID");
		if (PRESALEPLAN_ID == null || PRESALEPLAN_ID.equals("")) {
			pd.put("PROJECT_ID", this.get32UUID()); // 主键
			pd.put("PCREATE_TIME", Tools.date2Str(new Date()));
			pd.put("PCREATOR", Jurisdiction.getName());
			pd.put("PLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			pd.put("PLAST_MODIFIER", Jurisdiction.getName());
			pd.put("FSTATE", "未审核");
			pd.put("FEND_STATE", "未结束");

			// pd.put("PDEPARTMENT_ID", Jurisdiction.getDEPARTMENT_IDNEW());
			PageData pdX = projectService.testNo(pd);// 项目编号验重

			if (pdX != null && Integer.parseInt(pdX.get("FNUM").toString()) > 0) {
				errInfo = "fail1";
				map.put("result", errInfo);
				return map;
			} else {
				projectService.save(pd);
			}

			// 物料辅助属性为 planNO 的插入
			PageData auxData = new PageData();
			auxData.put("MAT_AUXILIARYMX_ID", this.get32UUID());
			auxData.put("MAT_AUXILIARYMX_CODE", pd.getString("PPROJECT_CODE"));
			auxData.put("MAT_AUXILIARYMX_NAME", pd.getString("PPROJECT_CODE") + "/项目");
			auxData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
			mat_auxiliarymxService.save(auxData);
		} else {
			PageData pdf = new PageData();
			pdf.put("PROJECT_ID", pd.getString("PRESALEPLAN_ID"));
			pdf.put("FFILETYPE", "双章合同");
			PageData pdFJ = dtprojectfileService.findFJ(pdf);// 根据类型获取项目申请附件
			if (pdFJ != null && Integer.parseInt(pdFJ.get("FNUM").toString()) == 0) {
				errInfo = "fail2";
				map.put("result", errInfo);
				return map;
			}
			pd.put("PROJECT_ID", this.get32UUID()); // 主键
			pd.put("PCREATE_TIME", Tools.date2Str(new Date()));
			pd.put("PCREATOR", Jurisdiction.getName());
			pd.put("PLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			pd.put("PLAST_MODIFIER", Jurisdiction.getName());
			pd.put("FSTATE", "未审核");
			pd.put("FEND_STATE", "未结束");
			// pd.put("PDEPARTMENT_ID", Jurisdiction.getDEPARTMENT_IDNEW());
			PageData pdX = projectService.testNo(pd);// 项目编号验重

			if (pdX != null && Integer.parseInt(pdX.get("FNUM").toString()) > 0) {
				errInfo = "fail1";
				map.put("result", errInfo);
				return map;
			} else {
				projectService.save(pd);
			}
			// 物料辅助属性为 planNO 的插入
			PageData auxData = new PageData();
			auxData.put("MAT_AUXILIARYMX_ID", this.get32UUID());
			auxData.put("MAT_AUXILIARYMX_CODE", pd.getString("PPROJECT_CODE"));
			auxData.put("MAT_AUXILIARYMX_NAME", pd.getString("PPROJECT_CODE") + "/项目");
			auxData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
			mat_auxiliarymxService.save(auxData);
			List<PageData> varList = presaleplanoneService.getCabinByType(pd); // 依据售前方案ID查询一级明细柜体类型汇总信息
			if (varList.size() > 0) {
				PageData pdC = new PageData();
				for (int i = 0; i < varList.size(); i++) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					// 设定格式
					/*
					 * String PDELIVERY_DATE = pd.getString("PDELIVERY_DATE"); //获取结束日期 Date d =
					 * df.parse(PDELIVERY_DATE); //转换成日期格式 String Buffer_Period =
					 * varList.get(i).getString("Buffer_Period"); //获取缓冲期 Integer a =
					 * Integer.parseInt(Buffer_Period); //转换成int String Estimate_Start_Date =
					 * df.format(new Date(d.getTime() - a * 24 * 60 * 60 * 1000)); //计算减去缓冲期获取开始时间
					 */ pdC.put("Cabinet_Assembly_ID", this.get32UUID());
					pdC.put("Cabinet_Type", varList.get(i).getString("FCABINETTYPE"));
					pdC.put("Cabinet_Count", varList.get(i).get("NUM").toString());
					pdC.put("Buffer_Period", varList.get(i).getString("Buffer_Period"));
					pdC.put("PROJECT_ID", pd.getString("PROJECT_ID"));
					pdC.put("PRO_PLAN_ID", pd.getString("PROJECT_ID"));
					pdC.put("FCREATOR", Jurisdiction.getName());
					pdC.put("FCTIME", Tools.date2Str(new Date()));
					/*
					 * pdC.put("Project_End_Date", PDELIVERY_DATE); pdC.put("Estimate_Start_Date",
					 * Estimate_Start_Date);
					 */
					pdC.put("Technical_Officer", pd.get("Responsible_Technology"));
					cabinetAssemblyService.save(pdC);
				}
			}
			List<PageData> varListO = presaleplanoneService.getCabin(pd); // 依据售前方案ID查询一级明细柜体信息
			if (varListO.size() > 0) {
				PageData pdK = new PageData();
				for (int i = 0; i < varListO.size(); i++) {
					PageData pdO = varListO.get(i);
					String Cabinet_Assembly_Detail_ID = this.get32UUID();
					String days = DateUtil.getDays().substring(2, 6);
					String Cabinet_No = new StringBuilder().append(pd.get("PPROJECT_CODE")).append(days)
							.append(codingRulesService.getRuleNumByRuleType("PMSDETAIL")).toString();
					List<PageData> varListT = presaleplantwoService.getHourByID(pdO); // 依据一级明细ID获取二级明细工时数据
					pdK.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
					pdO.put("PROJECT_ID", pd.getString("PROJECT_ID"));
					pdO.put("PRO_PLAN_ID", pd.getString("PROJECT_ID"));
					PageData pdl = cabinetAssemblyService.findByIdAndType(pdO);
					pdK.put("Cabinet_Assembly_ID", pdl.get("Cabinet_Assembly_ID")); // 柜体类型汇总信息ID
					pdK.put("Cabinet_No", Cabinet_No); // 柜体编号
					pdK.put("Cabinet_Aliases", pdO.get("FDESCRIPTION")); // 柜体别名
					if (varListT.size() > 0) {
						DecimalFormat df = new DecimalFormat("#.00");
						String FDESIGNHOUR = varListT.get(0).get("FQUANTITY").toString();// 设计工时
						String FDIVISIONHOUR = varListT.get(1).get("FQUANTITY").toString();// 分料工时
						String FASSEMBLYHOUR = varListT.get(2).get("FQUANTITY").toString();// 装配工时
						String FWIRINGHOUR = varListT.get(3).get("FQUANTITY").toString();// 接线工时
						String FEXAMINATIONHOUR = varListT.get(4).get("FQUANTITY").toString();// 上电工时
						String FFINALINSPECTIONHOUR = varListT.get(5).get("FQUANTITY").toString();// 终检工时
						String FPACKINGHOUR = varListT.get(6).get("FQUANTITY").toString();// 包装工时
						Double TOTALHOUR = Double.parseDouble(FDESIGNHOUR) + Double.parseDouble(FDIVISIONHOUR)
								+ Double.parseDouble(FASSEMBLYHOUR) + Double.parseDouble(FWIRINGHOUR)
								+ Double.parseDouble(FEXAMINATIONHOUR) + Double.parseDouble(FFINALINSPECTIONHOUR)
								+ Double.parseDouble(FPACKINGHOUR);
						pdK.put("FDESIGNHOUR", varListT.get(0).get("FQUANTITY")); // 设计工时
						pdK.put("FDIVISIONHOUR", varListT.get(1).get("FQUANTITY")); // 分料工时
						pdK.put("FASSEMBLYHOUR", varListT.get(2).get("FQUANTITY")); // 装配工时
						pdK.put("FWIRINGHOUR", varListT.get(3).get("FQUANTITY")); // 接线工时
						pdK.put("FEXAMINATIONHOUR", varListT.get(4).get("FQUANTITY")); // 上电工时
						pdK.put("FFINALINSPECTIONHOUR", varListT.get(5).get("FQUANTITY")); // 终检工时
						pdK.put("FPACKINGHOUR", varListT.get(6).get("FQUANTITY")); // 包装工时
						pdK.put("TOTALHOUR", df.format(TOTALHOUR)); // 总工时
					} else {
						pdK.put("FDESIGNHOUR", 0); // 设计工时
						pdK.put("FDIVISIONHOUR", 0); // 分料工时
						pdK.put("FASSEMBLYHOUR", 0); // 装配工时
						pdK.put("FWIRINGHOUR", 0); // 接线工时
						pdK.put("FEXAMINATIONHOUR", 0); // 上电工时
						pdK.put("FFINALINSPECTIONHOUR", 0); // 终检工时
						pdK.put("FPACKINGHOUR", 0); // 包装工时
						pdK.put("TOTALHOUR", 0); // 总工时
					}
					pdK.put("If_Bom_Done", "否");
					pdK.put("If_Drawings_Done", "否");
					pdK.put("If_Sync_Done", "否");
					pdK.put("If_Purchase_Done", "否");
					pdK.put("FStatus", "创建");
					pdK.put("Load_Status", "否");
					pdK.put("PROJECT_ID", pd.getString("PROJECT_ID"));
					pdK.put("Responsible_Technology", pd.get("Responsible_Technology"));
					pdK.put("PRESALEPLANONE_ID", pdO.get("PRESALEPLANONE_ID"));

					cabinetAssemblyDetailService.save(pdK);

					// 产成品代码同步
					pdK.put("If_Sync_Done", "是");
					// 新插入物料信息表 设置默认值
					// 判断当前柜体编号是否存在？存在略过:不存在继续插入
					List<PageData> listByMatCode = mat_basicService.getListByMatCode(pdK.getString("Cabinet_No"));
					if (CollectionUtils.isEmpty(listByMatCode)) {
						// continue;
						PageData matInsertParam = new PageData();
						matInsertParam.put("MAT_CLASS_ID", "afafa0d2cf3e4f69ad197101552c3837");
						matInsertParam.put("MAT_CODE", pdK.getString("Cabinet_No"));
						matInsertParam.put("MAT_NAME", pdK.getString("Cabinet_Aliases"));
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
						cabinetAssemblyDetailService.edit(pdK);
					}

					List<PageData> varListM = presaleplantwoService.listAll(pdO); // 依据一级明细ID获取二级明细BOM
					if (varListM.size() > 0) {
						PageData pdM = new PageData();
						for (int j = 0; j < varListM.size(); j++) {
							pdM.put("Cabinet_BOM_ID", this.get32UUID());
							pdM.put("Cabinet_Assembly_Detail_ID", Cabinet_Assembly_Detail_ID);
							pdM.put("If_Purchase", "否"); // 是否下推
							pdM.put("FSTATE", "正常"); // BOM状态(正常,变更)
							pdM.put("MAT_NAME", varListM.get(j).get("FNAME")); // 物料名称
							// pdM.put("MAT_CODE", ""); //物料编码
							// pdM.put("", ""); //物料分类
							pdM.put("MAT_SPECS", varListM.get(j).get("FORDERNUMBER")); // 物料规格
							pdM.put("MAT_MAIN_UNIT", varListM.get(j).get("FUNIT")); // 物料单位
							pdM.put("MAT_BRAND", varListM.get(j).get("FMANUFACTURER")); // 物料品牌
							pdM.put("BOM_COUNT", varListM.get(j).get("FQUANTITY")); // 物料bom数量
							pdM.put("MAT_CATEGRAY", "柜内"); // 物料种类
							pdM.put("FVersion", Tools.date2Str(new Date(), "yyyy-MM-dd")); // 版本

							List<PageData> matList = mat_basicService.findBySPECS(pdM);
							if (matList.size() == 1) {
								pdM.put("MAT_BASIC_ID", matList.get(0).getString("MAT_BASIC_ID")); // 物料ID
								pdM.put("MAT_CLASS", matList.get(0).getString("FCLASS")); // 物料分类
								pdM.put("MAT_CODE", matList.get(0).getString("MAT_CODE")); // 物料代码
								pdM.put("MAT_NAME", matList.get(0).getString("MAT_NAME"));
								pdM.put("MAT_BRAND", matList.get(0).getString("MAT_BRAND"));
								pdM.put("MAT_MAIN_UNIT", matList.get(0).getString("MAT_MAIN_UNIT_NAME"));
							}
							Cabinet_BOMService.save(pdM);
						}
					}
				}
			}
		}
		// 团队-项目经理
		PageData pdOp = new PageData();
		pdOp.put("NAME", Jurisdiction.getName());
		String FPTSTAFFID1 = usersService.findUser(pdOp).getString("USERNAME");
		pdOp.put("PROTEAM_ID", this.get32UUID());
		pdOp.put("FPTNAME", Jurisdiction.getName());
		pdOp.put("FPTSTAFFID", FPTSTAFFID1);
		pdOp.put("FPTISINNER", "项目经理");
		pdOp.put("EPROJECT_ID", pd.getString("PROJECT_ID"));
		pdOp.put("FCREATOR", Jurisdiction.getName());
		pdOp.put("FCTIME", Tools.date2Str(new Date()));
		proteamService.save(pdOp);
		// 团队-车间主任
		pdOp.put("NAME", "车间主任");
		String FPTSTAFFID3 = usersService.findUser(pdOp).getString("USERNAME");
		pdOp.put("PROTEAM_ID", this.get32UUID());
		pdOp.put("FPTNAME", "车间主任");
		pdOp.put("FPTSTAFFID", FPTSTAFFID3);
		pdOp.put("FPTISINNER", "车间主任");
		pdOp.put("EPROJECT_ID", pd.getString("PROJECT_ID"));
		pdOp.put("FCREATOR", Jurisdiction.getName());
		pdOp.put("FCTIME", Tools.date2Str(new Date()));
		proteamService.save(pdOp);
		if (PRESALEPLAN_ID != null && !PRESALEPLAN_ID.equals("")) {
			// 反写立项申请立项状态
			pd.put("FSTATE_LX", "已立项");
			pd.put("FTIME2", Tools.date2Str(new Date()));
			pd.put("FMAN2", Jurisdiction.getName());
			pd.put("PROJECT_ID", pd.getString("PROJECT_ID"));
			presaleplanService.editLX(pd);
			dtprojectfileService.insert(pd);// 插入附件
		}
		map.put("PROJECT_ID", pd.getString("PROJECT_ID"));
		map.put("result", errInfo);
		return map;
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
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	// @RequiresPermissions("dproject:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// projectService.delete(pd);
		pd.put("PVISIBLE", "0");
		pd.put("EVISIBLE", "0");
		pd.put("FTYPE", "type2");
		projectService.upVisible(pd);// 反写项目删除状态
		equipmentService.upVisible(pd);// 批量反写设备删除状态
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
	// @RequiresPermissions("dproject:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pd.put("PLAST_MODIFIER", Jurisdiction.getName());
		projectService.edit(pd);
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
		pd.put("PPROJECT_MANAGER", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = projectService.list(page); // 列出项目列表
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
	// @RequiresPermissions("dproject:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = projectService.findById(pd); // 根据ID读取
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
	@RequiresPermissions("dproject:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			projectService.deleteAll(ArrayDATA_IDS);
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
		titles.add("项目编号"); // 1
		titles.add("项目名称"); // 2
		titles.add("客户名称"); // 3
		titles.add("j交货地点"); // 4
		titles.add("交货期"); // 5
		titles.add("合同签订日期"); // 6
		titles.add("变更交货期"); // 7
		titles.add("合同金额"); // 8
		titles.add("客户地点"); // 9
		titles.add("项目经理"); // 10
		titles.add("项目负责人"); // 11
		titles.add("备注"); // 12
		titles.add("是否删除"); // 13
		titles.add("创建人"); // 14
		titles.add("创建时间"); // 15
		titles.add("最后修改人"); // 16
		titles.add("最后修改时间"); // 17
		dataMap.put("titles", titles);
		List<PageData> varOList = projectService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PPROJECT_CODE")); // 1
			vpd.put("var2", varOList.get(i).getString("PPROJECT_NAME")); // 2
			vpd.put("var3", varOList.get(i).getString("PCUSTOMER_NAME")); // 3
			vpd.put("var4", varOList.get(i).getString("PDELIVERY_PLACE")); // 4
			vpd.put("var5", varOList.get(i).getString("PDELIVERY_DATE")); // 5
			vpd.put("var6", varOList.get(i).getString("PSIGN_DATE")); // 6
			vpd.put("var7", varOList.get(i).getString("PCHANGE_DATE")); // 7
			vpd.put("var8", varOList.get(i).get("PCONTRACT_AMOUNT").toString()); // 8
			vpd.put("var9", varOList.get(i).getString("PCUSTOMER_PLACE")); // 9
			vpd.put("var10", varOList.get(i).getString("PPROJECT_MANAGER")); // 10
			vpd.put("var11", varOList.get(i).getString("PPROJECT_PRINCIPAL")); // 11
			vpd.put("var12", varOList.get(i).getString("PNOTE")); // 12
			vpd.put("var13", varOList.get(i).getString("PVISIBLE")); // 13
			vpd.put("var14", varOList.get(i).getString("PCREATOR")); // 14
			vpd.put("var15", varOList.get(i).getString("PCREATE_TIME")); // 15
			vpd.put("var16", varOList.get(i).getString("PLAST_MODIFIER")); // 16
			vpd.put("var17", varOList.get(i).getString("PLAST_MODIFIED_TIME")); // 17
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 生成项目号
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/generateNo")
	@ResponseBody
	public Object generateNo() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = projectService.generateNo(pd);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获得人员列表
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStaffList")
	@ResponseBody
	public Object getStaffList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = staffService.listAll(pd);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 2021-07-08 项目自审 v2 管悦 2021-08-30 消息提醒车间主任
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/audit")
	// @RequiresPermissions("dproject:edit")
	@ResponseBody
	public Object audit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = projectService.findById(pd); // 根据ID读取
		pd.put("FSTATE", "已审核");
		projectService.edit(pd);

		pd.put("ROLE_NAME", "车间主任");
		List<PageData> staffList = usersService.listAllByRoleName(pd);

		String ReceivingAuthority = ",";
		for (PageData pdStaffx : staffList) {
			ReceivingAuthority += pdStaffx.getString("USERNAME");
			ReceivingAuthority = ReceivingAuthority + ",";
		}
		// 消息推送
		PageData pdNotice = new PageData();

		// 跳转页面
		pdNotice.put("AccessURL", "../../../views/projectManager/project/project_list.html?NOTICE_TYPE=" + "ALL"
				+ "&PROJECT_ID=" + pd.getString("PROJECT_ID"));//
		pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
		pdNotice.put("ReadPeople", ",");// 已读人默认空
		pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
		pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
		pdNotice.put("TType", "消息推送");// 消息类型
		pdNotice.put("FContent",
				"项目编号为" + pd.getString("PPROJECT_CODE") + "，项目名称为" + pd.getString("PPROJECT_NAME") + "的项目已立项");// 消息正文
		pdNotice.put("FTitle", "立项提醒");// 消息标题
		pdNotice.put("LinkIf", "no");// 是否跳转页面
		pdNotice.put("DataSources", "立项提醒");// 数据来源
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
		String WXNR = "【立项提醒】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间：" + Tools.date2Str(new Date())
				+ "\r\n" + "消息内容：" + pdNotice.get("FContent");
		// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
		// "0");
		weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 2021-07-08 变更
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/change")
	// @RequiresPermissions("dproject:edit")
	@ResponseBody
	public Object change() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pd.put("PLAST_MODIFIER", Jurisdiction.getName());
		projectService.edit(pd);

		PageData pdChange = new PageData();
		pdChange.put("Change_ID", this.get32UUID()); // 主键
		pdChange.put("Relation_ID", pd.getString("PROJECT_ID"));
		pdChange.put("FType", "项目");
		pdChange.put("Change_Duty", pd.getString("Change_Duty"));
		pdChange.put("Change_Description", pd.getString("REMARK"));
		pdChange.put("Change_Date", Tools.date2Str(new Date()));
		pdChange.put("CC", Jurisdiction.getName());
		ChangeService.save(pdChange);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 项目列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getProjectList")
	@ResponseBody
	public Object getProjectList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = projectService.getProjectList(pd); // 列出项目列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 项目结项
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/over")
	// @RequiresPermissions("dproject:edit")
	@ResponseBody
	public Object over() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FEND_TIME", Tools.date2Str(new Date()));
		pd.put("FEND_MAN", Jurisdiction.getName());
		pd.put("FEND_STATE", "结束");
		projectService.over(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210902 项目物料成本对比列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listWLDB")
	@ResponseBody
	public Object listWLDB(Page page) throws Exception {
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
			List<PageData> varList = projectService.listWLDB(page); // 列出Cabinet_Assembly_Detail列表
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
	 * v1 管悦 20210902 单柜体物料成本对比列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listWLDBMX")
	@ResponseBody
	public Object listWLDBMX(Page page) throws Exception {
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
			List<PageData> varList = projectService.listWLDBMX(page); // 列出Cabinet_Assembly_Detail列表
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
	 * v1 管悦 20210902 项目人工成本对比列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listRGDB")
	@ResponseBody
	public Object listRGDB(Page page) throws Exception {
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
			List<PageData> varList = projectService.listRGDB(page); // 列出Cabinet_Assembly_Detail列表
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
	 * v1 管悦 20210902 单柜体人工成本对比列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listRGDBMX")
	@ResponseBody
	public Object listRGDBMX(Page page) throws Exception {
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
			List<PageData> varList = projectService.listRGDBMX(page); // 列出Cabinet_Assembly_Detail列表
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
	 * v1 管悦 20210917 生产排程-项目单条修改排产时间
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/editTimeXM")
	@ResponseBody
	public Object editTimeXM() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pdX = new PageData();
			pdX.put("PlannedBeginTimeXM", pd.getString("PlannedBeginTime"));
			pdX.put("PlannedEndTimeXM", pd.getString("PlannedEndTime"));
			pdX.put("progressXM", pd.getString("progress"));
			pdX.put("PROJECT_ID", pd.getString("PROJECT_ID"));
			projectService.editTimeXM(pdX);
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
	 * v1 管悦 20210929 项目看板-DDP折线图 按年、月、日、周查看项目延时率和正常率
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listDDP")
	@ResponseBody
	public Object listDDP() throws Exception {
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
			List<PageData> varList = projectService.listDDP(pd); // 列出Cabinet_Assembly_Detail列表
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

	/**
	 * v1 管悦 20210929 项目看板-准时交付数量 按年、月、日、周查看准时交货率和准时订单数
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listZSDDS")
	@ResponseBody
	public Object listZSDDS() throws Exception {
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
			List<PageData> varList = projectService.listZSDDS(pd); // 列出Cabinet_Assembly_Detail列表
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

	/**
	 * v1 管悦 20210929 项目看板-准时交付金额 按年、月、日、周查看准时交货率和准时订单金额
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listZSDDJE")
	@ResponseBody
	public Object listZSDDJE() throws Exception {
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
			List<PageData> varList = projectService.listZSDDJE(pd); // 列出Cabinet_Assembly_Detail列表
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

	/**
	 * v1 管悦 20210929 项目看板-财务损失，订单金额*1%*延期天数
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listCWSS")
	@ResponseBody
	public Object listCWSS() throws Exception {
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
			List<PageData> varList = projectService.listCWSS(pd); // 列出Cabinet_Assembly_Detail列表
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

	/**
	 * v1 管悦 20210930 设计看板-设计效率统计
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listSJXL")
	@ResponseBody
	public Object listSJXL() throws Exception {
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
			List<PageData> varList = projectService.listSJXL(pd); // 列出Cabinet_Assembly_Detail列表
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

	/**
	 * v1 管悦 20210930 项目异常结项审批
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/postYCJX")
	@ResponseBody
	public Object postYCJX() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdAudit = new PageData();
		pdAudit.put("CABINET_AUDIT_ID", this.get32UUID()); // 主键
		pdAudit.put("FAUDIT_STATE", "待审核");
		pdAudit.put("FTYPE", "异常结项审核");
		pdAudit.put("CABINET_ASSEMBLY_DETAIL_ID", pd.getString("PROJECT_ID"));
		pdAudit.put("FCREATOR", Jurisdiction.getName()); // 发布人
		pdAudit.put("FCTIME", DateUtil.date2Str(new Date())); // 发布时间
		cabinet_auditService.save(pdAudit);
		PageData old = projectService.findById(pd);
		old.put("FAudit_State", "待审核");
		old.put("FEND_NOTE", pd.getString("FEND_NOTE"));
		projectService.edit(old);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210930 设计看板-技术部月度统计分析 当月的数据
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listJSYDMAIN")
	@ResponseBody
	public Object listJSYDMAIN() throws Exception {
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
			PageData pdMain = projectService.listJSYDMAIN(pd); // 列出Cabinet_Assembly_Detail列表
			map.put("pd", pdMain);
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
	 * v1 管悦 20210930 设计看板-技术部月度统计分析明细 当月的数据
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/listJSYDMX")
	@ResponseBody
	public Object listJSYDMX() throws Exception {
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
			List<PageData> varList = projectService.listJSYDMX(pd); // 列出Cabinet_Assembly_Detail列表
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
