package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.REPAIR_REPORTWORKMapper;
import org.yy.service.mdm.REPAIR_REPORTWORKService;

/** 
 * 说明： 维修报工接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-05-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class REPAIR_REPORTWORKServiceImpl implements REPAIR_REPORTWORKService{

	@Autowired
	private REPAIR_REPORTWORKMapper repair_reportworkMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		repair_reportworkMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		repair_reportworkMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		repair_reportworkMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return repair_reportworkMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return repair_reportworkMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return repair_reportworkMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		repair_reportworkMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return repair_reportworkMapper.findCount(pd);
	}

	/**
	 * 查询维修报工时间段中数据数量
	 */
	@Override
	public PageData findCountByTime(PageData pd) throws Exception {
		return repair_reportworkMapper.findCountByTime(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		// TODO 自动生成的方法存根
		return repair_reportworkMapper.AppList(page);
	}
	
}

