package org.yy.controller.scheduled.run;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.opc.utils.OPCUtil;
import org.yy.service.ny.SnapshotService;
import org.yy.service.project.manager.ASSEMBLYSTATISTICSService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.zm.PLCService;
import org.yy.util.Tools;

import lombok.extern.apachecommons.CommonsLog;
import org.yy.util.UuidUtil;

@Component
@Configuration
@EnableScheduling
@CommonsLog
public class StaticScheduleTask extends BaseController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PLCService plcService;

    @Autowired
    private SnapshotService snapshotService;

    //每5s读取plc数据存入redis
    @Scheduled(cron = "*/5 * * * * ?")
    public void getPlcDataToRedis() throws Exception{
        PageData pd = new PageData();
        pd.put("FModel","只读");
        pd.put("NoContainsParamType", "立体库");
        List<PageData> plcList = plcService.useListByLoop(pd);
        //System.out.println("读取plc"+"  "+Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //将所有符合条件的plc参数值全部读回并存入pd
        plcList = OPCUtil.getAllData(plcList);
        //System.out.println("读取plc结束"+"  "+Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        for (PageData plc : plcList){
            String plcId = plc.getString("PLC_ID");
            String value = plc.get("value").toString();
            stringRedisTemplate.opsForValue().set(plcId,value);
        }
    }

    /**
     * 每天23:59点插入快照信息
     *
     * @throws Exception
     */
    @Scheduled(cron = "30 59 23 * * ?")
    public void setSnapshot() throws Exception {

        String FTime = Tools.date2Str(new Date(), "yyyy-MM-dd");

        //获取需要快照且启用的plc参数信息
        List<PageData> plcList = snapshotService.plcList(new PageData());

        //查询条件列表
        List<PageData> queryParamList = new ArrayList<>();

        //填充字段列表
        for (PageData plc : plcList){

            PageData queryParam = new PageData();

            String field = "isnull(MAX(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) -" + "isnull(MIN(CAST(f." + plc.getString("SaveField") + " AS numeric(10,2))),0.00) AS FVALUE , '"+plc.getString("PLC_ID")+"' AS PLC_ID";
            String table = plc.getString("SaveTable");

            queryParam.put("QueryField",field);
            queryParam.put("QueryTable",table);
            queryParam.put("QueryCondition","SUBSTRING(f.TimeStamp,0,11) = '"+FTime+"'");

            queryParamList.add(queryParam);
        }

        List<PageData> varList = new ArrayList<>();
        varList = snapshotService.querySnapshotInfo(queryParamList);

        for (PageData var : varList){
            var.put("SNAPSHOT_ID", UuidUtil.get32UUID());
            var.put("FDATE",FTime);
            var.put("CREATOR", "系统管理员");
            var.put("CREATETIME", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }

        if(varList.size() > 0){
            snapshotService.saveAll(varList);
        }

        System.err.println("执行静态定时任务: 本日数据快照 执行时间:" + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss")+" 快照数据量:"+ varList.size());
    }

    /**
     * 每月1日 00:00 删除半年前数据
     *
     * @throws Exception
     */
//    @Scheduled(cron = "0 0 0 1 * ?")
//    public void deleteData() throws Exception {
//
//        String FTime = Tools.date2Str(new Date(), "yyyy-MM-dd");
//        String deleteMonth = TimeUtil.DiffMonth(FTime,-7);
//        System.err.println("执行静态定时任务: 数据清除,清除的月份为: "+deleteMonth+" 执行时间:" + Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
//
//        PageData deleteParam = new PageData();
//        deleteParam.put("deleteMonth",deleteMonth);
//        if(!"".equals(deleteMonth)){
//            snapshotService.deleteData(deleteParam);
//        }
//    }

    /**
     * 每半年执行一次  数据库备份
     *
     * @throws Exception
     */
//    @Scheduled(cron = "0 0 0 1 1/6 ?")
//    public void deleteData() throws Exception {
//
//        String FTime = Tools.date2Str(new Date(), "yyyyMMddHHmmss");
//        String path = Const.DATABASEBACKUPPATH + "energy_" + FTime + ".bak";
//        String backUpSql = "BACKUP DATABASE CollegeEnergy TO DISK = '" + path+"'";
//
//        PageData pd = new PageData();
//        pd.put("sql",backUpSql);
//        snapshotService.runSql(pd);
//
//        System.err.println("执行定时任务:数据库备份 执行时间:"+FTime+" 路径:"+path);
//    }
}