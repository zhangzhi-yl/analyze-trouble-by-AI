package org.yy.service.pm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pm.SPAREREQUISITIONMXMapper;
import org.yy.service.pm.SPAREREQUISITIONMXService;

/** 
 * 说明： 备件领用单明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-26
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SPAREREQUISITIONMXServiceImpl implements SPAREREQUISITIONMXService{

	@Autowired
	private SPAREREQUISITIONMXMapper sparerequisitionmxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		sparerequisitionmxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		sparerequisitionmxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		sparerequisitionmxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return sparerequisitionmxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return sparerequisitionmxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return sparerequisitionmxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		sparerequisitionmxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**审核校验
	 * @param pd
	 * @throws Exception
	 */
	public PageData listAllVerify(PageData pd)throws Exception{
		return sparerequisitionmxMapper.listAllVerify(pd);
	}
}

