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
import org.yy.service.mdmc.EQM_MAINTENANCE_ITEM_MAINService;
import org.yy.service.mdmc.EQM_MAINTENANCE_ITEM_MONTHService;
import org.yy.service.mdmc.EQM_MAINTENANCE_ITEM_QUARTERService;
import org.yy.service.mdmc.EQM_MAINTENANCE_ITEM_WEEKService;

/** 
 * 说明：设备保养项主模板
 * 作者：YuanYe
 * 时间：2020-06-18
 * 
 */
@Controller
@RequestMapping("/eqm_maintenance_item_main")
public class EQM_MAINTENANCE_ITEM_MAINController extends BaseController {
	
	@Autowired
	private EQM_MAINTENANCE_ITEM_MAINService eqm_maintenance_item_mainService;
	
	@Autowired
	private EQM_MAINTENANCE_ITEM_MONTHService eqm_maintenance_item_monthService;
	
	@Autowired
	private EQM_MAINTENANCE_ITEM_QUARTERService eqm_maintenance_item_quarterService;
	
	@Autowired
	private EQM_MAINTENANCE_ITEM_WEEKService eqm_maintenance_item_weekService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("eqm_maintenance_item_main:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_MAINTENANCE_ITEM_MAIN_ID", this.get32UUID());	//主键
		pd.put("FCREATOR", Jurisdiction.getUsername());
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));
		pd.put("LAST_MODIFIER", Jurisdiction.getUsername());
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		eqm_maintenance_item_mainService.save(pd);
		pd = eqm_maintenance_item_mainService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("eqm_maintenance_item_main:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			//if(Integer.parseInt(eqm_maintenance_item_monthService.findCount(pd).get("zs").toString()) > 0||Integer.parseInt(eqm_maintenance_item_quarterService.findCount(pd).get("zs").toString()) > 0||Integer.parseInt(eqm_maintenance_item_weekService.findCount(pd).get("zs").toString()) > 0){
				//errInfo = "error";
				eqm_maintenance_item_weekService.deleteByFatherID(pd);
				eqm_maintenance_item_monthService.deleteByFatherID(pd);
				eqm_maintenance_item_quarterService.deleteByFatherID(pd);
				eqm_maintenance_item_mainService.delete(pd);
			//}else{
			//	eqm_maintenance_item_mainService.delete(pd);
			//}
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
	@RequiresPermissions("eqm_maintenance_item_main:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("LAST_MODIFIER", Jurisdiction.getUsername());
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		eqm_maintenance_item_mainService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("eqm_maintenance_item_main:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = eqm_maintenance_item_mainService.list(page);	//列出EQM_MAINTENANCE_ITEM_MAIN列表
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
	@RequiresPermissions("eqm_maintenance_item_main:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_maintenance_item_mainService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
		titles.add("设备保养项主键");	//1
		titles.add("编号（自动生成）");	//2
		titles.add("设备类主键");	//3
		titles.add("设备类标识");	//4
		titles.add("设备类名称");	//5
		titles.add("备注");	//6
		titles.add("创建人");	//7
		titles.add("创建时间");	//8
		titles.add("最后修改人");	//9
		titles.add("最后修改时间");	//10
		titles.add("扩展字段1");	//11
		titles.add("扩展字段2");	//12
		titles.add("扩展字段3");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_maintenance_item_mainService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_MAINTENANCE_ITEM_MAIN_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FBILLNO"));	    //2
			vpd.put("var3", varOList.get(i).getString("FCLASS_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("FCLASS_IDENTIFY"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCLASS_NAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("FNOTE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FCREATOR"));	    //7
			vpd.put("var8", varOList.get(i).getString("CREATE_TIME"));	    //8
			vpd.put("var9", varOList.get(i).getString("LAST_MODIFIER"));	    //9
			vpd.put("var10", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("FEXTEND1"));	    //11
			vpd.put("var12", varOList.get(i).getString("FEXTEND2"));	    //12
			vpd.put("var13", varOList.get(i).getString("FEXTEND3"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
