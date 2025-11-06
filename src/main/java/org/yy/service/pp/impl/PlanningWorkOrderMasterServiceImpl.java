package org.yy.service.pp.impl;

import java.util.List;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PlanningWorkOrderMasterMapper;
import org.yy.service.pp.PlanningWorkOrderMasterService;

/** 
 * 说明： 计划工单_主接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-02
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PlanningWorkOrderMasterServiceImpl implements PlanningWorkOrderMasterService{

	@Autowired
	private PlanningWorkOrderMasterMapper PlanningWorkOrderMasterMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PlanningWorkOrderMasterMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PlanningWorkOrderMasterMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PlanningWorkOrderMasterMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PlanningWorkOrderMasterMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PlanningWorkOrderMasterMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PlanningWorkOrderMasterMapper.findById(pd);
	}
	
	/**通过销售订单明细ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findBySalesOrderDetailID(PageData pd)throws Exception{
		return PlanningWorkOrderMasterMapper.findBySalesOrderDetailID(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PlanningWorkOrderMasterMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public void changeStatus(PageData pd) {
		PageData findById = PlanningWorkOrderMasterMapper.findById(pd);
		findById.put("FStatus", pd.getString("FStatus"));
		PlanningWorkOrderMasterMapper.edit(findById);
	}

	@Override
	public Boolean ableCreateMasterPlan(String salesOrderDetailID) {
		boolean able = true;
		PageData pd = new PageData();
		pd.put("SalesOrderDetailID", salesOrderDetailID);
		 List<PageData> listAll = PlanningWorkOrderMasterMapper.listAll(pd);
		 if (CollectionUtil.isNotEmpty(listAll)) {
			return false;
		}
		return able;
	}

	@Override
	public List<PageData> getWorkOrderNumList(PageData pd) {
		
		return PlanningWorkOrderMasterMapper.getWorkOrderNumList(pd);
	}
	
}

