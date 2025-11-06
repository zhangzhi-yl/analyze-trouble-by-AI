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
import org.yy.service.mom.Unit_InfoService;
import org.yy.service.mom.Unit_UiomService;

/** 
 * 说明：基础单位信息管理
 * 作者：YuanYe
 * 时间：2020-01-09
 * 
 */
@Controller
@RequestMapping("/unit_info")
public class Unit_InfoController extends BaseController {
	
	@Autowired
	private Unit_InfoService unit_infoService;
	
	@Autowired
	private Unit_UiomService unit_uiomService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("unit_info:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = unit_infoService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			pd.put("UNIT_INFO_ID", this.get32UUID());	//主键,单位信息ID
			unit_infoService.save(pd);
		}
		pd = unit_infoService.findById(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("unit_info:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData subpd = new PageData();
		Page page = new Page();
		page.setPd(subpd);
		pd = this.getPageData();
		pd = unit_infoService.findById(pd);
		if(null != pd) {
			subpd.put("RELATION_UNIT_CODE", pd.get("FCODE").toString());
			if(unit_uiomService.list(page).size()>0) {	//如果存在关联数据，则不能删除
				errInfo = "error";
			} else {
				unit_infoService.delete(pd);
			}
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("unit_info:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = unit_infoService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			unit_infoService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("unit_info:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String FCODE = pd.getString("FCODE");						//代码检索条件
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		String FNAME = pd.getString("FNAME");						//名称检索条件
		if(Tools.notEmpty(FNAME))pd.put("FNAME", FNAME.trim());
		String FTYPE = pd.getString("FTYPE");						//类型检索条件
		if(Tools.notEmpty(FTYPE))pd.put("FTYPE", FTYPE.trim());
		page.setPd(pd);
		List<PageData>	varList = unit_infoService.list(page);	//列出UNIT_INFO列表
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
	//@RequiresPermissions("unit_info:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = unit_infoService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("unit_info:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			unit_infoService.deleteAll(ArrayDATA_IDS);
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
		titles.add("单位信息ID");	//1
		titles.add("代码");	//2
		titles.add("名称");	//3
		titles.add("类型");	//4
		titles.add("描述");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = unit_infoService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("UNIT_INFO_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FTYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FDES"));	    //5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	 /**获取单位列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("unit_info:edit")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = unit_infoService.listAll(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		map.put("varList", varList);
		return map;
	}
	
	 /**获取单位下拉列表，前100
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getUnitList")
	@ResponseBody
	public Object getUnitList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> varList = unit_infoService.getUnitList(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		map.put("varList", varList);
		return map;
	}
}
