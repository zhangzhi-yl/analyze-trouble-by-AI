package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工作站管理Mapper
 * 作者：YuanYe
 * 时间：2020-01-13
 * 
 * @version
 */
public interface WC_StationMapper{

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
	PageData findCountByCode(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**根据工作中心ID查询工作站总数
	 * @param pd
	 * @return pd
	 */
	PageData findCount(PageData pd);
	
	/**删除文件
	 * @param pd
	 * @throws Exception
	 */
	void delFile(PageData pd)throws Exception;
	
	/**删除图片
	 * @param pd
	 * @throws Exception
	 */
	void delTp(PageData pd)throws Exception;
	
	/**修改作业指导书名称路径字段数据
	 * @param pd
	 * @throws Exception
	 */
	void editFile(PageData pd)throws Exception;
	
	/**获取附件路径
	 * @param pd
	 * @throws Exception
	 */
	PageData findByPath(PageData pd);
	
	/**根据车间和工位名称获取工位ID
	 * @param pd
	 * @throws Exception
	 */
	PageData getStationId(PageData pd);
}

