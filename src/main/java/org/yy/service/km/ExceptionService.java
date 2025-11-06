package org.yy.service.km;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 异常接口 作者：YuanYes QQ356703572 时间：2020-11-09 官网：356703572@qq.com
 * 
 * @version
 */
public interface ExceptionService {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception;

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception;

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception;

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception;

	public List<PageData> Applist(AppPage page) throws Exception;

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception;

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception;

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception;

	/**
	 * 下发异常
	 */
	public void goLssue(PageData pd) throws Exception;

	/**
	 * 异常处理记录列表(带分页)
	 * 
	 * @param page
	 * @return
	 */
	public List<PageData> datalistPageHandling(Page page) throws Exception;

	/**
	 * 异常处理记录（不带分页，用于导出）
	 */
	public List<PageData> listAllHandling(PageData pd) throws Exception;

	/**
	 * 异常处理记录查看
	 */
	public PageData getHandling(PageData pd) throws Exception;

	/**
	 * 完成异常
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void FinishException(PageData pd) throws Exception;

	/**
	 * 设备列表
	 */
	public List<PageData> getEQMList(PageData pd) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listSC(Page page) throws Exception;

	/**
	 * @param pd
	 */
	public void setRead(PageData pd) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listHis(Page page) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listXM(Page page) throws Exception;

}
