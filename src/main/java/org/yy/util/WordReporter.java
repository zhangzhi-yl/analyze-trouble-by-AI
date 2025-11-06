package org.yy.util;



import org.apache.poi.xwpf.usermodel.*;


import java.util.*;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class WordReporter {
	
	public static void changeTableText(XWPFDocument document, Map<String, Object> data) {
		       // 获取文件的表格
		        List<XWPFTable> tableList = document.getTables();
		 
		       // 循环所有需要进行替换的文本，进行替换
		        for (int i = 0; i < tableList.size(); i++) {
		            XWPFTable table = tableList.get(i);
		            if (checkText(table.getText())) {
		                 List<XWPFTableRow> rows = table.getRows();
		                // 遍历表格，并替换模板
		                 eachTable(document, rows, data);
		             }
		         }
		     }
			public static boolean checkText(String text) {
		        boolean check = false;
		         if (text.contains("$")) {
		            check = true;
		        }
		        return check;
		    }
			
			 public static void eachTable(XWPFDocument document, List<XWPFTableRow> rows, Map<String, Object> textMap) {
				          for (XWPFTableRow row : rows) {
				              List<XWPFTableCell> cells = row.getTableCells();
				              for (XWPFTableCell cell : cells) {
				                  // 判断单元格是否需要替换
				                  if (checkText(cell.getText())) {
				                      // System.out.println("cell:" + cell.getText());
				                      List<XWPFParagraph> paragraphs = cell.getParagraphs();
				                      for (XWPFParagraph paragraph : paragraphs) {
				                          List<XWPFRun> runs = paragraph.getRuns();
				                          for (XWPFRun run : runs) {
				  
				                              Object ob = changeValue(run.toString(), textMap);
				                              if (ob instanceof String) {
				                            	  if (textMap.containsKey(run.toString())) {
				                                      //System.out.println("run:" + run.toString() + "替换为" + (String)ob);
				                                      run.setText((String) ob, 0);
				                                  }
				                              }
				                          }
				                      }
				                  }
				              }
				          }
				      }
			 
			 public static Object changeValue(String value, Map<String, Object> textMap) {
				          Set<Map.Entry<String, Object>> textSets = textMap.entrySet();
				          Object valu = "";
				          for (Map.Entry<String, Object> textSet : textSets) {
				              // 匹配模板与替换值 格式${key}
				              String key = textSet.getKey();
				              if (value.contains(key)) {
				                  valu = textSet.getValue();
				              }
				          }
				          return valu;
				      }

			

	}
