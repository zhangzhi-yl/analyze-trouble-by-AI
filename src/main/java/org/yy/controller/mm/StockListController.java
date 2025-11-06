package org.yy.controller.mm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.bag.CollectionSortedBag;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.MATSPLITService;
import org.yy.service.mm.StockListDetailService;
import org.yy.service.mm.StockListService;
import org.yy.service.mm.StockOperationRecordService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.PurchaseMaterialDetailsService;

/** 
 * 说明：出入库单
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/StockList")
public class StockListController extends BaseController {
	
	@Autowired
	private StockListService StockListService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private StockListDetailService StockListDetailService;
	@Autowired
	private StockService StockService;
	@Autowired
	private PurchaseMaterialDetailsService PurchaseMaterialDetailsService;
	@Autowired
	private MATSPLITService MATSPLITService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private StockOperationRecordService StockOperationRecordService;//库存操作记录
	
	/**入库单新增
	 * @author 管悦
	 * @date 2020-11-15
	 * @param DocumentTypeInOut：区分出入库、入库/出库
	 * @throws Exception
	 */
	@RequestMapping(value="/addIn")
	//@RequiresPermissions("StockList:add")
	@ResponseBody
	public Object addIn() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = StockListService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {	
			pd.put("StockList_ID", this.get32UUID());	//主键
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
			//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
			pd.put("FMakeBillsTime", Tools.date2Str(new Date()));
			pd.put("AuditMark", "N");
			pd.put("FStatus", "未开始");
			StockListService.save(pd);
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem",pd.get("DocumentTypeInOut")+"单");//功能项
			pdOp.put("OperationType", "新增");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("StockList_ID"));
			operationrecordService.save(pdOp);
			map.put("StockList_ID", pd.get("StockList_ID"));
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**入库单删除
	 * @author 管悦
	 * @date 2020-11-15
	 * @param DocumentTypeInOut：区分出入库
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteIn")
	//@RequiresPermissions("StockList:del")
	@ResponseBody
	public Object deleteIn() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdSave = StockListDetailService.findById(pd);	//根据ID读取
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
		StockListService.delete(pd);
		StockListDetailService.rowCloseByMainId(pd);//关联行关闭
		PurchaseMaterialDetailsService.calFPushCount(pdSave);//反写采购订单明细下推数量
		StockListDetailService.calFPushCount(pdSave);//反写入库单明细下推数量
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem",pd.get("DocumentTypeInOut")+"单");//功能项
		pdOp.put("OperationType", "删除");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("StockList_ID"));
		operationrecordService.save(pdOp);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**入库单修改
	 * @author 管悦
	 * @date 2020-11-15
	 * @param DocumentTypeInOut：区分出入库
	 * @throws Exception
	 */
	@RequestMapping(value="/editIn")
	//@RequiresPermissions("StockList:edit")
	@ResponseBody
	public Object editIn() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = StockListService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {	
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
			//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
			StockListService.edit(pd);
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem",pd.get("DocumentTypeInOut")+"单");//功能项
			pdOp.put("OperationType", "修改");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("StockList_ID"));
			operationrecordService.save(pdOp);
		}		
		map.put("result", errInfo);
		return map;
	}
	
	/**入库单列表
	 * @author 管悦
	 * @date 2020-11-15
	 * @param DocumentTypeInOut：区分出入库、入库/出库
	 * @throws Exception
	 */
	@RequestMapping(value="/listIn")
	//@RequiresPermissions("StockList:list")
	@ResponseBody
	public Object listIn(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_DocumentNo = pd.getString("KEYWORDS_DocumentNo");						//入库单号
		if(Tools.notEmpty(KEYWORDS_DocumentNo))pd.put("KEYWORDS_DocumentNo", KEYWORDS_DocumentNo.trim());
		String KEYWORDS_AuditMark = pd.getString("KEYWORDS_AuditMark");						//审核状态
		if(Tools.notEmpty(KEYWORDS_AuditMark))pd.put("KEYWORDS_AuditMark", KEYWORDS_AuditMark.trim());
		String KEYWORDS_FCreator = pd.getString("KEYWORDS_FCreator");						//创建人
		if(Tools.notEmpty(KEYWORDS_FCreator))pd.put("KEYWORDS_FCreator", KEYWORDS_FCreator.trim());
		page.setPd(pd);
		List<PageData>	varList = StockListService.list(page);	
		//插入操作日志
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", pd.get("DocumentTypeInOut")+"单");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", "");//删改数据ID	
		operationrecordService.save(pdOp);	
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**根据ID获取详情
	 * @author 管悦
	 * @date 2020-11-15
	 * @param DocumentTypeInOut：区分出入库、入库/出库
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("StockList:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = StockListService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("StockList:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			StockListService.deleteAll(ArrayDATA_IDS);
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
		titles.add("出入库单号");	//1
		titles.add("单据类型(红字,篮字)");	//2
		titles.add("单据类型(出库,入库)");	//3
		titles.add("客户");	//4
		titles.add("收货人");	//5
		titles.add("收货地址");	//6
		titles.add("交货人");	//7
		titles.add("交货日期");	//8
		titles.add("交货地点");	//9
		titles.add("制单人");	//10
		titles.add("制单时间");	//11
		titles.add("审核标志");	//12
		titles.add("审核人");	//13
		titles.add("审核时间");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = StockListService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("DocumentNo"));	    //1
			vpd.put("var2", varOList.get(i).getString("DocumentTypeBlueRed"));	    //2
			vpd.put("var3", varOList.get(i).getString("DocumentTypeInOut"));	    //3
			vpd.put("var4", varOList.get(i).getString("FCustomer"));	    //4
			vpd.put("var5", varOList.get(i).getString("FConsignee"));	    //5
			vpd.put("var6", varOList.get(i).getString("ShippingAddress"));	    //6
			vpd.put("var7", varOList.get(i).getString("DeliveredBy"));	    //7
			vpd.put("var8", varOList.get(i).getString("DeliveryDate"));	    //8
			vpd.put("var9", varOList.get(i).getString("PlaceOfDelivery"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCreator"));	    //10
			vpd.put("var11", varOList.get(i).getString("FMakeBillsTime"));	    //11
			vpd.put("var12", varOList.get(i).getString("AuditMark"));	    //12
			vpd.put("var13", varOList.get(i).getString("FReviewer"));	    //13
			vpd.put("var14", varOList.get(i).getString("AuditTime"));	    //14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**审核单据，查询入库单明细及扫码进行物料拆分的物料（支持一物一码物料）进行影响库存，添加mm_stock表数据
	 * 验证是否存在明细，如果不存在将无法通过，验证所有明细是否都选择了仓库库位，如果存在没选择库位的明细，则无法通过审核
	 * @author 宋
	 * @date 2020-12-10
	 * @param 
	 * StockList_ID:单据ID
	 * AuditMark:审核标志（Y、N）
	 * @throws Exception
	 */
	@RequestMapping(value="/editAuditIn")
	//@RequiresPermissions("salesorder:del")
	@ResponseBody
	public Object editAuditIn() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdKC = new PageData();
		pd = this.getPageData();
		String time=Tools.date2Str(new Date());
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		//查询出入库单明细（不含支持一物一码物料）及二级明细（唯一码物料）,插入库存
		List<PageData> varList=StockListDetailService.listAllAudit(pd);//pd.StockList_ID，出入库单id
		if(!CollectionUtils.isEmpty(varList)) {
			for(PageData temp: varList) {
				if("".equals(temp.getString("PositionID")) || "".equals(temp.getString("WarehouseID")) 
						|| null==temp.getString("PositionID") || null==temp.getString("WarehouseID")) {
					errInfo="error";
				}
			}
			if("success".equals(errInfo)) {
				for(PageData pdVar : varList) {
					String Stock_ID = this.get32UUID();
					pdKC.put("Stock_ID", Stock_ID);	//主键
					pdKC.put("StorageStatus", pdVar.getString("SubordinateInventory")==null?"":pdVar.getString("SubordinateInventory").substring(0, 2));	
					pdKC.put("QRCode", pdVar.getString("MAT_CODE"));//二维码（物料代码）
					if("是".equals(pdVar.getString("UNIQUE_CODE_WHETHER"))) {
						pdKC.put("OneThingCode", pdVar.getString("OneThingCode"));//一物码
					} else {
						pdKC.put("OneThingCode", "");//一物码
					}
					pdKC.put("ItemID", pdVar.getString("ItemID"));
					pdKC.put("MaterialSProp", pdVar.getString("MaterialSProp"));
					pdKC.put("MaterialSPropKey", pdVar.getString("MaterialSPropKey"));
					pdKC.put("SpecificationDesc", pdVar.getString("MAT_SPECS"));
					pdKC.put("WarehouseID", pdVar.getString("WarehouseID"));
					pdKC.put("PositionID", pdVar.getString("PositionID"));
					pdKC.put("ActualCount", pdVar.get("MaterialQuantity")==null?"0":pdVar.get("MaterialQuantity").toString());//
					pdKC.put("UseCount", "0");
					pdKC.put("UseIf", "NO");//是否占用
					pdKC.put("FUnit", pdVar.getString("MAT_MAIN_UNIT"));	
					pdKC.put("FLevel", 1);//等级
					pdKC.put("SalesOrder", "");//销售订单
					pdKC.put("ProjectNum", "");//项目编码
					pdKC.put("QualityStatus", "合格");//质量状态
					pdKC.put("QITime", time);//质检时间
					pdKC.put("BusinessStatus", "");//业务状态
					pdKC.put("FBatch", "");//批次
					pdKC.put("CustomerID", "");//客户id
					pdKC.put("SupplierID", "");//供应商id
					pdKC.put("GuaranteePeriod", "");//保质期
					pdKC.put("PlanTrackingNumber", "");//pdVar.getString("WorkOrderNum"));	
					pdKC.put("SupplierBatch", "");//供应商批次
					pdKC.put("IncomingBatch", "");//入场批次
					pdKC.put("ProductionBatch", pdVar.getString("MaterialBatchNumber"));
					pdKC.put("RunningState", "在用");
					pdKC.put("FCreatePersonID", staffid);//staffService.getStaffId(pdKC).getString("STAFF_ID"));//查询职员ID
					pdKC.put("FCreateTime", time);
					pdKC.put("UsageTime", time);//使用时间
					pdKC.put("DateOfManufacture", "");//生产日期
					pdKC.put("IncomingSpecification", "");//入厂规格
					pdKC.put("LastModifiedTime", time);
					pdKC.put("LastCountTime", time);//上次盘点时间
					pdKC.put("FExplanation", "");//备注
					pdKC.put("FStatus", "入库");	
					//pdKC.put("UNIQUE_CODE_WHETHER", pdVar.getString("UNIQUE_CODE_WHETHER"));
					//pdKC.put("FRelatedID", pdVar.getString("StockListDetail_ID"));	
					StockService.inStock(pdKC);
					StockOperationRecordService.add(Stock_ID,"入库单审核",
							pdVar.get("MaterialQuantity")==null?"0":pdVar.get("MaterialQuantity").toString(),
							pdVar.getString("WarehouseID"),pdVar.getString("PositionID"),
							"入库蓝字",pdVar.getString("ItemID"),staffid);
				}
				pd.put("FReviewer",staffid);// 职员ID
				pd.put("AuditTime", Tools.date2Str(new Date()));
				StockListService.editAudit(pd);
			} else {
				errInfo="locationOrWareHourseIsInvaild";
			}
		} else {
			errInfo="error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**审核或反审核单据
	 * @author 管悦
	 * @date 2020-11-06
	 * @param 
	 * StockList_ID:单据ID
	 * AuditMark:审核标志（Y、N）
	 * DocumentTypeInOut：区分出入库、入库/出库
	 * @throws Exception
	 */
	@RequestMapping(value="/editAuditIn13")
	//@RequiresPermissions("salesorder:del")
	@ResponseBody
	public Object editAuditIn13() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DocumentTypeInOut=pd.getString("DocumentTypeInOut");
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FReviewer", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FReviewer", "c3e8a7d350cc43d9b9e87641947168b8");
		pd.put("AuditTime", Tools.date2Str(new Date()));
		StockListService.editAudit(pd);
		//int countKC=0;
		//插入库存
		List<PageData> varList=StockListDetailService.listAllAudit(pd);
		PageData pdKC=new PageData();
		for(int i=0;i<varList.size();i++) {
			if(DocumentTypeInOut.equals("入库") && varList.get(i).getString("DocumentTypeBlueRed").equals("红字")) {
				PageData pdKCx=new PageData();
				pdKCx.put("WarehouseID", varList.get(i).getString("WarehouseID"));
				pdKCx.put("ItemID", varList.get(i).getString("FCode"));
				pdKCx.put("MaterialSPropKey", varList.get(i).getString("MaterialSPropKey"));
				PageData pdStockSum=StockService.getSum(pdKCx);//即时库存
				if(pdStockSum == null || Double.parseDouble(pdStockSum.get("stockSum").toString())<=0) {
					errInfo = "fail1";//无库存
					break;
				}
			}else if(DocumentTypeInOut.equals("出库") && varList.get(i).getString("DocumentTypeBlueRed").equals("蓝字")) {
				PageData pdKCx=new PageData();
				pdKCx.put("WarehouseID", varList.get(i).getString("WarehouseID"));
				pdKCx.put("ItemID", varList.get(i).getString("FCode"));
				pdKCx.put("MaterialSPropKey", varList.get(i).getString("MaterialSPropKey"));
				PageData pdStockSum=StockService.getSum(pdKCx);//即时库存
				if(pdStockSum == null || Double.parseDouble(pdStockSum.get("stockSum").toString())<=0) {
					errInfo = "fail1";//无库存
					break;
				}
			}
			PageData pdcc=new PageData();
			pdcc.put("FRelatedID", varList.get(i).getString("StockListDetail_ID"));
			PageData pdNum =MATSPLITService.getNum(pdcc);//获取拆分物料数量
			if(null !=varList.get(i).getString("UNIQUE_CODE_WHETHER") && varList.get(i).getString("UNIQUE_CODE_WHETHER").equals("是") && (pdNum == null || Integer.parseInt(pdNum.get("FNum").toString())==0)) {
				errInfo = "fail2";//不能再次拆分
				break;
			}
		}
		if(!errInfo.equals("fail1") && !errInfo.equals("fail2")) {
		for(PageData pdVar:varList) {
			pdKC.put("Stock_ID", this.get32UUID());	//主键
			pdKC.put("ItemID", pdVar.getString("FCode"));	
			pdKC.put("StorageStatus", pdVar.getString("SubordinateInventory")==null?"":pdVar.getString("SubordinateInventory").substring(0, 2));	
			pdKC.put("WarehouseID", pdVar.getString("WarehouseID"));	
			pdKC.put("FUnit", pdVar.getString("FUnitName"));	
			pdKC.put("MaterialSPropKey", pdVar.getString("MaterialSPropKey"));	
			pdKC.put("RunningState", "在用");	
			pdKC.put("FStatus", "入库");	
			pdKC.put("UseCount", "0");	
			pdKC.put("PlanTrackingNumber", pdVar.getString("WorkOrderNum"));	
			pdKC.put("FNAME", Jurisdiction.getName());
			pdKC.put("FCreatePersonID", staffService.getStaffId(pdKC).getString("STAFF_ID"));//查询职员ID
			//pdKC.put("FCreatePersonID", "c3e8a7d350cc43d9b9e87641947168b8");
			pdKC.put("FCreateTime", Tools.date2Str(new Date()));
			pdKC.put("LastModifiedTime", Tools.date2Str(new Date()));
			if(DocumentTypeInOut.equals("入库") && pdVar.getString("DocumentTypeBlueRed").equals("红字")) {
				pdKC.put("num", pdVar.get("MaterialQuantity")==null?"0":pdVar.get("MaterialQuantity").toString());//
				StockService.inStockRed(pdKC);
			}else if(DocumentTypeInOut.equals("入库") && pdVar.getString("DocumentTypeBlueRed").equals("蓝字")) {
				pdKC.put("ActualCount", pdVar.get("MaterialQuantity")==null?"0":pdVar.get("MaterialQuantity").toString());//
				pdKC.put("PositionID", pdVar.getString("PositionID"));	
				pdKC.put("ProductionBatch", pdVar.getString("MaterialBatchNumber"));
				pdKC.put("UNIQUE_CODE_WHETHER", pdVar.getString("UNIQUE_CODE_WHETHER"));
				pdKC.put("FRelatedID", pdVar.getString("StockListDetail_ID"));	
				StockService.inStock(pdKC);
			}
		}
		//插入操作日志
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**获取编号列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-16
	 * @param pd
	 * DocumentTypeBlueRed
	 * DocumentTypeInOut
	 * @throws Exception
	 */
	@RequestMapping(value="/getDocumentNoList")
	@ResponseBody
	public Object getDocumentNoList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = StockListService.getDocumentNoList(pd);	
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**入厂记录列表
	 * @author 管悦
	 * @date 2020-11-17
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/listInRecord")
	@ResponseBody
	public Object listInRecord(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_QRCodeInformation = pd.getString("KEYWORDS_QRCodeInformation");						//二维码信息
		if(Tools.notEmpty(KEYWORDS_QRCodeInformation))pd.put("KEYWORDS_QRCodeInformation", KEYWORDS_QRCodeInformation.trim());
		page.setPd(pd);
		List<PageData>	varList = StockListService.listInRecord(page);	
		//插入操作日志
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "入厂记录");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", "");//删改数据ID	
		operationrecordService.save(pdOp);	
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	 /**导出入厂记录到excel
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/exportInRecord")
		public ModelAndView exportInRecord() throws Exception{
			ModelAndView mv = new ModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("二维码");	//1
			titles.add("物料编号/名称");	//2
			titles.add("数量");	//3
			titles.add("入库仓库");	//4
			titles.add("入库仓位");	//4
			titles.add("辅助属性值");	//5
			titles.add("行关闭");	//6
			titles.add("客户");	//7
			titles.add("入库单号");	//8
			titles.add("类型");	//8
			titles.add("收货人");	//9
			titles.add("收货地址");	//10
			titles.add("交货人");	//11
			titles.add("交货日期");	//12
			titles.add("交货地点");	//13
			titles.add("制单人");	//14
			titles.add("制单时间");	//15
			titles.add("审核标志");	//16
			titles.add("审核人");	//17
			titles.add("审核时间");	//18
			dataMap.put("titles", titles);
			List<PageData> varOList = StockListService.exportInRecord(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("QRCodeInformation"));	    //1
				vpd.put("var2", varOList.get(i).getString("FMatJoint"));	    //2
				vpd.put("var3", (varOList.get(i).get("MaterialQuantity")==null?"":varOList.get(i).get("MaterialQuantity")).toString());	    //3
				vpd.put("var4", varOList.get(i).getString("FWAREHOUSEJoint"));	    //4
				vpd.put("var5", varOList.get(i).getString("FLOCATIONJoint"));	    //4
				vpd.put("var6", varOList.get(i).getString("MAT_AUXILIARYMX_NAME"));	    //5
				vpd.put("var7", varOList.get(i).getString("LineCloseIf"));	    //6
				vpd.put("var8", varOList.get(i).getString("FCustomerJoint"));	    //7
				vpd.put("var9", varOList.get(i).getString("DocumentNo"));	    //8
				vpd.put("var10", varOList.get(i).getString("DocumentTypeBlueRed"));	    //4
				vpd.put("var11", varOList.get(i).getString("FConsignee"));	    //5
				vpd.put("var12", varOList.get(i).getString("ShippingAddress"));	    //6
				vpd.put("var13", varOList.get(i).getString("DeliveredBy"));	    //7
				vpd.put("var14", varOList.get(i).getString("DeliveryDate"));	    //8
				vpd.put("var15", varOList.get(i).getString("PlaceOfDelivery"));	    //9
				vpd.put("var16", varOList.get(i).getString("FCreatorName"));	    //10
				vpd.put("var17", varOList.get(i).getString("FMakeBillsTime"));	    //11
				vpd.put("var18", varOList.get(i).getString("AuditMark"));	    //12
				vpd.put("var19", varOList.get(i).getString("FReviewerName"));	    //13
				vpd.put("var20", varOList.get(i).getString("AuditTime"));	    //14
				
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
			return mv;
		}
		/**出库单新增
		 * @author 管悦
		 * @date 2020-11-17
		 * @param DocumentTypeInOut：区分出入库、入库/出库
		 * @throws Exception
		 */
		@RequestMapping(value="/addOut")
		//@RequiresPermissions("StockList:add")
		@ResponseBody
		public Object addOut() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			//单号验重
			PageData pdNum = StockListService.getRepeatNum(pd);
			if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
				errInfo = "fail1";//单号重复
			}else {	
			pd.put("StockList_ID", this.get32UUID());	//主键
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
			//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
			pd.put("FMakeBillsTime", Tools.date2Str(new Date()));
			pd.put("AuditMark", "N");
			pd.put("FStatus", "未开始");
			StockListService.save(pd);
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem",pd.get("DocumentTypeInOut")+"单");//功能项
			pdOp.put("OperationType", "新增");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("StockList_ID"));
			operationrecordService.save(pdOp);
			}
			map.put("result", errInfo);
			return map;
		}
		/**出库单删除
		 * @author 管悦
		 * @date 2020-11-17
		 * @param DocumentTypeInOut：区分出入库
		 * @throws Exception
		 */
		@RequestMapping(value="/deleteOut")
		//@RequiresPermissions("StockList:del")
		@ResponseBody
		public Object deleteOut() throws Exception{
			Map<String,String> map = new HashMap<String,String>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pdSave = StockListDetailService.findById(pd);	//根据ID读取
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
			//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
			StockListService.delete(pd);
			//StockListDetailService.rowCloseByMainId(pd);//关联行关闭
			StockListDetailService.deleteByStockList_ID(pd);
			//PurchaseMaterialDetailsService.calFPushCount(pdSave);//反写采购订单明细下推数量
			//StockListDetailService.calFPushCount(pdSave);//反写入库单明细下推数量
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem",pd.get("DocumentTypeInOut")+"单");//功能项
			pdOp.put("OperationType", "删除");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("StockList_ID"));
			operationrecordService.save(pdOp);
			map.put("result", errInfo);				//返回结果
			return map;
		}
		
		/**出库单修改
		 * @author 管悦
		 * @date 2020-11-17
		 * @param DocumentTypeInOut：区分出入库
		 * @throws Exception
		 */
		@RequestMapping(value="/editOut")
		//@RequiresPermissions("StockList:edit")
		@ResponseBody
		public Object editOut() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			//单号验重
			PageData pdNum = StockListService.getRepeatNum(pd);
			if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
				errInfo = "fail1";//单号重复
			}else {	
				pd.put("FNAME", Jurisdiction.getName());
				pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
				//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
				StockListService.edit(pd);
				//插入操作日志
				PageData pdOp=new PageData();
				pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
				pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
				pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
				pdOp.put("FunctionType", "");//功能类型
				pdOp.put("FunctionItem",pd.get("DocumentTypeInOut")+"单");//功能项
				pdOp.put("OperationType", "修改");//操作类型
				pdOp.put("Fdescribe", "");//描述
				pdOp.put("DeleteTagID", pd.get("StockList_ID"));
				operationrecordService.save(pdOp);
			}		
			map.put("result", errInfo);
			return map;
		}
		
		/**出库单列表
		 * @author 管悦
		 * @date 2020-11-17
		 * @param DocumentTypeInOut：区分出入库、入库/出库
		 * @throws Exception
		 */
		@RequestMapping(value="/listOut")
		//@RequiresPermissions("StockList:list")
		@ResponseBody
		public Object listOut(Page page) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String KEYWORDS_DocumentNo = pd.getString("KEYWORDS_DocumentNo");						//入库单号
			if(Tools.notEmpty(KEYWORDS_DocumentNo))pd.put("KEYWORDS_DocumentNo", KEYWORDS_DocumentNo.trim());
			String KEYWORDS_AuditMark = pd.getString("KEYWORDS_AuditMark");						//审核状态
			if(Tools.notEmpty(KEYWORDS_AuditMark))pd.put("KEYWORDS_AuditMark", KEYWORDS_AuditMark.trim());
			String KEYWORDS_FCreator = pd.getString("KEYWORDS_FCreator");						//创建人
			if(Tools.notEmpty(KEYWORDS_FCreator))pd.put("KEYWORDS_FCreator", KEYWORDS_FCreator.trim());
			page.setPd(pd);
			List<PageData>	varList = StockListService.list(page);	
			//插入操作日志
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
			//pd.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", pd.get("DocumentTypeInOut")+"单");//功能项
			pdOp.put("OperationType", "查询");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", "");//删改数据ID	
			operationrecordService.save(pdOp);	
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", errInfo);
			return map;
		}
		/**出库审核，扣减库存
		 * @author 管悦
		 * @date 2020-11-17
		 * @param 
		 * StockList_ID:单据ID
		 * AuditMark:审核标志（Y、N）
		 * DocumentTypeInOut：区分出入库、入库/出库
		 * @throws Exception
		 */
		@RequestMapping(value="/editAuditOut")
		//@RequiresPermissions("salesorder:del")
		@ResponseBody
		public Object editAuditOut() throws Exception{
			Map<String,String> map = new HashMap<String,String>();
			String errInfo = "success";
			PageData staff = new PageData();
			staff.put("FNAME", Jurisdiction.getName());
			staff=staffService.getStaffId(staff);
			String staffid=null==staff?"":staff.getString("STAFF_ID");
			PageData pd = new PageData();
			pd = this.getPageData();
			String DocumentTypeInOut=pd.getString("DocumentTypeInOut");
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FReviewer", staffid);//职员ID
			pd.put("AuditTime", Tools.date2Str(new Date()));
			StockListService.editAudit(pd);
			//扣减库存
			List<PageData> varList=StockListDetailService.listAll(pd);
			for(PageData pdVar:varList) {
				PageData pdKC=new PageData();
				pdKC.put("Stock_ID", this.get32UUID());	//主键
				pdKC.put("ItemID", pdVar.getString("FCode"));	
				pdKC.put("StorageStatus", pdVar.getString("SubordinateInventory")==null?"":pdVar.getString("SubordinateInventory").substring(0, 2));	
				pdKC.put("WarehouseID", pdVar.getString("WarehouseID"));	
				pdKC.put("FUnit", pdVar.getString("FUnitName"));	
				pdKC.put("MaterialSPropKey", pdVar.getString("MaterialSPropKey"));	
				pdKC.put("RunningState", "在用");	
				pdKC.put("FStatus", "出库");	
				pdKC.put("UseCount", "0");	
				pdKC.put("PlanTrackingNumber", pdVar.getString("WorkOrderNum"));	
				pdKC.put("FCreatePersonID", staffid);//"c3e8a7d350cc43d9b9e87641947168b8");
				pdKC.put("FCreateTime", Tools.date2Str(new Date()));
				pdKC.put("LastModifiedTime", Tools.date2Str(new Date()));
				if(DocumentTypeInOut.equals("出库") && pdVar.getString("DocumentTypeBlueRed").equals("蓝字")) {
					pdKC.put("num", pdVar.get("MaterialQuantity")==null?"0":pdVar.get("MaterialQuantity").toString());//
					StockService.outStock(pdKC);
					StockOperationRecordService.add(pdKC.getString("Stock_ID"),"出库单审核",
							pdVar.get("MaterialQuantity")==null?"0":pdVar.get("MaterialQuantity").toString(),
							pdVar.getString("WarehouseID"),pdVar.getString("PositionID"),
							"出库蓝字",pdVar.getString("FCode"),staffid);
				}else if(DocumentTypeInOut.equals("出库") && pdVar.getString("DocumentTypeBlueRed").equals("红字")) {
					pdKC.put("ActualCount", pdVar.get("MaterialQuantity")==null?"0":pdVar.get("MaterialQuantity").toString());//
					pdKC.put("PositionID", pdVar.getString("PositionID"));	
					pdKC.put("ProductionBatch", pdVar.getString("MaterialBatchNumber"));
					StockService.outStockRed(pdKC);
				}
			}
			map.put("result", errInfo);				//返回结果
			return map;
		}
		/**出厂记录列表
		 * @author 管悦
		 * @date 2020-11-17
		 * @param 
		 * @throws Exception
		 */
		@RequestMapping(value="/listOutRecord")
		@ResponseBody
		public Object listOutRecord(Page page) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String KEYWORDS_QRCodeInformation = pd.getString("KEYWORDS_QRCodeInformation");						//二维码信息
			if(Tools.notEmpty(KEYWORDS_QRCodeInformation))pd.put("KEYWORDS_QRCodeInformation", KEYWORDS_QRCodeInformation.trim());
			page.setPd(pd);
			List<PageData>	varList = StockListService.listInRecord(page);	
			//插入操作日志
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", "出厂记录");//功能项
			pdOp.put("OperationType", "查询");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", "");//删改数据ID	
			operationrecordService.save(pdOp);	
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", errInfo);
			return map;
		}		
		 /**导出出厂记录到excel
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/exportOutRecord")
		public ModelAndView exportOutRecord() throws Exception{
			ModelAndView mv = new ModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("二维码");	//1
			titles.add("物料编号/名称");	//2
			titles.add("数量");	//3
			titles.add("出库仓库");	//4
			titles.add("出库仓位");	//4
			titles.add("辅助属性值");	//5
			titles.add("行关闭");	//6
			titles.add("客户");	//7
			titles.add("入库单号");	//8
			titles.add("类型");	//8
			titles.add("收货人");	//9
			titles.add("收货地址");	//10
			titles.add("交货人");	//11
			titles.add("交货日期");	//12
			titles.add("交货地点");	//13
			titles.add("制单人");	//14
			titles.add("制单时间");	//15
			titles.add("审核标志");	//16
			titles.add("审核人");	//17
			titles.add("审核时间");	//18
			dataMap.put("titles", titles);
			List<PageData> varOList = StockListService.exportInRecord(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("QRCodeInformation"));	    //1
				vpd.put("var2", varOList.get(i).getString("FMatJoint"));	    //2
				vpd.put("var3", (varOList.get(i).get("MaterialQuantity")==null?"":varOList.get(i).get("MaterialQuantity")).toString());	    //3
				vpd.put("var4", varOList.get(i).getString("FWAREHOUSEJoint"));	    //4
				vpd.put("var5", varOList.get(i).getString("FLOCATIONJoint"));	    //4
				vpd.put("var6", varOList.get(i).getString("MAT_AUXILIARYMX_NAME"));	    //5
				vpd.put("var7", varOList.get(i).getString("LineCloseIf"));	    //6
				vpd.put("var8", varOList.get(i).getString("FCustomerJoint"));	    //7
				vpd.put("var9", varOList.get(i).getString("DocumentNo"));	    //8
				vpd.put("var10", varOList.get(i).getString("DocumentTypeBlueRed"));	    //4
				vpd.put("var11", varOList.get(i).getString("FConsignee"));	    //5
				vpd.put("var12", varOList.get(i).getString("ShippingAddress"));	    //6
				vpd.put("var13", varOList.get(i).getString("DeliveredBy"));	    //7
				vpd.put("var14", varOList.get(i).getString("DeliveryDate"));	    //8
				vpd.put("var15", varOList.get(i).getString("PlaceOfDelivery"));	    //9
				vpd.put("var16", varOList.get(i).getString("FCreatorName"));	    //10
				vpd.put("var17", varOList.get(i).getString("FMakeBillsTime"));	    //11
				vpd.put("var18", varOList.get(i).getString("AuditMark"));	    //12
				vpd.put("var19", varOList.get(i).getString("FReviewerName"));	    //13
				vpd.put("var20", varOList.get(i).getString("AuditTime"));	    //14
				
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
			return mv;
		}
		/**出入厂记录列表
		 * @author 管悦
		 * @date 2021-01-18
		 * @param 
		 * @throws Exception
		 */
		@RequestMapping(value="/listInOutRecord")
		@ResponseBody
		public Object listInOutRecord(Page page) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String KEYWORDS_Material = pd.getString("KEYWORDS_Material");						//二维码信息
			if(Tools.notEmpty(KEYWORDS_Material))pd.put("KEYWORDS_Material", KEYWORDS_Material.trim());
			page.setPd(pd);
			List<PageData>	varList = StockListService.listInOutRecord(page);	
			//插入操作日志
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreator", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FOperatorID", pd.get("FCreator"));//操作人	
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", "出入厂记录");//功能项
			pdOp.put("OperationType", "查询");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", "");//删改数据ID	
			operationrecordService.save(pdOp);	
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", errInfo);
			return map;
		}	
		/**导出出入厂记录到excel
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/exportInOutRecord")
		public ModelAndView exportInOutRecord() throws Exception{
			ModelAndView mv = new ModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("物料编号/名称");	//2
			titles.add("类别");	//2
			titles.add("数量");	//3
			titles.add("仓库");	//4
			titles.add("仓位");	//4
			titles.add("辅助属性值");	//5
			titles.add("行关闭");	//6
			titles.add("客户");	//7
			titles.add("单号");	//8
			titles.add("类型");	//8
			titles.add("收货人");	//9
			titles.add("收货地址");	//10
			titles.add("交货人");	//11
			titles.add("交货日期");	//12
			titles.add("交货地点");	//13
			titles.add("制单人");	//14
			titles.add("制单时间");	//15
			titles.add("审核标志");	//16
			titles.add("审核人");	//17
			titles.add("审核时间");	//18
			dataMap.put("titles", titles);
			List<PageData> varOList = StockListService.exportInRecord(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("FMatJoint"));	    //2
				vpd.put("var2", varOList.get(i).getString("DocumentTypeInOut"));	    //1
				vpd.put("var3", (varOList.get(i).get("MaterialQuantity")==null?"":varOList.get(i).get("MaterialQuantity")).toString());	    //3
				vpd.put("var4", varOList.get(i).getString("FWAREHOUSEJoint"));	    //4
				vpd.put("var5", varOList.get(i).getString("FLOCATIONJoint"));	    //4
				vpd.put("var6", varOList.get(i).getString("MAT_AUXILIARYMX_NAME"));	    //5
				vpd.put("var7", varOList.get(i).getString("LineCloseIf"));	    //6
				vpd.put("var8", varOList.get(i).getString("FCustomerJoint"));	    //7
				vpd.put("var9", varOList.get(i).getString("DocumentNo"));	    //8
				vpd.put("var10", varOList.get(i).getString("DocumentTypeBlueRed"));	    //4
				vpd.put("var11", varOList.get(i).getString("FConsignee"));	    //5
				vpd.put("var12", varOList.get(i).getString("ShippingAddress"));	    //6
				vpd.put("var13", varOList.get(i).getString("DeliveredBy"));	    //7
				vpd.put("var14", varOList.get(i).getString("DeliveryDate"));	    //8
				vpd.put("var15", varOList.get(i).getString("PlaceOfDelivery"));	    //9
				vpd.put("var16", varOList.get(i).getString("FCreatorName"));	    //10
				vpd.put("var17", varOList.get(i).getString("FMakeBillsTime"));	    //11
				vpd.put("var18", varOList.get(i).getString("AuditMark"));	    //12
				vpd.put("var19", varOList.get(i).getString("FReviewerName"));	    //13
				vpd.put("var20", varOList.get(i).getString("AuditTime"));	    //14
				
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
			return mv;
		}
}