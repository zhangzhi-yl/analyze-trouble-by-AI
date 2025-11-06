package org.yy.service.zm.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.AppVersionMapper;
import org.yy.service.zm.AppVersionService;

import java.util.List;

/** 
 * 说明： App版本接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class AppVersionServiceImpl implements AppVersionService {

	@Autowired
	private AppVersionMapper appVersionMapper;


	@Override
	public void edit(PageData pd) throws Exception {
		appVersionMapper.edit(pd);
	}

	@Override
	public List<PageData> list(Page page) throws Exception {
		return appVersionMapper.datalistPage(page);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return appVersionMapper.findById(pd);
	}

}

