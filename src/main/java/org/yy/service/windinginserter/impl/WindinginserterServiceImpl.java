package org.yy.service.windinginserter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno2.windinginserter.WindinginserterMapper;
import org.yy.mapper.dsno3.taskDetails.TaskDetailsMapper;
import org.yy.service.windinginserter.WindinginserterService;

import java.util.List;

/**
 * 说明： 下线机
 * 作者：YuanYes Q356703572
 * 时间：2021-10-18
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WindinginserterServiceImpl implements WindinginserterService {

	@Autowired
	private WindinginserterMapper windinginserterMapper;

	@Autowired
	private TaskDetailsMapper taskDetailsMapper;


	@Override
	public List<PageData> MonthUseLine() throws Exception {
		return windinginserterMapper.MonthUseLine();
	}

	@Override
	public List<PageData> NoFaultDayNum() throws Exception {
		return windinginserterMapper.NoFaultDayNum();
	}

	@Override
	public List<PageData> CurrentCapacity() throws Exception {
		return windinginserterMapper.CurrentCapacity();
	}

	@Override
	public List<PageData> CurrentYearProduction() throws Exception {
		return windinginserterMapper.CurrentYearProduction();
	}

	@Override
	public List<PageData> CurrentMonthProduction() throws Exception {
		return windinginserterMapper.CurrentMonthProduction();
	}

	@Override
	public List<PageData> CurrentDayProduction() throws Exception {
		return windinginserterMapper.CurrentDayProduction();
	}

	@Override
	public List<PageData> TotalProductionQuantity() throws Exception {
		return windinginserterMapper.TotalProductionQuantity();
	}

	@Override
	public List<PageData> CurrentEquipmentStatus() throws Exception {
		return windinginserterMapper.CurrentEquipmentStatus();
	}

	@Override
	public List<PageData> TaskDetails(PageData pd) throws Exception {
		return taskDetailsMapper.TaskDetails(pd);
	}

	@Override
	public List<PageData> TaskDetailsdatalistPage(Page page) throws Exception {
		return taskDetailsMapper.TaskDetailsdatalistPage(page);
	}
}

