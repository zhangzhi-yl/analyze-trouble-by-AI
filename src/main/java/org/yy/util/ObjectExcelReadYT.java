package org.yy.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yy.entity.PageData;
/**
 * 从EXCEL导入到数据库
 * 创建人：
 * 创建时间：2014年12月23日
 * @version
 */
public class ObjectExcelReadYT {

	private static final TreeSet Integer = null;
	/**
	 * 采购申请模板导入
	 * @param filepath //文件路径
	 * @param filename //文件名
	 * @param startrow //开始行号
	 * @param startcol //开始列号
	 * @param sheetnum //sheet
	 * @return list
	 */
	public static List<Object> readExcelMA(String filepath, String filename, int startrow, int startcol, int sheetnum,int endcol) {
		List<Object> varList = new ArrayList<Object>();
		try {
			File target = new File(filepath, filename);
			FileInputStream fi = new FileInputStream(target);
		    XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
		    XSSFSheet sheet= wb.getSheetAt(sheetnum); //sheet 从0开始
		    boolean flg=false;
			int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号
			for (int i = startrow; i < rowNum; i++) {					//行循环开始
				PageData varpd = new PageData();
				XSSFRow row = sheet.getRow(i); 							//行
				for (int j = startcol; j < endcol; j++) {				//列循环开始
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
					if(cellValue!=null){
						if(cellValue.trim().equals("end")){
							flg=true;
							break;
						}
					}
					varpd.put("var"+j, cellValue);
				}
				if(flg){
					break;
				}
				varList.add(varpd);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return varList;
	}
	public static List<Object> readExcelMAZ(String filepath, String filename, int startrow, int startcol, int sheetnum,int endcol) {
		List<Object> varList = new ArrayList<Object>();
		List<Object> varList1 = new ArrayList<Object>();//返回主数据
		try {
			File target = new File(filepath, filename);
			FileInputStream fi = new FileInputStream(target);
		    XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
		    XSSFSheet sheet= wb.getSheetAt(sheetnum); //sheet 从0开始
		    boolean flg=false;
			int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号
			for (int i = 1; i < rowNum; i++) {					//行循环开始
				PageData varpd = new PageData();
				XSSFRow row = sheet.getRow(i); 							//行
				XSSFCell cellIF = row.getCell(0);
				if (cellIF != null) {
					cellIF.setCellType(1);
					String cellValueIF = cellIF.getStringCellValue();
					if(cellValueIF.equals("")){
					}
				}else{
				}
				for (int j = startcol; j <= endcol; j++) {				//列循环开始
					XSSFCell cell = row.getCell(j);
					String cellValue;
					try {
						cellValue = null;
						if (cell != null) {
							cell.setCellType(1);
							cellValue = cell.getStringCellValue();
						} else {
							cellValue = "";
						}
					} catch (Exception e) {
						cellValue = "";
					}
					if(cellValue!=null){
						if(cellValue.trim().equals("end")){
							flg=true;
							continue;
						}
					}
					varpd.put("var"+j, cellValue);
				}
				if(flg){
					break;
				}
				varList.add(varpd);
			}
			//--------------------------------------------------
			int NumsHig=varList.size();//excel导入条数总数
			int endrow=varList.size();//excel导入条数总数
			System.out.println(varList.size());
			int[] getNums=getCombineCellList(sheet,endrow,endcol);
			int intsHig=getNums[getNums.length-1];//合并单元格最后一个行数
			for (int i = 0; i < getNums.length; i++) {
				PageData vars1 = (PageData) varList.get(getNums[i]);
				if(intsHig==getNums[i]){//最后一个合并单元数量
					if((NumsHig-1)>intsHig){
						int intQJ=(NumsHig-1)-intsHig;
						List<Object> varList2 = new ArrayList<Object>();//F2
						for (int j = 1; j <= intQJ; j++) {
							PageData vars2 = (PageData) varList.get(getNums[i]+j);
							varList2.add(vars2);
						}
						vars1.put("FDs", varList2);
					}
				}else{
					int intQJ=(getNums[i+1]-getNums[i])-1;
					List<Object> varList2 = new ArrayList<Object>();//F2
					for (int j = 1; j <= intQJ; j++) {
						PageData vars2 = (PageData) varList.get(getNums[i]+j);
						varList2.add(vars2);
					}
					vars1.put("FDs", varList2);
				}
				varList1.add(vars1);
			}
			//-----------------------------------------------------------
		} catch (Exception e) {
			System.out.println(varList.size());
			System.out.println(e);
		}
		return varList1;
	}
	
	
	public static List<Object> readExcelMAZCOM(String filepath, String filename, int startrow, int startcol, int sheetnum,int endcol) {
		List<Object> varList = new ArrayList<Object>();
		try {
			File target = new File(filepath, filename);
			FileInputStream fi = new FileInputStream(target);
		    XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
		    XSSFSheet sheet= wb.getSheetAt(sheetnum); //sheet 从0开始
		    boolean flg=false;
			int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号
			for (int i = 1; i < rowNum; i++) {					//行循环开始
				PageData varpd = new PageData();
				XSSFRow row = sheet.getRow(i); 							//行
				XSSFCell cellIF = row.getCell(0);
				if (cellIF != null) {
					cellIF.setCellType(1);
					String cellValueIF = cellIF.getStringCellValue();
					if(cellValueIF.equals("")){
					}
				}else{
					
				}
				for (int j = startcol; j <= endcol; j++) {				//列循环开始
					XSSFCell cell = row.getCell(j);
					String cellValue;
					try {
						cellValue = null;
						if (cell != null) {
							cell.setCellType(1);
							cellValue = cell.getStringCellValue();
						} else {
							cellValue = "";
						}
					} catch (Exception e) {
						cellValue = "";
					}
					if(cellValue!=null){
						if(cellValue.trim().equals("end")){
							flg=true;
							continue;
						}
					}
					varpd.put("var"+j, cellValue);
				}
				if(flg){
					break;
				}
				varList.add(varpd);
			}
			
		} catch (Exception e) {
			System.out.println(varList.size());
			System.out.println(e);
		}
		return varList;
	}
	
	/**
	 * 方案模板导入
	 * @param filepath //文件路径
	 * @param filename //文件名
	 * @param startrow //开始行号
	 * @param startcol //开始列号
	 * @param sheetnum //sheet
	 * @return list
	 */
	public static List<Object> readExcelFA(String filepath, String filename, int startrow, int startcol, int sheetnum,int endcol) {
		List<Object> varList = new ArrayList<Object>();
		List<Object> varList1 = new ArrayList<Object>();//返回主数据
		int detail=0;
		try {
			File target = new File(filepath, filename);
			FileInputStream fi = new FileInputStream(target);
		    XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
		    XSSFSheet sheet= wb.getSheetAt(sheetnum); //sheet 从0开始
		    boolean flg=false;
			int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号
			detail = findRow(sheet, "detail:");
			if(detail==-1){//没找到detail:
				return varList1;
			}else{
				startrow=detail+1;
			}
			for (int i = startrow; i < rowNum; i++) {					//行循环开始
				PageData varpd = new PageData();
				XSSFRow row = sheet.getRow(i); 							//行
				XSSFCell cellIF = row.getCell(0);
				if (cellIF != null) {
					cellIF.setCellType(1);
					String cellValueIF = cellIF.getStringCellValue();
				}else{
					continue;
				}
				for (int j = startcol; j <= endcol; j++) {				//列循环开始
					XSSFCell cell = row.getCell(j);
					String cellValue;
					try {
						cellValue = null;
						if (cell != null) {
							cell.setCellType(1);
							cellValue = cell.getStringCellValue();
						} else {
							cellValue = "";
						}
					} catch (Exception e) {
						cellValue = "";
					}
					if(cellValue!=null){
						if(cellValue.trim().equals("end:")){
							flg=true;
							continue;
						}
					}
					varpd.put("var"+j, cellValue);
				}
				if(flg){
					break;
				}
				varList.add(varpd);
			}
			//--------------------------------------------------
			int NumsHig=varList.size();//excel导入条数总数
			int endrow=varList.size()+detail;//excel导入条数总数
			System.out.println(varList.size());
			int[] getNums=getCombineCellListFA(sheet,endrow,endcol,detail);//varList最后一行行号
			int intsHig=getNums[getNums.length-1];//合并单元格最后一个行数
			for (int i = 0; i < getNums.length; i++) {
				PageData vars1 = (PageData) varList.get(getNums[i]);
				if(intsHig==getNums[i]){//最后一个合并单元数量
					if((NumsHig-1)>intsHig){
						int intQJ=(NumsHig-1)-intsHig;
						List<Object> varList2 = new ArrayList<Object>();//F2
						for (int j = 2; j <= intQJ; j++) {
							PageData vars2 = (PageData) varList.get(getNums[i]+j);
							varList2.add(vars2);
						}
						vars1.put("FDs", varList2);
					}
				}else{
					int intQJ=(getNums[i+1]-getNums[i])-1;
					List<Object> varList2 = new ArrayList<Object>();//F2
					for (int j = 2; j <= intQJ; j++) {
						PageData vars2 = (PageData) varList.get(getNums[i]+j);
						varList2.add(vars2);
					}
					vars1.put("FDs", varList2);
				}
				varList1.add(vars1);
			}
		} catch (Exception e) {
			System.out.println(varList.size());
			System.out.println(e);
		}
		
		return varList1;
	}
	//获取合并单元格集合
	public static int[] getCombineCellListFA(XSSFSheet sheet,int endrow,int endcol,int detail)
	{
		List<CellRangeAddress> list = new ArrayList();
		//获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		ArrayList<Integer> array = new ArrayList<Integer>();
		//遍历所有的合并单元格
		for(int i = 0; i<sheetmergerCount;i++){
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();   
		    int lastColumn = ca.getLastColumn();   
		    int firstRow = ca.getFirstRow();   
		    int lastRow = ca.getLastRow();   
			if (endrow >= lastRow && firstRow > detail) {   
		         if (endcol >= lastColumn) {   
		        	 System.out.println(ca.getFirstRow()-detail-1);
		        	 array.add(new Integer(ca.getFirstRow()-detail-1));   
		        }   
		   }   
		}
		Object[] rowNums=array.toArray();
		 int[] getrowNums=getIntOrder(rowNums);
		return getrowNums;
	}
	//获取合并单元格集合
	public static int[] getCombineCellList(XSSFSheet sheet,int endrow,int endcol)
	{
		List<CellRangeAddress> list = new ArrayList();
		//获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		ArrayList<Integer> array = new ArrayList<Integer>();
		//遍历所有的合并单元格
		for(int i = 0; i<sheetmergerCount;i++){
			//获得合并单元格保存进list中
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();   
		    int lastColumn = ca.getLastColumn();   
		    int firstRow = ca.getFirstRow();   
		    int lastRow = ca.getLastRow();   
			if (endrow >= lastRow) {   
		         if (endcol >= lastColumn) {   
		        	 System.out.println(ca.getFirstRow()-1);
		        	 array.add(new Integer(ca.getFirstRow()-1));   
		        }   
		   }   
		}
		Object[] rowNums=array.toArray();
		 int[] getrowNums=getIntOrder(rowNums);
		return getrowNums;
	}
	public static int[]  getIntOrder(Object[] rowNums){
		 HashSet<Integer> hashSet = new HashSet<Integer>();
	        for (int i = 0; i < rowNums.length; i++){
	            hashSet.add((Integer) rowNums[i]);
	        }
	        Set<Integer> set = new TreeSet<Integer>(hashSet);
	        Integer[] integers = set.toArray(new Integer[]{});
	        int[] result = new int[integers.length];        
	        for (int i = 0; i < integers.length; i++){
	            result[i] = integers[i].intValue();
	        }
		return result;
	}
	public static List<Object> readExcel(String filepathfilename, int startrow, int startcol, int sheetnum) {
		List<Object> varList = new ArrayList<Object>();
		try {
			File target = new File(filepathfilename);
			FileInputStream fi = new FileInputStream(target);
		    XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
		    XSSFSheet sheet= wb.getSheetAt(sheetnum); //sheet 从0开始
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
	//查找具有字符串值的Excel单元格并获取其位置（行）
	private static int findRow(XSSFSheet sheet, String cellContent) {
	    for (Row row : sheet) {
	        for (Cell cell : row) {
	            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	                if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
	                    return row.getRowNum();  
	                }
	            }
	        }
	    }               
	    return -1;
	}
}
