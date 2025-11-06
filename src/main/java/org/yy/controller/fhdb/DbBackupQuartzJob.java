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
 * 说明：quartz 定时任务调度 数据库自动备份工作域
 * 作者：YuanYe Q356703572
 * 
 */
public class DbBackupQuartzJob extends BaseController implements Job{

	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Map<String,Object> parameter = (Map<String,Object>)dataMap.get("parameterList");	//获取参数
		String TABLENAME = parameter.get("TABLENAME").toString();
		String TIMINGBACKUP_ID = parameter.get("TIMINGBACKUP_ID").toString();
		
		//普通类从spring容器中拿出service
		BRdbService brdbService = (BRdbService)SpringUtil.getBean("bRdbServiceImpl");
		PageData pd = new PageData();
		try {
		System.out.println(System.currentTimeMillis()+"---------------------");
		System.out.println(TABLENAME);
		System.out.println(TIMINGBACKUP_ID);
			//shutdownJob(context,pd,parameter);
		} catch (Exception e) {
			try {
				shutdownJob(context,pd,parameter);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
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
