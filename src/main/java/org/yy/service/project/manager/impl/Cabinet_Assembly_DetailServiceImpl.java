package org.yy.service.project.manager.impl;

import java.util.List;

import org.apache.regexp.recompile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.Cabinet_Assembly_DetailMapper;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;

/** 
 * 说明： 装配详情表接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-04-30
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class Cabinet_Assembly_DetailServiceImpl implements Cabinet_Assembly_DetailService{

	@Autowired
	private Cabinet_Assembly_DetailMapper Cabinet_Assembly_DetailMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		Cabinet_Assembly_DetailMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		Cabinet_Assembly_DetailMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		Cabinet_Assembly_DetailMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return Cabinet_Assembly_DetailMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return Cabinet_Assembly_DetailMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return Cabinet_Assembly_DetailMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		Cabinet_Assembly_DetailMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> listJssj(Page page) {
		return Cabinet_Assembly_DetailMapper.JssJdatalistPage(page);
	}

	@Override
	public List<PageData> mesTocStatus(PageData pd) throws Exception {
		return Cabinet_Assembly_DetailMapper.mesTocStatus(pd);
	}
	
	public PageData mesTocStatusCountByYJBS (PageData pd)throws Exception {
		return Cabinet_Assembly_DetailMapper.mesTocStatusCountByYJBS(pd);
	}

	@Override
	public PageData LoadCountByBeginEndTime(PageData pd) throws Exception {
		return Cabinet_Assembly_DetailMapper.LoadCountByBeginEndTime(pd);
	}

	@Override
	public List<PageData> getBeginEndTime() throws Exception {
		return Cabinet_Assembly_DetailMapper.getBeginEndTime();
	}

	@Override
	public List<PageData> findByAssemblyID(PageData findByAssemblyID) {
		
		return Cabinet_Assembly_DetailMapper.findByAssemblyID(findByAssemblyID);
	}

	@Override
	public void editByUser(PageData pdParend) {
		Cabinet_Assembly_DetailMapper.editByUser(pdParend);
		
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.Cabinet_Assembly_DetailService#updateMx(org.yy.entity.PageData)
	 */
	@Override
	public void updateMx(PageData pd) throws Exception {
		Cabinet_Assembly_DetailMapper.updateMx(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.Cabinet_Assembly_DetailService#listXMJSSJ(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listXMJSSJ(Page page) throws Exception {
		return Cabinet_Assembly_DetailMapper.listXMJSSJdatalistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.Cabinet_Assembly_DetailService#listXMJLByIDS(java.lang.String[])
	 */
	@Override
	public List<PageData> listXMJLByIDS(String[] arrayDATA_IDS) throws Exception {
		return Cabinet_Assembly_DetailMapper.listXMJLByIDS(arrayDATA_IDS);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.Cabinet_Assembly_DetailService#listJSFZRByIDS(java.lang.String[])
	 */
	@Override
	public List<PageData> listJSFZRByIDS(String[] arrayDATA_IDS) throws Exception {
		return Cabinet_Assembly_DetailMapper.listJSFZRByIDS(arrayDATA_IDS);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.Cabinet_Assembly_DetailService#listAllPC(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllPC(PageData pd) throws Exception {
		return Cabinet_Assembly_DetailMapper.listAllPC(pd);
	}
}

