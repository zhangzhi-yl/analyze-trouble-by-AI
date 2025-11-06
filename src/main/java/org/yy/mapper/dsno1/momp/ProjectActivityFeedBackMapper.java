package org.yy.mapper.dsno1.momp;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

public interface ProjectActivityFeedBackMapper {
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	/**通过项目名称（工单名称）获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findPlanByFPINAME(PageData pd);
	/**通过id查询活动映射对象
	 * @param pd
	 * @throws Exception
	 */
	PageData findActivityById(PageData pd);
	/**feedback反馈列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> feedDatalistPage(Page page);
	/**新增文档
	 * @param page
	 * @throws Exception
	 */
	void save(PageData pd);
	/**修改文档
	 * @param page
	 * @throws Exception
	 */
	void edit(PageData pd);
	/**查询文档信息
	 * @param page
	 * @throws Exception
	 */
	PageData findFileById(PageData pd);
	/**删除文档信息
	 * @param page
	 * @throws Exception
	 */
	void delete(PageData pd);
	/**批量删除文档信息
	 * @param page
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	/**修改活动映射
	 * @param page
	 * @throws Exception
	 */
	void editActivity(PageData pd);
	/**通过activityid查询文档数量
	 * @param page
	 * @throws Exception
	 */
	PageData findFileByAcid(PageData pd);
	/**
	 * 查询最小阶段
	 * @param pd
	 * @return
	 */
	PageData findMinPhase(PageData pd);
	/**
	 * 查询最大阶段
	 * @param pd
	 * @return
	 */
	PageData findMaxPhase(PageData pd);
	/**
	 * 查询实际开始时间
	 * @param pd
	 * @return
	 */
	PageData findFACTUALSTART(PageData pd);
	/**
	 * 查询实际结束时间
	 * @param pd
	 * @return
	 */
	PageData findFACTUALEND(PageData pd);
	void editTime(PageData pd);
}
