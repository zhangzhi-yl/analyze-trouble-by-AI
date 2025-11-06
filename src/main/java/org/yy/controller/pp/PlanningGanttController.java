package org.yy.controller.pp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.service.pp.PlanningGanttService;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;

@RestController
@RequestMapping("/PlanningGantt")
public class PlanningGanttController extends BaseController {

	@Autowired
	private PlanningGanttService PlanningGanttService;

	@RequestMapping("/ganttList")
	public Object ganttList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String WorkOrderNum = pd.getString("num");
		if (Tools.notEmpty(WorkOrderNum)) {
			pd.put("WorkOrderNum", WorkOrderNum);
		}
		String customer = pd.getString("customer");
		if (Tools.notEmpty(customer)) {
			pd.put("customer", customer);
		}
		String range_time = pd.getString("range_time");
		if (Tools.notEmpty(range_time)) {
			String[] split = range_time.split(" - ");
			String PlannedBeginTime = split[0];
			String PlannedEndTime = split[1];
			pd.put("PlannedBeginTime", PlannedBeginTime);
			pd.put("PlannedEndTime", PlannedEndTime);
		}
		List<PageData> masters = PlanningGanttService.getAllMasterPlanningWorkOrder(pd);
		List<PageData> result = Lists.newArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		for (PageData master : masters) {
			master.put("start_date", sdf.format(Tools.str2Date(master.getString("start_date"))));
			master.put("end_date", sdf.format(Tools.str2Date(master.getString("end_date"))));
			String material =  master.getString("material");
			String num = master.getString("num")+"/"+material;
			master.put("num", num);
			result.add(master);
			String MasterWorkOrder_ID = master.getString("id");
			pd.put("MasterWorkOrder_ID", MasterWorkOrder_ID);
			List<PageData> subs = PlanningGanttService.getAllSubPlanningWorkOrder(pd);
			for (PageData sub : subs) {
				sub.put("start_date", sdf.format(Tools.str2Date(sub.getString("start_date"))));
				sub.put("end_date", sdf.format(Tools.str2Date(sub.getString("end_date"))));
				String materialsub =  sub.getString("material");
				String subNum = sub.getString("num")+"/"+materialsub;
				sub.put("num", subNum);
				result.add(sub);
//				String PlanningWorkOrderID = sub.getString("id");
//				pd.put("PlanningWorkOrderID", PlanningWorkOrderID);
//				List<PageData> tasks = PlanningGanttService.getAllTask(pd);
//				for (PageData task : tasks) {
//					task.put("start_date", sdf.format(Tools.str2Date(task.getString("start_date"))));
//					task.put("end_date", sdf.format(Tools.str2Date(task.getString("end_date"))));	
//					String wpName =  task.getString("wpName");
//					String taskNum = task.getString("num")+"/"+wpName;
//					task.put("num", taskNum);	
//					result.add(task);
//				}
			}
		}
		pd = null;
		sdf = null;
		map.put("pd", result);
		map.put("result", errInfo);// 返回结果
		return map;
	}

}
