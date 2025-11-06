package org.yy.controller.act.util.technology;


import java.util.Date;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.act.RuprocdefMapper;
import org.yy.service.act.RuprocdefService;
import org.yy.service.fhdb.BRdbService;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;


/** 范贺男
 * 名称：质量异常审批 驳回时反写状态
 * 更新时间：2018年12月20日 09点34分
 * @version
 */
@SuppressWarnings("serial")
public class ProcessQATaskListener implements ExecutionListener {

	
	public void notify(DelegateExecution execution) {
		WebApplicationContext webctx=ContextLoader.getCurrentWebApplicationContext();
		RuprocdefService ruprocdefService = (RuprocdefService)SpringUtil.getBean("ruprocdefServiceImpl");
		PageData pd = new PageData();
		PageData editRPIMap = new PageData();
		String strSql="UPDATE OEM_QUALITY_ABNORMAL_APPROVAL set  RUN_STATUS = '驳回',APPROVAL_RES = '"+execution.getEventName() +"驳回' where "+execution.getVariable("PrimaryKeyName")+"='"+execution.getVariable("PrimaryKeyValue")+"'";
		editRPIMap.put("strSql", strSql);
		try {
			ruprocdefService.editReverseProcessID(editRPIMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String strSql1="insert into OEM_QUALITY_INSPECTION "
				+ "(QUALITY_INSPECTION_ID,INSPECTION_SENDER,INSPECTION_SEND_TIME,ACCEPTER,INSPECTION_TYPE,INSPECTION_CONCENT,FEXTEND1,FEXTEND2,FEXTEND3,FEXTEND4,FEXTEND5)"
				+ "values"
				+ "('"+this.get32UUID()+"','流程信息','"+Tools.date2Str(new Date(), "yyy-MM-dd hh:MM:ss")+"','"+execution.getVariable("UNAME")+"','流程信息','审批驳回','您发起的审批已被驳回','驳回时间','"+Tools.date2Str(new Date(), "yyy-MM-dd hh:MM:ss")+"',' ','')";
		editRPIMap.put("strSql", strSql1);
		try {
			ruprocdefService.editReverseProcessID(editRPIMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
}
