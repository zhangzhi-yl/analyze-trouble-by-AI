package org.yy.mapper.dsno1.system;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.system.Dictionaries;

/**
 * 说明：数据字典Mapper
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public interface DictionariesMapper {

	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<Dictionaries> listSubDictByParentId(String parentId);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByBianma(PageData pd);
	
	/**通过名称获取数据 
	 * @param pd
	 * @throws Exception
	 */
	PageData findByName(PageData pd);
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	PageData findFromTbs(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**
	 * 根据传过来的父ID参数查询是用途分类还是只是分类的下拉列表
	 * @param PARENT_ID
	 * @return
	 * @throws Exception
	 */
	List<PageData> listByParentID(PageData pd);
	List<PageData> getBadnessMX(PageData pd);
	
	/**通过数据字典名称获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByDICTIONARIESId(PageData pd);
}
