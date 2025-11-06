package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.WH_ICSTOCKBILLMapper;
import org.yy.service.mom.WH_ICSTOCKBILLService;

/** 
 * 说明： 出入库单接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-18
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class WH_ICSTOCKBILLServiceImpl implements WH_ICSTOCKBILLService{

	@Autowired
	private WH_ICSTOCKBILLMapper wh_icstockbillMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		wh_icstockbillMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		wh_icstockbillMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		wh_icstockbillMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return wh_icstockbillMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return wh_icstockbillMapper.listAll(pd);
	}
	
	/**非分页 列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> stockList(PageData pd)throws Exception{
		return wh_icstockbillMapper.stockList(pd);
	}
	
	/**
	 * 查询当前库存物料名称（去重,下拉选）
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getMatList()throws Exception{
		return wh_icstockbillMapper.getMatList();
	}
	
	/**查询设备类下的设备列表，根据设备类型id
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getEqmListByClassId(Page page)throws Exception{
		return wh_icstockbillMapper.getEqmlistPage(page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return wh_icstockbillMapper.findById(pd);
	}
	
	/**通过条码信息获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBarCode(PageData pd)throws Exception{
		return wh_icstockbillMapper.findByBarCode(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		wh_icstockbillMapper.deleteAll(ArrayDATA_IDS);
	}

	/**查询当前出入库单据最大的流水号
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findMaxBarCode(PageData pd) throws Exception {
		return wh_icstockbillMapper.findMaxBarCode(pd);
	}

	/**结束入库，批量更新
	 * @param pd 当前用户姓名
	 * @throws Exception
	 */
	@Override
	public void finishStock(PageData pd) throws Exception {
		wh_icstockbillMapper.finishStock(pd);
	}

	/**查询未结束入库列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> unFinishList(PageData pd) throws Exception {
		return wh_icstockbillMapper.unFinishList(pd);
	}

	/**通过库位代码获取库存信息列表 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByLocationCode(Page page) throws Exception {
		return wh_icstockbillMapper.findByLocationCodelistPage(page);
	}

	/**根据消耗产出料次明细ID查询库存  
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByCOUTBATCHMXID(PageData pd) throws Exception {
		return wh_icstockbillMapper.findByCOUTBATCHMXID(pd);
	}
	
	/**根据消耗产出料次明细查询库存数量和  
	 * @param page
	 * @throws Exception
	 */
	public PageData findSUMNUMByCOUTBATCHMXID(PageData pd) throws Exception{
		return wh_icstockbillMapper.findSUMNUMByCOUTBATCHMXID(pd);
	}

	/**更新库存数量
	 * @param 
	 * @throws Exception
	 */
	@Override
	public void updateQty(PageData pd) throws Exception {
		wh_icstockbillMapper.updateQty(pd);
	}

	/**查询该料次下是否存在称量库存总重量低于料次明细中物料要求下限值的数据 
	 * @param
	 * @throws Exception
	 */
	@Override
	public PageData findIllegalBatchMxNum(PageData pd) throws Exception {
		return wh_icstockbillMapper.findIllegalBatchMxNum(pd);
	}

	/**更新条码打印状态
	 * @param 
	 * @throws Exception
	 */
	@Override
	public void updatePrintState(PageData pd) throws Exception {
		wh_icstockbillMapper.updatePrintState(pd);
	}

	/**
	 * 根据条码信息查询最大的子物料条码
	 * @param pd
	 * @return Exception
	 */
	@Override
	public PageData findMaxBarCodeByInformationBarCode(PageData pd) throws Exception {
		return wh_icstockbillMapper.findMaxBarCodeByInformationBarCode(pd);
	}

	/**根据设备ID查询称（KEY_NAME='Clink'）的规范资料
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findEQMSPECIFICATIONByEQMBaseId(PageData pd) throws Exception {
		return wh_icstockbillMapper.findEQMSPECIFICATIONByEQMBaseId(pd);
	}
	
	/**
	 * 根据计划id查询物料接收单、领料单信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageData findIcsInfo(PageData pd) throws Exception {
		return wh_icstockbillMapper.findIcsInfo(pd);
	}
	
	/**
	 * 根据物料代码查询物料类别
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMatByCode(PageData pd) throws Exception{
		return wh_icstockbillMapper.findMatByCode(pd);
	}
}

