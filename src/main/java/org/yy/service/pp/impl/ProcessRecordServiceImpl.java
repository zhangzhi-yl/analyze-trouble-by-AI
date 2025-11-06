package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.ProcessRecordMapper;
import org.yy.service.pp.ProcessRecordService;

/** 
 * 说明： 过程记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-23
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ProcessRecordServiceImpl implements ProcessRecordService{

	@Autowired
	private ProcessRecordMapper ProcessRecordMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ProcessRecordMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ProcessRecordMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ProcessRecordMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ProcessRecordMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ProcessRecordMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ProcessRecordMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ProcessRecordMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> applist(AppPage page) throws Exception {
		return ProcessRecordMapper.applist(page);
	}
	
}

