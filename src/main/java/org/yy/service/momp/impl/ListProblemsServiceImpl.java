package org.yy.service.momp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.ListProblemsMapper;
import org.yy.service.momp.ListProblemsService;

/** 
 * 说明： 问题清单功能（龙油）接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-04-13
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class ListProblemsServiceImpl implements ListProblemsService{

	@Autowired
	private ListProblemsMapper listproblemsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		listproblemsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		listproblemsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		listproblemsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return listproblemsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return listproblemsMapper.listAll(pd);
	}
	public List<PageData> findlistByRisk(PageData pd)throws Exception{
		return listproblemsMapper.findlistByRisk(pd);
	}
	public List<PageData> findlistByItem(PageData pd)throws Exception{
		return listproblemsMapper.findlistByItem(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return listproblemsMapper.findById(pd);
	}
	
	/**删除按父id
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByMainId(PageData pd)throws Exception{
		listproblemsMapper.deleteByMainId(pd);
	}
	
	/**通过父id获取时间
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByMainId(PageData pd)throws Exception{
		return listproblemsMapper.findByMainId(pd);
	}
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		listproblemsMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

