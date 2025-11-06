package org.yy.service.ny.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.NYLoopMapper;
import org.yy.service.ny.NYLoopService;

import java.util.List;

/** 
 * 说明： 回路/支路管理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-15
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class NYLoopServiceImpl implements NYLoopService {

	@Autowired
	private NYLoopMapper loopMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		loopMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		loopMapper.delete(pd);
	}

	/**级联删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void deleteByParent(PageData pd) throws Exception {
		loopMapper.deleteByParent(pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		loopMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return loopMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return loopMapper.listAll(pd);
	}

	/**
	 * 回路列表(带plc id)
	 * @return
	 */
	@Override
	public List<PageData> loopPlcAll() throws Exception {
		return loopMapper.loopPlcAll();
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> goExcel(PageData pd)throws Exception{
		return loopMapper.goExcel(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return loopMapper.findById(pd);
	}

	@Override
	public List<PageData> findByName(PageData pd) throws Exception {
		return loopMapper.findByName(pd);
	}

	@Override
	public PageData findByCode(PageData pd) throws Exception {
		return loopMapper.findByCode(pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		loopMapper.deleteAll(ArrayDATA_IDS);
	}

	/**通获取回路数量
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> loopCount(PageData pd) throws Exception {
		return loopMapper.loopCount(pd);
	}

	@Override
	public PageData getCodeNumByType(PageData pd) throws Exception {
		return loopMapper.getCodeNumByType(pd);
	}

	@Override
	public void editCodeNumByType(PageData pd) throws Exception {
		loopMapper.editCodeNumByType(pd);
	}
}

