package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.SALESORDERMapper;
import org.yy.service.pp.SALESORDERService;

/** 
 * 说明： 销售订单接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SALESORDERServiceImpl implements SALESORDERService{

	@Autowired
	private SALESORDERMapper salesorderMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		salesorderMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		salesorderMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		salesorderMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return salesorderMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return salesorderMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return salesorderMapper.findById(pd);
	}
	
	/**通过订单编号查询数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByOrderNum(PageData pd)throws Exception{
		return salesorderMapper.findByOrderNum(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		salesorderMapper.deleteAll(ArrayDATA_IDS);
	}

	/**审核或反审核销售订单
	 * @param pd
	 */
	@Override
	public void editAudit(PageData pd) throws Exception {
		salesorderMapper.editAudit(pd);
	}

	/**订单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return salesorderMapper.getRepeatNum(pd);
	}

	/**查询-状态列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getStateList(PageData pd) throws Exception {
		return salesorderMapper.getStateList(pd);
	}

	/**获取订单编号列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getOrderNumList(PageData pd) throws Exception {
		return salesorderMapper.getOrderNumList(pd);
	}

	/**结束
	 * @param pd
	 */
	@Override
	public void over(PageData pd) throws Exception {
		salesorderMapper.over(pd);
	}

	/**反写销售订单投产数量
	 * @param pd
	 */
	@Override
	public void calFProductionQuantity(PageData pd) throws Exception {
		salesorderMapper.calFProductionQuantity(pd);
	}


	/**反写销售订单状态
	 * @param pd
	 */
	@Override
	public void calFStatus(PageData pd) throws Exception {
		salesorderMapper.calFStatus(pd);
	}
	
}

