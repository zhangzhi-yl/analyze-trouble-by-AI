package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 库区管理接口
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 * @version
 */
public interface WH_StorageAreaService{

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
	
	/**库区列表,下拉选用
	 * @param pd 是否禁用
	 * @throws Exception
	 */
	public List<PageData> storageAreaList(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
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
	
	/**根据仓库ID查询库区总数 
	 * @param pd
	 * @return pd
	 * @throws Exception
	 */
	public PageData findCountByWareHouse(PageData pd)throws Exception;
	
	/**根据工作中心ID查询库区总数
	 * @param pd
	 * @return pd
	 * @throws Exception
	 */
	public PageData findCountByWorkCenter(PageData pd)throws Exception;
}

