package org.yy.service.zm.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.ALARMRULEMapper;
import org.yy.service.zm.ALARMRULEService;

import java.util.List;

/**
 * 说明： 报警规则接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-18
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ALARMRULEServiceImpl implements ALARMRULEService{

	@Autowired
	private ALARMRULEMapper alarmruleMapper;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		alarmruleMapper.save(pd);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		alarmruleMapper.delete(pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		alarmruleMapper.edit(pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return alarmruleMapper.datalistPage(page);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return alarmruleMapper.listAll(pd);
	}

	/**
	 * 使用列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> uselist(PageData pd) throws Exception {
		return alarmruleMapper.uselist(pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return alarmruleMapper.findById(pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		alarmruleMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> getLoopAlarm(PageData pd) throws Exception {
		return alarmruleMapper.getLoopAlarm(pd);
	}

}

