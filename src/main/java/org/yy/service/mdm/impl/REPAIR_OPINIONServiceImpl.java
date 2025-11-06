package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.REPAIR_OPINIONMapper;
import org.yy.service.mdm.REPAIR_OPINIONService;

/** 
 * 说明： 报修审批意见接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-05-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class REPAIR_OPINIONServiceImpl implements REPAIR_OPINIONService{

	@Autowired
	private REPAIR_OPINIONMapper repair_opinionMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		repair_opinionMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		repair_opinionMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		repair_opinionMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return repair_opinionMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return repair_opinionMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return repair_opinionMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		repair_opinionMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return repair_opinionMapper.findCount(pd);
	}

	@Override
	public List<PageData> AppList(AppPage page) throws Exception {
		// TODO 自动生成的方法存根
		return repair_opinionMapper.AppList(page);
	}
	
}

