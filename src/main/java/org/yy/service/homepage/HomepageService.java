package org.yy.service.homepage;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

public interface HomepageService {
	/**
	 * 执行完计划
	 * @param page
	 * @return
	 */
	public List<PageData> finishedPlanlistPage(Page page)throws Exception;
	
	
	/**
	 * 执行中计划
	 * @param page
	 * @return
	 */
	public List<PageData> executingPlanlistPage(Page page)throws Exception;
	
	
	/**
	 * 未执行计划
	 * @param page
	 * @return
	 */
	public List<PageData> unexecutedPlanlistPage(Page page)throws Exception;
	
	
	/**
	 * 生产任务下发提醒
	 * @param page
	 * @return
	 */
	public List<PageData> issueRemindlistPage(Page page)throws Exception;
	
	
	
	/**
	 * 配方核对完成提醒
	 * @param page
	 * @return
	 */
	public List<PageData> checkDonelistPage(Page page)throws Exception;
	
	
	
	/**
	 * 生产呼叫质检任务
	 * @param page
	 * @return
	 */
	public List<PageData> callQIlistPage(Page page)throws Exception;
	
	
	/**
	 * 生产异常
	 * @param page
	 * @return
	 */
	public List<PageData> processExceptionlistPage(Page page)throws Exception;
	
	
	/**
	 * 返工任务
	 * @param page
	 * @return
	 */
	public List<PageData> reworkTasklistPage(Page page)throws Exception;
	
	
	
	
	/**
	 * 计划延期提醒
	 * @param page
	 * @return
	 */
	public List<PageData> planDelaylistPage(Page page)throws Exception;
	
	
	

}
