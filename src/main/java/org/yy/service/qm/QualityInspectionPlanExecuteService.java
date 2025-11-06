package org.yy.service.qm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 质检任务接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-12
 * 官网：356703572@qq.com
 * @version
 */
public interface QualityInspectionPlanExecuteService{

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
	/**列表(已下发)
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listRun(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	public List<PageData> getGX(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	/**
	 * 下发质检任务
	 */
	public void goLssue(PageData pd)throws Exception;
	/**
	 * 修改开始时间
	 */
	public void saveFBeginTime(PageData pd)throws Exception;
	/**
	 * 修改结束时间
	 */
	public void saveFEndTime(PageData pd)throws Exception;
	
	public PageData getJG(PageData pd) throws Exception;
	public void editJG(PageData pd) throws Exception;
	public void editZT(PageData pd) throws Exception; 
	public List<PageData> listQI(PageData pd)throws Exception;
	public List listQIMAT(PageData pd)throws Exception;

	/**柜体质检任务列表
	 * @param page
	 * @return
	 */
	public List<PageData> listCabinet(Page page)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public PageData findByIdx(PageData pd)throws Exception;
}

