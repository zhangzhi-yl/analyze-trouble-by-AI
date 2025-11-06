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
import org.yy.service.mdm.EQM_BASEService;
import org.yy.service.mdm.EQM_UNBOXCHECKService;

/** 
 * 说明：设备开箱检查验收单
 * 作者：YuanYe
 * 时间：2020-06-10
 * 
 */
@Controller
@RequestMapping("/eqm_unboxcheck")
public class EQM_UNBOXCHECKController extends BaseController {
	
	@Autowired
	private EQM_BASEService eqm_baseService;
	
	@Autowired
	private EQM_UNBOXCHECKService eqm_unboxcheckService;
	
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
		pd.put("EQM_UNBOXCHECK_ID", this.get32UUID());	//主键设备开箱验收ID
		/*pd.put("FEXTEND1", "");	//预留拓展1
		pd.put("FEXTEND2", "");	//预留拓展2
		pd.put("FEXTEND3", "");	//预留拓展3
		pd.put("FEXTEND4", "");	//预留拓展4
		pd.put("FEXTEND5", "");	//预留拓展5*/		
		eqm_unboxcheckService.save(pd);
		pd = eqm_unboxcheckService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_unboxcheckService.delete(pd);
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
		eqm_unboxcheckService.edit(pd);
		map.put("result", errInfo);				//返回结果
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
		List<PageData> varList = eqm_unboxcheckService.list(page);	//列出EQM_UNBOXCHECK列表
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
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_unboxcheckService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
		pd = eqm_unboxcheckService.findByParentId(pd);	//根据设备ID读取
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
		titles.add("设备开箱验收ID");	//1
		titles.add("设备基础信息ID");	//2
		titles.add("设备名称");	//3
		titles.add("规格型号");	//4
		titles.add("制造厂商");	//5
		titles.add("出厂年月");	//6
		titles.add("检查负责人");	//7
		titles.add("参检部门");	//8
		titles.add("外包装检查");	//9
		titles.add("设备外表检查");	//10
		titles.add("设备随机附件插件");	//11
		titles.add("设备电器检查");	//12
		titles.add("设备随机资料检查");	//13
		titles.add("工程设备部意见");	//14
		titles.add("工程设备部意见负责人");	//15
		titles.add("备注");	//16
		/*titles.add("预留拓展1");	//16
		titles.add("预留拓展2");	//17
		titles.add("预留拓展3");	//18
		titles.add("预留拓展4");	//19
		titles.add("预留拓展5");	//20*/		
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_unboxcheckService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_UNBOXCHECK_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQM_BASE_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FSPECS"));	    //4
			vpd.put("var5", varOList.get(i).getString("FMANUFACTOR"));	    //5
			vpd.put("var6", varOList.get(i).getString("MANUFACTURE_DATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("INSPECTOR"));	    //7
			vpd.put("var8", varOList.get(i).getString("CHECK_DEP"));	    //8
			vpd.put("var9", varOList.get(i).getString("BOX_CHECK"));	    //9
			vpd.put("var10", varOList.get(i).getString("APPEARANCE_CHECK"));	    //10
			vpd.put("var11", varOList.get(i).getString("ANNEX_CHECK"));	    //11
			vpd.put("var12", varOList.get(i).getString("ELECTRIC_CHECK"));	    //12
			vpd.put("var13", varOList.get(i).getString("MATERIAL_CHECK"));	    //13
			vpd.put("var14", varOList.get(i).getString("FOPINION"));	    //14
			vpd.put("var15", varOList.get(i).getString("OPINION_PERSON"));	    //15
			vpd.put("var16", varOList.get(i).getString("FREMARKS"));	    //16
			/*vpd.put("var16", varOList.get(i).getString("FEXTEND1"));	    //16
			vpd.put("var17", varOList.get(i).getString("FEXTEND2"));	    //17
			vpd.put("var18", varOList.get(i).getString("FEXTEND3"));	    //18
			vpd.put("var19", varOList.get(i).getString("FEXTEND4"));	    //19
			vpd.put("var20", varOList.get(i).getString("FEXTEND5"));	    //20*/		
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
