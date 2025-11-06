package org.yy.controller.project.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.project.manager.DTPROJECTFILEService;

/** 
 * 说明：项目文件
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-05
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/dtprojectfile")
public class DTPROJECTFILEController extends BaseController {
	
	@Autowired
	private DTPROJECTFILEService dtprojectfileService;
	
	@Autowired
	private StaffService staffService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("dtprojectfile:add")
	@ResponseBody
	public Object add(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		pd = this.getPageData();
		String FFILENAME = "";
		if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+FFILENAME;
			pd.put("FFILEPATH",FPFFILEPATH);
		}
		
		pd.put("DTPROJECTFILE_ID", this.get32UUID());			//主键
		pd.put("FFOUNDER", Jurisdiction.getName());				//创建人
		pd.put("FFOUNDERNUM", Jurisdiction.getUsername());		//创建人账号
		pd.put("FCREATTIME", Tools.date2Str(new Date()));		//创建时间
		pd.put("FLASTUPDATE", Jurisdiction.getName());			//修改人
		pd.put("FLASTUPDATENUM", Jurisdiction.getUsername());	//修改人账号
		pd.put("FLASTUPDATETIME", Tools.date2Str(new Date()));	//修改时间
		pd.put("VISIABLE", "1");								//删除状态,0删除,1未删除
		//pd.put("DEPTMENT_FNAME", Jurisdiction.getDEPARTMENT_ID());	//部门名称
		pd.put("DEPTMENT_ID", Jurisdiction.getDEPARTMENT_ID());		//部门ID
		//pd.put("YL1", "手动上传");									//项目文件来源
		PageData pd1 = new PageData();
		pd1.put("USERNAME", Jurisdiction.getName());
		PageData pd2 = staffService.getDEPTNAME(pd1);
		if(pd2 != null){
			pd.put("DEPTMENT_FNAME", pd2.getString("DNAME"));
		}else{
			pd.put("DEPTMENT_FNAME", "");
		}
		dtprojectfileService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("dtprojectfile:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//dtprojectfileService.delete(pd);
		pd.put("VISIABLE", "0");
		dtprojectfileService.updateDel(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("dtprojectfile:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FLASTUPDATE", Jurisdiction.getName());				//修改人
		pd.put("FLASTUPDATENUM", Jurisdiction.getUsername());		//修改人账号
		pd.put("FLASTUPDATETIME", Tools.date2Str(new Date()));		//修改时间
		//pd.put("DEPTMENT_FNAME", Jurisdiction.getDEPARTMENT_ID());	//部门名称
		pd.put("DEPTMENT_ID", Jurisdiction.getDEPARTMENT_ID());		//部门ID
		PageData pd1 = new PageData();
		pd1.put("USERNAME", Jurisdiction.getName());
		PageData pd2 = staffService.getDEPTNAME(pd1);
		if(pd2 != null){
			pd.put("DEPTMENT_FNAME", pd2.getString("DNAME"));
		}else{
			pd.put("DEPTMENT_FNAME", "");
		}
		dtprojectfileService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("dtprojectfile:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS1 = pd.getString("KEYWORDS1");					//检索项目号/名称
		String KEYWORDS2 = pd.getString("KEYWORDS2");					//关键设备号/名称
		String KEYWORDS3 = pd.getString("KEYWORDS3");					//关键词项目阶段
		String KEYWORDS4 = pd.getString("KEYWORDS4");					//关键词活动名称
		String KEYWORDS5 = pd.getString("KEYWORDS5");					//关键词文件名称
		String KEYTYPE = pd.getString("KEYTYPE");						//关键词文档类型
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		if(Tools.notEmpty(KEYWORDS2))pd.put("KEYWORDS2", KEYWORDS2.trim());
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		if(Tools.notEmpty(KEYWORDS4))pd.put("KEYWORDS4", KEYWORDS4.trim());
		if(Tools.notEmpty(KEYWORDS5))pd.put("KEYWORDS5", KEYWORDS5.trim());
		if(Tools.notEmpty(KEYTYPE))pd.put("KEYTYPE", KEYTYPE.trim());
		page.setPd(pd);
		List<PageData>	varList = dtprojectfileService.list(page);	//列出DTPROJECTFILE列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listx")
	@ResponseBody
	public Object listx(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = dtprojectfileService.listx(page);	//列出DTPROJECTFILE列表
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
	//@RequiresPermissions("dtprojectfile:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = dtprojectfileService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	 /**新增页获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/getDatax")
		//@RequiresPermissions("dtprojectfile:edit")
		@ResponseBody
		public Object getDatax() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = dtprojectfileService.getDatax(pd);	//根据ID读取
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("dtprojectfile:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			dtprojectfileService.deleteAll(ArrayDATA_IDS);
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
		titles.add("项目名称");	//1
		titles.add("项目代码");	//2
		titles.add("设备名称");	//3
		titles.add("设备号");	//4
		titles.add("文件名称");	//5
		titles.add("文件路径");	//6
		titles.add("文档类型");	//7
		titles.add("项目阶段");	//8
		titles.add("活动名称");	//9
		titles.add("部门名称");	//10
		titles.add("部门ID");	//11
		titles.add("创建人");	//12
		titles.add("创建人账号");	//13
		titles.add("创建时间");	//14
		titles.add("最后修改人");	//15
		titles.add("最后修改人账号");	//16
		titles.add("最后修改时间");	//17
		titles.add("预留字段1");	//18
		titles.add("预留字段2");	//19
		titles.add("预留字段3");	//20
		titles.add("预留字段4");	//21
		titles.add("预留字段5");	//22
		titles.add("项目ID");	//23
		dataMap.put("titles", titles);
		List<PageData> varOList = dtprojectfileService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PROJECT_FNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("PROJECT_FCODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("EQUIPMENT_FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("EQUIPMENT_FCODE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FFILENAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("FFILEPATH"));	    //6
			vpd.put("var7", varOList.get(i).getString("FFILETYPE"));	    //7
			vpd.put("var8", varOList.get(i).getString("PEOJECT_STAGE"));	    //8
			vpd.put("var9", varOList.get(i).getString("ACTIVITIES_FNAME"));	    //9
			vpd.put("var10", varOList.get(i).getString("DEPTMENT_FNAME"));	    //10
			vpd.put("var11", varOList.get(i).getString("DEPTMENT_ID"));	    //11
			vpd.put("var12", varOList.get(i).getString("FFOUNDER"));	    //12
			vpd.put("var13", varOList.get(i).getString("FFOUNDERNUM"));	    //13
			vpd.put("var14", varOList.get(i).getString("FCREATTIME"));	    //14
			vpd.put("var15", varOList.get(i).getString("FLASTUPDATE"));	    //15
			vpd.put("var16", varOList.get(i).getString("FLASTUPDATENUM"));	    //16
			vpd.put("var17", varOList.get(i).getString("FLASTUPDATETIME"));	    //17
			vpd.put("var18", varOList.get(i).getString("YL1"));	    //18
			vpd.put("var19", varOList.get(i).getString("YL2"));	    //19
			vpd.put("var20", varOList.get(i).getString("YL3"));	    //20
			vpd.put("var21", varOList.get(i).getString("YL4"));	    //21
			vpd.put("var22", varOList.get(i).getString("YL5"));	    //22
			vpd.put("var23", varOList.get(i).getString("PROJECT_ID"));	    //23
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**项目列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listProAll")
	@ResponseBody
	public Object listProAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = dtprojectfileService.listProAll(pd);	//列出项目列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**通过id获取项目编号
	 * @param page
	 * @throws Exception
	 */
//	@RequestMapping(value="/findProById")
//	@ResponseBody
//	public Object findProById() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		pd = dtprojectfileService.findProById(pd);	//根据ID读取
//		map.put("pd", pd);
//		map.put("result", errInfo);
//		return map;
//	}
	
	/**设备列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listEquAll")
	@ResponseBody
	public Object listEquAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = dtprojectfileService.listEquAll(pd);	//列出设备列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**阶段列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listStaAll")
	@ResponseBody
	public Object listStaAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = dtprojectfileService.listStaAll(pd);	//列出阶段列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**活动名称列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listActAll")
	@ResponseBody
	public Object listActAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = dtprojectfileService.listActAll(pd);	//列出阶段列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**下载
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/Dowland")
	public void downExcel(HttpServletResponse response)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = dtprojectfileService.findById(pd);	//根据ID读取
		String FFILEPATH = pd.getString("FFILEPATH"); //文件路径
		String FFILENAME = pd.getString("FFILENAME"); //文件名称
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + FFILEPATH, FFILENAME);
	}
}
