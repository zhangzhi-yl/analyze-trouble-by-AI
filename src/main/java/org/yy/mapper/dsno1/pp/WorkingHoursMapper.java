package org.yy.mapper.dsno1.pp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工时填报Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-25
 * 官网：356703572@qq.com
 * @version
 */
public interface WorkingHoursMapper{

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
	
	/**获取单件工时
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getHour(PageData pd);
	
	/**提交审核
	 * @param pd
	 * @throws Exception
	 */
	void goAudit(PageData pd);
	
	/**审核
	 * @param pd
	 * @throws Exception
	 */
	void audit(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageAudit(Page page);
}

