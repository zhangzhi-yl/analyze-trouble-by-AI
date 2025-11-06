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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.Jurisdiction;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.ForwardApplicationDetailService;
import org.yy.service.mm.ForwardApplicationService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.SALESORDERDETAILService;
import org.yy.service.pp.SALESORDERService;

/** 
 * 说明：发运申请明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ForwardApplicationDetail")
public class ForwardApplicationDetailController extends BaseController {
	@Autowired
	private ForwardApplicationService ForwardApplicationService;
	@Autowired
	private ForwardApplicationDetailService ForwardApplicationDetailService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private SALESORDERService salesorderService;
	@Autowired
	private SALESORDERDETAILService salesorderdetailService;
	@Autowired
	private StockService StockService;
	@Autowired
	private StaffService staffService;
	/**发运申请明细新增空白行
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("ForwardApplicationDetail:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ForwardApplicationDetail_ID", this.get32UUID());	//主键
		pd.put("LineClose", "N");	//行关闭
		pd.put("FCount", "0");	//数量
		pd.put("ThisExecutionNumber", "0");	//本次执行数
		pd.put("DataSources", "手动创建");	//数据来源
		PageData pdROWNO=ForwardApplicationDetailService.getRowNum(pd);//生成行号
		if(pdROWNO != null) {
			pd.put("RowNum", pdROWNO.get("RowNum").toString());
		}
		ForwardApplicationDetailService.save(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请明细");//功能项
		pdOp.put("OperationType", "新增");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("ForwardApplicationDetail_ID"));
		operationrecordService.save(pdOp);
		map.put("result", errInfo);
		map.put("pd", pd);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("ForwardApplicationDetail:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ForwardApplicationDetailService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**发运申请明细修改
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("ForwardApplicationDetail:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ForwardApplicationDetailService.edit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请明细");//功能项
		pdOp.put("OperationType", "修改");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("ForwardApplicationDetail_ID"));	
		operationrecordService.save(pdOp);
		PageData pdSave =ForwardApplicationDetailService.findById(pd);
		salesorderdetailService.calFPushCountForward(pdSave);//反写源单下推数量
		map.put("result", errInfo);
		return map;
	}
	
	/**发运申请明细列表
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("ForwardApplicationDetail:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = ForwardApplicationDetailService.list(page);	//列出ForwardApplicationDetail列表
		for(int i=0;i<varList.size();i++) {
			PageData pdKC=new PageData();
			//pdKC.put("WarehouseID", varList.get(i).getString("DeliveryWarehouseID"));
			pdKC.put("ItemID", varList.get(i).getString("ItemID"));
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
	/**
	 * 全部列表
	 * @param pd.ForwardApplication_ID 主表id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = ForwardApplicationDetailService.listAll(pd);
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("ForwardApplicationDetail:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ForwardApplicationDetailService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("ForwardApplicationDetail:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ForwardApplicationDetailService.deleteAll(ArrayDATA_IDS);
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
		titles.add("发运申请ID");	//1
		titles.add("数据来源");	//2
		titles.add("源单单号");	//3
		titles.add("物料ID");	//4
		titles.add("规格描述");	//5
		titles.add("本次执行数");	//6
		titles.add("数量");	//7
		titles.add("行号");	//8
		titles.add("行关闭");	//9
		titles.add("单位");	//10
		titles.add("可用库存");	//11
		titles.add("销售订单ID");	//12
		titles.add("客户");	//13
		titles.add("交货时间");	//14
		titles.add("备注");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = ForwardApplicationDetailService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ShippingApplicationID"));	    //1
			vpd.put("var2", varOList.get(i).getString("DataSources"));	    //2
			vpd.put("var3", varOList.get(i).getString("SourceOrderNum"));	    //3
			vpd.put("var4", varOList.get(i).getString("ItemID"));	    //4
			vpd.put("var5", varOList.get(i).getString("SpecificationDesc"));	    //5
			vpd.put("var6", varOList.get(i).getString("ThisExecutionNumber"));	    //6
			vpd.put("var7", varOList.get(i).get("FCount").toString());	//7
			vpd.put("var8", varOList.get(i).get("RowNum").toString());	//8
			vpd.put("var9", varOList.get(i).getString("LineClose"));	    //9
			vpd.put("var10", varOList.get(i).getString("FUnit"));	    //10
			vpd.put("var11", varOList.get(i).get("AvailableStock").toString());	//11
			vpd.put("var12", varOList.get(i).getString("SalesOrderID"));	    //12
			vpd.put("var13", varOList.get(i).getString("FCustomer"));	    //13
			vpd.put("var14", varOList.get(i).getString("DeliveryTime"));	    //14
			vpd.put("var15", varOList.get(i).getString("FExplanation"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**行关闭/反行关闭
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * ForwardApplicationDetail_ID:单据明细ID
	 * LineClose:行关闭（Y/N）
	 * @throws Exception
	 */
	@RequestMapping(value="/rowClose")
	@ResponseBody
	public Object rowClose() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ForwardApplicationDetailService.rowClose(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		String LineClose= pd.getString("LineClose");
		if(LineClose.equals("Y")) {
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
		pdOp.put("FunctionItem", "发运申请单明细");//功能项
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("ForwardApplicationDetail_ID"));//删改数据ID
		operationrecordService.save(pdOp);	
		PageData pdSave =ForwardApplicationDetailService.findById(pd);
		salesorderdetailService.calFPushCountForward(pdSave);//反写源单下推数量
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**发运申请-批量选择销售订单物料 
	 * @param ForwardApplicationDetail_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/selectAllSaleForward")
	@ResponseBody
	public Object selectAllSaleForward() throws Exception{
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
			List<PageData> varList=salesorderdetailService.selectAllSaleForward(ArrayDATA_IDS);
			successCount=varList.size();
			String OrderNum=varList.get(0).getString("OrderNum");
			for(PageData pdVar:varList) {
				PageData pdSave=new PageData();
				PageData pdROWNO=ForwardApplicationDetailService.getRowNum(pd);//生成行号
				if(pdROWNO != null) {
					pdSave.put("RowNum", pdROWNO.get("RowNum").toString());
				}
				pdSave.put("DataSources", "销售订单");
				pdSave.put("ForwardApplicationDetail_ID", this.get32UUID());	//主键
				pdSave.put("LineClose", "N");	//行关闭
				pdSave.put("ThisExecutionNumber", pdVar.get("FCanUseCount").toString());	//本次执行数
				pdSave.put("SourceOrderNum", pdVar.getString("OrderNum"));
				pdSave.put("SourceRowNum", pdVar.get("FROWNO").toString());
				pdSave.put("ItemID", pdVar.getString("MaterialID"));
				pdSave.put("FCount",  pdVar.get("FCanUseCount").toString());
				pdSave.put("FUnit",  pdVar.get("FUnit").toString());
				pdSave.put("ForwardApplication_ID", pd.getString("ForwardApplication_ID"));
				ForwardApplicationDetailService.save(pdSave);
				salesorderdetailService.calFPushCountForward(pdSave);//反写源单下推数量
				
			}
			errInfo = "success";
			PageData forward = ForwardApplicationService.findById(pd);
			pd.put("OrderNum", OrderNum);
			PageData salesOrder = salesorderService.findByOrderNum(pd);
			forward.put("FAddress", salesOrder.getString("FAddress"));
			forward.put("FCustomer", salesOrder.getString("FCustomer"));
			ForwardApplicationService.edit(forward);
			//插入操作日志
			PageData pdOp=new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
			pdOp.put("FNAME", Jurisdiction.getName());
			pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
			//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
			pdOp.put("FunctionType", "");//功能类型
			pdOp.put("FunctionItem", "发运申请明细-销售订单物料");//功能项
			pdOp.put("OperationType", "批量选择");//操作类型
			pdOp.put("Fdescribe", "");//描述
			pdOp.put("DeleteTagID", pd.get("ForwardApplicationDetail_ID"));
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
