package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.SALESORDERDETAILMapper;
import org.yy.service.pp.SALESORDERDETAILService;

/** 
 * 说明： 销售订单明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SALESORDERDETAILServiceImpl implements SALESORDERDETAILService{

	@Autowired
	private SALESORDERDETAILMapper salesorderdetailMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		salesorderdetailMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		salesorderdetailMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		salesorderdetailMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return salesorderdetailMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return salesorderdetailMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return salesorderdetailMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		salesorderdetailMapper.deleteAll(ArrayDATA_IDS);
	}

	/**关联行关闭订单明细
	 * @param pd
	 */
	@Override
	public void deleteMxRelated(PageData pd) throws Exception {
		salesorderdetailMapper.deleteMxRelated(pd);
	}

	/**行关闭/反行关闭
	 * @param pd
	 */
	@Override
	public void rowClose(PageData pd) throws Exception {
		salesorderdetailMapper.rowClose(pd);
	}

	/**生成订单明细行号
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getFROWNO(PageData pd) throws Exception {
		return salesorderdetailMapper.getFROWNO(pd);
	}

	/**销售订单明细-物料列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllMat(PageData pd) throws Exception {
		return salesorderdetailMapper.listAllMat(pd);
	}

	/**批量选择销售订单物料 
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> listAllSelect(String[] arrayDATA_IDS) throws Exception {
		return salesorderdetailMapper.listAllSelect(arrayDATA_IDS);
	}

	/**批量选择销售订单物料
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> selectAllSale(String[] arrayDATA_IDS) throws Exception {
		return salesorderdetailMapper.selectAllSale(arrayDATA_IDS);
	}

	/**反写源单下推数量
	 * @param pdSave
	 */
	@Override
	public void calFPushCount(PageData pdSave) throws Exception {
		salesorderdetailMapper.calFPushCount(pdSave);
	}

	/**销售订单明细-物料列表-下推发运申请
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllMatForward(PageData pd) throws Exception {
		return salesorderdetailMapper.listAllMatForward(pd);
	}

	/**发运申请-批量选择销售订单物料
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> selectAllSaleForward(String[] arrayDATA_IDS) throws Exception {
		return salesorderdetailMapper.selectAllSaleForward(arrayDATA_IDS);
	}

	/**反写源单下推发运申请数量
	 * @param pdSave
	 */
	@Override
	public void calFPushCountForward(PageData pdSave) throws Exception {
		 salesorderdetailMapper.calFPushCountForward(pdSave);
	}

	/**一键反写源单下推数量
	 * @param pd
	 */
	@Override
	public void calFPushCountForwardAll(PageData pd) throws Exception {
		 salesorderdetailMapper.calFPushCountForwardAll(pd);
	}


	/**反写源单下推计划工单数量
	 * @param pdSave
	 */
	@Override
	public void calProductionQuantity(PageData pdSave) throws Exception {
		salesorderdetailMapper.calProductionQuantity(pdSave);
	}
	
}

