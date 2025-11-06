package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 装配详情表接口 作者：YuanYes QQ356703572 时间：2021-04-30 官网：356703572@qq.com
 * 
 * @version
 */
public interface Cabinet_Assembly_DetailService {

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

	public List<PageData> listJssj(Page page);

	public List<PageData> mesTocStatus(PageData pd) throws Exception;

	public PageData mesTocStatusCountByYJBS(PageData pd) throws Exception;

	public PageData LoadCountByBeginEndTime(PageData pd) throws Exception;

	public List<PageData> getBeginEndTime() throws Exception;

	public List<PageData> findByAssemblyID(PageData findByAssemblyID);

	public void editByUser(PageData pdParend);

	/**
	 * @param pd
	 */
	public void updateMx(PageData pd) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listXMJSSJ(Page page)throws Exception;

	/**
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> listXMJLByIDS(String[] arrayDATA_IDS)throws Exception;

	/**
	 * @param arrayDATA_IDS
	 * @return
	 */
	public List<PageData> listJSFZRByIDS(String[] arrayDATA_IDS)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllPC(PageData pd)throws Exception;

	
}
