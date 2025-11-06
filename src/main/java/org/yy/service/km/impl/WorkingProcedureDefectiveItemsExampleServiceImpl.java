package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.WorkingProcedureDefectiveItemsExampleMapper;
import org.yy.service.km.WorkingProcedureDefectiveItemsExampleService;

/** 
 * 说明： 工艺工序实例次品项列表接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-19
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WorkingProcedureDefectiveItemsExampleServiceImpl implements WorkingProcedureDefectiveItemsExampleService{

	@Autowired
	private WorkingProcedureDefectiveItemsExampleMapper WorkingProcedureDefectiveItemsExampleMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		WorkingProcedureDefectiveItemsExampleMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		WorkingProcedureDefectiveItemsExampleMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		WorkingProcedureDefectiveItemsExampleMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return WorkingProcedureDefectiveItemsExampleMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return WorkingProcedureDefectiveItemsExampleMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return WorkingProcedureDefectiveItemsExampleMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		WorkingProcedureDefectiveItemsExampleMapper.deleteAll(ArrayDATA_IDS);
	}


	/**删除次品项列表
	 * @param pd
	 */
	@Override
	public void deleteByBomId(PageData pd) throws Exception {
		WorkingProcedureDefectiveItemsExampleMapper.deleteByBomId(pd);
	}
	
}

