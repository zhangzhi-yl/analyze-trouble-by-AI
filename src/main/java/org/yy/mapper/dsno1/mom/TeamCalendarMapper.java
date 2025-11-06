package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 工厂日历管理Mapper
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 * @version
 */
public interface TeamCalendarMapper{

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
	 * 批量插入数据，入参为LIST
	 * @param list
	 */
	void batchInsert(List<PageData> list);
	
	/**
	 * 时间段重复检查
	 * @param pd
	 * @return pd
	 */
	PageData checkRepeat(PageData pd);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> dayList(PageData pd);
}

