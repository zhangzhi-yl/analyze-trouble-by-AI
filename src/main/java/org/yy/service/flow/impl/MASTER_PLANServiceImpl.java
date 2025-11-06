package org.yy.service.flow.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.flow.MASTER_PLANMapper;
import org.yy.service.flow.MASTER_PLANService;

/**
 * 说明： 主计划工单接口实现类 作者：YuanYes Q356703572 时间：2020-12-01 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class MASTER_PLANServiceImpl implements MASTER_PLANService {

	@Autowired
	private MASTER_PLANMapper MASTER_PLANMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		MASTER_PLANMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		MASTER_PLANMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		MASTER_PLANMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return MASTER_PLANMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return MASTER_PLANMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return MASTER_PLANMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		MASTER_PLANMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> findByMasterPlan_ID(PageData pd) {
		return MASTER_PLANMapper.findByMasterPlan_ID(pd);
	}

	@Override
	public void deleteByMasterPlanIdAndNodeId(PageData pd) {
		MASTER_PLANMapper.deleteByMasterPlanIdAndNodeId(pd);
	}

	@Override
	public void rename(PageData pd) {
		MASTER_PLANMapper.rename(pd);
	}
}
