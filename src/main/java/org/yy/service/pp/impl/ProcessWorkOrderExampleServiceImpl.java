package org.yy.service.pp.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.ProcessWorkOrderExampleMapper;
import org.yy.service.pp.ProcessWorkOrderExampleService;

@Service
@Transactional
public class ProcessWorkOrderExampleServiceImpl implements ProcessWorkOrderExampleService {
	@Autowired
	private ProcessWorkOrderExampleMapper ProcessWorkOrderExampleMapper;

	@Override
	public void updateExecutorIDByWPAndPlanningWorkOrderID(String planningWorkOrder_ID, String WP, String ExecutorID,
			String FStation) {
		ProcessWorkOrderExampleMapper.updateExecutorIDByWPAndPlanningWorkOrderID(planningWorkOrder_ID, WP, ExecutorID,FStation);
	}

	@Override
	public void updateByConditionMap(Map<String, Object> pd) {
		ProcessWorkOrderExampleMapper.updateByConditionMap(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return ProcessWorkOrderExampleMapper.datalistPage(page);
	}

	@Override
	public PageData findById(PageData pageData1) {
		return ProcessWorkOrderExampleMapper.findById(pageData1);
	}

	@Override
	public List<PageData> listAll(PageData po) {
		return ProcessWorkOrderExampleMapper.listAll(po);
	}

	@Override
	public void edit(PageData pageData) {
		ProcessWorkOrderExampleMapper.edit(pageData);
	}

	@Override
	public void add(PageData pd) {
		
		ProcessWorkOrderExampleMapper.save(pd);
	}

	@Override
	public void delete(PageData pd) {
		ProcessWorkOrderExampleMapper.delete(pd);
		
	}

	@Override
	public void EditQIIF(PageData pd) {
		ProcessWorkOrderExampleMapper.EditQIIF(pd);
	}

	/**
	 * 	配方核对任务列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> recipeChecklistPage(Page page) throws Exception{
		return ProcessWorkOrderExampleMapper.recipeChecklistPage(page);
	}
	
	/**
	 * 	编辑是否核对
	 */
	public void editCheckDoneIF(PageData pd) throws Exception{
		ProcessWorkOrderExampleMapper.editCheckDoneIF(pd);
	}
	
	/**
	 * 	称重称量任务列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> weighinglistPage(Page page) throws Exception{
		return ProcessWorkOrderExampleMapper.weighinglistPage(page);
	}
	
	/**
	 * 	编辑是否完成称重
	 */
	public void editWeighingDoneIF(PageData pd) throws Exception{
		ProcessWorkOrderExampleMapper.editWeighingDoneIF(pd);
	}
	
	/**
	 * 	编辑任务指定设备
	 */
	public void editProcessWorkOrderExampleEqm(PageData pd) throws Exception{
		ProcessWorkOrderExampleMapper.editProcessWorkOrderExampleEqm(pd);
	}
	
	/**
	 * 任务关联计划列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> workOrderExamplelistPage(Page page) throws Exception {
		return ProcessWorkOrderExampleMapper.datalistPageWorkOrderExample(page);
	}

	@Override
	public List<PageData> getWoklistPage(Page page) {
		return ProcessWorkOrderExampleMapper.getWoklistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.ProcessWorkOrderExampleService#listSC(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listSC(Page page) throws Exception {
		return ProcessWorkOrderExampleMapper.listSClistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.ProcessWorkOrderExampleService#findByWP(org.yy.entity.PageData)
	 */
	@Override
	public PageData findByWP(PageData pd) throws Exception {
		return ProcessWorkOrderExampleMapper.findByWP(pd);
	}
}
