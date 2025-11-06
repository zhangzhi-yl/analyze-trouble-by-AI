package org.yy.controller.fhdb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;


/**
 * 说明：quartz 设备易损件 数据库自动备份工作域
 * 作者：YuanYe Q356703572
 * 
 */
public class FragilePartController extends BaseController implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Map<String,Object> parameter = (Map<String,Object>)dataMap.get("parameterList");	//获取参数
		//普通类从spring容器中拿出service
		PageData pd = new PageData();
		PageData pd1 = new PageData();
		//EQM_VULNERABLE_PARTSService eqm_vulnerable_partsService = (EQM_VULNERABLE_PARTSService)SpringUtil.getBean("EQM_VULNERABLE_PARTSServiceImpl");
		//QUALITY_INSPECTIONService quality_inspectionService = (QUALITY_INSPECTIONService)SpringUtil.getBean("QUALITY_INSPECTIONServiceImpl");
		try {
			List<PageData> list = null;//eqm_vulnerable_partsService.listBB(pd);	//获取距离维护日期当天为天数0的日期的数据列表
			for(int i=0;i<list.size();i++){
				pd.put("QUALITY_INSPECTION_ID", this.get32UUID());			//消息主键
				pd.put("INSPECTION_SENDER", "定时查询消息");					//消息发送人
				pd.put("INSPECTION_SEND_TIME", Tools.date2Str(new Date())); //消息发送时间
				pd.put("ACCEPTER", list.get(i).getString("FFManagerID"));	//消息接收人
				pd.put("INSPECTION_TYPE", "易损件报备");							//消息类型
				pd.put("NSPECTION_CONCENT", "新增易损件报备通知");			//消息标题
				String FEXTEND1 = "新增易损件报备,设备名称:"+list.get(i).getString("SHEBEINAME")+
								  ";易损件名称:"+list.get(i).getString("YSJNAME")+
								  ";易损件型号规格:"+list.get(i).getString("FSPECS")+
								  ";易损件材质:"+list.get(i).getString("FTEXTURE")+
								  ";数量:"+list.get(i).get("FAMOUNT").toString()+
								  ";易损件价格:"+list.get(i).get("FPRICE").toString()+
								  ";易损件生产厂家:"+list.get(i).getString("FMANUFACTURER");
				pd.put("FEXTEND1", FEXTEND1); //消息内容
				//quality_inspectionService.save(pd);		//消息提醒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse(list.get(i).getString("CHECKIN_TIME"));	//获取上一次的维护日期
				Calendar calendar =new GregorianCalendar();
				Integer day = Integer.parseInt(list.get(i).getString("MAINTENANCE_CYCLE"));		//获取维护周期
				calendar.setTime(date);
				calendar.add(calendar.DATE, day);		//计算下一期维护日期
				date = calendar.getTime();		//计算后的日期
				pd1.put("CHECKIN_TIME", Tools.date2Str(date, "yyyy-MM-dd"));
				pd1.put("VULNERABLE_PARTS_ID", list.get(i).getString("VULNERABLE_PARTS_ID"));
				//eqm_vulnerable_partsService.editDay(pd1);		//根据ID修改下一次的维护日期
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
