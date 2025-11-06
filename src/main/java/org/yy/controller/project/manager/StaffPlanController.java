package org.yy.controller.project.manager;

import java.io.IOException;
import java.util.ArrayList;
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
import org.yy.entity.PageData;
import org.yy.service.momp.epm.phasetemplate.PhaseTemplateService;
import org.yy.service.project.manager.DepPlanService;
import org.yy.service.project.manager.PLANCHANGEService;
import org.yy.service.project.manager.PROJECTTASKService;
import org.yy.service.project.manager.StaffPlanService;
import org.yy.service.system.FhsmsService;

/** 
 * 说明：部门人员计划
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/staffplan")
public class StaffPlanController extends BaseController {
	
	@Autowired
	private StaffPlanService staffplanService;
	@Autowired
	private DepPlanService depplanService;
	@Autowired
	private PhaseTemplateService phasetemplateService;
	@Autowired
	private PLANCHANGEService planchangeService;
	@Autowired
	private PROJECTTASKService projecttaskService;
	@Autowired
	private FhsmsService fhsmsService;
	/**保存
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
		pd.put("STAFFPLAN_ID", this.get32UUID());	//主键
		PageData pdOrder =staffplanService.getOrder(pd);//获得最大编号
		int FORDER=1;
		if(pdOrder != null) {
			FORDER=Integer.parseInt(pdOrder.get("FORDER").toString())+1;
		}

		pd.put("PX", FORDER);
		pd.put("VISIABLE", "1");	//删除
		pd.put("STAFFPLAN_TYPE", "新建");
		pd.put("FSOURCE", "手动创建");
		pd.put("FTYPE", "未下发");
		pd.put("STAFFPLAN_NAME", "");
		pd.put("PLAN_START_TIME", "");
		pd.put("PLAN_END_TIME", "");
		pd.put("PLAN_HOURS", "");
		pd.put("IS_OUTPUT", "");
		pd.put("STAFFPLAN_MANAGER", "");
		pd.put("OUT_BIANMA", "");
		pd.put("NOW_PLAN_TYPE", "");
		pd.put("NEW_PLAN_START_TIME", "");
		pd.put("NEW_PLAN_END_TIME", "");
		pd.put("YL3", "有");
		staffplanService.save(pd);
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
		List<PageData> varList = phasetemplateService.listAll(pd);
		for (PageData pd1 : varList) {
			pd.put("STAFFPLAN_NAME", pd1.get("FHTNAME"));
			pd.put("YL1", pd1.get("PHASETEMPLATE_ID"));
			PageData pdOrder =staffplanService.getOrder(pd);//获得最大编号
			int FORDER=1;
			if(pdOrder != null) {
				FORDER=Integer.parseInt(pdOrder.get("FORDER").toString())+1;
			}

			pd.put("PX", FORDER);
			//pd.put("PX", pd1.get("FHTLEVEL"));
			pd.put("STAFFPLAN_ID", this.get32UUID());	//主键
			pd.put("VISIABLE", "1");	//删除
			pd.put("STAFFPLAN_TYPE", "新建");
			pd.put("FSOURCE", "模板生成");
			pd.put("FTYPE", "未下发");
			pd.put("PLAN_START_TIME", "");
			pd.put("PLAN_END_TIME", "");
			pd.put("PLAN_HOURS", "");
			pd.put("IS_OUTPUT", "");
			pd.put("STAFFPLAN_MANAGER", "");
			pd.put("OUT_BIANMA", "");
			pd.put("NOW_PLAN_TYPE", "");
			pd.put("NEW_PLAN_START_TIME", "");
			pd.put("NEW_PLAN_END_TIME", "");
			pd.put("YL3", "有");
			staffplanService.save(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
		staffplanService.delete(pd);
		 errInfo = "success";
		}catch(Exception e){
			 errInfo = "defeate";
		}
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
		PageData oldpd = new PageData();
		PageData Timepd = new PageData();
		pd = this.getPageData();
		oldpd = staffplanService.findById(pd);
		pd.put("STAFFPLAN_TYPE", oldpd.get("STAFFPLAN_TYPE"));
		pd.put("FSOURCE", oldpd.get("FSOURCE"));
		pd.put("FTYPE", oldpd.get("FTYPE"));
		pd.put("STAFFPLAN_TYPE", oldpd.get("STAFFPLAN_TYPE"));
		pd.put("DEPPLAN_ID", oldpd.get("DEPPLAN_ID"));
		staffplanService.edit(pd);
		
		String PLAN_START_TIME = staffplanService.getStartTime(pd).get("PLAN_START_TIME").toString();
		String PLAN_END_TIME = staffplanService.getEndTime(pd).get("PLAN_END_TIME").toString();
		
		/*String NEW_PLAN_START_TIME = staffplanService.getStartTime(pd).get("PLAN_START_TIME").toString();
		String NEW_PLAN_END_TIME = staffplanService.getEndTime(pd).get("PLAN_END_TIME").toString();
		Timepd.put("PLAN_START_TIME", PLAN_START_TIME);
		Timepd.put("PLAN_END_TIME", PLAN_END_TIME);
		
		Timepd.put("NEW_PLAN_START_TIME", NEW_PLAN_START_TIME);
		Timepd.put("NEW_PLAN_END_TIME", NEW_PLAN_END_TIME);
		Timepd.put("DEPPLAN_ID", oldpd.get("DEPPLAN_ID"));
		depplanService.updateTime(Timepd);*/
		
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
		page.setPd(pd);
		List<PageData>	varList = staffplanService.list(page);	//列出StaffPlan列表
		map.put("varList", varList);
		map.put("page", page);
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
		List<PageData>	varList = staffplanService.listx(pd);	//列出StaffPlan列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("staffplan:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = staffplanService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
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
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = staffplanService.findById(pd);	//根据ID读取
			PageData pdNUM=staffplanService.getNUM(pd);//获取超负荷数量
			String CONTENT="责任人"+pdNUM.getString("STAFFPLAN_MANAGER")+pdNUM.getString("FSTART")+"至"+
					pdNUM.getString("FEND")+"日已存在"+pdNUM.get("FTASKNUM").toString()+"个任务，分别是：<br>";
			if(pdNUM !=null) {
				List<PageData> varList = staffplanService.listTest(pd);
				for(int i=0;i<varList.size();i++) {
					PageData pdx=varList.get(i);
					CONTENT+=""+(i+1)+"、"+pdx.getString("FTASK")+"，<br>";
				}
			}else {
				errInfo = "fail";
			}
			CONTENT+="剩余可用工时"+pdNUM.get("FDIFFREMAIN").toString()+"天，您发布的任务计划工时为"+pdNUM.get("FPANDAY").toString()+"天，是否要继续派发任务？";
			map.put("CONTENT", CONTENT);
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
			pd = staffplanService.findById(pd);	//根据ID读取
			PageData pdNUM=staffplanService.getNUM(pd);//获取超负荷数量
			if(pdNUM !=null) {	
				errInfo = "fail";
			}else {
			}
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}	
	@RequestMapping(value="/goXiafa")
	@ResponseBody
	public Object goXiafa() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData Newpd = new PageData();
		pd = this.getPageData();
		staffplanService.goXiafa(pd);
		List<PageData> varList = staffplanService.listAll(pd);
		try{
		int  b = 0;
		for(int a=0;a<varList.size();a++){
			String FTYPE =  varList.get(a).get("FTYPE").toString();
			if(FTYPE.equals("已下发") || FTYPE == "已下发"){
				b++;
			}
		}
		Newpd.put("DEPPLAN_ID", pd.get("DEPPLAN_ID"));
		if(b >= 0 && b<varList.size()){
			Newpd.put("ETYPE", "部分下发");
			depplanService.updateType(Newpd);
		}else{
			Newpd.put("ETYPE", "已下发");
			depplanService.updateType(Newpd);
		}
		//向任务表插数据
		PageData pdPlan=planchangeService.getStaffPlan(pd);
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
		pd.put("CURRENT_PLANNED_STATE", "");
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
		pdMsg.put("CONTENT", "您有新的活动任务已下发，任务信息："+pdPlan.getString("PPROJECT_NAME")+"("+pdPlan.getString("PPROJECT_CODE")+")项目"+pdPlan.getString("EEQM_NAME")+"("+pdPlan.getString("EEQM_NO")+")设备"+pdPlan.getString("FHTNAME")+"阶段"+pdPlan.getString("STAFFPLAN_NAME")+"任务，请到任务列表查看详情");	//消息内容
		pdMsg.put("FRELATE_ID", pdSave.getString("PROJECTTASK_ID"));	//关联ID
		pdMsg.put("TO_USERNAME", Jurisdiction.getUsername());	//发信人
		if(pdUser != null) {
			pdMsg.put("FROM_USERNAME",pdUser.get("USERNAME"));	//收信人
		}	
		fhsmsService.save(pdMsg);									//存入发信
		map.put("pd", pd);
		}catch(Exception e){
			errInfo = "defeat";
		}finally{
			map.put("result", errInfo);
		}
		return map;
	}	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			staffplanService.deleteAllx(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	@RequestMapping(value="/getPlanTime")
	@ResponseBody
	public Object getPlanTime() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		pd=depplanService.getPlanTime(pd);
		map.put("pd", pd);
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
		titles.add("部门计划ID");	//1
		titles.add("活动名称");	//2
		titles.add("执行状态");	//3
		titles.add("排序");	//4
		titles.add("活动负责人");	//5
		titles.add("是否有输出物");	//6
		titles.add("计划开始时间");	//7
		titles.add("计划结束时间");	//8
		titles.add("计划工时");	//9
		titles.add("出厂编码");	//10
		titles.add("来源");	//11
		titles.add("状态");	//12
		titles.add("删除");	//13
		titles.add("预留1");	//14
		titles.add("预留");	//15
		titles.add("预留");	//16
		titles.add("预留");	//17
		titles.add("预留");	//18
		dataMap.put("titles", titles);
		List<PageData> varOList = staffplanService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("DEPPLAN_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("STAFFPLAN_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("STAFFPLAN_TYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("PX"));	    //4
			vpd.put("var5", varOList.get(i).getString("STAFFPLAN_MANAGER"));	    //5
			vpd.put("var6", varOList.get(i).getString("IS_OUTPUT"));	    //6
			vpd.put("var7", varOList.get(i).getString("PLAN_START_TIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("PLAN_END_TIME"));	    //8
			vpd.put("var9", varOList.get(i).getString("PLAN_HOURS"));	    //9
			vpd.put("var10", varOList.get(i).getString("OUT_BIANMA"));	    //10
			vpd.put("var11", varOList.get(i).getString("FSOURCE"));	    //11
			vpd.put("var12", varOList.get(i).getString("FTYPE"));	    //12
			vpd.put("var13", varOList.get(i).getString("VISIABLE"));	    //13
			vpd.put("var14", varOList.get(i).getString("YL1"));	    //14
			vpd.put("var15", varOList.get(i).getString("YL2"));	    //15
			vpd.put("var16", varOList.get(i).getString("YL3"));	    //16
			vpd.put("var17", varOList.get(i).getString("YL4"));	    //17
			vpd.put("var18", varOList.get(i).getString("YL5"));	    //18
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	 /**获得人员超负荷详情
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/getOverList")
		@ResponseBody
		public Object getOverList() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = staffplanService.findById(pd);	//根据ID读取
			List<PageData> varList = staffplanService.getOverList(pd);	
			map.put("varList", varList);
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
			        
			        for(int rowNum = 3;rowNum <= realRowCount;rowNum++) {
			        	PageData pdMx=new PageData();
			        	Row row = sheet.getRow(rowNum);
			        	if(ObjectExcelRead.getCellValue(sheet, row, 0).equals("end")) {
		        			break;
		        		}else {
		        			
		        			pdMx.put("STAFFPLAN_ID", this.get32UUID());	//主键
		        			pdMx.put("PX",(int)(Double.parseDouble(ObjectExcelRead.getCellValue(sheet, row, 0).toString())));
		        			pdMx.put("YL2", ObjectExcelRead.getCellValue(sheet, row, 1));
		        			pdMx.put("STAFFPLAN_NAME", ObjectExcelRead.getCellValue(sheet, row, 2));
		        			pdMx.put("YL3", ObjectExcelRead.getCellValue(sheet, row, 3));
		        			pdMx.put("STAFFPLAN_MANAGER", ObjectExcelRead.getCellValue(sheet, row, 4));
		        			pdMx.put("IS_OUTPUT", ObjectExcelRead.getCellValue(sheet, row, 5));
		        			pdMx.put("VISIABLE", "1");	//删除
		        			pdMx.put("STAFFPLAN_TYPE", "新建");
		        			pdMx.put("FSOURCE", "excel导入");
		        			pdMx.put("FTYPE", "未下发");
		        			pdMx.put("PLAN_START_TIME", ObjectExcelRead.getCellValue(sheet, row, 6));
		        			pdMx.put("PLAN_END_TIME", ObjectExcelRead.getCellValue(sheet, row, 7));
		        			pdMx.put("NEW_PLAN_START_TIME", ObjectExcelRead.getCellValue(sheet, row, 6));
		        			pdMx.put("NEW_PLAN_END_TIME", ObjectExcelRead.getCellValue(sheet, row, 7));
		        			pdMx.put("DEPPLAN_ID", pd.get("DEPPLAN_ID"));
		        			pdMx.put("NOW_PLAN_TYPE", "");
		        			pdMx.put("OUT_BIANMA", "");
		        			pdMx.put("PRO_PLAN_ID", pd.get("PRO_PLAN_ID"));
		        			pdMx.put("PLAN_HOURS", ObjectExcelRead.getCellValue(sheet, row, 8));
		        			staffplanService.save(pdMx);

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
			FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "tasks.xls", "tasks.xls");
		}
}
