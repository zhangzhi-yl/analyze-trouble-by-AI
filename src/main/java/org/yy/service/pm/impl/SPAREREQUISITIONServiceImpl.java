package org.yy.service.pm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pm.SPAREREQUISITIONMapper;
import org.yy.service.pm.SPAREREQUISITIONService;

/** 
 * 说明： 备件领用单接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-26
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SPAREREQUISITIONServiceImpl implements SPAREREQUISITIONService{

	@Autowired
	private SPAREREQUISITIONMapper sparerequisitionMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		sparerequisitionMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		sparerequisitionMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		sparerequisitionMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return sparerequisitionMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return sparerequisitionMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return sparerequisitionMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		sparerequisitionMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**备品备件详情列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPageEqm(Page page)throws Exception{
		return sparerequisitionMapper.datalistPageEqm(page);
	}
}

