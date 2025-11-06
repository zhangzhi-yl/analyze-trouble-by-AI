package org.yy.service.run.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.run.SecondStageMapper;
import org.yy.service.run.SecondStageService;

@Service
@Transactional

public class SecondStageServiceImpl implements SecondStageService {
	@Autowired
	private SecondStageMapper secondStageMapper;
	
	@Override
	public PageData getStartNodeId(PageData pd) {
		return secondStageMapper.getStartNodeId(pd);
	}

	@Override
	public List<PageData> findCount(PageData pd) {
		return secondStageMapper.findCount(pd);
	}

	@Override
	public void changeState(PageData pd) {	
		secondStageMapper.changeState(pd);
	}

	@Override
	public void updateStateUnexecuted(PageData pd) {		
		secondStageMapper.updateStateUnexecuted(pd);
	}

	@Override
	public List<PageData> findNextNodes(PageData pd) {		
		return secondStageMapper.findNextNodes(pd);
	}

	@Override
	public PageData getONodeId(PageData pageData) {		
		return secondStageMapper.getONodeId(pageData);
	}

	@Override
	public void changePhState(PageData pageData) {	
		secondStageMapper.changePhState(pageData);
	}

	@Override
	public List<PageData> findStartTask(PageData pd) {
		return secondStageMapper.findStartTask(pd);
	}

	@Override
	public List<PageData> findStartSubPlan(PageData pd) {
		return secondStageMapper.findStartSubPlan(pd);
	}

	@Override
	public List<PageData> findStartTaskNoStandard(PageData pageData2) {
		return secondStageMapper.findStartTaskNoStandard(pageData2);
	}

}
