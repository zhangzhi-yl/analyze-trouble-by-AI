package org.yy.controller.mm;

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
import org.yy.service.mm.CallMaterialDetailsFLService;

/** 
 * 说明：分料任务明细
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-23
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/callmaterialdetailsfl")
public class CallMaterialDetailsFLController extends BaseController {
	
	@Autowired
	private CallMaterialDetailsFLService callmaterialdetailsflService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("callmaterialdetailsfl:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CallMaterialDetailsFL_ID", this.get32UUID());	//主键
		callmaterialdetailsflService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("callmaterialdetailsfl:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		callmaterialdetailsflService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("callmaterialdetailsfl:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdX=callmaterialdetailsflService.findById(pd);
		pdX.put("QuantityCount", pd.get("QuantityCount"));
		callmaterialdetailsflService.edit(pdX);
		map.put("result", errInfo);
		return map;
	}
	/**v1 管悦 20210824 车间入库
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/inStock")
	//@RequiresPermissions("callmaterialdetailsfl:edit")
	@ResponseBody
	public Object inStock() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdX=callmaterialdetailsflService.findById(pd);
		pdX.put("TargetPositionID", pd.get("TargetPositionID"));
		pdX.put("TargetWarehouseID", pd.get("TargetWarehouseID"));
		pdX.put("TargetWarehouse", pd.get("TargetWarehouse"));
		pdX.put("TargetPosition", pd.get("TargetPosition"));
		callmaterialdetailsflService.edit(pdX);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("callmaterialdetailsfl:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = callmaterialdetailsflService.list(page);	//列出CallMaterialDetailsFL列表
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
	//@RequiresPermissions("callmaterialdetailsfl:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = callmaterialdetailsflService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("callmaterialdetailsfl:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			callmaterialdetailsflService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备注2");	//1
		titles.add("备注3");	//2
		titles.add("备注4");	//3
		titles.add("备注5");	//4
		titles.add("备注6");	//5
		titles.add("备注7");	//6
		titles.add("备注8");	//7
		titles.add("备注9");	//8
		titles.add("备注10");	//9
		titles.add("备注11");	//10
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
		titles.add("备注22");	//21
		titles.add("备注23");	//22
		titles.add("备注24");	//23
		titles.add("备注25");	//24
		titles.add("备注26");	//25
		titles.add("备注27");	//26
		titles.add("备注28");	//27
		dataMap.put("titles", titles);
		List<PageData> varOList = callmaterialdetailsflService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CallMaterial_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("Material_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("MaterialName"));	    //3
			vpd.put("var4", varOList.get(i).getString("Specification"));	    //4
			vpd.put("var5", varOList.get(i).getString("DemandNum"));	    //5
			vpd.put("var6", varOList.get(i).getString("QuantityCount"));	    //6
			vpd.put("var7", varOList.get(i).getString("SProp"));	    //7
			vpd.put("var8", varOList.get(i).getString("BatchNum"));	    //8
			vpd.put("var9", varOList.get(i).getString("DeliveryWarehouse"));	    //9
			vpd.put("var10", varOList.get(i).getString("DeliveryPosition"));	    //10
			vpd.put("var11", varOList.get(i).getString("TargetWarehouse"));	    //11
			vpd.put("var12", varOList.get(i).getString("TargetPosition"));	    //12
			vpd.put("var13", varOList.get(i).getString("TType"));	    //13
			vpd.put("var14", varOList.get(i).getString("OperatePersion"));	    //14
			vpd.put("var15", varOList.get(i).getString("OperateTime"));	    //15
			vpd.put("var16", varOList.get(i).getString("DeliveryWarehouseID"));	    //16
			vpd.put("var17", varOList.get(i).getString("DeliveryPositionID"));	    //17
			vpd.put("var18", varOList.get(i).getString("TargetWarehouseID"));	    //18
			vpd.put("var19", varOList.get(i).getString("TargetPositionID"));	    //19
			vpd.put("var20", varOList.get(i).getString("MAT_AUXILIARY_ID"));	    //20
			vpd.put("var21", varOList.get(i).getString("FUnitName"));	    //21
			vpd.put("var22", varOList.get(i).getString("UNIT_INFO_ID"));	    //22
			vpd.put("var23", varOList.get(i).getString("MaterialCode"));	    //23
			vpd.put("var24", varOList.get(i).getString("MAT_AUXILIARYMX_ID"));	    //24
			vpd.put("var25", varOList.get(i).getString("IsCheck"));	    //25
			vpd.put("var26", varOList.get(i).getString("CallMaterialFL_ID"));	    //26
			vpd.put("var27", varOList.get(i).getString("CallMaterialDetails_ID"));	    //27
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
