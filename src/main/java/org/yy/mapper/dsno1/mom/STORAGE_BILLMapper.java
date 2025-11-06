package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 出入存储单据Mapper
 * 作者：YuanYe
 * 时间：2020-01-16
 * 
 * @version
 */
public interface STORAGE_BILLMapper{

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
	
	/**先进先出查询数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByqtyBig(PageData pd);
	
	/**先进后出查询数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByqty(PageData pd);
	
	/**更改状态
	 * @param pd
	 * @throws Exception
	 */
	void editState(PageData pd);
}

