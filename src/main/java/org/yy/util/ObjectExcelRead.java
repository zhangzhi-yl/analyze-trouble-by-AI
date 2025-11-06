package org.yy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yy.entity.PageData;


/**
 * 说明：从EXCEL导入到系统
 * 作者：YuanYe Q356703572
 * 
 */
public class ObjectExcelRead {

	/**导入值校验
     * @param sheet 工作表
     * @param row 行
     * @param colNum 列编号
     * @param errorHint 错误提示
     * @return 校验通过返回空，否则抛出异常
     */
    public static void validCellValue(Sheet sheet,Row row,int colNum,String errorHint) {
        if("".equals(ObjectExcelRead.getCellValue(sheet, row, colNum - 1))) {
            throw new RuntimeException("校验 :第" + (row.getRowNum() + 1) + "行" + colNum +"列"+ errorHint + "不能为空");
        }
    }

    /**从输入流中获取excel工作表
     * @param iStream 输入流
     * @param fileName 带 .xls或.xlsx 后缀的文件名
     * @return 文件名为空返回空; 格式不正确抛出异常;  正常返回excel工作空间对象
     */
    public static Workbook getWorkbookByInputStream(InputStream iStream, String fileName) {
        Workbook workbook = null;
        
        try {
            if(null == fileName) {
                return null;
            }
            
            if(fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(iStream);
            }else if(fileName.endsWith(".xlsx")){
                workbook = new XSSFWorkbook(iStream);
            }else {
                throw new IOException("The document type don't support");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (iStream != null){
                try {
                    iStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return workbook;
    }
    
    /**从Workbook中获取一个sheet,如果没有就创建一个
     * @param workbook 工作空间
     * @param index 第几个sheet
     * @return 返回sheet
     */
    public static Sheet getSheetByWorkbook(Workbook workbook,int index) {
        Sheet sheet = workbook.getSheetAt(index);
        if(null == sheet) {
            sheet = workbook.createSheet();
        }
        
        sheet.setDefaultRowHeightInPoints(20);//行高
        sheet.setDefaultColumnWidth(20);//列宽
        
        return sheet;
    }
    
    /** 获取指定sheet指定row中指定column的cell值
     * @param sheet 工作表
     * @param row 行
     * @param column 第几列
     * @return 返回单元格的值或""
     */
    public static String getCellValue(Sheet sheet,Row row,int column) {
        if(sheet == null || row == null) {
            return "";
        }
        
        return ObjectExcelRead.getCellValue(row.getCell(column));
    }
    /** 从单元格中获取单元格的值
     * @param cell 单元格
     * @return 返回值或""
     */
    public static String getCellValue(Cell cell) {
        if(cell == null) {
            return "";
        }
        String cellValue="";
        switch(cell.getCellType()) {
	        case Cell.CELL_TYPE_NUMERIC:
	        	short format = cell.getCellStyle().getDataFormat();
	        	if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                SimpleDateFormat sdf = null;  
	                //System.out.println("cell.getCellStyle().getDataFormat()="+cell.getCellStyle().getDataFormat());
	                if (format == 20 || format == 32) {  
	                    sdf = new SimpleDateFormat("HH:mm");  
	                } else if (format == 14 || format == 31 || format == 57 || format == 58) {  
	                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
	                    sdf = new SimpleDateFormat("yyyy-MM-dd");  
	                    double value = cell.getNumericCellValue();  
	                    Date date = org.apache.poi.ss.usermodel.DateUtil  
	                            .getJavaDate(value);  
	                    cellValue = sdf.format(date);  
	                }else {// 日期  
	                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	                }  
	                try {
	                    cellValue = sdf.format(cell.getDateCellValue());// 日期
	                } catch (Exception e) {
	                    try {
	                        throw new Exception("exception on get date data !".concat(e.toString()));
	                    } catch (Exception e1) {
	                        e1.printStackTrace();
	                    }
	                }finally{
	                    sdf = null;
	                }
	            }  else {
	                BigDecimal bd = new BigDecimal(cell.getNumericCellValue()); 
	                cellValue = bd.toPlainString();// 数值 这种用BigDecimal包装再获取plainString，可以防止获取到科学计数值
	            }
	            return cellValue;
	        case Cell.CELL_TYPE_STRING:
	            return cell.getStringCellValue().trim();
	        case Cell.CELL_TYPE_FORMULA://公式
	            return "";
	        case Cell.CELL_TYPE_BLANK:
	            return "";
	        case Cell.CELL_TYPE_BOOLEAN:
	            return String.valueOf(cell.getBooleanCellValue());
	        default :
	            break;
        }
        
        return "";
    }
    
    /**判断该行是否为空行
     * @param row 行
     * @return 为空行返回true,不为空行返回false
     */
    public static boolean isBlankRow(Row row) {
        if(row == null) {
            return true;
        }
        
        Iterator<Cell> iter = row.cellIterator();
        while(iter.hasNext()) {
            Cell cell = iter.next();
            if(cell == null) {
                continue;
            }
            
            String value = ObjectExcelRead.getCellValue(cell);
            if(!ObjectExcelRead.isNULLOrBlank(value)) {
                return false;
            }
        }
        
        return true;
    }
    
    /** 判断一个对象是否为空
     * @param obj 对象
     * @return 为空返回true,不为空返回false
     */
    public static boolean isNULLOrBlank(Object obj) {
        if(obj != null && !"".equals(obj.toString())) {
            return false;
        }
        
        return true;
    }
	
	/**
	 * @param filepath //文件路径
	 * @param filename //文件名
	 * @param startrow //开始行号
	 * @param startcol //开始列号
	 * @param sheetnum //sheet
	 * @return list
	 */
	public static List<Object> readExcel(String filepath, String filename, int startrow, int startcol, int sheetnum) {
		List<Object> varList = new ArrayList<Object>();

		try {
			File target = new File(filepath, filename);
			FileInputStream fi = new FileInputStream(target);
			XSSFWorkbook wb = new XSSFWorkbook(fi);
			XSSFSheet sheet = wb.getSheetAt(sheetnum); 					//sheet 从0开始
			int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号

			for (int i = startrow; i < rowNum; i++) {					//行循环开始
				
				PageData varpd = new PageData();
				XSSFRow row = sheet.getRow(i); 							//行
				int cellNum = row.getLastCellNum(); 					//每行的最后一个单元格位置

				for (int j = startcol; j < cellNum; j++) {				//列循环开始

					XSSFCell cell = row.getCell(Short.parseShort(j + ""));
					String cellValue = null;
					if (null != cell) {
						switch (cell.getCellType()) { 					// 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
						case 0:
							cellValue = String.valueOf((int) cell.getNumericCellValue());
							break;
						case 1:
							cellValue = cell.getStringCellValue();
							break;
						case 2:
							cellValue = cell.getNumericCellValue() + "";
							// cellValue = String.valueOf(cell.getDateCellValue());
							break;
						case 3:
							cellValue = "";
							break;
						case 4:
							cellValue = String.valueOf(cell.getBooleanCellValue());
							break;
						case 5:
							cellValue = String.valueOf(cell.getErrorCellValue());
							break;
						}
					} else {
						cellValue = "";
					}
					
					varpd.put("var"+j, cellValue);
					
				}
				varList.add(varpd);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return varList;
	}
}
