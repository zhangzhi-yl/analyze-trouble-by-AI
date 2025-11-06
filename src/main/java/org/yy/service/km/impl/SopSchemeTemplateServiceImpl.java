package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.SopSchemeTemplateMapper;
import org.yy.service.km.SopSchemeTemplateService;

/** 
 * 说明： SOP方案模板接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-18
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SopSchemeTemplateServiceImpl implements SopSchemeTemplateService{

	@Autowired
	private SopSchemeTemplateMapper SopSchemeTemplateMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		SopSchemeTemplateMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		SopSchemeTemplateMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		SopSchemeTemplateMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return SopSchemeTemplateMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return SopSchemeTemplateMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return SopSchemeTemplateMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		SopSchemeTemplateMapper.deleteAll(ArrayDATA_IDS);
	}

	/**禁用/启用
	 * @param pd
	 */
	@Override
	public void editState(PageData pd) throws Exception {
		SopSchemeTemplateMapper.editState(pd);
	}

	/**关联删除明细
	 * @param pd
	 */
	@Override
	public void deleteMx(PageData pd) throws Exception {
		SopSchemeTemplateMapper.deleteMx(pd);
	}

	/**单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return SopSchemeTemplateMapper.getRepeatNum(pd);
	}
	
}

