package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.CallMaterialDetailsFLMapper;
import org.yy.service.mm.CallMaterialDetailsFLService;

/** 
 * 说明： 分料任务明细接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-23
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class CallMaterialDetailsFLServiceImpl implements CallMaterialDetailsFLService{

	@Autowired
	private CallMaterialDetailsFLMapper callmaterialdetailsflMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		callmaterialdetailsflMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		callmaterialdetailsflMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		callmaterialdetailsflMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return callmaterialdetailsflMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return callmaterialdetailsflMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return callmaterialdetailsflMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		callmaterialdetailsflMapper.deleteAll(ArrayDATA_IDS);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mm.CallMaterialDetailsFLService#editTargetWarehouse(org.yy.entity.PageData)
	 */
	@Override
	public void editTargetWarehouse(PageData detail) throws Exception {
		callmaterialdetailsflMapper.editTargetWarehouse(detail);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mm.CallMaterialDetailsFLService#listAllQL(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllQL(PageData bomParam) throws Exception {
		return callmaterialdetailsflMapper.listAllQL(bomParam);
	}
	
}

