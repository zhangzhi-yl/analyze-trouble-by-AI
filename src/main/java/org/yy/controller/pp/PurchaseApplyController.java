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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.util.Jurisdiction;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.PurchasDetailsService;
import org.yy.service.pp.PurchaseApplyService;

/** 
 * 说明：采购申请
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-21
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PurchaseApply")
public class PurchaseApplyController extends BaseController {
	
	@Autowired
	private PurchaseApplyService PurchaseApplyService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PurchasDetailsService PurchasDetailsService;
	
	/**采购申请新增
	 * @author 管悦
	 * @date 2020-11-21
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("PurchaseApply:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = PurchaseApplyService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {	
		pd.put("PurchaseApply_ID", this.get32UUID());	//主键
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FMakeBillsPersoID", "c3e8a7d350cc43d9b9e87641947168b8");
		pd.put("FMakeBillsTime", Tools.date2Str(new Date()));
		pd.put("FCheckFlag", "N");
		PurchaseApplyService.save(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请");//功能项
		pdOp.put("OperationType", "新增");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchaseApply_ID"));
		operationrecordService.save(pdOp);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**采购申请删除
	 * @author 管悦
	 * @date 2020-11-21
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("PurchaseApply:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseApplyService.delete(pd);
		//关联行关闭采购申请明细
		pd.put("RowClose", "Y");
		PurchasDetailsService.rowCloseByApplyId(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请");//功能项
		pdOp.put("OperationType", "删除");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchaseApply_ID"));
		operationrecordService.save(pdOp);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**采购申请修改
	 * @author 管悦
	 * @date 2020-11-21
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("PurchaseApply:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = PurchaseApplyService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {	
		PurchaseApplyService.edit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请");//功能项
		pdOp.put("OperationType", "修改");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchaseApply_ID"));
		operationrecordService.save(pdOp);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**采购申请列表
	 * @author 管悦
	 * @date 2020-11-21
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("PurchaseApply:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = PurchaseApplyService.list(page);	//列出PurchaseApply列表
		String KEYWORDS_FNum = pd.getString("KEYWORDS_FNum");						//采购申请编号
		if(Tools.notEmpty(KEYWORDS_FNum))pd.put("KEYWORDS_FNum", KEYWORDS_FNum.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum");						//物料编号
		if(Tools.notEmpty(KEYWORDS_MaterialNum))pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName");						//物料名称
		if(Tools.notEmpty(KEYWORDS_MaterialName))pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		String KEYWORDS_FMakeBillsTimeStart = pd.getString("KEYWORDS_FMakeBillsTimeStart");						//创建开始时间
		if(Tools.notEmpty(KEYWORDS_FMakeBillsTimeStart))pd.put("KEYWORDS_FMakeBillsTimeStart", KEYWORDS_FMakeBillsTimeStart.trim());
		String KEYWORDS_FMakeBillsTimeEnd = pd.getString("KEYWORDS_FMakeBillsTimeEnd");						//创建结束时间
		if(Tools.notEmpty(KEYWORDS_FMakeBillsTimeEnd))pd.put("KEYWORDS_FMakeBillsTimeEnd", KEYWORDS_FMakeBillsTimeEnd.trim());
		String KEYWORDS_DemandTimeStart = pd.getString("KEYWORDS_DemandTimeStart");						//需求开始时间
		if(Tools.notEmpty(KEYWORDS_DemandTimeStart))pd.put("KEYWORDS_DemandTimeStart", KEYWORDS_DemandTimeStart.trim());
		String KEYWORDS_DemandTimeEnd = pd.getString("KEYWORDS_DemandTimeEnd");						//需求结束时间
		if(Tools.notEmpty(KEYWORDS_DemandTimeEnd))pd.put("KEYWORDS_DemandTimeEnd", KEYWORDS_DemandTimeEnd.trim());		
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		operationrecordService.save(pdOp);
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
	//@RequiresPermissions("PurchaseApply:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PurchaseApplyService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("PurchaseApply:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			PurchaseApplyService.deleteAll(ArrayDATA_IDS);
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
		titles.add("客户");	//2
		titles.add("关联编号");	//3
		titles.add("业务类型");	//4
		titles.add("来源");	//5
		titles.add("编号");	//6
		titles.add("订货人");	//7
		titles.add("备注");	//8
		titles.add("申请人");	//9
		titles.add("制单人");	//10
		titles.add("制单时间");	//11
		titles.add("审核人");	//12
		titles.add("审核时间");	//13
		titles.add("审核标志");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = PurchaseApplyService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PurchaseApply_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCustomer"));	    //2
			vpd.put("var3", varOList.get(i).getString("RelationNum"));	    //3
			vpd.put("var4", varOList.get(i).getString("BusinessType"));	    //4
			vpd.put("var5", varOList.get(i).getString("DataSources"));	    //5
			vpd.put("var6", varOList.get(i).getString("FNum"));	    //6
			vpd.put("var7", varOList.get(i).getString("FOrderer"));	    //7
			vpd.put("var8", varOList.get(i).getString("FExplanation"));	    //8
			vpd.put("var9", varOList.get(i).getString("ApplyPerson"));	    //9
			vpd.put("var10", varOList.get(i).getString("FMakeBillsPersoID"));	    //10
			vpd.put("var11", varOList.get(i).getString("FMakeBillsTime"));	    //11
			vpd.put("var12", varOList.get(i).getString("FCheckPersonID"));	    //12
			vpd.put("var13", varOList.get(i).getString("FCheckTime"));	    //13
			vpd.put("var14", varOList.get(i).getString("FCheckFlag"));	    //14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**审核或反审核采购申请
	 * @author 管悦
	 * @date 2020-11-06
	 * @param PurchaseApply_ID:采购申请ID、FCheckFlag:审核标志（Y、N）
	 * @throws Exception
	 */
	@RequestMapping(value="/editAudit")
	//@RequiresPermissions("salesorder:del")
	@ResponseBody
	public Object editAudit() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCheckPersonID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FCheckPersonID", "c3e8a7d350cc43d9b9e87641947168b8");
		pd.put("FCheckTime", Tools.date2Str(new Date()));
		PurchaseApplyService.editAudit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		String FCheckFlag= pd.getString("FCheckFlag");
		if(FCheckFlag.equals("Y")) {
			pdOp.put("OperationType", "审核");//操作类型
		}else {
			pdOp.put("OperationType", "反审核");//操作类型
		}
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "采购申请");//功能项
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("PurchaseApply_ID"));//删改数据ID
		operationrecordService.save(pdOp);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**获取采购申请编号列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-11
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getCGNumList")
	@ResponseBody
	public Object getCGNumList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = PurchaseApplyService.getCGNumList(pd);	
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去新增页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	//@RequiresPermissions("ForwardApplication:edit")
	@ResponseBody
	public Object goAdd() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//pd.put("FNAME", "管悦");
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FOperatorID", staffService.getStaffId(pd).getString("STAFF_ID"));
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	/**根据销售订单ID获取单据下物料
	 * @author 管悦
	 * @date 2020-11-11
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getWLNumList")
	@ResponseBody
	public Object getWLNumList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = PurchaseApplyService.getWLNumList(pd);	
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
