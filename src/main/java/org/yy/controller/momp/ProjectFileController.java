package org.yy.controller.momp;

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
import org.yy.service.momp.ProjectFileService;

/** 
 * 说明：项目文件
 * 作者：YuanYe
 * 时间：2020-04-29
 * 
 */
@Controller
@RequestMapping("/projectfile")
public class ProjectFileController extends BaseController {
	
	@Autowired
	private ProjectFileService projectfileService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("projectfile:add")
	@ResponseBody
	public Object add(@RequestParam(value = "PATH", required = false) MultipartFile file) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays(), fileName = "";
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FPFFILENAME").substring(0, pd.getString("FPFFILENAME").indexOf(".")); // 文件上传路径
			fileName = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+fileName;
			pd.put("FPFFILEPATH",FPFFILEPATH);
		   }
		pd.put("PROJECTFILE_ID", this.get32UUID());	//主键
		pd.put("FCREATOR", Jurisdiction.getName());
		pd.put("FCTIME", Tools.date2Str(new Date()));
		pd.put("FLATESTMODIFY", Jurisdiction.getName());
		pd.put("FMODIFYTIME", Tools.date2Str(new Date()));
		projectfileService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("projectfile:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		projectfileService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("projectfile:edit")
	@ResponseBody
	public Object edit(@RequestParam(value = "PATH", required = false) MultipartFile file) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays(), fileName = "";
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FPFFILENAME").substring(0, pd.getString("FPFFILENAME").indexOf(".")); // 文件上传路径
			fileName = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+fileName;
			pd.put("FPFFILEPATH",FPFFILEPATH);
		   }
		pd.put("FLATESTMODIFY", Jurisdiction.getName());
		pd.put("FMODIFYTIME", Tools.date2Str(new Date()));
		projectfileService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("projectfile:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = projectfileService.list(page);	//列出ProjectFile列表
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
//	@RequiresPermissions("projectfile:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = projectfileService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("projectfile:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			projectfileService.deleteAll(ArrayDATA_IDS);
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
		titles.add("计划id");	//1
		titles.add("项目名称");	//2
		titles.add("项目编号");	//3
		titles.add("文件名称");	//4
		titles.add("文档类型");	//5
		titles.add("备注");	//6
		titles.add("文件路径");	//7
		titles.add("创建人");	//8
		titles.add("创建时间");	//9
		titles.add("最后修改人");	//10
		titles.add("最后修改时间");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = projectfileService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PLANINSTANCE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FEPNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FEPROJECTCODE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FPFFILENAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("FPFFILETYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("FPFREMARK"));	    //6
			vpd.put("var7", varOList.get(i).getString("FPFFILEPATH"));	    //7
			vpd.put("var8", varOList.get(i).getString("FCREATOR"));	    //8
			vpd.put("var9", varOList.get(i).getString("FCTIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("FLATESTMODIFY"));	    //10
			vpd.put("var11", varOList.get(i).getString("FMODIFYTIME"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
