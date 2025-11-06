package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 仓库管理接口
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 * @version
 */
public interface WH_WareHouseService{

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
	
	/**仓库列表（下拉选用）
	 * @param pd 是否禁用
	 * @throws Exception
	 */
	public List<PageData> wareHouseList(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过coDe获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCode(PageData pd)throws Exception;
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> findByFCODE(Page page)throws Exception;
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**获取仓库列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getWarehouseList(PageData pd)throws Exception;
	
}

