package org.yy.service.app;

import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.PageData;

public interface AppService {
	public List<PageData> appMaintenanceList(AppPage page);
	public List<PageData> appMaintenanceMxList(AppPage page);
	public List<PageData> appMaintainConsumeList(AppPage page);
	public List<PageData> appMaintainManHourList(AppPage page);
}
