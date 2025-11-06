package org.yy.mapper.dsno1.mdm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备易损件Mapper
 * 作者：YuanYe
 * 时间：2020-06-09
 * 
 * @version
 */
public interface EQM_VULNERABLE_PARTSMapper{

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
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	void deleteBase(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	/**报备列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listBB(PageData pd);
	
	/**修改(设备维护时间)
	 * @param pd
	 * @throws Exception
	 */
	void editDay(PageData pd);
}

