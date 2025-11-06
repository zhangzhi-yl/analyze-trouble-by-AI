package org.yy.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

/**
 * 说明：权限工具类
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public class Jurisdiction {
	
	/**shiro管理的session
	 * @return
	 */
	public static Session getSession(){
		return SecurityUtils.getSubject().getSession();
	}

	/**获取当前登录的StationNUM
	 * @return
	 */
	public static String getSTCode(){
		try {
			String STNUM=getSession().getAttribute(Const.SESSION_STCODE).toString();
			return STNUM.equals("")?"NO":STNUM;
		} catch (Exception e) {
			return "NO";
		}
	}
	/**获取当前登录的StationName
	 * @return
	 */
	public static String getSTName(){
		try {
			String STNAME=getSession().getAttribute(Const.SESSION_STNAME).toString();
			return STNAME.equals("")?"NO":STNAME;
		} catch (Exception e) {
			return "NO";
		}
	}
	
	/**获取当前登录的用户名
	 * @return
	 */
	public static String getUsername(){
		return getSession().getAttribute(Const.SESSION_USERNAME).toString();
	}
	
	/**获取当前登录的姓名
	 * @return
	 */
	public static String getName(){
		return getSession().getAttribute(Const.SESSION_U_NAME).toString();
	}
	
	/**获取当前登录用户的角色编码
	 * @return
	 */
	public static String getRnumbers(){
		return getSession().getAttribute(Const.SESSION_RNUMBERS).toString();
	}
	
	/**获取用户的最高组织机构权限集合
	 * @return
	 */
	public static String getDEPARTMENT_IDS(){
		return getSession().getAttribute(Const.DEPARTMENT_IDS).toString();
	}
	
	/**获取用户的最高组织机构权限
	 * @return
	 */
	public static String getDEPARTMENT_ID(){
		return getSession().getAttribute(Const.DEPARTMENT_ID).toString();
	}

	/**获取当前登录的ID
	 * @return
	 */
	public static String getUserId(){
		return getSession().getAttribute(Const.SESSION_USERID).toString();
	}
	
}
