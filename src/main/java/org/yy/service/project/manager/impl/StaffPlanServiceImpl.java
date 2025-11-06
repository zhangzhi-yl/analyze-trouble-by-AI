package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.StaffPlanMapper;
import org.yy.service.project.manager.StaffPlanService;

/** 
 * 说明： 部门人员计划接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class StaffPlanServiceImpl implements StaffPlanService{

	@Autowired
	private StaffPlanMapper staffplanMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		staffplanMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		staffplanMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		staffplanMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return staffplanMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return staffplanMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return staffplanMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		staffplanMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public void goXiafa(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		staffplanMapper.goXiafa(pd);
	}

	@Override
	public void updateType(PageData pd) throws Exception {
		staffplanMapper.updateType(pd);
	}
	@Override
	public PageData getEndTime(PageData pd) throws Exception {
		return staffplanMapper.getEndTime(pd);
	}
	@Override
	public PageData getStartTime(PageData pd) throws Exception {
		return staffplanMapper.getStartTime(pd);
	}

	/**获得人员超负荷详情
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getOverList(PageData pd) throws Exception {
		return staffplanMapper.getOverList(pd);
	}

	/**下发前验证
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listTest(PageData pd) throws Exception {
		return staffplanMapper.listTest(pd);
	}

	/**获取超负荷数量
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getNUM(PageData pd) throws Exception {
		return staffplanMapper.getNUM(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.StaffPlanService#listx(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listx(PageData pd) throws Exception {
		return staffplanMapper.listx(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.StaffPlanService#deleteAllx(java.lang.String[])
	 */
	@Override
	public void deleteAllx(String[] arrayDATA_IDS) throws Exception {
		staffplanMapper.deleteAllx(arrayDATA_IDS);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.StaffPlanService#getOrder(org.yy.entity.PageData)
	 */
	@Override
	public PageData getOrder(PageData pd) throws Exception {
		return staffplanMapper.getOrder(pd);
	}
}

