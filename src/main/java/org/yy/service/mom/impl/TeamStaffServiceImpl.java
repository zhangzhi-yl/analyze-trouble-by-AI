package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.TeamStaffMapper;
import org.yy.service.mom.TeamStaffService;

/** 
 * 说明： 班组人员管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-14
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class TeamStaffServiceImpl implements TeamStaffService{

	@Autowired
	private TeamStaffMapper teamstaffMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		teamstaffMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		teamstaffMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		teamstaffMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return teamstaffMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return teamstaffMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return teamstaffMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		teamstaffMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 根据车间ID查询班组人员数量
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findCount(PageData pd) throws Exception {
		return teamstaffMapper.findCount(pd);
	}
	
	/**
	 * 批量绑定组员方法，入参为组员list
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void staffBinding(List<PageData> staffArray)throws Exception{
		teamstaffMapper.staffBinding(staffArray);
	}
	
	/**通过人员查询班组信息
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByTeam(PageData pd)throws Exception{
		return teamstaffMapper.findByTeam(pd);
	}
	
	/**通过人员查询班组
	 * @param pd
	 * @throws Exception
	 */
	public PageData getTeam(PageData pd)throws Exception{
		return teamstaffMapper.getTeam(pd);
	}
	
	/**查询人员班组列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllStaff(PageData pd)throws Exception{
		return teamstaffMapper.listAllStaff(pd);
	}
}

