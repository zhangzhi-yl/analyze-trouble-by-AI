package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.Cabinet_AssemblyMapper;
import org.yy.service.project.manager.Cabinet_AssemblyService;

/** 
 * 说明： 装配表接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-04-30
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class Cabinet_AssemblyServiceImpl implements Cabinet_AssemblyService{

	@Autowired
	private Cabinet_AssemblyMapper Cabinet_AssemblyMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		Cabinet_AssemblyMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		Cabinet_AssemblyMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		Cabinet_AssemblyMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return Cabinet_AssemblyMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return Cabinet_AssemblyMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return Cabinet_AssemblyMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		Cabinet_AssemblyMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**通过项目ID和柜体类型获取柜体类型汇总ID
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByIdAndType(PageData pd)throws Exception{
		return Cabinet_AssemblyMapper.findByIdAndType(pd);
	}
}

