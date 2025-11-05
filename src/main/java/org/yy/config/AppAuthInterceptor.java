package org.yy.config;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.system.UsersService;
import org.yy.util.Tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * app请求拦截校验Token
 */
@Component
public class AppAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UsersService usersService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //System.err.println(">>>AuthInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前)");

        String token = request.getHeader("X-Access-Token");

//        System.err.println("token : " + token);

        //....处理逻辑
        PageData getUser = new PageData();
        getUser.put("Token", token);
        List<PageData> appUserList = usersService.findAppUserTimeByToken(getUser);
        if (appUserList.size() != 1) {
            resetReponse(response);
            return false;
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            PageData appUser = appUserList.get(0);
            String TokenTime = appUser.getString("TokenTime");
            String time = Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");

            Date d1 = df.parse(TokenTime);
            Date d2 = df.parse(time);
            long diff = d2.getTime() - d1.getTime();
            int s = (int) (diff / 1000);

            if (s > 86400) {
                usersService.editAppUserToken(appUser);
                resetReponse(response);
                return false;
            } else {
                appUser.put("TokenTime", time);
                usersService.editAppUserTokenTime(appUser);
            }
        }

        // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;

    }

    private static void resetReponse(HttpServletResponse response) throws IOException {
        //重置response
        response.reset();
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter pw = response.getWriter();
        JSONObject obj = new JSONObject();
        obj.put("message", "Token失效，请重新登录");
        obj.put("status", 500);
        response.setStatus(500);
        pw.write(obj.toString());
        pw.flush();
        pw.close();
    }
}