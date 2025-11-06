package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 项目计划接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-03
 * 官网：356703572@qq.com
 * @version
 */
public interface Pro_PlanService{

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
	public void updateType(PageData pd)throws Exception;
	public void goXiafa(PageData pd)throws Exception;
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	public List<PageData> listOverview(Page page)throws Exception;
	/**导出到excel 列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> excellist(PageData pd)throws Exception;
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	public List<PageData> listEQU(PageData pd)throws Exception;
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
	public void updateTime(PageData pd)throws Exception;

	/**项目总览页获取信息
	 * @param pd
	 * @return
	 */
	public PageData getData(PageData pd)throws Exception;

	/**项目进度视角-计划变更记录
	 * @param page
	 * @return
	 */
	public List<PageData> listChange(Page page)throws Exception;

	/**部门项目视角列表
	 * @param page
	 * @return
	 */
	public List<PageData> listDept(Page page)throws Exception;
	/**
	 * 	部门项目视角-导出到excel 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> excelListDept(PageData pd)throws Exception;
	/**人员项目视角列表
	 * @param page
	 * @return
	 */
	public List<PageData> listPerson(Page page)throws Exception;

	/**
	 * 人员项目视角-excel导出
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> excelListPerson(PageData pd)throws Exception;


	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> getDepts(PageData pd)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> getUsers(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllXMS(PageData pd)throws Exception;
}

