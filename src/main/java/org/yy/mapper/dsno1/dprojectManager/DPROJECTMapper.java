package org.yy.mapper.dsno1.dprojectManager;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 项目Mapper 作者：YuanYes QQ356703572 时间：2020-09-01 官网：356703572@qq.com
 * 
 * @version
 */
public interface DPROJECTMapper {

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * 自动生成项目号
	 * 
	 * @param pd
	 * @return
	 */
	PageData generateNo(PageData pd);

	/**
	 * 项目编号验重
	 * 
	 * @param pd
	 * @return
	 */
	PageData testNo(PageData pd);

	/**
	 * 反写设备删除状态
	 * 
	 * @param pd
	 */
	void upVisible(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> getProjectList(PageData pd);

	/**
	 * 结项
	 * 
	 * @param pd
	 */
	void over(PageData pd);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listWLDBlistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listWLDBMXlistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listRGDBlistPage(Page page);

	/**
	 * @param page
	 * @return
	 */
	List<PageData> listRGDBMXlistPage(Page page);

	/**
	 * @param pdX
	 */
	void editTimeXM(PageData pdX);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listDDP(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listZSDDS(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listZSDDJE(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listCWSS(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listSJXL(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	PageData listJSYDMAIN(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listJSYDMX(PageData pd);

}
