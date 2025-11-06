package org.yy.service.KeyCustomers.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.KeyCustomers.KeyCustomersMapper;
import org.yy.service.KeyCustomers.KeyCustomersService;

/** 
 * 说明： 客户接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class KeyCustomersServiceImpl implements KeyCustomersService{

	@Autowired
	private KeyCustomersMapper KeyCustomersMapper;

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return KeyCustomersMapper.datalistPage(page);
	}

	@Override
	public List<PageData> listAll(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return KeyCustomersMapper.listAll(pd);
	}
	
}

