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
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.MATSPLITService;
import org.yy.service.mm.StockListDetailService;
import org.yy.service.mm.StockListService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.PurchaseMaterialDetailsService;
import org.yy.service.system.UsersService;

/** 
 * 说明：出入库单
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-15
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/appStockList")
public class AppStockListController extends BaseController {
	
	@Autowired
	private StockListService StockListService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private StockListDetailService StockListDetailService;
	@Autowired
	private StockService StockService;
	@Autowired
	private PurchaseMaterialDetailsService PurchaseMaterialDetailsService;
	@Autowired
	private MATSPLITService MATSPLITService;
	@Autowired
	private StaffService staffService;
	
	/**
	 *	 根据id查询
	 * @author 宋
	 * @date 2020-12-25
	 * @param pd.StockList_ID
	 * @param pd.UserName
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("StockListDetail:edit")
	@ResponseBody
	public Object goEdit(HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = StockListService.findById(pd);	//根据ID读取
			return AppResult.success(pd, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**入库单列表
	 * @author 宋
	 * @date 2020-12-25
	 * @param pd.UserName
	 * @param pd.orderby
	 * @param pd.sort
	 * @param pd.KEYWORDS_DocumentNo 入库单号
	 * @param pd.KEYWORDS_AuditMark 审核状态
	 * @param pd.KEYWORDS_FCreator
	 * @param pd.DocumentTypeInOut 入库、出库
	 * @param pd.showCount
	 * @param pd.currentPage
	 * @throws Exception
	 */
	@RequestMapping(value="/getList")
	@ResponseBody
	public Object listIn(AppPage page,HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			String KEYWORDS_DocumentNo = pd.getString("KEYWORDS_DocumentNo");						//入库单号
			if(Tools.notEmpty(KEYWORDS_DocumentNo))pd.put("KEYWORDS_DocumentNo", KEYWORDS_DocumentNo.trim());
			String KEYWORDS_AuditMark = pd.getString("KEYWORDS_AuditMark");						//审核状态
			if(Tools.notEmpty(KEYWORDS_AuditMark))pd.put("KEYWORDS_AuditMark", KEYWORDS_AuditMark.trim());
			String KEYWORDS_FCreator = pd.getString("KEYWORDS_FCreator");						//创建人
			if(Tools.notEmpty(KEYWORDS_FCreator))pd.put("KEYWORDS_FCreator", KEYWORDS_FCreator.trim());
			String orderby = "FMakeBillsTime";
			if (Tools.notEmpty(pd.getString("orderby"))) {
				orderby = pd.getString("orderby");
			}
			String sort = "desc";
			if ("asc".equals(pd.getString("sort"))) {
				sort = "asc";
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(),orderby + " " + sort);
			PageInfo<PageData> pageInfo = new PageInfo<>(StockListService.appListIn(page));
			varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
}