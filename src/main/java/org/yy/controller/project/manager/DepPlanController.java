package org.yy.controller.project.manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;

import com.alibaba.druid.util.StringUtils;

import org.yy.entity.PageData;
import org.yy.service.momp.epm.phasetemplate.PhaseTemplateService;
import org.yy.service.project.manager.DepPlanService;
import org.yy.service.project.manager.PLANCHANGEService;
import org.yy.service.project.manager.PROJECTTASKService;
import org.yy.service.project.manager.Pro_PlanService;
import org.yy.service.project.manager.StaffPlanService;
import org.yy.service.system.FhsmsService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;

/** 
 * 说明：部门计划
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-03
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/depplan")
public class DepPlanController extends BaseController {
	
	@Autowired
	private DepPlanService depplanService;
	@Autowired
	private Pro_PlanService pro_planService;
	@Autowired
	private StaffPlanService staffplanService;
	@Autowired
	private PLANCHANGEService planchangeService;
	@Autowired
	private PROJECTTASKService projecttaskService;
	@Autowired
	private FhsmsService fhsmsService;
	@Autowired
	private PhaseTemplateService phasetemplateService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private StaffService StaffService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("depplan:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdOrder =phasetemplateService.getOrder(pd);//获得最大编号
		int FORDER=1;
		if(pdOrder != null) {
			FORDER=Integer.parseInt(pdOrder.get("FORDER").toString())+1;
		}

		pd.put("FHTLEVEL", FORDER);
		pd.put("DEPPLAN_ID", this.get32UUID());	//主键
		pd.put("VISIABLE", "1");	//删除
		pd.put("RUNTYPE", "新建");
		pd.put("FSOURCE", "手动创建");
		pd.put("FTYPE", "未下发");
		pd.put("ETYPE", "未下发");
		pd.put("PLAN_START_TIME", "");
		pd.put("PLAN_END_TIME", "");
		pd.put("NEW_PLAN_START_TIME", "");
		pd.put("NEW_PLAN_END_TIME", "");
		pd.put("NOW_PLAN_TYPE", "");
		pd.put("PRO_PLAN_ID", pd.get("PRO_PLAN_ID"));
		depplanService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/save")
	@ResponseBody
	public Object save() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdOrder =phasetemplateService.getOrder(pd);//获得最大编号
		int FORDER=1;
		if(pdOrder != null) {
			FORDER=Integer.parseInt(pdOrder.get("FORDER").toString())+1;
		}
		List<PageData> varList = phasetemplateService.listAll(pd);
		for ( int i=0;i< varList.size();i++) {
			PageData pd1=varList.get(i);
			pd.put("FHTNAME", pd1.get("FHTNAME"));
			pd.put("PHASETEMPLATE_ID", pd1.get("PHASETEMPLATE_ID"));
			pd.put("FHTLEVEL", FORDER);
			pd.put("DEPPLAN_ID", this.get32UUID());	//主键
			pd.put("VISIABLE", "1");	//删除
			pd.put("RUNTYPE", "新建");
			pd.put("FSOURCE", "模板生成");
			pd.put("FTYPE", "未下发");
			pd.put("ETYPE", "未下发");
			pd.put("PLAN_START_TIME", "");
			pd.put("PLAN_END_TIME", "");
			pd.put("NEW_PLAN_START_TIME", "");
			pd.put("NEW_PLAN_END_TIME", "");
			pd.put("NOW_PLAN_TYPE", "");
			pd.put("PRO_PLAN_ID", pd.get("PRO_PLAN_ID"));
			depplanService.save(pd);
			FORDER+=1;
		}
		map.put("result", errInfo);
		return map;
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("depplan:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		depplanService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/deletex")
	@ResponseBody
	public Object deletex() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		depplanService.deletex(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData Timepd = new PageData();
		pd = this.getPageData();
		PageData oldpd = depplanService.findById(pd);
		pd.put("PHASETEMPLATE_ID", oldpd.get("PHASETEMPLATE_ID"));
		pd.put("FTYPE", oldpd.get("FTYPE"));
		//pd.put("FHTNAME", oldpd.get("FHTNAME"));
		pd.put("PRO_PLAN_ID", oldpd.get("PRO_PLAN_ID"));
		//pd.put("FHTLEVEL", oldpd.get("FHTLEVEL"));
		pd.put("RUNTYPE", oldpd.get("RUNTYPE"));
		pd.put("NAME", pd.getString("FMANAGER"));
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("YL5", pdDept.get("DEPARTMENT_ID"));
		}
		depplanService.edit(pd);
		
		String PLAN_START_TIME = depplanService.getStartTime(pd).get("PLAN_START_TIME").toString();
		String PLAN_END_TIME = depplanService.getEndTime(pd).get("PLAN_END_TIME").toString();
		String NEW_PLAN_START_TIME = depplanService.getStartTime(pd).get("PLAN_START_TIME").toString();
		String NEW_PLAN_END_TIME = depplanService.getEndTime(pd).get("PLAN_END_TIME").toString();
		Timepd.put("PLAN_START_TIME", PLAN_START_TIME);
		Timepd.put("PLAN_END_TIME", PLAN_END_TIME);
		Timepd.put("NEW_PLAN_START_TIME", NEW_PLAN_START_TIME);
		Timepd.put("NEW_PLAN_END_TIME", NEW_PLAN_END_TIME);
		Timepd.put("PRO_PLAN_ID", oldpd.get("PRO_PLAN_ID"));
		pro_planService.updateTime(Timepd);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listx")
	@ResponseBody
	public Object listx() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("DEPARTMENT_ID", pdDept.get("DEPARTMENT_ID"));
		}
		List<PageData>	varList = depplanService.listx(pd);	//列出DepPlan列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("DEPARTMENT_ID", pdDept.get("DEPARTMENT_ID"));
		}
		page.setPd(pd);
		List<PageData>	varList = depplanService.list(page);	//列出DepPlan列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listDep")
	@ResponseBody
	public Object listDep(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		//pd.put("ISALL", Jurisdiction.getUserISALL());
		//pd.put("ISBZ", Jurisdiction.getUserSFLD());
		pd.put("Guan", Jurisdiction.getName());
		//pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_IDNEW());
		page.setPd(pd);
		List<PageData>	varList = depplanService.listDep(page);	//列出DepPlan列表
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
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = depplanService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	 /**去修改页面获取数据
	  * V1吴双双 20210531下发提醒
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goXiafa")
	@ResponseBody
	public Object goXiafa() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData Newpd = new PageData();
		pd = this.getPageData();
		depplanService.goXiafa(pd);
		List<PageData> varList = depplanService.listAll(pd);
		try{
		int  b = 0;
		for(int a=0;a<varList.size();a++){
			String FTYPE =  varList.get(a).get("FTYPE").toString();
			if(FTYPE.equals("已下发") || FTYPE == "已下发"){
				b++;
			}
		}
		Newpd.put("PRO_PLAN_ID", pd.get("PRO_PLAN_ID"));
		if(b >= 0 && b<varList.size()){
			Newpd.put("ETYPE", "部分下发");
			pro_planService.updateType(Newpd);
		}else{
			Newpd.put("ETYPE", "已下发");
			pro_planService.updateType(Newpd);
		}
		map.put("pd", pd);
		
		
		//发送阶段负责人任务提醒
		PageData findById = depplanService.findById(pd);
		String PPROJECT_MANAGER = findById.getString("FMANAGER");//根据key值，获取接收人
		if (StringUtils.isEmpty(PPROJECT_MANAGER)) { //若PPROJECT_MANAGER为空，则找不到接收人
			map.put("result", "error");
			return map;
		}
		PageData staffParam = new PageData();
		staffParam.put("FNAME", PPROJECT_MANAGER);

		PageData staffInfo = StaffService.getStaffId(staffParam);//根据接收人，获取英文名称
		if(null == staffInfo){ //若staffInfo为空，则找不到接收人
			map.put("result", "error");
			return map;
		}
		PageData pdNotice = new PageData();
		
		// 跳转页面
	    pdNotice.put("AccessURL", "../../../views/projectManager/depplan/pro_plan_list.html");// 
		
		pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
		pdNotice.put("ReadPeople", ",");// 已读人默认空
		pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
		pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
		pdNotice.put("TType", "消息推送");// 消息类型
		pdNotice.put("FContent",
				 Jurisdiction.getName() + "下发了" + findById.getString("FHTNAME") + "，请注意查看");// 消息正文
		pdNotice.put("FTitle", "阶段下发");// 消息标题
		pdNotice.put("LinkIf", "no");// 是否跳转页面
		pdNotice.put("DataSources", "阶段下发");// 数据来源
		pdNotice.put("ReceivingAuthority", "," +  staffInfo.getString("USER_ID") + ",");// 接收人
		pdNotice.put("Report_Key", "changeDrawingBOM");
		pdNotice.put("Report_Value", "");
		noticeService.save(pdNotice);
		
		
		}catch(Exception e){
			errInfo = "defeat";
		}finally{
			map.put("result", errInfo);
		}
		return map;
	}	
	/**超负荷消息
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/overList")
	@ResponseBody
	public Object overList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "fail";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = staffplanService.listAll(pd);
		String CONTENTX="";
		for(int z=0;z<varList.size();z++){
			String CONTENT="";
			PageData pdz=varList.get(z);
			PageData pdNUM=staffplanService.getNUM(pdz);//获取超负荷数量
			if(pdNUM !=null) {	
				errInfo = "success";
				CONTENT+="责任人"+pdNUM.getString("STAFFPLAN_MANAGER")+pdNUM.getString("FSTART")+"至"+
				pdNUM.getString("FEND")+"日已存在"+pdNUM.get("FTASKNUM").toString()+"个任务，分别是：<br>";
				List<PageData> varListx = staffplanService.listTest(pdz);
				for(int i=0;i<varListx.size();i++) {
					PageData pdx=varListx.get(i);
					CONTENT+=""+(i+1)+"、"+pdx.getString("FTASK")+"，<br>";
				}
				CONTENT+="剩余可用工时"+pdNUM.get("FDIFFREMAIN").toString()+"天，您发布的任务计划工时为"+pdNUM.get("FPANDAY").toString()+"天；<br><br>";
				CONTENTX+=CONTENT;
			}else {
				
			}
		}
		CONTENTX+="是否要继续派发任务？";
		map.put("CONTENT", CONTENTX);
		map.put("result", errInfo);
		return map;
	}	
	 /**下发前验证
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/testXF")
	@ResponseBody
	public Object testXF() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = staffplanService.listAll(pd);
		for(int z=0;z<varList.size();z++){
			PageData pdz=varList.get(z);
			PageData pdNUM=staffplanService.getNUM(pdz);//获取超负荷数量
			if(pdNUM !=null) {	
				errInfo = "fail";
				break;
			}else {
				
			}
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	@RequestMapping(value="/goXiafaAll")
	@ResponseBody
	public Object goXiafaAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData Newpd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = staffplanService.listAll(pd);
		System.out.println(varList.size());
			int i = 0;
		for(int a=0;a<varList.size();a++){
			String STAFFPLAN_NAME = varList.get(a).get("STAFFPLAN_NAME").toString();
			String PLAN_START_TIME = varList.get(a).get("PLAN_START_TIME").toString();
			String PLAN_END_TIME = varList.get(a).get("PLAN_END_TIME").toString();
			String PLAN_HOURS = varList.get(a).get("PLAN_HOURS").toString();
			String IS_OUTPUT = varList.get(a).get("IS_OUTPUT").toString();
			String STAFFPLAN_MANAGER = varList.get(a).get("STAFFPLAN_MANAGER").toString();
			String OUT_BIANMA = varList.get(a).get("OUT_BIANMA").toString();
			String FTYPE = varList.get(a).get("FTYPE").toString();
			if(STAFFPLAN_NAME.equals("") || STAFFPLAN_NAME == ""){
				i++;
			}
			if(PLAN_START_TIME.equals("") || PLAN_START_TIME == ""){
				i++;
			}
			if(PLAN_END_TIME.equals("") || PLAN_END_TIME == ""){
				i++;
			}
			if(PLAN_HOURS.equals("") || PLAN_HOURS == ""){
				i++;
			}
			if(IS_OUTPUT.equals("") || IS_OUTPUT == ""){
				i++;
			}
			if(STAFFPLAN_MANAGER.equals("") || STAFFPLAN_MANAGER == ""){
				i++;
			}
			/*if(OUT_BIANMA.equals("") || OUT_BIANMA == ""){
				i++;
			}*/
			
		}
		
		if(i <= 0 && varList.size() >0){
			pd.put("ETYPE", "已下发");
			depplanService.updateType(pd);
			staffplanService.updateType(pd);
			for(int b=0;b<varList.size();b++){
				PageData pdStaff=varList.get(b);
				//向任务表插数据
				PageData pdPlan=planchangeService.getStaffPlan(pdStaff);
				PageData pdSave=new PageData();
				pdSave.put("PROJECTTASK_ID", this.get32UUID());	//主键
				pdSave.put("PTLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
				pdSave.put("PTLAST_MODIFIER", Jurisdiction.getName());
				pdSave.put("PTCREATE_TIME", Tools.date2Str(new Date()));
				pdSave.put("PTCREATOR", Jurisdiction.getName());
				pdSave.put("PTPROJECT_ID", pdPlan.getString("PROJECT_ID"));
				pdSave.put("PTEQUIPMENT_ID", pdPlan.getString("EQUIPMENT_ID"));
				pdSave.put("PTSTAFFPLAN_ID", pdPlan.getString("STAFFPLAN_ID"));
				pdSave.put("PTPRINCIPAL", pdPlan.getString("STAFFPLAN_MANAGER"));
				pdSave.put("PTPLANSTARTDATE", pdPlan.getString("PLAN_START_TIME"));
				pdSave.put("PTPLANENDDATE", pdPlan.getString("PLAN_END_TIME"));
				pdSave.put("PTPHASENAME", pdPlan.getString("FHTNAME"));
				pdSave.put("PTACTIVITYNAME", pdPlan.getString("STAFFPLAN_NAME"));
				pdSave.put("PTTASK_NAME", pdPlan.getString("STAFFPLAN_NAME"));
				pdSave.put("PTORDER", pdPlan.get("PX").toString());
			
				pdSave.put("PTPROJECT_PRINCIPAL", pdPlan.getString("EPROJECT_PRINCIPAL"));//设备负责人
				pdSave.put("PTISOUT", pdPlan.getString("IS_OUTPUT"));
				pdSave.put("PTOUTCODE", pdPlan.getString("OUT_BIANMA"));
				pdSave.put("PTPLAN_HOUR",pdPlan.get("PLAN_HOURS").toString());
				pdSave.put("PTFINISHPROGRESS", "0");
				pdSave.put("PTACTUAL_HOUR", "0");
				pdSave.put("PTRUNSTATE", "新建");
				pdSave.put("CURRENT_PLANNED_STATE", "");
				pdSave.put("PTVISIBLE", "1");
				pdSave.put("PTDEPT",pdPlan.getString("DEPARTMENT_ID"));
				projecttaskService.save(pdSave);
				
				
				//发送站内信
				PageData pdU=new PageData();
				pdU.put("NAME",pdPlan.getString("STAFFPLAN_MANAGER"));
				PageData pdUser = planchangeService.findUser(pdU);//根据姓名查用户表
				PageData pdMsg=new PageData();
				pdMsg.put("SANME_ID", this.get32UUID());					//共同ID
				pdMsg.put("SEND_TIME", DateUtil.getTime());				//发送时间
				pdMsg.put("FHSMS_ID", this.get32UUID());					//主键1
				pdMsg.put("TYPE", "1");									//类型1：收信
				pdMsg.put("STATUS", "2");								//已读状态：未读2
				pdMsg.put("FSOURCE", "新活动任务提醒");						//消息类型
				pdMsg.put("FRELATE_ID", pdSave.getString("PROJECTTASK_ID"));	//关联ID
				pdMsg.put("CONTENT", "1");									//类型1：收信
				pdMsg.put("TO_USERNAME", Jurisdiction.getUsername());	//发信人
				if(pdUser != null) {
					pdMsg.put("FROM_USERNAME",pdUser.get("USERNAME"));	//收信人
				}	
				pdMsg.put("CONTENT", "您有新的活动任务已下发，任务信息："+pdPlan.getString("PPROJECT_NAME")+"("+pdPlan.getString("PPROJECT_CODE")+")项目"+pdPlan.getString("EEQM_NAME")+"("+pdPlan.getString("EEQM_NO")+")设备"+pdPlan.getString("FHTNAME")+"阶段"+pdPlan.getString("STAFFPLAN_NAME")+"任务，请到任务列表查看详情");	//消息内容
				fhsmsService.save(pdMsg);									//存入发信
			}
		}else{
			errInfo = "exception";
		}
			map.put("result", errInfo);
		return map;
	}	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("depplan:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			depplanService.deleteAll(ArrayDATA_IDS);
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
		titles.add("计划明细ID");	//1
		titles.add("执行状态");	//2
		titles.add("阶段负责人");	//3
		titles.add("计划开始时间");	//4
		titles.add("计划结束时间");	//5
		titles.add("计划工时");	//6
		titles.add("是否下发");	//7
		titles.add("预留1");	//8
		titles.add("预留2");	//9
		titles.add("预留3");	//10
		titles.add("预留4");	//11
		titles.add("YL5");	//12
		titles.add("删除");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = depplanService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PHASETEMPLATE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("RUNTYPE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FMANAGER"));	    //3
			vpd.put("var4", varOList.get(i).getString("PLAN_START_TIME"));	    //4
			vpd.put("var5", varOList.get(i).getString("PLAN_END_TIME"));	    //5
			vpd.put("var6", varOList.get(i).getString("PLAN_HOURS"));	    //6
			vpd.put("var7", varOList.get(i).getString("FTYPE"));	    //7
			vpd.put("var8", varOList.get(i).getString("YL1"));	    //8
			vpd.put("var9", varOList.get(i).getString("YL2"));	    //9
			vpd.put("var10", varOList.get(i).getString("YL3"));	    //10
			vpd.put("var11", varOList.get(i).getString("YL4"));	    //11
			vpd.put("var12", varOList.get(i).getString("YL5"));	    //12
			vpd.put("var13", varOList.get(i).getString("VISIABLE"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**项目计划部门反馈列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listFeedback")
	@ResponseBody
	public Object listFeedback(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//项目号
		String KEYWORDS2 = pd.getString("KEYWORDS2");						//项目名称
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//设备号
		String KEYWORDS4 = pd.getString("KEYWORDS4");						//设备名称
		String KEYTYPE = pd.getString("KEYTYPE");						//按钮状态
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		if(Tools.notEmpty(KEYWORDS2))pd.put("KEYWORDS2", KEYWORDS2.trim());
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		if(Tools.notEmpty(KEYWORDS4))pd.put("KEYWORDS4", KEYWORDS4.trim());
		if(Tools.notEmpty(KEYTYPE))pd.put("KEYTYPE", KEYTYPE.trim());
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("DEPARTMENT_ID", pdDept.get("DEPARTMENT_ID"));
		}
		page.setPd(pd);
		List<PageData>	varList = depplanService.listFeedBack(page);	//列出DepPlan列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**项目计划部门反馈列表-excel导出
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/excelFeedBack")
	//@RequiresPermissions("toExcel")
	public ModelAndView excelFeedBack() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("DEPARTMENT_ID", pdDept.get("DEPARTMENT_ID"));
		}
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("序号");		//1
		titles.add("项目编号");	//2
		titles.add("项目名称");	//3
		titles.add("设备号");		//4
		titles.add("设备名称");	//5
		titles.add("所属阶段");	//6
		titles.add("负责部门");	//7
		titles.add("负责人");		//8
		titles.add("计划开始时间");	//9
		titles.add("计划结束时间");	//10
		titles.add("实际开始时间");		//11
		titles.add("实际结束时间");		//12
		titles.add("执行状态");		//13
		dataMap.put("titles", titles);
		List<PageData> varOList = depplanService.excelFeedBack(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", Integer.valueOf(i + 1).toString()); 			// 1
			vpd.put("var2", varOList.get(i).getString("PPROJECT_CODE")); 	// 2
			vpd.put("var3", varOList.get(i).getString("PPROJECT_NAME")); 	// 3
			vpd.put("var4", varOList.get(i).getString("EEQM_NO")); 			// 4
			vpd.put("var5", varOList.get(i).getString("EEQM_NAME")); 		// 5
			vpd.put("var6", varOList.get(i).getString("FHTNAME")); 			// 6
			vpd.put("var7", varOList.get(i).getString("YL1")); 		// 7
			vpd.put("var8", varOList.get(i).getString("FMANAGER")); 		// 8
			vpd.put("var9", varOList.get(i).getString("NEW_PLAN_START_TIME")); 		// 9
			vpd.put("var10", varOList.get(i).getString("NEW_PLAN_END_TIME"));		//10
			vpd.put("var11", varOList.get(i).getString("REAL_SATRT_TIME")); 		// 11
			vpd.put("var12", varOList.get(i).getString("REAL_END_TIME")); 			// 12
			vpd.put("var13", varOList.get(i).getString("RUNTYPE")); 				// 13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**项目设计列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listDepDeisgn")
	@ResponseBody
	public Object listDepDeisgn(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("NAME", Jurisdiction.getName());
		PageData pdDept=depplanService.findDept(pd);
		if(pdDept != null) {
			pd.put("DEPARTMENT_ID", pdDept.get("DEPARTMENT_ID"));
		}
		page.setPd(pd);
		List<PageData>	varList = depplanService.listDepDeisgn(page);	//列出DepPlan列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	//@RequiresPermissions("fromExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//listproblemsService.deleteByMainId(pd);
		if (null != file && !file.isEmpty()) {

	        int realRowCount = 0;//真正有数据的行数
	        //得到工作空间
	        Workbook workbook = null;
	        try {
	            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        int sheetIndex=0;
		        //得到sheet
		        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, sheetIndex);
		        realRowCount = sheet.getPhysicalNumberOfRows();
		        
		        for(int rowNum = 1;rowNum <= realRowCount;rowNum++) {
		        	PageData pdMx=new PageData();
		        	Row row = sheet.getRow(rowNum);
		        	if(ObjectExcelRead.getCellValue(sheet, row, 0).equals("end")) {
	        			break;
	        		}else {
	        			
	        			pdMx.put("DEPPLAN_ID", this.get32UUID());	//主键
	        			pdMx.put("FHTLEVEL", ObjectExcelRead.getCellValue(sheet, row, 0));
	        			pdMx.put("FHTNAME", ObjectExcelRead.getCellValue(sheet, row, 1));
	        			pdMx.put("FMANAGER", ObjectExcelRead.getCellValue(sheet, row, 2));
	        			pdMx.put("NAME", pdMx.getString("FMANAGER"));
	        			PageData pdDept=depplanService.findDept(pdMx);
	        			if(pdDept != null) {
	        				pdMx.put("YL5", pdDept.get("DEPARTMENT_ID"));
	        			}
	        			pdMx.put("VISIABLE", "1");	//删除
	        			pdMx.put("RUNTYPE", "新建");
	        			pdMx.put("FSOURCE", "excel导入");
	        			pdMx.put("FTYPE", "未下发");
	        			pdMx.put("ETYPE", "未下发");
	        			pdMx.put("PLAN_START_TIME", ObjectExcelRead.getCellValue(sheet, row, 3));
	        			pdMx.put("PLAN_END_TIME", ObjectExcelRead.getCellValue(sheet, row, 4));
	        			pdMx.put("NEW_PLAN_START_TIME", ObjectExcelRead.getCellValue(sheet, row, 3));
	        			pdMx.put("NEW_PLAN_END_TIME", ObjectExcelRead.getCellValue(sheet, row, 4));
	        			pdMx.put("NOW_PLAN_TYPE", "");
	        			pdMx.put("PRO_PLAN_ID", pd.get("PRO_PLAN_ID"));
	        			pdMx.put("PLAN_HOURS", ObjectExcelRead.getCellValue(sheet, row, 5));
	        			depplanService.save(pdMx);
	        		}
		        	
		        }
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "stages.xlsx", "stages.xlsx");
	}
}
