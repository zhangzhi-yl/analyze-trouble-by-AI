package org.yy.controller.mdm;

import java.text.SimpleDateFormat;
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
import org.yy.service.mdm.EQM_AUTOINFOService;

/** 
 * 说明：设备自动化资料
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_autoinfo")
public class EQM_AUTOINFOController extends BaseController {
	
	@Autowired
	private EQM_AUTOINFOService eqm_autoinfoService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_autoinfo:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_AUTOINFO_ID", this.get32UUID());	//主键
		//pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
		eqm_autoinfoService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_autoinfo:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_autoinfoService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_autoinfo:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_autoinfoService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_autoinfo:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_autoinfoService.list(page);	//列出EQM_AUTOINFO列表
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
	//@RequiresPermissions("eqm_autoinfo:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_autoinfoService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_autoinfo:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_autoinfoService.deleteAll(ArrayDATA_IDS);
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
		titles.add("自动化资料ID");	//1
		titles.add("列KEY名");	//2
		titles.add("获取变量名称1");	//3
		titles.add("获取变量名称2");	//4
		titles.add("获取变量名称3");	//5
		titles.add("获取变量名称4");	//6
		titles.add("获取变量名称5");	//7
		titles.add("获取单位");	//8
		titles.add("获取值");	//9
		titles.add("上限");	//10
		titles.add("下限");	//11
		titles.add("上上限");	//12
		titles.add("下下限");	//13
		titles.add("扩展字段1");	//14
		titles.add("扩展字段2");	//15
		titles.add("扩展字段3");	//16
		titles.add("扩展字段4");	//17
		titles.add("扩展字段5");	//18
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_autoinfoService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_AUTOINFO_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("KEY_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("VARIABLE_NAME1"));	    //3
			vpd.put("var4", varOList.get(i).getString("VARIABLE_NAME2"));	    //4
			vpd.put("var5", varOList.get(i).getString("VARIABLE_NAME3"));	    //5
			vpd.put("var6", varOList.get(i).getString("VARIABLE_NAME4"));	    //6
			vpd.put("var7", varOList.get(i).getString("VARIABLE_NAME5"));	    //7
			vpd.put("var8", varOList.get(i).getString("GET_UNIT"));	    //8
			vpd.put("var9", varOList.get(i).getString("GET_VALUES"));	    //9
			vpd.put("var10", varOList.get(i).get("QTY_H").toString());	//10
			vpd.put("var11", varOList.get(i).get("QTY_L").toString());	//11
			vpd.put("var12", varOList.get(i).get("QTY_QTY_H").toString());	//12
			vpd.put("var13", varOList.get(i).get("QTY_QTY_L").toString());	//13
			vpd.put("var14", varOList.get(i).getString("FEXTEND1"));	    //14
			vpd.put("var15", varOList.get(i).getString("FEXTEND2"));	    //15
			vpd.put("var16", varOList.get(i).getString("FEXTEND3"));	    //16
			vpd.put("var17", varOList.get(i).getString("FEXTEND4"));	    //17
			vpd.put("var18", varOList.get(i).getString("FEXTEND5"));	    //18
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
