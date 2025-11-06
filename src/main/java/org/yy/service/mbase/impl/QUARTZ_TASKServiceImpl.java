package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.QUARTZ_TASKMapper;
import org.yy.service.mbase.QUARTZ_TASKService;

/** 
 * 说明： 定时任务接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-05-25
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class QUARTZ_TASKServiceImpl implements QUARTZ_TASKService{

	@Autowired
	private QUARTZ_TASKMapper quartz_taskMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		quartz_taskMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		quartz_taskMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		quartz_taskMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return quartz_taskMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return quartz_taskMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return quartz_taskMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		quartz_taskMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**切换状态
	 * @param pd
	 * @throws Exception
	 */
	public void changeStatus(PageData pd)throws Exception{
		quartz_taskMapper.changeStatus(pd);
	}
	
}

