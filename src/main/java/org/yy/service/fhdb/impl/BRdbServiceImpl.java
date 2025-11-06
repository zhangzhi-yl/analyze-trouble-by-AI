package org.yy.service.fhdb.impl;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.fhdb.BRdbMapper;
import org.yy.service.fhdb.BRdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：数据库管理接口实现类
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
@Service(value="bRdbServiceImpl")
@Transactional //开启事物
public class BRdbServiceImpl implements BRdbService {
	
	@Autowired
	private BRdbMapper bRdbMapper;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		bRdbMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		bRdbMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		bRdbMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return bRdbMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return bRdbMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return bRdbMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		bRdbMapper.deleteAll(ArrayDATA_IDS);
	}
	/**
	 * 更新企业微信access_token
	 * @param pd
	 * @throws Exception
	 */
	public void editWX(PageData pd)throws Exception{
		bRdbMapper.editWX(pd);
	}
	/**
	 * 查询企业微信access_token
	 * @return
	 * @throws Exception
	 */
	public PageData findWXById()throws Exception{
		return (PageData)bRdbMapper.findWXById();
	}
}
