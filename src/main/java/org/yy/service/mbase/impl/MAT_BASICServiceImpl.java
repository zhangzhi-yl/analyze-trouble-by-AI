package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.MAT_BASICMapper;
import org.yy.service.mbase.MAT_BASICService;

/** 
 * 说明： 物料基础资料接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-07
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class MAT_BASICServiceImpl implements MAT_BASICService{

	@Autowired
	private MAT_BASICMapper mat_basicMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		mat_basicMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		mat_basicMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		mat_basicMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return mat_basicMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return mat_basicMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return mat_basicMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		mat_basicMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**根据物料关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getBasic(PageData pd)throws Exception{
		return mat_basicMapper.getBasic(pd);
	}
	
	/**新增归档
	 * @param pd
	 * @throws Exception
	 */
	public void saveGD(PageData pd)throws Exception{
		mat_basicMapper.saveGD(pd);
	}

	/**获取物料列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> getMaterialList(PageData pd) throws Exception {
		return mat_basicMapper.getMaterialList(pd);
	}

	/**获取物料详情-销售订单
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getMaterialMessage(PageData pd) throws Exception {
		return mat_basicMapper.getMaterialMessage(pd);
	}

	@Override
	public List<PageData> getListByMatCode(String MAT_CODE) {
		
		return mat_basicMapper.getListByMatCode(MAT_CODE);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return mat_basicMapper.findCountByCode(pd);
	}
	
	/**通过物料代码获取物料详情
	 * @param pd
	 * @throws Exception
	 */
	public PageData getBasicId(PageData pd)throws Exception{
		return mat_basicMapper.getBasicId(pd);
	}
	
	/**车间库存物料列表
	 * @author s
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getWorkShopStockList(PageData pd)throws Exception{
		return mat_basicMapper.getWorkShopStockList(pd);
	}

	@Override
	public String getUnitID(PageData detail) {
		return mat_basicMapper. getUnitID(detail);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mbase.MAT_BASICService#findBySPECS(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> findBySPECS(PageData pdM) throws Exception {
		return mat_basicMapper. findBySPECS(pdM);
	}
}

