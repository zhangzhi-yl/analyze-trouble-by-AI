package org.yy.controller.momp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.momp.PhaseService;
import org.yy.util.Const;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/phasesop")
public class PhaseSopController extends BaseController{
	@Autowired
	private PhaseService phaseService;
	/**
	 *  查看sop
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sign")
    @ResponseBody
    public Object count() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> mapnew = new HashMap<String,Object>();
        String res = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        String code =pd.getString("code");
        String path =pd.getString("path");
        String type =pd.getString("type");
       // String title =pd.getString("title");
        PageData  newPd = phaseService.findbycode(pd);
   
        String jsonstring ="";
        List<PageData> varlist = new ArrayList<>();
		List<PageData> varlistout = new ArrayList<>();
		PageData inputjson = new PageData();
		PageData outjson = new PageData();
        try{
            if(null!=newPd) { 
           	 String PHASET_INPUTJSON=newPd.getString("PHASET_INPUTJSON");//输入格式json
                String PHASET_OUTPUTJSON=newPd.getString("PHASET_OUTPUTJSON");
           
        			JSONObject obj = new JSONObject(PHASET_INPUTJSON);
        			JSONObject objout = new JSONObject(PHASET_OUTPUTJSON);
        			JSONObject files = (JSONObject) obj.get("files");
        			JSONObject results = (JSONObject) objout.get("results");
        			String materiala = (String) files.get("file").toString();
        			String materialaout = (String) results.get("result").toString();
        			Gson gson = new Gson();
        			Gson gsonout = new Gson();
        			varlist = gson.fromJson(materiala, new TypeToken<List<PageData>>() {
        			}.getType());
        			varlistout = gsonout.fromJson(materialaout, new TypeToken<List<PageData>>() {
        			}.getType());
        			 inputjson=varlist.get(0);
        			 outjson=varlistout.get(0);
        			String PHASET_CODE=inputjson.getString("code");
        			String pathp=inputjson.getString("path");
        			String typep=inputjson.getString("type");
        			String rcode = outjson.getString("rcode");
        			 if(files!=null) {
        				 if(path.contains(pathp)==true){
         					if(typep.contains(type)==true) {
         						outjson.put("rcode", "success");
         					}else {outjson.put("rcode", "fail");}
             			}else {outjson.put("rcode", "fail");}
        			 }else {
        				 outjson.put("rcode", "fail");}
            }else {
            	PageData pdcode = new PageData();
            	pdcode.put("rcode", "fail");
            	varlistout.add(0,pdcode);
            }
            map.put("result", varlistout);	
        }catch (Exception e){
        	PageData pdcode = new PageData();
        	pdcode.put("rcode", "fail");
        	varlistout.add(0,pdcode);
        	map.put("result", varlistout);	
        	mapnew.put("results",map);
            
        }finally{
            mapnew.put("results",map);
        }
        return mapnew;
    }
	
	@RequestMapping(value="/file")
	@ResponseBody
	public Object list() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
        try {
 
    		String FPFFILEPATH =  Const.FILEPATHFILE +"/"+"YL2020011关于公司复工值班相关要求通知.pdf";
    		System.out.println(FPFFILEPATH);
    		map.put("pd", FPFFILEPATH);
        } catch (Exception e) { 
			// TODO: handle exception
		}
 
		 
		map.put("result", errInfo);
		return map;
	}
}
