package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.SupplierMapper;
import org.yy.service.km.SupplierService;

/** 
 * 说明： 供应商接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SupplierServiceImpl implements SupplierService{

	@Autowired
	private SupplierMapper supplierMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		supplierMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		supplierMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		supplierMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return supplierMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return supplierMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return supplierMapper.findById(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return supplierMapper.findCountByCode(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		supplierMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获取供应商列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getSupplierList(PageData pd) {
		return supplierMapper.getSupplierList(pd);
	}
	
}

