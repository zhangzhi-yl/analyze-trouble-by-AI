package org.yy.controller.mdm;

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
import org.yy.service.mdm.EQM_CULTURE_PLANService;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mdm.EQM_BASEService;
import org.yy.service.mdm.EQM_CULTURE_PLANMxService;

/** 
 * 说明：保修保养计划
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_culture_plan")
public class EQM_CULTURE_PLANController extends BaseController {
	
	@Autowired
	private EQM_CULTURE_PLANService eqm_culture_planService;
	
	@Autowired
	private EQM_CULTURE_PLANMxService eqm_culture_planmxService;
	@Autowired
	private EQM_BASEService eqm_baseService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private DepartmentService departmentService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_culture_plan:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_CULTURE_PLAN_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		pd.put("COMPILING_PERSON",Jurisdiction.getName());//编制人
		pd.put("COMPLETE_WHETHER","NO");//是否完成
		eqm_culture_planService.save(pd);
		pd = eqm_culture_planService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		map.put("pd", pd);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_culture_plan:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			eqm_culture_planService.delete(pd);
			eqm_culture_planmxService.deletePlan(pd);//通过主表主键删除数据
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_culture_plan:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_culture_planService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_culture_plan:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = eqm_culture_planService.list(page);	//列出EQM_CULTURE_PLAN列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**带办任务列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listTask")
	//@RequiresPermissions("eqm_culture_plan:list")
	@ResponseBody
	public Object listTask(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("USERNAME", Jurisdiction.getName());//当前登录人
		pd.put("DEPTNAME", staffService.getDEPTNAME(pd).getString("DNAME"));//当前登录人部门
		page.setPd(pd);
		List<PageData> varList = eqm_culture_planService.listTask(page);	//列出EQM_CULTURE_PLAN列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("eqm_culture_plan:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_culture_planService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	/**去新增页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	//@RequiresPermissions("eqm_culture_plan:edit")
	@ResponseBody
	public Object goAdd()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData planPd = new PageData();
		pd = this.getPageData();
		pd = eqm_baseService.findById(pd);	//根据基础资料ID读取相关信息
		if(null!=pd) {
			planPd.put("EQM_BASE_ID", pd.getString("EQM_BASE_ID"));//设备基础资料ID
			planPd.put("EQM_NAME", pd.getString("FNAME")!=null?pd.getString("FNAME"):"");//设备名称
			planPd.put("EQM_CODE", pd.getString("FIDENTIFY")!=null?pd.getString("FIDENTIFY"):"");//设备编号
			planPd.put("EQM_TYPE", pd.getString("CLASS_NAME")!=null?pd.getString("CLASS_NAME"):"");//设备类名
		}
		map.put("pd", planPd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	/**获取用户表数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getUser")
	@ResponseBody
	public Object getUser() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> roleList = staffService.listAll(pd);		//列出所有系统用户
		map.put("roleList", roleList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	/**获取部门表数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getDept")
	@ResponseBody
	public Object getDept() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> roleList = departmentService.listAll(pd);		//列出所有系统用户
		map.put("roleList1", roleList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**更新发布状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editState")
	//@RequiresPermissions("eqm_culture_plan:edit")
	@ResponseBody
	public Object editState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_culture_planService.editState(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**更新完成状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editOver")
	//@RequiresPermissions("eqm_culture_plan:edit")
	@ResponseBody
	public Object editOver() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_culture_planService.editOver(pd);
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
		titles.add("设备类型");	//1
		titles.add("计划开始时间");	//2
		titles.add("计划结束时间");	//3
		titles.add("设备名称");	//4
		titles.add("设备代码");	//5
		titles.add("保修期限");	//6
		titles.add("是否完成");	//7
		titles.add("执行日期");	//8
		titles.add("编制人");	//9
		titles.add("保修执行人");	//10
		titles.add("审核人");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_culture_planService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_TYPE"));	    //1
			vpd.put("var2", varOList.get(i).getString("BEGIN_TIME"));	    //2
			vpd.put("var3", varOList.get(i).getString("END_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("EQM_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("EQM_CODE"));	    //5
			vpd.put("var6", varOList.get(i).getString("FWARRANTY"));	    //6
			vpd.put("var7", varOList.get(i).getString("COMPLETE_WHETHER"));	    //7
			vpd.put("var8", varOList.get(i).getString("EXECUTE_DATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("COMPILING_PERSON"));	    //9
			vpd.put("var10", varOList.get(i).getString("WARRANTY_EXECUTOR"));	    //10
			vpd.put("var11", varOList.get(i).getString("FREVIEWER"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
