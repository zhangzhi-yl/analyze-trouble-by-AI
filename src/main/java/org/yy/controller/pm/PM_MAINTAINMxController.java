package org.yy.controller.pm;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.yy.service.km.AttachmentSetService;
import org.yy.service.pm.PM_MAINTAINMxService;

/** 
 * 说明：设备保养任务(明细)
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-30
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PM_MAINTAINMx")
public class PM_MAINTAINMxController extends BaseController {
	
	@Autowired
	private PM_MAINTAINMxService PM_MAINTAINMxService;
	
	@Autowired
	private AttachmentSetService attachmentsetService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(
			@RequestParam(value = "RESERVE_TWO", required = false) MultipartFile file,
			@RequestParam(value="MAINTAIN_NORM_FILE",required=false) String path,
			@RequestParam(value="PM_MAINTAINMx_ID",required=false) String PM_MAINTAINMx_ID,
			@RequestParam(value="PM_MAINTAIN_ID",required=false) String PM_MAINTAIN_ID,
			@RequestParam(value="MAINTAIN_PART",required=false) String MAINTAIN_PART,
			@RequestParam(value="MAINTAIN_CONTENT",required=false) String MAINTAIN_CONTENT,
			@RequestParam(value="MAINTAIN_NORM",required=false) String MAINTAIN_NORM,
			@RequestParam(value="IF_SIGNIN",required=false) String IF_SIGNIN,
			@RequestParam(value="SIGNIN_TIME",required=false) String SIGNIN_TIME,
			@RequestParam(value="SIGNIN_RERSON",required=false) String SIGNIN_RERSON,
			@RequestParam(value="DESCRIBE",required=false) String DESCRIBE,
			@RequestParam(value="RESERVE_ONE",required=false) String RESERVE_ONE,
			@RequestParam(value="RESERVE_THREE",required=false) String RESERVE_THREE
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PM_MAINTAINMx_ID", this.get32UUID());	//主键
		pd.put("PM_MAINTAIN_ID", PM_MAINTAIN_ID);	
		pd.put("MAINTAIN_PART", MAINTAIN_PART);	
		pd.put("MAINTAIN_CONTENT", MAINTAIN_CONTENT);	
		pd.put("MAINTAIN_NORM", MAINTAIN_NORM);	
		pd.put("IF_SIGNIN", IF_SIGNIN);	
		pd.put("SIGNIN_TIME", SIGNIN_TIME);	
		pd.put("SIGNIN_RERSON", SIGNIN_RERSON);	
		pd.put("DESCRIBE", DESCRIBE);	
		pd.put("RESERVE_ONE", RESERVE_ONE);	
		pd.put("RESERVE_THREE", RESERVE_THREE);	
		//附件上传
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		String MAINTAIN_NORM_FILE = "";
		String PMRESERVE_TWO = "";
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
			String fileNamereal = pd.getString("MAINTAIN_NORM_FILE").substring(0, pd.getString("MAINTAIN_NORM_FILE").indexOf(".")); // 文件上传路径
			MAINTAIN_NORM_FILE = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
			PMRESERVE_TWO = Const.FILEPATHFILE +DateUtil.getDays()+"/"+MAINTAIN_NORM_FILE;
			//附件集插入数据
			PageData pdFile=new PageData();
			pdFile.put("DataSources", "设备保养项");
			pdFile.put("AssociationIDTable", "PM_MAINTAINMx");
			pdFile.put("AssociationID", pd.getString("PM_MAINTAINMx_ID"));
			pdFile.put("FName", MAINTAIN_NORM_FILE);
			pdFile.put("FUrl", PMRESERVE_TWO);
			pdFile.put("FExplanation", "");
			pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
			pdFile.put("FCreateTime", Tools.date2Str(new Date()));
			attachmentsetService.check(pdFile);
		}
		pd.put("RESERVE_TWO", PMRESERVE_TWO);	
		PM_MAINTAINMxService.save(pd);
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
		PM_MAINTAINMxService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value = "RESERVE_TWO", required = false) MultipartFile file,
			@RequestParam(value="MAINTAIN_NORM_FILE",required=false) String path,
			@RequestParam(value="PM_MAINTAINMx_ID",required=false) String PM_MAINTAINMx_ID,
			@RequestParam(value="PM_MAINTAIN_ID",required=false) String PM_MAINTAIN_ID,
			@RequestParam(value="MAINTAIN_PART",required=false) String MAINTAIN_PART,
			@RequestParam(value="MAINTAIN_CONTENT",required=false) String MAINTAIN_CONTENT,
			@RequestParam(value="MAINTAIN_NORM",required=false) String MAINTAIN_NORM,
			@RequestParam(value="IF_SIGNIN",required=false) String IF_SIGNIN,
			@RequestParam(value="SIGNIN_TIME",required=false) String SIGNIN_TIME,
			@RequestParam(value="SIGNIN_RERSON",required=false) String SIGNIN_RERSON,
			@RequestParam(value="DESCRIBE",required=false) String DESCRIBE,
			@RequestParam(value="RESERVE_ONE",required=false) String RESERVE_ONE,
			@RequestParam(value="RESERVE_THREE",required=false) String RESERVE_THREE
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PM_MAINTAINMx_ID", PM_MAINTAINMx_ID);	//主键
		pd.put("PM_MAINTAIN_ID", PM_MAINTAIN_ID);	
		pd.put("MAINTAIN_PART", MAINTAIN_PART);	
		pd.put("MAINTAIN_CONTENT", MAINTAIN_CONTENT);	
		pd.put("MAINTAIN_NORM", MAINTAIN_NORM);	
		pd.put("IF_SIGNIN", IF_SIGNIN);	
		pd.put("SIGNIN_TIME", SIGNIN_TIME);	
		pd.put("SIGNIN_RERSON", SIGNIN_RERSON);	
		pd.put("DESCRIBE", DESCRIBE);	
		pd.put("RESERVE_ONE", RESERVE_ONE);	
		pd.put("RESERVE_THREE", RESERVE_THREE);	
		//上传附件
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		String ffile = DateUtil.getDays();
		String MAINTAIN_NORM_FILE = "";
		String PMRESERVE_TWO = "";
		PageData pdJ = new PageData();
		pdJ=PM_MAINTAINMxService.findById(pd);
		String MAINTAIN_NORM_FILEJ="";
		if(null!=pdJ) {
			MAINTAIN_NORM_FILEJ=pdJ.getString("MAINTAIN_NORM_FILE");
		}
		if (null != file && !file.isEmpty()) {//上传附件
			if(!MAINTAIN_NORM_FILEJ.equals(pd.getString("MAINTAIN_NORM_FILE"))) {
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
				String fileNamereal = pd.getString("MAINTAIN_NORM_FILE").substring(0, pd.getString("MAINTAIN_NORM_FILE").indexOf(".")); // 文件上传路径
				MAINTAIN_NORM_FILE = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
				PMRESERVE_TWO = Const.FILEPATHFILE +DateUtil.getDays()+"/"+MAINTAIN_NORM_FILE;
				//附件集插入数据
				PageData pdFile=new PageData();
				pdFile.put("DataSources", "设备保养项");
				pdFile.put("AssociationIDTable", "PM_MAINTAINMx");
				pdFile.put("AssociationID", pd.getString("PM_MAINTAINMx_ID"));
				pdFile.put("FName", MAINTAIN_NORM_FILE);
				pdFile.put("FUrl", PMRESERVE_TWO);
				pdFile.put("FExplanation", "");
				pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
				pdFile.put("FCreateTime", Tools.date2Str(new Date()));
				pd.put("RESERVE_TWO", PMRESERVE_TWO);
				attachmentsetService.check(pdFile);
			}
		}else {//没上传附件引用之前的附件信息
			pd.put("MAINTAIN_NORM_FILE", pdJ.getString("MAINTAIN_NORM_FILE")!=null?pdJ.getString("MAINTAIN_NORM_FILE"):"");//附件名称
			pd.put("RESERVE_TWO", pdJ.getString("RESERVE_TWO")!=null?pdJ.getString("RESERVE_TWO"):"");//附件名称
		}
		PM_MAINTAINMxService.edit(pd);
		map.put("result", errInfo);				//返回结果
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
		List<PageData>	varList = PM_MAINTAINMxService.list(page);	//列出PM_MAINTAINMx列表
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
		pd = PM_MAINTAINMxService.findById(pd);	//根据ID读取
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
			PM_MAINTAINMxService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
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
			PM_MAINTAINMxService.delFj(pd);													//删除数据库中附件数据
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	@ResponseBody
	public Object readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file,
			@RequestParam(value="PM_MAINTAIN_ID",required=false) String PM_MAINTAIN_ID
			) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData basicPd = new PageData();
		if (null != file && !file.isEmpty()) {
	        int realRowCount = 0;//真正有数据的行数
	        String reg="^\\d+$";
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
	            
	            basicPd.put("PM_MAINTAINMx_ID", this.get32UUID());//保养项ID主键
	            basicPd.put("PM_MAINTAIN_ID", PM_MAINTAIN_ID);//保养主表ID主键
	            if(ObjectExcelRead.getCellValue(sheet, row, 0).matches(reg)==true) {//判断表格序号值是否为数字，不为数字填写0
	            	 basicPd.put("RESERVE_ONE", ObjectExcelRead.getCellValue(sheet, row, 0));//排序
	 	            }else {
	 	            	 basicPd.put("RESERVE_ONE", 0);//排序
	 	            }
	            basicPd.put("MAINTAIN_PART", ObjectExcelRead.getCellValue(sheet, row, 1));//保养部位
	            basicPd.put("MAINTAIN_NORM", ObjectExcelRead.getCellValue(sheet, row, 2));//保养标准
	            basicPd.put("MAINTAIN_CONTENT", ObjectExcelRead.getCellValue(sheet, row, 3));//保养内容
	            PM_MAINTAINMxService.save(basicPd);
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
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "MAINTAINITEM.xlsx", "MAINTAINITEM.xlsx");
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
		titles.add("保养部位");	//1
		titles.add("保养内容");	//2
		titles.add("保养标准");	//3
		titles.add("保养标准附件");	//4
		titles.add("是否签到");	//5
		titles.add("签到时间");	//6
		titles.add("签到人");	//7
		titles.add("描述");	//8
		titles.add("预留1");	//9
		titles.add("预留2");	//10
		titles.add("预留3");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = PM_MAINTAINMxService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MAINTAIN_PART"));	    //1
			vpd.put("var2", varOList.get(i).getString("MAINTAIN_CONTENT"));	    //2
			vpd.put("var3", varOList.get(i).getString("MAINTAIN_NORM"));	    //3
			vpd.put("var4", varOList.get(i).getString("MAINTAIN_NORM_FILE"));	    //4
			vpd.put("var5", varOList.get(i).getString("IF_SIGNIN"));	    //5
			vpd.put("var6", varOList.get(i).getString("SIGNIN_TIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("SIGNIN_RERSON"));	    //7
			vpd.put("var8", varOList.get(i).getString("DESCRIBE"));	    //8
			vpd.put("var9", varOList.get(i).getString("RESERVE_ONE"));	    //9
			vpd.put("var10", varOList.get(i).getString("RESERVE_TWO"));	    //10
			vpd.put("var11", varOList.get(i).getString("RESERVE_THREE"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

	 /**更新执行项
	  * 杨城 21010927
	  * v1:更新执行项，通过明细主键id更新，前台传参明细id 执行项
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editExecute")
	@ResponseBody
	public Object editExecute() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FEXECUTE_TIME", Tools.date2Str(new Date()));//执行时间
		pd.put("FEXECUTOR", Jurisdiction.getName());//执行人
		PM_MAINTAINMxService.editExecute(pd);	//更新执行项
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	 /**列表全部
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="listAll")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = PM_MAINTAINMxService.listAll(pd);	//列出PM_MAINTAINMx列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
}
