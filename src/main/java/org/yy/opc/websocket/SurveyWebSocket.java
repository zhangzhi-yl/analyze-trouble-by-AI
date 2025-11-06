package org.yy.opc.websocket;

import org.openscada.opc.lib.da.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yy.entity.PageData;
import org.yy.opc.OPCService;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.ny.RecordService;
import org.yy.util.JSONUtil;
import org.yy.util.TimeUtil;
import org.yy.util.Tools;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket
 * 用电概况  --- 图表
 * v1 sunlz 20211014
 */

@ServerEndpoint(value = "/energy/survey", encoders = {ServerEncoder.class})
@Component
public class SurveyWebSocket {

    public static RecordService recordService;

    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    private static final Logger log = LoggerFactory.getLogger(SurveyWebSocket.class);

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineCount.incrementAndGet(); // 在线数加1
        log.info("有新连接加入：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        log.info("有一连接关闭：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        //System.out.println(message);
        PageData pd = JSONUtil.JSONToPageData(message);
        //System.out.println("客户端发送的消息为: " + pd.getString("FType"));

        String FType = pd.getString("FType");

        PageData result = new PageData();
        List<PageData> resList = new ArrayList<>();

        //获取数据字段
        pd.put("SaveTable",FType);
        pd.put("ParamType","总能耗");
        List<PageData> plcList = new ArrayList<>();
        plcList = recordService.getTotalField(pd);

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
                    res.put("FValue",FValue);
                    res.put("TimeStamp",res.getString("TimeStamp")+":00:00");
                }
            }
        }


        //获取plc读数和
        double value = 0;
        plcList = OPCUtil.getAllNyPlcData(plcList);
        for(PageData plc : plcList){
            value = value + Double.parseDouble(plc.get("value").toString());
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

        //获取昨日/上月/去年最大数据
        if(plcList.size() > 0){
            List<String> fieldList = new ArrayList<>();

            for (PageData plc : plcList) {
                String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) " + plc.getString("SaveField") + " ";
                fieldList.add(field);
            }
            //查询pd
            PageData getValue = new PageData();
            String field = String.join(",", fieldList.toArray(new String[fieldList.size()]));
            getValue.put("SaveField", field);
            getValue.put("SaveTable", FType);

            //获取昨日最大值
            List<PageData> beforeOneList = recordService.getBeforeOneMaxPower(getValue);

            if(beforeOneList.size() == 1){
                PageData beforeOne = beforeOneList.get(0);

                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeOneValue = beforeOneValue + (Double.parseDouble(beforeOne.get(Field).toString()));
                }
            }

            //获取上月最大值
            List<PageData> beforeOneMonthList = recordService.getBeforeOneMonthMaxPower(getValue);

            if(beforeOneMonthList.size() == 1){
                PageData beforeOneMonth = beforeOneMonthList.get(0);

                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeOneMonthValue = beforeOneMonthValue + (Double.parseDouble(beforeOneMonth.get(Field).toString()));
                }
            }

            //获取去年最大值
            List<PageData> beforeOneYearList = recordService.getBeforeOneYearMaxPower(getValue);

            if(beforeOneYearList.size() == 1){
                PageData beforeOneYear = beforeOneYearList.get(0);

                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeOneYearValue = beforeOneYearValue + (Double.parseDouble(beforeOneYear.get(Field).toString()));
                }
            }

            String FTime = Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss");
            getValue.put("FTime",FTime);

            //获取昨日同期用能
            List<PageData> beforeDayUseList = recordService.getBeforeDayUsePower(getValue);

            if(beforeDayUseList.size() == 1){
                PageData beforeDayUse = beforeDayUseList.get(0);
                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeDayUseValue = beforeDayUseValue + (Double.parseDouble(beforeDayUse.get(Field).toString()));
                }
            }
            //获取前日最大值
            List<PageData> beforeTwoDayList = recordService.getBeforeTwoMaxPower(getValue);

            if(beforeTwoDayList.size() == 1){
                PageData beforeTwoDay = beforeTwoDayList.get(0);
                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeTwoDayValue = beforeTwoDayValue + (Double.parseDouble(beforeTwoDay.get(Field).toString()));
                }
            }

            //获取上月同期用能
            List<PageData> beforeMonthUseList = recordService.getBeforeMonthUsePower(getValue);

            if(beforeMonthUseList.size() == 1){
                PageData beforeMonthUse = beforeMonthUseList.get(0);
                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeMonthUseValue = beforeMonthUseValue + (Double.parseDouble(beforeMonthUse.get(Field).toString()));
                }
            }
            //获取上上月最大值
            List<PageData> beforeTwoMonthList = recordService.getBeforeTwoMonthMaxPower(getValue);

            if(beforeTwoMonthList.size() == 1){
                PageData beforeTwoMonth = beforeTwoMonthList.get(0);
                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeTwoMonthValue = beforeTwoMonthValue + (Double.parseDouble(beforeTwoMonth.get(Field).toString()));
                }
            }

            //获取去年同期用能
            List<PageData> beforeYearUseList = recordService.getBeforeMonthUsePower(getValue);

            if(beforeYearUseList.size() == 1){
                PageData beforeYearUse = beforeYearUseList.get(0);
                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeYearUseValue = beforeYearUseValue + (Double.parseDouble(beforeYearUse.get(Field).toString()));
                }
            }
            //获取前年最大值
            List<PageData> beforeTwoYearList = recordService.getBeforeTwoYearMaxPower(getValue);

            if(beforeTwoYearList.size() == 1){
                PageData beforeTwoYear = beforeTwoYearList.get(0);
                for (PageData plc : plcList){
                    String Field = plc.getString("SaveField");
                    beforeTwoYearValue = beforeTwoYearValue + (Double.parseDouble(beforeTwoYear.get(Field).toString()));
                }
            }
        }


        double resDay = 0.00;
        resDay = value-beforeOneValue;
        double resMonth = 0.00;
        resMonth = value-beforeOneMonthValue;
        double resYear = 0.00;
        resYear = value-beforeOneYearValue;

        double resDayUse = 0.00;
        resDayUse = beforeDayUseValue-beforeTwoDayValue;
        double resMonthUse = 0.00;
        resMonthUse = beforeMonthUseValue-beforeTwoMonthValue;
        double resYearUse = 0.00;
        resYearUse = beforeYearUseValue-beforeTwoYearValue;

        PageData data = new PageData();

        data.put("resDay",String.format("%.2f",resDay));
        data.put("resMonth",String.format("%.2f",resMonth));
        data.put("resYear",String.format("%.2f",resYear));

        data.put("resDayUse",String.format("%.2f",resDayUse));
        data.put("resMonthUse",String.format("%.2f",resMonthUse));
        data.put("resYearUse",String.format("%.2f",resYearUse));

        if(resDayUse == 0){
            data.put("dayUsePercent",100);
        }else {
            data.put("dayUsePercent",String.format("%.2f",(resDay-resDayUse)/resDayUse));
        }
        if(resMonthUse == 0){
            data.put("monthUsePercent",100);
        }else {
            data.put("monthUsePercent",String.format("%.2f",(resMonth-resMonthUse)/resMonthUse));
        }
        if(resYearUse == 0){
            data.put("yearUsePercent",100);
        }else {
            data.put("yearUsePercent",String.format("%.2f",(resYear-resYearUse)/resYearUse));
        }




        data.put("dayUseDiff",String.format("%.2f",resDay-resDayUse));
        data.put("monthUseDiff",String.format("%.2f",resMonth-resMonthUse));
        data.put("yearUseDiff",String.format("%.2f",resYear-resYearUse));



        //保存要发送的数据
        result.put("recordList", getList(resList));
        result.put("dataPd", data);

        //发送至客户端
        this.sendMessage(result, session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
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
            value.add(Double.parseDouble(String.format("%.2f",Double.parseDouble(pd.get("FValue").toString()))));
            res.put("value", value);
            resList.add(res);
        }

        return resList;
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(PageData pdReturn, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), pdReturn);
            toSession.getBasicRemote().sendObject(new ClockMessage(pdReturn));
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：{}", e);
        }
    }
}
