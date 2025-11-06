package org.yy.controller.pp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.PurchasDetailsService;
import org.yy.service.pp.PurchaseMaterialDetailsService;

/** 
 * 说明：采购物料明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-09
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PurchaseMaterialDetails")
public class PurchaseMaterialDetailsController extends BaseController {
	
	@Autowired
	private PurchaseMaterialDetailsService PurchaseMaterialDetailsService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private MaterialRequirementService MaterialRequirementService;
	@Autowired
	private PurchasDetailsService PurchasDetailsService;
	@Autowired
	private StockService StockService;
	@Autowired
	private StaffService staffService;
	/**保存
	 * @author 管悦
	 * @date 2020-11-09
	 * @param 采购订单明细信息
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("PurchaseMaterialDetails:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PurchaseMaterialDetails_ID", this.get32UUID());	//主键
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
		pd.put("FCreatetime", Tools.date2Str(new Date()));
		pd.put("RowClose", "N");
		pd.put("Admission", "0");	//入厂数量
		pd.put("ReturnedMaterialsCount", "0");	//退料数
		pd.put("FPushCount", "0");
		pd.put("LocksCount", "0");	//锁库数量
		pd.put("EnableEarlyWarningIF", "N");	//是否启用预警
		pd.put("OccupationOfInventory", "N");	//占用库存
		PageData pdRowNum=PurchaseMaterialDetailsService.getRowNum(pd);//生成行号
		if(pdRowNum != null) {
			pd.put("RowNum", pdRowNum.get("RowNum").toString());
		}
		pd.put("SourceOrderId", "");
		PurchaseMaterialDetailsService.save(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购订单明细");//功能项
		pdOp.put("OperationType", "新增");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchaseMaterialDetails_ID"));//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}
	
	/**行关闭/反行关闭
	 * @author 管悦
	 * @date 2020-11-09
	 * @param PurchaseMaterialDetails_ID:清单明细ID、RowClose:行关闭（Y/N）
	 * @throws Exception
	 */
	@RequestMapping(value="/rowClose")
	@ResponseBody
	public Object rowClose() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseMaterialDetailsService.rowClose(pd);
		PageData pdx=PurchaseMaterialDetailsService.findById(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		String RowClose= pd.getString("RowClose");
		if(RowClose.equals("Y")) {
			pdOp.put("OperationType", "行关闭");//操作类型
			pdx.put("PushDownPurchaseIF", "N");//源单下推状态
			MaterialRequirementService.updateState(pdx);//反写物料需求单下推状态
			PurchasDetailsService.calFPushCount(pdx);//反写采购申请明细下推数量
		}else {
			pdOp.put("OperationType", "反行关闭");//操作类型
			pdx.put("PushDownPurchaseIF", "Y");//源单下推状态
			MaterialRequirementService.updateState(pdx);//反写物料需求单下推状态
			PurchasDetailsService.calFPushCount(pdx);//反写采购申请明细下推数量
		}
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
	    pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购订单明细");//功能项
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrderDetail_ID"));//删改数据ID
		operationrecordService.save(pdOp);	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * 删除采购订单明细，同时，根据源单类型，
	 * 源单类型如果是物料需求单，反写是否已下推字段值为未下推
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("PurchaseMaterialDetails:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd=PurchaseMaterialDetailsService.findById(pd);
		if(null!=pd && pd.containsKey("SourceOrderType") && "物料需求".equals(pd.getString("SourceOrderType"))) {
			//更新对应物料需求单是否已下推字段值为未下推
			String SourceOrderId = pd.getString("SourceOrderId");//物料需求单id
			pd.put("MaterialRequirement_ID", SourceOrderId);
			pd.put("PushDownPurchaseIF", "N");
			MaterialRequirementService.updatePushDownPurchaseByMaterialRequirement_ID(pd);
		}
		PurchaseMaterialDetailsService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @author 管悦
	 * @date 2020-11-09
	 * @param PurchaseMaterialDetails_ID:清单明细ID
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("PurchaseMaterialDetails:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseMaterialDetailsService.edit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购订单明细");//功能项
		pdOp.put("OperationType", "修改");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchaseMaterialDetails_ID"));//删改数据ID	
		operationrecordService.save(pdOp);		
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @author 管悦
	 * @date 2020-11-09
	 * @param 
	 * @throws Exception
	 *//*
	@RequestMapping(value="/list")
	//@RequiresPermissions("PurchaseMaterialDetails:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum");						//销售订单编号
		if(Tools.notEmpty(KEYWORDS_OrderNum))pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		String KEYWORDS_WorkOrderNum = pd.getString("KEYWORDS_WorkOrderNum");						//计划工单编号
		if(Tools.notEmpty(KEYWORDS_WorkOrderNum))pd.put("KEYWORDS_WorkOrderNum", KEYWORDS_WorkOrderNum.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
		if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
		if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		String KEYWORDS_FMakeBillsTimeStart = pd.getString("KEYWORDS_FMakeBillsTimeStart");						//创建开始时间
		if(Tools.notEmpty(KEYWORDS_FMakeBillsTimeStart))pd.put("KEYWORDS_FMakeBillsTimeStart", KEYWORDS_FMakeBillsTimeStart.trim());
		String KEYWORDS_FMakeBillsTimeEnd = pd.getString("KEYWORDS_FMakeBillsTimeEnd");						//创建结束时间
		if(Tools.notEmpty(KEYWORDS_FMakeBillsTimeEnd))pd.put("KEYWORDS_FMakeBillsTimeEnd", KEYWORDS_FMakeBillsTimeEnd.trim());
		String KEYWORDS_DeMandTimeStart = pd.getString("KEYWORDS_DeMandTimeStart");						//需求开始时间
		if(Tools.notEmpty(KEYWORDS_DeMandTimeStart))pd.put("KEYWORDS_DeMandTimeStart", KEYWORDS_DeMandTimeStart.trim());
		String KEYWORDS_DeMandTimeEnd = pd.getString("KEYWORDS_DeMandTimeEnd");						//需求结束时间
		if(Tools.notEmpty(KEYWORDS_DeMandTimeEnd))pd.put("KEYWORDS_DeMandTimeEnd", KEYWORDS_DeMandTimeEnd.trim());
		String KEYWORDS_EstimatedTimeOfArrivalStart = pd.getString("KEYWORDS_EstimatedTimeOfArrivalStart");						//预计到达开始时间
		if(Tools.notEmpty(KEYWORDS_EstimatedTimeOfArrivalStart))pd.put("KEYWORDS_EstimatedTimeOfArrivalStart", KEYWORDS_EstimatedTimeOfArrivalStart.trim());
		String KEYWORDS_EstimatedTimeOfArrivalEnd = pd.getString("KEYWORDS_EstimatedTimeOfArrivalEnd");						//预计到达结束时间
		if(Tools.notEmpty(KEYWORDS_EstimatedTimeOfArrivalEnd))pd.put("KEYWORDS_EstimatedTimeOfArrivalEnd", KEYWORDS_EstimatedTimeOfArrivalEnd.trim());
		page.setPd(pd);
		List<PageData>	varList = PurchaseMaterialDetailsService.list(page);	//列出PurchaseMaterialDetails列表
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购订单明细");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", "");//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}*/
	
	/**列表
	 * @author 管悦
	 * @date 2020-11-09
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("PurchaseMaterialDetails:list")
	@ResponseBody
	public Object list() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum");						//销售订单编号
		if(Tools.notEmpty(KEYWORDS_OrderNum))pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		String KEYWORDS_WorkOrderNum = pd.getString("KEYWORDS_WorkOrderNum");						//计划工单编号
		if(Tools.notEmpty(KEYWORDS_WorkOrderNum))pd.put("KEYWORDS_WorkOrderNum", KEYWORDS_WorkOrderNum.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
		if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
		if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		String KEYWORDS_FMakeBillsTimeStart = pd.getString("KEYWORDS_FMakeBillsTimeStart");						//创建开始时间
		if(Tools.notEmpty(KEYWORDS_FMakeBillsTimeStart))pd.put("KEYWORDS_FMakeBillsTimeStart", KEYWORDS_FMakeBillsTimeStart.trim());
		String KEYWORDS_FMakeBillsTimeEnd = pd.getString("KEYWORDS_FMakeBillsTimeEnd");						//创建结束时间
		if(Tools.notEmpty(KEYWORDS_FMakeBillsTimeEnd))pd.put("KEYWORDS_FMakeBillsTimeEnd", KEYWORDS_FMakeBillsTimeEnd.trim());
		String KEYWORDS_DeMandTimeStart = pd.getString("KEYWORDS_DeMandTimeStart");						//需求开始时间
		if(Tools.notEmpty(KEYWORDS_DeMandTimeStart))pd.put("KEYWORDS_DeMandTimeStart", KEYWORDS_DeMandTimeStart.trim());
		String KEYWORDS_DeMandTimeEnd = pd.getString("KEYWORDS_DeMandTimeEnd");						//需求结束时间
		if(Tools.notEmpty(KEYWORDS_DeMandTimeEnd))pd.put("KEYWORDS_DeMandTimeEnd", KEYWORDS_DeMandTimeEnd.trim());
		String KEYWORDS_EstimatedTimeOfArrivalStart = pd.getString("KEYWORDS_EstimatedTimeOfArrivalStart");						//预计到达开始时间
		if(Tools.notEmpty(KEYWORDS_EstimatedTimeOfArrivalStart))pd.put("KEYWORDS_EstimatedTimeOfArrivalStart", KEYWORDS_EstimatedTimeOfArrivalStart.trim());
		String KEYWORDS_EstimatedTimeOfArrivalEnd = pd.getString("KEYWORDS_EstimatedTimeOfArrivalEnd");						//预计到达结束时间
		if(Tools.notEmpty(KEYWORDS_EstimatedTimeOfArrivalEnd))pd.put("KEYWORDS_EstimatedTimeOfArrivalEnd", KEYWORDS_EstimatedTimeOfArrivalEnd.trim());
		//page.setPd(pd);
		String FocusPerson="";
		List<PageData>	varList = PurchaseMaterialDetailsService.listAllX(pd);	//列出PurchaseMaterialDetails列表
		PageData temp = new PageData();
		PageData spd = new PageData();
		for(int i=0;i<varList.size();i++) {
			PageData pdx=varList.get(i);
			FocusPerson="";
			if(varList.get(i).get("FocusPersonID") != null && !"".equals(varList.get(i).get("FocusPersonID"))) {
			String[] sarr=(varList.get(i).get("FocusPersonID")== null?"":varList.get(i).getString("FocusPersonID")).split(",yl,");
			if(sarr.length>1) {
				for(int j=0;j<sarr.length;j++) {
					temp.put("STAFF_ID", sarr[j]);
					spd = staffService.findById(temp);
					if(null!=spd && spd.containsKey("NAME")) {
						if(j>0)FocusPerson+=",";
						FocusPerson+=spd.getString("NAME");
					}
				}
			} else if(sarr.length==1) {
				if(sarr[0] !=null) {
				temp.put("STAFF_ID", sarr[0]);
				spd = staffService.findById(temp);
				if(null!=spd && spd.containsKey("NAME")) {
					FocusPerson = spd.getString("NAME");
				}
				}
			}
			varList.get(i).put("FocusPerson", FocusPerson);
			}
			
			
			String MaterialID = pdx.getString("MaterialID");
			String FClass = pdx.getString("MAT_CLASS");
			String wareHouseType = "工厂库";
			PageData pg = new PageData();
			pg.put("ItemId", MaterialID);
			pg.put("FType", wareHouseType);
			pg.put("FClass", FClass);
			List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);

			if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
				pdx.put("ActualCount", 0.00);
			} else {
				PageData actualCountByFTYPEAndItemIDData = actualCountByFTYPEAndItemID.get(0);
				String ActualCountStr = String.valueOf(actualCountByFTYPEAndItemIDData.get("ActualCount"));
				Double ActualCount = Double.valueOf(ActualCountStr);
				pdx.put("ActualCount", ActualCount);
			}
		}
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购订单明细");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", "");//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	/**根据ID获取详情
	 * @author 管悦
	 * @date 2020-11-09
	 * @param PurchaseMaterialDetails_ID:清单明细ID
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("PurchaseMaterialDetails:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PurchaseMaterialDetailsService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("PurchaseMaterialDetails:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			PurchaseMaterialDetailsService.deleteAll(ArrayDATA_IDS);
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
		titles.add("行关闭");	//1
		titles.add("行号");	//2
		titles.add("源单单号");	//3
		titles.add("源单行号");	//4
		titles.add("源单类型");	//5
		titles.add("采购ID");	//6
		titles.add("物料ID");	//7
		titles.add("辅助属性");	//8
		titles.add("辅助属性值");	//9
		titles.add("规格描述");	//10
		titles.add("数量");	//11
		titles.add("需求时间");	//12
		titles.add("预计到达时间");	//13
		titles.add("入场数");	//14
		titles.add("退料数");	//15
		titles.add("计划工单ID");	//16
		titles.add("关注人ID");	//17
		titles.add("是否启用预警");	//18
		titles.add("预警提前期");	//19
		titles.add("产地");	//20
		titles.add("入场批号");	//21
		titles.add("备注");	//22
		titles.add("毛坯单价");	//23
		titles.add("占用库存");	//24
		titles.add("锁库数量");	//25
		dataMap.put("titles", titles);
		List<PageData> varOList = PurchaseMaterialDetailsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("RowClose"));	    //1
			vpd.put("var2", varOList.get(i).get("RowNum").toString());	//2
			vpd.put("var3", varOList.get(i).getString("SourceOrderNum"));	    //3
			vpd.put("var4", varOList.get(i).get("SourceRowNum").toString());	//4
			vpd.put("var5", varOList.get(i).getString("SourceOrderType"));	    //5
			vpd.put("var6", varOList.get(i).getString("PurchaseID"));	    //6
			vpd.put("var7", varOList.get(i).getString("MaterialID"));	    //7
			vpd.put("var8", varOList.get(i).getString("SProp"));	    //8
			vpd.put("var9", varOList.get(i).getString("SPropKey"));	    //9
			vpd.put("var10", varOList.get(i).getString("SpecificationsDesc"));	    //10
			vpd.put("var11", varOList.get(i).get("FCount").toString());	//11
			vpd.put("var12", varOList.get(i).getString("DemandTime"));	    //12
			vpd.put("var13", varOList.get(i).getString("EstimatedTimeOfArrival"));	    //13
			vpd.put("var14", varOList.get(i).get("Admission").toString());	//14
			vpd.put("var15", varOList.get(i).get("ReturnedMaterialsCount").toString());	//15
			vpd.put("var16", varOList.get(i).getString("PlanningWorkOrderID"));	    //16
			vpd.put("var17", varOList.get(i).getString("FocusPersonID"));	    //17
			vpd.put("var18", varOList.get(i).getString("EnableEarlyWarningIF"));	    //18
			vpd.put("var19", varOList.get(i).getString("EarlyWarningLeadTime"));	    //19
			vpd.put("var20", varOList.get(i).getString("BirthPlace"));	    //20
			vpd.put("var21", varOList.get(i).getString("AdmissionBatchNumber"));	    //21
			vpd.put("var22", varOList.get(i).getString("FExplanation"));	    //22
			vpd.put("var23", varOList.get(i).getString("UnitPriceOfBlank"));	    //23
			vpd.put("var24", varOList.get(i).getString("OccupationOfInventory"));	    //24
			vpd.put("var25", varOList.get(i).get("LocksCount").toString());	//25
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**入库单明细-采购订单物料列表
	 * @author 管悦
	 * @date 2020-11-15
	 * @param StockList_ID：入库单ID
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("PurchaseMaterialDetails:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_FNum = pd.getString("KEYWORDS_FNum");						//采购订单编号
		if(Tools.notEmpty(KEYWORDS_FNum))pd.put("KEYWORDS_FNum", KEYWORDS_FNum.trim());
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum");						//销售订单编号
		if(Tools.notEmpty(KEYWORDS_OrderNum))pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		String KEYWORDS_WorkOrderNum = pd.getString("KEYWORDS_WorkOrderNum");						//计划工单编号
		if(Tools.notEmpty(KEYWORDS_WorkOrderNum))pd.put("KEYWORDS_WorkOrderNum", KEYWORDS_WorkOrderNum.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
		if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
		if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		List<PageData>	varList = PurchaseMaterialDetailsService.listAll(pd);	
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "入库单-采购订单物料");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.getString("StockList_ID"));//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量选择计划工单物料 
	 * @param PurchaseID，DATA_IDS_OCCUPY，DATA_IDS_NOOCCUPY
	 * @throws Exception
	 */
	@RequestMapping(value="/selectAllGD")
	@ResponseBody
	public Object selectAllGD() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS_NOOCCUPY = pd.getString("DATA_IDS_NOOCCUPY");
		String DATA_IDS_OCCUPY = pd.getString("DATA_IDS_OCCUPY");
		int totalCount=0;
		int successCount=0;
		if(Tools.notEmpty(DATA_IDS_NOOCCUPY) || Tools.notEmpty(DATA_IDS_OCCUPY)){
			//不占用库存
			if(Tools.notEmpty(DATA_IDS_NOOCCUPY)){
				JSONArray jarr = JSONArray.fromObject(pd.getString("arr"));
				for(int i=0;i<jarr.size();i++) {
					PageData pdVar = new PageData();
					JSONObject o = jarr.getJSONObject(i);
					Iterator it =o.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Object, Object> entry = (Entry<Object, Object>) it.next();
						pdVar.put(entry.getKey(), entry.getValue());
					}
					/*if(!temp.containsKey("FCount") || null==temp.get("FCount") || "".equals(temp.get("FCount").toString()))
						temp.put("FCount", 0);
					InputOutputService.edit(temp);*/
					Double DemandCount=Double.parseDouble(pdVar.get("DemandCount")==null?"0":pdVar.get("DemandCount").toString());//需求数量
					if(DemandCount != 0) {
						PageData pdSave=new PageData();
						PageData pdRowNum=PurchaseMaterialDetailsService.getRowNum(pd);//生成行号
						if(pdRowNum != null) {
							pdSave.put("RowNum", pdRowNum.get("RowNum").toString());
						}
						pdSave.put("PurchaseMaterialDetails_ID", this.get32UUID());	//主键
						pdSave.put("FNAME", Jurisdiction.getName());
						pdSave.put("FCreator", staffid);//查询职员ID
						//pdSave.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
						pdSave.put("FCreatetime", Tools.date2Str(new Date()));
						pdSave.put("RowClose", "N");
						pdSave.put("Admission", "0");	//入厂数量
						pdSave.put("ReturnedMaterialsCount", "0");	//退料数
						pdSave.put("FPushCount", "0");
						pdSave.put("LocksCount", "0");	//锁库数量
						pdSave.put("EnableEarlyWarningIF", "N");	//是否启用预警
						pdSave.put("OccupationOfInventory", "N");	//占用库存
						pdSave.put("SubordinateInventory", "工厂库");	//隶属库存
						pdSave.put("SourceOrderType", "物料需求");
						//pdSave.put("SourceRowNum", pdVar.get("RowNum").toString());
						pdSave.put("SourceOrderNum", pdVar.getString("FWorkOrderNum"));
						pdSave.put("PurchaseID", pd.getString("PurchaseID"));
						pdSave.put("MaterialID", pdVar.getString("MaterialID"));
						pdSave.put("SProp",  "2540539e45324232a50bde60ac2951d3");//计划工单编号对应的辅助属性主键
						pdSave.put("SPropKey",  pdVar.getString("SPropKey"));
						pdSave.put("FCount", pdVar.get("DemandCount").toString());
						pdSave.put("PlanningWorkOrderID", pdVar.getString("PlanningWorkOrderID"));
						pdSave.put("DemandTime", pdVar.getString("PlannedBeginTime"));
						//pdSave.put("OccupationOfInventory", pdVar.getString("OccupationOfInventory"));//占用库存
						//pdSave.put("PushDownPurchaseIF", "Y");
						pdSave.put("AdmissionBatchNumber", pdVar.getString("BatchNum"));
						//pdSave.put("SourceOrderId", pdVar.getString("MaterialRequirement_ID"));//源单id
						PurchaseMaterialDetailsService.save(pdSave);
						//MaterialRequirementService.updateState(pdSave);//反写源单下推状态
						successCount+=1;
					}
				}
			/*String ArrayDATA_IDS_NOOCCUPY[] = DATA_IDS_NOOCCUPY.split(",");
			totalCount+=ArrayDATA_IDS_NOOCCUPY.length;
			List<PageData> varList=PlanningWorkOrderService.selectAllGD(ArrayDATA_IDS_NOOCCUPY);
			
			for(PageData pdVar:varList) {
				Double DemandCount=Double.parseDouble(pdVar.get("DemandCount")==null?"0":pdVar.get("DemandCount").toString());//需求数量
				if(DemandCount != 0) {
					PageData pdSave=new PageData();
					PageData pdRowNum=PurchaseMaterialDetailsService.getRowNum(pd);//生成行号
					if(pdRowNum != null) {
						pdSave.put("RowNum", pdRowNum.get("RowNum").toString());
					}
					pdSave.put("PurchaseMaterialDetails_ID", this.get32UUID());	//主键
					pdSave.put("FNAME", Jurisdiction.getName());
					pdSave.put("FCreator", staffService.getStaffId(pdSave).getString("STAFF_ID"));//查询职员ID
					//pdSave.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
					pdSave.put("FCreatetime", Tools.date2Str(new Date()));
					pdSave.put("RowClose", "N");
					pdSave.put("Admission", "0");	//入厂数量
					pdSave.put("ReturnedMaterialsCount", "0");	//退料数
					pdSave.put("FPushCount", "0");
					pdSave.put("LocksCount", "0");	//锁库数量
					pdSave.put("EnableEarlyWarningIF", "N");	//是否启用预警
					pdSave.put("OccupationOfInventory", "N");	//占用库存
					pdSave.put("SubordinateInventory", "工厂库");	//隶属库存
					pdSave.put("SourceOrderType", "物料需求");
					//pdSave.put("SourceRowNum", pdVar.get("RowNum").toString());
					pdSave.put("SourceOrderNum", pdVar.getString("FWorkOrderNum"));
					pdSave.put("PurchaseID", pd.getString("PurchaseID"));
					pdSave.put("MaterialID", pdVar.getString("MAT_BASIC_ID"));
					pdSave.put("SProp",  pdVar.getString("SProp"));//计划工单编号对应的辅助属性主键
					pdSave.put("SPropKey",  pdVar.getString("SPropKey"));
					pdSave.put("FCount", pdVar.get("DemandCount").toString());
					pdSave.put("PlanningWorkOrderID", pdVar.getString("PlanningWorkOrderID"));
					pdSave.put("OccupationOfInventory", pdVar.getString("OccupationOfInventory"));
					//pdSave.put("PushDownPurchaseIF", "Y");
					pdSave.put("AdmissionBatchNumber", pdVar.getString("BatchNum"));
					pdSave.put("SourceOrderId", pdVar.getString("MaterialRequirement_ID"));
					PurchaseMaterialDetailsService.save(pdSave);
					//MaterialRequirementService.updateState(pdSave);//反写源单下推状态
					
					successCount+=1;
				}
			}*/
		}
		//占用库存
			/*if(Tools.notEmpty(DATA_IDS_OCCUPY)){
					String ArrayDATA_IDS_OCCUPY[] = DATA_IDS_OCCUPY.split(",");
					totalCount+=ArrayDATA_IDS_OCCUPY.length;
					List<PageData> varList=PlanningWorkOrderService.selectAllGD(ArrayDATA_IDS_OCCUPY);
					
					for(PageData pdVar:varList) {
						Double DemandCount=Double.parseDouble(pdVar.get("DemandCount")==null?"0":pdVar.get("DemandCount").toString());//需求数量
						Double stockSum=0.00;
						Double FCount=0.00;
						PageData pdKC=new PageData();
						pdKC.put("WarehouseID", pdVar.getString("DeliveryWarehouse"));
						pdKC.put("ItemID", pdVar.getString("MaterialID"));
						pdKC.put("MaterialSPropKey", pdVar.getString("SPropKey"));
						PageData pdStockSum=StockService.getSum(pdKC);//即时库存
						if(pdStockSum != null) {
							stockSum=Double.parseDouble(pdStockSum.get("stockSum")==null?"0":pdStockSum.get("stockSum").toString());
						}
						if(DemandCount>stockSum && DemandCount>0) {//
							pdKC.put("num", stockSum);
							StockService.outStock(pdKC);//扣减库存
							
							FCount=DemandCount-stockSum;
							PageData pdSave=new PageData();
							PageData pdRowNum=PurchaseMaterialDetailsService.getRowNum(pd);//生成行号
							if(pdRowNum != null) {
								pdSave.put("RowNum", pdRowNum.get("RowNum").toString());
							}
							pdSave.put("PurchaseMaterialDetails_ID", this.get32UUID());	//主键
							pdSave.put("FNAME", Jurisdiction.getName());
							pdSave.put("FCreator", staffService.getStaffId(pdSave).getString("STAFF_ID"));//查询职员ID
							//pdSave.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
							pdSave.put("FCreatetime", Tools.date2Str(new Date()));
							pdSave.put("RowClose", "N");
							pdSave.put("Admission", "0");	//入厂数量
							pdSave.put("ReturnedMaterialsCount", "0");	//退料数
							pdSave.put("FPushCount", "0");
							pdSave.put("LocksCount", "0");	//锁库数量
							pdSave.put("EnableEarlyWarningIF", "N");	//是否启用预警
							pdSave.put("OccupationOfInventory", "N");	//占用库存
							pdSave.put("SourceOrderType", "物料需求");
							pdSave.put("SourceRowNum", pdVar.get("RowNum").toString());
							pdSave.put("SourceOrderNum", pdVar.getString("FWorkOrderNum"));
							pdSave.put("PurchaseID", pd.getString("PurchaseID"));
							pdSave.put("MaterialID", pdVar.getString("MAT_BASIC_ID"));
							pdSave.put("SPropKey",  pdVar.getString("SPropKey"));
							pdSave.put("FCount", FCount);
							pdSave.put("PlanningWorkOrderID", pdVar.getString("PlanningWorkOrderID"));
							pdSave.put("OccupationOfInventory", pdVar.getString("OccupationOfInventory"));
							pdSave.put("PushDownPurchaseIF", "Y");
							pdSave.put("AdmissionBatchNumber", pdVar.getString("BatchNum"));
							PurchaseMaterialDetailsService.save(pdSave);
							MaterialRequirementService.updateState(pdSave);//反写源单下推状态
							successCount+=1;
						}else if(DemandCount<=stockSum && DemandCount>0) {//
							pdKC.put("num", DemandCount);
							StockService.outStock(pdKC);
							PageData pdSave=new PageData();
							pdSave.put("SourceRowNum", pdVar.get("RowNum").toString());
							pdSave.put("SourceOrderNum", pdVar.getString("FWorkOrderNum"));
							MaterialRequirementService.updateState(pdSave);//反写源单下推状态
							successCount+=1;
						}
						
						
					}
			}*/
			errInfo = "success";
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FNAME", Jurisdiction.getName());
			pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
			//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", "出库单明细-销售订单物料");//功能项
			pdOp.put("OperationType", "批量选择");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("StockList_ID"));
			operationrecordService.save(pdOp);
		}else{
			errInfo = "error";
		}
		map.put("totalCount", totalCount);//选择总条数		
		map.put("successCount", successCount);//导入成功条数		
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**批量选择采购申请物料 
	 * @param PurchaseID
	 * @throws Exception
	 */
	@RequestMapping(value="/selectAllCGSQ")
	@ResponseBody
	public Object selectAllCGSQ() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		int totalCount=0;
		int successCount=0;
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			totalCount=ArrayDATA_IDS.length;
			List<PageData> varList=PurchasDetailsService.selectAllCGSQ(ArrayDATA_IDS);
			successCount=varList.size();
			for(PageData pdVar:varList) {
				PageData pdSave=new PageData();
				PageData pdRowNum=PurchaseMaterialDetailsService.getRowNum(pd);//生成行号
				if(pdRowNum != null) {
					pdSave.put("RowNum", pdRowNum.get("RowNum").toString());
				}
				pdSave.put("PurchaseMaterialDetails_ID", this.get32UUID());	//主键
				pdSave.put("FNAME", Jurisdiction.getName());
				pdSave.put("FCreator", staffService.getStaffId(pdSave).getString("STAFF_ID"));//查询职员ID
				//pdSave.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
				pdSave.put("FCreatetime", Tools.date2Str(new Date()));
				pdSave.put("RowClose", "N");
				pdSave.put("Admission", "0");	//入厂数量
				pdSave.put("ReturnedMaterialsCount", "0");	//退料数
				pdSave.put("FPushCount", "0");
				pdSave.put("LocksCount", "0");	//锁库数量
				pdSave.put("EnableEarlyWarningIF", "N");	//是否启用预警
				pdSave.put("OccupationOfInventory", "N");	//占用库存
				pdSave.put("SubordinateInventory", "工厂库");	//隶属库存
				pdSave.put("SourceOrderType", "采购申请");
				pdSave.put("SourceRowNum", pdVar.get("RowNum").toString());
				pdSave.put("SourceOrderNum", pdVar.getString("FNum"));
				pdSave.put("PurchaseID", pd.getString("PurchaseID"));
				pdSave.put("MaterialID", pdVar.getString("MaterialID"));
				pdSave.put("SPropKey",  pdVar.getString("SPropKey"));
				pdSave.put("FCount", pdVar.get("PurchaseApplyKey").toString());
				pdSave.put("SProp",  pdVar.getString("SProp"));
				pdSave.put("SPropKey",  pdVar.getString("SPropKey"));
				pdSave.put("DemandTime", pdVar.getString("DemandTime"));
				pdSave.put("SpecificationsDesc", pdVar.getString("SpecificationsDesc"));
				pdSave.put("SourceOrderId", pdVar.getString("PurchasDetails_ID"));
				PurchaseMaterialDetailsService.save(pdSave);
				PurchasDetailsService.calFPushCount(pdSave);//反写采购申请明细下推数量
				
			}
			errInfo = "success";
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FNAME", Jurisdiction.getName());
			pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
			//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", "采购订单明细-采购申请物料");//功能项
			pdOp.put("OperationType", "批量选择");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("PurchaseID"));
			operationrecordService.save(pdOp);
		}else{
			errInfo = "error";
		}
		map.put("totalCount", totalCount);//选择总条数		
		map.put("successCount", successCount);//导入成功条数		
		map.put("result", errInfo);				//返回结果
		return map;
	}
}
