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
 * 类名称：PurchaseOrderReadExcel
 * 类描述：
 * 创建人：cuiyu
 * 创建时间：2019年10月27日 下午7:41:54
 * 修改人：cuiyu
 * 修改时间：2019年10月27日 下午7:41:54
 * 修改备注：
 * @version
 *
 */
public class PurchaseOrderReadExcel {
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
	
	public static int  copyExcelCQUOTemplate2(String filepath,String newfilepath2,int recordNums,int recordNum,int recordNumt,int recordNumi,List<PageData> varlist,List<PageData> nvarlist,List<PageData> tvarlist,List<PageData> ivarlist) throws Exception{
		
		 File file = new File(filepath);
		 FileInputStream fi = new FileInputStream(file);
	        XSSFWorkbook wb = new XSSFWorkbook(fi);//xslx
	        XSSFSheet sheet= wb.getSheetAt(0); //sheet 从0开始 	
	        try {
	        	if(tvarlist.size()>0){
	        		int t=0;
	        		if(recordNumt>1){
	        		insertRow(wb,sheet,10,recordNumt-1); 
	        		}
	        		for (Iterator iterator = tvarlist.iterator(); iterator.hasNext();) {
	        			PageData pageData = (PageData) iterator.next();
	        			//姓名
	        			 XSSFRow rowxh;
		        		    if(recordNumt<=1){
		        		    	rowxh = sheet.getRow(10);
		        		    }else{
		        		    	rowxh = sheet.getRow(10+t); 
		        		    }
	        			XSSFCell cell1=rowxh.getCell(0);
	        			rowxh.setHeight((short)(20*20));
	        			cell1.setCellValue( pageData.getString("NAME")!= null ? pageData.getString("NAME"):"");
	        			//职位
	        			XSSFCell cell3=rowxh.getCell(2);
	        			rowxh.setHeight((short)(20*20));
	        			cell3.setCellValue( pageData.getString("POSITION")!= null ? pageData.getString("POSITION"):"");
	        			t++;
	        		}   
	        	}else{
	        		XSSFRow rowxh = sheet.getRow(10); 
        			XSSFCell cell1=rowxh.getCell(0);
        			cell1.setCellValue("");
        			XSSFCell cell3=rowxh.getCell(2);
        			cell3.setCellValue("");
	        	}
	        	//会议议程
	        	if(nvarlist.size()>0){
	        		int i=0;
	        		if(recordNum>1){
	        			if(recordNumt<=1){
	        				insertRow2(wb,sheet,13,recordNum-1); 
	        			}else{
	        				insertRow2(wb,sheet,10+recordNumt+2,recordNum-1); 
	        			}
	        		
	        		}
	        		for (Iterator iterator = nvarlist.iterator(); iterator.hasNext();) {
	        			PageData pageData = (PageData) iterator.next();
	        			//会议时间
	        			XSSFRow rowxh;
	        		    if(recordNum<=1){
	        		    	if(recordNumt<=1){
	        		    		rowxh = sheet.getRow(13+i);
	        		    	}else{
	        		    		rowxh = sheet.getRow(10+recordNumt+2+i);
	        		    	}
	        		    }else{
	        		    	if(recordNumt<=1){
	        		    		rowxh = sheet.getRow(13+i);
	        		    	}else{
	        		    		rowxh = sheet.getRow(10+recordNumt+2+i); 
	        		    	}
	        		    	
	        		    }
	        			XSSFCell cell1=rowxh.getCell(0);
	        			rowxh.setHeight((short)(20*20));
	        			cell1.setCellValue(pageData.getString("TIME")!= null ? pageData.getString("TIME"):"");
	        			//议题
	        			XSSFCell cell3=rowxh.getCell(1);
	        			rowxh.setHeight((short)(20*20));
	        			cell3.setCellValue( pageData.getString("THEME")!= null ? pageData.getString("THEME"):"");
	        			//发言人
	        			XSSFCell cellbz=rowxh.getCell(3);
	        			rowxh.setHeight((short)(20*20));
	        			cellbz.setCellValue( pageData.getString("SPOKESMAN")!= null ? pageData.getString("SPOKESMAN"):"");
	        			if(recordNumt<=1){
	        				if(i!=0){
    	        				CellRangeAddress region = new CellRangeAddress(13+i, 13+i, 1, 2);
    		        			sheet.addMergedRegion(region);
    	        			}
        		    	}else{
        		    		if(i!=0){
    	        				CellRangeAddress region = new CellRangeAddress(10+recordNumt+2+i, 10+recordNumt+2+i, 1, 2);
    		        			sheet.addMergedRegion(region);
    	        			}
        		    	}
	        			
	        			i++;
	        		}   
	        	}else{
	        		XSSFRow rowxh = sheet.getRow(10+recordNumt+2); 
        			XSSFCell cell1=rowxh.getCell(0);
        			cell1.setCellValue("");
        			XSSFCell cell2=rowxh.getCell(1);
        			cell2.setCellValue("");
        			XSSFCell cell3=rowxh.getCell(3);
        			cell3.setCellValue("");
	        	}
	        	//会议讨论
	        	if(varlist.size()>0){
	        		int a=0;
	        		if(recordNums>1){
	        			if(recordNumt<=1&&recordNum<=1){
	        				insertRow3(wb,sheet,15,recordNums-1); 
	        			}else if(recordNumt<=1&&recordNum>1){//参会人员<=1 会议议程>1 recordNum
	        				insertRow3(wb,sheet,13+recordNum+1,recordNums-1); 
	        			}else if(recordNum<=1&&recordNumt>1){//参会人员>1 会议议程<=1 recordNumt
	        				insertRow3(wb,sheet,10+recordNumt+2+2,recordNums-1); 
	        			}else{
	        				insertRow3(wb,sheet,10+recordNumt+2+recordNum+1,recordNums-1); 
	        			}
	        		
	        		}
	        		for (Iterator iterator = varlist.iterator(); iterator.hasNext();) {
	        			PageData pageData = (PageData) iterator.next();
	        			
	        			XSSFRow rowxh;
	        		    if(recordNums<=1){
	        		    	if(recordNumt<=1&&recordNum<=1){
	        		    		rowxh = sheet.getRow(15+a);
	        		    	}else if(recordNumt<=1&&recordNum>1){//参会人员<=1 会议议程>1
	        		    		rowxh = sheet.getRow(13+recordNum+1+a);//
	        		    	}else if(recordNum<=1&&recordNumt>1){//参会人员>1 会议议程<=1
	        		    		rowxh = sheet.getRow(10+recordNumt+2+2+a);
	        		    	}else{
	        		    		rowxh = sheet.getRow(10+recordNumt+2+recordNum+1+a);
	        		    	}
	        		    }else{
	        		    	if(recordNumt<=1&&recordNum<=1){
	        		    		rowxh = sheet.getRow(15+a);
	        		    	}else if(recordNumt<=1&&recordNum>1){//参会人员<=1 会议议程>1
	        		    		rowxh = sheet.getRow(13+recordNum+1+a);//
	        		    	}else if(recordNum<=1&&recordNumt>1){//参会人员>1 会议议程<=1
	        		    		rowxh = sheet.getRow(10+recordNumt+2+2+a);
	        		    	}else{
	        		    		rowxh = sheet.getRow(10+recordNumt+2+recordNum+1+a);
	        		    	}
	        		    	
	        		    }
	        		    
	        		    XSSFCell cell1=rowxh.getCell(0);
	        		    rowxh.setHeight((short)(20*20));
	        			cell1.setCellValue(pageData.getString("FNO")!= null ? pageData.getString("FNO"):"");
	        			XSSFCell cell2=rowxh.getCell(1);
	        			rowxh.setHeight((short)(20*20));
	        			cell2.setCellValue(pageData.getString("CONTENT")!= null ? pageData.getString("CONTENT"):"");
	        			
	        		    if(recordNum<=1&&recordNumt<=1){
	        		    	if(a!=0){
	        		    		CellRangeAddress region = new CellRangeAddress(15+a, 15+a, 1, 3);
			            		sheet.addMergedRegion(region);
	        		    	}
	            			
	            		}else{
	            			if(a!=0){
		            			if(recordNumt<=1&&recordNum>1){//参会人员<=1 会议议程>1
		            				CellRangeAddress region = new CellRangeAddress(13+recordNum+1+a, 13+recordNum+1+a, 1, 3);
			            			sheet.addMergedRegion(region);
		            			}else if(recordNum<=1&&recordNumt>1){//参会人员>1 会议议程<=1
		            				CellRangeAddress region = new CellRangeAddress(10+recordNumt+2+2+a, 10+recordNumt+2+2+a, 1, 3);
			            			sheet.addMergedRegion(region);
		            			}else{
		            				CellRangeAddress region = new CellRangeAddress(10+recordNumt+2+recordNum+1+a, 10+recordNumt+2+recordNum+1+a, 1, 3);
			            			sheet.addMergedRegion(region);
		            			}
	            			}	
	            		}
	        			a++;
	        		}   
	        	}else{
//	        		XSSFRow rowxh = sheet.getRow(10+recordNumt+2+recordNum+1); 
//        			XSSFCell cell1=rowxh.getCell(0);
//        			cell1.setCellValue("");
//        			XSSFCell cell2=rowxh.getCell(1);
//        			cell2.setCellValue("");
        			
	        	}
	            if(ivarlist.size()>0){
	            	int j=0;
//	            	if(recordNums>1){
//	            	insertRow3(wb,sheet,10+recordNumt+2+recordNum+1,recordNums-1); 
//	            	}
	            	XSSFCellStyle style = wb.createCellStyle();
            		style.setAlignment(HorizontalAlignment.CENTER);
            		style.setBorderBottom(BorderStyle.THIN);
            		style.setBorderLeft(BorderStyle.THIN);
            		style.setBorderRight(BorderStyle.THIN);
            		style.setBorderTop(BorderStyle.THIN);
	            	for (Iterator iterator = ivarlist.iterator(); iterator.hasNext();) {
	            		PageData pageData = (PageData) iterator.next();
	            		//序号
	            		XSSFRow rowxh;
	        		    if(recordNumi<=1){
	        		    	if(recordNum<=1&&recordNumt<=1&&recordNums<=1){
	        		    		rowxh = sheet.getRow(18+j);
	        		    	}else{
	        		    		if(recordNumt<=1&&recordNum>1){//参会人员<=1 会议议程>1
	        		    			if(recordNums>1){
	        		    				rowxh = sheet.getRow(18+recordNum+recordNums-2+j);
	        		    			}else{
	        		    				rowxh = sheet.getRow(18+recordNum-1+j);
	        		    			}
		        		    		
		        		    	}else if(recordNum<=1&&recordNumt>1){//参会人员>1 会议议程<=1
		        		    		if(recordNums>1){
	        		    				rowxh = sheet.getRow(18+recordNumt+recordNums-2+j);
	        		    			}else{
	        		    				rowxh = sheet.getRow(18+recordNumt-1+j);
	        		    			}
		        		    	}else{
		        		    		rowxh = sheet.getRow(10+recordNumt+2+recordNum+1+recordNums+2+j);
		        		    	}
	        		    	}
	        		    	
	        		    }else{
	        		    	if(recordNum<=1&&recordNumt<=1){
	        		    		rowxh = sheet.getRow(18+j);
	        		    	}else{
	        		    		if(recordNumt<=1&&recordNum>1){//参会人员<=1 会议议程>1
	        		    			if(recordNums>1){
	        		    				rowxh = sheet.getRow(18+recordNum+recordNums-2+j);
	        		    			}else{
	        		    				rowxh = sheet.getRow(18+recordNum-1+j);
	        		    			}
		        		    		
		        		    	}else if(recordNum<=1&&recordNumt>1){//参会人员>1 会议议程<=1
		        		    		if(recordNums>1){
	        		    				rowxh = sheet.getRow(18+recordNumt+recordNums-2+j);
	        		    			}else{
	        		    				rowxh = sheet.getRow(18+recordNumt-1+j);
	        		    			}
		        		    	}else{
		        		    		rowxh = sheet.getRow(10+recordNumt+2+recordNum+1+recordNums+2+j);
		        		    	}
	        		    		
	        		    	}
	        		    	
	        		    }
	            		if(rowxh==null){
	            			if(recordNum<=1&&recordNumt<=1){
	            				rowxh = sheet.createRow(18+j);
	            			}else{
	            				if(recordNumt<=1&&recordNum>1){//参会人员<=1 会议议程>1
	            					if(recordNums>1){
	        		    				rowxh = sheet.createRow(18+recordNum+recordNums-2+j);
	        		    			}else{
	        		    				rowxh = sheet.createRow(18+recordNum-1+j);
	        		    			}
		        		    	}else if(recordNum<=1&&recordNumt>1){//参会人员>1 会议议程<=1
		        		    		if(recordNums>1){
	        		    				rowxh = sheet.createRow(18+recordNumt+recordNums-2+j);
	        		    			}else{
	        		    				rowxh = sheet.createRow(18+recordNumt-1+j);
	        		    			}
		        		    	}else{
		        		    		rowxh = sheet.createRow(10+recordNumt+2+recordNum+1+recordNums+2+j);
		        		    	}
	            			}
	            			rowxh.setHeight((short)(20*20));
	            			rowxh.createCell(0).setCellValue(pageData.getString("ACTIONPLAN")!= null ? pageData.getString("ACTIONPLAN"):"");
	            			XSSFCell cell1=rowxh.getCell(0);
		            		cell1.setCellStyle(style);
	            		}else{
	            			XSSFCell cell1=rowxh.getCell(0);
	            			cell1.setCellValue(pageData.getString("ACTIONPLAN")!= null ? pageData.getString("ACTIONPLAN"):"");
	            			
	            		}
	            		//内容
	            		if(rowxh.getCell(1)==null){
	            			rowxh.createCell(1).setCellValue( pageData.getString("HEAD")!= null ? pageData.getString("HEAD"):"");
	            			XSSFCell cell3=rowxh.getCell(1);
		            		cell3.setCellStyle(style);
	            		}else{
	            			XSSFCell cell3=rowxh.getCell(1);
	            			cell3.setCellValue( pageData.getString("HEAD")!= null ? pageData.getString("HEAD"):"");
	            		}
	            		
	            		if(rowxh.getCell(2)==null){
	            			rowxh.createCell(2).setCellValue( pageData.getString("DEADLINE")!= null ? pageData.getString("DEADLINE"):"");
	            			XSSFCell cell4=rowxh.getCell(2);
		            		cell4.setCellStyle(style);
	            		}else{
	            			XSSFCell cell4=rowxh.getCell(2);
	            			cell4.setCellValue( pageData.getString("DEADLINE")!= null ? pageData.getString("DEADLINE"):"");
	            		}
	            		
	            		if(rowxh.getCell(3)==null){
	            			rowxh.createCell(3).setCellValue( pageData.getString("FSTATE")!= null ? pageData.getString("FSTATE"):"");
	            			XSSFCell cell5=rowxh.getCell(3);
		            		cell5.setCellStyle(style);
	            		}else{
	            			XSSFCell cell5=rowxh.getCell(3);
	            			cell5.setCellValue( pageData.getString("FSTATE")!= null ? pageData.getString("FSTATE"):"");
	            		}
	            		
	            		
	            		j++;
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
	    targetCell.setCellValue("111188");

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
