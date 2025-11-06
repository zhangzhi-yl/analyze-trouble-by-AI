package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.TakeStock_Task_ExecuteMapper;
import org.yy.service.mm.TakeStock_Task_ExecuteService;

/** 
 * 说明： 盘点任务物料明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-28
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class TakeStock_Task_ExecuteServiceImpl implements TakeStock_Task_ExecuteService{

	@Autowired
	private TakeStock_Task_ExecuteMapper TakeStock_Task_ExecuteMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		TakeStock_Task_ExecuteMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		TakeStock_Task_ExecuteMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		TakeStock_Task_ExecuteMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return TakeStock_Task_ExecuteMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return TakeStock_Task_ExecuteMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return TakeStock_Task_ExecuteMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		TakeStock_Task_ExecuteMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 根据物料ID从盘点任务明细表中获取数据，判断当前扫码的物料是否存在于表中
	 */
	public PageData getMaterial(PageData pd) throws Exception {
		return TakeStock_Task_ExecuteMapper.getMaterial(pd);
	}

	@Override
	public Integer getEntryID(PageData pd) throws Exception {
		return TakeStock_Task_ExecuteMapper.getEntryID(pd);
	}

	@Override
	public void FinishTakeStock(PageData pd) throws Exception {
		TakeStock_Task_ExecuteMapper.FinishTakeStock(pd);
	}

	@Override
	public void CloseHang(PageData pd) throws Exception {
		TakeStock_Task_ExecuteMapper.CloseHang(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		// TODO 自动生成的方法存根
		return TakeStock_Task_ExecuteMapper.AppList(page);
	}
	
}

