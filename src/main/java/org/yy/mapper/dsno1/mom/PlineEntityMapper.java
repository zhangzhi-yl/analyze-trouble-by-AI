package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 产线实体管理Mapper
 * 作者：YuanYe
 * 时间：2020-01-07
 * 
 * @version
 */
public interface PlineEntityMapper{

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
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	List<PageData> findByFCODE(Page page);
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**
	 * 根据产线类别ID查询产线实体总数
	 * @param pd
	 * @return pd
	 */
	PageData findCountByPlineClassID(PageData pd);
	
	/**
	 * 根据车间ID查询产线实体总数
	 * @param pd
	 * @return pd
	 */
	PageData findCountByAreaID(PageData pd);
}

