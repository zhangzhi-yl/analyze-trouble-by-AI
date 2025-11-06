package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.STORAGE_UNITMapper;
import org.yy.service.mom.STORAGE_UNITService;

/** 
 * 说明： 存储单元实体接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-16
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class STORAGE_UNITServiceImpl implements STORAGE_UNITService{

	@Autowired
	private STORAGE_UNITMapper storage_unitMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		storage_unitMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		storage_unitMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		storage_unitMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return storage_unitMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return storage_unitMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return storage_unitMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		storage_unitMapper.deleteAll(ArrayDATA_IDS);
	}

	/**存储单元实体-选择存储单元类列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> getSTORAGE_UNITCLASS_IDList(PageData pd) throws Exception {
		return storage_unitMapper.getSTORAGE_UNITCLASS_IDList(pd);

	}

	/**模板阶段库-工作中心列表和查询
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> getWORKCENTER_CODEList(PageData pd) {
		return storage_unitMapper.getWORKCENTER_CODEList(pd);
	}

	/**模板阶段库-物料列表和查询
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> getMAT_CODEList(PageData pd) {
		return storage_unitMapper.getMAT_CODEList(pd);

	}
	
	/**更新容器实际数量
	 * @param pd
	 * @throws Exception
	 */
	public void editQty(PageData pd)throws Exception{
		storage_unitMapper.editQty(pd);
	}
	
	/**获取容器列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getVesselList(PageData pd)throws Exception{
		return storage_unitMapper.getVesselList(pd);
	}
	
	/**通过编号获取数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData getVesselNum(PageData pd)throws Exception{
		return storage_unitMapper.getVesselNum(pd);
	}
	
	/**通过编号获取数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData getVesselVerify(PageData pd)throws Exception{
		return storage_unitMapper.getVesselVerify(pd);
	}
}

