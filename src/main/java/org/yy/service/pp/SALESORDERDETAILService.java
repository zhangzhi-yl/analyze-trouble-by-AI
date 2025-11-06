package org.yy.service.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 销售订单明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
public interface SALESORDERDETAILService{

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

	/**关联行关闭订单明细
	 * @param pd
	 */
	public void deleteMxRelated(PageData pd)throws Exception;

	/**行关闭/反行关闭
	 * @param pd
	 */
	public void rowClose(PageData pd)throws Exception;

	/**订单号验重
	 * @param pd
	 * @return
	 */
	public PageData getFROWNO(PageData pd)throws Exception;

	/**销售订单明细-物料列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllMat(PageData pd)throws Exception;

	/**批量选择销售订单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> listAllSelect(String[] arrayDATA_IDS)throws Exception;

	/**批量选择销售订单物料
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> selectAllSale(String[] arrayDATA_IDS)throws Exception;

	/**反写源单下推计划工单数量
	 * @param pdSave
	 */
	public void calFPushCount(PageData pdSave)throws Exception;

	/**销售订单明细-物料列表-下推发运申请
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllMatForward(PageData pd)throws Exception;

	/**发运申请-批量选择销售订单物料
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> selectAllSaleForward(String[] arrayDATA_IDS)throws Exception;

	/**反写源单下推发运申请数量
	 * @param pdSave
	 */
	public void calFPushCountForward(PageData pdSave)throws Exception;

	/**一键反写源单下推数量
	 * @param pd
	 */
	public void calFPushCountForwardAll(PageData pd)throws Exception;
	
	/**反写源单下推计划工单数量
	 * @param pdSave
	 */
	public void calProductionQuantity(PageData pdSave)throws Exception;
}

