package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.Cabinet_BOM_changeMapper;
import org.yy.service.project.manager.Cabinet_BOM_changeService;

/** 
 * 说明： BOM变更记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-26
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class Cabinet_BOM_changeServiceImpl implements Cabinet_BOM_changeService{

	@Autowired
	private Cabinet_BOM_changeMapper cabinet_bom_changeMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		cabinet_bom_changeMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		cabinet_bom_changeMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		cabinet_bom_changeMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return cabinet_bom_changeMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return cabinet_bom_changeMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return cabinet_bom_changeMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		cabinet_bom_changeMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

