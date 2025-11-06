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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.km.WORKINGPROCEDURESELFCHECKEXAMPLEService;

/** 
 * 说明：自检执行
 * 作者：YuanYes QQ356703572
 * 时间：2021-07-13
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/workingprocedureselfcheckexample")
public class WORKINGPROCEDURESELFCHECKEXAMPLEController extends BaseController {
	
	@Autowired
	private WORKINGPROCEDURESELFCHECKEXAMPLEService workingprocedureselfcheckexampleService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("workingprocedureselfcheckexample:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("WORKINGPROCEDURESELFCHECKEXAMPLE_ID", this.get32UUID());	//主键
		workingprocedureselfcheckexampleService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("workingprocedureselfcheckexample:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		workingprocedureselfcheckexampleService.delete(pd);
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
		pd.put("FBACKMAN", Jurisdiction.getName());		
		pd.put("FBACKTIME", Tools.date2Str(new Date()));
		workingprocedureselfcheckexampleService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	/**一键修改是否符合
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editAll")
	@ResponseBody
	public Object editAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FBACKMAN", Jurisdiction.getName());		
		pd.put("FBACKTIME", Tools.date2Str(new Date()));
		workingprocedureselfcheckexampleService.editAll(pd);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("workingprocedureselfcheckexample:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = workingprocedureselfcheckexampleService.list(page);	//列出WORKINGPROCEDURESELFCHECKEXAMPLE列表
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
	@RequiresPermissions("workingprocedureselfcheckexample:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = workingprocedureselfcheckexampleService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("workingprocedureselfcheckexample:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			workingprocedureselfcheckexampleService.deleteAll(ArrayDATA_IDS);
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
		titles.add("实例工序ID");	//1
		titles.add("检验标准");	//2
		titles.add("自检项");	//3
		titles.add("检验项备注");	//4
		titles.add("是否符合");	//5
		titles.add("执行备注");	//6
		titles.add("反馈人");	//7
		titles.add("反馈时间");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = workingprocedureselfcheckexampleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("WORKINGPROCEDUREEXAMPLE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FTESTSTANDARD"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTESTITEMNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FTESTNOTE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FISACCORD"));	    //5
			vpd.put("var6", varOList.get(i).getString("FRUNNOTE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FBACKMAN"));	    //7
			vpd.put("var8", varOList.get(i).getString("FBACKTIME"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
