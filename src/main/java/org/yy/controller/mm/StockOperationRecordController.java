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
import org.yy.service.mm.StockOperationRecordService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：库存操作记录
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-17
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/StockOperationRecord")
public class StockOperationRecordController extends BaseController {
	
	@Autowired
	private StockOperationRecordService StockOperationRecordService;
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	@Autowired
	private StaffService staffService;//员工
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("StockOperationRecord:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("StockOperationRecord_ID", this.get32UUID());	//主键
		String currentTime = Tools.date2Str(new Date());
		pd.put("FCreatePersonID", staffid);
		pd.put("FCreateTime", currentTime);
		StockOperationRecordService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("StockOperationRecord:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockOperationRecordService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("StockOperationRecord:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockOperationRecordService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("StockOperationRecord:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = StockOperationRecordService.list(page);	//列出StockOperationRecord列表
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
	//@RequiresPermissions("StockOperationRecord:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = StockOperationRecordService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("StockOperationRecord:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			StockOperationRecordService.deleteAll(ArrayDATA_IDS);
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
		titles.add("物料需求单ID");	//1
		titles.add("库存id");	//2
		titles.add("源单行号");	//3
		titles.add("操作类型(出入库，转移，退料)");	//4
		titles.add("状态");	//5
		titles.add("数据来源");	//6
		titles.add("物料需求单ID");	//7
		titles.add("物料转移申请单主表ID");	//8
		titles.add("计划工单编号");	//9
		titles.add("物料编号");	//10
		titles.add("物料名称");	//11
		titles.add("辅助属性");	//12
		titles.add("辅助属性值");	//13
		titles.add("需求数量");	//14
		titles.add("实发数量");	//15
		titles.add("发出仓库");	//16
		titles.add("目标仓位");	//17
		titles.add("需求时间");	//18
		titles.add("工单编号");	//19
		titles.add("日期批次");	//20
		titles.add("工序");	//21
		titles.add("类型");	//22
		titles.add("创建人");	//23
		titles.add("创建时间");	//24
		dataMap.put("titles", titles);
		List<PageData> varOList = StockOperationRecordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("StockOperationRecord_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("Stock_ID"));	    //2
			vpd.put("var3", varOList.get(i).get("SourceRowNum").toString());	//3
			vpd.put("var4", varOList.get(i).getString("TransferType"));	    //4
			vpd.put("var5", varOList.get(i).getString("FStatus"));	    //5
			vpd.put("var6", varOList.get(i).getString("DataSources"));	    //6
			vpd.put("var7", varOList.get(i).getString("MaterialRequisitionID"));	    //7
			vpd.put("var8", varOList.get(i).getString("MTA_ID"));	    //8
			vpd.put("var9", varOList.get(i).getString("PlannedWorkOrderNum"));	    //9
			vpd.put("var10", varOList.get(i).getString("MaterialNum"));	    //10
			vpd.put("var11", varOList.get(i).getString("MaterialName"));	    //11
			vpd.put("var12", varOList.get(i).getString("SProp"));	    //12
			vpd.put("var13", varOList.get(i).getString("SPropKey"));	    //13
			vpd.put("var14", varOList.get(i).get("DemandQuantity").toString());	//14
			vpd.put("var15", varOList.get(i).get("IssuedQuantity").toString());	//15
			vpd.put("var16", varOList.get(i).getString("DeliveryWarehouse"));	    //16
			vpd.put("var17", varOList.get(i).getString("TargetPosition"));	    //17
			vpd.put("var18", varOList.get(i).getString("DemandTime"));	    //18
			vpd.put("var19", varOList.get(i).getString("WorkOrderNum"));	    //19
			vpd.put("var20", varOList.get(i).getString("DateBatch"));	    //20
			vpd.put("var21", varOList.get(i).getString("WP"));	    //21
			vpd.put("var22", varOList.get(i).getString("TType"));	    //22
			vpd.put("var23", varOList.get(i).getString("FCreatePersonID"));	    //23
			vpd.put("var24", varOList.get(i).getString("FCreateTime"));	    //24
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
