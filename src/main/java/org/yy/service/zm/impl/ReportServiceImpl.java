package org.yy.service.zm.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.GroupMapper;
import org.yy.mapper.dsno1.zm.ReportMapper;
import org.yy.service.zm.GroupService;
import org.yy.service.zm.ReportService;

import java.util.List;

/** 
 * 说明：报表
 * 作者：YuanYes Q356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportMapper reportMapper;
	

	/**日用能列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> dayElectricReport(PageData pd)throws Exception{
		return reportMapper.dayElectricReport(pd);
	}

	/**月用能列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> monthElectricReport(PageData pd)throws Exception{
		return reportMapper.monthElectricReport(pd);
	}

	/**日用能列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> dayElectricUseReport(PageData pd)throws Exception{
		return reportMapper.dayElectricUseReport(pd);
	}

	/**月用能列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> monthElectricUseReport(PageData pd)throws Exception{
		return reportMapper.monthElectricUseReport(pd);
	}
}

