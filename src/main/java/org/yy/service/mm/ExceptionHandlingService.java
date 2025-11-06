package org.yy.service.mm;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 异常处理接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-10
 * 官网：356703572@qq.com
 * @version
 */
public interface ExceptionHandlingService{

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
	 * 修改异常处理记录的异常判别状态
	 */
	public void EditDisposeType(PageData pd)throws Exception;
	/**
	 * 获取异常下异常处理记录中最后一条
	 * @param pd
	 * @return
	 */
	public PageData getReorder(PageData pd)throws Exception;
	/**
	 * 异常处理完成,修改异常判别状态为已处理
	 */
	public void FinishException(PageData pd)throws Exception;
}

