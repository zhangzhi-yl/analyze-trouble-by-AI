package org.yy.service.flow.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.flow.NEWPLAN_BOMMapper;
import org.yy.service.flow.NEWPLAN_BOMService;

/**
 * 说明： 实例阶段-计划BOM工单接口实现类 作者：YuanYes Q356703572 时间：2020-12-01 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class NEWPLAN_BOMServiceImpl implements NEWPLAN_BOMService {

	@Autowired
	private NEWPLAN_BOMMapper NEWPLAN_BOMMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		NEWPLAN_BOMMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		NEWPLAN_BOMMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		NEWPLAN_BOMMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return NEWPLAN_BOMMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return NEWPLAN_BOMMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return NEWPLAN_BOMMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		NEWPLAN_BOMMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 根据子计划工单删除所有节点
	 */
	public void deleteBySubPlanID(String SUBPLAN_ID) throws Exception {
		NEWPLAN_BOMMapper.deleteBySubPlanID(SUBPLAN_ID);
	}

	/**
	 * 下一个节点
	 */
	@Override
	public List<PageData> nextNodeList(PageData pd) throws Exception {
		return NEWPLAN_BOMMapper.nextNodeList(pd);
	}

	/**
	 * 上一个节点
	 */
	@Override
	public List<PageData> lastNodeList(PageData pd) throws Exception {
		return NEWPLAN_BOMMapper.lastNodeList(pd);
	}

	@Override
	public void deleteBySubPlanIdAndNodeId(PageData pd) {
		NEWPLAN_BOMMapper.deleteBySubPlanIdAndNodeId(pd);
		
	}

}
