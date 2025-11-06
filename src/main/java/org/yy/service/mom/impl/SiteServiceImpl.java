package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.SiteMapper;
import org.yy.service.mom.SiteService;

/** 
 * 说明： 工厂管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-06
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class SiteServiceImpl implements SiteService{

	@Autowired
	private SiteMapper siteMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		siteMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		siteMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		siteMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return siteMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return siteMapper.listAll(pd);
	}

	/**列表(全部) 有车间的
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listTopAll(PageData pd) throws Exception {
		return siteMapper.listTopAll(pd);
	}

	@Override
	public List<PageData> checkIDSInArea(PageData pd) throws Exception {
		return siteMapper.checkIDSInArea(pd);
	}

	@Override
	public List<PageData> checkIDSInSite(PageData pd) throws Exception {
		return siteMapper.checkIDSInSite(pd);
	}

	@Override
	public List<PageData> findByName(PageData pd) throws Exception {
		return siteMapper.findByName(pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return siteMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		siteMapper.deleteAll(ArrayDATA_IDS);
	}

	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByFCODE(Page page) throws Exception {
		return siteMapper.findByFCODE(page);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return siteMapper.findCountByCode(pd);
	}
	
}

