package org.yy.mapper.dsno2.windinginserter;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： 下线机
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
public interface WindinginserterMapper {

	/**
	 * 下线机导线月消耗数量
	 * @return
	 */
	List<PageData> MonthUseLine();

	/**
	 * 下线机无故障运行天数
	 * @return
	 */
	List<PageData> NoFaultDayNum();

	/**
	 * 下线机当前产能
	 * @return
	 */
	List<PageData> CurrentCapacity();

	/**
	 * 下线机当年生产数
	 * @return
	 */
	List<PageData> CurrentYearProduction();

	/**
	 * 下线机当月生产数
	 * @return
	 */
	List<PageData> CurrentMonthProduction();

	/**
	 * 下线机当天生产数
	 * @return
	 */
	List<PageData> CurrentDayProduction();

	/**
	 * 下线机总生产数
	 * @return
	 */
	List<PageData> TotalProductionQuantity();

	/**
	 * 下线机当前设备状态
	 * @return
	 */
	List<PageData> CurrentEquipmentStatus();
	
}

