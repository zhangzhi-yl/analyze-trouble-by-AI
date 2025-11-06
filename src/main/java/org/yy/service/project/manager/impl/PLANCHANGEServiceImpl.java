package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.dprojectManager.PLANCHANGEMapper;
import org.yy.service.project.manager.PLANCHANGEService;

/** 
 * 说明： 项目计划任务变更接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-09-08
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PLANCHANGEServiceImpl implements PLANCHANGEService{

	@Autowired
	private PLANCHANGEMapper planchangeMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		planchangeMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		planchangeMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		planchangeMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return planchangeMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return planchangeMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return planchangeMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		planchangeMapper.deleteAll(ArrayDATA_IDS);
	}

	
	@Override
	public PageData getStaffPlan(PageData pd) throws Exception {
		return planchangeMapper.getStaffPlan(pd);
	}

	/**反写删除状态
	 * @param pd
	 */
	@Override
	public void upVisible(PageData pd) throws Exception {
		planchangeMapper.upVisible(pd);
	}

	/**获取审批流参数
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getAudit(PageData pd) throws Exception {
		return planchangeMapper.getAudit(pd);
	}

	/**根据姓名查用户表
	 * @param pd
	 * @return
	 */
	@Override
	public PageData findUser(PageData pd) throws Exception {
		return planchangeMapper.findUser(pd);
	}
	
}

