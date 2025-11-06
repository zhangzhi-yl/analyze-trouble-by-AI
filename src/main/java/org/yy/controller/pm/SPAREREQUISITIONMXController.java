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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mm.StockService;
import org.yy.service.pm.SPAREREQUISITIONMXService;

/** 
 * 说明：备件领用单明细
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-26
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/sparerequisitionmx")
public class SPAREREQUISITIONMXController extends BaseController {
	
	@Autowired
	private SPAREREQUISITIONMXService sparerequisitionmxService;
	@Autowired
	private StockService StockService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("sparerequisitionmx:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SPAREREQUISITIONMX_ID", this.get32UUID());	//主键
		sparerequisitionmxService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/spareSave")
	//@RequiresPermissions("sparerequisitionmx:add")
	@ResponseBody
	public Object spareSave() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		int	Flen = Integer.parseInt(pd.get("Flen").toString());
		for(int i =0 ;i < Flen; i++){
			PageData pdData = new PageData();
			String SPAREREQUISITION_ID = pd.get("SPAREREQUISITION_ID")==null?"":pd.get("SPAREREQUISITION_ID").toString();
			if(Tools.notEmpty(SPAREREQUISITION_ID)){
				pdData.put("SPAREREQUISITIONMX_ID", this.get32UUID());	//主键
				pdData.put("SPAREREQUISITION_ID", SPAREREQUISITION_ID);
				pdData.put("MATERIALNUM", pd.get("varList["+i+"][MAT_CODE]")==null?"":pd.get("varList["+i+"][MAT_CODE]").toString());
				pdData.put("MATERIALNAME", pd.get("varList["+i+"][MAT_NAME]")==null?"":pd.get("varList["+i+"][MAT_NAME]").toString());
				pdData.put("FUNIT", pd.get("varList["+i+"][FUnitName]")==null?"":pd.get("varList["+i+"][FUnitName]").toString());
				//pdData.put("DEMANDQUANTITY", pd.get("varList["+i+"][ActualCount]")==null?"":pd.get("varList["+i+"][ActualCount]").toString());
				pdData.put("WAREHOUSEID", pd.get("varList["+i+"][WarehouseName]")==null?"":pd.get("varList["+i+"][WarehouseName]").toString());
				pdData.put("POSITIONID", pd.get("varList["+i+"][PositionName]")==null?"":pd.get("varList["+i+"][PositionName]").toString());
				pdData.put("S_010", pd.get("varList["+i+"][WarehouseID]")==null?"":pd.get("varList["+i+"][WarehouseID]").toString());//仓库id
				pdData.put("S_009", pd.get("varList["+i+"][PositionID]")==null?"":pd.get("varList["+i+"][PositionID]").toString());//仓位ID
				pdData.put("S_008", pd.get("varList["+i+"][ItemID]")==null?"":pd.get("varList["+i+"][ItemID]").toString());//物料ID
				sparerequisitionmxService.save(pdData);
			}
			
		}
//		pd.put("SPAREREQUISITIONMX_ID", this.get32UUID());	//主键
//		sparerequisitionmxService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("sparerequisitionmx:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		sparerequisitionmxService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("sparerequisitionmx:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		sparerequisitionmxService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("sparerequisitionmx:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = sparerequisitionmxService.list(page);	//列出SPAREREQUISITIONMX列表
		for (PageData pdData : varList) {
			PageData pds = new PageData();
			pds.put("WarehouseID", pdData.get("S_010")==null?"":pdData.get("S_010").toString());
			pds.put("PositionID", pdData.get("S_009")==null?"":pdData.get("S_009").toString());
			pds.put("ItemID", pdData.get("S_008")==null?"":pdData.get("S_008").toString());
			PageData stockpd = StockService.getSum(pds);
			String stocknum = stockpd.get("stockSum").toString();
			pdData.put("DEMANDQUANTITY", stocknum);
		}
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
	//@RequiresPermissions("sparerequisitionmx:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = sparerequisitionmxService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("sparerequisitionmx:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			sparerequisitionmxService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备件领用单");	//1
		titles.add("物料编号");	//2
		titles.add("物料名称");	//3
		titles.add("单位");	//4
		titles.add("规格");	//5
		titles.add("需求数量");	//6
		titles.add("实际数量");	//7
		titles.add("仓库");	//8
		titles.add("仓位");	//9
		titles.add("备注");	//10
		titles.add("备注12");	//11
		titles.add("备注13");	//12
		titles.add("备注14");	//13
		titles.add("备注15");	//14
		titles.add("备注16");	//15
		titles.add("备注17");	//16
		titles.add("备注18");	//17
		titles.add("备注19");	//18
		titles.add("备注20");	//19
		titles.add("备注21");	//20
		dataMap.put("titles", titles);
		List<PageData> varOList = sparerequisitionmxService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("SPAREREQUISITION_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("MATERIALNUM"));	    //2
			vpd.put("var3", varOList.get(i).getString("MATERIALNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FUNIT"));	    //4
			vpd.put("var5", varOList.get(i).getString("FSPECS"));	    //5
			vpd.put("var6", varOList.get(i).getString("DEMANDQUANTITY"));	    //6
			vpd.put("var7", varOList.get(i).getString("ACTUALQUANTITY"));	    //7
			vpd.put("var8", varOList.get(i).getString("WAREHOUSEID"));	    //8
			vpd.put("var9", varOList.get(i).getString("POSITIONID"));	    //9
			vpd.put("var10", varOList.get(i).getString("FREMARKS"));	    //10
			vpd.put("var11", varOList.get(i).getString("S_001"));	    //11
			vpd.put("var12", varOList.get(i).getString("S_002"));	    //12
			vpd.put("var13", varOList.get(i).getString("S_003"));	    //13
			vpd.put("var14", varOList.get(i).getString("S_004"));	    //14
			vpd.put("var15", varOList.get(i).getString("S_005"));	    //15
			vpd.put("var16", varOList.get(i).getString("S_006"));	    //16
			vpd.put("var17", varOList.get(i).getString("S_007"));	    //17
			vpd.put("var18", varOList.get(i).getString("S_008"));	    //18
			vpd.put("var19", varOList.get(i).getString("S_009"));	    //19
			vpd.put("var20", varOList.get(i).getString("S_010"));	    //20
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
