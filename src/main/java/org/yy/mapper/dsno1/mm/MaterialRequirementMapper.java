package org.yy.mapper.dsno1.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料需求单Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-12
 * 官网：356703572@qq.com
 * @version
 */
public interface MaterialRequirementMapper{

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
	
	/**
	 * 根据计划工单获取行号
	 * @param planningWorkOrder_ID
	 * @return
	 */
	String getRowNumByPlanningWorkOrderID(String planningWorkOrderID);
	
	/**物料需求单列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getMaterialRequirementList(PageData pd);

	/**反写源单下推状态
	 * @param pdSave
	 */
	void updateState(PageData pdSave);

	/**一键反写源单下推状态
	 * @param pd
	 */
	void updateStateAll(PageData pd);
	
	/**根据计划工单id和物料id反写源单是否下推转移状态(Y,N)
	 * @param pd
	 */
	void updateTransferStateByWorkOrderIdAndMaterialId(PageData pd);
	
	/**反写源单是否下推转移状态
	 * @param pd
	 */
	void updateTransferState(PageData pd);

	/**根据物料需求单id修改是否下推采购状态
	 * @param pd
	 */
	void updatePushDownPurchaseByMaterialRequirement_ID(PageData pd);
	
	/**获取物料需求编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getNumList(PageData pd);
	
	/**
	 * 总物料需求单-汇总
	 * @param pd
	 * @return
	 */
	List<PageData> getTotalMRByMasterPlanningWorkOrderIDlistPage(Page page);
	
	/**
	 * 总物料需求单-明细
	 * @param pd
	 * @return
	 */
	List<PageData> getTotalMRDetailByMasterPlanningWorkOrderIDlistPage(Page page);

	List<PageData> MaterialRequirementlistPage(Page page);

}

