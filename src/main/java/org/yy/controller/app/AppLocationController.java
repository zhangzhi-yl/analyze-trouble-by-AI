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
import org.yy.util.ObjectExcelView;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;

import org.yy.entity.PageData;
import org.yy.service.mom.WH_LocationService;
import org.yy.service.mom.WH_WareHouseService;
import org.yy.service.system.UsersService;

/** 
 * 说明：库位管理
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 */
@Controller
@RequestMapping("/appLocation")
public class AppLocationController extends BaseController {
	
	@Autowired
	private WH_WareHouseService wh_warehouseService;
	
	@Autowired
	private WH_LocationService wh_locationService;
	
	/**库位列表,下拉选用
	 * @param pd.UserName
	 * @param pd.FNAME
	 * @param pd.FCODE
	 * @param pd.PROHIBIT_WHETHER
	 * @param pd.WH_STORAGEAREA_ID
	 * @throws Exception
	 */
	@RequestMapping(value="/getList")
	@ResponseBody
	public Object getList(HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			varList = wh_locationService.appList(pd);
			return AppResult.success(varList, "获取成功", "success");
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
	//@RequiresPermissions("wh_location:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键字检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String FCODE = pd.getString("FCODE");						//代码检索条件
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		String FNAME = pd.getString("FNAME");						//名称检索条件
		if(Tools.notEmpty(FNAME))pd.put("FNAME", FNAME.trim());
		String OUTGOING_RULES = pd.getString("OUTGOING_RULES");						//出库规则检索条件
		if(Tools.notEmpty(OUTGOING_RULES))pd.put("OUTGOING_RULES", OUTGOING_RULES.trim());
		String PROHIBIT_WHETHER = pd.getString("PROHIBIT_WHETHER");						//是否禁用条件
		if(Tools.notEmpty(PROHIBIT_WHETHER))pd.put("PROHIBIT_WHETHER", PROHIBIT_WHETHER.trim());
		String FSTATE = pd.getString("FSTATE");						//状态检索条件
		if(Tools.notEmpty(FSTATE))pd.put("FSTATE", FSTATE.trim());
		String WH_STORAGEAREA_ID = pd.getString("WH_STORAGEAREA_ID");						//库区ID检索条件
		if(Tools.notEmpty(WH_STORAGEAREA_ID))pd.put("WH_STORAGEAREA_ID", WH_STORAGEAREA_ID.trim());
		page.setPd(pd);
		List<PageData>	varList = wh_locationService.list(page);	//列出WH_Location列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("wh_location:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = wh_locationService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**通过code获取数据
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/findByCode")
	//@RequiresPermissions("wh_location:edit")
	@ResponseBody
	public Object findByCode(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String qrcode = pd.getString("CODE");
		String[] arr = qrcode.split(",YL,");
		PageData warehousePd = new PageData();
		warehousePd.put("FCODE", arr[0]);
		PageData locationPd = new PageData();
		warehousePd = wh_warehouseService.findByCode(warehousePd);
		if(null!=warehousePd && warehousePd.containsKey("WH_WAREHOUSE_ID")) {
			if(Tools.notEmpty(arr[1]))locationPd.put("FCODE", arr[1].trim());
			locationPd.put("WH_STORAGEAREA_ID", warehousePd.getString("WH_WAREHOUSE_ID"));
			locationPd = wh_locationService.findByCode(locationPd);	//根据coDe读取
		}
		return AppResult.success(locationPd, "获取成功", "success");
	}
	
	/**获取仓位列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-16
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getLocationList")
	@ResponseBody
	public Object getLocationList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-仓位名/仓位编号
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = wh_locationService.getLocationList(pd);	
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
