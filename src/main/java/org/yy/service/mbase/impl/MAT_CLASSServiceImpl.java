package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.MAT_CLASSMapper;
import org.yy.service.mbase.MAT_CLASSService;

/** 
 * 说明： 物料类接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-07
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class MAT_CLASSServiceImpl implements MAT_CLASSService{

	@Autowired
	private MAT_CLASSMapper mat_classMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		mat_classMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		mat_classMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		mat_classMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return mat_classMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return mat_classMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return mat_classMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		mat_classMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获取物料类列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> classList(PageData pd)throws Exception{
		return mat_classMapper.classList(pd);
	}
	
	/**通过类名称获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByClassId(PageData pd)throws Exception{
		return mat_classMapper.findByClassId(pd);
	}

	/**获取物料类别列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getMatClassList(PageData pd) throws Exception {
		return mat_classMapper.getMatClassList(pd);
	}
}

