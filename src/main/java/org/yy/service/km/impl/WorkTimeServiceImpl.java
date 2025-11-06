package org.yy.service.km.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.WorkTimeMapper;
import org.yy.mapper.dsno1.km.WorkTimeSlotMapper;
import org.yy.service.km.WorkTimeService;
import org.yy.util.Tools;

/**
 * 说明： 工作时间接口实现类 作者：YuanYes Q356703572 时间：2020-11-05 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class WorkTimeServiceImpl implements WorkTimeService {

	@Autowired
	private WorkTimeMapper worktimeMapper;
	@Autowired
	private WorkTimeSlotMapper workTimeSlotMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd) throws Exception {
		// 关联时间段数据
		String dataIdsStr = pd.getString("SLOT_IDS");
		List<String> dataIDList = Arrays.asList(dataIdsStr.split(","));
		// 默认只能加一个 不能加多个时间段
		if(dataIDList.size()>1){
			throw new RuntimeException("只允许添加一个时间段");
		}
		
		for (String dataID : dataIDList) {
			workTimeSlotMapper.updateWorkTimeSlotByTimeID(dataID, String.valueOf(pd.get("WORKTIME_ID")));
		}
		List<PageData> timeSlotListByWorkingTimeID = workTimeSlotMapper
				.getTimeSlotListByWorkingTimeID(pd.getString("WORKTIME_ID"));
		
		Integer min = 0;
		Integer hour = 0;
		for (PageData slot : timeSlotListByWorkingTimeID) {
			String Fduration = slot.getString("FDURATION");
			if (Tools.isEmpty(Fduration)) {
				continue;
			}
			List<String> hlist = Arrays.asList(Fduration.split("时"));
			hour += Integer.valueOf(hlist.get(0));
			List<String> mlist = Arrays.asList(hlist.get(1).split("分"));
			min += Integer.valueOf(mlist.get(0));
		}
		if (min > 60) {
			hour += 1;
			min -= 60;
		}
		pd.put("TOTALDURATION", hour + "小时" + min + "分钟");
		worktimeMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delete(PageData pd) throws Exception {
		worktimeMapper.delete(pd);
		// 删除明细表信息
		String WORKINGTIME_ID = String.valueOf(pd.get("WORKTIME_ID"));
		List<PageData> timeSlotList = workTimeSlotMapper.getTimeSlotListByWorkingTimeID(WORKINGTIME_ID);
		timeSlotList.forEach(slot -> {
			String WorkTimeSlot_ID = slot.getString("WORKTIMESLOT_ID");
			PageData pdData = new PageData();
			pdData.put("WorkTimeSlot_ID", WorkTimeSlot_ID);
			workTimeSlotMapper.delete(pdData);
		});
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void edit(PageData pd) throws Exception {
		String dataIdsStr = pd.getString("SLOT_IDS");
		List<String> dataIDList = Arrays.asList(dataIdsStr.split(","));
		// 关联时间段数据
		// 默认只能加一个 不能加多个时间段
		if(dataIDList.size()>1){
			throw new RuntimeException("只允许添加一个时间段");
		}
		for (String dataID : dataIDList) {
			workTimeSlotMapper.updateWorkTimeSlotByTimeID(dataID, String.valueOf(pd.get("WORKTIME_ID")));
		}
		List<PageData> timeSlotListByWorkingTimeID = workTimeSlotMapper
				.getTimeSlotListByWorkingTimeID(pd.getString("WORKTIME_ID"));

		Integer min = 0;
		Integer hour = 0;
		for (PageData slot : timeSlotListByWorkingTimeID) {
			String Fduration = slot.getString("FDURATION");
			if (Tools.isEmpty(Fduration)) {
				continue;
			}
			List<String> hlist = Arrays.asList(Fduration.split("时"));
			hour += Integer.valueOf(hlist.get(0));
			List<String> mlist = Arrays.asList(hlist.get(1).split("分"));
			min += Integer.valueOf(mlist.get(0));
		}
		if (min > 60) {
			hour += 1;
			min -= 60;
		}
		pd.put("TOTALDURATION", hour + "小时" + min + "分钟");
		worktimeMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page) throws Exception {
		return worktimeMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAll(PageData pd) throws Exception {
		return worktimeMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findById(PageData pd) throws Exception {
		return worktimeMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		worktimeMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 修改状态
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void changeStatus(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		PageData findById = worktimeMapper.findById(pd);
		if ("生效".equals(findById.get("FSTATUS"))) {
			findById.put("FSTATUS", "失效");
		} else {
			findById.put("FSTATUS", "生效");
		}
		worktimeMapper.edit(findById);

	}

	/**
	 * 根据主表id获取时间段列表
	 * 
	 * @param workTimeId
	 * @return
	 */
	@Override
	public List<PageData> getTimeSlotListByWorkingTimeID(String WORKTIME_ID) {
		return workTimeSlotMapper.getTimeSlotListByWorkingTimeID(WORKTIME_ID);
	}

}
