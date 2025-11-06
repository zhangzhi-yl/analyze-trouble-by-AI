package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 库区管理Mapper
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 * @version
 */
public interface WH_StorageAreaMapper{

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
	
	/**库区列表,下拉选用
	 * @param pd 是否禁用
	 * @throws Exception
	 */
	List<PageData> storageAreaList(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	List<PageData> findByFCODE(Page page);
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByCode(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**根据仓库ID查询库区总数 
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByWareHouse(PageData pd);
	
	/**根据工作中心ID查询库区总数 
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByWorkCenter(PageData pd);
}

