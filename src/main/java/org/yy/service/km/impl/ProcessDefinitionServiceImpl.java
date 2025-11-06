package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.ProcessDefinitionMapper;
import org.yy.service.km.ProcessDefinitionService;

/** 
 * 说明： 工序定义接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-10
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService{

	@Autowired
	private ProcessDefinitionMapper ProcessDefinitionMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ProcessDefinitionMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ProcessDefinitionMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ProcessDefinitionMapper.edit(pd);
	}
	
	/**修改状态
	 * @param ProcessDefinition_ID 工序id
	 * @param FStatus 状态
	 * @throws Exception
	 */
	public void editStatus(PageData pd)throws Exception{
		ProcessDefinitionMapper.editStatus(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ProcessDefinitionMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ProcessDefinitionMapper.listAll(pd);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return ProcessDefinitionMapper.findCount(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ProcessDefinitionMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ProcessDefinitionMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

