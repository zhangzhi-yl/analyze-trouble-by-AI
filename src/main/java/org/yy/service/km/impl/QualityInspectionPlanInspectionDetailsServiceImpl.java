package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.QualityInspectionPlanInspectionDetailsMapper;
import org.yy.service.km.QualityInspectionPlanInspectionDetailsService;

/** 
 * 说明： 质检方案检验明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-10
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class QualityInspectionPlanInspectionDetailsServiceImpl implements QualityInspectionPlanInspectionDetailsService{

	@Autowired
	private QualityInspectionPlanInspectionDetailsMapper QualityInspectionPlanInspectionDetailsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		QualityInspectionPlanInspectionDetailsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		QualityInspectionPlanInspectionDetailsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		QualityInspectionPlanInspectionDetailsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return QualityInspectionPlanInspectionDetailsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return QualityInspectionPlanInspectionDetailsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return QualityInspectionPlanInspectionDetailsMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		QualityInspectionPlanInspectionDetailsMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public void editSortKey(PageData pd) throws Exception {
		QualityInspectionPlanInspectionDetailsMapper.editSortKey(pd);
	}
	
}

