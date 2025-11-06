package org.yy.service.pp;

import java.util.List;
import java.util.Map;

import org.yy.entity.Page;
import org.yy.entity.PageData;

public interface ProcessWorkOrderExampleService {

	void updateExecutorIDByWPAndPlanningWorkOrderID(String planningWorkOrder_ID, String WP, String ExecutorID,
			String FStation);

	void updateByConditionMap(Map<String, Object> map2);

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception;

	public PageData findById(PageData pageData1);

	public List<PageData> listAll(PageData po) ;

	void edit(PageData pageData);

	void add(PageData pd);
	void delete(PageData pd);
	void EditQIIF(PageData pd);
	
	/**
	 * 	配方核对任务列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> recipeChecklistPage(Page page) throws Exception;
	
	/**
	 * 	编辑是否核对
	 */
	public void editCheckDoneIF(PageData pd) throws Exception;
	
	/**
	 * 	称重称量任务列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> weighinglistPage(Page page) throws Exception;
	
	/**
	 * 	编辑是否完成称重
	 */
	public void editWeighingDoneIF(PageData pd) throws Exception;
	
	/**
	 * 	编辑任务指定设备
	 */
	public void editProcessWorkOrderExampleEqm(PageData pd) throws Exception;
	
	/**
	 * 任务关联计划列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> workOrderExamplelistPage(Page page) throws Exception;

	List<PageData> getWoklistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listSC(Page page)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	PageData findByWP(PageData pd)throws Exception;
}
