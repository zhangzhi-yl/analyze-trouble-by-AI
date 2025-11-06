package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_CULTURE_PLANMapper;
import org.yy.service.mdm.EQM_CULTURE_PLANService;

/** 
 * 说明： 保修保养计划接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-14
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_CULTURE_PLANServiceImpl implements EQM_CULTURE_PLANService{

	@Autowired
	private EQM_CULTURE_PLANMapper eqm_culture_planMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_culture_planMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_culture_planMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_culture_planMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_culture_planMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_culture_planMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_culture_planMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_culture_planMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception{
		eqm_culture_planMapper.deleteBase(pd);
	}
	
	/**更新发布状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		eqm_culture_planMapper.editState(pd);
	}
	
	/**带办任务列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listTask(Page page)throws Exception{
		return eqm_culture_planMapper.datalistPageTask(page);
	}
	
	/**更新完成状态
	 * @param pd
	 * @throws Exception
	 */
	public void editOver(PageData pd)throws Exception{
		eqm_culture_planMapper.editOver(pd);
	}
}

