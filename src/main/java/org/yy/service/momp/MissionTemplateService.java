package org.yy.service.momp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 任务流程模板接口
 * 作者：YuanYe
 * 时间：2020-03-19
 * 
 * @version
 */
public interface MissionTemplateService{

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
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listTree(PageData pd) throws Exception;
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listByParentId(PageData pd) throws Exception;
	/**
	 * 查询当前最大个数 生成编号
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public PageData count(PageData pd)throws Exception;
	/**
	 * 查询对应编号下明细总数
	 * @param pd
	 * @return
	 */
	public PageData countPhase(PageData pd)throws Exception;
	
	
}

