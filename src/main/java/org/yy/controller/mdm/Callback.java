package org.yy.controller.mdm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yy.service.mdm.EQM_BASEService;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 常规MQTT回调函数
 *
 * @author cuiyu
 * @since 2021/06/05
 */
@Component
@Slf4j
public class Callback implements MqttCallback {

    @Autowired
    EQM_BASEService variableBiz;

    private static EQM_BASEService employeeService;
    /**
     * @Date: 2021/6/4
     * 静态工具类注入
     * @return: void
     */
    @PostConstruct
    public void init() {
        employeeService = variableBiz;
    }

    /**
     * MQTT 断开连接会执行此方法
     */
    @Override
    public void connectionLost(Throwable throwable) {
        //log.info("断开了MQTT连接 ：{}", throwable.getMessage());
        //log.error(throwable.getMessage(), throwable);
    }

    /**
     * publish发布成功后会执行到这里
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //log.info("发布消息成功");
    }

    /**
     * subscribe订阅后得到的消息会执行到这里
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        //获取当前时间并格式化
        SimpleDateFormat DateAll=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat FDate=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat FTime=new SimpleDateFormat("HH:mm");
        Date date=new Date();
        String dataAll = DateAll.format(date);
        String dateStr = FDate.format(date);
        String timeStr = FTime.format(date);
        //  TODO    此处可以将订阅得到的消息进行业务处理、数据存储
        //log.info("收到来自 " + topic + " 的消息：{}", new String(message.getPayload()));
        String messageMqtt = new String(message.getPayload());
        JSONObject addjsonResult = JSONObject.parseObject(messageMqtt, Feature.OrderedField);
    }

}

