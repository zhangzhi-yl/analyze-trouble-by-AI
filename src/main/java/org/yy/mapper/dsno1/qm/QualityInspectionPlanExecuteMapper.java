package org.yy.mapper.dsno1.qm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 质检任务Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-12
 * 官网：356703572@qq.com
 * @version
 */
public interface QualityInspectionPlanExecuteMapper{

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
	/**列表(已下发)
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageRun(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	List<PageData> getGX(PageData pd);
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
	 * 下发质检任务
	 */
	void goLssue(PageData pd);
	/**
	 * 修改开始时间
	 */
	void saveFBeginTime(PageData pd);
	/**
	 * 修改结束时间
	 */
	void saveFEndTime(PageData pd);
	
	PageData getJG(PageData pd);
	void editJG(PageData pd);
	void editZT(PageData pd);
	List listQI(PageData pd);
	List listQIMAT(PageData pd);

	/**柜体质检任务列表
	 * @param page
	 * @return
	 */
	List<PageData> CabinetlistPage(Page page);

	/**
	 * @param pd
	 * @return
	 */
	PageData findByIdx(PageData pd);
}

