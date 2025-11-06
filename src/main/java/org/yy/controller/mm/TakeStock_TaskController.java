package org.yy.controller.mm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.yy.service.km.CodingRulesService;
import org.yy.service.mm.TakeStock_Task_ExecuteService;
import org.yy.service.mm.TakeStock_Task_MaterialService;
import org.yy.service.mm.TakeStock_WinOrLoseService;
import org.yy.service.mm.Takestock_TaskService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：盘点任务
 * 作者：YuanYe
 * 时间：2020-11-26
 * 
 */
@Controller
@RequestMapping("/TakeStock_task")
public class TakeStock_TaskController extends BaseController {
	
	@Autowired
	private Takestock_TaskService takestock_taskService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private TakeStock_Task_MaterialService TakeStock_Task_MaterialService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private TakeStock_WinOrLoseService TakeStock_WinOrLoseService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("takestock_task:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		String msg = "";
		PageData pd = new PageData();
		PageData pdStaff = new PageData();
		PageData pdVar = new PageData();
		PageData pdMX = new PageData();
		pd = this.getPageData();
		pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
		pd.put("TakeStock_Task_ID", this.get32UUID());	//主键
		pd.put("FMakeBillsPersoID",pdStaff.get("STAFF_ID"));			//制单人 STAFF_ID
		pd.put("FMakeBillsTime", Tools.date2Str(new Date())); //制单时间
		pd.put("TakeStock_Task_Status", "创建");//盘点任务状态 默认创建
		pd.put("FIfDifferenceOrder", 0);//默认不按差异单处理
		pd.put("DifferenceOrder_NUM", "1071");//差异单编码  默认后缀为1 ，如需要做差异单，在最后一位+1
		pd.put("FCheckFlag", "未审核");//默认审核状态
		pd.put("FCheckPersonID", "");//审核人 默认空
		pd.put("FCheckTime", "");//审核时间 默认空
		pd.put("FIfCreatOrder", 0);//默认未生成盘盈盘亏单
		//新增盘点任务后 获取物料明细
		String str = pd.get("TakeStock_Task_Type").toString();//根据不同盘点类型，从不同数据源获取数据
		if(str == "按工单盘点" || str.equals("按工单盘点")){
			List<PageData> varList = takestock_taskService.getAllDDMaterial(pd);//获取工单下物料数量不为0 的列表
			if(varList.size() > 0){
			for(int i = 0;i<varList.size();i++){
				pdVar = varList.get(i);
				pdMX.put("TakeStock_Task_Material_ID", this.get32UUID());//盘点任务物料明细ID
				pdMX.put("TakeStock_Task_ID", pd.get("TakeStock_Task_ID"));//盘点任务ID
				pdMX.put("FEntryID", i+1);//行号
				pdMX.put("Material_ID", pdVar.get("ItemID"));//物料ID
				pdMX.put("Repertory_Count", pdVar.get("ActualCount"));//库存数量
				pdMX.put("IfFEntryClose", 1);//是否行关闭  默认 否 0 
				TakeStock_Task_MaterialService.save(pdMX);//保存
				operationrecordService.add("","盘点任务工单物料","添加",pd.getString("TakeStock_Task_Material_ID"),"","");//操作日志
			}
			takestock_taskService.save(pd);
			operationrecordService.add("","盘点任务","添加",pd.getString("TakeStock_Task_ID"),"","");//操作日志
			}else{
				errInfo = "error";
				msg = "该工单下没有物料，请核实";
			}
		}else{
			List<PageData> varList = takestock_taskService.getAllMaterial(pd);//获取仓库下物料数量不为0 的列表
			if(varList.size() > 0){
			for(int i = 0;i<varList.size();i++){
				pdVar = varList.get(i);
				pdMX.put("TakeStock_Task_Material_ID", this.get32UUID());//盘点任务物料明细ID
				pdMX.put("TakeStock_Task_ID", pd.get("TakeStock_Task_ID"));//盘点任务ID
				pdMX.put("FEntryID", i+1);//行号
				pdMX.put("Material_ID", pdVar.get("ItemID"));//物料ID
				pdMX.put("Repertory_Count", pdVar.get("ActualCount"));//库存数量
				pdMX.put("IfFEntryClose", 1);//是否行关闭  默认 否 0 
				TakeStock_Task_MaterialService.save(pdMX);//保存
				operationrecordService.add("","盘点任务库存物料","添加",pd.getString("TakeStock_Task_Material_ID"),"","");//操作日志
			}
			takestock_taskService.save(pd);
			operationrecordService.add("","盘点任务","添加",pd.getString("TakeStock_Task_ID"),"","");//操作日志
			}else{
				errInfo = "error";
				msg = "该仓库下没有物料，请核实";
			}
		}
		map.put("msg", msg);
		map.put("TakeStock_Task_ID", pd.get("TakeStock_Task_ID"));
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 下发盘点任务
	 * @param TakeStock_Task_ID 盘点任务ID
	 * @throws Exception
	 */
	@RequestMapping(value="goLssue")
	@ResponseBody
	public Object goLssue() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = takestock_taskService.findById(pd);	//根据ID读取
		//判断当前状态为创建的才可下发 
		if(pd.get("TakeStock_Task_Status") == "创建" || pd.get("TakeStock_Task_Status").equals("创建")){
			takestock_taskService.goLssue(pd);
		}else{
			errInfo = "error";
		}
		operationrecordService.add("","盘点任务","下发",pd.getString("TakeStock_Task_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 生成盘盈盘亏单
	 */
	@RequestMapping(value="CreatResult")
	@ResponseBody
	public Object CreatResult() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdMA = new PageData();
		PageData pdEX = new PageData();
		PageData pdStaff = new PageData();
		pd = this.getPageData();
		pd = takestock_taskService.findById(pd);	//根据ID读取
		//获取盘点任务物料，
		List<PageData> varList = TakeStock_Task_MaterialService.listAll(pd);
		//循环物料表
		for (int i = 0; i < varList.size(); i++) {
			//获取物料个数
			Integer material = Integer.parseInt(varList.get(i).get("Repertory_Count").toString());
			//获取物料表主键ID,根据ID从执行表中获取物料数量
			String TakeStock_Task_Material_ID = varList.get(i).get("TakeStock_Task_Material_ID").toString();
			pd.put("TakeStock_Task_Material_ID", TakeStock_Task_Material_ID);
			pdMA = takestock_taskService.getCount(pd);
			pdEX = takestock_taskService.findByMaterialId(pd);//获取单条物料的数据
			//获取盘点物料下执行表中差异单编号与盘点任务差异单编号相同的物料个数
			Integer excuteCount = Integer.parseInt(pdMA.get("num").toString());
			//计算单条盘点结果
			Integer result = excuteCount - material;//计算物料数量差异
			pd.put("TakeStock_WinOrLose_ID", this.get32UUID());//盘盈盘亏单ID
			pd.put("FEntryID", varList.get(i).get("FEntryID"));//行号
			pd.put("TakeStock_Task_ID", pd.get("TakeStock_Task_ID"));//盘点任务ID
			pd.put("TakeStock_Batch",pd.get("DifferenceOrder_NUM"));//盘点批次
			pd.put("Material_ID", varList.get(i).get("Material_ID"));//物料ID
			pd.put("TakeStock_Result", result);//盘点结果
			if(result >= 0){
				pd.put("WinOrLose", 1);//盘盈
			}else{
				pd.put("WinOrLose", 0);//盘亏
			}
			pd.put("Difference_Count", Math.abs(result));//差异数量
			pd.put("DifferenceOrder_NUM", pd.get("DifferenceOrder_NUM"));//差异单批号
			pd.put("FOperator", 0);//是否处理 默认否
			pd.put("Relevance_Order", "");//关联单号
			pd.put("IfFEntryClose", 0);//行关闭  默认否
			pd.put("FExplanation", "");//描述
			TakeStock_WinOrLoseService.save(pd);//保存
		}
		takestock_taskService.CreatOrder(pd);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 盘点任务提交审核
	 */
	@RequestMapping(value="goCheck")
	@ResponseBody
	public Object goCheck() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success";
		takestock_taskService.goCheck(pd);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 盘点任务审核执行
	 */
	@RequestMapping(value="FinishCheck")
	@ResponseBody
	public Object FinishCheck() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success";
		String KeyWords = pd.get("KeyWords").toString();
		//判断参数 如果为通过 则正常修改状态，不通过的 进行重盘处理，修改差异单编号
		if(KeyWords == "通过" || KeyWords.equals("通过")){
			pd.put("FCheckTime", Tools.date2Str(new Date()));
			takestock_taskService.CheckPass(pd);
		}else{
			pd = takestock_taskService.findById(pd);
			Integer Order_NUM = Integer.parseInt(pd.get("DifferenceOrder_NUM").toString())+1;
			pd.put("DifferenceOrder_NUM", Order_NUM);
			pd.put("FCheckTime", Tools.date2Str(new Date()));
			takestock_taskService.updateDifferenceOrder_NUM(pd);//差异单编号+1  并且盘点任务状态修改为执行中，审核状态修改为驳回重盘
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 盘点任务复制
	 * @param TakeStock_Task_ID 盘点任务ID
	 */
	@RequestMapping(value="goCopy")
	@ResponseBody
	public Object goCopy() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		String msg = "";
		PageData pd = new PageData();
		PageData pd1 = new PageData();
		PageData pdMX = new PageData();
		PageData pdVar = new PageData();
		PageData pdStaff = new PageData();
		PageData pdNew = new PageData();
		pd = this.getPageData();
		//复制主表信息 
		pd1 = takestock_taskService.findById(pd);	//根据ID读取
		pdNew.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pdNew);						//根据人员的中文姓名获取人员ID
		//默认信息
		pdNew.put("TakeStock_Task_ID", this.get32UUID());	//主键
		pdNew.put("FMakeBillsPersoID",pdStaff.get("STAFF_ID"));			//制单人 STAFF_ID
		pdNew.put("FMakeBillsTime", Tools.date2Str(new Date())); //制单时间
		pdNew.put("TakeStock_Task_Status", "创建");//盘点任务状态 默认创建
		pdNew.put("FIfDifferenceOrder", 0);//默认不按差异单处理
		pdNew.put("DifferenceOrder_NUM", "1071");//差异单编码  默认后缀为1 ，如需要做差异单，在最后一位+1
		pdNew.put("FCheckFlag", "未审核");//默认审核状态
		pdNew.put("FCheckPersonID", "");//审核人 默认空
		pdNew.put("FCheckTime", "");//审核时间 默认空
		pdNew.put("FIfCreatOrder", 0);//默认未生成盘盈盘亏单
		String TakeStock_Task_Kind = pd1.get("TakeStock_Task_Kind").toString();
		if(TakeStock_Task_Kind == "明盘" || TakeStock_Task_Kind.equals("明盘")){
			pdNew.put("TakeStock_Task_Kind", 1);//将前端显示的字符串转换成int进行存储
		}else{
			pdNew.put("TakeStock_Task_Kind", 0);
		}
		pdNew.put("TakeStock_Task_Order",pd.get("TakeStock_Task_Order"));//盘点任务单号，调用接口自动生成单号
		//从被复制的盘点任务中获取
		pdNew.put("TakeStock_Task_Type", pd1.get("TakeStock_Task_Type"));//盘点类型 
		pdNew.put("OrderAndWarehouse", pd1.get("OrderAndWarehouse"));///工单或仓库ID
		pdNew.put("TakeStock_Task_Date", Tools.date2Str(new Date()));//盘点日期默认当前日期
		pdNew.put("FExplanation", pd1.get("FExplanation"));//描述
		
		operationrecordService.add("","盘点任务","复制",pd1.getString("TakeStock_Task_ID"),"","");//操作日志
		//生成主表信息后 根据工单ID或者仓库ID获取物料明细
		if(pdNew.get("TakeStock_Task_Type") == "按工单盘点" || pdNew.get("TakeStock_Task_Type").equals("按工单盘点")){
			//根据不同的盘点类型，从不同的数据表中获取数据
			//获取工单下物料明细列表
			List<PageData> varList = takestock_taskService.getAllDDMaterial(pd1);//获取工单下物料数量不为0 的列表
			if(varList.size() > 0){
			for(int i = 0;i<varList.size();i++){
				pdVar = varList.get(i);
				pdMX.put("TakeStock_Task_Material_ID", this.get32UUID());//盘点任务物料明细ID
				pdMX.put("TakeStock_Task_ID", pdNew.get("TakeStock_Task_ID"));//盘点任务ID
				pdMX.put("FEntryID", i+1);//行号
				pdMX.put("Material_ID", pdVar.get("ItemID"));//物料ID
				pdMX.put("Repertory_Count", pdVar.get("ActualCount"));//库存数量
				pdMX.put("IfFEntryClose", 1);//是否行关闭  默认 否 0 
				operationrecordService.add("","盘点任务工单物料","复制",pd.getString("TakeStock_Task_Material_ID"),"","");//操作日志
				TakeStock_Task_MaterialService.save(pdMX);//保存
			}
			takestock_taskService.save(pdNew);
			}else{
				errInfo = "error";
				msg = "该工单下没有物料，请核实";
			}
		}else{
			//获取仓库下物料明细列表
			List<PageData> varList = takestock_taskService.getAllMaterial(pd1);//获取仓库下物料数量不为0 的列表
			if(varList.size() > 0){
			for(int i = 0;i<varList.size();i++){
				pdVar = varList.get(i);
				pdMX.put("TakeStock_Task_Material_ID", this.get32UUID());//盘点任务物料明细ID
				pdMX.put("TakeStock_Task_ID", pdNew.get("TakeStock_Task_ID"));//盘点任务ID
				pdMX.put("FEntryID", i+1);//行号
				pdMX.put("Material_ID", pdVar.get("ItemID"));//物料ID
				pdMX.put("Repertory_Count", pdVar.get("ActualCount"));//库存数量
				pdMX.put("IfFEntryClose", 1);//是否行关闭  默认 否 0 
				TakeStock_Task_MaterialService.save(pdMX);//保存
				operationrecordService.add("","盘点任务库存物料","复制",pd.getString("TakeStock_Task_Material_ID"),"","");//操作日志
			}
			takestock_taskService.save(pdNew);
			}else{
				errInfo = "error";
				msg = "该仓库下没有物料，请核实";
			}
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		map.put("msg", msg);
		return map;
	}
	
	
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("takestock_task:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		takestock_taskService.delete(pd);
		operationrecordService.add("","盘点任务","删除",pd.getString("TakeStock_Task_ID"),"","");//操作日志
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("takestock_task:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		takestock_taskService.edit(pd);
		operationrecordService.add("","盘点任务","修改",pd.getString("TakeStock_Task_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("takestock_task:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = takestock_taskService.list(page);	//列出Takestock_Task列表
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
	//@RequiresPermissions("takestock_task:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = takestock_taskService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("takestock_task:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			takestock_taskService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**
	 * 仓库列表接口
	 */
	@RequestMapping(value="getWarehouse")
	@ResponseBody
	public Object getWarehouse() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		List<PageData> varList = takestock_taskService.getWarehouse(pd);//
		map.put("varList", varList);
		map.put("result", errInfo);
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
		titles.add("盘点任务ID");	//1
		titles.add("盘点任务单号");	//2
		titles.add("盘点类型");	//3
		titles.add("工单或仓库ID");	//4
		titles.add("盘点日期");	//5
		titles.add("是否生成盘盈盘亏单  1 是 0否 ");	//6
		titles.add("是否差异单处理");	//7
		titles.add("审核状态");	//8
		titles.add("审核人");	//9
		titles.add("审核时间");	//10
		titles.add("盘点任务状态");	//11
		titles.add("制单人");	//12
		titles.add("制单时间");	//13
		titles.add("差异单编号");	//14
		titles.add("描述");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = takestock_taskService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TAKESTOCK_TASK_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("TAKESTOCK_TASK_ORDER"));	    //2
			vpd.put("var3", varOList.get(i).getString("TAKESTOCK_TASK_TYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("ORDERANDWAREHOUSE"));	    //4
			vpd.put("var5", varOList.get(i).getString("TAKESTOCK_TASK_DATE"));	    //5
			vpd.put("var6", varOList.get(i).get("FIFCREATORDER").toString());	//6
			vpd.put("var7", varOList.get(i).get("FIFDIFFERENCEORDER").toString());	//7
			vpd.put("var8", varOList.get(i).getString("FCHECKFLAG"));	    //8
			vpd.put("var9", varOList.get(i).getString("FCHECKPERSONID"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCHECKTIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("TAKESTOCK_TASK_STATUS"));	    //11
			vpd.put("var12", varOList.get(i).getString("FMAKEBILLSPERSOID"));	    //12
			vpd.put("var13", varOList.get(i).getString("FMAKEBILLSTIME"));	    //13
			vpd.put("var14", varOList.get(i).getString("DIFFERENCEORDER_NUM"));	    //14
			vpd.put("var15", varOList.get(i).getString("FEXPLANATION"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**盘点记录列表列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listHIS")
	//@RequiresPermissions("takestock_task:list")
	@ResponseBody
	public Object listHIS(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = takestock_taskService.listHIS(page);	//列出Takestock_Task列表
		for(int i = 0;i<varList.size();i++){
			pd.put("TakeStock_Task_ID", varList.get(i).get("TakeStock_Task_ID"));
			List<PageData>	List  = takestock_taskService.DifferenceOrder_Count(pd);//获取盘点记录下有几个盘盈盘亏单
			Integer count = List.size();
			varList.get(i).put("count",count);
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**盘点记录列表列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getTakeStocHIS")
	//@RequiresPermissions("takestock_task:list")
	@ResponseBody
	public Object getTakeStocHIS(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = takestock_taskService.getTakeStocHIS(pd);
		List<PageData>	List  = takestock_taskService.DifferenceOrder_Count(pd);//获取盘点记录下有几个盘盈盘亏单
		List<PageData> ListMx = new ArrayList<PageData>();
		if(List.size() > 0){//判断盘盈盘亏单个数大于0  
			for(int a = 0;a<List.size();a++){
				pd.put("DifferenceOrder_NUM", 	List.get(a).get("DifferenceOrder_NUM"));//获取差异单编号
				ListMx = takestock_taskService.listResult(pd);//获取列表
				List.get(a).put("ListMx", ListMx);
			}
		}
		pd.put("List", List);
		map.put("page", page);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
