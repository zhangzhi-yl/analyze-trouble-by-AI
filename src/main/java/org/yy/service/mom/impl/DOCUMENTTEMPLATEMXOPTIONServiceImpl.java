package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.DOCUMENTTEMPLATEMXOPTIONMapper;
import org.yy.service.mom.DOCUMENTTEMPLATEMXOPTIONService;

/** 
 * 说明： 检验单模板明细接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-21
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class DOCUMENTTEMPLATEMXOPTIONServiceImpl implements DOCUMENTTEMPLATEMXOPTIONService{

	@Autowired
	private DOCUMENTTEMPLATEMXOPTIONMapper documenttemplatemxoptionMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		documenttemplatemxoptionMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		documenttemplatemxoptionMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		documenttemplatemxoptionMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return documenttemplatemxoptionMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return documenttemplatemxoptionMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return documenttemplatemxoptionMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		documenttemplatemxoptionMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

