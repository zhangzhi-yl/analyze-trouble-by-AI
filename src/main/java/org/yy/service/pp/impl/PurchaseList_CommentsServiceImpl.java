package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PurchaseList_CommentsMapper;
import org.yy.service.pp.PurchaseList_CommentsService;

/** 
 * 说明： 采购订单审核意见接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-14
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PurchaseList_CommentsServiceImpl implements PurchaseList_CommentsService{

	@Autowired
	private PurchaseList_CommentsMapper PurchaseList_CommentsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PurchaseList_CommentsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PurchaseList_CommentsMapper.delete(pd);
	}
	
	/**根据单据id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByBillID(PageData pd)throws Exception{
		PurchaseList_CommentsMapper.deleteByBillID(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PurchaseList_CommentsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PurchaseList_CommentsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PurchaseList_CommentsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PurchaseList_CommentsMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PurchaseList_CommentsMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

