package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_VULNERABLE_PARTSMapper;
import org.yy.service.mdm.EQM_VULNERABLE_PARTSService;

/** 
 * 说明： 设备易损件接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-06-09
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_VULNERABLE_PARTSServiceImpl implements EQM_VULNERABLE_PARTSService{

	@Autowired
	private EQM_VULNERABLE_PARTSMapper eqm_vulnerable_partsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_vulnerable_partsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_vulnerable_partsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_vulnerable_partsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_vulnerable_partsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_vulnerable_partsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_vulnerable_partsMapper.findById(pd);
	}
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception{
		eqm_vulnerable_partsMapper.deleteBase(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_vulnerable_partsMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return eqm_vulnerable_partsMapper.findCount(pd);
	}
	
	/**报备列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listBB(PageData pd)throws Exception{
		return eqm_vulnerable_partsMapper.listBB(pd);
	}
	
	/**修改(设备维护时间)
	 * @param pd
	 * @throws Exception
	 */
	public void editDay(PageData pd)throws Exception{
		eqm_vulnerable_partsMapper.editDay(pd);
	}
}

