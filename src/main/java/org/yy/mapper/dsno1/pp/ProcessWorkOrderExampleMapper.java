package org.yy.mapper.dsno1.pp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 工艺工单工序实例Mapper 作者：YuanYes QQ356703572 时间：2020-11-11 官网：356703572@qq.com
 * 
 * @version
 */
public interface ProcessWorkOrderExampleMapper {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);

	/**
	 * 列表(根据计划工单查询)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByPlanningWorkOrderID(PageData pd);

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * 根据工位和时间查询列表
	 * 
	 * @param TimeStr
	 * @param StationID
	 * @return
	 */
	List<PageData> listByTimeStrAndStationID(String TimeStr, String StationID);

	/**
	 * 根据计划工单编号和工序更新执行人和工位
	 * 
	 * @param planningWorkOrder_ID
	 * @param wP
	 * @param executorID
	 */
	void updateExecutorIDByWPAndPlanningWorkOrderID(
			@Param(value = "PlanningWorkOrderID") String PlanningWorkOrderID,
			@Param(value = "WP") String WP,
			@Param(value = "ExecutorID") String ExecutorID,
			@Param(value = "FStation") String FStation
	);

	/**
	 * 根据参数选择更新共工艺工单工序实例
	 * 
	 * @param pd
	 */
	void updateByConditionMap(Map<String, Object> pd);
	
	/**
	 * 修改状态，是否生成了质检任务
	 */
	void EditQIIF(PageData pd);
	
	/**
	 * 	配方核对任务列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> recipeChecklistPage(Page page);
	
	/**
	 * 	编辑是否核对
	 */
	void editCheckDoneIF(PageData pd);
	
	/**
	 * 	称重称量任务列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> weighinglistPage(Page page);
	
	/**
	 * 	编辑是否完成称重
	 */
	void editWeighingDoneIF(PageData pd);
	
	/**
	 * 	编辑任务指定设备
	 */
	void editProcessWorkOrderExampleEqm(PageData pd);
	
	/**
	 * 任务关联计划列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageWorkOrderExample(Page page);

	List<PageData> getWoklistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listSClistPage(Page page);

	/**
	 * @param pd
	 * @return
	 */
	PageData findByWP(PageData pd);
}
