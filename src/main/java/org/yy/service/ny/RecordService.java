package org.yy.service.ny;

import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： PLC参数配置接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
public interface RecordService {

	/**新增
	 * @param list
	 * @throws Exception
	 */
	public void save(List<PageData> list)throws Exception;

	/**
	 * 查询用电概况字段
	 * @param pd
	 * @return
	 */
	public List<PageData> getTotalField(PageData pd)throws Exception;

	/**
	 * 获取用能参数列表
	 * @param pd
	 * @return
	 */
	public List<PageData> useEnergyPlcList(PageData pd)throws Exception;

	/**
	 * 查询当日用能量
	 * @param pd
	 * @return
	 */
	public List<PageData> getTotalPower(PageData pd)throws Exception;

	/**
	 * 查询昨日最大值
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeOneMaxPower(PageData pd)throws Exception;

	/**
	 * 查询前日最大值
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeTwoMaxPower(PageData pd)throws Exception;

	/**
	 * A00最大值
	 * @param pd
	 * @return
	 */
	public PageData MaxA00(PageData pd)throws Exception;

	/**
	 * 查询上月最大值
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeOneMonthMaxPower(PageData pd)throws Exception;

	/**
	 * 查询上上月最大值
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeTwoMonthMaxPower(PageData pd)throws Exception;

	/**
	 * 查询去年最大值
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeOneYearMaxPower(PageData pd)throws Exception;

	/**
	 * 查询前年最大值
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeTwoYearMaxPower(PageData pd)throws Exception;

	/**
	 * 昨日同期用能
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeDayUsePower(PageData pd)throws Exception;

	/**
	 * 上月同期用能
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeMonthUsePower(PageData pd)throws Exception;

	/**
	 * 去年同期用能
	 * @param pd
	 * @return
	 */
	public List<PageData> getBeforeYearUsePower(PageData pd)throws Exception;

	public PageData getTopNum(PageData pd)throws Exception;
}

