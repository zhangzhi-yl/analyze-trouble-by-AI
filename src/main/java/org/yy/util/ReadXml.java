package org.yy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yy.entity.PageData;

public class ReadXml {
	/**
     * 读取标签
     * xml读取结果封装为list
     * @author cy
     */
	@SuppressWarnings("rawtypes")
	public static List<PageData> readXml(String filepath){
		 //创建XML 解释器对象
        SAXReader reader = new SAXReader();
        List<PageData> varlist = new ArrayList<>();
        try {
        	 //读取xml文件对象
            Document doc=reader.read(new File(filepath));
            Element rootElem=doc.getRootElement();
            List<Element> eList = rootElem.elements();
            for(Element e: eList) {
            	PageData pd=new PageData();
                String text =e.getTextTrim().replaceAll("\"", "");//将xml中双引号去掉
                String[] arr=text.split(" ");//按空格截取字符串
                for (String entry : arr) {
                	if (entry.contains("=")) {
                	   String[] sub = entry.split("\\=");//截取等号左右两端数据
                	   if (sub.length > 1) {
                            pd.put(sub[0], sub[1]);
                       } else {
                           pd.put(sub[0], "");
                       }
                   }
               }
                varlist.add(pd);  
            }
		  }catch (Exception e) {
			e.printStackTrace();
		  }
        return varlist;
	}

}
