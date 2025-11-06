package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工作站管理接口
 * 作者：YuanYe
 * 时间：2020-01-13
 * 
 * @version
 */
public interface WC_StationService{

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
	
	/**根据工作中心ID查询工作站总数
	 * @param pd
	 * @return pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;
	
	/**删除文件
	 * @param pd
	 * @throws Exception
	 */
	public void delFile(PageData pd)throws Exception;
	
	/**删除图片
	 * @param pd
	 * @throws Exception
	 */
	public void delTp(PageData pd)throws Exception;
	
	/**修改作业指导书名称路径字段数据
	 * @param pd
	 * @throws Exception
	 */
	public void editFile(PageData pd)throws Exception;
	
	/**获取附件路径
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPath(PageData pd)throws Exception;
	
	/**根据车间和工位名称获取工位ID
	 * @param pd
	 * @throws Exception
	 */
	public PageData getStationId(PageData pd)throws Exception;
}

