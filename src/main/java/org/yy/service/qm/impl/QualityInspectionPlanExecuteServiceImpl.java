package org.yy.service.qm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.qm.QualityInspectionPlanExecuteMapper;
import org.yy.service.qm.QualityInspectionPlanExecuteService;

/** 
 * 说明： 质检任务接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-12
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class QualityInspectionPlanExecuteServiceImpl implements QualityInspectionPlanExecuteService{

	@Autowired
	private QualityInspectionPlanExecuteMapper QualityInspectionPlanExecuteMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		QualityInspectionPlanExecuteMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		QualityInspectionPlanExecuteMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		QualityInspectionPlanExecuteMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return QualityInspectionPlanExecuteMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return QualityInspectionPlanExecuteMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return QualityInspectionPlanExecuteMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		QualityInspectionPlanExecuteMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public void goLssue(PageData pd) throws Exception {
		QualityInspectionPlanExecuteMapper.goLssue(pd);
	}

	@Override
	public void saveFBeginTime(PageData pd) throws Exception {
		QualityInspectionPlanExecuteMapper.saveFBeginTime(pd);
	}

	@Override
	public void saveFEndTime(PageData pd) throws Exception {
		QualityInspectionPlanExecuteMapper.saveFEndTime(pd);
	}

	@Override
	public List<PageData> listRun(Page page) throws Exception {
		return QualityInspectionPlanExecuteMapper.datalistPageRun(page);
	}

	@Override
	public List<PageData> getGX(PageData pd) throws Exception {
		return QualityInspectionPlanExecuteMapper.getGX(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		return QualityInspectionPlanExecuteMapper.AppList(page);
	}

	@Override
	public PageData getJG(PageData pd) throws Exception {
		return QualityInspectionPlanExecuteMapper.getJG(pd);
	}

	@Override
	public void editJG(PageData pd) throws Exception {
		QualityInspectionPlanExecuteMapper.editJG(pd);
	}

	@Override
	public void editZT(PageData pd) throws Exception {
		QualityInspectionPlanExecuteMapper.editZT(pd);
	}

	@Override
	public List<PageData> listQI(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return QualityInspectionPlanExecuteMapper.listQI(pd);
	}

	@Override
	public List listQIMAT(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return QualityInspectionPlanExecuteMapper.listQIMAT(pd);
	}

	/**柜体质检任务列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listCabinet(Page page) throws Exception {
		return QualityInspectionPlanExecuteMapper.CabinetlistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.qm.QualityInspectionPlanExecuteService#findByIdx(org.yy.entity.PageData)
	 */
	@Override
	public PageData findByIdx(PageData pd) throws Exception {
		return QualityInspectionPlanExecuteMapper.findByIdx(pd);
	}
}

