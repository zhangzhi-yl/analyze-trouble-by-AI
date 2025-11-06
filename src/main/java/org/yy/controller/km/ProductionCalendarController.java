package org.yy.controller.km;

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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.km.ProductionCalendarService;

/** 
 * 说明：生产日历
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-14
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ProductionCalendar")
public class ProductionCalendarController extends BaseController {
	
	@Autowired
	private ProductionCalendarService ProductionCalendarService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("ProductionCalendar:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ProductionCalendar_ID", this.get32UUID());	//主键
		ProductionCalendarService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("ProductionCalendar:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProductionCalendarService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("ProductionCalendar:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProductionCalendarService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("ProductionCalendar:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = ProductionCalendarService.list(page);	//列出ProductionCalendar列表
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
	@RequiresPermissions("ProductionCalendar:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ProductionCalendarService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("ProductionCalendar:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ProductionCalendarService.deleteAll(ArrayDATA_IDS);
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
		titles.add("适合工位");	//1
		titles.add("适用日期类型");	//2
		titles.add("适用日期值");	//3
		titles.add("状态");	//4
		titles.add("是否工作日");	//5
		titles.add("工作时间");	//6
		titles.add("优先级");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = ProductionCalendarService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("SUITABLEFORSTATION"));	    //1
			vpd.put("var2", varOList.get(i).getString("APPLICABLEDATETYPE"));	    //2
			vpd.put("var3", varOList.get(i).getString("APPLICABLEDATEVALUE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FSTATUS"));	    //4
			vpd.put("var5", varOList.get(i).getString("WEEKDAYIF"));	    //5
			vpd.put("var6", varOList.get(i).getString("WORKINGHOURS"));	    //6
			vpd.put("var7", varOList.get(i).get("FPRIORITYID").toString());	//7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@RequestMapping(value="/dayList")
	@ResponseBody
	public Object dayList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			List<PageData> varList = ProductionCalendarService.listAll(pd);
			List<Map<String, Object>> varJsons=new ArrayList<Map<String, Object>>();
			for (int i = 0; i < varList.size(); i++) {
				PageData pd1=varList.get(i);
				Map<String, Object> mapjson = new HashMap<String, Object>();
				mapjson.put("title","适合工位:"+pd1.getString("SuitableForStation")+"<br>状态:"+pd1.get("FStatus"));
				mapjson.put("start", pd1.get("WorkingHours"));
				mapjson.put("end", pd1.get("WorkingHours"));
				//mapjson.put("end", pd1.get("END_TIMEDAY"));
				if(pd1.get("WorkingHours").equals(pd1.get("WorkingHours"))) {
					mapjson.put("className", "label-grey");
				}else {
					mapjson.put("className", "label-purple");
				}
				mapjson.put("id", pd1.get("ProductionCalendar_ID"));
				varJsons.add(mapjson);
			}
			if(varJsons.size()>0){
					map.put("strjson",varJsons);
					map.put("ftype",200);
			}else{
				map.put("ftype",500);
			}
			
			map.put("msg","ok");
			map.put("msgText","查询成功！");
		}catch (Exception e){
			result = "500";
			map.put("msg","no");
			map.put("msgText","未知错误，请联系管理员！");
		}finally{
			map.put("result", result);
		}
		return map;
	}
}
