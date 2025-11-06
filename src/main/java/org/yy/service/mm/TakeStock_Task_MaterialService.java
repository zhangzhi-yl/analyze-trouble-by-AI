package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 盘点任务物料明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-30
 * 官网：356703572@qq.com
 * @version
 */
public interface TakeStock_Task_MaterialService{

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
	 * 行关闭
	 */
	public void CloseHang(PageData pd)throws Exception;
	
	/**获取一级物料明细数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData getTaskMaterialNum(PageData pd)throws Exception;
	
	/**获取一级物料明细详情
	 * @param pd
	 * @throws Exception
	 */
	public PageData getTaskMaterial(PageData pd)throws Exception;
	
	/**通过任务ID获取物料明细列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getTaskMaterialAll(PageData pd)throws Exception;
}

