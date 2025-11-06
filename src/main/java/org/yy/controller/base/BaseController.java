package org.yy.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.yy.entity.PageData;
import org.yy.util.UuidUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 说明：所有处理类父类  
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public class BaseController {

	/**
	 * new PageData对象
	 * @return
	 */
	public PageData getPageData() {
		return new PageData(this.getRequest());
	}

	/**
	 * 得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}
	
	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
}
