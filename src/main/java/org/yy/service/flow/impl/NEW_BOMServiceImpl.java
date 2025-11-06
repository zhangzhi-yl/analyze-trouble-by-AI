package org.yy.service.flow.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.flow.NEW_BOMMapper;
import org.yy.service.flow.NEW_BOMService;

/** 
 * 说明： 实例阶段-生产BOM接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-01
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class NEW_BOMServiceImpl implements NEW_BOMService{

	@Autowired
	private NEW_BOMMapper NEW_BOMMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		NEW_BOMMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		NEW_BOMMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		NEW_BOMMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return NEW_BOMMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return NEW_BOMMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return NEW_BOMMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		NEW_BOMMapper.deleteAll(ArrayDATA_IDS);
	}

	/**删除实例生产BOM流程列表
	 * @param pd
	 */
	@Override
	public void deleteByBomId(PageData pd) throws Exception {
		NEW_BOMMapper.deleteByBomId(pd);
	}
	
}

