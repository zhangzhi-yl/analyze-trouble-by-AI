package org.yy.service.mm.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.mm.MAKECODEMapper;
import org.yy.service.mm.MAKECODEService;

/** 
 * 说明： 制码接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2020-12-08
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class MAKECODEServiceImpl implements MAKECODEService{

	@Autowired
	private MAKECODEMapper MAKECODEMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		MAKECODEMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		MAKECODEMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		MAKECODEMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return MAKECODEMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return MAKECODEMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return MAKECODEMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		MAKECODEMapper.deleteAll(ArrayDATA_IDS);
	}

	/**根据物料类型获取当前码
	 * @param pd
	 * @return
	 */
	@Override
	public PageData getCode(PageData pd) throws Exception {
		return MAKECODEMapper.getCode(pd);
	}

	/**根据物料类型更新当前码
	 * @param pd
	 */
	@Override
	public void editCode(PageData pd) throws Exception {
		MAKECODEMapper.editCode(pd);
	}
	
}

