package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.MaterialRequirementMapper;
import org.yy.service.mm.MaterialRequirementService;

/** 
 * 说明： 物料需求单接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-12
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class MaterialRequirementServiceImpl implements MaterialRequirementService{

	@Autowired
	private MaterialRequirementMapper MaterialRequirementMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		MaterialRequirementMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		MaterialRequirementMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		MaterialRequirementMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return MaterialRequirementMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return MaterialRequirementMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return MaterialRequirementMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		MaterialRequirementMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 根据计划工单获取行号
	 * @param planningWorkOrder_ID
	 * @return
	 */
	public String getRowNumByPlanningWorkOrderID(String PlanningWorkOrderID) {
		return MaterialRequirementMapper.getRowNumByPlanningWorkOrderID(PlanningWorkOrderID);
	}
	/**物料需求单列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getMaterialRequirementList(PageData pd)throws Exception{
		return MaterialRequirementMapper.getMaterialRequirementList(pd);
	}

	/**反写源单下推状态
	 * @param pdSave
	 */
	@Override
	public void updateState(PageData pdSave) throws Exception {
		 MaterialRequirementMapper.updateState(pdSave);
	}

	/**一键反写源单下推状态
	 * @param pd
	 */
	@Override
	public void updateStateAll(PageData pd) throws Exception {
		MaterialRequirementMapper.updateStateAll(pd);
	}
	
	/**根据计划工单id和物料id反写源单是否下推转移状态(Y,N)
	 * @param pd
	 */
	public void updateTransferStateByWorkOrderIdAndMaterialId(PageData pd)throws Exception{
		MaterialRequirementMapper.updateTransferStateByWorkOrderIdAndMaterialId(pd);
	}
	
	/**反写源单是否下推转移状态
	 * @param pd
	 */
	public void updateTransferState(PageData pd)throws Exception{
		MaterialRequirementMapper.updateTransferState(pd);
	}

	/**根据物料需求单id修改是否下推采购状态
	 * @param pd
	 */
	public void updatePushDownPurchaseByMaterialRequirement_ID(PageData pd)throws Exception{
		MaterialRequirementMapper.updatePushDownPurchaseByMaterialRequirement_ID(pd);
	}
	
	/**获取物料需求编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getNumList(PageData pd) throws Exception {
		return MaterialRequirementMapper.getNumList(pd);
	}
	/**
	 * 总物料需求单
	 */
	@Override
	public List<PageData> getTotalMRByMasterPlanningWorkOrderIDlistPage(Page page) throws Exception {
		return MaterialRequirementMapper.getTotalMRByMasterPlanningWorkOrderIDlistPage(page);
	}

	@Override
	public List<PageData> getTotalMRDetailByMasterPlanningWorkOrderID(Page page) throws Exception {
		return MaterialRequirementMapper.getTotalMRDetailByMasterPlanningWorkOrderIDlistPage(page);
	}

	@Override
	public List<PageData> MaterialRequirementlistPage(Page page) throws Exception {
		// TODO Auto-generated method stub
		return MaterialRequirementMapper.MaterialRequirementlistPage(page);
	}
}

