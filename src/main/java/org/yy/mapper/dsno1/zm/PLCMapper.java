package org.yy.mapper.dsno1.zm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： PLC参数配置Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 * @version
 */
public interface PLCMapper{

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

	/** 获取空闲字段
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getField(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);

	/**启用列表(全部)
	 * @param
	 * @throws Exception
	 */
	List<PageData> useList();

	/**启用列表(全部)回路筛选
	 * @param
	 * @throws Exception
	 */
	List<PageData> useListByLoop(PageData pd);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * 存储开关记录
	 * @param pd
	 */
	void saveRecord(List<PageData> list);

	/**
	 * 获取开关时长
	 * @param pd
	 * @return
	 */
	List<PageData> getDuration(PageData pd);

	/**
	 * 清空存储字段数据
	 * @param pd
	 */
	void deletePlcData(PageData pd);
	
}

