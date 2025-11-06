package org.yy.controller.ny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.service.ny.*;
import org.yy.service.zm.*;
import org.yy.service.zm.EquipmentsService;
import org.yy.util.TimeUtil;
import org.yy.util.Tools;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 说明：首页大屏
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-26
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/screen")
public class BigScreenController extends BaseController {

    @Autowired
    private CollectionTwoService collectionTwoService;

    @Autowired
    private NYLoopService loopService;

    @Autowired
    private ALARMService alarmService;

    @Autowired
    private SnapshotService snapshotService;

    /**
     * 年耗能趋势---快照
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/yearUseEnergy")
    @ResponseBody
    public Object yearUseElectricEnergy() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        List<String> timeList = new ArrayList<>();
        List<Double> valList = new ArrayList<>();

        String SaveTable = pd.getString("SaveTable");

        if (null != SaveTable && !"".equals(SaveTable)) {

            try {

                if("T_PatraElectric".equals(SaveTable)){
                    pd.put("EnergyType", "有功电能");
                }else {
                    pd.put("EnergyType", "流量");
                }

                List<PageData> varList = snapshotService.queryYearUseEnergy(pd);
                for (PageData var : varList){
                    timeList.add(var.get("month").toString());
                    valList.add(Double.parseDouble(var.get("value").toString()));
                }

            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        map.put("timeList", timeList);
        map.put("valList", valList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 耗能数据
     *
     * @return
     */
    @RequestMapping(value = "/useElectricEnergyData")
    @ResponseBody
    public Object useElectricEnergyData() {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        String dayValue = "0.00";
        String beforeDayValue = "0.00";

        if (null != pd.getString("SaveTable") && !"".equals(pd.getString("SaveTable"))) {

            try {

                PageData getPlc = new PageData();
                getPlc.put("isTotalUseEnergy", "isTotalUseEnergy");
                if("T_PatraElectric".equals(pd.getString("SaveTable"))){
                    getPlc.put("AccParamType", "有功电能");
                }else {
                    getPlc.put("AccParamType", "流量");
                }
                getPlc.put("SaveTable", pd.getString("SaveTable"));
                List<PageData> plcList = collectionTwoService.plcList(getPlc);    //列出plc列表

                //字段名list
                List<String> fieldList = new ArrayList<>();

                for (PageData plc : plcList) {
                    String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) ";
                    fieldList.add(field);
                }

                //查询pd
                PageData getValue = new PageData();
                String field = String.join("+", fieldList.toArray(new String[fieldList.size()])) + " AS value";
                getValue.put("QueryField", field);

                //获取值列表
                if (fieldList.size() > 0) {

                    getValue.put("SaveTable", pd.getString("SaveTable"));

                    //日数据
                    getValue.put("day", Tools.date2Str(new Date(), "yyyy-MM-dd"));
                    List<PageData> dayResultList = new ArrayList<>();
                    dayResultList = collectionTwoService.useEnergyData(getValue);
                    if (dayResultList.size() > 0) {
                        dayValue = dayResultList.get(0).get("value").toString();
                    }

                    //昨日同期
                    getValue.remove("day");
                    getValue.put("beforeDay", TimeUtil.DateResuceOne(Tools.date2Str(new Date(), "yyyy-MM-dd")) + " " + Tools.date2Str(new Date(), "HH:mm:ss"));
                    List<PageData> beforeDayResultList = new ArrayList<>();
                    beforeDayResultList = collectionTwoService.useEnergyData(getValue);
                    if (beforeDayResultList.size() > 0) {
                        beforeDayValue = beforeDayResultList.get(0).get("value").toString();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                errInfo = "程序错误!请联系管理员";
            }
        } else {
            errInfo = "请选择能源类型";
        }

        map.put("dayValue", dayValue);
        map.put("beforeDayValue", beforeDayValue);
        map.put("diffDayValue", String.format("%.2f", Double.parseDouble(dayValue) - Double.parseDouble(beforeDayValue)));
        map.put("result", errInfo);
        return map;
    }

    /**
     * 主页报警
     *
     * @return
     */
    @RequestMapping(value = "/consume")
    @ResponseBody
    public Object consume() {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";

        List<PageData> valueList = new ArrayList<>();
        List<Integer> valList = new ArrayList<>();
        List<String> monthList = new ArrayList<>();

        try {
            valueList = alarmService.getAlarmMonth();

            for (PageData var : valueList){
                monthList.add(var.getString("MONTH"));
                valList.add(Integer.parseInt(var.get("ALARMCOUNT").toString()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            errInfo = "程序错误!请联系管理员";
        }

        map.put("valueList", valList);
        map.put("monthList", monthList);
        map.put("result", errInfo);
        return map;
    }


    /**
     * 首页中间数据
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/loopCount")
    @ResponseBody
    public Object loopCount() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();

        List<PageData> varList = loopService.loopCount(pd);
        if (varList.size() > 0) {
            PageData count = varList.get(0);

            pd.put("ZMCOUNT", count.get("ZMCOUNT").toString());
            pd.put("KTCOUNT", count.get("KTCOUNT").toString());
            pd.put("CZCOUNT", count.get("CZCOUNT").toString());
            pd.put("ZMLOOPCOUNT", count.get("ZMLOOPCOUNT").toString());
            pd.put("KTLOOPCOUNT", count.get("KTLOOPCOUNT").toString());
            pd.put("CZLOOPCOUNT", count.get("CZLOOPCOUNT").toString());
        }

        map.put("pd", pd);
        map.put("result", errInfo);                //返回结果
        return map;
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


}
