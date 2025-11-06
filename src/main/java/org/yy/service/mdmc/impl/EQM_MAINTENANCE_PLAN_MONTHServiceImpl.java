package org.yy.service.mdmc.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdmc.EQM_MAINTENANCE_PLAN_MONTHMapper;
import org.yy.service.mdmc.EQM_MAINTENANCE_PLAN_MONTHService;

/** 
 * 说明： 设备保养月计划接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-06-22
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_MAINTENANCE_PLAN_MONTHServiceImpl implements EQM_MAINTENANCE_PLAN_MONTHService{

	@Autowired
	private EQM_MAINTENANCE_PLAN_MONTHMapper eqm_maintenance_plan_monthMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_maintenance_plan_monthMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_maintenance_plan_monthMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_maintenance_plan_monthMapper.edit(pd);
	}
	public void editState(PageData pd)throws Exception{
		eqm_maintenance_plan_monthMapper.editState(pd);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_maintenance_plan_monthMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_maintenance_plan_monthMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_maintenance_plan_monthMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_maintenance_plan_monthMapper.deleteAll(ArrayDATA_IDS);
	}

	/**根据选中年份（2020）查找本年创建过月计划数量
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getNUM(PageData pd)throws Exception{
		return eqm_maintenance_plan_monthMapper.getNUM(pd);

	}

	/**月计划待选择年份列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getYearList(PageData pd)throws Exception{
		return eqm_maintenance_plan_monthMapper.getYearList(pd);
	}

	/**本单号列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getNoList(PageData pd)throws Exception{
		return eqm_maintenance_plan_monthMapper.getNoList(pd);
	}

	/**设备类列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getClassList(PageData pd)throws Exception{
		return eqm_maintenance_plan_monthMapper.getClassList(pd);
	}

	/**车间列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getWsList(PageData pd)throws Exception{
		return eqm_maintenance_plan_monthMapper.getWsList(pd);
	}
	
}

