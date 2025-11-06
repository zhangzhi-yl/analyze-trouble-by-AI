package org.yy.service.system.impl;

import org.yy.service.system.UsersService;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.system.User;
import org.yy.mapper.dsno1.system.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：用户服务接口实现类
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
@Service
@Transactional //开启事物
public class UsersServiceImpl implements UsersService {
	
	@Autowired
	private UsersMapper usersMapper;
	
	/**通过用户名获取用户信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd) throws Exception {
		return	usersMapper.findByUsername(pd);
	}
	
	/**通过用户ID获取用户信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return	usersMapper.findById(pd);
	}

	/**用户列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> userlistPage(Page page) throws Exception {
		return	usersMapper.userlistPage(page);
	}
	
	/**通过用户ID获取用户信息和角色信息
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public User getUserAndRoleById(String USER_ID) throws Exception {
		return	usersMapper.getUserAndRoleById(USER_ID);
	}

	/**保存用户IP
	 * @param pd
	 * @throws Exception
	 */
	public void saveIP(PageData pd) throws Exception {
		usersMapper.saveIP(pd);
	}

	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByEmail(PageData pd) throws Exception {
		return usersMapper.findByEmail(pd);
	}
	
	/**通过编码获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByNumbe(PageData pd) throws Exception {
		return usersMapper.findByNumbe(pd);
	}
	/**通过app用户名获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByAppUserName(PageData pd) throws Exception{
		return usersMapper.findByAppUserName(pd);
	}
	/**列出某角色下的所有用户
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllUserByRoldId(PageData pd) throws Exception{
		return usersMapper.listAllUserByRoldId(pd);
	}
	
	/**用户列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllUser(PageData pd)throws Exception{
		return usersMapper.listAllUser(pd);
	}

	/**
	 * 全部用户
	 * @return
	 */
	@Override
	public List<PageData> listAll() throws Exception {
		return usersMapper.listAll();
	}

	/**用户列表(弹窗选择用)
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listUsersBystaff(Page page)throws Exception{
		return usersMapper.userBystafflistPage(page);
	}
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveUser(PageData pd)throws Exception {
		usersMapper.saveUser(pd);
	}
	
	public void saveAppUser(PageData pd)throws Exception {
		usersMapper.saveAppUser(pd);
	}
	
	/**保存用户系统皮肤
	 * @param pd
	 * @throws Exception
	 */
	public void saveSkin(PageData pd)throws Exception{
		usersMapper.saveSkin(pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editUser(PageData pd)throws Exception{
		usersMapper.editUser(pd);
	}
	@Override
	public void uniAppEditUser(PageData pd)throws Exception{
		usersMapper.uniAppEditUser(pd);
	}
	public void editAppUser(PageData pd)throws Exception{
		usersMapper.editAppUser(pd);
	}

	@Override
	public void editAppUserTokenTime(PageData pd) throws Exception {
		usersMapper.editAppUserTokenTime(pd);
	}

	public void editAppUserToken(PageData pd)throws Exception{
		usersMapper.editAppUserToken(pd);
	}

	@Override
	public List<PageData> findAppUserTimeByToken(PageData pd) {
		return usersMapper.findAppUserTimeByToken(pd);
	}

	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteUser(PageData pd)throws Exception{
		usersMapper.deleteUser(pd);
	}
	
	public void deleteAppUser(PageData pd)throws Exception{
		usersMapper.deleteAppUser(pd);
	}
	
	/**批量删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteAllUser(String[] USER_IDS)throws Exception{
		usersMapper.deleteAllUser(USER_IDS);
	}
	/**切换工作站
	 * @param pd
	 * @throws Exception
	 */
	public void editStation(PageData pd)throws Exception{
		usersMapper.editStation(pd);
	}
	
	/**通过用户名查询当前工作站
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByStation(PageData pd) throws Exception {
		return	usersMapper.findByStation(pd);
	}

	/**根据姓名查用户表
	 * @param pd
	 * @return
	 */
	@Override
	public PageData findUser(PageData pd) throws Exception {
		return	usersMapper.findUser(pd);
	}

	/* (non-Javadoc)
	 * @see org.yy.service.system.UsersService#listAllByRoleName(org.yy.entity.PageData)
	 */
	@Override
	public List<PageData> listAllByRoleName(PageData pd) throws Exception {
		return	usersMapper.listAllByRoleName(pd);
	}

	
}
