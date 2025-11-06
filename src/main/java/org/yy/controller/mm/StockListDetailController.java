package org.yy.controller.mm;

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

import com.github.pagehelper.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mm.MATSPLITService;
import org.yy.service.mm.StockListDetailService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.PurchaseMaterialDetailsService;
import org.yy.service.pp.SALESORDERDETAILService;

/** 
 * 说明：出入库单明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/StockListDetail")
public class StockListDetailController extends BaseController {
	
	@Autowired
	private StockListDetailService StockListDetailService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private StockService StockService;
	@Autowired
	private PurchaseMaterialDetailsService PurchaseMaterialDetailsService;
	@Autowired
	private SALESORDERDETAILService salesorderdetailService;
	@Autowired
	private MATSPLITService MATSPLITService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	
	/**更新实际数量、仓库id、库位id
	 * @author 宋
	 * @date 2020-12-15
	 * @param 
	 * StockListDetail_ID: 单据明细ID
	 * WarehouseID: 仓库id
	 * PositionID：库位id
	 * MaterialQuantity：实际数量
	 * @throws Exception
	 */
	@RequestMapping(value="/editPositionAndQty")
	@ResponseBody
	public Object editPositionAndQty() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockListDetailService.editPositionAndQty(pd);	//编辑数量、仓库id、库位id
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**入库单明细新增
	 * @author 管悦
	 * @date 2020-11-15
	 * @param DocumentTypeInOut：区分出入库、入库/出库
	 * StockList_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/addIn")
	//@RequiresPermissions("StockListDetail:add")
	@ResponseBody
	public Object addIn() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();		
		pd.put("StockListDetail_ID", this.get32UUID());	//主键
		PageData pdRowNum=StockListDetailService.getRowNum(pd);//生成行号
		if(pdRowNum != null) {
			pd.put("RowNum", pdRowNum.get("RowNum").toString());
		}
		pd.put("LineCloseIf", "N");
		pd.put("FPushCount", "0");
		pd.put("MaterialQuantity", "0");
		pd.put("SubordinateInventory", "工厂库");
		pd.put("DataSources", "手动创建");
		pd.put("SplitStatus", "未拆分");
		StockListDetailService.save(pd);
		//插入操作日志
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("StockListDetail:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockListDetailService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**入库单明细修改
	 * @author 管悦
	 * @date 2020-11-15
	 * @param 
	 * DocumentTypeInOut：区分出入库、入库/出库
	 * DocumentTypeBlueRed
	 * StockListDetail_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/editIn")
	//@RequiresPermissions("StockListDetail:edit")
	@ResponseBody
	public Object editIn() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdKC=new PageData();
		pdKC.put("WarehouseID", pd.getString("WarehouseID"));
		pdKC.put("ItemID", pd.getString("FCode"));
		pdKC.put("MaterialSPropKey", pd.getString("MaterialSPropKey"));
		PageData pdStockSum=StockService.getSum(pdKC);//即时库存
		if((pd.containsKey("DocumentTypeBlueRed") && pd.getString("DocumentTypeBlueRed").equals("红字")) 
				&& (pdStockSum == null || Double.parseDouble(pdStockSum.get("stockSum").toString())<=0)) {
			errInfo="fail1";//没有库存
		}else {
			StockListDetailService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**入库单明细列表
	 * @author 管悦
	 * @date 2020-11-15
	 * @param 
	 * DocumentTypeInOut：区分出入库、入库/出库
	 * StockList_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/listIn")
	//@RequiresPermissions("StockListDetail:list")
	@ResponseBody
	public Object listIn(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//用于区分 物料追溯功能 的接口条件
		if(StringUtil.isEmpty(pd.getString("KEYWORDS_zhuisu"))){
			pd.put("KEYWORDS_zhuisu", "NO");
		}
		
		page.setPd(pd);
		List<PageData>	varList = StockListDetailService.list(page);	//列出StockListDetail列表
		for(int i=0;i<varList.size();i++) {
			PageData pdKC=new PageData();
			pdKC.put("WarehouseID", varList.get(i).getString("WarehouseID"));
			pdKC.put("ItemID", varList.get(i).getString("FCode"));
			pdKC.put("MaterialSPropKey", varList.get(i).getString("MaterialSPropKey"));
			PageData pdStockSum=StockService.getSum(pdKC);//即时库存
			if(pdStockSum != null) {
				varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
			} else {
				varList.get(i).put("stockSum", "0");
			}
		}
		//插入操作日志
		
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
	//@RequiresPermissions("StockListDetail:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = StockListDetailService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("StockListDetail:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			StockListDetailService.deleteAll(ArrayDATA_IDS);
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
		titles.add("行号");	//1
		titles.add("来源类型");	//2
		titles.add("源单单号");	//3
		titles.add("源单行号");	//4
		titles.add("出入库单ID");	//5
		titles.add("行关闭（N/Y)");	//6
		titles.add("二维码信息");	//7
		titles.add("品项");	//8
		titles.add("物料批次号");	//9
		titles.add("产品代码");	//10
		titles.add("物料数量");	//11
		titles.add("物料辅助属性");	//12
		titles.add("物料辅助值");	//13
		titles.add("仓库ID");	//14
		titles.add("仓位ID");	//15
		titles.add("隶属库存(工厂库,车间库)");	//16
		titles.add("检验合格状态");	//17
		titles.add("物料皮重");	//18
		titles.add("物料净重");	//19
		titles.add("单价金额");	//20
		titles.add("单位");	//21
		titles.add("成本");	//22
		titles.add("备注1");	//23
		titles.add("备注2");	//24
		titles.add("备注3");	//25
		dataMap.put("titles", titles);
		List<PageData> varOList = StockListDetailService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("RowNum").toString());	//1
			vpd.put("var2", varOList.get(i).getString("DataSources"));	    //2
			vpd.put("var3", varOList.get(i).getString("SourceOrderNum"));	    //3
			vpd.put("var4", varOList.get(i).get("SourceRowNum").toString());	//4
			vpd.put("var5", varOList.get(i).getString("StockList_ID"));	    //5
			vpd.put("var6", varOList.get(i).getString("LineCloseIf"));	    //6
			vpd.put("var7", varOList.get(i).getString("QRCodeInformation"));	    //7
			vpd.put("var8", varOList.get(i).getString("FItem"));	    //8
			vpd.put("var9", varOList.get(i).getString("MaterialBatchNumber"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCode"));	    //10
			vpd.put("var11", varOList.get(i).get("MaterialQuantity").toString());	//11
			vpd.put("var12", varOList.get(i).getString("MaterialSProp"));	    //12
			vpd.put("var13", varOList.get(i).getString("MaterialSPropKey"));	    //13
			vpd.put("var14", varOList.get(i).getString("WarehouseID"));	    //14
			vpd.put("var15", varOList.get(i).getString("PositionID"));	    //15
			vpd.put("var16", varOList.get(i).getString("SubordinateInventory"));	    //16
			vpd.put("var17", varOList.get(i).getString("QualificationInspection"));	    //17
			vpd.put("var18", varOList.get(i).get("TareWeightOfMaterial").toString());	//18
			vpd.put("var19", varOList.get(i).get("NetWeightOfMaterial").toString());	//19
			vpd.put("var20", varOList.get(i).get("UnitPrice").toString());	//20
			vpd.put("var21", varOList.get(i).getString("FUnit"));	    //21
			vpd.put("var22", varOList.get(i).get("FCost").toString());	//22
			vpd.put("var23", varOList.get(i).getString("FExplanation1"));	    //23
			vpd.put("var24", varOList.get(i).getString("FExplanation2"));	    //24
			vpd.put("var25", varOList.get(i).getString("FExplanation3"));	    //25
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**行关闭/反行关闭
	 * @author 管悦
	 * @date 2020-11-15
	 * @param 
	 * StockListDetail_ID:单据明细ID
	 * LineCloseIf:行关闭（Y/N）
	 * DocumentTypeInOut：区分出入库、入库/出库
	 * @throws Exception
	 */
	@RequestMapping(value="/rowClose")
	@ResponseBody
	public Object rowClose() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockListDetailService.rowClose(pd);
		//插入操作日志
		String LineCloseIf= pd.getString("LineCloseIf");
		PageData pdSave = StockListDetailService.findById(pd);	//根据ID读取
		pdSave.put("DocumentTypeInOut", pd.getString("DocumentTypeInOut"));
		PurchaseMaterialDetailsService.calFPushCount(pdSave);//反写采购订单明细下推数量
		StockListDetailService.calFPushCount(pdSave);//反写入库单明细下推数量
		salesorderdetailService.calFPushCount(pdSave);//反写销售订单明细下推数量
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**批量选择采购请单物料 ，添加入库单明细
	 * @param StockList_ID
	 * @param Arrays
	 * @throws Exception
	 */
	@RequestMapping(value="/selectAllPur")
	@ResponseBody
	public Object selectAllPur() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();	
		PageData arraypd = new PageData();
		pd = this.getPageData();
		int totalCount=0;
		int successCount=0;
		JSONArray array = JSONArray.fromObject(pd.getString("Arrays"));//得到采购订单明细ID、下推数量、已推数量json
		if(Tools.notEmpty(pd.getString("Arrays"))){
			totalCount=array.size();
			for(int i=0;i<array.size();i++) {//循环array中的采购订单明细ID、下推数量、已推数量，用下推数量+已推数量更新采购订单中的下推数量
				arraypd.put("PurchaseMaterialDetails_ID", array.getJSONObject(i).get("PurchaseMaterialDetails_ID"));//采购订单明细ID
				double FPushNum=Double.parseDouble(array.getJSONObject(i).get("FPushCount").toString());//已推数量
				double FUsableCount=Double.parseDouble(array.getJSONObject(i).get("FUsableCount").toString());//下推数量
				double FPushCount=FPushNum+FUsableCount;
				arraypd.put("FPushCount", FPushCount);//采购订单明细ID
				if(FUsableCount>0) {//判断下推数量是否大于0，大于0可以下推入库，小于0不下推
					PageData varpd = PurchaseMaterialDetailsService.findByPushId(arraypd);//通过ID查询数据
					if(null!=varpd) {
						successCount=successCount+1;
						PageData pdSave=new PageData();
						pdSave.put("StockListDetail_ID", this.get32UUID());	//主键
						PageData pdRowNum = StockListDetailService.getRowNum(pd);//生成行号
						if(pdRowNum != null) {
							pdSave.put("RowNum", pdRowNum.get("RowNum").toString());
						}
						pdSave.put("DataSources", "采购订单");
						pdSave.put("SourceOrderNum", varpd.getString("FNum"));
						pdSave.put("SourceRowNum", varpd.get("RowNum").toString());
						pdSave.put("StockList_ID", pd.getString("StockList_ID"));
						pdSave.put("LineCloseIf", "N");
						pdSave.put("QRCodeInformation", "");//二维码 TODO
						pdSave.put("FItem", "");//品项	TODO
						pdSave.put("MaterialBatchNumber", "");//物料批次号
						pdSave.put("FCode", varpd.getString("MaterialID"));//产品代码，物料代码
						pdSave.put("MaterialQuantity",  0);//实际数量
						pdSave.put("DemandQuantity",  FUsableCount);//需求数量，默认可用数量，采购物料明细数量-下推数量
						pdSave.put("SplitQuantity",  FUsableCount);//拆分数量，默认可用数量，采购物料明细数量-下推数量
						pdSave.put("MaterialSProp", varpd.getString("SProp"));
						pdSave.put("MaterialSPropKey", varpd.getString("SPropKey"));
						//pdSave.put("WarehouseID", "");//仓库ID
						//pdSave.put("PositionID", "");//仓位ID
						pdSave.put("SubordinateInventory", "工厂库");
						pdSave.put("FUnit", varpd.getString("UNIT_INFO_ID"));//单位id
						pdSave.put("FIsScan", "N");//是否扫码
						pdSave.put("UNIQUE_CODE_WHETHER", varpd.getString("UNIQUE_CODE_WHETHER"));//是否一物一码物料
						pdSave.put("MAT_CODE", varpd.getString("MAT_CODE"));//物料代码
						pdSave.put("MAT_NAME", varpd.getString("MAT_NAME"));//物料名称
						pdSave.put("METADATA_WHETHER", "Y");//是否元数据
						pdSave.put("FPushCount", "0");//下推数量
						pdSave.put("DocumentTypeInOut", pd.getString("DocumentTypeInOut"));
						pdSave.put("MaterialBatchNumber", varpd.getString("AdmissionBatchNumber"));
						pdSave.put("SplitStatus", "未拆分");
						StockListDetailService.save(pdSave);
						PurchaseMaterialDetailsService.editFPushCount(arraypd);//更新采购订单下推数量
						//PurchaseMaterialDetailsService.calFPushCount(pdSave);//更新采购物料明细下推数量
					}
				}
				
			}
			/*String ArrayDATA_IDS[] = DATA_IDS.split(",");
			totalCount=ArrayDATA_IDS.length;
			List<PageData> varList=PurchaseMaterialDetailsService.listAllSelect(ArrayDATA_IDS);
			successCount=varList.size();
			for(PageData pdVar:varList) {
				
			}*/
		}else{
			errInfo = "error";
		}
		map.put("totalCount", totalCount);//选择总条数		
		map.put("successCount", successCount);//导入成功条数		
		map.put("result", errInfo);				//返回结果
		return map;
	}
		
		/**入库红字单添加物料列表
		 * @author 管悦
		 * @date 2020-11-15
		 * @param StockList_ID：入库单ID
		 * DocumentTypeInOut：区分出入库、入库/出库
		 * @throws Exception
		 */
		@RequestMapping(value="/listAllRedMatIn")
		@ResponseBody
		public Object listAllRedMatIn() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String KEYWORDS_DocumentNo = pd.getString("KEYWORDS_DocumentNo");						//红字单编号
			if(Tools.notEmpty(KEYWORDS_DocumentNo))pd.put("KEYWORDS_DocumentNo", KEYWORDS_DocumentNo.trim());
			String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
			if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
			String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
			if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
			List<PageData>	varList = StockListDetailService.listAllRedMat(pd);	
			for(int i=0;i<varList.size();i++) {
				PageData pdKC=new PageData();
				pdKC.put("WarehouseID", varList.get(i).getString("WarehouseID"));
				pdKC.put("ItemID", varList.get(i).getString("FCode"));
				pdKC.put("MaterialSPropKey", varList.get(i).getString("MaterialSPropKey"));
				PageData pdStockSum=StockService.getSum(pdKC);//即时库存
				if(pdStockSum != null) {
					varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
				}else {
					varList.get(i).put("stockSum", "0");
				}
			}
			//插入操作日志
			map.put("varList", varList);
			map.put("result", errInfo);
			return map;
		}	
		/**批量选择蓝字入库单物料 
		 * @param StockList_ID
		 * DocumentTypeInOut：区分出入库、入库/出库
		 * @throws Exception
		 */
		@RequestMapping(value="/selectAllBlueIn")
		@ResponseBody
		public Object selectAllBlueIn() throws Exception{
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
				List<PageData> varList=StockListDetailService.selectAllBlueIn(ArrayDATA_IDS);
				successCount=varList.size();
				for(PageData pdVar:varList) {
					PageData pdSave=new PageData();
					PageData pdRowNum=StockListDetailService.getRowNum(pd);//生成行号
					pdVar.put("RowNumx", pdVar.get("RowNum").toString());
					if(pdRowNum != null) {
						pdVar.put("RowNum", pdRowNum.get("RowNum").toString());
						
					}
					pdVar.put("StockListDetail_ID", this.get32UUID());	//主键
					pdVar.put("DataSources", "入库单");
					pdVar.put("PositionID", "");
					pdVar.put("LineCloseIf", "N");
					pdVar.put("SourceOrderNum", pdVar.getString("DocumentNo"));
					pdVar.put("SourceRowNum", pdVar.get("RowNumx").toString());
					pdVar.put("StockList_ID", pd.getString("StockList_ID"));
					pdVar.put("FPushCount", "0");
					StockListDetailService.save(pdVar);
					pdVar.put("DocumentTypeInOut", pd.getString("DocumentTypeInOut"));
					StockListDetailService.calFPushCount(pdVar);//反写入库单明细下推数量
				}
				errInfo = "success";
				//插入操作日志
			}else{
				errInfo = "error";
			}
			map.put("totalCount", totalCount);//选择总条数		
			map.put("successCount", successCount);//导入成功条数		
			map.put("result", errInfo);				//返回结果
			return map;
		}
		/**出库单明细新增
		 * @author 管悦
		 * @date 2020-11-17
		 * @param DocumentTypeInOut：区分出入库、入库/出库
		 * StockList_ID
		 * @throws Exception
		 */
		@RequestMapping(value="/addOut")
		//@RequiresPermissions("StockListDetail:add")
		@ResponseBody
		public Object addOut() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();		
			pd.put("StockListDetail_ID", this.get32UUID());	//主键
			PageData pdRowNum=StockListDetailService.getRowNum(pd);//生成行号
			if(pdRowNum != null) {
				pd.put("RowNum", pdRowNum.get("RowNum").toString());
			}
			pd.put("LineCloseIf", "N");
			pd.put("FPushCount", "0");
			pd.put("DataSources", "手动创建");
			StockListDetailService.save(pd);
			//插入操作日志
			map.put("result", errInfo);
			map.put("pd", pd);
			return map;
		}	
		/**出库单明细修改
		 * @author 管悦
		 * @date 2020-11-17
		 * @param 
		 * DocumentTypeInOut：区分出入库、入库/出库
		 * DocumentTypeBlueRed
		 * StockListDetail_ID
		 * @throws Exception
		 */
		@RequestMapping(value="/editOut")
		//@RequiresPermissions("StockListDetail:edit")
		@ResponseBody
		public Object editOut() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pdKC=new PageData();
			pdKC.put("WarehouseID", pd.getString("WarehouseID"));
			pdKC.put("ItemID", pd.getString("FCode"));
			pdKC.put("MaterialSPropKey", pd.getString("MaterialSPropKey"));
			PageData pdStockSum=StockService.getSum(pdKC);//即时库存
			if(pd.getString("DocumentTypeBlueRed").equals("蓝字") 
					&& (pdStockSum == null || Double.parseDouble(pdStockSum.get("stockSum").toString())<=0)) {
				errInfo="fail1";//没有库存
			} else {
				if(!pd.containsKey("MaterialQuantity") || "".equals(pd.get("MaterialQuantity")))pd.put("MaterialQuantity", 0);
				if(!pd.containsKey("TareWeightOfMaterial") || "".equals(pd.get("TareWeightOfMaterial")))pd.put("TareWeightOfMaterial", 0);
				if(!pd.containsKey("NetWeightOfMaterial") || "".equals(pd.get("NetWeightOfMaterial")))pd.put("NetWeightOfMaterial", 0);
				if(!pd.containsKey("UnitPrice") || "".equals(pd.get("UnitPrice")))pd.put("UnitPrice", 0);
				if(!pd.containsKey("FCost") || "".equals(pd.get("FCost")))pd.put("FCost", 0);
				if(!pd.containsKey("DemandQuantity") || "".equals(pd.get("DemandQuantity")))pd.put("DemandQuantity", 0);
				if(!pd.containsKey("SplitQuantity") || "".equals(pd.get("SplitQuantity")))pd.put("SplitQuantity", 0);
				StockListDetailService.edit(pd);
				//插入操作日志 TODO
			}
			map.put("result", errInfo);
			return map;
		}
		
		 /**批量选择销售订单物料 
		 * @param StockList_ID
		 * @throws Exception
		 */
		@RequestMapping(value="/selectAllSale")
		@ResponseBody
		public Object selectAllSale() throws Exception{
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
				List<PageData> varList=salesorderdetailService.selectAllSale(ArrayDATA_IDS);
				successCount=varList.size();
				for(PageData pdVar:varList) {
					PageData pdSave=new PageData();
					PageData pdRowNum=StockListDetailService.getRowNum(pd);//生成行号
					if(pdRowNum != null) {
						pdSave.put("RowNum", pdRowNum.get("RowNum").toString());		
					}
					pdSave.put("StockListDetail_ID", this.get32UUID());	//主键
					pdSave.put("DataSources", "销售订单");
					pdSave.put("SourceOrderNum", pdVar.getString("OrderNum"));
					pdSave.put("SourceRowNum", pdVar.get("FROWNO").toString());
					pdSave.put("StockList_ID", pd.getString("StockList_ID"));
					pdSave.put("LineCloseIf", "N");
					pdSave.put("QRCodeInformation", "");//二维码信息，W/Y,YL,唯一码/物料代码,YL,数量,YL,辅助属性值代码（主工单代码）TODO
					pdSave.put("FItem", "");
					pdSave.put("MaterialBatchNumber", "");//
					pdSave.put("FCode", pdVar.getString("MaterialID"));
					pdSave.put("MaterialQuantity",  pdVar.get("FCanUseCount").toString());
					pdSave.put("MaterialSProp", "");//TODO
					String WorkOrderNum = pdVar.getString("WorkOrderNum");
					pdVar.put("MAT_AUXILIARYMX_CODE", WorkOrderNum);
					PageData materialSPropKeyPd = mat_auxiliarymxService.findByMAT_AUXILIARYMX_CODE(pdVar);
					pdSave.put("MaterialSPropKey", null==materialSPropKeyPd 
							|| !materialSPropKeyPd.containsKey("MAT_AUXILIARYMX_ID")?
							"":materialSPropKeyPd.getString("MAT_AUXILIARYMX_ID"));
					pdSave.put("WarehouseID", "");//TODO
					pdSave.put("PositionID", "");//TODO
					pdSave.put("SubordinateInventory", "工厂库");
					pdSave.put("FUnit", pdVar.getString("UNIT_INFO_ID"));
					pdSave.put("FIsScan", "N");
					pdSave.put("DemandQuantity", pdVar.get("TotalCount").toString());
					pdSave.put("SplitQuantity", 0);//TODO
					pdSave.put("UNIQUE_CODE_WHETHER", pdVar.getString("UNIQUE_CODE_WHETHER"));
					pdSave.put("MAT_CODE", pdVar.getString("MAT_CODE"));
					pdSave.put("MAT_NAME", pdVar.getString("MAT_NAME"));
					pdSave.put("METADATA_WHETHER", "Y");
					pdSave.put("SplitStatus", "未拆分");
					StockListDetailService.save(pdSave);
					salesorderdetailService.calFPushCount(pdSave);//反写源单下推数量
				}
				errInfo = "success";
				//插入操作日志
			}else{
				errInfo = "error";
			}
			map.put("totalCount", totalCount);//选择总条数		
			map.put("successCount", successCount);//导入成功条数		
			map.put("result", errInfo);				//返回结果
			return map;
		}
		/**出库单明细列表
		 * @author 管悦
		 * @date 2020-11-18
		 * @param 
		 * DocumentTypeInOut：区分出入库、入库/出库
		 * StockList_ID
		 * @throws Exception
		 */
		@RequestMapping(value="/listOut")
		//@RequiresPermissions("StockListDetail:list")
		@ResponseBody
		public Object listOut(Page page) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			if(StringUtil.isEmpty(pd.getString("KEYWORDS_zhuisu"))){
				pd.put("KEYWORDS_zhuisu", "NO");
			}
			page.setPd(pd);
			List<PageData>	varList = StockListDetailService.list(page);	//列出StockListDetail列表
			for(int i=0;i<varList.size();i++) {
				PageData pdKC=new PageData();
				pdKC.put("WarehouseID", varList.get(i).getString("WarehouseID"));
				pdKC.put("ItemID", varList.get(i).getString("FCode"));
				pdKC.put("MaterialSPropKey", varList.get(i).getString("MaterialSPropKey"));
				PageData pdStockSum=StockService.getSum(pdKC);//即时库存
				if(pdStockSum != null) {
					varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
				}else {
					varList.get(i).put("stockSum", "0");
				}
			}
			//插入操作日志	
			map.put("varList", varList);
			map.put("page", page);
			map.put("result", errInfo);
			return map;
		}
		
		/**出库红字单添加物料列表
		 * @author 管悦
		 * @date 2020-11-15
		 * @param StockList_ID：入库单ID
		 * DocumentTypeInOut：区分出入库、入库/出库
		 * @throws Exception
		 */
		@RequestMapping(value="/listAllRedMatOut")
		@ResponseBody
		public Object listAllRedMatOut() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String KEYWORDS_DocumentNo = pd.getString("KEYWORDS_DocumentNo");						//红字单编号
			if(Tools.notEmpty(KEYWORDS_DocumentNo))pd.put("KEYWORDS_DocumentNo", KEYWORDS_DocumentNo.trim());
			String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
			if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
			String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
			if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
			List<PageData>	varList = StockListDetailService.listAllRedMat(pd);	
			for(int i=0;i<varList.size();i++) {
				PageData pdKC=new PageData();
				pdKC.put("WarehouseID", varList.get(i).getString("WarehouseID"));
				pdKC.put("ItemID", varList.get(i).getString("FCode"));
				pdKC.put("MaterialSPropKey", varList.get(i).getString("MaterialSPropKey"));
				PageData pdStockSum=StockService.getSum(pdKC);//即时库存
				if(pdStockSum != null) {
					varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
				}else {
					varList.get(i).put("stockSum", "0");
				}
			}
			//插入操作日志
			map.put("varList", varList);
			map.put("result", errInfo);
			return map;
		}	
		/**批量选择蓝字出库单物料 
		 * @param StockList_ID
		 * DocumentTypeInOut：区分出入库、入库/出库
		 * @throws Exception
		 */
		@RequestMapping(value="/selectAllBlueOut")
		@ResponseBody
		public Object selectAllBlueOut() throws Exception{
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
				List<PageData> varList=StockListDetailService.selectAllBlueOut(ArrayDATA_IDS);
				successCount=varList.size();
				for(PageData pdVar:varList) {
					PageData pdSave=new PageData();
					PageData pdRowNum=StockListDetailService.getRowNum(pd);//生成行号
					pdVar.put("RowNumx", pdVar.get("RowNum").toString());
					if(pdRowNum != null) {
						pdVar.put("RowNum", pdRowNum.get("RowNum").toString());
						
					}
					
					pdVar.put("StockListDetail_ID", this.get32UUID());	//主键
					pdVar.put("DataSources", "出库单");
					pdVar.put("LineCloseIf", "N");
					pdVar.put("SourceOrderNum", pdVar.getString("DocumentNo"));
					pdVar.put("SourceRowNum", pdVar.get("RowNumx").toString());
					pdVar.put("StockList_ID", pd.getString("StockList_ID"));
					pdVar.put("FPushCount", "0");
					StockListDetailService.save(pdVar);
					pdVar.put("DocumentTypeInOut", pd.getString("DocumentTypeInOut"));
					StockListDetailService.calFPushCount(pdVar);//反写入库单明细下推数量
				}
				errInfo = "success";
				//插入操作日志
			}else{
				errInfo = "error";
			}
			map.put("totalCount", totalCount);//选择总条数		
			map.put("successCount", successCount);//导入成功条数		
			map.put("result", errInfo);				//返回结果
			return map;
		}
		/**入库单拆分物料
		 * @author 管悦
		 * @date 2020-12-2
		 * @param
		 * StockList_ID
		 * @throws Exception
		 */
		/*@RequestMapping(value="/splitMat")
		@ResponseBody
		public Object splitMat() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();		
			pd.put("FRelatedID", pd.getString("StockListDetail_ID"));
			PageData pdNum =MATSPLITService.getNum(pd);//获取拆分物料数量
			PageData pdMain = StockListDetailService.findById(pd);	//根据ID读取
			String MaterialQuantity =(pdMain.get("MaterialQuantity")==null || pdMain.get("MaterialQuantity").equals(""))?"0.00":pdMain.get("MaterialQuantity").toString();
			if(Double.parseDouble(MaterialQuantity)==0) {
				errInfo = "fail1";//数量不能为0
			}else if(null == pdMain.get("UNIQUE_CODE_WHETHER") || !pdMain.get("UNIQUE_CODE_WHETHER").equals("是")) {
				errInfo = "fail3";//非一物一码管理物料
			}else if(pdNum != null && Double.parseDouble(pdNum.get("FNum").toString())>0) {
				errInfo = "fail2";//不能再次拆分
			}else {
			int index = MaterialQuantity.lastIndexOf(".");
			int QuantityZ =Integer.parseInt(MaterialQuantity.substring(0, index));
			double QuantityX =Double.parseDouble("0."+MaterialQuantity.substring(index + 1));
			if(QuantityZ >0) {//拆分整数部分
				for(int i=0;i<QuantityZ;i++) {
					PageData pdMATSPLIT=new PageData();
					pdMATSPLIT.put("MATSPLIT_ID", this.get32UUID());	//主键
					pdMATSPLIT.put("ItemID", pdMain.getString("FCode"));	//
					pdMATSPLIT.put("FRelatedID", pdMain.getString("StockListDetail_ID"));	//
					pdMATSPLIT.put("FMainUnit", pdMain.getString("MAT_MAIN_UNIT"));	//
					pdMATSPLIT.put("FQuantity", "1");	//
					pdMATSPLIT.put("FNAME", Jurisdiction.getName());
					pdMATSPLIT.put("FCreator", staffService.getStaffId(pdMATSPLIT).getString("STAFF_ID"));//查询职员ID
					//pdMATSPLIT.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
					pdMATSPLIT.put("FCreatetime", Tools.date2Str(new Date()));
					MATSPLITService.save(pdMATSPLIT);
				}
				
			}
			if(QuantityX >0) {//拆分小数部分
				PageData pdMATSPLIT=new PageData();
				pdMATSPLIT.put("MATSPLIT_ID", this.get32UUID());	//主键
				pdMATSPLIT.put("ItemID", pdMain.getString("FCode"));	//
				pdMATSPLIT.put("FRelatedID", pdMain.getString("StockListDetail_ID"));	//
				pdMATSPLIT.put("FMainUnit", pdMain.getString("MAT_MAIN_UNIT"));	//
				pdMATSPLIT.put("FQuantity", QuantityX);	//
				pdMATSPLIT.put("FNAME", Jurisdiction.getName());
				pdMATSPLIT.put("FCreator", staffService.getStaffId(pdMATSPLIT).getString("STAFF_ID"));//查询职员ID
				//pdMATSPLIT.put("FCreator", "c3e8a7d350cc43d9b9e87641947168b8");
				pdMATSPLIT.put("FCreatetime", Tools.date2Str(new Date()));
				MATSPLITService.save(pdMATSPLIT);					
			}
		}	
			map.put("result", errInfo);
			return map;
		}*/
		/**入库单拆分物料
		 * ！！！！！！！！！！！！！类型码物料（不支持一物一码物料）！！！！！！！！！！！！！
		 * 	点击第一行数据的拆分，拆分出第二行，更新第一行数据的拆分数量和拆分状态，
		 * 	点击第二行的拆分，拆分出第三行，更新第二行的拆分数量和拆分状态，
		 * @author 宋
		 * @date 2020-12-10
		 * @param pd.MaterialQuantity 实际数量
		 * @param pd.StockListDetail_ID 明细id
		 * @throws Exception
		 */
		@RequestMapping(value="/splitMat")
		@ResponseBody
		public Object splitMat() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData pdSave = new PageData();
			pd = this.getPageData();
			String MaterialQuantity = pd.get("MaterialQuantity").toString();//手写输入将要放入库位的物料实际数量
			String StockListDetail_ID=pd.getString("StockListDetail_ID");
			pd = StockListDetailService.findById(pd);	//根据ID读取
			BigDecimal SplitQuantity = new BigDecimal(pd.get("SplitQuantity").toString());
			//如果是不支持一物一码的物料
			if(null != pd && pd.containsKey("UNIQUE_CODE_WHETHER") && "否".equals(pd.getString("UNIQUE_CODE_WHETHER"))){
				pd.put("SplitQuantity", new BigDecimal(MaterialQuantity));//将拆分数量更新为实际数量
				pd.put("MaterialQuantity", new BigDecimal(MaterialQuantity));
				pd.put("SplitStatus", "已拆分");
				StockListDetailService.edit(pd);//更新被点击数据的拆分数量
				pdSave=pd;
				pdSave.put("StockListDetail_ID", this.get32UUID());	//主键
				PageData pdRowNum=StockListDetailService.getRowNum(pd);//生成行号
				if(pdRowNum != null) {
					pdSave.put("RowNum", pdRowNum.get("RowNum").toString());
				}
				pdSave.put("WarehouseID", pd.getString("WarehouseID"));//仓库id
				pdSave.put("PositionID", pd.getString("PositionID"));//仓位id
				pdSave.put("MaterialQuantity",  new BigDecimal("0"));//新被拆分出来的数据实际数量为0
				pdSave.put("DemandQuantity", new BigDecimal(pd.get("DemandQuantity").toString()));//需求数量
				pdSave.put("SplitQuantity", SplitQuantity.subtract(new BigDecimal(MaterialQuantity)));//拆分数量为拆分数量-实际数量
				pdSave.put("SplitStatus", "未拆分");
				pdSave.put("RelationID", StockListDetail_ID);
				StockListDetailService.save(pdSave);
			} else {
				errInfo="error";
			}
			map.put("result", errInfo);
			return map;
		}

		/**打印物料列表
		 * @author 管悦
		 * @date 2020-12-4
		 * @param StockList_ID：入库单ID
		 * @throws Exception
		 */
		@RequestMapping(value="/listAllPrintMat")
		@ResponseBody
		public Object listAllPrintMat() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			String KEYWORDS_DocumentNo = pd.getString("KEYWORDS_DocumentNo");						//编号
			if(Tools.notEmpty(KEYWORDS_DocumentNo))pd.put("KEYWORDS_DocumentNo", KEYWORDS_DocumentNo.trim());
			String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
			if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
			String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
			if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
			List<PageData>	varList = StockListDetailService.listAllPrintMat(pd);	
			//插入操作日志
			map.put("varList", varList);
			map.put("result", errInfo);
			return map;
		}	
}
