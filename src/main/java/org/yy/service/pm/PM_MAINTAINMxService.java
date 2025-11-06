package org.yy.service.pm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备保养任务(明细)接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-30
 * 官网：356703572@qq.com
 * @version
 */
public interface PM_MAINTAINMxService{

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
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;
	
	/**删除附件
	 * @param pd
	 * @throws Exception
	 */
	public void delFj(PageData pd)throws Exception;
	
	/**更新执行项
	 * @param pd
	 * @throws Exception
	 */
	public void editExecute(PageData pd)throws Exception;
}

