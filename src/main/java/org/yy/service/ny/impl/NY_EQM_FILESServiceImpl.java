package org.yy.service.ny.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.NY_EQM_FILESMapper;
import org.yy.mapper.dsno1.zm.EQM_FILESMapper;
import org.yy.service.ny.NY_EQM_FILESService;

import java.util.List;

/** 
 * 说明： 设备附件接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-09
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class NY_EQM_FILESServiceImpl implements NY_EQM_FILESService {

	@Autowired
	private NY_EQM_FILESMapper eqm_filesMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_filesMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_filesMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_filesMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_filesMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_filesMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_filesMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_filesMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 设备级联删除
	 * @param pd
	 */
	@Override
	public void deleteByEqm(PageData pd) throws Exception {
		eqm_filesMapper.deleteByEqm(pd);
	}

}

