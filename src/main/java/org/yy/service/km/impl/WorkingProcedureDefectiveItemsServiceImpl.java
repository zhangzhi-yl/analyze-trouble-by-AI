package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.WorkingProcedureDefectiveItemsMapper;
import org.yy.service.km.WorkingProcedureDefectiveItemsService;

/** 
 * 说明： 工艺路线工序次品项接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-12
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WorkingProcedureDefectiveItemsServiceImpl implements WorkingProcedureDefectiveItemsService{

	@Autowired
	private WorkingProcedureDefectiveItemsMapper WorkingProcedureDefectiveItemsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		WorkingProcedureDefectiveItemsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		WorkingProcedureDefectiveItemsMapper.delete(pd);
	}
	
	/**根据工艺工序id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByWorkingProcedure_ID(PageData pd)throws Exception{
		WorkingProcedureDefectiveItemsMapper.deleteByWorkingProcedure_ID(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		WorkingProcedureDefectiveItemsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return WorkingProcedureDefectiveItemsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return WorkingProcedureDefectiveItemsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return WorkingProcedureDefectiveItemsMapper.findById(pd);
	}
	
	/**通过次品项分类和次品项名称查询数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByDefectiveCategoryAndDefectiveItemName(PageData pd)throws Exception {
		return WorkingProcedureDefectiveItemsMapper.findByDefectiveCategoryAndDefectiveItemName(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		WorkingProcedureDefectiveItemsMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

