package org.yy.mapper.dsno1.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： 回路/支路管理Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
public interface NYLoopMapper {

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

	/**级联删除
	 * @param pd
	 * @throws Exception
	 */
	void deleteByParent(PageData pd);
	
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
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> goExcel(PageData pd);

	/**
	 * 回路列表(带plc id)
	 * @return
	 */
	List<PageData> loopPlcAll();
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**
	 * 根据名称获取数据
	 * @param pd
	 * @return
	 */
	List<PageData> findByName(PageData pd);

	/**通过code获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByCode(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**通获取回路数量
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> loopCount(PageData pd);

	/**
	 * 查询编码表
	 * @param pd
	 * @return
	 */
	PageData getCodeNumByType(PageData pd);

	/**
	 * 更新编码表
	 * @param pd
	 * @return
	 */
	void editCodeNumByType(PageData pd);
	
}

