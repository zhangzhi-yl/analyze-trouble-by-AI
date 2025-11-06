package org.yy.service.mom.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mom.WC_WorkCenterMapper;
import org.yy.service.mom.WC_WorkCenterService;

/** 
 * 说明： 工作中心管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-13
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class WC_WorkCenterServiceImpl implements WC_WorkCenterService{

	@Autowired
	private WC_WorkCenterMapper wc_workcenterMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		wc_workcenterMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		wc_workcenterMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		wc_workcenterMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return wc_workcenterMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return wc_workcenterMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return wc_workcenterMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		wc_workcenterMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**根据产线实体ID查询工作中心总数
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findCount(PageData pd) throws Exception {
		return wc_workcenterMapper.findCount(pd);
	}
	
	/**工作中心下拉列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> workCenterList(PageData pd)throws Exception{
		return wc_workcenterMapper.workCenterList(pd);
	}
	
	/**删除图片
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delTp(PageData pd) throws Exception {
		wc_workcenterMapper.delTp(pd);
	}

	/** 通过FCODE获取数据
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> findByFCODE(Page page) throws Exception {
		return wc_workcenterMapper.findByFCODE(page);
	}
	
	/**查询编号数据数量
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCountByCode(PageData pd)throws Exception{
		return wc_workcenterMapper.findCountByCode(pd);
	}
}

