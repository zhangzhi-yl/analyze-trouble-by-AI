package org.yy.service.homepage.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.homepage.HomepageMapper;
import org.yy.service.homepage.HomepageService;

@Service
@Transactional // 开启事物
public class HomepageServiceImpl implements HomepageService {

	@Autowired
	private HomepageMapper HomepageMapper;

	/**
	 * 执行完计划
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> finishedPlanlistPage(Page page) throws Exception {
		return HomepageMapper.finishedPlanlistPage(page);
	}

	/**
	 * 执行中计划
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> executingPlanlistPage(Page page) throws Exception {
		return HomepageMapper.executingPlanlistPage(page);
	}

	/**
	 * 未执行计划
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> unexecutedPlanlistPage(Page page) throws Exception {
		return HomepageMapper.unexecutedPlanlistPage(page);
	}

	/**
	 * 生产任务下发提醒
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> issueRemindlistPage(Page page) throws Exception {
		return HomepageMapper.issueRemindlistPage(page);
	}

	/**
	 * 配方核对完成提醒
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> checkDonelistPage(Page page) throws Exception {
		return HomepageMapper.checkDonelistPage(page);
	}

	/**
	 * 生产呼叫质检任务
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> callQIlistPage(Page page) throws Exception {
		return HomepageMapper.callQIlistPage(page);
	}

	/**
	 * 生产异常
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> processExceptionlistPage(Page page) throws Exception {
		return HomepageMapper.processExceptionlistPage(page);
	}

	/**
	 * 返工任务
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> reworkTasklistPage(Page page) throws Exception {
		return HomepageMapper.reworkTasklistPage(page);
	}

	/**
	 * 计划延期提醒
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> planDelaylistPage(Page page) throws Exception {
		return HomepageMapper.planDelaylistPage(page);
	}

}
