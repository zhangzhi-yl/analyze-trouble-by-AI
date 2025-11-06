package org.yy.service.act;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 流程管理接口
 * 作者：YuanYe
 * 
 * @version
 */
public interface ProcdefService{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
}

