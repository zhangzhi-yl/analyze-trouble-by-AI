package org.yy.controller.mdmc;

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
import org.yy.service.mdmc.EQM_MAINTENANCE_PLAN_QUARTERService;
import org.yy.service.mdmc.EQM_MAINTENANCE_PLAN_WEEKService;

/** 
 * 说明：设备保养季计划
 * 作者：YuanYe
 * 时间：2020-06-22
 * 
 */
@Controller
@RequestMapping("/eqm_maintenance_plan_quarter")
public class EQM_MAINTENANCE_PLAN_QUARTERController extends BaseController {
	
	@Autowired
	private EQM_MAINTENANCE_PLAN_QUARTERService eqm_maintenance_plan_quarterService;
	@Autowired
	private EQM_MAINTENANCE_PLAN_WEEKService eqm_maintenance_plan_weekService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("eqm_maintenance_plan_quarter:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdSave = new PageData();
		PageData pdNum=eqm_maintenance_plan_quarterService.getNUM(pd);	//根据选中年份（2020）查找本年创建过季计划数量
		if(null != pdNum && Integer.parseInt(pdNum.get("NUM").toString())!=0) {
			errInfo = "fail1";//选中年份已创建过计划
		}else {
			
		List<PageData>	eqmList =new ArrayList<PageData>();
		PageData pdEqm=new PageData();
		eqmList = eqm_maintenance_plan_weekService.getEqmList(pdEqm);//查设备列表	
		for(int j=0;j<eqmList.size();j++) {
			PageData empPd=eqmList.get(j);
			pdSave.put("EQM_MAINTENANCE_PLAN_QUARTER_ID", this.get32UUID());	//主键
			pdSave.put("FEQM_ID", empPd.getString("EQM_BASE_ID"));	
			pdSave.put("FEQM_CLASS_ID", empPd.getString("EQM_CLASS_ID"));
			pdSave.put("FRECEIPT_NUMBER", pd.getString("year"));//选中年份，2020
			pdSave.put("FWORKSHOP_ID", empPd.getString("USE_DEP"));
			pdSave.put("FEQM_NAME", empPd.getString("FNAME"));
			pdSave.put("FEQM_CODE", empPd.getString("FIDENTIFY"));
			pdSave.put("FMAINTENANCE_CLASS", "三级保养");
			pdSave.put("FCREATOR", Jurisdiction.getName());	//创建人
			pdSave.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
			pdSave.put("LAST_MODIFIER", Jurisdiction.getName());	
			pdSave.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));	
			pdSave.put("FBILLNO", "MAQ"+Tools.date2Str(new Date(),"yyyyMMddHHmmss"));	//编号
			for(int m=1;m<=4;m++) {
				pdSave.put("FMAINTENANCE_COUNT"+m, 0);//保养次数
				pdSave.put("FSTATE"+m, "未保养");//保养状态
			}
			pdSave.put("FEXTEND1", "新增");//创建方式
			eqm_maintenance_plan_quarterService.save(pdSave);
		}
		
		}
		
		
		map.put("result", errInfo);
		return map;
	}
	/**更新
	 * @param KEYWORDS1:选中本单号
	 * @throws Exception
	 */
	@RequestMapping(value="/updateEqm")
	//@RequiresPermissions("eqm_maintenance_plan_month:add")
	@ResponseBody
	public Object updateEqm() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdSave = new PageData();
		PageData pdNum=eqm_maintenance_plan_quarterService.getNUM(pd);	//根据选中年份（2020）查找本年创建过月计划数量
		if(null != pdNum && Integer.parseInt(pdNum.get("NUM").toString())==0) {
			errInfo = "fail1";//选中年份未创建过计划
		}else {			
		List<PageData>	eqmList =new ArrayList<PageData>();
		PageData pdEqm=new PageData();
		pdEqm.put("FTYPE", "QUARTER_UP");//区分计划类型
		pdEqm.put("KEYWORDS1", pd.getString("KEYWORDS1"));//本单号
		eqmList = eqm_maintenance_plan_weekService.getEqmList(pdEqm);//查设备列表	
		for(int j=0;j<eqmList.size();j++) {
			PageData empPd=eqmList.get(j);
			pdSave.put("EQM_MAINTENANCE_PLAN_QUARTER_ID", this.get32UUID());	//主键
			pdSave.put("FEQM_ID", empPd.getString("EQM_BASE_ID"));	
			pdSave.put("FEQM_CLASS_ID", empPd.getString("EQM_CLASS_ID"));
			pdSave.put("FRECEIPT_NUMBER", pd.getString("year"));//选中年份，2020
			pdSave.put("FWORKSHOP_ID", empPd.getString("USE_DEP"));
			pdSave.put("FEQM_NAME", empPd.getString("FNAME"));
			pdSave.put("FEQM_CODE", empPd.getString("FIDENTIFY"));
			pdSave.put("FMAINTENANCE_CLASS", "三级保养");
			pdSave.put("FCREATOR", Jurisdiction.getName());	//创建人
			pdSave.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
			pdSave.put("LAST_MODIFIER", Jurisdiction.getName());	
			pdSave.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));	
			pdSave.put("FBILLNO", "MAQ"+Tools.date2Str(new Date(),"yyyyMMddHHmmss"));	//编号
			for(int m=1;m<=4;m++) {
				pdSave.put("FMAINTENANCE_COUNT"+m, 0);//保养次数
				pdSave.put("FSTATE"+m, "未保养");//保养状态
			}
			pdSave.put("FEXTEND1", "更新");//创建方式
			eqm_maintenance_plan_quarterService.save(pdSave);
		}
		
		}
		map.put("result", errInfo);
		return map;
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("eqm_maintenance_plan_quarter:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_maintenance_plan_quarterService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("eqm_maintenance_plan_quarter:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_maintenance_plan_quarterService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("eqm_maintenance_plan_quarter:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1)) {
			pd.put("KEYWORDS1", KEYWORDS1.trim());
		}else {
			pd.put("KEYWORDS1", Tools.date2Str(new Date(),"yyyy"));	//编号
		}
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_maintenance_plan_quarterService.list(page);	//列出EQM_MAINTENANCE_PLAN_QUARTER列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("eqm_maintenance_plan_quarter:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_maintenance_plan_quarterService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("eqm_maintenance_plan_quarter:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_maintenance_plan_quarterService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备主键");	//1
		titles.add("设备类主键");	//2
		titles.add("本单号");	//3
		titles.add("隶属车间");	//4
		titles.add("设备名称");	//5
		titles.add("设备编码");	//6
		titles.add("保养等级");	//7
		titles.add("FDATE1");	//8
		titles.add("第二季度");	//9
		titles.add("第三季度");	//10
		titles.add("第四季度");	//11
		titles.add("第一季度保养次数");	//12
		titles.add("第二季度保养次数");	//13
		titles.add("第三季度保养次数");	//14
		titles.add("第四季度保养次数");	//15
		titles.add("第一季度保养状态");	//16
		titles.add("第二季度保养状态");	//17
		titles.add("第三季度保养状态");	//18
		titles.add("第四季度保养状态");	//19
		titles.add("备注");	//20
		titles.add("编号");	//21
		titles.add("创建人");	//22
		titles.add("创建时间");	//23
		titles.add("最后修改人");	//24
		titles.add("最后修改时间");	//25
		titles.add("扩展字段1");	//26
		titles.add("扩展字段2");	//27
		titles.add("扩展字段3");	//28
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_maintenance_plan_quarterService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FEQM_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FEQM_CLASS_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FRECEIPT_NUMBER"));	    //3
			vpd.put("var4", varOList.get(i).getString("FWORKSHOP_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("FEQM_NAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("FEQM_CODE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FMAINTENANCE_CLASS"));	    //7
			vpd.put("var8", varOList.get(i).getString("FDATE1"));	    //8
			vpd.put("var9", varOList.get(i).getString("FDATE2"));	    //9
			vpd.put("var10", varOList.get(i).getString("FDATE3"));	    //10
			vpd.put("var11", varOList.get(i).getString("FDATE4"));	    //11
			vpd.put("var12", varOList.get(i).get("FMAINTENANCE_COUNT1").toString());	//12
			vpd.put("var13", varOList.get(i).get("FMAINTENANCE_COUNT2").toString());	//13
			vpd.put("var14", varOList.get(i).get("FMAINTENANCE_COUNT3").toString());	//14
			vpd.put("var15", varOList.get(i).get("FMAINTENANCE_COUNT4").toString());	//15
			vpd.put("var16", varOList.get(i).getString("FSTATE1"));	    //16
			vpd.put("var17", varOList.get(i).getString("FSTATE2"));	    //17
			vpd.put("var18", varOList.get(i).getString("FSTATE3"));	    //18
			vpd.put("var19", varOList.get(i).getString("FSTATE4"));	    //19
			vpd.put("var20", varOList.get(i).getString("FNOTE"));	    //20
			vpd.put("var21", varOList.get(i).getString("FBILLNO"));	    //21
			vpd.put("var22", varOList.get(i).getString("FCREATOR"));	    //22
			vpd.put("var23", varOList.get(i).getString("CREATE_TIME"));	    //23
			vpd.put("var24", varOList.get(i).getString("LAST_MODIFIER"));	    //24
			vpd.put("var25", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //25
			vpd.put("var26", varOList.get(i).getString("FEXTEND1"));	    //26
			vpd.put("var27", varOList.get(i).getString("FEXTEND2"));	    //27
			vpd.put("var28", varOList.get(i).getString("FEXTEND3"));	    //28
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	 /**季计划待选择年份列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getYearList")
	@ResponseBody
	public Object getYearList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//pd.put("FYEAR", Tools.date2Str(new Date(),"yyyy"));	
		List<PageData>	varList = eqm_maintenance_plan_quarterService.getYearList(pd);	
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}	
	/**本单号列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getNoList")
	@ResponseBody
	public Object getNoList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = eqm_maintenance_plan_quarterService.getNoList(pd);	
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}	
	/**设备类列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getClassList")
	@ResponseBody
	public Object getClassList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = eqm_maintenance_plan_quarterService.getClassList(pd);	
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}	
	/**车间列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getWsList")
	@ResponseBody
	public Object getWsList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = eqm_maintenance_plan_quarterService.getWsList(pd);	
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}	
	
	/**三级保养计划执行列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listRun")
	@RequiresPermissions("eqm_maintenance_plan_quarter_run:list")
	@ResponseBody
	public Object listRun(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1)) {
			pd.put("KEYWORDS1", KEYWORDS1.trim());
		}else {
			pd.put("KEYWORDS1", Tools.date2Str(new Date(),"yyyy"));	//编号
		}
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_maintenance_plan_quarterService.list(page);	//列出EQM_MAINTENANCE_PLAN_QUARTER列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}
	/**三级保养计划历史列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listHistory")
	@RequiresPermissions("eqm_maintenance_plan_quarter_history:list")
	@ResponseBody
	public Object listHistory(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1)) {
			pd.put("KEYWORDS1", KEYWORDS1.trim());
		}else {
			pd.put("KEYWORDS1", Tools.date2Str(new Date(),"yyyy"));	//编号
		}
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_maintenance_plan_quarterService.list(page);	//列出EQM_MAINTENANCE_PLAN_QUARTER列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}
}
