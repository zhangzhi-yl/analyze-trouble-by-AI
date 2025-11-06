package org.yy.service.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 采购申请(明细)接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-20
 * 官网：356703572@qq.com
 * @version
 */
public interface PurchaseApplyForMxService{

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
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;

	/**已选物料列表(分页)
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listSeepush(Page page)throws Exception;
	
	/**已选物料列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listSeepushAll(PageData pd)throws Exception;
	
	/**更新主表ID与状态
	 * @param pd
	 * @throws Exception
	 */
	public void creact(PageData pd)throws Exception;
	
	/**修改数量
	 * @param pd
	 * @throws Exception
	 */
	public void editQty(PageData pd)throws Exception;
}

