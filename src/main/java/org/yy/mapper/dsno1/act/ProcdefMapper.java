package org.yy.mapper.dsno1.act;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 流程管理Mapper
 * 作者：YuanYe
 * 
 * @version
 */
public interface ProcdefMapper{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
}

