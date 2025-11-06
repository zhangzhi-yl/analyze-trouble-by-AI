package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 库位管理Mapper
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 * @version
 */
public interface WH_LocationMapper{

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
	
	/**根据code数组查询列表，库存查询用
	 * @param page
	 * @throws Exception
	 */
	List<PageData> locationlistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**库位列表,下拉选用
	 * @param pd 是否禁用，库区id
	 * @throws Exception
	 */
	List<PageData> locationList(PageData pd);
	
	/**手机端库位列表,下拉选用
	 * @param pd 是否禁用，库区id
	 * @throws Exception
	 */
	List<PageData> appList(PageData pd);
	
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
	
	/**根据库区ID查询库位总数
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	/**通过code获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByCode(PageData pd);

	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByCode(PageData pd);
	
	/**获取仓位列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	List<PageData> getLocationList(PageData pd);
	
	/**扫码验证
	 * @param pd
	 * @throws Exception
	 */
	PageData locationScanVerify(PageData pd);
}

