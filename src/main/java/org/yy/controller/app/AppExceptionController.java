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
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.ExceptionService;
import org.yy.service.mm.ExceptionDefinitionService;
import org.yy.service.mm.ExceptionHandlingService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：异常
 * 作者：范贺男
 * 时间：2020-11-09
 */
@Controller
@RequestMapping("/appException")
public class AppExceptionController extends BaseController {
	
	@Autowired
	private ExceptionService ExceptionService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ExceptionHandlingService ExceptionHandlingService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ExceptionDefinitionService ExceptionDefinitionService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(HttpServletResponse response) throws Exception{
		try{
			PageData pd = new PageData();
			PageData pdHandling = new PageData();
			PageData pdStaff = new PageData();
			pd = this.getPageData();
			pd.put("Exception_ID", this.get32UUID());	//主键
			pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
			pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
			pd.put("FIssuedID",pdStaff.get("STAFF_ID"));			//制单人 STAFF_ID
			pd.put("ReleaseTime", Tools.date2Str(new Date())); //制单时间
			pd.put("IssueType", "已下发");
			ExceptionService.save(pd);//异常主表保存
			pdHandling.put("ExceptionHandling_ID", this.get32UUID());//主键ID
			pdHandling.put("Exception_ID", pd.get("Exception_ID"));//异常ID
			String InitOperatorID = pd.get("InitOperatorID").toString();
			if(InitOperatorID == "部门" || InitOperatorID.equals("部门")){
				pdHandling.put("InitOperatorID", 1);//移交给部门
			}else{
				pdHandling.put("InitOperatorID", 2);//移交给人
			}
			pdHandling.put("WaitingOperatorID", pd.get("ExceptionPendingParty"));//待处理人或部门
			pdHandling.put("IfTurnOver", 0);//是否移交处理  默认0 不移交
			pdHandling.put("Reorder", 1);//排序字段，自动生成时为1，每移交一次+1
			pdHandling.put("DisposeType", "待处理");//处理判别，初始默认待处理
			ExceptionHandlingService.save(pdHandling);
			operationrecordService.add("","异常","添加",pd.getString("Exception_ID"),"","");//操作日志
			return AppResult.success("添加成功", "success");
		}catch(Exception e){
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
		try{
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
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(ExceptionService.Applist(page));
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
	//@RequiresPermissions("Exception:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = ExceptionService.findById(pd);	//根据ID读取
			map.put("pd", pd);
			return AppResult.success(map, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	/**范贺男
	 * 显示全部部门列表
	 * @return
	 */
	@RequestMapping(value="/listAllDep")
	@ResponseBody
	public Object listAll(HttpServletResponse response)throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			PageData pd = new PageData();
			pd=this.getPageData();
			List<PageData> varList = departmentService.listAll(pd);
			return AppResult.success(varList, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}	
	/**用户列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/staffList")
	@ResponseBody
	public Object staffList(AppPage page,HttpServletResponse response)throws Exception{
		try{
			List<PageData> varList = Lists.newArrayList();
			PageData pd = new PageData();
			pd = this.getPageData();
			page.setPd(pd);
			PageInfo<PageData> pageInfo = new PageInfo<>(staffService.AppList(page));
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**获取数据字典中的异常类型
	 * @param 无参数
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value="/getExceptionType")
	@ResponseBody
	public Object getExceptionType(HttpServletResponse response) throws Exception{
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			PageData pd = new PageData();
			List<PageData> varList = ExceptionDefinitionService.getExceptionType(pd); //获取数据字典中的异常状态
			return AppResult.success(varList, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
}
