package org.yy.opc.websocket;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.*;
import org.yy.opc.utils.JiVariantUtil;

import java.util.concurrent.Executors;

public class main {

    public static void main(String[] args) throws Exception {
        // 连接信息
        final ConnectionInformation ci = new ConnectionInformation();
        ci.setHost("172.16.30.11"); // 安装opc电脑IP
        ci.setDomain(""); // 域，为空就行
        ci.setUser("OPCServer"); // 电脑上自己建好的用户名
        ci.setPassword("yl_0323"); // 用户名的密码
        ci.setClsid("7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729"); // KEPServer的注册表ID，可以在“组件服务”里看到
        // ci.setProgId("");
        // 要读取的标记
        //String itemId = "通道 8.设备 1.@LOCALMACHINE::.SIMATIC S7 Protocol Suite.TCP/IP.NewConnection_1.socket_out_7";
        String itemId = "通道 1.设备 1.标记 1";
        // final String itemId = "通道 1.设备 1.标记 1";

        // 启动服务
        final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());

        try {
            // 连接到服务
            server.connect();
            // add sync access, poll every 500 ms，启动一个同步的access用来读取地址上的值，线程池每500ms读值一次
            // 这个是用来循环读值的，只读一次值不用这样
            final AccessBase access = new SyncAccess(server, 500);
            // 这是个回调函数，就是读到值后执行这个打印，是用匿名类写的，当然也可以写到外面去

            System.err.println(access);

            access.addItem(itemId, new DataCallback() {

                @Override
                public void changed(Item item, ItemState itemState) {

                    String value = "";
                    try {
                        value = JiVariantUtil.parseVariant(itemState.getValue())+""; // 类型实际是数字，用常量定义的
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("监控项的数据值是：-----" + value);
                    System.out.println("监控项的时间戳是：-----" + itemState.getTimestamp().getTime());
                    System.out.println("监控项的详细信息是：-----" + itemState);
                }
            });
            // start reading，开始读值
            access.bind();
            // wait a little bit，有个10秒延时
            Thread.sleep(10 * 1000);
            // stop reading，停止读取
            access.unbind();
        } catch (final JIException e) {
            System.out.println(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
        }
    }
}