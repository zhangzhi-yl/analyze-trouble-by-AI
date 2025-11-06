package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.CallMaterialFLMapper;
import org.yy.service.mm.CallMaterialFLService;

/** 
 * 说明： 分料任务接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-08-23
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class CallMaterialFLServiceImpl implements CallMaterialFLService{

	@Autowired
	private CallMaterialFLMapper callmaterialflMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		callmaterialflMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		callmaterialflMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		callmaterialflMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return callmaterialflMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return callmaterialflMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return callmaterialflMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		callmaterialflMapper.deleteAll(ArrayDATA_IDS);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mm.CallMaterialFLService#listHis(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listHis(Page page) throws Exception {
		return callmaterialflMapper.listHislistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mm.CallMaterialFLService#listCJ(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listCJ(Page page) throws Exception {
		return callmaterialflMapper.listCJlistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mm.CallMaterialFLService#listCJHis(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listCJHis(Page page) throws Exception {
		return callmaterialflMapper.listCJHislistPage(page);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mm.CallMaterialFLService#listCJHisWL(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listCJHisWL(Page page) throws Exception {
		return callmaterialflMapper.listCJHisWLlistPage(page);
	}
	
}

