package org.yy.controller.km;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.CodingRulesDetailService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.ProcessDefectiveItemsService;
import org.yy.service.km.ProcessDefinitionFieldService;
import org.yy.service.km.ProcessDefinitionService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.mom.WC_StationService;

/** 
 * 说明：工序定义
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-10
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ProcessDefinition")
public class ProcessDefinitionController extends BaseController {
	
	@Autowired
	private ProcessDefinitionService ProcessDefinitionService;//工序定义serverce
	
	@Autowired
	private WC_StationService wc_stationService;//工位
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private AttachmentSetService attachmentsetService;//附件
	
	@Autowired
	private CodingRulesService codingrulesService;// 编码规则接口
	
	@Autowired
	private CodingRulesDetailService codingRulesDetailService;//编码规则详细接口
	
	@Autowired
	private ProcessDefectiveItemsService ProcessDefectiveItemsService;
	
	@Autowired
	private ProcessDefinitionFieldService ProcessDefinitionFieldService;
	
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("ProcessDefinition:add")
	@ResponseBody
	public Object add(@RequestParam(value="Attachment",required=false) MultipartFile file,
			@RequestParam(value="FNum",required=false) String FNum,
			@RequestParam(value="FName",required=false) String FName,
			@RequestParam(value="FStatus",required=false) String FStatus,
			@RequestParam(value="FStationID",required=false) String FStationID,
			@RequestParam(value="WordScannedIF",required=false) String WordScannedIF,
			@RequestParam(value="OneYardToTheEndIF",required=false) String OneYardToTheEndIF,	
			@RequestParam(value="MaterialTraceability",required=false) String MaterialTraceability,	
			@RequestParam(value="UnqualifiedProducts",required=false) String UnqualifiedProducts,	
			@RequestParam(value="FrozenIF",required=false) String FrozenIF,	
			@RequestParam(value="ReportTemplateID",required=false) String ReportTemplateID,	
			@RequestParam(value="ProductionDesc",required=false) String ProductionDesc,	
			@RequestParam(value="QIPlanID",required=false) String QIPlanID,	
			@RequestParam(value="WPType",required=false) String WPType) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String time = Tools.date2Str(new Date());
		PageData num = new PageData();
		num = ProcessDefinitionService.findCount(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			PageData staff = new PageData();
			staff.put("FNAME", Jurisdiction.getName());
			staff=staffService.getStaffId(staff);
			String staffid=null==staff?"":staff.getString("STAFF_ID");
			pd.put("ProcessDefinition_ID", this.get32UUID());	//主键
			//FNum = codingRulesService.getRuleNumByRuleType("ProcessDefinitionNO").toString();//根据规则类型获取规则号
			pd.put("FNum", FNum);	//编码
			pd.put("FName", FName);	//名称
			pd.put("FStatus", FStatus);	//状态
			pd.put("FStationID", FStationID);	//工位id，多个‘,yl,’分隔
			pd.put("WordScannedIF", WordScannedIF);	//单次扫码
			pd.put("OneYardToTheEndIF", OneYardToTheEndIF);	//一码到底
			pd.put("MaterialTraceability", MaterialTraceability);	//用料追溯关系
			pd.put("UnqualifiedProducts", UnqualifiedProducts);	//不合格品投产
			pd.put("FrozenIF", FrozenIF);	//产出是否冻结
			pd.put("ReportTemplateID", ReportTemplateID);	//报告模板，text手写
			pd.put("ProductionDesc", ProductionDesc);	//生产描述
			pd.put("QIPlanID", QIPlanID);	//质检方案id
			pd.put("WPType", WPType);	//工序类型(投入产出,投入,产出,记录)
			pd.put("FCreatePersonID", staffid);
			pd.put("FCreateTime", time);
			pd.put("LastModifiedBy", staffid);
			pd.put("LastModificationTime", time);
			ProcessDefinitionService.save(pd);
			pd = ProcessDefinitionService.findById(pd);	//根据ID读取
			String  ffile = DateUtil.getDays(), fileName = "";
			if (null != file && !file.isEmpty()) {	//上传附件
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
				pd.put("DataSources", "工序定义");//数据来源
				pd.put("AssociationIDTable", "KM_ProcessDefinition");//数据来源表名
				pd.put("AssociationID", pd.getString("ProcessDefinition_ID"));//数据来源表ID
				pd.put("FName", file.getOriginalFilename());//附件名称
				pd.put("FUrl", Const.FILEPATHFILE + ffile + "/" + fileName);//附件路径
				pd.put("FExplanation", "");//Jurisdiction.getName()+"添加");//备注
		        pd.put("FCreatePersonID", staffid);//员工id
				pd.put("FCreateTime", Tools.date2Str(new Date()));
				attachmentsetService.check(pd);//上传附件
			}
			operationrecordService.add("","工序定义","添加",pd.getString("ProcessDefinition_ID"),staffid,"添加工序定义");
		}
		map.put("ProcessDefinition_ID", pd.get("ProcessDefinition_ID"));
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("ProcessDefinition:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		try{
			ProcessDefinitionService.delete(pd);
			pd.put("AssociationID", pd.getString("ProcessDefinition_ID"));
			attachmentsetService.delete(pd);//删除附件
			pd.put("WP_ID", pd.getString("ProcessDefinition_ID"));
			ProcessDefectiveItemsService.deleteByProcessDefinition_ID(pd);//删除关联次品项
			pd.put("WPDefinitionID", pd.getString("ProcessDefinition_ID"));
			ProcessDefinitionFieldService.deleteByProcessDefinition_ID(pd);//删除关联自定义字段
		} catch(Exception e){
			errInfo = "error";
		}
		operationrecordService.add("","工序定义","删除",pd.getString("ProcessDefinition_ID"),staffid,"删除工序定义");
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("ProcessDefinition:edit")
	@ResponseBody
	public Object edit(@RequestParam(value="Attachment",required=false) MultipartFile file,
			@RequestParam(value="FNum",required=false) String FNum,
			@RequestParam(value="FName",required=false) String FName,
			@RequestParam(value="FStatus",required=false) String FStatus,
			@RequestParam(value="FStationID",required=false) String FStationID,
			@RequestParam(value="WordScannedIF",required=false) String WordScannedIF,
			@RequestParam(value="OneYardToTheEndIF",required=false) String OneYardToTheEndIF,	
			@RequestParam(value="MaterialTraceability",required=false) String MaterialTraceability,	
			@RequestParam(value="UnqualifiedProducts",required=false) String UnqualifiedProducts,	
			@RequestParam(value="FrozenIF",required=false) String FrozenIF,	
			@RequestParam(value="ReportTemplateID",required=false) String ReportTemplateID,	
			@RequestParam(value="ProductionDesc",required=false) String ProductionDesc,	
			@RequestParam(value="QIPlanID",required=false) String QIPlanID,	
			@RequestParam(value="WPType",required=false) String WPType,
			@RequestParam(value="ProcessDefinition_ID",required=false) String ProcessDefinition_ID,
			@RequestParam(value="FCreatePersonID",required=false) String FCreatePersonID,
			@RequestParam(value="FCreateTime",required=false) String FCreateTime) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = ProcessDefinitionService.findCount(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			PageData staff = new PageData();
			staff.put("FNAME", Jurisdiction.getName());
			String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
			pd.put("ProcessDefinition_ID", ProcessDefinition_ID);	//主键
			pd.put("FNum", FNum);	//编码
			pd.put("FName", FName);	//名称
			pd.put("FStatus", FStatus);	//状态
			pd.put("FStationID", FStationID);	//工位id，多个‘,yl,’分隔
			pd.put("WordScannedIF", WordScannedIF);	//单次扫码
			pd.put("OneYardToTheEndIF", OneYardToTheEndIF);	//一码到底
			pd.put("MaterialTraceability", MaterialTraceability);	//用料追溯关系
			pd.put("UnqualifiedProducts", UnqualifiedProducts);	//不合格品投产
			pd.put("FrozenIF", FrozenIF);	//产出是否冻结
			pd.put("ReportTemplateID", ReportTemplateID);	//报告模板，text手写
			pd.put("ProductionDesc", ProductionDesc);	//生产描述
			pd.put("QIPlanID", QIPlanID);	//质检方案id
			pd.put("WPType", WPType);	//工序类型(投入产出,投入,产出,记录)
			pd.put("FCreatePersonID", FCreatePersonID);
			pd.put("FCreateTime", FCreateTime);
			pd.put("LastModifiedBy", staffid);
			pd.put("LastModificationTime", Tools.date2Str(new Date()));
			ProcessDefinitionService.edit(pd);
			String  ffile = DateUtil.getDays(), fileName = "";
			if (null != file && !file.isEmpty()) {	//上传附件
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
				pd.put("DataSources", "工序定义");//数据来源
				pd.put("AssociationIDTable", "KM_ProcessDefinition");//数据来源表名
				pd.put("AssociationID", ProcessDefinition_ID);//数据来源表ID
				pd.put("FName", file.getOriginalFilename());//附件名称
				pd.put("FUrl", Const.FILEPATHFILE + ffile + "/" + fileName);//附件路径
				pd.put("FExplanation", "");//Jurisdiction.getName()+"修改");//备注
		        pd.put("FCreatePersonID", staffid);//员工id;
				pd.put("FCreateTime", Tools.date2Str(new Date()));
				attachmentsetService.check(pd);//上传附件
			}
			operationrecordService.add("","工序定义","修改",pd.getString("ProcessDefinition_ID"),staffid,"修改工序定义");
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改状态
	 * @param ProcessDefinition_ID 工序id
	 * @param FStatus 状态
	 * @throws Exception
	 */
	@RequestMapping(value="/editStatus")
	//@RequiresPermissions("ProcessDefinition:edit")
	@ResponseBody
	public Object editStatus() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		pd = this.getPageData();
		pd.put("LastModifiedBy", staffid);
		pd.put("LastModificationTime", Tools.date2Str(new Date()));
		ProcessDefinitionService.editStatus(pd);
		operationrecordService.add("","工序定义","修改状态",pd.getString("ProcessDefinition_ID"),staffid,"修改工序定义状态");
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**全部列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("ProcessDefinition:list")
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
		List<PageData> varList = ProcessDefinitionService.listAll(pd);	//列出ProcessDefinition列表
		String stationname="";
		for(int i=0;i<varList.size();i++) {
			if(varList.get(i).containsKey("FStationID") && null!= varList.get(i).getString("FStationID")) {
				stationname="";
				String[] sarr=varList.get(i).getString("FStationID").split(",yl,");
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
			}else {
				varList.get(i).put("stations", "");
			}
		}
		map.put("varList", varList);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("ProcessDefinition:list")
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
		List<PageData> varList = ProcessDefinitionService.list(page);	//列出ProcessDefinition列表
		String stationname="";
		for(int i=0;i<varList.size();i++) {
			if(varList.get(i).containsKey("FStationID") && null!= varList.get(i).getString("FStationID")) {
				stationname="";
				String[] sarr=varList.get(i).getString("FStationID").split(",yl,");
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
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("ProcessDefinition:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData spd = new PageData();
		pd = this.getPageData();
		pd = ProcessDefinitionService.findById(pd);	//根据ID读取
		String stationname="";
		if(null!=pd && pd.containsKey("FStationID") && null!= pd.getString("FStationID")) {
			String[] sarr=pd.getString("FStationID").split(",yl,");
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
		map.put("result", errInfo);						//返回结果
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
		titles.add("编号");	//2
		titles.add("名称");	//3
		titles.add("状态");	//4
		titles.add("工位ID");	//5
		titles.add("是否单词扫码");	//6
		titles.add("是否一码到底");	//7
		titles.add("用料追溯关系ID");	//8
		titles.add("不合格品投产");	//9
		titles.add("产出是否冻结");	//10
		titles.add("报告模板");	//11
		titles.add("生产描述");	//12
		titles.add("质检方案ID");	//13
		titles.add("附件名称");	//14
		titles.add("附件路径");	//15
		titles.add("附件IF");	//16
		titles.add("启用SOP");	//17
		titles.add("SOPID");	//18
		titles.add("工序类型(投入产出,投入,产出,记录)");	//19
		dataMap.put("titles", titles);
		List<PageData> varOList = ProcessDefinitionService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var2", varOList.get(i).getString("FNum"));	    //2
			vpd.put("var3", varOList.get(i).getString("FName"));	    //3
			vpd.put("var4", varOList.get(i).getString("FStatus"));	    //4
			vpd.put("var5", varOList.get(i).getString("FStationID"));	    //5
			vpd.put("var6", varOList.get(i).getString("WordScannedIF"));	    //6
			vpd.put("var7", varOList.get(i).getString("OneYardToTheEndIF"));	    //7
			vpd.put("var8", varOList.get(i).getString("MaterialTraceability"));	    //8
			vpd.put("var9", varOList.get(i).getString("UnqualifiedProducts"));	    //9
			vpd.put("var10", varOList.get(i).getString("FrozenIF"));	    //10
			vpd.put("var11", varOList.get(i).getString("ReportTemplateID"));	    //11
			vpd.put("var12", varOList.get(i).getString("ProductionDesc"));	    //12
			vpd.put("var13", varOList.get(i).getString("QIPlanID"));	    //13
			vpd.put("var14", varOList.get(i).getString("FAttachmentName"));	    //14
			vpd.put("var15", varOList.get(i).getString("FAttachmentDirect"));	    //15
			vpd.put("var16", varOList.get(i).getString("FAttachmentIF"));	    //16
			vpd.put("var17", varOList.get(i).getString("EnableSOP"));	    //17
			vpd.put("var18", varOList.get(i).getString("SOP_ID"));	    //18
			vpd.put("var19", varOList.get(i).getString("WPType"));	    //19
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**从EXCEL导入到数据库
	 * 1.导入时自动生成工序编码，将自定义编码调用功能复制到导入中
	 * 2.导入时判断表格中的车间与工位是否为空，为空不执行保存操作，记录有多少条导入执行后返回前台提示
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	//@RequiresPermissions("fromExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		PageData stationpd = new PageData();
		int succeedNum=0;
		int failNum=0;
		if (null != file && !file.isEmpty()) {
	        int realRowCount = 0;//真正有数据的行数
	        //得到工作空间
	        Workbook workbook = null;
	        try {
	            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        //得到sheet
	        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, 0);
	        realRowCount = sheet.getPhysicalNumberOfRows();
	        for(int rowNum = 1;rowNum <= realRowCount;rowNum++) {
	        	Row row = sheet.getRow(rowNum);
	            if(ObjectExcelRead.isBlankRow(row)) {	//空行跳过
	            	break;
	            }
	            if(row.getRowNum() == -1) {
	                continue;
	            }else {
	                if(row.getRowNum() == 0) {//第一行表头跳过
	                    continue;
	                }
	            }
	            try {
	            	if(null!=ObjectExcelRead.getCellValue(sheet, row, 2)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 2))&&null!=ObjectExcelRead.getCellValue(sheet, row, 3)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 3))) {//判断车间与工位字符是否为空
	            		stationpd.put("PLANTNAME", ObjectExcelRead.getCellValue(sheet, row, 2));
	            		stationpd.put("STATIONNAME", ObjectExcelRead.getCellValue(sheet, row, 3));
	            		stationpd=wc_stationService.getStationId(stationpd);
	            		if(null!=stationpd) {
	            			String reg="^\\d+$";
	            			// 根据规则类型 查询规则详情
	        	    		PageData pgData = new PageData();
	        	    		pgData.put("CODINGRULESTYPE", "ProcessDefinitionNO");//编码类型
	        	    		List<PageData> dataByCodingRulesType = codingrulesService.getDataByCodingRulesType(pgData);
	        	    		if (CollectionUtils.isEmpty(dataByCodingRulesType)) {
	        	    			throw new RuntimeException("获取规则号失败，该类型数据不存在");
	        	    		}
	        	    		String returnCode = "";
	        	    		String CODINGRULEID = "";
	        	    		String ACQUISITIONTIME = "";
	        	    		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
	        	    		for (PageData pageData : dataByCodingRulesType) {
	        	    			// 根据详情类型 生成对应的编号
	        	    			CODINGRULEID = pageData.getString("CODINGRULES_ID");
	        	    			String CODINGRULESDETAILID = pageData.getString("CODINGRULESDETAIL_ID");
	        	    			String TERMOFVALIDITY = pageData.getString("TERMOFVALIDITY");
	        	    			String DETAILTYPE = pageData.getString("DETAILTYPE");
	        	    			String FLENGTH = pageData.getString("FLENGTH");
	        	    			String FFORMAT = pageData.getString("FFORMAT");
	        	    			String TSTEP = pageData.getString("TSTEP");
	        	    			String SETTINGVALUE = pageData.getString("SETTINGVALUE");
	        	    			String RESETPERIOD = pageData.getString("RESETPERIOD");
	        	    			// 进制问题需要确定
	        	    			/* String STREAMCODING = pageData.getString("STREAMCODING"); */
	        	    			ACQUISITIONTIME = pageData.getString("ACQUISITIONTIME");
	        	    			// 判断是否过期
	        	    			String termDateTime = TERMOFVALIDITY.substring(12, TERMOFVALIDITY.length());
	        	    			Date parse = null;
	        	    			parse = sdFormat.parse(termDateTime);
	        	    			long time = parse.getTime();
	        	    			long currentTimeMillis = new Date().getTime();
	        	    			if (time < currentTimeMillis) {
	        	    				throw new RuntimeException("该编码规则已过期");
	        	    			}
	        	    			// 常量类型直接返回当初的设置值
	        	    			if ("constant".equals(DETAILTYPE)) {
	        	    				returnCode += SETTINGVALUE;
	        	    			}
	        	    			// 日期类型直接返回当初的设置格式后 格式化时间返回
	        	    			if ("date".equals(DETAILTYPE)) {
	        	    				String code = "";
	        	    				String acqNow = sdFormat.format(new Date());
	        	    				SimpleDateFormat sdf = new SimpleDateFormat(FFORMAT);
	        	    				try {
	        	    					code = sdf.format(sdFormat.parse(ACQUISITIONTIME));
	        	    				} catch (Exception e) {
	        	    					// TODO Auto-generated catch block
	        	    					e.printStackTrace();
	        	    				}
	        	    				if ("everyday".equals(RESETPERIOD)) {
	        	    					if (!acqNow.equals(ACQUISITIONTIME)) {
	        	    						code = sdf.format(new Date());
	        	    					}
	        	    				}
	        	    				returnCode += code;
	        	    			}
	        	    			// 根据规则类型生成流水号
	        	    			if ("serial".equals(DETAILTYPE)) {
	        	    				String acqNow = sdFormat.format(new Date());
	        	    				// 如果每天重置，日期变更 重新设置位1开始
	        	    				if ("everyday".equals(RESETPERIOD)) {
	        	    					if (!acqNow.equals(ACQUISITIONTIME)) {
	        	    						SETTINGVALUE = "0";
	        	    					}
	        	    				}
	        	    				// 如果不重置，则接着前一天的时间继续编号
	        	    				else {
	        	    					acqNow = ACQUISITIONTIME;
	        	    				}
	        	    				// 自增 步长
	        	    				Integer getValueInc = Integer.valueOf(SETTINGVALUE) + Integer.valueOf(TSTEP);
	        	    				String formatNum = this.formatNum(getValueInc, Integer.valueOf(FLENGTH));

	        	    				// 回写数据
	        	    				PageData pgRuleDetail = new PageData();
	        	    				pgRuleDetail.put("CODINGRULESDETAIL_ID", CODINGRULESDETAILID);
	        	    				PageData findDetailById = codingRulesDetailService.findById(pgRuleDetail);
	        	    				findDetailById.put("SETTINGVALUE", formatNum);
	        	    				codingRulesDetailService.edit(findDetailById);
	        	    				// 返回結果
	        	    				returnCode += formatNum;
	        	    			}
	        	    		}
	        	    		// 更新数据
	        	    		PageData pgRule = new PageData();
	        	    		pgRule.put("CODINGRULES_ID", CODINGRULEID);
	        	    		PageData findById = codingrulesService.findById(pgRule);
	        	    		findById.put("GETVALUE", returnCode);
	        	    		findById.put("ACQUISITIONTIME", sdFormat.format(new Date()));
	        	    		codingrulesService.edit(findById);
	            			pd.put("ProcessDefinition_ID", this.get32UUID());
			  	            pd.put("FNum", returnCode);//编码
			  	            pd.put("FName", ObjectExcelRead.getCellValue(sheet, row, 0));//名称
			  	            pd.put("FStatus", "启用".equals(ObjectExcelRead.getCellValue(sheet, row, 1))?
			  	            		ObjectExcelRead.getCellValue(sheet, row, 1):"停用");//状态
			  	            pd.put("FStationID", stationpd.getString("WC_STATION_ID"));//工位ID
			  	            pd.put("WordScannedIF", "是".equals(ObjectExcelRead.getCellValue(sheet, row, 4))?
			  	            		ObjectExcelRead.getCellValue(sheet, row, 4):"否");//是否单词扫码
			  	            pd.put("OneYardToTheEndIF", "是".equals(ObjectExcelRead.getCellValue(sheet, row, 5))?
			  	            		ObjectExcelRead.getCellValue(sheet, row, 5):"否");//是否一码到底
			  	            pd.put("MaterialTraceability", ObjectExcelRead.getCellValue(sheet, row, 6));//用料追溯关系ID
			  	            pd.put("UnqualifiedProducts", "允许".equals(ObjectExcelRead.getCellValue(sheet, row, 7))?
			  	            		ObjectExcelRead.getCellValue(sheet, row, 7):"不允许");//不合格品投产
			  	            pd.put("FrozenIF", "是".equals(ObjectExcelRead.getCellValue(sheet, row, 8))?
			  	            		ObjectExcelRead.getCellValue(sheet, row, 8):"否");//产出是否冻结
			  	            pd.put("ReportTemplateID", "");//报告模板ID
			  	            pd.put("ProductionDesc", ObjectExcelRead.getCellValue(sheet, row, 12));//生产描述
			  	            pd.put("QIPlanID", "");//质检方案ID
			  	            pd.put("FAttachmentName", "");//附件名称
			  	            pd.put("FAttachmentDirect", "");//附件路径
			  	            pd.put("FAttachmentIF", "NO");//附件IF
			  	            pd.put("EnableSOP", "NO");//启用SOP
			  	            pd.put("SOP_ID", "");//SOPID
			  	            if(ObjectExcelRead.getCellValue(sheet, row, 10).matches(reg)==true) {//判断表格工作时长值是否为数字，不为数字填写0
			  	            	pd.put("WorkHours", ObjectExcelRead.getCellValue(sheet, row, 10));//工作时长
		 	 	            }else {
		 	 	            	pd.put("WorkHours", 0);//工作时长
		 	 	            }
			  	            pd.put("WorkHoursUnit", ObjectExcelRead.getCellValue(sheet, row, 11));//工作时长单位
			  	            pd.put("WPType", ObjectExcelRead.getCellValue(sheet, row, 9));//工序类型(投入产出,投入,产出,记录)
			  	            pd.put("FCreatePersonID", staffid);//创建人
			  			    pd.put("FCreateTime", Tools.date2Str(new Date()));//创建时间
			  			    pd.put("LastModifiedBy", staffid);//最后修改人
			  			    pd.put("LastModificationTime", Tools.date2Str(new Date()));//最后修改时间
			  	            ProcessDefinitionService.save(pd);
			  	            operationrecordService.add("","工序定义","导入",pd.getString("ProcessDefinition_ID"),staffid,"导入工序定义");
			  	            succeedNum=succeedNum+1;
	            		}else {
		 	            	failNum=failNum+1;
		 	            }
	            		
		            }else {
	            		failNum=failNum+1;
	            	}
	            }catch (IOException e) {
		            e.printStackTrace();
		            errInfo = "fail";
		        }
	            
	        }
		}
		map.put("succeedNum", succeedNum+"");				//返回成功数量结果
		map.put("failNum", failNum+"");				//返回失败数量结果
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "ProcessDefinition.xlsx", "ProcessDefinition.xlsx");
	}
	
	
	private String formatNum(int input, int FLENGTH) {
		// 大于1000时直接转换成字符串返回
		double pow = Math.pow(10.00, FLENGTH);
		if (input > pow - 1) {
			throw new RuntimeException("生成失败，编码号已经超过了" + FLENGTH + "位");
		}
		return String.format("%0" + FLENGTH + "d", input);
	}
}
