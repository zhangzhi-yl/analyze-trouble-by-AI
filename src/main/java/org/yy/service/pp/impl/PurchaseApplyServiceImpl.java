package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PurchaseApplyMapper;
import org.yy.service.pp.PurchaseApplyService;

/** 
 * 说明： 采购申请接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-21
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PurchaseApplyServiceImpl implements PurchaseApplyService{

	@Autowired
	private PurchaseApplyMapper PurchaseApplyMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PurchaseApplyMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PurchaseApplyMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PurchaseApplyMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PurchaseApplyMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PurchaseApplyMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PurchaseApplyMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PurchaseApplyMapper.deleteAll(ArrayDATA_IDS);
	}

	/**单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return PurchaseApplyMapper.getRepeatNum(pd);
	}

	/**审核或反审核采购申请
	 * @param pd
	 */
	@Override
	public void editAudit(PageData pd) throws Exception {
		PurchaseApplyMapper.editAudit(pd);
	}

	/**获取采购申请编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getCGNumList(PageData pd) throws Exception {
		return PurchaseApplyMapper.getCGNumList(pd);
	}
	
	/**根据销售订单ID获取单据下物料
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getWLNumList(PageData pd) throws Exception {
		return PurchaseApplyMapper.getWLNumList(pd);
	}
	
}

