package org.yy.mapper.dsno1.qm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 质检任务明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-14
 * 官网：356703572@qq.com
 * @version
 */
public interface QualityInspectionPlanDetailExecuteMapper{

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
	List<PageData> AppList(AppPage page);
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
	PageData findBySortKeyList_ID(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	/**
	 * 获取排序值最大值
	 */
	String getMaxSortKey(PageData pd);
	/**
	 * 完成单条质检任务明细
	 */
	void goFinish(PageData pd);
	/**
	 * 获取上一条的完成状态
	 * @param pd
	 * @return
	 */
	PageData getLastMx(PageData pd);
	/**
	 * 质检任务明细执行
	 */
	void ImplementQI(PageData pd);
	 
	void editSortKey(PageData pd);
	
	void editBadness(PageData pd);
}

