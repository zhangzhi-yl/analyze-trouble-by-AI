package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.Unit_InfoMapper;
import org.yy.service.mom.Unit_InfoService;

/** 
 * 说明： 基础单位信息管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-09
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class Unit_InfoServiceImpl implements Unit_InfoService{

	@Autowired
	private Unit_InfoMapper unit_infoMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		unit_infoMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		unit_infoMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		unit_infoMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return unit_infoMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return unit_infoMapper.listAll(pd);
	}
	
	/**获取单位下拉列表，前100
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getUnitList(PageData pd)throws Exception{
		return unit_infoMapper.getUnitList(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return unit_infoMapper.findById(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return unit_infoMapper.findCountByCode(pd);
	}
	
	/**根据单位代码查询单位信息
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUnitCode(PageData pd)throws Exception{
		return unit_infoMapper.findByUnitCode(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		unit_infoMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

