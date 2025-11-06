package org.yy.controller.windinginserter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.windinginserter.WindinginserterService;
import org.yy.service.zm.ALARMRULEService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import java.util.*;

/**
 * 说明：下线机
 * 作者：YuanYe
 * 时间：2021-10-18
 *
 */
@Controller
@RequestMapping("/windinginserter")
public class WindinginserterController extends BaseController {

	@Autowired
	private WindinginserterService windinginserterService;

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
		String KEYWORDS = pd.getString("KEYWORDS");
		if(Tools.notEmpty(KEYWORDS)) {
			pd.put("KEYWORDS", KEYWORDS.trim());
		}
		page.setPd(pd);
		//下线机任务详情列表
		List<PageData>	varList = windinginserterService.TaskDetailsdatalistPage(page);

		List<PageData>	monthUseLine = windinginserterService.MonthUseLine();
		List<PageData>	noFaultDayNum = windinginserterService.NoFaultDayNum();
		List<PageData>	currentCapacity = windinginserterService.CurrentCapacity();
		List<PageData>	currentYearProduction = windinginserterService.CurrentYearProduction();
		List<PageData>	currentMonthProduction = windinginserterService.CurrentMonthProduction();
		List<PageData>	currentDayProduction = windinginserterService.CurrentDayProduction();
		List<PageData>	totalProductionQuantity = windinginserterService.TotalProductionQuantity();
		List<PageData>	currentEquipmentStatus = windinginserterService.CurrentEquipmentStatus();


		map.put("varList", varList);
		map.put("page", page);
		map.put("monthUseLine", monthUseLine.get(0));
		map.put("noFaultDayNum", noFaultDayNum.get(0));
		map.put("currentCapacity", currentCapacity.get(0));
		map.put("currentYearProduction", currentYearProduction.get(0));
		map.put("currentMonthProduction", currentMonthProduction.get(0));
		map.put("currentDayProduction", currentDayProduction.get(0));
		map.put("totalProductionQuantity", totalProductionQuantity.get(0));
		map.put("currentEquipmentStatus", currentEquipmentStatus.get(0));
		map.put("result", errInfo);
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
		titles.add("工单条码");	//1
		titles.add("柜体名称");	//2
		titles.add("柜体别名");	//3
		titles.add("是否变更");
		titles.add("生产效率");	//4
		titles.add("生产数量");	//5
		titles.add("线束种类");	//6
		titles.add("导线长度");	//7
		titles.add("开始加工时间");	//8
		titles.add("预计结束时间");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = windinginserterService.TaskDetails(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("工单条码"));	    //1
			vpd.put("var2", varOList.get(i).getString("柜体名称"));	    //2
			vpd.put("var3", varOList.get(i).getString("柜体别名"));	    //3
			vpd.put("var4", varOList.get(i).getString("是否变更"));	    //3
			vpd.put("var5", varOList.get(i).getString("生产效率"));	    //4
			vpd.put("var6", varOList.get(i).getString("生产数量"));	    //5
			vpd.put("var7", varOList.get(i).getString("线束种类"));	    //6
			vpd.put("var8", varOList.get(i).getString("导线长度"));	    //7
			vpd.put("var9", varOList.get(i).getString("开始加工时间"));	    //8
			vpd.put("var10", varOList.get(i).getString("预计结束时间"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
