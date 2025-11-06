package org.yy.controller.pm;

import java.math.BigDecimal;
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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.github.pagehelper.util.StringUtil;

import org.yy.entity.PageData;
import org.yy.service.mm.StockService;
import org.yy.service.pm.SPAREREQUISITIONMXService;
import org.yy.service.pm.SPAREREQUISITIONService;

/** 
 * 说明：备件领用单
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-26
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/sparerequisition")
public class SPAREREQUISITIONController extends BaseController {
	
	@Autowired
	private SPAREREQUISITIONService sparerequisitionService;
	@Autowired
	private SPAREREQUISITIONMXService sparerequisitionmxService;
	@Autowired
	private StockService StockService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("sparerequisition:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String SPAREREQUISITION_ID = this.get32UUID();
		pd.put("SPAREREQUISITION_ID", SPAREREQUISITION_ID);	//主键
		pd.put("FCREATETIME", Tools.date2Str(new Date()));//创建时间
		pd.put("FCREATEPERSONID", Jurisdiction.getName());//创建人
		pd.put("FSTATE", "创建");//状态
		sparerequisitionService.save(pd);
		map.put("SPAREREQUISITION_ID", SPAREREQUISITION_ID);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("sparerequisition:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		sparerequisitionService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("sparerequisition:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		sparerequisitionService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 备件领用单 领用功能 库存库位的物料的库存大于领用数量，则扣减库存    
	 * v1 吴双双  2021-08-26
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goReceive")
	//@RequiresPermissions("sparerequisition:edit")
	@ResponseBody
	public Object goReceive() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = sparerequisitionmxService.listAll(pd);	//列出SPAREREQUISITIONMX列表
		for (PageData pdData : varList) {
			PageData pdNum = new PageData();
			pdNum = sparerequisitionmxService.listAllVerify(pd);//审核校验
			if(Integer.parseInt(pdNum.get("FNUM").toString())>0) {//审核校验，返回值大于0说明有信息填写不全
				map.put("result", "errorNo");//信息填写不全
				return map;
			}
			PageData pds = new PageData();
			pds.put("WarehouseID", pdData.get("S_010")==null?"":pdData.get("S_010").toString());
			pds.put("PositionID", pdData.get("S_009")==null?"":pdData.get("S_009").toString());
			pds.put("ItemID", pdData.get("S_008")==null?"":pdData.get("S_008").toString());
			pds.put("num", pdData.get("ACTUALQUANTITY")==null?"0":pdData.get("ACTUALQUANTITY").toString());
			PageData stockpd = StockService.getSum(pds); //根据仓库、库位、物料的ID获取该物料的库存数量
			BigDecimal stocknum = new BigDecimal(stockpd.get("stockSum").toString());
			BigDecimal ACTUALQUANTITY = new BigDecimal(0);
			try {
				ACTUALQUANTITY = new BigDecimal(pdData.get("ACTUALQUANTITY").toString());
			} catch (Exception e) {
				ACTUALQUANTITY = new BigDecimal(0);
			}
			
			if(ACTUALQUANTITY.compareTo(stocknum)==1){ //领用数量大于库存数量则不影响库存
				errInfo = "greaterthan";
				map.put("result", errInfo);
				return map;
			}
		}
		for (PageData pdData : varList) {
			PageData pds = new PageData();
			pds.put("WarehouseID", pdData.get("S_010")==null?"":pdData.get("S_010").toString());
			pds.put("PositionID", pdData.get("S_009")==null?"":pdData.get("S_009").toString());
			pds.put("ItemID", pdData.get("S_008")==null?"":pdData.get("S_008").toString());
			pds.put("num", pdData.get("ACTUALQUANTITY")==null?"0":pdData.get("ACTUALQUANTITY").toString());
			StockService.outStock(pds); //若程序没有结束，扣减库存
		}
		pd=sparerequisitionService.findById(pd);//获取主表数据
		pd.put("FSTATE", "已领"); //状态 已领
		pd.put("FRECEIVER", Jurisdiction.getName());//领用人
		pd.put("S_002", Tools.date2Str(new Date()));//领用时间
		sparerequisitionService.edit(pd); //修改主表数据
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("sparerequisition:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = sparerequisitionService.list(page);	//列出SPAREREQUISITION列表
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
	//@RequiresPermissions("sparerequisition:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = sparerequisitionService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("sparerequisition:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			sparerequisitionService.deleteAll(ArrayDATA_IDS);
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
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("编号");	//1
		titles.add("设备名称");	//2
		titles.add("状态");	//3
		titles.add("备注");	//4
		titles.add("领用人");	//5
		titles.add("制单人");	//6
		titles.add("制单时间");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = sparerequisitionService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("REQUISITIONNUM"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQUIPMENTNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FSTATE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FREMARKS"));	    //4
			vpd.put("var5", varOList.get(i).getString("FRECEIVER"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCREATEPERSONID"));	    //6
			vpd.put("var7", varOList.get(i).getString("FCREATETIME"));	    //7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**备品备件详情列表、
	 * 杨城 20210927 
	 * v1：备品备件详情列表，保养任务与维修报工通用
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listEqm")
	@ResponseBody
	public Object listEqm(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = sparerequisitionService.datalistPageEqm(page);	//备品备件详情列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
}
