package org.yy.service.mm.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.StockMapper;
import org.yy.mapper.dsno1.mm.StockOperationRecordMapper;
import org.yy.service.mm.MATSPLITService;
import org.yy.service.mm.StockService;
import org.yy.util.Tools;

/** 
 * 说明： 库存接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class StockServiceImpl implements StockService{

	@Autowired
	private StockMapper StockMapper;
	
	@Autowired
	private StockOperationRecordMapper stockOperationRecordMapper;
	@Autowired
	private MATSPLITService MATSPLITService;
	
	/**
	 * 入库红字，扣减库存，选择到仓库
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean inStockRed(PageData pd)throws Exception {
		BigDecimal num = new BigDecimal(pd.get("num").toString());//入库数量
		PageData stockpd = new PageData();
		stockpd = StockMapper.getSum(pd);//获取仓库id和物料id下的库存数量
		BigDecimal stocknum = new BigDecimal("0");//库存数量
		if(null!=stockpd && stockpd.containsKey("stockSum") && null!=stockpd.get("stockSum")) {
			stocknum = new BigDecimal(stockpd.get("stockSum").toString());
		}
		if(stocknum.compareTo(num) == -1) {//如果库存数量小于入库红字数量
			return false;
		}
		String currentTime = Tools.date2Str(new Date());
		List<PageData> list = StockMapper.stockList(pd);//获取仓库id和物料id下的库存列表,根据库存数量由小到大排序
		for(int i=0;i<list.size();i++) {
			BigDecimal a = new BigDecimal(list.get(i).get("ActualCount").toString());
			PageData temp = new PageData();
			if(num.subtract(a).compareTo(new BigDecimal("0")) == -1) {	//入库数量-库存数量《0
				//更新库存数量为负的num-a，即为num=num.subtract(a).negate();
				temp.put("Stock_ID", list.get(i).getString("Stock_ID"));
				temp.put("ActualCount", num.subtract(a).negate());
				temp.put("LastModifiedTime", currentTime);
				StockMapper.editActualCount(temp);
				break;
			} else {
				num=num.subtract(a);
				//更新库存数量为0
				temp.put("Stock_ID", list.get(i).getString("Stock_ID"));
				temp.put("ActualCount", 0);
				temp.put("LastModifiedTime", currentTime);
				StockMapper.editActualCount(temp);
			}
		}
		return true;
	}
	
	/**
	 * 入库蓝字，插入一条库存数据（出库红字），选择到库位
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean inStock(PageData pd)throws Exception {
		String UNIQUE_CODE_WHETHER=(pd.get("UNIQUE_CODE_WHETHER")==null || "".equals(pd.get("UNIQUE_CODE_WHETHER")))
				?"":pd.getString("UNIQUE_CODE_WHETHER");
		/*if(UNIQUE_CODE_WHETHER.equals("是")) {
			List<PageData> varList=MATSPLITService.listAll(pd);
			for(PageData pdVar:varList) {
				pd.put("ActualCount", pdVar.get("FQuantity")==null?"0":pdVar.get("FQuantity").toString());//
				StockMapper.save(pd);
			}
		}else {*/
			StockMapper.save(pd);
		//}
		return true;
	}
	
	/**
	 * 出库红字，插入一条库存数据（入库蓝字），选择到库位
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean outStockRed(PageData pd)throws Exception {
		//插入一条库存数据（入库蓝字）
		StockMapper.save(pd);
		return true;
	}
	
	/**
	 * 出库蓝字，添加类型码物料、唯一码物料判断
	 * @param pd.num 扣减数量
	 * @param pd.WarehouseID 仓库id
	 * @param pd.ItemID 物料id
	 * @param pd.MaterialSPropKey 辅助属性值id
	 * @param pd.PositionID(非必须，唯一码物料从指定库位扣减库存)
	 * @return
	 * @throws Exception
	 */
	public boolean outStock(PageData pd)throws Exception {
		BigDecimal num = new BigDecimal(pd.get("num").toString());//出库数量
		PageData stockpd = new PageData();
		stockpd = StockMapper.getSum(pd);//获取仓库id（仓位id）和物料id（库存二维码）和辅助属性值id下的库存数量
		BigDecimal stocknum = new BigDecimal("0");//库存数量
		//String currentTime = Tools.date2Str(new Date());
		if(null!=stockpd && stockpd.containsKey("stockSum") && null!=stockpd.get("stockSum")) {
			stocknum = new BigDecimal(stockpd.get("stockSum").toString());
		}
		if(stocknum.compareTo(num) == -1) {//如果库存数量小于出库数量
			return false;
		}
		List<PageData> list = StockMapper.stockList(pd);//获取仓库id和物料id和辅助属性值id下的库存列表,根据库存数量由小到大排序
		if(!list.isEmpty()) {
			for(int i=0;i<list.size();i++) {
				BigDecimal a = new BigDecimal(list.get(i).get("ActualCount").toString());
				PageData temp = new PageData();
				//出库数量-库存数量《0
				if(num.subtract(a).compareTo(new BigDecimal("0")) == -1) {	
					//更新库存数量为负的num-a，即为num=num.subtract(a).negate();
					temp.put("Stock_ID", list.get(i).getString("Stock_ID"));
					temp.put("ActualCount", num.subtract(a).negate());
					//temp.put("LastModifiedTime", currentTime);
					StockMapper.editActualCount(temp);
					break;
				} else {
					num=num.subtract(a);
					//更新库存数量为0
					temp.put("Stock_ID", list.get(i).getString("Stock_ID"));
					temp.put("ActualCount", 0);
					//temp.put("LastModifiedTime", currentTime);
					StockMapper.editActualCount(temp);
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		StockMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		StockMapper.delete(pd);
	}
	
	/**根据唯一码删除库存物料
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByOneThingCode(PageData pd)throws Exception{
		StockMapper.deleteByOneThingCode(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		StockMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return StockMapper.datalistPage(page);
	}
	
	/**批号查询列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> getBatchList(Page page)throws Exception{
		return StockMapper.getBatchlistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return StockMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return StockMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		StockMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**获取仓库id和物料id和辅助属性值id下的库存数量 
	 * @param pd
	 * @throws Exception
	 */
	public PageData getSum(PageData pd)throws Exception{
		return StockMapper.getSum(pd);
	}
	
	/**获取仓库id和物料id和辅助属性值id下的库存列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> stockList(PageData pd)throws Exception{
		return StockMapper.stockList(pd);
	}
	
	/**修改库存数量
	 * @param pd
	 * @throws Exception
	 */
	public void editActualCount(PageData pd)throws Exception{
		StockMapper.editActualCount(pd);
	}
	
	/**库存查询 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> stocklistPage(Page page)throws Exception{
		return StockMapper.stocklistPage(page);
	}

	/**
	 * 根据物料id和仓库类型获取物料实时库存
	 * @param pg
	 * @return
	 */
	public List<PageData> getActualCountByFTYPEAndItemID(PageData pg) {
		return StockMapper.getActualCountByFTYPEAndItemID(pg);
	}
	/**
	 * 根据条件获取二维码查询物料信息
	 */
	@Override
	public List<PageData> getMaterialInfo4QrcodeSearch(Page page) throws Exception {
		return StockMapper.getMaterialInfo4QrcodeSearchlistPage(page);
	}
	/**
	 * 库存查询-汇总
	 */
	@Override
	public List<PageData> stockTotallistPage(Page page) {
		return StockMapper.stockTotallistPage(page);
	}
	
	/**获取物料详情
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDetailsItem(PageData pd)throws Exception{
		return StockMapper.getDetailsItem(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		// TODO 自动生成的方法存根
		return StockMapper.AppList(page);
	}
	
	/**库存预警列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> safeStaocklistPage(Page page)throws Exception{
		return StockMapper.safeStaocklistPage(page);
	}

	@Override
	public List<PageData> getStockListByFTYPEAndItemID(PageData pd) {
		return StockMapper.getStockListByFTYPEAndItemID(pd);
	}
	
	/**车间-库存查询-汇总
	 * @param pd ItemID MAT_NAME PositionID
	 * @throws Exception
	 */
	public List<PageData> stockTotallistPageCJ(Page page){
		return StockMapper.stockTotallistPageCJ(page);
	}
	
	/**车间-库存查询 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> stocklistPageCJ(Page page)throws Exception{
		return StockMapper.stocklistPageCJ(page);
	}
}

