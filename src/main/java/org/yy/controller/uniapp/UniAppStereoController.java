package org.yy.controller.uniapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.zm.PLCService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：立体库
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/api/stereo")
public class UniAppStereoController extends BaseController {

    @Autowired
    private PLCService plcService;

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/info")
    @ResponseBody
    public Object add(@RequestBody PageData pd){
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        pd.put("AccParamType", "立体库");
        List<PageData> plcList = new ArrayList<>();

        try {
            plcList = plcService.useListByLoop(pd);
            OPCUtil.getAllData(plcList);

            List<PageData> resList = new ArrayList<>();
            for (PageData plc : plcList) {
                PageData res = new PageData();
                String value = plc.get("value").toString();
                String SymbolName = plc.getString("SymbolName");
                if ("Char".equals(SymbolName)) {
                    byte[] bytes = value.getBytes();
                    value = String.valueOf(bytes[0]);
                    res.put("name", plc.getString("ParaName"));
                    res.put("value", value);
                    resList.add(res);
                } else {
                    res.put("name", plc.getString("ParaName"));
                    res.put("value", getName(SymbolName, value));
                    resList.add(res);
                }
            }
            map.put("pd", resList);
            map.put("result", errInfo);

        }catch (Exception e){
            e.printStackTrace();
            map.put("result", "数据获取失败");
        }
        return map;
    }

    private static String getName(String SymbolName, String value) {
        String name = "";
        if ("Fork_MOTOR".equals(SymbolName)) {
            switch (value) {
                case "0":
                    name = "中间状态";
                    break;
                case "1":
                    name = "左插到位";
                    break;
                case "2":
                    name = "右插到位";
                    break;
                case "3":
                    name = "故障";
                    break;
                case "4":
                    name = "中插到位";
                    break;
                case "5":
                    name = "故障";
                    break;
                default:
                    name = "";
            }
        } else if ("Stacker_STATE".equals(SymbolName)) {
            switch (value) {
                case "0":
                    name = "维修模式";
                    break;
                case "1":
                    name = "手动模式";
                    break;
                case "2":
                    name = "自动模式";
                    break;
                default:
                    name = "";
            }
        } else if ("Walking_MOTOR".equals(SymbolName)) {
            switch (value) {
                case "0":
                    name = "中间状态";
                    break;
                case "1":
                    name = "电机停止";
                    break;
                case "2":
                    name = "设备运行";
                    break;
                case "3":
                    name = "故障";
                    break;
                case "4":
                    name = "故障";
                    break;
                case "5":
                    name = "故障";
                    break;
                default:
                    name = "";
            }
        } else if ("Stacker_State_ER".equals(SymbolName)) {
            switch (value) {
                case "0":
                    name = "无报警";
                    break;
                case "1":
                    name = "防坠器绳松报警";
                    break;
                case "2":
                    name = "防坠器超速报警";
                    break;
                case "3":
                    name = "钢丝绳过载报警";
                    break;
                case "4":
                    name = "钢丝绳松绳报警";
                    break;
                case "5":
                    name = "行走条码故障";
                    break;
                case "6":
                    name = "升降条码故障";
                    break;
                case "7":
                    name = "货叉编码故障";
                    break;
                case "8":
                    name = "行走驱动器故障";
                    break;
                case "9":
                    name = "升降驱动器故障";
                    break;
                case "10":
                    name = "货叉驱动器故障";
                    break;
                case "11":
                    name = "行走后极限位报警";
                    break;
                case "12":
                    name = "升降后极限位报警";
                    break;
                case "13":
                    name = "货叉右极限位报警";
                    break;
                case "14":
                    name = "行走前极限位报警";
                    break;
                case "15":
                    name = "升降前极限位报警";
                    break;
                case "16":
                    name = "货叉左极限位报警";
                    break;
                case "17":
                    name = "行走运行超时报警";
                    break;
                case "18":
                    name = "升降运行超时报警";
                    break;
                case "19":
                    name = "货叉运行超时报警";
                    break;
                case "20":
                    name = "行走数值越界报警";
                    break;
                case "21":
                    name = "升降数值越界报警";
                    break;
                case "22":
                    name = "货叉数值越界报警";
                    break;
                case "23":
                    name = "行走数值变化异常报警";
                    break;
                case "24":
                    name = "升降数值变化异常报警";
                    break;
                case "25":
                    name = "货叉数值变化异常报警";
                    break;
                case "26":
                    name = "货物偏后报警";
                    break;
                case "27":
                    name = "货物偏前报警";
                    break;
                case "28":
                    name = "货物左限高报警";
                    break;
                case "29":
                    name = "货物偏左报警";
                    break;
                case "30":
                    name = "货叉运行超时报警";
                    break;
                case "31":
                    name = "放货有货报警";
                    break;
                case "32":
                    name = "右限高报警";
                    break;
                case "33":
                    name = "货物偏右报警";
                    break;
                case "34":
                    name = "取货时无货报警";
                    break;
                case "35":
                    name = "取货时货物未取上来报警";
                    break;
                case "36":
                    name = "过弯异常";
                    break;
                case "37":
                    name = "行走到位误差偏大";
                    break;
                case "38":
                    name = "升降到位误差偏大";
                    break;
                case "39":
                    name = "取里面货物外部有货";
                    break;
                case "40":
                    name = "相序报警";
                    break;
                default:
                    name = "";
            }
        } else {
            name = value;
        }
        return name;
    }

}
