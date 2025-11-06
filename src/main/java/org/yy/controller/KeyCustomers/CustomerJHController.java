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
import org.yy.service.KeyCustomers.CustomerJHService;

/** 
 * 说明：客户计划与执行
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-03
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/CustomerJH")
public class CustomerJHController extends BaseController {
	
	@Autowired
	private CustomerJHService CustomerJHService;
	
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
		pd.put("CustomerJH_ID", this.get32UUID());	//主键
		pd.put("FCREATER", Jurisdiction.getName());
		pd.put("FCREATETIME", Tools.date2Str(new Date(), "yyyy-MM-dd"));
		CustomerJHService.save(pd);
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
		CustomerJHService.delete(pd);
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
		pd.put("FCHANGER", Jurisdiction.getName());
		pd.put("FCHANGETIME", Tools.date2Str(new Date(), "yyyy-MM-dd"));
		CustomerJHService.edit(pd);
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
		List<PageData>	varList = CustomerJHService.list(page);	//列出CustomerJH列表
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
		pd = CustomerJHService.findById(pd);	//根据ID读取
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
			CustomerJHService.deleteAll(ArrayDATA_IDS);
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
		titles.add("客户名称");	//1
		titles.add("客户类型");	//2
		titles.add("阶段");	//3
		titles.add("计划开始时间");	//4
		titles.add("计划结束时间");	//5
		titles.add("实际开始时间");	//6
		titles.add("实际结束时间");	//7
		titles.add("创建人");	//8
		titles.add("创建时间");	//9
		titles.add("最后修改人");	//10
		titles.add("最后修改时间");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = CustomerJHService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CUSTOMER_NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("FTYPE"));	    //2
			vpd.put("var3", varOList.get(i).getString("FSTATE"));	    //3
			vpd.put("var4", varOList.get(i).getString("JHBEGIN"));	    //4
			vpd.put("var5", varOList.get(i).getString("JHFINISH"));	    //5
			vpd.put("var6", varOList.get(i).getString("SJBEGIN"));	    //6
			vpd.put("var7", varOList.get(i).getString("SJFINISH"));	    //7
			vpd.put("var8", varOList.get(i).getString("FCREATER"));	    //8
			vpd.put("var9", varOList.get(i).getString("FCREATETIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCHANGER"));	    //10
			vpd.put("var11", varOList.get(i).getString("FCHANGETIME"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
