package org.yy.controller.pp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.MaterialConsumeService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.pp.WorkorderProcessIOExampleService;
import org.yy.service.qm.QualityInspectionPlanExecuteService;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

/**
 * 生产追溯
 * 
 * @author chen
 *
 */
@Controller
@RequestMapping("/productTrace")
public class ProductTraceController extends BaseController {

	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	
	@Autowired
	private WorkorderProcessIOExampleService WorkorderProcessIOExampleService;

	@Autowired
	private MaterialConsumeService MaterialConsumeService;

	@Autowired
	private QualityInspectionPlanExecuteService QualityInspectionPlanExecuteService;
	
	@Autowired
	private StaffService StaffService;

	@RequestMapping("/trace")
	@ResponseBody
	public Object trace() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> result = Maps.newHashMap();
		// 根据 工单号和工单id获取详情
		PageData findById = PlanningWorkOrderService.findById(pd);
		
		// 获取 任务列表
		String PlanningWorkOrderID = pd.getString("PlanningWorkOrder_ID");
		List<PageData> ProcessWorkOrderExampleList = PlanningWorkOrderService
				.getListProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);
		
		// 取最后一个产出的工序 为 总产出数量
		BigDecimal ConsumptionQuantity = new BigDecimal("0");
		
		PageData lastTaskOut = new PageData();
		lastTaskOut.put("WorkOrderRel", PlanningWorkOrderID);
		lastTaskOut.put("MaterialID",findById.getString("OutputMaterialID") );
		lastTaskOut.put("FType", "产出");
		List<PageData> lastTaskOutputList = MaterialConsumeService.listAll(lastTaskOut);		
		for (PageData out : lastTaskOutputList) {
			ConsumptionQuantity = ConsumptionQuantity.add(new BigDecimal(String.valueOf(out.get("ConsumptionQuantity"))));
			
		}
	
		findById.put("ConsumptionQuantity", ConsumptionQuantity);
		result.put("PlanningWorkOrder", findById);
		// 获取配方实例
		for (PageData pwo : ProcessWorkOrderExampleList) {
			//开始获取投入产出
			List<PageData> listByProcessWorkOrderExampleID
			= WorkorderProcessIOExampleService
			.listByProcessWorkOrderExampleID(pwo.getString("ProcessWorkOrderExample_ID"));
			List<PageData> inputListPwo = Lists.newArrayList();
			List<PageData> OutputListPwo = Lists.newArrayList();
			for (PageData pageData : listByProcessWorkOrderExampleID) {
				// 循环任务列表获取投入列表
				PageData pdPWOIn = new PageData();
				pdPWOIn.put("ConsumptionDocumentID", pageData.getString("WorkorderProcessIOExample_ID"));
				pdPWOIn.put("DataSources", "PP_WorkorderProcessIOExample");
				pdPWOIn.put("FType", "消耗");
				List<PageData> inputList = MaterialConsumeService.listAll(pdPWOIn);
				for (PageData in : inputList) {
					PageData staffData = new PageData();
					staffData.put("STAFF_ID", in.getString("FOperatorID"));
					PageData staff = StaffService.findById(staffData);
					if(null!=staff){
						in.put("FOperatorName", staff.getString("NAME"));
					}
					
				}
				inputListPwo.addAll(inputList);
			

				// 循环任务列表获取产出列表
				PageData pdPWOOut = new PageData();
				pdPWOOut.put("ConsumptionDocumentID", pageData.getString("WorkorderProcessIOExample_ID"));
				pdPWOOut.put("DataSources", "PP_WorkorderProcessIOExample");
				pdPWOOut.put("FType", "产出");
				List<PageData> outputList = MaterialConsumeService.listAll(pdPWOOut);
				for (PageData out : outputList) {
					PageData staffData = new PageData();
					staffData.put("STAFF_ID", out.getString("FOperatorID"));
					PageData staff = StaffService.findById(staffData);
					if(null!=staff){
						out.put("FOperatorName", staff.getString("NAME"));
					}
					
				}
				OutputListPwo.addAll(outputList);
				
			}
			pwo.put("inputList", inputListPwo);
			pwo.put("outputList", OutputListPwo);
			// 循环任务列表根据任务id获取是否创建了质检任务，如果创建则带出来 IMG 点击进入质检详情
			PageData qmTaskPd = new PageData();
			qmTaskPd.put("ProcessWorkOrderExample_ID", pwo.getString("ProcessWorkOrderExample_ID"));
			List<PageData> qmTaskList = QualityInspectionPlanExecuteService.listAll(qmTaskPd);
			// 展示是否有检验任务
			if (CollectionUtil.isNotEmpty(qmTaskList)) {
				pwo.put("showJian", "YES");
			} else {
				pwo.put("showJian", "NO");
			}
		

		}

		result.put("ProcessWorkOrderExampleList", ProcessWorkOrderExampleList);

		map.put("pd", result);
		map.put("result", errInfo);// 返回结果
		return map;
	}
}
