package org.yy.mapper.dsno1.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： 快照Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-26
 * 官网：356703572@qq.com
 * @version
 */
public interface SnapshotMapper {

	void runSql(PageData pd);

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);

	/**
	 * 批量插入
	 * @param list
	 */
	void saveAll(List<PageData> list);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);

	/**
	 * 清除指定月份前数据
	 * @param pd
	 */
	void deleteData(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);

	/**
	 * 通过ID查询
	 * @param pd
	 * @return
	 */
	PageData findById(PageData pd);

	/**
	 * 查询快照数据
	 * @param pd
	 * @return
	 */
	List<PageData> querySnapshotInfo(List<PageData> list);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);

	/**plc参数列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> plcList(PageData pd);

	/**
	 * 查询年用能数据
	 * @param pd
	 * @return
	 */
	List<PageData> queryYearUseEnergy(PageData pd);

	/**
	 * 以ids查询年用能数据
	 * @param pd
	 * @return
	 */
	List<PageData> queryYearUseEnergyByIds(PageData pd);

	List<PageData> queryRingdatalistPage(Page page);
	List<PageData> queryRing(PageData pd);

	/**
	 * 用能报表
	 * @param page
	 * @return
	 */
	List<PageData> queryUseEnergyByIdsdatalistPage(Page page);
	List<PageData> queryUseEnergyByIds(PageData pd);

	/**
	 * 同比
	 * @param page
	 * @return
	 */
	List<PageData> queryGrewdatalistPage(Page page);
	List<PageData> queryGrewAll(PageData pd);

	/**
	 * 查询年用能单耗数据
	 * @param pd
	 * @return
	 */
	List<PageData> queryYearYield(PageData pd);

	/**
	 * 本年用能
	 * @param pd
	 * @return
	 */
	PageData queryThisYearUserEnergy(PageData pd);

	/**
	 * 去年第一天到去年今天用能
 	 * @param pd
	 * @return
	 */
	PageData queryBeforeYearThisDayUserEnergy(PageData pd);
	
}

