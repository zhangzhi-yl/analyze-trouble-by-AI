package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 盘点任务物料明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-28
 * 官网：356703572@qq.com
 * @version
 */
public interface TakeStock_Task_ExecuteService{

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
	 * 根据物料ID从盘点任务明细表中获取数据，判断当前扫码的物料是否存在于表中
	 */
	public PageData getMaterial(PageData pd)throws Exception;
	/**
	 * 获取物料明细下盘点执行明细的条数 ，用以确定行号
	 */
	public Integer getEntryID(PageData pd)throws Exception;
	/**
	 * 盘点任务完成
	 */
	public void FinishTakeStock(PageData pd)throws Exception;
	/**
	 * 行关闭
	 */
	public void CloseHang(PageData pd)throws Exception;
}

