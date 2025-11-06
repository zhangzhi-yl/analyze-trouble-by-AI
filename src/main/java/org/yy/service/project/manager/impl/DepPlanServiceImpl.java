package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.DepPlanMapper;
import org.yy.service.project.manager.DepPlanService;

/** 
 * 说明： 部门计划接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-09-03
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class DepPlanServiceImpl implements DepPlanService{

	@Autowired
	private DepPlanMapper depplanMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		depplanMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		depplanMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		depplanMapper.edit(pd);
	}
	public void goXiafa(PageData pd)throws Exception{
		depplanMapper.goXiafa(pd);
	}
	public void goXiafaAll(PageData pd)throws Exception{
		depplanMapper.goXiafaAll(pd);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return depplanMapper.datalistPage(page);
	}
	public List<PageData> listDep(Page page)throws Exception{
		return depplanMapper.datalistPageDep(page);
	}
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return depplanMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return depplanMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		depplanMapper.deleteAll(ArrayDATA_IDS);
	}
	@Override
	public PageData getStartTime(PageData pd) throws Exception {
		return depplanMapper.getStartTime(pd);
	}

	@Override
	public PageData getEndTime(PageData pd) throws Exception {
		return depplanMapper.getEndTime(pd);
	}
	public PageData getPlanTime(PageData pd) throws Exception{
		return depplanMapper.getPlanTime(pd);
	}

	@Override
	public void updateType(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		depplanMapper.updateType(pd);
	}

	@Override
	public void updateTime(PageData pd) throws Exception {
		depplanMapper.updateTime(pd);
	}
	
	/**项目计划部门反馈列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listFeedBack(Page page)throws Exception{
		return depplanMapper.datalistPageFeedBack(page);
	}
	
	/**项目计划部门反馈列表-excel导出
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> excelFeedBack(PageData pd)throws Exception{
		return depplanMapper.excelFeedBack(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.DepPlanService#findDept(org.yy.entity.PageData)
	 */
	@Override
	public PageData findDept(PageData pd) throws Exception {
		return depplanMapper.findDept(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.DepPlanService#listDepDeisgn(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listDepDeisgn(Page page) throws Exception {
		return depplanMapper.listDepDeisgnlistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.DepPlanService#deletex(org.yy.entity.PageData)
	 */
	@Override
	public void deletex(PageData pd) throws Exception {
		depplanMapper.deletex(pd);
		
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.DepPlanService#listx(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listx(PageData pd) throws Exception {
		return depplanMapper.listx(pd);
	}
}

