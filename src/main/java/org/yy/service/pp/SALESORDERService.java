package org.yy.service.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 销售订单接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
public interface SALESORDERService{

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
	
	/**通过订单编号查询数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByOrderNum(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**审核或反审核销售订单
	 * @param pd
	 */
	public void editAudit(PageData pd)throws Exception;

	/**订单号验重
	 * @param pd
	 * @return
	 */
	public PageData getRepeatNum(PageData pd)throws Exception;

	/**查询-状态列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getStateList(PageData pd)throws Exception;

	/**获取订单编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getOrderNumList(PageData pd)throws Exception;

	/**结束
	 * @param pd
	 */
	public void over(PageData pd)throws Exception;

	/**反写销售订单投产数量
	 * @param pd
	 */
	public void calFProductionQuantity(PageData pd)throws Exception;

	/**反写销售订单状态
	 * @param pd
	 */
	public void calFStatus(PageData pd)throws Exception;
	
}

