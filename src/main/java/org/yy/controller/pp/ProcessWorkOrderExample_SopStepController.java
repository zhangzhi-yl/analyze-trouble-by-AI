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
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessWorkOrderExample_SopStepService;

/** 
 * 说明：生产任务SOP实例
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-23
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ProcessWorkOrderExample_SopStep")
public class ProcessWorkOrderExample_SopStepController extends BaseController {
	
	@Autowired
	private ProcessWorkOrderExample_SopStepService ProcessWorkOrderExample_SopStepService;
	@Autowired
	private PlanningWorkOrderService planningWorkOrderService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("ProcessWorkOrderExample_SopStep:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ProcessWorkOrderExample_SopStep_ID", this.get32UUID());	//主键
		ProcessWorkOrderExample_SopStepService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("ProcessWorkOrderExample_SopStep:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProcessWorkOrderExample_SopStepService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("ProcessWorkOrderExample_SopStep:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProcessWorkOrderExample_SopStepService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/editMS")
	@ResponseBody
	public Object editMS() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String OperationComment=pd.getString("OperationComment");
		pd=ProcessWorkOrderExample_SopStepService.findById(pd);
		pd.put("OperationComment", OperationComment);
		ProcessWorkOrderExample_SopStepService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/start")
	@ResponseBody
	public Object start() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd=ProcessWorkOrderExample_SopStepService.findById(pd);
		pd.put("Begin_Time", Tools.date2Str(new Date()));
		pd.put("FOperator", Jurisdiction.getName());
		ProcessWorkOrderExample_SopStepService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/end")
	@ResponseBody
	public Object end() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd=ProcessWorkOrderExample_SopStepService.findById(pd);
		pd.put("End_Time", Tools.date2Str(new Date()));
		ProcessWorkOrderExample_SopStepService.edit(pd);
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
		PageData appProcessWorkOrderExampleDetailByPK = planningWorkOrderService
				.appProcessWorkOrderExampleDetailByPK(pd);
		if (null == appProcessWorkOrderExampleDetailByPK) {
			appProcessWorkOrderExampleDetailByPK = new PageData();
		}
		
		String SOP_ID = appProcessWorkOrderExampleDetailByPK.getString("SOP_ID");
		PageData soPageData = new PageData();
		soPageData.put("SopSchemeTemplate_ID", SOP_ID);
		soPageData.put("ProcessWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
		page.setPd(pd);
		List<PageData>	varList = ProcessWorkOrderExample_SopStepService.list(page);	//列出ProcessWorkOrderExample_SopStep列表
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
	@RequiresPermissions("ProcessWorkOrderExample_SopStep:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ProcessWorkOrderExample_SopStepService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("ProcessWorkOrderExample_SopStep:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ProcessWorkOrderExample_SopStepService.deleteAll(ArrayDATA_IDS);
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
		dataMap.put("titles", titles);
		List<PageData> varOList = ProcessWorkOrderExample_SopStepService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FSortTime"));	    //1
			vpd.put("var2", varOList.get(i).getString("ProcessWorkOrderExample_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FIsFirst"));	    //3
			vpd.put("var4", varOList.get(i).getString("StepName"));	    //4
			vpd.put("var5", varOList.get(i).getString("FShowName"));	    //5
			vpd.put("var6", varOList.get(i).getString("FIsAlarm"));	    //6
			vpd.put("var7", varOList.get(i).getString("FType"));	    //7
			vpd.put("var8", varOList.get(i).getString("SopSchemeTemplate_ID"));	    //8
			vpd.put("var9", varOList.get(i).get("FStandardHour").toString());	//9
			vpd.put("var10", varOList.get(i).getString("Begin_Time"));	    //10
			vpd.put("var11", varOList.get(i).getString("End_Time"));	    //11
			vpd.put("var12", varOList.get(i).getString("FOperator"));	    //12
			vpd.put("var13", varOList.get(i).getString("OperationComment"));	    //13
			vpd.put("var14", varOList.get(i).getString("FCreatePersonID"));	    //14
			vpd.put("var15", varOList.get(i).getString("FCreateTime"));	    //15
			vpd.put("var16", varOList.get(i).getString("FNumber"));	    //16
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
