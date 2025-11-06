package org.yy.mapper.dsno1.mdm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 检修计划明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-25
 * 官网：356703572@qq.com
 * @version
 */
public interface EQM_OVERHAULMXMapper{

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
	
	
	/**通过id获取主从表全部数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByTick(PageData pd);
	
	/**查询当日、月、年反馈次数
	 * @param pd
	 * @throws Exception
	 */
	PageData findByCount(PageData pd);
	
	/**更新反馈
	 * @param pd
	 * @throws Exception
	 */
	void editValue(PageData pd);
}

