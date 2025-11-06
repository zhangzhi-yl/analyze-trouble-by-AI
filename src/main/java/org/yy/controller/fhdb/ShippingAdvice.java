package org.yy.controller.fhdb;

import java.util.Date;
import java.util.Map;

import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.fhdb.TimingBackUpMapper;
import org.yy.service.fhdb.BRdbService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.DbFH;
import org.yy.util.FileUtil;
import org.yy.util.K3InterfaceDock;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
/**
 * 说明：quartz 定时任务调度 数据库自动备份工作域
 * 作者：YuanYe Q356703572
 * 
 */
public class ShippingAdvice extends BaseController implements Job{
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context){
		// TODO Auto-generated method stub
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Map<String,Object> parameter = (Map<String,Object>)dataMap.get("parameterList");	//获取参数
		//普通类从spring容器中拿出service
		PageData pd = new PageData();
		//QUALITY_INSPECTIONService quality_inspectionService = (QUALITY_INSPECTIONService)SpringUtil.getBean("QUALITY_INSPECTIONServiceImpl");
		String[] params={"getProductStorage",Tools.date2Str(new Date(),"yyyy-MM-dd"),"1"};
		try {
			String str = K3InterfaceDock.GetK3PublicWarehousing(params);
			JSONArray jsonList=new JSONArray();
			JSONObject object = new JSONObject();
			object = object.fromObject(str);
			jsonList = jsonList.fromObject(object.get("data"));
			List<PageData> list = (List)JSONArray.toCollection(jsonList,PageData.class);
			for(int i = 0 ; i< list.size() ; i++){
				pd.put("QUALITY_INSPECTION_ID", this.get32UUID());			//消息主键
				pd.put("INSPECTION_SENDER", "定时查询消息");					//消息发送人
				pd.put("INSPECTION_SEND_TIME", Tools.date2Str(new Date())); //消息发送时间
				pd.put("ACCEPTER", list.get(i).getString("FFManagerID"));	//消息接收人
				pd.put("INSPECTION_TYPE", "发货通知");							//消息类型
				pd.put("NSPECTION_CONCENT", "新增产成品入库发货通知");			//消息标题
				String FEXTEND1 = "新增产成品入库,产成品名称:"+list.get(i).getString("FItemID")+
								  ";长代码:"+list.get(i).getString("FNumber")+
								  ";数量:"+list.get(i).get("FQty").toString()+",提醒进行发货";
				pd.put("FEXTEND1", FEXTEND1); //消息内容
				try {
					//quality_inspectionService.save(pd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
						e.printStackTrace();
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
