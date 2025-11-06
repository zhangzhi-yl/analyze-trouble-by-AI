package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.Unit_UiomMapper;
import org.yy.service.mom.Unit_UiomService;

/** 
 * 说明： 单位比例转换接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-09
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class Unit_UiomServiceImpl implements Unit_UiomService{

	@Autowired
	private Unit_UiomMapper unit_uiomMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		unit_uiomMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		unit_uiomMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		unit_uiomMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return unit_uiomMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return unit_uiomMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return unit_uiomMapper.findById(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return unit_uiomMapper.findCountByCode(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		unit_uiomMapper.deleteAll(ArrayDATA_IDS);
	}

	/**根据基础计量单位ID查询单位比例转换总数
	 * @param pd
	 * @return pd
	 * @throws Exception
	 */
	@Override
	public PageData findCount(PageData pd) throws Exception {
		return unit_uiomMapper.findCount(pd);
	}
	
}

