package org.yy.controller.pp;

import java.math.BigDecimal;
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
import org.yy.service.pp.WorkingHoursService;

/** 
 * 说明：工时填报
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-25
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/WorkingHours")
public class WorkingHoursController extends BaseController {
	
	@Autowired
	private WorkingHoursService WorkingHoursService;
	
	@Autowired
	private StaffService staffService; 
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("WorkingHours:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdOp = new PageData();
		pd = this.getPageData();
		pd.put("WorkingHours_ID", this.get32UUID());	//主键
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String FillInThePersonID = staffService.getStaffId(pdOp).getString("STAFF_ID"); //填报人
		pd.put("FillInThePersonID", FillInThePersonID);
		pd.put("ReportingTime", Tools.date2Str(new Date()));
		pd.put("AuditMark", "创建");
		pd.put("GeneratedType", "手动生成");
		WorkingHoursService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("WorkingHours:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WorkingHoursService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("WorkingHours:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WorkingHoursService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**提交审核
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAudit")
	@ResponseBody
	public Object goAudit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("AuditMark", "审批中");
		WorkingHoursService.goAudit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**审核
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/audit")
	@ResponseBody
	public Object audit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdOp = new PageData();
		pd = this.getPageData();
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String Examiners = staffService.getStaffId(pdOp).getString("STAFF_ID"); //填报人
		pd.put("Examiners", Examiners);
		pd.put("AuditTime", Tools.date2Str(new Date()));
		WorkingHoursService.audit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("WorkingHours:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("NAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData>	varList = WorkingHoursService.list(page);	//列出WorkingHours列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**待审批列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAudit")
	//@RequiresPermissions("WorkingHours:list")
	@ResponseBody
	public Object listAudit(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = WorkingHoursService.listAudit(page);	//列出WorkingHours列表
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
	@RequiresPermissions("WorkingHours:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = WorkingHoursService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("WorkingHours:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			WorkingHoursService.deleteAll(ArrayDATA_IDS);
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
		String AuditMark = pd.getString("AuditMark");						//关键词检索条件
		pd.put("AuditMark", AuditMark);
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("工单ID");	//1
		titles.add("任务ID");	//2
		titles.add("任务单件工时");	//3
		titles.add("零件数量");	//4
		titles.add("额定工时");	//5
		titles.add("开始时间");	//6
		titles.add("结束时间");	//7
		titles.add("实际工时");	//8
		titles.add("调整工时");	//9
		titles.add("合计工时");	//10
		titles.add("生成类型(自动，手动)");	//11
		titles.add("填报描述");	//12
		titles.add("填报人");	//13
		titles.add("填报时间");	//14
		titles.add("审核标记");	//15
		titles.add("审核人");	//16
		titles.add("审核时间");	//17
		dataMap.put("titles", titles);
		List<PageData> varOList = WorkingHoursService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PlanningWorkOrder_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("ProcessWorkOrderExample_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("TaskUnitManHour"));	    //3
			vpd.put("var4", varOList.get(i).getString("NumberOfParts"));	    //4
			vpd.put("var5", varOList.get(i).getString("RatedManHour"));	    //5
			vpd.put("var6", varOList.get(i).getString("StartTime"));	    //6
			vpd.put("var7", varOList.get(i).getString("EndTime"));	    //7
			vpd.put("var8", varOList.get(i).getString("ActualManHour"));	    //8
			vpd.put("var9", varOList.get(i).getString("AdjustManHour"));	    //9
			vpd.put("var10", varOList.get(i).getString("TotalManHour"));	    //10
			vpd.put("var11", varOList.get(i).getString("GeneratedType"));	    //11
			vpd.put("var12", varOList.get(i).getString("FillInTheDescription"));	    //12
			vpd.put("var13", varOList.get(i).getString("FillInThePersonID"));	    //13
			vpd.put("var14", varOList.get(i).getString("ReportingTime"));	    //14
			vpd.put("var15", varOList.get(i).getString("AuditMark"));	    //15
			vpd.put("var16", varOList.get(i).getString("Examiners"));	    //16
			vpd.put("var17", varOList.get(i).getString("AuditTime"));	    //17
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**获取单件工时
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getHour")
	@ResponseBody
	public Object getHour() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = WorkingHoursService.getHour(pd);	//根据ID读取
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
}
