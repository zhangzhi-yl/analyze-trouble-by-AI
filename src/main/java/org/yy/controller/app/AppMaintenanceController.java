package org.yy.controller.app;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.service.app.AppService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.pm.PM_MAINTAINMxService;
import org.yy.service.pm.PM_MAINTAINService;
import org.yy.service.pm.PM_MAINTAIN_CONSUMEService;
import org.yy.service.pm.PM_MAINTAIN_MANHOURService;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 设备保养
 * @author YULONG
 *
 */
@RestController
@RequestMapping("/appMaintenance")
public class AppMaintenanceController extends BaseController{
	@Autowired
	private AppService appService;
	@Autowired
	private PM_MAINTAINService PM_MAINTAINService;
	@Autowired
	private PM_MAINTAINMxService PM_MAINTAINMxService;
	@Autowired
	private PM_MAINTAIN_CONSUMEService PM_MAINTAIN_CONSUMEService;
	@Autowired
	private PM_MAINTAIN_MANHOURService PM_MAINTAIN_MANHOURService;
	@Autowired
	private StaffService staffService;
	/**
	 * 设备保养计划
	 */
	@RequestMapping("/maintenanceList")
	public Object maintenanceList(AppPage page) {
		try {
			PageData pd = new PageData();
			List<PageData> varList = Lists.newArrayList();
			pd = this.getPageData();
			// 获取数据
			if (null != pd) {
				String orderby = "MAINTAIN_PLAN_NO";
				if (Tools.notEmpty(pd.getString("orderby"))) {
					orderby = pd.getString("orderby");
				}
				String sort = "asc";
				if ("desc".equals(pd.getString("sort"))) {
					sort = "desc";
				}

				page.setPd(pd);
				PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
				PageInfo<PageData> pageInfo = new PageInfo<>(
						appService.appMaintenanceList(page));
				varList = pageInfo.getList();
				page.setTotalPage(pageInfo.getPages());
				page.setTotalResult(pageInfo.getTotal());
			}
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
		@RequestMapping(value="/findById")
		@ResponseBody
		public Object findById(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				pd = PM_MAINTAINService.findById(pd);
				return AppResult.success(pd, "获取成功", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		
		 /**修改数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/saveSigin")
		@ResponseBody
		public Object saveSigin(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				String IF_SIGNIN = pd.getString("IF_SIGNIN");
				String PRACTICAL_START_TIME = pd.getString("PRACTICAL_START_TIME")==null?"":pd.getString("PRACTICAL_START_TIME");
				if(PRACTICAL_START_TIME.equals("")){
					PageData pds=PM_MAINTAINService.findById(pd);
					pds.put("PRACTICAL_START_TIME", Tools.date2Str(new Date()));
					pds.put("RUN_STATUS", "执行中");
					PM_MAINTAINService.edit(pds);
				}
				PageData pds = PM_MAINTAINMxService.findById(pd);
				if(IF_SIGNIN.equals("是")){
					pds.put("IF_SIGNIN", "是");
					pds.put("SIGNIN_TIME", Tools.date2Str(new Date()));
					pds.put("SIGNIN_RERSON", pd.get("UserName"));
				}else{
					pds.put("IF_SIGNIN", null);
					pds.put("SIGNIN_TIME", null);
					pds.put("SIGNIN_RERSON", null);
				}
				PM_MAINTAINMxService.edit(pds);
				return AppResult.success("success", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		
		 /**修改数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/saveFinish")
		@ResponseBody
		public Object saveFinish(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				PageData pdf =PM_MAINTAINService.findById(pd);
				pdf.put("PRACTICAL_END_TIME", Tools.date2Str(new Date()));
				long MAINTAIN_MANHOUR=Tools.timeSubtraction(pdf.getString("PRACTICAL_START_TIME"), pdf.getString("PRACTICAL_END_TIME"));
				pdf.put("IF_LEFTOVER_PROBLEM", pd.getString("IF_LEFTOVER_PROBLEM"));
				pdf.put("LEFTOVER_PROBLEM", pd.getString("LEFTOVER_PROBLEM"));
				pdf.put("MAINTAIN_MANHOUR", MAINTAIN_MANHOUR);
				pdf.put("RUN_STATUS", "结束");
				PM_MAINTAINService.edit(pdf);
				return AppResult.success("success", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		
		/**
		 * 设备保养计划明细
		 * @param page
		 * @return
		 */
		@RequestMapping("/maintenanceMxList")
		public Object maintenanceMxList(AppPage page) {
			try {
				PageData pd = new PageData();
				List<PageData> varList = Lists.newArrayList();
				pd = this.getPageData();
				// 获取数据
				if (null != pd) {
					String orderby = "CAST(RESERVE_ONE AS DECIMAL)";
					if (Tools.notEmpty(pd.getString("orderby"))) {
						orderby = pd.getString("orderby");
					}
					String sort = "asc";
					page.setPd(pd);
					PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
					PageInfo<PageData> pageInfo = new PageInfo<>(
							appService.appMaintenanceMxList(page));
					varList = pageInfo.getList();
					page.setTotalPage(pageInfo.getPages());
					page.setTotalResult(pageInfo.getTotal());
				}
				return AppResult.success(varList, page);
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		
		/**
		 * 备件消耗
		 * @param page
		 * @return
		 */
		@RequestMapping("/maintainConsumeList")
		public Object maintainConsumeList(AppPage page) {
			try {
				PageData pd = new PageData();
				List<PageData> varList = Lists.newArrayList();
				pd = this.getPageData();
				// 获取数据
				if (null != pd) {
					String orderby = "MAT_NAME";
					if (Tools.notEmpty(pd.getString("orderby"))) {
						orderby = pd.getString("orderby");
					}
					String sort = "asc";
					page.setPd(pd);
					PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
					PageInfo<PageData> pageInfo = new PageInfo<>(
							appService.appMaintainConsumeList(page));
					varList = pageInfo.getList();
					page.setTotalPage(pageInfo.getPages());
					page.setTotalResult(pageInfo.getTotal());
				}
				return AppResult.success(varList, page);
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		
		 /**修改数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/saveConsume")
		@ResponseBody
		public Object saveConsume(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				PageData pdf =PM_MAINTAIN_CONSUMEService.findById(pd);
				pdf.put("CONSUME_NUM", pd.getString("CONSUME_NUM"));
				PM_MAINTAIN_CONSUMEService.edit(pdf);
				return AppResult.success("success", "success");
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		
		/**
		 * 工时
		 * @param page
		 * @return
		 */
		@RequestMapping("/maintainManHourList")
		public Object maintainManHourList(AppPage page) {
			try {
				PageData pd = new PageData();
				List<PageData> varList = Lists.newArrayList();
				pd = this.getPageData();
				// 获取数据
				if (null != pd) {
					String orderby = "ACTUAL_START_TIME";
					if (Tools.notEmpty(pd.getString("orderby"))) {
						orderby = pd.getString("orderby");
					}
					String sort = "asc";
					page.setPd(pd);
					PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
					PageInfo<PageData> pageInfo = new PageInfo<>(
							appService.appMaintainManHourList(page));
					varList = pageInfo.getList();
					page.setTotalPage(pageInfo.getPages());
					page.setTotalResult(pageInfo.getTotal());
				}
				return AppResult.success(varList, page);
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
		
		 /**保存数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/saveManHour")
		@ResponseBody
		public Object saveManHour(HttpServletResponse response) throws Exception{
			try {
				PageData pd = new PageData();
				pd = this.getPageData();
				String UserName = pd.getString("STAFF_NAME");
				PageData pduser = new PageData();
				pduser.put("USERNAME", UserName);
				pduser=staffService.getDEPTNAME(pduser);
				if(pduser==null){
					return AppResult.failed("当前员工不存在！", "当前员工不存在！");
				}else{
					long MAINTAIN_MANHOUR=Tools.timeSubtraction(pd.getString("ACTUAL_START_TIME"), pd.getString("ACTUAL_END_TIME"));
					pd.put("STAFF_ID",pduser.getString("STAFF_ID"));
					pd.put("DEPARTMENT_ID",pduser.getString("DEPARTMENT_ID"));
					pd.put("PM_MAINTAIN_MANHOUR_ID",this.get32UUID());
					pd.put("ACTUAL_MANHOUR", MAINTAIN_MANHOUR);
					PM_MAINTAIN_MANHOURService.save(pd);
					return AppResult.success("success", "success");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return AppResult.failed(e.getMessage());
			}
		}
}
