package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.CallMaterialDetailsMapper;
import org.yy.service.mm.CallMaterialDetailsService;

/** 
 * 说明： 叫料申请明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-25
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class CallMaterialDetailsServiceImpl implements CallMaterialDetailsService{

	@Autowired
	private CallMaterialDetailsMapper CallMaterialDetailsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		CallMaterialDetailsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		CallMaterialDetailsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		CallMaterialDetailsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return CallMaterialDetailsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return CallMaterialDetailsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return CallMaterialDetailsMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		CallMaterialDetailsMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData>  detailListByCallMaterialID(AppPage page) {
		return CallMaterialDetailsMapper.detailListByCallMaterialID(page);
	}

	@Override
	public void deleteByCondition(PageData pd) throws Exception {
		CallMaterialDetailsMapper.deleteByCondition(pd);
		
	}
	
}

