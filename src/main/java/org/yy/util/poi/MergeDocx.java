package org.yy.util.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
public class MergeDocx {
	private XWPFParagraphBuilder paragraphBuilder;
	@SuppressWarnings("static-access")
	public boolean Merge( List<File> srcfile,File newFile){
		boolean result=true;
		try {
            OutputStream dest = new FileOutputStream(newFile);
            ArrayList<XWPFDocument> documentList = new ArrayList<>();
            XWPFDocument doc = null;
            ExportWord ew = new ExportWord();
            for (int i = 0; i < srcfile.size(); i++) {
                FileInputStream in = new FileInputStream(srcfile.get(i).getPath());
                OPCPackage open = OPCPackage.open(in);
                XWPFDocument document = new XWPFDocument(open);
                documentList.add(document);
            }
            for (int i = 0; i < documentList.size(); i++) {
                doc = documentList.get(0);
                if(i == 0){//首页直接分页，不再插入首页文档内容
                    //documentList.get(i).createParagraph().createRun().addBreak(BreakType.PAGE);
                    //appendBody(doc,documentList.get(i));
                }else if(i == documentList.size()-1){//尾页不再分页，直接插入最后文档内容
                    appendBody(doc,documentList.get(i));
                }else{
                	//XWPFParagraph p=documentList.get(i).createParagraph();
                	//p.createRun().addBreak(BreakType.PAGE);
                    appendBody(doc,documentList.get(i));
                  
                }
               
            }
            ew.setPageMar(doc);
            ew.createDefaultFooter(doc);
            doc.write(dest);
           // System.out.println("*****合成成功********");
           // Runtime.getRuntime().exec("cmd /c start winword E:/test/Paticulars.docx");//直接调用cmd打开合成文档
        } catch (Exception e) {
            e.printStackTrace();
            result=false;
        }
		return result;
	}
	 public static void appendBody(XWPFDocument src, XWPFDocument append) throws Exception {	
	        CTBody src1Body = src.getDocument().getBody();
	        CTBody src2Body = append.getDocument().getBody();
	        List<XWPFPictureData> allPictures = append.getAllPictures();
	        // 记录图片合并前及合并后的ID
	        Map<String,String> map = new HashMap<String,String>();
	        for (XWPFPictureData picture : allPictures) {
	            String before = append.getRelationId(picture);
	            //将原文档中的图片加入到目标文档中
	            String after = src.addPictureData(picture.getData(), Document.PICTURE_TYPE_PNG);
	            map.put(before, after);
	        }
	        appendBody(src1Body, src2Body,map);
	        //appendBody(src1Body, src2Body);
	    }

	    private static void appendBody(CTBody src, CTBody append,Map<String,String> map) throws Exception {
	        XmlOptions optionsOuter = new XmlOptions();
	        optionsOuter.setSaveOuter();
	        String appendString = append.xmlText(optionsOuter);

	        String srcString = src.xmlText();
	        String prefix = srcString.substring(0,srcString.indexOf(">")+1);
	        String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
	        String sufix = srcString.substring( srcString.lastIndexOf("<") );
	        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
	        if (map != null && !map.isEmpty()) {
	            //对xml字符串中图片ID进行替换
	            for (Map.Entry<String, String> set : map.entrySet()) {
	                addPart = addPart.replace(set.getKey(), set.getValue());
	            }
	        }
	        //将两个文档的xml内容进行拼接
	        CTBody makeBody = CTBody.Factory.parse(prefix+mainPart+addPart+sufix);
	        src.set(makeBody);
	    }
	    
	    @SuppressWarnings("unused")
		private static void appendBody(CTBody src, CTBody append) throws Exception {
	        XmlOptions optionsOuter = new XmlOptions();
	        optionsOuter.setSaveOuter();
	        String appendString = append.xmlText(optionsOuter);
	        String srcString = src.xmlText();
	        String prefix = srcString.substring(0,srcString.indexOf(">")+1);
	        String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
	        String sufix = srcString.substring( srcString.lastIndexOf("<") );
	        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
	        CTBody makeBody = CTBody.Factory.parse(prefix+mainPart+addPart+sufix);
	        src.set(makeBody);
	    }
	    public CTPPr getPrOfParagraph(XWPFParagraph p) {
	        CTPPr pPr = null;
	        if (p.getCTP() != null) {
	            if (p.getCTP().getPPr() != null) {
	                pPr = p.getCTP().getPPr();
	            } else {
	                pPr = p.getCTP().addNewPPr();
	            }
	        }
	        return pPr;
	    }
}
