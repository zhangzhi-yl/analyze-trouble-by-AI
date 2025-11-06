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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.mm.MaterialTransferApplicationDetailsService;
import org.yy.service.mm.MaterialTransferSplitService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：物料转移申请明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-14
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/MaterialTransferApplicationDetails")
public class MaterialTransferApplicationDetailsController extends BaseController {
	
	@Autowired
	private MaterialTransferApplicationDetailsService MaterialTransferApplicationDetailsService;
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private MaterialRequirementService MaterialRequirementService;//物料需求单
	
	@Autowired
	private StockService StockService;//库存
	
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;//辅助属性值
	
	@Autowired
	private MaterialTransferSplitService MaterialTransferSplitService;//拆分明细
	
	/**保存调拨明细
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/addAllocationDetail")
	//@RequiresPermissions("MaterialTransferApplicationDetails:add")
	@ResponseBody
	public Object addAllocationDetail() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MTADetails_ID", this.get32UUID());	//物料转移申请明细ID
		pd.put("FCreatePersonID", staffid);//创建人  
		pd.put("TransferOperator", staffid);//
		pd.put("FCreateTime", Tools.date2Str(new Date()));//创建时间
		pd.put("TransferOperationTime", Tools.date2Str(new Date()));//
		MaterialTransferApplicationDetailsService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**物料转入单-拆分物料
	 * ！！！！！！！！！！！！！类型码物料（不支持一物一码物料）！！！！！！！！！！！！！
	 * 	点击第一行数据的拆分，拆分出第二行，更新第一行数据的拆分数量和拆分状态，
	 * 	点击第二行的拆分，拆分出第三行，更新第二行的拆分数量和拆分状态，
	 * @author 宋
	 * @date 2020-12-15
	 * @param pd.IssuedQuantity 实际发出数量
	 * @param pd.MTADetails_ID 明细id
	 * @throws Exception
	 */
	@RequestMapping(value="/splitIn")
	@ResponseBody
	public Object splitIn() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		PageData pdSave = new PageData();
		pd = this.getPageData();
		BigDecimal IssuedQuantity = new BigDecimal(pd.get("IssuedQuantity").toString());//手写输入将要放入库位的物料实际数量
		String MTADetails_ID = pd.get("MTADetails_ID").toString();
		String TargetWarehouse = pd.get("TargetWarehouse").toString();
		String TargetPosition = pd.get("TargetPosition").toString();
		String TransitionType = pd.get("TransitionType").toString();
		pd = MaterialTransferApplicationDetailsService.findById(pd);	//根据ID读取
		//如果是不支持一物一码的物料
		if(null != pd && pd.containsKey("UNIQUE_CODE_WHETHER") && "否".equals(pd.getString("UNIQUE_CODE_WHETHER"))){
			BigDecimal SplitQuantity = new BigDecimal(pd.get("SplitQuantity").toString());
			pd.put("SplitQuantity", IssuedQuantity);//将拆分数量更新为实际数量
			pd.put("IssuedQuantity", IssuedQuantity);
			pd.put("SplitStatus", "已拆分");//拆分状态，未拆分、已拆分
			pd.put("DeliveryWarehouse", "");//发出仓库id
			pd.put("DeliveryPosition", "");//发出仓位id
			pd.put("TargetWarehouse", TargetWarehouse);//目标仓库id
			pd.put("TargetPosition", TargetPosition);//目标仓位id
			MaterialTransferApplicationDetailsService.edit(pd);//更新被点击数据的拆分数量
			pdSave=pd;
			pdSave.put("MTADetails_ID", this.get32UUID());	//主键
			pdSave.put("DeliveryWarehouse", "");//发出仓库id
			pdSave.put("DeliveryPosition", "");//发出仓位id
			pdSave.put("TargetWarehouse", TargetWarehouse);//目标仓库id
			pdSave.put("TargetPosition", TargetPosition);//目标仓位id
			pdSave.put("MaterialQuantity",  new BigDecimal("0"));//新被拆分出来的数据实际数量为0
			pdSave.put("DemandQuantity", new BigDecimal(pd.get("DemandQuantity").toString()));//需求数量
			pdSave.put("SplitQuantity", SplitQuantity.subtract(IssuedQuantity));//拆分数量为拆分数量-实际数量
			pdSave.put("IssuedQuantity", new BigDecimal("0"));
			pdSave.put("FCreatePersonID", staffid);//创建人 
			pdSave.put("FCreateTime", Tools.date2Str(new Date()));//创建时间
			pdSave.put("TransitionType", TransitionType);//运行状态，转出、转入
			pdSave.put("METADATA_WHETHER", "N");//是否元数据，Y/N
			pdSave.put("RelationID", MTADetails_ID);//关联id，元数据为空
			pdSave.put("SplitStatus", "未拆分");//拆分状态，未拆分、已拆分
			MaterialTransferApplicationDetailsService.save(pdSave);
		}else {
			errInfo="error";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**物料转出单-拆分物料
	 * ！！！！！！！！！！！！！类型码物料（不支持一物一码物料）！！！！！！！！！！！！！
	 * 	点击第一行数据的拆分，拆分出第二行，更新第一行数据的拆分数量和拆分状态，
	 * 	点击第二行的拆分，拆分出第三行，更新第二行的拆分数量和拆分状态，
	 * @author 宋
	 * @date 2020-12-15
	 * @param pd.IssuedQuantity 实际发出数量
	 * @param pd.MTADetails_ID 明细id
	 * @throws Exception
	 */
	@RequestMapping(value="/split")
	@ResponseBody
	public Object split() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		PageData pdSave = new PageData();
		pd = this.getPageData();
		BigDecimal IssuedQuantity = new BigDecimal(pd.get("IssuedQuantity").toString());//手写输入将要放入库位的物料实际数量
		String MTADetails_ID = pd.get("MTADetails_ID").toString();
		String DeliveryWarehouse = pd.get("DeliveryWarehouse").toString();
		String DeliveryPosition = pd.get("DeliveryPosition").toString();
		String TargetWarehouse = pd.get("TargetWarehouse").toString();
		String TargetPosition = pd.get("TargetPosition").toString();
		String TransitionType = pd.get("TransitionType").toString();
		pd = MaterialTransferApplicationDetailsService.findById(pd);	//根据ID读取
		//如果是不支持一物一码的物料
		if(null != pd && pd.containsKey("UNIQUE_CODE_WHETHER") && "否".equals(pd.getString("UNIQUE_CODE_WHETHER"))){
			PageData pdKC=new PageData();
			pdKC.put("WarehouseID", pd.getString("DeliveryWarehouse"));
			pdKC.put("ItemID", pd.getString("MaterialId"));
			pdKC.put("MaterialSPropKey", pd.getString("SPropKey"));
			PageData pdStockSum=StockService.getSum(pdKC);//即时库存
			BigDecimal stockSum = new BigDecimal(0) ;
			if(pdStockSum != null) {
				stockSum = new BigDecimal(pdStockSum.get("stockSum").toString());
			}
			BigDecimal SplitQuantity = new BigDecimal(pd.get("SplitQuantity").toString());
			pd.put("SplitQuantity", IssuedQuantity);//将拆分数量更新为实际数量
			pd.put("IssuedQuantity", IssuedQuantity);
			pd.put("SplitStatus", "已拆分");//拆分状态，未拆分、已拆分
			pd.put("DeliveryWarehouse", DeliveryWarehouse);//发出仓库id
			pd.put("DeliveryPosition", DeliveryPosition);//发出仓位id
			pd.put("TargetWarehouse", TargetWarehouse);//目标仓库id
			pd.put("TargetPosition", TargetPosition);//目标仓位id
			MaterialTransferApplicationDetailsService.edit(pd);//更新被点击数据的拆分数量
			pdSave=pd;
			pdSave.put("MTADetails_ID", this.get32UUID());	//主键
			pdSave.put("DeliveryWarehouse", DeliveryWarehouse);//发出仓库id
			pdSave.put("DeliveryPosition", DeliveryPosition);//发出仓位id
			pdSave.put("TargetWarehouse", TargetWarehouse);//目标仓库id
			pdSave.put("TargetPosition", TargetPosition);//目标仓位id
			pdSave.put("MaterialQuantity",  new BigDecimal("0"));//新被拆分出来的数据实际数量为0
			pdSave.put("DemandQuantity", new BigDecimal(pd.get("DemandQuantity").toString()));//需求数量
			pdSave.put("SplitQuantity", SplitQuantity.subtract(IssuedQuantity));//拆分数量为拆分数量-实际数量
			pdSave.put("IssuedQuantity", new BigDecimal("0"));
			pdSave.put("FCreatePersonID", staffid);//创建人  
			pdSave.put("FCreateTime", Tools.date2Str(new Date()));//创建时间
			pdSave.put("TransitionType", TransitionType);//运行状态，转出、转入
			pdSave.put("METADATA_WHETHER", "N");//是否元数据，Y/N
			pdSave.put("RelationID", MTADetails_ID);//关联id，元数据为空
			pdSave.put("SplitStatus", "未拆分");//拆分状态，未拆分、已拆分
			MaterialTransferApplicationDetailsService.save(pdSave);
		}else {
			errInfo="error";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**创建物料转移申请明细
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/create")
	//@RequiresPermissions("MaterialTransferApplicationDetails:add")
	@ResponseBody
	public Object create() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		String currentTime = Tools.date2Str(new Date());
		JSONArray jarr = JSONArray.fromObject(pd.getString("arr"));
		String ArrayDATA_IDS[]=new String[jarr.size()];
		String planWorkOrderId="";
		String DATA_IDS = pd.getString("DATA_IDS");
		String MaterialTransferApplicationForm_ID = pd.getString("MaterialTransferApplicationForm_ID");
		if(Tools.notEmpty(DATA_IDS)){
			//String ArrayDATA_IDS[] = DATA_IDS.split(",");
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
				// 修改物料需求单的状态为‘Y’
				PageData materialRequirement=new PageData();
				materialRequirement.put("array", ArrayDATA_IDS);
				materialRequirement.put("PushDownTransferIF", "Y");
				materialRequirement.put("PlanningWorkOrderID", planWorkOrderId);
				MaterialRequirementService.updateTransferStateByWorkOrderIdAndMaterialId(materialRequirement);
			}
			/*for(int i=0;i<ArrayDATA_IDS.length;i++) {
				PageData mta = new PageData();
				pd.put("MaterialRequirement_ID", ArrayDATA_IDS[i]);
				mta=MaterialRequirementService.findById(pd);
				if(null != mta) {
					String currentTime = Tools.date2Str(new Date());
					PageData detailpd=new PageData();
					detailpd.put("MTADetails_ID", this.get32UUID());//物料转移申请单明细ID
					detailpd.put("LineClose", "N");//行关闭（N/Y)
					detailpd.put("TransferOperator", staffid);//转移操作人
					detailpd.put("TransferOperationTime", currentTime);//转移操作时间
					detailpd.put("SourceRowNum", Integer.parseInt(mta.get("RowNum").toString()));//源单行号
					detailpd.put("TransferType", "单据转移");//转移类型(直接转移,单据转移)
					detailpd.put("TransitionType", "转出");
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
					detailpd.put("DeliveryWarehouse", mta.getString("DeliveryWarehouse"));//发出仓库id
					detailpd.put("DeliveryPosition", mta.getString("DeliveryPosition"));//发出仓位id
					detailpd.put("TargetWarehouse", mta.getString("TargetWarehouse"));//目标仓库id
					detailpd.put("TargetPosition", mta.getString("TargetPosition"));//目标仓位id
					detailpd.put("DemandTime", mta.getString("DemandTime"));//需求时间
					detailpd.put("DateBatch", mta.getString("DateBatch"));//日期批次
					detailpd.put("WP", mta.getString("WP"));//工序
					detailpd.put("FCreatePersonID", staffid);//创建人  
					detailpd.put("FCreateTime", currentTime);//创建时间
					detailpd.put("MaterialId", mta.getString("MaterialID"));//物料id
					detailpd.put("SplitQuantity", new BigDecimal(
							mta.containsKey("DemandCount") && null!=mta.get("DemandCount")?mta.get("DemandCount").toString():"0"));//拆分数量
					detailpd.put("METADATA_WHETHER", "Y");//是否元数据，Y/N
					detailpd.put("RelationID", "");//关联id，元数据为空
					detailpd.put("SplitStatus", "未拆分");//拆分状态，未拆分、已拆分
					detailpd.put("FIsScan", "N");
					MaterialTransferApplicationDetailsService.save(detailpd);
					// 修改物料需求单的状态为‘Y’
					PageData materialRequirement=new PageData();
					materialRequirement.put("array", ArrayDATA_IDS);
					materialRequirement.put("PushDownTransferIF", "Y");
					MaterialRequirementService.updateTransferState(materialRequirement);
				}
			}*/
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
	//@RequiresPermissions("MaterialTransferApplicationDetails:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MTADetails_ID", this.get32UUID());	//物料转移申请明细ID
		pd.put("FCreatePersonID", staffid);//创建人  
		pd.put("TransferOperator", staffid);//
		pd.put("FCreateTime", Tools.date2Str(new Date()));//创建时间
		pd.put("TransferOperationTime", Tools.date2Str(new Date()));//
		MaterialTransferApplicationDetailsService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除，如果为唯一码物料，应同时删除所属拆分明细
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("MaterialTransferApplicationDetails:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData p = MaterialTransferApplicationDetailsService.findById(pd);
		if(p.containsKey("UNIQUE_CODE_WHETHER") && "是".equals(p.getString("UNIQUE_CODE_WHETHER"))) {
			p.put("FRelatedID", p.getString("MTADetails_ID"));
			List<PageData> splitList = MaterialTransferSplitService.listAll(p);
			String[] ArrayDATA_IDS=new String[splitList.size()];
			for(int i=0;i<splitList.size();i++) {
				ArrayDATA_IDS[i]=splitList.get(i).getString("MaterialTransferSplit_ID");
			}
			MaterialTransferSplitService.deleteAll(ArrayDATA_IDS);
		}
		MaterialTransferApplicationDetailsService.delete(pd);
		operationrecordService.add("","物料转移明细","删除",p.getString("MTADetails_ID"),staffid,"删除物料转移明细");
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("MaterialTransferApplicationDetails:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialTransferApplicationDetailsService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**修改行关闭状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editLineClose")
	//@RequiresPermissions("MaterialTransferApplicationDetails:edit")
	@ResponseBody
	public Object editLineClose() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialTransferApplicationDetailsService.editLineClose(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**修改状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editFStatus")
	//@RequiresPermissions("MaterialTransferApplicationDetails:edit")
	@ResponseBody
	public Object editFStatus() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialTransferApplicationDetailsService.editFStatus(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("MaterialTransferApplicationDetails:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = MaterialTransferApplicationDetailsService.list(page);	//列出MaterialTransferApplicationDetails列表
		for(int i=0;i<varList.size();i++) {
			PageData pdKC=new PageData();
			pdKC.put("PositionID", varList.get(i).getString("DeliveryPosition"));
			pdKC.put("WarehouseID", varList.get(i).getString("DeliveryWarehouse"));
			pdKC.put("ItemID", varList.get(i).getString("MaterialId"));
			pdKC.put("MaterialSPropKey", varList.get(i).getString("SPropKey"));
			PageData pdStockSum=StockService.getSum(pdKC);//即时库存
			if(pdStockSum != null) {
				varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
			}else {
				varList.get(i).put("stockSum", "0");
			}
		}
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
	//@RequiresPermissions("MaterialTransferApplicationDetails:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = MaterialTransferApplicationDetailsService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("MaterialTransferApplicationDetails:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			MaterialTransferApplicationDetailsService.deleteAll(ArrayDATA_IDS);
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
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("物料转移申请明细ID");	//1
		titles.add("行关闭");	//2
		titles.add("转移操作人");	//3
		titles.add("转移操作时间");	//4
		titles.add("源单行号");	//5
		titles.add("转移类型");	//6
		titles.add("状态");	//7
		titles.add("数据来源");	//8
		titles.add("物料需求单ID");	//9
		titles.add("物料转移申请单主表ID");	//10
		titles.add("计划工单编号");	//11
		titles.add("物料编号");	//12
		titles.add("物料名称");	//13
		titles.add("辅助属性");	//14
		titles.add("辅助属性值");	//15
		titles.add("需求数量");	//16
		titles.add("实发数量");	//17
		titles.add("发出仓库");	//18
		titles.add("目标仓位");	//19
		titles.add("需求时间");	//20
		titles.add("工单编号");	//21
		titles.add("日期批次");	//22
		titles.add("工序");	//23
		titles.add("类型");	//24
		titles.add("占用库存");	//25
		titles.add("创建人");	//26
		titles.add("创建时间");	//27
		dataMap.put("titles", titles);
		List<PageData> varOList = MaterialTransferApplicationDetailsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MTADetails_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("LineClose"));	    //2
			vpd.put("var3", varOList.get(i).getString("TransferOperator"));	    //3
			vpd.put("var4", varOList.get(i).getString("TransferOperationTime"));	    //4
			vpd.put("var5", varOList.get(i).get("SourceRowNum").toString());	//5
			vpd.put("var6", varOList.get(i).getString("TransferType"));	    //6
			vpd.put("var7", varOList.get(i).getString("FStatus"));	    //7
			vpd.put("var8", varOList.get(i).getString("DataSources"));	    //8
			vpd.put("var9", varOList.get(i).getString("MaterialRequisitionID"));	    //9
			vpd.put("var10", varOList.get(i).getString("MTA_ID"));	    //10
			vpd.put("var11", varOList.get(i).getString("PlannedWorkOrderNum"));	    //11
			vpd.put("var12", varOList.get(i).getString("MaterialNum"));	    //12
			vpd.put("var13", varOList.get(i).getString("MaterialName"));	    //13
			vpd.put("var14", varOList.get(i).getString("SProp"));	    //14
			vpd.put("var15", varOList.get(i).getString("SPropKey"));	    //15
			vpd.put("var16", varOList.get(i).get("DemandQuantity").toString());	//16
			vpd.put("var17", varOList.get(i).get("IssuedQuantity").toString());	//17
			vpd.put("var18", varOList.get(i).getString("DeliveryWarehouse"));	    //18
			vpd.put("var19", varOList.get(i).getString("TargetPosition"));	    //19
			vpd.put("var20", varOList.get(i).getString("DemandTime"));	    //20
			vpd.put("var21", varOList.get(i).getString("WorkOrderNum"));	    //21
			vpd.put("var22", varOList.get(i).getString("DateBatch"));	    //22
			vpd.put("var23", varOList.get(i).getString("WP"));	    //23
			vpd.put("var24", varOList.get(i).getString("TType"));	    //24
			vpd.put("var25", varOList.get(i).getString("OccupationOfInventory"));	    //25
			vpd.put("var26", varOList.get(i).getString("FCreatePersonID"));	    //26
			vpd.put("var27", varOList.get(i).getString("FCreateTime"));	    //27
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="/wlzy/zhuisu")
	public Object GET_WLZY_ZHUISU_listPage(Page page)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = MaterialTransferApplicationDetailsService.GET_WLZY_ZHUISU_listPage(page);	
		
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
}
