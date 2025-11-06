package org.yy.service.flow.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.flow.TECHNOLOGY_FLOWMapper;
import org.yy.service.flow.TECHNOLOGY_FLOWService;

/** 
 * 说明： 工艺工序节点接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-02
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class TECHNOLOGY_FLOWServiceImpl implements TECHNOLOGY_FLOWService{

	@Autowired
	private TECHNOLOGY_FLOWMapper TECHNOLOGY_FLOWMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		TECHNOLOGY_FLOWMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		TECHNOLOGY_FLOWMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		TECHNOLOGY_FLOWMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return TECHNOLOGY_FLOWMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return TECHNOLOGY_FLOWMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return TECHNOLOGY_FLOWMapper.findById(pd);
	}
	
	/**通过工艺路线ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findByProcessRoute_ID(PageData pd)throws Exception{
		return TECHNOLOGY_FLOWMapper.findByProcessRoute_ID(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		TECHNOLOGY_FLOWMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

