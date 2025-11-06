package org.yy.service.pp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.WorkorderProcessIOExampleMapper;
import org.yy.service.pp.WorkorderProcessIOExampleService;

@Service
@Transactional //开启事务
public class WorkorderProcessIOExampleServiceImpl implements WorkorderProcessIOExampleService{

	@Autowired
	private WorkorderProcessIOExampleMapper WorkorderProcessIOExampleMapper;
	@Override
	public List<PageData> listByProcessWorkOrderExampleID(String processWorkOrderExample_ID) {
		return WorkorderProcessIOExampleMapper.listByProcessWorkOrderExampleID(processWorkOrderExample_ID);
	}

	/**
	 * 根据工艺工单工序实例id查询投入产出实例列表
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findIOExampleByProcessWorkOrderExampleID(PageData pd){
		return WorkorderProcessIOExampleMapper.findIOExampleByProcessWorkOrderExampleID(pd);
	}
	
	@Override
	public void edit(PageData pqData) {
		WorkorderProcessIOExampleMapper.edit(pqData);
	}
	public List<PageData> list(Page page)throws Exception{
		return WorkorderProcessIOExampleMapper.datalistPage(page);
	}
	@Override
	public List<PageData> listAll(PageData pd) {
		return WorkorderProcessIOExampleMapper.listAll(pd);
	}

	@Override
	public void save(PageData pd) {
		WorkorderProcessIOExampleMapper.save(pd);
		
	}

	@Override
	public void delete(PageData pd) {
		WorkorderProcessIOExampleMapper.delete(pd);
		
	}
	
	/**
	 *	 调整投入产出实例数量及描述
	 * @param pd
	 */
	public void editPlannedQuantityAndFRemarks(PageData pd) throws Exception{
		WorkorderProcessIOExampleMapper.editPlannedQuantityAndFRemarks(pd);
	}

	@Override
	public PageData findById(PageData pd) {
		return WorkorderProcessIOExampleMapper.findById(pd);
	}

	@Override
	public List<PageData> getOutput(PageData appProcessWorkOrderExampleDetailByPK) {
		return WorkorderProcessIOExampleMapper.getOutput(appProcessWorkOrderExampleDetailByPK);
	}

}
