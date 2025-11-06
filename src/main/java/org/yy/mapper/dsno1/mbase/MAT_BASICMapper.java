package org.yy.mapper.dsno1.mbase;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 物料基础资料Mapper
 * 作者：YuanYe
 * 时间：2020-01-07
 * 
 * @version
 */
public interface MAT_BASICMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**根据物料关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getBasic(PageData pd);

	/**新增归档
	 * @param pd
	 * @throws Exception
	 */
	void saveGD(PageData pd);

	/**获取物料列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getMaterialList(PageData pd);

	/**获取物料详情-销售订单
	 * @param pd
	 * @return
	 */
	PageData getMaterialMessage(PageData pd);

	List<PageData> getListByMatCode(String mAT_CODE);
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByCode(PageData pd);
	
	/**过物料代码获取物料详情
	 * @param pd
	 * @throws Exception
	 */
	PageData getBasicId(PageData pd);
	
	/**车间库存物料列表
	 * @author s
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getWorkShopStockList(PageData pd);

	String getUnitID(PageData detail);

	/**
	 * @param pdM
	 * @return
	 */
	List<PageData> findBySPECS(PageData pdM);
}

