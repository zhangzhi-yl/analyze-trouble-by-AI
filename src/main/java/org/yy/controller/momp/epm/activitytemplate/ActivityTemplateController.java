package org.yy.controller.momp.epm.activitytemplate;

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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.momp.epm.activitytemplate.ActivityTemplateService;


/** 
 * 说明：活动模板
 * 作者：YuanYe
 * 时间：2020-03-12
 * 
 */
@Controller
@RequestMapping("/activitytemplate")
public class ActivityTemplateController extends BaseController {
	
	@Autowired
	private ActivityTemplateService activitytemplateService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("activitytemplate:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ACTIVITYTEMPLATE_ID", this.get32UUID());	//主键
		pd.put("FRF1",Tools.date2Str(new Date()));	//主键
		activitytemplateService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("activitytemplate:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		activitytemplateService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("activitytemplate:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		activitytemplateService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("activitytemplate:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = activitytemplateService.list(page);	//列出ActivityTemplate列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/findByFATNAME")
	@ResponseBody
	public Object findByFATNAME() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>  newPd = activitytemplateService.findByFATNAME(pd);
		 try{
			 errInfo = "200";
	            map.put("list", newPd);   
	            map.put("msg", "ok");
	            map.put("msgText","查询成功！");
	        }catch (Exception e){
	        	errInfo = "500";
	            map.put("msg","no");
	            map.put("msgText","未知错误，请联系管理员！");
	        }finally{
	            map.put("result", errInfo);
	        }
		return map;
	}
	
	@RequestMapping(value="/findById")
	@ResponseBody
	public Object findById() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData  newPd = activitytemplateService.findById(pd);
		 try{
			 errInfo = "200";
	            map.put("pd", newPd);   
	            map.put("msg", "ok");
	            map.put("msgText","查询成功！");
	        }catch (Exception e){
	        	errInfo = "500";
	            map.put("msg","no");
	            map.put("msgText","未知错误，请联系管理员！");
	        }finally{
	            map.put("result", errInfo);
	        }
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("activitytemplate:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = activitytemplateService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("activitytemplate:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			activitytemplateService.deleteAll(ArrayDATA_IDS);
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
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("备注1");	//1
		titles.add("备注2");	//2
		titles.add("备注3");	//3
		titles.add("备注4");	//4
		titles.add("备注5");	//5
		titles.add("备注6");	//6
		titles.add("备注7");	//7
		titles.add("备注8");	//8
		titles.add("备注9");	//9
		titles.add("备注10");	//10
		titles.add("备注11");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = activitytemplateService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ACTIVITYTEMPLATE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FATNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FATDESCRIPTION"));	    //3
			vpd.put("var4", varOList.get(i).getString("FATLEVEL"));	    //4
			vpd.put("var5", varOList.get(i).getString("FATREMARK"));	    //5
			vpd.put("var6", varOList.get(i).getString("FATISCHECK"));	    //6
			vpd.put("var7", varOList.get(i).getString("FATACHECKID"));	    //7
			vpd.put("var8", varOList.get(i).getString("PHASETEMPLATE_ID"));	    //8
			vpd.put("var9", varOList.get(i).getString("PLANTEMPLATE_ID"));	    //9
			vpd.put("var10", varOList.get(i).getString("FRF1"));	    //10
			vpd.put("var11", varOList.get(i).getString("FRF2"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
