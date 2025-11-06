package org.yy.controller.wt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
import org.yy.util.*;
import org.yy.entity.PageData;
import org.yy.service.wt.QuestionbankMXenclosuresService;

import javax.servlet.http.HttpServletResponse;

/** 
 * 说明：问题库明细附件
 * 作者：YuanYes QQ356703572
 * 时间：2021-09-03
 * 官网：356703572@qq.com
 */
@Controller
	@RequestMapping("/questionbankmxenclosures")
public class QuestionbankMXenclosuresController extends BaseController {
	
	@Autowired
	private QuestionbankMXenclosuresService questionbankmxenclosuresService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("QUESTIONBANKMXENCLOSURE_ID", this.get32UUID());	//主键
		questionbankmxenclosuresService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		questionbankmxenclosuresService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	@RequestMapping(value="/Downland")
	public void downExcel(HttpServletResponse response)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = questionbankmxenclosuresService.findById(pd);   //根据ID读取
		String FFILEPATH = pd.getString("PATH"); //文件路径
		String FFILENAME = pd.getString("NAME"); //文件名称
		System.out.println(PathUtil.getProjectpath()+FFILEPATH);
		FileDownload.fileDownload(response,PathUtil.getProjectpath()+FFILEPATH,FFILENAME);
	}
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/upload")
	@ResponseBody
	public Object add(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		pd = this.getPageData();
		String FFILENAME = "";
		String type = "";
		if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("FFILENAME").substring(0, pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
			String filenametype = pd.getString("FFILENAME").substring(pd.getString("FFILENAME").indexOf("."),pd.getString("FFILENAME").length());
			FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal+dateName);// 执行上传
			String FPFFILEPATH = Const.FILEPATHFILE +DateUtil.getDays()+"/"+FFILENAME;
			pd.put("TYPE",filenametype);
			pd.put("PATH",FPFFILEPATH);
			type = FPFFILEPATH;
		}
		pd.put("NAME",FFILENAME);
		pd.put("QUESTIONBANKMXENCLOSURE_ID",this.get32UUID());
		pd.put("TIME",Tools.date2Str(new Date()));
		questionbankmxenclosuresService.save(pd);
		map.put("type",type);
		map.put("result", errInfo);				//返回结果
		return map;
	}


	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		questionbankmxenclosuresService.edit(pd);
		map.put("result", errInfo);
		return map;
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
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = questionbankmxenclosuresService.list(page);	//列出QuestionbankMXenclosures列表
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
		pd = questionbankmxenclosuresService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			questionbankmxenclosuresService.deleteAll(ArrayDATA_IDS);
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
		titles.add("从表id");	//1
		titles.add("附件名称");	//2
		titles.add("附件类型");	//3
		titles.add("附件路径");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = questionbankmxenclosuresService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("QUESTIONBANKMX_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("TYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("PATH"));	    //4
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
