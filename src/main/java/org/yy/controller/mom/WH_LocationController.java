package org.yy.controller.mom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mom.WH_LocationService;

/** 
 * 说明：库位管理
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 */
@Controller
@RequestMapping("/location")
public class WH_LocationController extends BaseController {
	
	@Autowired
	private WH_LocationService wh_locationService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("wh_location:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = wh_locationService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			pd.put("WH_LOCATION_ID", this.get32UUID());	//主键,库位ID
			wh_locationService.save(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("wh_location:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		wh_locationService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("wh_location:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = wh_locationService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			wh_locationService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
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
	
	/**库位列表,下拉选用
	 * @param pd 是否禁用，库区id
	 * @throws Exception
	 */
	@RequestMapping(value="/locationList")
	//@RequiresPermissions("wh_location:list")
	@ResponseBody
	public Object locationList(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = wh_locationService.locationList(pd);
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
	public Object findByCode() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FCODE = pd.getString("FCODE");						//代码
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		pd = wh_locationService.findByCode(pd);	//根据coDe读取
		if(null != pd && null != pd.get("FCODE") && !"".equals(pd.getString("FCODE")) && !"null".equals(pd.getString("FCODE"))) {
			map.put("num", 1);
		}else {
			map.put("num", 0);
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("wh_location:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			wh_locationService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("库位ID");	//1
		titles.add("位置代码");	//2
		titles.add("位置名称");	//3
		titles.add("出库规则");	//4
		titles.add("库位容量限");	//5
		titles.add("是否禁用");	//6
		titles.add("库位状态");	//7
		titles.add("描述");	//8
		titles.add("库区ID");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = wh_locationService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("WH_LOCATION_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("OUTGOING_RULES"));	    //4
			vpd.put("var5", varOList.get(i).get("MAXIMUM_WEIGHT").toString());	//5
			vpd.put("var6", varOList.get(i).getString("PROHIBIT_WHETHER"));	    //6
			vpd.put("var7", varOList.get(i).getString("FSTATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FDES"));	    //8
			vpd.put("var9", varOList.get(i).getString("WH_STORAGEAREA_ID"));	    //9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
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
