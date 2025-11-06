package org.yy.service.ny.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.SnapshotMapper;
import org.yy.mapper.dsno1.ny.YIELDMapper;
import org.yy.service.ny.SnapshotService;
import org.yy.service.ny.YIELDService;

import java.util.List;

/** 
 * 说明： 快照口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-26
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SnapshotServiceImpl implements SnapshotService {

	@Autowired
	private SnapshotMapper snapshotMapper;

	@Override
	public void runSql(PageData pd) {
		snapshotMapper.runSql(pd);
	}

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		snapshotMapper.save(pd);
	}

	/**
	 * 批量插入
	 * @param list
	 */
	@Override
	public void saveAll(List<PageData> list) throws Exception {
		snapshotMapper.saveAll(list);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		snapshotMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		snapshotMapper.edit(pd);
	}

	@Override
	public List<PageData> querySnapshotInfo(List<PageData> list) throws Exception {
		return snapshotMapper.querySnapshotInfo(list);
	}

	@Override
	public void deleteData(PageData pd) throws Exception {
		snapshotMapper.deleteData(pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return snapshotMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return snapshotMapper.listAll(pd);
	}

	@Override
	public List<PageData> plcList(PageData pd) throws Exception {
		return snapshotMapper.plcList(pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return snapshotMapper.findById(pd);
	}

	/**
	 * 查询年用能数据
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> queryYearUseEnergy(PageData pd) throws Exception {
		return snapshotMapper.queryYearUseEnergy(pd);
	}

	/**
	 * 以ids查询年用能数据
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> queryYearUseEnergyByIds(PageData pd) throws Exception {
		return snapshotMapper.queryYearUseEnergyByIds(pd);
	}

	@Override
	public List<PageData> queryRingdatalistPage(Page page) {
		return snapshotMapper.queryRingdatalistPage(page);
	}

	@Override
	public List<PageData> queryRing(PageData pd) {
		return snapshotMapper.queryRing(pd);
	}

	@Override
	public List<PageData> queryUseEnergyByIdsdatalistPage(Page page) throws Exception {
		return snapshotMapper.queryUseEnergyByIdsdatalistPage(page);
	}

	@Override
	public List<PageData> queryUseEnergyByIds(PageData pd) throws Exception {
		return snapshotMapper.queryUseEnergyByIds(pd);
	}

	@Override
	public List<PageData> queryGrewdatalistPage(Page page) throws Exception {
		return snapshotMapper.queryGrewdatalistPage(page);
	}

	@Override
	public List<PageData> queryGrewAll(PageData pd) throws Exception {
		return snapshotMapper.queryGrewAll(pd);
	}

	/**
	 * 查询年用能单耗数据
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> queryYearYield(PageData pd) throws Exception {
		return snapshotMapper.queryYearYield(pd);
	}

	/**
	 * 本年用能
	 * @param pd
	 * @return
	 */
	@Override
	public PageData queryThisYearUserEnergy(PageData pd) throws Exception {
		return snapshotMapper.queryThisYearUserEnergy(pd);
	}

	/**
	 * 去年第一天到去年今天用能
	 * @param pd
	 * @return
	 */
	@Override
	public PageData queryBeforeYearThisDayUserEnergy(PageData pd) throws Exception {
		return snapshotMapper.queryBeforeYearThisDayUserEnergy(pd);
	}

}

