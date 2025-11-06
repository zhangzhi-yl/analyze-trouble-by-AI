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
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mm.TakeStock_Task_ExecuteService;
import org.yy.service.mm.Takestock_TaskService;

/** 
 * 说明：盘点任务执行明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-28
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/TakeStock_Task_Execute")
public class TakeStock_Task_ExecuteController extends BaseController {
	
	@Autowired
	private TakeStock_Task_ExecuteService TakeStock_Task_ExecuteService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private Takestock_TaskService takestock_taskService;
	/**盘点执行，扫码新增
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("TakeStock_Task_Execute:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		String msg = "";
		PageData pd = new PageData();
		PageData pdMa = new PageData();
		PageData pdStaff = new PageData();
		pd = this.getPageData();
		pd.put("FNAME",Jurisdiction.getName());					//获取当前登录人的中文名称
		pdStaff = staffService.getStaffId(pd);						//根据人员的中文姓名获取人员ID
		pdMa = TakeStock_Task_ExecuteService.getMaterial(pd);//根据物料ID从盘点任务明细表中获取数据，判断当前扫码的物料是否存在于表中
		if(pdMa != null){//判断 ，如果当前扫码的物料存在于盘点任务物料明细表中
			Integer EnteryID = TakeStock_Task_ExecuteService.getEntryID(pdMa);
			pd.put("TakeStock_Task_Execute_ID", this.get32UUID());	//主键
			pd.put("FEntryID", EnteryID+1);//行号
			pd.put("Warehouse_ID","");//仓库ID 默认空
			pd.put("Seat_ID", "");//仓位ID  默认空
			pd.put("TakeStock_Task_Material_ID", pd.get("TakeStock_Task_Material_ID"));//盘点任务物料ID
			pd.put("TakeStock_Count", 1);//数量 默认1 
			pd.put("TakeStock_Task_PersonID", pdStaff.get("STAFF_ID"));//盘点人
			pd.put("TakeStock_Task_Time", Tools.date2Str(new Date()));//盘点时间 默认当前时间
			pd.put("IfFEntryClose", 1);// 行关闭  默认未关闭
			pd.put("FExplanation", "");//描述，默认为空 
			pd.put("DifferenceOrder_NUM", pd.get("DifferenceOrder_NUM"));//差异单批号
			TakeStock_Task_ExecuteService.save(pd);
			msg = "扫描成功";
		}else{
			errInfo = "error";
			 msg = "该物料不存在于此盘点任务中，请核实";//错误提示
		}
		map.put("msg", msg);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 盘点完成
	 */
	@RequestMapping(value="FinishTakeStock")
	@ResponseBody
	public Object FinishTakeStock() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		TakeStock_Task_ExecuteService.FinishTakeStock(pd);
		map.put("result", errInfo);
		return map;
	}
	
	
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("TakeStock_Task_Execute:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		TakeStock_Task_ExecuteService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("TakeStock_Task_Execute:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		TakeStock_Task_ExecuteService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("TakeStock_Task_Execute:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = TakeStock_Task_ExecuteService.list(page);	//列出TakeStock_Task_Execute列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 行关闭
	 */
	@RequestMapping(value="CloseHang")
	@ResponseBody
	public Object CloseHang() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		String errInfo = "success";
		pd = this.getPageData();
		TakeStock_Task_ExecuteService.CloseHang(pd);
		map.put("result", errInfo);
		return map;
	}
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("TakeStock_Task_Execute:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = TakeStock_Task_ExecuteService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("TakeStock_Task_Execute:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			TakeStock_Task_ExecuteService.deleteAll(ArrayDATA_IDS);
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
		titles.add("盘点任务执行ID");	//1
		titles.add("行号");	//2
		titles.add("仓库ID");	//3
		titles.add("库位ID");	//4
		titles.add("盘点任务ID");	//5
		titles.add("物料ID");	//6
		titles.add("盘点数量");	//7
		titles.add("盘点人ID");	//8
		titles.add("盘点时间");	//9
		titles.add("是否行关闭  1是 0否");	//10
		titles.add("描述");	//11
		titles.add("差异单批号");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = TakeStock_Task_ExecuteService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TakeStock_Task_Execute_ID"));	    //1
			vpd.put("var2", varOList.get(i).get("FEntryID").toString());	//2
			vpd.put("var3", varOList.get(i).getString("Warehouse_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("Seat_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("TakeStock_Task_ID"));	    //5
			vpd.put("var6", varOList.get(i).getString("Material_ID"));	    //6
			vpd.put("var7", varOList.get(i).get("TakeStock_Count").toString());	//7
			vpd.put("var8", varOList.get(i).getString("TakeStock_Task_PersonID"));	    //8
			vpd.put("var9", varOList.get(i).getString("TakeStock_Task_Time"));	    //9
			vpd.put("var10", varOList.get(i).get("IfFEntryClose").toString());	//10
			vpd.put("var11", varOList.get(i).getString("FExplanation"));	    //11
			vpd.put("var12", varOList.get(i).getString("DifferenceOrder_NUM"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
