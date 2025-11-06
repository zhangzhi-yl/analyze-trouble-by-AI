package org.yy.controller.mdm;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mdm.EQM_OVERHAULMXService;
import org.yy.service.mdm.EQM_OVERHAULService;

/** 
 * 说明：设备检修计划
 * 作者：YuanYe
 * 时间：2020-02-18
 * 
 */
@Controller
@RequestMapping("/eqm_overhaul")
public class EQM_OVERHAULController extends BaseController {
	
	@Autowired
	private EQM_OVERHAULService eqm_overhaulService;
	@Autowired
	private EQM_OVERHAULMXService eqm_overhaulmxService;
	@Autowired
	private StaffService staffService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_overhaul:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_OVERHAUL_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		eqm_overhaulService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_overhaul:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_overhaulService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_overhaul:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_overhaulService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**更新发布状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editState")
	@ResponseBody
	public Object editState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_overhaulService.editState(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_overhaul:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_overhaulService.list(page);	//列出EQM_OVERHAUL列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**每日检修待办列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/dayList")
	//@RequiresPermissions("eqm_overhaul:list")
	@ResponseBody
	public Object dayList(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("FTYPE", "每日");
		List<PageData>	varListDay=eqm_overhaulService.listAll(pd);
		if(varListDay.size()>0) {
			for (int i = 0; i < varListDay.size(); i++) {
				PageData pdDay = varListDay.get(i);
				PageData sumPd = eqm_overhaulmxService.findByCount(pdDay);
				if(Integer.parseInt(sumPd.get("FSUM").toString())==0) {
					PageData savePd = new PageData();
					savePd.put("EQM_OVERHAULMX_ID",  this.get32UUID());//设备检修计划反馈主键
					savePd.put("EQM_OVERHAUL_ID", pdDay.getString("EQM_OVERHAUL_ID"));//设备检修计划主键
					savePd.put("FTYPE", pdDay.getString("FUPKEEP_TYPE"));//类型
					savePd.put("FDATE", Tools.date2Str(new Date(),"yyyy-MM-dd"));//日期
					savePd.put("FCREATOR", Jurisdiction.getName());//创建人
					savePd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
					savePd.put("FEXTEND1", "NO");//是否完成
					eqm_overhaulmxService.save(savePd);
				}
			}
		}
		pd.put("USERNAME", Jurisdiction.getName());//当前登录人
		pd.put("DEPTNAME", staffService.getDEPTNAME(pd).getString("DNAME"));//当前登录人部门
		page.setPd(pd);
		List<PageData>	varList = eqm_overhaulService.dayList(page);	//列出EQM_OVERHAUL列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**每月检修待办列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/monthList")
	//@RequiresPermissions("eqm_overhaul:list")
	@ResponseBody
	public Object monthList(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("FTYPE", "每月");
		List<PageData>	varListDay=eqm_overhaulService.listAll(pd);
		if(varListDay.size()>0) {
			for (int i = 0; i < varListDay.size(); i++) {
				PageData pdDay = varListDay.get(i);
				PageData sumPd = eqm_overhaulmxService.findByCount(pdDay);
				if(Integer.parseInt(sumPd.get("FSUM").toString())==0) {
					PageData savePd = new PageData();
					savePd.put("EQM_OVERHAULMX_ID",  this.get32UUID());//设备检修计划反馈主键
					savePd.put("EQM_OVERHAUL_ID", pdDay.getString("EQM_OVERHAUL_ID"));//设备检修计划主键
					savePd.put("FTYPE", pdDay.getString("FUPKEEP_TYPE"));//类型
					savePd.put("FDATE", Tools.date2Str(new Date(),"yyyy-MM-dd"));//日期
					savePd.put("FCREATOR", Jurisdiction.getName());//创建人
					savePd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
					savePd.put("FEXTEND1", "NO");//是否完成
					eqm_overhaulmxService.save(savePd);
				}
			}
		}
		pd.put("USERNAME", Jurisdiction.getName());//当前登录人
		pd.put("DEPTNAME", staffService.getDEPTNAME(pd).getString("DNAME"));//当前登录人部门
		page.setPd(pd);
		List<PageData>	varList = eqm_overhaulService.monthList(page);	//列出EQM_OVERHAUL列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**每年检修待办列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/yearList")
	//@RequiresPermissions("eqm_overhaul:list")
	@ResponseBody
	public Object yearList(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("FTYPE", "每年");
		List<PageData>	varListDay=eqm_overhaulService.listAll(pd);
		if(varListDay.size()>0) {
			for (int i = 0; i < varListDay.size(); i++) {
				PageData pdDay = varListDay.get(i);
				PageData sumPd = eqm_overhaulmxService.findByCount(pdDay);
				if(Integer.parseInt(sumPd.get("FSUM").toString())==0) {
					PageData savePd = new PageData();
					savePd.put("EQM_OVERHAULMX_ID",  this.get32UUID());//设备检修计划反馈主键
					savePd.put("EQM_OVERHAUL_ID", pdDay.getString("EQM_OVERHAUL_ID"));//设备检修计划主键
					savePd.put("FTYPE", pdDay.getString("FUPKEEP_TYPE"));//类型
					savePd.put("FDATE", Tools.date2Str(new Date(),"yyyy-MM-dd"));//日期
					savePd.put("FCREATOR", Jurisdiction.getName());//创建人
					savePd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
					savePd.put("FEXTEND1", "NO");//是否完成
					eqm_overhaulmxService.save(savePd);
				}
			}
		}
		pd.put("USERNAME", Jurisdiction.getName());//当前登录人
		pd.put("DEPTNAME", staffService.getDEPTNAME(pd).getString("DNAME"));//当前登录人部门
		page.setPd(pd);
		List<PageData>	varList = eqm_overhaulService.yearList(page);	//列出EQM_OVERHAUL列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("eqm_overhaul:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_overhaulService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_overhaul:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_overhaulService.deleteAll(ArrayDATA_IDS);
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
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("设备基础资料ID");	//1
		titles.add("排序号");	//2
		titles.add("保养类型");	//3
		titles.add("保养项");	//4
		titles.add("排时规则");	//5
		titles.add("创建人");	//6
		titles.add("创建时间");	//7
		titles.add("扩展字段1");	//8
		titles.add("扩展字段2");	//9
		titles.add("扩展字段3");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_overhaulService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_BASE_ID"));	    //1
			vpd.put("var2", varOList.get(i).get("FSORT").toString());	//2
			vpd.put("var3", varOList.get(i).getString("FUPKEEP_TYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FUPKEEP_MATTER"));	    //4
			vpd.put("var5", varOList.get(i).getString("FTIME_RULE"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCREATOR"));	    //6
			vpd.put("var7", varOList.get(i).getString("CREATE_TIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("FEXTEND1"));	    //8
			vpd.put("var9", varOList.get(i).getString("FEXTEND2"));	    //9
			vpd.put("var10", varOList.get(i).getString("FEXTEND3"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
