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
import org.yy.service.mdm.EQM_MAINTAIN_WORKService;
import org.yy.service.mdm.EQM_MAINTAIN_WORKMXTWOService;
import org.yy.service.mdm.EQM_MAINTAIN_WORKMxService;

/** 
 * 说明：设备维修报工
 * 作者：YuanYe
 * 时间：2020-02-19
 * 
 */
@Controller
@RequestMapping("/eqm_maintain_work")
public class EQM_MAINTAIN_WORKController extends BaseController {
	
	@Autowired
	private EQM_MAINTAIN_WORKService eqm_maintain_workService;
	
	@Autowired
	private EQM_MAINTAIN_WORKMxService eqm_maintain_workmxService;
	
	@Autowired
	private EQM_MAINTAIN_WORKMXTWOService eqm_maintain_workmxtwoService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_maintain_work:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData numpd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_MAINTAIN_WORK_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		numpd.put("FDATE", Tools.date2Str(new Date(),"yyyyMMdd"));
		numpd=eqm_maintain_workService.findByNum(numpd);//通过时间获取编号数据
		String number="WX"+Tools.date2Str(new Date(),"yyyyMMdd");
		pd.put("FMAINTAIN_NUMBER", number+numpd.getString("FNUMBER"));//维修编号
		eqm_maintain_workService.save(pd);
		pd = eqm_maintain_workService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_maintain_work:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
				eqm_maintain_workService.delete(pd);
				eqm_maintain_workmxService.deleteWork(pd);//通过设备维修报工ID删除设备故障处理方法
				eqm_maintain_workmxtwoService.deleteWork(pd);//通过设备维修报工ID删除预防及改善措施
				
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_maintain_work:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_maintain_workService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_maintain_work:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = eqm_maintain_workService.list(page);	//列出EQM_MAINTAIN_WORK列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("eqm_maintain_work:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_maintain_workService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
		titles.add("维修单号");	//1
		titles.add("设备基础资料ID");	//2
		titles.add("设备名称");	//3
		titles.add("设备编号");	//4
		titles.add("设备类型");	//5
		titles.add("工作中心");	//6
		titles.add("申请日期");	//7
		titles.add("故障发现时间");	//8
		titles.add("维修开始时间");	//9
		titles.add("维修结束时间");	//10
		titles.add("故障处理等级");	//11
		titles.add("故障现象");	//12
		titles.add("故障原因");	//13
		titles.add("验收结论");	//14
		titles.add("验收结论人");	//15
		titles.add("分析及结果评定");	//16
		titles.add("结果评定人");	//17
		titles.add("创建人");	//18
		titles.add("创建时间");	//19
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_maintain_workService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FMAINTAIN_NUMBER"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQM_BASE_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FEQM_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FEQM_FNUMBER"));	    //4
			vpd.put("var5", varOList.get(i).getString("FEQM_TYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("FWORKCENTER"));	    //6
			vpd.put("var7", varOList.get(i).getString("FCLAIMER_DATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FBREAKBOWN_DATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("FMAINTAIN_START_DATE"));	    //9
			vpd.put("var10", varOList.get(i).getString("FMAINTAIN_END_DATE"));	    //10
			vpd.put("var11", varOList.get(i).getString("FBREAKBOWN_GRADE"));	    //11
			vpd.put("var12", varOList.get(i).getString("FBREAKBOWN_CAUSE"));	    //12
			vpd.put("var13", varOList.get(i).getString("FBREAKBOWN_ISSUE"));	    //13
			vpd.put("var14", varOList.get(i).getString("FVERIFICAT_RESULT"));	    //14
			vpd.put("var15", varOList.get(i).getString("FACCEPTOR"));	    //15
			vpd.put("var16", varOList.get(i).getString("FRESULT_ANALYSIS"));	    //16
			vpd.put("var17", varOList.get(i).getString("FADJUSTER"));	    //17
			vpd.put("var18", varOList.get(i).getString("FCREATOR"));	    //18
			vpd.put("var19", varOList.get(i).getString("CREATE_TIME"));	    //19
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
