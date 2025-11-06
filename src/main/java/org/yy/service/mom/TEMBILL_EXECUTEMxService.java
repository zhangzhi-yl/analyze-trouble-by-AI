package org.yy.service.mom;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 质量检测发布(明细)接口
 * 作者：YuanYe
 * 时间：2020-02-24
 * 
 * @version
 */
public interface TEMBILL_EXECUTEMxService{

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
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;
	
	/**根据主表ID删除数据
	 * @param pd
	 * @throws Exception
	 */
	public void deleteId(PageData pd)throws Exception;
	
	/**根据主表ID查询列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listMxAll(PageData pd)throws Exception;
	
	/**更新反馈内容
	 * @param pd
	 * @throws Exception
	 */
	public void setFeedback(PageData pd)throws Exception;

	/**设备点巡检明细
	 * @param page
	 * @return
	 */
	public List<PageData> eqmPointInspectListMx(AppPage page)throws Exception;
}

