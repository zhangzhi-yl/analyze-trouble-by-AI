package org.yy.controller.app;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.system.FHlogService;
import org.yy.service.system.PhotoService;
import org.yy.service.system.UsersService;
import org.yy.util.Const;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;

/**
 * 手机端登录接口
 * 
 * @author YULONG
 *
 */
@Controller
@RequestMapping("/appLogin")
public class AppLoginController extends BaseController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private FHlogService FHLOG;
	@Autowired
    private PhotoService photoService;
	@Autowired
	private StaffService staffService;

	/**
	 * 请求登录验证用户接口
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/check")
	@ResponseBody
	public Object check() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		PageData pdResult = new PageData();
		pd = this.getPageData();
		String errInfo = "success";
		// String KEYDATA[] =
		// pd.getString("KEYDATA").replaceAll("qQ356703572fh",
		// "").split(",fh,");
		if (pd != null) {
			String USERNAME = pd.getString("USERNAME");
			String PASSWORD = pd.getString("PASSWORD");
			UsernamePasswordToken token = new UsernamePasswordToken(USERNAME,
					new SimpleHash("SHA-1", USERNAME, PASSWORD).toString());
			Subject subject = SecurityUtils.getSubject();
			try {
				subject.login(token); // 这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中
			} catch (UnknownAccountException uae) {
				errInfo = "usererror";
			} catch (IncorrectCredentialsException ice) {
				errInfo = "usererror";
			} catch (LockedAccountException lae) {
				errInfo = "usererror";
			} catch (ExcessiveAttemptsException eae) {
				errInfo = "usererror4";
			} catch (DisabledAccountException sae) {
				errInfo = "usererror";
			} catch (AuthenticationException ae) {
				errInfo = "usererror";
			}
			if (subject.isAuthenticated()) { // 验证是否登录成功
				pd.put("USERNAME", USERNAME);
				pd = usersService.findByUsername(pd);
				String UserName = pd.getString("USERNAME");
				String Role = pd.getString("Role");
				String Premission = pd.getString("Premission");
				String Token = Tools.generateToken();
				String TokenTime = Tools.date2Str(new Date());
				PageData pdPhoto;
				pdPhoto = photoService.findById(pd);
				pdResult.put("userPhoto", null == pdPhoto?"assets/images/user/avatar-2.jpg":pdPhoto.getString("PHOTO2"));//用户头像
				pd.put("UserName", UserName);
				pd.put("Role", Role);
				pd.put("Premission", Premission);
				pd.put("Token", Token);
				pd.put("TokenTime", TokenTime);
				pdResult.put("Role", Role);
				pdResult.put("Token", Token);
				pdResult.put("NAME", pd.getString("NAME"));
				usersService.editAppUser(pd);
				FHLOG.save(USERNAME, "成功登录系统"); // 记录日志
			} else {
				token.clear();
				errInfo = "usererror";
			}
			if (!"success".equals(errInfo))
				FHLOG.save(USERNAME, "尝试登录系统失败,用户名密码错误,无权限");
		} else {
			errInfo = "error"; // 缺少参数
		}
		return AppResult.success(pdResult, "获取成功", errInfo);
	}

	@RequestMapping(value = "/appGetToken")
	@ResponseBody
	public Object appGetToken() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success";
		String Token = "";
		// String KEYDATA[] =
		// pd.getString("KEYDATA").replaceAll("qQ356703572fh",
		// "").split(",fh,");
		if (pd != null) {
			String USERNAME = pd.getString("USERNAME");
			String PASSWORD = pd.getString("PASSWORD");
			UsernamePasswordToken token = new UsernamePasswordToken(USERNAME,
					new SimpleHash("SHA-1", USERNAME, PASSWORD).toString());
			Subject subject = SecurityUtils.getSubject();
			try {
				subject.login(token); // 这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中
			} catch (UnknownAccountException uae) {
				errInfo = "usererror";
			} catch (IncorrectCredentialsException ice) {
				errInfo = "usererror";
			} catch (LockedAccountException lae) {
				errInfo = "usererror";
			} catch (ExcessiveAttemptsException eae) {
				errInfo = "usererror4";
			} catch (DisabledAccountException sae) {
				errInfo = "usererror";
			} catch (AuthenticationException ae) {
				errInfo = "usererror";
			}
			if (subject.isAuthenticated()) { // 验证是否登录成功
				pd.put("USERNAME", USERNAME);
				pd = usersService.findByUsername(pd);
				String UserName = pd.getString("USERNAME");
				String Role = pd.getString("Role");
				String Premission = pd.getString("Premission");
				Token = Tools.generateToken();
				String TokenTime = Tools.date2Str(new Date());
				pd.put("UserName", UserName);
				pd.put("Role", Role);
				pd.put("Premission", Premission);
				pd.put("Token", Token);
				pd.put("TokenTime", TokenTime);
				usersService.editAppUser(pd);
				FHLOG.save(USERNAME, "获取token"); // 记录日志
			} else {
				token.clear();
				errInfo = "usererror";
			}
			if (!"success".equals(errInfo))
				FHLOG.save(USERNAME, "尝试获取token失败,用户名密码错误,无权限");
		} else {
			errInfo = "error"; // 缺少参数
		}
		map.put("Token", Token);
		return map;
	}

	/**
	 * 判断Token是否有效 true 有效 false 无效
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isTokenValid(String USERNAME) throws Exception {
		boolean isTokenValid = false;
		PageData pd = new PageData();
		pd.put("UserName", USERNAME);
		UsersService usersServiceApp = (UsersService) SpringUtil.getBean("usersServiceImpl");
		PageData pdApp = usersServiceApp.findByAppUserName(pd);
		if (pdApp.getString("Token") != null) {
			String TokenTime = pdApp.getString("TokenTime");
			String NowDate = Tools.date2Str(new Date());
			if (Tools.timeSubtraction(TokenTime, NowDate) < Const.TOKEN_PEROID) {
				isTokenValid = true;
			}
		} else {
			isTokenValid = false;
		}
		return isTokenValid;
	}
	
	 	/**个人信息
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/findUserInformation")
		@ResponseBody
		public Object findUserInformation(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				pd.put("USERNAME", pd.getString("UserName"));
				pd = staffService.findById(pd);	//根据ID读取
				if(pd!=null){
					
				}else{
					return AppResult.success("当前人员不存在！", "当前人员不存在！");
				}
				return AppResult.success(pd, "获取成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}	
}
