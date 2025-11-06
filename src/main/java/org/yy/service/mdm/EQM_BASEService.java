package org.yy.service.mdm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备基础资料接口
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 * @version
 */
public interface EQM_BASEService{

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
	
	/**根据物料关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getBasic(PageData pd)throws Exception;
	
	/**通过设备标识获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByNumber(PageData pd)throws Exception;
	
	/**修改设备运行状态
	 * @param pd
	 * @throws Exception
	 */
	public void stateEdit(PageData pd)throws Exception;
	
	/**检修待办列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listJX(Page page)throws Exception;

	/**获取设备列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getEQMList(PageData pd);
}

