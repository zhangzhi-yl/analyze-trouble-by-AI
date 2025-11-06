package org.yy.service.windinginserter;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/**
 * 说明： 下线机
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-18
 * 官网：356703572@qq.com
 * @version
 */
public interface WindinginserterService {

	/**
	 * 下线机导线月消耗数量
	 * @return
	 */
	public List<PageData> MonthUseLine()throws Exception;

	/**
	 * 下线机无故障运行天数
	 * @return
	 */
	public List<PageData> NoFaultDayNum()throws Exception;

	/**
	 * 下线机当前产能
	 * @return
	 */
	public List<PageData> CurrentCapacity()throws Exception;

	/**
	 * 下线机当年生产数
	 * @return
	 */
	public List<PageData> CurrentYearProduction()throws Exception;

	/**
	 * 下线机当月生产数
	 * @return
	 */
	public List<PageData> CurrentMonthProduction()throws Exception;

	/**
	 * 下线机当天生产数
	 * @return
	 */
	public List<PageData> CurrentDayProduction()throws Exception;

	/**
	 * 下线机总生产数
	 * @return
	 */
	public List<PageData> TotalProductionQuantity()throws Exception;

	/**
	 * 下线机当前设备状态
	 * @return
	 */
	public List<PageData> CurrentEquipmentStatus()throws Exception;

	/**
	 * 任务详情
	 */
	public List<PageData> TaskDetails(PageData pd)throws Exception;

	/**
	 * 任务详情
	 */
	public List<PageData> TaskDetailsdatalistPage(Page page)throws Exception;

}

