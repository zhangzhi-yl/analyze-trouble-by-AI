package org.yy.opc;

import org.apache.poi.ss.formula.functions.T;
import org.openscada.opc.lib.da.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.ny.NYPLCService;
import org.yy.service.ny.RecordService;
import org.yy.service.zm.*;
import org.yy.util.PLCWrite;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * v1 cuiyu 2021-07-08
 * 监听plc状态 判断是否下发任务
 *
 * @author cuidayu
 */
@Component
@EnableScheduling
@Configuration
public class OPCConnect {

    @Autowired
    PLCService plcService;

    @Autowired
    SceneService sceneBiz;

    @Autowired
    SceneTimeService sceneTimeBiz;

    @Autowired
    LoopService loopBiz;

    @Autowired
    LoopTimeService loopTimeBiz;

    @Autowired
    LogService logBiz;

    @Autowired
    ALARMRULEService alarmruleService;

    @Autowired
    ALARMService alarmService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private NYPLCService nyplcService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RecordService recordService;



    /**
     * v1 sunlz 2021-10-17
     * 监听plc状态 读取并存储读数(照明)
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void setRecord() throws Exception {

        //System.out.println("执行静态定时任务  " + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));

        List<PageData> plcList = plcService.useList();

        List<PageData> sqlList = new ArrayList<>();
        List<PageData> saveList = new ArrayList<>();
        List<PageData> electricList = new ArrayList<>();

        String FTime = Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");

        if (plcList.size() != 0) {
            for (int i = 0; i < plcList.size(); i++) {

                PageData plc = plcList.get(i);

                //存储字段名和值
                PageData saveLight = new PageData();
                PageData saveElectric = new PageData();

                //读取PLC数据
                String value = OPCUtil.getPlcData(plc);

                if (!"".equals(value)) {

                    //存储表名
                    String SaveTable = plc.getString("SaveTable");
                    //存储字段数据
                    String SaveField = plc.getString("SaveField");

                    //存入list
                    if ("T_PatraElectric".equals(SaveTable)) {
                        saveElectric.put("name", SaveField);
                        saveElectric.put("value", value);
                        electricList.add(saveElectric);
                    } else if ("T_PatraLight".equals(SaveTable)) {
                        saveLight.put("name", SaveField);
                        saveLight.put("value", value);
                        saveList.add(saveLight);
                    }
                }
            }

            if (saveList.size() > 0) {
                PageData sql = new PageData();
                //拼接灯新增SQL
                StringBuilder lightField = new StringBuilder();
                StringBuilder lightValue = new StringBuilder();
                for (PageData light : saveList) {
                    lightField.append(light.getString("name")).append(",");
                    lightValue.append("'").append(light.getString("value")).append("',");
                }
                //截掉最后一个逗号
                String lightFieldString = lightField.substring(0, lightField.toString().length() - 1);
                String lightValueString = lightValue.substring(0, lightValue.toString().length() - 1);
                String saveSql = "INSERT INTO T_PatraLight (ID,TimeStamp," + lightFieldString + ") VALUES ('" + UuidUtil.get32UUID() + "','" + FTime + "'," + lightValueString + ")";
                sql.put("INSERTSQL", saveSql);
                sqlList.add(sql);
            }

            if (electricList.size() > 0) {
                PageData sql = new PageData();
                //拼接电新增SQL
                StringBuilder electricField = new StringBuilder();
                StringBuilder electricValue = new StringBuilder();
                for (PageData electric : electricList) {
                    electricField.append(electric.getString("name")).append(",");
                    electricValue.append("'").append(electric.getString("value")).append("',");
                }
                //截掉最后一个逗号
                String electricFieldString = electricField.substring(0, electricField.toString().length() - 1);
                String electricValueString = electricValue.substring(0, electricValue.toString().length() - 1);
                String electricSql = "INSERT INTO T_PatraElectric (ID,TimeStamp," + electricFieldString + ") VALUES ('" + UuidUtil.get32UUID() + "','" + FTime + "'," + electricValueString + ")";
                sql.put("INSERTSQL", electricSql);
                sqlList.add(sql);
            }

            if (sqlList.size() > 0) {
                plcService.saveRecord(sqlList);
            }
        }
    }

    /**
     * v1 sunlz 2021-10-17
     * 监听plc状态 读取并存储读数(能源)
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void setNyRecord() throws Exception {

        //System.out.println("执行静态定时任务  " + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String FTime = Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");

        List<PageData> plcList = nyplcService.useList();

        //演示===================================================
        List<PageData> ysList = new ArrayList<PageData>();

        for (PageData plc : plcList){
            String field = plc.getString("SaveField");
            if("A95".equals(field) || "A96".equals(field) || "A97".equals(field) || "A98".equals(field)){
                ysList.add(plc);
            }
        }

        plcList.removeAll(ysList);

        for (PageData plc : ysList){

            String field = plc.getString("SaveField");
            PageData p = new PageData();
            p.put("field",field);
            p = recordService.getTopNum(p);

            Random r = new Random();
            String value = String.format("%.2f",Double.parseDouble(p.get(field).toString()) + r.nextDouble());
            plc.put("value",value);
        }

        //=======================================================
        //批量读取plc数据
        plcList = OPCUtil.getAllNyPlcData(plcList);

        plcList.addAll(ysList);

        List<PageData> sqlList = new ArrayList<>();
        List<PageData> saveList = new ArrayList<>();
        List<PageData> electricList = new ArrayList<>();

        if (plcList.size() != 0) {
            for (int i = 0; i < plcList.size(); i++) {

                PageData plc = plcList.get(i);

                //存储字段名和值
                PageData saveLight = new PageData();
                PageData saveElectric = new PageData();

                String value = plc.get("value").toString();
                if (!"".equals(value)) {

                    //存储表名
                    String SaveTable = plc.getString("SaveTable");
                    //存储字段数据
                    String SaveField = plc.getString("SaveField");

                    //存入list
                    if ("T_PatraElectric".equals(SaveTable)) {
                        saveElectric.put("name", SaveField);
                        saveElectric.put("value", value);
                        electricList.add(saveElectric);
                    } else if ("T_PatraLight".equals(SaveTable)) {
                        saveLight.put("name", SaveField);
                        saveLight.put("value", value);
                        saveList.add(saveLight);
                    }
                }
            }

            if (saveList.size() > 0) {
                PageData sql = new PageData();
                //拼接灯新增SQL
                StringBuilder lightField = new StringBuilder();
                StringBuilder lightValue = new StringBuilder();
                for (PageData light : saveList) {
                    lightField.append(light.getString("name")).append(",");
                    lightValue.append("'").append(light.getString("value")).append("',");
                }
                //截掉最后一个逗号
                String lightFieldString = lightField.substring(0, lightField.toString().length() - 1);
                String lightValueString = lightValue.substring(0, lightValue.toString().length() - 1);
                String saveSql = "INSERT INTO T_PatraElectric (ID,TimeStamp," + lightFieldString + ") VALUES ('" + UuidUtil.get32UUID() + "','" + FTime + "'," + lightValueString + ")";
                sql.put("INSERTSQL", saveSql);
                sqlList.add(sql);
            }

            if (electricList.size() > 0) {
                PageData sql = new PageData();
                //拼接电新增SQL
                StringBuilder electricField = new StringBuilder();
                StringBuilder electricValue = new StringBuilder();
                for (PageData electric : electricList) {
                    electricField.append(electric.getString("name")).append(",");
                    electricValue.append("'").append(electric.getString("value")).append("',");
                }
                //截掉最后一个逗号
                String electricFieldString = electricField.substring(0, electricField.toString().length() - 1);
                String electricValueString = electricValue.substring(0, electricValue.toString().length() - 1);
                String electricSql = "INSERT INTO T_PatraElectric (ID,TimeStamp," + electricFieldString + ") VALUES ('" + UuidUtil.get32UUID() + "','" + FTime + "'," + electricValueString + ")";
                sql.put("INSERTSQL", electricSql);
                sqlList.add(sql);
            }

            if (sqlList.size() > 0) {
                plcService.saveRecord(sqlList);
            }
        }
    }

    /**
     * v1 sunlz 2021-10-17
     * 根据场景定时开关灯,每分钟执行一次
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void TimingOperation() throws Exception {

        //("执行静态定时任务:定时开关灯  " + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));

        //获取当前系统日期、时间
        SimpleDateFormat FDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat FTime = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String dateStr = FDate.format(date);
        String timeStr = FTime.format(date);

        //定义批量开启关闭列表
        List<PageData> onList = new ArrayList<>();
        List<PageData> offList = new ArrayList<>();

        //获取全部已开启的场景列表
        PageData getScene = new PageData();
        List<PageData> sceneList = sceneBiz.listOnAll(getScene);

        for (PageData scene : sceneList) {

            //根据IDS获取全部已启用定时回路
            String IDS = scene.getString("Loop");
            PageData getLoopParam = handleIDS(IDS);
            List<PageData> loopList = loopBiz.loopOnAllByIDS(getLoopParam);

            //若不存在已启用定时的回路，直接continue
            if (loopList.size() == 0) {
                continue;
            }

            //判断场景日期判断定时是否启用
            boolean isHandleLoop = false;
            String sceneTimeStatus = scene.getString("FDateStatus");
            if ("1".equals(sceneTimeStatus)) {

                //日期启用时根据场景ID及当前系统日期查询设置的日期列表
                PageData getSceneTime = new PageData();
                getSceneTime.put("TimePrarm", dateStr);
                getSceneTime.put("Scene_ID", scene.getString("SCENE_ID"));
                List<PageData> sceneTimeList = sceneTimeBiz.listAllByDate(getSceneTime);

                //若日期列表大小大于0则在日期段内   则开始处理回路列表
                if (sceneTimeList.size() > 0) {
                    isHandleLoop = true;
                } else {
                    //若不在则直接循环到下一次
                    continue;
                }
            } else if ("0".equals(sceneTimeStatus)) {
                //若未启用日期定时则直接处理回路列表
                isHandleLoop = true;
            }

            //开始处理回路数据判断是否开启
            if (isHandleLoop) {

                PageData getLoopTime = new PageData();

                //定时列表全部回路
                List<PageData> loopTimeAllStartList = loopTimeBiz.listAllByTimeStart(getLoopTime);

                for (PageData loop : loopList) {

                    //判断回路日期定时是否启用
                    String loopTimeStatus = loop.getString("FTimeStatus");

                    if ("1".equals(loopTimeStatus)) {

                        //定义时间点开启的回路列表
                        List<PageData> loopTimeStartList = new ArrayList<>();
                        //定义时间点关闭的回路列表
                        List<PageData> loopTimeEndList = new ArrayList<>();

                        for (PageData loopTime : loopTimeAllStartList){
                            String StartTime = loopTime.getString("StartTime");
                            String EndTime = loopTime.getString("EndTime");
                            String Loop_ID = loopTime.getString("Loop_ID");
                            if(timeStr.equals(StartTime) && Loop_ID.equals(loop.getString("LOOP_ID"))){
                                loopTimeStartList.add(loopTime);
                            }
                            if(timeStr.equals(EndTime) && Loop_ID.equals(loop.getString("LOOP_ID"))){
                                loopTimeEndList.add(loopTime);
                            }
                        }

                        //当前回路开关状态
                        String loopStatus = loop.getString("FStatus");

                        //若当前系统时间为定义的开启时间，则判断该回路开启状态，若关闭则将回路状态赋值为1并加入开启list 若开启则不做处理
                        if (loopTimeStartList.size() > 0) {

                            //判断回路状态
                            if (!"1".equals(loopStatus)) {
                                loop.put("FStatus", "1");
                                onList.add(loop);
                            }

                        }

                        //若当前系统时间为定义的关闭时间，则判断回路开启状态  若为开启则将回路状态赋值为0并加入关闭list  若关闭则不做处理
                        if (loopTimeEndList.size() > 0) {

                            //判断回路状态
                            if ("1".equals(loopStatus)) {
                                loop.put("FStatus", "0");
                                offList.add(loop);
                            }
                        }
                    }
                }
            }
        }

        PageData getPlc = new PageData();
        getPlc.put("ParamType", "开关");
        getPlc.put("FModel", "读写");
        List<PageData> plcAllList = plcService.listAll(getPlc);

        //若开启列表不为空则批量开启回路  并写入PLC
        if (onList.size() > 0) {

            boolean isOperate = false;
            StringBuilder loopName = new StringBuilder();
            List<PageData> airPlcList = new ArrayList<>();
            List<PageData> otherPlcList = new ArrayList<>();
            for (PageData loop : onList) {
                loopName.append(loop.getString("FName")).append("/");
                for (PageData plc : plcAllList){
                    if(plc.getString("LOOP_ID").equals(loop.getString("LOOP_ID"))){
                        if("空调".equals(plc.getString("FType"))){
                            airPlcList.add(plc);
                        }else if("照明".equals(plc.getString("FType")) || "插座".equals(plc.getString("FType"))){
                            otherPlcList.add(plc);
                        }
                    }
                }
            }

            PLCWrite.writeAll(airPlcList, "1");

            PLCWrite.writeAll(otherPlcList, "1");
            isOperate = PLCWrite.writeAll(otherPlcList, "0");

            if (isOperate) {

                loopBiz.editStatusAll(onList);
                String obj = loopName.toString().substring(0, loopName.toString().length() - 1);
                logBiz.autoSave("场景定时控制:回路开启", obj, "-");

                //System.out.println("场景回路定时开启,开启的回路为:" + obj + ";当前时间:" + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                //System.out.println("1111");
            }
        }

        //若关闭列表不为空则批量关闭回路  并写入PLC
        if (offList.size() > 0) {

            boolean isOperate = false;

            StringBuilder loopName = new StringBuilder();
            List<PageData> airPlcList = new ArrayList<>();
            List<PageData> otherPlcList = new ArrayList<>();

            for (PageData loop : offList) {
                loopName.append(loop.getString("FName")).append("/");
                for (PageData plc : plcAllList){
                    if(plc.getString("LOOP_ID").equals(loop.getString("LOOP_ID"))){
                        if("空调".equals(plc.getString("FType"))){
                            airPlcList.add(plc);
                        }else if("照明".equals(plc.getString("FType")) || "插座".equals(plc.getString("FType"))){
                            otherPlcList.add(plc);
                        }
                    }
                }
            }
            PLCWrite.writeAll(airPlcList, "0");

            PLCWrite.writeAll(otherPlcList, "1");
            isOperate = PLCWrite.writeAll(otherPlcList, "0");

            if (isOperate) {
                loopBiz.editStatusAll(offList);
                String obj = loopName.toString().substring(0, loopName.toString().length() - 1);
                logBiz.autoSave("场景定时控制:回路关闭", obj, "-");
                //System.out.println("场景回路定时关闭,关闭的回路为:" + obj + ";当前时间:" + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                //System.out.println("1111");
            }
        }
    }

    /**
     * v1 sunlz 2021-10-29
     * 监听plc 读取开关状态并更新数据库
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void ReadOPCToUpdate() throws Exception {

        //System.out.println("执行静态定时任务:读取PLC更新回路开关  " + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));

        //获取当前系统日期、时间
        SimpleDateFormat FDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateStr = FDate.format(date);

        List<String> onLoopNameList = new ArrayList<>();
        List<String> offLoopNameList = new ArrayList<>();

        //定义批量开启关闭列表
        List<PageData> onList = new ArrayList<>();
        List<PageData> offList = new ArrayList<>();

        //循环获取plc配置并读取点位数据
        PageData pd = new PageData();
        pd.put("ParamType", "反馈");
        pd.put("FModel", "只读");

        //查询当前启用的plc
        List<PageData> plcControlList = plcService.useListByLoop(pd);

        //从redis读取plc数据并保存
        for (PageData plc : plcControlList){
            String PLC_ID = plc.get("PLC_ID").toString();
            plc.put("status",stringRedisTemplate.opsForValue().get(PLC_ID));
        }

        List<PageData> loopList = loopBiz.listAll(new PageData());

        for (PageData plc : plcControlList) {
            String Loop_Id = plc.getString("LOOP_ID");
            String value = plc.get("status").toString();

            for (PageData loop : loopList){
                String LOOP_ID = loop.getString("LOOP_ID");
                if (LOOP_ID.equals(Loop_Id)){
                    String loopStatus = loop.get("FStatus").toString();

                    //判断开关状态
                    if ("0".equals(value)) {
                        if ("1".equals(loopStatus)) {
                            loop.put("FStatus","0");
                            offList.add(loop);
                            offLoopNameList.add(loop.getString("FName"));
                        }
                    } else if ("1".equals(value)) {
                        if ("0".equals(loopStatus)) {
                            loop.put("FStatus", "1");
                            onList.add(loop);
                            onLoopNameList.add(loop.getString("FName"));
                        }
                    }
                }
            }
        }

        //分组状态
        List<PageData> groupList = groupService.listAll(new PageData());
        List<PageData> closeGroupList = new ArrayList<>();
        List<PageData> openGroupList = new ArrayList<>();
        for (PageData group : groupList){
            String LOOP_IDS = group.getString("Loop");

            int loopOpenNum = 0;
            int loopCloseNum = 0;
            String groupStatus = group.get("FStatus").toString();
            for (PageData loop : loopList){
                if(LOOP_IDS.contains(loop.getString("LOOP_ID"))){
                    String loopStatus = loop.get("FStatus").toString();
                    if("0".equals(loopStatus)){
                        loopCloseNum++;
                    }else {
                        loopOpenNum++;
                    }
                }
            }
            if (loopCloseNum == 0 && "0".equals(groupStatus)){
                group.put("FStatus","1");
                openGroupList.add(group);
            }else if(loopOpenNum == 0 && "1".equals(groupStatus)){
                group.put("FStatus","0");
                closeGroupList.add(group);
            }else if(loopCloseNum != 0 && loopOpenNum != 0 && "1".equals(groupStatus)){
                group.put("FStatus","0");
                closeGroupList.add(group);
            }
        }

        //若开启列表不为空则批量开启回路
        if (openGroupList.size() > 0) {
            groupService.editAllStatus(openGroupList);
        }

        //若开启列表不为空则批量开启回路
        if (closeGroupList.size() > 0) {
            groupService.editAllStatus(closeGroupList);
        }

        //若开启列表不为空则批量开启回路
        if (onList.size() > 0) {
            loopBiz.editStatusAll(onList);
            String obj = String.join("/", onLoopNameList.toArray(new String[onLoopNameList.size()]));
            logBiz.plcControlSave("断路器控制:回路开启", obj, "-");
            //System.out.println("短路器控制回路开启,开启的回路为:" + obj + ";当前时间:" + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }

        //若开启列表不为空则批量关闭回路
        if (offList.size() > 0) {
            loopBiz.editStatusAll(offList);
            String obj = String.join("/", offLoopNameList.toArray(new String[offLoopNameList.size()]));
            logBiz.plcControlSave("断路器控制:回路关闭", obj, "-");
            //System.out.println("场景回路定时关闭,关闭的回路为:" + obj + ";当前时间:" + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * v1 sunlz 2021-10-29
     * 监听plc 读取plc点位数据并判断是否报警
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void ReadOPCToAlarm() throws Exception {

        //System.out.println("执行静态定时任务:读取PLC判断报警  " + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));

        //获取当前系统日期、时间
        SimpleDateFormat FDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateStr = FDate.format(date);

        //定义批量存储报警列表
        List<PageData> saveList = new ArrayList<>();

        //查询当前启用的plc并获取plc数据(能源)
        List<PageData> plcList = nyplcService.useList();

        List<PageData> ysList = new ArrayList<PageData>();

        for (PageData plc : plcList){
            String field = plc.getString("SaveField");
            if("A95".equals(field) || "A96".equals(field) || "A97".equals(field) || "A98".equals(field)){
                ysList.add(plc);
            }
        }

        plcList.removeAll(ysList);

        OPCUtil.getAllNyPlcData(plcList);

        //查询当前启用的空调故障代码plc参数并获取plc数据(空调)
        PageData pd = new PageData();
        pd.put("AccParamType", "故障代码");
        pd.put("FType", "空调");
        pd.put("FModel", "只读");
        List<PageData> airPlcList = plcService.useListByLoop(pd);
       // OPCUtil.getAllData(airPlcList);
        //从redis读取plc数据并保存
        for (PageData plc : airPlcList){
            String PLC_ID = plc.get("PLC_ID").toString();
            plc.put("value",stringRedisTemplate.opsForValue().get(PLC_ID));
        }

        //所有未处理报警信息
        List<PageData> alarmList = new ArrayList<>();
        alarmList = alarmService.listAllNoHandle();

        //所有已启用报警规则列表
        PageData getRule = new PageData();
        getRule.put("FType","用电报警");
        List<PageData> ruleList = alarmruleService.uselist(getRule);
        getRule.put("FType","设备故障");
        List<PageData> faultList = alarmruleService.uselist(getRule);

        //能源报警
        for (PageData plc : plcList) {

            //读取plc数据
            String value = plc.get("value").toString();
            double doubleValue = Double.parseDouble(value);

            //循环判断是否报警
            for (PageData rule : ruleList){

                if(rule.getString("Param").equals(plc.getString("PLC_ID"))){
                    //获取条件及阀值
                    String Condition = rule.getString("CONDITION");
                    String Threshold = rule.get("THRESHOLD").toString();
                    double doubleThreshold = Double.parseDouble(Threshold);

                    //获取插入数据
                    PageData save = new PageData();
                    save.put("ALARM_ID",UuidUtil.get32UUID());
                    save.put("LOOP_ID",plc.getString("Loop"));
                    save.put("ALARM_CONTENT",rule.getString("EVENT_NAME"));
                    save.put("ALARM_TIME",dateStr);
                    save.put("PRIORITY",rule.getString("PRIORITY"));
                    save.put("FType",rule.getString("FType"));
                    save.put("IFHANDLE","未处理");

                    boolean isCheck = true;

                    if(alarmList.size() != 0){
                        for (PageData alarm : alarmList){
                            if(alarm.getString("ALARM_CONTENT").equals(rule.getString("EVENT_NAME"))){
                                //判断报警
                                isCheck = false;
                                break;
                            }
                        }
                    }

                    if(isCheck){
                        //判断报警
                        if("小于".equals(Condition)){
                            if(doubleValue < doubleThreshold){
                                saveList.add(save);
                            }
                        }else if("小于等于".equals(Condition)){
                            if(doubleValue <= doubleThreshold){
                                saveList.add(save);
                            }
                        }else if("等于".equals(Condition)){
                            if(doubleValue == doubleThreshold){
                                saveList.add(save);
                            }
                        }else if("大于等于".equals(Condition)){
                            if(doubleValue >= doubleThreshold){
                                saveList.add(save);
                            }
                        }else if("大于".equals(Condition)){
                            if(doubleValue > doubleThreshold){
                                saveList.add(save);
                            }
                        }
                    }
                }
            }
        }

        //空调故障
        for (PageData plc : airPlcList) {

            //读取plc数据
            String value = plc.get("value").toString();

            //循环判断是否报警
            for (PageData rule : faultList){

                if(rule.getString("Param").equals(plc.getString("PLC_ID"))){
                    //获取条件及阀值
                    String Threshold = rule.get("THRESHOLD").toString();

                    //获取插入数据
                    PageData save = new PageData();
                    save.put("ALARM_ID",UuidUtil.get32UUID());
                    save.put("LOOP_ID",plc.getString("LOOP_ID"));
                    save.put("ALARM_CONTENT",rule.getString("EVENT_NAME"));
                    save.put("ALARM_TIME",dateStr);
                    save.put("PRIORITY",rule.getString("PRIORITY"));
                    save.put("FType",rule.getString("FType"));
                    save.put("IFHANDLE","未处理");

                    boolean isCheck = true;

                    if(alarmList.size() != 0){
                        for (PageData alarm : alarmList){
                            if(alarm.getString("ALARM_CONTENT").equals(rule.getString("EVENT_NAME"))){
                                //判断报警
                                isCheck = false;
                                break;
                            }
                        }
                    }

                    if(isCheck){
                        //判断报警
                        if(Threshold.equals(value)){
                            saveList.add(save);
                        }
                    }
                }
            }
        }

        //批量插入报警列表
        if (saveList.size() > 0) {
            alarmService.saveAll(saveList);
        }
    }

    /**
     * 根据回路ID获取plc配置信息
     * @param pd loop_id
     * @return
     * @throws Exception
     */
    private PageData getPlcParam(PageData pd) throws Exception {

        //获取回路信息
        PageData plc = new PageData();
        PageData loop = loopBiz.findById(pd);
        if (null != loop) {
            //获取PLC数据
            List<PageData> plcList = new ArrayList<>();
            PageData getPlc = new PageData();
            getPlc.put("LOOP_ID", loop.getString("LOOP_ID"));
            getPlc.put("FType", "照明开关");
            plcList = plcService.useListByLoop(getPlc);
            if (plcList.size() > 0) {
                plc = plcList.get(0);
            }
        }
        return plc;
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
