package org.yy.service.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料转移物料拆分表接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-15
 * 官网：356703572@qq.com
 * @version
 */
public interface MaterialTransferSplitService{

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
	
	/**根据关联id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByFRelatedID(PageData pd)throws Exception;
	
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
	
	/**根据单据主表id、辅助属性值、物料编码查询唯一码物料转出拆分明细
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findOutSplitlistByMTA_IDAndSPropKeyAndMaterialNum(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过FUniqueCode和MTADetails_ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByMTADetails_IDAndFUniqueCode(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

