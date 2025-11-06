package org.yy.service.project.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.dprojectManager.DPROJECTMapper;
import org.yy.service.project.manager.DPROJECTService;

/**
 * 说明： 项目接口实现类 作者：YuanYes Q356703572 时间：2020-09-01 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class DPROJECTServiceImpl implements DPROJECTService {

	@Autowired
	private DPROJECTMapper projectMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		projectMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		projectMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		projectMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return projectMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return projectMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return projectMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		projectMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 自动生成项目号
	 * 
	 * @param pd
	 * @return
	 */
	@Override
	public PageData generateNo(PageData pd) throws Exception {
		return projectMapper.generateNo(pd);
	}

	/**
	 * 项目编号验重
	 * 
	 * @param pd
	 * @return
	 */
	@Override
	public PageData testNo(PageData pd) throws Exception {
		return projectMapper.testNo(pd);
	}

	/**
	 * 反写设备删除状态
	 * 
	 * @param pd
	 */
	@Override
	public void upVisible(PageData pd) throws Exception {
		projectMapper.upVisible(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yy.service.project.manager.DPROJECTService#getProjectList(org.yy.entity.
	 * PageData)
	 */
	@Override
	public List<PageData> getProjectList(PageData pd) throws Exception {
		return projectMapper.getProjectList(pd);
	}

	/**
	 * 结项
	 * 
	 * @param pd
	 */
	@Override
	public void over(PageData pd) throws Exception {
		projectMapper.over(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yy.service.project.manager.DPROJECTService#listWLDB(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listWLDB(Page page) throws Exception {
		return projectMapper.listWLDBlistPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yy.service.project.manager.DPROJECTService#listWLDBMX(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listWLDBMX(Page page) throws Exception {
		return projectMapper.listWLDBMXlistPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yy.service.project.manager.DPROJECTService#listRGDB(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listRGDB(Page page) throws Exception {
		return projectMapper.listRGDBlistPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yy.service.project.manager.DPROJECTService#listRGDBMX(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listRGDBMX(Page page) throws Exception {
		return projectMapper.listRGDBMXlistPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.project.manager.DPROJECTService#editTimeXM(org.yy.entity.
	 * PageData)
	 */
	@Override
	public void editTimeXM(PageData pdX) throws Exception {
		projectMapper.editTimeXM(pdX);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.project.manager.DPROJECTService#listDDP(org.yy.entity.
	 * PageData)
	 */
	@Override
	public List<PageData> listDDP(PageData pd) throws Exception {
		return projectMapper.listDDP(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.project.manager.DPROJECTService#listZSDDS(org.yy.entity.
	 * PageData)
	 */
	@Override
	public List<PageData> listZSDDS(PageData pd) {
		return projectMapper.listZSDDS(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.project.manager.DPROJECTService#listZSDDJE(org.yy.entity.
	 * PageData)
	 */
	@Override
	public List<PageData> listZSDDJE(PageData pd) {
		return projectMapper.listZSDDJE(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.project.manager.DPROJECTService#listCWSS(org.yy.entity.
	 * PageData)
	 */
	@Override
	public List<PageData> listCWSS(PageData pd) {
		return projectMapper.listCWSS(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.project.manager.DPROJECTService#listSJXL(org.yy.entity.
	 * PageData)
	 */
	@Override
	public List<PageData> listSJXL(PageData pd) {
		return projectMapper.listSJXL(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yy.service.project.manager.DPROJECTService#listJSYDMAIN(org.yy.entity.
	 * PageData)
	 */
	@Override
	public PageData listJSYDMAIN(PageData pd) {
		return projectMapper.listJSYDMAIN(pd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yy.service.project.manager.DPROJECTService#listJSYDMX(org.yy.entity.
	 * PageData)
	 */
	@Override
	public List<PageData> listJSYDMX(PageData pd) {
		return projectMapper.listJSYDMX(pd);
	}

}
