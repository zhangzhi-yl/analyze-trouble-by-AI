package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.dprojectManager.PROJECTTASKMapper;
import org.yy.service.project.manager.PROJECTTASKService;

/** 
 * 说明： 项目任务接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PROJECTTASKServiceImpl implements PROJECTTASKService{

	@Autowired
	private PROJECTTASKMapper projecttaskMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		projecttaskMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		projecttaskMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		projecttaskMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return projecttaskMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return projecttaskMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return projecttaskMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		projecttaskMapper.deleteAll(ArrayDATA_IDS);
	}

	/**修改执行状态
	 * @param pd
	 */
	@Override
	public void editState(PageData pd) throws Exception {
		projecttaskMapper.editState(pd);
	}

	/**查询任务未审核通过变更记录数量
	 * @param pd
	 * @return
	 */
	@Override
	public PageData findNum(PageData pd) throws Exception {
		return projecttaskMapper.findNum(pd);
	}

	/**定时更新更新当前计划状态
	 * @param pdMain
	 */
	@Override
	public void editActual(PageData pdMain) throws Exception {
		projecttaskMapper.editActual(pdMain);
	}

	/**任务变更审核通过更新最新计划开始时间、最新计划结束时间、当前计划状态
	 * @param pdMain
	 */
	@Override
	public void editPass(PageData pdMain) throws Exception {
		projecttaskMapper.editPass(pdMain);
	}

	/**获得部门负责人字符串
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getBZNAME(PageData pd) throws Exception {
		return projecttaskMapper.getBZNAME(pd);
	}

	/**判断是否是部长
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getBZUSERNAME(PageData pd) throws Exception {
		return projecttaskMapper.getBZUSERNAME(pd);
	}

	/**任务超期提醒
	 * @param pd1
	 * @return
	 */
	@Override
	public List<PageData> getCQList(PageData pd1) throws Exception {
		return projecttaskMapper.getCQList(pd1);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.projectManager.PROJECTTASKService#editBM(org.yy.entity.PageData)
	 */
	@Override
	public void editBM(PageData pd) throws Exception {
		projecttaskMapper.editBM(pd);
	}

	/**获得甘特图范围
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRange(PageData pd) throws Exception {
		return projecttaskMapper.getRange(pd);
	}

	/**获得甘特图日期列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getRangeList(PageData pd) throws Exception {
		return projecttaskMapper.getRangeList(pd);
	}

	/**获得甘特图任务明细列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getDataList(PageData pd) throws Exception {
		return projecttaskMapper.getDataList(pd);
	}
	
}

