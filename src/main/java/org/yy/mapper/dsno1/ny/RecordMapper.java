package org.yy.mapper.dsno1.ny;

import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： PLC参数配置Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
public interface RecordMapper {

	/**新增
	 * @param list
	 * @throws Exception
	 */
	void save(List<PageData> list);

	/**
	 * 查询用电概况字段
	 * @param pd
	 * @return
	 */
	List<PageData> getTotalField(PageData pd);

	/**
	 * 获取用能参数列表
	 * @param pd
	 * @return
	 */
	List<PageData> useEnergyPlcList(PageData pd);

	/**
	 * 查询当日用能量
	 * @param pd
	 * @return
	 */
	List<PageData> getTotalPower(PageData pd);

	/**
	 * 查询昨日最大值
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeOneMaxPower(PageData pd);

	/**
	 * 查询前日最大值
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeTwoMaxPower(PageData pd);

	/**
	 * 查询上月最大值
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeOneMonthMaxPower(PageData pd);

	/**
	 * A00最大值
	 * @param pd
	 * @return
	 */
	PageData MaxA00(PageData pd);

	/**
	 * 查询上上月最大值
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeTwoMonthMaxPower(PageData pd);

	/**
	 * 查询去年最大值
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeOneYearMaxPower(PageData pd);

	/**
	 * 查询前年最大值
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeTwoYearMaxPower(PageData pd);


	/**
	 * 昨日同期用能
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeDayUsePower(PageData pd);

	/**
	 * 上月同期用能
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeMonthUsePower(PageData pd);

	/**
	 * 去年同期用能
	 * @param pd
	 * @return
	 */
	List<PageData> getBeforeYearUsePower(PageData pd);

	PageData getTopNum(PageData pd);
}

