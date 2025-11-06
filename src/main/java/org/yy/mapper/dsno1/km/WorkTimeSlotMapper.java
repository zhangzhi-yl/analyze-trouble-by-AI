package org.yy.mapper.dsno1.km;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 工作时间段Mapper 作者：YuanYes QQ356703572 时间：2020-11-05 官网：356703572@qq.com
 * 
 * @version
 */
public interface WorkTimeSlotMapper {

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
	 * 列表WORKINGTIMEID
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
	 * 根据工作时间主表id 更新附属时间段表信息
	 * 
	 * @param dataID
	 * @param valueOf
	 */
	void updateWorkTimeSlotByTimeID(String WORKTIMESLOT_ID, String WORKINGTIMEID);

	/**
	 * 根据工作时间主表id 获取附属时间段信息列表
	 * 
	 * @param dataID
	 * @param valueOf
	 */
	List<PageData> getTimeSlotListByWorkingTimeID(String WORKINGTIME_ID);

	/**
	 * 根据id列表获取数据
	 * 
	 * @param arrayDATA_IDS
	 * @return
	 */
	List<PageData> listByIds(String[] arrayDATA_IDS);

}
