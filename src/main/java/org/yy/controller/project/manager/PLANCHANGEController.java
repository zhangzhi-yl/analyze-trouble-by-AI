package org.yy.controller.project.manager;

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
import org.yy.service.project.manager.PLANCHANGEService;
import org.yy.service.project.manager.PROJECTTASKService;

/** 
 * 说明：项目计划任务变更
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-08
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/planchange")
public class PLANCHANGEController extends BaseController {
	
	@Autowired
	private PLANCHANGEService planchangeService;
	@Autowired
	private PROJECTTASKService projecttaskService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("planchange:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdSave = new PageData();
		pd = this.getPageData();
		PageData pdP = projecttaskService.findById(pd);	//根据ID读取任务信息
		PageData pdNum = projecttaskService.findNum(pd);	//查询任务未审核通过变更记录数量
		if(pdNum !=null && Integer.parseInt(pdNum.get("FNUM").toString())>0) {
			errInfo = "fail1";//
		}else {
		pdSave.put("PLANCHANGE_ID", this.get32UUID());	//主键
		pdSave.put("PCLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pdSave.put("PCLAST_MODIFIER", Jurisdiction.getName());
		pdSave.put("PCCREATE_TIME", Tools.date2Str(new Date()));
		pdSave.put("PCCREATOR", Jurisdiction.getName());
		
		pdSave.put("PCPROJECTTASK_ID", pdP.getString("PROJECTTASK_ID"));
		pdSave.put("PCPROJECT_ID", pdP.getString("PTPROJECT_ID"));
		pdSave.put("PCEQUIPMENT_ID", pdP.getString("PTEQUIPMENT_ID"));
		pdSave.put("PCSTAFFPLAN_ID", pdP.getString("PTSTAFFPLAN_ID"));
		pdSave.put("PCPRINCIPAL", pdP.getString("PTPRINCIPAL"));
		pdSave.put("PCPLANSTARTDATE", pdP.getString("PTPLANSTARTDATE"));
		pdSave.put("PCPLANENDDATE", pdP.getString("PTPLANENDDATE"));
		pdSave.put("PCNEWENDDATE", pd.getString("PCNEWENDDATE"));
		pdSave.put("PCNEWSTARTDATE", pd.getString("PCNEWSTARTDATE"));
		pdSave.put("PCPHASENAME", pdP.getString("PTPHASENAME"));
		pdSave.put("PCPROJECT_PRINCIPAL", pdP.getString("PTPROJECT_PRINCIPAL"));
		pdSave.put("PCDEPT", pdP.getString("PTDEPT"));
		pdSave.put("PCCHANGEREASON", pd.getString("PCCHANGEREASON"));
		pdSave.put("RUN_STATUS", "未提交");
		pdSave.put("PCVISIBLE", "1");
		planchangeService.save(pdSave);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("planchange:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PCVISIBLE", "0");
		planchangeService.upVisible(pd);//反写删除状态
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("planchange:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PCLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pd.put("PCLAST_MODIFIER", Jurisdiction.getName());
		planchangeService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("planchange:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = planchangeService.list(page);	//列出PLANCHANGE列表
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
	//@RequiresPermissions("planchange:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = planchangeService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("planchange:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			planchangeService.deleteAll(ArrayDATA_IDS);
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
		titles.add("项目主键");	//1
		titles.add("设备主键");	//2
		titles.add("人员计划主键");	//3
		titles.add("项目任务主键");	//4
		titles.add("负责人");	//5
		titles.add("原计划开始时间");	//6
		titles.add("计划结束时间");	//7
		titles.add("新计划开始时间");	//8
		titles.add("新计划结束时间");	//9
		titles.add("阶段名称");	//10
		titles.add("活动名称");	//11
		titles.add("任务名称");	//12
		titles.add("项目负责人");	//13
		titles.add("提交人");	//14
		titles.add("提交时间");	//15
		titles.add("变更原因");	//16
		titles.add("审批状态");	//17
		titles.add("审批流ID");	//18
		titles.add("审批进度");	//19
		titles.add("创建人部门主键");	//20
		titles.add("创建人");	//21
		titles.add("创建时间");	//22
		titles.add("最后修改人");	//23
		titles.add("最后修改时间");	//24
		dataMap.put("titles", titles);
		List<PageData> varOList = planchangeService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PCPROJECT_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("PCEQUIPMENT_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("PCSTAFFPLAN_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("PCPROJECTTASK_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("PCPRINCIPAL"));	    //5
			vpd.put("var6", varOList.get(i).getString("PCPLANSTARTDATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("PCPLANENDDATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("PCNEWSTARTDATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("PCNEWENDDATE"));	    //9
			vpd.put("var10", varOList.get(i).getString("PCPHASENAME"));	    //10
			vpd.put("var11", varOList.get(i).getString("PCACTIVITYNAME"));	    //11
			vpd.put("var12", varOList.get(i).getString("PCTASK_NAME"));	    //12
			vpd.put("var13", varOList.get(i).getString("PCPROJECT_PRINCIPAL"));	    //13
			vpd.put("var14", varOList.get(i).getString("PCPOSTMAN"));	    //14
			vpd.put("var15", varOList.get(i).getString("PCPOSTTIME"));	    //15
			vpd.put("var16", varOList.get(i).getString("PCCHANGEREASON"));	    //16
			vpd.put("var17", varOList.get(i).getString("RUN_STATUS"));	    //17
			vpd.put("var18", varOList.get(i).getString("PROC_INST_ID_"));	    //18
			vpd.put("var19", varOList.get(i).getString("GENERATE_PROGRESS"));	    //19
			vpd.put("var20", varOList.get(i).getString("PCDEPT"));	    //20
			vpd.put("var21", varOList.get(i).getString("PCCREATOR"));	    //21
			vpd.put("var22", varOList.get(i).getString("PCCREATE_TIME"));	    //22
			vpd.put("var23", varOList.get(i).getString("PCLAST_MODIFIER"));	    //23
			vpd.put("var24", varOList.get(i).getString("PCLAST_MODIFIED_TIME"));	    //24
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**获得启动审核准备信息
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/setStarts")
	@ResponseBody
	public Object setStarts() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = planchangeService.getAudit(pd);
		PageData pdBack = new PageData();
		String headmans=pd.get("HEADMAN")==null?"":pd.getString("HEADMAN");
		String strs[]=headmans.split("、");
		for(int i=0;i<strs.length;i++) {				
			pd.put("NAME", strs[i]);
			PageData pdUser = planchangeService.findUser(pd);//根据姓名查用户表
			if(pdUser != null) {
				pdBack.put("ProjectGL"+(i+1), pdUser.get("USERNAME"));
			}
		}
		if(pd != null) {
			String DEPARTMENT_ID=pd.getString("DEPARTMENT_ID");
			String FSTATE=pd.getString("FSTATE");
			if(DEPARTMENT_ID.equals("e17d43d2ec2b4cccb48b8466b08f9296") && FSTATE.equals("正常")) {
				pdBack.put("SrartKeyId", "KeyChangeshouhouzhengchang");
			}else if(DEPARTMENT_ID.equals("e17d43d2ec2b4cccb48b8466b08f9296") && FSTATE.equals("超期")) {
				pdBack.put("SrartKeyId", "KeyChangeshouhouchaoqix");
			}else if((!DEPARTMENT_ID.equals("e17d43d2ec2b4cccb48b8466b08f9296")) && FSTATE.equals("正常")) {
				pdBack.put("SrartKeyId", "KeyChangeallzhengchang");
			}else if((!DEPARTMENT_ID.equals("e17d43d2ec2b4cccb48b8466b08f9296")) && FSTATE.equals("超期")) {
				
				pdBack.put("SrartKeyId", "KeyChangeallchaoqi");
			}
		}
		
		map.put("pd", pdBack);
		map.put("result", errInfo);
		return map;
	}	
}
