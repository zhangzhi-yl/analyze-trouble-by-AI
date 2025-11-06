package org.yy.service.momp.epm.plantemplate.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.PlanTemplateMapper;
import org.yy.service.momp.epm.plantemplate.PlanTemplateService;


/** 
 * 说明： 工单阶段模板接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-03-12
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class PlanTemplateServiceImpl implements PlanTemplateService{

	@Autowired
	private PlanTemplateMapper plantemplateMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		plantemplateMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		plantemplateMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		plantemplateMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return plantemplateMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return plantemplateMapper.listAll(pd);
	}
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return plantemplateMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		plantemplateMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

