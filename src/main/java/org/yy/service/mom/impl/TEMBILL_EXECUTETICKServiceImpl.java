package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.TEMBILL_EXECUTETICKMapper;
import org.yy.service.mom.TEMBILL_EXECUTETICKService;

/** 
 * 说明： 质量检测反馈接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-24
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class TEMBILL_EXECUTETICKServiceImpl implements TEMBILL_EXECUTETICKService{

	@Autowired
	private TEMBILL_EXECUTETICKMapper tembill_executetickMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		tembill_executetickMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		tembill_executetickMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		tembill_executetickMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return tembill_executetickMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return tembill_executetickMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return tembill_executetickMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		tembill_executetickMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**根据主表ID删除数据
	 * @param pd
	 * @throws Exception
	 */
	public void deleteId(PageData pd)throws Exception{
		tembill_executetickMapper.deleteId(pd);
	}
}

