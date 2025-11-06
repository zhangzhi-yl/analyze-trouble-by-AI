package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.MAT_SPECMapper;
import org.yy.service.mbase.MAT_SPECService;

/** 
 * 说明： 物料规格附表接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-25
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class MAT_SPECServiceImpl implements MAT_SPECService{

	@Autowired
	private MAT_SPECMapper MAT_SPECMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		MAT_SPECMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		MAT_SPECMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		MAT_SPECMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return MAT_SPECMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return MAT_SPECMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return MAT_SPECMapper.findById(pd);
	}
	
	/**查询规格单位及数据都相同的总数据量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByUnitAndQty(PageData pd)throws Exception{
		return MAT_SPECMapper.findCountByUnitAndQty(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		MAT_SPECMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过基础资料主键删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBasic(PageData pd)throws Exception{
		MAT_SPECMapper.deleteBasic(pd);
	}
}

