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
import org.yy.service.km.SopStepService;

/** 
 * 说明：SOP_步骤
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-18
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/SopStep")
public class SopStepController extends BaseController {
	
	@Autowired
	private SopStepService SopStepService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("SopStep:add")
	@ResponseBody
	public Object add(
			@RequestParam(value = "FANNEX_Path", required = false) MultipartFile file,
			@RequestParam(value="FAnnex_Name",required=false) String path,
			@RequestParam(value="SopStep_ID",required=false) String SopStep_ID,
			@RequestParam(value="FSortTime",required=false) String FSortTime,
			@RequestParam(value="FIsFirst",required=false) String FIsFirst,
			@RequestParam(value="FShowName",required=false) String FShowName,
			@RequestParam(value="FStepName",required=false) String FStepName,
			@RequestParam(value="FIsAlarm",required=false) String FIsAlarm,
			@RequestParam(value="FType",required=false) String FType,
			@RequestParam(value="SopSchemeTemplate_ID",required=false) String SopSchemeTemplate_ID,
			@RequestParam(value="FStandardHour",required=false) String FStandardHour,
			@RequestParam(value="FSource",required=false) String FSource,
			@RequestParam(value="FCreatePersonID",required=false) String FCreatePersonID,
			@RequestParam(value="FCreateTime",required=false) String FCreateTime,
			@RequestParam(value="FNumber",required=false) String FNumber,
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
		pd.put("SopStep_ID", this.get32UUID());	//主键
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		pd.put("FCreateTime", Tools.date2Str(new Date()));		
		pd.put("FIsFirst", FIsFirst);	
		pd.put("FStepName", FStepName);	
		pd.put("FShowName", FShowName);	
		pd.put("FIsAlarm", FIsAlarm);	
		pd.put("FType", FType);	
		pd.put("SopSchemeTemplate_ID", SopSchemeTemplate_ID);	
		pd.put("FStandardHour", Double.parseDouble(FStandardHour.toString()));
		pd.put("FSource", FSource);
		pd.put("FNumber", FNumber);
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
		PageData pdNum = SopStepService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail2";//单号重复
		}else {	
			String FIsFirst1=pd.getString("FIsFirst");
			if(FIsFirst1.equals("是")) {//起始步骤赋为最小时间戳
				PageData pdFirst=SopStepService.getFIsFirst(pd);//获取方案起始步骤
				if(pdFirst != null && Integer.parseInt(pdFirst.get("FNUM").toString())>0) {
					map.put("result", "fail1");//方案起始步骤应该唯一
					return map;
				}
				Date d = new Date();
				String minDate ="2000-01-01";
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				d = sf.parse(minDate);// 日期转换为时间戳
				pd.put("FSortTime", d.getTime());
			}else {
				pd.put("FSortTime", System.currentTimeMillis());
			}
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
				pdFile.put("DataSources", "SOP方案模板创建SOP步骤");
				pdFile.put("AssociationIDTable", "KM_SopStep");
				pdFile.put("AssociationID", pd.getString("SopStep_ID"));
				pdFile.put("FName", SopFAnnex_Name);
				pdFile.put("FUrl", SopFANNEX_Path);
				pdFile.put("FExplanation", "");
				pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
				pdFile.put("FCreateTime", Tools.date2Str(new Date()));
				attachmentsetService.check(pdFile);
			}
			pd.put("FANNEX_Path", SopFANNEX_Path);	
			SopStepService.save(pd);
	        pd = SopStepService.findById(pd);	//根据ID读取
		}
		
		map.put("result", errInfo);						//返回结果
		return map;
	}
	/*public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = SopStepService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail2";//单号重复
		}else {	
		String FIsFirst=pd.getString("FIsFirst");
		if(FIsFirst.equals("是")) {//起始步骤赋为最小时间戳
			PageData pdFirst=SopStepService.getFIsFirst(pd);//获取方案起始步骤
			if(pdFirst != null && Integer.parseInt(pdFirst.get("FNUM").toString())>0) {
				map.put("result", "fail1");//方案起始步骤应该唯一
				return map;
			}
			Date d = new Date();
			String minDate ="2000-01-01";
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			d = sf.parse(minDate);// 日期转换为时间戳
			pd.put("FSortTime", d.getTime());
		}else {
			pd.put("FSortTime", System.currentTimeMillis());
		}
		
		pd.put("SopStep_ID", this.get32UUID());	//主键
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		pd.put("FCreateTime", Tools.date2Str(new Date()));		
		SopStepService.save(pd);
		}
		map.put("result", errInfo);
		return map;
	}*/
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("SopStep:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		SopStepService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("SopStep:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value = "FANNEX_Path", required = false) MultipartFile file,
			@RequestParam(value="FAnnex_Name",required=false) String path,
			@RequestParam(value="SopStep_ID",required=false) String SopStep_ID,
			@RequestParam(value="FSortTime",required=false) String FSortTime,
			@RequestParam(value="FIsFirst",required=false) String FIsFirst,
			@RequestParam(value="FStepName",required=false) String FStepName,
			@RequestParam(value="FShowName",required=false) String FShowName,
			@RequestParam(value="FIsAlarm",required=false) String FIsAlarm,
			@RequestParam(value="FType",required=false) String FType,
			@RequestParam(value="SopSchemeTemplate_ID",required=false) String SopSchemeTemplate_ID,
			@RequestParam(value="FStandardHour",required=false) String FStandardHour,
			@RequestParam(value="FSource",required=false) String FSource,
			@RequestParam(value="FCreatePersonID",required=false) String FCreatePersonID,
			@RequestParam(value="FCreateTime",required=false) String FCreateTime,
			@RequestParam(value="FNumber",required=false) String FNumber,
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
		pd.put("SopStep_ID", SopStep_ID);	//主键
		pd.put("FSortTime", FSortTime);
		pd.put("FIsFirst", FIsFirst);
		pd.put("FStepName", FStepName);	
		pd.put("FShowName", FShowName);	
		pd.put("FIsAlarm", FIsAlarm);	
		pd.put("FType", FType);	
		pd.put("SopSchemeTemplate_ID", SopSchemeTemplate_ID);	
		pd.put("FStandardHour", Double.parseDouble(FStandardHour.toString()));	
		pd.put("FSource", FSource);	
		pd.put("FCreatePersonID", FCreatePersonID);	
		pd.put("FCreateTime", FCreateTime);	
		pd.put("FNumber", FNumber);	
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
		PageData pdNum = SopStepService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail2";//单号重复
		}else {	
			String FIsFirst1=pd.getString("FIsFirst");
			String FSortTime1=pd.getString("FSortTime");
			if(FIsFirst1.equals("是")) {//起始步骤赋为最小时间戳
				PageData pdFirst=SopStepService.getFIsFirst(pd);//获取方案起始步骤
				if(pdFirst != null && Integer.parseInt(pdFirst.get("FNUM").toString())>0) {
					map.put("result", "fail1");//方案起始步骤应该唯一
					return map;
				}
				Date d = new Date();
				String minDate ="2000-01-01";
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				d = sf.parse(minDate);// 日期转换为时间戳
				pd.put("FSortTime", d.getTime());
			}else if(FIsFirst1.equals("否") && FSortTime1.equals("946656000000")) {
				pd.put("FSortTime", System.currentTimeMillis());
			}
			//上传附件
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			String ffile = DateUtil.getDays();
			String SopFAnnex_Name = "";
			String SopFANNEX_Path = "";
			PageData pdJ = new PageData();
			pdJ=SopStepService.findById(pd);
			String FAnnex_NameJ="";
			if(null!=pdJ) {
				FAnnex_NameJ=pdJ.getString("FAnnex_Name");
			}
			if (null != file && !file.isEmpty()) {//上传附件
				if(null!=pd.getString("FAnnex_Name")&&!FAnnex_NameJ.equals(pd.getString("FAnnex_Name"))) {
					String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
					String fileNamereal = pd.getString("FAnnex_Name").substring(0, pd.getString("FAnnex_Name").indexOf(".")); // 文件上传路径
					SopFAnnex_Name = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
					SopFANNEX_Path = Const.FILEPATHFILE +DateUtil.getDays()+"/"+SopFAnnex_Name;
					//附件集插入数据
					PageData pdFile=new PageData();
					pdFile.put("DataSources", "SOP方案模板创建SOP步骤");
					pdFile.put("AssociationIDTable", "KM_SopStep");
					pdFile.put("AssociationID", pd.getString("SopStep_ID"));
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
			SopStepService.edit(pd);
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
		PageData pdNum = SopStepService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail2";//单号重复
		}else {	
					
		
		String FIsFirst=pd.getString("FIsFirst");
		String FSortTime=pd.getString("FSortTime");
		if(FIsFirst.equals("是")) {//起始步骤赋为最小时间戳
			PageData pdFirst=SopStepService.getFIsFirst(pd);//获取方案起始步骤
			if(pdFirst != null && Integer.parseInt(pdFirst.get("FNUM").toString())>0) {
				map.put("result", "fail1");//方案起始步骤应该唯一
				return map;
			}
			Date d = new Date();
			String minDate ="2000-01-01";
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			d = sf.parse(minDate);// 日期转换为时间戳
			pd.put("FSortTime", d.getTime());
		}else if(FIsFirst.equals("否") && FSortTime.equals("946656000000")) {
			pd.put("FSortTime", System.currentTimeMillis());
		}
		SopStepService.edit(pd);
		
		}
		map.put("result", errInfo);
		return map;
	}*/
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("SopStep:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = SopStepService.list(page);	//列出SopStep列表
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
	@RequiresPermissions("SopStep:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = SopStepService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("SopStep:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			SopStepService.deleteAll(ArrayDATA_IDS);
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
		titles.add("排序时间毫秒");	//1
		titles.add("是否为起始步骤");	//2
		titles.add("步骤名称");	//3
		titles.add("显示名称");	//4
		titles.add("未执行报警");	//5
		titles.add("类型");	//6
		titles.add("SOP方案模板ID");	//7
		titles.add("标准工时");	//8
		titles.add("来源（引用，自建）");	//9
		titles.add("创建人");	//10
		titles.add("创建时间");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = SopStepService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FSortTime"));	    //1
			vpd.put("var2", varOList.get(i).getString("FIsFirst"));	    //2
			vpd.put("var3", varOList.get(i).getString("FStepName"));	    //3
			vpd.put("var4", varOList.get(i).getString("FShowName"));	    //4
			vpd.put("var5", varOList.get(i).getString("FIsAlarm"));	    //5
			vpd.put("var6", varOList.get(i).getString("FType"));	    //6
			vpd.put("var7", varOList.get(i).getString("SopStepDatabase_ID"));	    //7
			vpd.put("var8", varOList.get(i).get("FStandardHour").toString());	//8
			vpd.put("var9", varOList.get(i).getString("FSource"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCreatePersonID"));	    //10
			vpd.put("var11", varOList.get(i).getString("FCreateTime"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**向下排序
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/down")
	@ResponseBody
	public Object down() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdy=SopStepService.findById(pd);
		PageData pdx=SopStepService.getDown(pdy);//获取选中条目下一个步骤
		PageData pdEdit=new PageData();
		PageData pdEdit1=new PageData();
		if(null != pdx) {
			if(pdy.get("FIsFirst").equals("是")) {
				pdEdit.put("FIsFirst", "是");
			}else {
				pdEdit.put("FIsFirst", "否");
			}
			//反写选中条目的下一条目排序时间
			pdEdit.put("SopStep_ID", pdx.get("SopStep_ID"));
			pdEdit.put("FSortTime", pdy.get("FSortTime"));
			SopStepService.editSort(pdEdit);
			//反写选中条目排序时间
			pdEdit1.put("SopStep_ID", pdy.get("SopStep_ID"));
			pdEdit1.put("FSortTime", pdx.get("FSortTime"));
			pdEdit1.put("FIsFirst", "否");
			SopStepService.editSort(pdEdit1);
			
		}else {
			errInfo="fail1";
		}
		map.put("result", errInfo);
		return map;
	}
	/**向上排序
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/up")
	@ResponseBody
	public Object up() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdy=SopStepService.findById(pd);
		PageData pdx=SopStepService.getUP(pdy);//获取选中条目上一个步骤
		PageData pdEdit=new PageData();
		PageData pdEdit1=new PageData();
		if(null != pdx) {
			if(pdx.get("FIsFirst").equals("是")) {
				pdEdit1.put("FIsFirst", "是");
			}else {
				pdEdit1.put("FIsFirst", "否");
			}
			//反写选中条目的上一条目排序时间
			pdEdit.put("SopStep_ID", pdx.get("SopStep_ID"));
			pdEdit.put("FSortTime", pdy.get("FSortTime"));
			pdEdit.put("FIsFirst", "否");
			SopStepService.editSort(pdEdit);
			//反写选中条目排序时间
			pdEdit1.put("SopStep_ID", pdy.get("SopStep_ID"));
			pdEdit1.put("FSortTime", pdx.get("FSortTime"));
			SopStepService.editSort(pdEdit1);
			
		}else {
			errInfo="fail1";
		}
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
			SopStepService.delFj(pd);													//删除数据库中附件数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
}
