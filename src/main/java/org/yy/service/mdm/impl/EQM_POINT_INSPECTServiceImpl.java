package org.yy.service.mdm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mdm.EQM_POINT_INSPECTMapper;
import org.yy.service.mdm.EQM_POINT_INSPECTService;

/** 
 * 说明： 设备点巡检接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-02-19
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class EQM_POINT_INSPECTServiceImpl implements EQM_POINT_INSPECTService{

	@Autowired
	private EQM_POINT_INSPECTMapper eqm_point_inspectMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		eqm_point_inspectMapper.save(pd);
	}
	public void editStatus(PageData pd)throws Exception{
		eqm_point_inspectMapper.editStatus(pd);
	}
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		eqm_point_inspectMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		eqm_point_inspectMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return eqm_point_inspectMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return eqm_point_inspectMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return eqm_point_inspectMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		eqm_point_inspectMapper.deleteAll(ArrayDATA_IDS);
	}
	
	/**更新点检人
	 * @param pd
	 * @throws Exception
	 */
	public void editOperator(PageData pd)throws Exception{
		eqm_point_inspectMapper.editOperator(pd);
	}
	
	/**更新确认人
	 * @param pd
	 * @throws Exception
	 */
	public void editIdentfied(PageData pd)throws Exception{
		eqm_point_inspectMapper.editIdentfied(pd);
	}
	
	/**手机设备点巡检列表
	 * @param page
	 * @return
	 */
	@Override
	public List<PageData> appEqmPointInspectList(AppPage page) throws Exception {
		return eqm_point_inspectMapper.EqmPointInspectList(page);
	}
	/**手机端完成
	 * @param pd
	 */
	@Override
	public void editStatusx(PageData pd) throws Exception {
		eqm_point_inspectMapper.editStatusx(pd);
	}
	
}

