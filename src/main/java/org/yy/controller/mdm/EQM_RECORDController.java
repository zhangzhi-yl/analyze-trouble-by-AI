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
import org.yy.service.mdm.EQM_RECORDService;

/** 
 * 说明：设备档案
 * 作者：YuanYe
 * 时间：2020-02-17
 * 
 */
@Controller
@RequestMapping("/eqm_record")
public class EQM_RECORDController extends BaseController {
	
	@Autowired
	private EQM_RECORDService eqm_recordService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_record:add")
	@ResponseBody
	public Object add(
			@RequestParam(value="FFILE_PATH",required=false) MultipartFile file,
			@RequestParam(value="EQM_BASE_ID",required=false) String EQM_BASE_ID,
			@RequestParam(value="FCOPIES",required=false) String FCOPIES,
			@RequestParam(value="FDEPOSITARY",required=false) String FDEPOSITARY,
			@RequestParam(value="FFILE_NAME",required=false) String FFILE_NAME
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("EQM_RECORD_ID", this.get32UUID());	//主键
			pd.put("FCREATOR",Jurisdiction.getName());//创建人
			pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
			pd.put("FFILE_PATH", Const.FILEPATHIMG + ffile + "/" + fileName);	
			pd.put("FFILE_NAME", FFILE_NAME);
			pd.put("EQM_BASE_ID", EQM_BASE_ID);	
			pd.put("FCOPIES", FCOPIES);	
			pd.put("FDEPOSITARY", FDEPOSITARY);	
			
			eqm_recordService.save(pd);
		}
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
			eqm_recordService.delFj(pd);													//删除数据库中附件数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_record:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_recordService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_record:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value="FFILE_PATH",required=false) MultipartFile file,
			@RequestParam(value="path",required=false) String path,
			@RequestParam(value="EQM_RECORD_ID",required=false) String EQM_RECORD_ID,
			@RequestParam(value="EQM_BASE_ID",required=false) String EQM_BASE_ID,
			@RequestParam(value="FCREATOR",required=false) String FCREATOR,
			@RequestParam(value="CREATE_TIME",required=false) String CREATE_TIME,
			@RequestParam(value="FCOPIES",required=false) String FCOPIES,
			@RequestParam(value="FDEPOSITARY",required=false) String FDEPOSITARY,
			@RequestParam(value="FFILE_NAME",required=false) String FFILE_NAME
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_RECORD_ID", EQM_RECORD_ID);	//主键
		pd.put("FCREATOR",FCREATOR);//创建人
		pd.put("CREATE_TIME", CREATE_TIME);//创建时间
		pd.put("EQM_BASE_ID", EQM_BASE_ID);	
		pd.put("FCOPIES", FCOPIES);	
		pd.put("FDEPOSITARY", FDEPOSITARY);
		pd.put("FFILE_NAME", FFILE_NAME);
		
		if (null != file && !file.isEmpty()) {
			String  ffile = DateUtil.getDays(), fileName = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("FFILE_PATH", Const.FILEPATHIMG + ffile + "/" + fileName);				//路径
		}else{
			pd.put("FFILE_PATH", path);
		}
		eqm_recordService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_record:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_recordService.list(page);	//列出EQM_RECORD列表
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
	//@RequiresPermissions("eqm_record:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_recordService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_record:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_recordService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备基础资料ID");	//1
		titles.add("资料名称");	//2
		titles.add("资料路径");	//3
		titles.add("份数");	//4
		titles.add("存放处");	//5
		titles.add("创建人");	//6
		titles.add("创建时间");	//7
		titles.add("扩展字段1");	//8
		titles.add("扩展字段2");	//9
		titles.add("扩展字段3");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_recordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_BASE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FFILE_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FFILE_PATH"));	    //3
			vpd.put("var4", varOList.get(i).get("FCOPIES").toString());	//4
			vpd.put("var5", varOList.get(i).getString("FDEPOSITARY"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCREATOR"));	    //6
			vpd.put("var7", varOList.get(i).getString("CREATE_TIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("FEXTEND1"));	    //8
			vpd.put("var9", varOList.get(i).getString("FEXTEND2"));	    //9
			vpd.put("var10", varOList.get(i).getString("FEXTEND3"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
