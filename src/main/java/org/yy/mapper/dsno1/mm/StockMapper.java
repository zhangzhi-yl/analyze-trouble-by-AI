package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 库存Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 * @version
 */
public interface StockMapper{

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
	
	/**根据唯一码删除库存物料
	 * @param pd
	 * @throws Exception
	 */
	void deleteByOneThingCode(PageData pd);
	
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
	
	
	/**
	 * 根据条件获取二维码查询物料信息
	 * @param page
	 * @return
	 */
	List<PageData> getMaterialInfo4QrcodeSearchlistPage(Page page);
	
	/**批号查询列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> getBatchlistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**获取仓库id和物料id和辅助属性值id下的库存数量 
	 * @param pd
	 * @throws Exception
	 */
	PageData getSum(PageData pd);
	
	/**获取仓库id和物料id和辅助属性值id下的库存列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> stockList(PageData pd);
	
	/**修改库存数量
	 * @param pd
	 * @throws Exception
	 */
	void editActualCount(PageData pd);
	
	/**库存查询 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> stocklistPage(Page page);
	
	/**库存查询 汇总
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> stockTotallistPage(Page page);
	/**
	 * 根据物料id和仓库类型获取物料实时库存
	 * @param pg
	 * @return
	 */
	List<PageData>getActualCountByFTYPEAndItemID(PageData pg);
	/**
	 * 根据物料id和仓库类型获取物料列表
	 * @param pg
	 * @return
	 */
	List<PageData>getStockListByFTYPEAndItemID(PageData pg);
	
	/**获取物料详情
	 * @param pd
	 * @throws Exception
	 */
	PageData getDetailsItem(PageData pd);
	/**
	 * 手机端库存查询列表
	 */
	List<PageData> AppList(AppPage page);
	
	/**库存预警列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> safeStaocklistPage(Page page);
	
	/**车间-库存查询-汇总
	 * @param pd ItemID MAT_NAME PositionID
	 * @throws Exception
	 */
	List<PageData> stockTotallistPageCJ(Page page);
	
	/**车间-库存查询 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> stocklistPageCJ(Page page);
}

