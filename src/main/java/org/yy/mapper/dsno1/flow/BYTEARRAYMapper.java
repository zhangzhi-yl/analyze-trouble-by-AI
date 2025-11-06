package org.yy.mapper.dsno1.flow;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 流程图文件Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-01
 * 官网：356703572@qq.com
 * @version
 */
public interface BYTEARRAYMapper{

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
	
	/**根据pid和 FType 删除 
	 * @param pd
	 * @throws Exception
	 */
	void deleteByPidAndFTYPE(PageData pd);
	
	/**修改JSON
	 * @param pd
	 * @throws Exception
	 */
	void editJson(PageData pd);
	
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
	
	/**通过PID获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByPID(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**删除流程图文件
	 * @param pd
	 */
	void deleteByBomId(PageData pd);
	
}

