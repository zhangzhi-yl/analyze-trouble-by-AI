package org.yy.task;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yy.service.trouble.TroubleService;
import org.yy.util.Tools;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定时推送AI进行隐患分析
 */
@Component
public class analyzeTask implements ApplicationRunner {
    @Autowired
    private TroubleService troubleService;

//    @Scheduled(cron="0 0/10 * * * ?")//没两小时执行一次
//    public void run() {
//        try {
//            troubleService.analyzeTask();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(10,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try{
                    troubleService.analyzeTask();
                    System.out.println("AI隐患分析执行+"+ Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },0,5, TimeUnit.SECONDS);
    }
}
