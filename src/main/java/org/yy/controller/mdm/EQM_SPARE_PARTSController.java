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
import org.yy.service.mdm.EQM_SPARE_PARTSService;

/** 
 * 说明：设备备品备件
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_spare_parts")
public class EQM_SPARE_PARTSController extends BaseController {
	
	@Autowired
	private EQM_SPARE_PARTSService eqm_spare_partsService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_spare_parts:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_SPARE_PARTS_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		eqm_spare_partsService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_spare_parts:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_spare_partsService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_spare_parts:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_spare_partsService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	/**修改数量
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editAmount")
	//@RequiresPermissions("eqm_spare_parts:edit")
	@ResponseBody
	public Object editAmount() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_spare_partsService.editAmount(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_spare_parts:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_spare_partsService.list(page);	//列出EQM_SPARE_PARTS列表
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
	//@RequiresPermissions("eqm_spare_parts:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_spare_partsService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_spare_parts:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_spare_partsService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备供应商");	//1
		titles.add("采购日期");	//2
		titles.add("使用日期");	//3
		titles.add("备品备件名称");	//4
		titles.add("备品备件型号");	//5
		titles.add("数量");	//6
		titles.add("设备使用部门");	//7
		titles.add("设备使用工作站");	//8
		titles.add("设备安全库存");	//9
		titles.add("是否启动安全库存");	//10
		titles.add("存放位置");	//11
		titles.add("返修数量");	//12
		titles.add("描述");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_spare_partsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_SUPPLIER"));	    //1
			vpd.put("var2", varOList.get(i).getString("SUPPLIER_DATE"));	    //2
			vpd.put("var3", varOList.get(i).getString("USE_DATE"));	    //3
			vpd.put("var4", varOList.get(i).getString("SPARE_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("SPARE_TYPE"));	    //5
			vpd.put("var6", varOList.get(i).get("SPARE_AMOUNT").toString());	//6
			vpd.put("var7", varOList.get(i).getString("EQM_DEP"));	    //7
			vpd.put("var8", varOList.get(i).getString("EQM_STATION"));	    //8
			vpd.put("var9", varOList.get(i).get("EQM_SAFETY_STOCK").toString());	//9
			vpd.put("var10", varOList.get(i).getString("FIRING_WHETHER"));	    //10
			vpd.put("var11", varOList.get(i).getString("DEPOSIT_LOC"));	    //11
			vpd.put("var12", varOList.get(i).get("REWORK_QTY").toString());	//12
			vpd.put("var13", varOList.get(i).getString("FDES"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
