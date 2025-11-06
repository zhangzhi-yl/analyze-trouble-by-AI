package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.QualityInspectionPlanMapper;
import org.yy.service.km.QualityInspectionPlanService;

/** 
 * 说明： 质检方案接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-10
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class QualityInspectionPlanServiceImpl implements QualityInspectionPlanService{

	@Autowired
	private QualityInspectionPlanMapper QualityInspectionPlanMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		QualityInspectionPlanMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		QualityInspectionPlanMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		QualityInspectionPlanMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return QualityInspectionPlanMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return QualityInspectionPlanMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return QualityInspectionPlanMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		QualityInspectionPlanMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**
	 * 修改状态
	 * 
	 * @throws Exception
	 */
	@Override
	public void changeStatus(PageData pd) throws Exception {
		PageData findById = QualityInspectionPlanMapper.findById(pd);
		if ("使用".equals(findById.get("FStatus"))) {
			findById.put("FStatus", "停用");
		} else {
			findById.put("FStatus", "使用");
		}
		QualityInspectionPlanMapper.edit(findById);

	}
	
}

