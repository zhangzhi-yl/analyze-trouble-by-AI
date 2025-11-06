package org.yy.controller.project.manager;

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
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.momp.epm.phasetemplate.PhaseTemplateService;
import org.yy.service.momp.epm.plantemplate.PlanTemplateService;
import org.yy.service.project.manager.DPROJECTService;
import org.yy.service.project.manager.DepPlanService;
import org.yy.service.project.manager.EQUIPMENTService;
import org.yy.service.project.manager.Pro_PlanService;

/** 
 * 说明：项目计划
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-03
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/pro_plan")
public class Pro_PlanController extends BaseController {
	
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
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("pro_plan:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PRO_PLAN_ID", this.get32UUID());	//主键
		pd.put("PLAN_START_TIME", "");//计划开始时间
		pd.put("PLAN_END_TIME", "");//计划结束时间
		pd.put("ECREATER", Jurisdiction.getName());//创建人
		pd.put("ECREATTIME", Tools.date2Str(new Date()));//创建时间
		pd.put("ETYPE", "未下发");//下发状态
		pd.put("VISIABLE", "1");
		pd.put("YL2", "新建");//执行状态
		pd.put("NEW_PLAN_START_TIME", pd.getString("PLAN_START_TIME"));
		pd.put("NEW_PLAN_END_TIME", pd.getString("PLAN_END_TIME"));
		List<PageData>	varList1 = plantemplateService.listAll(pd);	//
		pd.put("YL1", varList1.get(0).get("FPTNAME"));
		pd.put("NOW_PLAN_TYPE", "");
		
		List<PageData> EQUList = pro_planService.listEQU(pd);
		if(EQUList.size() >0){//判断设备是否重复
			errInfo = "exception";
		}else{
			pro_planService.save(pd);
		}
		
		map.put("result", errInfo);
		List<PageData> varList = phasetemplateService.listAll(pd);//获取计划模板列表,循环添加
		PageData PlanPd = new PageData();
		for (PageData varpd : varList) {
			PlanPd.put("DEPPLAN_ID", this.get32UUID());
			PlanPd.put("PRO_PLAN_ID", pd.get("PRO_PLAN_ID"));
			PlanPd.put("PHASETEMPLATE_ID", varpd.get("PHASETEMPLATE_ID"));
			PlanPd.put("FHTNAME", varpd.get("FHTNAME"));
			PlanPd.put("FHTLEVEL", varpd.get("FHTLEVEL"));
			PlanPd.put("RUNTYPE", "新建");
			PlanPd.put("FTYPE", "未下发");
			PlanPd.put("ETYPE", "未下发");
			PlanPd.put("PLAN_START_TIME", "");
			PlanPd.put("PLAN_END_TIEM", "");
			PlanPd.put("PLAN_HOURS", "");
			PlanPd.put("VISIABLE", "1");
			PlanPd.put("NEW_PLAN_START_TIME", "");
			PlanPd.put("NEW_PLAN_END_TIME", "");
			PlanPd.put("NOW_PLAN_TYPE", "");
			PlanPd.put("FSOURCE", "模板生成");
			depplanService.save(PlanPd);
		}
		return map;
	}
	@RequestMapping(value="/goXiafa")
	@ResponseBody
	public Object goXiafa() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData Newpd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = depplanService.listAll(pd);
		int a = 0;
		for (int i = 0; i < varList.size(); i++) {
			String FMANAGER = varList.get(i).get("FMANAGER").toString();
			String PLAN_START_TIME = varList.get(i).get("PLAN_START_TIME").toString();
			String PLAN_END_TIME = varList.get(i).get("PLAN_END_TIME").toString();
			String PLAN_HOURS = varList.get(i).get("PLAN_HOURS").toString();
			if(FMANAGER.equals("") || FMANAGER == ""){
				a++;
			}
			if(PLAN_START_TIME.equals("") || PLAN_START_TIME == ""){
				a++;
			}
			if(PLAN_END_TIME.equals("") || PLAN_END_TIME == ""){
				a++;
			}
			if(PLAN_HOURS.equals("") || PLAN_HOURS == ""){
				a++;
			}
		}
		if(a <= 0){
			pd.put("ETYPE", "已下发");
			pro_planService.updateType(pd);
			pro_planService.goXiafa(pd);
		}else{
			errInfo = "exception";
		}
		map.put("result", errInfo);
		return map;
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("pro_plan:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pro_planService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("pro_plan:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pro_planService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("pro_plan:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());

		// 查询项目经理是我自己的
		pd.put("PPROJECT_MANAGER", Jurisdiction.getName());
		page.setPd(pd);	
		
		List<PageData>	varList = pro_planService.list(page);	//列出Pro_Plan列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**项目列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = projectService.listAll(pd);	//列出项目列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	/**设备列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAllEqm")
	@ResponseBody
	public Object listAllEqm() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = equipmentService.listAll(pd);	//列出项目列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	/**计划模板列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAllPlan")
	@ResponseBody
	public Object listAllPlan() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = plantemplateService.listAll(pd);	//
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("pro_plan:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = pro_planService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("pro_plan:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			pro_planService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("项目ID");	//1
		titles.add("设备ID");	//2
		titles.add("计划ID");	//3
		titles.add("计划开始时间");	//4
		titles.add("计划结束时间");	//5
		titles.add("状态");	//6
		titles.add("创建人");	//7
		titles.add("创建时间");	//8
		titles.add("修改人");	//9
		titles.add("修改时间");	//10
		titles.add("预留1");	//11
		titles.add("预留2");	//12
		titles.add("预留3");	//13
		titles.add("预留4");	//14
		titles.add("预留5");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = pro_planService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PROJECT_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQUIPMENT_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("PLANTEMPLATE_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("PLAN_START_TIME"));	    //4
			vpd.put("var5", varOList.get(i).getString("PLAN_END_TIME"));	    //5
			vpd.put("var6", varOList.get(i).getString("ETYPE"));	    //6
			vpd.put("var7", varOList.get(i).getString("ECREATER"));	    //7
			vpd.put("var8", varOList.get(i).getString("ECREATTIME"));	    //8
			vpd.put("var9", varOList.get(i).getString("EDITUSER"));	    //9
			vpd.put("var10", varOList.get(i).getString("EDITTIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("YL1"));	    //11
			vpd.put("var12", varOList.get(i).getString("YL2"));	    //12
			vpd.put("var13", varOList.get(i).getString("YL3"));	    //13
			vpd.put("var14", varOList.get(i).getString("YL4"));	    //14
			vpd.put("var15", varOList.get(i).getString("YL5"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
