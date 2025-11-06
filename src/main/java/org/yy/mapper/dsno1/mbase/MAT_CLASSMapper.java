package org.yy.mapper.dsno1.mbase;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料类Mapper
 * 作者：YuanYe
 * 时间：2020-01-07
 * 
 * @version
 */
public interface MAT_CLASSMapper{

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
	
	/**获取物料类列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> classList(PageData pd);
	
	/**通过类名称获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByClassId(PageData pd);

	/**获取物料类别列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getMatClassList(PageData pd);
}

