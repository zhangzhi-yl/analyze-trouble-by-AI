package org.yy.service.momp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.MeetingGattendeeMapper;
import org.yy.service.momp.MeetingGattendeeService;

/** 
 * 说明： 参会人员表接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-04-13
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class MeetingGattendeeServiceImpl implements MeetingGattendeeService{

	@Autowired
	private MeetingGattendeeMapper meetinggattendeeMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		meetinggattendeeMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		meetinggattendeeMapper.delete(pd);
	}
	public void deleteFather(PageData pd)throws Exception{
		meetinggattendeeMapper.deleteFather(pd);
	}
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		meetinggattendeeMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return meetinggattendeeMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return meetinggattendeeMapper.listAll(pd);
	}
	public List<PageData> findByFatherId(PageData pd)throws Exception{
		return meetinggattendeeMapper.findByFatherId(pd);
	}
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return meetinggattendeeMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		meetinggattendeeMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

