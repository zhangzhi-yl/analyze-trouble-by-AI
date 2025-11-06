package org.yy.controller.uniapp;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.UniAppUser;
import org.yy.opc.OPCService;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.mom.AreaService;
import org.yy.service.system.FHlogService;
import org.yy.service.system.PhotoService;
import org.yy.service.system.UsersService;
import org.yy.service.zm.EquipmentsService;
import org.yy.service.zm.LoopService;
import org.yy.service.zm.PLCService;
import org.yy.util.Tools;

import java.util.*;

/**
 * uniApp设备回路列表
 */
@Controller
@RequestMapping("/api/uniEquipment")
public class UniAppEquipmentController extends BaseController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EquipmentsService equipmentService;

    @Autowired
    private LoopService loopService;

    @Autowired
    private PLCService plcService;

    @Autowired
    private AreaService areaService;
    /**列表
     * @param page
     * @throws Exception
     */
    @RequestMapping(value="/list")
    //@RequiresPermissions("plantemplate:list")
    @ResponseBody
    public Object list(Page page) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String Location = pd.getString("Location");						//关键词检索条件
        if(Tools.notEmpty(Location))pd.put("Location", Location.trim());
        page.setPd(pd);
        List<PageData>	varList = loopService.list(page);	//列出PlanTemplate列表
        PageData getData = new PageData();
        List<PageData> eqList = equipmentService.listAll(getData);
        for (PageData loop : varList){
            List<PageData> eqListByLoop = new ArrayList<>();
            for (PageData eq : eqList){
                if(loop.getString("LOOP_ID").equals(eq.getString("Loop"))){
                    eqListByLoop.add(eq);
                }
            }
            loop.put("equipmentList",eqListByLoop);
            if("1".equals(loop.get("FStatus").toString())){
                loop.put("FStatus",true);
            }else {
                loop.put("FStatus",false);
            }

        }
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }
    /**列表
     * @param
     * @throws Exception
     * @RequestBody PageData pd
     */
    @RequestMapping(value="/listAll")
    @ResponseBody
    public Object listAll(@RequestBody PageData pd) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData getData = new PageData();

        //回路列表
        getData.put("FType",pd.getString("FType"));
        getData.put("Location",pd.getString("Location"));
        getData.put("Workshop",pd.getString("Workshop"));
        List<PageData> varList = loopService.listAll(getData);

        //列出Equipment列表
        getData = new PageData();
        List<PageData> eqList = equipmentService.listAll(getData);

        for (PageData loop : varList){

            List<PageData> eqListByLoop = new ArrayList<>();
            for (PageData eq : eqList){
                if(loop.getString("LOOP_ID").equals(eq.getString("Loop"))){
                    eqListByLoop.add(eq);
                }
            }
            loop.put("equipmentList",eqListByLoop);
            if("1".equals(loop.get("FStatus").toString())){
                loop.put("FStatus",true);
            }else {
                loop.put("FStatus",false);
            }

        }

        map.put("varList", varList);
        map.put("result", errInfo);
        return map;
    }

    /**列表
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/Arealist")
    //@RequiresPermissions("area:list")
    @ResponseBody
    public Object Arealist(@RequestBody PageData pd) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        List<PageData>	varList = areaService.Arealist(pd);	//列出Area列表
        map.put("varList", varList);
        map.put("result", errInfo);
        return map;
    }
    /**列表
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/airConditionerList")
    @ResponseBody
    public Object airConditionerList(@RequestBody PageData param) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();

        //列出Equipment列表
        pd.put("FType","空调");
        pd.put("Location",param.getString("Location"));
        List<PageData>	varList = equipmentService.listAll(pd);

        PageData getPlc = new PageData();
        getPlc.put("ifUse", "ifUse");
        getPlc.put("FModel", "只读");
        getPlc.put("FType", "空调");
        List<PageData> plcList = plcService.listAll(getPlc);    //plc参数列表

        //将所有符合条件的plc参数值全部读回并存入pd
        //plcList = OPCUtil.getPlcDataAll(plcList);

//        for (PageData air : varList) {
//
//            air.put("pattern", "自动");
//            air.put("windSpeed", "自动");
//            air.put("settingTemperature", "0");
//            air.put("actualTemperature", "0");
//            air.put("faultCode", "0");
//
//            for (PageData plc : plcList) {
//                String ParamType = plc.getString("ParamType");
//                //筛选回路下的参数
//                if (air.getString("LOOP_ID").equals(plc.getString("LOOP_ID"))) {
//
//                    if ("模式".equals(ParamType)) {
//                        String pattern = plc.get("pattern").toString();
//                        if ("0".equals(pattern)) {
//                            air.put("pattern", "自动");
//                        } else if ("1".equals(pattern)) {
//                            air.put("pattern", "送风");
//                        } else if ("2".equals(pattern)) {
//                            air.put("pattern", "制冷");
//                        } else if ("3".equals(pattern)) {
//                            air.put("pattern", "除湿");
//                        } else if ("4".equals(pattern)) {
//                            air.put("pattern", "制热");
//                        }
//
//                    } else if ("风速".equals(ParamType)) {
//                        String windSpeed = plc.get("windSpeed").toString();
//                        if ("0".equals(windSpeed)) {
//                            air.put("windSpeed", "自动");
//                        } else if ("1".equals(windSpeed)) {
//                            air.put("windSpeed", "低风");
//                        } else if ("2".equals(windSpeed)) {
//                            air.put("windSpeed", "中风");
//                        } else if ("3".equals(windSpeed)) {
//                            air.put("windSpeed", "高风");
//                        }
//                    } else if ("设定温度".equals(ParamType)) {
//                        air.put("settingTemperature", Integer.parseInt(plc.get("settingTemperature").toString()));
//                    } else if ("实际温度".equals(ParamType)) {
//                        air.put("actualTemperature", plc.get("actualTemperature").toString());
//                    } else if ("故障代码".equals(ParamType)) {
//                        air.put("faultCode", plc.get("faultCode").toString());
//                    }
//                }
//            }
//        }

        map.put("varList", varList);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 空调
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/airConditioner")
    @ResponseBody
    public Object airConditioner(@RequestBody PageData pd) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";

        //参数数据
        PageData air = new PageData();

        List<PageData> modelList = new ArrayList<>();

        PageData equipment = equipmentService.findById(pd);
        String LOOP_ID = equipment.getString("LOOP_ID");

        if(null != equipment){
            PageData getPlc = new PageData();
            getPlc.put("ifUse", "ifUse");
            getPlc.put("FModel", "只读");
            getPlc.put("FType", "空调");
            getPlc.put("loop_id", LOOP_ID);
            //plc参数列表
            List<PageData> plcList = plcService.listAll(getPlc);

            //从redis读取plc数据并保存
            for (PageData plc : plcList){
                String PLC_ID = plc.get("PLC_ID").toString();
                plc.put("value",stringRedisTemplate.opsForValue().get(PLC_ID));
            }

            //初值
            air.put("pattern", "0");
            air.put("windSpeed", "0");
            air.put("settingTemperature", "0");
            air.put("actualTemperature", "0");
            air.put("faultCode", "0");
            air.put("LOOP_ID",equipment.getString("LOOP_ID"));
            air.put("LoopName",equipment.getString("LoopName"));
            air.put("Location",equipment.getString("Location"));
            air.put("FName",equipment.getString("FName"));
            air.put("LoopStatus",equipment.getString("LoopStatus"));


            if(null != LOOP_ID && !"".equals(LOOP_ID)){
                for (PageData plc : plcList) {

                    String ParamType = plc.getString("ParamType");

                    //筛选回路下的参数
                    if (LOOP_ID.equals(plc.getString("LOOP_ID"))) {
                        if ("模式".equals(ParamType)) {
                            air.put("pattern", plc.get("value").toString());
                        } else if ("风速".equals(ParamType)) {
                            air.put("windSpeed", plc.get("value").toString());
                        } else if ("设定温度".equals(ParamType)) {
                            air.put("settingTemperature", plc.get("value").toString());
                        } else if ("实际温度".equals(ParamType)) {
                            air.put("actualTemperature", plc.get("value").toString());
                        } else if ("故障代码".equals(ParamType)) {
                            air.put("faultCode", plc.get("value").toString());
                        }
                    }
                }
            }

            PageData model = new PageData();

            //填充模式顺序列表
            if("0".equals(air.getString("pattern"))){
                model.put("model",0);
                model.put("tem",Integer.valueOf(air.getString("settingTemperature")));
                model.put("name","自动");
                modelList.add(model);

                modelList.add(getModel("送风",1));
                modelList.add(getModel("制冷",2));
                modelList.add(getModel("除湿",3));
                modelList.add(getModel("制热",4));
            }else if("1".equals(air.getString("pattern"))){
                model.put("model",0);
                model.put("tem",Integer.valueOf(air.getString("settingTemperature")));
                model.put("name","送风");
                modelList.add(model);

                modelList.add(getModel("制冷",1));
                modelList.add(getModel("除湿",2));
                modelList.add(getModel("制热",3));
                modelList.add(getModel("自动",4));
            }else if("2".equals(air.getString("pattern"))){
                model.put("model",0);
                model.put("tem",Integer.valueOf(air.getString("settingTemperature")));
                model.put("name","制冷");
                modelList.add(model);

                modelList.add(getModel("除湿",1));
                modelList.add(getModel("制热",2));
                modelList.add(getModel("自动",3));
                modelList.add(getModel("送风",4));
            }
            else if("3".equals(air.getString("pattern"))){
                model.put("model",0);
                model.put("tem",Integer.valueOf(air.getString("settingTemperature")));
                model.put("name","除湿");
                modelList.add(model);

                modelList.add(getModel("制热",1));
                modelList.add(getModel("自动",2));
                modelList.add(getModel("送风",3));
                modelList.add(getModel("制冷",4));
            }
            else if("4".equals(air.getString("pattern"))){
                model.put("model",0);
                model.put("tem",Integer.valueOf(air.getString("settingTemperature")));
                model.put("name","制热");
                modelList.add(model);

                modelList.add(getModel("自动",1));
                modelList.add(getModel("送风",2));
                modelList.add(getModel("制冷",3));
                modelList.add(getModel("除湿",4));
            }


        }

        map.put("modelList", modelList);
        map.put("pd", air);
        map.put("result", errInfo);
        return map;
    }

    /**
     * 获取模式
     * @param model 模式名
     * @param i 顺序
     * @return res 模式
     */
    private PageData getModel(String model,Integer i){

        PageData res = new PageData();

        if("自动".equals(model)){
            res.put("model",i);
            res.put("tem",22);
            res.put("name","自动");
        }else if("送风".equals(model)){
            res.put("model",i);
            res.put("tem",20);
            res.put("name","送风");
        }else if("制冷".equals(model)){
            res.put("model",i);
            res.put("tem",18);
            res.put("name","制冷");
        }else if("除湿".equals(model)){
            res.put("model",i);
            res.put("tem",22);
            res.put("name","除湿");
        }else if("制热".equals(model)){
            res.put("model",i);
            res.put("tem",24);
            res.put("name","制热");
        }

        return res;
    }

}
