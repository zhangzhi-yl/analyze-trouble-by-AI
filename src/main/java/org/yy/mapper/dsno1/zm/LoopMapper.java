package org.yy.mapper.dsno1.zm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 回路管理Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-11
 * 官网：356703572@qq.com
 * @version
 */
public interface LoopMapper{

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

	/**修改开关状态
	 * @param pd
	 * @throws Exception
	 */
	void editStatus(PageData pd);

	/**修改开关状态(按类型)
	 * @param pd
	 * @throws Exception
	 */
	void editStatusByType(PageData pd);

	/**批量修改开关状态
	 * @param list
	 * @throws Exception
	 */
	void editStatusAll(List<PageData> list);

	/**修改定时启用状态
	 * @param pd
	 * @throws Exception
	 */
	void editTimeStatus(PageData pd);

	/**修改全部开启状态
	 * @param pd
	 * @throws Exception
	 */
	void editAllStatus(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);

	/**根据逗号分隔ID查询数据分页列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> loopdatalistPage(Page page);

	/**根据逗号分隔ID查询数据全部列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> loopAllByIDS(PageData pd);

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);

	/**根据逗号分隔ID查询数据全部已启用定时列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> loopOnAllByIDS(PageData pd);
	
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

    List<PageData> getLoopList(PageData pd);
}

