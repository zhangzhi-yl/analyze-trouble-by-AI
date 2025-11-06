package org.yy.service.momp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.PhaseProcessTemplateMapper;
import org.yy.service.momp.PhaseProcessTemplateService;

/** 
 * 说明： MOMPphase模板接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-03-23
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class PhaseProcessTemplateServiceImpl implements PhaseProcessTemplateService{

	@Autowired
	private PhaseProcessTemplateMapper phaseprocesstemplateMapper;
	/**
	 * 查询当前最大个数
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public PageData count(PageData pd)throws Exception{
		return phaseprocesstemplateMapper.count(pd);
	}
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		phaseprocesstemplateMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		phaseprocesstemplateMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		phaseprocesstemplateMapper.edit(pd);
	}
	public void updateinputjson(PageData pd)throws Exception{
		phaseprocesstemplateMapper.updateinputjson(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return phaseprocesstemplateMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return phaseprocesstemplateMapper.listAll(pd);
	}
	/**列表(全部) 物料列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllBASIC(PageData pd)throws Exception{
		return phaseprocesstemplateMapper.listAllBASIC(pd);
	}
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return phaseprocesstemplateMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		phaseprocesstemplateMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

