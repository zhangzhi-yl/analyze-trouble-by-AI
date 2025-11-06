package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.DOCUMENTTEMPLATEMxMapper;
import org.yy.service.mom.DOCUMENTTEMPLATEMxService;

/** 
 * 说明： 检验单主模板(明细)接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-21
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class DOCUMENTTEMPLATEMxServiceImpl implements DOCUMENTTEMPLATEMxService{

	@Autowired
	private DOCUMENTTEMPLATEMxMapper documenttemplatemxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		documenttemplatemxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		documenttemplatemxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		documenttemplatemxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return documenttemplatemxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return documenttemplatemxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return documenttemplatemxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		documenttemplatemxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return documenttemplatemxMapper.findCount(pd);
	}
	
	/**根据主表ID查询列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listMxAll(PageData pd)throws Exception{
		return documenttemplatemxMapper.listMxAll(pd);
	}
}

