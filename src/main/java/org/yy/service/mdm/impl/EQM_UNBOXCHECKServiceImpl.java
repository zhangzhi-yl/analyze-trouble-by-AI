package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_UNBOXCHECKMapper;
import org.yy.service.mdm.EQM_UNBOXCHECKService;

/** 
 * 说明： 设备开箱检查验收单接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-06-10
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_UNBOXCHECKServiceImpl implements EQM_UNBOXCHECKService{

	@Autowired
	private EQM_UNBOXCHECKMapper eqm_unboxcheckMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_unboxcheckMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_unboxcheckMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_unboxcheckMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_unboxcheckMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_unboxcheckMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_unboxcheckMapper.findById(pd);
	}
	
	/**通过设备id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByParentId(PageData pd)throws Exception{
		return eqm_unboxcheckMapper.findByParentId(pd);
	}
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception{
		eqm_unboxcheckMapper.deleteBase(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_unboxcheckMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

