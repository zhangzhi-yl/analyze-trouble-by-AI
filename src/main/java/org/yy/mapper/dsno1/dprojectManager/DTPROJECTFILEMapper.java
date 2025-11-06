package org.yy.mapper.dsno1.dprojectManager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 项目文件Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-05
 * 官网：356703572@qq.com
 * @version
 */
public interface DTPROJECTFILEMapper{

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
	
	/**逻辑删除
	 * @param pd
	 * @throws Exception
	 */
	void updateDel(PageData pd);
	
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
	
	/**项目列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listProAll(PageData pd);
	
	/**通过id获取项目编号
	 * @param pd
	 * @throws Exception
	 */
//	PageData findProById(PageData pd);
	
	/**设备列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listEquAll(PageData pd);
	
	/**阶段列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listStaAll(PageData pd);
	
	/**活动名称列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listActAll(PageData pd);

	/**附件列表
	 * @param page
	 * @return
	 */
	List<PageData> dataxlistPage(Page page);

	/**新增页获取数据
	 * @param pd
	 * @return
	 */
	PageData getDatax(PageData pd);

	/**
	 * @param pd
	 */
	void insert(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	PageData findFJ(PageData pd);
}

