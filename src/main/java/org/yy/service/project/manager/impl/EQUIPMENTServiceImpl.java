package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.dprojectManager.EQUIPMENTMapper;
import org.yy.service.project.manager.EQUIPMENTService;

/** 
 * 说明： 项目设备接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-09-01
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class EQUIPMENTServiceImpl implements EQUIPMENTService{

	@Autowired
	private EQUIPMENTMapper equipmentMapper;
	
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
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return equipmentMapper.listAll(pd);
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

	/**修改执行状态
	 * @param pd
	 */
	@Override
	public void editState(PageData pd) throws Exception {
		equipmentMapper.editState(pd);
	}

	/**反写删除状态
	 * @param pd
	 */
	@Override
	public void upVisible(PageData pd) throws Exception {
		equipmentMapper.upVisible(pd);
	}

	@Override
	public PageData getPROID(PageData pd) throws Exception {
		return equipmentMapper.getPROID(pd);
	}
	
}

