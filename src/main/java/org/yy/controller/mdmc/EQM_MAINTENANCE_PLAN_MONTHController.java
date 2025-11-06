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
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.mdmc.EQM_MAINTENANCE_PLAN_MONTHService;
import org.yy.service.mdmc.EQM_MAINTENANCE_PLAN_WEEKService;

/** 
 * 说明：设备保养月计划
 * 作者：YuanYe
 * 时间：2020-06-22
 * 
 */
@Controller
@RequestMapping("/eqm_maintenance_plan_month")
public class EQM_MAINTENANCE_PLAN_MONTHController extends BaseController {
	
	@Autowired
	private EQM_MAINTENANCE_PLAN_MONTHService eqm_maintenance_plan_monthService;
	@Autowired
	private EQM_MAINTENANCE_PLAN_WEEKService eqm_maintenance_plan_weekService;
	@Autowired
	private DepartmentService departmentService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("eqm_maintenance_plan_month:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdSave = new PageData();
		PageData pdNum=eqm_maintenance_plan_monthService.getNUM(pd);	//根据选中年份（2020）查找本年创建过月计划数量
		if(null != pdNum && Integer.parseInt(pdNum.get("NUM").toString())!=0) {
			errInfo = "fail1";//选中年份已创建过计划
		}else {			
		List<PageData>	eqmList =new ArrayList<PageData>();
		PageData pdEqm=new PageData();
		eqmList = eqm_maintenance_plan_weekService.getEqmList(pdEqm);//查设备列表	
		for(int j=0;j<eqmList.size();j++) {
			PageData empPd=eqmList.get(j);
			pdSave.put("EQM_MAINTENANCE_PLAN_MONTH_ID", this.get32UUID());	//主键
			pdSave.put("FEQM_ID", empPd.getString("EQM_BASE_ID"));	
			pdSave.put("FEQM_CLASS_ID", empPd.getString("EQM_CLASS_ID"));
			pdSave.put("FRECEIPT_NUMBER", pd.getString("year"));//选中年份，2020
			pdSave.put("FWORKSHOP_ID", empPd.getString("USE_DEP"));
			pdSave.put("FEQM_NAME", empPd.getString("FNAME"));
			pdSave.put("FEQM_CODE", empPd.getString("FIDENTIFY"));
			pdSave.put("FMAINTENANCE_CLASS", "二级保养");
			pdSave.put("FCREATOR", Jurisdiction.getName());	//创建人
			pdSave.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
			pdSave.put("LAST_MODIFIER", Jurisdiction.getName());	
			pdSave.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));	
			pdSave.put("FBILLNO", "MAM"+Tools.date2Str(new Date(),"yyyyMMddHHmmss"));	//编号
			for(int m=1;m<=12;m++) {
				pdSave.put("FMAINTENANCE_COUNT"+m, 0);//保养次数
				pdSave.put("FSTATE"+m, "未保养");//保养状态
			}
			pdSave.put("FEXTEND1", "新增");//创建方式
			eqm_maintenance_plan_monthService.save(pdSave);
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
		PageData pdNum=eqm_maintenance_plan_monthService.getNUM(pd);	//根据选中年份（2020）查找本年创建过月计划数量
		if(null != pdNum && Integer.parseInt(pdNum.get("NUM").toString())==0) {
			errInfo = "fail1";//选中年份未创建过计划
		}else {			
		List<PageData>	eqmList =new ArrayList<PageData>();
		PageData pdEqm=new PageData();
		pdEqm.put("FTYPE", "MONTH_UP");//区分计划类型
		pdEqm.put("KEYWORDS1", pd.getString("KEYWORDS1"));//本单号
		eqmList = eqm_maintenance_plan_weekService.getEqmList(pdEqm);//查设备列表	
		for(int j=0;j<eqmList.size();j++) {
			PageData empPd=eqmList.get(j);
			pdSave.put("EQM_MAINTENANCE_PLAN_MONTH_ID", this.get32UUID());	//主键
			pdSave.put("FEQM_ID", empPd.getString("EQM_BASE_ID"));	
			pdSave.put("FEQM_CLASS_ID", empPd.getString("EQM_CLASS_ID"));
			pdSave.put("FRECEIPT_NUMBER", pd.getString("year"));//选中年份，2020
			pdSave.put("FWORKSHOP_ID", empPd.getString("USE_DEP"));
			pdSave.put("FEQM_NAME", empPd.getString("FNAME"));
			pdSave.put("FEQM_CODE", empPd.getString("FIDENTIFY"));
			pdSave.put("FMAINTENANCE_CLASS", "二级保养");
			pdSave.put("FCREATOR", Jurisdiction.getName());	//创建人
			pdSave.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
			pdSave.put("LAST_MODIFIER", Jurisdiction.getName());	
			pdSave.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));	
			pdSave.put("FBILLNO", "MAM"+Tools.date2Str(new Date(),"yyyyMMddHHmmss"));	//编号
			for(int m=1;m<=12;m++) {
				pdSave.put("FMAINTENANCE_COUNT"+m, 0);//保养次数
				pdSave.put("FSTATE"+m, "未保养");//保养状态
			}
			pdSave.put("FEXTEND1", "更新");//创建方式
			eqm_maintenance_plan_monthService.save(pdSave);
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
	@RequiresPermissions("eqm_maintenance_plan_month:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_maintenance_plan_monthService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("eqm_maintenance_plan_month:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_maintenance_plan_monthService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("eqm_maintenance_plan_month:list")
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
		List<PageData>	varList = eqm_maintenance_plan_monthService.list(page);	//列出EQM_MAINTENANCE_PLAN_MONTH列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("eqm_maintenance_plan_month:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_maintenance_plan_monthService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("eqm_maintenance_plan_month:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_maintenance_plan_monthService.deleteAll(ArrayDATA_IDS);
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
		titles.add("一月");	//8
		titles.add("二月");	//9
		titles.add("三月");	//10
		titles.add("四月");	//11
		titles.add("五月");	//12
		titles.add("六月");	//13
		titles.add("七月");	//14
		titles.add("八月");	//15
		titles.add("九月");	//16
		titles.add("十月");	//17
		titles.add("十一月");	//18
		titles.add("十二月");	//19
		titles.add("一月保养次数");	//20
		titles.add("二月保养次数");	//21
		titles.add("三月保养次数");	//22
		titles.add("四月保养次数");	//23
		titles.add("五月保养次数");	//24
		titles.add("六月保养次数");	//25
		titles.add("七月保养次数");	//26
		titles.add("八月保养次数");	//27
		titles.add("九月保养次数");	//28
		titles.add("十月保养次数");	//29
		titles.add("十一月保养次数");	//30
		titles.add("十二月保养次数");	//31
		titles.add("一月保养状态");	//32
		titles.add("二月保养状态");	//33
		titles.add("三月保养状态");	//34
		titles.add("四月保养状态");	//35
		titles.add("五月保养状态");	//36
		titles.add("六月保养状态");	//37
		titles.add("七月保养状态");	//38
		titles.add("八月保养状态");	//39
		titles.add("九月保养状态");	//40
		titles.add("十月保养状态");	//41
		titles.add("十一月保养状态");	//42
		titles.add("十二月保养状态");	//43
		titles.add("备注");	//44
		titles.add("编号");	//45
		titles.add("创建人");	//46
		titles.add("创建时间");	//47
		titles.add("最后修改人");	//48
		titles.add("最后修改时间");	//49
		titles.add("扩展字段1");	//50
		titles.add("扩展字段2");	//51
		titles.add("扩展字段3");	//52
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_maintenance_plan_monthService.listAll(pd);
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
			vpd.put("var12", varOList.get(i).getString("FDATE5"));	    //12
			vpd.put("var13", varOList.get(i).getString("FDATE6"));	    //13
			vpd.put("var14", varOList.get(i).getString("FDATE7"));	    //14
			vpd.put("var15", varOList.get(i).getString("FDATE8"));	    //15
			vpd.put("var16", varOList.get(i).getString("FDATE9"));	    //16
			vpd.put("var17", varOList.get(i).getString("FDATE10"));	    //17
			vpd.put("var18", varOList.get(i).getString("FDATE11"));	    //18
			vpd.put("var19", varOList.get(i).getString("FDATE12"));	    //19
			vpd.put("var20", varOList.get(i).get("FMAINTENANCE_COUNT1").toString());	//20
			vpd.put("var21", varOList.get(i).get("FMAINTENANCE_COUNT2").toString());	//21
			vpd.put("var22", varOList.get(i).get("FMAINTENANCE_COUNT3").toString());	//22
			vpd.put("var23", varOList.get(i).get("FMAINTENANCE_COUNT4").toString());	//23
			vpd.put("var24", varOList.get(i).get("FMAINTENANCE_COUNT5").toString());	//24
			vpd.put("var25", varOList.get(i).get("FMAINTENANCE_COUNT6").toString());	//25
			vpd.put("var26", varOList.get(i).get("FMAINTENANCE_COUNT7").toString());	//26
			vpd.put("var27", varOList.get(i).get("FMAINTENANCE_COUNT8").toString());	//27
			vpd.put("var28", varOList.get(i).get("FMAINTENANCE_COUNT9").toString());	//28
			vpd.put("var29", varOList.get(i).get("FMAINTENANCE_COUNT10").toString());	//29
			vpd.put("var30", varOList.get(i).get("FMAINTENANCE_COUNT11").toString());	//30
			vpd.put("var31", varOList.get(i).get("FMAINTENANCE_COUNT12").toString());	//31
			vpd.put("var32", varOList.get(i).getString("FSTATE1"));	    //32
			vpd.put("var33", varOList.get(i).getString("FSTATE2"));	    //33
			vpd.put("var34", varOList.get(i).getString("FSTATE3"));	    //34
			vpd.put("var35", varOList.get(i).getString("FSTATE4"));	    //35
			vpd.put("var36", varOList.get(i).getString("FSTATE5"));	    //36
			vpd.put("var37", varOList.get(i).getString("FSTATE6"));	    //37
			vpd.put("var38", varOList.get(i).getString("FSTATE7"));	    //38
			vpd.put("var39", varOList.get(i).getString("FSTATE8"));	    //39
			vpd.put("var40", varOList.get(i).getString("FSTATE9"));	    //40
			vpd.put("var41", varOList.get(i).getString("FSTATE10"));	    //41
			vpd.put("var42", varOList.get(i).getString("FSTATE11"));	    //42
			vpd.put("var43", varOList.get(i).getString("FSTATE12"));	    //43
			vpd.put("var44", varOList.get(i).getString("FNOTE"));	    //44
			vpd.put("var45", varOList.get(i).getString("FBILLNO"));	    //45
			vpd.put("var46", varOList.get(i).getString("FCREATOR"));	    //46
			vpd.put("var47", varOList.get(i).getString("CREATE_TIME"));	    //47
			vpd.put("var48", varOList.get(i).getString("LAST_MODIFIER"));	    //48
			vpd.put("var49", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //49
			vpd.put("var50", varOList.get(i).getString("FEXTEND1"));	    //50
			vpd.put("var51", varOList.get(i).getString("FEXTEND2"));	    //51
			vpd.put("var52", varOList.get(i).getString("FEXTEND3"));	    //52
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	 /**月计划待选择年份列表
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
		List<PageData>	varList = eqm_maintenance_plan_monthService.getYearList(pd);	
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
		List<PageData>	varList = eqm_maintenance_plan_monthService.getNoList(pd);	
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
		List<PageData>	varList = eqm_maintenance_plan_monthService.getClassList(pd);	
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
		List<PageData>	varList = eqm_maintenance_plan_monthService.getWsList(pd);	
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**月计划执行列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listRun")
	@RequiresPermissions("eqm_maintenance_plan_month_run:list")
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
		//System.out.println(Jurisdiction.getDEPARTMENT_IDS()+Jurisdiction.getDEPARTMENT_ID());
		String deptC=Jurisdiction.getDEPARTMENT_ID();
		String workshop=getDept(deptC);
		if(!workshop.equals("")) {
			pd.put("workshop", workshop);
		}
		List<PageData>	varList = eqm_maintenance_plan_monthService.list(page);	//列出EQM_MAINTENANCE_PLAN_WEEK列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	public String getDept(String dept) throws Exception {
		String key="";
		String dept1="d8dc1efc3cfe42739a3975033caaa142";//一车间
		String dept2="551565d8826b4e998bd86354ba5f0722";//二车间
		String dept3="e1a0f1dbbd444c6ab066fd2214fdffd2";//三车间
		String dept4="6751b4905f8944aeae5e213f5e531eb9";//四车间
		String dept5="96b7b9e8227746898f48e3fcf493ed8e";//五车间
		String dept6="f3ef0baf2f094ff39dcaefc78463ddec";//质量保证部
		String dept7="57088ed6d8a1457eac5f06e336fc8714";//设备操作主管
		String dept8="04db8e793e9045b086aac72ee1b69dfe";//库房主管
		PageData pd1=departmentService.findByIdx(dept1);
		PageData pd2=departmentService.findByIdx(dept2);
		PageData pd3=departmentService.findByIdx(dept3);
		PageData pd4=departmentService.findByIdx(dept4);
		PageData pd5=departmentService.findByIdx(dept5);
		PageData pd6=departmentService.findByIdx(dept6);
		PageData pd7=departmentService.findByIdx(dept7);
		PageData pd8=departmentService.findByIdx(dept8);
		//一车间所有下级部门
		List<PageData> depList1=departmentService.getDEPARTMENT_IDSS(dept1);
		depList1.add(pd1);
		//二车间所有下级部门
		List<PageData> depList2=departmentService.getDEPARTMENT_IDSS(dept2);
		depList2.add(pd2);
		//三车间所有下级部门
		List<PageData> depList3=departmentService.getDEPARTMENT_IDSS(dept3);
		depList3.add(pd3);
		//四车间所有下级部门
		List<PageData> depList4=departmentService.getDEPARTMENT_IDSS(dept4);
		depList4.add(pd4);
		//五车间所有下级部门
		List<PageData> depList5=departmentService.getDEPARTMENT_IDSS(dept5);
		depList5.add(pd5);
		//质量保证部所有下级部门
		List<PageData> depList6=departmentService.getDEPARTMENT_IDSS(dept6);
		depList6.add(pd6);
		//设备操作主管所有下级部门
		List<PageData> depList7=departmentService.getDEPARTMENT_IDSS(dept7);
		depList7.add(pd7);
		//库房主管所有下级部门
		List<PageData> depList8=departmentService.getDEPARTMENT_IDSS(dept8);
		depList8.add(pd8);
		
		if(key.equals("")) {
		for(int i=0;i<depList1.size();i++) {
			if(depList1.get(i).getString("id").equals(dept)) {
				key=dept1;
				break;
			}
		}
		}
		if(key.equals("")) {
			for(int i=0;i<depList2.size();i++) {
				if(depList2.get(i).getString("id").equals(dept)) {
					key=dept2;
					break;
				}
			}
		}
		if(key.equals("")) {
			for(int i=0;i<depList3.size();i++) {
				if(depList3.get(i).getString("id").equals(dept)) {
					key=dept3;
					break;
				}
			}
		}
		if(key.equals("")) {
			for(int i=0;i<depList4.size();i++) {
				if(depList4.get(i).getString("id").equals(dept)) {
					key=dept4;
					break;
				}
			}
		}
		if(key.equals("")) {
			for(int i=0;i<depList5.size();i++) {
				if(depList5.get(i).getString("id").equals(dept)) {
					key=dept5;
					break;
				}
			}
		}
		if(key.equals("")) {
			for(int i=0;i<depList6.size();i++) {
				if(depList6.get(i).getString("id").equals(dept)) {
					key=dept6;
					break;
				}
			}
		}
		if(key.equals("")) {
			for(int i=0;i<depList7.size();i++) {
				if(depList7.get(i).getString("id").equals(dept)) {
					key=dept7;
					break;
				}
			}
		}
		if(key.equals("")) {
			for(int i=0;i<depList8.size();i++) {
				if(depList8.get(i).getString("id").equals(dept)) {
					key=dept8;
					break;
				}
			}
		}
		return key;
	}
	
	/**月计划历史列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listHistory")
	@RequiresPermissions("eqm_maintenance_plan_month_history:list")
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
		List<PageData>	varList = eqm_maintenance_plan_monthService.list(page);	//列出EQM_MAINTENANCE_PLAN_MONTH列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
