package org.yy.service.project.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.project.manager.FEEDBACKMapper;
import org.yy.service.project.manager.FEEDBACKService;

/** 
 * 说明： 柜体信息反馈接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-07-09
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class FEEDBACKServiceImpl implements FEEDBACKService{

	@Autowired
	private FEEDBACKMapper feedbackMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		feedbackMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		feedbackMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		feedbackMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return feedbackMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return feedbackMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return feedbackMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		feedbackMapper.deleteAll(ArrayDATA_IDS);
	}

	/** 获取描述信息
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getMsg(PageData pd) throws Exception {
		return feedbackMapper.getMsg(pd);
	}
	
}

