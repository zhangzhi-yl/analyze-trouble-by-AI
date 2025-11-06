package org.yy.mapper.dsno1.zm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/**
 * 说明： 报警Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-13
 * 官网：356703572@qq.com
 * @version
 */
public interface ALARMMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);

	/**批量新增
	 * @param list
	 * @throws Exception
	 */
	void saveAll(List<PageData> list);

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

	/**报警处理
	 * @param pd
	 * @throws Exception
	 */
	void handle(PageData pd);

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

	/**未处理列表(全部)
	 * @param
	 * @throws Exception
	 */
	List<PageData> listAllNoHandle();

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
	 * 每月报警数量
	 * @return
	 */
	List<PageData> getAlarmMonth();

}

