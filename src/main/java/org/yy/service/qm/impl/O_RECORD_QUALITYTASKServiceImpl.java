package org.yy.service.qm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.qm.O_RECORD_QUALITYTASKMapper;
import org.yy.service.qm.O_RECORD_QUALITYTASKService;

/** 
 * 说明： 柜体质检执行操作记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-07-21
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class O_RECORD_QUALITYTASKServiceImpl implements O_RECORD_QUALITYTASKService{

	@Autowired
	private O_RECORD_QUALITYTASKMapper o_record_qualitytaskMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		o_record_qualitytaskMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		o_record_qualitytaskMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		o_record_qualitytaskMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return o_record_qualitytaskMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return o_record_qualitytaskMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return o_record_qualitytaskMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		o_record_qualitytaskMapper.deleteAll(ArrayDATA_IDS);
	}


	@Override
	public PageData findByExampleID(PageData pd) throws Exception {
		return o_record_qualitytaskMapper.findByExampleID(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.qm.O_RECORD_QUALITYTASKService#editEnd(org.yy.entity.PageData)
	 */
	@Override
	public void editEnd(PageData pd) throws Exception {
		o_record_qualitytaskMapper.editEnd(pd);
	}
	
}

