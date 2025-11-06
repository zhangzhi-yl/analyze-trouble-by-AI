package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.WorkRecordMapper;
import org.yy.service.pp.WorkRecordService;

/** 
 * 说明： 操作工时记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-02-05
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WorkRecordServiceImpl implements WorkRecordService{

	@Autowired
	private WorkRecordMapper WorkRecordMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		WorkRecordMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		WorkRecordMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		WorkRecordMapper.edit(pd);
	}
	public void editEnd(PageData pd)throws Exception{
		WorkRecordMapper.editEnd(pd);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return WorkRecordMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return WorkRecordMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return WorkRecordMapper.findById(pd);
	}
	
	public PageData findByExampleID(PageData pd)throws Exception{
		return WorkRecordMapper.findByExampleID(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		WorkRecordMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过任务id统计个人工时
	 * @param pd
	 * @throws Exception
	 */
	public PageData selectWorkTime(PageData pd)throws Exception{
		return WorkRecordMapper.selectWorkTime(pd);
	}
}

