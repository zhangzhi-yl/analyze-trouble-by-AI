package org.yy.mapper.dsno1.system;

import org.yy.entity.PageData;

/**
 * 说明：头像编辑Mapper
 * 作者：YuanYe Q313596 790
 * 
 */
public interface PhotoMapper {
	
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
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

}
