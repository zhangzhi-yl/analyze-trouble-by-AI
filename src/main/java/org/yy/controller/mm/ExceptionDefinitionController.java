package org.yy.controller.mm;

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
import org.activiti.engine.impl.el.JuelExpression;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.ExceptionDefinitionService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：异常定义
 * 作者：范贺男
 * 时间：2020-11-07
 */
@Controller
@RequestMapping("/ExceptionDefinition")
public class ExceptionDefinitionController extends BaseController {
	
	@Autowired
	private ExceptionDefinitionService ExceptionDefinitionService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OperationRecordService operationrecordService;
	/**保存
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("ExceptionDefinition:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdStaff = new PageData();	
		pd = this.getPageData();
		pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
		pd.put("ExceptionDefinition_ID", this.get32UUID());		//主键
		pd.put("IfStartUsing", 0);								//是否起用，0 未启用 1 启用 默认未启用
		pd.put("FMakeBillsPersoID",pdStaff.get("STAFF_ID"));			//制单人 STAFF_ID
		pd.put("FTime", Tools.date2Str(new Date())); //制单时间
		ExceptionDefinitionService.save(pd);
		operationrecordService.add("","异常定义","添加",pd.getString("ExceptionDefinition_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	/**获取数据字典中的异常类型
	 * @param 无参数
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value="/getExceptionType")
	@ResponseBody
	public Object getExceptionType() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		List<PageData> varList = ExceptionDefinitionService.getExceptionType(pd); //获取数据字典中的异常状态
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("ExceptionDefinition:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ExceptionDefinitionService.delete(pd);
		operationrecordService.add("","异常定义","删除",pd.getString("ExceptionDefinition_ID"),"","");//操作日志
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 * 只修改个别字段， 指定给人或部门、异常状态、异常定义、异常描述、初始指定人或部门
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("ExceptionDefinition:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ExceptionDefinitionService.edit(pd);
		operationrecordService.add("","异常定义","修改",pd.getString("ExceptionDefinition_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("ExceptionDefinition:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KeyWords1 = pd.getString("KEYWORD1S");						//关键词检索条件：移交给人或部门
		if(Tools.notEmpty(KeyWords1))pd.put("KeyWords1", KeyWords1.trim());
		String KeyWords2 = pd.getString("KeyWords2");						//关键词检索条件：适用异常类型
		if(Tools.notEmpty(KeyWords2))pd.put("KeyWords2", KeyWords2.trim());
		String KeyWords3 = pd.getString("KeyWords3");						//关键词检索条件：是否启用
		if(Tools.notEmpty(KeyWords3))pd.put("KeyWords3", KeyWords3.trim());
		String KeyWords4 = pd.getString("KeyWords4");						//关键词检索条件：初始处理部门
		if(Tools.notEmpty(KeyWords4))pd.put("KeyWords4", KeyWords4.trim());
		String KeyWords5 = pd.getString("KeyWords5");						//关键词检索条件：初始处理人
		if(Tools.notEmpty(KeyWords5))pd.put("KeyWords5", KeyWords5.trim());
		page.setPd(pd);
		List<PageData>	varList = ExceptionDefinitionService.list(page);	//列出ExceptionDefinition列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("ExceptionDefinition:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KeyWords1 = pd.getString("KEYWORD1S");						//关键词检索条件：移交给人或部门
		if(Tools.notEmpty(KeyWords1))pd.put("KeyWords1", KeyWords1.trim());
		String KeyWords2 = pd.getString("KeyWords2");						//关键词检索条件：适用异常类型
		if(Tools.notEmpty(KeyWords2))pd.put("KeyWords2", KeyWords2.trim());
		String KeyWords3 = pd.getString("KeyWords3");						//关键词检索条件：是否启用
		if(Tools.notEmpty(KeyWords3))pd.put("KeyWords3", KeyWords3.trim());
		String KeyWords4 = pd.getString("KeyWords4");						//关键词检索条件：初始处理部门
		if(Tools.notEmpty(KeyWords4))pd.put("KeyWords4", KeyWords4.trim());
		String KeyWords5 = pd.getString("KeyWords5");						//关键词检索条件：初始处理人
		if(Tools.notEmpty(KeyWords5))pd.put("KeyWords5", KeyWords5.trim());
		List<PageData>	varList = ExceptionDefinitionService.listAll(pd);	//列出ExceptionDefinition列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
//	@RequiresPermissions("ExceptionDefinition:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ExceptionDefinitionService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	/**启用异常状态
	 * @param ExceptionDefinition_ID 主键ID
	 */
	@RequestMapping(value="/toStartUsing")
	@ResponseBody
	public Object toStartUsing() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ExceptionDefinitionService.findById(pd);	//根据ID读取
		String StartUsing = pd.get("IfStartUsing").toString();//获取启用状态  0 未启用 1 启用
		if(StartUsing.equals("停用")){//判断当前状态
			ExceptionDefinitionService.toStartUsing(pd);//将状态码修改为1
		}else{
			errInfo = "failure";
		}
		operationrecordService.add("","异常定义","启用",pd.getString("ExceptionDefinition_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	/**停用异常状态
	 * @param ExceptionDefinition_ID 主键ID
	 */
	@RequestMapping(value="/toEndUsing")
	@ResponseBody
	public Object toEndUsing() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ExceptionDefinitionService.findById(pd);	//根据ID读取
		String StartUsing = pd.get("IfStartUsing").toString();//获取启用状态  0 未启用 1 启用
		if(StartUsing.equals("启用")){//判断当前状态
			System.out.println("pd:"+pd);
			ExceptionDefinitionService.toEndUsing(pd);//将状态码修改为0
		}else{
			errInfo = "failure";
		}
		operationrecordService.add("","异常定义","停用",pd.getString("ExceptionDefinition_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("ExceptionDefinition:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ExceptionDefinitionService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		operationrecordService.add("","异常定义","批量删除",DATA_IDS,"","");//操作日志
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
//	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("异常定义ID");	//1
		titles.add("是否起用");	//2
		titles.add("移交给人或部门");	//3
		titles.add("适用异常类型");	//4
		titles.add("异常定义");	//5
		titles.add("异常定义描述");	//6
		titles.add("初始处理部门或人");	//7
		titles.add("制单人");	//8
		titles.add("制单时间");	//9
		dataMap.put("titles", titles);
		String KeyWords1 = pd.getString("KEYWORD1S");						//关键词检索条件：移交给人或部门
		if(Tools.notEmpty(KeyWords1))pd.put("KeyWords1", KeyWords1.trim());
		String KeyWords2 = pd.getString("KeyWords2");						//关键词检索条件：适用异常类型
		if(Tools.notEmpty(KeyWords2))pd.put("KeyWords2", KeyWords2.trim());
		String KeyWords3 = pd.getString("KeyWords3");						//关键词检索条件：是否启用
		if(Tools.notEmpty(KeyWords3))pd.put("KeyWords3", KeyWords3.trim());
		String KeyWords4 = pd.getString("KeyWords4");						//关键词检索条件：初始处理部门
		if(Tools.notEmpty(KeyWords4))pd.put("KeyWords4", KeyWords4.trim());
		String KeyWords5 = pd.getString("KeyWords5");						//关键词检索条件：初始处理人
		if(Tools.notEmpty(KeyWords5))pd.put("KeyWords5", KeyWords5.trim());
		List<PageData> varOList = ExceptionDefinitionService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ExceptionDefinition_ID"));	    //1
			vpd.put("var2", varOList.get(i).get("IfStartUsing").toString());	//2
			vpd.put("var3", varOList.get(i).get("FOperator").toString());	//3
			vpd.put("var4", varOList.get(i).getString("ExceptionType"));	    //4
			vpd.put("var5", varOList.get(i).getString("ExceptionDefinition"));	    //5
			vpd.put("var6", varOList.get(i).getString("FExplanation"));	    //6
			vpd.put("var7", varOList.get(i).getString("InitialOperatorID"));	    //7
			vpd.put("var8", varOList.get(i).getString("FMakeBillsPersoID"));	    //8
			vpd.put("var9", varOList.get(i).getString("FTime"));	    //9
			varList.add(vpd);
		}
		operationrecordService.add("","异常定义","导出",pd.getString("ExceptionDefinition_ID"),"","");//操作日志
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
