package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.Pro_PlanMapper;
import org.yy.service.project.manager.Pro_PlanService;

/** 
 * 说明： 项目计划接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-09-03
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class Pro_PlanServiceImpl implements Pro_PlanService{

	@Autowired
	private Pro_PlanMapper pro_planMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		pro_planMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		pro_planMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		pro_planMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return pro_planMapper.datalistPage(page);
	}
	public List<PageData> listOverview(Page page)throws Exception{
		return pro_planMapper.datalistPageOverview(page);
	}
	/**导出到excel 列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> excellist(PageData pd)throws Exception{
		return pro_planMapper.excellist(pd);
	}
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return pro_planMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return pro_planMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		pro_planMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public void updateType(PageData pd) throws Exception {
		pro_planMapper.updateType(pd);
	}

	@Override
	public void goXiafa(PageData pd) throws Exception {
		pro_planMapper.goXiafa(pd);
	}

	@Override
	public void updateTime(PageData pd) throws Exception {
		pro_planMapper.updateTime(pd);
	}

	@Override
	public List<PageData> listEQU(PageData pd) throws Exception {
		return pro_planMapper.listEQU(pd);
	}


	/**项目总览页获取信息
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getData(PageData pd) throws Exception {
		return pro_planMapper.getData(pd);
	}

	/**项目进度视角-计划变更记录
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listChange(Page page) throws Exception {
		return pro_planMapper.listChangelistPage(page);
	}

	/**部门项目视角列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listDept(Page page) throws Exception {
		return pro_planMapper.listDeptlistPage(page);
	}
	/**
	 * 	部门项目视角-导出到excel 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> excelListDept(PageData pd)throws Exception{
		return pro_planMapper.excelListDept(pd);
	}
	/**人员项目视角列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listPerson(Page page) throws Exception {
		return pro_planMapper.listPersonlistPage(page);
	}

	/**
	 * 人员项目视角-excel导出
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> excelListPerson(PageData pd)throws Exception{
		return pro_planMapper.excelListPerson(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.Pro_PlanService#getDepts(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> getDepts(PageData pd) throws Exception {
		return pro_planMapper.getDepts(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.Pro_PlanService#listUsers(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> getUsers(PageData pd) throws Exception {
		return pro_planMapper.getUsers(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.Pro_PlanService#listAllXMS(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllXMS(PageData pd) throws Exception {
		return pro_planMapper.listAllXMS(pd);
	}
}

