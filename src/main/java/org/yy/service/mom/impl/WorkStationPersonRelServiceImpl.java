package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.WorkStationPersonRelMapper;
import org.yy.service.mom.WorkStationPersonRelService;

/** 
 * 说明： 工作站关联人员接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WorkStationPersonRelServiceImpl implements WorkStationPersonRelService{

	@Autowired
	private WorkStationPersonRelMapper workstationpersonrelMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		workstationpersonrelMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		workstationpersonrelMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		workstationpersonrelMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return workstationpersonrelMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return workstationpersonrelMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return workstationpersonrelMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		workstationpersonrelMapper.deleteAll(ArrayDATA_IDS);
	}

	/**按工位名称查询工位下人员
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllByName(PageData pd) throws Exception {
		return workstationpersonrelMapper.listAllByName(pd);
	}
	
}

