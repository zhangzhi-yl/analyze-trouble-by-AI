package org.yy.controller.momp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.yy.entity.PageData;
import org.yy.service.momp.PhaseProcessTemplateService;
import org.yy.service.momp.PhaseService;

/** 
 * 说明：MOMPphase模板
 * 作者：YuanYe
 * 时间：2020-03-23
 * 
 */
@Controller
@RequestMapping("/phaseprocesstemplate")
public class PhaseProcessTemplateController extends BaseController {
	
	@Autowired
	private PhaseProcessTemplateService phaseprocesstemplateService;
	@Autowired
	private PhaseService phaseService;
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
		pd.put("PHASEPROCESSTEMPLATE_ID", this.get32UUID());	//主键
		phaseprocesstemplateService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/inputjsonlist")
	@ResponseBody
	public Object inputjsonlist() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdlist = new PageData();
		pd = this.getPageData();
		PageData  newPd = phaseprocesstemplateService.findById(pd);
		String PHASET_INPUTJSON=newPd.getString("PT_INPUTJSON");//输入格式json
	    JSONObject jsonResult = JSONObject.parseObject(PHASET_INPUTJSON,Feature.OrderedField);
		//JSONObject obj = new JSONObject(PHASET_INPUTJSON);
	    Iterator iter = jsonResult.entrySet().iterator();
	    int count=0;
	    List<PageData> nvarlist = new ArrayList<>();
        while (iter.hasNext()) {
        	List<PageData> varlist = new ArrayList<>();
            Map.Entry entry = (Map.Entry) iter.next();
            String one=entry.getKey().toString();//第一层数据
            String sprictone =one.substring(0, one.length() - 1);//第二层数据表头
            JSONObject ylbom = (JSONObject) jsonResult.get(one);
            Iterator itertwo = ylbom.entrySet().iterator();
            PageData pd1=new PageData();
            while (itertwo.hasNext()) {
            	List<TreeMap> varlist1 = new ArrayList<>();
                Map.Entry entrytwo = (Map.Entry) itertwo.next();//第二层数据
                String two=entrytwo.getKey().toString();
                String twovalue=entrytwo.getValue().toString();
                String sprict =one.substring(0, one.length() - 1);
                if(two.equals("title")||two.equals(sprict)){
                	 Gson gson = new Gson();
                     varlist1 = gson.fromJson(twovalue, new TypeToken<List<TreeMap>>() {
             		}.getType());
                     pd1.put(two,varlist1);
                }else{
                	pd1.put(two,twovalue);
                }
               
            }
            pd1.put("name", one);
            pd1.put("tablename", sprictone);
            varlist.add(pd1);
            pdlist.put("varlist"+count, varlist);
            count++;
        }
        nvarlist.add(pdlist);
        map.put("varList", nvarlist);
        map.put("number", pdlist.size());
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
		phaseprocesstemplateService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	@RequestMapping(value="/deleteinputjson")
	@ResponseBody
	public Object deleteinputjson() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String inputjson = pd.getString("inputjson");
		pd.put("PT_INPUTJSON", inputjson);
		phaseprocesstemplateService.updateinputjson(pd);
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
		phaseprocesstemplateService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	/**修改
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/updateinputjson")
	@ResponseBody
	public Object updateinputjson() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String name=pd.getString("name");//最外层名字 例如materials
		String tablename=pd.getString("tablename");//要修改的json名字 例如material
		String addinputjson=pd.getString("addinputjson");//新增的那条json
		JSONObject addjsonResult = JSONObject.parseObject(addinputjson,Feature.OrderedField);
		PageData  newPd = phaseprocesstemplateService.findById(pd);
		String PHASET_INPUTJSON=newPd.getString("PT_INPUTJSON");//输入格式json
		JSONObject jsonResult = JSONObject.parseObject(PHASET_INPUTJSON,Feature.OrderedField);
		Iterator oneiter = jsonResult.entrySet().iterator();
		PageData inputjsonpd1=new PageData();
		while (oneiter.hasNext()) {
			 Map.Entry oneentry = (Map.Entry) oneiter.next();
	         String one=oneentry.getKey().toString();//第一层数据key
	         String onevalue=oneentry.getValue().toString();//第一层数据value
	         if(one.equals(name)){//传过来要修改的key是哪个
	        	JSONObject ylbom = (JSONObject) jsonResult.get(name);
	     		String itemtitle = (String) ylbom.get("itemtitle").toString();
	     		String materiala = (String) ylbom.get(tablename).toString();
	     		String title = (String) ylbom.get("title").toString();
	     		List<Map<Object, String>> varlist = new ArrayList<>();
	     		List<Map<Object, String>> varlist1 = new ArrayList<>();
	     		Gson gson = new Gson();
	     		varlist = gson.fromJson(materiala, new TypeToken<List<TreeMap>>() {
	     		}.getType());
	     		varlist1 = gson.fromJson(title, new TypeToken<List<TreeMap>>() {
	     		}.getType());
	     		Iterator iter = addjsonResult.entrySet().iterator();
	     		Map<Object, String>  nPd= new TreeMap<Object, String>();
	     		while (iter.hasNext()) {
	     			Map.Entry entry = (Map.Entry) iter.next();
	                 String key=entry.getKey().toString();//第一层数据
	                 String value=entry.getValue().toString();
	                 nPd.put(key, value);
	     		}
	     		varlist.add(nPd);
	     		JSONArray ja = JSONArray.parseArray(JSON.toJSONString(varlist));
	     		JSONArray titleja = JSONArray.parseArray(JSON.toJSONString(varlist1)); 
	     		PageData inputjsonpd=new PageData();
	    		inputjsonpd.put("itemtitle", itemtitle);
	    		inputjsonpd.put("title", titleja);
	    		inputjsonpd.put(tablename, ja);
	    		inputjsonpd1.put(name, inputjsonpd);
	         }else{
	        	 inputjsonpd1.put(one, JSONObject.parseObject(onevalue,Feature.OrderedField));
	         }
		}
		JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(inputjsonpd1));
		String PT_INPUTJSON = itemJSONObj.toString();
		pd.put("PT_INPUTJSON", PT_INPUTJSON);
		phaseprocesstemplateService.updateinputjson(pd);
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
		List<PageData>	varList = phaseprocesstemplateService.list(page);	//列出PhaseProcessTemplate列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**物料类列表
	 * @param page
	 * @throws Exception 
	 */
	@RequestMapping(value="/listAllBASIC")
	@ResponseBody
	public Object listAllBASIC() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = phaseprocesstemplateService.listAllBASIC(pd);	//列出PhaseProcessTemplate列表
		if(varList.size()>0) {
			errInfo="success";
		}else {
			errInfo="fail";
		}
		map.put("list", varList);
		map.put("result", errInfo);
		return map;
	}
	
	
	/**Phase库列表
	 * @param page
	 * @throws Exception 
	 */
	@RequestMapping(value="/listAllPhase")
	@ResponseBody
	public Object listAllPhase() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = phaseService.listAll(pd);	//列出PhaseProcessTemplate列表
		if(varList.size()>0) {
			errInfo="success";
		}else {
			errInfo="fail";
		}
		map.put("list", varList);
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
		pd = phaseprocesstemplateService.findById(pd);	//根据ID读取
		pd.put("PT_EDITOR", Jurisdiction.getName());
		pd.put("PT_EDITTIME",  Tools.date2Str(new Date()));
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	/**
	 * 生成编号 OT+5位
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/count")
    @ResponseBody
    public Object count() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData  newPd = phaseprocesstemplateService.count(pd);
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
	@RequiresPermissions("phaseprocesstemplate:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			phaseprocesstemplateService.deleteAll(ArrayDATA_IDS);
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
		titles.add("关联任务单模板编号");	//1
		titles.add("phase流程节点编号");	//2
		titles.add("phase流程节点名称");	//3
		titles.add("关联phase功能编码");	//4
		titles.add("phase流程节点输入JSON");	//5
		titles.add("phase流程节点输出JSON");	//6
		titles.add("phase节点类型");	//7
		titles.add("phase下步节点编号");	//8
		titles.add("条件表达式");	//9
		titles.add("模板创建人");	//10
		titles.add("模板创建时间");	//11
		titles.add("模板最后修改时间");	//12
		titles.add("模板最后修改人");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = phaseprocesstemplateService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PT_MTCODE"));	    //1
			vpd.put("var2", varOList.get(i).getString("PT_CODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("PT_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("PT_PHASECODE"));	    //4
			vpd.put("var5", varOList.get(i).getString("PT_INPUTJSON"));	    //5
			vpd.put("var6", varOList.get(i).getString("PT_OUTPUTJSON"));	    //6
			vpd.put("var7", varOList.get(i).getString("PT_CODETYPE"));	    //7
			vpd.put("var8", varOList.get(i).getString("PT_NEXTCODEJSON"));	    //8
			vpd.put("var9", varOList.get(i).getString("PT_CONDITION"));	    //9
			vpd.put("var10", varOList.get(i).getString("PT_CREATOR"));	    //10
			vpd.put("var11", varOList.get(i).getString("PT_CREATETIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("PT_EDITTIME"));	    //12
			vpd.put("var13", varOList.get(i).getString("PT_EDITOR"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
