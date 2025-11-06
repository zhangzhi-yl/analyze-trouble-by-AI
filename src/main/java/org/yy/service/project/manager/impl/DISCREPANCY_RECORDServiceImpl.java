package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.DISCREPANCY_RECORDMapper;
import org.yy.service.project.manager.DISCREPANCY_RECORDService;

/** 
 * 说明： 差异记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-09-16
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class DISCREPANCY_RECORDServiceImpl implements DISCREPANCY_RECORDService{

	@Autowired
	private DISCREPANCY_RECORDMapper discrepancy_recordMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		discrepancy_recordMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		discrepancy_recordMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		discrepancy_recordMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return discrepancy_recordMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return discrepancy_recordMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return discrepancy_recordMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		discrepancy_recordMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**列表所有差异物料(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getAllListWLDB(PageData pd)throws Exception{
		return discrepancy_recordMapper.getAllListWLDB(pd);
	}
}

