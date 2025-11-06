package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.CallMaterialMapper;
import org.yy.service.mm.CallMaterialService;

/** 
 * 说明： 叫料申请接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-03-25
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class CallMaterialServiceImpl implements CallMaterialService{

	@Autowired
	private CallMaterialMapper CallMaterialMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		CallMaterialMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		CallMaterialMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		CallMaterialMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return CallMaterialMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return CallMaterialMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return CallMaterialMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		CallMaterialMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> listchejian(Page page) {
		return CallMaterialMapper.chejiandatalistPage(page);
	}

	@Override
	public List<PageData> listkufang(Page page) {
		return CallMaterialMapper.kufangdatalistPage(page);
	}

	@Override
	public List<PageData> listchejianApp(AppPage page) {
		return CallMaterialMapper.listchejianApp(page);
	}

	@Override
	public List<PageData> listkufangApp(AppPage page) {
		return CallMaterialMapper.listkufangApp(page);
	}

	@Override
	public List<PageData> getCallMaterialDataByMasterPlanningWorkNum(PageData pd) throws Exception {
		return CallMaterialMapper.getCallMaterialDataByMasterPlanningWorkNum(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mm.CallMaterialService#listAllMat(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllMat(PageData pd) throws Exception {
		return CallMaterialMapper.listAllMat(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.mm.CallMaterialService#listXMkufang(org.yy.entity.Page)
	 */
	@Override
	public List<PageData> listXMkufang(Page page) throws Exception {
		return CallMaterialMapper.listXMkufangdatalistPage(page);
	}

	
}

