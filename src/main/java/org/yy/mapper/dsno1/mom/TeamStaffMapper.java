package org.yy.mapper.dsno1.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 班组人员管理Mapper
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 * @version
 */
public interface TeamStaffMapper{

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
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	/**
	 * 批量绑定组员方法，入参为组员list
	 * @param staffArray
	 * @throws Exception
	 */
	void staffBinding(List<PageData> staffArray);
	
	/**通过人员查询班组信息
	 * @param pd
	 * @throws Exception
	 */
	PageData findByTeam(PageData pd);
	
	/**通过人员查询班组
	 * @param pd
	 * @throws Exception
	 */
	PageData getTeam(PageData pd);
	
	/**查询人员班组列表
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAllStaff(PageData pd);
}

