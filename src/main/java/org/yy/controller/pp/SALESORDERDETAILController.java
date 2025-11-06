package org.yy.controller.pp;

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
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.SALESORDERDETAILService;

/** 
 * 说明：销售订单明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/salesorderdetail")
public class SALESORDERDETAILController extends BaseController {
	
	@Autowired
	private SALESORDERDETAILService salesorderdetailService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private StockService StockService;
	/**保存
	 * @author 管悦
	 * @date 2020-11-06
	 * @param 订单明细信息、SalesOrderID:订单ID
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("salesorderdetail:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SalesOrderDetail_ID", this.get32UUID());	//主键
		pd.put("RowClose", "N");	//行关闭
		pd.put("ProductionQuantity", "0");	//投产数量
		pd.put("FPushCount", "0");	//下推数量（推计划工单）
		pd.put("FPushCountShipping", "0");	//下推数量（推发运申请）
		PageData pdROWNO=salesorderdetailService.getFROWNO(pd);//生成行号
		if(pdROWNO != null) {
			pd.put("FROWNO", pdROWNO.get("FROWNO").toString());
		}
		salesorderdetailService.save(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "销售订单明细");//功能项
		pdOp.put("OperationType", "新增");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrderDetail_ID"));//删改数据ID
		operationrecordService.save(pdOp);	
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}
	
	/**行关闭/反行关闭
	 * @author 管悦
	 * @date 2020-11-06
	 * @param SalesOrderDetail_ID:订单明细ID、RowClose:行关闭（Y/N）
	 * @throws Exception
	 */
	@RequestMapping(value="/rowClose")
	@ResponseBody
	public Object rowClose() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		salesorderdetailService.rowClose(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		String RowClose= pd.getString("RowClose");
		if(RowClose.equals("Y")) {
			pdOp.put("OperationType", "行关闭");//操作类型
		}else {
			pdOp.put("OperationType", "反行关闭");//操作类型
		}
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
	    pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "销售订单明细");//功能项
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrderDetail_ID"));//删改数据ID
		operationrecordService.save(pdOp);	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("salesorderdetail:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		salesorderdetailService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @author 管悦
	 * @date 2020-11-06
	 * @param SalesOrderDetail_ID:订单明细ID
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("salesorderdetail:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		salesorderdetailService.edit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "销售订单明细");//功能项
		pdOp.put("OperationType", "修改");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrderDetail_ID"));//删改数据ID
		operationrecordService.save(pdOp);
		map.put("result", errInfo);
		return map;
	}
	
	/**订单明细列表
	 * @author 管悦
	 * @date 2020-11-06
	 * @param SalesOrderID:订单ID
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("salesorderdetail:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = salesorderdetailService.list(page);	//列出SALESORDERDETAIL列表
		for(int i=0;i<varList.size();i++) {
			PageData pdKC=new PageData();
			pdKC.put("ItemID", varList.get(i).getString("MaterialID"));
			PageData pdStockSum=StockService.getSum(pdKC);//即时库存
			if(pdStockSum != null) {
				varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
			}else {
				varList.get(i).put("stockSum", "0");
			}
		}
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "销售订单明细");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrderID"));//删改数据ID
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**订单明细列表
	 * @author 管悦
	 * @date 2020-11-16
	 * @param SalesOrderID:订单ID
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("salesorderdetail:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						
		if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						
		if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		List<PageData>	varList = salesorderdetailService.listAll(pd);	//列出SALESORDERDETAIL列表
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "创建计划工单-销售订单明细");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrderID"));//删改数据ID
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**根据ID获取订单明细详情
	 * @author 管悦
	 * @date 2020-11-06
	 * @param SalesOrderDetail_ID:订单明细ID
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("salesorderdetail:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = salesorderdetailService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("salesorderdetail:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			salesorderdetailService.deleteAll(ArrayDATA_IDS);
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
		titles.add("销售订单ID");	//2
		titles.add("物料ID");	//3
		titles.add("物料辅助属性IF");	//4
		titles.add("物料辅助属性");	//5
		titles.add("占用ID");	//6
		titles.add("客人号码");	//7
		titles.add("项目组");	//8
		titles.add("总数量");	//9
		titles.add("投产数量(自动计算)");	//10
		titles.add("单位");	//11
		titles.add("交货日期");	//12
		titles.add("描述");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = salesorderdetailService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ROWCLOSE"));	    //1
			vpd.put("var2", varOList.get(i).getString("SALESORDERID"));	    //2
			vpd.put("var3", varOList.get(i).getString("MATERIALID"));	    //3
			vpd.put("var4", varOList.get(i).getString("MATERIALSPROPIF"));	    //4
			vpd.put("var5", varOList.get(i).getString("MATERIALSPROP"));	    //5
			vpd.put("var6", varOList.get(i).getString("OCCUPANCYID"));	    //6
			vpd.put("var7", varOList.get(i).getString("CUSTOMERPHONE"));	    //7
			vpd.put("var8", varOList.get(i).getString("PROJECTTEAM"));	    //8
			vpd.put("var9", varOList.get(i).get("TOTALCOUNT").toString());	//9
			vpd.put("var10", varOList.get(i).get("PRODUCTIONQUANTITY").toString());	//10
			vpd.put("var11", varOList.get(i).getString("FUNIT"));	    //11
			vpd.put("var12", varOList.get(i).getString("DELIVERYDATE"));	    //12
			vpd.put("var13", varOList.get(i).getString("FDESCRIBE"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**销售订单明细-物料列表
	 * @author 管悦
	 * @date 2020-11-18
	 * @param StockList_ID：出库单ID
	 * @throws Exception
	 */
	@RequestMapping(value="/listAllMat")
	@ResponseBody
	public Object listAllMat() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum");						//销售订单编号
		if(Tools.notEmpty(KEYWORDS_OrderNum))pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
		if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
		if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		List<PageData>	varList = salesorderdetailService.listAllMat(pd);	
		for(int i=0;i<varList.size();i++) {
			PageData pdKC=new PageData();
			pdKC.put("ItemID", varList.get(i).getString("MaterialID"));
			PageData pdStockSum=StockService.getSum(pdKC);//即时库存
			if(pdStockSum != null) {
				varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
			}else {
				varList.get(i).put("stockSum", "0");
			}
		}
		//插入操作日志 TODO
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**销售订单明细-物料列表-下推发运申请
	 * @author 管悦
	 * @date 2020-11-20
	 * @param ForwardApplication_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/listAllMatForward")
	@ResponseBody
	public Object listAllMatForward() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum");						//销售订单编号
		if(Tools.notEmpty(KEYWORDS_OrderNum))pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
		if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
		if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		List<PageData>	varList = salesorderdetailService.listAllMatForward(pd);	
		for(int i=0;i<varList.size();i++) {
			PageData pdKC=new PageData();
			pdKC.put("ItemID", varList.get(i).getString("MaterialID"));
			PageData pdStockSum=StockService.getSum(pdKC);//即时库存
			if(pdStockSum != null) {
				varList.get(i).put("stockSum", pdStockSum.get("stockSum").toString());
			}else {
				varList.get(i).put("stockSum", "0");
			}
		}
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//查询职员ID
		//pd.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请明细-销售订单物料列表");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.getString("ForwardApplication_ID"));//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
}
