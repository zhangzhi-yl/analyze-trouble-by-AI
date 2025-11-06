package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_BASEMapper;
import org.yy.service.mdm.EQM_BASEService;

/** 
 * 说明： 设备基础资料接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-14
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_BASEServiceImpl implements EQM_BASEService{

	@Autowired
	private EQM_BASEMapper eqm_baseMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_baseMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_baseMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_baseMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_baseMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_baseMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_baseMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_baseMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**根据物料关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getBasic(PageData pd)throws Exception{
		return eqm_baseMapper.getBasic(pd);
	}
	
	/**通过设备标识获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByNumber(PageData pd)throws Exception{
		return eqm_baseMapper.findByNumber(pd);
	}
	
	/**修改设备运行状态
	 * @param pd
	 * @throws Exception
	 */
	public void stateEdit(PageData pd)throws Exception{
		eqm_baseMapper.stateEdit(pd);
	}
	
	/**检修待办列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listJX(Page page)throws Exception{
		return eqm_baseMapper.datalistPageJX(page);
	}

	/**获取设备列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getEQMList(PageData pd) {
		return eqm_baseMapper.getEQMList(pd);
	}
}

