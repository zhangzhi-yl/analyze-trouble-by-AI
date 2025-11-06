package org.yy.controller.mdm;

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
import org.yy.service.mdm.REPAIR_ANNEXService;

/** 
 * 说明：报修工单(附件)
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/repair_annex")
public class REPAIR_ANNEXController extends BaseController {
	
	@Autowired
	private REPAIR_ANNEXService repair_annexService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(@RequestParam(value="FPATH",required=false) MultipartFile file,
			@RequestParam(value="FNAME",required=false) String FNAME,
			@RequestParam(value="REPAIR_WORKORDER_ID",required=false) String REPAIR_WORKORDER_ID) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if (null != file && !file.isEmpty()) {
			String  ffile = DateUtil.getDays(), fileName = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("FPATH", Const.FILEPATHFILE + ffile + "/" + fileName);				//路径
		}
		pd.put("REPAIR_ANNEX_ID", this.get32UUID());	//主键
		pd.put("FNAME", FNAME);
		pd.put("REPAIR_WORKORDER_ID", REPAIR_WORKORDER_ID);
		pd.put("FCREATER", Jurisdiction.getName());	//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
		repair_annexService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除文件和数据
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteFile")
	@ResponseBody
	public Object deleteFile() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			if(Tools.notEmpty(pd.getString("FPATH").trim())){
				DelFileUtil.delFolder(PathUtil.getProjectpath()+ pd.getString("FPATH")); //删除文件
			}
			repair_annexService.delete(pd);
		}catch(Exception e) {
			errInfo="error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		repair_annexService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**清空文件字段，删除文件
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/clearFile")
	@ResponseBody
	public Object clearFile() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			if(Tools.notEmpty(pd.getString("FPATH").trim())){
				DelFileUtil.delFolder(PathUtil.getProjectpath()+ pd.getString("FPATH")); //删除文件
			}
			pd.put("FNAME", "");
			pd.put("FPATH", "");
			repair_annexService.edit(pd);
		}catch(Exception e) {
			errInfo="error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit(@RequestParam(value="FPATH",required=false) MultipartFile file,
			@RequestParam(value="FNAME",required=false) String FNAME,
			@RequestParam(value="REPAIR_WORKORDER_ID",required=false) String REPAIR_WORKORDER_ID,
			@RequestParam(value="REPAIR_ANNEX_ID",required=false) String REPAIR_ANNEX_ID,
			@RequestParam(value="FCREATER",required=false) String FCREATER,
			@RequestParam(value="CREATE_TIME",required=false) String CREATE_TIME) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if (null != file && !file.isEmpty()) {
			String  ffile = DateUtil.getDays(), fileName = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("FPATH", Const.FILEPATHFILE + ffile + "/" + fileName);				//路径
		}
		pd.put("REPAIR_ANNEX_ID", REPAIR_ANNEX_ID);	//主键
		pd.put("FNAME", FNAME);
		pd.put("REPAIR_WORKORDER_ID", REPAIR_WORKORDER_ID);
		pd.put("FCREATER", FCREATER);	//创建人
		pd.put("CREATE_TIME", CREATE_TIME);	//创建时间
		repair_annexService.edit(pd);
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
		List<PageData>	varList = repair_annexService.list(page);	//列出REPAIR_WORKORDERMx列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);									//返回结果
		return map;
	}
	
	 /**去修改页面
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
		pd = repair_annexService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
			repair_annexService.deleteAll(ArrayDATA_IDS);
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
		titles.add("附件名称");	//1
		titles.add("附件路径");	//2
		titles.add("创建人");	//3
		titles.add("创建时间");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = repair_annexService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("FPATH"));	    //2
			vpd.put("var3", varOList.get(i).getString("FCREATER"));	    //3
			vpd.put("var4", varOList.get(i).getString("CREATE_TIME"));	    //4
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
