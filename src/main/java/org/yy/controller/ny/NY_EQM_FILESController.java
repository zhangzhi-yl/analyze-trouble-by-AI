package org.yy.controller.ny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.zm.EQM_FILESService;
import org.yy.util.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** 
 * 说明：设备附件
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-09
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ny_eqm_files")
public class NY_EQM_FILESController extends BaseController {
	
	@Autowired
	private EQM_FILESService eqm_filesService;

	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
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
		if(null != pd.getString("EQUIPMENT_ID") && !"".equals(pd.getString("EQUIPMENT_ID"))){
			if (null != file && !file.isEmpty()) {
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
				String fileNamereal = pd.getString("FName").substring(0, pd.getString("FName").indexOf(".")); // 文件上传路径
				fileName = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
				String FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+fileName;
				pd.put("FilePath",FPFFILEPATH);
			}
			pd.put("EQM_FILES_ID", this.get32UUID());	//主键
			pd.put("Creator", Jurisdiction.getName());	//创建人
			pd.put("CreateTime", Tools.date2Str(new Date()));	//创建时间
			pd.put("EQUIPMENT_ID", pd.getString("EQUIPMENT_ID"));	//设备主键
			pd.put("FName", pd.getString("FName"));	//文件名称
			pd.put("FileType", pd.getString("FileType"));	//文件类型
			eqm_filesService.save(pd);
		}else {
			errInfo = "201";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
//	@RequiresPermissions("eqm_files:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_filesService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
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
			String fileNamereal = pd.getString("FName").substring(0, pd.getString("FName").indexOf(".")); // 文件上传路径
			fileName = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+fileName;
			pd.put("FilePath",FPFFILEPATH);
		}
		pd.put("FName", pd.getString("FName"));	//文件名称
		pd.put("FileType", pd.getString("FileType"));	//文件类型
		pd.put("EQM_FILES_ID", pd.getString("EQM_FILES_ID"));	//主键
		eqm_filesService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
//	@RequiresPermissions("eqm_files:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_filesService.list(page);	//列出EQM_FILES列表
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
//	@RequiresPermissions("eqm_files:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_filesService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
//	@RequiresPermissions("eqm_files:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_filesService.deleteAll(ArrayDATA_IDS);
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
//	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("文件名");	//1
		titles.add("文件路径");	//2
		titles.add("创建人");	//3
		titles.add("创建时间");	//4
		titles.add("文件类型");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_filesService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FName"));	    //1
			vpd.put("var2", varOList.get(i).getString("FilePath"));	    //2
			vpd.put("var3", varOList.get(i).getString("Creator"));	    //3
			vpd.put("var4", varOList.get(i).getString("CreateTime"));	    //4
			vpd.put("var5", varOList.get(i).getString("FileType"));	    //5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

	/**下载
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/download")
	public Object downExcel(HttpServletResponse response)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		String FPFFILENAME = pd.getString("FName");
		String FPFFILEPATH = pd.getString("FilePath");
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + FPFFILEPATH, FPFFILENAME);
		return map;
	}
}
