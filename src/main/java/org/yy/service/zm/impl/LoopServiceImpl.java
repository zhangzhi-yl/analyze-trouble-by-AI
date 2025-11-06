package org.yy.service.zm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.LoopMapper;
import org.yy.service.zm.LoopService;

/** 
 * 说明： 回路管理接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-11
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class LoopServiceImpl implements LoopService{

	@Autowired
	private LoopMapper loopMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd)throws Exception{
		loopMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delete(PageData pd)throws Exception{
		loopMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void edit(PageData pd)throws Exception{
		loopMapper.edit(pd);
	}

	/**修改开关状态
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void editStatus(PageData pd) throws Exception {
		loopMapper.editStatus(pd);
	}

	@Override
	public void editStatusByType(PageData pd) throws Exception {
		loopMapper.editStatusByType(pd);
	}

	/**批量修改开关状态
	 * @param list
	 * @throws Exception
	 */
	@Override
	public void editStatusAll(List<PageData> list) throws Exception {
		loopMapper.editStatusAll(list);
	}

	/**修改定时启用状态
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void editTimeStatus(PageData pd) throws Exception {
		loopMapper.editTimeStatus(pd);
	}

	/**修改全部开启状态
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void editAllStatus(PageData pd) throws Exception {
		loopMapper.editAllStatus(pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page)throws Exception{
		return loopMapper.datalistPage(page);
	}

	/**根据IDS查询列表(全部)
	 * @param
	 * @throws Exception
	 */
	@Override
	public List<PageData> loopList(Page page) throws Exception {
		return loopMapper.loopdatalistPage(page);
	}

	/**根据IDS查询列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> loopAllByIDS(PageData pd) throws Exception {
		return loopMapper.loopAllByIDS(pd);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listAll(PageData pd)throws Exception{
		return loopMapper.listAll(pd);
	}

	/**根据逗号分隔ID查询数据全部已启用定时列表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> loopOnAllByIDS(PageData pd) throws Exception {
		return loopMapper.loopOnAllByIDS(pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findById(PageData pd)throws Exception{
		return loopMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		loopMapper.deleteAll(ArrayDATA_IDS);
	}

	@Override
	public List<PageData> loopCount(PageData pd) throws Exception {
		return null;
	}

	@Override
	public List<PageData> getLoopList(PageData pd) {
		return loopMapper.getLoopList(pd);
	}

}

