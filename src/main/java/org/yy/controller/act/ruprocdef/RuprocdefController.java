package org.yy.controller.act.ruprocdef;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.yy.controller.act.AcBusinessController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.act.HiprocdefService;
import org.yy.service.act.RuprocdefService;
import org.yy.service.system.FhsmsService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.activiti.engine.task.Task;
/**
 * 说明：正在运行的流程
 * 作者：YuanYe Q31-359-6790
 * 
 */
@Controller
@RequestMapping(value="/ruprocdef")
public class RuprocdefController extends AcBusinessController {
	
	@Autowired
	private RuprocdefService ruprocdefService;
	
	@Autowired
	private FhsmsService fhsmsService;
	
	@Autowired
	private HiprocdefService hiprocdefService;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("ruprocdef:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("keywords", KEYWORDS.trim());
		String STRARTTIME = pd.getString("STRARTTIME");					//开始时间
		String ENDTIME = pd.getString("ENDTIME");						//结束时间
		if(Tools.notEmpty(STRARTTIME))pd.put("lastStart", STRARTTIME+" 00:00:00");
		if(Tools.notEmpty(ENDTIME))pd.put("lastEnd", ENDTIME+" 00:00:00");
		page.setPd(pd);
		List<PageData>	varList = ruprocdefService.list(page);			//列出Ruprocdef列表
		for(int i=0;i<varList.size();i++){
			varList.get(i).put("INITATOR", getInitiator(varList.get(i).getString("PROC_INST_ID_")));//流程申请人
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**委派
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delegate")
	@RequiresPermissions("Delegate")
	@ResponseBody
	public Object delegate() throws Exception{
		Map<String,Object> zmap = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put("审批结果", " 任务由 ["+Jurisdiction.getUsername()+"] 委派  ");	//审批结果中记录委派
		setVariablesByTaskIdAsMap(pd.getString("ID_"),map);						//设置流程变量
		setAssignee(pd.getString("ID_"),pd.getString("ASSIGNEE_"));
		zmap.put("ASSIGNEE_",pd.getString("ASSIGNEE_"));						//用于给待办人发送新任务消息
		zmap.put("result", errInfo);				//返回结果
		return zmap;
	}
	
	/**激活or挂起任务
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/onoffTask")
	@RequiresPermissions("ruprocdef:edit")
	@ResponseBody
	public Object onoffTask()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		ruprocdefService.onoffTask(pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**作废流程
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("Abolish")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String reason = "【作废】"+Jurisdiction.getName()+"："+URLDecoder.decode(pd.getString("reason"), "UTF-8");	//作废原因
		/**任务结束时发站内信通知审批结束*/
		List<PageData>	hivarList = hiprocdefService.hivarList(pd);			//列出历史流程变量列表
		for(int i=0;i<hivarList.size();i++){
			if("USERNAME".equals(hivarList.get(i).getString("NAME_"))){
				sendSms(hivarList.get(i).getString("TEXT_"));
				break;
			}
		}
		deleteProcessInstance(pd.getString("PROC_INST_ID_"),reason);		//作废流程
		map.put("result", errInfo);
		return map;
	}
	
	/**发站内信通知审批结束
	 * @param USERNAME
	 * @throws Exception
	 */
	public void sendSms(String USERNAME) throws Exception{
		PageData pd = new PageData();
		pd.put("SANME_ID", this.get32UUID());			//ID
		pd.put("SEND_TIME", DateUtil.getTime());		//发送时间
		pd.put("FHSMS_ID", this.get32UUID());			//主键
		pd.put("TYPE", "1");							//类型1：收信
		pd.put("FROM_USERNAME", USERNAME);				//收信人
		pd.put("TO_USERNAME", "系统消息");
		pd.put("CONTENT", "您申请的任务已经被作废,请到已办任务列表查看");
		pd.put("STATUS", "2");
		fhsmsService.save(pd);
	}

	@RequestMapping(value="/startUpAct")
	@ResponseBody
	public Object startUpAct() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
			try {
				/*
						SrartKeyId  启动Key
						DisplayPages 显示链接
						BusinessTableName 表明
						ProcessInstanceID 流程ID字段名
						PrimaryKeyName 主键字段名
						PrimaryKeyValue  主键值
						*/
				/** 工作流的操作 **/
				Map<String, Object> mapVal = new LinkedHashMap<String, Object>();
				mapVal.put("USERNAME", Jurisdiction.getUsername());//申请人账号
				mapVal.put("UNAME", Jurisdiction.getName()); //申请人
				mapVal.put("SrartKeyId", pd.get("SrartKeyId")); //
				mapVal.put("BusinessTableName", pd.get("BusinessTableName")); //
				mapVal.put("ProcessInstanceID", pd.get("ProcessInstanceID")); //
				mapVal.put("PrimaryKeyName", pd.get("PrimaryKeyName")); //
				mapVal.put("PrimaryKeyValue", pd.get("PrimaryKeyValue")); //
				mapVal.put("ProjectNum", pd.get("ProjectNum")); //项目号
				mapVal.put("ProjectGL1", pd.get("ProjectGL1")); //项目关联字段1
				mapVal.put("ProjectGL2", pd.get("ProjectGL2")); //项目关联字段2
				mapVal.put("ProjectGL3", pd.get("ProjectGL3")); //项目关联字段3
				mapVal.put("ProjectGL4", pd.get("ProjectGL4")); //项目关联字段4
				mapVal.put("FTitle_", pd.get("FTitle_")); //流程定义
				mapVal.put("BXGDID", pd.get("BXGDID")); //工单条码
				String PROC_INST_ID_ = startProcessInstanceByKeyHasVariables(pd.getString("SrartKeyId"), mapVal); //启动流程实例通过KEY
				pd.put("PROC_INST_ID_", PROC_INST_ID_);
				pd.put("ID", get32UUID());
				ruprocdefService.saveProcessStatusTable(pd);
				PageData editRPIMap = new PageData();
				String strSql = "UPDATE " + pd.getString("BusinessTableName") + " SET "
						+ pd.getString("ProcessInstanceID") + "='" + PROC_INST_ID_ + "',RUN_STATUS='执行中'  WHERE "
						+ pd.getString("PrimaryKeyName") + "='" + pd.getString("PrimaryKeyValue") + "'";
				editRPIMap.put("strSql", strSql);
				ruprocdefService.editReverseProcessID(editRPIMap);
				 	List<Task> tasklist2 = findMyTaskList(PROC_INST_ID_);//跳过申请人
					for (int i = 0; i < tasklist2.size(); i++) {
						Task task=tasklist2.get(i);
						Map<String,Object> map1 = new LinkedHashMap<String, Object>();
						map1.put("审批结果", "【申请人自动完成】,"+Jurisdiction.getName()+"fh,申请人自动完成");		//审批结果
						setVariablesByTaskIdAsMap(task.getId(),map1);			//设置流程变量
						completeMyPersonalTask(task.getId());
					}
				
				//mv.addObject("ASSIGNEE_","");	//用于给待办人发送新任务消息
				//mv.addObject("ASSIGNEE_",Jurisdiction.getUsername());	//用于给待办人发送新任务消息
				result = "200";
			} catch (Exception e) {
				result = "500";
			}
			map.put("result", result);
			return map;
	}
	
}
