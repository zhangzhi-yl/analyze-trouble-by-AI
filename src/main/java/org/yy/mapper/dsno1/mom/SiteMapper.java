package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工厂管理Mapper
 * 作者：YuanYe
 * 时间：2020-01-06
 * 
 * @version
 */
public interface SiteMapper{

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

	/**列表(全部) 有车间的
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listTopAll(PageData pd);

	/**
	 * 检查IDS是否包含车间
	 * @param pd
	 * @return
	 */
	List<PageData> checkIDSInArea(PageData pd);
	/**
	 * 检查IDS是否包含工厂
	 * @param pd
	 * @return
	 */
	List<PageData> checkIDSInSite(PageData pd);

	/**
	 * 根据名称获取数据
	 * @param pd
	 * @return
	 */
	List<PageData> findByName(PageData pd);

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	List<PageData> findByFCODE(Page page);
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	PageData findCountByCode(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
}

