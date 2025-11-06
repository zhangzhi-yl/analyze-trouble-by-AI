package org.yy.mapper.dsno1.pm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备保养任务(明细)Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-30
 * 官网：356703572@qq.com
 * @version
 */
public interface PM_MAINTAINMxMapper{

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
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	/**删除附件
	 * @param pd
	 * @throws Exception
	 */
	void delFj(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void editExecute(PageData pd);
}

