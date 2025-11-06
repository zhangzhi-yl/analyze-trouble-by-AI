package org.yy.service.act;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 历史流程任务接口
 * 创建人：FH Q356703572
 * 
 */
public interface HiprocdefService {
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**历史流程变量列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hivarList(PageData pd)throws Exception;

}
