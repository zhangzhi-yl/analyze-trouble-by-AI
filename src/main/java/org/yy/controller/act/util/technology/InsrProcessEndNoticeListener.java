package org.yy.controller.act.util.technology;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.yy.entity.PageData;
import org.yy.service.act.RuprocdefService;
import org.yy.util.SpringUtil;
/**
 * 
 * @author cuiyu
 *工艺通知单审批 结束状态反写
 */
@SuppressWarnings("serial")
public class InsrProcessEndNoticeListener implements ExecutionListener{
	public void notify(DelegateExecution execution) {
		RuprocdefService ruprocdefService = (RuprocdefService)SpringUtil.getBean("ruprocdefServiceImpl");
		PageData editRPIMap = new PageData();
		String strSql="UPDATE OEM_INSR_PROCESS_NOTICE set  RUN_STATUS = '通过',GENERATE_PROGRESS='审批结束',APPROVAL_RES = '"+execution.getEventName() +"-通过' where "+execution.getVariable("PrimaryKeyName")+"='"+execution.getVariable("PrimaryKeyValue")+"'";
		editRPIMap.put("strSql", strSql);
		try {
			ruprocdefService.editReverseProcessID(editRPIMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
