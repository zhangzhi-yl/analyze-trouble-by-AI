//package org.yy;
//
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//import org.yy.opc.OPCConnect;
//
//import java.awt.*;
//
//
///**
// * 项目启动 监听PLC
// *
// * @author cuiyu
// * @since 2021/06/05
// */
//
//@Component
//public class OPCListener implements ApplicationListener<ContextRefreshedEvent> {
//
//    private static final Logger log = LoggerFactory.getLogger(OPCListener.class);
//
//    private final OPCConnect server;
//
//    @Autowired
//    public OPCListener(OPCConnect server) {
//        this.server = server;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        try {
//            //server.getPlcDataToRedis();
//            System.err.println("OPC监听启动成功");
//        } catch (Exception e) {
//            log.error(e+"");
//        }
//    }
//}
//
