package org.yy.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
import org.yy.util.Const;
import org.yy.entity.PageData;
import org.yy.service.project.manager.PROJECTTASKService;
import org.yy.util.PathUtil;

/**
* 导出到EXCEL（xlsx）
* 类名称：ObjectExcelViewxlsx.java
* @author 
* @version 1.0
 */
public class ObjectExcelViewxlsxGantt extends AbstractXlsxView{

	protected void buildExcelDocument(Map<String, Object> model,
			Workbook wb, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebApplicationContext webctx=ContextLoader.getCurrentWebApplicationContext();
		PROJECTTASKService projecttaskService = (PROJECTTASKService) SpringUtil.getBean("PROJECTTASKServiceImpl");

		Date date = new Date();
		String filename = Tools.date2Str(date, "yyyyMMddHHmmss");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+"Gantt_"+filename+".xlsx");

		Sheet sheet = wb.createSheet("项目计划甘特图");
		PageData pd=new PageData();
		pd.put("KEYWORDS1", (String) model.get("KEYWORDS1"));
		pd.put("KEYWORDS2", (String) model.get("KEYWORDS2"));
		pd.put("KEYWORDS3", (String) model.get("KEYWORDS3"));
		pd.put("KEYWORDS4", (String) model.get("KEYWORDS4"));
		pd.put("KEYWORDS5", (String) model.get("KEYWORDS5"));
		pd.put("KEYWORDS6", (String) model.get("KEYWORDS6"));
		pd.put("KEYWORDS7", (String) model.get("KEYWORDS7"));
		pd.put("KEYWORDS8", (String) model.get("KEYWORDS8"));
		pd.put("KEYWORDS9", (String) model.get("KEYWORDS9"));
		pd.put("KEYWORDS10", (String) model.get("KEYWORDS10"));
		pd.put("KEYWORDS11", (String) model.get("KEYWORDS11"));
		pd.put("KEYWORDS12", (String) model.get("KEYWORDS12"));
		pd.put("KEYWORDS13", (String) model.get("KEYWORDS13"));
		pd.put("KEYWORDS14", (String) model.get("KEYWORDS14"));
		pd.put("KEYWORDS15", (String) model.get("KEYWORDS15"));
		pd.put("KEYWORDS16", (String) model.get("KEYWORDS16"));
		pd.put("KEYWORDS17", (String) model.get("KEYWORDS17"));
		pd.put("KEYWORDS18", (String) model.get("KEYWORDS18"));
		PageData pdRange = projecttaskService.getRange(pd);	//获得甘特图范围
		List<PageData> rangeList = projecttaskService.getRangeList(pdRange);	//获得甘特图日期列表
		pd.put("START_TIME", pdRange.getString("START_TIME"));
		pd.put("END_TIME", pdRange.getString("END_TIME"));
		List<PageData> dataList = projecttaskService.getDataList(pd);	//获得甘特图任务明细列表

			sheet.setColumnWidth( 0, 2300);
			sheet.setColumnWidth( 1, 6000);
			sheet.setColumnWidth( 2, 6000); 
			sheet.setColumnWidth( 3, 6000); 
			sheet.setColumnWidth( 4, 4000); 
			sheet.setColumnWidth( 5, 4000);
			sheet.setColumnWidth( 6, 3200);
			sheet.setColumnWidth( 7, 3800);
			sheet.setColumnWidth( 8, 4000);
			sheet.setColumnWidth( 9, 4000);
			sheet.setColumnWidth( 10, 4000);
			sheet.setColumnWidth( 11, 5000);
			sheet.setColumnWidth( 12, 8000);
			sheet.setColumnWidth( 13, 3200);
			sheet.setColumnWidth( 14, 3200);
			sheet.setColumnWidth( 15, 4000);
			sheet.setColumnWidth( 16, 4000);
			sheet.setColumnWidth( 17, 4000);
			sheet.setColumnWidth( 18, 4000);
			sheet.createFreezePane( 0, 1, 0, 1 ); 
			
			VerticalAlignment align=VerticalAlignment.CENTER;
			HorizontalAlignment align1=HorizontalAlignment.CENTER;
	     // 表头标题样式
	        XSSFFont headfont = (XSSFFont) wb.createFont();
	        headfont.setFontName("宋体");
	        headfont.setFontHeightInPoints((short) 14);
	        XSSFCellStyle headstyle = (XSSFCellStyle) wb.createCellStyle();
	        headstyle.setFont(headfont);
			headstyle.setAlignment(align1);// 左右居中
	        headstyle.setVerticalAlignment(align);// 上下居中
	        headstyle.setLocked(true);
	        headstyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
	        headstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headstyle.setWrapText(true); // 换行
	        // 普通单元格样式（中文）
	        XSSFFont font1 = (XSSFFont) wb.createFont();
	        font1.setFontName("宋体");
	        font1.setFontHeightInPoints((short) 12);
	        XSSFFont font2 = (XSSFFont) wb.createFont();
	        font2.setFontName("宋体");
	        font2.setFontHeightInPoints((short) 12);
	        font2.setBold(true);
	        XSSFFont font5 = (XSSFFont) wb.createFont();
	        font5.setFontName("宋体");
	        font5.setFontHeightInPoints((short) 14);
	        font5.setBold(true);
	        XSSFFont font6 = (XSSFFont) wb.createFont();
	        font6.setFontName("宋体");
	        font6.setFontHeightInPoints((short) 12);
	        //font6.setColor(XSSFColor.RED.index);
	        font6.setBold(true);
	        XSSFCellStyle style2 = (XSSFCellStyle) wb.createCellStyle();
	        BorderStyle border=BorderStyle.THIN;
			style2.setBorderBottom(border); //下边框
	        style2.setBorderLeft(border);//左边框
	        style2.setBorderTop(border);//上边框
	        style2.setBorderRight(border);//右边框
	        style2.setFont(font1);
	        style2.setAlignment(align1);// 左右居中
	        style2.setWrapText(true); // 换行
	        style2.setVerticalAlignment(align);// 上下居中
	        
			 // 普通单元格样式（中文） 无边框
			XSSFCellStyle style3 = (XSSFCellStyle) wb.createCellStyle();
			style3.setBorderBottom(border); //下边框
	        style3.setBorderTop(border);//上边框
	        style3.setBorderRight(border);//右边框
	        style3.setFont(font1);
	        style3.setAlignment(align1);// 左右居中
	        style3.setWrapText(true); // 换行
	        style3.setVerticalAlignment(align);// 上下居中
	       
	        // 普通单元格样式（中文） 无边框
			XSSFCellStyle style4 = (XSSFCellStyle) wb.createCellStyle();
			
			style4.setBorderBottom(border); //下边框
	        style4.setBorderLeft(border);//左边框
	        style4.setBorderTop(border);//上边框
	        style4.setBorderRight(border);//右边框
	        style4.setFont(font1);
	        style4.setAlignment(align1);// 左右居中
	        style4.setWrapText(true); // 换行
	        style4.setVerticalAlignment(align);// 上下居中
	        style4.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		    style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    
		    // 普通单元格样式（中文） 无边框
			XSSFCellStyle style5 = (XSSFCellStyle) wb.createCellStyle();
			
			style5.setBorderBottom(border); //下边框
	        style5.setBorderLeft(border);//左边框
	        style5.setBorderTop(border);//上边框
	        style5.setBorderRight(border);//右边框
	        style5.setFont(font1);
	        style5.setAlignment(align1);// 左右居中
	        style5.setWrapText(true); // 换行
	        style5.setVerticalAlignment(align);// 上下居中
	        style5.setFillForegroundColor(IndexedColors.LIME.getIndex());
		    style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    
		    // 普通单元格样式（中文） 无边框
			XSSFCellStyle style6 = (XSSFCellStyle) wb.createCellStyle();
			
			style6.setBorderBottom(border); //下边框
	        style6.setBorderLeft(border);//左边框
	        style6.setBorderTop(border);//上边框
	        style6.setBorderRight(border);//右边框
	        style6.setFont(font1);
	        style6.setAlignment(align1);// 左右居中
	        style6.setWrapText(true); // 换行
	        style6.setVerticalAlignment(align);// 上下居中
	        style6.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		    style6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    
		    // 普通单元格样式（中文） 无边框
			XSSFCellStyle style7 = (XSSFCellStyle) wb.createCellStyle();
			style7.setBorderBottom(border); //下边框
	        style7.setBorderLeft(border);//左边框
	        style7.setBorderTop(border);//上边框
	        style7.setBorderRight(border);//右边框
	        style7.setFont(font1);
	        style7.setAlignment(align1);// 左右居中
	        style7.setWrapText(true); // 换行
	        style7.setVerticalAlignment(align);// 上下居中
	        style7.setFillForegroundColor(IndexedColors.RED.getIndex());
		    style7.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    // 普通单元格样式（中文） 无边框
			XSSFCellStyle style8 = (XSSFCellStyle) wb.createCellStyle();
			style8.setBorderBottom(border); //下边框
	        style8.setBorderLeft(border);//左边框
	        style8.setBorderTop(border);//上边框
	        style8.setBorderRight(border);//右边框
	        style8.setFont(font1);
	        style8.setAlignment(align1);// 左右居中
	        style8.setWrapText(true); // 换行
	        style8.setVerticalAlignment(align);// 上下居中
	        style8.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
	        style8.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    // 普通单元格样式（中文） 无边框
			XSSFCellStyle style9 = (XSSFCellStyle) wb.createCellStyle();
			style9.setBorderBottom(border); //下边框
	        style9.setBorderLeft(border);//左边框
	        style9.setBorderTop(border);//上边框
	        style9.setBorderRight(border);//右边框
	        style9.setFont(font1);
	        style9.setAlignment(align1);// 左右居中
	        style9.setWrapText(true); // 换行
	        style9.setVerticalAlignment(align);// 上下居中
	        style9.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		    style9.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			String[] head0 = new String[] { 
					"序号", "项目号", "项目名称", "客户名称", "设备号", "设备名称", "Type","交货期"
					,"当前阶段" ,"执行状态", "当前计划状态", "图样代号"
					,"任务名称", "有无此项", "负责人","是否有输出物", "计划开始日期", "计划结束日期", "工时"		
			};
			int totalNum=0;
			// 表头列名
			XSSFRow row0 = (XSSFRow) sheet.createRow(0);
			for (int gyi = 0; gyi < head0.length; gyi++) {
				XSSFCell cell0 = row0.createCell(gyi);
				cell0.setCellValue(head0[gyi]);
				cell0.setCellStyle(headstyle);
			}
			for(int gym=0;gym<rangeList.size();gym++) {
				XSSFCell cellm = row0.createCell(head0.length+gym);
				cellm.setCellValue(rangeList.get(gym).getString("every_time"));
				cellm.setCellStyle(style3);
			}
			for(int gyn=0;gyn<dataList.size();gyn++) {
				XSSFRow rown = (XSSFRow) sheet.createRow(gyn+1);
				PageData pdMx=dataList.get(gyn);
				XSSFCell celln0 = rown.createCell(0);
				celln0.setCellValue(pdMx.get("hh").toString());
				celln0.setCellStyle(style3);
				XSSFCell celln1 = rown.createCell(1);
				celln1.setCellValue(pdMx.getString("PPROJECT_CODE"));
				celln1.setCellStyle(style3);
				
				XSSFCell celln2 = rown.createCell(2);
				celln2.setCellValue(pdMx.getString("PPROJECT_NAME"));
				celln2.setCellStyle(style3);
				
				XSSFCell celln3 = rown.createCell(3);
				celln3.setCellValue(pdMx.getString("ECUSTOMER_NAME"));
				celln3.setCellStyle(style3);
				
				XSSFCell celln4 = rown.createCell(4);
				celln4.setCellValue(pdMx.getString("EEQM_NO"));
				celln4.setCellStyle(style3);
				
				XSSFCell celln5 = rown.createCell(5);
				celln5.setCellValue(pdMx.getString("EEQM_NAME"));
				celln5.setCellStyle(style3);
				
				XSSFCell celln6 = rown.createCell(6);
				celln6.setCellValue(pdMx.getString("ETYPE"));
				celln6.setCellStyle(style3);
				
				XSSFCell celln7 = rown.createCell(7);
				celln7.setCellValue(pdMx.getString("EDELIVERY_DATE"));
				celln7.setCellStyle(style3);
				
				XSSFCell celln8 = rown.createCell(8);
				celln8.setCellValue(pdMx.getString("FHTNAME"));
				celln8.setCellStyle(style3);
				
				XSSFCell celln9 = rown.createCell(9);
				celln9.setCellValue(pdMx.getString("STAFFPLAN_TYPE"));
				celln9.setCellStyle(style3);
				
				XSSFCell celln10 = rown.createCell(10);
				celln10.setCellValue(pdMx.getString("NOW_PLAN_TYPE"));
				celln10.setCellStyle(style3);
				
				XSSFCell celln11 = rown.createCell(11);
				celln11.setCellValue(pdMx.getString("YL2"));
				celln11.setCellStyle(style3);
				
				XSSFCell celln12 = rown.createCell(12);
				celln12.setCellValue(pdMx.getString("FTASK_NAME"));
				celln12.setCellStyle(style3);
				
				XSSFCell celln13 = rown.createCell(13);
				celln13.setCellValue(pdMx.getString("YL3"));
				celln13.setCellStyle(style3);
				
				XSSFCell celln14 = rown.createCell(14);
				celln14.setCellValue(pdMx.getString("STAFFPLAN_MANAGER"));
				celln14.setCellStyle(style3);
				
				XSSFCell celln15 = rown.createCell(15);
				celln15.setCellValue(pdMx.getString("IS_OUTPUT"));
				celln15.setCellStyle(style3);
				
				XSSFCell celln16 = rown.createCell(16);
				celln16.setCellValue(pdMx.getString("START_TIME"));
				celln16.setCellStyle(style3);
				
				XSSFCell celln17 = rown.createCell(17);
				celln17.setCellValue(pdMx.getString("END_TIME"));
				celln17.setCellStyle(style3);
				
				XSSFCell celln18 = rown.createCell(18);
				celln18.setCellValue(pdMx.get("SPACTUAL_HOUR")==null?"0.00":pdMx.get("SPACTUAL_HOUR").toString());
				celln18.setCellStyle(style3);
				String FTYPE=pdMx.getString("FTYPE");
				int start=head0.length;
				int orderStart=Integer.parseInt(pdMx.get("orderStart")==null?"0":pdMx.get("orderStart").toString());
				int orderEnd=Integer.parseInt(pdMx.get("orderEnd")==null?"0":pdMx.get("orderEnd").toString());
				if(FTYPE.equals("A") && pdMx.getString("START_TIMEX") != null && !pdMx.getString("START_TIMEX").equals("")) {//最新计划时间
					for(int celli=start+orderStart;celli<=start+orderEnd;celli++) {
						XSSFCell cellni = rown.createCell(celli);
						cellni.setCellValue(""+row0.getCell(celli));
						cellni.setCellStyle(style4);
					}
				}else if(FTYPE.equals("B1") && pdMx.getString("START_TIMEX") != null && !pdMx.getString("START_TIMEX").equals("")) {//实际时间提前完成
					for(int celli=start+orderStart;celli<=start+orderEnd;celli++) {
						XSSFCell cellni = rown.createCell(celli);
						cellni.setCellValue(""+row0.getCell(celli));
						cellni.setCellStyle(style6);
					}
				}else if(FTYPE.equals("B2") && pdMx.getString("START_TIMEX") != null && !pdMx.getString("START_TIMEX").equals("")) {//实际时间延期
					for(int celli=start+orderStart;celli<=start+orderEnd;celli++) {
						XSSFCell cellni = rown.createCell(celli);
						cellni.setCellValue(""+row0.getCell(celli));
						cellni.setCellStyle(style7);
					}
				}else if(FTYPE.equals("B3") && pdMx.getString("START_TIMEX") != null && !pdMx.getString("START_TIMEX").equals("")) {//实际时间正常
					for(int celli=start+orderStart;celli<=start+orderEnd;celli++) {
						XSSFCell cellni = rown.createCell(celli);
						cellni.setCellValue(""+row0.getCell(celli));
						cellni.setCellStyle(style5);
					}
				}else if(FTYPE.equals("C") && pdMx.getString("START_TIMEX") != null && !pdMx.getString("START_TIMEX").equals("")) {//初始计划时间
					for(int celli=start+orderStart;celli<=start+orderEnd;celli++) {
						XSSFCell cellni = rown.createCell(celli);
						cellni.setCellValue(""+row0.getCell(celli));
						cellni.setCellStyle(style9);
					}
				}else if(FTYPE.equals("D") && pdMx.getString("START_TIMEX") != null && !pdMx.getString("START_TIMEX").equals("")) {//多次变更时间
					for(int celli=start+orderStart;celli<=start+orderEnd;celli++) {
						XSSFCell cellni = rown.createCell(celli);
						cellni.setCellValue(""+row0.getCell(celli));
						cellni.setCellStyle(style8);
					}
				}
			}
	}
}
