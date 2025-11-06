package org.yy.mapper.dsno1.momp;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

public interface ActivityInstanceMapper {
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
	
    /**获取活动实例列表，用于拓扑图使用
     * @param pd
     * @throws Exception
     */
    List<PageData> getTptDatalist(PageData pd);
    
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
	
	
	/**活动修改执行的时候，重新计算阶段的工单开始时间和工单结束时间，并且更新阶段表里对应字段
	 * @param pd
	 * @throws Exception
	 */
	void updatePhasePlanStartEndTime(PageData pd);
	
	
	/**活动修改执行的时候，重新计算工单的工单开始时间和工单结束时间，并且更新工单表里对应字段
	 * @param pd
	 * @throws Exception
	 */
	void updatePlanPlanStartEndTime(PageData pd);
	
	/**自动更新活动进度
	 * @param pd
	 * @throws Exception
	 */
	void updateActivityCOMRATE(PageData pd);
	
	/**自动更新活动实际开始时间
	 * @param pd
	 * @throws Exception
	 */
	void updateActivityActualStart(PageData pd);
	
	/**自动更新活动实际结束时间
	 * @param pd
	 * @throws Exception
	 */
	void updateActivityActualEnd(PageData pd);
	
	/**按照活动ID更新运行状态
	 * @param pd
	 * @throws Exception
	 */
	void updateActivityStartStatus(PageData pd);
	
	
	/**按照活动ID更新结束状态
	 * @param pd
	 * @throws Exception
	 */
	void updateActivityEndStatus(PageData pd);
	
	
	
	/**按照工单实例ID列出活动实例列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listActivityByPlanID(PageData pd);
	
	/**通过项目UUID获得生产制造活动的开始结束时间
	 * @param pd
	 * @throws Exception
	 */
	PageData findPlanStartEndTimeByProID(PageData pd);

	PageData getArecordnum(PageData pd);

	PageData getPrecordnum(PageData pd);
	
	/**修改勾选信息
	 * @param pd
	 * @throws Exception
	 */
	void editFcheck(PageData pd);
}
