package org.yy.controller.momp;

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
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.yy.entity.PageData;
import org.yy.service.momp.PhaseService;

/** 
 * 说明：phase库结构
 * 作者：YuanYe
 * 时间：2020-03-16
 * 
 */
@Controller
@RequestMapping("/phase")
public class PhaseController extends BaseController {
	
	@Autowired
	private PhaseService phaseService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("phase:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PHASE_ID", this.get32UUID());	//主键
		pd.put("PHASET_CREATECIME", DateUtil.date2Str(new Date()));	
		pd.put("PHASET_CREATOR", Jurisdiction.getName());
		pd.put("PHASET_EDITTIME", DateUtil.date2Str(new Date()));	
		pd.put("PHASET_EDITOR",Jurisdiction.getName());	
		phaseService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("phase:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		phaseService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("phase:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PHASET_EDITTIME", DateUtil.date2Str(new Date()));	
		pd.put("PHASET_EDITOR",Jurisdiction.getName());	
		phaseService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("phase:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = phaseService.list(page);	//列出Phase列表
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
	@RequiresPermissions("phase:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = phaseService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	@RequestMapping(value="/count")
    @ResponseBody
    public Object count() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData  newPd = phaseService.count(pd);
        try{
            result = "200";
            map.put("pd", newPd);   
            map.put("msg", "ok");
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
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("phase:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			phaseService.deleteAll(ArrayDATA_IDS);
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
		titles.add("功能编码");	//1
		titles.add("功能名称");	//2
		titles.add("功能描述");	//3
		titles.add("配置地址");	//4
		titles.add("执行地址");	//5
		titles.add("前端路径");	//6
		titles.add("输入");	//7
		titles.add("输出");	//8
		titles.add("创建时间");	//9
		titles.add("创建人");	//10
		titles.add("修改时间");	//11
		titles.add("修改人");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = phaseService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PHASET_CODE"));	    //1
			vpd.put("var2", varOList.get(i).getString("PHASET_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("PHASET_DES"));	    //3
			vpd.put("var4", varOList.get(i).getString("PHASET_EDIT"));	    //4
			vpd.put("var5", varOList.get(i).getString("PHASET_EXE"));	    //5
			vpd.put("var6", varOList.get(i).getString("PHASET_PAGEVIEW"));	    //6
			vpd.put("var7", varOList.get(i).getString("PHASET_INPUTJSON"));	    //7
			vpd.put("var8", varOList.get(i).getString("PHASET_OUTPUTJSON"));	    //8
			vpd.put("var9", varOList.get(i).getString("PHASET_CREATECIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("PHASET_CREATOR"));	    //10
			vpd.put("var11", varOList.get(i).getString("PHASET_EDITTIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("PHASET_EDITOR"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
