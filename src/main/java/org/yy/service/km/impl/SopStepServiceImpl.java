package org.yy.service.km.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.km.SopStepMapper;
import org.yy.service.km.SopStepService;

/** 
 * 说明： SOP_步骤接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-01-18
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SopStepServiceImpl implements SopStepService{

	@Autowired
	private SopStepMapper SopStepMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		SopStepMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		SopStepMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		SopStepMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return SopStepMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return SopStepMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return SopStepMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		SopStepMapper.deleteAll(ArrayDATA_IDS);
	}

	/**获取选中条目下一个步骤
	 * @param pdy
	 * @return
	 */
	@Override
	public PageData getDown(PageData pdy) throws Exception {
		return SopStepMapper.getDown(pdy);
	}

	/**反写排序时间
	 * @param pd
	 * @return
	 */
	@Override
	public void editSort(PageData pdy) throws Exception {
		SopStepMapper.editSort(pdy);
	}

	/**获取选中条目上一个步骤
	 * @param pdy
	 * @return
	 */
	@Override
	public PageData getUP(PageData pdy) throws Exception {
		return SopStepMapper.getUP(pdy);
	}

	/**获取方案起始步骤
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getFIsFirst(PageData pd) throws Exception {
		return SopStepMapper.getFIsFirst(pd);
	}


	/**单号验重
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getRepeatNum(PageData pd) throws Exception {
		return SopStepMapper.getRepeatNum(pd);
	}
	
	/**删除附件
	 * @param pd
	 * @throws Exception
	 */
	public void delFj(PageData pd)throws Exception{
		SopStepMapper.delFj(pd);
	}
}

