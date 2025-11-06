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
import org.yy.service.pp.PurchaseApplyForService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.pp.PurchaseApplyForMxService;

/** 
 * 说明：采购申请
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PurchaseApplyFor")
public class PurchaseApplyForController extends BaseController {
	
	@Autowired
	private PurchaseApplyForService PurchaseApplyForService;
	
	@Autowired
	private PurchaseApplyForMxService PurchaseApplyFormxService;
	
	@Autowired
	private StaffService staffService;//人员
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("PurchaseApplyFor:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PurchaseApplyFor_ID", this.get32UUID());	//主键
		PurchaseApplyForService.save(pd);
		pd = PurchaseApplyForService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**采购申请保存
	 * 1.生成采购申请主表，根据当前登录人、生成主表状态为“Y”条件查询下推未生成主表的明细物料
	 * 如果明细物料list的size为0不保存主表返回，反则循环列表更新明细表的主表ID与生成主表状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/create")
	//@RequiresPermissions("PurchaseApplyFor:add")
	@ResponseBody
	public Object create() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		String PurchaseApplyFor_ID = this.get32UUID();
		PageData pd = new PageData();
		PageData pdOp = new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pd = this.getPageData();
		pd.put("PurchaseApplyFor_ID", PurchaseApplyFor_ID);	//主键
		pd.put("FState", "创建");	//状态
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pdOp).getString("STAFF_ID"));	//制单人ID
		pd.put("FMakeBillsTime", Tools.date2Str(new Date()));	//制单时间
		pd.put("FPushPeopleID", staffService.getStaffId(pdOp).getString("STAFF_ID"));	//当前登录人
		List<PageData> varList=PurchaseApplyFormxService.listSeepushAll(pd);
		if(varList.size()>0) {
			for (int i = 0; i < varList.size(); i++) {
				PageData pdMx = new PageData();
				pdMx=varList.get(i);
				pdMx.put("PurchaseApplyFor_ID", PurchaseApplyFor_ID);//主表ID
				pdMx.put("FIfCreate", "Y");//是否生成主表
				PurchaseApplyFormxService.creact(pdMx);//更新主表ID与状态
			}
			PurchaseApplyForService.save(pd);
		}else {
			errInfo="error";
		}
		//pd = PurchaseApplyForService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("PurchaseApplyFor:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(Integer.parseInt(PurchaseApplyFormxService.findCount(pd).get("zs").toString()) > 0){
				errInfo = "error";
			}else{
				PurchaseApplyForService.delete(pd);
			}
		} catch(Exception e){
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
	//@RequiresPermissions("PurchaseApplyFor:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseApplyForService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("PurchaseApplyFor:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = PurchaseApplyForService.list(page);	//列出PurchaseApplyFor列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**审批列表
	 * 只查询下发和通过的数据
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAudit")
	//@RequiresPermissions("PurchaseApplyFor:list")
	@ResponseBody
	public Object listAudit(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = PurchaseApplyForService.listAudit(page);	//列出PurchaseApplyFor列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("PurchaseApplyFor:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PurchaseApplyForService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	 /**更新审核状态
	  * 前台传2个参数1个主键id，1个状态值，通过id更新状态值
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editState")
	//@RequiresPermissions("PurchaseApplyFor:edit")
	@ResponseBody
	public Object editState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseApplyForService.editState(pd);//更新审核状态
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**更新审批状态
	  * 审批审批前台传3个参数1个主键id，1个状态值和审批意见通过id更新状态值、审批意见、审批人、审批时间
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editAudit")
	//@RequiresPermissions("PurchaseApplyFor:edit")
	@ResponseBody
	public Object editAudit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdOp = new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pd = this.getPageData();
		pd.put("FCheckTime", Tools.date2Str(new Date()));	//审批时间
		pd.put("FCheckPersonID", staffService.getStaffId(pdOp).getString("STAFF_ID"));	//审批人
		PurchaseApplyForService.editAudit(pd);//更新审核状态
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	  * 采购申请导出带明细
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
		titles.add("采购申请单号");	//1
		titles.add("描述");	//2
		titles.add("审核状态");	//3
		titles.add("制单人");	//4
		titles.add("制单时间");	//5
		titles.add("物料名称");	//6
		titles.add("物料代码");	//7
		titles.add("规格描述");	//8
		titles.add("数量");	//9
		titles.add("单位");	//10
		titles.add("图号");	//11
		titles.add("标准号");	//12
		titles.add("销售订单");	//13
		titles.add("产品物料");	//14
		titles.add("计划单号");	//15
		titles.add("供应商");	//16
		titles.add("类型");	//17
		dataMap.put("titles", titles);
		List<PageData> varOList = PurchaseApplyForService.listExcelAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNum"));	    //1
			vpd.put("var2", varOList.get(i).getString("FDescribe"));	    //2
			vpd.put("var3", varOList.get(i).getString("FState"));	    //3
			vpd.put("var4", varOList.get(i).getString("FMakeBillsPerso"));	    //4
			vpd.put("var5", varOList.get(i).getString("FMakeBillsTime"));	    //5
			vpd.put("var6", varOList.get(i).getString("MAT_NAME"));	    //6
			vpd.put("var7", varOList.get(i).getString("MAT_CODE"));	    //7
			vpd.put("var8", varOList.get(i).getString("SpecificationsDesc"));	    //8
			vpd.put("var9", varOList.get(i).get("FProductQuantity").toString());	    //9
			vpd.put("var10", varOList.get(i).getString("UnitName"));	    //10
			vpd.put("var11", varOList.get(i).getString("FigureNum"));	    //11
			vpd.put("var12", varOList.get(i).getString("StandardNum"));	    //12
			vpd.put("var13", varOList.get(i).getString("OrderNum"));	    //13
			vpd.put("var14", varOList.get(i).getString("ORDERMAT_NAME"));	    //14
			vpd.put("var15", varOList.get(i).getString("WorkOrderNum"));	    //15
			vpd.put("var16", varOList.get(i).getString("FSupplier"));	    //16
			vpd.put("var17", varOList.get(i).getString("FType"));	    //17
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
