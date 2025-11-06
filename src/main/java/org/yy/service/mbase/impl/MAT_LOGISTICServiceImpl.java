package org.yy.service.mbase.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mbase.MAT_LOGISTICMapper;
import org.yy.service.mbase.MAT_LOGISTICService;

/** 
 * 说明： 物流资料接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-07
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class MAT_LOGISTICServiceImpl implements MAT_LOGISTICService{

	@Autowired
	private MAT_LOGISTICMapper mat_logisticMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		mat_logisticMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		mat_logisticMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		mat_logisticMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return mat_logisticMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return mat_logisticMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return mat_logisticMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		mat_logisticMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过基础资料主键删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBasic(PageData pd)throws Exception{
		mat_logisticMapper.deleteBasic(pd);
	}
	
	/**去修改页面通过基础资料Id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findBySonId(PageData pd)throws Exception{
		return mat_logisticMapper.findBySonId(pd);
	}
}

