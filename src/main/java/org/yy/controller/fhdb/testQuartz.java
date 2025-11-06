package org.yy.controller.fhdb;

import java.util.Date;
import java.util.Map;

import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.fhdb.TimingBackUpMapper;
import org.yy.service.fhdb.BRdbService;
import org.yy.util.DateUtil;
import org.yy.util.DbFH;
import org.yy.util.FileUtil;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 说明：quartz 定时任务测试job
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public class testQuartz extends BaseController implements Job{

	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("===========开始执行定时任务==========");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Map<String,Object> parameter = (Map<String,Object>)dataMap.get("parameterList");	//获取参数
		
		String EXECUTE_ARGS = parameter.get("EXECUTE_ARGS").toString();
		System.out.println("EXECUTE_ARGS===="+EXECUTE_ARGS);
		
		//todo 写 执行逻辑
		System.out.println("===========执行定时任务结束==========");
	}
	
	/**把定时备份任务状态改为关闭
	 * @param pd
	 * @param parameter
	 * @param webctx
	 */
	public void shutdownJob(JobExecutionContext context, PageData pd, Map<String,Object> parameter){
		try {
			context.getScheduler().shutdown();	//备份异常时关闭任务
			//普通类从spring容器中拿出service
			TimingBackUpMapper timingbackupService = (TimingBackUpMapper)SpringUtil.getBean("timingBackUpServiceImpl");
			pd.put("STATUS", 2);				//改变定时运行状态为2，关闭
			pd.put("TIMINGBACKUP_ID", parameter.get("TIMINGBACKUP_ID").toString()); //定时备份ID
			timingbackupService.changeStatus(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
