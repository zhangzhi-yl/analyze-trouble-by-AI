package org.yy.mapper.dsno1.dprojectManager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 项目任务Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 * @version
 */
public interface PROJECTTASKMapper{

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

	/**修改执行状态
	 * @param pd
	 */
	void editState(PageData pd);

	/**查询任务未审核通过变更记录数量
	 * @param pd
	 * @return
	 */
	PageData findNum(PageData pd);

	/**定时更新更新当前计划状态
	 * @param pdMain
	 */
	void editActual(PageData pdMain);

	/**任务变更审核通过更新最新计划开始时间、最新计划结束时间、当前计划状态
	 * @param pdMain
	 */
	void editPass(PageData pdMain);

	/**获得部门负责人字符串
	 * @param pd
	 * @return
	 */
	PageData getBZNAME(PageData pd);

	/**判断是否是部长
	 * @param pd
	 * @return
	 */
	PageData getBZUSERNAME(PageData pd);

	/**任务超期提醒
	 * @param pd1
	 * @return
	 */
	List<PageData> getCQList(PageData pd1);

	/**
	 * @param pd
	 */
	void editBM(PageData pd);

	/**获得甘特图范围
	 * @param pd
	 * @return
	 */
	PageData getRange(PageData pd);

	/**获得甘特图日期列表
	 * @param pd
	 * @return
	 */
	List<PageData> getRangeList(PageData pd);

	/**获得甘特图任务明细列表
	 * @param pd
	 * @return
	 */
	List<PageData> getDataList(PageData pd);

	
}

