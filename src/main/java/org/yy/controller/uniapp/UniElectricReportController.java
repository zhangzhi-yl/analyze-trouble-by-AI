package org.yy.controller.uniapp;

import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.opc.OPCService;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.ny.NYLoopService;
import org.yy.service.ny.NYPLCService;
import org.yy.service.zm.EquipmentsService;
import org.yy.service.zm.LoopService;
import org.yy.service.zm.PLCService;
import org.yy.service.zm.ReportService;
import org.yy.util.Tools;

import java.util.*;

/**
 * uniApp设备回路列表
 */
@Controller
@RequestMapping("/api/electricReport")
public class UniElectricReportController extends BaseController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EquipmentsService equipmentService;

    @Autowired
    private NYLoopService loopService;

    @Autowired
    private NYPLCService plcService;

    @Autowired
    private ReportService reportService;

    /**
     * uniapp报表电能能耗
     *
     * @param
     * @throws Exception
     * @RequestBody PageData pd
     */
    @RequestMapping(value = "/electricDateReport")
    @ResponseBody
    public Object electricReport1(@RequestBody PageData pd) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        List<String> fieldList = new ArrayList<>();
//        PageData pd = new PageData();
//        pd = this.getPageData();
        List<PageData> returnList = new ArrayList<>();
        List<PageData> varList = new ArrayList<>();

        List<String> nameList = new ArrayList<>();
        List<Double> valueList = new ArrayList<>();

        //日期类型(月、日)
        String dateType = pd.getString("DateType");
        //班次(早、晚)
        String service = pd.getString("service");
        //日期
        String FTime = pd.getString("FTime");

        //获取总用电量参数配置
        PageData getPlcParam = new PageData();
        getPlcParam.put("ParamType", "总能耗");
        getPlcParam.put("SaveTable", "T_PatraElectric");
        getPlcParam.put("isUse", "isUse");
        List<PageData> plcList = plcService.listAll(getPlcParam);

        if (plcList.size() > 0) {
            for (PageData plc : plcList) {
                String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) ";
                fieldList.add(field);
            }

            //查询pd
            PageData getValue = new PageData();
            String field = String.join("+", fieldList.toArray(new String[fieldList.size()])) + " AS value";
            getValue.put("QueryField", field);
            getValue.put("FTime", FTime);

            if ("日".equals(dateType)) {
                if ("".equals(FTime) || null == FTime) {
                    getValue.put("FTime", Tools.date2Str(new Date(), "yyyy-MM-dd"));
                }
                String[] hour = null;
                hour = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
                getValue.put("morning", "morning");
                returnList = reportService.dayElectricReport(getValue);

                for (String h : hour) {
                    PageData var = new PageData();
                    var.put("name", h);
                    var.put("value", 0);
                    for (PageData r : returnList) {
                        if (h.equals(r.get("TimeStamp").toString())) {
                            var.put("value", Double.parseDouble(r.get("value").toString()));
                        }
                    }
                    varList.add(var);
                }

            } else if ("月".equals(dateType)) {
                if ("".equals(FTime) || null == FTime) {
                    getValue.put("FTime", Tools.date2Str(new Date(), "yyyy-MM"));
                }
                //本月天数
                int dayNum = Tools.getMonthDayNum();
                getValue.put("halfDay", dayNum);
                getValue.put("before", "before");
                returnList = reportService.monthElectricReport(getValue);
                for (int i = 1; i <= dayNum; i++) {
                    PageData var = new PageData();
                    var.put("name", i);
                    var.put("value", 0);
                    for (PageData r : returnList) {
                        if (i == Integer.parseInt(r.get("TimeStamp").toString())) {
                            var.put("value", Double.parseDouble(r.get("value").toString()));
                        }
                    }
                    varList.add(var);
                }
            }

            for (PageData var : varList) {
                nameList.add(var.get("name").toString());
                valueList.add(Double.parseDouble(var.get("value").toString()));
            }
        }

        map.put("nameList", nameList);
        map.put("valueList", valueList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * uniapp报表电能能耗
     *
     * @param
     * @throws Exception
     * @RequestBody PageData pd  @RequestBody PageData pd
     */
    @RequestMapping(value = "/electricUseReport")
    @ResponseBody
    public Object electricUseReport(@RequestBody PageData pd) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        List<String> fieldList = new ArrayList<>();
//        PageData pd = new PageData();
//        pd = this.getPageData();
        List<PageData> returnList = new ArrayList<>();
        List<PageData> varList = new ArrayList<>();

        //日期类型(月、日)
        String dateType = pd.getString("DateType");
        //班次(早、晚、上半月、下半月)
        String service = pd.getString("service");
        //日期
        String FTime = pd.getString("FTime");

        //获取总用电量参数配置
        PageData getPlcParam = new PageData();
        getPlcParam.put("ParamType", "总能耗");
        getPlcParam.put("SaveTable", "T_PatraElectric");
        getPlcParam.put("isUse", "isUse");
        List<PageData> plcList = plcService.listAll(getPlcParam);

        Double sumNum = 0.00;
        if (plcList.size() > 0) {

            for (PageData plc : plcList) {
                String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField");
                fieldList.add(field);
            }

            //查询pd
            PageData getValue = new PageData();
            String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
            getValue.put("QueryField", field);
            getValue.put("FTime", FTime);

            if ("日".equals(dateType)) {
                if ("".equals(FTime) || null == FTime) {
                    getValue.put("FTime", Tools.date2Str(new Date(), "yyyy-MM-dd"));
                }
                getValue.put("morning", "morning");
                returnList = reportService.dayElectricUseReport(getValue);

            } else if ("月".equals(dateType)) {
                if ("".equals(FTime) || null == FTime) {
                    getValue.put("FTime", Tools.date2Str(new Date(), "yyyy-MM"));
                }
                //本月天数
                int dayNum = Tools.getMonthDayNum();
                getValue.put("halfDay", dayNum);
                getValue.put("before", "before");
                returnList = reportService.monthElectricUseReport(getValue);

            }

            for (PageData plc : plcList) {
                PageData var = new PageData();
                Double value = 0.0;
                var.put("value", 0);
                var.put("legendname", plc.getString("FType"));
                var.put("name", plc.getString("FType"));
                String SaveField = plc.getString("SaveField");
                for (PageData r : returnList) {
                    value = value + Double.parseDouble(r.get(SaveField).toString());
                }
                var.put("value", value);
                varList.add(var);
                sumNum = sumNum + value;
            }
        }
        map.put("sumNum", String.format("%.2f", sumNum));
        map.put("valueList", varList);
        map.put("result", errInfo);
        return map;
    }
}
