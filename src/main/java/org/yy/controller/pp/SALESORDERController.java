package org.yy.controller.pp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.SALESORDERDETAILService;
import org.yy.service.pp.SALESORDERService;

/** 
 * 说明：销售订单
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/salesorder")
public class SALESORDERController extends BaseController {
	
	@Autowired
	private SALESORDERService salesorderService;
	@Autowired
	private SALESORDERDETAILService salesorderdetailService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	
	/**保存
	 * @author 管悦
	 * @date 2020-11-06
	 * @param FFILEPATH:文件、订单信息、OrderNum:订单编号
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("dtprojectfile:add")
	@ResponseBody
	public Object add(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
		//订单号验重
		PageData pdNum = salesorderService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//订单号重复
		}else {		
		//新增销售订单
		pd.put("SalesOrder_ID", this.get32UUID());	//主键
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FMakeBillsPersoID", "c3e8a7d350cc43d9b9e87641947168b8");
		pd.put("FCreateTime", Tools.date2Str(new Date()));
		pd.put("FMakeBillsTime", Tools.date2Str(new Date()));
		pd.put("FUpdateTime", Tools.date2Str(new Date()));
		pd.put("FStatus", "创建");
		pd.put("FCheckFlag", "N");
		salesorderService.save(pd);
		//上传附件
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		String FFILENAME = "";
		String FPFFILEPATH = "";
		if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal+dateName);// 执行上传
			FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+FFILENAME;
			//附件集插入数据
			PageData pdFile=new PageData();
			pdFile.put("DataSources", "销售订单");
			pdFile.put("AssociationIDTable", "PP_SalesOrder");
			pdFile.put("AssociationID", pd.getString("SalesOrder_ID"));
			pdFile.put("FName", FFILENAME);
			pdFile.put("FUrl", FPFFILEPATH);
			pdFile.put("FExplanation", "");
			pdFile.put("FCreatePersonID",pd.getString("FMakeBillsPersoID"));
			pdFile.put("FCreateTime", Tools.date2Str(new Date()));
			attachmentsetService.check(pdFile);
		}
		
		}
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "销售订单");//功能项
		pdOp.put("OperationType", "新增");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrder_ID"));
		operationrecordService.save(pdOp);
		}
		catch (Exception e) {
			errInfo="error";
		}
		map.put("result", errInfo);
		map.put("SalesOrder_ID", pd.get("SalesOrder_ID"));
		return map;
	}
	
	
	/**删除销售订单
	 * @author 管悦
	 * @date 2020-11-06
	 * @param SalesOrder_ID:销售订单ID
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("salesorder:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		salesorderService.delete(pd);//删除订单
		pd.put("RowClose", "Y");
		pd.put("SalesOrderID", pd.getString("SalesOrder_ID"));
		salesorderdetailService.deleteMxRelated(pd);//关联行关闭订单明细
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "销售订单");//功能项
		pdOp.put("OperationType", "删除");//操作类型
		pdOp.put("Fdescribe", "删除销售订单并行关闭订单明细");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrder_ID"));//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**保存
	 * @author 管悦
	 * @date 2020-11-06
	 * @param FFILEPATH:文件、订单信息、OrderNum:订单编号、SalesOrder_ID:销售订单ID、FTYPE:是否更换附件（Y、N）
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("dtprojectfile:add")
	@ResponseBody
	public Object edit(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FTYPE=pd.getString("FTYPE");
		try {
		//订单号验重
		PageData pdNum = salesorderService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//订单号重复
		}else {		
		pd.put("FUpdateTime", Tools.date2Str(new Date()));
		salesorderService.edit(pd);
		if(FTYPE.equals("Y")) {
		//上传附件
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		String FFILENAME = "";
		String FPFFILEPATH = "";
		if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal+dateName);// 执行上传
			FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+FFILENAME;
			//附件集插入数据
			PageData pdFile=new PageData();
			pdFile.put("DataSources", "销售订单");
			pdFile.put("AssociationIDTable", "PP_SalesOrder");
			pdFile.put("AssociationID", pd.getString("SalesOrder_ID"));
			pdFile.put("FName", FFILENAME);
			pdFile.put("FUrl", FPFFILEPATH);
			pdFile.put("FExplanation", "");
			pdFile.put("FNAME", Jurisdiction.getName());
			pdFile.put("FOperatorID", staffService.getStaffId(pdFile).getString("STAFF_ID"));//操作人	
			pdFile.put("FCreateTime", Tools.date2Str(new Date()));
			attachmentsetService.check(pdFile);
		}
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
		pdOp.put("FunctionItem", "销售订单");//功能项
		pdOp.put("OperationType", "修改");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrder_ID"));//删改数据ID
		operationrecordService.save(pdOp);
		}
		catch (Exception e) {
			errInfo="error";
		}
		map.put("result", errInfo);
		return map;
	}
	
	
	/**列表
	 * @author 管悦
	 * @date 2020-11-06
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("salesorder:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum");//订单编号检索条件
		if(Tools.notEmpty(KEYWORDS_OrderNum))pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		String KEYWORDS_FStatus = pd.getString("KEYWORDS_FStatus");//订单状态检索条件
		if(Tools.notEmpty(KEYWORDS_FStatus))pd.put("KEYWORDS_FStatus", KEYWORDS_FStatus.trim());
		String KEYWORDS_FCustomer = pd.getString("KEYWORDS_FCustomer");//客户名称检索条件
		if(Tools.notEmpty(KEYWORDS_FCustomer))pd.put("KEYWORDS_FCustomer", KEYWORDS_FCustomer.trim());
		page.setPd(pd);
		List<PageData>	varList = salesorderService.list(page);	//列出销售订单列表
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "销售订单");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**根据ID获取订单信息
	 * @author 管悦
	 * @date 2020-11-06
	 * @param SalesOrder_ID:销售订单ID
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("salesorder:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = salesorderService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**审核或反审核销售订单
	 * @author 管悦
	 * @date 2020-11-06
	 * @param SalesOrder_ID:销售订单ID、FCheckFlag:审核标志（Y、N）
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
		salesorderService.editAudit(pd);
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
		pdOp.put("FunctionItem", "销售订单");//功能项
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrder_ID"));//删改数据ID
		operationrecordService.save(pdOp);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("salesorder:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			salesorderService.deleteAll(ArrayDATA_IDS);
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
		titles.add("订单号");	//1
		titles.add("创建时间");	//2
		titles.add("更新时间");	//3
		titles.add("状态");	//4
		titles.add("类型");	//5
		titles.add("客户");	//6
		titles.add("备注");	//7
		titles.add("制单人");	//8
		titles.add("制单时间");	//9
		titles.add("审核标志");	//10
		titles.add("审核人");	//11
		titles.add("审核时间");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = salesorderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ORDERNUM"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCREATETIME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FUPDATETIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FSTATUS"));	    //4
			vpd.put("var5", varOList.get(i).getString("TTYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCUSTOMER"));	    //6
			vpd.put("var7", varOList.get(i).getString("FEXPLANATION"));	    //7
			vpd.put("var8", varOList.get(i).getString("FMAKEBILLSPERSOID"));	    //8
			vpd.put("var9", varOList.get(i).getString("FMAKEBILLSTIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCHECKFLAG"));	    //10
			vpd.put("var11", varOList.get(i).getString("FCHECKPERSONID"));	    //11
			vpd.put("var12", varOList.get(i).getString("FCHECKTIME"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**查询-状态列表
	 * @author 管悦
	 * @date 2020-11-10
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/getStateList")
	@ResponseBody
	public Object getStateList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = salesorderService.getStateList(pd);	
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**获取订单编号列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-11
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getOrderNumList")
	@ResponseBody
	public Object getOrderNumList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-供应商名/供应商编号
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = salesorderService.getOrderNumList(pd);	//列出供应商列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 订单检索 列表全部 下拉框使用
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
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-订单编码
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData> varList = salesorderService.listAll(pd);
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**结束
	 * @author 管悦
	 * @date 2020-11-11
	 * @param SalesOrder_ID:销售订单主键、FStatus:状态
	 * @throws Exception
	 */
	@RequestMapping(value="/over")
	@ResponseBody
	public Object over() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		salesorderService.over(pd);
		//插入操作日志
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FMakeBillsPersoID", "c3e8a7d350cc43d9b9e87641947168b8");
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "销售订单");//功能项
		pdOp.put("OperationType", "结束");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("SalesOrder_ID"));//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("result", errInfo);
		return map;
	}
}
