package org.yy.service.qm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 质检任务明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-14
 * 官网：356703572@qq.com
 * @version
 */
public interface QualityInspectionPlanDetailExecuteService{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	public List<PageData> AppList(AppPage page)throws Exception;
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	public PageData findBySortKeyList_ID(PageData pd)throws Exception;
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	/**
	 * 获取排序值最大值
	 */
	public String getMaxSortKey(PageData pd)throws Exception;
	/**
	 * 完成单条质检任务明细
	 */
	public void goFinish(PageData pd)throws Exception;
	/**
	 * 获取上一条的完成状态
	 */
	public PageData getLastMx(PageData pd)throws Exception;
	/**
	 * 质检任务明细执行
	 */
	public void ImplementQI(PageData pd)throws Exception;
	
	public void editSortKey(PageData pd)throws Exception;
	
	public void editBadness(PageData pd)throws Exception;
}

