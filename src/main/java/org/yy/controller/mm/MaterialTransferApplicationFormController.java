package org.yy.controller.mm;

import java.math.BigDecimal;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.MAKECODEService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.mm.MaterialTransferApplicationDetailsService;
import org.yy.service.mm.MaterialTransferApplicationFormService;
import org.yy.service.mm.MaterialTransferSplitService;
import org.yy.service.mm.StockOperationRecordService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.WH_LocationService;

/** 
 * 说明：物料转移申请单
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/MaterialTransferApplicationForm")
public class MaterialTransferApplicationFormController extends BaseController {
	
	@Autowired
	private MaterialTransferApplicationFormService MaterialTransferApplicationFormService;
	
	@Autowired
	private MaterialTransferApplicationDetailsService MaterialTransferApplicationDetailsService;
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private CodingRulesService codingRulesService;// 编码规则接口
	
	@Autowired
	private MaterialRequirementService MaterialRequirementService;//物料需求单
	
	@Autowired
	private StockOperationRecordService stockOperationRecordService;//库存操作记录
	
	@Autowired
	private StockService StockService;//库存
	
	@Autowired
	private WH_LocationService wh_locationService;//库位
	
	@Autowired
	private MaterialTransferSplitService MaterialTransferSplitService;//转移单物料拆分表
	
	@Autowired
	private StockOperationRecordService StockOperationRecordService;//库存操作记录
	
	@Autowired
	private MAT_BASICService mat_basicService;//物料
	
	@Autowired
	private MAKECODEService MAKECODEService;//制码
	
	/**
	 * 审核调拨单
	 * 循环单据明细，根据目标仓位和数量，扣减发出仓库库存，添加目标库位库存，
	 */
	@RequestMapping(value="/auditAllocation")
	@ResponseBody
	public Object auditAllocation() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		String MTA_ID = pd.getString("MTA_ID");//单据主表id
		String time = Tools.date2Str(new Date());
		pd.put("LineClose", "N");//包含LineClose和MTA_ID
		List<PageData> details = MaterialTransferApplicationDetailsService.listAll(pd);
		int n=0;
		for(PageData temp : details) {
			if("".equals(temp.getString("DeliveryWarehouse")) || "".equals(temp.getString("TargetPosition"))) {
				n=n+1;
			}
			//查询发出仓库中是否有足够数量的物料，如果数量不够，审核无法通过
			PageData stockpd=new PageData();
			stockpd.put("ItemID", temp.getString("MaterialId"));
			stockpd.put("WarehouseID", temp.getString("DeliveryWarehouse"));
			stockpd.put("PositionID", temp.getString("DeliveryPosition"));
			stockpd = StockService.getSum(stockpd);
			BigDecimal stockSum=new BigDecimal(stockpd.get("stockSum").toString());
			BigDecimal qty=new BigDecimal(temp.get("IssuedQuantity").toString());
			if(stockSum.compareTo(qty)==-1)n=n+1;
		}
		if(n==0) {
			for(PageData temp : details) {
				PageData locationpd = new PageData();
				locationpd.put("WH_LOCATION_ID", temp.getString("TargetPosition"));
				locationpd = wh_locationService.findById(locationpd);
				temp.put("MAT_BASIC_ID", temp.getString("MaterialId"));
				PageData matpd = mat_basicService.findById(temp);
				String Ftype = "类型码";
				//扣减车间库存
				PageData spd = new PageData();
				spd.put("num", new BigDecimal(temp.get("IssuedQuantity").toString()));//扣减的出库数量
				spd.put("WarehouseID", temp.getString("DeliveryWarehouse"));//发出仓库id
				spd.put("ItemID", temp.getString("MaterialId"));//物料id
				spd.put("MaterialSPropKey", 
						temp.containsKey("SPropKey") && 
						null!=temp.get("SPropKey")?temp.getString("SPropKey"):"4486905b9e47428985c36c9d55d21ad7");//辅助属性值id,如果没有值，设为公用
				StockService.outStock(spd);//扣减车间库存
				//添加工厂库存
				PageData stockpd=new PageData();
				stockpd.put("Stock_ID", UuidUtil.get32UUID());
				stockpd.put("StorageStatus", "工厂");
				if("是".equals(matpd.getString("UNIQUE_CODE_WHETHER"))){
					Ftype = "唯一码";
					PageData pdCode = MAKECODEService.getCode(pd);
					BigDecimal FCode = new BigDecimal(pdCode.get("FCode").toString());
					FCode = FCode.add(new BigDecimal(1));
					pd.put("Ftype", Ftype);
					pd.put("FCode", FCode);
					MAKECODEService.editCode(pd);
					String encode = "W,YL," + FCode + ",YL," + 1 + ",YL," + temp.getString("MAT_AUXILIARYMX_CODE");
					stockpd.put("QRCode", encode);//二维码=
					stockpd.put("OneThingCode", FCode);
				} else {
					String encode = "L,YL," + matpd.getString("MAT_CODE") + ",YL," + temp.get("IssuedQuantity").toString() 
							+ ",YL," + temp.getString("MAT_AUXILIARYMX_CODE");
					stockpd.put("QRCode", encode);//二维码=
					stockpd.put("OneThingCode", "");
				}
				stockpd.put("ItemID", temp.getString("MaterialId"));//物料id
				stockpd.put("MaterialSProp", temp.getString("SProp"));//辅助属性
				stockpd.put("MaterialSPropKey", temp.getString("SPropKey"));//辅助属性值
				stockpd.put("SpecificationDesc", matpd.get("MAT_SPECS_QTY").toString()+
						matpd.getString("FUNITCODE")+"/"+matpd.getString("AUXILIARY_UNITCODE"));//规格描述
				stockpd.put("WarehouseID", locationpd.getString("WH_STORAGEAREA_ID"));//仓库id
				stockpd.put("PositionID", temp.getString("TargetPosition"));//库位id
				stockpd.put("ActualCount", temp.get("IssuedQuantity"));//实际数量
				stockpd.put("UseCount", 0);//使用数量
				stockpd.put("UseIf", "NO");//是否占用
				stockpd.put("FUnit", matpd.getString("MAT_MAIN_UNIT"));//单位
				stockpd.put("FLevel", 1);//等级
				stockpd.put("SalesOrder", "");//销售订单
				stockpd.put("ProjectNum", "");//项目编码
				stockpd.put("QualityStatus", "合格");//质量状态
				stockpd.put("QITime", "");//质检时间
				stockpd.put("BusinessStatus", "");//业务状态
				stockpd.put("FBatch", "");//批次
				stockpd.put("CustomerID", "");//客户id
				stockpd.put("SupplierID", "");//供应商id
				stockpd.put("GuaranteePeriod", "");//保质期
				stockpd.put("PlanTrackingNumber", "");//计划跟踪号，计划工单编号
				stockpd.put("SupplierBatch", "");//供应商批次
				stockpd.put("IncomingBatch", "");//入场批次
				stockpd.put("ProductionBatch", "");//生产批次
				stockpd.put("RunningState", "在用");//运行状态，在用，停用
				stockpd.put("FCreatePersonID", staffid);//创建人id
				stockpd.put("FCreateTime", time);//创建时间
				stockpd.put("UsageTime", time);//使用时间
				stockpd.put("DateOfManufacture", "");//生产日期
				stockpd.put("IncomingSpecification", "");//入厂规格
				stockpd.put("LastModifiedTime", time);//最近修改时间
				stockpd.put("LastCountTime", time);//上次盘点时间
				stockpd.put("FExplanation", "");//备注
				stockpd.put("FStatus", "退料");//状态，入库，出库，转移，退料
				StockService.save(stockpd);//添加工厂库存
			}
			PageData formpd=new PageData();
			formpd.put("MaterialTransferApplicationForm_ID", MTA_ID);
			formpd.put("FReviewer", staffid);
			formpd.put("AuditTime", time);
			formpd.put("AuditMark", "已审核");
			MaterialTransferApplicationFormService.editAuditMark(formpd);
			operationrecordService.add("","调拨单","审核","",staffid,"调拨审核");
		} else {
			errInfo="error";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 添加调拨单
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/createAllocation")
	//@RequiresPermissions("MaterialTransferApplicationForm:add")
	@ResponseBody
	public Object createAllocation() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MaterialTransferApplicationForm_ID", this.get32UUID());	//主键
		String currentTime = Tools.date2Str(new Date());
		pd.put("Transferor", staffid);	//转移人	// 
		pd.put("TransferTime", currentTime);	//转移时间
		pd.put("TransferType", "调拨");	//转移类型（转移，退料）
		pd.put("FCreator", staffid);	//制单人	// 
		pd.put("PreparationTime", currentTime);	//制单时间
		pd.put("AuditMark", "待审");	//审核标志
		pd.put("FReviewer", "");	//审核人	// 
		pd.put("AuditTime", "");	//审核时间
		pd.put("TransitionType", "调拨");	//转移类型
		MaterialTransferApplicationFormService.save(pd);
		map.put("MaterialTransferApplicationForm_ID", pd.get("MaterialTransferApplicationForm_ID"));
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 审核退料申请
	 * 循环退料单明细，根据目标仓位和退料数量，扣减发出仓库库存，添加目标库位库存，
	 */
	@RequestMapping(value="/auditBack")
	@ResponseBody
	public Object auditBack() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		String MTA_ID = pd.getString("MTA_ID");//单据主表id
		String time = Tools.date2Str(new Date());
		pd.put("LineClose", "N");//包含LineClose和MTA_ID
		List<PageData> details = MaterialTransferApplicationDetailsService.listAll(pd);
		int n=0;
		for(PageData temp : details) {
			if("".equals(temp.getString("DeliveryWarehouse")) || "".equals(temp.getString("TargetPosition"))) {
				n=n+1;
			}
			//查询发出仓库中是否有足够数量的物料，如果数量不够，审核无法通过
			PageData stockpd=new PageData();
			stockpd.put("ItemID", temp.getString("MaterialId"));
			stockpd.put("WarehouseID", temp.getString("DeliveryWarehouse"));
			stockpd = StockService.getSum(stockpd);
			BigDecimal stockSum=new BigDecimal(stockpd.get("stockSum").toString());
			BigDecimal qty=new BigDecimal(temp.get("IssuedQuantity").toString());
			if(stockSum.compareTo(qty)==-1)n=n+1;
		}
		if(n==0) {
			for(PageData temp : details) {
				PageData locationpd = new PageData();
				locationpd.put("WH_LOCATION_ID", temp.getString("TargetPosition"));
				locationpd = wh_locationService.findById(locationpd);
				temp.put("MAT_BASIC_ID", temp.getString("MaterialId"));
				PageData matpd = mat_basicService.findById(temp);
				String Ftype = "类型码";
				//扣减车间库存
				PageData spd = new PageData();
				spd.put("num", new BigDecimal(temp.get("IssuedQuantity").toString()));//扣减的出库数量
				spd.put("WarehouseID", temp.getString("DeliveryWarehouse"));//发出仓库id
				spd.put("ItemID", temp.getString("MaterialId"));//物料id
				spd.put("MaterialSPropKey", 
						temp.containsKey("SPropKey") && 
						null!=temp.get("SPropKey")?temp.getString("SPropKey"):"4486905b9e47428985c36c9d55d21ad7");//辅助属性值id,如果没有值，设为公用
				StockService.outStock(spd);//扣减车间库存
				//添加工厂库存
				PageData stockpd=new PageData();
				stockpd.put("Stock_ID", UuidUtil.get32UUID());
				stockpd.put("StorageStatus", "工厂");
				if("是".equals(matpd.getString("UNIQUE_CODE_WHETHER"))){
					Ftype = "唯一码";
					PageData pdCode = MAKECODEService.getCode(pd);
					BigDecimal FCode = new BigDecimal(pdCode.get("FCode").toString());
					FCode = FCode.add(new BigDecimal(1));
					pd.put("Ftype", Ftype);
					pd.put("FCode", FCode);
					MAKECODEService.editCode(pd);
					String encode = "W,YL," + FCode + ",YL," + 1 + ",YL," + temp.getString("MAT_AUXILIARYMX_CODE");
					stockpd.put("QRCode", encode);//二维码=
					stockpd.put("OneThingCode", FCode);
				} else {
					String encode = "L,YL," + matpd.getString("MAT_CODE") + ",YL," + temp.get("IssuedQuantity").toString() 
							+ ",YL," + temp.getString("MAT_AUXILIARYMX_CODE");
					stockpd.put("QRCode", encode);//二维码=
					stockpd.put("OneThingCode", "");
				}
				stockpd.put("ItemID", temp.getString("MaterialId"));//物料id
				stockpd.put("MaterialSProp", temp.getString("SProp"));//辅助属性
				stockpd.put("MaterialSPropKey", temp.getString("SPropKey"));//辅助属性值
				stockpd.put("SpecificationDesc", matpd.get("MAT_SPECS_QTY").toString()+
						matpd.getString("FUNITCODE")+"/"+matpd.getString("AUXILIARY_UNITCODE"));//规格描述
				stockpd.put("WarehouseID", locationpd.getString("WH_STORAGEAREA_ID"));//仓库id
				stockpd.put("PositionID", temp.getString("TargetPosition"));//库位id
				stockpd.put("ActualCount", temp.get("IssuedQuantity"));//实际数量
				stockpd.put("UseCount", 0);//使用数量
				stockpd.put("UseIf", "NO");//是否占用
				stockpd.put("FUnit", matpd.getString("MAT_MAIN_UNIT"));//单位
				stockpd.put("FLevel", 1);//等级
				stockpd.put("SalesOrder", "");//销售订单
				stockpd.put("ProjectNum", "");//项目编码
				stockpd.put("QualityStatus", "合格");//质量状态
				stockpd.put("QITime", "");//质检时间
				stockpd.put("BusinessStatus", "");//业务状态
				stockpd.put("FBatch", "");//批次
				stockpd.put("CustomerID", "");//客户id
				stockpd.put("SupplierID", "");//供应商id
				stockpd.put("GuaranteePeriod", "");//保质期
				stockpd.put("PlanTrackingNumber", "");//计划跟踪号，计划工单编号
				stockpd.put("SupplierBatch", "");//供应商批次
				stockpd.put("IncomingBatch", "");//入场批次
				stockpd.put("ProductionBatch", "");//生产批次
				stockpd.put("RunningState", "在用");//运行状态，在用，停用
				stockpd.put("FCreatePersonID", staffid);//创建人id
				stockpd.put("FCreateTime", time);//创建时间
				stockpd.put("UsageTime", time);//使用时间
				stockpd.put("DateOfManufacture", "");//生产日期
				stockpd.put("IncomingSpecification", "");//入厂规格
				stockpd.put("LastModifiedTime", time);//最近修改时间
				stockpd.put("LastCountTime", time);//上次盘点时间
				stockpd.put("FExplanation", "");//备注
				stockpd.put("FStatus", "退料");//状态，入库，出库，转移，退料
				StockService.save(stockpd);//添加工厂库存
			}
			PageData formpd=new PageData();
			formpd.put("MaterialTransferApplicationForm_ID", MTA_ID);
			formpd.put("RunningState", "结束");
			formpd.put("TransitionType", "退料");
			MaterialTransferApplicationFormService.editRunningState(formpd);
			operationrecordService.add("","退料申请","审核","",staffid,"退料申请审核");
		} else {
			errInfo="error";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 审核转入单
	 * 新增库存，修改转入单状态为完成
	 * @param pd.MTA_ID
	 */
	@RequestMapping(value="/auditIn")
	@ResponseBody
	public Object auditIn() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		pd = this.getPageData();
		String MTA_ID = pd.getString("MTA_ID");
		String currentTime = Tools.date2Str(new Date());
		List<PageData> list = MaterialTransferApplicationDetailsService.auditInList(pd);
		for(int i=0;i<list.size();i++) {
			PageData p=list.get(i);
			//添加库存
			PageData stockpd=new PageData();
			String Stock_ID = UuidUtil.get32UUID();
			stockpd.put("Stock_ID", Stock_ID);
			stockpd.put("StorageStatus", "转入");
			stockpd.put("QRCode", p.getString("MAT_CODE"));//二维码
			if("是".equals(p.getString("UNIQUE_CODE_WHETHER"))){
				stockpd.put("OneThingCode", "OneThingCode");
			} else {
				stockpd.put("OneThingCode", "");
			}
			stockpd.put("ItemID", p.getString("MAT_BASIC_ID"));//物料id
			stockpd.put("MaterialSProp", p.getString("SProp"));//辅助属性
			stockpd.put("MaterialSPropKey", p.getString("SPropKey"));//辅助属性值
			stockpd.put("SpecificationDesc", p.getString("MAT_SPECS"));//规格描述
			stockpd.put("WarehouseID", p.getString("TargetWarehouse"));//仓库id
			stockpd.put("PositionID", p.getString("TargetPosition"));//库位id
			stockpd.put("ActualCount", new BigDecimal(p.get("IssuedQuantity").toString()));//实际数量
			stockpd.put("UseCount", 0);//使用数量
			stockpd.put("UseIf", "NO");//是否占用
			stockpd.put("FUnit", p.getString("MAT_MAIN_UNIT"));//单位
			stockpd.put("FLevel", 1);//等级
			stockpd.put("SalesOrder", "");//销售订单
			stockpd.put("ProjectNum", "");//项目编码
			stockpd.put("QualityStatus", "合格");//质量状态
			stockpd.put("QITime", currentTime);//质检时间
			stockpd.put("BusinessStatus", "");//业务状态
			stockpd.put("FBatch", p.getString("DateBatch"));//批次
			stockpd.put("CustomerID", "");//客户id
			stockpd.put("SupplierID", "");//供应商id
			stockpd.put("GuaranteePeriod", "");//保质期
			stockpd.put("PlanTrackingNumber", p.getString("PlannedWorkOrderNum"));//计划跟踪号，计划工单编号
			stockpd.put("SupplierBatch", "");//供应商批次
			stockpd.put("IncomingBatch", "");//入场批次
			stockpd.put("ProductionBatch", p.getString("DateBatch"));//生产批次
			stockpd.put("RunningState", "在用");//运行状态，在用，停用
			stockpd.put("FCreatePersonID", staffid);//创建人id
			stockpd.put("FCreateTime", currentTime);//创建时间
			stockpd.put("UsageTime", currentTime);//使用时间
			stockpd.put("DateOfManufacture", "");//生产日期
			stockpd.put("IncomingSpecification", "");//入厂规格
			stockpd.put("LastModifiedTime", currentTime);//最近修改时间
			stockpd.put("LastCountTime", currentTime);//上次盘点时间
			stockpd.put("FExplanation", "");//备注
			stockpd.put("FStatus", "转移");//状态，入库，出库，转移，退料
			StockService.save(stockpd);//添加库存
			StockOperationRecordService.add(Stock_ID,"转移申请",
					stockpd.get("ActualCount")==null?"0":stockpd.get("ActualCount").toString(),
							stockpd.getString("WarehouseID"),stockpd.getString("PositionID"),
					"审核转入",stockpd.getString("ItemID"),staffid);
		}
		//修改转储单状态为转入，关闭
		pd.put("MaterialTransferApplicationForm_ID", MTA_ID);
		pd = MaterialTransferApplicationFormService.findById(pd);
		pd.put("RunningState", "完成");
		pd.put("TransitionType", "转入");
		pd.put("AuditMark", "转入已审核");
		pd.put("FReviewer", staffid);
		pd.put("AuditTime", currentTime);
		pd.put("Transferor", staffid);
		pd.put("TransferTime", currentTime);
		MaterialTransferApplicationFormService.edit(pd);
		//MaterialTransferApplicationFormService.editRunningState(pd);
		operationrecordService.add("","转入单","审核","",staffid,"转入单审核");
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 审核转出单
	 * 获取合并后的转入物料列表，添加转入明细，及其拆分明细（唯一码物料才有拆分二级明细）
	 * 扣减库存（删除唯一码库存物料），生成转入单明细，修改转出单状态为转入
	 * @param pd.MTA_ID
	 */
	@RequestMapping(value="/auditOut")
	@ResponseBody
	public Object auditOut() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		pd = this.getPageData();
		String MTA_ID = pd.getString("MTA_ID");
		String currentTime = Tools.date2Str(new Date());
		//获取合并后的转入物料列表，添加转入明细，及其拆分明细（唯一码物料）
		List<PageData> details = MaterialTransferApplicationDetailsService.mergeTransferInlist(pd);
		for(int i=0;i<details.size();i++) {
			if(new BigDecimal(details.get(i).get("sjnum").toString()).compareTo(BigDecimal.ZERO)==1) {
				PageData detail = details.get(i);
				String MaterialNum = detail.getString("MaterialNum");
				String SPropKey = detail.getString("SPropKey");
				String UNIQUE_CODE_WHETHER = detail.getString("UNIQUE_CODE_WHETHER");
				String MTADetails_ID = this.get32UUID();
				//生成转入单明细，需要合并相同库位相同物料的数量，
				PageData detailpd=new PageData();
				detailpd.put("MTADetails_ID", MTADetails_ID);//物料转移申请单明细ID
				detailpd.put("LineClose", "N");//行关闭（N/Y)
				detailpd.put("TransferOperator", staffid);//转移操作人
				detailpd.put("TransferOperationTime", currentTime);//转移操作时间
				detailpd.put("SourceRowNum", "");//源单行号
				detailpd.put("TransferType", "单据转移");//转移类型(直接转移,单据转移)
				detailpd.put("FStatus", "待转移");//状态（待转移，已转移）
				detailpd.put("DataSources", "物料转出单");//数据来源
				detailpd.put("MaterialRequisitionID", "");//物料需求单ID
				detailpd.put("MTA_ID", MTA_ID);//物料转移申请单主表ID
				detailpd.put("PlannedWorkOrderNum", detail.getString("PlannedWorkOrderNum"));//计划工单编号
				detailpd.put("MaterialNum", detail.getString("MaterialNum"));//物料编码
				detailpd.put("MaterialName", detail.getString("MaterialName"));//物料名称
				detailpd.put("SProp", detail.getString("SProp"));//辅助属性
				detailpd.put("SPropKey", detail.getString("SPropKey"));//辅助属性值
				detailpd.put("DemandQuantity", new BigDecimal(detail.get("sjnum").toString()));//需求数量
				detailpd.put("IssuedQuantity", new BigDecimal("0"));//实发数量
				detailpd.put("DeliveryWarehouse", "");//发出仓库
				detailpd.put("DeliveryPosition", "");//发出仓位
				detailpd.put("TargetWarehouse", detail.getString("TargetWarehouse"));//目标仓库
				detailpd.put("TargetPosition", "");//目标仓位
				detailpd.put("DemandTime", detail.getString("DemandTime"));//需求时间
				detailpd.put("DateBatch", detail.getString("DateBatch"));//日期批次
				detailpd.put("WP", "");//工序
				detailpd.put("FCreatePersonID", staffid);//创建人
				detailpd.put("FCreateTime", currentTime);//创建时间
				detailpd.put("MaterialId", detail.getString("MaterialId"));//物料id
				detailpd.put("SplitQuantity", new BigDecimal(detail.get("sjnum").toString()));//拆分数量
				detailpd.put("SalesOrder", detail.containsKey("SalesOrder")?detail.getString("SalesOrder"):"");//销售订单
				detailpd.put("TransitionType", "转入");//运转类型（转入、转出）
				detailpd.put("METADATA_WHETHER", "Y");//是否元数据，Y/N
				detailpd.put("SplitStatus", "未拆分");//拆分状态，未拆分、已拆分
				detailpd.put("RelationID", "");//关联id，元数据为空s
				detailpd.put("FIsScan", "N");
				MaterialTransferApplicationDetailsService.save(detailpd);
				if("是".equals(UNIQUE_CODE_WHETHER)){
					//如果是唯一码物料，需插入物料拆分表二级明细
					PageData splitpd=new PageData();
					splitpd.put("MaterialNum", MaterialNum);
					splitpd.put("SPropKey", SPropKey);
					splitpd.put("MTA_ID", MTA_ID);
					//查询单据的拆分二级明细
					List<PageData> splitlist = MaterialTransferSplitService.findOutSplitlistByMTA_IDAndSPropKeyAndMaterialNum(splitpd);
					for(PageData temp:splitlist) {	//添加转入二级明细
						temp.put("MaterialTransferSplit_ID", UuidUtil.get32UUID());
						temp.put("FRelatedID", MTADetails_ID);
						temp.put("FIsScan", "否");
						temp.put("FCreatetime", Tools.date2Str(new Date()));
						MaterialTransferSplitService.save(temp);
					}
				}
			}
		}
		//扣减库存，如果是唯一码物料，删除对应库存，如果是类型码物料，扣减库存
		PageData deletepd = new PageData();
		deletepd.put("TransitionType", "转出");
		deletepd.put("MTA_ID", MTA_ID);
		deletepd.put("LineClose", "N");
		List<PageData> list = MaterialTransferApplicationDetailsService.listAll(deletepd);
		for(int i=0;i<list.size();i++) {
			if("是".equals(list.get(i).getString("UNIQUE_CODE_WHETHER"))){
				PageData split = new PageData();
				split.put("FRelatedID", list.get(i).getString("MTADetails_ID"));
				List<PageData> splitlist = MaterialTransferSplitService.listAll(split);
				for(PageData temp:splitlist) {
					if(temp.containsKey("FUniqueCode") && null!=temp.get("FUniqueCode") && !"".equals(temp.get("FUniqueCode"))) {
						//	删除唯一码库存物料，根据唯一码删除库存物料
						temp.put("OneThingCode", temp.getString("FUniqueCode"));
						StockService.deleteByOneThingCode(temp);
						StockOperationRecordService.add(temp.getString("MaterialTransferSplit_ID"),"转移申请",
								temp.get("FQuantity")==null?"0":temp.get("FQuantity").toString(),
										temp.getString("WH_WAREHOUSE_ID"),temp.getString("WH_LOCATION_ID"),
								"审核转出",temp.getString("ItemID"),staffid);
					}
				}
			} else {
				//扣减库存，根据库位、物料、辅助属性、工单由小到大扣减，
				PageData stock = new PageData();
				stock.put("num", new BigDecimal(list.get(i).containsKey("IssuedQuantity") && null!=list.get(i).get("IssuedQuantity")
						?list.get(i).get("IssuedQuantity").toString():"0"));
				stock.put("PositionID", list.get(i).getString("DeliveryPosition"));
				stock.put("WarehouseID", list.get(i).getString("DeliveryWarehouse"));
				stock.put("ItemID", list.get(i).getString("MaterialId"));
				stock.put("MaterialSPropKey", list.get(i).getString("SPropKey"));
				StockService.outStock(stock);//出库数量num，仓库id和物料id和辅助属性值id
				StockOperationRecordService.add(list.get(i).getString("MTA_ID"),"转移申请",
						list.get(i).get("IssuedQuantity")==null?"0":list.get(i).get("IssuedQuantity").toString(),
								list.get(i).getString("DeliveryWarehouse"),list.get(i).getString("DeliveryPosition"),
						"审核转出",list.get(i).getString("MaterialId"),staffid);
			}
		}
		//修改转储单状态为转入
		pd.put("MaterialTransferApplicationForm_ID", MTA_ID);
		pd = MaterialTransferApplicationFormService.findById(pd);
		pd.put("RunningState", "执行");
		pd.put("TransitionType", "转入");
		pd.put("AuditMark", "转出已审核");
		pd.put("FReviewer", staffid);
		pd.put("AuditTime", currentTime);
		pd.put("Transferor", staffid);
		pd.put("TransferTime", currentTime);
		MaterialTransferApplicationFormService.edit(pd);
		operationrecordService.add("","转出单","审核","",staffid,"转出单审核");
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 退料
	 * 选择退料单明细，选择目标仓位和退料数量，扣减车间库存，添加工厂库存，
	 */
	@RequestMapping(value="/back")
	@ResponseBody
	public Object back() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		String MaterialId = pd.getString("MaterialId");//物料id
		String DemandQuantity = pd.get("DemandQuantity").toString();//需求数量
		String TargetWarehouse = pd.getString("TargetWarehouse");//目标仓库
		String TargetPosition = pd.getString("TargetPosition");//目标仓位
		String MTADetails_ID = pd.getString("MTADetails_ID");//明细id
		String time = Tools.date2Str(new Date());
		String warehouseId = "";
		PageData mtaDetails = new PageData();
		mtaDetails = MaterialTransferApplicationDetailsService.findById(pd);
		String MaterialRequisitionID = mtaDetails.getString("MaterialRequisitionID");//转移申请单明细id
		mtaDetails.put("MTADetails_ID", MaterialRequisitionID);
		mtaDetails = MaterialTransferApplicationDetailsService.findById(mtaDetails);
		if("".equals(warehouseId)) {
			PageData lpd = new PageData();
			lpd.put("WH_LOCATION_ID", mtaDetails.getString("TargetPosition"));
			lpd=wh_locationService.findById(lpd);
			if(null!=lpd && lpd.containsKey("WH_WAREHOUSE_ID") && null!=lpd.get("WH_WAREHOUSE_ID")) {
				warehouseId=lpd.getString("WH_WAREHOUSE_ID");
			}
		}
		//扣减车间库存
		PageData spd = new PageData();
		spd.put("num", DemandQuantity);//扣减的出库数量
		spd.put("WarehouseID", warehouseId);//发出仓库id
		spd.put("ItemID", MaterialId);//物料id
		spd.put("MaterialSPropKey", 
				mtaDetails.containsKey("SPropKey") && null!=mtaDetails.get("SPropKey")?mtaDetails.getString("SPropKey"):"4486905b9e47428985c36c9d55d21ad7");//辅助属性值id,如果没有值，设为公用
		StockService.outStock(spd);//扣减车间库存
		//添加工厂库存
		PageData stockpd=new PageData();
		stockpd.put("Stock_ID", UuidUtil.get32UUID());
		stockpd.put("StorageStatus", "工厂");
		stockpd.put("QRCode", mtaDetails.getString("MaterialNum"));//二维码==仓库代码+物料代码????	TODO
		stockpd.put("OneThingCode", "");
		stockpd.put("ItemID", MaterialId);//物料id
		stockpd.put("MaterialSProp", "80108fa272884119b09eab03a78d3cc5");//辅助属性
		stockpd.put("MaterialSPropKey", "4486905b9e47428985c36c9d55d21ad7");//辅助属性值
		stockpd.put("SpecificationDesc", "");//规格描述
		stockpd.put("WarehouseID", TargetWarehouse);//仓库id
		stockpd.put("PositionID", TargetPosition);//库位id
		stockpd.put("ActualCount", DemandQuantity);//实际数量
		stockpd.put("UseCount", 0);//使用数量
		stockpd.put("UseIf", "NO");//是否占用
		stockpd.put("FUnit", "");//单位
		stockpd.put("FLevel", 1);//等级
		stockpd.put("SalesOrder", "");//销售订单
		stockpd.put("ProjectNum", "");//项目编码
		stockpd.put("QualityStatus", "合格");//质量状态
		stockpd.put("QITime", "");//质检时间
		stockpd.put("BusinessStatus", "");//业务状态
		stockpd.put("FBatch", "");//批次
		stockpd.put("CustomerID", "");//客户id
		stockpd.put("SupplierID", "");//供应商id
		stockpd.put("GuaranteePeriod", "");//保质期
		stockpd.put("PlanTrackingNumber", "");//计划跟踪号，计划工单编号
		stockpd.put("SupplierBatch", "");//供应商批次
		stockpd.put("IncomingBatch", "");//入场批次
		stockpd.put("ProductionBatch", "");//生产批次
		stockpd.put("RunningState", "在用");//运行状态，在用，停用
		stockpd.put("FCreatePersonID", staffid);//创建人id
		stockpd.put("FCreateTime", time);//创建时间
		stockpd.put("UsageTime", time);//使用时间
		stockpd.put("DateOfManufacture", "");//生产日期
		stockpd.put("IncomingSpecification", "");//入厂规格
		stockpd.put("LastModifiedTime", time);//最近修改时间
		stockpd.put("LastCountTime", time);//上次盘点时间
		stockpd.put("FExplanation", "");//备注
		stockpd.put("FStatus", "退料");//状态，入库，出库，转移，退料
		StockService.save(stockpd);//添加工厂库存
		//修改明细需求数量及目标仓位
		PageData detailpd=new PageData();
		detailpd.put("DemandQuantity", new BigDecimal(DemandQuantity));//需求数量
		detailpd.put("TargetPosition", TargetPosition);//目标仓位
		detailpd.put("MTADetails_ID", MTADetails_ID);//明细id
		MaterialTransferApplicationDetailsService.edit(detailpd);
		operationrecordService.add("","转移申请","退料","",staffid,"转移申请退料");
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 添加退料单明细，
	 * 选择车间库库存物料，变为退料单明细，根据库存id转换退料明细，
	 * 需要去手动修改每一个退料单明细的物料退料数量和目标仓位
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addBackDetail")
	//@RequiresPermissions("MaterialTransferApplicationForm:add")
	@ResponseBody
	public Object addBackDetail() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");//库存id
		String MaterialTransferApplicationForm_ID = pd.get("MaterialTransferApplicationForm_ID").toString();//退料申请单主表ID
		PageData transferForm = MaterialTransferApplicationFormService.findById(pd);//物料退料申请单
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			for(int i=0;i<ArrayDATA_IDS.length;i++) {
				PageData mta = new PageData();
				pd.put("Stock_ID", ArrayDATA_IDS[i]);
				mta=StockService.findById(pd);//根据库存id查询物料
				if(null != mta) {
					String currentTime = Tools.date2Str(new Date());
					PageData detailpd=new PageData();
					detailpd.put("MTADetails_ID", this.get32UUID());//物料退料申请单明细ID
					detailpd.put("MaterialId", mta.getString("ItemID"));//物料id
					detailpd.put("LineClose", "N");//行关闭（N/Y)
					detailpd.put("TransferOperator", staffid);//转移操作人
					detailpd.put("TransferOperationTime", currentTime);//转移操作时间
					detailpd.put("SourceRowNum", 0);//源单行号
					detailpd.put("TransferType", "单据转移");//转移类型(直接转移,单据转移)
					detailpd.put("FStatus", "待转移");//状态（待转移，已转移）
					detailpd.put("DataSources", "退料申请单");//数据来源
					detailpd.put("MaterialRequisitionID", "");//库存ID
					detailpd.put("MTA_ID", MaterialTransferApplicationForm_ID);//退料申请单主表ID
					detailpd.put("PlannedWorkOrderNum", mta.getString("MAT_AUXILIARYMX_CODE"));//计划工单编码
					detailpd.put("MaterialNum", mta.getString("MAT_CODE"));//物料编码
					detailpd.put("MaterialName", mta.getString("MAT_NAME"));//物料名称
					detailpd.put("SProp", mta.getString("MaterialSProp"));//辅助属性
					detailpd.put("SPropKey", mta.getString("MaterialSPropKey"));//辅助属性值
					detailpd.put("DemandQuantity", new BigDecimal(
							mta.containsKey("ActualCount") && null!=mta.get("ActualCount")
							?mta.get("ActualCount").toString():"0"));//需求数量
					detailpd.put("IssuedQuantity", new BigDecimal("0"));//实发数量
					detailpd.put("DeliveryWarehouse", mta.getString("WarehouseID"));//发出仓库
					detailpd.put("DeliveryPosition", mta.getString("PositionID"));//发出仓位
					detailpd.put("TargetWarehouse", "");//目标仓库
					detailpd.put("TargetPosition", "");//目标仓位
					detailpd.put("DemandTime", transferForm.getString("DemandTime"));//需求时间
					detailpd.put("DateBatch", mta.getString("ProductionBatch"));//日期批次
					detailpd.put("WP", "");//工序
					detailpd.put("FCreatePersonID", staffid);//创建人 
					detailpd.put("FCreateTime", currentTime);//创建时间
					detailpd.put("SplitQuantity", new BigDecimal("0"));//
					detailpd.put("SalesOrder", 
							mta.containsKey("SalesOrder") && null!=mta.get("SalesOrder")?mta.getString("SalesOrder"):"");//
					detailpd.put("TransitionType", "退料");//
					detailpd.put("RelationID", "");//
					detailpd.put("SplitStatus", "未拆分");//
					detailpd.put("METADATA_WHETHER", "Y");//
					detailpd.put("FIsScan", "N");
					MaterialTransferApplicationDetailsService.save(detailpd);
				}
			}
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 添加退料申请单
	 * 
	 * 把那个物料转移单改成退料单，退料单时，客户字段存物料转移申请单id
	 * 然后得选上一个目标库库位，然后我这头儿的那个仓库是哪个
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/createBack")
	//@RequiresPermissions("MaterialTransferApplicationForm:add")
	@ResponseBody
	public Object createBack() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData transferForm = MaterialTransferApplicationFormService.findById(pd);//物料转移申请单
		pd.put("MaterialTransferApplicationForm_ID", this.get32UUID());	//退料申请单主键
		/*if(Boolean.parseBoolean(pd.getString("isCodingRules"))) { //是否需要编码规则生成
			String FNum = codingRulesService.getRuleNumByRuleType("MaterialTransferApplicationFormNO").toString();//根据规则类型获取规则号
			pd.put("FNum", FNum);	//退料单号
		}*/
		pd.put("FPriorityID", new BigDecimal(pd.getString("FPriorityID")));	//优先级
		String currentTime = Tools.date2Str(new Date());
		//pd.put("DemandTime", "");	//需求时间
		pd.put("FCustomer", "MaterialTransferApplicationForm_ID");	//退料单时，客户字段存物料转移申请单id的
		pd.put("RunningState", "创建");	//运转状态（创建，执行，完成）
		pd.put("Transferor", staffid);	//转移人	// 
		pd.put("TransferTime", currentTime);	//转移时间
		pd.put("TransferType", "退料");	//转移类型（转移，退料）
		pd.put("FCreator", staffid);	//制单人	// 
		pd.put("PreparationTime", currentTime);	//制单时间
		pd.put("AuditMark", "待审");	//审核标志
		pd.put("FReviewer", staffid);	//审核人	// 
		pd.put("AuditTime", currentTime);	//审核时间
		//pd.put("TransitionType", TransitionType);	//冗余转移类型
		MaterialTransferApplicationFormService.save(pd);
		map.put("MaterialTransferApplicationForm_ID", pd.get("MaterialTransferApplicationForm_ID"));
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 转移
	 * 需要转移申请单id、转移申请单明细id
	 * 添加车间库存,扣减工厂库存 ,改转移单据和明细的状态
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/transfer")
	//@RequiresPermissions("MaterialTransferApplicationForm:add")
	@ResponseBody
	public Object transfer() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		String time = Tools.date2Str(new Date());
		//添加车间库存,扣减工厂库存 ,改转移状态,
		String MaterialTransferApplicationForm_ID=pd.getString("MaterialTransferApplicationForm_ID");//物料转移申请单id
		PageData mta = MaterialTransferApplicationDetailsService.findById(pd);//******需要MTADetails_ID******物料转移申请单明细id
		BigDecimal DemandQuantity = new BigDecimal(mta.get("DemandQuantity").toString());//需求数量
		String TargetPosition = mta.getString("TargetPosition");
		PageData lpd = new PageData();
		lpd.put("WH_LOCATION_ID", TargetPosition);
		lpd=wh_locationService.findById(lpd);
		String wareHouseId=lpd.getString("WH_WAREHOUSE_ID");
		String wareHouseCode=lpd.getString("WH_STORAGEAREA_CODE");
		//添加车间库存
		PageData stockpd=new PageData();
		stockpd.put("Stock_ID", UuidUtil.get32UUID());
		stockpd.put("StorageStatus", "车间");
		stockpd.put("QRCode", wareHouseCode+mta.getString("MaterialNum"));//二维码==仓库代码+物料代码????	TODO
		stockpd.put("OneThingCode", "");
		stockpd.put("ItemID", mta.getString("MaterialId"));//物料id
		stockpd.put("MaterialSProp", mta.getString("SProp"));//辅助属性
		stockpd.put("MaterialSPropKey", mta.containsKey("SPropKey") && null!=mta.get("SPropKey")?mta.getString("SPropKey"):"");//辅助属性值
		stockpd.put("SpecificationDesc", "");//规格描述
		stockpd.put("WarehouseID", wareHouseId);//仓库id
		stockpd.put("PositionID", TargetPosition);//库位id
		stockpd.put("ActualCount", DemandQuantity);//实际数量
		stockpd.put("UseCount", 0);//使用数量
		stockpd.put("UseIf", "NO");//是否占用
		stockpd.put("FUnit", "");//单位
		stockpd.put("FLevel", 1);//等级
		stockpd.put("SalesOrder", "");//销售订单
		stockpd.put("ProjectNum", "");//项目编码
		stockpd.put("QualityStatus", "合格");//质量状态
		stockpd.put("QITime", "");//质检时间
		stockpd.put("BusinessStatus", "");//业务状态
		stockpd.put("FBatch", "");//批次
		stockpd.put("CustomerID", "");//客户id
		stockpd.put("SupplierID", "");//供应商id
		stockpd.put("GuaranteePeriod", "");//保质期
		stockpd.put("PlanTrackingNumber", mta.getString("PlannedWorkOrderNum"));//计划跟踪号，计划工单编号
		stockpd.put("SupplierBatch", "");//供应商批次
		stockpd.put("IncomingBatch", "");//入场批次
		stockpd.put("ProductionBatch", "");//生产批次
		stockpd.put("RunningState", "在用");//运行状态，在用，停用
		stockpd.put("FCreatePersonID", staffid);//创建人id
		stockpd.put("FCreateTime", time);//创建时间
		stockpd.put("UsageTime", time);//使用时间
		stockpd.put("DateOfManufacture", "");//生产日期
		stockpd.put("IncomingSpecification", "");//入厂规格
		stockpd.put("LastModifiedTime", time);//最近修改时间
		stockpd.put("LastCountTime", time);//上次盘点时间
		stockpd.put("FExplanation", "");//备注
		stockpd.put("FStatus", "转移");//状态，入库，出库，转移，退料
		StockService.save(stockpd);//添加车间库存
		PageData spd = new PageData();
		spd.put("num", DemandQuantity);//扣减的出库数量
		spd.put("WarehouseID", mta.getString("DeliveryWarehouse"));//发出仓库id
		spd.put("ItemID", mta.getString("MaterialId"));//物料id
		spd.put("MaterialSPropKey", mta.containsKey("SPropKey") && null!=mta.get("SPropKey")?mta.getString("SPropKey"):"");//辅助属性值id
		StockService.outStock(spd);//扣减工厂库存
		mta.put("FStatus", "已转移");
		mta.put("TransferOperationTime", time);
		MaterialTransferApplicationDetailsService.edit(mta);//改物料转移申请单明细转移状态
		//查看明细中是否还有状态为待转移数据，如果有，则转移申请单主表状态改为运行，如果没有，转移申请单主表状态改为关闭
		PageData statuspd = new PageData();
		statuspd.put("MTA_ID", MaterialTransferApplicationForm_ID);
		statuspd.put("FStatus", "待转移");
		if(Integer.parseInt(MaterialTransferApplicationDetailsService.findCount(statuspd).get("zs").toString()) > 0){
			statuspd.put("RunningState", "执行");
		} else {
			statuspd.put("RunningState", "完成");
		}
		statuspd.put("MaterialTransferApplicationForm_ID", MaterialTransferApplicationForm_ID);
		MaterialTransferApplicationFormService.editRunningState(statuspd);//改物料转移申请单转移状态
		map.put("result", errInfo);
		return map;
	}
	
	/**创建物料转移申请单
	 * 通过选择工单后勾选工单下物料需求单添加物料转移申请明细
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/createTransfer")
	//@RequiresPermissions("MaterialTransferApplicationForm:add")
	@ResponseBody
	public Object createTransfer() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");//物料需求单id集合，多个用英文‘,’分隔
		if(Tools.notEmpty(DATA_IDS)){
			PageData num = new PageData();
			num = MaterialTransferApplicationFormService.findCountByFNum(pd);
			if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
				errInfo = "error";//编号重复
			} else {
				String MaterialTransferApplicationForm_ID = this.get32UUID();
				pd.put("MaterialTransferApplicationForm_ID", MaterialTransferApplicationForm_ID);	//主键
				/*if(Boolean.parseBoolean(pd.getString("isCodingRules"))) { //是否需要编码规则生成
					String FNum = codingRulesService.getRuleNumByRuleType("MaterialTransferApplicationFormNO").toString();//根据规则类型获取规则号
					pd.put("FNum", FNum);	//转移单号
				}*/
				pd.put("FPriorityID", new BigDecimal(pd.getString("FPriorityID")));	//优先级
				String currentTime = Tools.date2Str(new Date());
				//pd.put("DemandTime", currentTime);	//需求时间，前端传参
				//pd.put("FCustomer", FCustomer);	//客户，前端传参
				pd.put("RunningState", "创建");	//运转状态（创建，执行，完成）
				pd.put("Transferor", "");	//转移人	
				pd.put("TransferTime", currentTime);	//转移时间
				pd.put("TransferType", "转移");	//转移类型（转移，退料）
				pd.put("FCreator", staffid);	//制单人	
				pd.put("PreparationTime", currentTime);	//制单时间
				pd.put("AuditMark", "待审");	//审核标志
				pd.put("FReviewer", "");	//审核人 
				pd.put("AuditTime", currentTime);	//审核时间
				pd.put("TransitionType", "转出");	//运转状态(转出、转入)
				MaterialTransferApplicationFormService.save(pd);
				
				JSONArray jarr = JSONArray.fromObject(pd.getString("arr"));
				String ArrayDATA_IDS[]=new String[jarr.size()];
				String planWorkOrderId="";
				for(int i=0;i<jarr.size();i++) {
					PageData temp = new PageData();
					JSONObject o = jarr.getJSONObject(i);
					Iterator it =o.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Object, Object> entry = (Entry<Object, Object>) it.next();
						temp.put(entry.getKey(), entry.getValue());
					}
					PageData detailpd=new PageData();
					detailpd.put("MTADetails_ID", this.get32UUID());//物料转移申请单明细ID
					detailpd.put("LineClose", "N");//行关闭（N/Y)
					detailpd.put("TransferOperator", staffid);//转移操作人 
					detailpd.put("TransferOperationTime", currentTime);//转移操作时间
					detailpd.put("SourceRowNum", (i+1));//源单行号
					detailpd.put("TransferType", "单据转移");//转移类型(直接转移,单据转移)
					detailpd.put("FStatus", "待转移");//状态（待转移，已转移）
					detailpd.put("DataSources", "物料需求单");//数据来源
					detailpd.put("MaterialRequisitionID", "");//物料需求单ID
					detailpd.put("MTA_ID", MaterialTransferApplicationForm_ID);//物料转移申请单主表ID
					detailpd.put("PlannedWorkOrderNum", temp.getString("FWorkOrderNum"));//计划工单编号
					planWorkOrderId=temp.getString("PlanningWorkOrderID");//计划工单ID
					detailpd.put("MaterialNum", temp.getString("MaterialNum"));//物料编码
					detailpd.put("MaterialName", temp.getString("MaterialName"));//物料名称
					detailpd.put("SProp", "");//辅助属性
					detailpd.put("SPropKey", temp.getString("SPropKey"));//辅助属性值
					detailpd.put("DemandQuantity", new BigDecimal(temp.containsKey("DemandCount") && null!=temp.get("DemandCount")?
									temp.get("DemandCount").toString():"0"));//需求数量
					detailpd.put("IssuedQuantity", new BigDecimal(temp.containsKey("IssuedQuantity") && null!=temp.get("IssuedQuantity")?
									temp.get("IssuedQuantity").toString():"0"));//实发数量
					detailpd.put("DeliveryWarehouse", "");//发出仓库
					detailpd.put("TargetPosition", "");//目标仓位
					detailpd.put("DemandTime", "");//需求时间
					detailpd.put("DateBatch", temp.getString("BatchNum"));//日期批次
					detailpd.put("WP", "");//工序
					detailpd.put("FCreatePersonID", staffid);//创建人  
					detailpd.put("FCreateTime", currentTime);//创建时间
					detailpd.put("MaterialId", temp.getString("MaterialID"));//物料id
					ArrayDATA_IDS[i]=temp.getString("MaterialID");
					detailpd.put("TargetWarehouse", "");//目标仓库
					detailpd.put("DeliveryPosition", "");//发出仓位
					detailpd.put("SplitQuantity", temp.containsKey("DemandCount") && null!=temp.get("DemandCount")?
							temp.get("DemandCount").toString():"0");//拆分数量
					detailpd.put("SalesOrder", temp.get("FOrderNum"));//销售订单
					detailpd.put("TransitionType", "转出");//运行状态，转出、转入
					detailpd.put("METADATA_WHETHER", "Y");//是否元数据，Y/N
					detailpd.put("RelationID", "");//关联id，元数据为空
					detailpd.put("SplitStatus", "未拆分");//拆分状态，未拆分、已拆分
					detailpd.put("FIsScan", "N");
					MaterialTransferApplicationDetailsService.save(detailpd);
				}
				
				/*String ArrayDATA_IDS[] = DATA_IDS.split(",");
				for(int i=0;i<ArrayDATA_IDS.length;i++) {
					PageData mta = new PageData();
					pd.put("MaterialRequirement_ID", ArrayDATA_IDS[i]);
					mta=MaterialRequirementService.findById(pd);//根据物料需求单id查询需求物料
					if(null != mta) {
						PageData detailpd=new PageData();
						detailpd.put("MTADetails_ID", this.get32UUID());//物料转移申请单明细ID
						detailpd.put("LineClose", "N");//行关闭（N/Y)
						detailpd.put("TransferOperator", staffid);//转移操作人 
						detailpd.put("TransferOperationTime", currentTime);//转移操作时间
						detailpd.put("SourceRowNum", Integer.parseInt(mta.get("RowNum").toString()));//源单行号
						detailpd.put("TransferType", "单据转移");//转移类型(直接转移,单据转移)
						detailpd.put("FStatus", "待转移");//状态（待转移，已转移）
						detailpd.put("DataSources", "物料需求单");//数据来源
						detailpd.put("MaterialRequisitionID", mta.get("MaterialRequirement_ID").toString());//物料需求单ID
						detailpd.put("MTA_ID", MaterialTransferApplicationForm_ID);//物料转移申请单主表ID
						detailpd.put("PlannedWorkOrderNum", mta.getString("WorkOrderNum"));//计划工单编号
						detailpd.put("MaterialNum", mta.getString("MaterialNum"));//物料编码
						detailpd.put("MaterialName", mta.getString("MaterialName"));//物料名称
						detailpd.put("SProp", mta.getString("SProp"));//辅助属性
						detailpd.put("SPropKey", mta.getString("SPropKey"));//辅助属性值
						detailpd.put("DemandQuantity", new BigDecimal(
								mta.containsKey("DemandCount") && null!=mta.get("DemandCount")?mta.get("DemandCount").toString():"0"));//需求数量
						detailpd.put("IssuedQuantity", new BigDecimal(
								mta.containsKey("IssuedQuantity") && null!=mta.get("IssuedQuantity")?mta.get("IssuedQuantity").toString():"0"));//实发数量
						detailpd.put("DeliveryWarehouse", mta.getString("DeliveryWarehouse"));//发出仓库
						detailpd.put("TargetPosition", mta.getString("TargetPosition"));//目标仓位
						detailpd.put("DemandTime", mta.getString("DemandTime"));//需求时间
						detailpd.put("DateBatch", mta.getString("DateBatch"));//日期批次
						detailpd.put("WP", mta.getString("WP"));//工序
						detailpd.put("FCreatePersonID", staffid);//创建人  
						detailpd.put("FCreateTime", currentTime);//创建时间
						detailpd.put("MaterialId", mta.getString("MaterialID"));//物料id
						detailpd.put("TargetWarehouse", mta.getString("TargetWarehouse"));//目标仓库
						detailpd.put("DeliveryPosition", mta.getString("DeliveryPosition"));//发出仓位
						detailpd.put("SplitQuantity", mta.containsKey("DemandCount") && null!=mta.get("DemandCount")?
								mta.get("DemandCount").toString():"0");//拆分数量
						detailpd.put("SalesOrder", "");//销售订单
						detailpd.put("TransitionType", "转出");//运行状态，转出、转入
						detailpd.put("METADATA_WHETHER", "Y");//是否元数据，Y/N
						detailpd.put("RelationID", "");//关联id，元数据为空
						detailpd.put("SplitStatus", "未拆分");//拆分状态，未拆分、已拆分
						detailpd.put("FIsScan", "N");
						MaterialTransferApplicationDetailsService.save(detailpd);
					}
				}*/
				// 修改物料需求单的状态为‘Y’
				PageData materialRequirement=new PageData();
				materialRequirement.put("array", ArrayDATA_IDS);
				materialRequirement.put("PushDownTransferIF", "Y");
				materialRequirement.put("PlanningWorkOrderID", planWorkOrderId);
				MaterialRequirementService.updateTransferStateByWorkOrderIdAndMaterialId(materialRequirement);
				map.put("MaterialTransferApplicationForm_ID",MaterialTransferApplicationForm_ID);
			}
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("MaterialTransferApplicationForm:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		pd = this.getPageData();
		pd.put("MaterialTransferApplicationForm_ID", this.get32UUID());	//主键
		if(Boolean.parseBoolean(pd.getString("isCodingRules"))) { //是否需要编码规则生成
			String FNum = codingRulesService.getRuleNumByRuleType("MaterialTransferApplicationFormNO").toString();//根据规则类型获取规则号
			pd.put("FNum", FNum);	//转移单号
		}
		String currentTime = Tools.date2Str(new Date());
		pd.put("RunningState", "待转");	//运转状态（待转，运行，关闭）
		pd.put("Transferor", staffid);	//转移人	// 
		pd.put("TransferTime", currentTime);	//转移时间
		pd.put("TransferType", "转移");	//转移类型（转移，退料）
		pd.put("FCreator", staffid);	//制单人
		pd.put("PreparationTime", currentTime);	//制单时间
		pd.put("AuditMark", "待审");	//审核标志
		pd.put("FReviewer", staffid);	//审核人	// 
		pd.put("AuditTime", currentTime);	//审核时间
		MaterialTransferApplicationFormService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("MaterialTransferApplicationForm:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MTA_ID", pd.getString("MaterialTransferApplicationForm_ID"));
		try{
			List<PageData> list = MaterialTransferApplicationDetailsService.listAll(pd);
			MaterialTransferApplicationDetailsService.deleteByMTA_ID(pd);
			for(PageData p : list) {
				p.put("FRelatedID", p.getString("MTADetails_ID"));
				MaterialTransferSplitService.deleteByFRelatedID(p);
			}
			MaterialTransferApplicationFormService.delete(pd);
			operationrecordService.add("","转移申请","删除","",staffid,"转移申请删除");
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
	//@RequiresPermissions("MaterialTransferApplicationForm:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = MaterialTransferApplicationFormService.findCountByFNum(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			MaterialTransferApplicationFormService.edit(pd);
			operationrecordService.add("","转移申请","修改","",staffid,"转移申请修改");
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**编辑运转状态：创建，执行，关闭，下发操作
	 * @param pd.RunningState:创建，执行，关闭，下发
	 * @param pd.TransitionType:转出、转入
	 * @throws Exception
	 */
	@RequestMapping(value="/editRunningState")
	//@RequiresPermissions("MaterialTransferApplicationForm:edit")
	@ResponseBody
	public Object editRunningState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialTransferApplicationFormService.editRunningState(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("MaterialTransferApplicationForm:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = MaterialTransferApplicationFormService.list(page);	//列出MaterialTransferApplicationForm列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**下拉列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/formList")
	//@RequiresPermissions("MaterialTransferApplicationForm:list")
	@ResponseBody
	public Object formList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = MaterialTransferApplicationFormService.listAll(pd);	//列出MaterialTransferApplicationForm列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("MaterialTransferApplicationForm:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = MaterialTransferApplicationFormService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("MaterialTransferApplicationForm:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			MaterialTransferApplicationFormService.deleteAll(ArrayDATA_IDS);
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
		titles.add("ID");	//1
		titles.add("转移单号");	//2
		titles.add("优先级");	//3
		titles.add("需求时间");	//4
		titles.add("客户");	//5
		titles.add("运转状态");	//6
		titles.add("转移人");	//7
		titles.add("转移时间");	//8
		titles.add("转移类型");	//9
		titles.add("制单人");	//10
		titles.add("制单时间");	//11
		titles.add("审核标志");	//12
		titles.add("审核人");	//13
		titles.add("审核时间");	//14
		titles.add("转移类型");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = MaterialTransferApplicationFormService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MaterialTransferApplicationForm_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNum"));	    //2
			vpd.put("var3", varOList.get(i).get("FPriorityID").toString());	//3
			vpd.put("var4", varOList.get(i).getString("DemandTime"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCustomer"));	    //5
			vpd.put("var6", varOList.get(i).getString("RunningState"));	    //6
			vpd.put("var7", varOList.get(i).getString("Transferor"));	    //7
			vpd.put("var8", varOList.get(i).getString("TransferTime"));	    //8
			vpd.put("var9", varOList.get(i).getString("TransferType"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCreator"));	    //10
			vpd.put("var11", varOList.get(i).getString("PreparationTime"));	    //11
			vpd.put("var12", varOList.get(i).getString("AuditMark"));	    //12
			vpd.put("var13", varOList.get(i).getString("FReviewer"));	    //13
			vpd.put("var14", varOList.get(i).getString("AuditTime"));	    //14
			vpd.put("var15", varOList.get(i).getString("TransitionType"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
