package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 出入库单Mapper
 * 作者：YuanYe
 * 时间：2020-02-18
 * 
 * @version
 */
public interface WH_ICSTOCKBILLMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**非分页 列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> stockList(PageData pd);
	
	/**
	 * 查询当前库存物料名称（去重,下拉选）
	 * @return
	 * @throws Exception
	 */
	List<PageData> getMatList();
	
	/**查询设备类下的设备列表，根据设备类型id
	 * @return
	 * @throws Exception
	 */
	List<PageData> getEqmlistPage(Page page);
	
	/**根据设备ID查询称（KEY_NAME='Clink'）的规范资料
	 * @param pd
	 * @throws Exception
	 */
	PageData findEQMSPECIFICATIONByEQMBaseId(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**通过条码信息获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByBarCode(PageData pd);
	
	/**
	 * 查询当前出入库单据最大的流水号
	 * @param pd
	 * @return
	 */
	PageData findMaxBarCode(PageData pd);
	
	/**
	 * 根据条码信息查询最大的子物料条码
	 * @param pd
	 * @return
	 */
	PageData findMaxBarCodeByInformationBarCode(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**结束入库，批量更新
	 * @param 当前用户姓名
	 * @throws Exception
	 */
	void finishStock(PageData pd);
	
	/**查询未结束入库列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> unFinishList(PageData pd);
	
	/**通过库位代码获取库存信息列表 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> findByLocationCodelistPage(Page page);
	
	/**根据消耗产出料次明细ID查询库存  
	 * @param page
	 * @throws Exception
	 */
	List<PageData> findByCOUTBATCHMXID(PageData pd);
	
	/**根据消耗产出料次明细查询库存数量和  
	 * @param page
	 * @throws Exception
	 */
	PageData findSUMNUMByCOUTBATCHMXID(PageData pd);
	
	/**更新库存数量
	 * @param 
	 * @throws Exception
	 */
	void updateQty(PageData pd);
	
	/**更新条码打印状态
	 * @param 
	 * @throws Exception
	 */
	void updatePrintState(PageData pd);
	
	/**查询该料次下是否存在称量库存总重量低于料次明细中物料要求下限值的数据 
	 * @param
	 * @throws Exception
	 */
	PageData findIllegalBatchMxNum(PageData pd);
	
	/** 根据计划id查询物料接收单、领料单信息
	 * @param pd
	 * @return
	 */
	PageData findIcsInfo(PageData pd);
	
	/** 根据物料代码查询物料类别
	 * @param pd
	 * @return
	 */
	PageData findMatByCode(PageData pd);
}

