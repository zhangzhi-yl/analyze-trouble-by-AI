package org.yy.service.act.impl;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.act.RuprocdefMapper;
import org.yy.service.act.RuprocdefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 说明： 正在运行的流程接口实现类
 * 创建人：FH Q356703572
 * 
 */
@Service(value="ruprocdefServiceImpl")
@Transactional //开启事物
public class RuprocdefServiceImpl implements RuprocdefService {
	
	@Autowired
	private RuprocdefMapper ruprocdefMapper;
	

	/**待办任务 or正在运行任务列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ruprocdefMapper.datalistPage(page);
	}
	
	/**流程变量列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> varList(PageData pd)throws Exception{
		return ruprocdefMapper.varList(pd);
	}
	
	/**历史任务节点列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hiTaskList(PageData pd)throws Exception{
		return ruprocdefMapper.hiTaskList(pd);
	}
	
	/**已办任务列表列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hitasklist(Page page)throws Exception{
		return ruprocdefMapper.hitaskdatalistPage(page);
	}
	
	/**激活or挂起任务(指定某个任务)
	 * @param pd
	 * @throws Exception
	 */
	public void onoffTask(PageData pd)throws Exception{
		ruprocdefMapper.onoffTask(pd);;
	}
	
	/**激活or挂起任务(指定某个流程的所有任务)
	 * @param pd
	 * @throws Exception
	 */
	public void onoffAllTask(PageData pd)throws Exception{
		ruprocdefMapper.onoffAllTask(pd);;
	}

	public void saveProcessStatusTable(PageData pd)throws Exception{
		ruprocdefMapper.saveProcessStatusTable(pd);;
	}
	public void editReverseProcessID(PageData pd)throws Exception{
		ruprocdefMapper.editReverseProcessID(pd);;
	}
	public List<PageData> getProParams(PageData pd)throws Exception{
		return ruprocdefMapper.getProParams(pd);
	}
	public void updProParams(PageData pd)throws Exception{
		ruprocdefMapper.updProParams(pd);;
	}
	public PageData getVarnum(PageData pd)throws Exception{
		return ruprocdefMapper.getVarnum(pd);
	}
}
