package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.REPAIR_OUTSOURCINGMapper;
import org.yy.service.mdm.REPAIR_OUTSOURCINGService;

/** 
 * 说明： 报修委外接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-05-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class REPAIR_OUTSOURCINGServiceImpl implements REPAIR_OUTSOURCINGService{

	@Autowired
	private REPAIR_OUTSOURCINGMapper repair_outsourcingMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		repair_outsourcingMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		repair_outsourcingMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		repair_outsourcingMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return repair_outsourcingMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return repair_outsourcingMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return repair_outsourcingMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		repair_outsourcingMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return repair_outsourcingMapper.findCount(pd);
	}

	/**
	 * 查询最大编号
	 */
	@Override
	public PageData findMaxNo() throws Exception {
		return repair_outsourcingMapper.findMaxNo();
	}

	/**
	 * 根据报修工单ID查询需要配件的委外信息数量
	 */
	@Override
	public PageData findNeedAccessoriesCount(PageData pd) throws Exception {
		return repair_outsourcingMapper.findNeedAccessoriesCount(pd);
	}
	
}

