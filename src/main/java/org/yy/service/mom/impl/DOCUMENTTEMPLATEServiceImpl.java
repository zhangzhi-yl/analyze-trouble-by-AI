package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.DOCUMENTTEMPLATEMapper;
import org.yy.service.mom.DOCUMENTTEMPLATEService;

/** 
 * 说明： 检验单主模板接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-21
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class DOCUMENTTEMPLATEServiceImpl implements DOCUMENTTEMPLATEService{

	@Autowired
	private DOCUMENTTEMPLATEMapper documenttemplateMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		documenttemplateMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		documenttemplateMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		documenttemplateMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return documenttemplateMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return documenttemplateMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return documenttemplateMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		documenttemplateMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**修改发布状态
	 * @param pd
	 * @throws Exception
	 */
	public void editIssue(PageData pd)throws Exception{
		documenttemplateMapper.editIssue(pd);
	}
	
	/**模板名称根据工单关键字查询数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getTemplate(PageData pd)throws Exception{
		return documenttemplateMapper.getTemplate(pd);
	}
	
	/**修改SQL
	 * @param pd
	 * @throws Exception
	 */
	public void editSql(PageData pd)throws Exception{
		documenttemplateMapper.editSql(pd);
	}
	
	/**查询模板名称及ID
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByStencil(PageData pd)throws Exception{
		return documenttemplateMapper.findByStencil(pd);
	}
}

