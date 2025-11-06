package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购申请(明细)Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-20
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchaseApplyForMxMapper{

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
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	/**已选物料列表(分页)
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageSeepush(Page page);
	
	/**已选物料列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listSeepushAll(PageData pd);
	
	/**更新主表ID与状态
	 * @param pd
	 * @throws Exception
	 */
	void creact(PageData pd);
	
	/**修改数量
	 * @param pd
	 * @throws Exception
	 */
	void editQty(PageData pd);
}

