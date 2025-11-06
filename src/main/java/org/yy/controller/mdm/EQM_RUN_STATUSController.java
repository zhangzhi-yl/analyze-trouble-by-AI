package org.yy.controller.mdm;

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
import org.yy.service.mdm.EQM_RUN_STATUSService;

/** 
 * 说明：设备运行状态
 * 作者：YuanYe
 * 时间：2020-02-20
 * 
 */
@Controller
@RequestMapping("/eqm_run_status")
public class EQM_RUN_STATUSController extends BaseController {
	
	@Autowired
	private EQM_RUN_STATUSService eqm_run_statusService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_run_status:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_RUN_STATUS_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		eqm_run_statusService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_run_status:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_run_statusService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_run_status:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_run_statusService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_run_status:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_run_statusService.list(page);	//列出EQM_RUN_STATUS列表
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
	//@RequiresPermissions("eqm_run_status:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_run_statusService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_run_status:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_run_statusService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备基础资料ID");	//1
		titles.add("设备名称");	//2
		titles.add("设备编号");	//3
		titles.add("设备类型");	//4
		titles.add("工作中心");	//5
		titles.add("操作开始时间");	//6
		titles.add("操作结束时间");	//7
		titles.add("用时");	//8
		titles.add("计划单号");	//9
		titles.add("温度");	//10
		titles.add("压力");	//11
		titles.add("转速");	//12
		titles.add("数量");	//13
		titles.add("操作人");	//14
		titles.add("异常状态");	//15
		titles.add("备注");	//16
		titles.add("创建人");	//17
		titles.add("创建时间");	//18
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_run_statusService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_BASE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FEQM_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FEQM_FNUMBER"));	    //3
			vpd.put("var4", varOList.get(i).getString("FEQM_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FWORKCENTER"));	    //5
			vpd.put("var6", varOList.get(i).getString("FSTART_TIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("FEND_TIME"));	    //7
			vpd.put("var8", varOList.get(i).get("FWITHTIME").toString());	//8
			vpd.put("var9", varOList.get(i).getString("FPLAN_NUMBER"));	    //9
			vpd.put("var10", varOList.get(i).get("FTEMPERATURE").toString());	//10
			vpd.put("var11", varOList.get(i).getString("PERSSURE"));	    //11
			vpd.put("var12", varOList.get(i).getString("FREV"));	    //12
			vpd.put("var13", varOList.get(i).get("FQUANTITY").toString());	//13
			vpd.put("var14", varOList.get(i).getString("FOPERATOR"));	    //14
			vpd.put("var15", varOList.get(i).getString("FEXCEPT_STATE"));	    //15
			vpd.put("var16", varOList.get(i).getString("FDAS"));	    //16
			vpd.put("var17", varOList.get(i).getString("FCREATOR"));	    //17
			vpd.put("var18", varOList.get(i).getString("CREATE_TIME"));	    //18
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
