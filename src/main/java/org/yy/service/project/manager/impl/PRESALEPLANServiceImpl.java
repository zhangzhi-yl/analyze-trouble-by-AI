package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.PRESALEPLANMapper;
import org.yy.service.project.manager.PRESALEPLANService;

/** 
 * 说明： 售前方案计划接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class PRESALEPLANServiceImpl implements PRESALEPLANService{

	@Autowired
	private PRESALEPLANMapper presaleplanMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		presaleplanMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		presaleplanMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		presaleplanMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return presaleplanMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return presaleplanMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return presaleplanMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		presaleplanMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**审核操作
	 * @param pd
	 * @throws Exception
	 */
	public void goAduit(PageData pd)throws Exception{
		presaleplanMapper.goAduit(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.PRESALEPLANService#editLXSQ(org.yy.entity.PageData)
	 */
	@Override
	public void editLXSQ(PageData pd) throws Exception {
		presaleplanMapper.editLXSQ(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.PRESALEPLANService#editLX(org.yy.entity.PageData)
	 */
	@Override
	public void editLX(PageData pd) throws Exception {
		presaleplanMapper.editLX(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.project.manager.PRESALEPLANService#listLXSQ(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listLXSQ(Page page) throws Exception {
		return presaleplanMapper.LXSQlistPage(page);
	}
}

