package org.yy.mapper.dsno1.pm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备保养任务Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-29
 * 官网：356703572@qq.com
 * @version
 */
public interface PM_MAINTAINMapper{

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
	
	/**删除附件
	 * @param pd
	 * @throws Exception
	 */
	void delFj(PageData pd);
	
	/**修改运行状态
	 * @param pd
	 * @throws Exception
	 */
	void editIssue(PageData pd);
	
	/**获取最近一次生成保养任务的天数
	 * @param pd
	 * @throws Exception
	 */
	PageData findByPlanDateNum(PageData pd);
	
	/**获取生成保养任务的数量
	 * @param pd
	 * @throws Exception
	 */
	PageData findByPlanNum(PageData pd);
}

