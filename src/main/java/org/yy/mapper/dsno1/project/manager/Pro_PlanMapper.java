package org.yy.mapper.dsno1.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 项目计划Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-03
 * 官网：356703572@qq.com
 * @version
 */
public interface Pro_PlanMapper{

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
	void updateType(PageData pd);
	void goXiafa(PageData pd);
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	List<PageData> datalistPageOverview(Page page);
	/**导出到excel 列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> excellist(PageData pd);
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	List<PageData> listEQU(PageData pd);
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
	void updateTime(PageData pd);

	/**项目总览页获取信息
	 * @param pd
	 * @return
	 */
	PageData getData(PageData pd);

	/**项目进度视角-计划变更记录
	 * @param page
	 * @return
	 */
	List<PageData> listChangelistPage(Page page);

	/**部门项目视角列表
	 * @param page
	 * @return
	 */
	List<PageData> listDeptlistPage(Page page);
	/**
	 * 	部门项目视角-导出到excel 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<PageData> excelListDept(PageData pd);
	/**人员项目视角列表
	 * @param page
	 * @return
	 */
	List<PageData> listPersonlistPage(Page page);

	/**
	 * 人员项目视角-excel导出
	 * @param page
	 * @return
	 */
	List<PageData> excelListPerson(PageData pd);


	/**
	 * @param pd
	 * @return
	 */
	List<PageData> getDepts(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> getUsers(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listAllXMS(PageData pd);
}

