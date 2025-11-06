package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.PurchaseMaterialDetailsMapper;
import org.yy.service.pp.PurchaseMaterialDetailsService;

/** 
 * 说明： 采购物料明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-09
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PurchaseMaterialDetailsServiceImpl implements PurchaseMaterialDetailsService{

	@Autowired
	private PurchaseMaterialDetailsMapper PurchaseMaterialDetailsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		PurchaseMaterialDetailsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		PurchaseMaterialDetailsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		PurchaseMaterialDetailsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return PurchaseMaterialDetailsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return PurchaseMaterialDetailsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return PurchaseMaterialDetailsMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		PurchaseMaterialDetailsMapper.deleteAll(ArrayDATA_IDS);
	}

	/**生成行号
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRowNum(PageData pd) throws Exception {
		return PurchaseMaterialDetailsMapper.getRowNum(pd);
	}

	/**行关闭/反行关闭
	 * @param pd
	 */
	@Override
	public void rowClose(PageData pd) throws Exception {
		PurchaseMaterialDetailsMapper.rowClose(pd);
	}

	/**关联行关闭采购订单明细
	 * @param pd
	 */
	@Override
	public void deleteMxRelated(PageData pd) throws Exception {
		PurchaseMaterialDetailsMapper.deleteMxRelated(pd);
	}

	/**批量选择采购请单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> listAllSelect(String[] arrayDATA_IDS) throws Exception {
		return PurchaseMaterialDetailsMapper.listAllSelect(arrayDATA_IDS);
	}

	/**计算下推数量
	 * @param pd
	 */
	@Override
	public void calFPushCount(PageData pd) throws Exception {
		PurchaseMaterialDetailsMapper.calFPushCount(pd);
	}

	/**列表不分页
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllX(PageData pd) throws Exception {
		return PurchaseMaterialDetailsMapper.listAllX(pd);
	}
	
	/**更新下推数量
	 * @param pd
	 * @throws Exception
	 */
	public void editFPushCount(PageData pd)throws Exception{
		PurchaseMaterialDetailsMapper.editFPushCount(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPushId(PageData pd)throws Exception{
		return PurchaseMaterialDetailsMapper.findByPushId(pd);
	}
}

