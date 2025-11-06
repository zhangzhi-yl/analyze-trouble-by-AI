/**
 * 
 */
package org.yy.service.pp.impl;

import java.util.List;

import org.apache.regexp.recompile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.ProcessWorkOrderExampleMapper;
import org.yy.mapper.dsno1.pp.WorkorderProcessIOExampleMapper;
import org.yy.service.pp.APSService;

/**
 * 说明： 排程接口实现类
 * 
 * @author chen
 * @since 2020-11-16
 */
@Service
@Transactional // 开启事务
public class APSServiceImpl implements APSService {

	@Autowired
	private ProcessWorkOrderExampleMapper ProcessWorkOrderExampleMapper;
	@Autowired
	private WorkorderProcessIOExampleMapper WorkorderProcessIOExampleMapper;

	@Override
	public List<PageData> listByPlanningWorkOrderID(PageData pd) {
		return ProcessWorkOrderExampleMapper.listByPlanningWorkOrderID(pd);
	}

	@Override
	public PageData findProcessWorkOrderExampleById(PageData pdData) {
		return ProcessWorkOrderExampleMapper.findById(pdData);
	}

	@Override
	public void updateProcessWorkOrderExample(PageData pd) {
		ProcessWorkOrderExampleMapper.edit(pd);
	}

	@Override
	public List<PageData> getWorkorderProcessIOExampleListByProcessWorkOrderExampleID(
			String ProcessWorkOrderExampleID) {
		return WorkorderProcessIOExampleMapper.listByProcessWorkOrderExampleID(ProcessWorkOrderExampleID);
	}

}
