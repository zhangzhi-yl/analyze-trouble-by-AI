package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.TEMBILL_EXECUTEMxMapper;
import org.yy.service.mom.TEMBILL_EXECUTEMxService;

/** 
 * 说明： 质量检测发布(明细)接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-24
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class TEMBILL_EXECUTEMxServiceImpl implements TEMBILL_EXECUTEMxService{

	@Autowired
	private TEMBILL_EXECUTEMxMapper tembill_executemxMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		tembill_executemxMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		tembill_executemxMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		tembill_executemxMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return tembill_executemxMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return tembill_executemxMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return tembill_executemxMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		tembill_executemxMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return tembill_executemxMapper.findCount(pd);
	}
	
	/**根据主表ID删除数据
	 * @param pd
	 * @throws Exception
	 */
	public void deleteId(PageData pd)throws Exception{
		tembill_executemxMapper.deleteId(pd);
	}
	
	/**根据主表ID查询列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listMxAll(PageData pd)throws Exception{
		return tembill_executemxMapper.listMxAll(pd);
	}
	
	/**更新反馈内容
	 * @param pd
	 * @throws Exception
	 */
	public void setFeedback(PageData pd)throws Exception{
		tembill_executemxMapper.setFeedback(pd);
	}

	/**设备点巡检明细
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> eqmPointInspectListMx(AppPage page) throws Exception {
		return tembill_executemxMapper.eqmPointInspectListMx(page);
	}
}

