package org.yy.service.pm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pm.PM_MAINTAIN_MANHOURMapper;
import org.yy.service.pm.PM_MAINTAIN_MANHOURService;

/** 
 * 说明： 设备保养人员工时接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-30
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PM_MAINTAIN_MANHOURServiceImpl implements PM_MAINTAIN_MANHOURService{

	@Autowired
	private PM_MAINTAIN_MANHOURMapper PM_MAINTAIN_MANHOURMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PM_MAINTAIN_MANHOURMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PM_MAINTAIN_MANHOURMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PM_MAINTAIN_MANHOURMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PM_MAINTAIN_MANHOURMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PM_MAINTAIN_MANHOURMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PM_MAINTAIN_MANHOURMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PM_MAINTAIN_MANHOURMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

