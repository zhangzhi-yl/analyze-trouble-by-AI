package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.ExceptionDefinitionMapper;
import org.yy.service.mm.ExceptionDefinitionService;

/** 
 * 说明： 异常定义接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-07
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ExceptionDefinitionServiceImpl implements ExceptionDefinitionService{

	@Autowired
	private ExceptionDefinitionMapper ExceptionDefinitionMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ExceptionDefinitionMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ExceptionDefinitionMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ExceptionDefinitionMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ExceptionDefinitionMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ExceptionDefinitionMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ExceptionDefinitionMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ExceptionDefinitionMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获取数据字典中的异常类型
	 * @param 无参数
	 * @return 
	 * @throws Exception
	 */
	public List<PageData> getExceptionType(PageData pd) throws Exception {
		return ExceptionDefinitionMapper.getExceptionType(pd);
	}

	/**启用异常状态
	 * @param ExceptionDefinition_ID
	 * @return 
	 * @throws Exception
	 */
	public void toStartUsing(PageData pd) throws Exception {
		ExceptionDefinitionMapper.toStartUsing(pd);
	}
	/**停用异常状态
	 * @param ExceptionDefinition_ID
	 * @return 
	 * @throws Exception
	 */
	public void toEndUsing(PageData pd) throws Exception {
		ExceptionDefinitionMapper.toEndUsing(pd);
	}
}

