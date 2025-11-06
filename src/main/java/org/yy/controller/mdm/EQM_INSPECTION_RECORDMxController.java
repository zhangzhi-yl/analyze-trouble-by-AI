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
import org.yy.service.mdm.EQM_INSPECTION_RECORDMxService;

/** 
 * 说明：设备巡检记录(明细)
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_inspection_recordmx")
public class EQM_INSPECTION_RECORDMxController extends BaseController {
	
	@Autowired
	private EQM_INSPECTION_RECORDMxService eqm_inspection_recordmxService;
	
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
		pd.put("EQM_INSPECTION_RECORDMX_ID", this.get32UUID());	//主键
		eqm_inspection_recordmxService.save(pd);
		map.put("result", errInfo);				//返回结果
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
		eqm_inspection_recordmxService.delete(pd);
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
		eqm_inspection_recordmxService.edit(pd);
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
		List<PageData>	varList = eqm_inspection_recordmxService.list(page);	//列出EQM_INSPECTION_RECORDMx列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);									//返回结果
		return map;
	}
	
	 /**去修改页面
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
		pd = eqm_inspection_recordmxService.findById(pd);	//根据ID读取
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
			eqm_inspection_recordmxService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备标识");	//1
		titles.add("设备名称");	//2
		titles.add("巡检类型");	//3
		titles.add("巡检监管人");	//4
		titles.add("单条巡检人");	//5
		titles.add("巡检时间");	//6
		titles.add("巡检内容");	//7
		titles.add("巡检中设备处于项目");	//8
		titles.add("存在问题");	//9
		titles.add("问题是否提请处理");	//10
		titles.add("解决处理");	//11
		titles.add("描述");	//12
		titles.add("扩展字段1");	//13
		titles.add("扩展字段2");	//14
		titles.add("扩展字段3");	//15
		titles.add("扩展字段4");	//16
		titles.add("扩展字段5");	//17
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_inspection_recordmxService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FIDENTIFY"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FUSER"));	    //4
			vpd.put("var5", varOList.get(i).getString("ONESUPERVISE_NAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("FTIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("FCONTENT"));	    //7
			vpd.put("var8", varOList.get(i).getString("CONTENT_STAGE"));	    //8
			vpd.put("var9", varOList.get(i).getString("EXISTINGPROBLEM"));	    //9
			vpd.put("var10", varOList.get(i).getString("PROBLEMDISPOSE_WHETHER"));	    //10
			vpd.put("var11", varOList.get(i).getString("SOLVE_DISPOSE"));	    //11
			vpd.put("var12", varOList.get(i).getString("FDES"));	    //12
			vpd.put("var13", varOList.get(i).getString("FEXTEND1"));	    //13
			vpd.put("var14", varOList.get(i).getString("FEXTEND2"));	    //14
			vpd.put("var15", varOList.get(i).getString("FEXTEND3"));	    //15
			vpd.put("var16", varOList.get(i).getString("FEXTEND4"));	    //16
			vpd.put("var17", varOList.get(i).getString("FEXTEND5"));	    //17
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
