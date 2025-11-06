package org.yy.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yy.service.trouble.TroubleService;
import org.yy.util.Tools;

import java.util.Date;

/**
 * 定时获取监控画面并截图
 */
@Component
public class getImage {
    @Autowired
    private TroubleService troubleService;

    @Scheduled(cron="0 0/15 * * * ?")//没两小时执行一次
    public void run() {
        System.out.println("定时任务执行时间"+ Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            troubleService.createTroubleByUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
