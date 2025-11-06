package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 盘点任务Mapper
 * 作者：YuanYe
 * 时间：2020-11-26
 * 
 * @version
 */
public interface Takestock_TaskMapper{

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
	List<PageData> AppList(AppPage page);
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
	/**
	 * 下发盘点任务
	 */
	void goLssue(PageData pd);
	/**
	 * 获取仓库下物料列表
	 */
	List<PageData> getAllMaterial(PageData pd);
	/**
	 * 获取订单下物料列表
	 */
	List<PageData> getAllDDMaterial(PageData pd);
	/**
	 * 仓库列表
	 */
	List<PageData> getWarehouse(PageData pd);
	/**
	 * 盘点任务提交审核
	 */
	void goCheck(PageData pd);
	/**
	 * 驳回重盘后，修改差异单编号
	 */
	void updateDifferenceOrder_NUM(PageData pd);
	/**
	 * 审核通过
	 */
	void CheckPass(PageData pd);
	/**
	 * 获取盘点执行表中物料的数量
	 */
    PageData getCount(PageData pd);
    /**
     * 获取一条盘点执行表中的数据
     */
    PageData findByMaterialId(PageData pd);
    /**
     * 获取盘点任务已审核通过列表
     * @param pd
     * @return
     */
    List<PageData> datalistPageHIS(Page page);
    /**
     * 获取盘点任务下生成过几次盘盈盘亏单
     */
    List<PageData> DifferenceOrder_Count(PageData pd);
    /**
     * 生成盘盈盘亏单后修改主表的生成状态
     */
    void CreatOrder(PageData pd);
    List<PageData> listResult(PageData pd);
    PageData getTakeStocHIS(PageData pd);
}

