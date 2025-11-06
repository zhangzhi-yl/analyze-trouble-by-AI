package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_SPECIFICATIONMapper;
import org.yy.service.mdm.EQM_SPECIFICATIONService;

/** 
 * 说明： 设备规范明细资料接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-14
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_SPECIFICATIONServiceImpl implements EQM_SPECIFICATIONService{

	@Autowired
	private EQM_SPECIFICATIONMapper eqm_specificationMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_specificationMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_specificationMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_specificationMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_specificationMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_specificationMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_specificationMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_specificationMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过设备基础资料ID删除信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBase(PageData pd)throws Exception{
		eqm_specificationMapper.deleteBase(pd);
	}
	
	/**
	 *	根据列KEY名和设备id查询列VALUE值
	 * @param pd
	 * @return
	 */
	public PageData findByKeyNameAndEQM_BASE_ID(PageData pd)throws Exception{
		return eqm_specificationMapper.findByKeyNameAndEQM_BASE_ID(pd);
	}
}

