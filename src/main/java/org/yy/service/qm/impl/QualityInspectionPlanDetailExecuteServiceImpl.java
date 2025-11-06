package org.yy.service.qm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.qm.QualityInspectionPlanDetailExecuteMapper;
import org.yy.service.qm.QualityInspectionPlanDetailExecuteService;

/** 
 * 说明： 质检任务明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-14
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class QualityInspectionPlanDetailExecuteServiceImpl implements QualityInspectionPlanDetailExecuteService{

	@Autowired
	private QualityInspectionPlanDetailExecuteMapper QualityInspectionPlanDetailExecuteMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		QualityInspectionPlanDetailExecuteMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		QualityInspectionPlanDetailExecuteMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		QualityInspectionPlanDetailExecuteMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return QualityInspectionPlanDetailExecuteMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return QualityInspectionPlanDetailExecuteMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return QualityInspectionPlanDetailExecuteMapper.findById(pd);
	}
	public PageData findBySortKeyList_ID(PageData pd)throws Exception{
		return QualityInspectionPlanDetailExecuteMapper.findBySortKeyList_ID(pd);
	}
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		QualityInspectionPlanDetailExecuteMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public String getMaxSortKey(PageData pd) throws Exception {
		return QualityInspectionPlanDetailExecuteMapper.getMaxSortKey(pd);
	}

	@Override
	public void goFinish(PageData pd) throws Exception {
		QualityInspectionPlanDetailExecuteMapper.goFinish(pd);
	}
	public PageData getLastMx(PageData pd) throws Exception{
		return QualityInspectionPlanDetailExecuteMapper.getLastMx(pd);
	}

	@Override
	public void ImplementQI(PageData pd) throws Exception {
		QualityInspectionPlanDetailExecuteMapper.ImplementQI(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		// TODO 自动生成的方法存根
		return QualityInspectionPlanDetailExecuteMapper.AppList(page);
	}

	@Override
	public void editSortKey(PageData pd) throws Exception {
		QualityInspectionPlanDetailExecuteMapper.editSortKey(pd);
	}

	@Override
	public void editBadness(PageData pd) throws Exception {
		QualityInspectionPlanDetailExecuteMapper.editBadness(pd);
	};
}

