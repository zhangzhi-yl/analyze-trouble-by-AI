package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.STORAGE_BILLMapper;
import org.yy.service.mom.STORAGE_BILLService;

/** 
 * 说明： 出入存储单据接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-16
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class STORAGE_BILLServiceImpl implements STORAGE_BILLService{

	@Autowired
	private STORAGE_BILLMapper storage_billMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		storage_billMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		storage_billMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		storage_billMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return storage_billMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return storage_billMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return storage_billMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		storage_billMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**先进先出查询数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByqtyBig(PageData pd)throws Exception{
		return storage_billMapper.findByqtyBig(pd);
	}
	
	/**先进后出查询数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByqty(PageData pd)throws Exception{
		return storage_billMapper.findByqty(pd);
	}
	
	/**更改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		storage_billMapper.editState(pd);
	}
}

