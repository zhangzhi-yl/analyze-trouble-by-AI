package org.yy.controller.momp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.yy.util.FileDownload;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.momp.ListProblemsService;

/** 
 * 说明：问题清单功能（龙油）
 * 作者：YuanYe
 * 时间：2020-04-13
 * 
 */
@Controller
@RequestMapping("/listproblems")
public class ListProblemsController extends BaseController {
	
	@Autowired
	private ListProblemsService listproblemsService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
//	@RequiresPermissions("listproblems:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("LISTPROBLEMS_ID", this.get32UUID());	//主键
		pd.put("UPDATETIME", Tools.date2Str(new Date()));	//主键
		listproblemsService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/total")
//	@RequiresPermissions("listproblems:add")
	@ResponseBody
	public Object total() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList =listproblemsService.findlistByItem(pd);//汇总会议待办
		List<PageData>	tvarList=listproblemsService.findlistByRisk(pd);//汇总双周计划
		if(varList.size()>0){
			for (int i = 0; i < varList.size(); i++) {
				PageData pdItem = varList.get(i);
				pdItem.put("LISTPROBLEMS_ID", this.get32UUID());	//主键
				pdItem.put("UPDATETIME", Tools.date2Str(new Date()));	//时间
				pdItem.put("SOLUTIONDESCRIPTION", pdItem.getString("ACTIONPLAN"));	//解决方案
				String FSTATE=pdItem.getString("FSTATE");
				if(FSTATE.equals("未启动")){
					FSTATE="未开始";
				}else if(FSTATE.equals("进行中")){
					FSTATE="解决中";
				}else if(FSTATE.equals("已完成")){
					FSTATE="已解决";
				}
				pdItem.put("FSTATE", FSTATE);	//状态
				pdItem.put("PERSONINCHARGE", pdItem.getString("HEAD"));
				pdItem.put("PLANINSTANCE_ID", pd.getString("PLANINSTANCE_ID"));
				listproblemsService.save(pdItem);
			}
		}
		if(tvarList.size()>0){
			for (int i = 0; i <tvarList.size(); i++) {
				PageData pdRisk = tvarList.get(i);
				pdRisk.put("LISTPROBLEMS_ID", this.get32UUID());	//主键
				pdRisk.put("UPDATETIME", Tools.date2Str(new Date()));	//时间
				pdRisk.put("PLANINSTANCE_ID",pd.getString("PLANINSTANCE_ID"));
				pdRisk.put("PERSONINCHARGE",pdRisk.getString("FPRINCIPAL"));//负责人
				pdRisk.put("PROBLEMDESCRIPTION",pdRisk.getString("FDECRIBE"));
				pdRisk.put("SOLUTIONDESCRIPTION",pdRisk.getString("FMEASURE"));
				pdRisk.put("FSTATE", "未开始");
				listproblemsService.save(pdRisk);
			}
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
//	@RequiresPermissions("listproblems:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		listproblemsService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
//	@RequiresPermissions("listproblems:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		listproblemsService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		String FSTATE= pd.getString("FSTATE");		
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		if(Tools.notEmpty(FSTATE))pd.put("FSTATE", FSTATE.trim());
		page.setPd(pd);
		pd =listproblemsService.findByMainId(pd);
		if(pd!=null) {
			String UPDATETIME = pd.getString("UPDATETIME");
			pd.put("UPDATETIME", UPDATETIME);
		}
		List<PageData>	varList = listproblemsService.list(page);	//列出ListProblems列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
//	@RequiresPermissions("listproblems:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = listproblemsService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
//	@RequiresPermissions("listproblems:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			listproblemsService.deleteAll(ArrayDATA_IDS);
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
//	@RequestMapping(value="/excel")
//	@RequiresPermissions("toExcel")
//	public ModelAndView exportExcel() throws Exception{
//		ModelAndView mv = new ModelAndView();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		Map<String,Object> dataMap = new HashMap<String,Object>();
//		List<String> titles = new ArrayList<String>();
//		titles.add("序号");	//1
//		titles.add("部门");	//2
//		titles.add("部门编号");	//3
//		titles.add("问题类型");	//4
//		titles.add("问题出现阶段");	//5
//		titles.add("提出人");	//6
//		titles.add("提出时间");	//7
//		titles.add("问题名称");	//8
//		titles.add("问题描述");	//9
//		titles.add("优先级");	//10
//		titles.add("影响");	//11
//		titles.add("复杂性");	//12
//		titles.add("影响描述");	//13
//		titles.add("解决方案描述");	//14
//		titles.add("内外部负责人");	//15
//		titles.add("状态");	//16
//		titles.add("当前进度");	//17
//		titles.add("解决时间");	//18
//		titles.add("结果描述");	//19
//		titles.add("审核人");	//20
//		titles.add("备注");	//21
//		dataMap.put("titles", titles);
//		List<PageData> varOList = listproblemsService.listAll(pd);
//		List<PageData> varList = new ArrayList<PageData>();
//		for(int i=0;i<varOList.size();i++){
//			PageData vpd = new PageData();
//			vpd.put("var1", varOList.get(i).getString("SERIALNUMBER"));	    //1
//			vpd.put("var2", varOList.get(i).getString("DEPARTMENTNAME"));	    //2
//			vpd.put("var3", varOList.get(i).getString("DEPARTMENTNAME_ID"));	    //3
//			vpd.put("var4", varOList.get(i).getString("QUESTIONTYPE"));	    //4
//			vpd.put("var5", varOList.get(i).getString("PROBLEMSTAGE"));	    //5
//			vpd.put("var6", varOList.get(i).getString("PROPOSEPEOPLE"));	    //6
//			vpd.put("var7", varOList.get(i).getString("PROPOSETIME"));	    //7
//			vpd.put("var8", varOList.get(i).getString("PROBLEMNAME"));	    //8
//			vpd.put("var9", varOList.get(i).getString("PROBLEMDESCRIPTION"));	    //9
//			vpd.put("var10", varOList.get(i).getString("PRIORITY"));	    //10
//			vpd.put("var11", varOList.get(i).getString("INFLUENCE"));	    //11
//			vpd.put("var12", varOList.get(i).getString("COMPLEXITY"));	    //12
//			vpd.put("var13", varOList.get(i).getString("IMPACTDESCRIPTION"));	    //13
//			vpd.put("var14", varOList.get(i).getString("SOLUTIONDESCRIPTION"));	    //14
//			vpd.put("var15", varOList.get(i).getString("PERSONINCHARGE"));	    //15
//			vpd.put("var16", varOList.get(i).getString("FSTATE"));	    //16
//			vpd.put("var17", varOList.get(i).getString("CURRENTPROGRESS"));	    //17
//			vpd.put("var18", varOList.get(i).getString("SETTLINGTIME"));	    //18
//			vpd.put("var19", varOList.get(i).getString("RESULTDESCRIPTION"));	    //19
//			vpd.put("var20", varOList.get(i).getString("AUDITOR"));	    //20
//			vpd.put("var21", varOList.get(i).getString("REMARKS"));	    //21
//			varList.add(vpd);
//		}
//		dataMap.put("varList", varList);
//		ObjectExcelView erv = new ObjectExcelView();
//		mv = new ModelAndView(erv,dataMap);
//		return mv;
//	}
	
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	//@RequiresPermissions("toExcel")
	@ResponseBody
	public Object exportExcel() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		HttpServletRequest rt=this.getRequest();
		PageData pd=this.getPageData();
		PageData cpd=new PageData();
		List<PageData>	varOList=new ArrayList<PageData>();
		int recordNumt=0;
		try {
			 varOList = listproblemsService.listAll(pd);

			recordNumt=varOList.size();
		} catch (Exception e) {
			// TODO:获取数据异常
			map.put("result", "error");
		}
		 if(recordNumt<1)
		    {
		    	//没有数据
		    	map.put("result", "withoutrecord");
		    	errInfo="error";
		    }else {
		    	String realpath = PathUtil.getProjectpath() + Const.FILEPATHFILE;
			    String filepath=realpath+"\\"+"\\listproblemsdc.xlsx";///打印模板的物理路径
				String newfilename="PROBLEMS"+Tools.date2Str(new Date(),"yyyyMMddHHmmss");
				String newfilepath=realpath+"\\"+"\\"+newfilename +".xlsx";
				try {
					if(recordNumt>0){
						ProblemsReadExcel.copyExcelCQUOTemplate2(filepath, newfilepath, recordNumt,varOList);	
					}
				} catch (Exception e) {
					map.put("result", "copyfailed");
				}
				map.put("newfilename", newfilename);
		    }
			map.put("result", errInfo);
		
			return map;
			
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
		PageData pd = new PageData();
		pd = this.getPageData();
		String time = Tools.date2Str(new Date());
		//listproblemsService.deleteByMainId(pd);
		if (null != file && !file.isEmpty()) {

	        int realRowCount = 0;//真正有数据的行数
	        //得到工作空间
	        Workbook workbook = null;
	        try {
	            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        int sheetIndex=0;
		        //得到sheet
		        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, sheetIndex);
		        realRowCount = sheet.getPhysicalNumberOfRows();
		        
		        for(int rowNum = 1;rowNum <= realRowCount;rowNum++) {
		        	Row row = sheet.getRow(rowNum);
		            if(ObjectExcelRead.isBlankRow(row)) {//空行跳过
		            	break;
		            }
		            
		            if(row.getRowNum() == -1) {
		                continue;
		            }else {
		                if(row.getRowNum() == 0) {//第一行表头跳过
		                    continue;
		                }
		            }   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                    String createDate="1899-12-30";
	                    Date date = sdf.parse(createDate);
		            	pd.put("LISTPROBLEMS_ID", this.get32UUID());
		            	pd.put("SERIALNUMBER", ObjectExcelRead.getCellValue(sheet, row, 0).equals("")?"0":(int)(Float.parseFloat(ObjectExcelRead.getCellValue(sheet, row, 0))));
		            	pd.put("DEPARTMENTNAME", ObjectExcelRead.getCellValue(sheet, row, 1));
		            	pd.put("QUESTIONTYPE",ObjectExcelRead.getCellValue(sheet, row, 2));
		            	pd.put("PROBLEMSTAGE", ObjectExcelRead.getCellValue(sheet, row, 3));
			            pd.put("PROPOSEPEOPLE", ObjectExcelRead.getCellValue(sheet, row, 4));
		                String PROPOSETIME = "";
		                if(!ObjectExcelRead.getCellValue(sheet, row, 5).equals("")) {
		                   Calendar cl1 = Calendar.getInstance();
		                   cl1.setTime(date);
		                   cl1.add(Calendar.DATE,(int)(Float.parseFloat(ObjectExcelRead.getCellValue(sheet, row, 5))));
		                   PROPOSETIME = sdf.format(cl1.getTime());
		                }
			            pd.put("PROPOSETIME", PROPOSETIME);
			            pd.put("PROBLEMNAME", ObjectExcelRead.getCellValue(sheet, row, 6));
			            pd.put("PROBLEMDESCRIPTION", ObjectExcelRead.getCellValue(sheet, row, 7));
			            pd.put("PRIORITY", ObjectExcelRead.getCellValue(sheet, row, 8));
			            pd.put("INFLUENCE", ObjectExcelRead.getCellValue(sheet, row, 9));
			            pd.put("COMPLEXITY", ObjectExcelRead.getCellValue(sheet, row, 10));
			            pd.put("IMPACTDESCRIPTION", ObjectExcelRead.getCellValue(sheet, row, 11));
			            String STOPTIME = "";
		                if(!ObjectExcelRead.getCellValue(sheet, row, 12).equals("")) {
		                   Calendar cl1 = Calendar.getInstance();
		                   cl1.setTime(date);
		                   cl1.add(Calendar.DATE,(int)(Float.parseFloat(ObjectExcelRead.getCellValue(sheet, row, 12))));
		                   STOPTIME = sdf.format(cl1.getTime());
		                }
			            pd.put("STOPTIME",STOPTIME);
			            pd.put("SOLUTIONDESCRIPTION", ObjectExcelRead.getCellValue(sheet, row, 13));
			            pd.put("PERSONINCHARGE", ObjectExcelRead.getCellValue(sheet, row, 14));
			            pd.put("FSTATE", ObjectExcelRead.getCellValue(sheet, row, 15));
			            pd.put("CURRENTPROGRESS", ObjectExcelRead.getCellValue(sheet, row, 16));
			            String SETTLINGTIME = "";
		                if(!ObjectExcelRead.getCellValue(sheet, row, 17).equals("")) {
		                   Calendar cl1 = Calendar.getInstance();
		                   cl1.setTime(date);
		                   cl1.add(Calendar.DATE,(int)(Float.parseFloat(ObjectExcelRead.getCellValue(sheet, row, 17))));
		                   SETTLINGTIME = sdf.format(cl1.getTime());
		                }
			            pd.put("SETTLINGTIME", SETTLINGTIME);
			            pd.put("RESULTDESCRIPTION", ObjectExcelRead.getCellValue(sheet, row, 18));
			            pd.put("AUDITOR", ObjectExcelRead.getCellValue(sheet, row, 19));
			            pd.put("REMARKS", ObjectExcelRead.getCellValue(sheet, row, 20));
			            pd.put("UPDATETIME",Tools.date2Str(new Date()));
			            listproblemsService.save(pd);
		        }
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	//下载导出的文件
	@RequestMapping(value="/uploadExcel")
	public void uploadExcel(HttpServletResponse response)throws Exception{
		PageData pd=this.getPageData();
		String newfilename=pd.getString("newfilename");
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + newfilename +".xlsx", newfilename +".xlsx");
	}
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "listproblems.xlsx", "listproblems.xlsx");
	}
	
}
