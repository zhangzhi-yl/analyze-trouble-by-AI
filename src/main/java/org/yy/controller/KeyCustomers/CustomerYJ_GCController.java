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
import org.yy.service.KeyCustomers.CustomerYJ_GCService;

/** 
 * 说明：工程客户业绩
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-03
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/CustomerYJ_GC")
public class CustomerYJ_GCController extends BaseController {
	
	@Autowired
	private CustomerYJ_GCService CustomerYJ_GCService;
	
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
		pd.put("CustomerYJ_GC_ID", this.get32UUID());	//主键
		pd.put("FCREATER", Jurisdiction.getName());
		pd.put("FCREATTIME", Tools.date2Str(new Date(), "yyyy-MM-dd"));
		CustomerYJ_GCService.save(pd);
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
		CustomerYJ_GCService.delete(pd);
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
		CustomerYJ_GCService.edit(pd);
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
		List<PageData>	varList = CustomerYJ_GCService.list(page);	//列出CustomerYJ_GC列表
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
		pd = CustomerYJ_GCService.findById(pd);	//根据ID读取
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
			CustomerYJ_GCService.deleteAll(ArrayDATA_IDS);
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
		titles.add("类型");	//2
		titles.add("项目名称");	//3
		titles.add("项目类别");	//4
		titles.add("项目金额");	//5
		titles.add("项目状态");	//6
		titles.add("成功点");	//7
		titles.add("失败点");	//8
		titles.add("创建人");	//9
		titles.add("创建时间");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = CustomerYJ_GCService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CUSTOMER_NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("TTYPE"));	    //2
			vpd.put("var3", varOList.get(i).getString("PRONAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("PROTYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("PROJINE"));	    //5
			vpd.put("var6", varOList.get(i).getString("PROSTATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FSUCCESS"));	    //7
			vpd.put("var8", varOList.get(i).getString("FAILED"));	    //8
			vpd.put("var9", varOList.get(i).getString("FCREATER"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCREATTIME"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
