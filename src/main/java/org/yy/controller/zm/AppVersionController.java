package org.yy.controller.zm;

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
import org.yy.service.zm.AppVersionService;
import org.yy.service.zm.SceneTimeService;
import org.yy.util.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/** 
 * 说明：app版本日期
 * 作者：YuanYe
 * 时间：2021-10-13
 * 
 */
@Controller
@RequestMapping("/appVersion")
public class AppVersionController extends BaseController {
	
	@Autowired
	private AppVersionService appVersionService;

	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();

		String type = pd.getString("type");

		String version = pd.get("version").toString();
		String fileName = pd.getString("fileName").substring(0, pd.getString("fileName").indexOf("."));
		String ffile = DateUtil.getDays() + "/" + "v"+version;
		if (null != file && !file.isEmpty()) {
			// 文件上传路径
			String filePath = PathUtil.getProjectpath() + Const.APPFILEPATHFILE + ffile;
			// 执行上传
			fileName = FileUpload.fileUp(file, filePath, fileName+"_v"+version);
			// 文件上传路径
			String path = Const.APPFILEPATHFILE +DateUtil.getDays()+"/"+"v"+version+"/"+fileName;
			if("整包更新".equals(type)){
				pd.put("PATH",path);
			}else {
				pd.put("WGT_PATH",path);
			}

		}
		pd.put("FTYPE",type);
		pd.put("FILENAME",fileName);
		pd.put("FDATE",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		pd.put("VERSION",version);
		pd.put("APPVERSION_ID","1");
		appVersionService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**下载APP
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/download")
	public void downExcel(HttpServletResponse response)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		FileDownload.fileDownload(response, PathUtil.getProjectpath() +  pd.getString("path") , pd.getString("fileName"));
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = appVersionService.list(page);
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
		pd.put("APPVERSION_ID","1");
		pd = appVersionService.findById(pd);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	

}
