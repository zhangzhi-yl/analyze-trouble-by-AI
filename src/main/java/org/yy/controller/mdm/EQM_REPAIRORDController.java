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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mdm.EQM_REPAIRORDService;
import org.yy.service.mdm.EQM_REPAIRORDMxService;

/** 
 * 说明：设备报修工单
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_repairord")
public class EQM_REPAIRORDController extends BaseController {
	
	@Autowired
	private EQM_REPAIRORDService eqm_repairordService;
	
	@Autowired
	private EQM_REPAIRORDMxService eqm_repairordmxService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("eqm_repairord:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_REPAIRORD_ID", this.get32UUID());	//主键
		eqm_repairordService.save(pd);
		pd = eqm_repairordService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("eqm_repairord:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(Integer.parseInt(eqm_repairordmxService.findCount(pd).get("zs").toString()) > 0){
				errInfo = "error";
			}else{
				eqm_repairordService.delete(pd);
			}
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
	@RequiresPermissions("eqm_repairord:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_repairordService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("eqm_repairord:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = eqm_repairordService.list(page);	//列出EQM_REPAIRORD列表
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
	@RequiresPermissions("eqm_repairord:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_repairordService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
		titles.add("设备标识");	//1
		titles.add("设备名称");	//2
		titles.add("规格型号");	//3
		titles.add("申报人");	//4
		titles.add("申报人联系方式");	//5
		titles.add("申报日期");	//6
		titles.add("设备类型");	//7
		titles.add("设备位置");	//8
		titles.add("故障代码");	//9
		titles.add("故障现象描述");	//10
		titles.add("申报人主管签字");	//11
		titles.add("申报工程部门签字");	//12
		titles.add("紧急状态");	//13
		titles.add("要求完成时间");	//14
		titles.add("描述");	//15
		titles.add("接单人");	//16
		titles.add("接单时间");	//17
		titles.add("完成状态");	//18
		titles.add("完成时间");	//19
		titles.add("接单人描述");	//20
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_repairordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FIDENTIFY"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FSPECS"));	    //3
			vpd.put("var4", varOList.get(i).getString("DECLARANT_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("DECLARANT_PHONE"));	    //5
			vpd.put("var6", varOList.get(i).getString("DECLARE_DATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FTYPE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FLOC"));	    //8
			vpd.put("var9", varOList.get(i).getString("FAULT_CODE"));	    //9
			vpd.put("var10", varOList.get(i).getString("FAULT_DES"));	    //10
			vpd.put("var11", varOList.get(i).getString("DECLARANTSATRAP_SIGNATURE"));	    //11
			vpd.put("var12", varOList.get(i).getString("DECLAREDEP_SIGNATURE"));	    //12
			vpd.put("var13", varOList.get(i).getString("URGENT_STATE"));	    //13
			vpd.put("var14", varOList.get(i).getString("REQUIRE_COMPLETE_TIME"));	    //14
			vpd.put("var15", varOList.get(i).getString("FDES"));	    //15
			vpd.put("var16", varOList.get(i).getString("GETORDER_NAME"));	    //16
			vpd.put("var17", varOList.get(i).getString("GETORDER_TIME"));	    //17
			vpd.put("var18", varOList.get(i).getString("COMPLETE_STATE"));	    //18
			vpd.put("var19", varOList.get(i).getString("COMPLETE_TIME"));	    //19
			vpd.put("var20", varOList.get(i).getString("GETORDER_DES"));	    //20
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
