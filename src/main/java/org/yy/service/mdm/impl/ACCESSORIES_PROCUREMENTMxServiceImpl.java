package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.ACCESSORIES_PROCUREMENTMxMapper;
import org.yy.service.mdm.ACCESSORIES_PROCUREMENTMxService;

/** 
 * 说明： 设备配件采购(明细)接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-05-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ACCESSORIES_PROCUREMENTMxServiceImpl implements ACCESSORIES_PROCUREMENTMxService{

	@Autowired
	private ACCESSORIES_PROCUREMENTMxMapper accessories_procurementmxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		accessories_procurementmxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		accessories_procurementmxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		accessories_procurementmxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return accessories_procurementmxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return accessories_procurementmxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return accessories_procurementmxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		accessories_procurementmxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return accessories_procurementmxMapper.findCount(pd);
	}
	
}

