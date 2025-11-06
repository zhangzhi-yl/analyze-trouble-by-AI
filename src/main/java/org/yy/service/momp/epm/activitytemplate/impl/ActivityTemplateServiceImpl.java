package org.yy.service.momp.epm.activitytemplate.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.ActivityTemplateMapper;
import org.yy.service.momp.epm.activitytemplate.ActivityTemplateService;


/** 
 * 说明： 活动模板接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-03-12
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class ActivityTemplateServiceImpl implements ActivityTemplateService{

	@Autowired
	private ActivityTemplateMapper activitytemplateMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		activitytemplateMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		activitytemplateMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		activitytemplateMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return activitytemplateMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return activitytemplateMapper.listAll(pd);
	}
	public List<PageData> findByFATNAME(PageData pd)throws Exception{
		return activitytemplateMapper.findByFATNAME(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return activitytemplateMapper.findById(pd);
	}
	
	public List<PageData> listByParentId(PageData pd)throws Exception{
		return activitytemplateMapper.listByParentId(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		activitytemplateMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

