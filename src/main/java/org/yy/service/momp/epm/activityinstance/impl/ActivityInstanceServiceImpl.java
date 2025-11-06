package org.yy.service.momp.epm.activityinstance.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.ActivityInstanceMapper;
import org.yy.service.momp.epm.activityinstance.ActivityInstanceService;



/** 
 * 说明： 活动实例
 * 创建时间：2019-01-17
 * @version
 */
@Service
@Transactional //开启事物
public class ActivityInstanceServiceImpl implements ActivityInstanceService{
	@Autowired
	private ActivityInstanceMapper activityinstanceMapper;

	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		activityinstanceMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		activityinstanceMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		activityinstanceMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return activityinstanceMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return activityinstanceMapper.listAll(pd);
	}
	
   /**获取活动实例列表，用于拓扑图使用
     * @param pd
     * @throws Exception
     */
    public List<PageData> getTptDatalist(PageData pd) throws Exception
    {
        return activityinstanceMapper.getTptDatalist(pd);
    }
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return activityinstanceMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		activityinstanceMapper.deleteAll(ArrayDATA_IDS);
	}
	
	
	/**活动修改执行的时候，重新计算阶段的工单开始时间和工单结束时间，并且更新阶段表里对应字段
	 * @param pd
	 * @throws Exception
	 */
	public void updatePhasePlanStartEndTime(PageData pd)throws Exception{
		activityinstanceMapper.updatePhasePlanStartEndTime(pd);
	}
	/**活动修改执行的时候，重新计算工单的工单开始时间和工单结束时间，并且更新工单表里对应字段
	 * @param pd
	 * @throws Exception
	 */
	public void updatePlanPlanStartEndTime(PageData pd)throws Exception{
		activityinstanceMapper.updatePlanPlanStartEndTime(pd);
	}
	
	/**自动更新活动进度
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityCOMRATE(PageData pd)throws Exception{
		activityinstanceMapper.updateActivityCOMRATE(pd);
		
	}
	
	/**自动更新活动实际开始时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityActualStart(PageData pd)throws Exception{
		activityinstanceMapper.updateActivityActualStart(pd);
		
	}
	
	/**自动更新活动实际结束时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityActualEnd(PageData pd)throws Exception{
		
		activityinstanceMapper.updateActivityActualEnd(pd);
	}
	
	/**按照活动ID更新运行状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityStartStatus(PageData pd)throws Exception{
		activityinstanceMapper.updateActivityStartStatus(pd);
	}
	
	
	/**按照活动ID更新结束状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateActivityEndStatus(PageData pd)throws Exception{
		activityinstanceMapper.updateActivityEndStatus(pd);
		
	}
	
	
	
	/**通过项目UUID获得生产制造活动的开始结束时间
	 * @param pd
	 * @throws Exception
	 */
	public PageData findPlanStartEndTimeByProID(PageData pd)throws Exception{
		
		return activityinstanceMapper.findPlanStartEndTimeByProID(pd);
		
	}
	
	
	/**按照工单实例ID列出活动实例列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listActivityByPlanID(PageData pd)throws Exception{
		
		return activityinstanceMapper.listActivityByPlanID(pd);
		
	}

	@Override
	public PageData getArecordnum(PageData pd) throws Exception {
		return activityinstanceMapper.getArecordnum(pd);
	}

	@Override
	public PageData getPrecordnum(PageData pd) throws Exception {
		return activityinstanceMapper.getPrecordnum(pd);
	}
	
	/**修改勾选信息
	 * @param pd
	 * @throws Exception
	 */
	public void editFcheck(PageData pd)throws Exception{
		activityinstanceMapper.editFcheck(pd);
	}
	
}

