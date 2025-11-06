package org.yy.mapper.dsno1.mdmc;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备保养月计划Mapper
 * 作者：YuanYe
 * 时间：2020-06-22
 * 
 * @version
 */
public interface EQM_MAINTENANCE_PLAN_MONTHMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	void editState(PageData pd);
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

	/**根据选中年份（2020）查找本年创建过月计划数量
	 * @param pd
	 * @return
	 */
	PageData getNUM(PageData pd);

	/**月计划待选择年份列表
	 * @param pd
	 * @return
	 */
	List<PageData> getYearList(PageData pd);

	/**本单号列表
	 * @param pd
	 * @return
	 */
	List<PageData> getNoList(PageData pd);

	/**设备类列表
	 * @param pd
	 * @return
	 */
	List<PageData> getClassList(PageData pd);

	/**车间列表
	 * @param pd
	 * @return
	 */
	List<PageData> getWsList(PageData pd);
	
}

