package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 库存接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 * @version
 */
public interface StockService{

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
	
	/**根据唯一码删除库存物料
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByOneThingCode(PageData pd)throws Exception;
	
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
	
	/**批号查询列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> getBatchList(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**获取仓库id和物料id和辅助属性值id下的库存数量 
	 * @param pd
	 * @throws Exception
	 */
	public PageData getSum(PageData pd)throws Exception;
	
	/**获取仓库id和物料id和辅助属性值id下的库存列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> stockList(PageData pd)throws Exception;
	
	/**修改库存数量
	 * @param pd
	 * @throws Exception
	 */
	public void editActualCount(PageData pd)throws Exception;
	
	/**
	 * 出库蓝字
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean outStock(PageData pd)throws Exception;
	
	/**
	 * 入库蓝字，插入一条库存数据（出库红字），选择到库位
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean inStock(PageData pd)throws Exception;
	
	/**
	 * 出库红字，插入一条库存数据（入库蓝字），选择到库位
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean outStockRed(PageData pd)throws Exception ;

	/**
	 * 入库红字，扣减库存，选择到仓库
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean inStockRed(PageData pd)throws Exception ;
	
	/**库存查询 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> stocklistPage(Page page)throws Exception;
	
	/**
	 * 根据条件获取二维码查询物料信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData>  getMaterialInfo4QrcodeSearch (Page page)throws Exception;
	/**
	 * 根据物料id和仓库类型获取物料实时库存
	 * @param pg
	 * @return
	 */
	public List<PageData>getActualCountByFTYPEAndItemID(PageData pg);
	/**库存查询-汇总
	 * @param pd ItemID MAT_NAME PositionID
	 * @throws Exception
	 */
	public List<PageData> stockTotallistPage(Page page);
	
	/**获取物料详情
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDetailsItem(PageData pd)throws Exception;
	public List<PageData> AppList(AppPage page)throws Exception;
	
	/**库存预警列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> safeStaocklistPage(Page page)throws Exception;

	public List<PageData> getStockListByFTYPEAndItemID(PageData pd);
	
	/**车间-库存查询-汇总
	 * @param pd ItemID MAT_NAME PositionID
	 * @throws Exception
	 */
	public List<PageData> stockTotallistPageCJ(Page page);
	
	/**车间-库存查询 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> stocklistPageCJ(Page page)throws Exception;
}

