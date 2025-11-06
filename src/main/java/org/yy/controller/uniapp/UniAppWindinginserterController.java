package org.yy.controller.uniapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.windinginserter.WindinginserterService;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：下线机
 * 作者：YuanYe
 * 时间：2021-10-18
 *
 */
@Controller
@RequestMapping("api/uniWindinginserter")
public class UniAppWindinginserterController extends BaseController {

	@Autowired
	private WindinginserterService windinginserterService;

	/**列表
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(@RequestBody PageData pd) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		String KEYWORDS = pd.getString("KEYWORDS");
		if(Tools.notEmpty(KEYWORDS)) {
			pd.put("KEYWORDS", KEYWORDS.trim());
		}
		//下线机任务详情列表
		List<PageData>	varList = windinginserterService.TaskDetails(pd);

		List<PageData>	monthUseLine = windinginserterService.MonthUseLine();
		List<PageData>	noFaultDayNum = windinginserterService.NoFaultDayNum();
		List<PageData>	currentCapacity = windinginserterService.CurrentCapacity();
		List<PageData>	currentYearProduction = windinginserterService.CurrentYearProduction();
		List<PageData>	currentMonthProduction = windinginserterService.CurrentMonthProduction();
		List<PageData>	currentDayProduction = windinginserterService.CurrentDayProduction();
		List<PageData>	totalProductionQuantity = windinginserterService.TotalProductionQuantity();
		List<PageData>	currentEquipmentStatus = windinginserterService.CurrentEquipmentStatus();


		map.put("varList", varList);
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

}
