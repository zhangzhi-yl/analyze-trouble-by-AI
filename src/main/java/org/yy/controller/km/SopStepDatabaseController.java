package org.yy.controller.km;

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
import org.yy.util.DelFileUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.SopStepDatabaseService;
import org.yy.service.km.SopStepService;

/** 
 * 说明：SOP步骤库
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-18
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/SopStepDatabase")
public class SopStepDatabaseController extends BaseController {
	
	@Autowired
	private SopStepDatabaseService SopStepDatabaseService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private SopStepService SopStepService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("SopStepDatabase:add")
	@ResponseBody
	public Object add(
			@RequestParam(value = "FANNEX_Path", required = false) MultipartFile file,
			@RequestParam(value="FAnnex_Name",required=false) String path,
			@RequestParam(value="SopStepDatabase_ID",required=false) String SopStepDatabase_ID,
			@RequestParam(value="FNumber",required=false) String FNumber,
			@RequestParam(value="FStepName",required=false) String FStepName,
			@RequestParam(value="FShowName",required=false) String FShowName,
			@RequestParam(value="FIsAlarm",required=false) String FIsAlarm,
			@RequestParam(value="FType",required=false) String FType,
			@RequestParam(value="FState",required=false) String FState,
			@RequestParam(value="FCreatePersonID",required=false) String FCreatePersonID,
			@RequestParam(value="FCreateTime",required=false) String FCreateTime,
			@RequestParam(value="FNormalHours",required=false) String FNormalHours,
			@RequestParam(value="FPart_Number",required=false) String FPart_Number,
			@RequestParam(value="FPart_Dosage",required=false) String FPart_Dosage,
			@RequestParam(value="FMotion_Time",required=false) String FMotion_Time,
			@RequestParam(value="FSmbol",required=false) String FSmbol,
			@RequestParam(value="FTool",required=false) String FTool,
			@RequestParam(value="FCraft_Para",required=false) String FCraft_Para,
			@RequestParam(value="FKey_Point",required=false) String FKey_Point,
			@RequestParam(value="FCause",required=false) String FCause,
			@RequestParam(value="FSelf_Project",required=false) String FSelf_Project
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("SopStepDatabase_ID", this.get32UUID());	//主键
		pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		pd.put("FCreateTime", Tools.date2Str(new Date()));
		pd.put("FNumber", FNumber);	
		pd.put("FStepName", FStepName);	
		pd.put("FShowName", FShowName);	
		pd.put("FIsAlarm", FIsAlarm);	
		pd.put("FType", FType);	
		pd.put("FState", FState);	
		pd.put("FNormalHours", Double.parseDouble(FNormalHours.toString()));	
		pd.put("FPart_Number", FPart_Number);	
		pd.put("FPart_Dosage", FPart_Dosage);	
		pd.put("FMotion_Time", FMotion_Time);	
		pd.put("FSmbol", FSmbol);	
		pd.put("FTool", FTool);
		pd.put("FCraft_Para", FCraft_Para);
		pd.put("FKey_Point", FKey_Point);
		pd.put("FCause", FCause);
		pd.put("FSelf_Project", FSelf_Project);
		pd.put("FAnnex_Name", path);
		//单号验重
		PageData pdNum = SopStepDatabaseService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {		
			//上传附件
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			String ffile = DateUtil.getDays();
			String SopFAnnex_Name = "";
			String SopFANNEX_Path = "";
			if (null != file && !file.isEmpty()) {
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
				String fileNamereal = pd.getString("FAnnex_Name").substring(0, pd.getString("FAnnex_Name").indexOf(".")); // 文件上传路径
				SopFAnnex_Name = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
				SopFANNEX_Path = Const.FILEPATHFILE +DateUtil.getDays()+"/"+SopFAnnex_Name;
				//附件集插入数据
				PageData pdFile=new PageData();
				pdFile.put("DataSources", "SOP步骤库");
				pdFile.put("AssociationIDTable", "KM_SopStepDatabase");
				pdFile.put("AssociationID", pd.getString("SopStepDatabase_ID"));
				pdFile.put("FName", SopFAnnex_Name);
				pdFile.put("FUrl", SopFANNEX_Path);
				pdFile.put("FExplanation", "");
				pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
				pdFile.put("FCreateTime", Tools.date2Str(new Date()));
				attachmentsetService.check(pdFile);
			}
			pd.put("FANNEX_Path", SopFANNEX_Path);	
			SopStepDatabaseService.save(pd);
	        pd = SopStepDatabaseService.findById(pd);	//根据ID读取
		}
		
		map.put("result", errInfo);						//返回结果
		return map;
	}
	/*@RequestMapping(value="/add")
	@RequiresPermissions("SopStepDatabase:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = SopStepDatabaseService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {		
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("SopStepDatabase_ID", this.get32UUID());	//主键
		pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		pd.put("FCreateTime", Tools.date2Str(new Date()));
		SopStepDatabaseService.save(pd);
		}
		map.put("result", errInfo);
		return map;
	}*/
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("SopStepDatabase:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		SopStepDatabaseService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("SopStepDatabase:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value = "FANNEX_Path", required = false) MultipartFile file,
			@RequestParam(value="FAnnex_Name",required=false) String path,
			@RequestParam(value="SopStepDatabase_ID",required=false) String SopStepDatabase_ID,
			@RequestParam(value="FNumber",required=false) String FNumber,
			@RequestParam(value="FStepName",required=false) String FStepName,
			@RequestParam(value="FShowName",required=false) String FShowName,
			@RequestParam(value="FIsAlarm",required=false) String FIsAlarm,
			@RequestParam(value="FType",required=false) String FType,
			@RequestParam(value="FState",required=false) String FState,
			@RequestParam(value="FCreatePersonID",required=false) String FCreatePersonID,
			@RequestParam(value="FCreateTime",required=false) String FCreateTime,
			@RequestParam(value="FNormalHours",required=false) String FNormalHours,
			@RequestParam(value="FPart_Number",required=false) String FPart_Number,
			@RequestParam(value="FPart_Dosage",required=false) String FPart_Dosage,
			@RequestParam(value="FMotion_Time",required=false) String FMotion_Time,
			@RequestParam(value="FSmbol",required=false) String FSmbol,
			@RequestParam(value="FTool",required=false) String FTool,
			@RequestParam(value="FCraft_Para",required=false) String FCraft_Para,
			@RequestParam(value="FKey_Point",required=false) String FKey_Point,
			@RequestParam(value="FCause",required=false) String FCause,
			@RequestParam(value="FSelf_Project",required=false) String FSelf_Project,
			@RequestParam(value="FANNEX_PathEDIT",required=false) String FANNEX_PathEDIT
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SopStepDatabase_ID", SopStepDatabase_ID);	//主键
		pd.put("FNumber", FNumber);
		pd.put("FStepName", FStepName);
		pd.put("FShowName", FShowName);	
		pd.put("FIsAlarm", FIsAlarm);	
		pd.put("FType", FType);	
		pd.put("FState", FState);	
		pd.put("FCreatePersonID", FCreatePersonID);	
		pd.put("FCreateTime", FCreateTime);	
		pd.put("FNormalHours", Double.parseDouble(FNormalHours.toString()));	
		pd.put("FPart_Number", FPart_Number);	
		pd.put("FPart_Dosage", FPart_Dosage);	
		pd.put("FMotion_Time", FMotion_Time);	
		pd.put("FSmbol", FSmbol);	
		pd.put("FTool", FTool);
		pd.put("FCraft_Para", FCraft_Para);
		pd.put("FKey_Point", FKey_Point);
		pd.put("FCause", FCause);
		pd.put("FANNEX_Path", FANNEX_PathEDIT);
		pd.put("FAnnex_Name", path);
		//单号验重
		PageData pdNum = SopStepDatabaseService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {	
			//上传附件
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			String ffile = DateUtil.getDays();
			String SopFAnnex_Name = "";
			String SopFANNEX_Path = "";
			PageData pdJ = new PageData();
			pdJ=SopStepDatabaseService.findById(pd);
			String FAnnex_NameJ="";
			if(null!=pdJ) {
				FAnnex_NameJ=pdJ.getString("FAnnex_Name");
			}
			if (null != file && !file.isEmpty()) {//上传附件
				if(!FAnnex_NameJ.equals(pd.getString("FAnnex_Name"))) {
					String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
					String fileNamereal = pd.getString("FAnnex_Name").substring(0, pd.getString("FAnnex_Name").indexOf(".")); // 文件上传路径
					SopFAnnex_Name = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
					SopFANNEX_Path = Const.FILEPATHFILE +DateUtil.getDays()+"/"+SopFAnnex_Name;
					//附件集插入数据
					PageData pdFile=new PageData();
					pdFile.put("DataSources", "SOP步骤库");
					pdFile.put("AssociationIDTable", "KM_SopStepDatabase");
					pdFile.put("AssociationID", pd.getString("SopStepDatabase_ID"));
					pdFile.put("FName", SopFAnnex_Name);
					pdFile.put("FUrl", SopFANNEX_Path);
					pdFile.put("FExplanation", "");
					pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
					pdFile.put("FCreateTime", Tools.date2Str(new Date()));
					pd.put("FANNEX_Path", SopFANNEX_Path);
					attachmentsetService.check(pdFile);
				}
			}else {//没上传附件引用之前的附件信息
				pd.put("FAnnex_Name", pdJ.getString("FAnnex_Name"));//附件名称
				pd.put("FANNEX_Path", pdJ.getString("FANNEX_Path"));//附件名称
			}
			SopStepDatabaseService.edit(pd);
		}
		
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/*public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = SopStepDatabaseService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {		
		SopStepDatabaseService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
	}*/
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("SopStepDatabase:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = SopStepDatabaseService.list(page);	//列出SopStepDatabase列表
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
	@RequiresPermissions("SopStepDatabase:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = SopStepDatabaseService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("SopStepDatabase:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			SopStepDatabaseService.deleteAll(ArrayDATA_IDS);
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
		titles.add("编号");	//1
		titles.add("名称");	//2
		titles.add("显示名称");	//3
		titles.add("未执行报警");	//4
		titles.add("类型(数据字典)");	//5
		titles.add("状态(启用，禁用)");	//6
		titles.add("创建人");	//7
		titles.add("创建时间");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = SopStepDatabaseService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNumber"));	    //1
			vpd.put("var2", varOList.get(i).getString("FSopName"));	    //2
			vpd.put("var3", varOList.get(i).getString("FShowName"));	    //3
			vpd.put("var4", varOList.get(i).getString("FIsAlarm"));	    //4
			vpd.put("var5", varOList.get(i).getString("FType"));	    //5
			vpd.put("var6", varOList.get(i).getString("FState"));	    //6
			vpd.put("var7", varOList.get(i).getString("FCreatePersonID"));	    //7
			vpd.put("var8", varOList.get(i).getString("FCreateTime"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**批量引用
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/quoteAll")
	@ResponseBody
	public Object quoteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = SopStepDatabaseService.listAllByIds(pd);//根据ID获取SOP步骤库列表
		for(int i=0;i<varList.size();i++) {
			PageData pdMx=varList.get(i);
			pdMx.put("SopStep_ID", this.get32UUID());	//主键
			pdMx.put("FNAME", Jurisdiction.getName());
			pdMx.put("FCreatePersonID", staffService.getStaffId(pdMx).getString("STAFF_ID"));//查询职员ID
			pdMx.put("FCreateTime", Tools.date2Str(new Date()));
			pdMx.put("SopSchemeTemplate_ID", pd.get("SopSchemeTemplate_ID"));
			pdMx.put("FStandardHour", pdMx.get("FNormalHours").toString());
			pdMx.put("FSource", "引用");
			pdMx.put("FIsFirst", "否");
			pdMx.put("FSortTime", System.currentTimeMillis());
			pdMx.put("FPart_Number", pdMx.getString("FPart_Number"));
			pdMx.put("FPart_Dosage", pdMx.getString("FPart_Dosage"));
			pdMx.put("FMotion_Time", pdMx.getString("FMotion_Time"));
			pdMx.put("FSmbol", pdMx.getString("FSmbol"));
			pdMx.put("FTool", pdMx.getString("FTool"));
			pdMx.put("FCraft_Para", pdMx.getString("FCraft_Para"));
			pdMx.put("FKey_Point", pdMx.getString("FKey_Point"));
			pdMx.put("FCause", pdMx.getString("FCause"));
			pdMx.put("FSelf_Project", pdMx.getString("FSelf_Project"));
			if(null!=pdMx.getString("FAnnex_Name")&&!"".equals(pdMx.getString("FAnnex_Name"))) {
				pdMx.put("FAnnex_Name", pdMx.getString("FAnnex_Name"));
				pdMx.put("FANNEX_Path", pdMx.getString("FANNEX_Path"));
				//附件集插入数据
				PageData pdFile=new PageData();
				pdFile.put("DataSources", "SOP方案模板创建SOP步骤");
				pdFile.put("AssociationIDTable", "KM_SopStep");
				pdFile.put("AssociationID", pdMx.getString("SopStep_ID"));
				pdFile.put("FName", pdMx.getString("FAnnex_Name"));
				pdFile.put("FUrl", pdMx.getString("FANNEX_Path"));
				pdFile.put("FExplanation", "");
				pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
				pdFile.put("FCreateTime", Tools.date2Str(new Date()));
				attachmentsetService.check(pdFile);
			}
			SopStepService.save(pdMx);
		}
		map.put("result", errInfo);
		return map;
	}
	/**禁用/启用
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editState")
	@ResponseBody
	public Object editState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		SopStepDatabaseService.editState(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除附件
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delFj")
	@ResponseBody
	public Object delFj() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PATH = pd.getString("PATH");	
		if(Tools.notEmpty(pd.getString("PATH").trim())){								//附件路径
			DelFileUtil.delFolder(PathUtil.getProjectpath() + pd.getString("PATH")); 	//删除硬盘中的附件
		}
		if(PATH != null){
			SopStepDatabaseService.delFj(pd);													//删除数据库中附件数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
}
