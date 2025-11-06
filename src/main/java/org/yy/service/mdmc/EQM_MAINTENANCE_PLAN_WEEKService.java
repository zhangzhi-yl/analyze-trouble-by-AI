package org.yy.service.mdmc;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备保养周计划接口
 * 作者：YuanYe
 * 时间：2020-06-19
 * 
 * @version
 */
public interface EQM_MAINTENANCE_PLAN_WEEKService{

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
	public void editState(PageData pd)throws Exception;
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

	/**根据车间部门id查设备列表
	 * @param pdEqm
	 * @return
	 */
	public List<PageData> getEqmList(PageData pdEqm)throws Exception;

	/**根据日期（2020-09）查找本月创建过周计划数量
	 * @param pd
	 * @return
	 */
	public PageData getNUM(PageData pd)throws Exception;

	/**周计划待选择月份列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getMonthList(PageData pd)throws Exception;

	/**本单号列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getNoList(PageData pd)throws Exception;

	/**设备类列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getClassList(PageData pd)throws Exception;

	/**车间列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getWsList(PageData pd)throws Exception;

	
	
}

