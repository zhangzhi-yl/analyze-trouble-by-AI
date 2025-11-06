package org.yy.controller.yl;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.apache.commons.collections.CollectionUtils;
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
import org.yy.service.km.CodingRulesDetailService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.yl.ProjectTaskIssueCarryoutService;

/** 
 * 说明：计划任务下发执行
 * 作者：YuanYes QQ356703572
 * 时间：2021-02-23
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ProjectTaskIssueCarryout")
public class ProjectTaskIssueCarryoutController extends BaseController {
	
	@Autowired
	private ProjectTaskIssueCarryoutService ProjectTaskIssueCarryoutService;
	@Autowired
	private CodingRulesService codingrulesService;// 编码规则接口
	
	@Autowired
	private CodingRulesDetailService codingRulesDetailService;//编码规则详细接口
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("ProjectTaskIssueCarryout:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ProjectTaskIssueCarryout_ID", this.get32UUID());	//主键
		pd.put("FTaskState", "创建");	//任务状态
		pd.put("FCreator", Jurisdiction.getName());	//创建人
		pd.put("FreservedOne", Jurisdiction.getName());	//发布人
		pd.put("FCrerteTime", Tools.date2Str(new Date()));	//创建时间
		ProjectTaskIssueCarryoutService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("ProjectTaskIssueCarryout:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProjectTaskIssueCarryoutService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("ProjectTaskIssueCarryout:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProjectTaskIssueCarryoutService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	
	/**下发
	 * 计划制定点击下发更新任务状态、下发时间、下发人
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/issue")
	@ResponseBody
	public Object issue() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FTaskState", "下发");//任务状态
		pd.put("FTaskIssuer", Jurisdiction.getName());	//任务发布人
		pd.put("FTaskIssueTime", Tools.date2Str(new Date()));	//任务发布时间
		ProjectTaskIssueCarryoutService.editIssue(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**完成
	 * 执行反馈点击完成更新任务状态、反馈时间、反馈人
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/over")
	@ResponseBody
	public Object over() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FTaskState", "完成");//任务状态
		pd.put("FeedbackPeople", Jurisdiction.getName());	//任务反馈人
		pd.put("FeedbackTime", Tools.date2Str(new Date()));	//任务反馈时间
		ProjectTaskIssueCarryoutService.editOver(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("ProjectTaskIssueCarryout:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = ProjectTaskIssueCarryoutService.list(page);	//列出ProjectTaskIssueCarryout列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**计划制定列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/planlist")
	//@RequiresPermissions("ProjectTaskIssueCarryout:list")
	@ResponseBody
	public Object planlist(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("NAME", Jurisdiction.getName());//当前登录人
		page.setPd(pd);
		List<PageData>	varList = ProjectTaskIssueCarryoutService.planList(page);	//列出计划制定列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**执行反馈列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/carryOutlist")
	//@RequiresPermissions("ProjectTaskIssueCarryout:list")
	@ResponseBody
	public Object carryOutlist(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("NAME", Jurisdiction.getName());//当前登录人
		page.setPd(pd);
		List<PageData>	varList = ProjectTaskIssueCarryoutService.carryOutList(page);	//列出计划制定列表
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
	//@RequiresPermissions("ProjectTaskIssueCarryout:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ProjectTaskIssueCarryoutService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("ProjectTaskIssueCarryout:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ProjectTaskIssueCarryoutService.deleteAll(ArrayDATA_IDS);
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
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());//当前登录人
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("任务来源");	//1
		titles.add("阶段");	//2
		titles.add("任务类型");	//3
		titles.add("任务");	//4
		titles.add("任务描述");	//5
		titles.add("任务状态");	//6
		titles.add("计划开始时间");	//7
		titles.add("计划结束时间");	//8
		titles.add("标准工时");	//9
		titles.add("任务执行人");	//10
		//titles.add("任务发布人");	//11
		//titles.add("任务发布时间");	//12
		titles.add("任务发布人");	//11
		titles.add("任务发布时间");	//12
		titles.add("实际开始时间");	//13
		titles.add("实际结束时间");	//14
		titles.add("实际工时");	//15
		titles.add("反馈描述");	//16
		titles.add("反馈人");	//17
		titles.add("反馈时间");	//18
		
		dataMap.put("titles", titles);
		List<PageData> varOList = ProjectTaskIssueCarryoutService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FTaskSourceName"));	    //1
			vpd.put("var2", varOList.get(i).getString("FStageName"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTaskTypeName"));	    //3
			vpd.put("var4", varOList.get(i).getString("FTask"));	    //4
			vpd.put("var5", varOList.get(i).getString("FTaskDescribe"));	    //5
			vpd.put("var6", varOList.get(i).getString("FTaskState"));	    //6
			vpd.put("var7", varOList.get(i).getString("PlanBeginTime"));	    //7
			vpd.put("var8", varOList.get(i).getString("PlanEndTime"));	    //8
			vpd.put("var9", varOList.get(i).get("NormalHours").toString());	//9
			vpd.put("var10", varOList.get(i).getString("FTaskOperator"));	    //10
			//vpd.put("var11", varOList.get(i).getString("FTaskIssuer"));	    //11
			//vpd.put("var12", varOList.get(i).getString("FTaskIssueTime"));	    //12
			vpd.put("var11", varOList.get(i).getString("FCreator"));	    //11
			vpd.put("var12", varOList.get(i).getString("FCrerteTime"));	    //12
			vpd.put("var13", varOList.get(i).getString("FCatualBeginTime"));	    //13
			vpd.put("var14", varOList.get(i).getString("FCatualEndTime"));	    //14
			vpd.put("var15", varOList.get(i).get("FCatualHour").toString());	//15
			vpd.put("var16", varOList.get(i).getString("FeedbackDescribe"));	    //16
			vpd.put("var17", varOList.get(i).getString("FeedbackPeople"));	    //17
			vpd.put("var18", varOList.get(i).getString("FeedbackTime"));	    //18
			
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	@ResponseBody
	public Object readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file
			) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		int succeedNum=0;
		int failNum=0;
		PageData ProjectTaskIssueCarryoutPd = new PageData();
		if (null != file && !file.isEmpty()) {
	        int realRowCount = 0;//真正有数据的行数
	        String reg="^\\d+$";
	        //得到工作空间
	        Workbook workbook = null;
	        try {
	            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        //得到sheet
	        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, 0);
	        realRowCount = sheet.getPhysicalNumberOfRows();
	        for(int rowNum = 1;rowNum <= realRowCount;rowNum++) {
	        	Row row = sheet.getRow(rowNum);
	            if(ObjectExcelRead.isBlankRow(row)) {	//空行跳过
	            	break;
	            }
	            if(row.getRowNum() == -1) {
	                continue;
	            }else {
	                if(row.getRowNum() == 0) {//第一行表头跳过
	                    continue;
	                }
	            }
	            
	            ProjectTaskIssueCarryoutPd.put("ProjectTaskIssueCarryout_ID", this.get32UUID());//计划任务下发执行ID主键
	            //任务来源
	            if(null!=ObjectExcelRead.getCellValue(sheet, row, 0)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 0))) {
	            	PageData TaskSourcePd = new PageData();
	            	TaskSourcePd.put("NAME", ObjectExcelRead.getCellValue(sheet, row, 0));
	            	TaskSourcePd=ProjectTaskIssueCarryoutService.findByDICTIONARIESId(TaskSourcePd);
	            	if(null!=TaskSourcePd) {
	            		ProjectTaskIssueCarryoutPd.put("FTaskSource", TaskSourcePd.getString("DICTIONARIES_ID"));
	            	}else {
	            		failNum=failNum+1;
	            		continue;
	            	}
	            }else {
            		failNum=failNum+1;
            		continue;
            	}
	            //任务阶段
	            if(null!=ObjectExcelRead.getCellValue(sheet, row, 1)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 1))) {//任务阶段
	            	PageData StagePd = new PageData();
	            	StagePd.put("NAME", ObjectExcelRead.getCellValue(sheet, row, 1));
	            	StagePd=ProjectTaskIssueCarryoutService.findByDICTIONARIESId(StagePd);
	            	if(null!=StagePd) {
	            		ProjectTaskIssueCarryoutPd.put("FStage", StagePd.getString("DICTIONARIES_ID"));
	            	}else {
	            		failNum=failNum+1;
	            		continue;
	            	}
	            }else {
            		failNum=failNum+1;
            		continue;
            	}
	            //任务类型
	            if(null!=ObjectExcelRead.getCellValue(sheet, row, 9)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 9))) {//任务类型
	            	PageData TaskTypePd = new PageData();
	            	TaskTypePd.put("NAME", ObjectExcelRead.getCellValue(sheet, row, 9));
	            	TaskTypePd=ProjectTaskIssueCarryoutService.findByDICTIONARIESId(TaskTypePd);
	            	if(null!=TaskTypePd) {
	            		ProjectTaskIssueCarryoutPd.put("FTaskType", TaskTypePd.getString("DICTIONARIES_ID"));
	            	}else {
	            		ProjectTaskIssueCarryoutPd.put("FTaskType", "正常");
	            	}
	            }else {
	            	ProjectTaskIssueCarryoutPd.put("FTaskType", "正常");
            	}
	            ProjectTaskIssueCarryoutPd.put("FTask", ObjectExcelRead.getCellValue(sheet, row, 2));//任务
	            ProjectTaskIssueCarryoutPd.put("FTaskDescribe", ObjectExcelRead.getCellValue(sheet, row, 3));//任务描述
	            ProjectTaskIssueCarryoutPd.put("FTaskState", "创建");//任务状态
	            ProjectTaskIssueCarryoutPd.put("PlanBeginTime", ObjectExcelRead.getCellValue(sheet, row, 4));//计划开始时间
	            ProjectTaskIssueCarryoutPd.put("PlanEndTime", ObjectExcelRead.getCellValue(sheet, row, 5));//计划结束时间
	            if(ObjectExcelRead.getCellValue(sheet, row, 6).matches(reg)==true) {//判断表格标准工时值是否为数字，不为数字填写0
	            	ProjectTaskIssueCarryoutPd.put("NormalHours", ObjectExcelRead.getCellValue(sheet, row, 6));//标准工时
	 	            }else {
	 	            	ProjectTaskIssueCarryoutPd.put("NormalHours", 0);//标准工时
	 	            }
	            ProjectTaskIssueCarryoutPd.put("FreservedOne", ObjectExcelRead.getCellValue(sheet, row, 7));//任务发布人
	            
	            //任务执行人
	            if(null!=ObjectExcelRead.getCellValue(sheet, row, 8)&&!"".equals(ObjectExcelRead.getCellValue(sheet, row, 8))) {//任务执行人
	            	PageData TaskOperatorPd = new PageData();
	            	TaskOperatorPd.put("NAME", ObjectExcelRead.getCellValue(sheet, row, 8));
	            	TaskOperatorPd=ProjectTaskIssueCarryoutService.findByStaffId(TaskOperatorPd);
	            	if(Integer.parseInt(TaskOperatorPd.get("NUM").toString())>0) {
	            		ProjectTaskIssueCarryoutPd.put("FTaskOperator", ObjectExcelRead.getCellValue(sheet, row, 8));
	            	}else {
	            		failNum=failNum+1;
	            		continue;
	            	}
	            }else {
            		failNum=failNum+1;
            		continue;
            	}
	            PageData pgData = new PageData();
	    		pgData.put("CODINGRULESTYPE", "ProjectTaskIssueCarryout");//编码类型
	    		List<PageData> dataByCodingRulesType = codingrulesService.getDataByCodingRulesType(pgData);
	    		if (CollectionUtils.isEmpty(dataByCodingRulesType)) {
	    			throw new RuntimeException("获取规则号失败，该类型数据不存在");
	    		}
	    		String returnCode = "";
	    		String CODINGRULEID = "";
	    		String ACQUISITIONTIME = "";
	    		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
	    		for (PageData pageData : dataByCodingRulesType) {
	    			// 根据详情类型 生成对应的编号
	    			CODINGRULEID = pageData.getString("CODINGRULES_ID");
	    			String CODINGRULESDETAILID = pageData.getString("CODINGRULESDETAIL_ID");
	    			String TERMOFVALIDITY = pageData.getString("TERMOFVALIDITY");
	    			String DETAILTYPE = pageData.getString("DETAILTYPE");
	    			String FLENGTH = pageData.getString("FLENGTH");
	    			String FFORMAT = pageData.getString("FFORMAT");
	    			String TSTEP = pageData.getString("TSTEP");
	    			String SETTINGVALUE = pageData.getString("SETTINGVALUE");
	    			String RESETPERIOD = pageData.getString("RESETPERIOD");
	    			// 进制问题需要确定
	    			/* String STREAMCODING = pageData.getString("STREAMCODING"); */
	    			ACQUISITIONTIME = pageData.getString("ACQUISITIONTIME");
	    			// 判断是否过期
	    			String termDateTime = TERMOFVALIDITY.substring(12, TERMOFVALIDITY.length());
	    			Date parse = null;
	    			parse = sdFormat.parse(termDateTime);
	    			long time = parse.getTime();
	    			long currentTimeMillis = new Date().getTime();
	    			if (time < currentTimeMillis) {
	    				throw new RuntimeException("该编码规则已过期");
	    			}
	    			// 常量类型直接返回当初的设置值
	    			if ("constant".equals(DETAILTYPE)) {
	    				returnCode += SETTINGVALUE;
	    			}
	    			// 日期类型直接返回当初的设置格式后 格式化时间返回
	    			if ("date".equals(DETAILTYPE)) {
	    				String code = "";
	    				String acqNow = sdFormat.format(new Date());
	    				SimpleDateFormat sdf = new SimpleDateFormat(FFORMAT);
	    				try {
	    					code = sdf.format(sdFormat.parse(ACQUISITIONTIME));
	    				} catch (Exception e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    				if ("everyday".equals(RESETPERIOD)) {
	    					if (!acqNow.equals(ACQUISITIONTIME)) {
	    						code = sdf.format(new Date());
	    					}
	    				}
	    				returnCode += code;
	    			}
	    			// 根据规则类型生成流水号
	    			if ("serial".equals(DETAILTYPE)) {
	    				String acqNow = sdFormat.format(new Date());
	    				// 如果每天重置，日期变更 重新设置位1开始
	    				if ("everyday".equals(RESETPERIOD)) {
	    					if (!acqNow.equals(ACQUISITIONTIME)) {
	    						SETTINGVALUE = "0";
	    					}
	    				}
	    				// 如果不重置，则接着前一天的时间继续编号
	    				else {
	    					acqNow = ACQUISITIONTIME;
	    				}
	    				// 自增 步长
	    				Integer getValueInc = Integer.valueOf(SETTINGVALUE) + Integer.valueOf(TSTEP);
	    				String formatNum = this.formatNum(getValueInc, Integer.valueOf(FLENGTH));

	    				// 回写数据
	    				PageData pgRuleDetail = new PageData();
	    				pgRuleDetail.put("CODINGRULESDETAIL_ID", CODINGRULESDETAILID);
	    				PageData findDetailById = codingRulesDetailService.findById(pgRuleDetail);
	    				findDetailById.put("SETTINGVALUE", formatNum);
	    				codingRulesDetailService.edit(findDetailById);
	    				// 返回結果
	    				returnCode += formatNum;
	    			}
	    		}
	    		// 更新数据
	    		PageData pgRule = new PageData();
	    		pgRule.put("CODINGRULES_ID", CODINGRULEID);
	    		PageData findById = codingrulesService.findById(pgRule);
	    		findById.put("GETVALUE", returnCode);
	    		findById.put("ACQUISITIONTIME", sdFormat.format(new Date()));
	    		codingrulesService.edit(findById);
	    		ProjectTaskIssueCarryoutPd.put("FreservedTwo", returnCode);	//需求编号
	            ProjectTaskIssueCarryoutPd.put("FCreator", Jurisdiction.getName());	//创建人
	            ProjectTaskIssueCarryoutPd.put("FCrerteTime", Tools.date2Str(new Date()));	//创建时间
	            ProjectTaskIssueCarryoutService.save(ProjectTaskIssueCarryoutPd);
	            succeedNum=succeedNum+1;
	        }
		}
		map.put("succeedNum", succeedNum+"");				//返回成功数量结果
		map.put("failNum", failNum+"");				//返回失败数量结果
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "ProjectTaskIssueCarryout.xlsx", "ProjectTaskIssueCarryout.xlsx");
	}
	
	private String formatNum(int input, int FLENGTH) {
		// 大于1000时直接转换成字符串返回
		double pow = Math.pow(10.00, FLENGTH);
		if (input > pow - 1) {
			throw new RuntimeException("生成失败，编码号已经超过了" + FLENGTH + "位");
		}
		return String.format("%0" + FLENGTH + "d", input);
	}
}
