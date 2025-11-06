package org.yy.controller.act.util.technology;


import java.util.Date;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.session.Session;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.act.RuprocdefMapper;
import org.yy.service.act.RuprocdefService;
import org.yy.service.fhdb.BRdbService;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;


/** 袁野
 * 名称：过程中反写
 * 更新时间：2018年12月20日 09点34分
 * @version
 */
@SuppressWarnings("serial")
public class ProcessTaskListener implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		WebApplicationContext webctx=ContextLoader.getCurrentWebApplicationContext();
		RuprocdefService ruprocdefService = (RuprocdefService)SpringUtil.getBean("ruprocdefServiceImpl");
		PageData pd = new PageData();
		PageData editRPIMap = new PageData();
		String strSql="UPDATE "+delegateTask.getVariable("BusinessTableName")+" SET GENERATE_PROGRESS='"+delegateTask.getName()+"审批中'  WHERE "+delegateTask.getVariable("PrimaryKeyName")+"='"+delegateTask.getVariable("PrimaryKeyValue")+"'";
		editRPIMap.put("strSql", strSql);
		try {
			ruprocdefService.editReverseProcessID(editRPIMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String displayPages = (String)delegateTask.getDescription();
		delegateTask.removeVariable("DisplayPages");
		String BXGDID="?FID="+(String)delegateTask.getVariable("PrimaryKeyValue")+""
				+ "&ProjectNum="+(String)delegateTask.getVariable("ProjectNum")+""
						+ "&ProjectGL1="+(String)delegateTask.getVariable("ProjectGL1")+""
								+ "&ProjectGL2="+(String)delegateTask.getVariable("ProjectGL2")+""
										+ "&ProjectGL3="+(String)delegateTask.getVariable("ProjectGL3")+""
												+ "&ProjectGL4="+(String)delegateTask.getVariable("ProjectGL4");//
		delegateTask.setVariable("DisplayPages",displayPages+BXGDID);
	}
}
