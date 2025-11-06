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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.StockOperationRecordService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：库存
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/Stock")
public class StockController extends BaseController {
	
	@Autowired
	private StockService StockService;
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private StockOperationRecordService stockOperationRecordService;//库存操作记录
	
	/**入库红字
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/inStockRed")
	//@RequiresPermissions("Stock:edit")
	@ResponseBody
	public Object inStockRed() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("num", 6);//入库数量
		pd.put("WarehouseID", "6bf0c8494ad24b26a88135dc70e643f7");//仓库id
		pd.put("ItemID", "0219d7eba107404c8c7adb8d991eb694");//物料id
		StockService.inStockRed(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**入库蓝字
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/inStock")
	//@RequiresPermissions("Stock:edit")
	@ResponseBody
	public Object inStock() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockService.inStock(pd);
		map.put("result", errInfo);
		return map;
	}
	/**出库红字
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/outStockRed")
	//@RequiresPermissions("Stock:edit")
	@ResponseBody
	public Object outStockRed() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockService.outStockRed(pd);
		map.put("result", errInfo);
		return map;
	}
	/**出库蓝字
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/outStock")
	//@RequiresPermissions("Stock:edit")
	@ResponseBody
	public Object outStock() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("num", 6);//出库数量
		pd.put("WarehouseID", "6bf0c8494ad24b26a88135dc70e643f7");//仓库id
		pd.put("ItemID", "0219d7eba107404c8c7adb8d991eb694");//物料id
		pd.put("MaterialSPropKey", "4486905b9e47428985c36c9d55d21ad7");//辅助属性值id
		StockService.outStock(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**修改库存数量
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editActualCount")
	//@RequiresPermissions("Stock:edit")
	@ResponseBody
	public Object editActualCount() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("LastModifiedTime", Tools.date2Str(new Date()));
		StockService.editActualCount(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**获取仓库id和物料id和辅助属性值ID下的库存数量 
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getSum")
	@ResponseBody
	public Object getSum()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData stockpd = StockService.getSum(pd);
		BigDecimal stocknum = new BigDecimal("0");//库存数量
		if(null!=stockpd && stockpd.containsKey("stockSum") && null!=stockpd.get("stockSum")) {
			stocknum = new BigDecimal(stockpd.get("stockSum").toString());
		}
		pd.put("sum", stocknum);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**获取仓库id和物料id和辅助属性值id下的库存列表
	 * @param pd ItemID WarehouseID MaterialSPropKey
	 * @throws Exception
	 */
	@RequestMapping(value="/stockList")
	@ResponseBody
	public Object stockList()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockService.stockList(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**库存查询
	 * @param pd ItemID MAT_NAME PositionID
	 * @throws Exception
	 */
	@RequestMapping(value="/viewList")
	@ResponseBody
	public Object viewList(Page page)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = StockService.stocklistPage(page);
		map.put("varList", list);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	
	/**
	 * 库存查询-汇总
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/stockTotallistPage")
	@ResponseBody
	public Object stockTotallistPage(Page page)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = StockService.stockTotallistPage(page);
		map.put("varList", list);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 车间-库存查询-汇总
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/stockTotallistPageCJ")
	@ResponseBody
	public Object stockTotallistPageCJ(Page page)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = StockService.stockTotallistPageCJ(page);
		map.put("varList", list);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**车间-库存查询
	 * @param pd ItemID MAT_NAME PositionID
	 * @throws Exception
	 */
	@RequestMapping(value="/viewListCJ")
	@ResponseBody
	public Object viewListCJ(Page page)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = StockService.stocklistPageCJ(page);
		map.put("varList", list);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("Stock:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("Stock_ID", this.get32UUID());	//主键
		String currentTime = Tools.date2Str(new Date());
		pd.put("FCreatePersonID", "");//TODO
		pd.put("FCreateTime", currentTime);
		pd.put("LastModifiedTime", currentTime);
		pd.put("LastCountTime", currentTime);
		StockService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("Stock:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		StockService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("Stock:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String currentTime = Tools.date2Str(new Date());
		pd.put("LastModifiedTime", currentTime);
		StockService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("Stock:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = StockService.list(page);	//列出Stock列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**批号查询列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getBatchList")
	//@RequiresPermissions("Stock:list")
	@ResponseBody
	public Object getBatchList(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = StockService.getBatchList(page);	//列出Stock列表
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
	//@RequiresPermissions("Stock:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = StockService.listAll(pd);	//列出Stock列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("Stock:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = StockService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("Stock:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			StockService.deleteAll(ArrayDATA_IDS);
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
		titles.add("存储状态(工厂,车间)");	//2
		titles.add("二维码");	//3
		titles.add("一物码");	//4
		titles.add("物料ID");	//5
		titles.add("物料辅助属性");	//6
		titles.add("物料辅助属性值");	//7
		titles.add("规格描述");	//8
		titles.add("仓库ID");	//9
		titles.add("仓位ID");	//10
		titles.add("实际数量");	//11
		titles.add("使用数量");	//12
		titles.add("是否占用");	//13
		titles.add("单位");	//14
		titles.add("等级");	//15
		titles.add("销售订单");	//16
		titles.add("项目编号");	//17
		titles.add("质量状态(待检,合格)");	//18
		titles.add("质检时间");	//19
		titles.add("业务状态");	//20
		titles.add("批次");	//21
		titles.add("客户ID");	//22
		titles.add("供应商ID");	//23
		titles.add("保质期");	//24
		titles.add("计划跟踪号");	//25
		titles.add("供应商批次");	//26
		titles.add("入厂批次");	//27
		titles.add("生产批次");	//28
		titles.add("运行状态(在用,停用)");	//29
		titles.add("创建人");	//30
		titles.add("创建时间");	//31
		titles.add("使用时间");	//32
		titles.add("生产日期");	//33
		titles.add("入厂规格");	//34
		titles.add("最近修改时间");	//35
		titles.add("上次盘点时间");	//36
		titles.add("备注");	//37
		dataMap.put("titles", titles);
		List<PageData> varOList = StockService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("Stock_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("StorageStatus"));	    //2
			vpd.put("var3", varOList.get(i).getString("QRCode"));	    //3
			vpd.put("var4", varOList.get(i).getString("OneThingCode"));	    //4
			vpd.put("var5", varOList.get(i).getString("ItemID"));	    //5
			vpd.put("var6", varOList.get(i).getString("MaterialSProp"));	    //6
			vpd.put("var7", varOList.get(i).getString("MaterialSPropKey"));	    //7
			vpd.put("var8", varOList.get(i).getString("SpecificationDesc"));	    //8
			vpd.put("var9", varOList.get(i).getString("WarehouseID"));	    //9
			vpd.put("var10", varOList.get(i).getString("PositionID"));	    //10
			vpd.put("var11", varOList.get(i).get("ActualCount").toString());	//11
			vpd.put("var12", varOList.get(i).get("UseCount").toString());	//12
			vpd.put("var13", varOList.get(i).getString("UseIf"));	    //13
			vpd.put("var14", varOList.get(i).getString("FUnit"));	    //14
			vpd.put("var15", varOList.get(i).get("FLevel").toString());	//15
			vpd.put("var16", varOList.get(i).getString("SalesOrder"));	    //16
			vpd.put("var17", varOList.get(i).getString("ProjectNum"));	    //17
			vpd.put("var18", varOList.get(i).getString("QualityStatus"));	    //18
			vpd.put("var19", varOList.get(i).getString("QITime"));	    //19
			vpd.put("var20", varOList.get(i).getString("BusinessStatus"));	    //20
			vpd.put("var21", varOList.get(i).getString("FBatch"));	    //21
			vpd.put("var22", varOList.get(i).getString("CustomerID"));	    //22
			vpd.put("var23", varOList.get(i).getString("SupplierID"));	    //23
			vpd.put("var24", varOList.get(i).getString("GuaranteePeriod"));	    //24
			vpd.put("var25", varOList.get(i).getString("PlanTrackingNumber"));	    //25
			vpd.put("var26", varOList.get(i).getString("SupplierBatch"));	    //26
			vpd.put("var27", varOList.get(i).getString("IncomingBatch"));	    //27
			vpd.put("var28", varOList.get(i).getString("ProductionBatch"));	    //28
			vpd.put("var29", varOList.get(i).getString("RunningState"));	    //29
			vpd.put("var30", varOList.get(i).getString("FCreatePersonID"));	    //30
			vpd.put("var31", varOList.get(i).getString("FCreateTime"));	    //31
			vpd.put("var32", varOList.get(i).getString("UsageTime"));	    //32
			vpd.put("var33", varOList.get(i).getString("DateOfManufacture"));	    //33
			vpd.put("var34", varOList.get(i).getString("IncomingSpecification"));	    //34
			vpd.put("var35", varOList.get(i).getString("LastModifiedTime"));	    //35
			vpd.put("var36", varOList.get(i).getString("LastCountTime"));	    //36
			vpd.put("var37", varOList.get(i).getString("FExplanation"));	    //37
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**库存预警列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/safeStaocklist")
	//@RequiresPermissions("mat_basic:list")
	@ResponseBody
	public Object safeStaocklist(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = StockService.safeStaocklistPage(page);	//列出MAT_BASIC列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
}
