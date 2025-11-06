package org.yy.service.mdmc.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdmc.EQM_MAINTENANCE_ITEM_WEEKMapper;
import org.yy.service.mdmc.EQM_MAINTENANCE_ITEM_WEEKService;

/** 
 * 说明： 设备保养项周模板接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-06-18
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_MAINTENANCE_ITEM_WEEKServiceImpl implements EQM_MAINTENANCE_ITEM_WEEKService{

	@Autowired
	private EQM_MAINTENANCE_ITEM_WEEKMapper eqm_maintenance_item_weekMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_maintenance_item_weekMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_maintenance_item_weekMapper.delete(pd);
	}
	public void deleteByFatherID(PageData pd)throws Exception{
		eqm_maintenance_item_weekMapper.deleteByFatherID(pd);
	}
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_maintenance_item_weekMapper.edit(pd);
	}
	public List<PageData> findByFatherId(PageData pd)throws Exception{
		return eqm_maintenance_item_weekMapper.findByFatherId(pd);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_maintenance_item_weekMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_maintenance_item_weekMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_maintenance_item_weekMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_maintenance_item_weekMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return eqm_maintenance_item_weekMapper.findCount(pd);
	}
	
}

