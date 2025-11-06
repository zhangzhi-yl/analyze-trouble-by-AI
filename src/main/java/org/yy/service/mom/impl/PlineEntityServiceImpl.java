package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.PlineEntityMapper;
import org.yy.service.mom.PlineEntityService;

/** 
 * 说明： 产线实体管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-07
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class PlineEntityServiceImpl implements PlineEntityService{

	@Autowired
	private PlineEntityMapper plineentityMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		plineentityMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		plineentityMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		plineentityMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return plineentityMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return plineentityMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return plineentityMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		plineentityMapper.deleteAll(ArrayDATA_IDS);
	}

	/**
	 * 根据产线类别ID查询产线实体总数
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findCountByPlineClassID(PageData pd) throws Exception {
		return plineentityMapper.findCountByPlineClassID(pd);
	}
	
	/**
	 * 根据车间ID查询产线实体总数
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findCountByAreaID(PageData pd) throws Exception {
		return plineentityMapper.findCountByAreaID(pd);
	}

	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByFCODE(Page page) throws Exception {
		return plineentityMapper.findByFCODE(page);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return plineentityMapper.findCountByCode(pd);
	}
	
}

