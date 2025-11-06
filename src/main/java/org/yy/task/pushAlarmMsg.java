package org.yy.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yy.service.trouble.TroubleService;

/**
 * 定时获取监控画面并截图
 */
@Component
public class pushAlarmMsg {
@Autowired
private TroubleService troubleService;
    @Scheduled(cron="0 0/5 * * * ?")//没两小时执行一次
    public void run(){
        try {
            troubleService.pushAlarmMsg();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
