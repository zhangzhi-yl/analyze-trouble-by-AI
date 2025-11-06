package org.yy.mapper.dsno1.dprojectManager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 项目计划任务变更Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-08
 * 官网：356703572@qq.com
 * @version
 */
public interface PLANCHANGEMapper{

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

	/**
	 * @param pd
	 * @return
	 */
	PageData getStaffPlan(PageData pd);

	/**反写删除状态
	 * @param pd
	 */
	void upVisible(PageData pd);

	/**获取审批流参数
	 * @param pd
	 * @return
	 */
	PageData getAudit(PageData pd);

	/**根据姓名查用户表
	 * @param pd
	 * @return
	 */
	PageData findUser(PageData pd);
	
}

