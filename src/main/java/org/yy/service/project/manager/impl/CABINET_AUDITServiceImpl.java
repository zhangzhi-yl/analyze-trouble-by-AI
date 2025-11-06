package org.yy.service.project.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.CABINET_AUDITMapper;
import org.yy.service.project.manager.CABINET_AUDITService;

/**
 * 说明： 柜体审核接口实现类 作者：YuanYes Q356703572 时间：2021-07-07 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class CABINET_AUDITServiceImpl implements CABINET_AUDITService {

	@Autowired
	private CABINET_AUDITMapper cabinet_auditMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		cabinet_auditMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		cabinet_auditMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		cabinet_auditMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return cabinet_auditMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return cabinet_auditMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return cabinet_auditMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		cabinet_auditMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 柜体审核记录列表
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listSH(Page page) throws Exception {
		return cabinet_auditMapper.listSHlistPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yy.service.project.manager.CABINET_AUDITService#listXM(org.yy.entity.
	 * Page)
	 */
	@Override
	public List<PageData> listXM(Page page) throws Exception {
		return cabinet_auditMapper.listXMlistPage(page);
	}

}
