package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_SPARE_PARTSMapper;
import org.yy.service.mdm.EQM_SPARE_PARTSService;

/** 
 * 说明： 设备备品备件接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-14
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_SPARE_PARTSServiceImpl implements EQM_SPARE_PARTSService{

	@Autowired
	private EQM_SPARE_PARTSMapper eqm_spare_partsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_spare_partsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_spare_partsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_spare_partsMapper.edit(pd);
	}
	/**修改数量
	 * @param pd
	 * @throws Exception
	 */
	public void editAmount(PageData pd)throws Exception{
		eqm_spare_partsMapper.editAmount(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_spare_partsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_spare_partsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_spare_partsMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_spare_partsMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception{
		eqm_spare_partsMapper.deleteBase(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		// TODO 自动生成的方法存根
		return eqm_spare_partsMapper.AppList(page);
	}
}

