package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.ProcessDefectiveItemsMapper;
import org.yy.service.km.ProcessDefectiveItemsService;

/** 
 * 说明： 工序次品项列表接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-10
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ProcessDefectiveItemsServiceImpl implements ProcessDefectiveItemsService{

	@Autowired
	private ProcessDefectiveItemsMapper ProcessDefectiveItemsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ProcessDefectiveItemsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ProcessDefectiveItemsMapper.delete(pd);
	}
	
	/**根据工序定义id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByProcessDefinition_ID(PageData pd)throws Exception{
		ProcessDefectiveItemsMapper.deleteByProcessDefinition_ID(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ProcessDefectiveItemsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ProcessDefectiveItemsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ProcessDefectiveItemsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ProcessDefectiveItemsMapper.findById(pd);
	}
	
	/**通过工序定义ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findByWP_ID(PageData pd)throws Exception{
		return ProcessDefectiveItemsMapper.findByWP_ID(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ProcessDefectiveItemsMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

