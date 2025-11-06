package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 销售订单明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
public interface SALESORDERDETAILMapper{

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

	/**关联行关闭订单明细
	 * @param pd
	 */
	void deleteMxRelated(PageData pd);

	/**行关闭/反行关闭
	 * @param pd
	 */
	void rowClose(PageData pd);

	/**生成订单明细行号
	 * @param pd
	 * @return
	 */
	PageData getFROWNO(PageData pd);

	/**销售订单明细-物料列表
	 * @param pd
	 * @return
	 */
	List<PageData> listAllMat(PageData pd);

	/**批量选择销售订单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> listAllSelect(String[] arrayDATA_IDS);

	/**批量选择销售订单物料
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> selectAllSale(String[] arrayDATA_IDS);

	/***反写源单下推数量
	 * @param pdSave
	 */
	void calFPushCount(PageData pdSave);

	/**销售订单明细-物料列表-下推发运申请
	 * @param pd
	 * @return
	 */
	List<PageData> listAllMatForward(PageData pd);

	/**发运申请-批量选择销售订单物料
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> selectAllSaleForward(String[] arrayDATA_IDS);

	/**反写源单下推发运申请数量
	 * @param pdSave
	 */
	void calFPushCountForward(PageData pdSave);

	/**一键反写源单下推数量
	 * @param pd
	 */
	void calFPushCountForwardAll(PageData pd);

	/**反写源单下推计划工单数量
	 * @param pdSave
	 */
	void calProductionQuantity(PageData pdSave);
	
}

