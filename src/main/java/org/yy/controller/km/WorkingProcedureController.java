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
import org.yy.util.UuidUtil;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.ProcessDefectiveItemsService;
import org.yy.service.km.ProcessDefinitionService;
import org.yy.service.km.WorkingProcedureDefectiveItemsService;
import org.yy.service.km.WorkingProcedureService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.WC_StationService;

/** 
 * 说明：工艺路线工序
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/WorkingProcedure")
public class WorkingProcedureController extends BaseController {
	
	@Autowired
	private WorkingProcedureService WorkingProcedureService;
	
	@Autowired
	private WorkingProcedureDefectiveItemsService WorkingProcedureDefectiveItemsService;//工艺工序次品项service
	
	@Autowired
	private ProcessDefinitionService ProcessDefinitionService;//工序定义service
	
	@Autowired
	private ProcessDefectiveItemsService ProcessDefectiveItemsService;//工序定义次品项service
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private WC_StationService wc_stationService;//工位
	
	@Autowired
	private AttachmentSetService attachmentsetService;//附件
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(@RequestParam(value="Attachment",required=false) MultipartFile file) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData filepd = new PageData();
		pd = this.getPageData();
		String WorkingProcedure_ID=this.get32UUID();
		pd.put("WorkingProcedure_ID", WorkingProcedure_ID);	//主键
		pd.put("FNAME", "");//Jurisdiction.getName());				
		//String userid=staffService.getStaffId(pd).getString("STAFF_ID");
		pd.put("FCreatePersonID", "");						//创建人
		Date date = new Date();
		pd.put("FCreateTime", Tools.date2Str(date));			//创建时间
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {	//上传附件
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			filepd.put("DataSources", "工艺路线工序");//数据来源
			filepd.put("AssociationIDTable", "KM_WorkingProcedure");//数据来源表名
			filepd.put("AssociationID", WorkingProcedure_ID);//数据来源表ID
			filepd.put("FName", file.getOriginalFilename());//附件名称
			filepd.put("FUrl", Const.FILEPATHFILE + ffile + "/" + fileName);//附件路径
			filepd.put("FExplanation", "");//Jurisdiction.getName()+"添加");//备注
			filepd.put("FNAME", "");//Jurisdiction.getName());
			filepd.put("FCreatePersonID", "");//staffService.getStaffId(pd).getString("STAFF_ID"));
			filepd.put("FCreateTime", Tools.date2Str(new Date()));
			attachmentsetService.check(filepd);//上传附件
			pd.put("FAttachmentName", file.getOriginalFilename());//附件名称
			pd.put("FAttachmentDirect", Const.FILEPATHFILE + ffile + "/" + fileName);//附件路径
		}
		pd.put("SerialNum", Integer.parseInt(WorkingProcedureService.findMaxSerialNumByProcessRouteID(pd)
				.get("maxSerialNum").toString())+1);
		WorkingProcedureService.save(pd);
		operationrecordService.add("","工艺路线工序","添加",pd.getString("WorkingProcedure_ID"),"","");
		map.put("result", errInfo);
		return map;
	}
	
	/**删除，归档附件
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("WorkingProcedure:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WorkingProcedureService.delete(pd);
		map.put("result", errInfo);				//返回结果
		operationrecordService.add("","工艺路线工序","删除",pd.getString("WorkingProcedure_ID"),"","");
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("WorkingProcedure:edit")
	@ResponseBody
	public Object edit(@RequestParam(value="Attachment",required=false) MultipartFile file,
			@RequestParam(value="ProcessRouteID",required=false) String ProcessRouteID,
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
			@RequestParam(value="FTestMethod",required=false) String FTestMethod,
			@RequestParam(value="WorkingProcedure_ID",required=false) String WorkingProcedure_ID) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData filepd = new PageData();
		pd = this.getPageData();
		pd.put("ProcessRouteID", ProcessRouteID);	//工艺路线id
		pd.put("SerialNum", SerialNum);	//序号
		pd.put("WP", WP);	//工序
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
		pd.put("FTestMethod", FTestMethod);//检验任务生成方法
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {	//上传附件
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			filepd.put("DataSources", "工艺路线工序");//数据来源
			filepd.put("AssociationIDTable", "KM_WorkingProcedure");//数据来源表名
			filepd.put("AssociationID", WorkingProcedure_ID);//数据来源表ID
			filepd.put("FName", file.getOriginalFilename());//附件名称
			filepd.put("FUrl", Const.FILEPATHFILE + ffile + "/" + fileName);//附件路径
			filepd.put("FExplanation", "");//Jurisdiction.getName()+"修改");//备注
			filepd.put("FNAME", "");//Jurisdiction.getName());
			filepd.put("FCreatePersonID", "");//staffService.getStaffId(pd).getString("STAFF_ID"));
			filepd.put("FCreateTime", Tools.date2Str(new Date()));
			attachmentsetService.check(filepd);//上传附件
			pd.put("FAttachmentName", file.getOriginalFilename());//附件名称
			pd.put("FAttachmentDirect", Const.FILEPATHFILE + ffile + "/" + fileName);//附件路径
		}
		WorkingProcedureService.edit(pd);
		map.put("result", errInfo);
		operationrecordService.add("","工艺路线工序","修改",pd.getString("WorkingProcedure_ID"),"","");
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("WorkingProcedure:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData spd = new PageData();
		PageData temp = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = WorkingProcedureService.list(page);	//列出WorkingProcedure列表
		String stationname="";
		for(int i=0;i<varList.size();i++) {
			if(varList.get(i).containsKey("FStation") && null!= varList.get(i).getString("FStation")) {
				stationname="";
				String[] sarr=varList.get(i).getString("FStation").split(",yl,");
				if(sarr.length>1) {
					for(int j=0;j<sarr.length;j++) {
						temp.put("WC_STATION_ID", sarr[j]);
						spd = wc_stationService.findById(temp);
						if(null!=spd && spd.containsKey("FNAME")) {
							if(j>0)stationname+=",yl,";
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
			} else {
				varList.get(i).put("stations", "");
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**全部列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("WorkingProcedure:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData spd = new PageData();
		PageData temp = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = WorkingProcedureService.listAll(pd);	//列出WorkingProcedure列表
		String stationname="";
		for(int i=0;i<varList.size();i++) {
			if(varList.get(i).containsKey("FStation") && null!= varList.get(i).getString("FStation")) {
				stationname="";
				String[] sarr=varList.get(i).getString("FStation").split(",yl,");
				if(sarr.length>1) {
					for(int j=0;j<sarr.length;j++) {
						temp.put("WC_STATION_ID", sarr[j]);
						spd = wc_stationService.findById(temp);
						if(null!=spd && spd.containsKey("FNAME")) {
							if(j>0)stationname+=",yl,";
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
			} else {
				varList.get(i).put("stations", "");
			}
		}
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("WorkingProcedure:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData spd = new PageData();
		pd = this.getPageData();
		pd = WorkingProcedureService.findById(pd);	//根据ID读取
		String stationname="";
		if(null!=pd && pd.containsKey("FStation") && null!= pd.getString("FStation")) {
			String[] sarr=pd.getString("FStation").split(",yl,");
			for(int j=0;j<sarr.length;j++) {
				PageData temp = new PageData();
				temp.put("WC_STATION_ID", sarr[j]);
				spd = wc_stationService.findById(temp);	//根据ID读取
				if(null!=spd && spd.containsKey("FNAME")) {
					if(j>0)stationname+=",yl,";
					stationname+=spd.getString("FNAME");
				}
			}
			pd.put("stations", stationname);
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/** 通过工艺路线ID和节点id获取top 1 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/findByProcessRouteIDAndNodeId")
	//@RequiresPermissions("WorkingProcedure:edit")
	@ResponseBody
	public Object findByProcessRouteIDAndNodeId() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData spd = new PageData();
		pd = this.getPageData();
		pd = WorkingProcedureService.findByProcessRouteIDAndNodeId(pd);	//工艺路线ID和节点id获取top 1
		String stationname="";
		if(null!=pd && pd.containsKey("FStation") && null!= pd.getString("FStation")) {
			String[] sarr=pd.getString("FStation").split(",yl,");
			for(int j=0;j<sarr.length;j++) {
				PageData temp = new PageData();
				temp.put("WC_STATION_ID", sarr[j]);
				spd = wc_stationService.findById(temp);	//根据ID读取
				if(null!=spd && spd.containsKey("FNAME")) {
					if(j>0)stationname+=",yl,";
					stationname+=spd.getString("FNAME");
				}
			}
			pd.put("stations", stationname);
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("WorkingProcedure:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			WorkingProcedureService.deleteAll(ArrayDATA_IDS);
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
		titles.add("ID");	//1
		titles.add("工艺路线ID");	//2
		titles.add("序号");	//3
		titles.add("工序");	//4
		titles.add("接续方式");	//5
		titles.add("单次扫码");	//6
		titles.add("一码到底");	//7
		titles.add("用料追溯关系");	//8
		titles.add("不合格投产");	//9
		titles.add("准备时间");	//10
		titles.add("准备时间单位");	//11
		titles.add("工序产出比例");	//12
		titles.add("工位");	//13
		titles.add("产出是否冻结");	//14
		titles.add("生产描述");	//15
		titles.add("附件");	//16
		titles.add("创建人");	//17
		titles.add("创建日期");	//18
		dataMap.put("titles", titles);
		List<PageData> varOList = WorkingProcedureService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("WorkingProcedure_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("ProcessRouteID"));	    //2
			vpd.put("var3", varOList.get(i).getString("SerialNum"));	    //3
			vpd.put("var4", varOList.get(i).getString("WP"));	    //4
			vpd.put("var5", varOList.get(i).getString("ConnectionMode"));	    //5
			vpd.put("var6", varOList.get(i).getString("SingleScan"));	    //6
			vpd.put("var7", varOList.get(i).getString("OneCodeEnd"));	    //7
			vpd.put("var8", varOList.get(i).getString("MaterialTraceability"));	    //8
			vpd.put("var9", varOList.get(i).getString("UnqualifiedProducts"));	    //9
			vpd.put("var10", varOList.get(i).getString("PreparationTime"));	    //10
			vpd.put("var11", varOList.get(i).getString("PreparationTimeUnit"));	    //11
			vpd.put("var12", varOList.get(i).getString("WPOutputRatio"));	    //12
			vpd.put("var13", varOList.get(i).getString("FStation"));	    //13
			vpd.put("var14", varOList.get(i).getString("FrozenIF"));	    //14
			vpd.put("var15", varOList.get(i).getString("ProductionDesc"));	    //15
			vpd.put("var16", varOList.get(i).getString("FAttachment"));	    //16
			vpd.put("var17", varOList.get(i).getString("FCreatePersonID"));	    //17
			vpd.put("var18", varOList.get(i).getString("FCreateTime"));	    //18
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
