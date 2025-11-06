package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.KMCustomerMapper;
import org.yy.service.km.KMCustomerService;

/** 
 * 说明： 客户接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class KMCustomerServiceImpl implements KMCustomerService{

	@Autowired
	private KMCustomerMapper customerMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		customerMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		customerMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		customerMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return customerMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return customerMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return customerMapper.findById(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return customerMapper.findCountByCode(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		customerMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获取客户列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> getCusTomerList(PageData pd) throws Exception {
		return customerMapper.getCusTomerList(pd);
	}
	
}

