package org.yy.controller.mdm;

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
import org.yy.service.mdm.EQM_OVERHAULMXService;

/** 
 * 说明：检修计划明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-25
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/eqm_overhaulmx")
public class EQM_OVERHAULMXController extends BaseController {
	
	@Autowired
	private EQM_OVERHAULMXService eqm_overhaulmxService;
	
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
		pd.put("EQM_OVERHAULMX_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		eqm_overhaulmxService.save(pd);
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
		eqm_overhaulmxService.delete(pd);
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
		eqm_overhaulmxService.edit(pd);
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
		List<PageData>	varList = eqm_overhaulmxService.list(page);	//列出EQM_OVERHAULMX列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**修改反馈
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editValue")
	@ResponseBody
	public Object editValue() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{			
			eqm_overhaulmxService.editValue(pd);
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("result", result);
			map.put("pd", pd);
			//map.put("msg", msg);
		}
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
		pd = eqm_overhaulmxService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	/**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goTick")
	@ResponseBody
	public Object goTick() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_overhaulmxService.findByTick(pd);	//根据ID读取
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
			eqm_overhaulmxService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备检修计划反馈主键");	//1
		titles.add("设备检修计划主键");	//2
		titles.add("类型");	//3
		titles.add("日期");	//4
		titles.add("反馈内容");	//5
		titles.add("创建人");	//6
		titles.add("创建时间");	//7
		titles.add("扩展字段1");	//8
		titles.add("扩展字段2");	//9
		titles.add("扩展字段3");	//10
		titles.add("扩展字段4");	//11
		titles.add("扩展字段5");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_overhaulmxService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_OVERHAULMX_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQM_OVERHAUL_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FDATE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCOUPLE_CONTENT"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCREATOR"));	    //6
			vpd.put("var7", varOList.get(i).getString("CREATE_TIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("FEXTEND1"));	    //8
			vpd.put("var9", varOList.get(i).getString("FEXTEND2"));	    //9
			vpd.put("var10", varOList.get(i).getString("FEXTEND3"));	    //10
			vpd.put("var11", varOList.get(i).getString("FEXTEND4"));	    //11
			vpd.put("var12", varOList.get(i).getString("FEXTEND5"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
