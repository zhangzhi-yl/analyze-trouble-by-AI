package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 盘点任务接口
 * 作者：YuanYe
 * 时间：2020-11-26
 * 
 * @version
 */
public interface Takestock_TaskService{

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
	public List<PageData> AppList(AppPage page)throws Exception;
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
	/**
	 * 下发盘带任务
	 */
	public void goLssue(PageData pd)throws Exception;
	/**
	 * 获取仓库下物料列表
	 */
	public List<PageData> getAllMaterial(PageData pd)throws Exception;
	/**
	 * 获取订单下物料列表
	 */
	public List<PageData> getAllDDMaterial(PageData pd)throws Exception;
	/**
	 * 获取仓库列表
	 */
	public List<PageData> getWarehouse(PageData pd)throws Exception;
	/**
	 * 盘点任务提交审核
	 */
	public void goCheck(PageData pd)throws Exception;
	/**
	 * 驳回重盘后，修改差异单编号
	 */
	public void updateDifferenceOrder_NUM(PageData pd)throws Exception;
	/**
	 * 审核通过
	 */
	public void CheckPass(PageData pd)throws Exception;
	/**
	 * 获取盘点执行表中物料的数量
	 */
    public PageData getCount(PageData pd)throws Exception;
    /**
     * 获取一条盘点执行表中的数据
     */
    public PageData findByMaterialId(PageData pd)throws Exception;
    /**
     * 获取盘点任务已审核通过列表
     * @param pd
     * @return
     */
    public List<PageData> listHIS(Page page)throws Exception;
    /**
     * 获取盘点任务下生成过几次盘盈盘亏单
     */
   public List<PageData> DifferenceOrder_Count(PageData pd)throws Exception;
   /**
    * 生成盘盈盘亏单后修改主表的生成状态
    */
   public void CreatOrder(PageData pd)throws Exception;
   public List<PageData> listResult(PageData pd)throws Exception;
   public PageData getTakeStocHIS(PageData pd)throws Exception;
}

