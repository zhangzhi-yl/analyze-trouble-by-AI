package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.dprojectManager.DTPROJECTFILEMapper;
import org.yy.service.project.manager.DTPROJECTFILEService;

/** 
 * 说明： 项目文件接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-09-05
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class DTPROJECTFILEServiceImpl implements DTPROJECTFILEService{

	@Autowired
	private DTPROJECTFILEMapper dtprojectfileMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dtprojectfileMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dtprojectfileMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dtprojectfileMapper.edit(pd);
	}
	
	/**逻辑删除
	 * @param pd
	 * @throws Exception
	 */
	public void updateDel(PageData pd)throws Exception{
		dtprojectfileMapper.updateDel(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return dtprojectfileMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return dtprojectfileMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return dtprojectfileMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dtprojectfileMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**项目列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listProAll(PageData pd)throws Exception{
		return dtprojectfileMapper.listProAll(pd);
	}
	
	/**通过id获取项目编号
	 * @param pd
	 * @throws Exception
	 */
//	public PageData findProById(PageData pd)throws Exception{
//		return dtprojectfileMapper.findProById(pd);
//	}
	
	/**设备列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listEquAll(PageData pd)throws Exception{
		return dtprojectfileMapper.listEquAll(pd);
	}
	
	/**阶段列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listStaAll(PageData pd)throws Exception{
		return dtprojectfileMapper.listStaAll(pd);
	}
	
	/**活动名称列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listActAll(PageData pd)throws Exception{
		return dtprojectfileMapper.listActAll(pd);
	}


	/**附件列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> listx(Page page) throws Exception {
		return dtprojectfileMapper.dataxlistPage(page);
	}

	/**新增页获取数据
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getDatax(PageData pd) throws Exception {
		return dtprojectfileMapper.getDatax(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.DTPROJECTFILEService#insert(org.yy.entity.PageData)
	 */
	@Override
	public void insert(PageData pd) throws Exception {
		dtprojectfileMapper.insert(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.DTPROJECTFILEService#findFJ(org.yy.entity.PageData)
	 */
	@Override
	public PageData findFJ(PageData pd) throws Exception {
		return dtprojectfileMapper.findFJ(pd);
	}
}

