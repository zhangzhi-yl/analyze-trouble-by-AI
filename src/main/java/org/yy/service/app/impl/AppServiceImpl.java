package org.yy.service.app.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.app.AppMapper;
import org.yy.service.app.AppService;

@Service(value="appServiceImpl")
@Transactional //开启事物
public class AppServiceImpl implements AppService {
	@Autowired
	AppMapper appMapper;
	
	@Override
	public List<PageData> appMaintenanceList(AppPage page) {
		
		return appMapper.appMaintenanceList(page);
	}
	public List<PageData> appMaintenanceMxList(AppPage page){
		return appMapper.appMaintenanceMxList(page);
	}
	public List<PageData> appMaintainConsumeList(AppPage page){
		return appMapper.appMaintainConsumeList(page);
	}
	public List<PageData> appMaintainManHourList(AppPage page){
		return appMapper.appMaintainManHourList(page);
	}
}
