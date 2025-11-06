package org.yy.service.ny.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.CollectionMapper;
import org.yy.service.ny.CollectionService;

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
public class CollectionServiceImpl implements CollectionService {

	@Autowired
	private CollectionMapper collectionMapper;

	/**
	 * 保存录入数据
	 * @param pd
	 */
	@Override
	public void saveInput(PageData pd) throws Exception {
		collectionMapper.saveInput(pd);
	}

	/**
	 * 编辑录入数据
	 * @param pd
	 */
	@Override
	public void editInput(PageData pd) throws Exception {
		collectionMapper.editInput(pd);
	}

	@Override
	public List<PageData> findByTime(PageData pd)throws Exception {
		return collectionMapper.findByTime(pd);
	}

	/**
	 * 数据采集列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> plcList(PageData pd) throws Exception {
		return collectionMapper.plcList(pd);
	}

	/**
	 * 获取最大参数值
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> getDayList(PageData pd) throws Exception {
		return collectionMapper.getDayList(pd);
	}

	@Override
	public List<PageData> getWeekList(PageData pd) throws Exception {
		return collectionMapper.getWeekList(pd);
	}

	@Override
	public List<PageData> getMonthList(PageData pd) throws Exception {
		return collectionMapper.getMonthList(pd);
	}

	@Override
	public List<PageData> getYearList(PageData pd) throws Exception {
		return collectionMapper.getYearList(pd);
	}

	@Override
	public List<PageData> getQuarterList(PageData pd) throws Exception {
		return collectionMapper.getQuarterList(pd);
	}

	/**
	 * 保存查询结果至临时表
	 * @param list
	 */
	@Override
	public void saveAll(List<PageData> list) throws Exception {
		collectionMapper.saveAll(list);
	}
	@Override
	public void saveAllOne(List<PageData> list) throws Exception {
		collectionMapper.saveAllOne(list);
	}

	@Override
	public void saveAllTwo(List<PageData> list) throws Exception {
		collectionMapper.saveAllTwo(list);
	}

	@Override
	public void saveAllThree(List<PageData> list) throws Exception {
		collectionMapper.saveAllThree(list);
	}

	@Override
	public void saveAllFour(List<PageData> list) throws Exception {
		collectionMapper.saveAllFour(list);
	}

	@Override
	public void saveAllFive(List<PageData> list) throws Exception {
		collectionMapper.saveAllFive(list);
	}
	@Override
	public void saveAllSix(List<PageData> list) throws Exception {
		collectionMapper.saveAllSix(list);
	}

	/**
	 * 清空临时表
	 */
	@Override
	public void deleteAll() throws Exception {
		collectionMapper.deleteAll();
	}

	@Override
	public void deleteTableAll(PageData pd) throws Exception {
		collectionMapper.deleteTableAll(pd);
	}

	/**
	 * 获取临时表分页数据
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> getResult(Page page) throws Exception {
		return collectionMapper.getResultdatalistPage(page);
	}

	@Override
	public List<PageData> getLoopdatalistPage(Page page) throws Exception {
		return collectionMapper.getLoopdatalistPage(page);
	}

	@Override
	public List<PageData> rankList(PageData pd) throws Exception {
		return collectionMapper.ranklist(pd);
	}

	/**
	 * 获取临时表全部数据
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getResultAllList(PageData pd) throws Exception {
		return collectionMapper.getResultAllList(pd);
	}

	/**
	 * 获取临时表全部数据
	 * @param
	 * @return
	 */
	@Override
	public List<PageData> getResultList() throws Exception {
		return collectionMapper.getResultList();
	}

	/**
	 * 获取单件能耗趋势列表
	 * @param
	 * @return
	 */
	@Override
	public List<PageData> getOnlyConsume() throws Exception {
		return collectionMapper.getOnlyConsume();
	}

	/**
	 * 获取临时表统计数据（全部月份）
	 * @param
	 * @return
	 */
	@Override
	public List<PageData> getResultAllMonthList() throws Exception {
		return collectionMapper.getResultAllMonthList();
	}

	@Override
	public List<PageData> getResultTableAllMonthList(PageData pd) throws Exception {
		return collectionMapper.getResultTableAllMonthList(pd);
	}

	@Override
	public List<PageData> getResultTableList(PageData pd) throws Exception {
		return collectionMapper.getResultTableList(pd);
	}

	/**
	 * 获取临时表回路分组列表
	 * @param
	 * @return
	 */
	@Override
	public List<PageData> getResultGroupByName() throws Exception {
		return collectionMapper.getResultGroupByName();
	}

	/**
	 * 选中回路总功耗
	 * @return
	 */
	@Override
	public List<PageData> getAnalysisList() throws Exception {
		return collectionMapper.getAnalysisList();
	}

	/**
	 * 回路分类总功耗
	 * @return
	 */
	@Override
	public List<PageData> getAnalysisAllList() throws Exception {
		return collectionMapper.getAnalysisAllList();
	}

	/**
	 * 获取天排行数据
	 * @return
	 */
	@Override
	public List<PageData> getDayRank(PageData pd) throws Exception {
		return collectionMapper.getDayRank(pd);
	}

	/**
	 * 获取月排行数据
	 * @return
	 */
	@Override
	public List<PageData> getMonthRank(PageData pd) throws Exception {
		return collectionMapper.getMonthRank(pd);
	}

	/**
	 * 获取季排行数据
	 * @return
	 */
	@Override
	public List<PageData> getQuarterRank(PageData pd) throws Exception {
		return collectionMapper.getQuarterRank(pd);
	}

	/**
	 * 获取年排行数据
	 * @return
	 */
	@Override
	public List<PageData> getYearRank(PageData pd) throws Exception {
		return collectionMapper.getYearRank(pd);
	}

	/**
	 * 同比列表
	 * @return
	 */
	@Override
	public List<PageData> grewList(PageData pd) throws Exception {
		return collectionMapper.grewList(pd);
	}

	/**
	 * 保存查询结果至临时表 同比环比
	 * @param list
	 */
	@Override
	public void saveGrewAll(List<PageData> list) throws Exception {
		collectionMapper.saveGrewAll(list);
	}

	/**
	 * 清空临时表
	 */
	@Override
	public void deleteAllGrew() throws Exception {
		collectionMapper.deleteAllGrew();
	}

	/**
	 * 获取临时表分页数据
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> getGrewdatalistPage(Page page) throws Exception {
		return collectionMapper.getGrewdatalistPage(page);
	}

	/**
	 * 获取临时表全部数据
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getGrewList(PageData pd) throws Exception {
		return collectionMapper.getGrewList(pd);
	}
}

