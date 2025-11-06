package org.yy.service.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 售前方案计划二级明细接口
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 * @version
 */
public interface PRESALEPLANTWOService{

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
	public void deleteAll(PageData pd)throws Exception;
	
	/**读取出错删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteFBTwo(PageData pd)throws Exception;
	
	/**获取材料费、技术费、主要成本
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listProchance(PageData pd)throws Exception;
	
	/**获取各个工时费用
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findByJHNew(PageData pd)throws Exception;
	
	/**依据一级明细ID获取二级明细工时数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getHourByID(PageData pd)throws Exception;
}

