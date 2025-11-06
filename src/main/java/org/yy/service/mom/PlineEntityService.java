package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 产线实体管理接口
 * 作者：YuanYe
 * 时间：2020-01-07
 * 
 * @version
 */
public interface PlineEntityService{

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
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> findByFCODE(Page page)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**
	 * 根据产线类别ID查询产线实体总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByPlineClassID(PageData pd)throws Exception;
	
	/**
	 * 根据车间ID查询产线实体总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByAreaID(PageData pd)throws Exception;
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception;
}

