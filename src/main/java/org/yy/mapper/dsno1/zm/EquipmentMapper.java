package org.yy.mapper.dsno1.zm;

import java.util.List;

import org.springframework.stereotype.Component;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 照明Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-08
 * 官网：356703572@qq.com
 * @version
 */
@Component
public interface EquipmentMapper{

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

	/**列表(根据回路ID查询)
	 * @param page
	 * @throws Exception
	 */
	List<PageData> getByLoopdatalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	List<PageData> getMonthList(PageData pd);
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

    List<PageData> getCount(PageData pd);

	List<PageData> getFailCount(PageData pd);

}

