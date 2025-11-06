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
import org.yy.service.mdm.EQM_DEBUGService;

/** 
 * 说明：设备调试验收单
 * 作者：YuanYe
 * 时间：2020-06-10
 * 
 */
@Controller
@RequestMapping("/eqm_debug")
public class EQM_DEBUGController extends BaseController {
	
	@Autowired
	private EQM_DEBUGService eqm_debugService;
	
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
		pd.put("EQM_DEBUG_ID", this.get32UUID());	//主键设备调试验收单ID
		/*pd.put("FEXTEND1", "");	//扩展预留1
		pd.put("FEXTEND2", "");	//扩展预留2
		pd.put("FEXTEND3", "");	//扩展预留3
		pd.put("FEXTEND4", "");	//扩展预留4
		pd.put("FEXTEND5", "");	//扩展预留5*/		
		eqm_debugService.save(pd);
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
		eqm_debugService.delete(pd);
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
		eqm_debugService.edit(pd);
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
		List<PageData>	varList = eqm_debugService.list(page);	//列出EQM_DEBUG列表
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
		pd = eqm_debugService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	/**根据设备ID获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getByParentId")
	@ResponseBody
	public Object findByParentId()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_debugService.findByParentId(pd);	//根据设备ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
			eqm_debugService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备调试验收单ID");	//1
		titles.add("设备ID");	//2
		titles.add("设备名称");	//3
		titles.add("设备规格型号");	//4
		titles.add("生产厂家");	//5
		titles.add("出厂日期");	//6
		titles.add("安装地点");	//7
		titles.add("参检部门");	//8
		titles.add("调试记录");	//9
		titles.add("调试人");	//10
		titles.add("调试日期");	//11
		titles.add("运行记录");	//12
		titles.add("操作者");	//13
		titles.add("运行时间");	//14
		titles.add("结论");	//15
		titles.add("结论人");	//16
		titles.add("结论时间");	//17
		titles.add("备注");	//18
		titles.add("扩展预留1");	//19
		titles.add("扩展预留2");	//20
		titles.add("扩展预留3");	//21
		titles.add("扩展预留4");	//22
		titles.add("扩展预留5");	//23
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_debugService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_DEBUG_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQM_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FSPECS"));	    //4
			vpd.put("var5", varOList.get(i).getString("FMANUFACTOR"));	    //5
			vpd.put("var6", varOList.get(i).getString("MANUFACTURE_DATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("INSTALL_POSITION"));	    //7
			vpd.put("var8", varOList.get(i).getString("CHECK_DEP"));	    //8
			vpd.put("var9", varOList.get(i).getString("DEBUG_RECORD"));	    //9
			vpd.put("var10", varOList.get(i).getString("DEBUG_PERSON"));	    //10
			vpd.put("var11", varOList.get(i).getString("DEBUG_DATE"));	    //11
			vpd.put("var12", varOList.get(i).getString("RUN_RECORD"));	    //12
			vpd.put("var13", varOList.get(i).getString("RUN_PERSON"));	    //13
			vpd.put("var14", varOList.get(i).getString("RUN_DATE"));	    //14
			vpd.put("var15", varOList.get(i).getString("FCONCLUSION"));	    //15
			vpd.put("var16", varOList.get(i).getString("CONCLUSION_PERSON"));	    //16
			vpd.put("var17", varOList.get(i).getString("CONCLUSION_DATE"));	    //17
			vpd.put("var18", varOList.get(i).getString("FREMARKS"));	    //18
			vpd.put("var19", varOList.get(i).getString("FEXTEND1"));	    //19
			vpd.put("var20", varOList.get(i).getString("FEXTEND2"));	    //20
			vpd.put("var21", varOList.get(i).getString("FEXTEND3"));	    //21
			vpd.put("var22", varOList.get(i).getString("FEXTEND4"));	    //22
			vpd.put("var23", varOList.get(i).getString("FEXTEND5"));	    //23
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
