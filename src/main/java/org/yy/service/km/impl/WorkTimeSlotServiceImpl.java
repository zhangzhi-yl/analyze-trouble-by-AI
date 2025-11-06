package org.yy.service.km.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.WorkTimeSlotMapper;
import org.yy.service.km.WorkTimeSlotService;

/**
 * 说明： 工作时间段接口实现类 作者：YuanYes Q356703572 时间：2020-11-05 官网：356703572@qq.com
 * 
 * @version
 */
@Service
@Transactional // 开启事物
public class WorkTimeSlotServiceImpl implements WorkTimeSlotService {

	@Autowired
	private WorkTimeSlotMapper worktimeslotMapper;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd) throws Exception {
		worktimeslotMapper.save(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delete(PageData pd) throws Exception {
		worktimeslotMapper.delete(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void edit(PageData pd) throws Exception {
		worktimeslotMapper.edit(pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page) throws Exception {
		return worktimeslotMapper.datalistPage(page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAll(PageData pd) throws Exception {
		return worktimeslotMapper.listAll(pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findById(PageData pd) throws Exception {
		return worktimeslotMapper.findById(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		worktimeslotMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> listByIds(String[] arrayDATA_IDS) {
		// TODO Auto-generated method stub
		return worktimeslotMapper.listByIds(arrayDATA_IDS);
	}

}
