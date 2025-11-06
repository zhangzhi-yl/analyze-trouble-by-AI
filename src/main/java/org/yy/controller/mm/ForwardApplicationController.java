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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.ForwardApplicationDetailService;
import org.yy.service.mm.ForwardApplicationService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.SALESORDERDETAILService;

/** 
 * 说明：发运申请
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ForwardApplication")
public class ForwardApplicationController extends BaseController {
	
	@Autowired
	private ForwardApplicationService ForwardApplicationService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private ForwardApplicationDetailService ForwardApplicationDetailService;
	@Autowired
	private SALESORDERDETAILService salesorderdetailService;
	@Autowired
	private StaffService staffService;
	/**发运申请新增
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = ForwardApplicationService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {	
		pd.put("ForwardApplication_ID", this.get32UUID());	//主键
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FCreatePersonID", "c3e8a7d350cc43d9b9e87641947168b8");
		pd.put("FCreateTime", Tools.date2Str(new Date()));
		pd.put("FStatus", "已创建");
		pd.put("AuditMark", "N");
		ForwardApplicationService.save(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FCreatePersonID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请");//功能项
		pdOp.put("OperationType", "新增");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("ForwardApplication_ID"));
		operationrecordService.save(pdOp);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**发运申请删除
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();	
		ForwardApplicationService.delete(pd);
		//关联行关闭发运申请明细
		pd.put("LineClose", "Y");
		ForwardApplicationDetailService.rowCloseByForwardId(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请");//功能项
		pdOp.put("OperationType", "删除");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("ShippingApplicationID"));
		operationrecordService.save(pdOp);
		salesorderdetailService.calFPushCountForwardAll(pd);//一键反写源单下推数量
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**发运申请修改
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = ForwardApplicationService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {	
		ForwardApplicationService.edit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请");//功能项
		pdOp.put("OperationType", "修改");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("ForwardApplication_ID"));
		operationrecordService.save(pdOp);
		}	
		map.put("result", errInfo);
		return map;
	}
	
	/**发运申请列表
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_ShippingOrderNum = pd.getString("KEYWORDS_ShippingOrderNum");						//发运申请单号
		if(Tools.notEmpty(KEYWORDS_ShippingOrderNum))pd.put("KEYWORDS_ShippingOrderNum", KEYWORDS_ShippingOrderNum.trim());
		page.setPd(pd);
		List<PageData>	varList = ForwardApplicationService.list(page);	//列出ForwardApplication列表
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	 /**去新增页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	@ResponseBody
	public Object goAdd() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", Jurisdiction.getName());
		//pd.put("FNAME", "管悦");
		pd.put("FOperatorID", staffService.getStaffId(pd).getString("STAFF_ID"));
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
		
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ForwardApplicationService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ForwardApplicationService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**导出到excel
	 * @author 管悦
	 * @date 2020-11-20
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_ShippingOrderNum = pd.getString("KEYWORDS_ShippingOrderNum");						//发运申请单号
		if(Tools.notEmpty(KEYWORDS_ShippingOrderNum))pd.put("KEYWORDS_ShippingOrderNum", KEYWORDS_ShippingOrderNum.trim());
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("发运单号");	//1
		titles.add("发出仓库");	//2
		titles.add("发运类型");	//3
		titles.add("需求时间");	//4
		titles.add("备注");	//5
		titles.add("状态");	//6
		titles.add("发运人");	//7
		titles.add("发布时间");	//8
		titles.add("地址");	//9
		titles.add("客户");	//10
		titles.add("审核标志");	//11
		titles.add("审核人");	//12
		titles.add("审核时间");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = ForwardApplicationService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ShippingOrderNum"));	    //1
			vpd.put("var2", varOList.get(i).getString("FWAREHOUSEJoint"));	    //2
			vpd.put("var3", varOList.get(i).getString("ShippingType"));	    //3
			vpd.put("var4", varOList.get(i).getString("DemandTime"));	    //4
			vpd.put("var5", varOList.get(i).getString("FExplanation"));	    //5
			vpd.put("var6", varOList.get(i).getString("FStatus"));	    //6
			vpd.put("var7", varOList.get(i).getString("Shipper"));	    //7
			vpd.put("var8", varOList.get(i).getString("ReleaseTime"));	    //8
			vpd.put("var9", varOList.get(i).getString("FAddress"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCustomer"));	    //10
			vpd.put("var11", varOList.get(i).getString("AuditMark"));	    //11
			vpd.put("var12", varOList.get(i).getString("FReviewer"));	    //12
			vpd.put("var13", varOList.get(i).getString("AuditTime"));	    //13
			varList.add(vpd);
		}
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");		
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请");//功能项
		pdOp.put("OperationType", "导出");//操作类型
		pdOp.put("Fdescribe", "");//描述
		operationrecordService.save(pdOp);
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**审核或反审核发运申请
	 * @author 管悦
	 * @date 2020-11-20
	 * @param ForwardApplication_ID:发运申请ID、AuditMark:审核标志（Y、N）
	 * @throws Exception
	 */
	@RequestMapping(value="/editAudit")
	@ResponseBody
	public Object editAudit() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FReviewer", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		//pd.put("FReviewer", "c3e8a7d350cc43d9b9e87641947168b8");
		pd.put("AuditTime", Tools.date2Str(new Date()));
		ForwardApplicationService.editAudit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		String AuditMark= pd.getString("AuditMark");
		if(AuditMark.equals("Y")) {
			pdOp.put("OperationType", "审核");//操作类型
		}else {
			pdOp.put("OperationType", "反审核");//操作类型
		}
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请");//功能项
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("ForwardApplication_ID"));//删改数据ID
		operationrecordService.save(pdOp);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**下发或取消
	 * @author 管悦
	 * @date 2020-11-20
	 * @param ForwardApplication_ID:发运申请ID、FStatus:已创建/已下发
	 * @throws Exception
	 */
	@RequestMapping(value="/editFStatus")
	@ResponseBody
	public Object editFStatus() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ReleaseTime", Tools.date2Str(new Date()));
		ForwardApplicationService.editFStatus(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		String FStatus= pd.getString("FStatus");
		if(FStatus.equals("已下发")) {
			pdOp.put("OperationType", "发布");//操作类型
		}else {
			pdOp.put("OperationType", "取消发布");//操作类型
		}
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请");//功能项
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("ForwardApplication_ID"));//删改数据ID
		operationrecordService.save(pdOp);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	
	@RequestMapping(value="/fysq/zhuisu")
	@ResponseBody
	public Object GET_FYSQ_ZHUISU_listPage(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = ForwardApplicationService.GET_FYSQ_ZHUISU_listPage(page);	//列出ForwardApplication列表
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		//pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "发运申请");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
}
