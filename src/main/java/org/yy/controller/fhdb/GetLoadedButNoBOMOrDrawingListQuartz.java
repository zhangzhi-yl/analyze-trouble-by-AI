package org.yy.controller.fhdb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.fhdb.TimingBackUpMapper;
import org.yy.service.fhdb.EarlyWarningService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.system.UsersService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.SpringUtil;

import com.alibaba.fastjson.JSONObject;

import net.sf.json.JSONString;
@Controller
public class GetLoadedButNoBOMOrDrawingListQuartz extends BaseController implements Job{


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("===========开始执行定时任务==========");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		@SuppressWarnings("unchecked")
		Map<String,Object> parameter = (Map<String, Object>) dataMap.get("parameterList");	//获取参数
		String EXECUTE_ARGS = parameter.get("EXECUTE_ARGS").toString();
		JSONObject jsonObject = JSONObject.parseObject(EXECUTE_ARGS);
		PageData pd = new PageData();
		EarlyWarningService earlyWarningService = (EarlyWarningService)SpringUtil.getBean("earlyWarningServiceImpl");
		NoticeService noticeService = (NoticeService)SpringUtil.getBean("noticeServiceImpl");
		List<PageData> loadedButNoBOMOrDrawingList = earlyWarningService.getLoadedButNoBOMOrDrawingList(pd);
		for (PageData pageData : loadedButNoBOMOrDrawingList) {
			PageData pdNotice = new PageData();
			pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
			pdNotice.put("ReadPeople", ",");// 已读人默认空
			pdNotice.put("FIssuedID", ""); // 发布人
			pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
			pdNotice.put("TType", "消息推送");// 消息类型
			
			if("否".equals(pageData.getString("If_Bom_Done"))&&"否".equals(pageData.getString("If_Drawings_Done"))){
				pdNotice.put("FContent",
						pageData.getString("Cabinet_Aliases")+"的BOM和图纸都还没有设计完成，请注意");// 消息正文
			}
			if("否".equals(pageData.getString("If_Bom_Done"))&&"是".equals(pageData.getString("If_Drawings_Done"))){
				pdNotice.put("FContent",
						pageData.getString("Cabinet_Aliases")+"的BOM还没有设计完成，请注意");// 消息正文
			}
			if("是".equals(pageData.getString("If_Bom_Done"))&&"否".equals(pageData.getString("If_Drawings_Done"))){
				pdNotice.put("FContent",
						pageData.getString("Cabinet_Aliases")+"的图纸还没有设计完成，请注意");// 消息正文
			}
			pdNotice.put("FTitle", "柜体预警");// 消息标题
			pdNotice.put("LinkIf", "no");// 是否跳转页面
			pdNotice.put("DataSources", "柜体预警");// 数据来源
			pdNotice.put("ReceivingAuthority", "," +  pageData.getString("USER_ID") + ",");// 接收人
			pdNotice.put("Report_Key", "changeDrawingBOM");
			pdNotice.put("Report_Value", "");
			try {
				noticeService.save(pdNotice);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
