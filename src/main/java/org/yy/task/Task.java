package org.yy.task;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.yy.entity.PageData;
import org.yy.service.zm.*;
import org.yy.util.Tools;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class Task implements ApplicationRunner {

    @Autowired
    SceneService sceneService;

    @Autowired
    SceneTimeService sceneTimeService;

    @Autowired
    LoopService loopService;

    @Autowired
    LoopTimeService loopTimeService;

    @Autowired
    LogService logService;


    private SceneService sceneBiz;
    private SceneTimeService sceneTimeBiz;
    private LoopService loopBiz;
    private LoopTimeService loopTimeBiz;
    private LogService logBiz;


    /**
     * 静态工具类注入
     * @return: void
     */
    @PostConstruct
    public void init() {
        sceneBiz = sceneService;
        sceneTimeBiz = sceneTimeService;
        loopBiz = loopService;
        loopTimeBiz = loopTimeService;
        logBiz = logService;
    }

    /**
     * 每十秒判断定时开关
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try{
//                    autoControl();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },0,40, TimeUnit.SECONDS);
    }

    /**
     * 开关控制
     * @throws Exception
     */
    private void autoControl() throws Exception {

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

        for (PageData scene : sceneList){

            //根据IDS获取全部已启用定时回路
            String IDS = scene.getString("Loop");
            PageData getLoopParam = handleIDS(IDS);
            List<PageData> loopList = loopBiz.loopOnAllByIDS(getLoopParam);

            //若不存在已启用定时的回路，直接continue
            if(loopList.size() == 0){
                continue;
            }

            //判断场景日期判断定时是否启用
            boolean isHandleLoop = false;
            String sceneTimeStatus = scene.getString("FDateStatus");
            if("1".equals(sceneTimeStatus)){

                //日期启用时根据场景ID及当前系统日期查询设置的日期列表
                PageData getSceneTime = new PageData();
                getSceneTime.put("TimePrarm",dateStr);
                getSceneTime.put("Scene_ID",scene.getString("SCENE_ID"));
                List<PageData> sceneTimeList = sceneTimeBiz.listAllByDate(getSceneTime);

                //若日期列表大小大于0则在日期段内   则开始处理回路列表
                if(sceneTimeList.size() > 0){
                    isHandleLoop = true;
                }else {
                    //若不在则直接循环到下一次
                    continue;
                }
            }else if ("0".equals(sceneTimeStatus)){
                //若未启用日期定时则直接处理回路列表
                isHandleLoop = true;
            }

            //开始处理回路数据判断是否开启
            if (isHandleLoop){
                for (PageData loop : loopList){

                    //判断回路日期定时是否启用
                    String loopTimeStatus = loop.getString("FTimeStatus");

                    if("1".equals(loopTimeStatus)){

                        //若启用则按当前系统时间及回路ID查询该回路下的时间列表
                        PageData getLoopTime = new PageData();
                        getLoopTime.put("TimePrarm",timeStr);
                        getLoopTime.put("Loop_ID",loop.getString("LOOP_ID"));

                        //定义时间点开启的回路列表
                        List<PageData> loopTimeStartList = loopTimeBiz.listAllByTimeStart(getLoopTime);
                        //定义时间点关闭的回路列表
                        List<PageData> loopTimeEndList = loopTimeBiz.listAllByTimeEnd(getLoopTime);

                        //当前回路开关状态
                        String loopStatus = loop.getString("FStatus");

                        //若当前系统时间为定义的开启时间，则判断该回路开启状态，若关闭则将回路状态赋值为1并加入开启list 若开启则不做处理
                        if(loopTimeStartList.size() > 0){

                            //判断回路状态
                            if(!"1".equals(loopStatus)){
                                loop.put("FStatus","1");
                                onList.add(loop);
                            }

                        }

                        //若当前系统时间为定义的关闭时间，则判断回路开启状态  若为开启则将回路状态赋值为0并加入关闭list  若关闭则不做处理
                        if(loopTimeEndList.size() > 0) {

                            //判断回路状态
                            if("1".equals(loopStatus)){
                                loop.put("FStatus","0");
                                offList.add(loop);
                            }
                        }
                    }
                }
            }

            //若开启列表不为空则批量开启回路  并写入PLC
            List<PageData> logList = new ArrayList<>();
            if(onList.size() > 0){
                loopBiz.editStatusAll(onList);
                StringBuilder loopName = new StringBuilder();
                for (PageData loop : onList){
                    loopName.append(loop.getString("FName")).append("/");
                }
                String obj = loopName.toString().substring(0,loopName.toString().length()-1);
                logBiz.autoSave("场景定时控制:回路开启",obj,"-");

                System.out.println("场景回路定时开启,开启的回路为:"+obj+";当前时间:"+ Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
            }

            //若开启列表不为空则批量关闭回路  并写入PLC
            if(offList.size() > 0){
                loopBiz.editStatusAll(offList);
                StringBuilder loopName = new StringBuilder();
                for (PageData loop : offList){
                    loopName.append(loop.getString("FName")).append("/");
                }
                String obj = loopName.toString().substring(0,loopName.toString().length()-1);
                logBiz.autoSave("场景定时控制:回路关闭",obj,"-");
                System.out.println("场景回路定时关闭,关闭的回路为:"+obj+";当前时间:"+ Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
            }

        }
    }


    /**
     * 开关控制
     * @throws Exception
     */
    public void control() throws Exception {

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

        for (PageData scene : sceneList){

            //根据IDS获取全部已启用定时回路
            String IDS = scene.getString("Loop");
            PageData getLoopParam = handleIDS(IDS);
            List<PageData> loopList = loopBiz.loopOnAllByIDS(getLoopParam);

            //若不存在已启用定时的回路，直接continue
            if(loopList.size() == 0){
                continue;
            }

            //判断场景日期判断定时是否启用
            boolean isHandleLoop = false;
            String sceneTimeStatus = scene.getString("FDateStatus");
            if("1".equals(sceneTimeStatus)){

                //日期启用时根据场景ID及当前系统日期查询设置的日期列表
                PageData getSceneTime = new PageData();
                getSceneTime.put("TimePrarm",dateStr);
                getSceneTime.put("Scene_ID",scene.getString("SCENE_ID"));
                List<PageData> sceneTimeList = sceneTimeBiz.listAllByDate(getSceneTime);

                //若日期列表大小大于0则在日期段内   则开始处理回路列表
                if(sceneTimeList.size() > 0){
                    isHandleLoop = true;
                }else {
                    //若不在则直接循环到下一次
                    continue;
                }
            }else if ("0".equals(sceneTimeStatus)){
                //若未启用日期定时则直接处理回路列表
                isHandleLoop = true;
            }

            //开始处理回路数据判断是否开启
            if (isHandleLoop){
                for (PageData loop : loopList){

                    //判断回路日期定时是否启用
                    String loopTimeStatus = loop.getString("FTimeStatus");

                    if("1".equals(loopTimeStatus)){

                        //若启用则按当前系统时间及回路ID查询该回路下的时间列表
                        PageData getLoopTime = new PageData();
                        getLoopTime.put("TimePrarm",timeStr);
                        getLoopTime.put("Loop_ID",loop.getString("LOOP_ID"));
                        List<PageData> loopTimeList = loopTimeBiz.listAllByTime(getLoopTime);

                        //当前回路开关状态
                        String loopStatus = loop.getString("FStatus");

                        //若当前系统时间在定义的时间段内，则判断该回路开启状态，若关闭则将回路状态赋值为1并加入开启list 若开启则不做处理
                        if(loopTimeList.size() > 0){

                            //判断回路状态
                            if(!"1".equals(loopStatus)){
                                loop.put("FStatus","1");
                                onList.add(loop);
                            }

                        }else {
                            //若不在定义的时间段内 则判断回路开启状态  若为开启则将回路状态赋值为0并加入关闭list  若关闭则不做处理
                            //判断回路状态
                            if("1".equals(loopStatus)){
                                loop.put("FStatus","0");
                                offList.add(loop);
                            }
                        }
                    }
                }
            }

            //若开启列表不为空则批量开启回路  并写入PLC
            List<PageData> logList = new ArrayList<>();
            if(onList.size() > 0){
                loopBiz.editStatusAll(onList);
                StringBuilder loopName = new StringBuilder();
                for (PageData loop : onList){
                    loopName.append(loop.getString("FName")).append("/");
                }
                String obj = loopName.toString().substring(0,loopName.toString().length()-1);
                logBiz.autoSave("场景手动控制:回路开启",obj,"-");

                System.out.println("场景回路手动开启,开启的回路为:"+obj+";当前时间:"+ Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
            }

            //若开启列表不为空则批量关闭回路  并写入PLC
            if(offList.size() > 0){
                loopBiz.editStatusAll(offList);
                StringBuilder loopName = new StringBuilder();
                for (PageData loop : offList){
                    loopName.append(loop.getString("FName")).append("/");
                }
                String obj = loopName.toString().substring(0,loopName.toString().length()-1);
                logBiz.autoSave("场景手动控制:回路关闭",obj,"-");
                System.out.println("场景回路手动关闭,关闭的回路为:"+obj+";当前时间:"+ Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
            }

        }
    }

    private static PageData handleIDS(String IDS){
        PageData ids = new PageData();
        //处理逗号分割id
        String ID = "(";
        if(!"".equals(IDS) && null != IDS){
            String[] array = IDS.split(",");
            if(array.length != 0){
                for (int i = 0;i < array.length ; i++){
                    ID = ID + "'" + array[i] + "',";
                    if(i == array.length-1){
                        ID = ID.substring(0,ID.length()-1);
                    }
                }
                ID += ")";
            }
        }else {
            ID = "";
        }
        ids.put("IDS",ID);
        return ids;
    }
}
