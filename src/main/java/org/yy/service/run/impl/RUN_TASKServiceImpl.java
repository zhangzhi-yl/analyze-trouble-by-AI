package org.yy.service.run.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.run.RUN_TASKMapper;
import org.yy.service.run.RUN_TASKService;

@Service
@Transactional
public class RUN_TASKServiceImpl implements RUN_TASKService {

	@Autowired
	private RUN_TASKMapper RUN_TASKMapper;
	
	@Override
	public List<PageData> findStartPH(PageData pd) {
		return RUN_TASKMapper.findStartPH(pd);
	}

	@Override
	public List<PageData> openSubPlan(PageData pd) {
		return RUN_TASKMapper.openSubPlan(pd);
	}

}
