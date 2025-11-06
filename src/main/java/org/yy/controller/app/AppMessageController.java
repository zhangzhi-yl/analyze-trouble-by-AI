package org.yy.controller.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.yy.entity.PageData;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.StockOperationRecordService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：通知查询
 * 作者：  QQ356703572
 * 时间：2020-11-13
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/appMessage")
public class AppMessageController extends BaseController {
	@Autowired
	private NoticeService noticeService;
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("USERNAME", pd.get("UserName"));//当前登录人
			List<PageData> varList = Lists.newArrayList();
			String orderby = "f.ReleaseTime";//创建时间
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(noticeService.AppList(page));
			varList = pageInfo.getList();
			Map<String, Object> extra = new HashMap<>();
			for(int i=0;i<varList.size();i++){
				PageData pd1 = varList.get(i);
				if(pd1.get("Report_Key").toString() == "task"){
					JSONObject jsonObject = JSONObject.parseObject(pd1.get("Report_Value").toString());
					extra.put(pd1.get("Report_Key").toString(), jsonObject);
				}else{
					extra.put(pd1.get("Report_Key").toString(), pd1.get("Report_Value").toString());
				}
				varList.get(i).put("extra", extra);
			}
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**未读条数
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/NoReadNum")
	@ResponseBody
	public Object NoReadNum() throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("USERNAME", pd.get("UserName"));//当前登录人
			List<PageData> varList = noticeService.listAllWD(pd);//个人未读列表
			pd.put("NoReadNum", varList.size());
        return AppResult.success(pd, "success", "success");
	} catch (Exception e) {
		e.printStackTrace();
		return AppResult.failed(e.getMessage());
	}
	}
	/**一键已读
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/readAll")
	@ResponseBody
	public Object readAll() throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("USERNAME", pd.get("UserName"));//当前登录人
			String read = "";
			String USERNAME = pd.get("UserName").toString();
			List<PageData> varList = noticeService.listAllWD(pd);//个人未读列表
			for(int i = 0;i<varList.size();i++){
				 read = varList.get(i).get("ReadPeople").toString();//已读人
				 int a = read.indexOf(USERNAME);
				 if(a == -1){
					read += USERNAME;
				 }
				 read = read + ",";
				 pd.put("ReadPeople", read);
				 pd.put("NOTICE_ID", varList.get(i).get("NOTICE_ID"));
				noticeService.editRead(pd);
			}
        return AppResult.success(pd, "success", "success");
	} catch (Exception e) {
		e.printStackTrace();
		return AppResult.failed(e.getMessage());
	}
	}
	
	
	/**一条已读
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/readOne")
	@ResponseBody
	public Object readOne() throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("USERNAME", pd.get("UserName"));//当前登录人
			String read = "";
			String USERNAME = pd.get("UserName").toString();
			PageData pdOne = noticeService.findById(pd);//个人未读列表
				 read = pdOne.get("ReadPeople").toString();//已读人
				 int a = read.indexOf(USERNAME);
				 if(a == -1){
					read += USERNAME;
				 }
				 read = read + ",";
				 pd.put("ReadPeople", read);
				 pd.put("NOTICE_ID", pdOne.get("NOTICE_ID"));
				noticeService.editRead(pd);
        return AppResult.success(pd, "success", "success");
	} catch (Exception e) {
		e.printStackTrace();
		return AppResult.failed(e.getMessage());
	}
	}
}
