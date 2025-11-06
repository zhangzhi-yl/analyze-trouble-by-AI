package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.WorkingProcedureExampleMapper;
import org.yy.service.km.WorkingProcedureExampleService;

/** 
 * 说明： 工艺工序实例接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WorkingProcedureExampleServiceImpl implements WorkingProcedureExampleService{

	@Autowired
	private WorkingProcedureExampleMapper WorkingProcedureExampleMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		WorkingProcedureExampleMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		WorkingProcedureExampleMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		WorkingProcedureExampleMapper.edit(pd);
	}
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editStatus(PageData pd)throws Exception{
		WorkingProcedureExampleMapper.editStatus(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return WorkingProcedureExampleMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return WorkingProcedureExampleMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return WorkingProcedureExampleMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		WorkingProcedureExampleMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 根据bom id获取 工艺工序实例列表
	 * @param bomNum
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getWorkingProcedureExampleListByBOMId(String BOM_ID) throws Exception {
		return WorkingProcedureExampleMapper.getWorkingProcedureExampleListByBOMId(BOM_ID);
	}

	/**获取工序流程图列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllFlow(PageData pd) throws Exception {
		return WorkingProcedureExampleMapper.listAllFlow(pd);
	}

	/**删除BOM关联删除实例工序
	 * @param pd
	 */
	@Override
	public void deleteByBomId(PageData pd) throws Exception {
		WorkingProcedureExampleMapper.deleteByBomId(pd);
	}
	
}

