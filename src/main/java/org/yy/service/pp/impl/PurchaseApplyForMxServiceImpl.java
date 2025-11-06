package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PurchaseApplyForMxMapper;
import org.yy.service.pp.PurchaseApplyForMxService;

/** 
 * 说明： 采购申请(明细)接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-20
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PurchaseApplyForMxServiceImpl implements PurchaseApplyForMxService{

	@Autowired
	private PurchaseApplyForMxMapper PurchaseApplyForMxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PurchaseApplyForMxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PurchaseApplyForMxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PurchaseApplyForMxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PurchaseApplyForMxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PurchaseApplyForMxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PurchaseApplyForMxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PurchaseApplyForMxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return PurchaseApplyForMxMapper.findCount(pd);
	}
	
	/**已选物料列表(分页)
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listSeepush(Page page)throws Exception{
		return PurchaseApplyForMxMapper.datalistPageSeepush(page);
	}
	
	/**已选物料列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listSeepushAll(PageData pd)throws Exception{
		return PurchaseApplyForMxMapper.listSeepushAll(pd);
	}
	
	/**更新主表ID与状态
	 * @param pd
	 * @throws Exception
	 */
	public void creact(PageData pd)throws Exception{
		PurchaseApplyForMxMapper.creact(pd);
	}
	
	/**修改数量
	 * @param pd
	 * @throws Exception
	 */
	public void editQty(PageData pd)throws Exception{
		PurchaseApplyForMxMapper.editQty(pd);
	}
}

