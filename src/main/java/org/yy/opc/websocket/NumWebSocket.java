//package org.yy.opc.websocket;
//
//import org.jinterop.dcom.common.JIException;
//import org.openscada.opc.lib.da.Group;
//import org.openscada.opc.lib.da.Item;
//import org.openscada.opc.lib.da.ItemState;
//import org.openscada.opc.lib.da.Server;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.yy.entity.PageData;
//import org.yy.opc.OPCService;
//import org.yy.opc.utils.JiVariantUtil;
//
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * 前后端交互的类实现消息的接收推送(自己发送给自己)
// *
// * @ServerEndpoint(value = "/test/two") 前端通过此URI和后端交互，建立连接
// */
//
//@ServerEndpoint(value = "/test/num", encoders = {ServerEncoder.class})
//@Component
//public class NumWebSocket {
//
//    /**
//     * 记录当前在线连接数
//     */
//    private static AtomicInteger onlineCount = new AtomicInteger(0);
//
//    private static final Logger log = LoggerFactory.getLogger(NumWebSocket.class);
//
//    /**
//     * 连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session) {
//        onlineCount.incrementAndGet(); // 在线数加1
//        log.info("有新连接加入：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose(Session session) {
//        onlineCount.decrementAndGet(); // 在线数减1
//        log.info("有一连接关闭：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) throws Exception {
//        PageData pd = new PageData();
//        pd.put("message", message);
//        System.out.println("客户端发送的消息为: "+message );
//        PageData result = new PageData();
//        Server server = OPCService.openServer();
//
//        try {
//            //调用opc
//            Group group = server.addGroup();
//
//            //实际从数据库读取标记
////              List<PageData> fiexdList = new ArrayList<>();
////
////              for (PageData pageData : fiexdList){
////                  String line = pageData.getString("LINENAME");//kepserver通道名
////                  String ipAddress = pageData.getString("PLC_ADDRESS").replace(".", "_");//kepserver设备名
////                  String region = pageData.getString("FNAME");//kepserver区域前缀
////                  String Fiexd = line+"."+ipAddress+"."+region;
////                  String code1 = Fiexd;    //八位码前四位
////
////                  //调用opc
////                  Item itemCode1 = group.addItem(code1);
////                  ItemState itemStateCode1 = itemCode1.read(true);
////                  String result_code1= JiVariantUtil.parseVariant(itemStateCode1.getValue())+"";
////                  log.info(result_code1);
////
////                  //此处向下写业务逻辑
////
////              }
//
//            //测试用
//            String line = "通道1";
//            String ipAddress = "设备1";
//            String region = "标记1";
//            String Fiexd = "通道 1.设备 1.标记 1";
//            String code1 = Fiexd;    //八位码前四位
//
//            //调用opc
//            Item itemCode1 = group.addItem(code1);
//            ItemState itemStateCode1 = itemCode1.read(true);
//            String result_code1 = JiVariantUtil.parseVariant(itemStateCode1.getValue()) + "";
//            log.info(result_code1);
//            result.put("message", result_code1);
//
//        } catch (JIException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        this.sendMessage(result, session);
//    }
//
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("发生错误");
//        error.printStackTrace();
//    }
//
//    /**
//     * 服务端发送消息给客户端
//     */
//    private void sendMessage(PageData pdReturn, Session toSession) {
//        try {
//            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), pdReturn);
//            toSession.getBasicRemote().sendObject(new ClockMessage(pdReturn));
//        } catch (Exception e) {
//            log.error("服务端发送消息给客户端失败：{}", e);
//        }
//    }
//}
