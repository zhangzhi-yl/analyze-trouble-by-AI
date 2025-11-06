package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_OVERHAULMXMapper;
import org.yy.service.mdm.EQM_OVERHAULMXService;

/** 
 * 说明： 检修计划明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-05-25
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_OVERHAULMXServiceImpl implements EQM_OVERHAULMXService{

	@Autowired
	private EQM_OVERHAULMXMapper eqm_overhaulmxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_overhaulmxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_overhaulmxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_overhaulmxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_overhaulmxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_overhaulmxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_overhaulmxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_overhaulmxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/***通过id获取主从表全部数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByTick(PageData pd)throws Exception{
		return eqm_overhaulmxMapper.findByTick(pd);
	}
	
	/***查询当日、月、年反馈次数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCount(PageData pd)throws Exception{
		return eqm_overhaulmxMapper.findByCount(pd);
	}
	
	/**更新反馈
	 * @param pd
	 * @throws Exception
	 */
	public void editValue(PageData pd)throws Exception{
		eqm_overhaulmxMapper.editValue(pd);
	}
}

