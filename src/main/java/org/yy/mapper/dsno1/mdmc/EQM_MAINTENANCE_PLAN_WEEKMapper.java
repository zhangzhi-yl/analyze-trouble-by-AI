package org.yy.mapper.dsno1.mdmc;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备保养周计划Mapper
 * 作者：YuanYe
 * 时间：2020-06-19
 * 
 * @version
 */
public interface EQM_MAINTENANCE_PLAN_WEEKMapper{

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
	void editState(PageData pd);
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

	/**根据车间部门id查设备列表
	 * @param pdEqm
	 * @return
	 */
	List<PageData> getEqmList(PageData pdEqm);

	/**根据日期（2020-09）查找本月创建过周计划数量
	 * @param pd
	 * @return
	 */
	PageData getNUM(PageData pd);

	/**周计划待选择月份列表
	 * @param pd
	 * @return
	 */
	List<PageData> getMonthList(PageData pd);

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

