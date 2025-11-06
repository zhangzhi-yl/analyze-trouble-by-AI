package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.STORAGE_UNITCLASSMapper;
import org.yy.service.mom.STORAGE_UNITCLASSService;

/** 
 * 说明： 存储单元类接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-16
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class STORAGE_UNITCLASSServiceImpl implements STORAGE_UNITCLASSService{

	@Autowired
	private STORAGE_UNITCLASSMapper storage_unitclassMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		storage_unitclassMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		storage_unitclassMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		storage_unitclassMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return storage_unitclassMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return storage_unitclassMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return storage_unitclassMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		storage_unitclassMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

