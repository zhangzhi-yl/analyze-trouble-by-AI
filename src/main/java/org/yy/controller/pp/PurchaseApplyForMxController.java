package org.yy.controller.pp;

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
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.pp.PurchaseApplyForMxService;

/** 
 * 说明：采购申请(明细)
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PurchaseApplyForMx")
public class PurchaseApplyForMxController extends BaseController {
	
	@Autowired
	private PurchaseApplyForMxService PurchaseApplyForMxService;
	@Autowired
	private StaffService staffService;//人员
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
		pd.put("PurchaseApplyForMx_ID", this.get32UUID());	//主键
		PurchaseApplyForMxService.save(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseApplyForMxService.delete(pd);
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
		PurchaseApplyForMxService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * 更新采购申请数量
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editQty")
	@ResponseBody
	public Object editQty() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseApplyForMxService.editQty(pd);
		map.put("result", errInfo);				//返回结果
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
		List<PageData>	varList = PurchaseApplyForMxService.list(page);	//列出PurchaseApplyForMx列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);									//返回结果
		return map;
	}
	
	/**已选物料列表
	 * 查询推送人ID是当前登录人ID的是否生成主表为N的数据
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listSeepush")
	@ResponseBody
	public Object listSeepush(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdOp = new PageData();
		pd = this.getPageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("FPushPeopleID", staffService.getStaffId(pdOp).getString("STAFF_ID"));
		page.setPd(pd);
		List<PageData>	varList = PurchaseApplyForMxService.listSeepush(page);	//列出PurchaseApplyForMx列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);									//返回结果
		return map;
	}
	
	 /**去修改页面
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
		pd = PurchaseApplyForMxService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
			PurchaseApplyForMxService.deleteAll(ArrayDATA_IDS);
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
		titles.add("物料ID");	//1
		titles.add("规格描述");	//2
		titles.add("图号");	//3
		titles.add("标准号");	//4
		titles.add("产品数量");	//5
		titles.add("销售订单ID");	//6
		titles.add("销售订单下物料ID");	//7
		titles.add("计划工单ID");	//8
		titles.add("供应商");	//9
		titles.add("是否生成主表");	//10
		titles.add("推送人ID");	//11
		titles.add("推送时间");	//12
		titles.add("类型");	//13
		titles.add("毛坯");	//14
		titles.add("备注");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = PurchaseApplyForMxService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MaterialID"));	    //1
			vpd.put("var2", varOList.get(i).getString("SpecificationsDesc"));	    //2
			vpd.put("var3", varOList.get(i).getString("FigureNum"));	    //3
			vpd.put("var4", varOList.get(i).getString("StandardNum"));	    //4
			vpd.put("var5", varOList.get(i).get("FProductQuantity").toString());	//5
			vpd.put("var6", varOList.get(i).getString("SalesOrder_ID"));	    //6
			vpd.put("var7", varOList.get(i).getString("SalesOrderMaterial_ID"));	    //7
			vpd.put("var8", varOList.get(i).getString("PlanningWorkOrder_ID"));	    //8
			vpd.put("var9", varOList.get(i).getString("FSupplier"));	    //9
			vpd.put("var10", varOList.get(i).getString("FIfCreate"));	    //10
			vpd.put("var11", varOList.get(i).getString("FPushPeopleID"));	    //11
			vpd.put("var12", varOList.get(i).getString("FPushTime"));	    //12
			vpd.put("var13", varOList.get(i).getString("FType"));	    //13
			vpd.put("var14", varOList.get(i).getString("FWorkblank"));	    //14
			vpd.put("var15", varOList.get(i).getString("FRemark"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
