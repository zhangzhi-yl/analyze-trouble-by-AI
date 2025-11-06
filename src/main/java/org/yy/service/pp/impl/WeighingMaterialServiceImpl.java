package org.yy.service.pp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.pp.WeighingMaterialMapper;
import org.yy.service.pp.WeighingMaterialService;

/** 
 * 说明： 称量任务物料明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-30
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class WeighingMaterialServiceImpl implements WeighingMaterialService{

	@Autowired
	private WeighingMaterialMapper WeighingMaterialMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		WeighingMaterialMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		WeighingMaterialMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		WeighingMaterialMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return WeighingMaterialMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return WeighingMaterialMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return WeighingMaterialMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		WeighingMaterialMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

