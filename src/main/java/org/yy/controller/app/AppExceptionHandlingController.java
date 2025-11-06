package org.yy.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.ExceptionService;
import org.yy.service.mm.ExceptionHandlingService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：异常处理
 * 作者：范贺男
 * 时间：2020-11-10
 */
@Controller
@RequestMapping("/appExceptionHandling")
public class AppExceptionHandlingController extends BaseController {
	
	@Autowired
	private ExceptionHandlingService ExceptionHandlingService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private ExceptionService ExceptionService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("ExceptionHandling_ID", this.get32UUID());	//主键
			ExceptionHandlingService.save(pd);
			operationrecordService.add("","异常处理","添加",pd.getString("ExceptionHandling_ID"),"","");//操作日志
			return AppResult.success("添加成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit(HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			PageData pdStaff = new PageData();
			pd = this.getPageData();
			pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
			pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
			pd.put("FOperatorID",pdStaff.get("STAFF_ID"));			//制单人 STAFF_ID
			pd.put("FTime", Tools.date2Str(new Date())); //制单时间
			ExceptionHandlingService.edit(pd);
			operationrecordService.add("","异常处理","修改",pd.getString("ExceptionHandling_ID"),"","");//操作日志
			return AppResult.success("修改成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
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
			List<PageData> varList = Lists.newArrayList();
			String orderby = "RowNum";
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "asc";
			if ("desc".equals(pd.getString("sort"))) {
				sort = "desc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(ExceptionHandlingService.AppList(page));
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit(HttpServletResponse response) throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = ExceptionHandlingService.findById(pd);	//根据ID读取
			map.put("pd", pd);
			return AppResult.success(map,"获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	
	/**
	 * 异常移交
	 * @param ExceptionHandling_ID 主键ID Exception_ID 异常ID
	 * @throws Exception
	 */
	@RequestMapping(value="/goTurnOver")
	@ResponseBody
	public Object goTurnOver(HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();	
			PageData pdStaff = new PageData();	
			PageData pdReorder = new PageData();	
			PageData NewPd = new PageData();
			pd = this.getPageData();
				pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
				pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
				pd.put("FOperatorID",pdStaff.get("STAFF_ID"));			//处理人 STAFF_ID
				pd.put("FTime", Tools.date2Str(new Date()));//处理时间
				pd.put("TurnOverTime", Tools.date2Str(new Date()));//移交时间
				ExceptionHandlingService.EditDisposeType(pd);//修改本条异常处理记录的异常判别及移交时间
				///////////////////修改判别状态的同时，在异常记录中新增一条异常处理记录
				pdReorder = ExceptionHandlingService.getReorder(pd);//获取最大序号
				Integer Reorder = Integer.parseInt(pdReorder.get("Reorder").toString());//获取最大序号转成Integer方便计算
				int MaxReorder = Reorder+1;//最大序号+1
				NewPd.put("ExceptionHandling_ID", this.get32UUID());//主键ID
				NewPd.put("Exception_ID", pd.get("Exception_ID"));//异常ID
				NewPd.put("Reorder", MaxReorder);//排序序号
				NewPd.put("IfTurnOver", 0);//是否移交处理  默认0 不移交
				NewPd.put("FOperator",pd.get("FOperator"));//移交给人
				NewPd.put("WaitingOperatorID", pd.get("ExceptionPendingParty"));//待处理人或部门
				NewPd.put("DisposeType", "待处理");//处理判别，初始默认待处理
				ExceptionHandlingService.save(NewPd);
				return AppResult.success("移交成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**
	 * 异常处理完成
	 */
	@RequestMapping(value="/FinishException")
	@ResponseBody
	public Object FinishException(HttpServletResponse response) throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();	
			PageData pdStaff = new PageData();	
			pd = this.getPageData();
			pd = ExceptionHandlingService.findById(pd);
			pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
			pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
			pd.put("FOperatorID",pdStaff.get("STAFF_ID"));			//处理人 STAFF_ID
			pd.put("FTime", Tools.date2Str(new Date()));
			ExceptionHandlingService.FinishException(pd);//修改本条异常处理记录的异常判别及移交时间
			ExceptionService.FinishException(pd);//完成整条异常
			return AppResult.success("完成成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
}
