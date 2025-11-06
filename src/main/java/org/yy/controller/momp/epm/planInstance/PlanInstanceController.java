package org.yy.controller.momp.epm.planInstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.yy.service.momp.epm.activityinstance.ActivityInstanceService;
import org.yy.service.momp.epm.activitytemplate.ActivityTemplateService;
import org.yy.service.momp.epm.phaseinstance.PhaseInstanceService;
import org.yy.service.momp.epm.phasetemplate.PhaseTemplateService;
import org.yy.service.momp.epm.planinstance.PlanInstanceService;
import org.yy.service.momp.epm.plantemplate.PlanTemplateService;


/** 
 * 说明：项目计划
 * 作者：YuanYe
 * 时间：2020-03-13
 * 
 */
@Controller
@RequestMapping("/planinstance")
public class PlanInstanceController extends BaseController {
	
	@Autowired
	private PlanInstanceService planinstanceService;
	@Autowired
	private PlanTemplateService plantemplateService;
	@Autowired
	private ActivityTemplateService activitytemplateService;
	@Autowired
	private ActivityInstanceService activityinstanceService;
	@Autowired
	private PhaseTemplateService phasetemplateService;
	@Autowired
	private PhaseInstanceService phaseinstanceService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("planinstance:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PLANINSTANCE_ID", this.get32UUID());	//主键
		pd.put("FPICONTRACTINNERID", this.get32UUID());//项目id
		pd.put("FCTIME", Tools.date2Str(new Date())); //创建时间
        pd.put("FCREATOR", Jurisdiction.getUsername()); //创建人
        pd.put("FPISTATE", "创建");
        pd.put("FPICOMRATE", "0");
        pd.put("FPICUSTOMERID", "");
        pd.put("FPICUSTOMERNAME", "");
        pd.put("FPIPCHANCEID", "");
        pd.put("FPIPCHANCENAME", "");
        pd.put("FPIDUTYSALEID", "");
        pd.put("FPIDUTYSALENAME", "");
        pd.put("FPIDUTYBUSYID", "");
        pd.put("FPIDUTYBUSYNAME", "");
		planinstanceService.save(pd);
		try {
			List<PageData>	varListPhaseM= phasetemplateService.listByParentId(pd);
			for (Iterator iterator = varListPhaseM.iterator(); iterator.hasNext();) {
				PageData pdPhaseM = (PageData) iterator.next();
				  //组装阶段实例数据 
				PageData pdPhase = new PageData();
				pdPhase.put("PHASEINSTANCE_ID", this.get32UUID());	//主键
				pdPhase.put("FHINAME",pdPhaseM.getString("FHTNAME") );//名称
				pdPhase.put("FHIDESCRIPTION",pdPhaseM.getString("FHTDESCRIPTION"));//描述
				pdPhase.put("FHIREMARK",pdPhaseM.getString("FHTREMARK"));//备注
				pdPhase.put("FHILEVEL",pdPhaseM.getString("FHTLEVEL"));//所处级别
				pdPhase.put("PLANINSTANCE_ID",pd.getString("PLANINSTANCE_ID"));//阶段父ID
				pdPhase.put("FHISTATE","创建");
				pdPhase.put("FHICOMRATE", "0");
				pdPhase.put("FHIDUTYNAME", pd.getString("FPIDUTYNAME")!=null?pd.getString("FPIDUTYNAME"):"");
				pdPhase.put("FHIDUTYMAN", pd.getString("FPIDUTYMAN")!=null?pd.getString("FPIDUTYMAN"):"");
				pdPhase.put("FCTIME", Tools.date2Str(new Date())); //创建时间
				pdPhase.put("FCREATOR", pd.getString("USERNAME")); //创建人
				phaseinstanceService.save(pdPhase);  //增加阶段实例
				///按照阶段模板ID筛选出阶段模板下的活动列表，循环增加活动实例
				List<PageData>	varListActivityM=activitytemplateService.listByParentId(pdPhaseM);
				for (Iterator iterator2 = varListActivityM.iterator(); iterator2.hasNext();) {
					PageData pdActivityM = (PageData) iterator2.next();  //获取到单个活动的模板
					//组装活动实例数据
					PageData pdActivity = new PageData();
					pdActivity.put("ACTIVITYINSTANCE_ID", this.get32UUID());	//主键
					pdActivity.put("FAINAME", pdActivityM.getString("FATNAME"));
					if("确定实施期预算".equals(pdActivityM.getString("FATNAME"))||"用户验收".equals(pdActivityM.getString("FATNAME"))||"客户签收".equals(pdActivityM.getString("FATNAME"))){
						pdActivity.put("FCHECK", "YES");
					}else{
						pdActivity.put("FCHECK", "NO");
					}
					pdActivity.put("FAIDESCRIPTION", pdActivityM.getString("FATDESCRIPTION"));
					pdActivity.put("FAIREMARK", pdActivityM.getString("FATREMARK"));
					pdActivity.put("FAILEVEL", pdActivityM.getString("FATLEVEL"));
					pdActivity.put("PHASEINSTANCE_ID", pdPhase.getString("PHASEINSTANCE_ID"));
					pdActivity.put("PLANINSTANCE_ID",pd.getString("PLANINSTANCE_ID"));
					pdActivity.put("FAISTATE", "创建");
					pdActivity.put("FAICOMRATE", "0");
					pdActivity.put("FCTIME", Tools.date2Str(new Date())); //创建时间
					pdActivity.put("FCREATOR", pd.getString("USERNAME")); //创建人
					activityinstanceService.save(pdActivity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("planinstance:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		planinstanceService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	@RequestMapping(value="/deleteJH")
	@ResponseBody
	public Object deleteJH() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		planinstanceService.deleteJH(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("planinstance:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		planinstanceService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("planinstance:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		pd.put("FPTSTAFFID", Jurisdiction.getUsername());
		List<PageData>	varList = planinstanceService.list(page);	//列出PlanInstance列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	
	/**列表无分页
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	@ResponseBody
	public Object listAll(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("FPTSTAFFID", Jurisdiction.getUsername());
		List<PageData>	varList = planinstanceService.listAll(pd);	//列出PlanInstance列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/findBySno")
	@ResponseBody
	public Object findBySno() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData  newPd = planinstanceService.findBySno(pd);
		 try{
			 if(newPd==null){
				 errInfo = "200";
			 }else{
				 errInfo = "201";
			 }
			 
	        }catch (Exception e){
	        	errInfo = "500";
	            map.put("msg","no");
	            map.put("msgText","未知错误，请联系管理员！");
	        }finally{
	            map.put("result", errInfo);
	        }
		return map;
	}
	
	@RequestMapping(value="/listUserAll")
	@ResponseBody
	public Object listUserAll() throws Exception{
		
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		try{
            List<PageData>	varList = planinstanceService.listUserAll(pd);	//列出PlantList列表        
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
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("planinstance:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = planinstanceService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("planinstance:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			planinstanceService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	@RequestMapping(value="/releaseplan")
	@ResponseBody
	public Object releaseplan() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
        String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData  pdArecordnum =activityinstanceService.getArecordnum(pd);
		if(Integer.parseInt(""+pdArecordnum.get("arecordnum")) > 0){
			 map.put("pd",pd);
			 map.put("result","201");
	         map.put("msg","no");
	         map.put("msgText","下发失败，请验证该计划下是否有活动没填入开始结束时间和负责人！");
	         return map;
		}
		try{
			///下发无异常之后修改运行状态
			pd.put("FPISTATE","运行");
			planinstanceService.updatePlanState(pd);
            map.put("pd",pd);
            result = "200";
            map.put("msg","ok");
            map.put("msgText","计划发布成功！");
            
        }catch (Exception e){
            result = "500";
            map.put("msg","no");
            map.put("msgText","未知错误，请联系管理员！");
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
        PageData  newPd = planinstanceService.findById(pd);
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
		titles.add("id");	//1
		titles.add("计划名称");	//2
		titles.add("模板id");	//3
		titles.add("模板名称");	//4
		titles.add("实例描述");	//5
		titles.add("描述");	//6
		titles.add("运行状态");	//7
		titles.add("项目经理");	//8
		titles.add("项目经理账号");	//9
		titles.add("计划开始时间");	//10
		titles.add("计划结束时间");	//11
		titles.add("实际开始时间");	//12
		titles.add("实际结束时间");	//13
		titles.add("备注14");	//14
		titles.add("备注15");	//15
		titles.add("备注16");	//16
		titles.add("备注17");	//17
		titles.add("备注18");	//18
		titles.add("备注19");	//19
		titles.add("备注20");	//20
		titles.add("备注21");	//21
		titles.add("备注22");	//22
		titles.add("项目名称");	//23
		titles.add("项目id");	//24
		titles.add("创建人");	//25
		titles.add("创建时间");	//26
		dataMap.put("titles", titles);
		List<PageData> varOList = planinstanceService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PLANINSTANCE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FPINAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("PLANTEMPLATE_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("FPTNAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("FPIDESCRIPTION"));	    //5
			vpd.put("var6", varOList.get(i).getString("FPIREMARK"));	    //6
			vpd.put("var7", varOList.get(i).getString("FPISTATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FPIDUTYNAME"));	    //8
			vpd.put("var9", varOList.get(i).getString("FPIDUTYMAN"));	    //9
			vpd.put("var10", varOList.get(i).getString("FPISTARTTIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("FPIENDTIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("FACTUALSTART"));	    //12
			vpd.put("var13", varOList.get(i).getString("FACTUALEND"));	    //13
			vpd.put("var14", varOList.get(i).getString("FPICOMRATE"));	    //14
			vpd.put("var15", varOList.get(i).getString("FPICUSTOMERID"));	    //15
			vpd.put("var16", varOList.get(i).getString("FPICUSTOMERNAME"));	    //16
			vpd.put("var17", varOList.get(i).getString("FPIPCHANCEID"));	    //17
			vpd.put("var18", varOList.get(i).getString("FPIPCHANCENAME"));	    //18
			vpd.put("var19", varOList.get(i).getString("FPIDUTYSALEID"));	    //19
			vpd.put("var20", varOList.get(i).getString("FPIDUTYSALENAME"));	    //20
			vpd.put("var21", varOList.get(i).getString("FPIDUTYBUSYID"));	    //21
			vpd.put("var22", varOList.get(i).getString("FPIDUTYBUSYNAME"));	    //22
			vpd.put("var23", varOList.get(i).getString("FPICONTRACTNAME"));	    //23
			vpd.put("var24", varOList.get(i).getString("FPICONTRACTINNERID"));	    //24
			vpd.put("var25", varOList.get(i).getString("FCREATOR"));	    //25
			vpd.put("var26", varOList.get(i).getString("FCTIME"));	    //26
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
