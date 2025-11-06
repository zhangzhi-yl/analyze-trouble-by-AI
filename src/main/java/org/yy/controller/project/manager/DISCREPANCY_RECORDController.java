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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.project.manager.DISCREPANCY_RECORDService;

/** 
 * 说明：差异记录
 * 作者：YuanYes QQ356703572
 * 时间：2021-09-16
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/discrepancy_record")
public class DISCREPANCY_RECORDController extends BaseController {
	
	@Autowired
	private DISCREPANCY_RECORDService discrepancy_recordService;
	
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
		List<PageData> varList = discrepancy_recordService.listAll(pd);	//依据ID获取已生成数据列表
		List<PageData> varListO = discrepancy_recordService.getAllListWLDB(pd);	//获取差异信息
		if(varList.size()>0){		//判断是否已经生成过记录
			discrepancy_recordService.delete(pd);	//已经生成的，再次生成时删除上一次生成的
			if(varListO.size()>0){	//判断是否存在差异信息
				for(int i = 0;i < varListO.size();i++){
					PageData pdO = varListO.get(i);
					pd.put("DISCREPANCY_RECORD_ID", this.get32UUID());	//主键
					pd.put("PLAN_MATERIALS_NAME", pdO.get("MAT_NAME1"));	//计划物料名称
					pd.put("PLAN_MATERIALS_SPECIFICATIONS", pdO.get("MAT_SPECS1"));	//计划物料规格
					pd.put("PAN_MATERIALS_CODE", pdO.get("MAT_CODE1"));	//计划物料代码
					pd.put("PAN_MATERIALS_NUMBER", pdO.get("BOM_COUNT1"));	//计划物料数量
					pd.put("ACTUAL_MATERIALS_NAME", pdO.get("MAT_NAME2"));	//实际物料名称
					pd.put("ACTUAL_MATERIALS_SPECIFICATIONS", pdO.get("MAT_SPECS2"));	//实际物料规格
					pd.put("ACTUAL_MATERIALS_CODE", pdO.get("MAT_CODE2"));	//实际物料代码
					pd.put("ACTUAL_MATERIALS_NUMBER", pdO.get("BOM_COUNT2"));	//实际物料数量
					pd.put("CHANGE_STATE", pdO.get("FChangeState"));	//变更状态
					pd.put("PROJECT_ID", pd.get("PROJECT_ID"));	//项目ID
					discrepancy_recordService.save(pd);
				}
			}
		}else{
			if(varListO.size()>0){
				for(int i = 0;i < varListO.size();i++){
					PageData pdO = varListO.get(i);
					pd.put("DISCREPANCY_RECORD_ID", this.get32UUID());	//主键
					pd.put("PLAN_MATERIALS_NAME", pdO.get("MAT_NAME1"));	//计划物料名称
					pd.put("PLAN_MATERIALS_SPECIFICATIONS", pdO.get("MAT_SPECS1"));	//计划物料规格
					pd.put("PAN_MATERIALS_CODE", pdO.get("MAT_CODE1"));	//计划物料代码
					pd.put("PAN_MATERIALS_NUMBER", pdO.get("BOM_COUNT1"));	//计划物料数量
					pd.put("ACTUAL_MATERIALS_NAME", pdO.get("MAT_NAME2"));	//实际物料名称
					pd.put("ACTUAL_MATERIALS_SPECIFICATIONS", pdO.get("MAT_SPECS2"));	//实际物料规格
					pd.put("ACTUAL_MATERIALS_CODE", pdO.get("MAT_CODE2"));	//实际物料代码
					pd.put("ACTUAL_MATERIALS_NUMBER", pdO.get("BOM_COUNT2"));	//实际物料数量
					pd.put("CHANGE_STATE", pdO.get("FChangeState"));	//变更状态
					pd.put("PROJECT_ID", pd.get("PROJECT_ID"));	//项目ID
					discrepancy_recordService.save(pd);
				}
			}
		}
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
		discrepancy_recordService.delete(pd);
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
		discrepancy_recordService.edit(pd);
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
		List<PageData>	varList = discrepancy_recordService.list(page);	//列出DISCREPANCY_RECORD列表
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
	@RequiresPermissions("discrepancy_record:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = discrepancy_recordService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("discrepancy_record:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			discrepancy_recordService.deleteAll(ArrayDATA_IDS);
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
		titles.add("计划物料名称");	//1
		titles.add("计划物料规格");	//2
		titles.add("计划物料代码");	//3
		titles.add("计划物料ID");	//4
		titles.add("计划物料数量");	//5
		titles.add("实际物料名称");	//6
		titles.add("实际物料规格");	//7
		titles.add("实际物料代码");	//8
		titles.add("实际物料ID");	//9
		titles.add("实际物料数量");	//10
		titles.add("项目名称");	//11
		titles.add("项目号");	//12
		titles.add("客户名称");	//13
		titles.add("项目ID");	//14
		titles.add("创建时间");	//15
		titles.add("创建人");	//16
		titles.add("创建人ID");	//17
		titles.add("修改时间");	//18
		titles.add("修改人");	//19
		titles.add("修改人ID");	//20
		titles.add("差异原因");	//21
		dataMap.put("titles", titles);
		List<PageData> varOList = discrepancy_recordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PLAN_MATERIALS_NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("PLAN_MATERIALS_SPECIFICATIONS"));	    //2
			vpd.put("var3", varOList.get(i).getString("PAN_MATERIALS_CODE"));	    //3
			vpd.put("var4", varOList.get(i).getString("PAN_MATERIALS_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("PAN_MATERIALS_NUMBER"));	    //5
			vpd.put("var6", varOList.get(i).getString("ACTUAL_MATERIALS_NAME"));	    //6
			vpd.put("var7", varOList.get(i).getString("ACTUAL_MATERIALS_SPECIFICATIONS"));	    //7
			vpd.put("var8", varOList.get(i).getString("ACTUAL_MATERIALS_CODE"));	    //8
			vpd.put("var9", varOList.get(i).getString("ACTUAL_MATERIALS_ID"));	    //9
			vpd.put("var10", varOList.get(i).getString("ACTUAL_MATERIALS_NUMBER"));	    //10
			vpd.put("var11", varOList.get(i).getString("PROJECT_NAME"));	    //11
			vpd.put("var12", varOList.get(i).getString("PROJECT_NUMBER"));	    //12
			vpd.put("var13", varOList.get(i).getString("CUSTOMER_NAME"));	    //13
			vpd.put("var14", varOList.get(i).getString("PROJECT_ID"));	    //14
			vpd.put("var15", varOList.get(i).getString("FCREATE_TIME"));	    //15
			vpd.put("var16", varOList.get(i).getString("FFOUNDER"));	    //16
			vpd.put("var17", varOList.get(i).getString("FFOUNDER_ID"));	    //17
			vpd.put("var18", varOList.get(i).getString("FMODIFY_TIME"));	    //18
			vpd.put("var19", varOList.get(i).getString("FMODIFY_NAME"));	    //19
			vpd.put("var20", varOList.get(i).getString("FMODIFY_ID"));	    //20
			vpd.put("var21", varOList.get(i).getString("CAUSE_OF_DIFFERENCE"));	    //21
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
