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
import org.yy.service.mom.WH_StorageAreaService;

/** 
 * 说明：库区管理
 * 作者：YuanYe
 * 时间：2020-01-08
 * 
 */
@Controller
@RequestMapping("/storagearea")
public class WH_StorageAreaController extends BaseController {
	
	@Autowired
	private WH_StorageAreaService wh_storageareaService;
	
	@Autowired
	private WH_LocationService wh_locationService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("wh_storagearea:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = wh_storageareaService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			pd.put("WH_STORAGEAREA_ID", this.get32UUID());	//主键
			wh_storageareaService.save(pd);
		}
		pd = wh_storageareaService.findById(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("wh_storagearea:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if(Integer.parseInt(wh_locationService.findCount(pd).get("zs").toString())>0) {
			errInfo = "error";
		} else {
			wh_storageareaService.delete(pd);
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("wh_storagearea:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData num = new PageData();
		num = wh_storageareaService.findCountByCode(pd);
		if(num != null && null != num.get("zs") && Integer.parseInt(num.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
		} else {
			wh_storageareaService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("wh_storagearea:list")
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
		String FTYPE = pd.getString("FTYPE");						//类型检索条件
		if(Tools.notEmpty(FTYPE))pd.put("FTYPE", FTYPE.trim());
		String PROHIBIT_WHETHER = pd.getString("PROHIBIT_WHETHER");						//是否禁用条件
		if(Tools.notEmpty(PROHIBIT_WHETHER))pd.put("PROHIBIT_WHETHER", PROHIBIT_WHETHER.trim());
		String WH_WAREHOUSE_ID = pd.getString("WH_WAREHOUSE_ID");						//仓库ID检索条件
		if(Tools.notEmpty(WH_WAREHOUSE_ID))pd.put("WH_WAREHOUSE_ID", WH_WAREHOUSE_ID.trim());
		String WC_WORKCENTER_ID = pd.getString("WC_WORKCENTER_ID");						//工作中心ID检索条件
		if(Tools.notEmpty(WC_WORKCENTER_ID))pd.put("WC_WORKCENTER_ID", WC_WORKCENTER_ID.trim());
		page.setPd(pd);
		List<PageData>	varList = wh_storageareaService.list(page);	//列出WH_StorageArea列表
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
	//@RequiresPermissions("wh_storagearea:listAll")
	@ResponseBody
	public Object listAll(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = wh_storageareaService.listAll(pd);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**库区列表,下拉选用
	 * @param pd 是否禁用，仓库id
	 * @throws Exception
	 */
	@RequestMapping(value="/storageAreaList")
	//@RequiresPermissions("wh_storagearea:list")
	@ResponseBody
	public Object storageAreaList(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = wh_storageareaService.storageAreaList(pd);
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
	//@RequiresPermissions("wh_storagearea:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = wh_storageareaService.findById(pd);	//根据ID读取
		map.put("pd", pd);
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
//	@RequiresPermissions("wh_storagearea:list")
	@ResponseBody
	public Object findByFCODE(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FCODE = pd.getString("FCODE");						//代码
		if(Tools.notEmpty(FCODE))pd.put("FCODE", FCODE.trim());
		page.setPd(pd);
		List<PageData>	varList = wh_storageareaService.findByFCODE(page);	//列出列表
		map.put("num", varList.size());
		//map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("wh_storagearea:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			wh_storageareaService.deleteAll(ArrayDATA_IDS);
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
		titles.add("库区ID");	//1
		titles.add("代码");	//2
		titles.add("名称");	//3
		titles.add("库位属性");	//4
		titles.add("库位类型");	//5
		titles.add("是否允许负库存");	//6
		titles.add("面积");	//7
		titles.add("负责人");	//8
		titles.add("负责人电话");	//9
		titles.add("是否禁用");	//10
		titles.add("关联工作中心内码");	//11
		titles.add("仓库id");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = wh_storageareaService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("WH_STORAGEAREA_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FATTRIBUTE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FTYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("NEGATIVE_STOCK_WHETHER"));	    //6
			vpd.put("var7", varOList.get(i).get("FAREA").toString());	//7
			vpd.put("var8", varOList.get(i).getString("FHEAD"));	    //8
			vpd.put("var9", varOList.get(i).getString("HEAD_PHONE"));	    //9
			vpd.put("var10", varOList.get(i).getString("PROHIBIT_WHETHER"));	    //10
			vpd.put("var11", varOList.get(i).getString("WORKCENTER_CODE"));	    //11
			vpd.put("var12", varOList.get(i).getString("WH_WAREHOUSE_ID"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
