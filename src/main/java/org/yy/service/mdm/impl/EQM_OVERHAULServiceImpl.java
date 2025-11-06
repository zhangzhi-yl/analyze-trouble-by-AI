package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_OVERHAULMapper;
import org.yy.service.mdm.EQM_OVERHAULService;

/** 
 * 说明： 设备检修计划接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-18
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_OVERHAULServiceImpl implements EQM_OVERHAULService{

	@Autowired
	private EQM_OVERHAULMapper eqm_overhaulMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_overhaulMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_overhaulMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_overhaulMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_overhaulMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_overhaulMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_overhaulMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_overhaulMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception{
		eqm_overhaulMapper.deleteBase(pd);
	}
	
	/**更新发布状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		eqm_overhaulMapper.editState(pd);
	}
	
	/**每日检修待办列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> dayList(Page page)throws Exception{
		return eqm_overhaulMapper.datalistPageDay(page);
	}
	
	/**每月检修待办列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> monthList(Page page)throws Exception{
		return eqm_overhaulMapper.datalistPageMonth(page);
	}
	
	/**每年检修待办列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> yearList(Page page)throws Exception{
		return eqm_overhaulMapper.datalistPageYear(page);
	}
}

