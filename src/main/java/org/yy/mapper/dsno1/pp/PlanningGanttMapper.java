package org.yy.mapper.dsno1.pp;

import java.util.List;

import org.yy.entity.PageData;

public interface PlanningGanttMapper {
	/**
	 * 甘特图 获取一级主计划工单列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getAllMasterPlanningWorkOrder(PageData pd) throws Exception;

	/**
	 * 甘特图 获取二级子计划工单列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getAllSubPlanningWorkOrder(PageData pd) throws Exception;

	/**
	 * 甘特图 获取三级任务列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getAllTask(PageData pd) throws Exception;
}
