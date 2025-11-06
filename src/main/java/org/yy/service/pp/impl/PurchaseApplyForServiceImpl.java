package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PurchaseApplyForMapper;
import org.yy.service.pp.PurchaseApplyForService;

/** 
 * 说明： 采购申请接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-20
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PurchaseApplyForServiceImpl implements PurchaseApplyForService{

	@Autowired
	private PurchaseApplyForMapper PurchaseApplyForMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PurchaseApplyForMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PurchaseApplyForMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PurchaseApplyForMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PurchaseApplyForMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PurchaseApplyForMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PurchaseApplyForMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PurchaseApplyForMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**更新审核状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		PurchaseApplyForMapper.editState(pd);
	}
	
	/**更新审批状态
	 * @param pd
	 * @throws Exception
	 */
	public void editAudit(PageData pd)throws Exception{
		PurchaseApplyForMapper.editAudit(pd);
	}
	
	/**审批列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listAudit(Page page)throws Exception{
		return PurchaseApplyForMapper.datalistPageAudit(page);
	}
	
	/**带明细导出
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listExcelAll(PageData pd)throws Exception{
		return PurchaseApplyForMapper.listExcelAll(pd);
	}
}

