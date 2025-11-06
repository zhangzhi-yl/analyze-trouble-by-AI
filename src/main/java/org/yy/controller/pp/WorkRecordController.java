package org.yy.controller.pp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.pp.WorkRecordService;

/** 
 * 说明：操作工时记录
 * 作者：YuanYes QQ356703572
 * 时间：2021-02-05
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/WorkRecord")
public class WorkRecordController extends BaseController {
	
	@Autowired
	private WorkRecordService WorkRecordService;
	
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
		String name = Jurisdiction.getName();
		pd.put("O_RECORD_ID", this.get32UUID());	//主键
		pd.put("WorkingProcedureExampleID", pd.getString("WorkingProcedureExampleID"));
		pd.put("BeginTime",Tools.date2Str(new Date()));
		pd.put("OPerson",name);
		pd.put("OStation",pd.getString("FStation"));
		WorkRecordService.save(pd);
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
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WorkRecordService.delete(pd);
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
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd =WorkRecordService.findById(pd);
			if(null == pd){
				throw new RuntimeException("计时未开始！");
			}
			pd.put("EndTime",Tools.date2Str(new Date()));
			WorkRecordService.edit(pd);
			map.put("result", errInfo);
			return map;
		} catch (Exception e) {
			map.put("result", "exception");
			map.put("msg", e.getMessage());
			return map;
		}
		
	
	}
	
	@RequestMapping(value="/findByExampleID")
	@ResponseBody
	public Object findByExampleID() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = WorkRecordService.findByExampleID(pd);	//根据ID读取
		if(null == pd){
			map.put("msg", "暂无记录");
			map.put("result", "error");
			return map;
		}
		map.put("pd", pd);
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
		List<PageData>	varList = WorkRecordService.list(page);	//列出WorkRecord列表
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
		pd = WorkRecordService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
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
			WorkRecordService.deleteAll(ArrayDATA_IDS);
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
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("主键");	//1
		titles.add("备注2");	//2
		titles.add("备注3");	//3
		titles.add("备注4");	//4
		titles.add("备注5");	//5
		titles.add("备注6");	//6
		titles.add("备注7");	//7
		titles.add("备注8");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = WorkRecordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("O_RECORD_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("PlanningWorkOrder_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("WorkingProcedureExampleID"));	    //3
			vpd.put("var4", varOList.get(i).getString("BeginTime"));	    //4
			vpd.put("var5", varOList.get(i).getString("EndTime"));	    //5
			vpd.put("var6", varOList.get(i).getString("OPerson"));	    //6
			vpd.put("var7", varOList.get(i).getString("OStation"));	    //7
			vpd.put("var8", varOList.get(i).getString("FDescribe"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	 /**查询任务工作时长与时间
	  * 根据参数任务ID ProcessWorkOrderExample_ID，当前登录人查询任务最开始时间与任务最小时间和工时
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/selectWorkTime")
	@ResponseBody
	public Object selectWorkTime() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "error";
		PageData pd = new PageData();
		PageData workTimepd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", Jurisdiction.getName());
		workTimepd = WorkRecordService.selectWorkTime(pd);	//根据ID读取
		System.out.println("workTimepd:"+workTimepd);
		if(null!=workTimepd) {
			errInfo = "success";
		}
		map.put("pd", workTimepd);
		map.put("result", errInfo);
		return map;
	}	
}
