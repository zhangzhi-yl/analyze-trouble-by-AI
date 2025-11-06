package org.yy.mapper.dsno1.zm;

import org.yy.entity.Page;
import org.yy.entity.PageData;

import java.util.List;

/**
 * 说明： 报警规则Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-18
 * 官网：356703572@qq.com
 * @version
 */
public interface ALARMRULEMapper {

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);

	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);

	/**
	 * 使用列表
	 * @param pd
	 * @return
	 */
	List<PageData> uselist(PageData pd);

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

	/**
	 * 获取回路报警值
	 * @param pd
	 * @return
	 */
	List<PageData> getLoopAlarm(PageData pd);

}

