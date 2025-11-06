package org.yy.controller.mbase;

import java.text.SimpleDateFormat;
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
import org.yy.util.DelFileUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mbase.MAT_ANNEXService;

/** 
 * 说明：物料附件资料
 * 作者：YuanYe
 * 时间：2020-01-07
 * 
 */
@Controller
@RequestMapping("/mat_annex")
public class MAT_ANNEXController extends BaseController {
	
	@Autowired
	private MAT_ANNEXService mat_annexService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("mat_annex:add")
	@ResponseBody
	public Object add(
			@RequestParam(value="ANNEX_PATH",required=false) MultipartFile file,
			@RequestParam(value="MAT_BASIC_ID",required=false) String MAT_BASIC_ID,
			@RequestParam(value="ANNEX_NAME",required=false) String ANNEX_NAME,
			@RequestParam(value="ANNEX_DES",required=false) String ANNEX_DES
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("MAT_ANNEX_ID", this.get32UUID());	//主键
			pd.put("FCREATOR",Jurisdiction.getName());//创建人
			pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
			pd.put("ANNEX_PATH", Const.FILEPATHIMG + ffile + "/" + fileName);	
			pd.put("ANNEX_NAME", ANNEX_NAME);
			pd.put("MAT_BASIC_ID", MAT_BASIC_ID);	
			pd.put("ANNEX_DES", ANNEX_DES);	
		}
		mat_annexService.save(pd);
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
			mat_annexService.delFj(pd);													//删除数据库中附件数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("mat_annex:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		mat_annexService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("mat_annex:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value="ANNEX_PATH",required=false) MultipartFile file,
			@RequestParam(value="path",required=false) String path,
			@RequestParam(value="MAT_ANNEX_ID",required=false) String MAT_ANNEX_ID,
			@RequestParam(value="MAT_BASIC_ID",required=false) String MAT_BASIC_ID,
			@RequestParam(value="ANNEX_NAME",required=false) String ANNEX_NAME,
			@RequestParam(value="FCREATOR",required=false) String FCREATOR,
			@RequestParam(value="CREATE_TIME",required=false) String CREATE_TIME,
			@RequestParam(value="ANNEX_DES",required=false) String ANNEX_DES
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MAT_ANNEX_ID", MAT_ANNEX_ID);	//主键
		pd.put("FCREATOR",FCREATOR);//创建人
		pd.put("CREATE_TIME", CREATE_TIME);//创建时间
		pd.put("MAT_BASIC_ID", MAT_BASIC_ID);	
		pd.put("ANNEX_DES", ANNEX_DES);	
		pd.put("ANNEX_NAME", ANNEX_NAME);
		if (null != file && !file.isEmpty()) {
			String  ffile = DateUtil.getDays(), fileName = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("ANNEX_PATH", Const.FILEPATHIMG + ffile + "/" + fileName);				//路径
		}else{
			pd.put("ANNEX_PATH", path);
		}
		mat_annexService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("mat_annex:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = mat_annexService.list(page);	//列出MAT_ANNEX列表
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
	//@RequiresPermissions("mat_annex:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = mat_annexService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("mat_annex:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			mat_annexService.deleteAll(ArrayDATA_IDS);
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
		titles.add("基础资料ID");	//1
		titles.add("附件名称");	//2
		titles.add("附件路径");	//3
		titles.add("描述");	//4
		titles.add("创建人");	//5
		titles.add("创建时间");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = mat_annexService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MAT_BASIC_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("ANNEX_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("ANNEX_PATH"));	    //3
			vpd.put("var4", varOList.get(i).getString("ANNEX_DES"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCREATOR"));	    //5
			vpd.put("var6", varOList.get(i).getString("CREATE_TIME"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
