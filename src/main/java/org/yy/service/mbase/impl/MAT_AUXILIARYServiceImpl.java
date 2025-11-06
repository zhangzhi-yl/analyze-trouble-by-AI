package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.MAT_AUXILIARYMapper;
import org.yy.service.mbase.MAT_AUXILIARYService;

/** 
 * 说明： 物料辅助属性接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class MAT_AUXILIARYServiceImpl implements MAT_AUXILIARYService{

	@Autowired
	private MAT_AUXILIARYMapper mat_auxiliaryMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		mat_auxiliaryMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		mat_auxiliaryMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		mat_auxiliaryMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return mat_auxiliaryMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return mat_auxiliaryMapper.listAll(pd);
	}
	
	/**top100列表  
	 * @param KEYWORDS、MAT_AUXILIARY_CODE、MAT_AUXILIARY_NAME 代码、名称模糊搜索
	 * @throws Exception
	 */
	public List<PageData> getAuxliaryList(PageData pd){
		return mat_auxiliaryMapper.getAuxliaryList(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return mat_auxiliaryMapper.findById(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return mat_auxiliaryMapper.findCountByCode(pd);
	}
	
	/**根据辅助属性名称查询
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByMAT_AUXILIARY_NAME(PageData pd)throws Exception{
		return mat_auxiliaryMapper.findByMAT_AUXILIARY_NAME(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		mat_auxiliaryMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

