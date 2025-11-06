package org.yy.service.system;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.system.User;

/**
 * 说明：用户服务接口
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public interface UsersService {
	
	/**通过用户名获取用户信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd)throws Exception;
	
	/**通过用户ID获取用户信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**用户列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> userlistPage(Page page)throws Exception;
	
	/**通过用户ID获取用户信息和角色信息
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public User getUserAndRoleById(String USER_ID) throws Exception;
	
	/**保存用户IP
	 * @param pd
	 * @throws Exception
	 */
	public void saveIP(PageData pd)throws Exception;
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByEmail(PageData pd)throws Exception;
	
	/**通过编码获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByNumbe(PageData pd) throws Exception;
	/**
	 * 通过手机端用户名获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByAppUserName(PageData pd) throws Exception;
	/**列出某角色下的所有用户
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllUserByRoldId(PageData pd) throws Exception;
	
	/**用户列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllUser(PageData pd)throws Exception;

	/**
	 * 全部用户
	 * @return
	 */
	public List<PageData> listAll()throws Exception;
	
	/**用户列表(弹窗选择用)
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listUsersBystaff(Page page)throws Exception;
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveUser(PageData pd)throws Exception;
	public void saveAppUser(PageData pd)throws Exception;
	/**保存用户系统皮肤
	 * @param pd
	 * @throws Exception
	 */
	public void saveSkin(PageData pd)throws Exception;
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editUser(PageData pd)throws Exception;
	public void uniAppEditUser(PageData pd)throws Exception;
	public void editAppUser(PageData pd)throws Exception;

	/**
	 * 更新TokenTime
	 * @param pd
	 * @throws Exception
	 */
	public void editAppUserTokenTime(PageData pd)throws Exception;
	public void editAppUserToken(PageData pd)throws Exception;

	/**
	 * 通过token查询用户
	 * @param pd
	 * @return
	 */
	List<PageData> findAppUserTimeByToken(PageData pd);
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteUser(PageData pd)throws Exception;
	public void deleteAppUser(PageData pd)throws Exception;
	/**批量删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteAllUser(String[] USER_IDS)throws Exception;
	
	/**切换工作站
	 * @param pd
	 * @throws Exception
	 */
	public void editStation(PageData pd)throws Exception;
	
	/**通过用户名查询当前工作站
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByStation(PageData pd)throws Exception;

	/**根据姓名查用户表
	 * @param pd
	 * @return
	 */
	public PageData findUser(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public List<PageData> listAllByRoleName(PageData pd)throws Exception;
}
