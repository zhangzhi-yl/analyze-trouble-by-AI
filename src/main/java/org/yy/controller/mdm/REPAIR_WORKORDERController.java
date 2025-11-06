package org.yy.controller.mdm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mdm.ACCESSORIES_PROCUREMENTService;
import org.yy.service.mdm.REPAIR_ANNEXService;
import org.yy.service.mdm.REPAIR_OPINIONService;
import org.yy.service.mdm.REPAIR_OUTSOURCINGService;
import org.yy.service.mdm.REPAIR_REPORTWORKService;
import org.yy.service.mdm.REPAIR_SPARE_PARTSService;
import org.yy.service.mdm.REPAIR_WORKORDERService;

/** 
 * 说明：报修工单
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-12
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/repair_workorder")
public class REPAIR_WORKORDERController extends BaseController {
	
	@Autowired
	private REPAIR_WORKORDERService repair_workorderService;	//报修工单
	
	@Autowired
	private ACCESSORIES_PROCUREMENTService accessories_procurementService;	//报修设备配件采购
	
	@Autowired
	private REPAIR_OPINIONService repair_opinionService;	//报修工单领导意见
	
	@Autowired
	private REPAIR_OUTSOURCINGService repair_outsourcingService;	//报修工单委外信息
	
	@Autowired
	private REPAIR_REPORTWORKService repair_reportworkService;	//报修工单报工
	
	@Autowired
	private REPAIR_SPARE_PARTSService repair_spare_partsService;	//报修工单备品备件
	
	@Autowired
	private REPAIR_ANNEXService repair_annexService;	//报修工单附件
	
	@Autowired
	private ProcessEngine processEngine;		 //流程引擎对象
	
	@Autowired
	private RepositoryService repositoryService; //管理流程定义  与流程定义和部署对象相关的Service
	
	@Autowired
	private RuntimeService runtimeService; 		//与正在执行的流程实例和执行对象相关的Service(执行管理，包括启动、推进、删除流程实例等操作)
	
	@Autowired
	private TaskService taskService; 			//任务管理 与正在执行的任务管理相关的Service
	
	@Autowired
	private HistoryService historyService; 		//历史管理(执行完的数据的管理)
	
	/**保存附件及主表数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("repair_workorder:add")
	@ResponseBody
	public Object add(@RequestParam(value="files",required=false)List<MultipartFile> files,
			@RequestParam(value="EQM_NO",required=false) String EQM_NO,
			@RequestParam(value="EQM_NAME",required=false) String EQM_NAME,
			@RequestParam(value="FAULT_DISCOVER_TIME",required=false) String FAULT_DISCOVER_TIME,
			@RequestParam(value="FAULT_DES",required=false) String FAULT_DES,
			@RequestParam(value="FAPPLICANT",required=false) String FAPPLICANT,
			@RequestParam(value="APPLICATION_DATE",required=false) String APPLICATION_DATE,
			@RequestParam(value="FSTATE",required=false) String FSTATE,
			@RequestParam(value="REPAIR_WORKORDER_BILLNO",required=false) String REPAIR_WORKORDER_BILLNO,
			@RequestParam(value="WC_WORKCENTER_ID",required=false) String WC_WORKCENTER_ID,
			@RequestParam(value="EQM_TYPE",required=false) String EQM_TYPE,
			@RequestParam(value="FINISH_TIME",required=false) String FINISH_TIME,
			@RequestParam(value="REPAIR_TYPE",required=false) String REPAIR_TYPE,
			@RequestParam(value="FREMARK",required=false) String FREMARK,
			@RequestParam(value="EQM_ID",required=false) String EQM_ID) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String REPAIR_WORKORDER_ID = this.get32UUID();
		String currentTime = Tools.date2Str(new Date());
		try {
			pd.put("REPAIR_WORKORDER_ID", REPAIR_WORKORDER_ID);	//主键
			pd.put("EQM_NO", EQM_NO);
			pd.put("EQM_NAME", EQM_NAME);
			pd.put("FAULT_DISCOVER_TIME", FAULT_DISCOVER_TIME);
			pd.put("FAULT_DES", FAULT_DES);
			pd.put("APPLICATION_DATE", currentTime);
			pd.put("FSTATE", FSTATE);
			pd.put("FAPPLICANT", Jurisdiction.getName());
			pd.put("REPAIR_WORKORDER_BILLNO", REPAIR_WORKORDER_BILLNO);
			pd.put("WC_WORKCENTER_ID", WC_WORKCENTER_ID);
			pd.put("EQM_TYPE", EQM_TYPE);
			pd.put("FINISH_TIME", FINISH_TIME);
			pd.put("EQM_ID", EQM_ID);
			pd.put("RUN_STATUS", FSTATE);
			/*pd.put("FREMARK", FREMARK);
			pd.put("REPAIR_TYPE", REPAIR_TYPE);
			pd.put("FOPINION", FOPINION);
			pd.put("PROC_INST_ID_", PROC_INST_ID_);
			pd.put("GENERATE_PROGRESS", GENERATE_PROGRESS);
			pd.put("FOPINION1", FOPINION1);
			pd.put("PERSON_LIABLE", PERSON_LIABLE);*/
			repair_workorderService.save(pd);	//保存报修工单
			//上传附件，插入附件数据
			if (null != files && files.size()>0) {
				String dateStr = "", fileName = "", filePath = "";
				PageData annexpd = new PageData();
				for(int i=0;i<files.size();i++) {
					String originalFileName = files.get(i).getOriginalFilename();
					dateStr = DateUtil.getDays();
					filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + dateStr;	//文件上传路径
					fileName = FileUpload.fileUp(files.get(i), filePath, this.get32UUID());				//执行上传
					annexpd.put("REPAIR_ANNEX_ID", this.get32UUID());
					annexpd.put("FNAME", originalFileName);
					annexpd.put("FPATH", Const.FILEPATHFILE + dateStr + "/" + fileName);
					annexpd.put("REPAIR_WORKORDER_ID", REPAIR_WORKORDER_ID);
					annexpd.put("FCREATER", Jurisdiction.getName());
					annexpd.put("CREATE_TIME", currentTime);
					repair_annexService.save(annexpd);
				}
			}
		}catch(Exception e) {
			errInfo = "error";
		}
		
		map.put("result", errInfo);
		return map;
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add1")
	@RequiresPermissions("repair_workorder:add")
	@ResponseBody
	public Object add1() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("REPAIR_WORKORDER_ID", this.get32UUID());	//主键
		pd.put("FAPPLICANT", Jurisdiction.getName());
		pd.put("APPLICATION_DATE", Tools.date2Str(new Date()));
		repair_workorderService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("repair_workorder:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(Integer.parseInt(accessories_procurementService.findCount(pd).get("zs").toString()) > 0 || 
				Integer.parseInt(repair_opinionService.findCount(pd).get("zs").toString()) > 0 || 
				Integer.parseInt(repair_outsourcingService.findCount(pd).get("zs").toString()) > 0 || 
				Integer.parseInt(repair_reportworkService.findCount(pd).get("zs").toString()) > 0 || 
				Integer.parseInt(repair_spare_partsService.findCount(pd).get("zs").toString()) > 0 || 
				Integer.parseInt(repair_annexService.findCount(pd).get("zs").toString())>0){
				errInfo = "error";
			}else{
				repair_workorderService.delete(pd);
			}
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("repair_workorder:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		repair_workorderService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 	更新报修工单状态（创建、发布、结束）
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateState")
	@RequiresPermissions("repair_workorder:edit")
	@ResponseBody
	public Object updateState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		repair_workorderService.updateState(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 查询单号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getBillNo")
	@RequiresPermissions("repair_workorder:edit")
	@ResponseBody
	public Object getBillNo() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "";
		String currentDateStr = DateUtil.date2Str(new Date(), "yyyyMMdd");
		pd = repair_workorderService.findMaxBillNo();	//最大的单号
		if(null!=pd && null!=pd.get("REPAIR_WORKORDER_BILLNO") && !"".equals(pd.get("REPAIR_WORKORDER_BILLNO"))) {
			String REPAIR_WORKORDER_BILLNO = pd.get("REPAIR_WORKORDER_BILLNO").toString();	//示例：20021700001，yyMMdd+00001(五位流水号，顺序加一)
			String dateStr = REPAIR_WORKORDER_BILLNO.substring(0,8);
			String numberStr = REPAIR_WORKORDER_BILLNO.substring(8,12);
			if(dateStr.equals(currentDateStr)) {//流水号中的日期部分是今天日期，流水号+1
				result = dateStr+String.format("%0" + 4 + "d", Integer.parseInt(numberStr) + 1);
			}else {								//流水号中的日期部分不是今天日期，直接获取今天日期+流水号置为1
				result = currentDateStr+"0001";
			}
		} else {
			result = currentDateStr+"0001";
		}
		map.put("BILLNO", result);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 结束任务
	 * @return
	 */
	@RequestMapping(value="/finishtask")
	@ResponseBody
	public Object finishtask() {
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PROC_INST_ID_ = pd.getString("PROC_INST_ID_");
		Task task = taskService.createTaskQuery()//创建查询对象
			.processInstanceId(PROC_INST_ID_)//通过流程实例id来查询当前任务
			.singleResult();//获取单个查询结果
		if(pd.containsKey("RESULT")) {
			paramMap.put("RESULT", pd.getString("RESULT")) ;
			//提交任务的时候传入流程变量
			taskService.complete(task.getId(), paramMap);
		}else {
			taskService.complete(task.getId());
		}
		task = taskService.createTaskQuery()//创建查询对象
				.processInstanceId(PROC_INST_ID_)//通过流程实例id来查询当前任务
				.singleResult();//获取单个查询结果
		if(task==null) {
			pd.put("FSTATE","结束");
			try {
				repair_workorderService.updateStateByPROC_INST_ID(pd);
			} catch (Exception e) {
				System.out.println("这里捕获了一个异常：org.yy.controller.mdm.REPAIR_WORKORDERController.finishtask()");
			}
		}
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 获取任务信息（名称，路径）
	 * @return
	 */
	@RequestMapping(value="/dotask")
	@ResponseBody
	public Object dotask() {
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PROC_INST_ID_ = pd.getString("PROC_INST_ID_");
		Task task = taskService.createTaskQuery()//创建查询对象
			.processInstanceId(PROC_INST_ID_)//通过流程实例id来查询当前任务
			.singleResult();//获取单个查询结果
		if(task==null) {
			pd.put("FSTATE","结束");
			errInfo="error";
			try {
				repair_workorderService.updateStateByPROC_INST_ID(pd);
			} catch (Exception e) {
				System.out.println("这里捕获了一个异常：org.yy.controller.mdm.REPAIR_WORKORDERController.dotask()");
			}
		}else {
			String Description = task.getDescription();
			String taskname = task.getName();
			/*String Assignee = task.getAssignee();
			String Owner = task.getOwner();
			String ProcessDefinitionId = task.getProcessDefinitionId();
			String ProcessInstanceId = task.getProcessInstanceId();
			Map<String, Object> ProcessVariables = task.getProcessVariables();
			String TaskDefinitionKey = task.getTaskDefinitionKey();
			Map<String, Object> TaskLocalVariables = task.getTaskLocalVariables();
			String ParentTaskId = task.getParentTaskId();
			String taskId = task.getId();*/
			
			map.put("taskname", taskname);
			map.put("Description", Description);
		}
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("repair_workorder:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = repair_workorderService.list(page);	//列出REPAIR_WORKORDER列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("repair_workorder:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = repair_workorderService.findById(pd);	//根据ID读取
		if("已发布".equals(pd.getString("FSTATE"))) {
			Task task = taskService.createTaskQuery()//创建查询对象
					.processInstanceId(pd.getString("PROC_INST_ID_"))//通过流程实例id来查询当前任务
					.singleResult();//获取单个查询结果
			if(task==null) {
				pd.put("ID_", "");
			}else {
				pd.put("ID_", task.getId());
			}
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	/**
	 * 根据设备标识查询top1 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/findByIdentify")
	@RequiresPermissions("repair_workorder:edit")
	@ResponseBody
	public Object findByIdentify() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = repair_workorderService.findByIdentify(pd);	//根据设备标识读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("repair_workorder:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			repair_workorderService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		//titles.add("主键ID");	//1
		titles.add("设备编号");	//2
		titles.add("设备名称");	//3
		titles.add("故障发现时间");	//4
		titles.add("故障描述");	//5
		titles.add("申请时间");	//6
		titles.add("状态");	//7
		titles.add("申请人");	//8
		titles.add("单号");	//9
		titles.add("工作中心");	//10
		titles.add("设备类型");	//11
		titles.add("完成时间");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = repair_workorderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			//vpd.put("var1", varOList.get(i).getString("REPAIR_WORKORDER_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQM_NO"));	    //2
			vpd.put("var3", varOList.get(i).getString("EQM_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FAULT_DISCOVER_TIME"));	    //4
			vpd.put("var5", varOList.get(i).getString("FAULT_DES"));	    //5
			vpd.put("var6", varOList.get(i).getString("APPLICATION_DATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FSTATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FAPPLICANT"));	    //8
			vpd.put("var9", varOList.get(i).getString("REPAIR_WORKORDER_BILLNO"));	    //9
			vpd.put("var10", varOList.get(i).getString("WC_WORKCENTER_ID"));	    //10
			vpd.put("var11", varOList.get(i).getString("EQM_TYPE"));	    //11
			vpd.put("var12", varOList.get(i).getString("FINISH_TIME"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
