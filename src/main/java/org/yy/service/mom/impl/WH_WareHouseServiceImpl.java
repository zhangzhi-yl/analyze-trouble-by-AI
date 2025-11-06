package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.WH_WareHouseMapper;
import org.yy.service.mom.WH_WareHouseService;

/** 
 * 说明： 仓库管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-08
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class WH_WareHouseServiceImpl implements WH_WareHouseService{

	@Autowired
	private WH_WareHouseMapper wh_warehouseMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		wh_warehouseMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		wh_warehouseMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		wh_warehouseMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return wh_warehouseMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return wh_warehouseMapper.listAll(pd);
	}
	
	/**仓库列表（下拉选用）
	 * @param pd 是否禁用
	 * @throws Exception
	 */
	public List<PageData> wareHouseList(PageData pd)throws Exception{
		return wh_warehouseMapper.wareHouseList(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return wh_warehouseMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		wh_warehouseMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过coDe获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCode(PageData pd)throws Exception{
		return wh_warehouseMapper.findByCode(pd);
	}
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByFCODE(Page page) throws Exception {
		return wh_warehouseMapper.findByFCODE(page);
	}

	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return wh_warehouseMapper.findCountByCode(pd);
	}
	
	/**获取仓库列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getWarehouseList(PageData pd) throws Exception {
		return wh_warehouseMapper.getWarehouseList(pd);
	}
}

