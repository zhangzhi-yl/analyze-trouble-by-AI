package org.yy.service.zm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.SceneMapper;
import org.yy.service.zm.SceneService;

/** 
 * 说明： 场景接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class SceneServiceImpl implements SceneService{

	@Autowired
	private SceneMapper sceneMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		sceneMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		sceneMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		sceneMapper.edit(pd);
	}

	/**修改开关状态
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void editStatus(PageData pd) throws Exception {
		sceneMapper.editStatus(pd);
	}

	/**修改定时开关
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void editDateStatus(PageData pd) throws Exception {
		sceneMapper.editDateStatus(pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return sceneMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return sceneMapper.listAll(pd);
	}

	/**已启用列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listOnAll(PageData pd) throws Exception {
		return sceneMapper.listOnAll(pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return sceneMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		sceneMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

