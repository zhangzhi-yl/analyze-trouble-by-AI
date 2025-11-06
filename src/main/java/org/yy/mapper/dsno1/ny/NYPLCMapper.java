package org.yy.mapper.dsno1.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： PLC参数配置Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
public interface NYPLCMapper {

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
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

	/**列表(全部)
	 * @throws Exception
	 */
	List<PageData> listAllPlc();

	/**
	 * 根据设备获取参数
	 * @param pd
	 * @return
	 */
	List<PageData> getPLCByEquipment(PageData pd);

	/**启用列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> useList();

	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**获取存储表存储字段最大值
	 * @param pd
	 * @throws Exception
	 */
	PageData getFieldMaxValue(PageData pd);

	/**获取存储表存储字段最小值
	 * @param pd
	 * @throws Exception
	 */
	PageData getFieldMinValue(PageData pd);

	/**
	 * 启用列表带回路名
	 * @param pd
	 * @return
	 */
	List<PageData> useListByLoop(PageData pd);

	/**通过loop获取数据
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> findUseEnergyByLoop(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/** 获取空闲字段
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getField(PageData pd);

	/**
	 * 清空存储字段数据
	 * @param pd
	 */
	void deletePlcData(PageData pd);
	
}

