package org.yy.controller.system;

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
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.system.LOGOService;

/** 
 * 说明：LOGO管理
 * 作者：YuanYes QQ356703572
 * 时间：2021-09-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/logo")
public class LOGOController extends BaseController {
	
	@Autowired
	private LOGOService logoService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("logo:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("LOGO_ID", this.get32UUID());	//主键
		logoService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("logo:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		logoService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/*
	*//**修改
	 * @param
	 * @throws Exception
	 *//*
	@RequestMapping(value="/edit")
	@RequiresPermissions("logo:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		logoService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	*/
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("logo:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = logoService.list(page);	//列出LOGO列表
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
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = logoService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("logo:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			logoService.deleteAll(ArrayDATA_IDS);
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
		titles.add("登录背景图路径");	//1
		titles.add("登录LOGO图片路径");	//2
		titles.add("登录二维码图片路径");	//3
		titles.add("菜单LOGO图片路径");	//4
		titles.add("导航名称");	//5
		titles.add("菜单名称");	//6
		titles.add("登录LOGO宽度");	//7
		titles.add("登录LOGO高度");	//8
		titles.add("版权信息");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = logoService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PHOTO_BACKGROUND"));	    //1
			vpd.put("var2", varOList.get(i).getString("PHOTO_LOGIN_LOGO"));	    //2
			vpd.put("var3", varOList.get(i).getString("PHOTO_CODE"));	    //3
			vpd.put("var4", varOList.get(i).getString("PHOTO_MENU_LOGO"));	    //4
			vpd.put("var5", varOList.get(i).getString("NAVIGATION_NAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("MENU_NAME"));	    //6
			vpd.put("var7", varOList.get(i).getString("PHOTO_LOGIN_LOGO_WIDTH"));	    //7
			vpd.put("var8", varOList.get(i).getString("PHOTO_LOGIN_LOGO_HEIGHT"));	    //8
			vpd.put("var9", varOList.get(i).getString("COPYRIGHT"));	    //9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**v1 管悦 20210915 LOGO管理-修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit(@RequestParam(value = "FFILEPATH1", required = false) MultipartFile FFILEPATH1,@RequestParam(value = "FFILEPATH2", required = false) MultipartFile FFILEPATH2,@RequestParam(value = "FFILEPATH3", required = false) MultipartFile FFILEPATH3,@RequestParam(value = "FFILEPATH4", required = false) MultipartFile FFILEPATH4) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		pd = this.getPageData();
		String PHOTO_BACKGROUND = "";
		String PHOTO_LOGIN_LOGO = "";
		String PHOTO_CODE = "";
		String PHOTO_MENU_LOGO = "";
		//登录背景图片上传
		if (null != FFILEPATH1 && !FFILEPATH1.isEmpty()) {
			String FFILENAME = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILELOGO + ffile; // 文件上传路径
			//String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			//String filenametype = pd.getString("FFILENAME").substring(pd.getString("FFILENAME").indexOf("."),pd.getString("FFILENAME").length());
			FFILENAME = FileUpload.fileUp(FFILEPATH1, filePath, "登录背景图片"+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILELOGO +DateUtil.getDays()+"/"+FFILENAME;
			pd.put("PHOTO_BACKGROUND",FPFFILEPATH);
			PHOTO_BACKGROUND = FPFFILEPATH;
		}
		//登录logo上传
		if (null != FFILEPATH2 && !FFILEPATH2.isEmpty()) {
			String FFILENAME = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILELOGO + ffile; // 文件上传路径
			//String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			//String filenametype = pd.getString("FFILENAME").substring(pd.getString("FFILENAME").indexOf("."),pd.getString("FFILENAME").length());
			FFILENAME = FileUpload.fileUp(FFILEPATH2, filePath, "登录logo"+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILELOGO +DateUtil.getDays()+"/"+FFILENAME;
			pd.put("PHOTO_LOGIN_LOGO",FPFFILEPATH);
			PHOTO_LOGIN_LOGO = FPFFILEPATH;
		}
		//登录二维码上传
		if (null != FFILEPATH3 && !FFILEPATH3.isEmpty()) {
			String FFILENAME = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILELOGO + ffile; // 文件上传路径
			//String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			//String filenametype = pd.getString("FFILENAME").substring(pd.getString("FFILENAME").indexOf("."),pd.getString("FFILENAME").length());
			FFILENAME = FileUpload.fileUp(FFILEPATH3, filePath, "登录二维码"+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILELOGO +DateUtil.getDays()+"/"+FFILENAME;
			pd.put("PHOTO_CODE",FPFFILEPATH);
			PHOTO_CODE = FPFFILEPATH;
		}
		//菜单logo上传
		if (null != FFILEPATH4 && !FFILEPATH4.isEmpty()) {
			String FFILENAME = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILELOGO + ffile; // 文件上传路径
			//String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			//String filenametype = pd.getString("FFILENAME").substring(pd.getString("FFILENAME").indexOf("."),pd.getString("FFILENAME").length());
			FFILENAME = FileUpload.fileUp(FFILEPATH4, filePath, "菜单logo"+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILELOGO +DateUtil.getDays()+"/"+FFILENAME;
			pd.put("PHOTO_MENU_LOGO",FPFFILEPATH);
			PHOTO_MENU_LOGO = FPFFILEPATH;
		}
		logoService.edit(pd);
		map.put("pd",pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}

}
