package org.yy.controller.km;

import java.util.ArrayList;
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
import org.activiti.engine.impl.util.CollectionUtil;
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
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.InputOutputService;
import org.yy.service.km.WorkingProcedureExampleService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.WC_StationService;

/** 
 * 说明：工艺工序实例
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/WorkingProcedureExample")
public class WorkingProcedureExampleController extends BaseController {
	
	@Autowired
	private WorkingProcedureExampleService WorkingProcedureExampleService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private WC_StationService wc_stationService;//工位
	@Autowired
	private InputOutputService InputOutputService;
	@Autowired
	private AttachmentSetService attachmentsetService;//附件
	@Autowired
	private BYTEARRAYService BYTEARRAYService;
	@Autowired
	private StaffService staffService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("WorkingProcedureExample:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("WorkingProcedureExample_ID", this.get32UUID());	//主键
		WorkingProcedureExampleService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除工艺工序实例
	 * @author 管悦
	 * @date 2020-11-09
	 * @param WorkingProcedureExample_ID:工艺工序实例ID
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
		WorkingProcedureExampleService.delete(pd);//删除
		pd.put("FStatus", "Y");
		InputOutputService.deleteMxRelated(pd);//关联行关闭投入产出明细
		//插入操作日志
		PageData pdOp=new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "工艺工序实例");//功能项
		pdOp.put("OperationType", "删除");//操作类型
		pdOp.put("Fdescribe", "删除工艺工序实例并行关闭投入产出明细");//描述
		pdOp.put("DeleteTagID", pd.get("WorkingProcedureExample_ID"));//删改数据ID	
		operationrecordService.save(pdOp);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改  WorkingProcedureExample_ID:工艺工序实例ID
	 * @param
	 * @throws Exception
	 */
	/*@RequestMapping(value="/edit")
	//@RequiresPermissions("WorkingProcedureExample:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WorkingProcedureExampleService.edit(pd);
		//插入操作日志
		PageData pdOp=new PageData();
		//pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		//pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));//操作人	
		pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "工艺工序实例");//功能项
		pdOp.put("OperationType", "修改");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.get("WorkingProcedureExample_ID"));//删改数据ID
		operationrecordService.save(pdOp);
		map.put("result", errInfo);
		return map;
	}*/
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit(@RequestParam(value="Attachment",required=false) MultipartFile file,
			@RequestParam(value="BOM_ID",required=false) String BOM_ID,
			@RequestParam(value="SerialNum",required=false) String SerialNum,
			@RequestParam(value="WP",required=false) String WP,
			@RequestParam(value="ConnectionMode",required=false) String ConnectionMode,
			@RequestParam(value="SingleScan",required=false) String SingleScan,
			@RequestParam(value="OneCodeEnd",required=false) String OneCodeEnd,	
			@RequestParam(value="MaterialTraceability",required=false) String MaterialTraceability,	
			@RequestParam(value="UnqualifiedProducts",required=false) String UnqualifiedProducts,	
			@RequestParam(value="PreparationTime",required=false) String PreparationTime,	
			@RequestParam(value="PreparationTimeUnit",required=false) String PreparationTimeUnit,	
			@RequestParam(value="WPOutputRatio",required=false) String WPOutputRatio,	
			@RequestParam(value="FStation",required=false) String FStation,	
			@RequestParam(value="FrozenIF",required=false) String FrozenIF,	
			@RequestParam(value="ProductionDesc",required=false) String ProductionDesc,	
			@RequestParam(value="QIPlanID",required=false) String QIPlanID,	
			@RequestParam(value="WPType",required=false) String WPType,
			@RequestParam(value="FNum",required=false) String FNum,
			@RequestParam(value="FName",required=false) String FName,
			@RequestParam(value="FStatus",required=false) String FStatus,
			@RequestParam(value="ReportTemplateID",required=false) String ReportTemplateID,
			@RequestParam(value="EnableSOP",required=false) String EnableSOP,
			@RequestParam(value="SOP_ID",required=false) String SOP_ID,
			@RequestParam(value="WorkingProcedure_ID",required=false) String WorkingProcedure_ID,
			@RequestParam(value="WorkingProcedureExample_ID",required=false) String WorkingProcedureExample_ID) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("BOM_ID", BOM_ID);	//生产bomID
		pd.put("SerialNum", SerialNum);	//序号
		pd.put("WP", WP);	//工序
		pd.put("ConnectionMode", ConnectionMode);	//接续方式
		pd.put("SingleScan", SingleScan);	//单次扫码
		pd.put("OneCodeEnd", OneCodeEnd);	//一码到底
		pd.put("MaterialTraceability", MaterialTraceability);	//用料追溯关系
		pd.put("UnqualifiedProducts", UnqualifiedProducts);	//不合格品投产
		pd.put("PreparationTime", PreparationTime);	//准备时间
		pd.put("PreparationTimeUnit", PreparationTimeUnit);	//准备时间单位
		pd.put("WPOutputRatio", WPOutputRatio);	//工序产出比例
		pd.put("FStation", FStation);	//工位
		pd.put("ProductionDesc", ProductionDesc);	//生产描述
		pd.put("FrozenIF", FrozenIF);	//产出是否冻结
		pd.put("QIPlanID", QIPlanID);	//质检方案id
		pd.put("WPType", WPType);	//工序类型(投入产出,投入,产出,记录)
		pd.put("FNum", FNum);	//工序编码
		pd.put("FName", FName);	//工序名称
		pd.put("FStatus", FStatus);	//状态
		pd.put("ReportTemplateID", ReportTemplateID);	//报告模板ID
		pd.put("EnableSOP", EnableSOP);	//启用sop
		pd.put("SOP_ID", SOP_ID);	//SOP_ID
		pd.put("WorkingProcedure_ID", WorkingProcedure_ID);	//工艺工序ID
		pd.put("WorkingProcedureExample_ID", WorkingProcedureExample_ID);	//工艺工序实例ID
		WorkingProcedureExampleService.edit(pd);
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {	//上传附件
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("DataSources", "工艺工序实例");//数据来源
			pd.put("AssociationIDTable", "KM_WorkingProcedureExample");//数据来源表名
			pd.put("AssociationID", WorkingProcedureExample_ID);//数据来源表ID
			pd.put("FName", file.getOriginalFilename());//附件名称
			pd.put("FUrl", Const.FILEPATHIMG + ffile + "/" + fileName);//附件路径
			pd.put("FExplanation", "");//Jurisdiction.getName()+"修改");//备注
			pd.put("FNAME", Jurisdiction.getName());//Jurisdiction.getName()); TODO
	        pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));//staffService.getStaffId(pd).getString("STAFF_ID")); TODO
			pd.put("FCreateTime", Tools.date2Str(new Date()));
			attachmentsetService.check(pd);//上传附件
		}
		map.put("result", errInfo);
		operationrecordService.add("","工艺工序实例","修改",pd.getString("BOM_ID"),"","");
		return map;
	}
	
	/**
	 * 修改状态，根据id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editStatus")
	@ResponseBody
	public Object editStatus() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WorkingProcedureExampleService.editStatus(pd);
		map.put("result", errInfo);
		operationrecordService.add("","工艺工序实例","修改状态",pd.getString("BOM_ID"),"","");
		return map;
	}
	
	@RequestMapping(value="/list")
	//@RequiresPermissions("WorkingProcedureExample:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = WorkingProcedureExampleService.list(page);	//列出WorkingProcedureExample列表
		String stationname="";
		PageData spd = new PageData();
		PageData temp = new PageData();
		for(int i=0;i<varList.size();i++) {
			stationname="";
			String FStationStr = varList.get(i).getString("FStation");
			if(Tools.isEmpty(FStationStr)){
				continue;
			}
			String[] sarr=FStationStr.split(",yl,");
			if(sarr.length>1) {
				for(int j=0;j<sarr.length;j++) {
					temp.put("WC_STATION_ID", sarr[j]);
					spd = wc_stationService.findById(temp);
					if(null!=spd && spd.containsKey("FNAME")) {
						if(j>0)stationname+=",";
						stationname+=spd.getString("FNAME");
					}
				}
			} else {
				temp.put("WC_STATION_ID", sarr[0]);
				spd = wc_stationService.findById(temp);
				if(null!=spd && spd.containsKey("FNAME"))
				stationname = spd.getString("FNAME");
			}
			varList.get(i).put("stations", stationname);
		}
		
		//插入操作日志
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "工艺工序实例列表");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", "");//删改数据ID	
		operationrecordService.save(pdOp);	
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**获取工艺工序实例列表
	 * @author 管悦
	 * @date 2020-11-12
	 * @param BOM_ID:生产BOMID
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("WorkingProcedureExample:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = WorkingProcedureExampleService.listAll(pd);	//列出WorkingProcedureExample列表
		//插入操作日志
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "工艺工序实例列表");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", "");//删改数据ID	
		operationrecordService.save(pdOp);	
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**获取工序流程图列表
	 * @author 管悦
	 * @date 2020-11-12
	 * @param BOM_ID:生产BOMID
	 * @throws Exception
	 *//*
	@RequestMapping(value="/listAllFlow")
	@ResponseBody
	public Object listAllFlow() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = WorkingProcedureExampleService.listAllFlow(pd);	//列出WorkingProcedureExample列表
		//插入操作日志
		//pd.put("FNAME", Jurisdiction.getName());
		//pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		pd.put("FMakeBillsPersoID", "c3e8a7d350cc43d9b9e87641947168b8");
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "实例工艺工序流程图");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", "");//删改数据ID	
		operationrecordService.save(pdOp);	
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}*/
	/**获取工序流程图列表
	 * @author 管悦
	 * @date 2020-11-12
	 * @param BOM_ID:生产BOMID
	 * @throws Exception
	 */
	@RequestMapping(value="/listAllFlow")
	@ResponseBody
	public Object listAllFlow() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PID", pd.getString("BOM_ID"));
		PageData pdFlow= BYTEARRAYService.findByPID(pd);	
		//插入操作日志
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		PageData pdOp=new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID());	//主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date()));	//操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));//操作人	
		pdOp.put("FunctionType", "");//功能类型
		pdOp.put("FunctionItem", "实例工艺工序流程图");//功能项
		pdOp.put("OperationType", "查询");//操作类型
		pdOp.put("Fdescribe", "");//描述
		pdOp.put("DeleteTagID", pd.getString("BOM_ID"));//删改数据ID	
		operationrecordService.save(pdOp);	
		map.put("pdFlow", pdFlow);
		map.put("result", errInfo);
		return map;
	}
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("WorkingProcedureExample:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = WorkingProcedureExampleService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("WorkingProcedureExample:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			WorkingProcedureExampleService.deleteAll(ArrayDATA_IDS);
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
		titles.add("BOM编号");	//1
		titles.add("序号");	//2
		titles.add("工序");	//3
		titles.add("接续方式");	//4
		titles.add("单次扫码");	//5
		titles.add("一码到底");	//6
		titles.add("用料追溯关系");	//7
		titles.add("不合格投产");	//8
		titles.add("准备时间");	//9
		titles.add("准备时间单位");	//10
		titles.add("工序产出比例");	//11
		titles.add("工位");	//12
		titles.add("产出是否冻结");	//13
		titles.add("生产描述");	//14
		titles.add("附件");	//15
		titles.add("创建人");	//16
		titles.add("创建日期");	//17
		dataMap.put("titles", titles);
		List<PageData> varOList = WorkingProcedureExampleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BOM_Num"));	    //1
			vpd.put("var2", varOList.get(i).getString("SerialNum"));	    //2
			vpd.put("var3", varOList.get(i).getString("WP"));	    //3
			vpd.put("var4", varOList.get(i).getString("ConnectionMode"));	    //4
			vpd.put("var5", varOList.get(i).getString("SingleScan"));	    //5
			vpd.put("var6", varOList.get(i).getString("OneCodeEnd"));	    //6
			vpd.put("var7", varOList.get(i).getString("MaterialTraceability"));	    //7
			vpd.put("var8", varOList.get(i).getString("UnqualifiedProducts"));	    //8
			vpd.put("var9", varOList.get(i).getString("PreparationTime"));	    //9
			vpd.put("var10", varOList.get(i).getString("PreparationTimeUnit"));	    //10
			vpd.put("var11", varOList.get(i).getString("WPOutputRatio"));	    //11
			vpd.put("var12", varOList.get(i).getString("FStation"));	    //12
			vpd.put("var13", varOList.get(i).getString("FrozenIF"));	    //13
			vpd.put("var14", varOList.get(i).getString("ProductionDesc"));	    //14
			vpd.put("var15", varOList.get(i).getString("FAttachment"));	    //15
			vpd.put("var16", varOList.get(i).getString("FCreatePersonID"));	    //16
			vpd.put("var17", varOList.get(i).getString("FCreateTime"));	    //17
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
