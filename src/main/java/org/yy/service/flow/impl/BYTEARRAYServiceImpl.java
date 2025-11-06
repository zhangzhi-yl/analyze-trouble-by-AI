package org.yy.service.flow.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.flow.BYTEARRAYMapper;
import org.yy.service.flow.BYTEARRAYService;

/** 
 * 说明： 流程图文件接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-01
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class BYTEARRAYServiceImpl implements BYTEARRAYService{

	@Autowired
	private BYTEARRAYMapper BYTEARRAYMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		BYTEARRAYMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		BYTEARRAYMapper.delete(pd);
	}
	
	/**根据pid和 FType 删除 
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByPidAndFTYPE(PageData pd)throws Exception{
		BYTEARRAYMapper.deleteByPidAndFTYPE(pd);
	}
	
	/**修改JSON
	 * @param pd
	 * @throws Exception
	 */
	public void editJson(PageData pd)throws Exception{
		BYTEARRAYMapper.editJson(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		BYTEARRAYMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return BYTEARRAYMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return BYTEARRAYMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return BYTEARRAYMapper.findById(pd);
	}
	
	/**通过PID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPID(PageData pd)throws Exception{
		return BYTEARRAYMapper.findByPID(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		BYTEARRAYMapper.deleteAll(ArrayDATA_IDS);
	}

	/**删除流程图文件
	 * @param pd
	 */
	@Override
	public void deleteByBomId(PageData pd) throws Exception {
		BYTEARRAYMapper.deleteByBomId(pd);
	}
	
}

