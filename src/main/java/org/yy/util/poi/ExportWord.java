package org.yy.util.poi;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
//import com.spire.doc.Document;
//import com.spire.doc.Section;




/**
 * @Description 导出word文档
 * 
 * 
 */
public class ExportWord {
	/** 页脚样式 */
    public static final String STYLE_FOOTER = "footer";
    
    /** 页眉样式 */
    public static final String STYLE_HEADER = "header";
    /** 语言，简体中文 */
    public static final String LANG_ZH_CN = "zh-CN";
    private XWPFHelperTable xwpfHelperTable = null;    
    private XWPFHelper xwpfHelper = null;
    public ExportWord() {
        xwpfHelperTable = new XWPFHelperTable();
        xwpfHelper = new XWPFHelper();
    }
    /**
     * 创建好文档的基本 标题，表格  段落等部分
     * @return
     * 
     */
    public XWPFDocument createXWPFDocument(int rows,int cols,int colhbstart,int colhbend,int start,int end) {
        XWPFDocument doc = new XWPFDocument();
        setPageMar(doc);
        changeOrientation(doc,"landscape");
       // createTitleParagraph(doc);
        createTableParagraph(doc, rows, cols,colhbstart,colhbend,start,end);
       // createDefaultFooter(doc);
        return doc;
    }
    /*
     * 设置页边距
     */
    @SuppressWarnings("unused")
    public static void setPageMar(XWPFDocument doc){
    	CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(900L));
        pageMar.setTop(BigInteger.valueOf(800L));
        pageMar.setRight(BigInteger.valueOf(900L));
        pageMar.setBottom(BigInteger.valueOf(800L));
    }
    /**
     * 设置纸张横向
     * @param document
     * @param orientation
     */
    @SuppressWarnings("unused")
    public static void changeOrientation(XWPFDocument document, String orientation) {
    	 CTDocument1 doc = document.getDocument();
    	    CTBody body = doc.addNewBody();
    	    body.addNewSectPr();
    	    CTSectPr section = body.getSectPr();
        CTPageSz pageSize = section.isSetPgSz()? section.getPgSz() : section.addNewPgSz();
        if (orientation.equals("landscape")) {
            pageSize.setOrient(STPageOrientation.LANDSCAPE);
            pageSize.setW(BigInteger.valueOf(842 * 20));
            pageSize.setH(BigInteger.valueOf(595 * 20));
        } else {
            pageSize.setOrient(STPageOrientation.PORTRAIT);
            pageSize.setH(BigInteger.valueOf(842 * 20));
            pageSize.setW(BigInteger.valueOf(595 * 20));
        }
    }
//    public static void setparagraph(String filepath,String newfilename){
//    	Document doc = new Document();
//    	doc.loadFromFile(filepath);
//    	Section sec = doc.getSections().get(0);
//        sec.getParagraphs().get(0).getFormat().setLeftIndent(30f);//左缩进
//        doc.saveToFile(newfilename);
//        doc.dispose();
//    }
   
    /**
     * 设置页脚
     * @param docx
     */
    public static void createDefaultFooter(final XWPFDocument docx) {
        // TODO 设置页码起始值
//        CTP pageNo = CTP.Factory.newInstance();
//        XWPFParagraph footer = new XWPFParagraph(pageNo, docx);
//        CTPPr begin = pageNo.addNewPPr();
//        begin.addNewPStyle().setVal(STYLE_FOOTER);
//        begin.addNewJc().setVal(STJc.CENTER);
//        pageNo.addNewR().addNewFldChar().setFldCharType(STFldCharType.BEGIN);
//        pageNo.addNewR().addNewInstrText().setStringValue("PAGE   \\* MERGEFORMAT");
//        pageNo.addNewR().addNewFldChar().setFldCharType(STFldCharType.SEPARATE);
//        CTR end = pageNo.addNewR();
//        CTRPr endRPr = end.addNewRPr();
//        endRPr.addNewNoProof();
//        endRPr.addNewLang().setVal(LANG_ZH_CN);
//        end.addNewFldChar().setFldCharType(STFldCharType.END);
//        CTSectPr sectPr = docx.getDocument().getBody().isSetSectPr() ? docx.getDocument().getBody().getSectPr() : docx.getDocument().getBody().addNewSectPr();
//        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(docx, sectPr);
//        policy.createFooter(STHdrFtr.DEFAULT, new XWPFParagraph[] { footer });
    	CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
		XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docx, sectPr);
		XWPFFooter footer =  headerFooterPolicy.createFooter(STHdrFtr.DEFAULT);
		XWPFParagraph paragraph = footer.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.RIGHT);
		paragraph.setVerticalAlignment(TextAlignment.CENTER);
		//paragraph.setBorderTop(Borders.THICK);
		CTTabStop tabStop = paragraph.getCTP().getPPr().addNewTabs().addNewTab();
		tabStop.setVal(STTabJc.RIGHT);
		int twipsPerInch =  1440;
		tabStop.setPos(BigInteger.valueOf(6 * twipsPerInch));
		XWPFRun run = paragraph.createRun();
    	run = paragraph.createRun();
		run.setText("第");
		setXWPFRunStyle(run,"宋体",10);

		run = paragraph.createRun();
		CTFldChar fldChar = run.getCTR().addNewFldChar();
		fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));

		run = paragraph.createRun();
		CTText ctText = run.getCTR().addNewInstrText();
		ctText.setStringValue("PAGE  \\* MERGEFORMAT");
		ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
		setXWPFRunStyle(run,"宋体",10);

		fldChar = run.getCTR().addNewFldChar();
		fldChar.setFldCharType(STFldCharType.Enum.forString("end"));

		run = paragraph.createRun();
		run.setText("页 /共");
		setXWPFRunStyle(run,"宋体",10);

		run = paragraph.createRun();
		fldChar = run.getCTR().addNewFldChar();
		fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));

		run = paragraph.createRun();
		ctText = run.getCTR().addNewInstrText();
		ctText.setStringValue("NUMPAGES  \\* MERGEFORMAT ");
		ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
		setXWPFRunStyle(run,"宋体",10);

		fldChar = run.getCTR().addNewFldChar();
		fldChar.setFldCharType(STFldCharType.Enum.forString("end"));

		run = paragraph.createRun();
		run.setText("页");
		setXWPFRunStyle(run,"宋体",10);
    }
    
    private static void setXWPFRunStyle(XWPFRun r1,String font,int fontSize) {
		r1.setFontSize(fontSize);
		CTRPr rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR().addNewRPr();
		CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
		fonts.setAscii(font);
		fonts.setEastAsia(font);
		fonts.setHAnsi(font);
	}

    /**
     * 创建表格的标题样式
     * @param document
     * 
     */
    public void createTitleParagraph(XWPFDocument document) {
        XWPFParagraph titleParagraph = document.createParagraph();    //新建一个标题段落对象（就是一段文字）
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);//样式居中
        XWPFRun titleFun = titleParagraph.createRun();    //创建文本对象
//        titleFun.setText(titleName); //设置标题的名字
        titleFun.setBold(true); //加粗
        titleFun.setColor("000000");//设置颜色
        titleFun.setFontSize(25);    //字体大小
//        titleFun.setFontFamily("");//设置字体
        //...
        titleFun.addBreak();    //换行
    }
    /**
     * 设置表格
     * @param document
     * @param rows 多少行
     * @param cols 多少列
     * @param colhbstart 从第几行合并 例如前两行合并单元格    从第0行到第1行 colhbstart 就是0 colhbend 就是2
     * @param start 合并开始 5
     * @param end 合并结束 9
     */
    public void createTableParagraph(XWPFDocument document, int rows, int cols,int colhbstart,int colhbend,int start,int end) {
//        xwpfHelperTable.createTable(xdoc, rowSize, cellSize, isSetColWidth, colWidths)
        XWPFTable infoTable = document.createTable(rows, cols);
        xwpfHelperTable.setTableWidthAndHAlign(infoTable, "14000", STJc.CENTER);
        //合并表格
        //xwpfHelperTable.mergeCellsHorizontal(infoTable, 1, 5, 5);
        //xwpfHelperTable.mergeCellsVertically(infoTable, 0, 3, 6);
        for(int col = colhbstart; col < colhbend; col++) {
            xwpfHelperTable.mergeCellsHorizontal(infoTable, col, start, end);
        }
        //设置表格样式
        List<XWPFTableRow> rowList = infoTable.getRows();
        for(int i = 0; i < rowList.size(); i++) {
            XWPFTableRow infoTableRow = rowList.get(i);
            List<XWPFTableCell> cellList = infoTableRow.getTableCells();
            for(int j = 0; j < cellList.size(); j++) {
                XWPFParagraph cellParagraph = cellList.get(j).getParagraphArray(0);
                cellParagraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun cellParagraphRun = cellParagraph.createRun();
                cellParagraphRun.setFontSize(12);
//                if(i % 2 != 0) {
//                    cellParagraphRun.setBold(true);
//                }
            }
        }
        xwpfHelperTable.setTableHeight(infoTable, 560, STVerticalJc.CENTER);
        CTTbl ttbl = infoTable.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTTblLayoutType t = tblPr.isSetTblLayout()?tblPr.getTblLayout():tblPr.addNewTblLayout();
        t.setType(STTblLayoutType.FIXED);//使布局固定，不随内容改变宽度
        XWPFTableCell cell = infoTable.getRow(0).getCell(0);
        xwpfHelperTable.setCellWidthAndVAlign(cell, "300", STVerticalJc.CENTER,STJc.CENTER);
        cell = infoTable.getRow(0).getCell(1);
        xwpfHelperTable.setCellWidthAndVAlign(cell, "500", STVerticalJc.CENTER,STJc.CENTER);
        
    }
    
    /**
     * 往表格中填充数据
     * @param dataList
     * @param document
     * @throws IOException
     *
     */
    @SuppressWarnings("unchecked")
    public void exportCheckWord(Map<String, Object> dataList, XWPFDocument document, String savePath) throws IOException {
       // XWPFParagraph paragraph = document.getParagraphArray(0);
        //XWPFRun titleFun = paragraph.getRuns().get(0);
       // titleFun.setText(String.valueOf(dataList.get("TITLE")));
        List<List<Object>> tableData = (List<List<Object>>) dataList.get("TABLEDATA");
        XWPFTable table = document.getTableArray(0);
        fillTableData(table, tableData);
        xwpfHelper.saveDocument(document, savePath);
    }
    /**
     * 往表格中填充数据
     * @param table
     * @param tableData
     * 
     */
    public void fillTableData(XWPFTable table, List<List<Object>> tableData) {
        List<XWPFTableRow> rowList = table.getRows();
        for(int i = 0; i < tableData.size(); i++) {
            List<Object> list = tableData.get(i);
            List<XWPFTableCell> cellList = rowList.get(i).getTableCells();
            for(int j = 0; j < list.size(); j++) {
                XWPFParagraph cellParagraph = cellList.get(j).getParagraphArray(0);
                XWPFRun cellParagraphRun = cellParagraph.getRuns().get(0);
                cellParagraphRun.setText(String.valueOf(list.get(j)));
            }
        }
    }
}
