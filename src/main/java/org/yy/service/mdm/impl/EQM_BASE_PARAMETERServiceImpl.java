package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_BASE_PARAMETERMapper;
import org.yy.service.mdm.EQM_BASE_PARAMETERService;

/** 
 * 说明： 设备基础参数设置接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-14
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_BASE_PARAMETERServiceImpl implements EQM_BASE_PARAMETERService{

	@Autowired
	private EQM_BASE_PARAMETERMapper eqm_base_parameterMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_base_parameterMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_base_parameterMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_base_parameterMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_base_parameterMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_base_parameterMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_base_parameterMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_base_parameterMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception{
		eqm_base_parameterMapper.deleteBase(pd);
	}
	
	/**去修改页面通过基础资料Id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findBySonId(PageData pd)throws Exception{
		return eqm_base_parameterMapper.findBySonId(pd);
	}
}

