package org.yy.service.fhoa;


import java.util.List;

import org.yy.controller.app.AppPage;
import org.yy.entity.Page;
import org.yy.entity.PageData;

/** 
 * 说明： 员工管理接口
 * 创建人：FH Q356703572
 * 官网：356703572@qq.com
 */
public interface StaffService{

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
	public List<PageData> AppList(AppPage page)throws Exception;
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
	
	/**绑定用户
	 * @param pd
	 * @throws Exception
	 */
	public void userBinding(PageData pd)throws Exception;
	
	/**通过人员获取部门
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDEPTNAME(PageData pd)throws Exception;

	/**根据登录名查询职员ID
	 * @author 管悦
	 * @date 2020-11-06
	 * @param FNAME
	 * @throws Exception
	 */
	public PageData getStaffId(PageData pd)throws Exception;

	/**获取职员列表-可搜索-前100条
	 * @param pd
	 * @return
	 */
	public List<PageData> getStaffList(PageData pd);
	/**获取人员是否存在
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUser(PageData pd)throws Exception;
	/**根据部门id获取部门所有人
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllByDept(PageData pd)throws Exception;
}

