package org.yy.opc.utils;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.yy.entity.PageData;
import org.yy.opc.OPCService;
import org.yy.util.Tools;

import java.util.*;

public class OPCUtil {

    //读取PLC数据
    public static String getPlcData(PageData plc) {

        Server server = OPCService.openServer();

        String value = "";

        //kepServer通道名
        String line = plc.getString("Passageway");
        //kepServer设备名
        String ipAddress = plc.getString("Address");
        //kepServer 标记名
        String region = plc.getString("SymbolName");
        //itemId
        String itemId = line + "." + ipAddress + "." + region;

        String address = "@LOCALMACHINE::.SIMATIC S7 Protocol Suite.TCP/IP.NewConnection_1.";
        //判断plc连接方式，如果为直连则直接拼接点位，如果为映射则拼入指定字符串
        String ConnectType = plc.getString("ConnectType");
        if ("映射".equals(ConnectType)) {
            itemId = line + "." + ipAddress + "." + address + region;
        } else if ("直连".equals(ConnectType)) {
            itemId = line + "." + ipAddress + "." + region;
        }

        try {
            Group group = server.addGroup();
            //添加itemId
            Item item = group.addItem(itemId);
            //读取点位数据
            ItemState itemState = item.read(true);
            value = JiVariantUtil.parseVariant(itemState.getValue()) + "";

            server.removeGroup(group, true);

        } catch (JIException | NotConnectedException e) {
            System.err.println("连接丢失，尝试重连，时间为:"+Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
        } catch (AddFailedException e) {
            System.err.println("配置中存在kepserver未知的标记");
            value = "无";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    //批量读取
    public static List<PageData> getAllData(List<PageData> plcList) throws Exception {

        Server server = OPCService.openServer();

        //读取标记列表大小
        int size = plcList.size();

        //数组
        Item[] values = new Item[size];

        try {
            Group group = server.addGroup();

            List<String> itemIds = new ArrayList<>();
            for (int i = 0; i < size; i++) {

                PageData plc = plcList.get(i);

                //itemId
                String itemId = getItem(plc);
                itemIds.add(itemId);
                //将标记加入数组
                values[i] = group.addItem(itemId);

            }

            //批量读取模式
            Map<Item, ItemState> itemValueStatus = group.read(true, values);

            //将结果存入plc数据中
            for (int i = 0; i < plcList.size(); i++) {
                PageData plc = plcList.get(i);
                //获取模式数值
                ItemState valueState = itemValueStatus.get(values[i]);
                String valueValue = JiVariantUtil.parseVariant(valueState.getValue()) + "";
                plc.put("value", valueValue);
            }

            server.removeGroup(group, true);
        } catch (JIException | NotConnectedException e) {
            System.err.println("连接丢失，尝试重连，时间为:"+Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
            return new ArrayList<>();
        } catch (AddFailedException e) {
            System.err.println("配置中存在kepserver未知的标记");
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return plcList;
    }

    //读取能源参数
    public static List<PageData> getAllNyPlcData(List<PageData> plcList) {

        Server server = OPCService.openServer();

        List<PageData> returnList = new ArrayList<>();

        List<PageData> noFormulaList = new ArrayList<>();
        List<PageData> formulaList = new ArrayList<>();
        for (PageData plc : plcList) {
            String isFormula = plc.getString("isFormula");
            if ("是".equals(isFormula)) {
                formulaList.add(plc);
            } else {
                noFormulaList.add(plc);
            }
        }

        //读取标记列表大小
        int noFormulaSize = noFormulaList.size();
        int formulaSize = formulaList.size();

        //item组
        Item[] noFormulaItems = new Item[noFormulaSize];

        try {
            Group group1 = server.addGroup();

            for (int i = 0; i < noFormulaSize; i++) {

                PageData plc = noFormulaList.get(i);

                //itemId
                String itemId = getItem(plc);

                //将标记加入数组
                noFormulaItems[i] = group1.addItem(itemId);

            }
            //批量读取模式
            Map<Item, ItemState> itemStateNoFormula = group1.read(true, noFormulaItems);

            //将结果存入plc数据中
            for (int i = 0; i < noFormulaSize; i++) {
                PageData plc = noFormulaList.get(i);
                //获取模式数值
                ItemState noFormulaState = itemStateNoFormula.get(noFormulaItems[i]);
                String noFormulaValue = JiVariantUtil.parseVariant(noFormulaState.getValue()) + "";
                plc.put("value", noFormulaValue);
            }

            returnList = noFormulaList;

            //公式参数值获取
            Group group2 = server.addGroup();
            for (int i = 0; i < formulaSize; i++) {

                PageData plc = formulaList.get(i);

                if ("有功电能".equals(plc.getString("ParamType")) && "T_PatraElectric".equals(plc.getString("SaveTable"))) {
                    //itemId
                    PageData itemId = getFormulaItem(plc);
                    String temp_1 = itemId.getString("temp_1");
                    String temp_2 = itemId.getString("temp_2");
                    String pt = itemId.getString("pt");
                    String ct = itemId.getString("ct");

                    //添加itemId
                    Item tempIitem_1 = group2.addItem(temp_1);
                    Item tempIitem_2 = group2.addItem(temp_2);
                    Item ptItem = group2.addItem(pt);
                    Item ctItem = group2.addItem(ct);
                    //读取点位数据
                    ItemState tempItemState_1 = tempIitem_1.read(true);
                    String tempValue1 = JiVariantUtil.parseVariant(tempItemState_1.getValue()) + "";
                    ItemState tempItemState_2 = tempIitem_2.read(true);
                    String tempValue2 = JiVariantUtil.parseVariant(tempItemState_2.getValue()) + "";
                    ItemState ptItemState = ptItem.read(true);
                    String ptValue = JiVariantUtil.parseVariant(ptItemState.getValue()) + "";
                    ItemState ctItemState = ctItem.read(true);
                    String ctValue = JiVariantUtil.parseVariant(ctItemState.getValue()) + "";

//                    System.out.println("temp_1:"+tempValue1);
//                    System.out.println("temp_2:"+tempValue2);
//                    System.out.println("pt:"+ptValue);
//                    System.out.println("ct:"+ctValue);

                    //计算有功电能
                    double temp1 = Double.parseDouble(tempValue1);
                    double temp2 = Double.parseDouble(tempValue2);
                    double ptVal = Double.parseDouble(ptValue);
                    double ctVal = Double.parseDouble(ctValue);

                    String useElectric = String.format("%.2f", (temp1 * 65536 + temp2) / 1000 * ptVal * ctVal);

//                    System.out.println("value:"+useElectric);
                    plc.put("value", useElectric);

                } else if ("互感电流".equals(plc.getString("ParamType")) && "T_PatraElectric".equals(plc.getString("SaveTable"))) {
                    String itemId = getItem(plc);
                    Item item = group2.addItem(itemId);

                    //读取点位数据计算互感电流
                    ItemState itemState = item.read(true);
                    String value = JiVariantUtil.parseVariant(itemState.getValue()) + "";
                    double R = Double.parseDouble(value);
                    String IValue = String.format("%.2f", (R - 819) / (4095 - 819) * (20 - 4) + 4);
                    plc.put("value", IValue);
                }
            }

            returnList.addAll(formulaList);

            server.removeGroup(group2, true);
            server.removeGroup(group2, true);

        } catch (JIException | NotConnectedException e) {
            System.err.println("连接丢失，尝试重连，时间为:"+Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
            return new ArrayList<>();
        } catch (AddFailedException e) {
            System.err.println("配置中存在kepserver未知的标记");
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return plcList;
    }

    public static String getItem(PageData plc) {
        //映射标记拼接字段
        String address = "@LOCALMACHINE::.SIMATIC S7 Protocol Suite.TCP/IP.NewConnection_1.";
        //通道
        String Passageway = plc.getString("Passageway");
        //设备
        String Address = plc.getString("Address");
        //标记
        String SymbolName = plc.getString("SymbolName");
        StringBuilder Item = new StringBuilder(Passageway);
        //plc点位
        //判断plc连接方式，如果为直连则直接拼接点位，如果为映射则拼入指定字符串
        String ConnectType = plc.getString("ConnectType");
        if ("映射".equals(ConnectType)) {
            Item = Item.append(".").append(Address).append(".").append(address).append(SymbolName);
        } else if ("直连".equals(ConnectType)) {
            Item = Item.append(".").append(Address).append(".").append(SymbolName);
        }
        return Item.toString();
    }

    public static PageData getFormulaItem(PageData plc) {

        List<String> itemList = new ArrayList<>();

        //映射标记拼接字段
        String address = "@LOCALMACHINE::.SIMATIC S7 Protocol Suite.TCP/IP.NewConnection_1.";
        //通道
        String Passageway = plc.getString("Passageway");
        //设备
        String Address = plc.getString("Address");

        //点位
        String temp_1 = plc.getString("temp_1");
        String temp_2 = plc.getString("temp_2");
        String pt = plc.getString("pt");
        String ct = plc.getString("ct");

        StringBuilder Item = new StringBuilder(Passageway);

        //判断plc连接方式，如果为直连则直接拼接点位，如果为映射则拼入指定字符串
        PageData item = new PageData();
        String ConnectType = plc.getString("ConnectType");
        if ("映射".equals(ConnectType)) {
            item.put("temp_1", Item.append(".").append(Address).append(".").append(address).append(temp_1).toString());
            Item = new StringBuilder(Passageway);
            item.put("temp_2", Item.append(".").append(Address).append(".").append(address).append(temp_2).toString());
            Item = new StringBuilder(Passageway);
            item.put("pt", Item.append(".").append(Address).append(".").append(address).append(pt).toString());
            Item = new StringBuilder(Passageway);
            item.put("ct", Item.append(".").append(Address).append(".").append(address).append(ct).toString());
        } else if ("直连".equals(ConnectType)) {
            item.put("temp_1", Item.append(".").append(Address).append(".").append(temp_1).toString());
            Item = new StringBuilder(Passageway);
            item.put("temp_2", Item.append(".").append(Address).append(".").append(temp_2).toString());
            Item = new StringBuilder(Passageway);
            item.put("pt", Item.append(".").append(Address).append(".").append(pt).toString());
            Item = new StringBuilder(Passageway);
            item.put("ct", Item.append(".").append(Address).append(".").append(ct).toString());
        }
        return item;
    }

    //判断参数是否正确
    public static boolean check(PageData plc) {

        Server server = OPCService.openServer();

        boolean res = true;
        String value = "";

        //kepServer通道名
        String line = plc.getString("Passageway");
        //kepServer设备名
        String ipAddress = plc.getString("Address");
        //kepServer 标记名
        String region = plc.getString("SymbolName");
        //itemId
        String itemId = line + "." + ipAddress + "." + region;

        //判断plc连接方式，如果为直连则直接拼接点位，如果为映射则拼入指定字符串
        String ConnectType = plc.getString("ConnectType");
        if ("映射".equals(ConnectType)) {
            itemId = line + "." + ipAddress + "." + region;
        } else if ("直连".equals(ConnectType)) {
            itemId = line + "." + ipAddress + "." + region;
        }

        try {
            Group group = server.addGroup();
            //点位

            //添加itemId
            Item item = group.addItem(itemId);
            //读取点位数据
            ItemState itemState = item.read(true);
            value = JiVariantUtil.parseVariant(itemState.getValue()) + "";

        } catch (JIException | NotConnectedException e) {
            System.err.println("连接丢失，尝试重连，时间为:"+Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
        } catch (AddFailedException e) {
            System.err.println("配置中存在kepserver未知的标记");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

}
