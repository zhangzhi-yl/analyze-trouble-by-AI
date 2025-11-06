package org.yy.service.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： PLC参数配置接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
public interface NYPLCService {

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
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

	/**列表(全部)
	 * @throws Exception
	 */
	public List<PageData> listAllPlc()throws Exception;

	/**
	 * 根据设备获取参数
	 * @param pd
	 * @return
	 */
	public List<PageData> getPLCByEquipment(PageData pd)throws Exception;

	/**启用列表(全部)
	 * @param
	 * @throws Exception
	 */
	List<PageData> useList()throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**获取存储表存储字段最大值
	 * @param pd
	 * @throws Exception
	 */
	public PageData getFieldMaxValue(PageData pd)throws Exception;

	/**获取存储表存储字段最小值
	 * @param pd
	 * @throws Exception
	 */
	public PageData getFieldMinValue(PageData pd)throws Exception;

	/**
	 * 启用列表带回路名
	 * @param pd
	 * @return
	 */
	public List<PageData> useListByLoop(PageData pd)throws Exception;

	/**通过loop获取数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findUseEnergyByLoop(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/** 获取空闲字段
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getField(PageData pd)throws Exception;

	/**
	 * 清空存储字段数据
	 * @param pd
	 */
	public void deletePlcData(PageData pd)throws Exception;
	
}

