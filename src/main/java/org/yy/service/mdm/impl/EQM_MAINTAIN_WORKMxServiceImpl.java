package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_MAINTAIN_WORKMxMapper;
import org.yy.service.mdm.EQM_MAINTAIN_WORKMxService;

/** 
 * 说明： 设备维修报工(明细)接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-19
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_MAINTAIN_WORKMxServiceImpl implements EQM_MAINTAIN_WORKMxService{

	@Autowired
	private EQM_MAINTAIN_WORKMxMapper eqm_maintain_workmxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_maintain_workmxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_maintain_workmxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_maintain_workmxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_maintain_workmxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_maintain_workmxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_maintain_workmxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_maintain_workmxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return eqm_maintain_workmxMapper.findCount(pd);
	}
	
	/**通过设备维修报工ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteWork(PageData pd)throws Exception{
		eqm_maintain_workmxMapper.deleteWork(pd);
	}
}

