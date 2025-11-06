package org.yy.controller.fhdb;

import java.util.List;
import java.util.Map;
import org.yy.controller.app.AppLoginController;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.service.system.UsersService;
import org.yy.util.SpringUtil;
/**
 * 定时检查token是否过期
 * @author YULONG
 *
 */
public class CleanTokenQuartz  extends BaseController implements Job{
	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		
		UsersService usersService = (UsersService)SpringUtil.getBean("usersServiceImpl");
		AppLoginController appLoginController = new AppLoginController();
		PageData pd = new PageData();
		try {
			List<PageData> userListAll=userListAll = usersService.listAllUser(pd);
		for (PageData pageData : userListAll) {
			String USERNAME=pageData.getString("USERNAME");
			boolean isTokenValid=appLoginController.isTokenValid(USERNAME);
			if(isTokenValid==false){
				PageData pdFind = new PageData();
				pdFind.put("UserName", USERNAME);
				PageData pdApp=usersService.findByAppUserName(pdFind);
				pdApp.put("UserName", USERNAME);
				usersService.editAppUserToken(pdApp);
			}
		}
		
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}
