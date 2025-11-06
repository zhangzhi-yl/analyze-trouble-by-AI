package org.yy.controller.mom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.DelFileUtil;
import org.yy.util.FileDownload;
import org.yy.util.FileUpload;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mom.WC_StationService;
import org.yy.service.mom.WH_StorageAreaService;
import org.yy.service.mom.WC_WorkCenterService;

/** 
 * 说明：工作中心管理
 * 作者：YuanYe
 * 时间：2020-01-13
 * 
 */
@Controller
@RequestMapping("/wc_workcenter")
public class WC_WorkCenterController extends BaseController {
	
	@Autowired
	private WC_WorkCenterService wc_workcenterService;
	
	@Autowired
	private WC_StationService wc_stationService;
	
	@Autowired
	private WH_StorageAreaService wh_storageareaService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("wc_workcenter:add")
	@ResponseBody
	public Object add(
			@RequestParam(value="FIMG",required=false) MultipartFile file,
			@RequestParam(value="FNAME",required=false) String FNAME,
			@RequestParam(value="FCODE",required=false) String FCODE,
			@RequestParam(value="FTYPE",required=false) String FTYPE,
			@RequestParam(value="PLINE_ENTITY_ID",required=false) String PLINE_ENTITY_ID,
			@RequestParam(value="AREA_ID",required=false) String AREA_ID,
			@RequestParam(value="FDES",required=false) String FDES
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData num = new PageData();
		pd.put("WC_WORKCENTER_ID", this.get32UUID());	//工作中心ID，主键
		pd.put("FNAME", FNAME);	
		pd.put("FCODE", FCODE);	
		pd.put("FTYPE", FTYPE);	
		pd.put("PLINE_ENTITY_ID", PLINE_ENTITY_ID);
		pd.put("AREA_ID", AREA_ID);
		pd.put("FDES", FDES);
		num = wc_workcenterService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			String  ffile = DateUtil.getDays(), fileName = "";
			if (null != file && !file.isEmpty()) {
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
				pd.put("FIMG", Const.FILEPATHIMG + ffile + "/" + fileName);	
			}
			wc_workcenterService.save(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("wc_workcenter:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if(Integer.parseInt(wc_stationService.findCount(pd).get("zs").toString())>0 || //如果有关联工作站
				Integer.parseInt(wh_storageareaService.findCountByWorkCenter(pd).get("zs").toString())>0) {	//如果有关联库区
			errInfo = "error";
		} else {
			if(Tools.notEmpty(pd.getString("PATH").trim())){
				DelFileUtil.delFolder(PathUtil.getProjectpath()+ pd.getString("PATH")); //删除工作中心图标
			}
			wc_workcenterService.delete(pd);
		}
		map.put("result", errInfo);				//返回结果
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
			wc_workcenterService.delTp(pd);													//删除数据库中图片数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("wc_workcenter:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value="FIMG",required=false) MultipartFile file,
			@RequestParam(value="fimg",required=false) String fimg,
			@RequestParam(value="FNAME",required=false) String FNAME,
			@RequestParam(value="FCODE",required=false) String FCODE,
			@RequestParam(value="FTYPE",required=false) String FTYPE,
			@RequestParam(value="PLINE_ENTITY_ID",required=false) String PLINE_ENTITY_ID,
			@RequestParam(value="AREA_ID",required=false) String AREA_ID,
			@RequestParam(value="FDES",required=false) String FDES
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData num = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", FNAME);
		pd.put("FCODE", FCODE);
		pd.put("FTYPE", FTYPE);
		pd.put("PLINE_ENTITY_ID", PLINE_ENTITY_ID);
		pd.put("AREA_ID", AREA_ID);
		pd.put("FDES", FDES);
		num = wc_workcenterService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			if (null != file && !file.isEmpty()) {
				String  ffile = DateUtil.getDays(), fileName = "";
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
				pd.put("FIMG", Const.FILEPATHIMG + ffile + "/" + fileName);				//路径
			}else{
				pd.put("FIMG", fimg);
			}
			wc_workcenterService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("wc_workcenter:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String FCODE = pd.getString("FCODE");						//代码检索条件
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		String FNAME = pd.getString("FNAME");						//名称检索条件
		if(Tools.notEmpty(FNAME))pd.put("FNAME", FNAME.trim());
		String FTYPE = pd.getString("FTYPE");						//类型检索条件
		if(Tools.notEmpty(FTYPE))pd.put("FTYPE", FTYPE.trim());
		String PLINE_ENTITY_ID = pd.getString("PLINE_ENTITY_ID");						//产线实体ID检索条件
		if(Tools.notEmpty(PLINE_ENTITY_ID))pd.put("PLINE_ENTITY_ID", PLINE_ENTITY_ID.trim());
		String AREA_ID = pd.getString("AREA_ID");						//隶属车间ID检索条件
		if(Tools.notEmpty(AREA_ID))pd.put("AREA_ID", AREA_ID.trim());
		page.setPd(pd);
		List<PageData>	varList = wc_workcenterService.list(page);	//列出WC_WorkCenter列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**工作中心下拉列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/workCenterList")
	//@RequiresPermissions("wc_workcenter:list")
	@ResponseBody
	public Object workCenterList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = wc_workcenterService.workCenterList(pd);	//列出WC_WorkCenter列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 通过FCODE获取数据查询数量
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/findByCode")
//	@RequiresPermissions("wc_workcenter:list")
	@ResponseBody
	public Object findByFCODE(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FCODE = pd.getString("FCODE");						//代码
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		page.setPd(pd);
		List<PageData>	varList = wc_workcenterService.findByFCODE(page);	//列出列表
		map.put("num", varList.size());
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("wc_workcenter:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = wc_workcenterService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("wc_workcenter:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			wc_workcenterService.deleteAll(ArrayDATA_IDS);
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
		titles.add("代码");	//1
		titles.add("名称");	//2
		titles.add("类型");	//3
		titles.add("描述");	//4
		titles.add("隶属产线");	//5
		titles.add("隶属车间");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = wc_workcenterService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FCODE"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FDES"));	    //4
			vpd.put("var5", varOList.get(i).getString("PLINE_ENTITY_NAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("AREA_NAME"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	//@RequiresPermissions("fromExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData wcpd = new PageData();
		if (null != file && !file.isEmpty()) {
	        int realRowCount = 0;//真正有数据的行数
	        //得到工作空间
	        Workbook workbook = null;
	        try {
	            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        //得到sheet
	        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, 0);
	        realRowCount = sheet.getPhysicalNumberOfRows();
	        for(int rowNum = 1;rowNum <= realRowCount;rowNum++) {
	        	Row row = sheet.getRow(rowNum);
	            if(ObjectExcelRead.isBlankRow(row)) {	//空行跳过
	            	break;
	            }
	            if(row.getRowNum() == -1) {
	                continue;
	            }else {
	                if(row.getRowNum() == 0) {//第一行表头跳过
	                    continue;
	                }
	            }
	            wcpd.put("WC_WORKCENTER_ID", this.get32UUID());
	            wcpd.put("FCODE", ObjectExcelRead.getCellValue(sheet, row, 0));
	            wcpd.put("FNAME", ObjectExcelRead.getCellValue(sheet, row, 1));
	            wcpd.put("FDES", ObjectExcelRead.getCellValue(sheet, row, 2));
	            wcpd.put("FTYPE", "");
	            wcpd.put("FIMG", "");
	            wcpd.put("PLINE_ENTITY_ID", "");
	            wcpd.put("AREA_ID", "");
	            wc_workcenterService.save(wcpd);
	        }
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "workcenter.xlsx", "workcenter.xlsx");
	}
}
