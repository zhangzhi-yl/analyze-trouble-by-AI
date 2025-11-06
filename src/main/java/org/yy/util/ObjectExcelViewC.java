package org.yy.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.yy.entity.PageData;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * 说明：导出到EXCEL
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public class ObjectExcelViewC extends AbstractXlsView{
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Date date = new Date();
		String filename = DateUtil.date2Str(date, "yyyyMMddHHmmss");
		HSSFSheet sheet;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+filename+".xls");
		
		HSSFWorkbook book = (HSSFWorkbook) workbook;
		sheet = book.createSheet("sheet1");
		
		List<String> titles = (List<String>) model.get("titles");
		int len = titles.size();
		HSSFCellStyle headerStyle = book.createCellStyle(); //标题样式
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		HSSFFont headerFont = book.createFont();			//标题字体
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short)11);
		headerStyle.setFont(headerFont);
		short height=25*20;
		HSSFRow row = sheet.createRow(0);
		for(int i=0; i<len; i++){ //设置标题
			String title = titles.get(i);
			row.setRowStyle(headerStyle);
			row.createCell(i).setCellValue(title);  
		}
		sheet.getRow(0).setHeight(height);
		
		HSSFCellStyle contentStyle = book.createCellStyle(); //内容样式
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		
	
		List<PageData> varList = (List<PageData>) model.get("varList");
		int varCount = varList.size();
		for(int i=0; i<varCount-1; i++){
			PageData vpd = varList.get(i);
			HSSFRow rows = sheet.createRow(i+1); 
			for(int j=0;j<len;j++){
				String varstr = vpd.getString("var"+(j+1)) != null ? vpd.getString("var"+(j+1)) : "";
				rows.setRowStyle(contentStyle);
				if(j==8) {
					BigDecimal num1=new BigDecimal( vpd.getString("var"+8));
					BigDecimal num2=new BigDecimal( vpd.getString("var"+9));
					if(num1.compareTo(num2)==1) {
						  CellStyle style = workbook.createCellStyle();
						  style = workbook.createCellStyle();
					      style.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
					      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					      Cell cell = rows.createCell(j);
					      cell.setCellValue(varstr);
					      cell.setCellStyle(style);
					}else if(num1.compareTo(num2)==0) {
						  CellStyle style = workbook.createCellStyle();
						  style = workbook.createCellStyle();
					      style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
					      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					      Cell cell = rows.createCell(j);
					      cell.setCellValue(varstr);
					      cell.setCellStyle(style);
					}else if(num1.compareTo(num2)==-1) {
						  CellStyle style = workbook.createCellStyle();
						  style = workbook.createCellStyle();
					      style.setFillForegroundColor(IndexedColors.RED.getIndex());
					      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					      Cell cell = rows.createCell(j);
					      cell.setCellValue(varstr);
					      cell.setCellStyle(style);
					}
				}else {
					rows.createCell(j).setCellValue(varstr);
					
				}
				
				
			}
			
		}
		PageData epd = varList.get(varCount-1);
		HSSFRow rowe = sheet.createRow(varCount-1); 
		for(int k=0;k<len;k++){
			String varstr = epd.getString("var"+(k+1)) != null ? epd.getString("var"+(k+1)) : "";
			rowe.setRowStyle(headerStyle);
			rowe.createCell(k).setCellValue(varstr);
		}
	}



}
