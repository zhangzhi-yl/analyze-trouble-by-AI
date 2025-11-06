package org.yy.mapper.dsno1.zm;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： App版本Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
public interface AppVersionMapper {
	
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

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
}

