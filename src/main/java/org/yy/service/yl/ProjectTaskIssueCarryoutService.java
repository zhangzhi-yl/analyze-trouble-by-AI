package org.yy.service.yl;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 计划任务下发执行接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-02-23
 * 官网：356703572@qq.com
 * @version
 */
public interface ProjectTaskIssueCarryoutService{

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
	
	/**计划制定列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> planList(Page page)throws Exception;
	
	/**下发任务
	 * @param pd
	 * @throws Exception
	 */
	public void editIssue(PageData pd)throws Exception;
	
	/**完成任务
	 * @param pd
	 * @throws Exception
	 */
	public void editOver(PageData pd)throws Exception;
	
	/**执行反馈列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> carryOutList(Page page)throws Exception;
	
	/**通过数据字典名称获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByDICTIONARIESId(PageData pd)throws Exception;
	
	/**通过人员名称判断人员是否存在
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByStaffId(PageData pd)throws Exception;
}

