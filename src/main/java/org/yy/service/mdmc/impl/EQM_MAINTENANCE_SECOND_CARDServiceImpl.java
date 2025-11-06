package org.yy.service.mdmc.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdmc.EQM_MAINTENANCE_SECOND_CARDMapper;
import org.yy.service.mdmc.EQM_MAINTENANCE_SECOND_CARDService;

/** 
 * 说明： 二级保养卡主表接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-06-23
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_MAINTENANCE_SECOND_CARDServiceImpl implements EQM_MAINTENANCE_SECOND_CARDService{

	@Autowired
	private EQM_MAINTENANCE_SECOND_CARDMapper eqm_maintenance_second_cardMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_maintenance_second_cardMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_maintenance_second_cardMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_maintenance_second_cardMapper.edit(pd);
	}
	public void editState(PageData pd)throws Exception{
		eqm_maintenance_second_cardMapper.editState(pd);
	}
	//修改状态： 未完成、审批中、审核完成、已完成
	public void editState1(PageData pd)throws Exception{
		eqm_maintenance_second_cardMapper.editState1(pd);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_maintenance_second_cardMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_maintenance_second_cardMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_maintenance_second_cardMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_maintenance_second_cardMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

