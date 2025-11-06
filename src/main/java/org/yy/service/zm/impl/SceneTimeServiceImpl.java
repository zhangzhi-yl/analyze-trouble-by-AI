package org.yy.service.zm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.SceneTimeMapper;
import org.yy.service.zm.SceneTimeService;

/** 
 * 说明： 场景日期接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SceneTimeServiceImpl implements SceneTimeService{

	@Autowired
	private SceneTimeMapper scenetimeMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		scenetimeMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		scenetimeMapper.delete(pd);
	}

	/**级联删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void deleteByScene(PageData pd) throws Exception {
		scenetimeMapper.deleteByScene(pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		scenetimeMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return scenetimeMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return scenetimeMapper.listAll(pd);
	}

	/**根据场景ID及当前系统日期查询设置的日期列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAllByDate(PageData pd) throws Exception {
		return scenetimeMapper.listAllByDate(pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return scenetimeMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		scenetimeMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

