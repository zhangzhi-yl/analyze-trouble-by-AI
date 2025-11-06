package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_MAINTAIN_WORKMXTWOMapper;
import org.yy.service.mdm.EQM_MAINTAIN_WORKMXTWOService;

/** 
 * 说明： 预防及改善措施接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-20
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_MAINTAIN_WORKMXTWOServiceImpl implements EQM_MAINTAIN_WORKMXTWOService{

	@Autowired
	private EQM_MAINTAIN_WORKMXTWOMapper eqm_maintain_workmxtwoMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_maintain_workmxtwoMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_maintain_workmxtwoMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_maintain_workmxtwoMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_maintain_workmxtwoMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_maintain_workmxtwoMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_maintain_workmxtwoMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_maintain_workmxtwoMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过设备维修报工ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteWork(PageData pd)throws Exception{
		eqm_maintain_workmxtwoMapper.deleteWork(pd);
	}
}

