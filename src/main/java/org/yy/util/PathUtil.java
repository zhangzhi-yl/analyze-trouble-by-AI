package org.yy.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 说明：路径工具类
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public class PathUtil {

	/**获取Projectpath
	 * @return
	 */
	public static String getProjectpath(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String path = request.getServletContext().getRealPath("/").replaceAll("%20", " ").replaceAll("file:/", "").trim()+"/";
		path = "D:\\bushu11033\\";/**jar 包解开注释 ，路径改成实际硬盘位置 application.properties 文件同时解开注释*/
		return path;
	}
	
	/**获取Classpath
	 * @return
	 */
	public static String getClasspath(){
		String path =  (String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))).replaceAll("file:/", "").replaceAll("%20", " ").trim();	
		if(path.indexOf(":") != 1){
			path = File.separator + path;
		
		}
		path = "D:\\bushu11033\\";  //当项目以jar、war包运行时，路径改成实际硬盘位置
		return path;
	}
	
}
