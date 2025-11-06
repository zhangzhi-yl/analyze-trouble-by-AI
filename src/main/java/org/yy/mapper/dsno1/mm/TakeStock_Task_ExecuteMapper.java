package org.yy.mapper.dsno1.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 盘点任务物料明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-28
 * 官网：356703572@qq.com
 * @version
 */
public interface TakeStock_Task_ExecuteMapper{

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
	 * 根据物料ID从盘点任务明细表中获取数据，判断当前扫码的物料是否存在于表中
	 */
	PageData getMaterial(PageData pd);
	/**
	 * 获取物料明细下盘点执行明细的条数 ，用以确定行号
	 */
	Integer getEntryID(PageData pd);
	/**
	 * 盘点任务完成
	 */
	void FinishTakeStock(PageData pd);
	/**
	 * 行关闭
	 */
	void CloseHang(PageData pd);
}

