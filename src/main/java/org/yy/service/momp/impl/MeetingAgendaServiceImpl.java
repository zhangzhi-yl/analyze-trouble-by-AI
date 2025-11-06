package org.yy.service.momp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.MeetingAgendaMapper;
import org.yy.service.momp.MeetingAgendaService;

/** 
 * 说明： 会议议程表接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-04-13
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class MeetingAgendaServiceImpl implements MeetingAgendaService{

	@Autowired
	private MeetingAgendaMapper meetingagendaMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		meetingagendaMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		meetingagendaMapper.delete(pd);
	}
	public void deleteFather(PageData pd)throws Exception{
		meetingagendaMapper.deleteFather(pd);
	}
	
	public List<PageData> findByFatherId(PageData pd)throws Exception{
		return meetingagendaMapper.findByFatherId(pd);
	}
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		meetingagendaMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return meetingagendaMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return meetingagendaMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return meetingagendaMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		meetingagendaMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

