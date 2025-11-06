package org.yy.service.mom;

import java.util.List;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 班组人员管理接口
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 * @version
 */
public interface TeamStaffService{

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
	 * 根据车间ID查询班组人员数量
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;
	
	/**
	 * 批量绑定组员方法，入参为组员list
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void staffBinding(List<PageData> staffArray)throws Exception;
	
	/**通过人员查询班组信息
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByTeam(PageData pd)throws Exception;
	
	/**通过人员查询班组
	 * @param pd
	 * @throws Exception
	 */
	public PageData getTeam(PageData pd)throws Exception;
	

	/**查询人员班组列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllStaff(PageData pd)throws Exception;
}

