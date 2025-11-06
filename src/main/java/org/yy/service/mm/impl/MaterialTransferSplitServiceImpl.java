package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.MaterialTransferSplitMapper;
import org.yy.service.mm.MaterialTransferSplitService;

/** 
 * 说明： 物料转移物料拆分表接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-15
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class MaterialTransferSplitServiceImpl implements MaterialTransferSplitService{

	@Autowired
	private MaterialTransferSplitMapper MaterialTransferSplitMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		MaterialTransferSplitMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		MaterialTransferSplitMapper.delete(pd);
	}
	
	/**根据关联id删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByFRelatedID(PageData pd)throws Exception{
		MaterialTransferSplitMapper.deleteByFRelatedID(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		MaterialTransferSplitMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return MaterialTransferSplitMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return MaterialTransferSplitMapper.listAll(pd);
	}
	
	/**根据单据主表id、辅助属性值、物料编码查询唯一码物料转出拆分明细
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findOutSplitlistByMTA_IDAndSPropKeyAndMaterialNum(PageData pd)throws Exception{
		return MaterialTransferSplitMapper.findOutSplitlistByMTA_IDAndSPropKeyAndMaterialNum(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return MaterialTransferSplitMapper.findById(pd);
	}
	
	/**通过FUniqueCode和MTADetails_ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByMTADetails_IDAndFUniqueCode(PageData pd)throws Exception{
		return MaterialTransferSplitMapper.findByMTADetails_IDAndFUniqueCode(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		MaterialTransferSplitMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

