package org.yy.service.project.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.DISCREPANCYHOUR_RECORDMapper;
import org.yy.service.project.manager.DISCREPANCYHOUR_RECORDService;

/**
 * 说明： 项目工时统计异常说明接口实现类 作者：YuanYes Q356703572 时间：2021-09-29 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class DISCREPANCYHOUR_RECORDServiceImpl implements DISCREPANCYHOUR_RECORDService {

	@Autowired
	private DISCREPANCYHOUR_RECORDMapper discrepancyhour_recordMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		discrepancyhour_recordMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		discrepancyhour_recordMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		discrepancyhour_recordMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return discrepancyhour_recordMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return discrepancyhour_recordMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return discrepancyhour_recordMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		discrepancyhour_recordMapper.deleteAll(ArrayDATA_IDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yy.service.project.manager.DISCREPANCYHOUR_RECORDService#getAllListGXDB(
	 * org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> getAllListGXDB(PageData pd) throws Exception {
		return discrepancyhour_recordMapper.getAllListGXDB(pd);

	}

}
