package org.yy.service.yl.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.yl.ProjectTaskIssueCarryoutMapper;
import org.yy.service.yl.ProjectTaskIssueCarryoutService;

/** 
 * 说明： 计划任务下发执行接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-02-23
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ProjectTaskIssueCarryoutServiceImpl implements ProjectTaskIssueCarryoutService{

	@Autowired
	private ProjectTaskIssueCarryoutMapper ProjectTaskIssueCarryoutMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ProjectTaskIssueCarryoutMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ProjectTaskIssueCarryoutMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ProjectTaskIssueCarryoutMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ProjectTaskIssueCarryoutMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ProjectTaskIssueCarryoutMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ProjectTaskIssueCarryoutMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ProjectTaskIssueCarryoutMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**计划制定列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> planList(Page page)throws Exception{
		return ProjectTaskIssueCarryoutMapper.datalistPagePlan(page);
	}
	
	/**下发任务
	 * @param pd
	 * @throws Exception
	 */
	public void editIssue(PageData pd)throws Exception{
		ProjectTaskIssueCarryoutMapper.editIssue(pd);
	}
	
	/**完成任务
	 * @param pd
	 * @throws Exception
	 */
	public void editOver(PageData pd)throws Exception{
		ProjectTaskIssueCarryoutMapper.editOver(pd);
	}
	
	/**执行反馈列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> carryOutList(Page page)throws Exception{
		return ProjectTaskIssueCarryoutMapper.datalistPageCarryOut(page);
	}
	
	/**通过数据字典名称获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByDICTIONARIESId(PageData pd)throws Exception{
		return ProjectTaskIssueCarryoutMapper.findByDICTIONARIESId(pd);
	}
	
	/**通过人员名称判断人员是否存在
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByStaffId(PageData pd)throws Exception{
		return ProjectTaskIssueCarryoutMapper.findByStaffId(pd);
	}
}

