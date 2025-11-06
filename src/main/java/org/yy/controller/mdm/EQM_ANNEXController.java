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
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.mdm.EQM_ANNEXService;

/** 
 * 说明：设备附件
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_annex")
public class EQM_ANNEXController extends BaseController {
	
	@Autowired
	private EQM_ANNEXService eqm_annexService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private AttachmentSetService attachmentsetService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_annex:add")
	@ResponseBody
	public Object add(
			@RequestParam(value="FPATH",required=false) MultipartFile file,
			@RequestParam(value="EQM_BASE_ID",required=false) String EQM_BASE_ID,
			@RequestParam(value="FCODE",required=false) String FCODE,
			@RequestParam(value="FTYPE",required=false) String FTYPE,
			@RequestParam(value="FDES",required=false) String FDES,
			@RequestParam(value="FNAME",required=false) String FNAME,
			@RequestParam(value="FKIND",required=false) String FKIND
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pd1 = new PageData();
		PageData pdOp = new PageData();
		String  ffile = DateUtil.getDays(), fileName = "";
		String EQM_ANNEX_ID = this.get32UUID();
		boolean flag = false;
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = staffService.getStaffId(pdOp).getString("STAFF_ID");//操作人
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			
			pd1.put("FUrl",Const.FILEPATHIMG + ffile + "/" + fileName);	//附件路径
			pd1.put("FName", FNAME);	//附件名称
			pd1.put("DataSources", "设备信息附件");	//数据源
			pd1.put("AssociationIDTable", "MDM_EQM_ANNEX");	//数据源表
			pd1.put("AssociationID", EQM_ANNEX_ID);	//数据源ID
			pd1.put("FExplanation", pd.getString("FDES"));	//备注
			pd1.put("FCreatePersonID", STAFF_ID);	//创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date()));	//创建时间
			flag = attachmentsetService.check(pd1);
			if(flag == true){
				pd.put("EQM_ANNEX_ID", EQM_ANNEX_ID);	//主键
				pd.put("FCREATOR", Jurisdiction.getName());//创建人
				pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
				//pd.put("FPATH", Const.FILEPATHIMG + ffile + "/" + fileName);	
				//pd.put("FNAME", FNAME);
				pd.put("EQM_BASE_ID", EQM_BASE_ID);	
				pd.put("FCODE", FCODE);	
				pd.put("FTYPE", FTYPE);	
				pd.put("FDES", FDES);	
				pd.put("FKIND", FKIND);	
				eqm_annexService.save(pd);
				map.put("result", errInfo);
			}else{
				errInfo = "error";
				String msg = "请选择上传文件！";
				map.put("result", errInfo);
				map.put("msg", msg);
			}
		}
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
			eqm_annexService.delFj(pd);													//删除数据库中附件数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_annex:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_annexService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_annex:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value="FPATH",required=false) MultipartFile file,
			@RequestParam(value="path",required=false) String path,
			@RequestParam(value="EQM_ANNEX_ID",required=false) String EQM_ANNEX_ID,
			@RequestParam(value="EQM_BASE_ID",required=false) String EQM_BASE_ID,
			@RequestParam(value="FNAME",required=false) String FNAME,
			@RequestParam(value="FCREATOR",required=false) String FCREATOR,
			@RequestParam(value="CREATE_TIME",required=false) String CREATE_TIME,
			@RequestParam(value="FKIND",required=false) String FKIND,
			@RequestParam(value="FTYPE",required=false) String FTYPE,
			@RequestParam(value="FDES",required=false) String FDES,
			@RequestParam(value="FCODE",required=false) String FCODE
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pd1 = new PageData();
		PageData pdOp = new PageData();
		pd = this.getPageData();
		
		pd.put("EQM_ANNEX_ID", EQM_ANNEX_ID);	//主键
		pd.put("FCREATOR",FCREATOR);//创建人
		pd.put("CREATE_TIME", CREATE_TIME);//创建时间
		pd.put("EQM_BASE_ID", EQM_BASE_ID);	
		pd.put("FDES", FDES);	
		pd.put("FCODE", FCODE);
		pd.put("FKIND", FKIND);
		pd.put("FTYPE", FTYPE);
		pd.put("FNAME", FNAME);
		
		String name = Jurisdiction.getName();
		pdOp.put("FNAME", name);
		String STAFF_ID = staffService.getStaffId(pdOp).getString("STAFF_ID");//操作人
		boolean flag = false;
		
		if (null != file && !file.isEmpty()) {
			String  ffile = DateUtil.getDays(), fileName = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("FPATH", Const.FILEPATHIMG + ffile + "/" + fileName);				//路径
			
			pd1.put("FUrl",Const.FILEPATHIMG + ffile + "/" + fileName);	//附件路径
			pd1.put("FName", FNAME);	//附件名称
			pd1.put("DataSources", "设备信息附件");	//数据源
			pd1.put("AssociationIDTable", "MDM_EQM_ANNEX");	//数据源表
			pd1.put("AssociationID", EQM_ANNEX_ID);	//数据源ID
			pd1.put("FExplanation", FDES);	//备注
			pd1.put("FCreatePersonID", STAFF_ID);	//创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date()));	//创建时间
			flag = attachmentsetService.check(pd1);
			
			if(flag == true){
				eqm_annexService.edit(pd);
				map.put("result", errInfo);				//返回结果
			}else{
				errInfo = "error";
				String msg = "请选择上传文件！";
				map.put("result", errInfo);
				map.put("msg", msg);
			}
		}else{
			
			pd1.put("FUrl",path);	//附件路径
			pd1.put("FName", FNAME);	//附件名称
			pd1.put("DataSources", "设备信息附件");	//数据源
			pd1.put("AssociationIDTable", "MDM_EQM_ANNEX");	//数据源表
			pd1.put("AssociationID", EQM_ANNEX_ID);	//数据源ID
			pd1.put("FExplanation", FDES);	//备注
			pd1.put("FCreatePersonID", STAFF_ID);	//创建人ID
			pd1.put("FCreateTime", Tools.date2Str(new Date()));	//创建时间
			flag = attachmentsetService.check(pd1);
			
			if(flag == true){
				eqm_annexService.edit(pd);
				map.put("result", errInfo);				//返回结果
			}else{
				errInfo = "error";
				String msg = "请选择上传文件！";
				map.put("result", errInfo);
				map.put("msg", msg);
			}
		}
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_annex:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_annexService.list(page);	//列出EQM_ANNEX列表
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
	//@RequiresPermissions("eqm_annex:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_annexService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_annex:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_annexService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备附件ID");	//1
		titles.add("代码");	//2
		titles.add("名称");	//3
		titles.add("路径");	//4
		titles.add("种类");	//5
		titles.add("类型");	//6
		titles.add("描述");	//7
		titles.add("扩展字段1");	//8
		titles.add("扩展字段2");	//9
		titles.add("扩展字段3");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_annexService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_ANNEX_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FPATH"));	    //4
			vpd.put("var5", varOList.get(i).getString("FKIND"));	    //5
			vpd.put("var6", varOList.get(i).getString("FTYPE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FDES"));	    //7
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
