package org.yy.controller.project.manager;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelReadYT;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.project.manager.Cabinet_AssemblyService;
import org.yy.service.project.manager.PROJECTMANAGERVIEWService;

/** 
 * 说明：开工会
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-27
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/projectmanagerview")
public class PROJECTMANAGERVIEWController extends BaseController {
	
	@Autowired
	private PROJECTMANAGERVIEWService projectmanagerviewService;
	@Autowired
	private Cabinet_AssemblyService cabinetAssemblyService;
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
		pd.put("PROJECTMANAGERVIEW_ID", this.get32UUID());	//主键
		projectmanagerviewService.save(pd);
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
		projectmanagerviewService.delete(pd);
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
		projectmanagerviewService.edit(pd);
		PageData pdx=projectmanagerviewService.findById(pd);
		if(pdx !=null && null != pdx.get("FTYPENAME") && "包装".equals(pdx.get("FTYPENAME"))) {
		
		if(pd != null && pd !=pd.get("PLAN_END_TIME") && !"".equals(pd.get("PLAN_END_TIME"))) {
			String PDELIVERY_DATE = pd.getString("PLAN_END_TIME");		//获取结束日期
			List<PageData> varList = cabinetAssemblyService.listAll(pdx);
			for(int i=0 ;i<varList.size() ;i++){
				PageData pdX=varList.get(i);
				SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd"); 	//设定格式
				Date d = df1.parse(PDELIVERY_DATE);							//转换成日期格式
				String Buffer_Period = varList.get(i).getString("Buffer_Period");	//获取缓冲期
				Integer a = Integer.parseInt(Buffer_Period);				//转换成int
				String Estimate_Start_Date = df1.format(new Date(d.getTime() - a * 24 * 60 * 60 * 1000));	//计算减去缓冲期获取开始时间
				pdX.put("Project_End_Date", PDELIVERY_DATE);
				pdX.put("Estimate_Start_Date", Estimate_Start_Date);
				cabinetAssemblyService.edit(pdX);
			}
		}
		}
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
		List<PageData>	varList = projectmanagerviewService.listAll(pd);	//列出PROJECTMANAGERVIEW列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listGanTE")
	@ResponseBody
	public Object listGanTE(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = projectmanagerviewService.listGanTe(pd);	//列出PROJECTMANAGERVIEW列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/editGanTE")
	@ResponseBody
	public Object editGanTE(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			projectmanagerviewService.editGanTE(pd);
			errInfo = "success";
		}catch(Exception e){
			errInfo = "wrong";
		}
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
		pd = projectmanagerviewService.findById(pd);	//根据ID读取
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
			projectmanagerviewService.deleteAll(ArrayDATA_IDS);
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
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("类型名称");	//1
		titles.add("计划开始时间");	//2
		titles.add("结束时间");	//3
		titles.add("实际开始时间");	//4
		titles.add("实际结束时间");	//5
		titles.add("项目ID");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = projectmanagerviewService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FTYPENAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("PLAN_START_TIME"));	    //2
			vpd.put("var3", varOList.get(i).getString("PLAN_END_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("REAL_START_TIME"));	    //4
			vpd.put("var5", varOList.get(i).getString("REAL_END_TIME"));	    //5
			vpd.put("var6", varOList.get(i).getString("PROJECT_ID"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/getFormPlate")
	public void getFormPlate(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "projectmanagerview.xlsx", "开工会模板.xlsx");
	}
	
	/**读取文件
	 * v2 管悦 20210922 用包装计划结束日期反写柜体计划开始结束日期
   	 * @param
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "/readExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value = "file", required = false) MultipartFile file,String PROJECT_ID)throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdT = new PageData();
		DecimalFormat df = new DecimalFormat("#0.00");
		try {
			if (null != file && !file.isEmpty()) {

				String  ffile = DateUtil.getDays(), fileName = "";
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;	//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
				List<PageData> list = (List) ObjectExcelReadYT.readExcelMA(filePath, fileName, 1, 0, 0, 4);
				pdT.put("PROJECT_ID", PROJECT_ID);
				if(list.size()>0){
					for(int i=0 ;i<list.size() ;i++){
						PageData pdl = list.get(i);
						pd.put("PROJECTMANAGERVIEW_ID", this.get32UUID());
						pd.put("FTYPENAME", pdl.get("var1"));
						pd.put("PLAN_START_TIME", pdl.get("var2"));
						pd.put("PLAN_END_TIME", pdl.get("var3"));
						pd.put("PROJECT_ID", PROJECT_ID);
						pd.put("FSORT", pdl.get("var0"));
						projectmanagerviewService.save(pd);
					}
				}else{
					result = "exception";
					map.put("result", result);
					map.put("msgText", "文件内容为空,请确认！！！");
				}
				//
				pdT.put("FTYPENAME", "包装");
				PageData pdN=projectmanagerviewService.findByName(pdT);
				
				if(pdN != null && null !=pdN.get("PLAN_END_TIME") && !"".equals(pdN.get("PLAN_END_TIME"))) {
					String PDELIVERY_DATE = pdN.getString("PLAN_END_TIME");		//获取结束日期
					List<PageData> varList = cabinetAssemblyService.listAll(pdT);
					for(int i=0 ;i<varList.size() ;i++){
						PageData pdX=varList.get(i);
						SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd"); 	//设定格式
						Date d = df1.parse(PDELIVERY_DATE);							//转换成日期格式
						String Buffer_Period = varList.get(i).getString("Buffer_Period");	//获取缓冲期
						Integer a = Integer.parseInt(Buffer_Period);				//转换成int
						String Estimate_Start_Date = df1.format(new Date(d.getTime() - a * 24 * 60 * 60 * 1000));	//计算减去缓冲期获取开始时间
						pdX.put("Project_End_Date", PDELIVERY_DATE);
						pdX.put("Estimate_Start_Date", Estimate_Start_Date);
						cabinetAssemblyService.edit(pdX);
					}
					
				}
				
			}
			result = "success";
			map.put("result", result);
			map.put("msgText", "导入Execl成功！");
		} catch (Exception e) {
			result = "exception";
		} finally {
			map.put("result", result);
		}
		return map;
	}
	
	/**
	 * 判断时间格式 格式必须为“YYYY-MM-dd”
	 * 2004-2-30 是无效的
	 * 2003-2-29 是无效的
	 * @param sDate
	 * @return
	 * @版本:v.1  @创建人:娄鸿基    @创建时间:2021-06-07  @创建内容:时间格式判断
	 */
	private static boolean isLegalDate(String sDate) {
	    int legalLen = 10;
	    if ((sDate == null) || (sDate.length() != legalLen)) {
	        return false;
	    }
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	        Date date = formatter.parse(sDate);
	        return sDate.equals(formatter.format(date));
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	/**
	 * @param startDate,endTime
	 * @return
	 * @版本:v.1  @创建人:娄鸿基    @创建时间:2021-06-21  @创建内容:日期大小比较
	 */
	@SuppressWarnings("unused")
	private static boolean compareDate(String startDate,String endDate) {
		if((startDate == "" || startDate.equals("")) && (endDate == "" || endDate.equals(""))){
			return true;
		}else{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    try {
		    	Date startTime = format.parse(startDate);	//开始时间
		    	Date endTime = format.parse(endDate);		//结束时间
		    	long beginMillisecond = startTime.getTime();//毫秒转换
		    	long endMillisecond = endTime.getTime();
		    	if(beginMillisecond > endMillisecond){ //判断开始时间大于结束时间
		    		return false;
		    	}else{
		    		return true;
		    	}
		    } catch (Exception e) {
		        return false;
		    }
		}
	}
}
