package org.yy.service.zm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： PLC参数配置接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 * @version
 */
public interface PLCService{

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

	/** 获取空闲字段
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getField(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;

	/**启用且存储列表(全部)
	 * @param
	 * @throws Exception
	 */
	public List<PageData> useList()throws Exception;

	/**启用列表(全部)回路筛选
	 * @param
	 * @throws Exception
	 */
	public List<PageData> useListByLoop(PageData pd)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**
	 * 存储开关记录
	 * @param pd
	 */
	public void saveRecord(List<PageData> list)throws Exception;

	/**
	 * 获取开关时长
	 * @param pd
	 * @return
	 */
	public List<PageData> getDuration(PageData pd)throws Exception;

	/**
	 * 清空存储字段数据
	 * @param pd
	 */
	public void deletePlcData(PageData pd)throws Exception;
	
}

