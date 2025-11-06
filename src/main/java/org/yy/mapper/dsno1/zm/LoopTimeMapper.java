package org.yy.mapper.dsno1.zm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 回路时间Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
public interface LoopTimeMapper{

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

	/**按当前系统时间及回路ID查询该回路下的时间列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllByTime(PageData pd);

	/**按当前系统时间及回路ID查询该回路下的时间列表(开启)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllByTimeStart(PageData pd);

	/**按当前系统时间及回路ID查询该回路下的时间列表(关闭)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllByTimeEnd(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**级联删除
	 * @param pd
	 * @throws Exception
	 */
	void deleteByLoop(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
}

