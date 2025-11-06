package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.ProcessWorkOrderExample_SopStepMapper;
import org.yy.service.pp.ProcessWorkOrderExample_SopStepService;

/** 
 * 说明： 生产任务SOP实例接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-23
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ProcessWorkOrderExample_SopStepServiceImpl implements ProcessWorkOrderExample_SopStepService{

	@Autowired
	private ProcessWorkOrderExample_SopStepMapper ProcessWorkOrderExample_SopStepMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ProcessWorkOrderExample_SopStepMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ProcessWorkOrderExample_SopStepMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ProcessWorkOrderExample_SopStepMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ProcessWorkOrderExample_SopStepMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ProcessWorkOrderExample_SopStepMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ProcessWorkOrderExample_SopStepMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ProcessWorkOrderExample_SopStepMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public void deleteByTaskID(String taskId) throws Exception {
		ProcessWorkOrderExample_SopStepMapper.deleteByTaskID(taskId);
		
	}

	@Override
	public List<PageData> appListPage(AppPage page) {
		return ProcessWorkOrderExample_SopStepMapper.appListPage(page);
	}
	
}

