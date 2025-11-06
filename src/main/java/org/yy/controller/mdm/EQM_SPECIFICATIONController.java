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
import org.yy.service.mdm.EQM_SPECIFICATIONService;

/** 
 * 说明：设备规范明细资料
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_specification")
public class EQM_SPECIFICATIONController extends BaseController {
	
	@Autowired
	private EQM_SPECIFICATIONService eqm_specificationService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_specification:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_SPECIFICATION_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		eqm_specificationService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_specification:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_specificationService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_specification:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_specificationService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_specification:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_specificationService.list(page);	//列出EQM_SPECIFICATION列表
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
	//@RequiresPermissions("eqm_specification:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_specificationService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_specification:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_specificationService.deleteAll(ArrayDATA_IDS);
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
		titles.add("列KEY名");	//1
		titles.add("列VALUE值");	//2
		titles.add("描述");	//3
		titles.add("扩展字段1");	//4
		titles.add("扩展字段2");	//5
		titles.add("扩展字段3");	//6
		titles.add("扩展字段4");	//7
		titles.add("备注9");	//8
		titles.add("备注10");	//9
		titles.add("备注11");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_specificationService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("KEY_NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("FVALUES"));	    //2
			vpd.put("var3", varOList.get(i).getString("FDES"));	    //3
			vpd.put("var4", varOList.get(i).getString("FEXTEND1"));	    //4
			vpd.put("var5", varOList.get(i).getString("FEXTEND2"));	    //5
			vpd.put("var6", varOList.get(i).getString("FEXTEND3"));	    //6
			vpd.put("var7", varOList.get(i).getString("FEXTEND4"));	    //7
			vpd.put("var8", varOList.get(i).getString("FEXTEND5"));	    //8
			vpd.put("var9", varOList.get(i).getString("PARM_U"));	    //9
			vpd.put("var10", varOList.get(i).getString("EQM_BASE_ID"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**
	 * 根据列KEY名和设备id查询列VALUE值
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/findByKeyNameAndEQM_BASE_ID")
	@ResponseBody
	public Object findByKeyNameAndEQM_BASE_ID() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_specificationService.findByKeyNameAndEQM_BASE_ID(pd);	//根据列KEY名和设备id查询列VALUE值
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
