package org.yy.service.momp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.MeetingItemMapper;
import org.yy.service.momp.MeetingItemService;

/** 
 * 说明： 会议待办项接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-04-26
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class MeetingItemServiceImpl implements MeetingItemService{

	@Autowired
	private MeetingItemMapper meetingitemMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		meetingitemMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		meetingitemMapper.delete(pd);
	}
	public void deleteFather(PageData pd)throws Exception{
		meetingitemMapper.deleteFather(pd);
	}
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		meetingitemMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return meetingitemMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return meetingitemMapper.listAll(pd);
	}
	public List<PageData> findByFatherId(PageData pd)throws Exception{
		return meetingitemMapper.findByFatherId(pd);
	}
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return meetingitemMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		meetingitemMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

