package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_CLASSMapper;
import org.yy.service.mdm.EQM_CLASSService;

/** 
 * 说明： 设备类接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-14
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_CLASSServiceImpl implements EQM_CLASSService{

	@Autowired
	private EQM_CLASSMapper eqm_classMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_classMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_classMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_classMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_classMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_classMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_classMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_classMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**设备类下拉列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> classList(PageData pd)throws Exception{
		return eqm_classMapper.classList(pd);
	}
	
	/**删除图片
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delTp(PageData pd) throws Exception {
		eqm_classMapper.delTp(pd);
	}
}

