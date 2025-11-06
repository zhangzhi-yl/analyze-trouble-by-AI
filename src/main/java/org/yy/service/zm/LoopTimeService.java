package org.yy.service.zm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 回路时间接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
public interface LoopTimeService{

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

	/**按当前系统时间及回路ID查询该回路下的时间列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllByTime(PageData pd)throws Exception;

	/**按当前系统时间及回路ID查询该回路下的时间列表(开启)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllByTimeStart(PageData pd)throws Exception;
	/**按当前系统时间及回路ID查询该回路下的时间列表(关闭)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllByTimeEnd(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**级联删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByLoop(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

