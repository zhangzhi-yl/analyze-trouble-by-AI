package org.yy.service.pm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pm.PM_MAINTAINMxMapper;
import org.yy.service.pm.PM_MAINTAINMxService;

/** 
 * 说明： 设备保养任务(明细)接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-30
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PM_MAINTAINMxServiceImpl implements PM_MAINTAINMxService{

	@Autowired
	private PM_MAINTAINMxMapper PM_MAINTAINMxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PM_MAINTAINMxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PM_MAINTAINMxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PM_MAINTAINMxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PM_MAINTAINMxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PM_MAINTAINMxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PM_MAINTAINMxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PM_MAINTAINMxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return PM_MAINTAINMxMapper.findCount(pd);
	}
	
	/**删除附件
	 * @param pd
	 * @throws Exception
	 */
	public void delFj(PageData pd)throws Exception{
		PM_MAINTAINMxMapper.delFj(pd);
	}
	
	/**更新执行项
	 * @param pd
	 * @throws Exception
	 */
	public void editExecute(PageData pd)throws Exception{
		PM_MAINTAINMxMapper.editExecute(pd);
	}
}

