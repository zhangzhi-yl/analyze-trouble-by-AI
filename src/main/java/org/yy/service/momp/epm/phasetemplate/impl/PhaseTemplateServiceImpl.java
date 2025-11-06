package org.yy.service.momp.epm.phasetemplate.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.momp.PhaseTemplateMapper;
import org.yy.service.momp.epm.phasetemplate.PhaseTemplateService;


/** 
 * 说明： 工单模板接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-03-12
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class PhaseTemplateServiceImpl implements PhaseTemplateService{

	@Autowired
	private PhaseTemplateMapper phasetemplateMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		phasetemplateMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		phasetemplateMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		phasetemplateMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return phasetemplateMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return phasetemplateMapper.listAll(pd);
	}
	public List<PageData> findByIdTwo(PageData pd)throws Exception{
		return phasetemplateMapper.findByIdTwo(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return phasetemplateMapper.findById(pd);
	}
	public List<PageData> listByParentId(PageData pd)throws Exception{
		return phasetemplateMapper.listByParentId(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		phasetemplateMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获得最大编号
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getOrder(PageData pd) throws Exception {
		return phasetemplateMapper.getOrder(pd);
	}
	
}

