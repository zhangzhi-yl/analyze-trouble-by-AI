package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 项目任务接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 * @version
 */
public interface PROJECTTASKService{

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

	/**修改执行状态
	 * @param pd
	 */
	public void editState(PageData pd)throws Exception;

	/**查询任务未审核通过变更记录数量
	 * @param pd
	 * @return
	 */
	public PageData findNum(PageData pd)throws Exception;

	/**定时更新当前计划状态
	 * @param pdMain
	 */
	public void editActual(PageData pdMain)throws Exception;
	/**任务变更审核通过更新最新计划开始时间、最新计划结束时间、当前计划状态
	 * @param pdMain
	 */
	public void editPass(PageData pdMain)throws Exception;

	/**获得部门负责人字符串
	 * @param pd
	 * @return
	 */
	public PageData getBZNAME(PageData pd)throws Exception;

	/**判断是否是部长
	 * @param pd
	 * @return
	 */
	public PageData getBZUSERNAME(PageData pd)throws Exception;

	/**任务超期提醒
	 * @param pd1
	 * @return
	 */
	public List<PageData> getCQList(PageData pd1)throws Exception;

	/**
	 * @param pd
	 */
	public void editBM(PageData pd)throws Exception;

	/**获得甘特图范围
	 * @param pd
	 * @return
	 */
	public PageData getRange(PageData pd)throws Exception;

	/**获得甘特图日期列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getRangeList(PageData pd)throws Exception;

	/**获得甘特图任务明细列表
	 * @param pd
	 * @return
	 */
	public List<PageData> getDataList(PageData pd)throws Exception;

	
}

