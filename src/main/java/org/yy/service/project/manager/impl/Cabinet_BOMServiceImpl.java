package org.yy.service.project.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.Cabinet_BOMMapper;
import org.yy.service.project.manager.Cabinet_BOMService;

/**
 * 说明： 装配柜体BOM表接口实现类 作者：YuanYes Q356703572 时间：2021-05-07 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class Cabinet_BOMServiceImpl implements Cabinet_BOMService {

	@Autowired
	private Cabinet_BOMMapper Cabinet_BOMMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		Cabinet_BOMMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		Cabinet_BOMMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		Cabinet_BOMMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return Cabinet_BOMMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return Cabinet_BOMMapper.listAll(pd);
	}
	/**列表(合并同类物料)
	 * 
	 * v1 陈春光 20210528 由于给计划工单中 物料投入产出列表展示用
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllGroupByMat(PageData pd) throws Exception {
		return Cabinet_BOMMapper.listAllGroupByMat(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return Cabinet_BOMMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		Cabinet_BOMMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> listByCabinetAssemblyDetailIDS(String[] ArrayDATA_IDS) {
		return Cabinet_BOMMapper.listByCabinetAssemblyDetailIDS(ArrayDATA_IDS);
	}

	/** 同柜体物料验重
	 */
	@Override
	public PageData findNUM(PageData pd) throws Exception {
		return Cabinet_BOMMapper.findNUM(pd);
	}

	/**v1 管悦 2021-07-08 按项目查看物料汇总
	 * @param arrayDATA_IDS
	 * @return
	 */
	@Override
	public List<PageData> listByCabinetAssemblyDetailIDSXM(String[] arrayDATA_IDS) throws Exception {
		return Cabinet_BOMMapper.listByCabinetAssemblyDetailIDSXM(arrayDATA_IDS);
	}

	/**柜体设计-获取已选择bom列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllByIds(PageData pd) throws Exception {
		return Cabinet_BOMMapper.listAllByIds(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.Cabinet_BOMService#findOrder(org.yy.entity.PageData)
	 */
	@Override
	public PageData findOrder(PageData pd) throws Exception {
		return Cabinet_BOMMapper.findOrder(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.Cabinet_BOMService#editCGNUM(org.yy.entity.PageData)
	 */
	@Override
	public void editCGNUM(String[] arrayDATA_IDS) throws Exception {
		Cabinet_BOMMapper.editCGNUM(arrayDATA_IDS);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.Cabinet_BOMService#listByCabinetAssemblyDetailIDSCH(java.lang.String[])
	 */
	@Override
	public List<PageData> listByCabinetAssemblyDetailIDSCH(String[] arrayDATA_IDS) throws Exception {
		return Cabinet_BOMMapper.listByCabinetAssemblyDetailIDSCH(arrayDATA_IDS);
	}



}
