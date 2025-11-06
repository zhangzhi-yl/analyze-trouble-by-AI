package org.yy.mapper.dsno1.momp;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 任务流程模板Mapper
 * 作者：YuanYe
 * 时间：2020-03-19
 * 
 * @version
 */
public interface MissionTemplateMapper{

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
	/**通过ID获取其子级列表
	 * @param pd
	 * @return
	 */
	List<PageData> listByParentId(PageData pd);
	/**
	 * 查询当前最大个数
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	PageData count(PageData pd);
	/**
	 * 查询对应编号下明细总数
	 * @param pd
	 * @return
	 */
	PageData countPhase(PageData pd);
}

