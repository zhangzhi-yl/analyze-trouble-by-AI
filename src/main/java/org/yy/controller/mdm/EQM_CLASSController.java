package org.yy.controller.mdm;

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
import org.yy.service.mdm.EQM_CLASSService;

/** 
 * 说明：设备类
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_class")
public class EQM_CLASSController extends BaseController {
	
	@Autowired
	private EQM_CLASSService eqm_classService;
	
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_class:add")
	@ResponseBody
	public Object add(
			@RequestParam(value="PICTURE_PATH",required=false) MultipartFile file,
			@RequestParam(value="FIDENTIFY",required=false) String FIDENTIFY,
			@RequestParam(value="CLASS_NAME",required=false) String CLASS_NAME,
			@RequestParam(value="FDES",required=false) String FDES,
			@RequestParam(value="FSTATE",required=false) String FSTATE,
			@RequestParam(value="EQM_COLOR",required=false) String EQM_COLOR
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
			pd.put("EQM_CLASS_ID", this.get32UUID());	//主键
			pd.put("FCREATOR",Jurisdiction.getName());//创建人
			pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
			pd.put("PICTURE_PATH", Const.FILEPATHIMG + ffile + "/" + fileName);	
			pd.put("FIDENTIFY", FIDENTIFY);	
			pd.put("CLASS_NAME", CLASS_NAME);	
			pd.put("FDES", FDES);
			pd.put("FSTATE", FSTATE);	
			pd.put("EQM_COLOR", EQM_COLOR);	
			pd.put("EQM_ICON", fileName);	
		}
		eqm_classService.save(pd);
		pd = eqm_classService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除图片
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delImg")
	@ResponseBody
	public Object delImg() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PATH = pd.getString("PATH");	
		if(Tools.notEmpty(pd.getString("PATH").trim())){								//图片路径
			DelFileUtil.delFolder(PathUtil.getProjectpath() + pd.getString("PATH")); 	//删除硬盘中的图片
		}
		if(PATH != null){
			eqm_classService.delTp(pd);													//删除数据库中图片数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_class:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			eqm_classService.delete(pd);
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_class:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value="PICTURE_PATH",required=false) MultipartFile file,
			@RequestParam(value="path",required=false) String path,
			@RequestParam(value="FIDENTIFY",required=false) String FIDENTIFY,
			@RequestParam(value="CLASS_NAME",required=false) String CLASS_NAME,
			@RequestParam(value="FDES",required=false) String FDES,
			@RequestParam(value="FSTATE",required=false) String FSTATE,
			@RequestParam(value="EQM_COLOR",required=false) String EQM_COLOR,
			@RequestParam(value="EQM_CLASS_ID",required=false) String EQM_CLASS_ID,
			@RequestParam(value="FCREATOR",required=false) String FCREATOR,
			@RequestParam(value="CREATE_TIME",required=false) String CREATE_TIME,
			@RequestParam(value="EQM_ICON",required=false) String EQM_ICON
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_CLASS_ID", EQM_CLASS_ID);	//主键
		pd.put("FCREATOR",FCREATOR);//创建人
		pd.put("CREATE_TIME", CREATE_TIME);//创建时间
		pd.put("FIDENTIFY", FIDENTIFY);	
		pd.put("CLASS_NAME", CLASS_NAME);	
		pd.put("FDES", FDES);
		pd.put("FSTATE", FSTATE);	
		pd.put("EQM_COLOR", EQM_COLOR);	
		if (null != file && !file.isEmpty()) {
			String  ffile = DateUtil.getDays(), fileName = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("PICTURE_PATH", Const.FILEPATHIMG + ffile + "/" + fileName);				//路径
			pd.put("EQM_ICON", fileName);
		}else{
			pd.put("PICTURE_PATH", path);
			pd.put("EQM_ICON", EQM_ICON);	
		}
		eqm_classService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_class:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String FSTATE = pd.getString("FSTATE");						//状态
		if(Tools.notEmpty(FSTATE))pd.put("FSTATE", FSTATE.trim());
		page.setPd(pd);
		List<PageData> varList = eqm_classService.list(page);	//列出EQM_CLASS列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**设备类下拉列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/classList")
	//@RequiresPermissions("eqm_class:list")
	@ResponseBody
	public Object classList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = eqm_classService.classList(pd);	//列出设备类下拉列表
		map.put("varList", varList);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("eqm_class:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_classService.findById(pd);	//根据ID读取
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
		titles.add("标识");	//1
		titles.add("类名");	//2
		titles.add("描述");	//3
		titles.add("状态");	//4
		titles.add("设备颜色");	//5
		titles.add("设备字体颜色");	//6
		titles.add("设备ICON图");	//7
		titles.add("图片路径");	//8
		titles.add("扩展字段1");	//9
		titles.add("扩展字段2");	//10
		titles.add("扩展字段3");	//11
		titles.add("扩展字段4");	//12
		titles.add("扩展字段5");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_classService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FIDENTIFY"));	    //1
			vpd.put("var2", varOList.get(i).getString("CLASS_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FDES"));	    //3
			vpd.put("var4", varOList.get(i).getString("FSTATE"));	    //4
			vpd.put("var5", varOList.get(i).getString("EQM_COLOR"));	    //5
			vpd.put("var6", varOList.get(i).getString("EQM_FONT_COLOR"));	    //6
			vpd.put("var7", varOList.get(i).getString("EQM_ICON"));	    //7
			vpd.put("var8", varOList.get(i).getString("PICTURE_PATH"));	    //8
			vpd.put("var9", varOList.get(i).getString("FEXTEND1"));	    //9
			vpd.put("var10", varOList.get(i).getString("FEXTEND2"));	    //10
			vpd.put("var11", varOList.get(i).getString("FEXTEND3"));	    //11
			vpd.put("var12", varOList.get(i).getString("FEXTEND4"));	    //12
			vpd.put("var13", varOList.get(i).getString("FEXTEND5"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
