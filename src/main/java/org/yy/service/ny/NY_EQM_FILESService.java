package org.yy.service.ny;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/** 
 * 说明： 设备附件接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-09
 * 官网：356703572@qq.com
 * @version
 */
public interface NY_EQM_FILESService {

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**
	 * 设备级联删除
	 * @param pd
	 */
	void deleteByEqm(PageData pd)throws Exception;
	
}

