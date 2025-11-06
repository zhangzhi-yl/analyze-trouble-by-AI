package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.CallMaterialOperateMapper;
import org.yy.service.mm.CallMaterialOperateService;

/** 
 * 说明： 叫料申请操作记录接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-25
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class CallMaterialOperateServiceImpl implements CallMaterialOperateService{

	@Autowired
	private CallMaterialOperateMapper CallMaterialOperateMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		CallMaterialOperateMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		CallMaterialOperateMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		CallMaterialOperateMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return CallMaterialOperateMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return CallMaterialOperateMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return CallMaterialOperateMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		CallMaterialOperateMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

