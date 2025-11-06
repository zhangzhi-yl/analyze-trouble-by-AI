package org.yy.service.zm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.LoopTimeMapper;
import org.yy.service.zm.LoopTimeService;

/** 
 * 说明： 回路时间接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class LoopTimeServiceImpl implements LoopTimeService{

	@Autowired
	private LoopTimeMapper looptimeMapper;


	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		looptimeMapper.save(pd);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		looptimeMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		looptimeMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return looptimeMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return looptimeMapper.listAll(pd);
	}

	/**按当前系统时间及回路ID查询该回路下的时间列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAllByTime(PageData pd) throws Exception {
		return looptimeMapper.listAllByTime(pd);
	}

	/**按当前系统时间及回路ID查询该回路下的时间列表(开启)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAllByTimeStart(PageData pd) throws Exception {
		return looptimeMapper.listAllByTimeStart(pd);
	}

	/**按当前系统时间及回路ID查询该回路下的时间列表(关闭)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAllByTimeEnd(PageData pd) throws Exception {
		return looptimeMapper.listAllByTimeEnd(pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return looptimeMapper.findById(pd);
	}

	/**级联删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void deleteByLoop(PageData pd) throws Exception {
		looptimeMapper.deleteByLoop(pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		looptimeMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

