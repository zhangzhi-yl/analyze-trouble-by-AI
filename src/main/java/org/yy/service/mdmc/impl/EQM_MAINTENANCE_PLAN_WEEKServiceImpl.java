package org.yy.service.mdmc.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdmc.EQM_MAINTENANCE_PLAN_WEEKMapper;
import org.yy.service.mdmc.EQM_MAINTENANCE_PLAN_WEEKService;

/** 
 * 说明： 设备保养周计划接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-06-19
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_MAINTENANCE_PLAN_WEEKServiceImpl implements EQM_MAINTENANCE_PLAN_WEEKService{

	@Autowired
	private EQM_MAINTENANCE_PLAN_WEEKMapper eqm_maintenance_plan_weekMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_maintenance_plan_weekMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_maintenance_plan_weekMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_maintenance_plan_weekMapper.edit(pd);
	}
	public void editState(PageData pd)throws Exception{
		eqm_maintenance_plan_weekMapper.editState(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_maintenance_plan_weekMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_maintenance_plan_weekMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_maintenance_plan_weekMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_maintenance_plan_weekMapper.deleteAll(ArrayDATA_IDS);
	}

	/**根据车间部门id查设备列表
	 * @param pdEqm
	 * @return
	 */
	@Override
	public List<PageData> getEqmList(PageData pdEqm) throws Exception {
		return eqm_maintenance_plan_weekMapper.getEqmList(pdEqm);

	}

	/**根据日期（2020-09）查找本月创建过周计划数量
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getNUM(PageData pd) throws Exception {
		return eqm_maintenance_plan_weekMapper.getNUM(pd);

	}

	/**周计划待选择月份列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getMonthList(PageData pd) throws Exception {
		return eqm_maintenance_plan_weekMapper.getMonthList(pd);

	}

	/**本单号列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getNoList(PageData pd) throws Exception {
		return eqm_maintenance_plan_weekMapper.getNoList(pd);
	}

	/**设备类列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getClassList(PageData pd) {
		return eqm_maintenance_plan_weekMapper.getClassList(pd);
	}

	/**车间列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getWsList(PageData pd) throws Exception {
		return eqm_maintenance_plan_weekMapper.getWsList(pd);

	}
	
}

