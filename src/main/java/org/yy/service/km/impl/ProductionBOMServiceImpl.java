package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.ProductionBOMMapper;
import org.yy.service.km.ProductionBOMService;

/** 
 * 说明： 生产BOM接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ProductionBOMServiceImpl implements ProductionBOMService{

	@Autowired
	private ProductionBOMMapper ProductionBOMMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ProductionBOMMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ProductionBOMMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ProductionBOMMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ProductionBOMMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ProductionBOMMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ProductionBOMMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ProductionBOMMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获取BOM列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getBOMList(PageData pd) throws Exception {
		return ProductionBOMMapper.getBOMList(pd);
	}

	/**单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return ProductionBOMMapper.getRepeatNum(pd);
	}

	/**计划工单获取投入产出列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> getInOut(PageData pd) throws Exception {
		return ProductionBOMMapper.getInOut(pd);
	}

	/**发布/停用
	 * @param pd
	 */
	@Override
	public void release(PageData pd) throws Exception {
		ProductionBOMMapper.release(pd);
	}
	
}

