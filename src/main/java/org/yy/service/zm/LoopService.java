package org.yy.service.zm;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 回路管理接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-11
 * 官网：356703572@qq.com
 * @version
 */
public interface LoopService{

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

	/**修改开关状态
	 * @param pd
	 * @throws Exception
	 */
	void editStatus(PageData pd)throws Exception;

	/**修改开关状态(按类型)
	 * @param pd
	 * @throws Exception
	 */
	void editStatusByType(PageData pd)throws Exception;

	/**批量修改开关状态
	 * @param list
	 * @throws Exception
	 */
	void editStatusAll(List<PageData> list)throws Exception;

	/**修改定时启用状态
	 * @param pd
	 * @throws Exception
	 */
	void editTimeStatus(PageData pd)throws Exception;

	/**修改全部开启状态
	 * @param pd
	 * @throws Exception
	 */
	void editAllStatus(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;

	/**根据逗号分隔ID查询数据分页列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> loopList(Page page)throws Exception;

	/**根据IDS查询列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> loopAllByIDS(PageData pd)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;

	/**根据逗号分隔ID查询数据全部已启用定时列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> loopOnAllByIDS(PageData pd)throws Exception;
	
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


	/**获取回路数量
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> loopCount(PageData pd)throws Exception;

	public List<PageData> getLoopList(PageData pd);
}

