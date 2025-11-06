package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.ConversionUnitMapper;
import org.yy.service.mbase.ConversionUnitService;

/** 
 * 说明： 物料子表-转换单位接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-05
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ConversionUnitServiceImpl implements ConversionUnitService{

	@Autowired
	private ConversionUnitMapper conversionunitMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		conversionunitMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		conversionunitMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		conversionunitMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return conversionunitMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return conversionunitMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return conversionunitMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		conversionunitMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

