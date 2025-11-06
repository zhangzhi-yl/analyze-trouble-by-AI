package org.yy.mapper.dsno1.mdm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备基础资料Mapper
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 * @version
 */
public interface EQM_BASEMapper{

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
	
	/**根据物料关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getBasic(PageData pd);
	
	/**通过设备标识获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByNumber(PageData pd);
	
	/**修改设备运行状态
	 * @param pd
	 * @throws Exception
	 */
	void stateEdit(PageData pd);
	
	/**检修待办列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageJX(Page page);

	/**获取设备列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getEQMList(PageData pd);
}

