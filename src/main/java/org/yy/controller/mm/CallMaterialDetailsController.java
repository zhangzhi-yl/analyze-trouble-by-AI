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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.github.pagehelper.util.StringUtil;

import org.yy.entity.PageData;
import org.yy.service.mm.CallMaterialDetailsFLService;
import org.yy.service.mm.CallMaterialDetailsService;

/** 
 * 说明：叫料申请明细
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-25
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/CallMaterialDetails")
public class CallMaterialDetailsController extends BaseController {
	
	@Autowired
	private CallMaterialDetailsService CallMaterialDetailsService;
	@Autowired
	private CallMaterialDetailsFLService callmaterialdetailsflService;
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
		pd.put("CallMaterialDetails_ID", this.get32UUID());	//主键
		pd.put("OperatePersion", Jurisdiction.getName());
		pd.put("OperateTime", Tools.date2Str(new Date()));
		CallMaterialDetailsService.save(pd);
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
		CallMaterialDetailsService.delete(pd);
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
		CallMaterialDetailsService.edit(pd);
		
		if (!StringUtil.isEmpty(pd.getString("TargetWarehouse"))
				&&! StringUtil.isEmpty(pd.getString("TargetPosition"))) {
			callmaterialdetailsflService.editTargetWarehouse(pd);//通过物料ID更新接收仓库
		}
		map.put("result", errInfo);
		return map;
	}
	/**v1 管悦 20210907 一键修改仓库
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editAll")
	@ResponseBody
	public Object editAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = CallMaterialDetailsService.listAll(pd);
		for(PageData pdx :varList) {
			if (!StringUtil.isEmpty(pd.getString("TargetWarehouse"))
					&&! StringUtil.isEmpty(pd.getString("TargetPosition"))) {
				pdx.put("TargetWarehouseID", pd.getString("TargetWarehouseID"));
				pdx.put("TargetPositionID", pd.getString("TargetPositionID"));
				pdx.put("TargetWarehouse", pd.getString("TargetWarehouse"));
				pdx.put("TargetPosition", pd.getString("TargetPosition"));
			}
			if (!StringUtil.isEmpty(pd.getString("DeliveryWarehouse"))
					&&! StringUtil.isEmpty(pd.getString("DeliveryPosition"))) {
				pdx.put("DeliveryWarehouseID", pd.getString("DeliveryWarehouseID"));
				pdx.put("DeliveryPositionID", pd.getString("DeliveryPositionID"));
				pdx.put("DeliveryWarehouse", pd.getString("DeliveryWarehouse"));
				pdx.put("DeliveryPosition", pd.getString("DeliveryPosition"));
			}
			
			
			CallMaterialDetailsService.edit(pdx);
			
			if (!StringUtil.isEmpty(pd.getString("TargetWarehouse"))
					&&! StringUtil.isEmpty(pd.getString("TargetPosition"))) {
				callmaterialdetailsflService.editTargetWarehouse(pdx);//通过物料ID更新接收仓库
			}
		}
		
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
		List<PageData>	varList = CallMaterialDetailsService.list(page);	//列出CallMaterialDetails列表
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
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = CallMaterialDetailsService.findById(pd);	//根据ID读取
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
			CallMaterialDetailsService.deleteAll(ArrayDATA_IDS);
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
		dataMap.put("titles", titles);
		List<PageData> varOList = CallMaterialDetailsService.listAll(pd);
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
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
