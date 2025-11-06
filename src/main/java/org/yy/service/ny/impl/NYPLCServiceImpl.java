package org.yy.service.ny.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.NYPLCMapper;
import org.yy.service.ny.NYPLCService;

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
public class NYPLCServiceImpl implements NYPLCService {

	@Autowired
	private NYPLCMapper plcMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		plcMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		plcMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		plcMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return plcMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return plcMapper.listAll(pd);
	}

	/**列表(全部)
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAllPlc() throws Exception {
		return plcMapper.listAllPlc();
	}

	/**
	 * 根据设备获取参数
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getPLCByEquipment(PageData pd) throws Exception {
		return plcMapper.getPLCByEquipment(pd);
	}

	/**启用列表(全部)
	 * @param
	 * @throws Exception
	 */
	@Override
	public List<PageData> useList() throws Exception {
		return plcMapper.useList();
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return plcMapper.findById(pd);
	}

	@Override
	public PageData getFieldMaxValue(PageData pd) throws Exception {
		return plcMapper.getFieldMaxValue(pd);
	}

	@Override
	public PageData getFieldMinValue(PageData pd) throws Exception {
		return plcMapper.getFieldMinValue(pd);
	}

	@Override
	public List<PageData> useListByLoop(PageData pd) throws Exception {
		return plcMapper.useListByLoop(pd);
	}


	@Override
	public List<PageData> findUseEnergyByLoop(PageData pd) throws Exception {
		return plcMapper.findUseEnergyByLoop(pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		plcMapper.deleteAll(ArrayDATA_IDS);
	}

	/** 获取空闲字段
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> getField(PageData pd) throws Exception {
		return plcMapper.getField(pd);
	}

	/**
	 * 清空存储字段数据
	 * @param pd
	 */
	@Override
	public void deletePlcData(PageData pd) throws Exception {
		plcMapper.deletePlcData(pd);
	}

}

