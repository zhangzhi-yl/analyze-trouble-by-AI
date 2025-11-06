package org.yy.service.zm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.EquipmentMapper;
import org.yy.service.zm.EquipmentsService;

/** 
 * 说明： 照明接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-08
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class EquipmentServiceImpl implements EquipmentsService{

	@Autowired
	private EquipmentMapper equipmentMapper;

	@Override
	public List<PageData> getCount(PageData pd) throws Exception {
		return equipmentMapper.getCount(pd);
	}

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		equipmentMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		equipmentMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		equipmentMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return equipmentMapper.datalistPage(page);
	}

	/**列表(根据回路ID查询)
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> getByLoop(Page page) throws Exception {
		return equipmentMapper.getByLoopdatalistPage(page);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return equipmentMapper.listAll(pd);
	}
	/**查询12月
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getMonthList(PageData pd)throws Exception{
		return equipmentMapper.getMonthList(pd);
	}
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return equipmentMapper.findById(pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		equipmentMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> getFailCount(PageData pd) throws Exception {
		return equipmentMapper.getFailCount(pd);
	}

}

