package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.SopStepDatabaseMapper;
import org.yy.service.km.SopStepDatabaseService;

/** 
 * 说明： SOP步骤库接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-18
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SopStepDatabaseServiceImpl implements SopStepDatabaseService{

	@Autowired
	private SopStepDatabaseMapper SopStepDatabaseMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		SopStepDatabaseMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		SopStepDatabaseMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		SopStepDatabaseMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return SopStepDatabaseMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return SopStepDatabaseMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return SopStepDatabaseMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		SopStepDatabaseMapper.deleteAll(ArrayDATA_IDS);
	}

	/**根据ID获取SOP步骤库列表
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllByIds(PageData pd) throws Exception {
		return SopStepDatabaseMapper.listAllByIds(pd);
	}

	/**禁用/启用
	 * @param pd
	 */
	@Override
	public void editState(PageData pd) throws Exception {
		SopStepDatabaseMapper.editState(pd);
	}

	/**单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return SopStepDatabaseMapper.getRepeatNum(pd);
	}
	
	/**删除附件
	 * @param pd
	 * @throws Exception
	 */
	public void delFj(PageData pd)throws Exception{
		SopStepDatabaseMapper.delFj(pd);
	}
}

