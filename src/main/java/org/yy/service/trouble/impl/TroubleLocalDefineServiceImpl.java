package org.yy.service.trouble.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.trouble.TroubleLocalDefineMapper;
import org.yy.service.trouble.TroubleLocalDefineService;

/** 
 * 说明： 隐患管理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2025-09-23
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class TroubleLocalDefineServiceImpl implements TroubleLocalDefineService{

	@Autowired
	private TroubleLocalDefineMapper troublelocaldefineMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		troublelocaldefineMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		troublelocaldefineMapper.delete(pd);
	}

	@Override
	public void deleteByLocalId(PageData pd) throws Exception {
		troublelocaldefineMapper.deleteByLocalId(pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		troublelocaldefineMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return troublelocaldefineMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return troublelocaldefineMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return troublelocaldefineMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		troublelocaldefineMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

