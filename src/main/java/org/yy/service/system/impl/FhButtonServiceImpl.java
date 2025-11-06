package org.yy.service.system.impl;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.system.FhButtonMapper;
import org.yy.service.system.FhButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：按钮权限服务接口实现类
 * 作者：YuanYe Q356703572
 * 
 */
@Service
@Transactional //开启事物
public class FhButtonServiceImpl implements FhButtonService {
	
	@Autowired
	private FhButtonMapper fhButtonMapper;

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return fhButtonMapper.listAll(pd);
	}
	
	/**列表(带分页)
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return fhButtonMapper.datalistPage(page);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		fhButtonMapper.delete(pd);
	}

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		fhButtonMapper.save(pd);
		
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		fhButtonMapper.edit(pd);
		
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return fhButtonMapper.findById(pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		fhButtonMapper.deleteAll(ArrayDATA_IDS);
	}

}
