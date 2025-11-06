package org.yy.controller.project.manager;

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
import org.yy.service.project.manager.PRESALEPLANONEService;
import org.yy.service.project.manager.PRESALEPLANTWOService;

/** 
 * 说明：售前方案计划一级明细
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/presaleplanone")
public class PRESALEPLANONEController extends BaseController {
	
	@Autowired
	private PRESALEPLANONEService presaleplanoneService;
	
	@Autowired
	private PRESALEPLANTWOService presaleplantwoService;
	
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
		pd.put("PRESALEPLANONE_ID", this.get32UUID());	//主键
		pd.put("FCREATETIME", Tools.date2Str(new Date()));
		pd.put("FFOUNDER", Jurisdiction.getName());
		pd.put("FFOUNDERACCOUNT", Jurisdiction.getName());
		presaleplanoneService.save(pd);
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
		presaleplanoneService.delete(pd);
		presaleplantwoService.deleteAll(pd);
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
		pd.put("FMODIFIERSTIME", Tools.date2Str(new Date()));
		pd.put("FMODIFIERS", Jurisdiction.getName());
		pd.put("FMODIFIERSACCOUNT", Jurisdiction.getName());
		presaleplanoneService.edit(pd);
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
		List<PageData>	varList = presaleplanoneService.list(page);	//列出PRESALEPLANONE列表
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
		pd = presaleplanoneService.findById(pd);	//根据ID读取
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
			presaleplanoneService.deleteAll(ArrayDATA_IDS);
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
		titles.add("序号");	//1
		titles.add("名称描述");	//2
		titles.add("排序");	//3
		titles.add("创建时间");	//4
		titles.add("创建人");	//5
		titles.add("创建人账号");	//6
		titles.add("修改人");	//7
		titles.add("修改人账号");	//8
		titles.add("修改时间");	//9
		titles.add("预留字段1");	//10
		titles.add("预留字段2");	//11
		titles.add("预留字段3");	//12
		titles.add("预留字段4");	//13
		titles.add("预留字段5");	//14
		titles.add("主表ID");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = presaleplanoneService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNUMBER"));	    //1
			vpd.put("var2", varOList.get(i).getString("FDESCRIPTION"));	    //2
			vpd.put("var3", varOList.get(i).getString("FSORT"));	    //3
			vpd.put("var4", varOList.get(i).getString("FCREATETIME"));	    //4
			vpd.put("var5", varOList.get(i).getString("FFOUNDER"));	    //5
			vpd.put("var6", varOList.get(i).getString("FFOUNDERACCOUNT"));	    //6
			vpd.put("var7", varOList.get(i).getString("FMODIFIERS"));	    //7
			vpd.put("var8", varOList.get(i).getString("FMODIFIERSACCOUNT"));	    //8
			vpd.put("var9", varOList.get(i).getString("FMODIFIERSTIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("FRESERVE1"));	    //10
			vpd.put("var11", varOList.get(i).getString("FRESERVE2"));	    //11
			vpd.put("var12", varOList.get(i).getString("FRESERVE3"));	    //12
			vpd.put("var13", varOList.get(i).getString("FRESERVE4"));	    //13
			vpd.put("var14", varOList.get(i).getString("FRESERVE5"));	    //14
			vpd.put("var15", varOList.get(i).getString("PRESALEPLAN_ID"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
