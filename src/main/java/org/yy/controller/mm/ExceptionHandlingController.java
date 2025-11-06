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
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.ExceptionService;
import org.yy.service.mm.ExceptionHandlingService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：异常处理
 * 作者：范贺男
 * 时间：2020-11-10
 */
@Controller
@RequestMapping("/ExceptionHandling")
public class ExceptionHandlingController extends BaseController {
	
	@Autowired
	private ExceptionHandlingService ExceptionHandlingService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private ExceptionService ExceptionService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
//	@RequiresPermissions("ExceptionHandling:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ExceptionHandling_ID", this.get32UUID());	//主键
		ExceptionHandlingService.save(pd);
		operationrecordService.add("","异常处理","添加",pd.getString("ExceptionHandling_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("ExceptionHandling:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ExceptionHandlingService.delete(pd);
		operationrecordService.add("","异常处理","删除",pd.getString("ExceptionHandling_ID"),"","");//操作日志
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("ExceptionHandling:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdStaff = new PageData();
		pd = this.getPageData();
		pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
		pd.put("FOperatorID",pdStaff.get("STAFF_ID"));			//制单人 STAFF_ID
		pd.put("FTime", Tools.date2Str(new Date())); //制单时间
		ExceptionHandlingService.edit(pd);
		operationrecordService.add("","异常处理","修改",pd.getString("ExceptionHandling_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("ExceptionHandling:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = ExceptionHandlingService.list(page);	//列出ExceptionHandling列表
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
	//@RequiresPermissions("ExceptionHandling:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ExceptionHandlingService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("ExceptionHandling:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ExceptionHandlingService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		operationrecordService.add("","异常处理","批量删除",DATA_IDS,"","");//操作日志
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
		titles.add("异常ID");	//1
		titles.add("移交给人或部门(1，部门  2，人)");	//2
		titles.add("异常ID");	//3
		titles.add("待处理人或单位");	//4
		titles.add("是否移交处理（1是、0否）");	//5
		titles.add("移交时间");	//6
		titles.add("处理判别(待处理,已处理，移交处理，停滞处理)");	//7
		titles.add("处理描述");	//8
		titles.add("处理人");	//9
		titles.add("处理时间");	//10
		titles.add("处理状态");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = ExceptionHandlingService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ExceptionHandling_ID"));	    //1
			vpd.put("var2", varOList.get(i).get("FOperator").toString());	//2
			vpd.put("var3", varOList.get(i).getString("Exception_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("WaitingOperatorID"));	    //4
			vpd.put("var5", varOList.get(i).getString("IfTurnOver"));	    //5
			vpd.put("var6", varOList.get(i).getString("TurnOverTime"));	    //6
			vpd.put("var7", varOList.get(i).getString("DisposeType"));	    //7
			vpd.put("var8", varOList.get(i).getString("FExplanation"));	    //8
			vpd.put("var9", varOList.get(i).getString("FOperatorID"));	    //9
			vpd.put("var10", varOList.get(i).getString("FTime"));	    //10
			vpd.put("var11", varOList.get(i).getString("FStatus"));	    //11
			varList.add(vpd);
		}
		operationrecordService.add("","异常处理","导出",pd.getString("ExceptionHandling_ID"),"","");//操作日志
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**
	 * 异常移交
	 * @param ExceptionHandling_ID 主键ID Exception_ID 异常ID
	 * @throws Exception
	 */
	@RequestMapping(value="/goTurnOver")
	@ResponseBody
	public Object goTurnOver() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();	
		PageData pdStaff = new PageData();	
		PageData pdReorder = new PageData();	
		PageData NewPd = new PageData();
		pd = this.getPageData();
		try{
			pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
			pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
			pd.put("FOperatorID",pdStaff.get("STAFF_ID"));			//处理人 STAFF_ID
			pd.put("FTime", Tools.date2Str(new Date()));//处理时间
			pd.put("TurnOverTime", Tools.date2Str(new Date()));//移交时间
			ExceptionHandlingService.EditDisposeType(pd);//修改本条异常处理记录的异常判别及移交时间
			
			///////////////////修改判别状态的同时，在异常记录中新增一条异常处理记录
			pdReorder = ExceptionHandlingService.getReorder(pd);//获取最大序号
			Integer Reorder = Integer.parseInt(pdReorder.get("Reorder").toString());//获取最大序号转成Integer方便计算
			int MaxReorder = Reorder+1;//最大序号+1
			NewPd.put("ExceptionHandling_ID", this.get32UUID());//主键ID
			NewPd.put("Exception_ID", pd.get("Exception_ID"));//异常ID
			NewPd.put("Reorder", MaxReorder);//排序序号
			NewPd.put("IfTurnOver", 0);//是否移交处理  默认0 不移交
			NewPd.put("FExplanation", "");
			NewPd.put("FOperator",pd.get("FOperator"));//移交给人
			NewPd.put("WaitingOperatorID", pd.get("ExceptionPendingParty"));//待处理人或部门
			NewPd.put("DisposeType", "待处理");//处理判别，初始默认待处理
			ExceptionHandlingService.save(NewPd);
		}catch(Exception e){
			errInfo = "failure";
		}
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 异常处理完成
	 */
	@RequestMapping(value="/FinishException")
	@ResponseBody
	public Object FinishException() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();	
		PageData pdStaff = new PageData();	
		pd = this.getPageData();
		pd = ExceptionHandlingService.findById(pd);
		pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
		pd.put("FOperatorID",pdStaff.get("STAFF_ID"));			//处理人 STAFF_ID
		pd.put("FTime", Tools.date2Str(new Date()));
		ExceptionHandlingService.FinishException(pd);//修改本条异常处理记录的异常判别及移交时间
		ExceptionService.FinishException(pd);//完成整条异常
		map.put("result", errInfo);
		return map;
	}
}
