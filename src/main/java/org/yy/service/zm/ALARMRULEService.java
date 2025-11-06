package org.yy.service.zm;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/**
 * 说明： 报警规则接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-18
 * 官网：356703572@qq.com
 * @version
 */
public interface ALARMRULEService {

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

	/**
	 * 使用列表
	 * @param pd
	 * @return
	 */
	public List<PageData> uselist(PageData pd)throws Exception;

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
	 * 获取回路报警值
	 * @param pd
	 * @return
	 */
	public List<PageData> getLoopAlarm(PageData pd)throws Exception;

}

