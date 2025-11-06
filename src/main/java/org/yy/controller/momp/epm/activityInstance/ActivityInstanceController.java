package org.yy.controller.momp.epm.activityInstance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.momp.epm.activityinstance.ActivityInstanceService;
import org.yy.service.momp.epm.planinstance.PlanInstanceService;
import org.yy.util.Tools;
/**
 * 活动实例
 * @author AOC
 *
 */
@Controller
@RequestMapping("/activityinstance")
public class ActivityInstanceController extends BaseController {
	@Autowired
	private PlanInstanceService planinstanceService;
	@Autowired
	private ActivityInstanceService activityinstanceService;
	
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			activityinstanceService.edit(pd);
			//取回修改后的pd，修改阶段的计划开始时间结束时间
			PageData  newPd = activityinstanceService.findById(pd);
			//更新阶段数据
			activityinstanceService.updatePhasePlanStartEndTime(newPd);
			//更新计划数据
			activityinstanceService.updatePlanPlanStartEndTime(newPd);
			
			
            map.put("msg","ok");
            map.put("msgText","修改成功！");
            map.put("pd",pd);
            result = "200";
        }catch (Exception e){
            map.put("msg","no");
            map.put("msgText","未知错误，请联系管理员！");
            result = "500";
        }finally{
            map.put("result", result);
        }
        return map;
	
	}
	/**修改勾选信息
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editFcheck")
	@ResponseBody
	public Object editFcheck() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(null!=pd.getString("CHECK")&&"YES".equals(pd.getString("CHECK"))){
				pd.put("FCHECK", "NO");
			}else{
				pd.put("FCHECK", "YES");
			}
			activityinstanceService.editFcheck(pd);
			map.put("FCHECK",pd.getString("FCHECK"));
            map.put("msg","ok");
            map.put("msgText","修改成功！");
            map.put("pd",pd);
            result = "200";
        }catch (Exception e){
            map.put("msg","no");
            map.put("msgText","未知错误，请联系管理员！");
            result = "500";
        }finally{
            map.put("result", result);
        }
        return map;
	
	}
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
		pd.put("ACTIVITYINSTANCE_ID", this.get32UUID());	//主键
		pd.put("FCTIME", Tools.date2Str(new Date())); //创建时间
		if("确定实施期预算".equals(pd.getString("FATNAME"))||"用户验收".equals(pd.getString("FATNAME"))||"客户签收".equals(pd.getString("FATNAME"))){
			pd.put("FCHECK", "YES");
		}else{
			pd.put("FCHECK", "NO");
		}
		activityinstanceService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("activitytemplate:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = activityinstanceService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	@RequestMapping(value="/findById")
    @ResponseBody
    public Object findById() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData  newPd = activityinstanceService.findById(pd);
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
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		
		try{
            List<PageData>	varList = activityinstanceService.list(page);	//列出PlantList列表        
            map.put("total", page.getTotalResult());   
            map.put("currentPage",page.getCurrentPage());   
            map.put("showCount", page.getShowCount()); 
            if(varList.size()>0){
                map.put("list", varList);   
                result = "200";
            }else{
                result = "201";
            }
        }catch (Exception e){
            result = "500";
        }finally{
            map.put("result", result);
        }
        return map;
			
	}
	  /**
	    * @Title: getTptDatalist
	    * @Description:通过项目ID获取拓扑图数据
	    * @author Leo.L
	    * @param page
	    * @return
	    * @throws Exception
	    * @throws
	    */
		@RequestMapping(value="/getTptDatalist")
	    @ResponseBody
	    public Object getTptDatalist() throws Exception{
	        Map<String,Object> map = new HashMap<String,Object>();
	        String result = "500";
	        PageData pd = new PageData();
	        pd = this.getPageData();
	        try{
	            List<PageData>  varList = activityinstanceService.getTptDatalist(pd);         
	            if(varList.size()>0){
	                map.put("list", varList);
	                map.put("templateName",varList.get(0).getString("FPTNAME") );//模板名称
	                result = "200";
	            }else{
	                map.put("templateName","");//模板名称
	                result = "201";
	            }
	        }catch (Exception e){
	            result = "500";
	        }finally{
	            map.put("result", result);
	        }
	        return map;
	    }
}
