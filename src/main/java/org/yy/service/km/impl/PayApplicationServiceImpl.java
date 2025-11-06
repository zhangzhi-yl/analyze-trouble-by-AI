package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.PayApplicationMapper;
import org.yy.service.km.PayApplicationService;

/** 
 * 说明： 付款申请接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-17
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PayApplicationServiceImpl implements PayApplicationService{

	@Autowired
	private PayApplicationMapper PayApplicationMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PayApplicationMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PayApplicationMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PayApplicationMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PayApplicationMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PayApplicationMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PayApplicationMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PayApplicationMapper.deleteAll(ArrayDATA_IDS);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.km.PayApplicationService#getCopyName(org.yy.entity.PageData)
	 */
	@Override
	public PageData getCopyName(PageData pageData) throws Exception {
		return PayApplicationMapper.getCopyName(pageData);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.km.PayApplicationService#editAudit(org.yy.entity.PageData)
	 */
	@Override
	public void editAudit(PageData pd) throws Exception {
		 PayApplicationMapper.editAudit(pd);
	}
	
}

