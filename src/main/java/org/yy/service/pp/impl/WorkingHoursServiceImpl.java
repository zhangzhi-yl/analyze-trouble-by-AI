package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.WorkingHoursMapper;
import org.yy.service.pp.WorkingHoursService;

/** 
 * 说明： 工时填报接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-25
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WorkingHoursServiceImpl implements WorkingHoursService{

	@Autowired
	private WorkingHoursMapper WorkingHoursMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		WorkingHoursMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		WorkingHoursMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		WorkingHoursMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return WorkingHoursMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return WorkingHoursMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return WorkingHoursMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		WorkingHoursMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**获取单件工时
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getHour(PageData pd)throws Exception{
		return WorkingHoursMapper.getHour(pd);
	}
	
	/**提交审核
	 * @param pd
	 * @throws Exception
	 */
	public void goAudit(PageData pd)throws Exception{
		WorkingHoursMapper.goAudit(pd);
	}
	
	/**审核
	 * @param pd
	 * @throws Exception
	 */
	public void audit(PageData pd)throws Exception{
		WorkingHoursMapper.audit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listAudit(Page page)throws Exception{
		return WorkingHoursMapper.datalistPageAudit(page);
	}
}
