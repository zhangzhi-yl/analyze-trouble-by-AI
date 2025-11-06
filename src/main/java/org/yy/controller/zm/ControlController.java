package org.yy.controller.zm;

import com.github.pagehelper.Page;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.opc.OPCService;
import org.yy.opc.utils.JiVariantUtil;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.zm.*;
import org.yy.util.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 说明：照明
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-08
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/control")
public class ControlController extends BaseController {

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
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/airConditionerControl")
    @ResponseBody
    public Object airConditionerControl() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("FModel", "读写");
        List<PageData> plcList = plcService.useListByLoop(pd);    //plc参数列表

        //模式
        String pattern = pd.getString("pattern");
        //风速
        String windSpeed = pd.getString("windSpeed");
        //设定温度
        String settingTemperature = pd.getString("settingTemperature");
        //校验 16-30度
        if (Integer.parseInt(settingTemperature) > 30) {
            settingTemperature = "30";
        } else if (Integer.parseInt(settingTemperature) < 16) {
            settingTemperature = "16";
        }

        String LOOP_ID = pd.getString("LOOP_ID");
        if (null != LOOP_ID && !"".equals(LOOP_ID)) {
            for (PageData plc : plcList) {

                //筛选回路下的参数
                if (LOOP_ID.equals(plc.getString("LOOP_ID"))) {

                    if ("模式".equals(plc.getString("ParamType"))) {
                        PLCWrite.write(plc, pattern);
                    } else if ("风速".equals(plc.getString("ParamType"))) {
                        PLCWrite.write(plc, windSpeed);
                    } else if ("设定温度".equals(plc.getString("ParamType"))) {
                        PLCWrite.write(plc, settingTemperature);
                    }
                }
            }
        }

        map.put("result", errInfo);
        return map;
    }

    /**
     * 回路开关控制
     * v1 20211012 sunlz 增加日志记录
     * v1 20211013 sunlz 添加判断
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editLoopStatus")
    @ResponseBody
    public Object edit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> errList = new ArrayList<>();
        String opera = "";
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        try {
            //检验传入开关状态
            int status = 2;
            if (null != pd.get("FStatus").toString() && !"".equals(pd.get("FStatus").toString())) {
                status = Integer.parseInt(pd.get("FStatus").toString());
            } else {
                errInfo = "传入开关状态为空";
                map.put("result", errInfo);
                return map;
            }
            //检验开关状态是否为 0或1
            if (0 != status && 1 != status) {
                errInfo = "开关传值错误";
                map.put("result", errInfo);
                return map;
            }

            //获取PLC开关参数配置
            PageData plc = getPlcParam(pd);
            if (null == plc || plc.size() == 0) {
                errInfo = "plc参数配置错误或未配置";
                map.put("result", errInfo);
                return map;
            }

            //校验参数类型  如果为空调则直接写入0、1  如果为照明或插座 则先写入1再写入0
            String FType = plc.getString("FType");

            //判断是否有错误
            boolean isOperate = false;

            if ("空调".equals(FType)) {
                //写入plc
                if (0 == status) {
                    isOperate = PLCWrite.write(plc, "0");
                    opera = "回路关闭";
                } else {
                    isOperate = PLCWrite.write(plc, "1");
                    opera = "回路开启";
                }
            } else {
                //写入plc
                isOperate = PLCWrite.write(plc, "1");
                Thread.sleep(1000);
                isOperate = PLCWrite.write(plc, "0");

                if (0 == status) {
                    opera = "回路关闭";
                } else {
                    opera = "回路开启";
                }
            }

            if (isOperate) {
                //更新数据库状态
                loopService.editStatus(pd);
                if(!"1".equals(pd.getString("sign"))){
                    //日志记录
                    logService.save("系统手动控制:" + opera, plc.getString("LoopName"), "-");
                }
            } else {
                errInfo = "plc参数配置错误";
            }

        } catch (Exception e) {
            e.printStackTrace();
//            errInfo = "系统错误，请联系管理员";
        }

        map.put("err", errList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 回路总开关控制(插座、灯)
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/operAllLightAndSocket")
    @ResponseBody
    public Object operAllLightAndSocket() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> errList = new ArrayList<>();
        String opera = "";
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

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
                boolean isOperate = false;

                PageData plc = plcList.get(0);
                //写入plc
//                isOperate = PLCWrite.write(plc, "1");
//                isOperate = PLCWrite.write(plc, "0");

                if (0 == status) {
                    opera = "回路全部关闭";
                } else {
                    opera = "回路全部开启";
                }

                if (isOperate) {
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
            errInfo = "系统错误，请联系管理员";
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
    public Object operAllAir() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> errList = new ArrayList<>();
        String opera = "";
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

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

//            PageData plc = plcList.get(0);

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
            errInfo = "系统错误，请联系管理员";
        }

        map.put("err", errList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 根据回路ID获取plc开关配置信息
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
            getPlc.put("FModel", "读写");
            plcList = plcService.useListByLoop(getPlc);
            if (plcList.size() > 0) {
                plc = plcList.get(0);
            }
        }
        return plc;
    }

    /**
     * 分组开关控制
     * v1 20211013 sunlz 创建
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editGroupStatus")
    //@RequiresPermissions("group:edit")
    @ResponseBody
    public Object editStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        String opera = "";
        PageData pd = new PageData();
        pd = this.getPageData();

        try {
            //检验传入开关状态
            int status = 2;
            if (null != pd.get("FStatus").toString() && !"".equals(pd.get("FStatus").toString())) {
                status = Integer.parseInt(pd.get("FStatus").toString());
            } else {
                errInfo = "传入开关状态为空";
                map.put("result", errInfo);
                return map;
            }

            //检验开关状态是否为 0或1
            if (0 != status && 1 != status) {
                errInfo = "开关传值错误";
                map.put("result", errInfo);
                return map;
            }

            //获取分组及回路数据
            List<PageData> plcList = new ArrayList<>();
            List<PageData> loopList = new ArrayList<>();
            PageData group = groupService.findById(pd);

            String FType = group.getString("FType");

            if (null == group) {
                errInfo = "未获取到分组信息";
                map.put("result", errInfo);
                return map;
            } else {
                //获取包含的回路列表
                PageData ids = handleIDS(group.getString("Loop"));
                loopList = loopService.loopAllByIDS(ids);
                if (loopList.size() > 0) {
                    for (PageData loop : loopList) {
                        if (getPlcParam(loop).size() == 0) {
                            errInfo = "分组内存在未配置参数的回路";
                            map.put("result", errInfo);
                            return map;
                        }
                        plcList.add(getPlcParam(loop));
                    }
                }
            }

            boolean isOperate = false;

            //写入plc  更新回路状态
            if ("空调".equals(FType)) {
                //写入plc
                if (0 == status) {
                    isOperate = PLCWrite.writeAll(plcList, "0");
                    for (PageData loop : loopList) {
                        loop.put("FStatus", "0");
                    }
                    opera = "分组关闭";
                } else {
                    isOperate = PLCWrite.writeAll(plcList, "1");
                    for (PageData loop : loopList) {
                        loop.put("FStatus", "1");
                    }
                    opera = "分组开启";
                }
            } else {

                //写入plc
                isOperate = PLCWrite.writeAll(plcList, "1");
                isOperate = PLCWrite.writeAll(plcList, "0");

                if (0 == status) {
                    for (PageData loop : loopList) {
                        loop.put("FStatus", "0");
                    }
                    opera = "分组关闭";
                } else {
                    for (PageData loop : loopList) {
                        loop.put("FStatus", "1");
                    }
                    opera = "分组开启";
                }
            }

            if (isOperate) {
                //批量更新回路状态
                if (loopList.size() > 0) {
                    loopService.editStatusAll(loopList);
                }

                //更新分组状态
                groupService.editStatus(pd);

                //日志记录
                logService.save("系统手动控制:" + opera, group.getString("FName"), "-");
            } else {
                errInfo = "分组内存在plc参数配置错误的回路";
                map.put("result", errInfo);
                return map;
            }
        } catch (Exception e) {
            errInfo = "系统错误，请联系管理员";
        }

        map.put("result", errInfo);
        return map;
    }

    /**
     * 场景开关控制
     * v1 20211013 sunlz 创建
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editSceneStatus")
    //@RequiresPermissions("loop:edit")
    @ResponseBody
    public Object editDateStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> errList = new ArrayList<>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        //检验传入开关状态
        int status = 2;
        if (null != pd.get("FStatus").toString() && !"".equals(pd.get("FStatus").toString())) {
            status = Integer.parseInt(pd.get("FStatus").toString());
        } else {
            errInfo = "开关状态为空";
            errList.add(errInfo);
        }
        //检验开关状态是否为 0或1
        if (0 != status && 1 != status) {
            errInfo = "开关传值错误";
            errList.add(errInfo);
        }

        //获取场景信息
        PageData scene = sceneService.findById(pd);
        if (null == scene) {
            errInfo = "未获取到场景信息";
            errList.add(errInfo);
        }

        //判断是否有错误
        if ("success".equals(errInfo)) {
            String opera = "";
            //写入plc
            if (0 == status) {
                //PLCWrite.write(pd, "0");
                opera = "场景关闭";
            } else {
                //PLCWrite.write(pd, "1");
                opera = "场景开启";
            }

            //更新数据库状态
            sceneService.editStatus(pd);
            //日志记录
            logService.save("系统手动控制:" + opera, scene.getString("FName"), "-");
        }

        map.put("err", errList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 一键开关全部回路
     * v1 20211014 sunlz 创建
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editAllStatus")
    //@RequiresPermissions("loop:edit")
    @ResponseBody
    public Object editAllStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> errList = new ArrayList<>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        String content = "";
        if ("0".equals(pd.getString("FStatus"))) {
            content = "手动关闭全部回路";
        } else if ("1".equals(pd.getString("FStatus"))) {
            content = "手动开启全部回路";
        } else {
            errInfo = "开关传值非0或1";
        }
        if (errInfo.equals("success")) {
            loopService.editAllStatus(pd);
            logService.save(content, "全部", "-");
        }

        map.put("err", errList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 检查场景是否启用
     * v1 20211014 sunlz 创建
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/checkScene")
    @ResponseBody
    public Object checkScene() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        //判断是否存在已启用场景
        List<PageData> sceneList = sceneService.listOnAll(pd);
        if (sceneList.size() > 0) {
            errInfo = "203";
        }

        map.put("result", errInfo);
        return map;
    }

    private static PageData handleIDS(String IDS) {
        PageData ids = new PageData();
        //处理逗号分割id
        String ID = "(";
        if (!"".equals(IDS) && null != IDS) {
            String[] array = IDS.split(",");
            if (array.length != 0) {
                for (int i = 0; i < array.length; i++) {
                    ID = ID + "'" + array[i] + "',";
                    if (i == array.length - 1) {
                        ID = ID.substring(0, ID.length() - 1);
                    }
                }
                ID += ")";
            }
        } else {
            ID = "";
        }
        ids.put("IDS", ID);
        return ids;
    }
}
