package org.yy.controller.app;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PlanningWorkOrderMapper;
import org.yy.service.pp.PlanningWorkOrderMasterService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.run.FirstStageService;
import org.yy.service.run.SecondStageService;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;

@RestController
@RequestMapping("/appDonePatch")
public class AppDonePatchController extends BaseController {
	@Autowired
	private SecondStageService secondStageService;

	@Autowired
	private FirstStageService firstStageService;

	@Autowired
	private PlanningWorkOrderMapper pwoService;

	@Autowired
	private PlanningWorkOrderMasterService pwomService;

	@Autowired
	private ProcessWorkOrderExampleService processWorkOrderExampleService;

	/**
	 * 手机端点击结束，更新任务状态为结束，开启下一级节点
	 * 
	 * @return pd
	 * @throws Exception
	 */

	@RequestMapping(value = "/done")
	@ResponseBody
	public Object nodes() throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			PageData appProcessWorkOrderExampleDetailByPK = processWorkOrderExampleService.findById(pd);
			if (null == appProcessWorkOrderExampleDetailByPK) {
				return AppResult.failed("任务没找到!");
			}
			appProcessWorkOrderExampleDetailByPK.put("FStatus", "结束");
			appProcessWorkOrderExampleDetailByPK.put("FRUN_STATE", "结束");
			appProcessWorkOrderExampleDetailByPK.put("ActualEndTime", Tools.date2Str(new Date()));
			appProcessWorkOrderExampleDetailByPK.put("FOPERATOR", pd.getString("STAFF_ID"));
			appProcessWorkOrderExampleDetailByPK.put("EXECUTE_TIME", Tools.date2Str(new Date()));
			appProcessWorkOrderExampleDetailByPK.put("TIME_STAMP", System.currentTimeMillis());
			processWorkOrderExampleService.edit(appProcessWorkOrderExampleDetailByPK);
		
			List<PageData> runNodeInfo = pwoService.getRunNodeInfo(pd);
			for (PageData pageData : runNodeInfo) {
				String RUN_O_ID = pageData.getString("RUN_O_ID");
				String NODE_ID = pageData.getString("NODE_ID");
				String MASTERPLAN_ID = pageData.getString("MASTERPLAN_ID");
				String SUBPLAN_ID = pageData.getString("SUBPLAN_ID");
				getNode2("P2", RUN_O_ID, NODE_ID, MASTERPLAN_ID, SUBPLAN_ID);
			}
			return AppResult.success("success", "修改成功", "success");
		}  catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}

	// TYPE P1 是上级传入，P2是本机传入
	// RUN_O_ID
	// NODE_ID 节点ID
	// MASTERPLAN_ID 关联ID
	public Object getNode2(String TYPE, String RUN_O_ID, String NODE_ID, String MASTERPLAN_ID, String SUBPLAN_ID)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "200";
		PageData pd = new PageData();
		pd.put("MASTERPLAN_ID", MASTERPLAN_ID);// 工单主键值必须要有,为了保证一致性
		List<PageData> pdNum = Lists.newArrayList();
		if (TYPE.equals("P2")) { // 如果是本级节点直接从本级节点赋值，下面会用这个节点找下一下节点作为开始
			pd.put("BEGIN_NODE", NODE_ID);
			pd.put("SUBPLAN_ID", SUBPLAN_ID);
		} else { // 如果是上一级节点，那说明要从上一级节点找到这个节点的开始节点
			pd.put("RUN_O_ID", RUN_O_ID); // 加入第一级主键，获取一级节点ID
			PageData startNodePd = secondStageService.getStartNodeId(pd);// 获取开始节点ID
			if (startNodePd == null) {
				pd.put("BEGIN_NODE", "NULL");
			} else {
				pd.put("BEGIN_NODE", startNodePd.get("NODE_ID"));
				pd.put("SUBPLAN_ID", startNodePd.get("SUBPLAN_ID"));
			}

		}

		try {
			int num = 0;
			pdNum = secondStageService.findCount(pd);// 查询一级节点下的所有正在执行中和未执行的二级节点数量
			if (CollectionUtil.isNotEmpty(pdNum)) {
				num = pdNum.size();// 正在执行中和未执行总数量
				if (num > 1) {// 说明有并行节点未执行完的节点
					pd.put("EXECUTE_STATE", "执行结束");
					pd.put("ActualEndTime", Tools.date2Str(new Date()));
					secondStageService.changeState(pd);// 修改执行状态为执行结束******
				} else {// 本节点结束 开下一节点
					secondStageService.updateStateUnexecuted(pd);// 修改状态为未执行
					List<PageData> list = secondStageService.findNextNodes(pd);// 根据开始节点id查询所有下一步节点
					for (PageData pageData : list) {
						if (pageData.getString("NODE_TYPE").equals("end round")) {
							PageData firstNodePd = secondStageService.getONodeId(pageData);// 获取一级节点ID
							getNode1("P2", firstNodePd.getString("NODE_ID"), MASTERPLAN_ID);
							System.out.println("已经运转到第2级最后一级节点-下一步应该寻找上一层级节点了");
						} else {
							String NEXT_NODE_ID = pageData.getString("NODE_ID");
							String NEXT_SUBPLAN_ID = pageData.getString("SUBPLAN_ID");
							if (Tools.notEmpty(NEXT_SUBPLAN_ID)) {
								// 修改下一个节点为待执行状态
								pageData.put("EXECUTE_STATE", "待执行");
								secondStageService.changePhState(pageData);
								// 修改下一个生产任务的状态待执行
								PageData listpwoe = new PageData();
								listpwoe.put("PlanningWorkOrderID", NEXT_SUBPLAN_ID);
								listpwoe.put("NODE_ID", NEXT_NODE_ID);
								// 根据node节点和 PLANNING_WORK_ORDER_ID 获取下一个生产任务
								List<PageData> listAll = processWorkOrderExampleService.listAll(listpwoe);
								for (PageData pageData2 : listAll) {
									pageData2.put("FStatus", "待执行");
									pageData2.put("FRUN_STATE", "待执行");
									pageData2.put("TIME_STAMP", new Date().getTime());
									pageData2.put("FOPERATOR", pageData2.getString("ExecutorID"));
									pageData2.put("EXECUTE_TIME", Tools.date2Str(new Date()));
									pageData2.put("TIME_STAMP", System.currentTimeMillis());
									processWorkOrderExampleService.edit(pageData2);
								}
							}
						}
					}
				}
				/*
				 * 结束当前运行中任务节点,根据NODE_NO和MASTERPLAN_ID修改数据运行状态、执行人、当前操作用户、执行进入时间
				 * 
				 * @param pd NODE_NO 节点编号 MASTERPLAN_ID 生产工单ID FRUN_STATE 运行状态
				 * FOPERATOR 执行人 FUSERNAME 当前操作用户 EXECUTE_TIME 执行进入时间
				 */

				if (!"P1".equals(TYPE)) {
					// 结束当前运行中任务节点
					PageData pdData = new PageData();
					pdData.put("RUN_O_ID", RUN_O_ID);
					pdData.put("NODE_ID", NODE_ID);
					pdData.put("MASTERPLAN_ID", MASTERPLAN_ID);
					pdData.put("ActualEndTime", Tools.date2Str(new Date()));
					firstStageService.setPwoStatusDone(pdData);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "500";
		} finally {
			map.put("result", result);
			map.put("pd", pd);
		}
		return map;
	}

	public Object getNode1(String TYPE, String NODE_ID, String MASTERPLAN_ID) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "200";
		PageData pd = new PageData();
		pd.put("MASTERPLAN_ID", MASTERPLAN_ID);// 工单主键值必须要有,为了保证一致性
		List<PageData> pdNum = Lists.newArrayList();
		pd.put("BEGIN_NODE", NODE_ID);
		try {
			int num = 0;
			pdNum = firstStageService.findCount(pd);// 查询一级节点下的所有正在执行中和未执行的二级节点数量
			if (CollectionUtil.isNotEmpty(pdNum)) {
				num = pdNum.size();// 正在执行中和未执行总数量
				if (num > 1) {
					pd.put("EXECUTE_STATE", "执行结束");
					pd.put("ActualEndTime", Tools.date2Str(new Date()));
					firstStageService.changeState(pd);// 修改执行状态为执行结束
				} else {
					firstStageService.updateStateUnexecuted(pd);// 修改状态为未执行
					List<PageData> list = firstStageService.findNextNodes(pd);// 根据开始节点id查询所有下一步节点
					for (PageData pageData : list) {
						if (pageData.getString("NODE_TYPE").equals("end round")) {
							System.out.println("已经运转到第1级最后一级节点-下一步应该寻找上一层级节点了");
							// 反写主计划工单结束状态
							PageData pwomParam = new PageData();
							pwomParam.put("PlanningWorkOrderMaster_ID", MASTERPLAN_ID);
							PageData masterPlan = pwomService.findById(pwomParam);
							masterPlan.put("FStatus", "结束");
							pwomService.edit(masterPlan);
						} else {
							firstStageService.changeOState(pageData);// 修改本级节点为执行中状态
							// 修改当前子工单状态为 执行中
							firstStageService.setPwoStatusGoing(pageData);

							getNode2("P1", pageData.getString("MASTER_PLAN_FLOW_ID"), "", MASTERPLAN_ID, "");// 开下层级节点
						}
					}
				}

			}

			// 反写子计划工单结束时间和结束状态为 结束
			PageData pwoParam = new PageData();
			pwoParam.put("NODE_ID", NODE_ID);
			pwoParam.put("MasterWorkOrder_ID", MASTERPLAN_ID);
			List<PageData> listAll = pwoService.listAll(pwoParam);
			if (CollectionUtil.isNotEmpty(listAll)) {
				for (PageData pageData : listAll) {
					pageData.put("FStatus", "结束");
					pwoService.edit(pageData);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "500";
		} finally {
			map.put("result", result);
			map.put("pd", pd);
		}
		return map;
	}
}
