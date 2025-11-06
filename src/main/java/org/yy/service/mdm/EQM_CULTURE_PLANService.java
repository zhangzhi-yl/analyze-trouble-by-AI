package org.yy.service.mdm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 保修保养计划接口
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 * @version
 */
public interface EQM_CULTURE_PLANService{

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
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception;
	
	/**更新发布状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception;
	
	/**带办任务列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listTask(Page page)throws Exception;
	
	/**更新完成状态
	 * @param pd
	 * @throws Exception
	 */
	public void editOver(PageData pd)throws Exception;
}

