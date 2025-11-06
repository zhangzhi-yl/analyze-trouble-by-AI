package org.yy.controller.pm;

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
import org.yy.service.pm.PM_MAINTAIN_MANHOURService;

/** 
 * 说明：设备保养人员工时
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-30
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PM_MAINTAIN_MANHOUR")
public class PM_MAINTAIN_MANHOURController extends BaseController {
	
	@Autowired
	private PM_MAINTAIN_MANHOURService PM_MAINTAIN_MANHOURService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("PM_MAINTAIN_MANHOUR:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PM_MAINTAIN_MANHOUR_ID", this.get32UUID());	//主键
		PM_MAINTAIN_MANHOURService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("PM_MAINTAIN_MANHOUR:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PM_MAINTAIN_MANHOURService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("PM_MAINTAIN_MANHOUR:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PM_MAINTAIN_MANHOURService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("PM_MAINTAIN_MANHOUR:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = PM_MAINTAIN_MANHOURService.list(page);	//列出PM_MAINTAIN_MANHOUR列表
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
	//@RequiresPermissions("PM_MAINTAIN_MANHOUR:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PM_MAINTAIN_MANHOURService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("PM_MAINTAIN_MANHOUR:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			PM_MAINTAIN_MANHOURService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备保养计划ID");	//1
		titles.add("部门ID");	//2
		titles.add("人员ID");	//3
		titles.add("类型");	//4
		titles.add("描述");	//5
		titles.add("实际开始时间");	//6
		titles.add("实际结束时间");	//7
		titles.add("实际发生工时");	//8
		titles.add("预留1");	//9
		titles.add("预留2");	//10
		titles.add("预留3");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = PM_MAINTAIN_MANHOURService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PM_MAINTAIN_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("DEPARTMENT_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("STAFF_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("MANHOUR_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("DESCRIBE"));	    //5
			vpd.put("var6", varOList.get(i).getString("ACTUAL_START_TIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("ACTUAL_END_TIME"));	    //7
			vpd.put("var8", varOList.get(i).get("ACTUAL_MANHOUR").toString());	//8
			vpd.put("var9", varOList.get(i).getString("RESERVE_ONE"));	    //9
			vpd.put("var10", varOList.get(i).getString("RESERVE_TWO"));	    //10
			vpd.put("var11", varOList.get(i).getString("RESERVE_THREE"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
