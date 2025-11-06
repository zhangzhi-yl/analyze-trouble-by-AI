package org.yy.service.zm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.GroupMapper;
import org.yy.service.zm.GroupService;

/** 
 * 说明： 分组管理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class GroupServiceImpl implements GroupService{

	@Autowired
	private GroupMapper groupMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		groupMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		groupMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		groupMapper.edit(pd);
	}

	/**开启/关闭分组
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void editStatus(PageData pd) throws Exception {
		groupMapper.editStatus(pd);
	}

	/**
	 * 批量更新状态
	 * @param list
	 */
	@Override
	public void editAllStatus(List<PageData> list) throws Exception {
		groupMapper.editAllStatus(list);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return groupMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return groupMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return groupMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		groupMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

