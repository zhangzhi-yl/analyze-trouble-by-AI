package org.yy.mapper.dsno1.app;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.PageData;

/**
 * app公共mapper
 * @author YULONG
 *
 */
public interface AppMapper {
	/**
	 * 设备保养列表
	 * @param page
	 * @return
	 */
	List<PageData> appMaintenanceList(AppPage page);
	List<PageData> appMaintenanceMxList(AppPage page);
	List<PageData> appMaintainConsumeList(AppPage page);
	List<PageData> appMaintainManHourList(AppPage page);
}
