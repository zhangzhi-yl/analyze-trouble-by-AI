package org.yy.controller.app;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.yy.controller.act.AcBusinessController;
import javax.servlet.http.HttpServletResponse;
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
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.yy.entity.PageData;
import org.yy.service.act.RuprocdefService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mdm.ACCESSORIES_PROCUREMENTService;
import org.yy.service.mdm.EQM_SPARE_PARTSService;
import org.yy.service.mdm.REPAIR_ANNEXService;
import org.yy.service.mdm.REPAIR_OPINIONService;
import org.yy.service.mdm.REPAIR_OUTSOURCINGService;
import org.yy.service.mdm.REPAIR_REPORTWORKService;
import org.yy.service.mdm.REPAIR_SPARE_PARTSService;
import org.yy.service.mdm.REPAIR_WORKORDERService;
import org.yy.service.act.HiprocdefService;
import org.yy.service.act.RuprocdefService;
/** 
 * 说明：报修工单
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-12
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/apprepair_workorder")
public class AppREPAIR_WORKORDERController extends AcBusinessController {
	
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
	@Autowired
	private EQM_SPARE_PARTSService eqm_spare_partsService;
	
	@Autowired
	private StaffService staffService;		//人员
	@Autowired
	private RuprocdefService ruprocdefService;
	
	/**保存附件及主表数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String currentTime = Tools.date2Str(new Date());
		String REPAIR_WORKORDER_ID = pd.getString("REPAIR_WORKORDER_ID");
		//获取主键如果为空则创建主键并返回给前端
		if(REPAIR_WORKORDER_ID.equals("") || REPAIR_WORKORDER_ID == null){
			REPAIR_WORKORDER_ID = this.get32UUID();
		}
		String FSTATE = pd.getString("FSTATE");
		try {
			//获取人员账号，并根据账号查询名称
			PageData pdl = new PageData();
			String USERNAME = pd.getString("UserName");
			pdl.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pdl);
			pd.put("APPLICATION_DATE", currentTime);
			pd.put("FAPPLICANT", staff.getString("NAME"));
			pd.put("REPAIR_WORKORDER_ID", REPAIR_WORKORDER_ID);
			pd.put("FSTATE", FSTATE);
			pd.put("RUN_STATUS", FSTATE);
			repair_workorderService.save(pd);	//保存报修工单
			return AppResult.success("success", "success");
		}catch(Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**
	 * 查询单号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getBillNo")
	@ResponseBody
	public Object getBillNo(AppPage page,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String num = "";
		String result = "success";
		String currentDateStr = DateUtil.date2Str(new Date(), "yyyyMMdd");
		pd = repair_workorderService.findMaxBillNo();	//最大的单号
		if(null!=pd && null!=pd.get("REPAIR_WORKORDER_BILLNO") && !"".equals(pd.get("REPAIR_WORKORDER_BILLNO"))) {
			String REPAIR_WORKORDER_BILLNO = pd.get("REPAIR_WORKORDER_BILLNO").toString();	//示例：20021700001，yyMMdd+00001(五位流水号，顺序加一)
			String dateStr = REPAIR_WORKORDER_BILLNO.substring(0,8);
			String numberStr = REPAIR_WORKORDER_BILLNO.substring(8,12);
			if(dateStr.equals(currentDateStr)) {//流水号中的日期部分是今天日期，流水号+1
				num = dateStr+String.format("%0" + 4 + "d", Integer.parseInt(numberStr) + 1);
			}else {								//流水号中的日期部分不是今天日期，直接获取今天日期+流水号置为1
				num = currentDateStr+"0001";
			}
		} else {
			num = currentDateStr+"0001";
		}
		pd.put("BILLNO", num);
		return AppResult.success(pd,"success", "success");
	}
	
	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			//主表ID
			String REPAIR_WORKORDER_ID = pd.getString("REPAIR_WORKORDER_ID");
			//获取主键如果为空则创建主键并返回给前端
			if(REPAIR_WORKORDER_ID.equals("") || REPAIR_WORKORDER_ID == null){
				REPAIR_WORKORDER_ID = this.get32UUID();
			}
			//获取人员账号，并根据账号查询名称
			PageData pdl = new PageData();
			String USERNAME = pd.getString("UserName");
			pdl.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pdl);
			
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			String day = DateUtil.getDays();
			PageData pd1 = new PageData();
			String FFILENAME = "";
			String FPFFILEPATH = "";
			if (null != file && !file.isEmpty()) {
				String fileName = file.getOriginalFilename();
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + day; // 文件上传路径
				String fileNamereal = fileName.substring(0, fileName.indexOf(".")); // 文件上传路径
				FFILENAME = FileUpload.fileUp(file, filePath, fileNamereal + dateName);// 执行上传
				FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
				pd1.put("AttachmentSet_ID", this.get32UUID());
				pd1.put("FPATH", FPFFILEPATH); // 附件路径
				pd1.put("FNAME", fileName); // 附件名称
				pd1.put("REPAIR_ANNEX_ID", this.get32UUID()); //ID
				pd1.put("REPAIR_WORKORDER_ID", REPAIR_WORKORDER_ID); //主表ID
				pd1.put("FCREATER", staff.getString("NAME")); //创建人
				pd1.put("CREATE_TIME", Tools.date2Str(new Date())); //创建时间
				repair_annexService.save(pd1);
			} else {
				return AppResult.failed("上传文件为空");
			}
			PageData pdData = new PageData();
			pdData.put("REPAIR_WORKORDER_ID", REPAIR_WORKORDER_ID);
			return AppResult.success(REPAIR_WORKORDER_ID, "上传成功", "success");
		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**
	 * 获取文件列表
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listrepairworkorderupload")
	@ResponseBody
	public Object showFileList() throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String REPAIR_WORKORDER_ID = pd.getString("REPAIR_WORKORDER_ID");
			PageData pData = new PageData();
			pData.put("REPAIR_WORKORDER_ID", REPAIR_WORKORDER_ID);
			// 获取上传文件列表
			List<PageData> dataList = repair_annexService.listAll(pData);
			List<PageData> fileList = Lists.newArrayList();
			for (PageData d : dataList) {
				String fileUrl = d.getString("FPATH");
				PageData pData1 = new PageData();
				pData1.put("FUrl", fileUrl);
				pData1.put("REPAIR_ANNEX_ID", d.getString("REPAIR_ANNEX_ID"));
				fileList.add(pData1);
			}
			return AppResult.success(fileList, "获取成功", "success");
		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**
	 * 扫码获取设备信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getdeviceinformation")
	@ResponseBody
	public Object getdeviceinformation() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "success";
		pd = repair_workorderService.findByIdentify(pd);	//根据设备标识读取
		return AppResult.success(pd,"success", "success");
	}

	/**维修任务列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listRepair_Workorder")
	@ResponseBody
	public Object list(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			
			String KEYWORDS_EQM_NO = pd.getString("KEYWORDS_EQM_NO");						//设备编号
			if(Tools.notEmpty(KEYWORDS_EQM_NO))pd.put("KEYWORDS_EQM_NO", KEYWORDS_EQM_NO.trim());
			
			String KEYWORDS_EQM_NAME = pd.getString("KEYWORDS_EQM_NAME");						//设备名称
			if(Tools.notEmpty(KEYWORDS_EQM_NAME))pd.put("KEYWORDS_EQM_NAME", KEYWORDS_EQM_NAME.trim());
			
			String FAPPLICANT = pd.getString("FAPPLICANT");						//创建人
			if(Tools.notEmpty(FAPPLICANT))pd.put("FAPPLICANT", FAPPLICANT.trim());
			
			String orderby = "APPLICATION_DATE";//申请时间
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(repair_workorderService.AppList(page));
			varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	
	/**办理任务
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/handle")
	@ResponseBody
	public Object handle() throws Exception{
		try{
		Map<String,Object> zmap = new HashMap<String,Object>();
		String errInfo = "success";
		Session session = Jurisdiction.getSession();
		PageData pd = new PageData();
		pd = this.getPageData();
		String taskId = pd.getString("ID_");	//任务ID
		String sfrom = "";
		String msg = "yes";
		Object ofrom = getVariablesByTaskIdAsMap(taskId,"审批结果");
		if(null != ofrom){
			sfrom = ofrom.toString();
		}
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		String OPINION = sfrom + pd.getString("STAFF_NAME") + ",fh,"+pd.getString("OPINION");//审批人的姓名+审批意见
		String PROC_INST_ID_ = pd.getString("PROC_INST_ID_");
		if("yes".equals(msg)){								//批准
			map.put("审批结果", "【批准】" + OPINION);		//审批结果
			setVariablesByTaskIdAsMap(taskId,map);			//设置流程变量
			setVariablesByTaskId(taskId,"RESULT","批准");
			if(pd.containsKey("REPAIR_TYPE")) {
				setVariablesByTaskId(taskId,"REPAIR_TYPE", pd.getString("REPAIR_TYPE"));
			}
			if(pd.containsKey("ACCESSORY_WHETHER")) {
				setVariablesByTaskId(taskId,"ACCESSORY_WHETHER", pd.getString("ACCESSORY_WHETHER"));
			}
			completeMyPersonalTask(taskId);
		}else{												//驳回
			map.put("审批结果", "【驳回】" + OPINION);		//审批结果
			setVariablesByTaskIdAsMap(taskId,map);			//设置流程变量
			setVariablesByTaskId(taskId,"RESULT","驳回");
			completeMyPersonalTask(taskId);
		}
		try{
			removeVariablesByPROC_INST_ID_(pd.getString("PROC_INST_ID_"),"RESULT");			//移除流程变量(从正在运行中)
		}catch(Exception e){
			/*此流程变量在历史中**/
		}
		if(Tools.notEmpty(pd.getString("COPYTOER"))) {
			List<PageData>	varList = ruprocdefService.varList(pd);			//列出流程变量列表
			//this.sendSms(pd.getString("COPYTOER"), pd.getString("CREMARKS"), varList, map.get("审批结果").toString());			//发站内信给抄送对象
		}
		/**判断流程是否结束，查询正在执行的执行对象表*/
        ProcessInstance rpi = processEngine.getRuntimeService()//
                        .createProcessInstanceQuery()//创建流程实例查询对象
                        .processInstanceId(PROC_INST_ID_)
                        .singleResult();
        //说明流程实例结束了
        if(rpi==null){
        	//更新业务表状态字段
        	PageData editRPIMap = new PageData();
			String strSql = "UPDATE " + pd.getString("BusinessTableName") + " SET "
					+ "RUN_STATUS = '工单结束' WHERE "
					+ pd.getString("PrimaryKeyName") + "='" + pd.getString("PrimaryKeyValue") + "'";
			editRPIMap.put("strSql", strSql);
			ruprocdefService.editReverseProcessID(editRPIMap);
        }
        return AppResult.success(pd, "success", "success");
	} catch (Exception e) {
		e.printStackTrace();
		return AppResult.failed(e.getMessage());
	}
	}

	/**去详情页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getRWDetail")
	@ResponseBody
	public Object goEdit(HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = repair_workorderService.findById(pd);	//根据ID读取
			return AppResult.success(pd, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	/**备品备件列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listSpare_parts")
	@ResponseBody
	public Object listSpare_parts(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			
			String orderby = "b.CREATE_TIME";//申请时间
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(eqm_spare_partsService.AppList(page));
			varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**报工列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listReport_Work")
	@ResponseBody
	public Object listReport_Work(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			
			String orderby = "f.FTIME";//申请时间
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(repair_reportworkService.AppList(page));
			varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**领导意见列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listRepair_Opinion")
	@ResponseBody
	public Object listRepair_OPINION(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			
			String orderby = "f.FTIME";//申请时间
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(repair_opinionService.AppList(page));
			varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**附件列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listRepair_Annex")
	@ResponseBody
	public Object listRepair_Annex(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			
			String orderby = "f.CREATE_TIME";//申请时间
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(repair_annexService.AppList(page));
			varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**
	 * 消耗备品备件
	 */
	@RequestMapping(value="/XiaoHaoBeiJian")
	@ResponseBody
	public Object XiaoHaoBeiJian(){
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			PageData pd = new PageData();
			PageData pd1 = new PageData();//维修任务信息
			PageData pd2 = new PageData();//备品备件信息
			pd = this.getPageData();
			pd1 = repair_workorderService.findById(pd);//维修任务信息
			pd2 = eqm_spare_partsService.findById(pd);//备品备件信息
			pd.put("EQM_SPARE_PARTS_ID", pd.get("EQM_SPARE_PARTS_ID"));//备件主键ID
			double num1 = ((BigDecimal) pd2.get("SPARE_AMOUNT")).doubleValue();//原有备件数量
			double num2 =Double.parseDouble(pd.getString("XiaoHaoNum"));//要消耗的数量
			double num = num1 - num2;//剩余数量
			pd.put("SPARE_AMOUNT", num);//修改剩余数量
			eqm_spare_partsService.editAmount(pd);//修改现有备品备件的数量
			pd.put("REPAIR_SPARE_PARTS_ID", this.get32UUID());
			pd.put("SPARE_AMOUNT", num2);
			repair_spare_partsService.save(pd);//新增一条备品备件消耗信息
			map.put("pd", pd);
			return AppResult.success(map, "消耗成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	@RequestMapping(value="/addBG")
	@ResponseBody
	public Object addBG() throws Exception{
		/**
		 * REPORTWORK_ID,	
		REPAIR_WORKORDER_ID,	
		FSTAFF,	
		START_TIME,	
		END_TIME,	
		WORKING_HOURS,	
		FSTATE,	
		FTIME,
		DEDUCT_WORKING_HOURS,
		FREMARK
		 */
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("REPORTWORK_ID",this.get32UUID());
			pd.put("REPAIR_WORKORDER_ID", pd.get("REPAIR_WORKORDER_ID"));//维修任务ID
			pd.put("FSTAFF", pd.get("STAFF_NAME"));///人员
			pd.put("START_TIME", pd.get("beginTime"));//开始时间
			pd.put("END_TIME", pd.get("endTime"));//结束时间
			pd.put("WORKING_HOURS", pd.get("shijinum"));//工时
			pd.put("FSTATE", pd.get("workType"));//类型
			pd.put("DEDUCT_WORKING_HOURS", pd.get("Kounum"));//应扣工时
			pd.put("FREMARK", pd.get("beizhu"));//备注
			repair_reportworkService.save(pd);
			return AppResult.success(pd, "新增成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
}
