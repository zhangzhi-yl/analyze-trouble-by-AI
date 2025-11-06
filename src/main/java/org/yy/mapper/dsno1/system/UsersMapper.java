package org.yy.mapper.dsno1.system;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.system.User;

/**
 * 说明：用户Mapper
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public interface UsersMapper {
	
	/**通过用户获取数据
	 * @param pd
	 * @return
	 */
	PageData findByUsername(PageData pd);
	
	/**用户列表
	 * @param page
	 * @return
	 */
	List<PageData> userlistPage(Page page);
	
	/**通过用户ID获取用户信息和角色信息
	 * @param USER_ID
	 * @return
	 */
	User getUserAndRoleById(String USER_ID);
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData findByEmail(PageData pd);
	
	/**通过编码获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData findByNumbe(PageData pd);
	/**
	 * 通过用户名获取数据
	 */
	PageData findByAppUserName(PageData pd);
	/**列出某角色下的所有用户
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> listAllUserByRoldId(PageData pd);
	
	/**用户列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> listAllUser(PageData pd);

	/**
	 * 全部用户
	 * @return
	 */
	List<PageData> listAll();
	
	/**用户列表(弹窗选择用)
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<PageData> userBystafflistPage(Page page);
	
	/**通过用户ID获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**保存用户IP
	 * @param pd
	 */
	void saveIP(PageData pd);
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	void saveUser(PageData pd);
	void saveAppUser(PageData pd);
	/**保存用户系统皮肤
	 * @param pd
	 * @throws Exception
	 */
	void saveSkin(PageData pd);
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	void editUser(PageData pd);
	void uniAppEditUser(PageData pd);
	void editAppUser(PageData pd);
	void editAppUserTokenTime(PageData pd);
	void editAppUserToken(PageData pd);

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
	void deleteUser(PageData pd);
	void deleteAppUser(PageData pd);
	/**批量删除用户
	 * @param pd
	 * @throws Exception
	 */
	void deleteAllUser(String[] USER_IDS);
	/**切换工作站
	 * @param pd
	 * @throws Exception
	 */
	void editStation(PageData pd);
	
	/**通过用户名查询当前工作站
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData findByStation(PageData pd);

	/**根据姓名查用户表
	 * @param pd
	 * @return
	 */
	PageData findUser(PageData pd);

	/**
	 * @param pd
	 * @return
	 */
	List<PageData> listAllByRoleName(PageData pd);

}
