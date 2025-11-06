package org.yy.service.pm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pm.PM_MAINTAINMapper;
import org.yy.service.pm.PM_MAINTAINService;

/** 
 * 说明： 设备保养任务接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-29
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PM_MAINTAINServiceImpl implements PM_MAINTAINService{

	@Autowired
	private PM_MAINTAINMapper PM_MAINTAINMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PM_MAINTAINMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PM_MAINTAINMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PM_MAINTAINMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PM_MAINTAINMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PM_MAINTAINMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PM_MAINTAINMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PM_MAINTAINMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**删除附件
	 * @param pd
	 * @throws Exception
	 */
	public void delFj(PageData pd)throws Exception{
		PM_MAINTAINMapper.delFj(pd);
	}
	
	/**修改运行状态
	 * @param pd
	 * @throws Exception
	 */
	public void editIssue(PageData pd)throws Exception{
		PM_MAINTAINMapper.editIssue(pd);
	}
	
	/**获取最近一次生成保养任务的天数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPlanDateNum(PageData pd)throws Exception{
		return PM_MAINTAINMapper.findByPlanDateNum(pd);
	}
	
	/**获取生成保养任务的数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPlanNum(PageData pd)throws Exception{
		return PM_MAINTAINMapper.findByPlanNum(pd);
	}
}

