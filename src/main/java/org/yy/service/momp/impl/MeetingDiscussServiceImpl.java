package org.yy.service.momp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.MeetingDiscussMapper;
import org.yy.service.momp.MeetingDiscussService;

/** 
 * 说明： 会议讨论表接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-04-13
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class MeetingDiscussServiceImpl implements MeetingDiscussService{

	@Autowired
	private MeetingDiscussMapper meetingdiscussMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		meetingdiscussMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		meetingdiscussMapper.delete(pd);
	}
	public void deleteFather(PageData pd)throws Exception{
		meetingdiscussMapper.deleteFather(pd);
	}
	public List<PageData> findByFatherId(PageData pd)throws Exception{
		return meetingdiscussMapper.findByFatherId(pd);
	}
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		meetingdiscussMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return meetingdiscussMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return meetingdiscussMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return meetingdiscussMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		meetingdiscussMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

