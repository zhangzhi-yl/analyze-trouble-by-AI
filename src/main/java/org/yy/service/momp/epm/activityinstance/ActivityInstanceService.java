package org.yy.service.momp.epm.activityinstance;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;


/** 
 * 说明： 活动实例接口
 * 创建人：YY Q356703572
 * 创建时间：2019-01-17
 * @version
 */
public interface ActivityInstanceService{

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
	
    /**获取活动实例列表，用于拓扑图使用
     * @param pd
     * @throws Exception
     */
    public List<PageData> getTptDatalist(PageData pd)throws Exception;
    
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
	
	
	/**活动修改执行的时候，重新计算阶段的工单开始时间和工单结束时间，并且更新阶段表里对应字段
	 * @param pd
	 * @throws Exception
	 */
	public void updatePhasePlanStartEndTime(PageData pd)throws Exception;
	
	
	/**活动修改执行的时候，重新计算工单的工单开始时间和工单结束时间，并且更新工单表里对应字段
	 * @param pd
	 * @throws Exception
	 */
	public void updatePlanPlanStartEndTime(PageData pd)throws Exception;
	
	/**自动更新活动进度
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityCOMRATE(PageData pd)throws Exception;
	
	/**自动更新活动实际开始时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityActualStart(PageData pd)throws Exception;
	
	/**自动更新活动实际结束时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityActualEnd(PageData pd)throws Exception;
	
	/**按照活动ID更新运行状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityStartStatus(PageData pd)throws Exception;
	
	
	/**按照活动ID更新结束状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityEndStatus(PageData pd)throws Exception;
	
	
	
	/**按照工单实例ID列出活动实例列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listActivityByPlanID(PageData pd)throws Exception;
	
	/**通过项目UUID获得生产制造活动的开始结束时间
	 * @param pd
	 * @throws Exception
	 */
	public PageData findPlanStartEndTimeByProID(PageData pd)throws Exception;

	public PageData getArecordnum(PageData pd)throws Exception;

	public PageData getPrecordnum(PageData pd)throws Exception;
	
	/**修改勾选信息
	 * @param pd
	 * @throws Exception
	 */
	public void editFcheck(PageData pd)throws Exception;
}

