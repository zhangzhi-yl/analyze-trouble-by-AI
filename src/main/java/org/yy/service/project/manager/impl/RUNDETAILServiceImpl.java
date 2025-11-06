package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.dprojectManager.RUNDETAILMapper;
import org.yy.service.project.manager.RUNDETAILService;

/** 
 * 说明： 执行明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class RUNDETAILServiceImpl implements RUNDETAILService{

	@Autowired
	private RUNDETAILMapper rundetailMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		rundetailMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		rundetailMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		rundetailMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return rundetailMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return rundetailMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return rundetailMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		rundetailMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获得执行中明细数量
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getNum(PageData pd) throws Exception {
		return rundetailMapper.getNum(pd);
	}

	/**反写执行明细结束时间
	 * @param pdZ
	 */
	@Override
	public void editEndTime(PageData pdZ) throws Exception {
		rundetailMapper.editEndTime(pdZ);
	}


	/**查询明细进行中明细信息
	 * @param pd
	 * @return
	 */
	@Override
	public PageData findByIdN(PageData pd) throws Exception {
		return rundetailMapper.findByIdN(pd);
	}

	/**更新任务表实际时间
	 * @param pd
	 */
	@Override
	public void editActual(PageData pd) throws Exception {
		rundetailMapper.editActual(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.RUNDETAILService#upVisible(org.yy.entity.PageData)
	 */
	@Override
	public void upVisible(PageData pd) throws Exception {
		rundetailMapper.upVisible(pd);
	}
	
}

