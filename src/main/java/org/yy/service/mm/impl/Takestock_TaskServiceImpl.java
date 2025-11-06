package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.Takestock_TaskMapper;
import org.yy.service.mm.Takestock_TaskService;

/** 
 * 说明： 盘点任务接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-11-26
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class Takestock_TaskServiceImpl implements Takestock_TaskService{

	@Autowired
	private Takestock_TaskMapper takestock_taskMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		takestock_taskMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		takestock_taskMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		takestock_taskMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return takestock_taskMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return takestock_taskMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return takestock_taskMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		takestock_taskMapper.deleteAll(ArrayDATA_IDS);
	}
	/**
	 * 下发
	 */
	@Override
	public void goLssue(PageData pd) throws Exception {
		takestock_taskMapper.goLssue(pd);
	}
	/**
	 * 获取仓库下物料列表
	 */
	@Override
	public List<PageData> getAllMaterial(PageData pd) throws Exception {
		return takestock_taskMapper.getAllMaterial(pd);
	}
	/**
	 * 获取订单下物料列表
	 */
	@Override
	public List<PageData> getAllDDMaterial(PageData pd) throws Exception {
		return takestock_taskMapper.getAllDDMaterial(pd);
	}
	/**
	 * 获取仓库列表
	 */
	@Override
	public List<PageData> getWarehouse(PageData pd) throws Exception {
		return takestock_taskMapper.getWarehouse(pd);
	}

	/**
	 * 盘点任务提交审核
	 */
	public void goCheck(PageData pd) throws Exception {
		takestock_taskMapper.goCheck(pd);
	}

	@Override
	public void updateDifferenceOrder_NUM(PageData pd) throws Exception {
		takestock_taskMapper.updateDifferenceOrder_NUM(pd);
	}

	@Override
	public void CheckPass(PageData pd) throws Exception {
		takestock_taskMapper.CheckPass(pd);
	}

	@Override
	public PageData getCount(PageData pd) throws Exception {
		return takestock_taskMapper.getCount(pd);
	}

	@Override
	public PageData findByMaterialId(PageData pd) throws Exception {
		return takestock_taskMapper.findByMaterialId(pd);
	}

	@Override
	public List<PageData> listHIS(Page page) throws Exception {
		return takestock_taskMapper.datalistPageHIS(page);
	}

	@Override
	public List<PageData> DifferenceOrder_Count(PageData pd) throws Exception {
		return takestock_taskMapper.DifferenceOrder_Count(pd);
	}

	@Override
	public void CreatOrder(PageData pd) throws Exception {
		takestock_taskMapper.CreatOrder(pd);
	}

	@Override
	public List<PageData> listResult(PageData pd) throws Exception {
		return takestock_taskMapper.listResult(pd);
	}

	@Override
	public PageData getTakeStocHIS(PageData pd) throws Exception {
		return takestock_taskMapper.getTakeStocHIS(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		return takestock_taskMapper.AppList(page);
	}
}

