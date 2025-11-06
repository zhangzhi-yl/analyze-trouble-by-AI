package org.yy.mapper.dsno1.pp;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 计划工单Mapper 作者：YuanYes QQ356703572 时间：2020-11-11 官网：356703572@qq.com
 * 
 * @version
 */
public interface PlanningWorkOrderMapper {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	List<PageData> taskdatalistPage(Page page);

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);

	/**
	 * 列表(根据下发状态和排程状态获取)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllByScheduleAndDistributionProgresslistPage(Page pd);

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * 更新计划工单生产进度
	 */
	void updateWorkOrderDistributionProgress(PageData pg);

	/**
	 * 更新工单层级
	 */
	void updateTheOrderLevel(PageData pg);

	/**
	 * 创建物料转移单时 保存计划工单的转移单状态
	 */
	void saveCreateMRStatus(PageData pg);

	/**
	 * 根据计划工单id删除工单工序实例
	 * 
	 * @param PlanningWorkOrderID
	 */
	void deleteProcessWorkOrderExampleByPlanningWorkOrderID(String PlanningWorkOrderID);

	/**
	 * 根据计划工单id删除物料需求单
	 * 
	 * @param PlanningWorkOrderID
	 */
	void deleteMaterialRequirementByPlanningWorkOrderID(String PlanningWorkOrderID);

	/**
	 * 根据计划工单id删除工序投入产出
	 * 
	 * @param PlanningWorkOrderID
	 */
	void deleteWorkorderProcessIOExampleByPlanningWorkOrderID(String PlanningWorkOrderID);

	/**
	 * 根据计划工单id获取工序投入产出
	 * 
	 * @param PlanningWorkOrderID列表
	 */
	List<PageData> getListWorkorderProcessIOExampleByPlanningWorkOrderID(String planningWorkOrderID);

	/**
	 * 根据计划工单id获取物料需求单列表
	 * 
	 * @param PlanningWorkOrderID
	 */
	List<PageData> getListMaterialRequirementByPlanningWorkOrderID(String planningWorkOrderID);

	/**
	 * 根据计划工单id获取工单工序实例列表
	 * 
	 * @param PlanningWorkOrderID
	 */
	List<PageData> getListProcessWorkOrderExampleByPlanningWorkOrderID(String planningWorkOrderID);

	/**
	 * 根据主键获取工艺工单工序实例
	 * 
	 * @param ProcessWorkOrderExample_ID
	 */
	PageData getProcessWorkOrderExampleByPK(String ProcessWorkOrderExample_ID);

	/**
	 * 获取编号列表-可搜索-前100条
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> getWorkOrderNumList(PageData pd);

	/**
	 * 计划工单明细-物料列表-采购订单添加明细列表
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> listPurchaseMat(PageData pd);
	
	/**
	 * 计划工单明细-物料列表
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> listAllMat(PageData pd);

	/**
	 * 批量选择计划工单物料
	 * 
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> selectAllGD(String[] arrayDATA_IDS);

	/**
	 * 根据主计划工单id 和 主计划工单编号 获取列表
	 * 
	 * @param PlanningWorkOrderMaster_ID
	 * @return
	 */
	List<PageData> listByMasterIdAndMasterWorkOrderNum(PageData pd);
	/**
	 * 根据节点id和主计划工单id获取当前选中的子计划工单
	 * @param masterWorkOrder_ID
	 * @param nODE_ID
	 * @return
	 */
	PageData getPlanByNodeIdAndMasterPlanId(String MasterWorkOrder_ID, String NODE_ID);
	
	/**
	 * APP 使用 根据 STAFFID 获取 生产任务
	 * @param pd
	 * @return
	 */
	List<PageData> appTaskListByStaffId(AppPage page);
	
	/**
	 * APP 使用 根据主键获取生产任务详情
	 * @param pd
	 * @return
	 */
	PageData appProcessWorkOrderExampleDetailByPK(PageData pd);
	/**
	 * 根据排程状态和下发状态获取数量
	 * @param pd
	 * @return
	 */
	PageData getCountByScheduleAndDistributionProgress(PageData pd);
	
	/**
	 * 根据任务id获取要执行的node信息
	 * @param pd
	 * @return
	 */
	List<PageData> getRunNodeInfo(PageData pd);

	void editStatus(PageData pd);

	/**生产看板-工序列表，入参：工序名称；未结束的任务
	 * @param pd
	 * @return
	 */
	List<PageData> listAllGXlistPage(Page page);

	/**生产看板-工序列表角标；未结束的任务
	 * @param pd
	 * @return
	 */
	List<PageData> listAllGXNUM(PageData pd);

	/**工序执行状态数
	 * @param pd
	 * @return
	 */
	PageData getAllNum(PageData pd);

	/**toc饼图
	 * @param pd
	 * @return
	 */
	List<PageData> listAllBT(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllXMlistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllXMHislistPage(Page page);

	/**
	 * @param pd
	 * @return
	 */
	PageData getAllXMNUM(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listAllBTXM(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllSJHislistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllBTXMlistPage(Page page);

	/**
	 * @param pd
	 * @return
	 */
	PageData listAllSJNUM(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listAllBTSJ(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listAllBTSJX(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllSJlistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllJYHislistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllJYlistPage(Page page);
	/**
	 * @param pd
	 * @return
	 */
	PageData listAllJYNUM(PageData pd);
	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllZPlistPage(Page page);
	List<PageData> listAllZPRUNlistPage(Page page);
	List<PageData> listAllZPHISlistPage(Page page);
	PageData listAllZPNUM(PageData pd);
	List<PageData>  listAllBTZP(PageData pd);
	PageData listAllCGNUM(PageData pd);
	List<PageData>  listAllBTCG(PageData pd);
	List<PageData> listAllCGlistPage(Page page);
	List<PageData> listAllCGHislistPage(Page page);
	/**
	 * @param pd
	 * @return
	 */
	PageData listAllKFNUM(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllKFlistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllKFlistPage1(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listAllKFlistPage2(Page page);
	List<PageData> listXMGDlistPage(Page page);
	List<PageData> listAllGSlistPage(Page page);
	List<PageData> listSCPagelistPage(Page page);

	/**生产排程一键修改计划员
	 * @param pd
	 */
	void editByUser(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> getPCList(PageData pd);
}
