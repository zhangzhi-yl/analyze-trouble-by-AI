package org.yy.service.qm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.qm.QualityInspectionPlanExecuteSampleMapper;
import org.yy.service.qm.QualityInspectionPlanExecuteSampleService;

/** 
 * 说明： 样本管理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-07
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class QualityInspectionPlanExecuteSampleServiceImpl implements QualityInspectionPlanExecuteSampleService{

	@Autowired
	private QualityInspectionPlanExecuteSampleMapper QualityInspectionPlanExecuteSampleMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		QualityInspectionPlanExecuteSampleMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		QualityInspectionPlanExecuteSampleMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		QualityInspectionPlanExecuteSampleMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return QualityInspectionPlanExecuteSampleMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return QualityInspectionPlanExecuteSampleMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return QualityInspectionPlanExecuteSampleMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		QualityInspectionPlanExecuteSampleMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * v1-ccg-20210607-获取所有未绑定合样code的样品列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getAllNoBindComposeCode4SampleList(PageData pd) {
		return QualityInspectionPlanExecuteSampleMapper.getAllNoBindComposeCode4SampleList(pd);
	}
	
}

