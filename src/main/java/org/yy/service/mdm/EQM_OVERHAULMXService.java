package org.yy.service.mdm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 检修计划明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-25
 * 官网：356703572@qq.com
 * @version
 */
public interface EQM_OVERHAULMXService{

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
	
	/***通过id获取主从表全部数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByTick(PageData pd)throws Exception;
	
	/***查询当日、月、年反馈次数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCount(PageData pd)throws Exception;
	
	/**更新反馈
	 * @param pd
	 * @throws Exception
	 */
	public void editValue(PageData pd)throws Exception;
}

