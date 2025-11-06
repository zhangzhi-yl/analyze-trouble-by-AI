package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.TOOL_BASICSMapper;
import org.yy.service.km.TOOL_BASICSService;

/** 
 * 说明： 工器具管理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-09
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class TOOL_BASICSServiceImpl implements TOOL_BASICSService{

	@Autowired
	private TOOL_BASICSMapper TOOL_BASICSMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		TOOL_BASICSMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		TOOL_BASICSMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		TOOL_BASICSMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return TOOL_BASICSMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return TOOL_BASICSMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return TOOL_BASICSMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		TOOL_BASICSMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**更新工器具管理状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		TOOL_BASICSMapper.editState(pd);
	}
}

