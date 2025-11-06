package org.yy.controller.momp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yy.entity.PageData;


/**
 *
 * 项目名称：MOM2
 * 类名称：ProblemsReadExcel
 * 类描述：
 * 创建人：xuhaoyue
 * 创建时间：2019年10月27日 下午7:41:54
 * 修改人：cuiyu
 * 修改时间：2019年10月27日 下午7:41:54
 * 修改备注：
 * @version
 *
 */
public class ProblemsReadExcel {
	public static int  insertcquotationData1(String filepath,Map<String,String> map,int recordsNum,int leftNum) throws Exception{  
		/*
		 filepath:文件路径
		 map：固定位置的值
		 recordsNum：现有记录数
		 leftNum：模板原有记录数，现在是1
		 */
		/*把map null改成空字符串*/
		for (String key : map.keySet()) {
			if(null==map.get(key)){
				map.put(key, "");
			}
		   }
		
		File file = new File(filepath);
	    FileInputStream fi = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
        XSSFSheet sheet= wb.getSheetAt(0); //sheet 从0开始
        //设置会议主题1,1
        XSSFRow row = sheet.getRow(1); 
        XSSFCell cellcus=row.getCell(1);
        cellcus.setCellValue((String)map.get("THEME")); 
      //设置会议开始日期
        XSSFRow rowsp = sheet.getRow(2); 
        XSSFCell cellsp=rowsp.getCell(1);
        cellsp.setCellValue((String)map.get("BEGINDAY"));
        //设置会议结束日期
        XSSFRow row1 = sheet.getRow(2); 
        XSSFCell cellsale=row1.getCell(3);
        cellsale.setCellValue((String)map.get("ENDDAY")); 
        //设置会议地点
        XSSFRow row4 = sheet.getRow(3); 
        XSSFCell celltel=row4.getCell(1);
        celltel.setCellValue((String)map.get("LOCATION")); 
       //设置时间（开始-结束）
        XSSFRow row5 = sheet.getRow(4); 
        XSSFCell cellbt=row5.getCell(1);
        cellbt.setCellValue((String)map.get("FCTIME")); 
        //设置电话会议号
        XSSFRow row6 = sheet.getRow(4); 
        XSSFCell celldd=row6.getCell(3);
        celldd.setCellValue((String)map.get("TELNUMBER"));
      //设置会议发起人
        XSSFRow row7 = sheet.getRow(5); 
        XSSFCell cellsj=row7.getCell(1);
        cellsj.setCellValue((String)map.get("ORIGINATOR"));
      //设置网络邀请
        XSSFRow row8 = sheet.getRow(5); 
        XSSFCell cellsj8=row8.getCell(3);
        cellsj8.setCellValue((String)map.get("INTERNETINVITATION"));
      //设置编制人
        XSSFRow row9 = sheet.getRow(6); 
        XSSFCell cellsj9=row9.getCell(1);
        cellsj9.setCellValue((String)map.get("COMPILINGPERSON"));
      //设置审核人
        XSSFRow row10 = sheet.getRow(6); 
        XSSFCell cellsj10=row10.getCell(3);
        cellsj10.setCellValue((String)map.get("CHECKER"));
        //设置会议目标
        XSSFRow row11 = sheet.getRow(7); 
        XSSFCell cellsj11=row11.getCell(1);
        cellsj11.setCellValue((String)map.get("TARGET"));
       
			
        FileOutputStream out=new FileOutputStream(filepath);  
        
        wb.write(out);    
        out.close();   
        wb.close();
        
        
	    return 1;  //复制成功返回1
}
	public static int  copyExcelCQUOTemplate1(String filepath,String newfilepath,int recordNums) throws Exception{
		
		 File file = new File(filepath);
		 FileInputStream fi = new FileInputStream(file);
	        XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
	        XSSFSheet sheet= wb.getSheetAt(0); //sheet 从0开始 
	        if(recordNums>1)//需要在模板里面插入数据
	        {
	        	
	        	insertRow( wb,sheet,11,recordNums-1);  //从第实际的第8行下面开始插入
	        
	        }
	        FileOutputStream out=new FileOutputStream(newfilepath);  //向d://test.xls中写数据  
	      
	        wb.write(out);    
	        out.close();   
	        wb.close();
		
		return 1;///拷贝成功返回1
	}
	public static int  copyExcelCQUOTemplate2(String filepath,String newfilepath2,int recordNumt,List<PageData> varlist) throws Exception{
		
		 File file = new File(filepath);
		 FileInputStream fi = new FileInputStream(file);
	        XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
	        XSSFSheet sheet= wb.getSheetAt(0); //sheet 从0开始 	
	        try {
	        	if(varlist.size()>0){
	        		int t=0;
//	        		if(recordNumt>1){
//	        		insertRow(wb,sheet,1,recordNumt-1); 
//	        		}
	        		for (Iterator iterator = varlist.iterator(); iterator.hasNext();) {
	        			PageData pageData = (PageData) iterator.next();
	        			 XSSFRow rowxh;
		        		    if(recordNumt<=1){
		        		    	rowxh = sheet.getRow(1);
		        		    }else{
		        		    	rowxh = sheet.getRow(1+t); 
		        		    }
		        		if(rowxh==null) {
		        			
		        			rowxh = sheet.createRow(t+1);
//		        			XSSFCell cell1=rowxh.getCell(0);
		        			rowxh.createCell(0).setCellValue( pageData.getString("SERIALNUMBER")!= null ? pageData.getString("SERIALNUMBER"):"");
//		        			XSSFCell cell2=rowxh.getCell(1);
		        			rowxh.createCell(1).setCellValue( pageData.getString("DEPARTMENTNAME")!= null ? pageData.getString("DEPARTMENTNAME"):"");
//		        			XSSFCell cell3=rowxh.getCell(2);
		        			rowxh.createCell(2).setCellValue( pageData.getString("QUESTIONTYPE")!= null ? pageData.getString("QUESTIONTYPE"):"");
//		        			XSSFCell cell4=rowxh.getCell(3);
		        			rowxh.createCell(3).setCellValue( pageData.getString("PROBLEMSTAGE")!= null ? pageData.getString("PROBLEMSTAGE"):"");
//		        			XSSFCell cell5=rowxh.getCell(4);
		        			rowxh.createCell(4).setCellValue( pageData.getString("PROPOSEPEOPLE")!= null ? pageData.getString("PROPOSEPEOPLE"):"");
//		        			XSSFCell cell6=rowxh.getCell(5);
		        			rowxh.createCell(5).setCellValue( pageData.getString("PROPOSETIME")!= null ? pageData.getString("PROPOSETIME"):"");
//		        			XSSFCell cell7=rowxh.getCell(6);
		        			rowxh.createCell(6).setCellValue( pageData.getString("PROBLEMNAME")!= null ? pageData.getString("PROBLEMNAME"):"");
//		        			XSSFCell cell8=rowxh.getCell(7);
		        			rowxh.createCell(7).setCellValue( pageData.getString("PROBLEMDESCRIPTION")!= null ? pageData.getString("PROBLEMDESCRIPTION"):"");
//		        			XSSFCell cell9=rowxh.getCell(8);
		        			rowxh.createCell(8).setCellValue( pageData.getString("PRIORITY")!= null ? pageData.getString("PRIORITY"):"");
//		        			XSSFCell cell10=rowxh.getCell(9);
		        			rowxh.createCell(9).setCellValue( pageData.getString("INFLUENCE")!= null ? pageData.getString("INFLUENCE"):"");
//		        			XSSFCell cell11=rowxh.getCell(10);
		        			rowxh.createCell(10).setCellValue( pageData.getString("COMPLEXITY")!= null ? pageData.getString("COMPLEXITY"):"");
//		        			XSSFCell cell12=rowxh.getCell(11);
		        			rowxh.createCell(11).setCellValue( pageData.getString("IMPACTDESCRIPTION")!= null ? pageData.getString("IMPACTDESCRIPTION"):"");
//		        			XSSFCell cell13=rowxh.getCell(12);
		        			rowxh.createCell(12).setCellValue( pageData.getString("STOPTIME")!= null ? pageData.getString("STOPTIME"):"");
		        			rowxh.createCell(13).setCellValue( pageData.getString("SOLUTIONDESCRIPTION")!= null ? pageData.getString("SOLUTIONDESCRIPTION"):"");
//		        			XSSFCell cell14=rowxh.getCell(13);
		        			rowxh.createCell(14).setCellValue( pageData.getString("PERSONINCHARGE")!= null ? pageData.getString("PERSONINCHARGE"):"");
//		        			XSSFCell cell15=rowxh.getCell(14);
		        			rowxh.createCell(15).setCellValue( pageData.getString("FSTATE")!= null ? pageData.getString("FSTATE"):"");
//		        			XSSFCell cell16=rowxh.getCell(15);
		        			rowxh.createCell(16).setCellValue( pageData.getString("CURRENTPROGRESS")!= null ? pageData.getString("CURRENTPROGRESS"):"");
//		        			XSSFCell cell17=rowxh.getCell(16);
		        			rowxh.createCell(17).setCellValue( pageData.getString("SETTLINGTIME")!= null ? pageData.getString("SETTLINGTIME"):"");
//		        			XSSFCell cell18=rowxh.getCell(17);
		        			rowxh.createCell(18).setCellValue( pageData.getString("RESULTDESCRIPTION")!= null ? pageData.getString("RESULTDESCRIPTION"):"");
//		        			XSSFCell cell19=rowxh.getCell(18);
		        			rowxh.createCell(19).setCellValue( pageData.getString("AUDITOR")!= null ? pageData.getString("AUDITOR"):"");
//		        			XSSFCell cell20=rowxh.getCell(19);
		        			rowxh.createCell(20).setCellValue( pageData.getString("REMARKS")!= null ? pageData.getString("REMARKS"):"");
		        			
		        		}else {
		        			XSSFCell cell1=rowxh.getCell(0);
		        			cell1.setCellValue( pageData.getString("SERIALNUMBER")!= null ? pageData.getString("SERIALNUMBER"):"");
		        			XSSFCell cell2=rowxh.getCell(1);
		        			cell2.setCellValue( pageData.getString("DEPARTMENTNAME")!= null ? pageData.getString("DEPARTMENTNAME"):"");
		        			XSSFCell cell3=rowxh.getCell(2);
		        			cell3.setCellValue( pageData.getString("QUESTIONTYPE")!= null ? pageData.getString("QUESTIONTYPE"):"");
		        			XSSFCell cell4=rowxh.getCell(3);
		        			cell4.setCellValue( pageData.getString("PROBLEMSTAGE")!= null ? pageData.getString("PROBLEMSTAGE"):"");
		        			XSSFCell cell5=rowxh.getCell(4);
		        			cell5.setCellValue( pageData.getString("PROPOSEPEOPLE")!= null ? pageData.getString("PROPOSEPEOPLE"):"");
		        			XSSFCell cell6=rowxh.getCell(5);
		        			cell6.setCellValue( pageData.getString("PROPOSETIME")!= null ? pageData.getString("PROPOSETIME"):"");
		        			XSSFCell cell7=rowxh.getCell(6);
		        			cell7.setCellValue( pageData.getString("PROBLEMNAME")!= null ? pageData.getString("PROBLEMNAME"):"");
		        			XSSFCell cell8=rowxh.getCell(7);
		        			cell8.setCellValue( pageData.getString("PROBLEMDESCRIPTION")!= null ? pageData.getString("PROBLEMDESCRIPTION"):"");
		        			XSSFCell cell9=rowxh.getCell(8);
		        			cell9.setCellValue( pageData.getString("PRIORITY")!= null ? pageData.getString("PRIORITY"):"");
		        			XSSFCell cell10=rowxh.getCell(9);
		        			cell10.setCellValue( pageData.getString("INFLUENCE")!= null ? pageData.getString("INFLUENCE"):"");
		        			XSSFCell cell11=rowxh.getCell(10);
		        			cell11.setCellValue( pageData.getString("COMPLEXITY")!= null ? pageData.getString("COMPLEXITY"):"");
		        			XSSFCell cell12=rowxh.getCell(11);
		        			cell12.setCellValue( pageData.getString("IMPACTDESCRIPTION")!= null ? pageData.getString("IMPACTDESCRIPTION"):"");
		        			XSSFCell cell13=rowxh.getCell(12);
		        			cell13.setCellValue( pageData.getString("STOPTIME")!= null ? pageData.getString("STOPTIME"):"");
		        			XSSFCell cell14=rowxh.getCell(13);
		        			cell14.setCellValue( pageData.getString("SOLUTIONDESCRIPTION")!= null ? pageData.getString("SOLUTIONDESCRIPTION"):"");
		        			XSSFCell cell15=rowxh.getCell(14);
		        			cell15.setCellValue( pageData.getString("PERSONINCHARGE")!= null ? pageData.getString("PERSONINCHARGE"):"");
		        			XSSFCell cell16=rowxh.getCell(15);
		        			cell16.setCellValue( pageData.getString("FSTATE")!= null ? pageData.getString("FSTATE"):"");
		        			XSSFCell cell17=rowxh.getCell(16);
		        			cell17.setCellValue( pageData.getString("CURRENTPROGRESS")!= null ? pageData.getString("CURRENTPROGRESS"):"");
		        			XSSFCell cell18=rowxh.getCell(17);
		        			cell18.setCellValue( pageData.getString("SETTLINGTIME")!= null ? pageData.getString("SETTLINGTIME"):"");
		        			XSSFCell cell19=rowxh.getCell(18);
		        			cell19.setCellValue( pageData.getString("RESULTDESCRIPTION")!= null ? pageData.getString("RESULTDESCRIPTION"):"");
		        			XSSFCell cell20=rowxh.getCell(19);
		        			cell20.setCellValue( pageData.getString("AUDITOR")!= null ? pageData.getString("AUDITOR"):"");
		        			XSSFCell cell21=rowxh.getCell(20);
		        			cell21.setCellValue( pageData.getString("REMARKS")!= null ? pageData.getString("REMARKS"):"");	
		        		}
	        			t++;
	        		}   
	        	}
	        } catch (Exception e) {
				e.printStackTrace();
			}
	        FileOutputStream out=new FileOutputStream(newfilepath2);  //向d://test.xls中写数据  
	      
	        wb.write(out);    
	        out.close();   
	        wb.close();
		
		return 1;///拷贝成功返回1
	}

	
	public static void insertRow(XSSFWorkbook wb, XSSFSheet sheet, int starRow,int rows) {

        //  Parameters:
		//   startRow - the row to start shiftingx
		//   endRow - the row to end shifting
		//   n - the number of rows to shift
		//   copyRowHeight - whether to copy the row height during the shift
		//   resetOriginalRowHeight - whether to set the original row's height to the default 
	
	sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows,true,false);
	
	  
	  starRow = starRow - 1;

	  for (int i = 0; i < rows; i++) {

	   XSSFRow sourceRow = null;
	   XSSFRow targetRow = null;
	   XSSFCell sourceCell = null;
	   XSSFCell targetCell = null;
	   short m;

	   starRow = starRow + 1;
	   sourceRow = sheet.getRow(starRow);
	   targetRow = sheet.createRow(starRow + 1);
	   targetRow.setRowStyle(sourceRow.getRowStyle());
	   for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++) {

	    sourceCell = sourceRow.getCell(m);
	    targetCell = targetRow.createCell(m);
	  
	    targetCell.setCellStyle(sourceCell.getCellStyle());
	    targetCell.setCellValue("");

	   }
	   	CellRangeAddress region = new CellRangeAddress(10+i+1, 10+i+1, 0, 1);
	    sheet.addMergedRegion(region);
	    CellRangeAddress region2 = new CellRangeAddress(10+i+1, 10+i+1, 2, 3);
	    sheet.addMergedRegion(region2);
	  }

	 }
	public static void insertRow2(XSSFWorkbook wb, XSSFSheet sheet, int starRow,int rows) {

        //  Parameters:
		//   startRow - the row to start shiftingx
		//   endRow - the row to end shifting
		//   n - the number of rows to shift
		//   copyRowHeight - whether to copy the row height during the shift
		//   resetOriginalRowHeight - whether to set the original row's height to the default 
	sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows,true,false);
	
	  
	  starRow = starRow - 1;

	  for (int i = 0; i < rows; i++) {

	   XSSFRow sourceRow = null;
	   XSSFRow targetRow = null;
	   XSSFCell sourceCell = null;
	   XSSFCell targetCell = null;
	   short m;

	   starRow = starRow + 1;
	   sourceRow = sheet.getRow(starRow);
	   targetRow = sheet.createRow(starRow + 1);
	   targetRow.setRowStyle(sourceRow.getRowStyle());

	   for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++) {

	    sourceCell = sourceRow.getCell(m);
	    targetCell = targetRow.createCell(m);
	  
	    targetCell.setCellStyle(sourceCell.getCellStyle());
	    targetCell.setCellValue("");

	   }
	  }

	 }
	public static void insertRow3(XSSFWorkbook wb, XSSFSheet sheet, int starRow,int rows) {

        //  Parameters:
		//   startRow - the row to start shiftingx
		//   endRow - the row to end shifting
		//   n - the number of rows to shift
		//   copyRowHeight - whether to copy the row height during the shift
		//   resetOriginalRowHeight - whether to set the original row's height to the default 
		sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows,true,false);
	
	  
	  starRow = starRow - 1;

	  for (int i = 0; i < rows; i++) {

	   XSSFRow sourceRow = null;
	   XSSFRow targetRow = null;
	   XSSFCell sourceCell = null;
	   XSSFCell targetCell = null;
	   short m;

	   starRow = starRow + 1;
	   sourceRow = sheet.getRow(starRow);
	   targetRow = sheet.createRow(starRow + 1);
	   targetRow.setRowStyle(sourceRow.getRowStyle());

	   for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++) {

	    sourceCell = sourceRow.getCell(m);
	    targetCell = targetRow.createCell(m);
	  
	    targetCell.setCellStyle(sourceCell.getCellStyle());
	    targetCell.setCellValue("");

	   }
//	   CellRangeAddress region = new CellRangeAddress(starRow+1+i, starRow+1+i, 1, 3);
//	    sheet.addMergedRegion(region);
	  }

	 }
	
}
