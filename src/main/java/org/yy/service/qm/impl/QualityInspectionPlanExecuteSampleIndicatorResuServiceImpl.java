package org.yy.service.qm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.qm.QualityInspectionPlanExecuteSampleIndicatorResuMapper;
import org.yy.service.qm.QualityInspectionPlanExecuteSampleIndicatorResuService;

/** 
 * 说明： 指标结果接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-09
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class QualityInspectionPlanExecuteSampleIndicatorResuServiceImpl implements QualityInspectionPlanExecuteSampleIndicatorResuService{

	@Autowired
	private QualityInspectionPlanExecuteSampleIndicatorResuMapper QualityInspectionPlanExecuteSampleIndicatorResuMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		QualityInspectionPlanExecuteSampleIndicatorResuMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		QualityInspectionPlanExecuteSampleIndicatorResuMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		QualityInspectionPlanExecuteSampleIndicatorResuMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return QualityInspectionPlanExecuteSampleIndicatorResuMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return QualityInspectionPlanExecuteSampleIndicatorResuMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return QualityInspectionPlanExecuteSampleIndicatorResuMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		QualityInspectionPlanExecuteSampleIndicatorResuMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

