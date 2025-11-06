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
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.TOOL_BASICSService;
import org.yy.service.km.TOOL_BORROWService;

/** 
 * 说明：工器具借用归还记录
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-09
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/TOOL_BORROW")
public class TOOL_BORROWController extends BaseController {
	
	@Autowired
	private TOOL_BORROWService TOOL_BORROWService;
	@Autowired
	private TOOL_BASICSService TOOL_BASICSService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("TOOL_BORROW:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TOOL_BORROW_ID", this.get32UUID());	//主键
		TOOL_BORROWService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**借用保存
	 * 点击借用时新增一条借用归还数据，反写主表状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/addBorrow")
	//@RequiresPermissions("TOOL_BORROW:add")
	@ResponseBody
	public Object addBorrow() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_ID());//部门ID
		pd.put("TOOL_BORROW_ID", this.get32UUID());	//主键
		pd.put("TOOL_BASICS_ID", pd.getString("TOOL_BASICS_ID"));//工器具管理ID
		pd.put("FBorrower", Jurisdiction.getName());//借用人
		pd.put("FBorrower_Dept", departmentService.findById(pd).getString("NAME"));//借用部门
		pd.put("FBorrow_Time", Tools.date2Str(new Date()));//借用时间
		pd.put("FDESCRIBE", pd.getString("FComments"));//备注说明
		pd.put("FInspect_State", "借用");//状态
		pd.put("FFile_Name", "");//附件名称
		pd.put("FFile_Path", "");//附件路径
		pd.put("FCreatePerson", Jurisdiction.getName());//创建人
		pd.put("FCreateTime", Tools.date2Str(new Date()));//创建时间
		TOOL_BORROWService.save(pd);
		PageData pdBASICS = new PageData();
		pdBASICS.put("TOOL_BASICS_ID", pd.getString("TOOL_BASICS_ID"));//工器具管理ID
		pdBASICS.put("FSTATE", "占用");//工器具状态
		TOOL_BASICSService.editState(pdBASICS);//更新工器具管理状态
		map.put("result", errInfo);
		return map;
	}
	
	/**归还修改
	 * 点击归还通过主表ID查询借用的数据，并作相应的修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editReturn")
	//@RequiresPermissions("TOOL_BORROW:edit")
	@ResponseBody
	public Object editReturn() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdReturn = new PageData();
		pd = this.getPageData();
		pdReturn=TOOL_BORROWService.findByBASICSId(pd);//通过工器具管理ID获取状态为借用的数据
		if(null!=pdReturn) {
			pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_ID());//部门ID
			pdReturn.put("FDESCRIBE", pd.getString("FComments"));//备注说明
			pdReturn.put("FReturner", Jurisdiction.getName());//操作人
			pdReturn.put("FReturn_Dept", departmentService.findById(pd).getString("NAME"));//操作部门
			pdReturn.put("FReturn_Time", Tools.date2Str(new Date()));//操作时间
			pdReturn.put("FInspect_State", "归还");//状态
			TOOL_BORROWService.edit(pdReturn);
			PageData pdBASICS = new PageData();
			pdBASICS.put("TOOL_BASICS_ID", pd.getString("TOOL_BASICS_ID"));//工器具管理ID
			pdBASICS.put("FSTATE", "闲置");//工器具状态
			TOOL_BASICSService.editState(pdBASICS);//更新工器具管理状态
		}else {
			errInfo = "result";
		}
		map.put("result", errInfo);
		return map;
	}
	
	
	/**报废
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editScrap")
	@ResponseBody
	public Object editScrap(
			@RequestParam(value = "FFile_Path", required = false) MultipartFile file,
			@RequestParam(value="FFile_Name",required=false) String path,
			@RequestParam(value="TOOL_BASICS_ID",required=false) String TOOL_BASICS_ID,
			@RequestParam(value="FDESCRIBE",required=false) String FDESCRIBE
			
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdScrap = new PageData();
		pd = this.getPageData();
		pd.put("TOOL_BASICS_ID", TOOL_BASICS_ID);	//主键
		pd.put("FDESCRIBE", FDESCRIBE);
		pdScrap=TOOL_BORROWService.findByBASICSId(pd);//通过工器具管理ID获取状态为借用的数据
		if(null!=pdScrap) {
			pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_ID());//部门ID
			pdScrap.put("FDESCRIBE", pd.getString("FDESCRIBE"));//备注说明
			pdScrap.put("FReturner", Jurisdiction.getName());//操作人
			pdScrap.put("FReturn_Dept", departmentService.findById(pd).getString("NAME"));//操作部门
			pdScrap.put("FReturn_Time", Tools.date2Str(new Date()));//操作时间
			pdScrap.put("FInspect_State", "报废");//状态
			//上传附件
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			String ffile = DateUtil.getDays();
			String ToolFFile_Name = "";
			String ToolFFile_Path = "";
			if (null != file && !file.isEmpty()) {//上传附件
					String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
					String fileNamereal = pd.getString("FFile_Name").substring(0, pd.getString("FFile_Name").indexOf(".")); // 文件上传路径
					ToolFFile_Name = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
					ToolFFile_Path = Const.FILEPATHFILE +DateUtil.getDays()+"/"+ToolFFile_Name;
					//附件集插入数据
					PageData pdFile=new PageData();
					pdFile.put("DataSources", "工器具借用归还记录-报废");
					pdFile.put("AssociationIDTable", "KM_Tool_Borrow");
					pdFile.put("AssociationID", pd.getString("TOOL_BASICS_ID"));
					pdFile.put("FName", ToolFFile_Name);
					pdFile.put("FUrl", ToolFFile_Path);
					pdFile.put("FExplanation", "");
					pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
					pdFile.put("FCreateTime", Tools.date2Str(new Date()));
					attachmentsetService.check(pdFile);
			}
			pdScrap.put("FFile_Name", ToolFFile_Name);//附件名称
			pdScrap.put("FFile_Path", ToolFFile_Path);//附件路径
			TOOL_BORROWService.edit(pdScrap);
			PageData pdBASICS = new PageData();
			pdBASICS.put("TOOL_BASICS_ID", pd.getString("TOOL_BASICS_ID"));//工器具管理ID
			pdBASICS.put("FScrap_Explain", pd.getString("FDESCRIBE"));//报废说明
			pdBASICS.put("FSTATE", "报废");//工器具状态
			TOOL_BASICSService.editState(pdBASICS);//更新工器具管理状态
		}else {
			errInfo = "result";
		}
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("TOOL_BORROW:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		TOOL_BORROWService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("TOOL_BORROW:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		TOOL_BORROWService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("TOOL_BORROW:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = TOOL_BORROWService.list(page);	//列出TOOL_BORROW列表
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
	//@RequiresPermissions("TOOL_BORROW:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = TOOL_BORROWService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("TOOL_BORROW:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			TOOL_BORROWService.deleteAll(ArrayDATA_IDS);
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
		titles.add("工器具管理ID");	//1
		titles.add("借用人");	//2
		titles.add("借用部门");	//3
		titles.add("借用时间");	//4
		titles.add("操作人");	//5
		titles.add("操作部门");	//6
		titles.add("操作时间");	//7
		titles.add("备注说明");	//8
		titles.add("状态");	//9
		titles.add("附件名称");	//10
		titles.add("附件路径");	//11
		titles.add("创建人");	//12
		titles.add("创建时间");	//13
		titles.add("预留1");	//14
		titles.add("预留2");	//15
		titles.add("预留3");	//16
		dataMap.put("titles", titles);
		List<PageData> varOList = TOOL_BORROWService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TOOL_BASICS_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FBorrower"));	    //2
			vpd.put("var3", varOList.get(i).getString("FBorrower_Dept"));	    //3
			vpd.put("var4", varOList.get(i).getString("FBorrow_Time"));	    //4
			vpd.put("var5", varOList.get(i).getString("FReturner"));	    //5
			vpd.put("var6", varOList.get(i).getString("FReturn_Dept"));	    //6
			vpd.put("var7", varOList.get(i).getString("FReturn_Time"));	    //7
			vpd.put("var8", varOList.get(i).getString("FDESCRIBE"));	    //8
			vpd.put("var9", varOList.get(i).getString("FInspect_State"));	    //9
			vpd.put("var10", varOList.get(i).getString("FFile_Name"));	    //10
			vpd.put("var11", varOList.get(i).getString("FFile_Path"));	    //11
			vpd.put("var12", varOList.get(i).getString("FCreatePerson"));	    //12
			vpd.put("var13", varOList.get(i).getString("FCreateTime"));	    //13
			vpd.put("var14", varOList.get(i).getString("FReservedOne"));	    //14
			vpd.put("var15", varOList.get(i).getString("FReservedTwo"));	    //15
			vpd.put("var16", varOList.get(i).getString("FReservedThree"));	    //16
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
