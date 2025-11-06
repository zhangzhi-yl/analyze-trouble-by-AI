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
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.github.pagehelper.util.StringUtil;

import org.yy.entity.PageData;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.pp.PlanningWorkOrderService;

/** 
 * 说明：物料需求单
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-12
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/MaterialRequirement")
public class MaterialRequirementController extends BaseController {
	
	@Autowired
	private MaterialRequirementService MaterialRequirementService;
	
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DateBatch", Tools.date2Str(new Date()));
		pd.put("FMakeBillsPersoID", Jurisdiction.getUserId());
		pd.put("FMakeBillsTime", Tools.date2Str(new Date()));
		pd.put("MaterialRequirement_ID", this.get32UUID());	//主键
		MaterialRequirementService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialRequirementService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		if(StringUtil.isNotEmpty(pd.getString("PlanningWorkOrderID"))){
			PageData pData = new PageData();
			pData.put("PlanningWorkOrder_ID", pd.getString("PlanningWorkOrderID"));
			PageData findById = PlanningWorkOrderService.findById(pData);
			if(null!=findById){
				String BatchNum = findById.getString("BatchNum");
				pd.put("BatchNum", BatchNum);
				String WorkOrderCreateTime = findById.getString("WorkOrderCreateTime");
				pd.put("DemandTime", WorkOrderCreateTime);
				PageData mat_auxiliarymxParam = new PageData();
				mat_auxiliarymxParam.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
				mat_auxiliarymxParam.put("MAT_AUXILIARYMX_CODE", findById.getString("MasterWorkOrderNum"));
				List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
						.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(mat_auxiliarymxParam);
				String aux_mx_id = "";
				if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
					aux_mx_id = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0).getString("MAT_AUXILIARYMX_ID");
				}
				pd.put("SProp", "2540539e45324232a50bde60ac2951d3");
				pd.put("SPropKey", aux_mx_id);
			}
			
		}
		MaterialRequirementService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**MaterialRequirement列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/MaterialRequirementlistPage")
	@ResponseBody
	public Object MaterialRequirementlistPage(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = MaterialRequirementService.MaterialRequirementlistPage(page);	//列出MaterialRequirement列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = MaterialRequirementService.list(page);	//列出MaterialRequirement列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**物料需求单列表
	 * @param pd 
	 * @throws Exception
	 */
	@RequestMapping(value="/getlist")
	@ResponseBody
	public Object getMaterialRequirementList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = MaterialRequirementService.getMaterialRequirementList(pd);	//列出MaterialRequirement列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = MaterialRequirementService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			MaterialRequirementService.deleteAll(ArrayDATA_IDS);
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
		dataMap.put("titles", titles);
		List<PageData> varOList = MaterialRequirementService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("RowNum").toString());	//1
			vpd.put("var2", varOList.get(i).getString("RowClose"));	    //2
			vpd.put("var3", varOList.get(i).getString("PlanningWorkOrderID"));	    //3
			vpd.put("var4", varOList.get(i).getString("MaterialNum"));	    //4
			vpd.put("var5", varOList.get(i).getString("MaterialName"));	    //5
			vpd.put("var6", varOList.get(i).getString("SProp"));	    //6
			vpd.put("var7", varOList.get(i).getString("SPropKey"));	    //7
			vpd.put("var8", varOList.get(i).getString("DemandCount"));	    //8
			vpd.put("var9", varOList.get(i).get("IssuedQuantity").toString());	//9
			vpd.put("var10", varOList.get(i).getString("DeliveryWarehouse"));	    //10
			vpd.put("var11", varOList.get(i).getString("TargetPosition"));	    //11
			vpd.put("var12", varOList.get(i).getString("DemandTime"));	    //12
			vpd.put("var13", varOList.get(i).getString("WorkOrderNum"));	    //13
			vpd.put("var14", varOList.get(i).getString("DateBatch"));	    //14
			vpd.put("var15", varOList.get(i).getString("WP"));	    //15
			vpd.put("var16", varOList.get(i).getString("TType"));	    //16
			vpd.put("var17", varOList.get(i).getString("FStatus"));	    //17
			vpd.put("var18", varOList.get(i).getString("OccupationOfInventory"));	    //18
			vpd.put("var19", varOList.get(i).getString("FMakeBillsPersoID"));	    //19
			vpd.put("var20", varOList.get(i).getString("FMakeBillsTime"));	    //20
			vpd.put("var21", varOList.get(i).getString("PushDownTransferIF"));	    //21
			vpd.put("var22", varOList.get(i).getString("PushDownPurchaseIF"));	    //22
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**获取物料需求编号列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-12-7
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getNumList")
	@ResponseBody
	public Object getNumList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = MaterialRequirementService.getNumList(pd);	
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
