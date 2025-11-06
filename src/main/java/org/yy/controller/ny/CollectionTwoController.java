package org.yy.controller.ny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.ny.*;
import org.yy.service.zm.*;
import org.yy.service.mom.AreaService;
import org.yy.service.mom.SiteService;
import org.yy.util.ObjectExcelView;
import org.yy.util.TimeUtil;
import org.yy.util.Tools;

import java.util.*;

/**
 * 说明：产量
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-26
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/collectionTwo")
public class CollectionTwoController extends BaseController {

    @Autowired
    private CollectionTwoService collectionTwoService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private NYLoopService loopService;

    @Autowired
    private ElectricTypeService electricTypeService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private SnapshotService snapshotService;

    /**
     * 复费率报表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/multiRate")
    @ResponseBody
    public Object multiRate(Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        String date = pd.getString("FDate");

        double totalValue = 0.0;
        double totalAmount = 0.0;

        List<PageData> totalValueList = new ArrayList<>();
        List<PageData> totalAmountList = new ArrayList<>();

        List<String> timeList = new ArrayList<>();
        List<Double> valueList = new ArrayList<>();

        if (null != date && !"".equals(date)) {

            try {
                //列出plc列表
                String LOOP_ID = pd.getString("LOOP_ID");
                if (null != LOOP_ID && !"".equals(LOOP_ID)) {
                    pd.put("ParamType", "有功电能");
                    pd.put("SaveTable", "T_PatraElectric");
                    pd.put("Loop_ID", LOOP_ID);
                    List<PageData> plcList = collectionTwoService.plcList(pd);

                    if (plcList.size() > 0) {
                        //字段名list
                        List<String> fieldList = new ArrayList<>();
                        for (PageData plc : plcList) {
                            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) ";
                            fieldList.add(field);
                        }
                        //查询pd

                        String field = String.join("+", fieldList.toArray(new String[fieldList.size()])) + " AS value";

                        //System.out.println(Tools.date2Str(new Date(),"HH:mm:ss")+"==1");

                        //尖峰平谷列表
                        List<PageData> typeList = new ArrayList<>();
                        List<PageData> tipMultiRate = new ArrayList<>();
                        List<PageData> peakMultiRate = new ArrayList<>();
                        List<PageData> flatMultiRate = new ArrayList<>();
                        List<PageData> valleyMultiRate = new ArrayList<>();
                        typeList = electricTypeService.listAll(pd);

                        double tipPrice = 0.0;
                        double peakPrice = 0.0;
                        double flatPrice = 0.0;
                        double valleyPrice = 0.0;
                        //System.out.println(Tools.date2Str(new Date(),"HH:mm:ss")+"==2");
                        for (PageData type : typeList) {
                            PageData getValue = new PageData();
                            getValue.put("QueryField", field);
                            getValue.put("date", date);

                            String typeName = type.getString("FType");
                            String startTime = type.getString("StartTime");
                            String endTime = type.getString("EndTime");

                            //处理跨天
                            if (endTime.compareTo(startTime) < 0) {
                                startTime = type.getString("StartTime");
                                endTime = "23:59";
                                String startTimeBefore = "00:00";
                                String endTimeBefore = type.getString("EndTime");
                                getValue.put("startTimeBefore", startTimeBefore);
                                getValue.put("endTimeBefore", endTimeBefore);
                            }

                            getValue.put("startTime", startTime);
                            getValue.put("endTime", endTime);
                            double price = Double.parseDouble(type.get("FPrice").toString());

                            if ("月".equals(pd.getString("DateType"))) {
                                if ("尖".equals(typeName)) {
                                    tipPrice = price;
                                    tipMultiRate = collectionTwoService.monthMultiRate(getValue);
                                } else if ("峰".equals(typeName)) {
                                    peakPrice = price;
                                    peakMultiRate = collectionTwoService.monthMultiRate(getValue);
                                } else if ("平".equals(typeName)) {
                                    flatPrice = price;
                                    flatMultiRate = collectionTwoService.monthMultiRate(getValue);
                                } else if ("谷".equals(typeName)) {
                                    valleyPrice = price;
                                    valleyMultiRate = collectionTwoService.monthMultiRate(getValue);
                                }
                            } else if ("季度".equals(pd.getString("DateType"))) {

                                int year = Integer.parseInt(date.substring(0, date.indexOf("年")));
                                int quarter = Integer.parseInt(date.substring(date.indexOf("年"), date.indexOf("季")).substring("年".length()));
                                getValue.put("date", quarter);
                                getValue.put("year", year);
                                if ("尖".equals(typeName)) {
                                    tipPrice = price;
                                    tipMultiRate = collectionTwoService.quarterMultiRate(getValue);
                                } else if ("峰".equals(typeName)) {
                                    peakPrice = price;
                                    peakMultiRate = collectionTwoService.quarterMultiRate(getValue);
                                } else if ("平".equals(typeName)) {
                                    flatPrice = price;
                                    flatMultiRate = collectionTwoService.quarterMultiRate(getValue);
                                } else if ("谷".equals(typeName)) {
                                    valleyPrice = price;
                                    valleyMultiRate = collectionTwoService.quarterMultiRate(getValue);
                                }
                            } else if ("年".equals(pd.getString("DateType"))) {
                                if ("尖".equals(typeName)) {
                                    tipPrice = price;
                                    tipMultiRate = collectionTwoService.yearMultiRate(getValue);
                                } else if ("峰".equals(typeName)) {
                                    peakPrice = price;
                                    peakMultiRate = collectionTwoService.yearMultiRate(getValue);
                                } else if ("平".equals(typeName)) {
                                    flatPrice = price;
                                    flatMultiRate = collectionTwoService.yearMultiRate(getValue);
                                } else if ("谷".equals(typeName)) {
                                    valleyPrice = price;
                                    valleyMultiRate = collectionTwoService.yearMultiRate(getValue);
                                }
                            }
                        }

                        //System.out.println(Tools.date2Str(new Date(),"HH:mm:ss")+"==3");

                        for (PageData tip : tipMultiRate) {
                            String tipTime = tip.getString("TimeStamp");
                            String day = tip.get("day").toString();
                            PageData var = new PageData();
                            var.put("time", tipTime);
                            var.put("day", day);
                            var.put("tipPrice", tipPrice);
                            var.put("peakPrice", peakPrice);
                            var.put("flatPrice", flatPrice);
                            var.put("valleyPrice", valleyPrice);

                            //初值
                            var.put("tipValue", 0);
                            var.put("tipAmount", 0);
                            var.put("peakValue", 0);
                            var.put("peakAmount", 0);
                            var.put("flatValue", 0);
                            var.put("flatAmount", 0);
                            var.put("valleyValue", 0);
                            var.put("valleyAmount", 0);
                            var.put("sumValue", 0);
                            var.put("sumAmount", 0);

                            double sumValue = 0.0;
                            double sumAmount = 0.0;

                            double tipValue = 0.0;
                            for (PageData tip1 : tipMultiRate) {
                                String tip1Time = tip1.getString("TimeStamp");
                                if (tip1Time.equals(tipTime)) {
                                    tipValue = tipValue + Double.parseDouble(tip1.get("value").toString());
                                    sumValue = sumValue + Double.parseDouble(tip1.get("value").toString());
                                    var.put("tipValue", String.format("%.2f", tipValue));
                                    var.put("tipAmount", String.format("%.2f", tipPrice * tipValue));
                                    sumAmount = sumAmount + tipPrice * Double.parseDouble(tip1.get("value").toString());
                                }
                            }

                            double peakValue = 0.0;
                            for (PageData peak : peakMultiRate) {
                                String peakTime = peak.getString("TimeStamp");
                                if (peakTime.equals(tipTime)) {
                                    peakValue = peakValue + Double.parseDouble(peak.get("value").toString());
                                    sumValue = sumValue + Double.parseDouble(peak.get("value").toString());
                                    var.put("peakValue", String.format("%.2f", peakValue));
                                    var.put("peakAmount", String.format("%.2f", peakPrice * peakValue));
                                    sumAmount = sumAmount + peakPrice * Double.parseDouble(peak.get("value").toString());
                                }
                            }
                            double flatValue = 0.0;
                            for (PageData flat : flatMultiRate) {
                                String flatTime = flat.getString("TimeStamp");
                                if (flatTime.equals(tipTime)) {
                                    flatValue = flatValue + Double.parseDouble(flat.get("value").toString());
                                    sumValue = sumValue + Double.parseDouble(flat.get("value").toString());
                                    var.put("flatValue", String.format("%.2f", flatValue));
                                    var.put("flatAmount", String.format("%.2f", flatPrice * flatValue));
                                    sumAmount = sumAmount + flatPrice * Double.parseDouble(flat.get("value").toString());
                                }

                            }
                            double valleyValue = 0.0;
                            for (PageData valley : valleyMultiRate) {
                                String valleyTime = valley.getString("TimeStamp");
                                if (valleyTime.equals(tipTime)) {
                                    valleyValue = valleyValue + Double.parseDouble(valley.get("value").toString());
                                    sumValue = sumValue + Double.parseDouble(valley.get("value").toString());
                                    var.put("valleyValue", String.format("%.2f", valleyValue));
                                    var.put("valleyAmount", String.format("%.2f", valleyPrice * valleyValue));
                                    sumAmount = sumAmount + valleyPrice * Double.parseDouble(valley.get("value").toString());
                                }
                            }

                            var.put("sumValue", String.format("%.2f", sumValue));
                            var.put("sumAmount", String.format("%.2f", sumAmount));
                            varList.add(var);
                        }

                        //System.out.println(Tools.date2Str(new Date(),"HH:mm:ss")+"==4");

                        //清空临时表数据
                        collectionTwoService.deleteAll();

                        //调用mapper接口进行批量操作
                        if (varList.size() > 0) {
                            if (varList.size() <= 30) {
                                //存入临时表
                                collectionTwoService.saveMultiRate(varList);
                            } else {
                                // 每次插入数据库的数据量
                                int preInsertDataCount = 30;
                                // 可遍历的插入数据库的次数
                                int insertSqlCount = 0;
                                // 总数据条数
                                int totalDataCount = varList.size();
                                if (totalDataCount % preInsertDataCount == 0) {
                                    insertSqlCount = totalDataCount / preInsertDataCount;
                                } else {
                                    insertSqlCount = totalDataCount / preInsertDataCount + 1;
                                }
                                for (int i = 0; i < insertSqlCount; i++) {
                                    int startNumber = i * preInsertDataCount;
                                    int endUnmber = (i + 1) * preInsertDataCount;
                                    if (endUnmber > totalDataCount) {
                                        endUnmber = totalDataCount;
                                    }
                                    List<PageData> subListOK = varList.subList(startNumber, endUnmber);
                                    //根据自己的情况到mapper层
                                    collectionTwoService.saveMultiRate(subListOK);
                                }
                            }
                        }

                        //获取分页结果数据
                        pageList = collectionTwoService.getMultiRatedatalistPage(page);

                        //System.out.println(Tools.date2Str(new Date(),"HH:mm:ss")+"==5");
                        //获取总耗能和总金额
                        for (PageData var : varList) {
                            totalValue = totalValue + Double.parseDouble(var.get("sumValue").toString());
                            totalAmount = totalAmount + Double.parseDouble(var.get("sumAmount").toString());
                        }

                        //获取柱状图时间数值列表
                        for (PageData var : varList) {
                            Double value = Double.parseDouble(var.get("sumValue").toString());
                            String time = var.getString("day");
                            timeList.add(time);
                            valueList.add(value);
                        }

                        //获取饼状图图列表
                        double totalTipValue = 0.0;
                        double totalPeakValue = 0.0;
                        double totalFlatValue = 0.0;
                        double totalValleyValue = 0.0;

                        double totalTipAmount = 0.0;
                        double totalPeakAmount = 0.0;
                        double totalFlatAmount = 0.0;
                        double totalValleyAmount = 0.0;
                        for (PageData var : varList) {
                            totalTipValue = totalTipValue + Double.parseDouble(var.get("tipValue").toString());
                            totalPeakValue = totalPeakValue + Double.parseDouble(var.get("peakValue").toString());
                            totalFlatValue = totalFlatValue + Double.parseDouble(var.get("flatValue").toString());
                            totalValleyValue = totalValleyValue + Double.parseDouble(var.get("valleyValue").toString());

                            totalTipAmount = totalTipAmount + Double.parseDouble(var.get("tipAmount").toString());
                            totalPeakAmount = totalPeakAmount + Double.parseDouble(var.get("peakAmount").toString());
                            totalFlatAmount = totalFlatAmount + Double.parseDouble(var.get("flatAmount").toString());
                            totalValleyAmount = totalValleyAmount + Double.parseDouble(var.get("valleyAmount").toString());
                        }

                        PageData totalValuePd = new PageData();
                        PageData totalAmountPd = new PageData();
                        totalValuePd.put("name", "尖");
                        totalValuePd.put("legendname", "尖");
                        totalValuePd.put("value", String.format("%.2f", totalTipValue));
                        totalAmountPd.put("name", "尖");
                        totalAmountPd.put("legendname", "尖");
                        totalAmountPd.put("value", String.format("%.2f", totalTipAmount));
                        totalValueList.add(totalValuePd);
                        totalAmountList.add(totalAmountPd);

                        totalValuePd = new PageData();
                        totalAmountPd = new PageData();
                        totalValuePd.put("name", "峰");
                        totalValuePd.put("legendname", "峰");
                        totalValuePd.put("value", String.format("%.2f", totalPeakValue));
                        totalAmountPd.put("name", "峰");
                        totalAmountPd.put("legendname", "峰");
                        totalAmountPd.put("value", String.format("%.2f", totalPeakAmount));
                        totalValueList.add(totalValuePd);
                        totalAmountList.add(totalAmountPd);

                        totalValuePd = new PageData();
                        totalAmountPd = new PageData();
                        totalValuePd.put("name", "平");
                        totalValuePd.put("legendname", "平");
                        totalValuePd.put("value", String.format("%.2f", totalFlatValue));
                        totalAmountPd.put("name", "平");
                        totalAmountPd.put("legendname", "平");
                        totalAmountPd.put("value", String.format("%.2f", totalFlatAmount));
                        totalValueList.add(totalValuePd);
                        totalAmountList.add(totalAmountPd);

                        totalValuePd = new PageData();
                        totalAmountPd = new PageData();
                        totalValuePd.put("name", "谷");
                        totalValuePd.put("legendname", "谷");
                        totalValuePd.put("value", String.format("%.2f", totalValleyValue));
                        totalAmountPd.put("name", "谷");
                        totalAmountPd.put("legendname", "谷");
                        totalAmountPd.put("value", String.format("%.2f", totalValleyAmount));
                        totalValueList.add(totalValuePd);
                        totalAmountList.add(totalAmountPd);

                        //System.out.println(Tools.date2Str(new Date(),"HH:mm:ss")+"==1");
                    } else {
                        errInfo = "回路未绑定参数";
                    }
                } else {
                    errInfo = "未传入部门";
                }

            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }

        } else {
            errInfo = "未传入时间";
        }

        map.put("totalValueList", totalValueList);
        map.put("totalAmountList", totalAmountList);
        map.put("timeList", timeList);
        map.put("valueList", valueList);
        map.put("varList", pageList);
        map.put("totalValue", String.format("%.2f", totalValue));
        map.put("totalAmount", String.format("%.2f", totalAmount));
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    @RequestMapping(value = "/exportMultiRate")
    @ResponseBody
    public ModelAndView exportMultiRate() {
        ModelAndView mv = new ModelAndView();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        String date = pd.getString("FDate");

        double totalValue = 0.0;
        double totalAmount = 0.0;

        List<PageData> totalValueList = new ArrayList<>();
        List<PageData> totalAmountList = new ArrayList<>();

        List<String> timeList = new ArrayList<>();
        List<Double> valueList = new ArrayList<>();

        if (null != date && !"".equals(date)) {

            try {
                //列出plc列表
                String LOOP_ID = pd.getString("LOOP_ID");
                if (null != LOOP_ID && !"".equals(LOOP_ID)) {
                    pd.put("ParamType", "有功电能");
                    pd.put("SaveTable", "T_PatraElectric");
                    pd.put("Loop_ID", LOOP_ID);
                    List<PageData> plcList = collectionTwoService.plcList(pd);

                    if (plcList.size() > 0) {
                        //字段名list
                        List<String> fieldList = new ArrayList<>();
                        for (PageData plc : plcList) {
                            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) ";
                            fieldList.add(field);
                        }
                        //查询pd

                        String field = String.join("+", fieldList.toArray(new String[fieldList.size()])) + " AS value";

                        //尖峰平谷列表
                        List<PageData> typeList = new ArrayList<>();
                        List<PageData> tipMultiRate = new ArrayList<>();
                        List<PageData> peakMultiRate = new ArrayList<>();
                        List<PageData> flatMultiRate = new ArrayList<>();
                        List<PageData> valleyMultiRate = new ArrayList<>();
                        typeList = electricTypeService.listAll(pd);

                        double tipPrice = 0.0;
                        double peakPrice = 0.0;
                        double flatPrice = 0.0;
                        double valleyPrice = 0.0;

                        for (PageData type : typeList) {
                            PageData getValue = new PageData();
                            getValue.put("QueryField", field);
                            getValue.put("date", date);

                            String typeName = type.getString("FType");
                            String startTime = type.getString("StartTime");
                            String endTime = type.getString("EndTime");

                            //处理跨天
                            if (endTime.compareTo(startTime) < 0) {
                                startTime = type.getString("StartTime");
                                endTime = "23:59";
                                String startTimeBefore = "00:00";
                                String endTimeBefore = type.getString("EndTime");
                                getValue.put("startTimeBefore", startTimeBefore);
                                getValue.put("endTimeBefore", endTimeBefore);
                            }

                            getValue.put("startTime", startTime);
                            getValue.put("endTime", endTime);
                            double price = Double.parseDouble(type.get("FPrice").toString());

                            if ("月".equals(pd.getString("DateType"))) {
                                if ("尖".equals(typeName)) {
                                    tipPrice = price;
                                    tipMultiRate = collectionTwoService.monthMultiRate(getValue);
                                } else if ("峰".equals(typeName)) {
                                    peakPrice = price;
                                    peakMultiRate = collectionTwoService.monthMultiRate(getValue);
                                } else if ("平".equals(typeName)) {
                                    flatPrice = price;
                                    flatMultiRate = collectionTwoService.monthMultiRate(getValue);
                                } else if ("谷".equals(typeName)) {
                                    valleyPrice = price;
                                    valleyMultiRate = collectionTwoService.monthMultiRate(getValue);
                                }
                            } else if ("季度".equals(pd.getString("DateType"))) {

                                int year = Integer.parseInt(date.substring(0, date.indexOf("年")));
                                int quarter = Integer.parseInt(date.substring(date.indexOf("年"), date.indexOf("季")).substring("年".length()));
                                getValue.put("date", quarter);
                                getValue.put("year", year);
                                if ("尖".equals(typeName)) {
                                    tipPrice = price;
                                    tipMultiRate = collectionTwoService.quarterMultiRate(getValue);
                                } else if ("峰".equals(typeName)) {
                                    peakPrice = price;
                                    peakMultiRate = collectionTwoService.quarterMultiRate(getValue);
                                } else if ("平".equals(typeName)) {
                                    flatPrice = price;
                                    flatMultiRate = collectionTwoService.quarterMultiRate(getValue);
                                } else if ("谷".equals(typeName)) {
                                    valleyPrice = price;
                                    valleyMultiRate = collectionTwoService.quarterMultiRate(getValue);
                                }
                            } else if ("年".equals(pd.getString("DateType"))) {
                                if ("尖".equals(typeName)) {
                                    tipPrice = price;
                                    tipMultiRate = collectionTwoService.yearMultiRate(getValue);
                                } else if ("峰".equals(typeName)) {
                                    peakPrice = price;
                                    peakMultiRate = collectionTwoService.yearMultiRate(getValue);
                                } else if ("平".equals(typeName)) {
                                    flatPrice = price;
                                    flatMultiRate = collectionTwoService.yearMultiRate(getValue);
                                } else if ("谷".equals(typeName)) {
                                    valleyPrice = price;
                                    valleyMultiRate = collectionTwoService.yearMultiRate(getValue);
                                }
                            }
                        }

                        for (PageData tip : tipMultiRate) {
                            String tipTime = tip.getString("TimeStamp");
                            String day = tip.get("day").toString();
                            PageData var = new PageData();
                            var.put("time", tipTime);
                            var.put("day", day);
                            var.put("tipPrice", tipPrice);
                            var.put("peakPrice", peakPrice);
                            var.put("flatPrice", flatPrice);
                            var.put("valleyPrice", valleyPrice);

                            //初值
                            var.put("tipValue", 0);
                            var.put("tipAmount", 0);
                            var.put("peakValue", 0);
                            var.put("peakAmount", 0);
                            var.put("flatValue", 0);
                            var.put("flatAmount", 0);
                            var.put("valleyValue", 0);
                            var.put("valleyAmount", 0);
                            var.put("sumValue", 0);
                            var.put("sumAmount", 0);

                            double sumValue = 0.0;
                            double sumAmount = 0.0;

                            double tipValue = 0.0;
                            for (PageData tip1 : tipMultiRate) {
                                String tip1Time = tip1.getString("TimeStamp");
                                if (tip1Time.equals(tipTime)) {
                                    tipValue = tipValue + Double.parseDouble(tip1.get("value").toString());
                                    sumValue = sumValue + Double.parseDouble(tip1.get("value").toString());
                                    var.put("tipValue", String.format("%.2f", tipValue));
                                    var.put("tipAmount", String.format("%.2f", tipPrice * tipValue));
                                    sumAmount = sumAmount + tipPrice * Double.parseDouble(tip1.get("value").toString());
                                }
                            }

                            double peakValue = 0.0;
                            for (PageData peak : peakMultiRate) {
                                String peakTime = peak.getString("TimeStamp");
                                if (peakTime.equals(tipTime)) {
                                    peakValue = peakValue + Double.parseDouble(peak.get("value").toString());
                                    sumValue = sumValue + Double.parseDouble(peak.get("value").toString());
                                    var.put("peakValue", String.format("%.2f", peakValue));
                                    var.put("peakAmount", String.format("%.2f", peakPrice * peakValue));
                                    sumAmount = sumAmount + peakPrice * Double.parseDouble(peak.get("value").toString());
                                }
                            }
                            double flatValue = 0.0;
                            for (PageData flat : flatMultiRate) {
                                String flatTime = flat.getString("TimeStamp");
                                if (flatTime.equals(tipTime)) {
                                    flatValue = flatValue + Double.parseDouble(flat.get("value").toString());
                                    sumValue = sumValue + Double.parseDouble(flat.get("value").toString());
                                    var.put("flatValue", String.format("%.2f", flatValue));
                                    var.put("flatAmount", String.format("%.2f", flatPrice * flatValue));
                                    sumAmount = sumAmount + flatPrice * Double.parseDouble(flat.get("value").toString());
                                }

                            }
                            double valleyValue = 0.0;
                            for (PageData valley : valleyMultiRate) {
                                String valleyTime = valley.getString("TimeStamp");
                                if (valleyTime.equals(tipTime)) {
                                    valleyValue = valleyValue + Double.parseDouble(valley.get("value").toString());
                                    sumValue = sumValue + Double.parseDouble(valley.get("value").toString());
                                    var.put("valleyValue", String.format("%.2f", valleyValue));
                                    var.put("valleyAmount", String.format("%.2f", valleyPrice * valleyValue));
                                    sumAmount = sumAmount + valleyPrice * Double.parseDouble(valley.get("value").toString());
                                }
                            }

                            var.put("sumValue", String.format("%.2f", sumValue));
                            var.put("sumAmount", String.format("%.2f", sumAmount));
                            varList.add(var);
                        }

                        //导出
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("日期");    //1
                        titles.add("尖单价");
                        titles.add("尖金额");    //2
                        titles.add("尖电量");    //3
                        titles.add("峰单价");
                        titles.add("峰金额");    //2
                        titles.add("峰电量");    //3
                        titles.add("平单价");
                        titles.add("平金额");    //2
                        titles.add("平电量");    //3
                        titles.add("谷单价");
                        titles.add("谷金额");    //2
                        titles.add("谷电量");    //3
                        titles.add("合计金额");    //2
                        titles.add("合计电量");    //3
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        for (int i = 0; i < varList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", varList.get(i).getString("time"));        //1
                            vpd.put("var2", varList.get(i).get("tipPrice").toString());
                            vpd.put("var3", varList.get(i).get("tipAmount").toString());        //2
                            vpd.put("var4", varList.get(i).get("tipValue").toString());        //3
                            vpd.put("var5", varList.get(i).get("peakPrice").toString());
                            vpd.put("var6", varList.get(i).get("peakAmount").toString());
                            vpd.put("var7", varList.get(i).get("peakValue").toString());
                            vpd.put("var8", varList.get(i).get("flatPrice").toString());
                            vpd.put("var9", varList.get(i).get("flatAmount").toString());
                            vpd.put("var10", varList.get(i).get("flatValue").toString());
                            vpd.put("var11", varList.get(i).get("valleyPrice").toString());
                            vpd.put("var12", varList.get(i).get("valleyAmount").toString());
                            vpd.put("var13", varList.get(i).get("valleyValue").toString());
                            vpd.put("var14", varList.get(i).get("sumAmount").toString());
                            vpd.put("var15", varList.get(i).get("sumValue").toString());
                            exportList.add(vpd);
                        }
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else {
                        //导出
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("日期");    //1
                        titles.add("尖单价");
                        titles.add("尖金额");    //2
                        titles.add("尖电量");    //3
                        titles.add("峰单价");
                        titles.add("峰金额");    //2
                        titles.add("峰电量");    //3
                        titles.add("平单价");
                        titles.add("平金额");    //2
                        titles.add("平电量");    //3
                        titles.add("谷单价");
                        titles.add("谷金额");    //2
                        titles.add("谷电量");    //3
                        titles.add("合计金额");    //2
                        titles.add("合计电量");    //3
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    }
                } else {
                    errInfo = "未传入部门";
                }

            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }

        } else {
            errInfo = "未传入时间";
        }

        return mv;
    }

    /**
     * 实时监控报表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/monitor")
    @ResponseBody
    public Object monitor(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        PageData val = new PageData();
        String date = pd.getString("FDate");
        String SaveTable = pd.getString("SaveTable");
        String ParamType = pd.getString("ParamType");

        if (null != date && !"".equals(date)) {

            try {
                //列出plc列表
                String LOOP_ID = pd.getString("LOOP_ID");
                pd.put("ParamType", ParamType);
                pd.put("SaveTable", SaveTable);
                pd.put("Loop_ID", LOOP_ID);
                List<PageData> plcList = collectionTwoService.plcList(pd);

                if (plcList.size() > 0) {
                    //字段名list
                    List<String> fieldList = new ArrayList<>();
                    for (PageData plc : plcList) {
                        String field = "isnull(" + plc.getString("SaveField") + ",0) as " + plc.getString("SaveField");
                        fieldList.add(field);
                    }
                    //查询pd
                    String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                    PageData getValue = new PageData();
                    getValue.put("QueryField", field);
                    getValue.put("date", date);
                    getValue.put("SaveTable", SaveTable);

                    page.setPd(getValue);
                    List<PageData> monitorList = collectionTwoService.monitorList(page);
                    List<PageData> monitorAllList = collectionTwoService.monitorAllList(getValue);

                    //列绑定名
                    for (PageData monitor : monitorList) {
                        for (PageData plc : plcList) {
                            monitor.put(plc.getString("ParamAttribute"), monitor.get(plc.getString("SaveField")));
                        }
                    }
                    for (PageData monitor : monitorAllList) {
                        for (PageData plc : plcList) {
                            monitor.put(plc.getString("ParamAttribute"), monitor.get(plc.getString("SaveField")));
                        }
                        timeList.add(monitor.getString("TimeStamp"));
                    }

                    //折线图数据
                    for (PageData plc : plcList) {
                        val.put("name", plc.getString("ParamAttribute"));
                        List<Double> list = new ArrayList<>();
                        for (PageData monitor : monitorAllList) {
                            list.add(Double.parseDouble(monitor.get(plc.getString("ParamAttribute")).toString()));
                        }
                        val.put(plc.getString("ParamAttribute"), list);
                    }

                    varList = monitorList;
                }
            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }
        } else {
            errInfo = "未传入时间";
        }

        map.put("timeList", timeList);
        map.put("pd", val);
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    @RequestMapping(value = "/exportMonitor")
    @ResponseBody
    public ModelAndView exportMonitor() throws Exception {
        ModelAndView mv = new ModelAndView();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = new ArrayList<>();
        String date = pd.getString("FDate");
        String SaveTable = pd.getString("SaveTable");
        String ParamType = pd.getString("ParamType");

        if (null != date && !"".equals(date)) {

            try {
                //列出plc列表
                String LOOP_ID = pd.getString("LOOP_ID");
                pd.put("ParamType", ParamType);
                pd.put("SaveTable", SaveTable);
                pd.put("Loop_ID", LOOP_ID);
                List<PageData> plcList = collectionTwoService.plcList(pd);

                if (plcList.size() > 0) {
                    //字段名list
                    List<String> fieldList = new ArrayList<>();
                    for (PageData plc : plcList) {
                        String field = "isnull(" + plc.getString("SaveField") + ",0) as " + plc.getString("SaveField");
                        fieldList.add(field);
                    }
                    //查询pd
                    String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                    PageData getValue = new PageData();
                    getValue.put("QueryField", field);
                    getValue.put("date", date);
                    getValue.put("SaveTable", SaveTable);

                    List<PageData> monitorAllList = collectionTwoService.monitorAllList(getValue);

                    for (PageData monitor : monitorAllList) {
                        for (PageData plc : plcList) {
                            monitor.put(plc.getString("ParamAttribute"), monitor.get(plc.getString("SaveField")));
                        }
                    }

                    varList = monitorAllList;

                    //导出
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    List<String> titles = new ArrayList<String>();
                    if ("T_PatraElectric".equals(SaveTable) && "电流".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("Ia(A)");
                        titles.add("Ib(A)");    //2
                        titles.add("Ic(A)");    //3
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        for (int i = 0; i < varList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", varList.get(i).getString("TimeStamp"));        //1
                            vpd.put("var2", varList.get(i).get("Ia").toString());
                            vpd.put("var3", varList.get(i).get("Ib").toString());        //2
                            vpd.put("var4", varList.get(i).get("Ic").toString());        //3
                            exportList.add(vpd);
                        }
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else if ("T_PatraElectric".equals(SaveTable) && "电压".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("Ua(A)");
                        titles.add("Ub(A)");    //2
                        titles.add("Uc(A)");    //3
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        for (int i = 0; i < varList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", varList.get(i).getString("TimeStamp"));        //1
                            vpd.put("var2", varList.get(i).get("Ua").toString());
                            vpd.put("var3", varList.get(i).get("Ub").toString());        //2
                            vpd.put("var4", varList.get(i).get("Uc").toString());        //3
                            exportList.add(vpd);
                        }
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else if ("T_PatraElectric".equals(SaveTable) && "有功电能".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("epi(kw·h)");
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        for (int i = 0; i < varList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", varList.get(i).getString("TimeStamp"));        //1
                            vpd.put("var2", varList.get(i).get("Epi").toString());
                            exportList.add(vpd);
                        }
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else if ("T_PatraWater".equals(SaveTable) && "流量".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("flow(t)");
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        for (int i = 0; i < varList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", varList.get(i).getString("TimeStamp"));        //1
                            vpd.put("var2", varList.get(i).get("flow").toString());
                            exportList.add(vpd);
                        }
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else if ("T_PatraGas".equals(SaveTable) && "流量".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("flow(t)");
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        for (int i = 0; i < varList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", varList.get(i).getString("TimeStamp"));        //1
                            vpd.put("var2", varList.get(i).get("flow").toString());
                            exportList.add(vpd);
                        }
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    }

                } else {
                    //导出
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    List<String> titles = new ArrayList<String>();
                    if ("T_PatraElectric".equals(SaveTable) && "电流".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("Ia(A)");
                        titles.add("Ib(A)");    //2
                        titles.add("Ic(A)");    //3
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else if ("T_PatraElectric".equals(SaveTable) && "电压".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("Ua(A)");
                        titles.add("Ub(A)");    //2
                        titles.add("Uc(A)");    //3
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else if ("T_PatraElectric".equals(SaveTable) && "有功电能".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("epi(kw·h)");
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else if ("T_PatraWater".equals(SaveTable) && "流量".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("flow(t)");
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else if ("T_PatraGas".equals(SaveTable) && "流量".equals(ParamType)) {
                        titles.add("采集时间");    //1
                        titles.add("flow(t)");
                        dataMap.put("titles", titles);
                        List<PageData> exportList = new ArrayList<PageData>();
                        dataMap.put("varList", exportList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }
        } else {
            errInfo = "未传入时间";
        }

        return mv;
    }


    /**
     * 线路损耗报表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/lineLoss")
    @ResponseBody
    public Object lineLoss(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = new ArrayList<>();
        List<String> aa = new ArrayList<>();

        String SaveTable = pd.getString("SaveTable");
        String startTime = pd.getString("startTime");
        String endTime = pd.getString("endTime");

        if (null != SaveTable && !"".equals(SaveTable)) {

            try {
                //列出plc列表
                if ("T_PatraElectric".equals(SaveTable)) {
                    pd.put("AccParamType", "有功电能");
                } else {
                    pd.put("AccParamType", "流量");
                }
                pd.put("SaveTable", SaveTable);
                page.setPd(pd);
                List<PageData> plcList = collectionTwoService.plcPageList(page);
                List<PageData> plcAllList = collectionTwoService.plcList(pd);

                if (plcList.size() > 0) {
                    //字段名list
                    List<String> fieldList = new ArrayList<>();
                    for (PageData plc : plcList) {
                        String field = "isnull(MAX(CAST(" + plc.getString("SaveField") + " AS NUMERIC(10,2))),0) - isnull(MIN(CAST(" + plc.getString("SaveField") + " AS NUMERIC(10,2))),0) AS " + plc.getString("SaveField");
                        fieldList.add(field);
                        //存入回路名
                        String DeptName1 = plc.getString("DeptName1");
                        String DeptName2 = plc.getString("DeptName2");
                        String DeptName3 = plc.getString("DeptName3");
                        if (!"".equals(DeptName1) && null != DeptName1) {
                            plc.put("LoopName", DeptName1);
                        } else if (!"".equals(DeptName2) && null != DeptName2) {
                            plc.put("LoopName", DeptName2);
                        } else if (!"".equals(DeptName3) && null != DeptName3) {
                            plc.put("LoopName", DeptName3);
                        }
                    }
                    //查询pd
                    String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                    PageData getValue = new PageData();
                    getValue.put("QueryField", field);
                    getValue.put("startTime", startTime);
                    getValue.put("endTime", endTime);
                    getValue.put("SaveTable", SaveTable);

                    List<PageData> valueList = collectionTwoService.lineLossList(getValue);

                    List<PageData> ids = getNextIdList(plcAllList);
                    for (PageData plc : plcList) {

                        String parentId = plc.getString("Loop");
                        String SaveField = plc.getString("SaveField");
                        //存入自己回路的值
                        if (valueList.size() > 0) {
                            PageData value = valueList.get(0);
                            double fieldValue = 0.0;
                            fieldValue = Double.parseDouble(value.get(SaveField).toString());
                            plc.put("ownValue", String.format("%.2f", fieldValue));
                        }
                        for (PageData id : ids) {
                            String parent = id.getString("parent");
                            int childSize = Integer.parseInt(id.get("childSize").toString());
                            if (parent.equals(parentId)) {
                                if (childSize == 0) {
                                    plc.put("childrenValue", "无");
                                    plc.put("diffValue", "无");
                                    plc.put("shareValue", "无");
                                    plc.put("diffPercent", "无");
                                } else {
                                    PageData getChildValue = new PageData();
                                    String childField = id.getString("field");
                                    getChildValue.put("QueryField", childField);
                                    getChildValue.put("startTime", startTime);
                                    getChildValue.put("endTime", endTime);
                                    getChildValue.put("SaveTable", SaveTable);
                                    List<PageData> childValueList = collectionTwoService.lineLossList(getChildValue);
                                    if (childValueList.size() > 0) {
                                        PageData childValue = childValueList.get(0);
                                        double fieldValue = Double.parseDouble(childValue.get("value").toString());

                                        double ownValue = Double.parseDouble(plc.get("ownValue").toString());
                                        if (fieldValue > ownValue) {
                                            fieldValue = ownValue;
                                        }
                                        plc.put("childrenValue", String.format("%.2f", fieldValue));
                                        double diffValue = ownValue - fieldValue;
                                        double shareValue = diffValue / childSize;
                                        double diffPercent = diffValue / ownValue * 100;
                                        plc.put("diffValue", String.format("%.2f", diffValue));
                                        plc.put("shareValue", String.format("%.2f", shareValue));
                                        plc.put("diffPercent", String.format("%.2f", diffPercent) + "%");
                                    }
                                }
                            }
                        }
                    }


                    varList = plcList;
                }
            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    @RequestMapping(value = "/exportLineLoss")
    @ResponseBody
    public ModelAndView exportLineLoss() throws Exception {
        ModelAndView mv = new ModelAndView();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = new ArrayList<>();
        List<String> aa = new ArrayList<>();

        String SaveTable = pd.getString("SaveTable");
        String startTime = pd.getString("startTime");
        String endTime = pd.getString("endTime");

        if (null != SaveTable && !"".equals(SaveTable)) {

            try {
                //列出plc列表
                if ("T_PatraElectric".equals(SaveTable)) {
                    pd.put("EnergyType", "有功电能");
                } else {
                    pd.put("EnergyType", "流量");
                }
                pd.put("SaveTable", SaveTable);
                List<PageData> plcAllList = collectionTwoService.plcList(pd);

                if (plcAllList.size() > 0) {
                    //字段名list
                    List<String> fieldList = new ArrayList<>();
                    for (PageData plc : plcAllList) {
                        String field = "isnull(MAX(CAST(" + plc.getString("SaveField") + " AS NUMERIC(10,2))),0) - isnull(MIN(CAST(" + plc.getString("SaveField") + " AS NUMERIC(10,2))),0) AS " + plc.getString("SaveField");
                        fieldList.add(field);
                        //存入回路名
                        String DeptName1 = plc.getString("DeptName1");
                        String DeptName2 = plc.getString("DeptName2");
                        String DeptName3 = plc.getString("DeptName3");
                        if (!"".equals(DeptName1) && null != DeptName1) {
                            plc.put("LoopName", DeptName1);
                        } else if (!"".equals(DeptName2) && null != DeptName2) {
                            plc.put("LoopName", DeptName2);
                        } else if (!"".equals(DeptName3) && null != DeptName3) {
                            plc.put("LoopName", DeptName3);
                        }
                    }
                    //查询pd
                    String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                    PageData getValue = new PageData();
                    getValue.put("QueryField", field);
                    getValue.put("startTime", startTime);
                    getValue.put("endTime", endTime);
                    getValue.put("SaveTable", SaveTable);

                    List<PageData> valueList = collectionTwoService.lineLossList(getValue);

                    List<PageData> ids = getNextIdList(plcAllList);
                    for (PageData plc : plcAllList) {

                        String parentId = plc.getString("Loop");
                        String SaveField = plc.getString("SaveField");
                        //存入自己回路的值
                        if (valueList.size() > 0) {
                            PageData value = valueList.get(0);
                            double fieldValue = 0.0;
                            fieldValue = Double.parseDouble(value.get(SaveField).toString());
                            plc.put("ownValue", String.format("%.2f", fieldValue));
                        }
                        for (PageData id : ids) {
                            String parent = id.getString("parent");
                            int childSize = Integer.parseInt(id.get("childSize").toString());
                            if (parent.equals(parentId)) {
                                if (childSize == 0) {
                                    plc.put("childrenValue", "无");
                                    plc.put("diffValue", "无");
                                    plc.put("shareValue", "无");
                                    plc.put("diffPercent", "无");
                                } else {
                                    PageData getChildValue = new PageData();
                                    String childField = id.getString("field");
                                    getChildValue.put("QueryField", childField);
                                    getChildValue.put("startTime", startTime);
                                    getChildValue.put("endTime", endTime);
                                    getChildValue.put("SaveTable", SaveTable);
                                    List<PageData> childValueList = collectionTwoService.lineLossList(getChildValue);
                                    if (childValueList.size() > 0) {
                                        PageData childValue = childValueList.get(0);
                                        double fieldValue = Double.parseDouble(childValue.get("value").toString());
                                        plc.put("childrenValue", String.format("%.2f", fieldValue));

                                        double ownValue = Double.parseDouble(plc.get("ownValue").toString());
                                        double diffValue = ownValue - fieldValue;
                                        double shareValue = fieldValue / childSize;
                                        double diffPercent = diffValue / ownValue * 100;
                                        plc.put("diffValue", String.format("%.2f", diffValue));
                                        plc.put("shareValue", String.format("%.2f", shareValue));
                                        plc.put("diffPercent", String.format("%.2f", diffPercent) + "%");
                                    }
                                }
                            }
                        }
                    }

                    varList = plcAllList;

                    //导出
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    List<String> titles = new ArrayList<String>();
                    titles.add("回路名称");    //1
                    titles.add("当前支路能耗");
                    titles.add("下级支路能耗合计");    //2
                    titles.add("当前支路和下级支路能耗合计差值");    //3
                    titles.add("分摊损耗");
                    titles.add("相差百分比");    //2
                    dataMap.put("titles", titles);
                    List<PageData> exportList = new ArrayList<PageData>();
                    for (int i = 0; i < varList.size(); i++) {
                        PageData vpd = new PageData();
                        vpd.put("var1", varList.get(i).getString("LoopName"));        //1
                        vpd.put("var2", varList.get(i).get("ownValue").toString());
                        vpd.put("var3", varList.get(i).get("childrenValue").toString());        //2
                        vpd.put("var4", varList.get(i).get("diffValue").toString());        //3
                        vpd.put("var5", varList.get(i).get("shareValue").toString());
                        vpd.put("var6", varList.get(i).get("diffPercent").toString());
                        exportList.add(vpd);
                    }
                    dataMap.put("varList", exportList);
                    ObjectExcelView erv = new ObjectExcelView();
                    mv = new ModelAndView(erv, dataMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        return mv;
    }


    /**
     * 主页峰谷平
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/bigScreen")
    @ResponseBody
    public Object bigScreen() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        List<PageData> varList = new ArrayList<>();

        try {

            String date = Tools.date2Str(new Date(), "yyyy-MM-dd");
            List<PageData> electricTypeList = new ArrayList<>();
            electricTypeList = electricTypeService.listAll(new PageData());
            List<PageData> plcList = new ArrayList<>();
            PageData getPlc = new PageData();
            getPlc.put("SaveTable", "T_PatraElectric");
            getPlc.put("AccParamType", "有功电能");
            getPlc.put("isTotalUseEnergy", "isTotalUseEnergy");
            plcList = collectionTwoService.plcList(getPlc);

            if (plcList.size() > 0) {
                //字段名list
                List<String> fieldList = new ArrayList<>();
                for (PageData plc : plcList) {
                    String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) ";
                    fieldList.add(field);
                }

                String field = String.join("+", fieldList.toArray(new String[fieldList.size()])) + " AS value";

                for (PageData electricType : electricTypeList) {
                    String type = electricType.getString("FType");
                    String startTime = electricType.getString("StartTime");
                    String endTime = electricType.getString("EndTime");

                    PageData getValue = new PageData();
                    //处理跨天
                    if (endTime.compareTo(startTime) < 0) {
                        startTime = "00:00";
                        endTime = electricType.getString("EndTime");
                        String startTimeBefore = electricType.getString("StartTime");
                        String endTimeBefore = "23:59";
                        getValue.put("startTimeBefore", startTimeBefore);
                        getValue.put("endTimeBefore", endTimeBefore);
                    }
                    getValue.put("startTime", startTime);
                    getValue.put("endTime", endTime);
                    getValue.put("date", date);
                    getValue.put("QueryField", field);

                    List<PageData> valueList = collectionTwoService.BigMultiRate(getValue);
                    double value = 0.0;
                    for (PageData val : valueList) {
                        value = value + Double.parseDouble(val.get("value").toString());
                    }

                    PageData var = new PageData();
                    if ("尖".equals(type)) {
                        var.put("name", "尖");
                        var.put("value", String.format("%.2f", value));
                        pd.put("tipValue", String.format("%.2f", value));
                    } else if ("峰".equals(type)) {
                        var.put("name", "峰");
                        var.put("value", String.format("%.2f", value));
                        pd.put("peakValue", String.format("%.2f", value));
                    } else if ("谷".equals(type)) {
                        var.put("name", "谷");
                        var.put("value", String.format("%.2f", value));
                        pd.put("flatValue", String.format("%.2f", value));
                    }
                    if (!"平".equals(type)) {
                        varList.add(var);
                    }
                }

                PageData getValue = new PageData();
                getValue.put("startTime", "00:00");
                getValue.put("endTime", "23:59");
                getValue.put("date", date);
                getValue.put("QueryField", field);
                List<PageData> valueList = collectionTwoService.BigMultiRate(getValue);
                double value = 0.0;
                if (valueList.size() > 0) {
                    value = Double.parseDouble(valueList.get(0).get("value").toString());
                }
                if (value != 0.0) {
                    pd.put("tipPercent", Double.parseDouble(String.format("%.2f", (Double.parseDouble(pd.get("tipValue").toString()) / value) * 100)));
                    pd.put("peakPercent", Double.parseDouble(String.format("%.2f", (Double.parseDouble(pd.get("peakValue").toString()) / value) * 100)));
                    pd.put("flatPercent", Double.parseDouble(String.format("%.2f", (Double.parseDouble(pd.get("flatValue").toString()) / value) * 100)));
                } else {
                    pd.put("tipPercent", 0.0);
                    pd.put("peakPercent", 0.0);
                    pd.put("flatPercent", 0.0);
                }

            } else {
                pd.put("tipPercent", 0.00);
                pd.put("peakPercent", 0.00);
                pd.put("flatPercent", 0.00);
                pd.put("tipValue", 0.00);
                pd.put("peakValue", 0.00);
                pd.put("flatValue", 0.00);

                PageData var = new PageData();
                var.put("name", "尖");
                var.put("value", "0.00");
                varList.add(var);
                var = new PageData();
                var.put("name", "峰");
                var.put("value", "0.00");
                varList.add(var);
                var = new PageData();
                var.put("name", "谷");
                var.put("value", "0.00");
                varList.add(var);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errInfo = "程序错误!请联系管理员";
        }

        map.put("varList", varList);
        map.put("pd", pd);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 用能概况
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/useEnergy")
    @ResponseBody
    public Object useEnergy() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String FType = pd.getString("FType");

        PageData result = new PageData();
        List<PageData> resList = new ArrayList<>();

        //获取数据字段
        if ("T_PatraElectric".equals(FType)) {
            pd.put("ParamType", "有功电能");
        } else {
            pd.put("ParamType", "流量");
        }

        List<PageData> plcList = new ArrayList<>();
        plcList = recordService.getTotalField(pd);
        double value = 0.0;

        if (plcList.size() > 0) {

            List<String> fieldList = new ArrayList<>();

            for (PageData plc : plcList) {
                String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                fieldList.add(field);
            }

            //查询pd
            PageData getValue = new PageData();
            String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
            getValue.put("SaveField", field);
            getValue.put("SaveTable", FType);

            resList = recordService.getTotalPower(getValue);

            if (resList.size() > 0) {

                for (PageData res : resList) {
                    double FValue = 0;
                    for (PageData plc : plcList) {
                        String Field = plc.getString("SaveField");
                        FValue = FValue + (Double.parseDouble(res.get(Field).toString()));
                    }
                    res.put("FValue", FValue);
                    res.put("TimeStamp", res.getString("TimeStamp") + ":00:00");
                }
            }
            //获取plc读数和

//            List<String> maxFieldList = new ArrayList<>();
            String field1 = "isnull(MAX(CAST(f.A00 AS numeric(10,2))),0.00) sum_use";
//            maxFieldList.add(field1);
//
//            String field1 = String.join("+", maxFieldList.toArray(new String[maxFieldList.size()])) + " sum_use";
            PageData getMaxA00 = new PageData();
            getMaxA00.put("SaveTable", pd.getString("FType"));
            getMaxA00.put("field", field1);
            PageData A00 = recordService.MaxA00(getMaxA00);
            value += Double.parseDouble(A00.get("sum_use").toString());
        }


        double beforeOneValue = 0;
        double beforeOneMonthValue = 0;
        double beforeOneYearValue = 0;

        double beforeTwoDayValue = 0;
        double beforeTwoMonthValue = 0;
        double beforeTwoYearValue = 0;

        double beforeDayUseValue = 0;
        double beforeMonthUseValue = 0;
        double beforeYearUseValue = 0;

        //获取今日/本月/本年最小数据
        if (plcList.size() > 0) {
            List<String> fieldList = new ArrayList<>();

            for (PageData plc : plcList) {
                String field = "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + " ";
                fieldList.add(field);
            }
            //查询pd
            PageData getValue = new PageData();
            String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
            getValue.put("SaveField", field);
            getValue.put("SaveTable", FType);

            //获取今日最小值
            List<PageData> beforeOneList = recordService.getBeforeOneMaxPower(getValue);

            if (beforeOneList.size() == 1) {
                PageData beforeOne = beforeOneList.get(0);

                for (PageData plc : plcList) {
                    String Field = plc.getString("SaveField");
                    beforeOneValue = beforeOneValue + (Double.parseDouble(beforeOne.get(Field).toString()));
                }
            }

            //获取本月最小值
            List<PageData> beforeOneMonthList = recordService.getBeforeOneMonthMaxPower(getValue);

            if (beforeOneMonthList.size() == 1) {
                PageData beforeOneMonth = beforeOneMonthList.get(0);

                for (PageData plc : plcList) {
                    String Field = plc.getString("SaveField");
                    beforeOneMonthValue = beforeOneMonthValue + (Double.parseDouble(beforeOneMonth.get(Field).toString()));
                }
            }

//            //获取去年最大值
//            List<PageData> beforeOneYearList = recordService.getBeforeOneYearMaxPower(getValue);
//
//            if (beforeOneYearList.size() == 1) {
//                PageData beforeOneYear = beforeOneYearList.get(0);
//
//                for (PageData plc : plcList) {
//                    String Field = plc.getString("SaveField");
//                    beforeOneYearValue = beforeOneYearValue + (Double.parseDouble(beforeOneYear.get(Field).toString()));
//                }
//            }

            String FTime = Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");
            getValue.put("FTime", FTime);

            //获取昨日同期用能
            List<PageData> beforeDayUseList = recordService.getBeforeDayUsePower(getValue);

            if (beforeDayUseList.size() == 1) {
                PageData beforeDayUse = beforeDayUseList.get(0);
                for (PageData plc : plcList) {
                    String Field = plc.getString("SaveField");
                    beforeDayUseValue = beforeDayUseValue + (Double.parseDouble(beforeDayUse.get(Field).toString()));
                }
            }
            //获取前日最大值
            List<PageData> beforeTwoDayList = recordService.getBeforeTwoMaxPower(getValue);

            if (beforeTwoDayList.size() == 1) {
                PageData beforeTwoDay = beforeTwoDayList.get(0);
                for (PageData plc : plcList) {
                    String Field = plc.getString("SaveField");
                    beforeTwoDayValue = beforeTwoDayValue + (Double.parseDouble(beforeTwoDay.get(Field).toString()));
                }
            }

            //获取上月同期用能
            List<PageData> beforeMonthUseList = recordService.getBeforeMonthUsePower(getValue);

            if (beforeMonthUseList.size() == 1) {
                PageData beforeMonthUse = beforeMonthUseList.get(0);
                for (PageData plc : plcList) {
                    String Field = plc.getString("SaveField");
                    beforeMonthUseValue = beforeMonthUseValue + (Double.parseDouble(beforeMonthUse.get(Field).toString()));
                }
            }
            //获取上上月最大值
            List<PageData> beforeTwoMonthList = recordService.getBeforeTwoMonthMaxPower(getValue);

            if (beforeTwoMonthList.size() == 1) {
                PageData beforeTwoMonth = beforeTwoMonthList.get(0);
                for (PageData plc : plcList) {
                    String Field = plc.getString("SaveField");
                    beforeTwoMonthValue = beforeTwoMonthValue + (Double.parseDouble(beforeTwoMonth.get(Field).toString()));
                }
            }
        }


        double resDay = 0.00;
        resDay = value-beforeOneValue<0?0.00:value-beforeOneValue;
        double resMonth = 0.00;
        resMonth = value-beforeOneMonthValue<0?0.00:value-beforeOneMonthValue;
        double resYear = 0.00;
        //resYear = value-beforeOneYearValue;
        resYear = Double.parseDouble(snapshotService.queryThisYearUserEnergy(pd).get("FVALUE").toString());

        double resDayUse = 0.00;
        resDayUse = beforeDayUseValue-beforeTwoDayValue<0?0.00:beforeDayUseValue-beforeTwoDayValue;
        double resMonthUse = 0.00;
        resMonthUse = beforeMonthUseValue-beforeTwoMonthValue<0?0.00:beforeMonthUseValue-beforeTwoMonthValue;
        double resYearUse = 0.00;
        resYearUse = Double.parseDouble(snapshotService.queryBeforeYearThisDayUserEnergy(pd).get("FVALUE").toString());

        PageData data = new PageData();

        data.put("resDay", String.format("%.2f", resDay));
        data.put("resMonth", String.format("%.2f", resMonth));
        if(resYear < resMonth){
            resYear = resMonth;
        }
        data.put("resYear", String.format("%.2f", resYear));

        data.put("resDayUse", String.format("%.2f", resDayUse));
        data.put("resMonthUse", String.format("%.2f", resMonthUse));
        data.put("resYearUse", String.format("%.2f", resYearUse));

        if (resDayUse == 0) {
            data.put("dayUsePercent", 100);
        } else {
            data.put("dayUsePercent", String.format("%.2f", ((resDay - resDayUse) / resDayUse) * 100));
        }
        if (resMonthUse == 0) {
            data.put("monthUsePercent", 100);
        } else {
            data.put("monthUsePercent", String.format("%.2f", ((resMonth - resMonthUse) / resMonthUse) * 100));
        }
        if (resYearUse == 0) {
            data.put("yearUsePercent", 100);
        } else {
            data.put("yearUsePercent", String.format("%.2f", ((resYear - resYearUse) / resYearUse) * 100));
        }


        data.put("dayUseDiff", String.format("%.2f", resDay - resDayUse));
        data.put("monthUseDiff", String.format("%.2f", resMonth - resMonthUse));
        data.put("yearUseDiff", String.format("%.2f", resYear - resYearUse));


        //保存要发送的数据
        result.put("recordList",

                getList(resList));
        result.put("dataPd", data);
        map.put("pd", result);
        map.put("result", errInfo);
        return map;
    }

    private List<PageData> getList(List<PageData> list) throws Exception {

        List<PageData> resList = new ArrayList<>();

        for (PageData pd : list) {

            if (null == pd.get("FValue").toString() || "".equals(pd.get("FValue").toString()) || "0.00".equals(pd.get("FValue").toString())) {
                continue;
            }

            PageData res = new PageData();
            //转换时间戳
            Long time = Long.valueOf(TimeUtil.TimeToStamp(pd.getString("TimeStamp")));

            res.put("name", time);
            List<Object> value = new ArrayList<>();
            value.add(time);
            value.add(Double.parseDouble(String.format("%.2f", Double.parseDouble(pd.get("FValue").toString()))));
            res.put("value", value);
            resList.add(res);
        }

        return resList;
    }

    /**
     * 获取所有下级id列表
     *
     * @param
     * @return
     * @throws Exception
     */
    private List<PageData> getNextIdList(List<PageData> plcList) throws Exception {

        List<PageData> returnList = new ArrayList<>();

        //根据ID获取工厂
        PageData param = new PageData();

        List<PageData> areaList = areaService.listAll(param);

        List<PageData> loopList = loopService.listAll(param);

        for (PageData plc : plcList) {

            PageData pd = new PageData();
            String parentId = plc.getString("Loop");
            List<String> idList = new ArrayList<>();

            for (PageData area : areaList) {
                String siteId = area.getString("DM_SITE_ID");
                if (parentId.equals(siteId)) {
                    idList.add(area.getString("AREA_ID"));
                }
            }

            for (PageData loop : loopList) {
                String loopParentId = loop.getString("Parent_ID");
                if (parentId.equals(loopParentId)) {
                    idList.add(loop.getString("LOOP_ID"));
                }
            }

            String ids = "(" + String.join(",", idList.toArray(new String[idList.size()])) + ")";
            pd.put("parent", parentId);
            pd.put("children", ids);
            pd.put("childList", idList);
            returnList.add(pd);
        }

        for (PageData res : returnList) {
            List<String> idList = (List<String>) res.get("childList");
            List<String> fieldList = new ArrayList<>();
            for (String id : idList) {
                for (PageData plc : plcList) {
                    String parentId = plc.getString("Loop");
                    if (id.equals(parentId)) {
                        String field = "isnull(MAX(CAST(" + plc.getString("SaveField") + " AS NUMERIC(10,2))),0) - isnull(MIN(CAST(" + plc.getString("SaveField") + " AS NUMERIC(10,2))),0)";
                        fieldList.add(field);
                    }
                }
            }
            int childSize = fieldList.size();
            res.put("childSize", childSize);
            String field = String.join("+", fieldList.toArray(new String[fieldList.size()])) + " AS value";
            res.put("field", field);
        }

        return returnList;
    }

    /**
     * 根据回路ID获取所有子级id列表
     *
     * @param pd
     * @return
     * @throws Exception
     */
    private List<String> getIdList(PageData pd) throws Exception {
        List<String> loopIdList = new ArrayList<>();
        //获取选中回路下所有子级ID
        String LOOP_ID = pd.getString("LOOP_ID");
        loopIdList.add(LOOP_ID);
        //根据ID获取工厂
        PageData param = new PageData();
        param.put("DM_SITE_ID", LOOP_ID);
        PageData site = siteService.findById(param);

        if (null != site) {

            List<PageData> areaList = areaService.listAll(site);
            for (PageData area : areaList) {
                loopIdList.add(area.getString("AREA_ID"));
                pd.put("PID", area.getString("AREA_ID"));
                List<PageData> TopList = loopService.listAll(pd);
                pd.put("PID", "");
                List<PageData> AllList = loopService.listAll(pd);
                for (PageData top : TopList) {
                    loopIdList.add(top.getString("LOOP_ID"));
                    loopIdList.addAll(getChildren(top, AllList));
                }
            }
        } else {
            param.put("AREA_ID", LOOP_ID);
            PageData area = areaService.findById(param);

            if (null != area) {
                pd.put("PID", area.getString("AREA_ID"));
                List<PageData> TopList = loopService.listAll(pd);
                pd.put("PID", "");
                List<PageData> AllList = loopService.listAll(pd);
                for (PageData top : TopList) {
                    loopIdList.add(top.getString("LOOP_ID"));
                    loopIdList.addAll(getChildren(top, AllList));
                }
            } else {
                param.put("LOOP_ID", LOOP_ID);
                PageData loop = loopService.findById(param);

                if (null != loop) {
                    pd.put("LoopId", LOOP_ID);
                    List<PageData> TopList = loopService.listAll(pd);
                    pd.put("LoopId", "");
                    List<PageData> AllList = loopService.listAll(pd);
                    for (PageData top : TopList) {
                        loopIdList.add(top.getString("LOOP_ID"));
                        loopIdList.addAll(getChildren(top, AllList));
                    }
                }
            }
        }
        return loopIdList;
    }

    /**
     * 递归获取子菜单
     *
     * @param root 当前菜单
     * @param all  总的数据
     * @return 子菜单
     */
    public List<String> getChildren(PageData root, List<PageData> all) {

        List<String> children = new ArrayList<>();

        String PARENT_ID = root.getString("LOOP_ID");

        for (PageData loop : all) {
            if (PARENT_ID.equals(loop.getString("Parent_ID"))) {
                children.add(loop.getString("LOOP_ID"));
                children.addAll(getChildren(loop, all));
            }
        }
        return children;
    }

    //处理逗号分隔ID
    private static PageData handleIDS(String IDS) {
        PageData ids = new PageData();
        //处理逗号分割id
        String ID = "(";
        if (!"".equals(IDS) && null != IDS) {
            String[] array = IDS.split(",");
            if (array.length != 0) {
                for (int i = 0; i < array.length; i++) {
                    ID = ID + "'" + array[i] + "',";
                    if (i == array.length - 1) {
                        ID = ID.substring(0, ID.length() - 1);
                    }
                }
                ID += ")";
            }
        } else {
            ID = "";
        }
        ids.put("IDS", ID);
        return ids;
    }

}
