package org.yy.service.mdmc.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdmc.EQM_MAINTENANCE_THREE_CARDMXMapper;
import org.yy.service.mdmc.EQM_MAINTENANCE_THREE_CARDMXService;

/** 
 * 说明： 三级保养卡明细接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-06-23
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_MAINTENANCE_THREE_CARDMXServiceImpl implements EQM_MAINTENANCE_THREE_CARDMXService{

	@Autowired
	private EQM_MAINTENANCE_THREE_CARDMXMapper eqm_maintenance_three_cardmxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_maintenance_three_cardmxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_maintenance_three_cardmxMapper.delete(pd);
	}
	public void deleteFather(PageData pd)throws Exception{
		eqm_maintenance_three_cardmxMapper.deleteFather(pd);
	}
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_maintenance_three_cardmxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_maintenance_three_cardmxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_maintenance_three_cardmxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_maintenance_three_cardmxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_maintenance_three_cardmxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return eqm_maintenance_three_cardmxMapper.findCount(pd);
	}
	
}

