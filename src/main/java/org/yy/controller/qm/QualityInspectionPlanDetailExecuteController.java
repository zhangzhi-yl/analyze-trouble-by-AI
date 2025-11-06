package org.yy.controller.qm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.qm.QualityInspectionPlanDetailExecuteService;
import org.yy.service.qm.QualityInspectionPlanExecuteSampleService;
import org.yy.service.qm.QualityInspectionPlanExecuteService;
import org.yy.service.system.DictionariesService;

/** 
 * 说明：质检任务明细
 * 作者：范贺男
 * 时间：2020-11-14
 */
@Controller
@RequestMapping("/QualityInspectionPlanDetailExecute")
public class QualityInspectionPlanDetailExecuteController extends BaseController {
	
	@Autowired
	private QualityInspectionPlanDetailExecuteService QualityInspectionPlanDetailExecuteService;
	@Autowired
	private QualityInspectionPlanExecuteService QualityInspectionPlanExecuteService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private QualityInspectionPlanExecuteSampleService QualityInspectionPlanExecuteSampleService;
	@Autowired
	private DictionariesService dictionariesService;
	/**保存
	 * @param   质检任务ID   QualityInspectionPlanExecute_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/addDetail")
	//@RequiresPermissions("QualityInspectionPlanDetailExecute:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		JSONArray arr = JSONArray.fromObject(pd.get("QualityInspectionItems_ID"));
		for (int i = 0;i<arr.size();i++) {
			int  SortKey = 0;
			try{
			 SortKey = Integer.parseInt( QualityInspectionPlanDetailExecuteService.getMaxSortKey(pd));//根据质检任务获取明细列表中排序值的最大值
			}catch(Exception e){
				SortKey = 0;
			}
			String str = arr.get(i).toString();
			pd.put("QIItemID",str);
			pd.put("QualityInspectionPlanDetailExecute_ID", this.get32UUID());	//质检任务明细主键
			pd.put("QualityInspectionPlanExecute_ID", pd.get("QualityInspectionPlanExecute_ID"));//质检任务ID
			pd.put("SortKey", SortKey+1);//排序值+1
			pd.put("QIPlanID", "");//质检方案ID，手动新增，无关联质检方案
			pd.put("StandardType","");//标准类型
			pd.put("ToleranceType", "");//允差类型
			pd.put("FRetain", "");//保留
			pd.put("StandardValue", "");//标准值
			pd.put("UpperDeviation","");//上偏差
			pd.put("LowerDeviation", "");//下偏差
			pd.put("FMinimum",0);//最小值
			pd.put("FMaximum",0);//最大值
			pd.put("FUnit", "");//单位
			pd.put("FormulaValue", "");//公式值
			pd.put("FExplanation", "");//备注
			pd.put("BreakdownOfBadReasons", "");//不良原因细分 默认为空，由处理人填写
			pd.put("FCustomer", "");//客户
			pd.put("InspecteKey", "");//检验值
			pd.put("InspectionAndJudgment","");//检验判定
			pd.put("InspectionAndTestTime", "");//检验时间
			pd.put("InspectorID", "");//检验人	
			pd.put("TestResultsDesc", "");//检验结果描述
			pd.put("BadnessLevel", "");//不良等级
			pd.put("FinishOnce", 0);
			QualityInspectionPlanDetailExecuteService.save(pd);
			operationrecordService.add("","质检任务明细","添加",pd.getString("QualityInspectionPlanDetailExecute_ID"),"","");//操作日志
		}
		
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 完成单条质检任务明细
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goFinish")
	@ResponseBody
	public Object goFinish() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pd1 = new PageData();
		pd = this.getPageData();
		QualityInspectionPlanDetailExecuteService.goFinish(pd);
		pd = QualityInspectionPlanDetailExecuteService.findById(pd);
		Integer SortKey = Integer.parseInt(pd.get("SortKey").toString());//获取当前排序值
		Integer SortKeyMax = Integer.parseInt( QualityInspectionPlanDetailExecuteService.getMaxSortKey(pd));//获取最后一条的排序值
		if(SortKey == 1 && SortKey == SortKeyMax){//只有一条质检项的情况
			pd.put("FBeginTime", Tools.date2Str(new Date()));//获取当前时间
			QualityInspectionPlanExecuteService.saveFBeginTime(pd);//反写开始时间
			pd.put("FEndTime", Tools.date2Str(new Date()));//获取当前时间
			QualityInspectionPlanExecuteService.saveFEndTime(pd);//反写结束时间
			pd1 = QualityInspectionPlanExecuteService.getJG(pd);//获取合格的条数
			Integer num = Integer.parseInt(pd1.get("num").toString());
			if(num == 0){//如果没有合格的，反写状态为全部不合格
				pd.put("JudgmentResults", "全部不合格");
			}else if(num < SortKeyMax){//如果合格数小于最大条数，反写状态为部分合格
				pd.put("JudgmentResults", "部分合格");
			}else if(num == SortKeyMax){//合格数等于最大条数，反写状态为全部合格
				pd.put("JudgmentResults", "全部合格");
			}
			QualityInspectionPlanExecuteService.editJG(pd);//修改判定结果
			QualityInspectionPlanExecuteService.editZT(pd);//修改执行状态为已完成
		}else if(SortKey == 1){ //判断当前完成的质检任务明细为第几条，如果为第一条，反写开始时间到主表
			pd.put("FBeginTime", Tools.date2Str(new Date()));//获取当前时间
			QualityInspectionPlanExecuteService.saveFBeginTime(pd);//反写开始时间
		}else if(SortKey == SortKeyMax){//判断当前排序值等于最大排序值，反写结束时间到主表
			pd.put("FEndTime", Tools.date2Str(new Date()));//获取当前时间
			QualityInspectionPlanExecuteService.saveFEndTime(pd);//反写结束时间
			pd1 = QualityInspectionPlanExecuteService.getJG(pd);//获取合格的条数
			Integer num = Integer.parseInt(pd1.get("num").toString());
			if(num == 0){//如果没有合格的，反写状态为全部不合格
				pd.put("JudgmentResults", "全部不合格");
			}else if(num < SortKeyMax){//如果合格数小于最大条数，反写状态为部分合格
				pd.put("JudgmentResults", "部分合格");
			}else if(num == SortKeyMax){//合格数等于最大条数，反写状态为全部合格
				pd.put("JudgmentResults", "全部合格");
			}
			QualityInspectionPlanExecuteService.editJG(pd);//修改判定结果
			QualityInspectionPlanExecuteService.editZT(pd);//修改执行状态为已完成
		} 
		operationrecordService.add("","质检任务明细","完成",pd.getString("QualityInspectionPlanDetailExecute_ID"),"","");//操作日志
		PageData findById = QualityInspectionPlanDetailExecuteService.findById(pd);
		String QualityInspectionPlanExecuteSample_ID = findById.getString("QualityInspectionPlanExecuteSample_ID");
		
		PageData pds = new PageData();
		pds.put("QualityInspectionPlanExecuteSample_ID", QualityInspectionPlanExecuteSample_ID);
		List<PageData> listAll = QualityInspectionPlanDetailExecuteService.listAll(pds);
		Integer successCount = 0;
		for (PageData pageData : listAll) {
			if("合格".equals(pageData.getString("InspectionAndJudgment"))){
				successCount++;
			}
		}
		if(successCount  == listAll.size()){
			PageData Sample = QualityInspectionPlanExecuteSampleService.findById(pds);
			if(null!=Sample){
				Sample.put("QualifiedCount",Double.valueOf(Sample.getString("SampleCount")));
				Sample.put("CompromisedCount", 0);
				Sample.put("DisqualifiedCount", 0);
				QualityInspectionPlanExecuteSampleService.edit(Sample);
			}
		}
		map.put("result", errInfo);
		return map;
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteDetail")
	//@RequiresPermissions("QualityInspectionPlanDetailExecute:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		QualityInspectionPlanDetailExecuteService.delete(pd);
		operationrecordService.add("","质检任务明细","删除",pd.getString("QualityInspectionPlanDetailExecute_ID"),"","");//操作日志
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editDetail")
	//@RequiresPermissions("QualityInspectionPlanDetailExecute:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		QualityInspectionPlanDetailExecuteService.edit(pd);
		operationrecordService.add("","质检任务明细","修改",pd.getString("QualityInspectionPlanDetailExecute_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 质检任务明细执行
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ImplementQI")
	//@RequiresPermissions("QualityInspectionPlanDetailExecute:edit")
	@ResponseBody
	public Object ImplementQI() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdStaff = new PageData();
		pd = this.getPageData();
		/*pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
		pd.put("InspectorID",pdStaff.get("STAFF_ID"));			//检验人
*/		pd.put("InspectionAndTestTime", Tools.date2Str(new Date()));//检验时间
		QualityInspectionPlanDetailExecuteService.ImplementQI(pd);//
		operationrecordService.add("","质检任务明细","执行",pd.getString("QualityInspectionPlanDetailExecute_ID"),"","");//操作日志
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listDetail")
	//@RequiresPermissions("QualityInspectionPlanDetailExecute:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdList = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = QualityInspectionPlanDetailExecuteService.list(page);	//列出QualityInspectionPlanDetailExecute列表
		for(int i = 0;i<varList.size();i++){//循环列表
			pdList = varList.get(i);
			Integer SortKey = Integer.parseInt(pdList.get("SortKey").toString());//获取当前排序值
			String FinishOnce = pdList.get("FinishOnce").toString();//获取当前完成状态
			if(SortKey == 1 && FinishOnce == "未完成"){//判断当前排序为1 并且状态为未完成 
				varList.get(i).put("Temporary", "1");//插入临时标识，此表示为1时 显示质检任务执行和完成按钮
			}else if(SortKey > 1 && FinishOnce == "已完成" ){//判断当前明细非第一条，并且状态为未完成
				//获取上一条的完成状态
				Integer FinishOnceLast = Integer.parseInt(QualityInspectionPlanDetailExecuteService.getLastMx(pdList).get("FinishOnce").toString());
				if(FinishOnceLast == 1){
					varList.get(i).put("Temporary", "1");//插入临时标识，此表示为1时 显示质检任务执行和完成按钮
				}
			}
			if(null!=varList.get(i) && varList.get(i).containsKey("BreakdownOfBadReasons") && null!= varList.get(i).getString("BreakdownOfBadReasons")) {
				String[] sarr=varList.get(i).getString("BreakdownOfBadReasons").split(",yl,");
				List<String> varList1 =  new ArrayList<>();
				for(int j=0;j<sarr.length;j++) {
					PageData temp = new PageData();
					temp.put("DICTIONARIES_ID", sarr[j]);
					PageData spd = dictionariesService.findById(temp);	//根据ID读取
					if(null!=spd && spd.containsKey("NAME")) {
						varList1.add(spd.getString("NAME"));
					}
				}
				varList.get(i).put("BreakdownOfBadReasons", sarr);
				varList.get(i).put("BreakdownOfBadReasonsNAME", varList1);

			}
			if(null!=varList.get(i) && varList.get(i).containsKey("BadnessReasonLevel") && null!= varList.get(i).getString("BadnessReasonLevel")) {
				String[] sarr=varList.get(i).getString("BadnessReasonLevel").split(",yl,");
				List<String> varList1 =  new ArrayList<>();
				for(int j=0;j<sarr.length;j++) {
					PageData temp = new PageData();
					temp.put("DICTIONARIES_ID", sarr[j]);
					PageData spd = dictionariesService.findById(temp);	//根据ID读取
					if(null!=spd && spd.containsKey("NAME")) {
						varList1.add(spd.getString("NAME"));
					}
				}
				varList.get(i).put("BadnessReasonLevel", sarr);
				varList.get(i).put("BadnessReasonLevelNAME", varList1);

			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 根据明细ID获取不良原因
	 */
	@RequestMapping(value="/getBadnessReason")
	@ResponseBody
	public Object getBadnessReason() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = QualityInspectionPlanDetailExecuteService.findById(pd);	//根据ID读取
		String[] sarr = null;//不良原因
		if(null != pd.get("BreakdownOfBadReasons") ){
			 sarr=pd.getString("BreakdownOfBadReasons").split(",yl,");
		}
		List<String> varList1 =  new ArrayList<>();
		for(int j=0;j<sarr.length;j++) {
			PageData temp = new PageData();
			temp.put("DICTIONARIES_ID", sarr[j]);
			PageData spd = dictionariesService.findById(temp);	//根据ID读取
			if(null!=spd && spd.containsKey("NAME")) {
				varList1.add(spd.getString("DICTIONARIES_ID"));
			}
		}
		List<PageData> varList =  new ArrayList<>();
		for(int a = 0;a<varList1.size();a++){
			PageData pd1 = new PageData();
			PageData temp1 = new PageData();
			temp1.put("DICTIONARIES_ID", varList1.get(a));
			PageData spd1 = dictionariesService.findById(temp1);	//根据ID读取
			varList.add(spd1);
		}
		
		String[] sarrMx = null;//不良原因细分
		if(null != pd.get("BadnessReasonLevel") ){
			 sarrMx=pd.getString("BadnessReasonLevel").split(",yl,");
		}
		List<String> varList2 =  new ArrayList<>();
		for(int j=0;j<sarrMx.length;j++) {
			PageData temp = new PageData();
			temp.put("DICTIONARIES_ID", sarrMx[j]);
			PageData spd = dictionariesService.findById(temp);	//根据ID读取
			if(null!=spd && spd.containsKey("NAME")) {
				varList2.add(spd.getString("DICTIONARIES_ID"));
			}
		}
		List<PageData> varListMx =  new ArrayList<>();
		for(int a = 0;a<varList2.size();a++){
			PageData pd1 = new PageData();
			PageData temp1 = new PageData();
			temp1.put("DICTIONARIES_ID", varList2.get(a));
			PageData spd1 = dictionariesService.findById(temp1);	//根据ID读取
			varListMx.add(spd1);
		}
		map.put("varList", varList);
		map.put("varList222", varListMx);
		map.put("BreakdownOfBadReasons", pd.get("BreakdownOfBadReasons"));
		map.put("result", errInfo);
		return map;
	}
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
//	@RequiresPermissions("QualityInspectionPlanDetailExecute:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = QualityInspectionPlanDetailExecuteService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("QualityInspectionPlanDetailExecute:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			QualityInspectionPlanDetailExecuteService.deleteAll(ArrayDATA_IDS);
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
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("质检任务明细ID");	//1
		titles.add("排序值");	//2
		titles.add("质检方案ID");	//3
		titles.add("质检项ID");	//4
		titles.add("标准类型");	//5
		titles.add("允差类型");	//6
		titles.add("保留");	//7
		titles.add("标准值");	//8
		titles.add("上偏差");	//9
		titles.add("下偏差");	//10
		titles.add("最小值");	//11
		titles.add("最大值");	//12
		titles.add("单位");	//13
		titles.add("不良原因细分");	//14
		titles.add("客户");	//15
		titles.add("公式值");	//16
		titles.add("备注");	//17
		titles.add("检验值");	//18
		titles.add("检验判定");	//19
		titles.add("判定类型");	//20
		titles.add("检试验时间");	//21
		titles.add("检验人");	//22
		titles.add("检验结果描述");	//23
		dataMap.put("titles", titles);
		List<PageData> varOList = QualityInspectionPlanDetailExecuteService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("QualityInspectionPlanDetailExecute_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("SortKey"));	    //2
			vpd.put("var3", varOList.get(i).getString("QIPlanID"));	    //3
			vpd.put("var4", varOList.get(i).getString("QIItemID"));	    //4
			vpd.put("var5", varOList.get(i).getString("StandardType"));	    //5
			vpd.put("var6", varOList.get(i).getString("ToleranceType"));	    //6
			vpd.put("var7", varOList.get(i).getString("FRetain"));	    //7
			vpd.put("var8", varOList.get(i).getString("StandardValue"));	    //8
			vpd.put("var9", varOList.get(i).getString("UpperDeviation"));	    //9
			vpd.put("var10", varOList.get(i).getString("LowerDeviation"));	    //10
			vpd.put("var11", varOList.get(i).get("FMinimum").toString());	//11
			vpd.put("var12", varOList.get(i).get("FMaximum").toString());	//12
			vpd.put("var13", varOList.get(i).getString("FUnit"));	    //13
			vpd.put("var14", varOList.get(i).getString("BreakdownOfBadReasons"));	    //14
			vpd.put("var15", varOList.get(i).getString("FCustomer"));	    //15
			vpd.put("var16", varOList.get(i).getString("FormulaValue"));	    //16
			vpd.put("var17", varOList.get(i).getString("FExplanation"));	    //17
			vpd.put("var18", varOList.get(i).getString("InspecteKey"));	    //18
			vpd.put("var19", varOList.get(i).getString("InspectionAndJudgment"));	    //19
			vpd.put("var20", varOList.get(i).getString("JudgingType"));	    //20
			vpd.put("var21", varOList.get(i).getString("InspectionAndTestTime"));	    //21
			vpd.put("var22", varOList.get(i).getString("InspectorID"));	    //22
			vpd.put("var23", varOList.get(i).getString("TestResultsDesc"));	    //23
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	//自动判断填写的检验结果是否合格
		@RequestMapping(value="/AutoJudeg")
		@ResponseBody
		public Object AutoJudeg() throws Exception{
			Map<String, Object> map = new HashMap<String,Object>();
			PageData pd = new PageData();
			pd=this.getPageData();
			String result = "";
			BigDecimal InspecteKey = new BigDecimal(pd.get("InspecteKey").toString());//前台输入的检验结果
			pd = QualityInspectionPlanDetailExecuteService.findById(pd);
			String StandardType = pd.get("StandardType").toString();//获取检验类型
			if(StandardType == "区间" || StandardType.equals("区间")){
				BigDecimal FMinimum = new BigDecimal(pd.get("FMinimum").toString());//最小值
				BigDecimal FMaximum = new BigDecimal(pd.get("FMaximum").toString());//最大值
				int a = InspecteKey.compareTo(FMinimum);//与最小值比较 1 大于 0 等于 -1 小于
				int b = InspecteKey.compareTo(FMaximum);//与最大值比较
				if(a > -1 && b < 1){
					result = "合格";
				}
			}else if(StandardType == "允差" || StandardType.equals("允差")){
				BigDecimal StandardValue = new BigDecimal(pd.get("StandardValue").toString());//标准值
				BigDecimal UpperDeviation = new BigDecimal(pd.get("UpperDeviation").toString());//上偏差
				BigDecimal LowerDeviation = new BigDecimal(pd.get("LowerDeviation").toString());//下偏差
				int a = InspecteKey.compareTo(StandardValue.add(UpperDeviation));//标准值+上偏差
				int b = InspecteKey.compareTo(StandardValue.subtract(LowerDeviation));//标准值-下偏差
				if(a < 1 && b > -1){
					result = "合格";
				}
			}else if(StandardType == ">" || StandardType.equals(">")){
				BigDecimal StandardValue = new BigDecimal(pd.get("StandardValue").toString());//标准值
				int a = InspecteKey.compareTo(StandardValue);
				if(a == 1){
					result = "合格";
				}
			}else if(StandardType == "<" || StandardType.equals("<")){
				BigDecimal StandardValue = new BigDecimal(pd.get("StandardValue").toString());//标准值
				int a = InspecteKey.compareTo(StandardValue);
				if(a == -1){
					result = "合格";
				}
			}else if(StandardType == "=" || StandardType.equals("=")){
				BigDecimal StandardValue = new BigDecimal(pd.get("StandardValue").toString());//标准值
				int a = InspecteKey.compareTo(StandardValue);
				if(a == 0){
					result = "合格";
				}
			}else if(StandardType == "≥" || StandardType.equals("≥")){
				BigDecimal StandardValue = new BigDecimal(pd.get("StandardValue").toString());//标准值
				int a = InspecteKey.compareTo(StandardValue);
				if(a > -1){
					result = "合格";
				}
			}else if(StandardType == "≤" || StandardType.equals("≤")){
				BigDecimal StandardValue = new BigDecimal(pd.get("StandardValue").toString());//标准值
				int a = InspecteKey.compareTo(StandardValue);
				if(a < 1){
					result = "合格";
				}
			}
			map.put("result", result);
			return map;
		}
		
		//手动排序，向上一条
		@RequestMapping(value="/upPX")
		@ResponseBody
		public Object upPXx() throws Exception{
			Map<String, Object> map = new HashMap<String,Object>();
			String result = "";
			PageData pd = new PageData();
			pd=this.getPageData();
			List<PageData> varList = QualityInspectionPlanDetailExecuteService.listAll(pd);//获取质检项列表
			for (int i = 0; i < varList.size(); i++) {
				PageData pd1 = varList.get(i);
				if(Integer.parseInt(pd.get("SortKey").toString()) != 1){//如果为第一条，不能再向上排序返回error
					if(varList.get(i).get("SortKey") == pd.get("SortKey") || varList.get(i).get("SortKey").equals(pd.get("SortKey"))){//判断当前条为要修改的条
						pd.put("QualityInspectionPlanDetailExecute_ID", varList.get(i).get("QualityInspectionPlanDetailExecute_ID"));//当前条目的ID
						pd.put("SortKey", varList.get(i).get("SortKey"));//改之前的排序值，作为筛选条件
						pd.put("NewSortKey", Integer.parseInt(pd.get("SortKey").toString())-1);//新排序值=老排序值-1 
						QualityInspectionPlanDetailExecuteService.editSortKey(pd);//修改排序值
						
						pd.put("QualityInspectionPlanDetailExecute_ID", varList.get(i-1).get("QualityInspectionPlanDetailExecute_ID"));//上一条的ID
						pd.put("SortKey", varList.get(i-1).get("SortKey"));//改之前的排序值，作为筛选条件
						pd.put("NewSortKey", varList.get(i).get("SortKey"));//新排序值=老排序值+1 
						QualityInspectionPlanDetailExecuteService.editSortKey(pd);//修改排序值
						result = "success";
					}
				}else{
					//当前修改的条不是1 、2 条，
					if(Integer.parseInt(pd.get("SortKey").toString()) != 2 && Integer.parseInt(pd.get("SortKey").toString()) != 1){
						result="error";
					}
				}
			}
			map.put("result", result);
			return map;
		}
		//手动排序，向下一条
			@RequestMapping(value="/downPX")
			@ResponseBody
			public Object downPXx() throws Exception{
				Map<String, Object> map = new HashMap<String,Object>();
				String result = "";
				PageData pd = new PageData();
				PageData pdnew = new PageData();
				pd=this.getPageData();
				List<PageData> varList = QualityInspectionPlanDetailExecuteService.listAll(pd);//获取质检项列表
				for (int i = 0; i < varList.size(); i++) {
					PageData pd1 = varList.get(i);
					if(Integer.parseInt(pd.get("SortKey").toString()) != varList.size()){//如果为最后一条，不能再向下排序返回error
						if(varList.get(i).get("SortKey") == pd.get("SortKey") || varList.get(i).get("SortKey").equals(pd.get("SortKey"))){//判断当前条为要修改的条
							pdnew.put("QualityInspectionPlanDetailExecute_ID", varList.get(i).get("QualityInspectionPlanDetailExecute_ID"));//当前条目的ID
							pdnew.put("SortKey", varList.get(i).get("SortKey"));//改之前的排序值，作为筛选条件
							pdnew.put("NewSortKey", Integer.parseInt(pd.get("SortKey").toString())+1);//新排序值=老排序值-1 
							QualityInspectionPlanDetailExecuteService.editSortKey(pdnew);//修改排序值
							
							pdnew.put("QualityInspectionPlanDetailExecute_ID", varList.get(i+1).get("QualityInspectionPlanDetailExecute_ID"));//下一条的ID
							pdnew.put("SortKey", varList.get(i+1).get("SortKey"));//改之前的排序值，作为筛选条件
							pdnew.put("NewSortKey", varList.get(i).get("SortKey"));//新排序值=老排序值+1 
							QualityInspectionPlanDetailExecuteService.editSortKey(pdnew);//修改排序值
							result = "success";
						}
					}else{
						if(Integer.parseInt(pd.get("SortKey").toString()) != varList.size()-1 && Integer.parseInt(pd.get("SortKey").toString()) != varList.size()){
							result="error";
						}
					}
				}
				map.put("result", result);
				return map;
			}
			/**查询不良原因细分
			 * @param PARENT_ID
			 * @throws Exception
			 */
			@RequestMapping(value="/editBadness")
			@ResponseBody
			public Object editBadness(Page page) throws Exception{
				Map<String,Object> map = new HashMap<String,Object>();
				String errInfo = "success";
				PageData pd = new PageData();
				pd = this.getPageData();
				QualityInspectionPlanDetailExecuteService.editBadness(pd);
				map.put("result", errInfo);
				return map;
			}
			/**柜体质检执行列表列表
			 * @param page
			 * @throws Exception
			 */
			@RequestMapping(value="/listDetailCabinet")
			//@RequiresPermissions("QualityInspectionPlanDetailExecute:list")
			@ResponseBody
			public Object listDetailCabinet(Page page) throws Exception{
				Map<String,Object> map = new HashMap<String,Object>();
				String errInfo = "success";
				PageData pd = new PageData();
				PageData pdList = new PageData();
				pd = this.getPageData();
				page.setPd(pd);
				List<PageData>	varList = QualityInspectionPlanDetailExecuteService.list(page);	//列出QualityInspectionPlanDetailExecute列表
				for(int i = 0;i<varList.size();i++){//循环列表
					pdList = varList.get(i);
					/*Integer SortKey = Integer.parseInt(pdList.get("SortKey").toString());//获取当前排序值
					String FinishOnce = pdList.get("FinishOnce").toString();//获取当前完成状态
					if(SortKey == 1 && FinishOnce == "未完成"){//判断当前排序为1 并且状态为未完成 
						varList.get(i).put("Temporary", "1");//插入临时标识，此表示为1时 显示质检任务执行和完成按钮
					}else if(SortKey > 1 && FinishOnce == "已完成" ){//判断当前明细非第一条，并且状态为未完成
						//获取上一条的完成状态
						Integer FinishOnceLast = Integer.parseInt(QualityInspectionPlanDetailExecuteService.getLastMx(pdList).get("FinishOnce").toString());
						if(FinishOnceLast == 1){
							varList.get(i).put("Temporary", "1");//插入临时标识，此表示为1时 显示质检任务执行和完成按钮
						}
					}*/
					if(null!=varList.get(i) && varList.get(i).containsKey("BreakdownOfBadReasons") && null!= varList.get(i).getString("BreakdownOfBadReasons")) {
						String[] sarr=varList.get(i).getString("BreakdownOfBadReasons").split(",yl,");
						List<String> varList1 =  new ArrayList<>();
						for(int j=0;j<sarr.length;j++) {
							PageData temp = new PageData();
							temp.put("DICTIONARIES_ID", sarr[j]);
							PageData spd = dictionariesService.findById(temp);	//根据ID读取
							if(null!=spd && spd.containsKey("NAME")) {
								varList1.add(spd.getString("NAME"));
							}
						}
						varList.get(i).put("BreakdownOfBadReasons", sarr);
						varList.get(i).put("BreakdownOfBadReasonsNAME", varList1);

					}
					if(null!=varList.get(i) && varList.get(i).containsKey("BadnessReasonLevel") && null!= varList.get(i).getString("BadnessReasonLevel")) {
						String[] sarr=varList.get(i).getString("BadnessReasonLevel").split(",yl,");
						List<String> varList1 =  new ArrayList<>();
						for(int j=0;j<sarr.length;j++) {
							PageData temp = new PageData();
							temp.put("DICTIONARIES_ID", sarr[j]);
							PageData spd = dictionariesService.findById(temp);	//根据ID读取
							if(null!=spd && spd.containsKey("NAME")) {
								varList1.add(spd.getString("NAME"));
							}
						}
						varList.get(i).put("BadnessReasonLevel", sarr);
						varList.get(i).put("BadnessReasonLevelNAME", varList1);

					}
				}
				map.put("varList", varList);
				map.put("page", page);
				map.put("result", errInfo);
				return map;
			}
}
