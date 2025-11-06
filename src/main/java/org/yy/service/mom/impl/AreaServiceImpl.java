package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.AreaMapper;
import org.yy.service.mom.AreaService;

/** 
 * 说明： 车间管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-06
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class AreaServiceImpl implements AreaService{

	@Autowired
	private AreaMapper areaMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		areaMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		areaMapper.delete(pd);
	}

	/**级联删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void deleteBySite(PageData pd) throws Exception {
		areaMapper.deleteBySite(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		areaMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return areaMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return areaMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return areaMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		areaMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过工厂id查询车间总数
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findCount(PageData pd) throws Exception {
		return areaMapper.findCount(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return areaMapper.findCountByCode(pd);
	}
	
	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByFCODE(Page page) throws Exception {
		return areaMapper.findByFCODE(page);
	}


	@Override
	public List<PageData> Arealist(PageData pd) {
		return areaMapper.Arealist(pd);
	}

}

