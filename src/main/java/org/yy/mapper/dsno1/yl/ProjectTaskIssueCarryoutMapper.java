package org.yy.mapper.dsno1.yl;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 计划任务下发执行Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-02-23
 * 官网：356703572@qq.com
 * @version
 */
public interface ProjectTaskIssueCarryoutMapper{

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
	
	/**计划制定列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPagePlan(Page page);
	
	/**下发任务
	 * @param pd
	 * @throws Exception
	 */
	void editIssue(PageData pd);
	
	/**完成任务
	 * @param pd
	 * @throws Exception
	 */
	void editOver(PageData pd);
	
	/**执行反馈列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageCarryOut(Page page);
	
	/**通过数据字典名称获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByDICTIONARIESId(PageData pd);
	
	/**通过人员名称判断人员是否存在
	 * @param pd
	 * @throws Exception
	 */
	PageData findByStaffId(PageData pd);
}

