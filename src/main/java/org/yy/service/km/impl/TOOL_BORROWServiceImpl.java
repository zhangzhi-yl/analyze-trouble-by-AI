package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.TOOL_BORROWMapper;
import org.yy.service.km.TOOL_BORROWService;

/** 
 * 说明： 工器具借用归还记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-09
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class TOOL_BORROWServiceImpl implements TOOL_BORROWService{

	@Autowired
	private TOOL_BORROWMapper TOOL_BORROWMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		TOOL_BORROWMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		TOOL_BORROWMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		TOOL_BORROWMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return TOOL_BORROWMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return TOOL_BORROWMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return TOOL_BORROWMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		TOOL_BORROWMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过工器具管理ID获取状态为借用的数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBASICSId(PageData pd)throws Exception{
		return TOOL_BORROWMapper.findByBASICSId(pd);
	}
	
	/**通过主表ID删除数据
	 * @param pd
	 * @throws Exception
	 */
	public void deleteBASICS(PageData pd)throws Exception{
		TOOL_BORROWMapper.deleteBASICS(pd);
	}
}

