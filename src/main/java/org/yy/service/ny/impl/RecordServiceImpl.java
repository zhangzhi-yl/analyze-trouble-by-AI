package org.yy.service.ny.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.RecordMapper;
import org.yy.service.ny.RecordService;

import java.util.List;

/** 
 * 说明： PLC参数配置接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class RecordServiceImpl implements RecordService {

	@Autowired
	private RecordMapper recordMapper;
	
	/**新增
	 * @param list
	 * @throws Exception
	 */
	public void save(List<PageData> list)throws Exception{
		recordMapper.save(list);
	}

	/**
	 * 查询用电概况字段
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getTotalField(PageData pd) throws Exception {
		return recordMapper.getTotalField(pd);
	}

	@Override
	public List<PageData> useEnergyPlcList(PageData pd) throws Exception {
		return recordMapper.useEnergyPlcList(pd);
	}

	/**
	 * 查询当日用能量
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getTotalPower(PageData pd) throws Exception {
		return recordMapper.getTotalPower(pd);
	}

	/**
	 * 查询昨日最大值
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getBeforeOneMaxPower(PageData pd) throws Exception {
		return recordMapper.getBeforeOneMaxPower(pd);
	}

	@Override
	public List<PageData> getBeforeTwoMaxPower(PageData pd) throws Exception {
		return recordMapper.getBeforeTwoMaxPower(pd);
	}

	@Override
	public List<PageData> getBeforeOneMonthMaxPower(PageData pd) throws Exception {
		return recordMapper.getBeforeOneMonthMaxPower(pd);
	}

	@Override
	public PageData MaxA00(PageData pd) throws Exception {
		return recordMapper.MaxA00(pd);
	}

	@Override
	public List<PageData> getBeforeTwoMonthMaxPower(PageData pd) throws Exception {
		return recordMapper.getBeforeTwoMonthMaxPower(pd);
	}

	@Override
	public List<PageData> getBeforeOneYearMaxPower(PageData pd) throws Exception {
		return recordMapper.getBeforeOneYearMaxPower(pd);
	}

	@Override
	public List<PageData> getBeforeTwoYearMaxPower(PageData pd) throws Exception {
		return recordMapper.getBeforeTwoYearMaxPower(pd);
	}

	/**
	 * 昨日同期用能
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getBeforeDayUsePower(PageData pd) throws Exception {
		return recordMapper.getBeforeDayUsePower(pd);
	}

	@Override
	public List<PageData> getBeforeMonthUsePower(PageData pd) throws Exception {
		return recordMapper.getBeforeMonthUsePower(pd);
	}

	@Override
	public List<PageData> getBeforeYearUsePower(PageData pd) throws Exception {
		return recordMapper.getBeforeYearUsePower(pd);
	}

	@Override
	public PageData getTopNum(PageData pd) throws Exception {
		return recordMapper.getTopNum(pd);
	}

}

