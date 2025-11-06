package org.yy.controller.KeyCustomers;

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
import org.yy.service.KeyCustomers.CustomerGJService;
import org.yy.service.KeyCustomers.WeekReportService;

/** 
 * 说明：客户跟进
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-02
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/CustomerGJ")
public class CustomerGJController extends BaseController {
	
	@Autowired
	private CustomerGJService CustomerGJService;
	@Autowired
	private WeekReportService WeekReportService;
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
		pd.put("CustomerGJ_ID", this.get32UUID());	//主键
		CustomerGJService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	/**保存周报
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/saveWeekReport")
	@ResponseBody
	public Object saveWeekReport() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("WEEKREPORT_ID", this.get32UUID());	//主键
		pd.put("FCREATER", Jurisdiction.getName());
		pd.put("FCREATTIME", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
		CustomerGJService.saveWeekReport(pd);
		map.put("result", errInfo);
		return map;
	}
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editWeekReport")
	@ResponseBody
	public Object editWeekReport() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		WeekReportService.edit(pd);
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
		CustomerGJService.delete(pd);
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
		CustomerGJService.edit(pd);
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
		List<PageData>	varList = CustomerGJService.list(page);	//列出CustomerGJ列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listWeekList")
	@ResponseBody
	public Object listWeekList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList1 = CustomerGJService.LastWeekList(pd);
		List<PageData>	varList2 = CustomerGJService.undoneList(pd);
		List<PageData>	varList3 = CustomerGJService.FinishList(pd);
		map.put("varList1", varList1);
		map.put("varList2", varList2);
		map.put("varList3", varList3);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listToday")
	@ResponseBody
	public Object listToday(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("GJTIME", Tools.date2Str(new Date(), "yyyy-MM-dd"));
		page.setPd(pd);
		List<PageData> varList = CustomerGJService.listToday(page);
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
		pd = CustomerGJService.findById(pd);	//根据ID读取
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
			CustomerGJService.deleteAll(ArrayDATA_IDS);
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
		titles.add("客户");	//1
		titles.add("阶段");	//2
		titles.add("类型");	//3
		titles.add("发布人");	//4
		titles.add("发布时间");	//5
		titles.add("责任人");	//6
		titles.add("截止时间");	//7
		titles.add("客户联系方式");	//8
		titles.add("跟进方式");	//9
		titles.add("跟进任务");	//10
		titles.add("跟进时间");	//11
		titles.add("跟进状态");	//12
		titles.add("反馈时间");	//13
		titles.add("反馈结果");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = CustomerGJService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CUSTOMER_NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("FSTAGE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FCREATER"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCREATTIME"));	    //5
			vpd.put("var6", varOList.get(i).getString("FMANAGER"));	    //6
			vpd.put("var7", varOList.get(i).getString("FENDTIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("CUSTOMER_PHONE"));	    //8
			vpd.put("var9", varOList.get(i).getString("GJTYPE"));	    //9
			vpd.put("var10", varOList.get(i).getString("GJTASK"));	    //10
			vpd.put("var11", varOList.get(i).getString("GJTIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("GJSTATE"));	    //12
			vpd.put("var13", varOList.get(i).getString("FKTIME"));	    //13
			vpd.put("var14", varOList.get(i).getString("FKCONTENT"));	    //14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
