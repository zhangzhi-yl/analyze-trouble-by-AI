package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.TeamCalendarMapper;
import org.yy.service.mom.TeamCalendarService;

/** 
 * 说明： 工厂日历管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-14
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class TeamCalendarServiceImpl implements TeamCalendarService{

	@Autowired
	private TeamCalendarMapper teamcalendarMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		teamcalendarMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		teamcalendarMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		teamcalendarMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return teamcalendarMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return teamcalendarMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return teamcalendarMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		teamcalendarMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 	排班方法，批量插入数据,入参为list
	 * @param list
	 * @throws Exception
	 */
	@Override
	public void batchInsert(List<PageData> list) throws Exception {
		teamcalendarMapper.batchInsert(list);
	}
	
	/**
	 * 时间段重复检查
	 * @param pd
	 * @return pd
	 * @throws Exception
	 */
	public PageData checkRepeat(PageData pd)throws Exception{
		return teamcalendarMapper.checkRepeat(pd);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> dayList(PageData pd)throws Exception{
		return teamcalendarMapper.dayList(pd);
	}
}

