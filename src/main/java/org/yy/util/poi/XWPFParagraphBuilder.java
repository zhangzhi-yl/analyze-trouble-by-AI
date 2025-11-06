package org.yy.util.poi;

import java.math.BigInteger;
import java.util.*;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

//段落构建器
public class XWPFParagraphBuilder {
	//常量，在文档中定义长度和高度的单位
    private static final int PER_LINE = 100;
    //每个字符的单位长度
    private static final int PER_CHART = 100;
    //1厘米≈567
    private static final int PER_CM = 567;
    //每一磅的单位长度
    private static final int PER_POUND = 20;
    //行距单位长度
    private static final int ONE_LINE = 240;

    private XWPFParagraph paragraph = null;
    private CTPPr pPr = null;
    //保存通用段落属性引用，方便复用
    private Map<String, CTPPr> savedPPr = null;
    //设定间距的对象
    private CTSpacing pSpacing = null;
    //设定缩进的对象
    private CTInd pInd = null;


    public XWPFParagraphBuilder init(XWPFDocument document) {
        return init(document.createParagraph());
    }

    public XWPFParagraphBuilder init(XWPFParagraph paragraph) {
        if (paragraph == null) {
            throw new IllegalArgumentException("the paragraph should not be null");
        }
        this.paragraph = paragraph;
        pPr = getPrOfParagraph(paragraph);
        return this;
    }

    //设置段落对齐方式
    public XWPFParagraphBuilder align(ParagraphAlignment pAlign, TextAlignment vAlign) {
        ensureInit();
        if (pAlign != null) {
            paragraph.setAlignment(pAlign);
        }
        if (vAlign != null) {
            paragraph.setVerticalAlignment(vAlign);
        }
        return this;
    }

    //初始化段落间距属性，在设置各段落间距前调用
    public XWPFParagraphBuilder initSpacing() {
        ensureInit();
        pSpacing = pPr.getSpacing() != null ? pPr.getSpacing() : pPr.addNewSpacing();
        return this;
    }

    //设置段前和段后间距，以磅为单位
    public XWPFParagraphBuilder spaceInPound(double before, double after) {
        ensureInit();
        if (pSpacing == null) {
            initSpacing();
        }
        pSpacing.setBefore(BigInteger.valueOf((long) (before * PER_POUND)));
        pSpacing.setAfter(BigInteger.valueOf((long) (after * PER_POUND)));
        return this;
    }

    //设置段前和段后间距，以行为单位
    public XWPFParagraphBuilder spaceInLine(double beforeLines, double afterLines) {
        ensureInit();
        if (pSpacing == null) {
            initSpacing();
        }
        pSpacing.setBeforeLines(BigInteger.valueOf((long) (beforeLines * PER_LINE)));
        pSpacing.setAfterLines(BigInteger.valueOf((long) (afterLines * PER_LINE)));
        return this;
    }

    //设置段落行距
    public XWPFParagraphBuilder lineSpace(double value, STLineSpacingRule.Enum spaceRule) {
        ensureInit();
        if (pSpacing == null) {
            initSpacing();
        }
        int unit;
        if (spaceRule == null) {
            spaceRule = STLineSpacingRule.AUTO;
        }
        if (spaceRule.intValue() == STLineSpacingRule.INT_AUTO) {
            //当行距规则为多倍行距时，单位为行，且最小为0.06行
            unit = ONE_LINE;
            if (value < 0.06) {
                value = 0.06;
            }
        } else {
            //当行距规则为固定值或最小值时，单位为磅，且最小为0.7磅
            unit = PER_POUND;
            if (value < 0.7) {
                value = 0.7;
            }
        }
        pSpacing.setLine(BigInteger.valueOf((long) (value * unit)));
        pSpacing.setLineRule(spaceRule);
        return this;
    }

    public XWPFParagraphBuilder initInd() {
        ensureInit();
        pInd = pPr.getInd() != null ? pPr.getInd() : pPr.addNewInd();
        return this;
    }

    //设置段落缩进，以厘米为单位; 悬挂缩进高于首行缩进；右侧缩进高于左侧缩进
    public XWPFParagraphBuilder indentInCM(double firstLine, double hanging, double right, double left) {
        ensureInit();
        if (pInd == null) {
            initInd();
        }
        if (firstLine != 0) {
            pInd.setFirstLine(BigInteger.valueOf((long) (firstLine * PER_CM)));
        }
        if (hanging != 0) {
            pInd.setHanging(BigInteger.valueOf((long) (hanging * PER_CM)));
        }
        if (right != 0) {
            pInd.setRight(BigInteger.valueOf((long) (right * PER_CM)));
        }
        if (left != 0) {
            pInd.setLeft(BigInteger.valueOf((long) (left * PER_CM)));
        }
        return this;
    }

    //设置段落缩进，以字符为单位; 悬挂缩进高于首行缩进；右侧缩进高于左侧缩进
    public void indentInChart(int firstLine, int hanging, int left, int right) {
        ensureInit();
        if (pInd == null) {
            initInd();
        }
        if (firstLine != 0) {
            pInd.setFirstLineChars(BigInteger.valueOf((long) (firstLine * PER_CHART)));
        }
        if (hanging != 0) {
            pInd.setHangingChars(BigInteger.valueOf((long) (hanging * PER_CHART)));
        }
        if (right != 0) {
            pInd.setRightChars(BigInteger.valueOf((long) (right * PER_CHART)));
        }
        if (left != 0) {
            pInd.setLeftChars(BigInteger.valueOf((long) (left * PER_CHART)));
        }
        //return this;
    }

    public XWPFParagraphBuilder savePr(String pPrName) {
        ensureInit();
        if (savedPPr == null) {
            savedPPr = new HashedMap<String, CTPPr>();
        }

        savedPPr.put(pPrName, pPr);

        return this;
    }

    public XWPFParagraphBuilder samePrOf(String pPrName) {
        ensureInit();

        if (savedPPr != null && savedPPr.containsKey(pPrName)) {
            return samePrOf(savedPPr.get(pPrName));
        }

        return this;
    }

    public XWPFParagraphBuilder samePrOf(CTPPr pPr) {
        ensureInit();
        if (pPr != null) {
            paragraph.getCTP().setPPr(pPr);
        }
        return this;
    }

    public XWPFParagraphBuilder samePrOf(XWPFParagraph otherPra) {
        ensureInit();
        paragraph.getCTP().setPPr(getPrOfParagraph(otherPra));
        return this;
    }

    public XWPFParagraph build() {
        return paragraph;
    }

    //确保init方法是第一个调用的，避免出现空指针异常
    private void ensureInit() {
        if (this.paragraph == null) {
            throw new IllegalStateException("the init method must be invoked firstly");
        }
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

