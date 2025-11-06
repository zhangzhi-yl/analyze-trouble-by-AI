package org.yy.mapper.dsno1.pp;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 生产任务SOP实例Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-23
 * 官网：356703572@qq.com
 * @version
 */
public interface ProcessWorkOrderExample_SopStepMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	/**
	 * 根据计划子工单删除所有关联的SOP实例
	 * @param planningWorkOrderID
	 */
	void deleteProcessWorkOrderExampleSopStepByPlanningWorkOrderID(String planningWorkOrderID);

	/**
	 * 根据任务id删除SOP
	 * @param taskId
	 */
	void deleteByTaskID(String taskId);

	List<PageData> appListPage(AppPage page);
	
}

