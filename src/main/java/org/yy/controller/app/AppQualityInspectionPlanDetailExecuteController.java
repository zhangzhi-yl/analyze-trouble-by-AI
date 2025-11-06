package org.yy.controller.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.qm.QualityInspectionPlanDetailExecuteService;
import org.yy.service.qm.QualityInspectionPlanExecuteService;
import org.yy.service.system.DictionariesService;

/** 
 * 说明：质检任务明细
 * 作者：范贺男
 * 时间：2020-11-14
 */
@Controller
@RequestMapping("/appQualityInspectionPlanDetailExecute")
public class AppQualityInspectionPlanDetailExecuteController extends BaseController {
	
	@Autowired
	private QualityInspectionPlanDetailExecuteService QualityInspectionPlanDetailExecuteService;
	@Autowired
	private QualityInspectionPlanExecuteService QualityInspectionPlanExecuteService;
	@Autowired
	private DictionariesService dictionariesService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OperationRecordService operationrecordService;
	/**保存
	 * @param   质检任务ID   QualityInspectionPlanExecute_ID
	 * @throws Exception
	 *//*
	@RequestMapping(value="/addDetail")
	//@RequiresPermissions("QualityInspectionPlanDetailExecute:add")
	@ResponseBody
	public Object add(HttpServletResponse response) throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			int  SortKey = 0;
			try{
			 SortKey = Integer.parseInt( QualityInspectionPlanDetailExecuteService.getMaxSortKey(pd));//根据质检任务获取明细列表中排序值的最大值
			}catch(Exception e){
				SortKey = 0;
			}
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
			pd.put("FinishOnce", 0);
			QualityInspectionPlanDetailExecuteService.save(pd);
			operationrecordService.add("","质检任务明细","添加",pd.getString("QualityInspectionPlanDetailExecute_ID"),"","");//操作日志
			return AppResult.success(map, "获取成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
	}*/
	/**
	 * 完成单条质检任务明细
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goFinish")
	@ResponseBody
	public Object goFinish() throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pd1 = new PageData();
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
			return AppResult.success("success", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	
			//自动判断填写的检验结果是否合格
			@RequestMapping(value="/AutoJudeg")
			@ResponseBody
			public Object AutoJudeg() throws Exception{
				Map<String, Object> map = new HashMap<String,Object>();
				PageData pd = new PageData();
				pd=this.getPageData();
				String result = "";
				try{
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
					return AppResult.success(map,"success", "success");
				}catch (Exception e) {
					e.printStackTrace();
					return AppResult.failed(e.getMessage());
				}
			}
	
	/**
	 * 质检任务明细执行
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value="/ImplementQI")
	@ResponseBody
	public Object ImplementQI() throws Exception{
		try{
			PageData pd = new PageData();
			PageData pddif = new PageData();
			PageData pdStaff = new PageData();
			pd = this.getPageData();
			pd.put("FNAME",pd.getString("InspectorID"));					//获取当前登录人的中文名称
			pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
			if(pdStaff==null) {
				return AppResult.success("当前员工不存在！", "当前员工不存在！");
			}
			pd.put("InspectorID",pdStaff.get("STAFF_ID"));			//检验人
			pd.put("InspectionAndTestTime", Tools.date2Str(new Date()));//检验时间
			pd.put("BadnessReasonLevelNAME", pd.getString("LastDictionariesFilter")==null?"":pd.getString("LastDictionariesFilter"));
			String[] NAME=(pd.getString("LastDictionariesFilter")==null?"":pd.getString("LastDictionariesFilter")).split("，");//不良原因细分
			List<String> BadnessReasonLevelS=new ArrayList<>();
			for (String string : NAME) {
				pddif.put("NAME", string);
				PageData dif=dictionariesService.findByName(pddif);
				if(dif!=null){
					BadnessReasonLevelS.add(dif.getString("DICTIONARIES_ID")==null?"":dif.getString("DICTIONARIES_ID"));
				}
			}
			if(BadnessReasonLevelS!=null&&!BadnessReasonLevelS.equals("")){
				String BadnessReasonLevel= this.listToString(BadnessReasonLevelS, ",yl,");
				pd.put("BadnessReasonLevel", BadnessReasonLevel);
			}
			QualityInspectionPlanDetailExecuteService.ImplementQI(pd);//
			operationrecordService.add("","质检任务明细","执行",pd.getString("QualityInspectionPlanDetailExecute_ID"),"","");//操作日志
			return AppResult.success("success", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listDetail")
	@ResponseBody
	public Object list(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			String orderby = "SortKey";
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "asc";
			if ("desc".equals(pd.getString("sort"))) {
				sort = "desc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(QualityInspectionPlanDetailExecuteService.AppList(page));
			varList = pageInfo.getList();
			for(int i = 0;i<varList.size();i++){//循环列表
				PageData pdList=new PageData();
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
			}
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**不良原因细分
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listByParentID")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PARENT_ID = pd.getString("PARENT_ID");
		if(Tools.isEmpty(PARENT_ID)){
			map.put("varList", Lists.newArrayList());
			map.put("result", errInfo);
			return map;
		}
		String[] split = PARENT_ID.split(",yl,");
		List<String> PARENT_IDList = Lists.newArrayList(split);
		List<PageData>	varList = Lists.newArrayList();
		for (String PARENT_ID_STR : PARENT_IDList) {
			PageData pData = new PageData();
			pData.put("PARENT_ID", PARENT_ID_STR);
			varList.addAll(dictionariesService.listByParentID(pData));	
		}
		return AppResult.success(varList, "获取成功", "success");
	}
	
	public static String listToString(List list, String separator) {
        return StringUtils.join(list.toArray(), separator);
    }
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit(HttpServletResponse response) throws Exception{
		try {
			boolean preflag = false;
			boolean nextflag = false;
			PageData pd = new PageData();
			pd = this.getPageData();
			int SortKey = Integer.parseInt(pd.get("SortKey").toString());
			if(SortKey==0){
				pd.put("FIRST", "FIRST");
			}else{
				pd.put("SECOND", "SECOND");
			}
			pd = QualityInspectionPlanDetailExecuteService.findBySortKeyList_ID(pd);	//根据ID读取
			if(pd!=null){
				String DICTIONARIES_ID = pd.getString("BadnessReasonLevel");
				if(DICTIONARIES_ID!=null && !DICTIONARIES_ID.equals("")){
					String[] split = DICTIONARIES_ID.split(",yl,");
					List<String> PARENT_IDList = Lists.newArrayList(split);
					List<String> varList = Lists.newArrayList();
					for (String PARENT_ID_STR : PARENT_IDList) {
						PageData pData = new PageData();
						pData.put("DICTIONARIES_ID", PARENT_ID_STR);
						PageData pDatas=dictionariesService.findById(pData);
						varList.add(pDatas.getString("NAME"));	
					}
					String ss = String.join(",", varList);
					pd.put("BadnessReasonLevelNAME", ss);
				}
			}else{
				return AppResult.success("当前任务没有待执行项！", "当前任务没有待执行项！");
			}
			if(null!=pd && pd.containsKey("SortKey")) {
				PageData prepd = new PageData();
				PageData nextpd = new PageData();
				int pre = Integer.parseInt(pd.get("SortKey").toString())-1;
				int next = Integer.parseInt(pd.get("SortKey").toString())+1;
				if(pre>0) {
					prepd.put("SortKey", pre);
					prepd.put("SECOND", "SECOND");
					prepd.put("QualityInspectionPlanExecute_ID", pd.getString("QualityInspectionPlanExecute_ID"));
					prepd = QualityInspectionPlanDetailExecuteService.findBySortKeyList_ID(prepd);
					if(null!=prepd)preflag=true;
				}
				nextpd.put("SortKey", next);
				nextpd.put("SECOND", "SECOND");
				nextpd.put("QualityInspectionPlanExecute_ID", pd.getString("QualityInspectionPlanExecute_ID"));
				nextpd = QualityInspectionPlanDetailExecuteService.findBySortKeyList_ID(nextpd);
				if(null!=nextpd)nextflag=true;
			}
			pd.put("hasPre", preflag);
			pd.put("hasNext", nextflag);
			return AppResult.success(pd, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	
}
