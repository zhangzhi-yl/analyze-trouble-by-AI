package org.yy.controller.uniapp;

import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.opc.OPCService;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.zm.*;
import org.yy.util.PLCWrite;
import org.yy.util.Tools;

import java.util.*;

/**
 * uniApp回路控制
 */
@Controller
@RequestMapping("/api/uniControl")
public class UniAppControlController extends BaseController {

    @Autowired
    private LoopService loopService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private LogService logService;

    @Autowired
    private PLCService plcService;

    @Autowired
    SceneService sceneService;

    /**
     * 空调设置
     * v1 2021-11-06  sunlz
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/airConditionerControl")
    @ResponseBody
    public Object airConditionerControl(@RequestBody PageData pd){
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
//        System.out.println("time1: "+ Tools.date2Str(new Date(),"HH:mm:ss"));
        //plc参数列表

        try {
            pd.put("FModel", "读写");
            List<PageData> plcList = plcService.useListByLoop(pd);

            //模式
            String pattern = pd.getString("pattern");
            if ("自动".equals(pattern)) {
                pattern = "0";
            } else if ("送风".equals(pattern)) {
                pattern = "1";
            } else if ("制冷".equals(pattern)) {
                pattern = "2";
            } else if ("除湿".equals(pattern)) {
                pattern = "3";
            } else if ("制热".equals(pattern)) {
                pattern = "4";
            }

            //风速
            String windSpeed = pd.get("windSpeed").toString();

            //设定温度
            String settingTemperature = pd.get("settingTemperature").toString();
            //校验 16-30度
            if (Integer.parseInt(settingTemperature) > 30) {
                settingTemperature = "30";
            } else if (Integer.parseInt(settingTemperature) < 16) {
                settingTemperature = "16";
            }
//        System.out.println("time2: "+ Tools.date2Str(new Date(),"HH:mm:ss"));
            boolean isOpe = true;
            //获取前台传参
            String LOOP_ID = pd.getString("LOOP_ID");
            if (null != LOOP_ID && !"".equals(LOOP_ID)) {
                for (PageData plc : plcList) {

                    //筛选回路下的参数
                    if (LOOP_ID.equals(plc.getString("LOOP_ID"))) {

                        //plc写入
                        if ("模式".equals(plc.getString("ParamType"))) {
                            isOpe = PLCWrite.write(plc, pattern);
                        } else if ("风速".equals(plc.getString("ParamType"))) {
                            isOpe = PLCWrite.write(plc, windSpeed);
                        } else if ("设定温度".equals(plc.getString("ParamType"))) {
                            isOpe = PLCWrite.write(plc, settingTemperature);
                        }
                    }
                }
            }

            if (isOpe) {
                map.put("result", errInfo);
            } else {
                errInfo = "网络连接不稳定，请重试";
                map.put("result", errInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
            errInfo = "操作失败";
            map.put("result", errInfo);
            return map;
        }

        return map;
    }

    /**
     * 回路开关控制
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editLoopStatus")
    @ResponseBody
    public Object edit(@RequestBody PageData pd) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> errList = new ArrayList<>();
        String opera = "";
        String errInfo = "success";
        boolean res = Boolean.parseBoolean(pd.get("FStatus").toString());
        if (res) {
            pd.put("FStatus", "1");
        } else {
            pd.put("FStatus", "0");
        }

        try {
            //检验传入开关状态
            int status = 2;
            if (null != pd.get("FStatus").toString() && !"".equals(pd.get("FStatus").toString())) {
                status = Integer.parseInt(pd.get("FStatus").toString());
            } else {
                errInfo = "传入开关状态为空";
                errList.add(errInfo);
                map.put("result", errInfo);
                return map;
            }

            PageData plc = getPlcParam(pd);
            if (null == plc || plc.size() == 0) {
                errInfo = "未配置plc参数";
                map.put("result", errInfo);
                return map;
            }


            //校验参数类型  如果为空调则直接写入0、1  如果为照明或插座 则先写入1再写入0
            String FType = plc.getString("FType");

            boolean isOperate1 = false;
            boolean isOperate0 = false;
            boolean isOperate = false;

            if("空调".equals(FType)){
                //写入plc
                if (0 == status) {
                    isOperate = PLCWrite.write(plc, "0");
                    opera = "回路关闭";
                } else {
                    isOperate = PLCWrite.write(plc, "1");
                    opera = "回路开启";
                }
            }else {
                //写入plc
                isOperate1 = PLCWrite.write(plc, "1");
                isOperate0 = PLCWrite.write(plc, "0");

                if (0 == status) {
                    opera = "回路关闭";
                } else {
                    opera = "回路开启";
                }
            }

            if ((isOperate1 && isOperate0) || isOperate) {
                //更新数据库状态
                loopService.editStatus(pd);
                //日志记录
                logService.appSave(pd.getString("USERNAME"), "手机端手动控制:" + opera, plc.getString("LoopName"), "-");
            } else {
                errInfo = "网络连接不稳定，请重试";
            }
        } catch (Exception e) {
            System.out.println("======" + e + "======");
            errInfo = "操作失败";
        }

        map.put("err", errList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 根据回路ID获取plc配置信息
     *
     * @param pd loop_id
     * @return
     * @throws Exception
     */
    private PageData getPlcParam(PageData pd) throws Exception {

        //获取回路信息
        PageData plc = new PageData();
        PageData loop = loopService.findById(pd);
        if (null != loop) {
            //获取PLC数据
            List<PageData> plcList = new ArrayList<>();
            PageData getPlc = new PageData();
            getPlc.put("LOOP_ID", loop.getString("LOOP_ID"));
            getPlc.put("ParamType", "开关");
            getPlc.put("FType", loop.getString("FType"));
            plcList = plcService.useListByLoop(getPlc);
            if (plcList.size() > 0) {
                plc = plcList.get(0);
            }
        }
        return plc;
    }

    /**
     * 回路总开关控制(插座、灯)
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/operAllLightAndSocket")
    @ResponseBody
    public Object operAllLightAndSocket(@RequestBody PageData pd) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> errList = new ArrayList<>();
        String opera = "";
        String errInfo = "success";
//        pd = this.getPageData();

        pd.put("FStatus", pd.get("FStatus").toString());

        String FType = pd.getString("FType");
        String ParamType = "";
        int status = 2;
        status = Integer.parseInt(pd.get("FStatus").toString());

        if ("插座".equals(FType)) {
            //判断开关
            if (0 == status) {
                ParamType = "插座总关";
            } else if (1 == status) {
                ParamType = "插座总开";
            }
        } else if ("照明".equals(FType)) {
            //判断开关
            if (0 == status) {
                ParamType = "照明总关";
            } else if (1 == status) {
                ParamType = "照明总开";
            }
        } else {
            errInfo = "开关传值错误";
            map.put("result", errInfo);
            return map;
        }


        try {
            if (!"".equals(ParamType)) {

                //获取PLC开关参数配置
                PageData getAll = new PageData();
                getAll.put("FModel", "读写");
                getAll.put("AccParamType", ParamType);
                getAll.put("FType", FType);
                List<PageData> plcList = plcService.useListByLoop(getAll);
                if (plcList.size() != 1) {
                    errInfo = "plc参数配置错误或未配置";
                    map.put("result", errInfo);
                    return map;
                }

                //判断是否有错误
                boolean isOperate1 = false;
                boolean isOperate0 = false;

                PageData plc = plcList.get(0);
                //写入plc
                isOperate1 = PLCWrite.write(plc, "1");
                isOperate0 = PLCWrite.write(plc, "0");

                if (0 == status) {
                    opera = "回路全部关闭";
                } else {
                    opera = "回路全部开启";
                }

                if (isOperate1 && isOperate0) {
                    PageData control = new PageData();
                    control.put("FType",FType);
                    control.put("FStatus",pd.get("FStatus").toString());
                    //更新数据库状态
                    loopService.editStatusByType(control);
                    //日志记录
                    logService.appSave(pd.getString("USERNAME"), "手机端手动控制:" + opera, plc.getString("LoopName"), "-");
                } else {
                    errInfo = "plc参数配置错误";
                }
            }

        } catch (Exception e) {
            System.out.println("======" + e + "======");
            errInfo = "操作失败";
        }

        map.put("err", errList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 回路总开关控制(空调)
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/operAllAir")
    @ResponseBody
    public Object operAllAir(@RequestBody PageData pd) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> errList = new ArrayList<>();
        String opera = "";
        String errInfo = "success";

        pd.put("FStatus", pd.get("FStatus").toString());

        String FType = pd.getString("FType");
        String ParamType = "空调总开关";
        int status = 2;
        status = Integer.parseInt(pd.get("FStatus").toString());


        try {

            //获取PLC开关参数配置
            PageData getAll = new PageData();
            getAll.put("FModel", "读写");
            getAll.put("AccParamType", ParamType);
            getAll.put("FType", "空调");
            List<PageData> plcList = plcService.useListByLoop(getAll);
            if (plcList.size() != 2) {
                errInfo = "plc参数配置错误或未配置";
                map.put("result", errInfo);
                return map;
            }

            //判断是否有错误
            boolean isOperate = false;

            //写入plc
            if (0 == status) {
                isOperate = PLCWrite.writeAll(plcList, "0");
                opera = "回路全部关闭";
            } else if (1 == status) {
                isOperate = PLCWrite.writeAll(plcList, "1");
                opera = "回路全部开启";
            }

            if (isOperate) {
                PageData control = new PageData();
                control.put("FType","空调");
                control.put("FStatus",pd.get("FStatus").toString());
                //更新数据库状态
                loopService.editStatusByType(control);
                //日志记录
                logService.appSave(pd.getString("USERNAME"), "手机端手动控制:" + opera, "全部空调", "-");
            } else {
                errInfo = "plc参数配置错误";
            }

        } catch (Exception e) {
            System.out.println("======" + e + "======");
            errInfo = "操作失败";
        }

        map.put("err", errList);
        map.put("result", errInfo);
        return map;
    }
//
//    /**
//     * 分组开关控制
//     * v1 20211013 sunlz 创建
//     *
//     * @param
//     * @throws Exception
//     */
//    @RequestMapping(value = "/editGroupStatus")
//    @ResponseBody
//    public Object editStatus(@RequestBody PageData pd) throws Exception {
//        Map<String, Object> map = new HashMap<String, Object>();
//        String errInfo = "success";
//        String opera = "";
//
//        try {
//            //检验传入开关状态
//            int status = 2;
//            if (null != pd.get("FStatus").toString() && !"".equals(pd.get("FStatus").toString())) {
//                status = Integer.parseInt(pd.get("FStatus").toString());
//            } else {
//                errInfo = "传入开关状态为空";
//                map.put("result", errInfo);
//                return map;
//            }
//
//            //检验开关状态是否为 0或1
//            if (0 != status && 1 != status) {
//                errInfo = "开关传值错误";
//                map.put("result", errInfo);
//                return map;
//            }
//
//            //获取分组及回路数据
//            List<PageData> plcList = new ArrayList<>();
//            List<PageData> loopList = new ArrayList<>();
//            PageData group = groupService.findById(pd);
//            if (null == group) {
//                errInfo = "未获取到分组信息";
//                map.put("result", errInfo);
//                return map;
//            } else {
//                //获取包含的回路列表
//                PageData ids = handleIDS(group.getString("Loop"));
//                loopList = loopService.loopAllByIDS(ids);
//                if (loopList.size() > 0) {
//                    for (PageData loop : loopList) {
//                        if (getPlcParam(loop).size() == 0) {
//                            errInfo = "分组内存在未配置参数的回路";
//                            map.put("result", errInfo);
//                            return map;
//                        }
//                        plcList.add(getPlcParam(loop));
//                    }
//                }
//            }
//
//            boolean isOperate = false;
//
//            //写入plc  更新回路状态
//            if (0 == status) {
//                isOperate = PLCWrite.writeAll(plcList, "0");
//                for (PageData loop : loopList) {
//                    loop.put("FStatus", "0");
//                }
//                opera = "分组关闭";
//            } else {
//                isOperate = PLCWrite.writeAll(plcList, "1");
//                for (PageData loop : loopList) {
//                    loop.put("FStatus", "1");
//                }
//                opera = "分组开启";
//            }
//
//            if (isOperate) {
//                //批量更新回路状态
//                if (loopList.size() > 0) {
//                    loopService.editStatusAll(loopList);
//                }
//
//                //更新分组状态
//                groupService.editStatus(pd);
//
//                //日志记录
//                logService.save("系统手动控制:" + opera, group.getString("FName"), "-");
//            } else {
//                errInfo = "分组内存在plc参数配置错误的回路";
//                map.put("result", errInfo);
//                return map;
//            }
//        } catch (Exception e) {
//            errInfo = "操作失败";
//        }
//
//        map.put("result", errInfo);
//        return map;
//    }
//
//    /**
//     * 场景开关控制
//     * v1 20211013 sunlz 创建
//     *
//     * @param
//     * @throws Exception
//     */
//    @RequestMapping(value = "/editSceneStatus")
//    @ResponseBody
//    public Object editDateStatus(@RequestBody PageData pd) throws Exception {
//        Map<String, Object> map = new HashMap<String, Object>();
//        ArrayList<String> errList = new ArrayList<>();
//        String errInfo = "success";
//
//        //检验传入开关状态
//        int status = 2;
//        if (null != pd.get("FStatus").toString() && !"".equals(pd.get("FStatus").toString())) {
//            status = Integer.parseInt(pd.get("FStatus").toString());
//        } else {
//            errInfo = "开关状态为空";
//            errList.add(errInfo);
//        }
//        //检验开关状态是否为 0或1
//        if (0 != status && 1 != status) {
//            errInfo = "开关传值错误";
//            errList.add(errInfo);
//        }
//
//        //获取场景信息
//        PageData scene = sceneService.findById(pd);
//        if (null == scene) {
//            errInfo = "未获取到场景信息";
//            errList.add(errInfo);
//        }
//
//        //判断是否有错误
//        if ("success".equals(errInfo)) {
//            String opera = "";
//            //写入plc
//            if (0 == status) {
//                //PLCWrite.write(pd, "0");
//                opera = "场景关闭";
//            } else {
//                //PLCWrite.write(pd, "1");
//                opera = "场景开启";
//            }
//
//            //更新数据库状态
//            sceneService.editStatus(pd);
//            //日志记录
//            logService.save("系统手动控制:" + opera, scene.getString("FName"), "-");
//        }
//
//        map.put("err", errList);
//        map.put("result", errInfo);
//        return map;
//    }
//
//    /**
//     * 一键开关全部回路
//     * v1 20211014 sunlz 创建
//     *
//     * @param
//     * @throws Exception
//     */
//    @RequestMapping(value = "/editAllStatus")
//    @ResponseBody
//    public Object editAllStatus(@RequestBody PageData pd) throws Exception {
//        Map<String, Object> map = new HashMap<String, Object>();
//        ArrayList<String> errList = new ArrayList<>();
//        String errInfo = "success";
//
//        String content = "";
//        if ("0".equals(pd.getString("FStatus"))) {
//            content = "手动关闭全部回路";
//        } else if ("1".equals(pd.getString("FStatus"))) {
//            content = "手动开启全部回路";
//        } else {
//            errInfo = "开关传值非0或1";
//        }
//        if (errInfo.equals("success")) {
//            loopService.editAllStatus(pd);
//            logService.save(content, "全部", "-");
//        }
//
//        map.put("err", errList);
//        map.put("result", errInfo);
//        return map;
//    }
//
//    /**
//     * 检查场景是否启用
//     * v1 20211014 sunlz 创建
//     *
//     * @param
//     * @throws Exception
//     */
//    @RequestMapping(value = "/checkScene")
//    @ResponseBody
//    public Object checkScene(@RequestBody PageData pd) throws Exception {
//        Map<String, Object> map = new HashMap<String, Object>();
//        String errInfo = "success";
//
//        //判断是否存在已启用场景
//        List<PageData> sceneList = sceneService.listOnAll(pd);
//        if (sceneList.size() > 0) {
//            errInfo = "203";
//        }
//
//        map.put("result", errInfo);
//        return map;
//    }

//    private static PageData handleIDS(String IDS) {
//        PageData ids = new PageData();
//        //处理逗号分割id
//        String ID = "(";
//        if (!"".equals(IDS) && null != IDS) {
//            String[] array = IDS.split(",");
//            if (array.length != 0) {
//                for (int i = 0; i < array.length; i++) {
//                    ID = ID + "'" + array[i] + "',";
//                    if (i == array.length - 1) {
//                        ID = ID.substring(0, ID.length() - 1);
//                    }
//                }
//                ID += ")";
//            }
//        } else {
//            ID = "";
//        }
//        ids.put("IDS", ID);
//        return ids;
//    }

}
