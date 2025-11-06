package org.yy.mapper.dsno1.project.manager;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 售前方案计划二级明细Mapper
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 * @version
 */
public interface PRESALEPLANTWOMapper{

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
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(PageData pd);
	
	/**读取出错删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteFBTwo(PageData pd);
	
	/**获取材料费、技术费、主要成本
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listProchance(PageData pd);
	
	/**获取各个工时费用
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> findByJHNew(PageData pd);
	
	/**依据一级明细ID获取二级明细工时数据
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> getHourByID(PageData pd);
}

