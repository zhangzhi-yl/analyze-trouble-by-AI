package org.yy.service.mdmc;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备保养月计划接口
 * 作者：YuanYe
 * 时间：2020-06-22
 * 
 * @version
 */
public interface EQM_MAINTENANCE_PLAN_MONTHService{

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

	/**根据选中年份（2020）查找本年创建过月计划数量
	 * @param pd
	 * @return
	 */
	public PageData getNUM(PageData pd)throws Exception;

	/**月计划待选择年份列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getYearList(PageData pd)throws Exception;

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

