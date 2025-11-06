package org.yy.service.project.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.ASSEMBLYSTATISTICSMapper;
import org.yy.service.project.manager.ASSEMBLYSTATISTICSService;

/**
 * 说明： toc折线图报表接口实现类 作者：YuanYes Q356703572 时间：2021-05-13 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class ASSEMBLYSTATISTICSServiceImpl implements ASSEMBLYSTATISTICSService {

	@Autowired
	private ASSEMBLYSTATISTICSMapper ASSEMBLYSTATISTICSMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		ASSEMBLYSTATISTICSMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		ASSEMBLYSTATISTICSMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		ASSEMBLYSTATISTICSMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return ASSEMBLYSTATISTICSMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return ASSEMBLYSTATISTICSMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return ASSEMBLYSTATISTICSMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		ASSEMBLYSTATISTICSMapper.deleteAll(ArrayDATA_IDS);
	}

}
