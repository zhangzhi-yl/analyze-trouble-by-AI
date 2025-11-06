package org.yy.service.pp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PlanningGanttMapper;
import org.yy.service.pp.PlanningGanttService;

@Service
public class PlanningGanttServiceImpl implements PlanningGanttService {

	@Autowired
	private PlanningGanttMapper PlanningGanttMapper;

	// 甘特图 获取一级主计划工单列表
	@Override
	public List<PageData> getAllMasterPlanningWorkOrder(PageData pd) throws Exception {
		return PlanningGanttMapper.getAllMasterPlanningWorkOrder(pd);
	}

	// 甘特图 获取二级子计划工单列表
	@Override
	public List<PageData> getAllSubPlanningWorkOrder(PageData pd) throws Exception {
		return PlanningGanttMapper.getAllSubPlanningWorkOrder(pd);
	}

	// 甘特图 获取三级任务列表
	@Override
	public List<PageData> getAllTask(PageData pd) throws Exception {
		return PlanningGanttMapper.getAllTask(pd);
	}

}
