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
import org.yy.service.project.manager.PROJECTTASKService;
import org.yy.service.project.manager.RUNDETAILService;

/** 
 * 说明：执行明细
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-04
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/rundetail")
public class RUNDETAILController extends BaseController {
	
	@Autowired
	private RUNDETAILService rundetailService;
	@Autowired
	private PROJECTTASKService projecttaskService;
	
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
		pd.put("RDPROJECTTASK_ID", pd.getString("RDPROJECTTASK_ID"));	//
		pd.put("RUNDETAIL_ID", this.get32UUID());	//主键
		pd.put("RDCREATE_TIME", Tools.date2Str(new Date()));
		pd.put("RDCREATOR", Jurisdiction.getName());
		pd.put("RDLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pd.put("RDLAST_MODIFIER", Jurisdiction.getName());
		pd.put("RDTYPE", "手动");
		pd.put("RDSTATE", "已结束");
		pd.put("RDVISIBLE", "1");
		pd.put("RDISOVER", "否");
		rundetailService.save(pd);
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
		rundetailService.upVisible(pd);
		PageData pdMain = projecttaskService.findById(pd);	//根据ID读取
		pdMain.put("FUPTYPE", "TASK");
		rundetailService.editActual(pdMain);//更新任务表实际时间、实际总工时、正常工时、加班工时、完成进度、当前计划状态
		
		pdMain.put("FUPTYPE", "STAFFPLAN");
		rundetailService.editActual(pdMain);//更新人员计划表实际时间、实际总工时、正常工时、加班工时、当前计划状态、执行状态
		
		pdMain.put("FUPTYPE", "DEPTPLAN");
		rundetailService.editActual(pdMain);//更新部门计划表实际时间、实际总工时、正常工时、加班工时、当前计划状态、执行状态
		
		pdMain.put("FUPTYPE", "PROPLAN");
		rundetailService.editActual(pdMain);//更新部门计划表实际时间、实际总工时、正常工时、加班工时、当前计划状态、执行状态
		//rundetailService.delete(pd);
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
		PageData pdTask = new PageData();
		pd = this.getPageData();
		pdTask.put("PROJECTTASK_ID", pd.getString("RDPROJECTTASK_ID"));
		pd.put("RDLAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pd.put("RDLAST_MODIFIER", Jurisdiction.getName());
		rundetailService.edit(pd);
		PageData pdNum =rundetailService.getNum(pd);//获得执行中明细数量
		if(pdNum != null && Integer.parseInt(pdNum.get("FNUM").toString()) >0) {
			pdTask.put("PTRUNSTATE", "执行中");
			projecttaskService.editState(pdTask);//修改执行状态
		}else {
			pdTask.put("PTRUNSTATE", "暂停");
			projecttaskService.editState(pdTask);//修改执行状态
		}
		PageData pdMain = projecttaskService.findById(pdTask);	//根据ID读取
		pdMain.put("FUPTYPE", "TASK");
		rundetailService.editActual(pdMain);//更新任务表实际时间、实际工时、完成进度、当前计划状态
		
		pdMain.put("FUPTYPE", "STAFFPLAN");
		rundetailService.editActual(pdMain);//更新人员计划表实际时间、实际工时、当前计划状态、执行状态
		
		pdMain.put("FUPTYPE", "DEPTPLAN");
		rundetailService.editActual(pdMain);//更新部门计划表实际时间、实际工时、当前计划状态、执行状态
		
		pdMain.put("FUPTYPE", "PROPLAN");
		rundetailService.editActual(pdMain);//更新部门计划表实际时间、实际工时、当前计划状态、执行状态
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
		List<PageData>	varList = rundetailService.list(page);	//列出RUNDETAIL列表
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
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = rundetailService.findById(pd);	//根据ID读取
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
			rundetailService.deleteAll(ArrayDATA_IDS);
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
		titles.add("项目任务主键");	//1
		titles.add("开始时间");	//2
		titles.add("结束时间");	//3
		titles.add("工时");	//4
		titles.add("备注");	//5
		titles.add("状态");	//6
		titles.add("是否删除");	//7
		titles.add("创建人");	//8
		titles.add("创建时间");	//9
		titles.add("最后修改人");	//10
		titles.add("最后修改时间");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = rundetailService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("RDPROJECTTASK_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("RDSTARTTIME"));	    //2
			vpd.put("var3", varOList.get(i).getString("RDENDTIME"));	    //3
			vpd.put("var4", varOList.get(i).get("RDHOUR").toString());	//4
			vpd.put("var5", varOList.get(i).getString("RDNOTE"));	    //5
			vpd.put("var6", varOList.get(i).getString("RDSTATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("RDVISIBLE"));	    //7
			vpd.put("var8", varOList.get(i).getString("RDCREATOR"));	    //8
			vpd.put("var9", varOList.get(i).getString("RDCREATE_TIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("RDLAST_MODIFIER"));	    //10
			vpd.put("var11", varOList.get(i).getString("RDLAST_MODIFIED_TIME"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
