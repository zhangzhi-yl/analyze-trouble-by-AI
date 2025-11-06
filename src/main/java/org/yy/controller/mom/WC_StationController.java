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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mom.WC_StationService;
import org.yy.service.system.UsersService;

/** 
 * 说明：工作站管理(工位)
 * 作者：YuanYe
 * 时间：2020-01-13
 * 
 */
@Controller
@RequestMapping("/wc_station")
public class WC_StationController extends BaseController {
	
	@Autowired
	private WC_StationService wc_stationService;
	@Autowired
    private UsersService usersService;//用户
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("wc_station:add")
	@ResponseBody
	public Object add(
			@RequestParam(value="FIMG",required=false) MultipartFile file,
			@RequestParam(value="FNAME",required=false) String FNAME,
			@RequestParam(value="FCODE",required=false) String FCODE,
			@RequestParam(value="FTYPE",required=false) String FTYPE,
			@RequestParam(value="FWORKCENTER",required=false) String FWORKCENTER,
			@RequestParam(value="FDES",required=false) String FDES,
			@RequestParam(value="EQM_BASE_ID",required=false) String EQM_BASE_ID,	
			@RequestParam(value="MULTI_TASK_STATION",required=false) String MULTI_TASK_STATION,	
			@RequestParam(value="STATION_GROUP",required=false) String STATION_GROUP,	
			@RequestParam(value="QR_CODE",required=false) String QR_CODE,
			@RequestParam(value="ConsumptionWarehouse",required=false) String ConsumptionWarehouse,
			@RequestParam(value="ConsumptionLocation",required=false) String ConsumptionLocation,
			@RequestParam(value="ProduceWarehouse",required=false) String ProduceWarehouse,
			@RequestParam(value="ProduceLocation",required=false) String ProduceLocation
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData num = new PageData();
		pd.put("WC_STATION_ID", this.get32UUID());	//工位ID，主键
		pd.put("FINSTRUCTOR_PATH", "");
		pd.put("FINSTRUCTOR_NAME", "");
		pd.put("FNAME", FNAME);	
		pd.put("FCODE", FCODE);	
		pd.put("FTYPE", FTYPE);	
		pd.put("WC_WORKCENTER_ID", FWORKCENTER);
		pd.put("FDES", FDES);	
		pd.put("EQM_BASE_ID", EQM_BASE_ID);
		pd.put("MULTI_TASK_STATION", MULTI_TASK_STATION);
		pd.put("STATION_GROUP", STATION_GROUP);
		pd.put("QR_CODE", QR_CODE);
		pd.put("ConsumptionWarehouse", ConsumptionWarehouse);
		pd.put("ConsumptionLocation", ConsumptionLocation);
		pd.put("ProduceWarehouse", ProduceWarehouse);
		pd.put("ProduceLocation", ProduceLocation);
		num = wc_stationService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			String  ffile = DateUtil.getDays(), fileName = "";
			if (null != file && !file.isEmpty()) {
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
				pd.put("FIMG", Const.FILEPATHIMG + ffile + "/" + fileName);	
			}
			wc_stationService.save(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("wc_station:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if(Tools.notEmpty(pd.getString("FIMG").trim())){
			DelFileUtil.delFolder(PathUtil.getProjectpath()+ pd.getString("FIMG")); //删除图标
		}
		if(Tools.notEmpty(pd.getString("PATH").trim())){
			DelFileUtil.delFolder(PathUtil.getProjectpath()+ pd.getString("PATH")); //删除文件
		}
		wc_stationService.delete(pd);
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
			wc_stationService.delTp(pd);													//删除数据库中图片数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除文件
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delFile")
	@ResponseBody
	public Object delFile() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PATH = pd.getString("PATH");	
		if(Tools.notEmpty(pd.getString("PATH").trim())){								//文件路径
			DelFileUtil.delFolder(PathUtil.getProjectpath() + pd.getString("PATH")); 	//删除硬盘中的文件
		}
		if(PATH != null){
			wc_stationService.delFile(pd);													//删除数据库中指导书数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("wc_station:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value="FIMG",required=false) MultipartFile FIMG,
			@RequestParam(value="fimg",required=false) String fimg,
			@RequestParam(value="FINSTRUCTOR_PATH",required=false) MultipartFile file,
			@RequestParam(value="FINSTRUCTOR_NAME",required=false) String FINSTRUCTOR_NAME,
			@RequestParam(value="FINSTRUCTORPATH",required=false) String FINSTRUCTORPATH,
			@RequestParam(value="FINSTRUCTORNAME",required=false) String FINSTRUCTORNAME,
			@RequestParam(value="FNAME",required=false) String FNAME,
			@RequestParam(value="FCODE",required=false) String FCODE,
			@RequestParam(value="FTYPE",required=false) String FTYPE,
			@RequestParam(value="FWORKCENTER",required=false) String FWORKCENTER,
			@RequestParam(value="FDES",required=false) String FDES,
			@RequestParam(value="EQM_BASE_ID",required=false) String EQM_BASE_ID,	
			@RequestParam(value="MULTI_TASK_STATION",required=false) String MULTI_TASK_STATION,	
			@RequestParam(value="STATION_GROUP",required=false) String STATION_GROUP,	
			@RequestParam(value="QR_CODE",required=false) String QR_CODE
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData num = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", FNAME);
		pd.put("FCODE", FCODE);
		pd.put("FTYPE", FTYPE);
		pd.put("FINSTRUCTOR_PATH", FINSTRUCTORPATH);
		pd.put("FINSTRUCTOR_NAME", FINSTRUCTORNAME);
		pd.put("WC_WORKCENTER_ID", FWORKCENTER);
		pd.put("FDES", FDES);
		pd.put("EQM_BASE_ID", EQM_BASE_ID);
		pd.put("MULTI_TASK_STATION", MULTI_TASK_STATION);
		pd.put("STATION_GROUP", STATION_GROUP);
		pd.put("QR_CODE", QR_CODE);
		num = wc_stationService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			if (null != FIMG && !FIMG.isEmpty()) {
				String  ffile = DateUtil.getDays(), fileName = "";
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
				fileName = FileUpload.fileUp(FIMG, filePath, this.get32UUID());				//执行上传
				pd.put("FIMG", Const.FILEPATHIMG + ffile + "/" + fileName);				//路径
			}else{
				pd.put("FIMG", fimg);
			}
			wc_stationService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**修改作业指导书字段
	 * @param file
	 * @param FINSTRUCTOR_NAME
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editFile")
	//@RequiresPermissions("wc_station:editFile")
	@ResponseBody
	public Object editFile(
			@RequestParam(value="FINSTRUCTOR_PATH",required=false) MultipartFile file,
			@RequestParam(value="FINSTRUCTOR_NAME",required=false) String FINSTRUCTOR_NAME
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FINSTRUCTOR_NAME", FINSTRUCTOR_NAME);
		if (null != file && !file.isEmpty()) {
			String  ffile = DateUtil.getDays(), fileName = "";
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("FINSTRUCTOR_PATH", Const.FILEPATHFILE + ffile + "/" + fileName);				//路径
		}
		wc_stationService.editFile(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("wc_station:list")
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
		String WC_WORKCENTER_ID = pd.getString("WC_WORKCENTER_ID");						//工作中心ID检索条件
		if(Tools.notEmpty(WC_WORKCENTER_ID))pd.put("WC_WORKCENTER_ID", WC_WORKCENTER_ID.trim());
		page.setPd(pd);
		List<PageData>	varList = wc_stationService.list(page);	//列出WC_Station列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**全部列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("wc_station:listAll")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = wc_stationService.listAll(pd);	//列出列表
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
//	@RequiresPermissions("wc_station:list")
	@ResponseBody
	public Object findByFCODE(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FCODE = pd.getString("FCODE");						//代码
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		page.setPd(pd);
		List<PageData>	varList = wc_stationService.findByFCODE(page);	//列出列表
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
	//@RequiresPermissions("wc_station:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = wc_stationService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	
	 	/**获取工作站全部列表
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/getStation")
		@ResponseBody
		public Object getStation() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData>	varList = wc_stationService.listAll(pd);	//列出列表
			map.put("varList", varList);
			map.put("result", errInfo);
			return map;
		}
	
	 /**获取附件路径
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/getPath")
		@ResponseBody
		public Object getPath() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData userPd = new PageData();
			pd = this.getPageData();
			try {
				pd.put("USERNAME", Jurisdiction.getUsername());
				userPd=usersService.findByStation(pd);
				if(null!=pd.getString("KEYWORDS")&&!"".equals(pd.getString("KEYWORDS"))) {//判断是否有参数
					pd = wc_stationService.findByPath(pd);	//获取附件路径
					if("".equals(pd.getString("FINSTRUCTOR_PATH"))) {
						pd.put("FINSTRUCTOR_PATH", "无");
					}
				}else if(null!=userPd.getString("FSTATION_CODE")&&!"".equals(userPd.getString("FSTATION_CODE"))){
					pd.put("KEYWORDS", userPd.getString("FSTATION_CODE"));
					pd = wc_stationService.findByPath(pd);	//获取附件路径
					if("".equals(pd.getString("FINSTRUCTOR_PATH"))) {
						pd.put("FINSTRUCTOR_PATH", "无");
					}
				}else {
					pd.put("FINSTRUCTOR_PATH", "无");
				}
			} catch(Exception e){
				pd.put("FINSTRUCTOR_PATH", "无");
			}
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}
	
		
		 /**去切换工作站页面获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goCut")
		//@RequiresPermissions("wc_station:edit")
		@ResponseBody
		public Object goCut() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("USERNAME", Jurisdiction.getUsername());
			pd=usersService.findByStation(pd);
			pd.put("FUSERNAME", Jurisdiction.getUsername());//当前用户
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}	
		
		/**切换工作站
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/editStation")
		//@RequiresPermissions("wc_station:edit")
		@ResponseBody
		public Object editStation() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("USERNAME", Jurisdiction.getUsername());
			if(null!=pd.getString("USERNAME")&&!"".equals(pd.getString("USERNAME"))) {
				usersService.editStation(pd);
				Jurisdiction.getSession().removeAttribute(Const.SESSION_STCODE);
				Jurisdiction.getSession().removeAttribute(Const.SESSION_STNAME);
				Jurisdiction.getSession().setAttribute(Const.SESSION_STCODE, pd.getString("FSTATION_CODE"));
				Jurisdiction.getSession().setAttribute(Const.SESSION_STNAME, pd.getString("FSTATION_NAME"));
				
			}
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}	
		@RequestMapping(value="/goStationID")
		@ResponseBody
		public Object goStationID() throws Exception{
			PageData pd = new PageData();
			pd.put("USERNAME", Jurisdiction.getUsername());//当前操作用户
			pd=usersService.findByStation(pd);
			
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			if(null!=pd.getString("FSTATION_CODE")&&!"".equals(pd.getString("FSTATION_CODE"))) {
				map.put("stcode", pd.getString("FSTATION_CODE"));
				map.put("stname", pd.getString("FSTATION_NAME"));
			}else {
				map.put("stcode","NULL");
				map.put("stname", "NULL");
			}
			map.put("result", errInfo);
			return map;
		}
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("wc_station:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			wc_stationService.deleteAll(ArrayDATA_IDS);
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
		titles.add("所属工作中心");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = wc_stationService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FCODE"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FDES"));	    //4
			vpd.put("var5", varOList.get(i).getString("WC_WORKCENTER_NAME"));	    //5
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
		PageData stationpd = new PageData();
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
	            stationpd.put("WC_STATION_ID", this.get32UUID());
	            stationpd.put("FCODE", ObjectExcelRead.getCellValue(sheet, row, 0));
	            stationpd.put("FNAME", ObjectExcelRead.getCellValue(sheet, row, 1));
	            stationpd.put("FDES", ObjectExcelRead.getCellValue(sheet, row, 2));
	            stationpd.put("FTYPE", "");
	            stationpd.put("FIMG", "");
	            stationpd.put("WC_WORKCENTER_ID", "");
	            stationpd.put("FINSTRUCTOR_NAME", "");
	            stationpd.put("FINSTRUCTOR_PATH", "");
	            stationpd.put("EQM_BASE_ID", "");
	            stationpd.put("MULTI_TASK_STATION", "NO");
	            stationpd.put("STATION_GROUP", "");
	            stationpd.put("QR_CODE", ObjectExcelRead.getCellValue(sheet, row, 0));
	            wc_stationService.save(stationpd);
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
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "station.xlsx", "station.xlsx");
	}
}
