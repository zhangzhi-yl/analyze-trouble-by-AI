package org.yy.mapper.dsno1.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 部门计划Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-03
 * 官网：356703572@qq.com
 * @version
 */
public interface DepPlanMapper{

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
	void goXiafa(PageData pd);
	void goXiafaAll(PageData pd);
	void updateType(PageData pd);
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	List<PageData> datalistPageDep(Page page);
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	PageData getPlanTime(PageData pd);
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
	PageData getStartTime (PageData pd);
	PageData getEndTime (PageData pd);
	void updateTime(PageData pd);
	
	/**项目计划部门反馈列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPageFeedBack(Page page);
	
	/**项目计划部门反馈列表-excel导出
	 * @param page
	 * @throws Exception
	 */
	List<PageData> excelFeedBack(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	PageData findDept(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listDepDeisgnlistPage(Page page);

	/**
	 * @param pd
	 */
	void deletex(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listx(PageData pd);
}

