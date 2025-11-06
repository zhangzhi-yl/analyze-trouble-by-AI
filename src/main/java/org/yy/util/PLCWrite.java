package org.yy.util;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.yy.entity.PageData;
import org.yy.opc.OPCService;
import org.yy.opc.utils.JiVariantUtil;

import java.util.Date;
import java.util.List;

public class PLCWrite {

    /**
     * 写入单个plc
     *
     * @param plc Passageway通道名,Address设备名,SymbolName符号名
     * @param val
     * @return
     */
    public static boolean write(PageData plc, String val) {

        Server server = OPCService.openServer();
        boolean res = false;

        //kepServer通道名
        String line = plc.getString("Passageway");
        //kepServer设备名
//        String ipAddress = plc.getString("Address").replace(".", "_");
        String ipAddress = plc.getString("Address");
        //kepServer 标记名
        String region = plc.getString("SymbolName");

        //itemId
        String itemId = line + "." + ipAddress + "." + region;

        //判断plc连接方式，如果为直连则直接拼接点位，如果为映射则拼入指定字符串
        String ConnectType = plc.getString("ConnectType");
        if("映射".equals(ConnectType)){
            itemId = line + "." + ipAddress + "." + region;
        }else if("直连".equals(ConnectType)){
            itemId = line + "." + ipAddress + "." + region;
        }

        try {
            //添加组
            Group group = server.addGroup();

            //添加itemId
            Item item = group.addItem(itemId);

            //写入plc
            JIVariant value = new JIVariant(val);
            item.write(value);
            System.out.println("itemId为" + itemId + "的变量写入数值为：" + value);
            res = true;

        } catch (JIException | NotConnectedException e) {
            System.err.println("连接丢失，尝试重连，时间为:"+Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
        } catch (AddFailedException e) {
            System.err.println("配置中存在kepserver未知的标记");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 批量写入单一值
     *
     * @param varlist Passageway通道名,PLC_ADDRESS设备名,FNAME区域前缀
     * @param val     写入值
     * @return
     */
    public static boolean writeAll(List<PageData> varlist, String val) {

        boolean res = true;

        //读取plc数据  若全部读到则写入，否则返回false
        for (PageData plc : varlist) {

            if (plc.size() != 0) {
                Server server = OPCService.openServer();
                //kepServer通道名
                String line = plc.getString("Passageway");
//                String line = "YLCS";
                //kepServer设备名
//                String ipAddress = plc.getString("Address").replace(".", "_");
                String ipAddress = plc.getString("Address");
                //kepServer 标记名
                String region = plc.getString("SymbolName");
                //itemId
                String itemId = line + "." + ipAddress + "." + region;

                //判断plc连接方式，如果为直连则直接拼接点位，如果为映射则拼入指定字符串
                String ConnectType = plc.getString("ConnectType");
                if("映射".equals(ConnectType)){
                    itemId = line + "." + ipAddress + "." + region;
                }else if("直连".equals(ConnectType)){
                    itemId = line + "." + ipAddress + "." + region;
                }

                //读取plc数据  若全部读到则写入，否则返回false
                try {
                    //添加组
                    Group group = server.addGroup();

                    //添加itemId
                    Item item = group.addItem(itemId);

                    //读取点位数据
                    ItemState itemState = item.read(true);
                    String value = JiVariantUtil.parseVariant(itemState.getValue()) + "";

                } catch (JIException | NotConnectedException e) {
                    res = false;
                    System.err.println("连接丢失，尝试重连，时间为:"+Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
                } catch (AddFailedException e) {
                    res = false;
                    System.err.println("配置中存在kepserver未知的标记");
                } catch (Exception e) {
                    res = false;
                    e.printStackTrace();
                }
            }
        }

        //循环写入
        if (res) {

            for (PageData plc : varlist) {
                if (plc.size() != 0) {
                    Server server = OPCService.openServer();
                    //kepServer通道名
                    String line = plc.getString("Passageway");
//                    String line = "YLCS";
                    //kepServer设备名
//                    String ipAddress = plc.getString("Address").replace(".", "_");
                    String ipAddress = plc.getString("Address");
                    //kepServer 标记名
                    String region = plc.getString("SymbolName");
                    //itemId
                    String itemId = line + "." + ipAddress + "." + region;

                    //判断plc连接方式，如果为直连则直接拼接点位，如果为映射则拼入指定字符串
                    String ConnectType = plc.getString("ConnectType");
                    if("映射".equals(ConnectType)){
                        itemId = line + "." + ipAddress + "." + region;
                    }else if("直连".equals(ConnectType)){
                        itemId = line + "." + ipAddress + "." + region;
                    }

                    try {
                        //添加组
                        Group group = server.addGroup();

                        //添加itemId
                        Item item = group.addItem(itemId);

                        //写入plc
                        JIVariant value = new JIVariant(val);
                        item.write(value);
                        System.out.println("itemId为" + itemId + "的变量写入数值为：" + value);

                    } catch (JIException | NotConnectedException e) {
                        res = false;
                        System.err.println("连接丢失，尝试重连，时间为:"+Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
                    } catch (AddFailedException e) {
                        res = false;
                        System.err.println("配置中存在kepserver未知的标记");
                    } catch (Exception e) {
                        res = false;
                        e.printStackTrace();
                    }
                }
            }
        }

        return res;
    }

    /**
     * 批量写入
     *
     * @param varlist 每个pd必须包含val(要写入的值),Passageway通道名,PLC_ADDRESS设备名,FNAME区域前缀
     * @return
     */
    public static boolean writeList(List<PageData> varlist) {

        Server server = OPCService.openServer();
        boolean res = true;

        //循环写入
        for (PageData pageData : varlist) {

//            String line = pageData.getString("LINENAME");//kepserver通道名
//            String ipAddress = pageData.getString("PLC_ADDRESS").replace(".", "_");//kepserver设备名
//            String region = pageData.getString("FNAME");//kepserver区域前缀
//            String Fiexd = line + "." + ipAddress + "." + region;

            try {
                String val = pageData.getString("val");
                //添加组
                Group group = server.addGroup();
                //测试用itemId
                String itemId = "通道 1.设备 1.标记 1";
//            String itemId = Fiexd;
                //添加itemId
                Item item = group.addItem(itemId);

                //写入plc
                JIVariant value = new JIVariant(val);
                item.write(value);
                System.out.println("itemId为" + itemId + "的变量写入数值为：" + value);

            } catch (JIException e) {
                res = false;
                e.printStackTrace();
            } catch (Exception e) {
                res = false;
                e.printStackTrace();
            }
        }

        return res;
    }
}
