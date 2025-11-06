package org.yy.service.run.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.run.FirstStageMapper;
import org.yy.service.run.FirstStageService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FirstStageServiceImpl implements FirstStageService {

	@Autowired
	private FirstStageMapper firstStageMapper;
	
	@Override
	public List<PageData> findCount(PageData pd) {
		return firstStageMapper.findCount(pd);
	}

	@Override
	public void changeState(PageData pd) {
		firstStageMapper.changeState(pd);
	}

	@Override
	public void updateStateUnexecuted(PageData pd) {
		firstStageMapper.updateStateUnexecuted(pd);
	}

	@Override
	public List<PageData> findNextNodes(PageData pd) {
		return firstStageMapper.findNextNodes(pd);
	}

	@Override
	public void changeOState(PageData pageData) {
		firstStageMapper.changeOState(pageData);
	}

	@Override
	public void setPwoStatusGoing(PageData pageData) {
		firstStageMapper.setPwoStatusGoing(pageData);
	}

	@Override
	public void setPwoStatusDone(PageData pageData) {
		firstStageMapper.setPwoStatusDone(pageData);
	}
		
}
