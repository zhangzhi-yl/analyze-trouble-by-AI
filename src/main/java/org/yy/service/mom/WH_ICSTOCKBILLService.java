package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 出入库单接口
 * 作者：YuanYe
 * 时间：2020-02-18
 * 
 * @version
 */
public interface WH_ICSTOCKBILLService{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**非分页 列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> stockList(PageData pd)throws Exception;
	
	/**
	 * 查询当前库存物料名称（去重,下拉选）
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getMatList()throws Exception;
	
	/**查询设备类下的设备列表，根据设备类型id
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getEqmListByClassId(Page page)throws Exception;

	/**根据设备ID查询称（KEY_NAME='Clink'）的规范资料
	 * @param pd
	 * @throws Exception
	 */
	public PageData findEQMSPECIFICATIONByEQMBaseId(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过条码信息获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBarCode(PageData pd)throws Exception;
	
	/**查询当前出入库单据最大的流水号
	 * @param pd
	 * @throws Exception
	 */
	public PageData findMaxBarCode(PageData pd)throws Exception;
	
	/**
	 * 根据条码信息查询最大的子物料条码
	 * @param pd
	 * @return Exception
	 */
	public PageData findMaxBarCodeByInformationBarCode(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**结束入库，批量更新 
	 * @param pd 当前用户姓名
	 * @throws Exception
	 */
	public void finishStock(PageData pd)throws Exception;
	
	/**查询未结束入库列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> unFinishList(PageData pd)throws Exception;
	
	/**通过库位代码获取库存信息列表 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findByLocationCode(Page page)throws Exception;
	
	/**根据消耗产出料次明细ID查询库存  
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> findByCOUTBATCHMXID(PageData pd)throws Exception;
	
	/**根据消耗产出料次明细查询库存数量和  
	 * @param page
	 * @throws Exception
	 */
	public PageData findSUMNUMByCOUTBATCHMXID(PageData pd) throws Exception;
	
	/**更新库存数量
	 * @param 
	 * @throws Exception
	 */
	public void updateQty(PageData pd) throws Exception;
	
	/**更新条码打印状态
	 * @param 
	 * @throws Exception
	 */
	public void updatePrintState(PageData pd) throws Exception;
	
	/**查询该料次下是否存在称量库存总重量低于料次明细中物料要求下限值的数据 
	 * @param
	 * @throws Exception
	 */
	public PageData findIllegalBatchMxNum(PageData pd) throws Exception;
	
	/**
	 * 根据计划id查询物料接收单、领料单信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findIcsInfo(PageData pd) throws Exception;

	/**
	 * 根据物料代码查询物料类别
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMatByCode(PageData pd) throws Exception;
}

