package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 销售订单Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
public interface SALESORDERMapper{

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
	
	/**通过订单编号查询数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByOrderNum(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**审核或反审核销售订单
	 * @param pd
	 */
	void editAudit(PageData pd);

	/**订单号验重
	 * @param pd
	 * @return
	 */
	PageData getRepeatNum(PageData pd);

	/**查询-状态列表
	 * @param pd
	 * @return
	 */
	List<PageData> getStateList(PageData pd);

	/**获取订单编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getOrderNumList(PageData pd);

	/**结束
	 * @param pd
	 */
	void over(PageData pd);

	/**反写销售订单投产数量
	 * @param pd
	 */
	void calFProductionQuantity(PageData pd);

	/**反写销售订单状态
	 * @param pd
	 */
	void calFStatus(PageData pd);
	
}

