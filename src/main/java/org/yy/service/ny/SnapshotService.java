package org.yy.service.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： 产量接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-26
 * 官网：356703572@qq.com
 * @version
 */
public interface SnapshotService {

	void runSql(PageData pd);

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;

	/**
	 * 批量插入
	 * @param list
	 */
	public void saveAll(List<PageData> list)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;

	/**
	 * 查询快照数据
	 * @param pd
	 * @return
	 */
	public List<PageData> querySnapshotInfo(List<PageData> list)throws Exception;

	/**
	 * 清除指定月份前数据
	 * @param pd
	 */
	public void deleteData(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;

	/**plc列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> plcList(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**
	 * 查询年用能数据
	 * @param pd
	 * @return
	 */
	public List<PageData> queryYearUseEnergy(PageData pd)throws Exception;

	/**
	 * 以ids查询年用能数据
	 * @param pd
	 * @return
	 */
	public List<PageData> queryYearUseEnergyByIds(PageData pd)throws Exception;


	/**
	 * 环比
	 * @param page
	 * @return
	 */
	List<PageData> queryRingdatalistPage(Page page);
	List<PageData> queryRing(PageData pd);

	/**
	 * 用能报表
	 * @param page
	 * @return
	 */
	List<PageData> queryUseEnergyByIdsdatalistPage(Page page)throws Exception;
	List<PageData> queryUseEnergyByIds(PageData pd)throws Exception;

	/**
	 * 同比
	 * @param page
	 * @return
	 */
	List<PageData> queryGrewdatalistPage(Page page)throws Exception;
	List<PageData> queryGrewAll(PageData pd)throws Exception;


	/**
	 * 查询年用能单耗数据
	 * @param pd
	 * @return
	 */
	public List<PageData> queryYearYield(PageData pd)throws Exception;

	/**
	 * 本年用能
	 * @param pd
	 * @return
	 */
	public PageData queryThisYearUserEnergy(PageData pd)throws Exception;

	/**
	 * 去年第一天到去年今天用能
	 * @param pd
	 * @return
	 */
	public PageData queryBeforeYearThisDayUserEnergy(PageData pd)throws Exception;
	
}

