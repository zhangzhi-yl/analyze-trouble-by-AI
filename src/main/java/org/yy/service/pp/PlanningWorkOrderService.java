package org.yy.service.pp;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.List;

/**
 * 说明： 计划工单接口 作者：YuanYes QQ356703572 时间：2020-11-11 官网：356703572@qq.com
 * 
 * @version
 */
public interface PlanningWorkOrderService {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception;

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception;

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception;

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception;
	
	public List<PageData> taskdatalistPage(Page page) throws Exception;

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception;

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception;

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception;

	/**
	 * 修改状态
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void changeStatus(PageData pd) throws Exception;

	/**
	 * 创建物料转移单时
	 * 保存计划工单的转移单状态
	 */
	public void saveCreateMRStatus(PageData pg) throws Exception;

	/**
	 * 插入工艺工单实例表数据
	 */
	public void insertProcessWorkOrderExample(PageData pg) throws Exception;

	/**
	 * 插入产出实例表数据
	 */
	public void insertWorkorderProcessIOExample(PageData pg) throws Exception;
	
	/**
	 * 更新计划工单生产进度
	 */
	public void updateWorkOrderDistributionProgress(PageData pg) throws Exception;
	
	/**
	 * 根据计划工单id删除工单工序实例
	 * 
	 * @param PlanningWorkOrderID
	 */
	public void deleteProcessWorkOrderExampleByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception;
	/**
	 * 根据计划工单id删除物料需求单
	 * 
	 * @param PlanningWorkOrderID
	 */
	public void deleteMaterialRequirementByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception;

	/**
	 * 根据计划工单id删除工序投入产出
	 * 
	 * @param PlanningWorkOrderID
	 */
	public void deleteWorkorderProcessIOExampleByPlanningWorkOrderID(String PlanningWorkOrderID)throws Exception;
	
	
	/**
	 * 根据计划工单id获取工单工序实例列表
	 * 
	 * @param PlanningWorkOrderID
	 */
	public List<PageData>  getListProcessWorkOrderExampleByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception;
	/**
	 * 根据主键获取工艺工单实例
	 * 
	 * @param ProcessWorkOrderExample_ID
	 */
	public PageData getProcessWorkOrderExampleByPK (String ProcessWorkOrderExample_ID) throws Exception;

	/**
	 * 根据计划工单id获取物料需求单列表
	 * 
	 * @param PlanningWorkOrderID
	 */
	public List<PageData>  getListMaterialRequirementByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception;
	/**
	 * 根据计划工单id获取工序投入产出
	 * 
	 * @param PlanningWorkOrderID列表
	 */
	public List<PageData>  getListWorkorderProcessIOExampleByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception;

	/**获取编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getWorkOrderNumList(PageData pd)throws Exception;

	public List<PageData> listAllByScheduleAndDistributionProgresslistPage(Page page);

	/**计划工单明细-物料列表-采购订单添加明细列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listPurchaseMat(PageData pd)throws Exception;

	/**计划工单明细-物料列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllMat(PageData pd)throws Exception;

	/**批量选择计划工单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> selectAllGD(String[] arrayDATA_IDS)throws Exception;

	/**
	 * 根据时间戳和工位获取列表
	 * @param TimeStr
	 * @param StationID
	 * @return
	 */
	public List<PageData> listByTimeStrAndStationID(String TimeStr,String StationID);
	/**
	 * 根据主计划工单id 和 主计划工单编号 获取列表
	 * @param PlanningWorkOrderMaster_ID
	 * @return
	 */
	public List<PageData> listByMasterIdAndMasterWorkOrderNum(PageData pd);

	public PageData getPlanByNodeIdAndMasterPlanId(String MasterWorkOrder_ID, String NODE_ID);
	
	/**
	 * APP 根据staff id 获取 生产任务
	 * @param page
	 * @return
	 */
	public List<PageData> appTaskListByStaffId(AppPage page);
	/**
	 * APP 使用 根据主键获取生产任务详情
	 * @param pd
	 * @return
	 */
	public PageData appProcessWorkOrderExampleDetailByPK(PageData pd);

	public PageData getCountByScheduleAndDistributionProgress(PageData pd1);
	
	public List<PageData> getRunNodeInfo(PageData pd);

	public void deleteProcessWorkOrderExampleSopStepByPlanningWorkOrderID(String planningWorkOrderID);

	public void editStatus(PageData pd);

	/**生产看板-工序列表，入参：工序名称；未结束的任务
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllGX(Page page)throws Exception;

	/**生产看板-工序列表角标；未结束的任务
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllGXNUM(PageData pd)throws Exception;

	/**工序执行状态数
	 * @param pd
	 * @return
	 */
	public PageData getAllNum(PageData pd)throws Exception;

	/**toc饼图
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllBT(PageData pd)throws Exception;

	/**项目看板-项目预警列表
	 * @param page
	 * @return
	 */
	public List<PageData> listAllXM(Page page)throws Exception;

	/**项目看板-历史记录列表
	 * @param page
	 * @return
	 */
	public List<PageData> listAllXMHis(Page page)throws Exception;

	/**项目看板-数字和饼图
	 * @param pd
	 * @return
	 */
	public PageData getAllXMNUM(PageData pd)throws Exception;

	/**项目看板-数字和饼图
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllBTXM(PageData pd)throws Exception;

	/**
	 * 设计看板-历史记录列表
	 * @throws Exception
	 */
	public List<PageData> listAllSJHis(Page page)throws Exception;
	/**
	 * 设计看板-柜体预警列表
	 * @throws Exception
	 */
	public List<PageData> listAllSJ(Page page)throws Exception;
	/**
	 * 设计看板-数字和饼图
	 * @throws Exception
	 */
	public PageData listAllSJNUM(PageData pd)throws Exception;
	/**
	 * 设计看板-数字和饼图
	 * @throws Exception
	 */
	public List<PageData> listAllBTSJ(PageData pd)throws Exception;
	/**
	 * 设计看板-数字和饼图
	 * @throws Exception
	 */
	public List<PageData> listAllBTSJX(PageData pd)throws Exception;
	
	
	/**
	 * 检验看板-历史记录列表
	 * @throws Exception
	 */
	public List<PageData> listAllJYHis(Page page)throws Exception;
	/**
	 * 设计看板-柜体预警列表
	 * @throws Exception
	 */
	public List<PageData> listAllJY(Page page)throws Exception;
	/**
	 * 检验看板-今日任务列表
	 * @throws Exception
	 */
	public PageData listAllJYNUM(PageData pd)throws Exception;
	/**
	 * 装配看板-项目预警列表
	 * @throws Exception
	 */
	public List<PageData> listAllZP(Page page)throws Exception;
	/**
	 * 装配看板-待执行、执行中列表
	 * @throws Exception
	 */
	public List<PageData> listAllZPRUN(Page page)throws Exception;
	/**
	 * 装配看板-历史记录列表
	 * @throws Exception
	 */
	public List<PageData> listAllZPHIS(Page page)throws Exception;
	/**
	 * 装配看板-数字和饼图
	 * @throws Exception
	 */
	public PageData listAllZPNUM(PageData pd)throws Exception;
	public List<PageData> listAllBTZP(PageData pd)throws Exception;
	public PageData listAllCGNUM(PageData pd)throws Exception;
	public List<PageData> listAllBTCG(PageData pd)throws Exception;
	public List<PageData> listAllCG(Page page)throws Exception;
	public List<PageData> listAllCGHis(Page page)throws Exception;
	/**库房看板-数字
	 * @param pd
	 * @return
	 */
	public PageData listAllKFNUM(PageData pd)throws Exception;

	/**库房看板-请料列表
	 * @param page
	 * @return
	 */
	public List<PageData> listAllKF(Page page)throws Exception;
	public List<PageData> listAllKF1(Page page)throws Exception;
	public List<PageData> listAllKF2(Page page)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listXMGD(Page page)throws Exception;
	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listAllGS(Page page)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listSCPage(Page page)throws Exception;

	/**生产排程一键修改计划员
	 * @param pd
	 */
	public void editByUser(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> getPCList(PageData pd)throws Exception;
}
