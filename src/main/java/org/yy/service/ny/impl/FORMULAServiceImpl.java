package org.yy.service.ny.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.ny.FORMULAMapper;
import org.yy.service.ny.FORMULAService;

/** 
 * 说明： 公式接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2021-11-20
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class FORMULAServiceImpl implements FORMULAService{

	@Autowired
	private FORMULAMapper formulaMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		formulaMapper.save(pd);
	}

	@Override
	public void saveAll(List<PageData> list) throws Exception {
		formulaMapper.saveAll(list);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		formulaMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		formulaMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return formulaMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return formulaMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return formulaMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		formulaMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

