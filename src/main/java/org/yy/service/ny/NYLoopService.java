package org.yy.service.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： 回路/支路管理接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
public interface NYLoopService {

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

	/**级联删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByParent(PageData pd)throws Exception;
	
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

	/**
	 * 回路列表(带plc id)
	 * @return
	 */
	public List<PageData> loopPlcAll()throws Exception;

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> goExcel(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**
	 * 根据名称获取数据
	 * @param pd
	 * @return
	 */
	public List<PageData> findByName(PageData pd)throws Exception;

	/**通过code获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCode(PageData pd)throws Exception;

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**通获取回路数量
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> loopCount(PageData pd)throws Exception;

	/**
	 * 查询编码表
	 * @param pd
	 * @return
	 */
	public PageData getCodeNumByType(PageData pd)throws Exception;

	/**
	 * 更新编码表
	 * @param pd
	 * @return
	 */
	public void editCodeNumByType(PageData pd)throws Exception;
	
}

