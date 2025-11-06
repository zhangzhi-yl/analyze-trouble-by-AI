package org.yy.service.mbase;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料基础资料接口
 * 作者：YuanYe
 * 时间：2020-01-07
 * 
 * @version
 */
public interface MAT_BASICService{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**根据物料关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getBasic(PageData pd)throws Exception;
	
	/**新增归档
	 * @param pd
	 * @throws Exception
	 */
	public void saveGD(PageData pd)throws Exception;

	/**获取物料列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getMaterialList(PageData pd)throws Exception;

	/**获取物料详情-销售订单
	 * @param pd
	 * @return
	 */
	public PageData getMaterialMessage(PageData pd)throws Exception;
	/**
	 * 根据物料编码获取物料列表
	 * @param MAT_CODE
	 * @return
	 */
	public List<PageData> getListByMatCode(String MAT_CODE);
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception;
	
	/**通过物料代码获取物料详情
	 * @param pd
	 * @throws Exception
	 */
	public PageData getBasicId(PageData pd)throws Exception;
	
	/**车间库存物料列表
	 * @author s
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getWorkShopStockList(PageData pd)throws Exception;

	public String getUnitID(PageData detail);

	/**
	 * @param pdM
	 * @return
	 */
	public List<PageData> findBySPECS(PageData pdM)throws Exception;
}

