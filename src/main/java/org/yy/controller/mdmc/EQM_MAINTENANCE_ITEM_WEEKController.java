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
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mdmc.EQM_MAINTENANCE_ITEM_WEEKService;

/** 
 * 说明：设备保养项周模板
 * 作者：YuanYe
 * 时间：2020-06-18
 * 
 */
@Controller
@RequestMapping("/eqm_maintenance_item_week")
public class EQM_MAINTENANCE_ITEM_WEEKController extends BaseController {
	
	@Autowired
	private EQM_MAINTENANCE_ITEM_WEEKService eqm_maintenance_item_weekService;
	
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
		pd.put("EQM_MAINTENANCE_ITEM_WEEK_ID", this.get32UUID());	//主键
		pd.put("FCREATOR", Jurisdiction.getUsername());
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));
		pd.put("LAST_MODIFIER", Jurisdiction.getUsername());
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		eqm_maintenance_item_weekService.save(pd);
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
		eqm_maintenance_item_weekService.delete(pd);
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
		pd.put("LAST_MODIFIER", Jurisdiction.getUsername());
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		eqm_maintenance_item_weekService.edit(pd);
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
		List<PageData>	varList = eqm_maintenance_item_weekService.list(page);	//列出EQM_MAINTENANCE_ITEM_WEEK列表
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
		pd = eqm_maintenance_item_weekService.findById(pd);	//根据ID读取
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
			eqm_maintenance_item_weekService.deleteAll(ArrayDATA_IDS);
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
		titles.add("保养内容");	//1
		titles.add("备注");	//2
		titles.add("创建人");	//3
		titles.add("创建时间");	//4
		titles.add("最后修改人");	//5
		titles.add("最后修改时间");	//6
		titles.add("扩展字段1");	//7
		titles.add("扩展字段2");	//8
		titles.add("扩展字段3");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_maintenance_item_weekService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FMAINTENANCE_CONTENT"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNOTE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FCREATOR"));	    //3
			vpd.put("var4", varOList.get(i).getString("CREATE_TIME"));	    //4
			vpd.put("var5", varOList.get(i).getString("LAST_MODIFIER"));	    //5
			vpd.put("var6", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("FEXTEND1"));	    //7
			vpd.put("var8", varOList.get(i).getString("FEXTEND2"));	    //8
			vpd.put("var9", varOList.get(i).getString("FEXTEND3"));	    //9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
