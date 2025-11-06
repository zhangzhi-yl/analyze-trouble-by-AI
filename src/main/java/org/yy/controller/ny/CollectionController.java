package org.yy.controller.ny;

import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.opc.OPCService;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.mom.SiteService;
import org.yy.service.ny.CollectionService;
import org.yy.service.ny.NYLoopService;
import org.yy.service.ny.NYPLCService;
import org.yy.service.ny.SnapshotService;
import org.yy.service.zm.LoopService;
import org.yy.service.zm.PLCService;
import org.yy.util.ObjectExcelView;
import org.yy.util.TimeUtil;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;

import java.text.DecimalFormat;
import java.util.*;

import static org.yy.util.TimeUtil.DateResuceOne;

/**
 * 说明：回路/支路管理
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/collection")
public class CollectionController extends BaseController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private NYLoopService loopService;

    @Autowired
    private NYPLCService plcService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private SnapshotService snapshotService;

    /**
     * 保存录入数据
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/saveInput")
    @ResponseBody
    public Object addInput() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> varList = collectionService.findByTime(pd);
        if (varList.size() == 0) {
            collectionService.saveInput(pd);
        } else {
            collectionService.editInput(pd);
        }
        map.put("result", errInfo);
        return map;
    }


//    /**
//     * 地图数据
//     *
//     * @param
//     * @throws Exception
//     */
//    @RequestMapping(value = "/param")
//    @ResponseBody
//    public Object param() throws Exception {
//        Map<String, Object> map = new HashMap<String, Object>();
//        String errInfo = "success";
//
//        List<PageData> loopList = loopService.loopPlcAll();
//        List<PageData> plcList = plcService.listAllPlc();
//
//        Server server = OPCService.openServer();
//
//        for (PageData loop : loopList) {
//            for (PageData plc : plcList) {
//
//                //获取回路绑定的plc
//                if (loop.getString("LOOP_ID").equals(plc.getString("Loop"))) {
//
//                    //存储表
//                    String SaveTable = plc.getString("SaveTable");
//
//                    //判断类型并读取kepserver
//                    if ("T_PatraElectric".equals(SaveTable)) {
//                        String ElectricValue = OPCUtil.getPlcData(server, plc);
//                        loop.put("ElectricValue", ElectricValue);
//                    } else {
//                        loop.put("ElectricValue", 0);
//                    }
//                    if ("T_PatraGas".equals(SaveTable)) {
//                        String GasValue = OPCUtil.getPlcData(server, plc);
//                        loop.put("GasValue", GasValue);
//                    } else {
//                        loop.put("GasValue", 0);
//                    }
//                    if ("T_PatraWater".equals(SaveTable)) {
//                        String WaterValue = OPCUtil.getPlcData(server, plc);
//                        loop.put("WaterValue", WaterValue);
//                    } else {
//                        loop.put("WaterValue", 0);
//                    }
//                } else {
//                    loop.put("ElectricValue", 0);
//                    loop.put("GasValue", 0);
//                    loop.put("WaterValue", 0);
//                }
//            }
//
//        }
//
//        map.put("result", errInfo);
//        map.put("loopList", loopList);
//        return map;
//    }


    /**
     * 采集报表列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Page page) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        String table = pd.getString("SaveTable");
        if (null != table && !"".equals(table)) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {
                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }
                    List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                    //字段名list
                    List<String> fieldList = new ArrayList<>();

                    for (PageData plc : plcList) {
                        String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                        fieldList.add(field);
                    }

                    //查询pd
                    PageData getValue = new PageData();
                    String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                    getValue.put("SaveField", field);

                    //获取值列表
                    if (fieldList.size() > 0) {


                        getValue.put("SaveTable", pd.getString("SaveTable"));
                        //获取时间条件
                        String StartTime = "";
                        String EndTime = "";
                        String FTime = pd.getString("FTime");

                        if ("日".equals(pd.getString("DateType"))) {
                            StartTime = FTime;
                            getValue.put("StartTime", StartTime);
                            String[] arr = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

                            for (String time : arr) {
                                getValue.put("time", time);

                                List<PageData> resultList = new ArrayList<>();

                                resultList = collectionService.getDayList(getValue);
                                if (resultList.size() == 0) {
                                    continue;
                                }

                                valueList.addAll(getDayValueList(plcList, resultList));
                            }
                        } else if ("月".equals(pd.getString("DateType"))) {
                            getValue.put("StartTime", FTime);
                            List<PageData> resultList = collectionService.getMonthList(getValue);

                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        } else if ("季度".equals(pd.getString("DateType"))) {

                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                StartTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("StartTime");
                                EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                            }
                            getValue.put("StartTime", StartTime);
                            getValue.put("EndTime", EndTime);
                            List<PageData> resultList = collectionService.getQuarterList(getValue);

                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        } else if ("年".equals(pd.getString("DateType"))) {
                            StartTime = FTime;
                            getValue.put("StartTime", StartTime);
                            List<PageData> resultList = collectionService.getYearList(getValue);
                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        }
                    }

                    //清空临时表数据
                    collectionService.deleteAll();

                    if (valueList.size() > 0) {
                        //批量插入结果
                        collectionService.saveAll(valueList);

                        //获取结果分页列表
                        pageList = collectionService.getResult(page);

                        //折算折标煤
                        for (PageData p : pageList) {
                            double value = 0.0;
                            String AValue = p.get("AValue").toString();
                            if ("".equals(AValue)) {
                                value = Double.parseDouble(p.get("FValue").toString());
                            } else {
                                value = Double.parseDouble(AValue);
                            }

                            if ("T_PatraElectric".equals(table)) {
                                p.put("ZBM", String.format("%.2f", value * 0.1229));
                            } else if ("T_PatraWater".equals(table)) {
                                //循环水
                                p.put("ZBM", String.format("%.2f", value * 0.1429));
                            } else if ("T_PatraGas".equals(table)) {
                                //压缩空气
                                p.put("ZBM", String.format("%.2f", value * 0.04));
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        map.put("varList", pageList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 采集报表列表导出
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goExcel")
    @ResponseBody
    public ModelAndView goExcel() throws Exception {
        ModelAndView mv = new ModelAndView();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String table = pd.getString("SaveTable");
        List<PageData> valueList = new ArrayList<>();
        List<PageData> allList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {
                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }
                    List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                    //字段名list
                    List<String> fieldList = new ArrayList<>();

                    for (PageData plc : plcList) {
                        String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                        fieldList.add(field);
                    }

                    //查询pd
                    PageData getValue = new PageData();
                    String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                    getValue.put("SaveField", field);

                    //获取值列表
                    if (fieldList.size() > 0) {


                        getValue.put("SaveTable", pd.getString("SaveTable"));
                        //获取时间条件
                        String StartTime = "";
                        String EndTime = "";
                        String FTime = pd.getString("FTime");

                        if ("日".equals(pd.getString("DateType"))) {
                            StartTime = FTime;
                            getValue.put("StartTime", StartTime);
                            String[] arr = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

                            for (String time : arr) {
                                getValue.put("time", time);

                                List<PageData> resultList = new ArrayList<>();

                                resultList = collectionService.getDayList(getValue);
                                if (resultList.size() == 0) {
                                    continue;
                                }

                                valueList.addAll(getDayValueList(plcList, resultList));
                            }
                        } else if ("月".equals(pd.getString("DateType"))) {
                            getValue.put("StartTime", FTime);
                            List<PageData> resultList = collectionService.getMonthList(getValue);

                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        } else if ("季度".equals(pd.getString("DateType"))) {

                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                StartTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("StartTime");
                                EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                            }
                            getValue.put("StartTime", StartTime);
                            getValue.put("EndTime", EndTime);
                            List<PageData> resultList = collectionService.getQuarterList(getValue);

                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        } else if ("年".equals(pd.getString("DateType"))) {
                            StartTime = FTime;
                            getValue.put("StartTime", StartTime);
                            List<PageData> resultList = collectionService.getYearList(getValue);
                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        }
                    }

                    //清空临时表数据
                    collectionService.deleteAll();

                    if (valueList.size() > 0) {
                        //批量插入结果
                        collectionService.saveAll(valueList);

                        //获取结果分页列表
                        allList = collectionService.getResultAllList(new PageData());

                        //折算折标煤
                        for (PageData p : allList) {
                            double value = 0.0;
                            String AValue = p.get("AValue").toString();
                            if ("".equals(AValue)) {
                                value = Double.parseDouble(p.get("FValue").toString());
                            } else {
                                value = Double.parseDouble(AValue);
                            }

                            if ("T_PatraElectric".equals(table)) {
                                p.put("ZBM", String.format("%.2f", value * 0.1229));
                            } else if ("T_PatraWater".equals(table)) {
                                //循环水
                                p.put("ZBM", String.format("%.2f", value * 0.1429));
                            } else if ("T_PatraGas".equals(table)) {
                                //压缩空气
                                p.put("ZBM", String.format("%.2f", value * 0.04));
                            }
                        }

                        //导出
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("回路名称");    //1
                        titles.add("回路编号");
                        titles.add("能源类型");    //2
                        titles.add("时间");    //3
                        titles.add("能源单位");
                        titles.add("能耗");
                        titles.add("折标煤");
                        dataMap.put("titles", titles);
                        List<PageData> varList = new ArrayList<PageData>();
                        for (int i = 0; i < allList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", allList.get(i).getString("FName"));        //1
                            vpd.put("var2", allList.get(i).getString("FCode"));
                            vpd.put("var3", allList.get(i).getString("EnergyType"));        //2
                            vpd.put("var4", allList.get(i).getString("FTime"));        //3
                            vpd.put("var5", allList.get(i).getString("FUnit"));
                            vpd.put("var6", allList.get(i).get("FValue").toString());
                            vpd.put("var7", allList.get(i).get("ZBM").toString());
                            varList.add(vpd);
                        }
                        dataMap.put("varList", varList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else {
                        //导出
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("回路名称");    //1
                        titles.add("回路编号");
                        titles.add("能源类型");    //2
                        titles.add("时间");    //3
                        titles.add("能源单位");
                        titles.add("能耗");
                        titles.add("折标煤");
                        dataMap.put("titles", titles);
                        List<PageData> varList = new ArrayList<PageData>();
                        dataMap.put("varList", varList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    }

                } catch (Exception e) {
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        return mv;
    }

    /**
     * 能源分析图表
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/analysisList")
    @ResponseBody
    public Object analysisList(Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        List<PageData> resList = new ArrayList<>();
        List<PageData> varList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {
                    if ("".equals(pd.getString("LOOP_ID"))) {
                        List<PageData> siteList = siteService.listAll(new PageData());
                        StringBuilder IDS = new StringBuilder();
                        IDS.append("(");
                        for (PageData site : siteList) {
                            IDS.append("'").append(site.getString("DM_SITE_ID")).append("',");
                        }

                        pd.put("IDS", IDS.substring(0, IDS.length() - 1) + ")");
                    } else {
                        String IDS = handleIDS(pd.getString("LOOP_ID")).getString("IDS");
                        PageData check = new PageData();
                        check.put("IDS", IDS);
                        List<PageData> siteList = siteService.checkIDSInSite(check);
                        StringBuilder ids = new StringBuilder("(");
                        if (siteList.size() > 0) {
                            for (PageData site : siteList) {
                                ids.append("'").append(site.getString("ID")).append("'").append(",");
                            }
                            pd.put("IDS", ids.substring(0, ids.length() - 1) + ")");
                        } else {
                            List<PageData> areaList = siteService.checkIDSInArea(check);
                            if (areaList.size() > 0) {
                                for (PageData area : areaList) {
                                    ids.append("'").append(area.getString("ID")).append("'").append(",");
                                }
                                pd.put("IDS", ids.substring(0, ids.length() - 1) + ")");
                            } else {
                                pd.put("IDS", IDS);
                            }
                        }
                    }

                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }
                    List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                    //字段名list
                    List<String> fieldList = new ArrayList<>();

                    for (PageData plc : plcList) {
                        String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                        fieldList.add(field);
                    }

                    //查询pd
                    PageData getValue = new PageData();
                    String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                    getValue.put("SaveField", field);

                    //获取值列表
                    if (fieldList.size() > 0) {


                        getValue.put("SaveTable", pd.getString("SaveTable"));
                        //获取时间条件
                        String StartTime = "";
                        String EndTime = "";
                        String FTime = pd.getString("FTime");

                        if ("日".equals(pd.getString("DateType"))) {
                            StartTime = FTime;
                            getValue.put("StartTime", StartTime);
                            String[] arr = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

                            for (String time : arr) {
                                getValue.put("time", time);

                                List<PageData> resultList = new ArrayList<>();

                                resultList = collectionService.getDayList(getValue);
                                if (resultList.size() == 0) {
                                    continue;
                                }

                                valueList.addAll(getDayValueList(plcList, resultList));
                            }
                        } else if ("月".equals(pd.getString("DateType"))) {
                            getValue.put("StartTime", FTime);
                            List<PageData> resultList = collectionService.getMonthList(getValue);

                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        } else if ("季度".equals(pd.getString("DateType"))) {

                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                StartTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("StartTime");
                                EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                            }
                            getValue.put("StartTime", StartTime);
                            getValue.put("EndTime", EndTime);
                            List<PageData> resultList = collectionService.getQuarterList(getValue);

                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        } else if ("年".equals(pd.getString("DateType"))) {
                            StartTime = FTime;
                            getValue.put("StartTime", StartTime);
                            List<PageData> resultList = collectionService.getYearList(getValue);
                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        }
                    }

                    //清空临时表数据
                    collectionService.deleteAll();

                    if (valueList.size() > 0) {
                        //批量插入结果
                        collectionService.saveAll(valueList);

                        //获取结果分页列表
                        resList = collectionService.getAnalysisList();

                        pageList = collectionService.getLoopdatalistPage(page);

                        PageData var = new PageData();
                        List<PageData> ColumnList = new ArrayList<>();
                        PageData col = new PageData();
                        if ("年".equals(pd.getString("DateType")) || "季度".equals(pd.getString("DateType"))) {
                            col.put("tableColumn", "月份");
                            col.put("tableColumnValue", "FName");
                        } else if ("月".equals(pd.getString("DateType"))) {
                            col.put("tableColumn", "月份");
                            col.put("tableColumnValue", "FName");
                        } else if ("日".equals(pd.getString("DateType"))) {
                            col.put("tableColumn", "时间");
                            col.put("tableColumnValue", "FName");
                        }
                        ColumnList.add(col);
                        for (PageData p : resList) {
                            PageData column = new PageData();
                            column.put("tableColumn", p.getString("FTime"));
                            column.put("tableColumnValue", p.getString("FTime"));
                            ColumnList.add(column);
                        }
                        var.put("ColumnList", ColumnList);
                        for (PageData loop : pageList) {
                            String code = loop.getString("FCode");
                            for (PageData p : resList) {
                                String resCode = p.getString("FCode");
                                if (code.equals(resCode)) {
                                    loop.put(p.getString("FTime"), p.get("FValue").toString());
                                }
                            }
                        }
                        List<PageData> ValueList = new ArrayList<>(pageList);
                        var.put("ValueList", ValueList);
                        varList.add(var);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        List<String> timeList = new ArrayList<>();
        List<Double> valList = new ArrayList<>();

        if (resList.size() > 0) {
            for (PageData var : resList) {
                if ("日".equals(pd.getString("DateType"))) {
                    timeList.add(var.getString("FTime").substring(11));
                } else {
                    timeList.add(var.getString("FTime"));
                }

                double value = Double.parseDouble(var.get("FValue").toString());
                valList.add(value);
            }
        }

        map.put("timeList", timeList);
        map.put("valList", valList);
        map.put("varList", varList);
        map.put("pageList", pageList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }


    /**
     * 能耗排行
     *
     * @return
     */
    @RequestMapping(value = "/rankList")
    @ResponseBody
    public Object rankList(Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        List<PageData> allList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {
                    String DateType = pd.getString("DateType");
                    String paramTime = pd.getString("FTime");

                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }


                    if ("日".equals(DateType) && Tools.date2Str(new Date(), "yyyy-MM-dd").equals(paramTime)) {

                        List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                        //字段名list
                        List<String> fieldList = new ArrayList<>();

                        for (PageData plc : plcList) {
                            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                            fieldList.add(field);
                        }

                        //查询pd
                        PageData getValue = new PageData();
                        String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                        getValue.put("SaveField", field);

                        //获取值列表
                        if (fieldList.size() > 0) {

                            getValue.put("SaveTable", pd.getString("SaveTable"));
                            //获取时间条件
                            String StartTime = "";
                            String EndTime = "";
                            String FTime = pd.getString("FTime");

                            if ("日".equals(pd.getString("DateType"))) {
                                StartTime = FTime;
                                getValue.put("StartTime", StartTime);
                                List<PageData> resultList = new ArrayList<>();

                                resultList = collectionService.getDayRank(getValue);

                                valueList.addAll(getValueList(plcList, resultList));

                            } else if ("月".equals(pd.getString("DateType"))) {
                                getValue.put("StartTime", FTime);
                                List<PageData> resultList = collectionService.getMonthRank(getValue);

                                if (resultList.size() > 0) {
                                    valueList.addAll(getValueList(plcList, resultList));
                                }
                            } else if ("季度".equals(pd.getString("DateType"))) {

                                //转换日期
                                if (null != FTime && !"".equals(FTime)) {
                                    int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                    int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                    StartTime = String.valueOf(quarter);
                                    EndTime = String.valueOf(year);
                                    //EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                                }
                                getValue.put("FYear", EndTime);
                                getValue.put("StartTime", StartTime);
//                            getValue.put("EndTime", EndTime);
                                List<PageData> resultList = collectionService.getQuarterRank(getValue);

                                if (resultList.size() > 0) {
                                    valueList.addAll(getValueList(plcList, resultList));
                                }
                            } else if ("年".equals(pd.getString("DateType"))) {
                                StartTime = FTime;
                                getValue.put("StartTime", StartTime);
                                List<PageData> resultList = collectionService.getYearRank(getValue);
                                if (resultList.size() > 0) {
                                    valueList.addAll(getValueList(plcList, resultList));
                                }
                            }
                        }

                        //清空临时表数据
                        collectionService.deleteAll();

                        if (valueList.size() > 0) {
                            //批量插入结果
                            collectionService.saveAll(valueList);

                            //获取结果列表
                            PageData getRank = new PageData();
                            pageList = collectionService.rankList(getRank);
                            allList = collectionService.getAnalysisAllList();
                        }
                    } else {
                        if ("日".equals(DateType)) {
                            pd.put("day", "day");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,11) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,11)");
                        } else if ("月".equals(DateType)) {
                            pd.put("month", "month");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,8) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,8)");
                        } else if ("年".equals(DateType)) {
                            pd.put("year", "year");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,5)");
                        } else if ("季度".equals(DateType)) {
                            String FTime = pd.getString("FTime");
                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                String year = FTime.substring(0, FTime.indexOf("年"));
                                String quarter = FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length());

                                pd.put("year", year);
                                pd.put("quarter", quarter);
                                pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{pd.year} AND datename(qq,FDATE) = #{pd.quarter}");
                                pd.put("queryTimeField", "datename(qq,FDATE)");
                            }

                        }

                        page.setPd(pd);
                        pageList = snapshotService.queryUseEnergyByIdsdatalistPage(page);
                        if ("日".equals(DateType)) {
                            pd.put("day", "day");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,11) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,11)");
                        } else if ("月".equals(DateType)) {
                            pd.put("month", "month");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,8) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,8)");
                        } else if ("年".equals(DateType)) {
                            pd.put("year", "year");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,5)");
                        } else if ("季度".equals(DateType)) {
                            String FTime = pd.getString("FTime");
                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                String year = FTime.substring(0, FTime.indexOf("年"));
                                String quarter = FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length());

                                pd.put("year", year);
                                pd.put("quarter", quarter);
                                pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{year} AND datename(qq,FDATE) = #{quarter}");
                                pd.put("queryTimeField", "datename(qq,FDATE)");
                            }

                        }
                        allList = snapshotService.queryUseEnergyByIds(pd);

                        //处理结果变成需要的列表
                        for (PageData p : pageList) {
                            p.put("NowTime", pd.getString("FTime"));
                            p.put("FTime", p.get("NowTime").toString());
                            p.put("FValue", p.get("NowValue").toString());

                            //存入回路名
                            String FName = "";
                            String DeptName1 = p.getString("DeptName1");
                            String DeptName2 = p.getString("DeptName2");
                            String DeptName3 = p.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            p.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = p.getString("FCode1");
                            String FCode2 = p.getString("FCode2");
                            String FCode3 = p.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            p.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "气");
                            }
                            p.put("FUnit", p.getString("Extend1"));

                        }
                        //处理结果变成需要的列表
                        for (PageData p : allList) {
                            p.put("NowTime", pd.getString("FTime"));
                            p.put("FTime", p.get("NowTime").toString());
                            p.put("FValue", p.get("NowValue").toString());

                            //存入回路名
                            String FName = "";
                            String DeptName1 = p.getString("DeptName1");
                            String DeptName2 = p.getString("DeptName2");
                            String DeptName3 = p.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            p.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = p.getString("FCode1");
                            String FCode2 = p.getString("FCode2");
                            String FCode3 = p.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            p.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "气");
                            }
                            p.put("FUnit", p.getString("Extend1"));

                        }
                    }

                } catch (Exception e) {
                    System.out.println(e);
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        List<String> loopList = new ArrayList<>();
        List<Double> valList = new ArrayList<>();
        List<PageData> objList = new ArrayList<>();

        if (allList.size() > 0) {
            for (PageData all : allList) {
                PageData obj = new PageData();
                loopList.add(all.getString("FName"));
                double value = Double.parseDouble(all.get("FValue").toString());
                valList.add(value);

                obj.put("name", all.getString("FName"));
                obj.put("value", Double.parseDouble(all.get("FValue").toString()));
                objList.add(obj);
            }
        }

        map.put("loopList", loopList);
        map.put("valList", valList);
        map.put("varList", pageList);
        map.put("objList", objList);
//        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 能耗导出
     *
     * @return
     */
    @RequestMapping(value = "/exportRankList")
    @ResponseBody
    public ModelAndView exportRankList() {
        ModelAndView mv = new ModelAndView();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        List<PageData> allList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {
                    String DateType = pd.getString("DateType");
                    String paramTime = pd.getString("FTime");

                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }

                    if ("日".equals(DateType) && Tools.date2Str(new Date(), "yyyy-MM-dd").equals(paramTime)) {

                        List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                        //字段名list
                        List<String> fieldList = new ArrayList<>();

                        for (PageData plc : plcList) {
                            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                            fieldList.add(field);
                        }

                        //查询pd
                        PageData getValue = new PageData();
                        String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                        getValue.put("SaveField", field);

                        //获取值列表
                        if (fieldList.size() > 0) {

                            getValue.put("SaveTable", pd.getString("SaveTable"));
                            //获取时间条件
                            String StartTime = "";
                            String EndTime = "";
                            String FTime = pd.getString("FTime");

                            if ("日".equals(pd.getString("DateType"))) {
                                StartTime = FTime;
                                getValue.put("StartTime", StartTime);
                                List<PageData> resultList = new ArrayList<>();

                                resultList = collectionService.getDayRank(getValue);

                                valueList.addAll(getValueList(plcList, resultList));

                            } else if ("月".equals(pd.getString("DateType"))) {
                                getValue.put("StartTime", FTime);
                                List<PageData> resultList = collectionService.getMonthRank(getValue);

                                if (resultList.size() > 0) {
                                    valueList.addAll(getValueList(plcList, resultList));
                                }
                            } else if ("季度".equals(pd.getString("DateType"))) {

                                //转换日期
                                if (null != FTime && !"".equals(FTime)) {
                                    int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                    int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                    StartTime = String.valueOf(quarter);
                                    EndTime = String.valueOf(year);
                                    //EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                                }
                                getValue.put("FYear", EndTime);
                                getValue.put("StartTime", StartTime);
//                            getValue.put("EndTime", EndTime);
                                List<PageData> resultList = collectionService.getQuarterRank(getValue);

                                if (resultList.size() > 0) {
                                    valueList.addAll(getValueList(plcList, resultList));
                                }
                            } else if ("年".equals(pd.getString("DateType"))) {
                                StartTime = FTime;
                                getValue.put("StartTime", StartTime);
                                List<PageData> resultList = collectionService.getYearRank(getValue);
                                if (resultList.size() > 0) {
                                    valueList.addAll(getValueList(plcList, resultList));
                                }
                            }
                        }

                        //清空临时表数据
                        collectionService.deleteAll();

                        if (valueList.size() > 0) {
                            //批量插入结果
                            collectionService.saveAll(valueList);

                            //获取结果列表
                            allList = collectionService.getAnalysisAllList();
                        }
                    } else {

                        if ("日".equals(DateType)) {
                            pd.put("day", "day");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,11) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,11)");
                        } else if ("月".equals(DateType)) {
                            pd.put("month", "month");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,8) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,8)");
                        } else if ("年".equals(DateType)) {
                            pd.put("year", "year");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,5)");
                        } else if ("季度".equals(DateType)) {
                            String FTime = pd.getString("FTime");
                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                String year = FTime.substring(0, FTime.indexOf("年"));
                                String quarter = FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length());

                                pd.put("year", year);
                                pd.put("quarter", quarter);
                                pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{year} AND datename(qq,FDATE) = #{quarter}");
                                pd.put("queryTimeField", "datename(qq,FDATE)");
                            }

                        }
                        allList = snapshotService.queryUseEnergyByIds(pd);

                        //处理结果变成需要的列表
                        for (PageData p : allList) {
                            p.put("NowTime", pd.getString("FTime"));
                            p.put("FTime", p.get("NowTime").toString());
                            p.put("FValue", p.get("NowValue").toString());

                            //存入回路名
                            String FName = "";
                            String DeptName1 = p.getString("DeptName1");
                            String DeptName2 = p.getString("DeptName2");
                            String DeptName3 = p.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            p.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = p.getString("FCode1");
                            String FCode2 = p.getString("FCode2");
                            String FCode3 = p.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            p.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "气");
                            }
                            p.put("FUnit", p.getString("Extend1"));

                        }
                    }
                    if (allList.size() > 0) {

                        //导出
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("回路名称");    //1
                        titles.add("回路编号");
                        titles.add("能源类型");    //2
                        titles.add("时间");    //3
                        titles.add("能源单位");
                        titles.add("能耗");
                        dataMap.put("titles", titles);
                        List<PageData> varList = new ArrayList<PageData>();
                        for (int i = 0; i < allList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", allList.get(i).getString("FName"));        //1
                            vpd.put("var2", allList.get(i).getString("FCode"));
                            vpd.put("var3", allList.get(i).getString("EnergyType"));        //2
                            vpd.put("var4", allList.get(i).getString("FTime"));        //3
                            vpd.put("var5", allList.get(i).getString("FUnit"));
                            vpd.put("var6", allList.get(i).get("FValue").toString());
                            varList.add(vpd);
                        }
                        dataMap.put("varList", varList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else {
                        //导出
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("回路名称");    //1
                        titles.add("回路编号");
                        titles.add("能源类型");    //2
                        titles.add("时间");    //3
                        titles.add("能源单位");
                        titles.add("能耗");
                        dataMap.put("titles", titles);
                        List<PageData> varList = new ArrayList<PageData>();
                        dataMap.put("varList", varList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    }

                } catch (Exception e) {
                    System.out.println(e);
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }
        return mv;
    }

    /**
     * 同比
     * v1 2021-10-25 sunlz
     *
     * @return
     */
    @RequestMapping(value = "/grewList")
    @ResponseBody
    public Object grewList(Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> pageList = new ArrayList<>();
        List<PageData> allList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {

                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }

                    page.setPd(pd);
                    pageList = snapshotService.queryGrewdatalistPage(page);
                    allList = snapshotService.queryGrewAll(pd);

                    for (PageData p : pageList) {

                        //存入回路名
                        String FName = "";
                        String DeptName1 = p.getString("DeptName1");
                        String DeptName2 = p.getString("DeptName2");
                        String DeptName3 = p.getString("DeptName3");
                        if (null != DeptName1 && !"".equals(DeptName1)) {
                            FName = DeptName1;
                        } else if (null != DeptName2 && !"".equals(DeptName2)) {
                            FName = DeptName2;
                        } else if (null != DeptName3 && !"".equals(DeptName3)) {
                            FName = DeptName3;
                        }
                        p.put("FName", FName);

                        //存入回路编号
                        String FCode = "";
                        String FCode1 = p.getString("FCode1");
                        String FCode2 = p.getString("FCode2");
                        String FCode3 = p.getString("FCode3");
                        if (null != FCode1 && !"".equals(FCode1)) {
                            FCode = FCode1;
                        } else if (null != FCode2 && !"".equals(FCode2)) {
                            FCode = FCode2;
                        } else if (null != FCode3 && !"".equals(FCode3)) {
                            FCode = FCode3;
                        }
                        p.put("FCode", FCode);

                        //存入能源类型
                        if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                            p.put("EnergyType", "电");
                        } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                            p.put("EnergyType", "水");
                        } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                            p.put("EnergyType", "气");
                        }
                        p.put("FUnit", p.getString("Extend1"));
                    }

                    for (PageData all : allList) {
                        //存入回路名
                        String FName = "";
                        String DeptName1 = all.getString("DeptName1");
                        String DeptName2 = all.getString("DeptName2");
                        String DeptName3 = all.getString("DeptName3");
                        if (null != DeptName1 && !"".equals(DeptName1)) {
                            FName = DeptName1;
                        } else if (null != DeptName2 && !"".equals(DeptName2)) {
                            FName = DeptName2;
                        } else if (null != DeptName3 && !"".equals(DeptName3)) {
                            FName = DeptName3;
                        }
                        all.put("FName", FName);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        List<String> loopList = new ArrayList<>();
        List<Double> nowList = new ArrayList<>();
        List<Double> beforeList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        if (allList.size() > 0) {
            for (PageData all : allList) {

                loopList.add(all.getString("FName"));
                double nowValue = Double.parseDouble(all.get("NowValue").toString());
                double beforeValue = Double.parseDouble(all.get("BeforeValue").toString());
                nowList.add(nowValue);
                beforeList.add(beforeValue);
            }
            timeList.add(allList.get(0).get("BeforeTime").toString());
            timeList.add(allList.get(0).get("NowTime").toString());
        }
        for (PageData pageData : pageList) {
            double diffValue = Double.parseDouble(pageData.get("NowValue").toString()) - Double.parseDouble(pageData.get("BeforeValue").toString());
            pageData.put("diffValue", String.format("%.2f", diffValue));
            if (diffValue > 0) {
                pageData.put("trend", "上升");
            } else if (diffValue < 0) {
                pageData.put("trend", "下降");
            } else {
                pageData.put("trend", "持平");
            }
        }

        map.put("timeList", timeList);
        map.put("loopList", loopList);
        map.put("nowList", nowList);
        map.put("beforeList", beforeList);
        map.put("varList", pageList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 同比导出
     *
     * @return
     */
    @RequestMapping(value = "/exportGrewList")
    @ResponseBody
    public ModelAndView exportGrewList() {
        ModelAndView mv = new ModelAndView();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> grewList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        List<PageData> allList = new ArrayList<>();

        String StartTime = "";
        String EndTime = "";

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {

                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }

                    allList = snapshotService.queryGrewAll(pd);

                    for (PageData all : allList) {

                        //存入回路名
                        String FName = "";
                        String DeptName1 = all.getString("DeptName1");
                        String DeptName2 = all.getString("DeptName2");
                        String DeptName3 = all.getString("DeptName3");
                        if (null != DeptName1 && !"".equals(DeptName1)) {
                            FName = DeptName1;
                        } else if (null != DeptName2 && !"".equals(DeptName2)) {
                            FName = DeptName2;
                        } else if (null != DeptName3 && !"".equals(DeptName3)) {
                            FName = DeptName3;
                        }
                        all.put("FName", FName);

                        //存入回路编号
                        String FCode = "";
                        String FCode1 = all.getString("FCode1");
                        String FCode2 = all.getString("FCode2");
                        String FCode3 = all.getString("FCode3");
                        if (null != FCode1 && !"".equals(FCode1)) {
                            FCode = FCode1;
                        } else if (null != FCode2 && !"".equals(FCode2)) {
                            FCode = FCode2;
                        } else if (null != FCode3 && !"".equals(FCode3)) {
                            FCode = FCode3;
                        }
                        all.put("FCode", FCode);

                        //存入能源类型
                        if ("T_PatraElectric".equals(all.getString("SaveTable"))) {
                            all.put("EnergyType", "电");
                        } else if ("T_PatraWater".equals(all.getString("SaveTable"))) {
                            all.put("EnergyType", "水");
                        } else if ("T_PatraGas".equals(all.getString("SaveTable"))) {
                            all.put("EnergyType", "气");
                        }
                        all.put("FUnit", all.getString("Extend1"));

                    }


                    if (allList.size() > 0) {

                        for (PageData pageData : allList) {
                            double diffValue = Double.parseDouble(pageData.get("NowValue").toString()) - Double.parseDouble(pageData.get("BeforeValue").toString());
                            pageData.put("diffValue", String.format("%.2f", diffValue));
                            if (diffValue > 0) {
                                pageData.put("trend", "上升");
                            } else if (diffValue < 0) {
                                pageData.put("trend", "下降");
                            } else {
                                pageData.put("trend", "持平");
                            }
                        }

                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("回路名称");    //1
                        titles.add("回路编码");
                        titles.add("同比年份");    //2
                        titles.add("同比能耗");    //3
                        titles.add("年份");
                        titles.add("能耗");
                        titles.add("差值");
                        titles.add("趋势");
                        titles.add("单位");
                        dataMap.put("titles", titles);
                        List<PageData> varList = new ArrayList<PageData>();
                        for (int i = 0; i < allList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", allList.get(i).getString("FName"));        //1
                            vpd.put("var2", allList.get(i).getString("FCode"));
                            vpd.put("var3", allList.get(i).get("BeforeTime").toString());        //2
                            vpd.put("var4", allList.get(i).get("BeforeValue").toString());        //3
                            vpd.put("var5", allList.get(i).get("NowTime").toString());
                            vpd.put("var6", allList.get(i).get("NowValue").toString());
                            vpd.put("var7", allList.get(i).getString("diffValue"));
                            vpd.put("var8", allList.get(i).getString("trend"));
                            vpd.put("var9", allList.get(i).getString("FUnit"));
                            varList.add(vpd);
                        }
                        dataMap.put("varList", varList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else {
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("回路名称");    //1
                        titles.add("回路编码");
                        titles.add("同比年份");    //2
                        titles.add("同比能耗");    //3
                        titles.add("年份");
                        titles.add("能耗");
                        titles.add("差值");
                        titles.add("趋势");
                        titles.add("单位");
                        dataMap.put("titles", titles);
                        List<PageData> varList = new ArrayList<PageData>();
                        dataMap.put("varList", varList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }
        return mv;
    }

    /**
     * 环比
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/ringList")
    @ResponseBody
    public Object ringList(Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        List<PageData> allList = new ArrayList<>();

        List<PageData> nowList = new ArrayList<>();
        List<PageData> beforeList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {
                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }
                    String DateType = pd.getString("DateType");
                    String paramTime = pd.getString("FTime");

                    if ("日".equals(DateType) && Tools.date2Str(new Date(), "yyyy-MM-dd").equals(paramTime)) {
                        List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                        //字段名list
                        List<String> fieldList = new ArrayList<>();

                        for (PageData plc : plcList) {
                            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                            fieldList.add(field);
                        }

                        //查询pd
                        PageData getValue = new PageData();
                        String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                        getValue.put("SaveField", field);

                        String FTime = pd.getString("FTime");
                        String BeforeTime = "";
                        String NowTime = "";

                        //获取值列表
                        if (fieldList.size() > 0) {

                            getValue.put("SaveTable", pd.getString("SaveTable"));
                            //获取时间条件
                            String StartTime = "";
                            String EndTime = "";

                            if ("日".equals(pd.getString("DateType"))) {

                                StartTime = FTime;
                                BeforeTime = DateResuceOne(StartTime);
                                NowTime = StartTime;

                                getValue.put("StartTime", StartTime);
                                List<PageData> resultList = new ArrayList<>();
                                nowList = collectionService.getDayRank(getValue);
                                getValue.put("StartTime", BeforeTime);
                                beforeList = collectionService.getDayRank(getValue);

                            } else if ("月".equals(pd.getString("DateType"))) {

                                BeforeTime = TimeUtil.MonthResuceOne(FTime);
                                NowTime = FTime;

                                getValue.put("StartTime", FTime);
                                nowList = collectionService.getMonthRank(getValue);

                                getValue.put("StartTime", BeforeTime);
                                beforeList = collectionService.getMonthRank(getValue);


                            } else if ("季度".equals(pd.getString("DateType"))) {

                                //转换日期
                                if (null != FTime && !"".equals(FTime)) {
                                    int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                    int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                    StartTime = String.valueOf(quarter);
                                    EndTime = String.valueOf(year);
                                    //EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                                }

                                NowTime = EndTime + "年" + StartTime + "季度";

                                getValue.put("FYear", EndTime);
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getQuarterRank(getValue);

                                //若季度为第一季度，则年份减一，季度为4
                                if ("1".equals(StartTime)) {
                                    BeforeTime = (Integer.parseInt(EndTime) - 1) + "年4季度";
                                    getValue.put("FYear", String.valueOf(Integer.parseInt(EndTime) - 1));
                                    getValue.put("StartTime", "4");
                                } else {
                                    BeforeTime = EndTime + "年" + (Integer.parseInt(StartTime) - 1) + "季度";
                                    getValue.put("FYear", EndTime);
                                    getValue.put("StartTime", String.valueOf(Integer.parseInt(StartTime) - 1));
                                }
                                beforeList = collectionService.getQuarterRank(getValue);

                                if (nowList.size() > 0) {
                                    for (PageData now : nowList) {
                                        now.put("TimeStamp", EndTime + "年" + now.getString("TimeStamp") + "季度");
                                    }
                                }
                                if (beforeList.size() > 0) {
                                    for (PageData before : beforeList) {
                                        before.put("TimeStamp", EndTime + "年" + before.getString("TimeStamp") + "季度");
                                    }
                                }

                            } else if ("年".equals(pd.getString("DateType"))) {

                                BeforeTime = String.valueOf(Integer.parseInt(pd.getString("FTime")) - 1);
                                NowTime = FTime;
                                StartTime = FTime;
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getYearRank(getValue);
                                getValue.put("StartTime", BeforeTime);
                                beforeList = collectionService.getYearRank(getValue);
                            }
                        }

                        //若结果为空则加入一组0
                        if (nowList.size() == 0) {
                            PageData nullPd = new PageData();
                            for (PageData plc : plcList) {
                                String nowField = plc.getString("SaveField");
                                nullPd.put(nowField, 0);
                            }
                            nullPd.put("TimeStamp", NowTime);
                            nowList.add(nullPd);
                        }
                        if (beforeList.size() == 0) {
                            PageData nullPd = new PageData();
                            for (PageData plc : plcList) {
                                String nowField = plc.getString("SaveField");
                                nullPd.put(nowField, 0);
                            }
                            nullPd.put("TimeStamp", BeforeTime);
                            beforeList.add(nullPd);
                        }

                        for (PageData plc : plcList) {
                            String Field = plc.getString("SaveField");
                            PageData now = nowList.get(0);
                            PageData before = beforeList.get(0);

                            PageData ringPd = new PageData();
                            String nowValue = now.get(Field).toString();
                            String beforeValue = before.get(Field).toString();
                            ringPd.put("nowValue", nowValue);
                            ringPd.put("beforeValue", beforeValue);
                            ringPd.put("nowTime", now.getString("TimeStamp"));
                            ringPd.put("beforeTime", before.getString("TimeStamp"));

                            //存入回路名
                            String FName = "";
                            String DeptName1 = plc.getString("DeptName1");
                            String DeptName2 = plc.getString("DeptName2");
                            String DeptName3 = plc.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            ringPd.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = plc.getString("FCode1");
                            String FCode2 = plc.getString("FCode2");
                            String FCode3 = plc.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            ringPd.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "气");
                            }
                            ringPd.put("FUnit", plc.getString("Extend1"));
                            valueList.add(ringPd);
                        }

                        //清空临时表数据
                        collectionService.deleteAllGrew();

                        if (valueList.size() > 0) {
                            //批量插入结果
                            collectionService.saveGrewAll(valueList);

                            //获取结果列表
                            pageList = collectionService.getGrewdatalistPage(page);
                            allList = collectionService.getGrewList(pd);
                        }
                    } else {

                        if ("日".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DateResuceOne(paramTime));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,11) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,11)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,11) = #{pd.BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,11)");
                        } else if ("月".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DiffMonth(paramTime, -1));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,8) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,8)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,8) = #{pd.BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,8)");
                        } else if ("年".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DateDiffOneYear(paramTime));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,5)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,5) = #{pd.BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,5)");
                        } else if ("季度".equals(DateType)) {
                            String FTime = pd.getString("FTime");
                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                Integer year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                Integer quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                String beforeYear = String.valueOf(year);
                                String beforeQuarter = String.valueOf(quarter - 1);
                                if (quarter == 1) {
                                    beforeYear = String.valueOf(year - 1);
                                    beforeQuarter = String.valueOf(4);
                                }

                                pd.put("year", String.valueOf(year));
                                pd.put("quarter", String.valueOf(quarter));
                                pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{pd.year} AND datename(qq,FDATE) = #{pd.quarter}");
                                pd.put("queryTimeField", "datename(qq,FDATE)");
                                pd.put("beforeYear", beforeYear);
                                pd.put("beforeQuarter", beforeQuarter);
                                pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,5) = #{pd.beforeYear} AND datename(qq,FDATE) = #{pd.beforeQuarter}");
                                pd.put("queryBeforeTimeField", "datename(qq,FDATE)");
                            }

                        }

                        page.setPd(pd);
                        pageList = snapshotService.queryRingdatalistPage(page);

                        if ("日".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DateResuceOne(paramTime));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,11) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,11)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,11) = #{BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,11)");
                        } else if ("月".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DiffMonth(paramTime, -1));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,8) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,8)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,8) = #{BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,8)");
                        } else if ("年".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DateDiffOneYear(paramTime));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,5)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,5) = #{BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,5)");
                        } else if ("季度".equals(DateType)) {
                            String FTime = pd.getString("FTime");
                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                Integer year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                Integer quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                String beforeYear = String.valueOf(year);
                                String beforeQuarter = String.valueOf(quarter - 1);
                                if (quarter == 1) {
                                    beforeYear = String.valueOf(year - 1);
                                    beforeQuarter = String.valueOf(4);
                                }

                                pd.put("year", String.valueOf(year));
                                pd.put("quarter", String.valueOf(quarter));
                                pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{year} AND datename(qq,FDATE) = #{quarter}");
                                pd.put("queryTimeField", "datename(qq,FDATE)");
                                pd.put("beforeYear", beforeYear);
                                pd.put("beforeQuarter", beforeQuarter);
                                pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,5) = #{beforeYear} AND datename(qq,FDATE) = #{beforeQuarter}");
                                pd.put("queryBeforeTimeField", "datename(qq,FDATE)");
                            }

                        }

                        allList = snapshotService.queryRing(pd);


                        //处理结果变成需要的列表
                        for (PageData p : pageList) {
                            String FTime = pd.getString("FTime");
                            p.put("NowTime", FTime);
                            if ("季度".equals(DateType)) {
                                int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                if (quarter == 1) {
                                    p.put("BeforeTime", (year - 1) + "年4季度");
                                } else {
                                    p.put("BeforeTime", year + "年" + (quarter - 1) + "季度");
                                }
                            }
                            //存入回路名
                            String FName = "";
                            String DeptName1 = p.getString("DeptName1");
                            String DeptName2 = p.getString("DeptName2");
                            String DeptName3 = p.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            p.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = p.getString("FCode1");
                            String FCode2 = p.getString("FCode2");
                            String FCode3 = p.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            p.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "气");
                            }
                            p.put("FUnit", p.getString("Extend1"));

                        }

                        //处理结果变成需要的列表
                        for (PageData p : allList) {
                            String FTime = pd.getString("FTime");
                            p.put("NowTime", FTime);
                            if ("季度".equals(DateType)) {
                                int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                if (quarter == 1) {
                                    p.put("BeforeTime", (year - 1) + "年4季度");
                                } else {
                                    p.put("BeforeTime", year + "年" + (quarter - 1) + "季度");
                                }
                            }

                            //存入回路名
                            String FName = "";
                            String DeptName1 = p.getString("DeptName1");
                            String DeptName2 = p.getString("DeptName2");
                            String DeptName3 = p.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            p.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = p.getString("FCode1");
                            String FCode2 = p.getString("FCode2");
                            String FCode3 = p.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            p.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "气");
                            }
                            p.put("FUnit", p.getString("Extend1"));

                        }
                    }


                } catch (Exception e) {
                    System.out.println(e);
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        List<String> loopList = new ArrayList<>();
        List<Double> nowValueList = new ArrayList<>();
        List<Double> beforeValueList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        if (allList.size() > 0) {
            for (PageData all : allList) {

                loopList.add(all.getString("FName"));
                double nowValue = Double.parseDouble(all.get("NowValue").toString());
                double beforeValue = Double.parseDouble(all.get("BeforeValue").toString());
                nowValueList.add(nowValue);
                beforeValueList.add(beforeValue);
            }
            timeList.add(allList.get(0).getString("BeforeTime"));
            timeList.add(allList.get(0).getString("NowTime"));
        }
        for (PageData pageData : pageList) {
            double diffValue = Double.parseDouble(pageData.get("NowValue").toString()) - Double.parseDouble(pageData.get("BeforeValue").toString());
            pageData.put("diffValue", String.format("%.2f", diffValue));
            if (diffValue > 0) {
                pageData.put("trend", "上升");
            } else if (diffValue < 0) {
                pageData.put("trend", "下降");
            } else {
                pageData.put("trend", "持平");
            }
        }

        map.put("timeList", timeList);
        map.put("loopList", loopList);
        map.put("nowList", nowValueList);
        map.put("beforeList", beforeValueList);
        map.put("varList", pageList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 环比导出
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/exportRingList")
    @ResponseBody
    public ModelAndView exportRingList() {
        ModelAndView mv = new ModelAndView();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();
        List<PageData> allList = new ArrayList<>();

        List<PageData> nowList = new ArrayList<>();
        List<PageData> beforeList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {

                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }
                    String DateType = pd.getString("DateType");
                    String paramTime = pd.getString("FTime");

                    if ("日".equals(DateType) && Tools.date2Str(new Date(), "yyyy-MM-dd").equals(paramTime)) {
                        List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                        //字段名list
                        List<String> fieldList = new ArrayList<>();

                        for (PageData plc : plcList) {
                            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                            fieldList.add(field);
                        }

                        //查询pd
                        PageData getValue = new PageData();
                        String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                        getValue.put("SaveField", field);

                        String FTime = pd.getString("FTime");
                        String BeforeTime = "";
                        String NowTime = "";

                        //获取值列表
                        if (fieldList.size() > 0) {

                            getValue.put("SaveTable", pd.getString("SaveTable"));
                            //获取时间条件
                            String StartTime = "";
                            String EndTime = "";

                            if ("日".equals(pd.getString("DateType"))) {

                                StartTime = FTime;
                                BeforeTime = DateResuceOne(StartTime);
                                NowTime = StartTime;

                                getValue.put("StartTime", StartTime);
                                List<PageData> resultList = new ArrayList<>();
                                nowList = collectionService.getDayRank(getValue);
                                getValue.put("StartTime", BeforeTime);
                                beforeList = collectionService.getDayRank(getValue);

                            } else if ("月".equals(pd.getString("DateType"))) {

                                BeforeTime = TimeUtil.MonthResuceOne(FTime);
                                NowTime = FTime;

                                getValue.put("StartTime", FTime);
                                nowList = collectionService.getMonthRank(getValue);

                                getValue.put("StartTime", BeforeTime);
                                beforeList = collectionService.getMonthRank(getValue);


                            } else if ("季度".equals(pd.getString("DateType"))) {

                                //转换日期
                                if (null != FTime && !"".equals(FTime)) {
                                    int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                    int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                    StartTime = String.valueOf(quarter);
                                    EndTime = String.valueOf(year);
                                    //EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                                }

                                NowTime = EndTime + "年" + StartTime + "季度";

                                getValue.put("FYear", EndTime);
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getQuarterRank(getValue);

                                //若季度为第一季度，则年份减一，季度为4
                                if ("1".equals(StartTime)) {
                                    BeforeTime = (Integer.parseInt(EndTime) - 1) + "年4季度";
                                    getValue.put("FYear", String.valueOf(Integer.parseInt(EndTime) - 1));
                                    getValue.put("StartTime", "4");
                                } else {
                                    BeforeTime = EndTime + "年" + (Integer.parseInt(StartTime) - 1) + "季度";
                                    getValue.put("FYear", EndTime);
                                    getValue.put("StartTime", String.valueOf(Integer.parseInt(StartTime) - 1));
                                }
                                beforeList = collectionService.getQuarterRank(getValue);

                                if (nowList.size() > 0) {
                                    for (PageData now : nowList) {
                                        now.put("TimeStamp", EndTime + "年" + now.getString("TimeStamp") + "季度");
                                    }
                                }
                                if (beforeList.size() > 0) {
                                    for (PageData before : beforeList) {
                                        before.put("TimeStamp", EndTime + "年" + before.getString("TimeStamp") + "季度");
                                    }
                                }

                            } else if ("年".equals(pd.getString("DateType"))) {

                                BeforeTime = String.valueOf(Integer.parseInt(pd.getString("FTime")) - 1);
                                NowTime = FTime;
                                StartTime = FTime;
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getYearRank(getValue);
                                getValue.put("StartTime", BeforeTime);
                                beforeList = collectionService.getYearRank(getValue);
                            }
                        }

                        //若结果为空则加入一组0
                        if (nowList.size() == 0) {
                            PageData nullPd = new PageData();
                            for (PageData plc : plcList) {
                                String nowField = plc.getString("SaveField");
                                nullPd.put(nowField, 0);
                            }
                            nullPd.put("TimeStamp", NowTime);
                            nowList.add(nullPd);
                        }
                        if (beforeList.size() == 0) {
                            PageData nullPd = new PageData();
                            for (PageData plc : plcList) {
                                String nowField = plc.getString("SaveField");
                                nullPd.put(nowField, 0);
                            }
                            nullPd.put("TimeStamp", BeforeTime);
                            beforeList.add(nullPd);
                        }

                        for (PageData plc : plcList) {
                            String Field = plc.getString("SaveField");
                            PageData now = nowList.get(0);
                            PageData before = beforeList.get(0);

                            PageData ringPd = new PageData();
                            String nowValue = now.get(Field).toString();
                            String beforeValue = before.get(Field).toString();
                            ringPd.put("nowValue", nowValue);
                            ringPd.put("beforeValue", beforeValue);
                            ringPd.put("nowTime", now.getString("TimeStamp"));
                            ringPd.put("beforeTime", before.getString("TimeStamp"));

                            //存入回路名
                            String FName = "";
                            String DeptName1 = plc.getString("DeptName1");
                            String DeptName2 = plc.getString("DeptName2");
                            String DeptName3 = plc.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            ringPd.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = plc.getString("FCode1");
                            String FCode2 = plc.getString("FCode2");
                            String FCode3 = plc.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            ringPd.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "气");
                            }
                            ringPd.put("FUnit", plc.getString("Extend1"));
                            valueList.add(ringPd);
                        }

                        //清空临时表数据
                        collectionService.deleteAllGrew();

                        if (valueList.size() > 0) {
                            //批量插入结果
                            collectionService.saveGrewAll(valueList);

                            allList = collectionService.getGrewList(pd);
                        }
                    } else {

                        if ("日".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DateResuceOne(paramTime));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,11) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,11)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,11) = #{BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,11)");
                        } else if ("月".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DiffMonth(paramTime, -1));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,8) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,8)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,8) = #{BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,8)");
                        } else if ("年".equals(DateType)) {
                            pd.put("BeforeTime", TimeUtil.DateDiffOneYear(paramTime));
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,5)");
                            pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,5) = #{BeforeTime}");
                            pd.put("queryBeforeTimeField", "SUBSTRING(FDATE,0,5)");
                        } else if ("季度".equals(DateType)) {
                            String FTime = pd.getString("FTime");
                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                Integer year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                Integer quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                String beforeYear = String.valueOf(year);
                                String beforeQuarter = String.valueOf(quarter - 1);
                                if (quarter == 1) {
                                    beforeYear = String.valueOf(year - 1);
                                    beforeQuarter = String.valueOf(4);
                                }

                                pd.put("year", String.valueOf(year));
                                pd.put("quarter", String.valueOf(quarter));
                                pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{year} AND datename(qq,FDATE) = #{quarter}");
                                pd.put("queryTimeField", "datename(qq,FDATE)");
                                pd.put("beforeYear", beforeYear);
                                pd.put("beforeQuarter", beforeQuarter);
                                pd.put("beforeQueryCondition", "AND SUBSTRING(FDATE,0,5) = #{beforeYear} AND datename(qq,FDATE) = #{beforeQuarter}");
                                pd.put("queryBeforeTimeField", "datename(qq,FDATE)");
                            }

                        }

                        allList = snapshotService.queryRing(pd);

                        //处理结果变成需要的列表
                        for (PageData p : allList) {
                            String FTime = pd.getString("FTime");
                            p.put("NowTime", FTime);
                            if ("季度".equals(DateType)) {
                                int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                if (quarter == 1) {
                                    p.put("BeforeTime", (year - 1) + "年4季度");
                                } else {
                                    p.put("BeforeTime", year + "年" + (quarter - 1) + "季度");
                                }
                            }

                            //存入回路名
                            String FName = "";
                            String DeptName1 = p.getString("DeptName1");
                            String DeptName2 = p.getString("DeptName2");
                            String DeptName3 = p.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            p.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = p.getString("FCode1");
                            String FCode2 = p.getString("FCode2");
                            String FCode3 = p.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            p.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "气");
                            }
                            p.put("FUnit", p.getString("Extend1"));

                        }
                    }

                } catch (Exception e) {
                    System.out.println(e);
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        if (allList.size() > 0) {

            for (PageData pageData : allList) {
                double diffValue = Double.parseDouble(pageData.get("NowValue").toString()) - Double.parseDouble(pageData.get("BeforeValue").toString());
                pageData.put("diffValue", String.valueOf(diffValue));
                if (diffValue > 0) {
                    pageData.put("trend", "上升");
                } else if (diffValue < 0) {
                    pageData.put("trend", "下降");
                } else {
                    pageData.put("trend", "持平");
                }
            }

            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            titles.add("回路名称");    //1
            titles.add("回路编码");
            titles.add("环比时间");    //2
            titles.add("环比能耗");    //3
            titles.add("年份");
            titles.add("能耗");
            titles.add("差值");
            titles.add("趋势");
            titles.add("单位");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<PageData>();
            for (int i = 0; i < allList.size(); i++) {
                PageData vpd = new PageData();
                vpd.put("var1", allList.get(i).getString("FName"));        //1
                vpd.put("var2", allList.get(i).getString("FCode"));
                vpd.put("var3", allList.get(i).getString("BeforeTime"));        //2
                vpd.put("var4", allList.get(i).get("BeforeValue").toString());        //3
                vpd.put("var5", allList.get(i).getString("NowTime"));
                vpd.put("var6", allList.get(i).get("NowValue").toString());
                vpd.put("var7", allList.get(i).get("diffValue").toString());
                vpd.put("var8", allList.get(i).get("trend").toString());
                vpd.put("var9", allList.get(i).getString("FUnit"));
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, dataMap);
        } else {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            titles.add("回路名称");    //1
            titles.add("回路编码");
            titles.add("环比时间");    //2
            titles.add("环比能耗");    //3
            titles.add("年份");
            titles.add("能耗");
            titles.add("差值");
            titles.add("趋势");
            titles.add("单位");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<PageData>();
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, dataMap);
        }

        return mv;
    }


    /**
     * 用能报表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/useEnergyList")
    @ResponseBody
    public Object useEnergyList(Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> pageList = new ArrayList<>();

        List<PageData> nowList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {
                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }
                    List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                    String DateType = pd.getString("DateType");
                    String paramTime = pd.getString("FTime");
                    if ("日".equals(DateType) && Tools.date2Str(new Date(), "yyyy-MM-dd").equals(paramTime)) {
                        //字段名list
                        List<String> fieldList = new ArrayList<>();

                        for (PageData plc : plcList) {
                            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                            fieldList.add(field);
                        }

                        //查询pd
                        PageData getValue = new PageData();
                        String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                        getValue.put("SaveField", field);

                        String FTime = pd.getString("FTime");
                        String BeforeTime = "";
                        String NowTime = "";

                        //获取值列表
                        if (fieldList.size() > 0) {

                            getValue.put("SaveTable", pd.getString("SaveTable"));
                            //获取时间条件
                            String StartTime = "";
                            String EndTime = "";

                            if ("日".equals(pd.getString("DateType"))) {

                                StartTime = FTime;
                                NowTime = StartTime;
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getDayRank(getValue);

                            } else if ("月".equals(pd.getString("DateType"))) {

                                NowTime = FTime;

                                getValue.put("StartTime", FTime);
                                nowList = collectionService.getMonthRank(getValue);

                            } else if ("季度".equals(pd.getString("DateType"))) {

                                //转换日期
                                if (null != FTime && !"".equals(FTime)) {
                                    int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                    int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                    StartTime = String.valueOf(quarter);
                                    EndTime = String.valueOf(year);
                                    //EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                                }

                                NowTime = EndTime + "年" + StartTime + "季度";

                                getValue.put("FYear", EndTime);
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getQuarterRank(getValue);


                                if (nowList.size() > 0) {
                                    for (PageData now : nowList) {
                                        now.put("TimeStamp", EndTime + "年" + now.getString("TimeStamp") + "季度");
                                    }
                                }

                            } else if ("年".equals(pd.getString("DateType"))) {

                                NowTime = FTime;
                                StartTime = FTime;
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getYearRank(getValue);
                            }
                        }

                        //若结果为空则加入一组0
                        if (nowList.size() == 0) {
                            PageData nullPd = new PageData();
                            for (PageData plc : plcList) {
                                String nowField = plc.getString("SaveField");
                                nullPd.put(nowField, 0);
                            }
                            nullPd.put("TimeStamp", NowTime);
                            nowList.add(nullPd);
                        }

                        //处理结果变成需要的列表
                        for (PageData plc : plcList) {
                            String Field = plc.getString("SaveField");
                            PageData now = nowList.get(0);

                            PageData ringPd = new PageData();
                            String nowValue = now.get(Field).toString();
                            ringPd.put("nowValue", nowValue);
                            ringPd.put("nowTime", now.getString("TimeStamp"));

                            //存入回路名
                            String FName = "";
                            String DeptName1 = plc.getString("DeptName1");
                            String DeptName2 = plc.getString("DeptName2");
                            String DeptName3 = plc.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            ringPd.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = plc.getString("FCode1");
                            String FCode2 = plc.getString("FCode2");
                            String FCode3 = plc.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            ringPd.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "气");
                            }
                            ringPd.put("FUnit", plc.getString("Extend1"));
                            valueList.add(ringPd);
                        }

                        //清空临时表数据
                        collectionService.deleteAllGrew();

                        if (valueList.size() > 0) {
                            //批量插入结果
                            collectionService.saveGrewAll(valueList);

                            //获取结果列表
                            pageList = collectionService.getGrewdatalistPage(page);
                        }
                    } else {
                        if ("日".equals(DateType)) {
                            pd.put("day", "day");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,11) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,11)");
                        } else if ("月".equals(DateType)) {
                            pd.put("month", "month");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,8) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,8)");
                        } else if ("年".equals(DateType)) {
                            pd.put("year", "year");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{pd.FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,5)");
                        } else if ("季度".equals(DateType)) {
                            String FTime = pd.getString("FTime");
                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                String year = FTime.substring(0, FTime.indexOf("年"));
                                String quarter = FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length());

                                pd.put("year", year);
                                pd.put("quarter", quarter);
                                pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{pd.year} AND datename(qq,FDATE) = #{pd.quarter}");
                                pd.put("queryTimeField", "datename(qq,FDATE)");
                            }

                        }

                        page.setPd(pd);
                        pageList = snapshotService.queryUseEnergyByIdsdatalistPage(page);

                        //处理结果变成需要的列表
                        for (PageData p : pageList) {
                            p.put("NowTime", pd.getString("FTime"));
                            //存入回路名
                            String FName = "";
                            String DeptName1 = p.getString("DeptName1");
                            String DeptName2 = p.getString("DeptName2");
                            String DeptName3 = p.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            p.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = p.getString("FCode1");
                            String FCode2 = p.getString("FCode2");
                            String FCode3 = p.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            p.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "气");
                            }
                            p.put("FUnit", p.getString("Extend1"));

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        map.put("varList", pageList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 用能报表导出
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/exportUseEnergyList")
    @ResponseBody
    public ModelAndView exportUseEnergyList() {
        ModelAndView mv = new ModelAndView();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> varOList = new ArrayList<>();

        List<PageData> nowList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            if (null != pd.getString("FTime") && !"".equals(pd.getString("FTime"))) {

                try {
                    pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));
//                    pd.put("ParamType", "有功电能");
                    if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                        pd.put("ParamType", "有功电能");
                    } else {
                        pd.put("ParamType", "流量");
                    }
                    List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                    String DateType = pd.getString("DateType");
                    String paramTime = pd.getString("FTime");
                    if ("日".equals(DateType) && Tools.date2Str(new Date(), "yyyy-MM-dd").equals(paramTime)) {

                        //字段名list
                        List<String> fieldList = new ArrayList<>();

                        for (PageData plc : plcList) {
                            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                            fieldList.add(field);
                        }

                        //查询pd
                        PageData getValue = new PageData();
                        String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                        getValue.put("SaveField", field);

                        String FTime = pd.getString("FTime");
                        String BeforeTime = "";
                        String NowTime = "";

                        //获取值列表
                        if (fieldList.size() > 0) {

                            getValue.put("SaveTable", pd.getString("SaveTable"));
                            //获取时间条件
                            String StartTime = "";
                            String EndTime = "";

                            if ("日".equals(pd.getString("DateType"))) {

                                StartTime = FTime;
                                NowTime = StartTime;
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getDayRank(getValue);

                            } else if ("月".equals(pd.getString("DateType"))) {

                                NowTime = FTime;

                                getValue.put("StartTime", FTime);
                                nowList = collectionService.getMonthRank(getValue);

                            } else if ("季度".equals(pd.getString("DateType"))) {

                                //转换日期
                                if (null != FTime && !"".equals(FTime)) {
                                    int year = Integer.parseInt(FTime.substring(0, FTime.indexOf("年")));
                                    int quarter = Integer.parseInt(FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length()));
                                    StartTime = String.valueOf(quarter);
                                    EndTime = String.valueOf(year);
                                    //EndTime = TimeUtil.getQuarterStartEndTime(year, quarter).getString("EndTime");
                                }

                                NowTime = EndTime + "年" + StartTime + "季度";

                                getValue.put("FYear", EndTime);
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getQuarterRank(getValue);


                                if (nowList.size() > 0) {
                                    for (PageData now : nowList) {
                                        now.put("TimeStamp", EndTime + "年" + now.getString("TimeStamp") + "季度");
                                    }
                                }

                            } else if ("年".equals(pd.getString("DateType"))) {

                                NowTime = FTime;
                                StartTime = FTime;
                                getValue.put("StartTime", StartTime);
                                nowList = collectionService.getYearRank(getValue);
                            }
                        }

                        //若结果为空则加入一组0
                        if (nowList.size() == 0) {
                            PageData nullPd = new PageData();
                            for (PageData plc : plcList) {
                                String nowField = plc.getString("SaveField");
                                nullPd.put(nowField, 0);
                            }
                            nullPd.put("TimeStamp", NowTime);
                            nowList.add(nullPd);
                        }

                        //处理结果变成需要的列表
                        for (PageData plc : plcList) {
                            String Field = plc.getString("SaveField");
                            PageData now = nowList.get(0);

                            PageData ringPd = new PageData();
                            String nowValue = now.get(Field).toString();
                            ringPd.put("nowValue", nowValue);
                            ringPd.put("nowTime", now.getString("TimeStamp"));

                            //存入回路名
                            String FName = "";
                            String DeptName1 = plc.getString("DeptName1");
                            String DeptName2 = plc.getString("DeptName2");
                            String DeptName3 = plc.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            ringPd.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = plc.getString("FCode1");
                            String FCode2 = plc.getString("FCode2");
                            String FCode3 = plc.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            ringPd.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(plc.getString("SaveTable"))) {
                                ringPd.put("EnergyType", "气");
                            }
                            ringPd.put("FUnit", plc.getString("Extend1"));
                            valueList.add(ringPd);
                        }

                        //清空临时表数据
                        collectionService.deleteAllGrew();

                        if (valueList.size() > 0) {
                            //批量插入结果
                            collectionService.saveGrewAll(valueList);
                        }

                        //获取结果列表
                        varOList = collectionService.getGrewList(new PageData());

                    } else {
                        if ("日".equals(DateType)) {
                            pd.put("day", "day");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,11) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,11)");
                        } else if ("月".equals(DateType)) {
                            pd.put("month", "month");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,8) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,8)");
                        } else if ("年".equals(DateType)) {
                            pd.put("year", "year");
                            pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{FTime}");
                            pd.put("queryTimeField", "SUBSTRING(FDATE,0,5)");
                        } else if ("季度".equals(DateType)) {
                            String FTime = pd.getString("FTime");
                            //转换日期
                            if (null != FTime && !"".equals(FTime)) {
                                String year = FTime.substring(0, FTime.indexOf("年"));
                                String quarter = FTime.substring(FTime.indexOf("年"), FTime.indexOf("季")).substring("年".length());

                                pd.put("year", year);
                                pd.put("quarter", quarter);
                                pd.put("queryCondition", "AND SUBSTRING(FDATE,0,5) = #{year} AND datename(qq,FDATE) = #{quarter}");
                                pd.put("queryTimeField", "datename(qq,FDATE)");
                            }

                        }

                        varOList = snapshotService.queryUseEnergyByIds(pd);

                        //处理结果变成需要的列表
                        for (PageData p : varOList) {
                            p.put("NowTime", pd.getString("FTime"));
                            //存入回路名
                            String FName = "";
                            String DeptName1 = p.getString("DeptName1");
                            String DeptName2 = p.getString("DeptName2");
                            String DeptName3 = p.getString("DeptName3");
                            if (null != DeptName1 && !"".equals(DeptName1)) {
                                FName = DeptName1;
                            } else if (null != DeptName2 && !"".equals(DeptName2)) {
                                FName = DeptName2;
                            } else if (null != DeptName3 && !"".equals(DeptName3)) {
                                FName = DeptName3;
                            }
                            p.put("FName", FName);

                            //存入回路编号
                            String FCode = "";
                            String FCode1 = p.getString("FCode1");
                            String FCode2 = p.getString("FCode2");
                            String FCode3 = p.getString("FCode3");
                            if (null != FCode1 && !"".equals(FCode1)) {
                                FCode = FCode1;
                            } else if (null != FCode2 && !"".equals(FCode2)) {
                                FCode = FCode2;
                            } else if (null != FCode3 && !"".equals(FCode3)) {
                                FCode = FCode3;
                            }
                            p.put("FCode", FCode);

                            //存入能源类型
                            if ("T_PatraElectric".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "电");
                            } else if ("T_PatraWater".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "水");
                            } else if ("T_PatraGas".equals(p.getString("SaveTable"))) {
                                p.put("EnergyType", "气");
                            }
                            p.put("FUnit", p.getString("Extend1"));

                        }

                    }

                    if (varOList.size() > 0) {

                        //导出
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("回路名称");    //1
                        titles.add("回路编号");
                        titles.add("能源类型");    //2
                        titles.add("时间");    //3
                        titles.add("能源单位");
                        titles.add("能耗");
                        dataMap.put("titles", titles);
                        List<PageData> varList = new ArrayList<PageData>();
                        for (int i = 0; i < varOList.size(); i++) {
                            PageData vpd = new PageData();
                            vpd.put("var1", varOList.get(i).getString("FName"));        //1
                            vpd.put("var2", varOList.get(i).getString("FCode"));
                            vpd.put("var3", varOList.get(i).getString("EnergyType"));        //2
                            vpd.put("var4", varOList.get(i).getString("NowTime"));        //3
                            vpd.put("var5", varOList.get(i).getString("FUnit"));
                            vpd.put("var6", varOList.get(i).get("NowValue").toString());
                            varList.add(vpd);
                        }
                        dataMap.put("varList", varList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    } else {
                        //导出
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        List<String> titles = new ArrayList<String>();
                        titles.add("回路名称");    //1
                        titles.add("回路编号");
                        titles.add("能源类型");    //2
                        titles.add("时间");    //3
                        titles.add("能源单位");
                        titles.add("能耗");
                        dataMap.put("titles", titles);
                        List<PageData> varList = new ArrayList<PageData>();
                        dataMap.put("varList", varList);
                        ObjectExcelView erv = new ObjectExcelView();
                        mv = new ModelAndView(erv, dataMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    errInfo = "程序错误!请联系管理员";
                }

            } else {
                errInfo = "未传入时间";
            }
        } else {
            errInfo = "请选择能源类型";
        }
        return mv;
    }

    /**
     * 用能概况趋势图
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/useEnergyTrendList")
    @ResponseBody
    public Object useEnergyTrendList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<PageData> valueList = new ArrayList<>();
        List<PageData> allList = new ArrayList<>();
        List<Double> varList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {


            try {
                pd.put("IDS", handleIDS(pd.getString("LOOP_ID")).getString("IDS"));

                if ("T_PatraElectric".equals(pd.getString("SaveTable"))) {
                    pd.put("ParamType", "有功电能");
                } else {
                    pd.put("ParamType", "流量");
                }
                List<PageData> plcList = collectionService.plcList(pd);    //列出plc列表

                if (!"年".equals(pd.getString("DateType"))) {
                    //字段名list
                    List<String> fieldList = new ArrayList<>();

                    for (PageData plc : plcList) {
                        String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + "";
                        fieldList.add(field);
                    }

                    //查询pd
                    PageData getValue = new PageData();
                    String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
                    getValue.put("SaveField", field);

                    //获取值列表
                    if (fieldList.size() > 0) {


                        getValue.put("SaveTable", pd.getString("SaveTable"));
                        //获取时间条件
                        String StartTime = "";

                        if ("日".equals(pd.getString("DateType"))) {
                            getValue.put("StartTime", Tools.date2Str(new Date(), "yyyy-MM-dd"));
                            String[] arr = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

                            for (String time : arr) {
                                getValue.put("time", time);

                                List<PageData> resultList = new ArrayList<>();

                                resultList = collectionService.getDayList(getValue);
                                if (resultList.size() == 0) {
                                    continue;
                                }

                                valueList.addAll(getDayValueList(plcList, resultList));
                            }
                        } else if ("月".equals(pd.getString("DateType"))) {
                            getValue.put("StartTime", Tools.date2Str(new Date(), "yyyy-MM"));
                            List<PageData> resultList = collectionService.getMonthList(getValue);

                            if (resultList.size() > 0) {
                                valueList.addAll(getValueList(plcList, resultList));
                            }
                        }
//                    else if ("年".equals(pd.getString("DateType"))) {
//                        StartTime = Tools.date2Str(new Date(), "yyyy");
//                        getValue.put("StartTime", StartTime);
//                        List<PageData> resultList = collectionService.getYearList(getValue);
//                        if (resultList.size() > 0) {
//                            valueList.addAll(getValueList(plcList, resultList));
//                        }
//                    }
                    }

                    //清空临时表数据
                    collectionService.deleteAll();

                    if (valueList.size() > 0) {
                        //批量插入结果
                        collectionService.saveAll(valueList);

                        //获取结果分页列表
                        allList = collectionService.getResultList();
                    }
                } else {
                    allList = snapshotService.queryYearUseEnergyByIds(pd);
                }

                if (allList.size() > 0) {
                    for (PageData all : allList) {
                        Double var = Double.parseDouble(all.get("FValue").toString());
                        String time = all.get("FTime").toString();
                        varList.add(var);
                        timeList.add(time);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }

        } else {
            errInfo = "请选择能源类型";
        }

        map.put("varList", varList);
        map.put("timeList", timeList);

        map.put("result", errInfo);
        return map;
    }


    //处理查询结果
    public static List<PageData> getValueList(List<PageData> plcList, List<PageData> list) {

        List<PageData> resultList = new ArrayList<>();

        for (PageData plc : plcList) {

            for (PageData pd : list) {

                PageData var = new PageData();
                String SaveField = plc.getString("SaveField");

                String FValue = pd.get(SaveField).toString();
                var.put("FValue", FValue);
                var.put("FTime", pd.getString("TimeStamp"));

                //存入回路名
                String FName = "";
                String DeptName1 = plc.getString("DeptName1");
                String DeptName2 = plc.getString("DeptName2");
                String DeptName3 = plc.getString("DeptName3");
                if (null != DeptName1 && !"".equals(DeptName1)) {
                    FName = DeptName1;
                } else if (null != DeptName2 && !"".equals(DeptName2)) {
                    FName = DeptName2;
                } else if (null != DeptName3 && !"".equals(DeptName3)) {
                    FName = DeptName3;
                }
                var.put("FName", FName);

                //存入回路编号
                String FCode = "";
                String FCode1 = plc.getString("FCode1");
                String FCode2 = plc.getString("FCode2");
                String FCode3 = plc.getString("FCode3");
                if (null != FCode1 && !"".equals(FCode1)) {
                    FCode = FCode1;
                } else if (null != FCode2 && !"".equals(FCode2)) {
                    FCode = FCode2;
                } else if (null != FCode3 && !"".equals(FCode3)) {
                    FCode = FCode3;
                }
                var.put("FCode", FCode);

                //存入能源类型
                if ("T_PatraElectric".equals(plc.getString("SaveTable"))) {
                    var.put("EnergyType", "电");
                } else if ("T_PatraWater".equals(plc.getString("SaveTable"))) {
                    var.put("EnergyType", "水");
                } else if ("T_PatraGas".equals(plc.getString("SaveTable"))) {
                    var.put("EnergyType", "气");
                }
                var.put("FUnit", plc.getString("Extend1"));

                resultList.add(var);
            }

        }

        return resultList;
    }

    //处理查询结果
    public static List<PageData> getDayValueList(List<PageData> plcList, List<PageData> list) {

        List<PageData> resList = new ArrayList<>();

        for (PageData plc : plcList) {
            for (int i = 0; i < list.size(); i++) {

                PageData pd = list.get(i);
                String field = plc.getString("SaveField");
                if (null != pd.get(field).toString() && !"".equals(pd.get(field).toString())) {

                    DecimalFormat format = new DecimalFormat("0.00");
                    Double Value = Double.valueOf(pd.get(field).toString());
                    PageData var = new PageData();
                    var.put("FValue", Value);
                    var.put("FTime", pd.getString("TimeStamp") + ":00:00");
                    //存入回路名
                    String FName = "";
                    String DeptName1 = plc.getString("DeptName1");
                    String DeptName2 = plc.getString("DeptName2");
                    String DeptName3 = plc.getString("DeptName3");
                    if (null != DeptName1 && !"".equals(DeptName1)) {
                        FName = DeptName1;
                    } else if (null != DeptName2 && !"".equals(DeptName2)) {
                        FName = DeptName2;
                    } else if (null != DeptName3 && !"".equals(DeptName3)) {
                        FName = DeptName3;
                    }
                    var.put("FName", FName);

                    //存入回路编号
                    String FCode = "";
                    String FCode1 = plc.getString("FCode1");
                    String FCode2 = plc.getString("FCode2");
                    String FCode3 = plc.getString("FCode3");
                    if (null != FCode1 && !"".equals(FCode1)) {
                        FCode = FCode1;
                    } else if (null != FCode2 && !"".equals(FCode2)) {
                        FCode = FCode2;
                    } else if (null != FCode3 && !"".equals(FCode3)) {
                        FCode = FCode3;
                    }
                    var.put("FCode", FCode);

                    //存入能源类型
                    if ("T_PatraElectric".equals(plc.getString("SaveTable"))) {
                        var.put("EnergyType", "电");
                    } else if ("T_PatraWater".equals(plc.getString("SaveTable"))) {
                        var.put("EnergyType", "水");
                    } else if ("T_PatraGas".equals(plc.getString("SaveTable"))) {
                        var.put("EnergyType", "气");
                    }
                    var.put("FUnit", plc.getString("Extend1"));

                    resList.add(var);
                }
            }
        }

        return resList;

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
