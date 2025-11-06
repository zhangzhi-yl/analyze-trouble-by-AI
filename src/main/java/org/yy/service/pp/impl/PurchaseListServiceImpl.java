package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PurchaseListMapper;
import org.yy.service.pp.PurchaseListService;

/** 
 * 说明： 采购订单接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-09
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PurchaseListServiceImpl implements PurchaseListService{

	@Autowired
	private PurchaseListMapper PurchaseListMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PurchaseListMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PurchaseListMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PurchaseListMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PurchaseListMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PurchaseListMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PurchaseListMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PurchaseListMapper.deleteAll(ArrayDATA_IDS);
	}

	/**审核或反审核采购订单
	 * @param pd
	 */
	@Override
	public void editAudit(PageData pd) throws Exception {
		PurchaseListMapper.editAudit(pd);
	}

	/**单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return PurchaseListMapper.getRepeatNum(pd);
	}

	/**结束
	 * @param pd
	 */
	@Override
	public void over(PageData pd) throws Exception {
		PurchaseListMapper.over(pd);
	}

	/**获取采购订单编号列表-可搜索-前100条
	 * @param pd
	 */
	@Override
	public List<PageData> getFNumList(PageData pd)throws Exception {
		return PurchaseListMapper.getFNumList(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.pp.PurchaseListService#listXMCG(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listXMCG(Page page) throws Exception {
		return PurchaseListMapper.listXMCGlistPage(page);
	}
	
}

