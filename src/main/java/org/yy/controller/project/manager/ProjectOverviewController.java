package org.yy.controller.project.manager;

import java.util.ArrayList;
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
import org.yy.service.momp.epm.phasetemplate.PhaseTemplateService;
import org.yy.service.momp.epm.plantemplate.PlanTemplateService;
import org.yy.service.project.manager.DPROJECTService;
import org.yy.service.project.manager.DepPlanService;
import org.yy.service.project.manager.EQUIPMENTService;
import org.yy.service.project.manager.Pro_PlanService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.ObjectExcelViewxlsxGantt;
import org.yy.util.Tools;
@Controller
@RequestMapping("/project_overview")
public class ProjectOverviewController extends BaseController{
	@Autowired
	private Pro_PlanService pro_planService;
	@Autowired
	private DPROJECTService projectService;
	@Autowired
	private EQUIPMENTService equipmentService;
	@Autowired
	private PlanTemplateService plantemplateService;
	@Autowired
	private DepPlanService depplanService;
	@Autowired
	private PhaseTemplateService phasetemplateService;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		String KEYWORDS2 = pd.getString("KEYWORDS2");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS2))pd.put("KEYWORDS2", KEYWORDS2.trim());
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		String KEYWORDS4 = pd.getString("KEYWORDS4");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS4))pd.put("KEYWORDS4", KEYWORDS4.trim());
		String KEYWORDS5 = pd.getString("KEYWORDS5");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS5))pd.put("KEYWORDS5", KEYWORDS5.trim());
		String KEYWORDS6 = pd.getString("KEYWORDS6");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS6))pd.put("KEYWORDS6", KEYWORDS6.trim());
		page.setPd(pd);
		List<PageData>	varList = pro_planService.listOverview(page);	//列出Pro_Plan列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**项目总览页获取信息
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getData")
	@ResponseBody
	public Object getData() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = pro_planService.getData(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	/**项目进度视角-计划变更记录
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listChange")
	//@RequiresPermissions("planchange:list")
	@ResponseBody
	public Object listChange(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = pro_planService.listChange(page);	//列出PLANCHANGE列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**部门项目视角列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listDept")
	@ResponseBody
	public Object listDept(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("FDEPTNAME", pdDept.get("FDEPTNAME"));
			pd.put("FDEPT", pdDept.get("DEPARTMENT_ID"));
		}
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		String KEYWORDS2 = pd.getString("KEYWORDS2");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS2))pd.put("KEYWORDS2", KEYWORDS2.trim());
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		String KEYWORDS4 = pd.getString("KEYWORDS4");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS4))pd.put("KEYWORDS4", KEYWORDS4.trim());
		String KEYWORDS5 = pd.getString("KEYWORDS5");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS5))pd.put("KEYWORDS5", KEYWORDS5.trim());
		String KEYWORDS6 = pd.getString("KEYWORDS6");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS6))pd.put("KEYWORDS6", KEYWORDS6.trim());
		page.setPd(pd);
		List<PageData>	varList = pro_planService.listDept(page);	
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**人员项目视角列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listPerson")
	@ResponseBody
	public Object listPerson(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("FDEPTNAME", pdDept.get("FDEPTNAME"));
			pd.put("FDEPT", pdDept.get("DEPARTMENT_ID"));
		}
		
		//pd.put("ISALL", Jurisdiction.getUserISALL());
		//pd.put("ISBZ", Jurisdiction.getUserSFLD());
		pd.put("Guan", Jurisdiction.getName());
		//pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_IDNEW());
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		String KEYWORDS2 = pd.getString("KEYWORDS2");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS2))pd.put("KEYWORDS2", KEYWORDS2.trim());
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		String KEYWORDS4 = pd.getString("KEYWORDS4");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS4))pd.put("KEYWORDS4", KEYWORDS4.trim());
		String KEYWORDS5 = pd.getString("KEYWORDS5");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS5))pd.put("KEYWORDS5", KEYWORDS5.trim());
		String KEYWORDS6 = pd.getString("KEYWORDS6");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS6))pd.put("KEYWORDS6", KEYWORDS6.trim());
		page.setPd(pd);
		List<PageData>	deptList = pro_planService.getDepts(pd);//获得部门
		List<PageData>	userList = pro_planService.getUsers(pd);//获得人员
		List<PageData>	varList = pro_planService.listPerson(page);
		map.put("varList", varList);
		map.put("userList", userList);
		map.put("deptList", deptList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**项目总览-导出到excel
	 * @param KEYWORDS1~6
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("序号");		//1
		titles.add("项目号");		//2
		titles.add("项目名称");	//3
		titles.add("设备号");		//4
		titles.add("设备名称");	//5
		titles.add("Type");		//6
		titles.add("台数");		//7
		titles.add("客户名称");	//8
		titles.add("合同签订日期");	//9
		titles.add("合同金额");	//10
		titles.add("交货期");		//11
		titles.add("交货地点");	//12
		titles.add("当前阶段");	//13
		titles.add("机械主设计人");	//14
		titles.add("电气主设计人");	//15
		titles.add("项目经理");	//16
		titles.add("项目合同金额");	//17
		titles.add("执行状态");	//18
		titles.add("当前状态");	//19
		dataMap.put("titles", titles);
		List<PageData> varOList = pro_planService.excellist(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", Integer.valueOf(i + 1).toString()); // 1
			vpd.put("var2", varOList.get(i).getString("EPROJECT_CODE")); // 2
			vpd.put("var3", varOList.get(i).getString("EPROJECT_NAME")); // 3
			vpd.put("var4", varOList.get(i).getString("EEQM_NO")); // 4
			vpd.put("var5", varOList.get(i).getString("EEQM_NAME")); // 5
			vpd.put("var6", varOList.get(i).getString("ETYPE")); // 6
			vpd.put("var7", varOList.get(i).get("ESET_NUMBER").toString()); // 7台数
			vpd.put("var8", varOList.get(i).getString("ECUSTOMER_NAME")); // 8
			vpd.put("var9", varOList.get(i).getString("ESIGN_DATE")); // 9
			vpd.put("var10", varOList.get(i).get("ECONTRACT_AMOUNT").toString());//10合同金额
			vpd.put("var11", varOList.get(i).getString("EDELIVERY_DATE")); // 11
			vpd.put("var12", varOList.get(i).getString("EDELIVERY_PLACE")); // 12
			vpd.put("var13", varOList.get(i).getString("FHTNAME")); // 13
			vpd.put("var14", varOList.get(i).getString("EENGINEER_DESIGN")); // 14
			vpd.put("var15", varOList.get(i).getString("EELECTRIC_DESIGN")); // 15
			vpd.put("var16", varOList.get(i).getString("EPROJECT_MANAGER")); // 16
			vpd.put("var17", varOList.get(i).get("ECONTRACT_AMOUNT").toString());//17项合同金额
			vpd.put("var18", varOList.get(i).getString("RUN_STATE"));//18
			vpd.put("var19", varOList.get(i).getString("NOW_PLAN_TYPE"));//19
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**部门项目视角-导出到excel
	 * @param KEYWORDS1~6
	 * @throws Exception
	 */
	@RequestMapping(value="/excelListDept")
	@RequiresPermissions("toExcel")
	public ModelAndView excelListDept() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("FDEPTNAME", pdDept.get("FDEPTNAME"));
			pd.put("FDEPT", pdDept.get("DEPARTMENT_ID"));
		}
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("序号");		//1
		titles.add("项目编号");	//2
		titles.add("项目名称");	//3
		titles.add("设备号");		//4
		titles.add("设备名称");	//5
		titles.add("Type");		//6
		titles.add("台数");		//7
		titles.add("客户名称");	//8
		titles.add("合同签订日期");	//9
		titles.add("合同交货期");	//10
		titles.add("交货地点");	//11
		titles.add("所属阶段");	//12
		titles.add("负责人");		//13
		titles.add("启动计划开始时间");	//14
		titles.add("启动计划结束时间");	//15
		titles.add("实际开始时间");		//16
		titles.add("实际结束时间");		//17
		titles.add("最新计划开始时间");	//18
		titles.add("最新计划结束时间");	//19
		titles.add("执行状态");		//20
		titles.add("当前状态");		//21
		dataMap.put("titles", titles);
		List<PageData> varOList = pro_planService.excelListDept(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", Integer.valueOf(i + 1).toString()); 			// 1
			vpd.put("var2", varOList.get(i).getString("PPROJECT_CODE")); 	// 2
			vpd.put("var3", varOList.get(i).getString("PPROJECT_NAME")); 	// 3
			vpd.put("var4", varOList.get(i).getString("EEQM_NO")); 			// 4
			vpd.put("var5", varOList.get(i).getString("EEQM_NAME")); 		// 5
			vpd.put("var6", varOList.get(i).getString("EETYPE")); 			// 6
			vpd.put("var7", varOList.get(i).get("ESET_NUMBER").toString()); // 7台数
			vpd.put("var8", varOList.get(i).getString("ECUSTOMER_NAME")); 	// 8
			vpd.put("var9", varOList.get(i).getString("ESIGN_DATE")); 		// 9
			vpd.put("var10", varOList.get(i).get("EDELIVERY_DATE"));		//10
			vpd.put("var11", varOList.get(i).getString("EDELIVERY_PLACE")); 	// 11
			vpd.put("var12", varOList.get(i).getString("FHTNAME")); 			// 12
			vpd.put("var13", varOList.get(i).getString("FMANAGER")); 			// 13
			vpd.put("var14", varOList.get(i).getString("PLAN_START_TIME")); 	// 14
			vpd.put("var15", varOList.get(i).getString("PLAN_END_TIME")); 		// 15
			vpd.put("var16", varOList.get(i).getString("REAL_SATRT_TIME")); 	// 16
			vpd.put("var17", varOList.get(i).get("REAL_END_TIME"));				// 17
			vpd.put("var18", varOList.get(i).getString("NEW_PLAN_START_TIME"));	// 18
			vpd.put("var19", varOList.get(i).getString("NEW_PLAN_END_TIME"));	// 19
			vpd.put("var20", varOList.get(i).getString("RUNTYPE"));				// 20
			vpd.put("var21", varOList.get(i).getString("NOW_PLAN_TYPE"));		// 21
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**人员项目视角-excel导出
	 * @param KEYWORDS1~6
	 * @throws Exception
	 */
	@RequestMapping(value="/excelListPerson")
	@RequiresPermissions("toExcel")
	public ModelAndView excelListPerson() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd=this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("FDEPTNAME", pdDept.get("FDEPTNAME"));
			pd.put("FDEPT", pdDept.get("DEPARTMENT_ID"));
		}		
		//pd.put("ISALL", Jurisdiction.getUserISALL());
		//pd.put("ISBZ", Jurisdiction.getUserSFLD());
		pd.put("Guan", Jurisdiction.getName());
		//pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_IDNEW());
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		String KEYWORDS2 = pd.getString("KEYWORDS2");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS2))pd.put("KEYWORDS2", KEYWORDS2.trim());
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		String KEYWORDS4 = pd.getString("KEYWORDS4");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS4))pd.put("KEYWORDS4", KEYWORDS4.trim());
		String KEYWORDS5 = pd.getString("KEYWORDS5");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS5))pd.put("KEYWORDS5", KEYWORDS5.trim());
		String KEYWORDS6 = pd.getString("KEYWORDS6");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS6))pd.put("KEYWORDS6", KEYWORDS6.trim());
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("序号");		//1
		titles.add("项目编号");	//2
		titles.add("项目名称");	//3
		titles.add("设备号");		//4
		titles.add("设备名称");	//5
		titles.add("Type");		//6
		titles.add("台数");		//7
		titles.add("客户名称");	//8
		titles.add("合同签订日期");	//9
		titles.add("合同交货期");	//10
		titles.add("交货地点");	//11
		titles.add("所属阶段");	//12
		titles.add("所属任务");	//13
		titles.add("负责人");		//14
		titles.add("启动计划开始时间");	//15
		titles.add("启动计划结束时间");	//16
		titles.add("实际开始时间");		//17
		titles.add("实际结束时间");		//18
		titles.add("最新计划开始时间");	//19
		titles.add("最新计划结束时间");	//20
		titles.add("完成任务进度");		//21
		titles.add("执行状态");		//22
		titles.add("当前状态");		//23
		dataMap.put("titles", titles);
		List<PageData> varOList = pro_planService.excelListPerson(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", Integer.valueOf(i + 1).toString()); 			// 1
			vpd.put("var2", varOList.get(i).getString("EPROJECT_CODE")); 	// 2
			vpd.put("var3", varOList.get(i).getString("EPROJECT_NAME")); 	// 3
			vpd.put("var4", varOList.get(i).getString("EEQM_NO")); 			// 4
			vpd.put("var5", varOList.get(i).getString("EEQM_NAME")); 		// 5
			vpd.put("var6", varOList.get(i).getString("EETYPE")); 			// 6
			vpd.put("var7", varOList.get(i).get("ESET_NUMBER").toString()); // 7台数
			vpd.put("var8", varOList.get(i).getString("ECUSTOMER_NAME")); 	// 8
			vpd.put("var9", varOList.get(i).getString("ESIGN_DATE")); 		// 9
			vpd.put("var10", varOList.get(i).getString("EDELIVERY_DATE"));		//10
			vpd.put("var11", varOList.get(i).getString("EDELIVERY_PLACE")); 	// 11
			vpd.put("var12", varOList.get(i).getString("FHTNAME")); 			// 12
			vpd.put("var13", varOList.get(i).getString("STAFFPLAN_NAME")); 		// 13
			vpd.put("var14", varOList.get(i).getString("STAFFPLAN_MANAGER")); 	// 14
			vpd.put("var15", varOList.get(i).getString("PLAN_START_TIME")); 	// 15
			vpd.put("var16", varOList.get(i).getString("PLAN_END_TIME")); 		// 16
			vpd.put("var17", varOList.get(i).getString("REAL_SATRT_TIME"));		// 17
			vpd.put("var18", varOList.get(i).getString("REAL_END_TIME"));		// 18
			vpd.put("var19", varOList.get(i).getString("NEW_PLAN_START_TIME"));	// 19
			vpd.put("var20", varOList.get(i).getString("NEW_PLAN_END_TIME"));
			vpd.put("var21", varOList.get(i).get("PTFINISHPROGRESS").toString());
			vpd.put("var22", varOList.get(i).getString("STAFFPLAN_TYPE"));		
			vpd.put("var23", varOList.get(i).getString("NOW_PLAN_TYPE"));		
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

	 /**
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getUsers")
	@ResponseBody
	public Object getUsers() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("FDEPTNAME", pdDept.get("FDEPTNAME"));
			pd.put("FDEPT", pdDept.get("DEPARTMENT_ID"));
		}
		//pd.put("ISALL", Jurisdiction.getUserISALL());
		//pd.put("ISBZ", Jurisdiction.getUserSFLD());
		pd.put("Guan", Jurisdiction.getName());
		List<PageData>	userList = pro_planService.getUsers(pd);//获得人员
		map.put("userList", userList);
		map.put("result", errInfo);
		return map;
	}
	 /**导出甘特图到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/exportExcelGantt")
	public ModelAndView exportExcelGantt() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		pd = this.getPageData();
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1))dataMap.put("KEYWORDS1", KEYWORDS1.trim());
		String KEYWORDS2 = pd.getString("KEYWORDS2");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS2))dataMap.put("KEYWORDS2", KEYWORDS2.trim());
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))dataMap.put("KEYWORDS3", KEYWORDS3.trim());
		String KEYWORDS4 = pd.getString("KEYWORDS4");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS4))dataMap.put("KEYWORDS4", KEYWORDS4.trim());
		String KEYWORDS5 = pd.getString("KEYWORDS5");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS5))dataMap.put("KEYWORDS5", KEYWORDS5.trim());
		String KEYWORDS6 = pd.getString("KEYWORDS6");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS6))dataMap.put("KEYWORDS6", KEYWORDS6.trim());
		
		String KEYWORDS7 = pd.getString("KEYWORDS7");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS7))dataMap.put("KEYWORDS7", KEYWORDS7.trim());
		String KEYWORDS8 = pd.getString("KEYWORDS8");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS8))dataMap.put("KEYWORDS8", KEYWORDS8.trim());
		String KEYWORDS9 = pd.getString("KEYWORDS9");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS9))dataMap.put("KEYWORDS9", KEYWORDS9.trim());
		String KEYWORDS10 = pd.getString("KEYWORDS10");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS10))dataMap.put("KEYWORDS10", KEYWORDS10.trim());
		String KEYWORDS11 = pd.getString("KEYWORDS11");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS11))dataMap.put("KEYWORDS11", KEYWORDS11.trim());
		String KEYWORDS12 = pd.getString("KEYWORDS12");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS12))dataMap.put("KEYWORDS12", KEYWORDS12.trim());
		String KEYWORDS13 = pd.getString("KEYWORDS13");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS13))dataMap.put("KEYWORDS13", KEYWORDS13.trim());
		String KEYWORDS14 = pd.getString("KEYWORDS14");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS14))dataMap.put("KEYWORDS14", KEYWORDS14.trim());
		String KEYWORDS15 = pd.getString("KEYWORDS15");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS15))dataMap.put("KEYWORDS15", KEYWORDS15.trim());
		String KEYWORDS16 = pd.getString("KEYWORDS16");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS16))dataMap.put("KEYWORDS16", KEYWORDS16.trim());
		String KEYWORDS17 = pd.getString("KEYWORDS17");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS17))dataMap.put("KEYWORDS17", KEYWORDS17.trim());
		String KEYWORDS18 = pd.getString("KEYWORDS18");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS18))dataMap.put("KEYWORDS18", KEYWORDS18.trim());
		
		String result="200";
		//String PROPRESOLUTION_ID=pd.getString("PROPRESOLUTION_ID");
		//dataMap.put("PROPRESOLUTION_ID", PROPRESOLUTION_ID);
		//生成文件并上传
		try{
		    //result = "200";
		}catch (Exception e){
			result = "500";
			
		}finally{
		}
		ObjectExcelViewxlsxGantt erv = new ObjectExcelViewxlsxGantt();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
}
