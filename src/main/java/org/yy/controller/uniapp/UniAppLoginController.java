package org.yy.controller.uniapp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.entity.UniAppUser;
import org.yy.entity.system.User;
import org.yy.service.system.FHlogService;
import org.yy.service.system.PhotoService;
import org.yy.service.system.UsersService;
import org.yy.util.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * uniApp登录接口
 */
@Controller
@RequestMapping("/api/uniApp")
public class UniAppLoginController extends BaseController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private FHlogService FHLOG;

    @Autowired
    private PhotoService photoService;

    /**
     * app登录
     *
     * @throws Exception
     */
    @RequestMapping(value = "/mLogin")
    @ResponseBody
    public Object mLogin(@RequestBody UniAppUser appUser) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        String errInfo = "success";

        String username = appUser.getUsername();
        String password = appUser.getPassword();

        //1. 校验用户是否有效
        PageData getUser = new PageData();
        getUser.put("USERNAME", username);
        PageData sysUser = usersService.findByUsername(getUser);
        if (null == sysUser) {
            errInfo = "用户名不存在";
        }
        if (!"success".equals(errInfo)) {
            map.put("result", errInfo);
            return map;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = new SimpleHash("SHA-1", username, password).toString();
        String syspassword = sysUser.getString("PASSWORD");
        if (!syspassword.equals(userpassword)) {
            errInfo = "用户名或密码错误";
            map.put("result", errInfo);
            return map;
        }

        // 生成token
        //String token = JwtUtil.sign(username, syspassword);
        UsernamePasswordToken token = new UsernamePasswordToken(username, syspassword);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token); // 这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中
        } catch (UnknownAccountException uae) {
            errInfo = "用户名或密码错误";
        } catch (IncorrectCredentialsException ice) {
            errInfo = "用户名或密码错误";
        } catch (LockedAccountException lae) {
            errInfo = "用户名或密码错误";
        } catch (ExcessiveAttemptsException eae) {
            errInfo = "usererror4";
        } catch (DisabledAccountException sae) {
            errInfo = "用户名或密码错误";
        } catch (AuthenticationException ae) {
            errInfo = "用户名或密码错误";
        }
        if (subject.isAuthenticated()) { // 验证是否登录成功
            pd.put("USERNAME", username);
            pd = usersService.findByUsername(pd);
            String UserName = pd.getString("USERNAME");
            String Role = pd.getString("Role");
            String Premission = pd.getString("Premission");
            String Token = Tools.generateToken();
            String TokenTime = Tools.date2Str(new Date());
            PageData pdPhoto;
            pdPhoto = photoService.findById(pd);
            sysUser.put("userPhoto", null == pdPhoto ? "assets/images/user/avatar-2.jpg" : pdPhoto.getString("PHOTO2"));//用户头像
            pd.put("UserName", UserName);
            pd.put("Role", Role);
            pd.put("Premission", Premission);
            pd.put("Token", Token);
            pd.put("TokenTime", TokenTime);
            usersService.editAppUser(pd);
            FHLOG.save(username, "成功登录系统"); // 记录日志
        } else {
            token.clear();
            errInfo = "用户名或密码错误";
        }
        if (!"success".equals(errInfo)) {
            FHLOG.save(username, "尝试登录系统失败,用户名密码错误,无权限");
        }
        PageData obj = new PageData();
        obj.put("userInfo", sysUser);
        obj.put("token", pd.getString("Token"));
        map.put("result", obj);
        map.put("success", true);
        map.put("code", 200);

        return map;
    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value="/queryById")
    @ResponseBody
    public Object queryById() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("USER_ID",pd.getString("id"));
        PageData user = usersService.findById(pd);

        map.put("success",true);
        map.put("code",200);
        map.put("result", user);
        return map;
    }

    /**
     * 用户注销
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logout", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object logout(@RequestBody PageData pd) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";

        //清除Token
        PageData clear = new PageData();
        clear.put("UserName",pd.getString("USERNAME"));
        usersService.editAppUserToken(clear);

        //shiro销毁登录
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        map.put("result", errInfo);
        return map;
    }

    /**
     * 用户修改
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uniAppEdit")
    @ResponseBody
    public Object edit(@RequestBody PageData pd) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";

        usersService.uniAppEditUser(pd);

        map.put("result", errInfo);
        return map;
    }

    /**
     * 用户修改头像上传
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uniAppUpload")
    @ResponseBody
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String USERNAME = pd.getString("USERNAME");
        PageData userPhoto = photoService.findById(pd);

        String FPFFILEPATH = "";

        if(!"".equals(USERNAME) && null != USERNAME){

            //文件上传
            if (null != file) {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar calendar = Calendar.getInstance();
                String dateName = df.format(calendar.getTime());
                String ffile = DateUtil.getDays(), fileName = "";
                // 文件上传路径
                String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;
                // 文件上传路径
                String fileNamereal = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf("."));
                // 执行上传
                fileName = FileUpload.fileUp(file, filePath, fileNamereal + dateName);
                FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + fileName;
            }
        }

        //头像信息判断
        if(null == userPhoto){
            userPhoto = new PageData();
            //附件路径
            userPhoto.put("USERPHOTO_ID",this.get32UUID());
            userPhoto.put("USERNAME",USERNAME);
            userPhoto.put("PHOTO0", FPFFILEPATH);
            userPhoto.put("PHOTO1", FPFFILEPATH);
            userPhoto.put("PHOTO2", FPFFILEPATH);
            userPhoto.put("PHOTO3", FPFFILEPATH);
            //修改头像
            photoService.save(userPhoto);
        }else {
            userPhoto.put("USERNAME",USERNAME);
            userPhoto.put("PHOTO0", FPFFILEPATH);
            userPhoto.put("PHOTO1", FPFFILEPATH);
            userPhoto.put("PHOTO2", FPFFILEPATH);
            userPhoto.put("PHOTO3", FPFFILEPATH);
            //新增头像
            photoService.edit(userPhoto);
        }

        map.put("path",FPFFILEPATH);
        map.put("result", errInfo);
        return map;
    }
}
