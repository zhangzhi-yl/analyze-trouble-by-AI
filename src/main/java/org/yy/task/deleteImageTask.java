package org.yy.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yy.entity.PageData;
import org.yy.service.trouble.TroubleService;
import org.yy.util.FileDeleteUtil;

import java.io.IOException;
import java.util.List;

/**
 * 定时获取监控画面并截图
 */
@Component
public class deleteImageTask {
    @Autowired
    private TroubleService troubleService;

//    @Scheduled(cron = "0 0 7 * * ?")//每天早上7点执行
//    @Scheduled(fixedDelay = 300000)// 等待上次结束后五分钟执行
    public void run() throws Exception {
        PageData pd = new PageData();
        pd.put("deleteFile", "1");//查询条件标识
        List<PageData> list = troubleService.listAll(pd);
        for (PageData pageData : list) {
            String imgUrl = pageData.getString("IMG_URL");
            imgUrl = imgUrl.split("11033")[1];
            imgUrl = "D:/bushu11033" + imgUrl.replace("\\", "/");
            if (imgUrl != null && !imgUrl.isEmpty()) {
                // 调用工具类删除单个文件
                boolean success = FileDeleteUtil.deleteFile(imgUrl);
                if(success){
                    pageData.put("STATUS","已删除");
                    troubleService.edit(pageData);
                }
                System.out.println("单个文件删除结果：" + (success ? "成功" : "失败（文件不存在）"));
            }
        }
    }
}
