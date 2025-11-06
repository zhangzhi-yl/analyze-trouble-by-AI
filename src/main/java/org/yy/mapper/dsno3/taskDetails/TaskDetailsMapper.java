package org.yy.mapper.dsno3.taskDetails;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： 下线机任务详情
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
public interface TaskDetailsMapper {

	/**
	 * 任务详情
	 */
	List<PageData> TaskDetails(PageData pd);

	/**
	 * 任务详情
	 */
	List<PageData> TaskDetailsdatalistPage(Page page);
	
}

