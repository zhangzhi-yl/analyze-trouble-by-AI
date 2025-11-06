package org.yy.service.mm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料需求单接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-12
 * 官网：356703572@qq.com
 * @version
 */
public interface MaterialRequirementService{

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
	public List<PageData> MaterialRequirementlistPage(Page page) throws Exception;
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

	/**
	 * 根据计划工单获取行号
	 * @param planningWorkOrder_ID
	 * @return
	 */
	public String getRowNumByPlanningWorkOrderID(String PlanningWorkOrderID);
	
	/**物料需求单列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getMaterialRequirementList(PageData pd)throws Exception;

	/**反写源单下推状态
	 * @param pdSave
	 */
	public void updateState(PageData pdSave)throws Exception;

	/**一键反写源单下推状态
	 * @param pd
	 */
	public void updateStateAll(PageData pd)throws Exception;
	
	/**根据计划工单id和物料id反写源单是否下推转移状态(Y,N)
	 * @param pd
	 */
	public void updateTransferStateByWorkOrderIdAndMaterialId(PageData pd)throws Exception;
	
	/**反写源单是否下推转移状态
	 * @param pd
	 */
	public void updateTransferState(PageData pd)throws Exception;
	
	/**根据物料需求单id修改是否下推采购状态
	 * @param pd
	 */
	public void updatePushDownPurchaseByMaterialRequirement_ID(PageData pd)throws Exception;

	/**获取物料需求编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getNumList(PageData pd)throws Exception;
	/**
	 * 总物料需求单-汇总
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getTotalMRByMasterPlanningWorkOrderIDlistPage(Page page)throws Exception;
	
	
	/**
	 * 总物料需求单-明细
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getTotalMRDetailByMasterPlanningWorkOrderID(Page page)throws Exception;
}

