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
import org.yy.service.mom.AreaService;
import org.yy.service.mom.PlineEntityService;

/** 
 * 说明：车间管理
 * 作者：YuanYe
 * 时间：2020-01-06
 * 
 */
@Controller
@RequestMapping("/area")
public class AreaController extends BaseController {
	
	@Autowired
	private AreaService areaService;
	@Autowired
	private PlineEntityService plineEntityService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("area:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = areaService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			if(null != pd.get("DM_SITE_ID") && !"".equals(pd.get("DM_SITE_ID"))) {
				pd.put("AREA_ID", this.get32UUID());	//主键
				areaService.save(pd);
				pd = areaService.findById(pd);	//根据id读取
			}else {
				errInfo="DM_SITE_ID is null value!";
			}
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("area:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			areaService.delete(pd);
		} catch(Exception e) {
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("area:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = areaService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			areaService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("area:list")
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
		String DM_SITE_ID = pd.getString("DM_SITE_ID");						//父级ID(工厂id)检索条件
		if(Tools.notEmpty(DM_SITE_ID))pd.put("DM_SITE_ID", DM_SITE_ID.trim());
		page.setPd(pd);
		List<PageData>	varList = areaService.list(page);	//列出Area列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**全部列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("area:listAll")
	@ResponseBody
	public Object listAll(Page page) throws Exception{
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
		String DM_SITE_ID = pd.getString("DM_SITE_ID");						//父级ID(工厂id)检索条件
		if(Tools.notEmpty(DM_SITE_ID))pd.put("DM_SITE_ID", DM_SITE_ID.trim());
		List<PageData>	varList = areaService.listAll(pd);	//列出Area列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**全部列表下拉菜单用
	 * 20211011 sunlz 创建
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/areaAll")
	//@RequiresPermissions("area:listAll")
	@ResponseBody
	public Object areaAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = areaService.listAll(pd);	//列出Area列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 通过FCODE获取数据查询数量
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/findByCode")
//	@RequiresPermissions("area:list")
	@ResponseBody
	public Object findByFCODE(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FCODE = pd.getString("FCODE");						//代码
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		page.setPd(pd);
		List<PageData>	varList = areaService.findByFCODE(page);	//列出列表
		map.put("num", varList.size());
		//map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("area:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = areaService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("area:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			areaService.deleteAll(ArrayDATA_IDS);
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
		titles.add("车间名称");	//1
		titles.add("车间代码");	//2
		titles.add("隶属工厂");	//3
		titles.add("描述");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = areaService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("DM_SITE_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("FDES"));	    //4
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
