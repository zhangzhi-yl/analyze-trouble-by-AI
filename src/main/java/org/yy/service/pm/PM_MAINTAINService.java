package org.yy.service.pm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备保养任务接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-29
 * 官网：356703572@qq.com
 * @version
 */
public interface PM_MAINTAINService{

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
	
	/**删除附件
	 * @param pd
	 * @throws Exception
	 */
	public void delFj(PageData pd)throws Exception;
	
	/**修改运行状态
	 * @param pd
	 * @throws Exception
	 */
	public void editIssue(PageData pd)throws Exception;
	
	/**获取最近一次生成保养任务的天数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPlanDateNum(PageData pd)throws Exception;
	
	/**获取生成保养任务的数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPlanNum(PageData pd)throws Exception;
}

