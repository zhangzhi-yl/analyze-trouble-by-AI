package org.yy.mapper.dsno1.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 部门人员计划Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 * @version
 */
public interface StaffPlanMapper{

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
	void goXiafa(PageData pd);
	void updateType(PageData pd);
	PageData getStartTime (PageData pd);
	PageData getEndTime (PageData pd);

	/**获得人员超负荷详情
	 * @param pd
	 * @return
	 */
	List<PageData> getOverList(PageData pd);

	/**下发前验证
	 * @param pd
	 * @return
	 */
	List<PageData> listTest(PageData pd);

	/**获取超负荷数量
	 * @param pd
	 * @return
	 */
	PageData getNUM(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listx(PageData pd);

	/**
	 * @param arrayDATA_IDS
	 */
	void deleteAllx(String[] arrayDATA_IDS);

	/**
	 * @param pd
	 * @return
	 */
	PageData getOrder(PageData pd);
}

