package org.yy.service.KeyCustomers.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.KeyCustomers.CustomerGJMapper;
import org.yy.service.KeyCustomers.CustomerGJService;

/** 
 * 说明： 客户跟进接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-02
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class CustomerGJServiceImpl implements CustomerGJService{

	@Autowired
	private CustomerGJMapper CustomerGJMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		CustomerGJMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		CustomerGJMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		CustomerGJMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return CustomerGJMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return CustomerGJMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return CustomerGJMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		CustomerGJMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> listToday(Page page) throws Exception {
		return CustomerGJMapper.listToday(page);
	}

	@Override
	public List<PageData> LastWeekList(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return CustomerGJMapper.LastWeekList(pd);
	}

	@Override
	public List<PageData> undoneList(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return CustomerGJMapper.undoneList(pd);
	}

	@Override
	public List<PageData> FinishList(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return CustomerGJMapper.FinishList(pd);
	}

	@Override
	public void saveWeekReport(PageData pd) throws Exception {
		CustomerGJMapper.saveWeekReport(pd);
	}
	
}

