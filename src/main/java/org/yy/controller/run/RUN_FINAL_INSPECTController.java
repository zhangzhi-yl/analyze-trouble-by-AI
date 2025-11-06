package org.yy.controller.run;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.yy.service.fhoa.StaffService;
import org.yy.service.mom.TEMBILL_EXECUTEMxService;
import org.yy.service.mom.TEMBILL_EXECUTEService;
import org.yy.service.run.RUN_FINAL_INSPECTService;

/** 
 * 说明：终检PH执行
 * 作者：YuanYe
 * 时间：2020-03-25
 * 
 */
@Controller
@RequestMapping("/run_final_inspect")
public class RUN_FINAL_INSPECTController extends BaseController {
	
	@Autowired
	private RUN_FINAL_INSPECTService run_final_inspectService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private TEMBILL_EXECUTEService tembill_executeService;
	@Autowired
	private TEMBILL_EXECUTEMxService tembill_executemxService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("run_final_inspect:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		long res = Calendar.getInstance().getTimeInMillis();
		pd.put("FEXTEND1", res);//时间戳
		pd.put("RUN_FINAL_INSPECT_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		pd.put("LAST_MODIFIER",Jurisdiction.getName());//最后修改人
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));//最后修改时间
		run_final_inspectService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("run_final_inspect:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		run_final_inspectService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("run_final_inspect:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		long res = Calendar.getInstance().getTimeInMillis();
		pd.put("FEXTEND1", res);//时间戳
		pd.put("LAST_MODIFIER",Jurisdiction.getName());//最后修改人
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));//最后修改时间
		run_final_inspectService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**修改时间戳
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editTime")
	//@RequiresPermissions("run_final_inspect:edit")
	@ResponseBody
	public Object editTime() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		long res = Calendar.getInstance().getTimeInMillis();
		pd.put("FEXTEND1", res);//时间戳
		pd.put("LAST_MODIFIER",Jurisdiction.getName());//最后修改人
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));//最后修改时间
		System.out.println(pd+"-----------------------LAST_MODIFIED_TIME");
		run_final_inspectService.editTime(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("run_final_inspect:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = run_final_inspectService.list(page);	//列出RUN_FINAL_INSPECT列表
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
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{			
			pd = run_final_inspectService.findById(pd);	//根据ID读取
			if(pd==null) {
				result = "201";
			}
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("pd", pd);
			map.put("result", result);
		}		
		return map;
	}
	
	 /**获取用户表数据
		 * @param
		 * @throws Exception
		 */
	@RequestMapping(value="/goAdd")
	//@RequiresPermissions("run_final_inspect:edit")
	@ResponseBody
	public Object goAdd() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> roleList = staffService.listAll(pd);		//列出所有系统用户
		map.put("roleList", roleList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	/**生成终检检验单据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAddFinal")
	@ResponseBody
	public Object goAddFinal() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData tempd = new PageData();
		pd = this.getPageData();
		pd=run_final_inspectService.findById(pd);
		if(null!=pd.getString("DOCUMENTTEMPLATE_ID")&&!"".equals(pd.getString("DOCUMENTTEMPLATE_ID"))) {
			pd.put("IDNAME", "RUN_FINAL_INSPECT_ID");//主键字段名
			pd.put("ID", pd.getString("RUN_FINAL_INSPECT_ID"));//主键ID
			pd.put("FTABLE_NAME", "MRECIPE_RUN_FINAL_INSPECT");//表名
			pd.put("FIEID_NAME", "TEMBILL_EXECUTE_ID");//字段名
			tembill_executeService.saveTem(pd);//生成检验单据
			pd=run_final_inspectService.findById(pd);//根据ID读取
			tempd = tembill_executeService.findById(pd);	//根据检验单据ID读取
			List<PageData> varList = tembill_executemxService.listMxAll(pd);	//列出TEMBILL_EXECUTEMX列表
			map.put("varList", varList);
		}
		map.put("pd", tempd);
		map.put("finalPd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("run_final_inspect:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			run_final_inspectService.deleteAll(ArrayDATA_IDS);
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
		titles.add("多级阶段实例化关联编号");	//1
		titles.add("标题");	//2
		titles.add("上传凭证类型");	//3
		titles.add("检验级别");	//4
		titles.add("执行要求描述");	//5
		titles.add("执行状态");	//6
		titles.add("终检描述");	//7
		titles.add("执行人");	//8
		titles.add("执行时间");	//9
		titles.add("批次号");	//10
		titles.add("是否结束");	//11
		titles.add("是否合格");	//12
		titles.add("是否生成检验单据");	//13
		titles.add("检测模板ID");	//14
		titles.add("检测模板名称");	//15
		titles.add("质量检测ID");	//16
		titles.add("工单编号");	//17
		titles.add("PH节点ID");	//18
		titles.add("UP节点ID");	//19
		titles.add("O节点ID");	//20
		titles.add("P节点ID");	//21
		titles.add("创建人");	//22
		titles.add("创建时间");	//23
		titles.add("扩展字段1");	//24
		titles.add("扩展字段2");	//25
		titles.add("扩展字段3");	//26
		dataMap.put("titles", titles);
		List<PageData> varOList = run_final_inspectService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GE_MUTILIBPHNEW_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCAPTION"));	    //2
			vpd.put("var3", varOList.get(i).getString("FFILETYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FGRADE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FRUNFDS"));	    //5
			vpd.put("var6", varOList.get(i).getString("FRUNSTATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FINALFDS"));	    //7
			vpd.put("var8", varOList.get(i).getString("FOPERATOR"));	    //8
			vpd.put("var9", varOList.get(i).getString("FEXECUT_TIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("PRO_BATCH_NO"));	    //10
			vpd.put("var11", varOList.get(i).getString("FIFOVER"));	    //11
			vpd.put("var12", varOList.get(i).getString("FIFQUALIFIDE"));	    //12
			vpd.put("var13", varOList.get(i).getString("FIFCREATE"));	    //13
			vpd.put("var14", varOList.get(i).getString("DOCUMENTTEMPLATE_ID"));	    //14
			vpd.put("var15", varOList.get(i).getString("FSTENCIL_NAME"));	    //15
			vpd.put("var16", varOList.get(i).getString("TEMBILL_EXECUTE_ID"));	    //16
			vpd.put("var17", varOList.get(i).getString("PLAN_ID"));	    //17
			vpd.put("var18", varOList.get(i).getString("RUN_PH_ID"));	    //18
			vpd.put("var19", varOList.get(i).getString("RUN_UP_ID"));	    //19
			vpd.put("var20", varOList.get(i).getString("RUN_O_ID"));	    //20
			vpd.put("var21", varOList.get(i).getString("RUN_P_ID"));	    //21
			vpd.put("var22", varOList.get(i).getString("FCREATOR"));	    //22
			vpd.put("var23", varOList.get(i).getString("CREATE_TIME"));	    //23
			vpd.put("var24", varOList.get(i).getString("FEXTEND1"));	    //24
			vpd.put("var25", varOList.get(i).getString("FEXTEND2"));	    //25
			vpd.put("var26", varOList.get(i).getString("FEXTEND3"));	    //26
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
