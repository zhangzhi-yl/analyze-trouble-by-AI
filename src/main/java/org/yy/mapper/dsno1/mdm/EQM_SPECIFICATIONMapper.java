package org.yy.mapper.dsno1.mdm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 设备规范明细资料Mapper
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 * @version
 */
public interface EQM_SPECIFICATIONMapper{

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
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	void deleteBase(PageData pd);
	
	/**
	 *	根据列KEY名和设备id查询列VALUE值
	 * @param pd
	 * @return
	 */
	PageData findByKeyNameAndEQM_BASE_ID(PageData pd);
}

