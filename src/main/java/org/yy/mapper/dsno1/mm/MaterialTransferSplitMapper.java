package org.yy.mapper.dsno1.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料转移物料拆分表Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-15
 * 官网：356703572@qq.com
 * @version
 */
public interface MaterialTransferSplitMapper{

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
	
	/**根据关联id删除
	 * @param pd
	 * @throws Exception
	 */
	void deleteByFRelatedID(PageData pd);
	
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
	
	/**根据单据主表id、辅助属性值、物料编码查询唯一码物料转出拆分明细
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> findOutSplitlistByMTA_IDAndSPropKeyAndMaterialNum(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**通过FUniqueCode和MTADetails_ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByMTADetails_IDAndFUniqueCode(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
}

