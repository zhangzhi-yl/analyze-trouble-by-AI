package org.yy.mapper.dsno1.act;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 历史流程Mapper
 * 作者：YuanYe
 * 
 */
public interface HiprocdefMapper {
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPage(Page page)throws Exception;
	
	/**历史流程变量列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hivarList(PageData pd)throws Exception;

}
