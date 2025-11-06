package org.yy.controller.momp.epm.phaseInstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.momp.epm.phaseinstance.PhaseInstanceService;
import org.yy.service.project.manager.DepPlanService;
import org.yy.service.project.manager.Pro_PlanService;
import org.yy.service.project.manager.StaffPlanService;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;

import net.sf.json.JSONArray;


/** 
 * 说明：项目阶段
 * 创建人：YY Q356703572
 * 创建时间：2018-12-18
 */
@Controller
@RequestMapping(value="/phaseinstance")
public class PhaseInstanceController extends BaseController {
	@Autowired
	private PhaseInstanceService phaseinstanceService;
	@Autowired
	private Pro_PlanService pro_planService;
	@Autowired
	private DepPlanService depplanService;
	@Autowired
	private StaffPlanService staffplanService;
	
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			phaseinstanceService.edit(pd);
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
	
	@RequestMapping(value="/findById")
    @ResponseBody
    public Object findById() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData  newPd = phaseinstanceService.findById(pd);
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
	@RequestMapping(value="/findPhaseID")
    @ResponseBody
    public Object findPhaseID() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData  newPd = phaseinstanceService.findPhaseID(pd);
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
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = phaseinstanceService.list(page);	//列出PlanInstance列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/listAllGantee")
	@ResponseBody
	public Object listAllGantee() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//pd.put("ISALL", Jurisdiction.getUserISALL());
		//pd.put("ISBZ", Jurisdiction.getUserSFLD());
		pd.put("Guan", Jurisdiction.getName());
		//pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_IDNEW());
		List<PageData>	varList = phaseinstanceService.listAllGante(pd);	//列出PlanInstance列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/listGante")
	@ResponseBody
	public Object listGante(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		String KEYWORDS2 = pd.getString("KEYWORDS2");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS2))pd.put("KEYWORDS2", KEYWORDS2.trim());
		String KEYWORDS3 = pd.getString("KEYWORDS3");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS3))pd.put("KEYWORDS3", KEYWORDS3.trim());
		String KEYWORDS4 = pd.getString("KEYWORDS4");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS4))pd.put("KEYWORDS4", KEYWORDS4.trim());
		String KEYWORDS5 = pd.getString("KEYWORDS5");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS5))pd.put("KEYWORDS5", KEYWORDS5.trim());
		String KEYWORDS6 = pd.getString("KEYWORDS6");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS6))pd.put("KEYWORDS6", KEYWORDS6.trim());
		
		String KEYWORDS7 = pd.getString("KEYWORDS7");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS7))pd.put("KEYWORDS7", KEYWORDS7.trim());
		String KEYWORDS8 = pd.getString("KEYWORDS8");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS8))pd.put("KEYWORDS8", KEYWORDS8.trim());
		String KEYWORDS9 = pd.getString("KEYWORDS9");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS9))pd.put("KEYWORDS9", KEYWORDS9.trim());
		String KEYWORDS10 = pd.getString("KEYWORDS10");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS10))pd.put("KEYWORDS10", KEYWORDS10.trim());
		String KEYWORDS11 = pd.getString("KEYWORDS11");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS11))pd.put("KEYWORDS11", KEYWORDS11.trim());
		String KEYWORDS12 = pd.getString("KEYWORDS12");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS12))pd.put("KEYWORDS12", KEYWORDS12.trim());
		String KEYWORDS13 = pd.getString("KEYWORDS13");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS13))pd.put("KEYWORDS13", KEYWORDS13.trim());
		String KEYWORDS14 = pd.getString("KEYWORDS14");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS14))pd.put("KEYWORDS14", KEYWORDS14.trim());
		String KEYWORDS15 = pd.getString("KEYWORDS15");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS15))pd.put("KEYWORDS15", KEYWORDS15.trim());
		String KEYWORDS16 = pd.getString("KEYWORDS16");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS16))pd.put("KEYWORDS16", KEYWORDS16.trim());
		String KEYWORDS17 = pd.getString("KEYWORDS17");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS17))pd.put("KEYWORDS17", KEYWORDS17.trim());
		String KEYWORDS18 = pd.getString("KEYWORDS17");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS18))pd.put("KEYWORDS18", KEYWORDS18.trim());
		List<PageData>	tvarlist = new ArrayList<PageData>();
		List<PageData> proList=pro_planService.listAllXMS(pd);//查出
		for(int i=0;i<proList.size();i++) {
			PageData pdPro=proList.get(i);
			PageData pddata=pro_planService.findById(pdPro);
			String fatherID=pddata.getString("PRO_PLAN_ID");
			PageData pdParend=new PageData();
			pdParend.put("id", fatherID);
			pdParend.put("text", pddata.getString("PPROJECT_GANTEE_NAME")==null?"":pddata.getString("PPROJECT_GANTEE_NAME"));
			if(pddata.getString("NEW_PLAN_START_TIME")!=null && !pddata.getString("NEW_PLAN_START_TIME").equals("")){
				pdParend.put("start_date",formatDate(pddata.getString("NEW_PLAN_START_TIME")));
			}else{
				pdParend.put("start_date","");
			}
			if(pddata.getString("NEW_PLAN_END_TIME")!=null && !pddata.getString("NEW_PLAN_END_TIME").equals("")){
				pdParend.put("end_date",formatDate(pddata.getString("NEW_PLAN_END_TIME")));
			}else{
				pdParend.put("end_date","");
			}
			if(pddata.getString("NEW_PLAN_END_TIME")!=null && !pddata.getString("NEW_PLAN_END_TIME").equals("") && pddata.getString("NEW_PLAN_START_TIME")!=null && !pddata.getString("NEW_PLAN_START_TIME").equals("")){
				pdParend.put("duration", Tools.caculateTotalTime(pddata.getString("NEW_PLAN_END_TIME"),pddata.getString("NEW_PLAN_START_TIME")));	
			}else{
				pdParend.put("duration", 0);	
			}
			pdParend.put("principal", pddata.getString("EPROJECT_MANAGER")==null?"":pddata.getString("EPROJECT_MANAGER"));
			pdParend.put("customer", pddata.getString("ECUSTOMER_NAME")==null?"":pddata.getString("ECUSTOMER_NAME"));
			pdParend.put("empno", pddata.getString("EEQM_NO")==null?"":pddata.getString("EEQM_NO"));		
			pdParend.put("picno", "");
			pdParend.put("message","");
			pdParend.put("progress", 0);
			pdParend.put("open", true);
			pdParend.put("hh", 0);
			pdParend.put("FTYPE", "E");
			tvarlist.add(pdParend);
			pd.put("PRO_PLAN_ID", pdPro.get("PRO_PLAN_ID"));
			List<PageData>	varList = phaseinstanceService.listGante(pd);	//列出PlanInstance列表 根据项目计划id查询项目阶段
			
			for (PageData pageData : varList) {
				PageData pd1=new PageData();
				String fatherTwoID=pageData.getString("DEPPLAN_ID");
				pd1.put("id", fatherTwoID);
				pd1.put("parent", fatherID);
				pd1.put("text", pageData.getString("FHTNAME"));
				if(pageData.getString("PLAN_START_TIME")!=null && !pageData.getString("PLAN_START_TIME").equals("")){
					pd1.put("start_date", formatDate(pageData.getString("PLAN_START_TIME")));
				}else{
					pd1.put("start_date", "");
				}
				if(pageData.getString("PLAN_END_TIME")!=null && !pageData.getString("PLAN_END_TIME").equals("")){
					pd1.put("end_date",formatDate(pageData.getString("PLAN_END_TIME")));
				}else{
					pd1.put("end_date","");
				}
				if(pageData.getString("PLAN_END_TIME")!=null && !pageData.getString("PLAN_END_TIME").equals("") && pageData.getString("PLAN_START_TIME")!=null && !pageData.getString("PLAN_START_TIME").equals("")){
					pd1.put("duration", Tools.caculateTotalTime(pageData.getString("PLAN_END_TIME"),pageData.getString("PLAN_START_TIME")));
				}else{
					pd1.put("duration", 0);	
				}
				pd1.put("customer", pddata.getString("ECUSTOMER_NAME")==null?"":pddata.getString("ECUSTOMER_NAME"));
				pd1.put("empno", pddata.getString("EEQM_NO")==null?"":pddata.getString("EEQM_NO"));	
				pd1.put("principal", pageData.getString("FMANAGER")==null?"":pageData.getString("FMANAGER"));
				pd1.put("picno", "");
				pd1.put("message","");
				pd1.put("progress", 0);
				pd1.put("open", true);
				pd1.put("hh", 0);
				pd1.put("FTYPE", "E");
				tvarlist.add(pd1);
				pd.put("DEPPLAN_ID", pageData.get("DEPPLAN_ID"));
				List<PageData>	varListT = phaseinstanceService.listGante2(pd);
				for (PageData pageData2 : varListT) {
					PageData pd2=new PageData();
					String fatherThreeID=this.get32UUID();
					pd2.put("id", fatherThreeID);
					pd2.put("parent", fatherTwoID);
					pd2.put("text", pageData2.getString("FTASK_NAME"));
					if(pageData2.getString("START_TIMEX")!=null && !pageData2.getString("START_TIMEX").equals("")){
						pd2.put("start_date", formatDate(pageData2.getString("START_TIMEX")));
					}else{
						pd2.put("start_date", "");
					}
					if(pageData2.getString("END_TIMEX")!=null && !pageData2.getString("END_TIMEX").equals("")){
						pd2.put("end_date",formatDate(pageData2.getString("END_TIMEX")));
					}else{
						pd2.put("end_date","");
					}
					if(pageData2.getString("END_TIMEX")!=null && !pageData2.getString("END_TIMEX").equals("") && pageData2.getString("START_TIMEX")!=null && !pageData2.getString("START_TIMEX").equals("")){
						pd2.put("duration", Tools.caculateTotalTime(pageData2.getString("END_TIMEX"),pageData2.getString("START_TIMEX")));
					}else{
						pd2.put("duration", 0);
					}
					pd2.put("customer", pddata.getString("ECUSTOMER_NAME")==null?"":pddata.getString("ECUSTOMER_NAME"));
					pd2.put("empno", pddata.getString("EEQM_NO")==null?"":pddata.getString("EEQM_NO"));	
					pd2.put("principal", pageData2.getString("STAFFPLAN_MANAGER")==null?"":pageData2.getString("STAFFPLAN_MANAGER"));
					pd2.put("picno", pageData2.getString("FPICNO")==null?"":pageData2.getString("FPICNO"));
					pd2.put("message", pageData2.getString("FMESSAGE")==null?"":pageData2.getString("FMESSAGE"));
					pd2.put("progress", 0);
					pd2.put("open", true);
					pd2.put("hh", pageData2.get("hh"));
					pd2.put("FTYPE", pageData2.get("FTYPE"));
					tvarlist.add(pd2);
				}
			}
		}
		map.put("tvarlist", tvarlist);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/listGantex")
	@ResponseBody
	public Object listGantex(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = phaseinstanceService.listGante2(pd);	//列出PlanInstance列表 根据项目计划id查询项目阶段
		PageData pddata=depplanService.findById(pd);
		List<PageData>	tvarlist = new ArrayList<PageData>();
		String fatherID=this.get32UUID();
		PageData pdParend=new PageData();
		pdParend.put("id", fatherID);
		pdParend.put("text", pddata.getString("FHTNAME")==null?"":pddata.getString("FHTNAME"));
		if(pddata.getString("NEW_PLAN_START_TIME")!=null && !pddata.getString("NEW_PLAN_START_TIME").equals("")){
			pdParend.put("start_date",formatDate(pddata.getString("NEW_PLAN_START_TIME")));
		}else{
			pdParend.put("start_date","");
		}
		
		if(pddata.getString("NEW_PLAN_END_TIME")!=null && !pddata.getString("NEW_PLAN_END_TIME").equals("") && pddata.getString("NEW_PLAN_START_TIME")!=null && !pddata.getString("NEW_PLAN_START_TIME").equals("")){
			pdParend.put("duration", Tools.caculateTotalTime(pddata.getString("NEW_PLAN_END_TIME"),pddata.getString("NEW_PLAN_START_TIME")));	
		}else{
			pdParend.put("duration", 0);	
		}
		pdParend.put("progress", 0);
		pdParend.put("open", true);
		pdParend.put("hh", 0);
		pdParend.put("FTYPE", "E");
		tvarlist.add(pdParend);
		List<PageData>	varListT = phaseinstanceService.listGante2(pd);
		for (PageData pageData2 : varListT) {
			PageData pd2=new PageData();
			String fatherTwoID=this.get32UUID();
			pd2.put("id", fatherTwoID);
			pd2.put("parent", fatherID);
			pd2.put("text", pageData2.getString("FTASK_NAME"));
			if(pageData2.getString("START_TIME")!=null && !pageData2.getString("START_TIME").equals("")){
				pd2.put("start_date", formatDate(pageData2.getString("START_TIME")));
			}else{
				pd2.put("start_date", "");
			}
			if(pageData2.getString("END_TIME")!=null && !pageData2.getString("END_TIME").equals("") && pageData2.getString("START_TIME")!=null && !pageData2.getString("START_TIME").equals("")){
				pd2.put("duration", Tools.caculateTotalTime(pageData2.getString("END_TIME"),pageData2.getString("START_TIME")));
			}else{
				pd2.put("duration", 0);
			}
			
			pd2.put("progress", 0);
			pd2.put("open", true);
			pd2.put("hh", pageData2.get("hh"));
			pd2.put("FTYPE",pageData2.get("FTYPE"));
			tvarlist.add(pd2);
		}
		
		map.put("tvarlist", tvarlist);
		map.put("start_date", pddata.getString("NEW_PLAN_START_TIME"));
		map.put("end_date", pddata.getString("NEW_PLAN_END_TIME"));
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/listGantexx")
	@ResponseBody
	public Object listGantexx(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pddata=staffplanService.findById(pd);
		List<PageData>	tvarlist = new ArrayList<PageData>();
		String fatherID=this.get32UUID();
		PageData pdParend=new PageData();
		pdParend.put("id", fatherID);
		pdParend.put("text", pddata.getString("STAFFPLAN_NAME")==null?"":pddata.getString("STAFFPLAN_NAME"));
		if(pddata.getString("NEW_PLAN_START_TIME")!=null && !pddata.getString("NEW_PLAN_START_TIME").equals("")){
			pdParend.put("start_date",formatDate(pddata.getString("NEW_PLAN_START_TIME")));
		}else{
			pdParend.put("start_date","");
		}
		
		if(pddata.getString("NEW_PLAN_END_TIME")!=null && !pddata.getString("NEW_PLAN_END_TIME").equals("") && pddata.getString("NEW_PLAN_START_TIME")!=null && !pddata.getString("NEW_PLAN_START_TIME").equals("")){
			pdParend.put("duration", Tools.caculateTotalTime(pddata.getString("NEW_PLAN_END_TIME"),pddata.getString("NEW_PLAN_START_TIME")));	
		}else{
			pdParend.put("duration", 0);	
		}
		pdParend.put("progress", 0);
		pdParend.put("open", true);
		pdParend.put("hh", 0);
		pdParend.put("FTYPE", "E");
		tvarlist.add(pdParend);
		
		List<PageData>	varListT = phaseinstanceService.listGante3(pd);
		for (PageData pageData2 : varListT) {
			PageData pd2=new PageData();
			String fatherTwoID=this.get32UUID();
			pd2.put("id", fatherTwoID);
			pd2.put("parent", fatherID);
			pd2.put("text", pageData2.getString("FTASK_NAME"));
			if(pageData2.getString("START_TIME")!=null && !pageData2.getString("START_TIME").equals("")){
				pd2.put("start_date", formatDate(pageData2.getString("START_TIME")));
			}else{
				pd2.put("start_date", "");
			}
			if(pageData2.getString("END_TIME")!=null && !pageData2.getString("END_TIME").equals("") && pageData2.getString("START_TIME")!=null && !pageData2.getString("START_TIME").equals("")){
				pd2.put("duration", Tools.caculateTotalTime(pageData2.getString("END_TIME"),pageData2.getString("START_TIME")));
			}else{
				pd2.put("duration", 0);
			}
			
			pd2.put("progress", 0);
			pd2.put("open", true);
			pd2.put("hh", pageData2.get("hh"));
			pd2.put("FTYPE",pageData2.get("FTYPE"));
			tvarlist.add(pd2);
		}
		
		map.put("tvarlist", tvarlist);
		map.put("start_date", pddata.getString("NEW_PLAN_START_TIME"));
		map.put("end_date", pddata.getString("NEW_PLAN_END_TIME"));
		map.put("result", errInfo);
		return map;
	}
	  /** 
     * 日期格式字符串转换成时间戳 
     * @param date 字符串日期 
     * @param format 如：yyyy-MM-dd HH:mm:ss 
     * @return 
     */  
    public static String date2TimeStamp(String date_str,String format){  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return String.valueOf(sdf.parse(date_str).getTime()/1000);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "";  
    }  
    //日期转换为时间戳 (精确到毫秒)
	 public static long timeToStamp(String timers) {
		Date d = new Date();
		long timeStemp = 0;
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = sf.parse(timers);// 日期转换为时间戳
		} catch (Exception e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		}
		timeStemp = d.getTime();
		return timeStemp;
	}
	 public static String formatDate(String days){
		 Date d = new Date();
		 try {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				d = sf.parse(days);
			} catch (Exception e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			}
		 String daysStemp=Tools.date2Str(d,"dd-MM-yyyy");
		 return daysStemp;
	 }
}
