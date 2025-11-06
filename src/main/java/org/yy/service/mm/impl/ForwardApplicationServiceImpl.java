package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.ForwardApplicationMapper;
import org.yy.service.mm.ForwardApplicationService;

/** 
 * 说明： 发运申请接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-11-20
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class ForwardApplicationServiceImpl implements ForwardApplicationService{

	@Autowired
	private ForwardApplicationMapper ForwardApplicationMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		ForwardApplicationMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ForwardApplicationMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ForwardApplicationMapper.edit(pd);
	}
	
	/**修改FCustomer
	 * @param pd
	 * @throws Exception
	 */
	public void editFCustomer(PageData pd)throws Exception{
		ForwardApplicationMapper.editFCustomer(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ForwardApplicationMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return ForwardApplicationMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ForwardApplicationMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ForwardApplicationMapper.deleteAll(ArrayDATA_IDS);
	}

	/**单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return ForwardApplicationMapper.getRepeatNum(pd);
	}

	/**审核或反审核发运申请
	 * @param pd
	 */
	@Override
	public void editAudit(PageData pd) throws Exception {
		ForwardApplicationMapper.editAudit(pd);
	}

	/**下发或取消
	 * @param pd
	 */
	@Override
	public void editFStatus(PageData pd) throws Exception {
		ForwardApplicationMapper.editFStatus(pd);
	}

	@Override
	public List<PageData> GET_FYSQ_ZHUISU_listPage(Page page) throws Exception {
		return ForwardApplicationMapper.GET_FYSQ_ZHUISU_listPage(page);
	}
	
}

