package org.yy.service.pp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PlanningWorkOrderMapper;
import org.yy.mapper.dsno1.pp.ProcessWorkOrderExampleMapper;
import org.yy.mapper.dsno1.pp.ProcessWorkOrderExample_SopStepMapper;
import org.yy.mapper.dsno1.pp.WorkorderProcessIOExampleMapper;
import org.yy.service.pp.PlanningWorkOrderService;

/**
 * 说明： 计划工单接口实现类 作者：YuanYes Q356703572 时间：2020-11-11 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class PlanningWorkOrderServiceImpl implements PlanningWorkOrderService {

	@Autowired
	private PlanningWorkOrderMapper PlanningWorkOrderMapper;

	@Autowired
	private ProcessWorkOrderExampleMapper processWorkOrderExampleMapper;

	@Autowired
	private WorkorderProcessIOExampleMapper workorderProcessIOExampleMapper;
	
	@Autowired
	private ProcessWorkOrderExample_SopStepMapper processWorkOrderExample_SopStepMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		PlanningWorkOrderMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		PlanningWorkOrderMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		PlanningWorkOrderMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return PlanningWorkOrderMapper.datalistPage(page);
	}
	public List<PageData> taskdatalistPage(Page page) throws Exception{
		return PlanningWorkOrderMapper.taskdatalistPage(page);
	}
	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		// 根据计划工单id，删除计划工单主信息
		PlanningWorkOrderMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 修改状态
	 * 
	 * @throws Exception
	 */
	@Override
	public void changeStatus(PageData pd) throws Exception {
		PageData findById = PlanningWorkOrderMapper.findById(pd);
		findById.put("FStatus", pd.getString("FStatus"));
		PlanningWorkOrderMapper.edit(findById);
	}

	/**
	 * 创建物料转移单时 保存计划工单的转移单状态
	 */
	@Override
	public void saveCreateMRStatus(PageData pg) throws Exception {
		PlanningWorkOrderMapper.saveCreateMRStatus(pg);
	}

	/**
	 * 插入工艺工单实例表数据
	 */
	@Override
	public void insertProcessWorkOrderExample(PageData pg) throws Exception {
		processWorkOrderExampleMapper.save(pg);
	}

	/**
	 * 插入产出实例表数据
	 */
	@Override
	public void insertWorkorderProcessIOExample(PageData pg) throws Exception {
		workorderProcessIOExampleMapper.save(pg);
	}

	/**
	 * 更新计划工单生产进度
	 */
	@Override
	public void updateWorkOrderDistributionProgress(PageData pg) throws Exception {
		PlanningWorkOrderMapper.updateWorkOrderDistributionProgress(pg);
	}

	/**
	 * 根据计划工单id删除工单工序实例
	 * 
	 * @param PlanningWorkOrderID
	 */
	@Override
	public void deleteProcessWorkOrderExampleByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception {
		PlanningWorkOrderMapper.deleteProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);

	}

	/**
	 * 根据计划工单id删除物料需求单
	 * 
	 * @param PlanningWorkOrderID
	 */
	@Override
	public void deleteMaterialRequirementByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception {
		PlanningWorkOrderMapper.deleteMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrderID);

	}

	/**
	 * 根据计划工单id删除工序投入产出
	 * 
	 * @param PlanningWorkOrderID
	 */
	@Override
	public void deleteWorkorderProcessIOExampleByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception {
		PlanningWorkOrderMapper.deleteWorkorderProcessIOExampleByPlanningWorkOrderID(PlanningWorkOrderID);
	}

	/**
	 * 根据计划工单id获取工单工序实例列表
	 * 
	 * @param PlanningWorkOrderID
	 */
	public List<PageData> getListProcessWorkOrderExampleByPlanningWorkOrderID(String PlanningWorkOrderID)
			throws Exception {
		return PlanningWorkOrderMapper.getListProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);

	}

	/**
	 * 根据计划工单id获取物料需求单列表
	 * 
	 * @param PlanningWorkOrderID
	 */
	public List<PageData> getListMaterialRequirementByPlanningWorkOrderID(String PlanningWorkOrderID) throws Exception {
		return PlanningWorkOrderMapper.getListMaterialRequirementByPlanningWorkOrderID(PlanningWorkOrderID);

	}

	/**
	 * 根据计划工单id获取工序投入产出
	 * 
	 * @param PlanningWorkOrderID列表
	 */
	public List<PageData> getListWorkorderProcessIOExampleByPlanningWorkOrderID(String PlanningWorkOrderID)
			throws Exception {
		return PlanningWorkOrderMapper.getListWorkorderProcessIOExampleByPlanningWorkOrderID(PlanningWorkOrderID);
	}

	/**
	 * 获取编号列表-可搜索-前100条
	 * 
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getWorkOrderNumList(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.getWorkOrderNumList(pd);
	}

	@Override
	public List<PageData> listAllByScheduleAndDistributionProgresslistPage(Page page) {
		return PlanningWorkOrderMapper.listAllByScheduleAndDistributionProgresslistPage(page);
	}

	/**计划工单明细-物料列表-采购订单添加明细列表
	 * @param pd
	 * @return
	 */
	public List<PageData> listPurchaseMat(PageData pd)throws Exception{
		return PlanningWorkOrderMapper.listPurchaseMat(pd);
	}
	
	/**
	 * 计划工单明细-物料列表
	 * 
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllMat(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllMat(pd);
	}

	/**
	 * 批量选择计划工单物料
	 * 
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> selectAllGD(String[] arrayDATA_IDS) throws Exception {
		return PlanningWorkOrderMapper.selectAllGD(arrayDATA_IDS);
	}

	/**
	 * 根据主键获取工艺工单实例
	 * 
	 * @param ProcessWorkOrderExample_ID
	 */
	@Override
	public PageData getProcessWorkOrderExampleByPK(String ProcessWorkOrderExample_ID) throws Exception {

		return PlanningWorkOrderMapper.getProcessWorkOrderExampleByPK(ProcessWorkOrderExample_ID);
	}

	/**
	 * 根据时间戳和工位获取列表
	 * 
	 * @param TimeStr
	 * @param StationID
	 * @return
	 */
	@Override
	public List<PageData> listByTimeStrAndStationID(String TimeStr, String StationID) {
		return processWorkOrderExampleMapper.listByTimeStrAndStationID(TimeStr, StationID);
	}

	/**
	 * 根据主计划工单id 和 主计划工单编号 获取列表
	 * 
	 * @param PlanningWorkOrderMaster_ID
	 * @return
	 */
	@Override
	public List<PageData> listByMasterIdAndMasterWorkOrderNum(PageData pd) {
		return PlanningWorkOrderMapper.listByMasterIdAndMasterWorkOrderNum(pd);
	}

	@Override
	public PageData getPlanByNodeIdAndMasterPlanId(String MasterWorkOrder_ID, String NODE_ID) {
		return PlanningWorkOrderMapper.getPlanByNodeIdAndMasterPlanId(MasterWorkOrder_ID, NODE_ID);
	}

	/**
	 * 根据staff id 获取 生产任务
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> appTaskListByStaffId(AppPage page) {
		return PlanningWorkOrderMapper.appTaskListByStaffId(page);
	}
	/**
	 * APP 使用 根据主键获取生产任务详情
	 * @param pd
	 * @return
	 */
	@Override
	public PageData appProcessWorkOrderExampleDetailByPK(PageData pd) {
		return PlanningWorkOrderMapper.appProcessWorkOrderExampleDetailByPK(pd);
	}

	@Override
	public PageData getCountByScheduleAndDistributionProgress(PageData pd1) {
		return PlanningWorkOrderMapper.getCountByScheduleAndDistributionProgress(pd1);
	}

	@Override
	public List<PageData> getRunNodeInfo(PageData pd) {
		return PlanningWorkOrderMapper.getRunNodeInfo(pd);
	}

	@Override
	public void deleteProcessWorkOrderExampleSopStepByPlanningWorkOrderID(String planningWorkOrderID) {
		processWorkOrderExample_SopStepMapper.deleteProcessWorkOrderExampleSopStepByPlanningWorkOrderID(planningWorkOrderID);
	}

	@Override
	public void editStatus(PageData pd) {
		PlanningWorkOrderMapper.editStatus(pd);
	}

	/**生产看板-工序列表，入参：工序名称；未结束的任务
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllGX(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllGXlistPage(page);
	}

	/**生产看板-工序列表角标；未结束的任务
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllGXNUM(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllGXNUM(pd);
	}

	/**工序执行状态数
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getAllNum(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.getAllNum(pd);
	}

	/**toc饼图
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllBT(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllBT(pd);
	}

	/**项目看板-项目预警列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listAllXM(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllXMlistPage(page);
	}

	/**项目看板-历史记录列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listAllXMHis(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllXMHislistPage(page);
	}

	/**项目看板-数字和饼图
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getAllXMNUM(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.getAllXMNUM(pd);
	}

	/**项目看板-数字和饼图
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllBTXM(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllBTXM(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllSJHis(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllSJHis(Page page)throws Exception {
		return PlanningWorkOrderMapper.listAllSJHislistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllSJ(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllSJ(Page page)throws Exception {
		return PlanningWorkOrderMapper.listAllSJlistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllSJNUM(org.yy.entity.PageData)
	 */
	@Override
	public PageData listAllSJNUM(PageData pd) throws Exception{
		return PlanningWorkOrderMapper.listAllSJNUM(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllBTSJ(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllBTSJ(PageData pd) throws Exception{
		return PlanningWorkOrderMapper.listAllBTSJ(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllBTSJX(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllBTSJX(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllBTSJX(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllJYHis(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllJYHis(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllJYHislistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllJY(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllJY(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllJYlistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllJYNUM(org.yy.entity.PageData)
	 */
	@Override
	public PageData listAllJYNUM(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllJYNUM(pd);
	}

	/**
	 * 装配看板-项目预警列表
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAllZP(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllZPlistPage(page);
	}

	/**
	 * 装配看板-待执行、执行中列表
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAllZPRUN(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllZPRUNlistPage(page);
	}

	/**
	 * 装配看板-历史记录列表
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAllZPHIS(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllZPHISlistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllZPNUM(org.yy.entity.PageData)
	 */
	@Override
	public PageData listAllZPNUM(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllZPNUM(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllBTZP(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllBTZP(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllBTZP(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllCGNUM(org.yy.entity.PageData)
	 */
	@Override
	public PageData listAllCGNUM(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllCGNUM(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllBTCG(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllBTCG(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllBTCG(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllCG(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllCG(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllCGlistPage(page);
	}
	@Override
	public List<PageData> listAllCGHis(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllCGHislistPage(page);
	}
	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllKFNUM(org.yy.entity.PageData)
	 */
	@Override
	public PageData listAllKFNUM(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.listAllKFNUM(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllKF(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllKF(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllKFlistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllKF1(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllKF1(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllKFlistPage1(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllKF2(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllKF2(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllKFlistPage2(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listXMGD(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listXMGD(Page page) throws Exception {
		return PlanningWorkOrderMapper.listXMGDlistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listAllGS(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listAllGS(Page page) throws Exception {
		return PlanningWorkOrderMapper.listAllGSlistPage(page);

	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#listSCPage(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listSCPage(Page page) throws Exception {
		return PlanningWorkOrderMapper.listSCPagelistPage(page);
	}

	/**生产排程一键修改计划员
	 * @param pd
	 */
	@Override
	public void editByUser(PageData pd) throws Exception {
		PlanningWorkOrderMapper.editByUser(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PlanningWorkOrderService#getPCList(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> getPCList(PageData pd) throws Exception {
		return PlanningWorkOrderMapper.getPCList(pd);
	}
}
