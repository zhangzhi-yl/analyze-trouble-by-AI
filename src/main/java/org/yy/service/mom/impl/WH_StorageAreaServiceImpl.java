package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.WH_StorageAreaMapper;
import org.yy.service.mom.WH_StorageAreaService;

/** 
 * 说明： 库区管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-08
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class WH_StorageAreaServiceImpl implements WH_StorageAreaService{

	@Autowired
	private WH_StorageAreaMapper wh_storageareaMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		wh_storageareaMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		wh_storageareaMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		wh_storageareaMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return wh_storageareaMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return wh_storageareaMapper.listAll(pd);
	}
	
	/**库区列表,下拉选用
	 * @param pd 是否禁用
	 * @throws Exception
	 */
	public List<PageData> storageAreaList(PageData pd)throws Exception{
		return wh_storageareaMapper.storageAreaList(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return wh_storageareaMapper.findById(pd);
	}
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByFCODE(Page page) throws Exception {
		return wh_storageareaMapper.findByFCODE(page);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return wh_storageareaMapper.findCountByCode(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		wh_storageareaMapper.deleteAll(ArrayDATA_IDS);
	}

	/**根据仓库ID查询库区总数 
	 * @param pd
	 * @return pd
	 * @throws Exception
	 */
	@Override
	public PageData findCountByWareHouse(PageData pd) throws Exception {
		return wh_storageareaMapper.findCountByWareHouse(pd);
	}
	
	/**根据工作中心ID查询库区总数 
	 * @param pd
	 * @return pd
	 * @throws Exception
	 */
	@Override
	public PageData findCountByWorkCenter(PageData pd) throws Exception {
		return wh_storageareaMapper.findCountByWorkCenter(pd);
	}
}

