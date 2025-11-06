package org.yy.service.project.manager;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 项目接口 作者：YuanYes QQ356703572 时间：2020-09-01 官网：356703572@qq.com
 * 
 * @version
 */
public interface DPROJECTService {

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

	/**
	 * 自动生成项目号
	 * 
	 * @param pd
	 * @return
	 */
	public PageData generateNo(PageData pd) throws Exception;

	/**
	 * 项目编号验重
	 * 
	 * @param pd
	 * @return
	 */
	public PageData testNo(PageData pd) throws Exception;

	/**
	 * 反写设备删除状态
	 * 
	 * @param pd
	 */
	public void upVisible(PageData pd) throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> getProjectList(PageData pd) throws Exception;

	/**
	 * 结项
	 * 
	 * @param pd
	 */
	public void over(PageData pd) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listWLDB(Page page) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listWLDBMX(Page page) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listRGDB(Page page) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> listRGDBMX(Page page) throws Exception;

	/**
	 * @param pdX
	 */
	public void editTimeXM(PageData pdX) throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listDDP(PageData pd) throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listZSDDS(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listZSDDJE(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listCWSS(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listSJXL(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	public PageData listJSYDMAIN(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listJSYDMX(PageData pd);

}
