package org.yy.mapper.dsno1.mom;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 质量检测发布(明细)Mapper
 * 作者：YuanYe
 * 时间：2020-02-24
 * 
 * @version
 */
public interface TEMBILL_EXECUTEMxMapper{

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
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	/**根据主表ID删除数据
	 * @param pd
	 * @throws Exception
	 */
	void deleteId(PageData pd);
	
	/**根据主表ID查询列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listMxAll(PageData pd);
	
	/**更新反馈内容
	 * @param pd
	 * @throws Exception
	 */
	void setFeedback(PageData pd);

	/**设备点巡检明细
	 * @param page
	 * @return
	 */
	List<PageData> eqmPointInspectListMx(AppPage page);
}

