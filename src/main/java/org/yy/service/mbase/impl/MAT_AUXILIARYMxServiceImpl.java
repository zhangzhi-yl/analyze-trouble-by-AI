package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.MAT_AUXILIARYMxMapper;
import org.yy.service.mbase.MAT_AUXILIARYMxService;

/** 
 * 说明： 物料辅助属性(明细)接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-06
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class MAT_AUXILIARYMxServiceImpl implements MAT_AUXILIARYMxService{

	@Autowired
	private MAT_AUXILIARYMxMapper mat_auxiliarymxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		mat_auxiliarymxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		mat_auxiliarymxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		mat_auxiliarymxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return mat_auxiliarymxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return mat_auxiliarymxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return mat_auxiliarymxMapper.findById(pd);
	}
	
	/**通过MAT_AUXILIARYMX_CODE获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByMAT_AUXILIARYMX_CODE(PageData pd)throws Exception{
		return mat_auxiliarymxMapper.findByMAT_AUXILIARYMX_CODE(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return mat_auxiliarymxMapper.findCountByCode(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		mat_auxiliarymxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return mat_auxiliarymxMapper.findCount(pd);
	}

	/**获取辅助属性值列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> getAuxiliaryList(PageData pd) {
		return mat_auxiliarymxMapper.getAuxiliaryList(pd);
	}
	
	/**根据计划工单编号类型id和物料辅助属性值编码删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(PageData pd) {
		mat_auxiliarymxMapper.deleteByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pd);
	}

	@Override
	public List<PageData> getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(PageData pd) {
		return mat_auxiliarymxMapper.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pd);
		
	}
	
}

