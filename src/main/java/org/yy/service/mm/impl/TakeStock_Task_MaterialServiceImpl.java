package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.TakeStock_Task_MaterialMapper;
import org.yy.service.mm.TakeStock_Task_MaterialService;

/** 
 * 说明： 盘点任务物料明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-30
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class TakeStock_Task_MaterialServiceImpl implements TakeStock_Task_MaterialService{

	@Autowired
	private TakeStock_Task_MaterialMapper TakeStock_Task_MaterialMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		TakeStock_Task_MaterialMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		TakeStock_Task_MaterialMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		TakeStock_Task_MaterialMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return TakeStock_Task_MaterialMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return TakeStock_Task_MaterialMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return TakeStock_Task_MaterialMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		TakeStock_Task_MaterialMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public void CloseHang(PageData pd) throws Exception {
		TakeStock_Task_MaterialMapper.CloseHang(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		return TakeStock_Task_MaterialMapper.AppList(page);
	}
	
	/**获取一级物料明细数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData getTaskMaterialNum(PageData pd)throws Exception{
		return TakeStock_Task_MaterialMapper.getTaskMaterialNum(pd);
	}
	
	/**获取一级物料明细详情
	 * @param pd
	 * @throws Exception
	 */
	public PageData getTaskMaterial(PageData pd)throws Exception{
		return TakeStock_Task_MaterialMapper.getTaskMaterial(pd);
	}
	
	/**通过任务ID获取物料明细列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getTaskMaterialAll(PageData pd)throws Exception{
		return TakeStock_Task_MaterialMapper.getTaskMaterialAll(pd);
	}
}

