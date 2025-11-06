package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_INSTALLMapper;
import org.yy.service.mdm.EQM_INSTALLService;

/** 
 * 说明： 设备安装情况记录接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-06-10
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_INSTALLServiceImpl implements EQM_INSTALLService{

	@Autowired
	private EQM_INSTALLMapper eqm_installMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_installMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_installMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_installMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_installMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_installMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_installMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_installMapper.deleteAll(ArrayDATA_IDS);
	}

	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception{
		eqm_installMapper.deleteBase(pd);
	}
	
	/**通过设备id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findByParentId(PageData pd) throws Exception {
		return eqm_installMapper.findByParentId(pd);
	}
	
}

