package org.yy.controller.mdmc;

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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mdmc.EQM_MAINTENANCE_FIRST_CARDMXService;

/** 
 * 说明：一级保养卡明细
 * 作者：YuanYe
 * 时间：2020-06-22
 * 
 */
@Controller
@RequestMapping("/eqm_maintenance_first_cardmx")
public class EQM_MAINTENANCE_FIRST_CARDMXController extends BaseController {
	
	@Autowired
	private EQM_MAINTENANCE_FIRST_CARDMXService eqm_maintenance_first_cardmxService;
	
	
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
		pd.put("EQM_MAINTENANCE_FIRST_CARDMX_ID", this.get32UUID());	//主键
		eqm_maintenance_first_cardmxService.save(pd);
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
		eqm_maintenance_first_cardmxService.delete(pd);
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
		pd.put("FDATE", Tools.date2Str(new Date()));
		pd.put("LAST_MODIFIER", Jurisdiction.getUsername());//最后修改人
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));//最后修改时间
		eqm_maintenance_first_cardmxService.edit(pd);
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
		List<PageData>	varList = eqm_maintenance_first_cardmxService.list(page);	//列出EQM_MAINTENANCE_FIRST_CARDMX列表
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
		pd = eqm_maintenance_first_cardmxService.findById(pd);	//根据ID读取
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
			eqm_maintenance_first_cardmxService.deleteAll(ArrayDATA_IDS);
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
		titles.add("保养周计划主键");	//1
		titles.add("保养项主模板主键");	//2
		titles.add("设备主键");	//3
		titles.add("设备类主键");	//4
		titles.add("日期");	//5
		titles.add("保养内容反馈");	//6
		titles.add("保养项周模板主键");	//7
		titles.add("保养项周模板保养内容");	//8
		titles.add("创建人");	//9
		titles.add("创建时间");	//10
		titles.add("最后修改人");	//11
		titles.add("最后修改时间");	//12
		titles.add("扩展字段1");	//13
		titles.add("扩展字段2");	//14
		titles.add("扩展字段3");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_maintenance_first_cardmxService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_MAINTENANCE_PLAN_WEEK_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQM_MAINTENANCE_ITEM_MAIN_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FEQM_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("FCLASS_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("FDATE"));	    //5
			vpd.put("var6", varOList.get(i).getString("FEEDBACK"));	    //6
			vpd.put("var7", varOList.get(i).getString("EQM_MAINTENANCE_ITEM_WEEK_ID"));	    //7
			vpd.put("var8", varOList.get(i).getString("FMAINTENANCE_CONTENT"));	    //8
			vpd.put("var9", varOList.get(i).getString("FCREATOR"));	    //9
			vpd.put("var10", varOList.get(i).getString("CREATE_TIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("LAST_MODIFIER"));	    //11
			vpd.put("var12", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //12
			vpd.put("var13", varOList.get(i).getString("FEXTEND1"));	    //13
			vpd.put("var14", varOList.get(i).getString("FEXTEND2"));	    //14
			vpd.put("var15", varOList.get(i).getString("FEXTEND3"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
