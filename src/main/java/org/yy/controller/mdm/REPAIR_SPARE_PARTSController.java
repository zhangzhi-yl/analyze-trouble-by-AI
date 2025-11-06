package org.yy.controller.mdm;

import java.math.BigDecimal;
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
import org.yy.util.UuidUtil;
import org.yy.entity.PageData;
import org.yy.service.mdm.EQM_SPARE_PARTSService;
import org.yy.service.mdm.REPAIR_SPARE_PARTSService;

/** 
 * 说明：报修工单(备品备件)
 * 作者：YuanYes QQ356703572
 * 时间：2020-05-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/repair_spare_parts")
public class REPAIR_SPARE_PARTSController extends BaseController {
	
	@Autowired
	private REPAIR_SPARE_PARTSService repair_spare_partsService;
	
	@Autowired
	private EQM_SPARE_PARTSService eqm_spare_partsService;
	
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
		pd.put("REPAIR_SPARE_PARTS_ID", this.get32UUID());	//备品备件ID
		pd.put("FCREATOR", Jurisdiction.getName());	//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
		repair_spare_partsService.save(pd);
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
		PageData oldpd = new PageData();
		PageData newpd = new PageData();
		pd = this.getPageData();
		try {
			//查询报修备件数量
			oldpd = eqm_spare_partsService.findById(pd);
			newpd = repair_spare_partsService.findById(pd);
			//更新设备备件数量
			BigDecimal oSPARE_AMOUNT = (BigDecimal)oldpd.get("SPARE_AMOUNT");
			BigDecimal nSPARE_AMOUNT = (BigDecimal)newpd.get("SPARE_AMOUNT");
			pd.put("SPARE_AMOUNT", oSPARE_AMOUNT.add(nSPARE_AMOUNT));
			eqm_spare_partsService.editAmount(pd);
			//删除报修备件
			repair_spare_partsService.delete(pd);
		}catch(Exception e) {
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
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		repair_spare_partsService.edit(pd);
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
		List<PageData>	varList = repair_spare_partsService.list(page);	//列出列表
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
		pd = repair_spare_partsService.findById(pd);	//根据ID读取
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
			repair_spare_partsService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备品备件ID");	//1
		titles.add("创建人");	//2
		titles.add("创建时间");	//3
		titles.add("设备基础资料ID");	//4
		titles.add("设备供应商");	//5
		titles.add("采购日期");	//6
		titles.add("使用日期");	//7
		titles.add("备品备件名称");	//8
		titles.add("备品备件型号");	//9
		titles.add("数量");	//10
		titles.add("设备使用部门");	//11
		titles.add("设备使用工作站");	//12
		titles.add("设备安全库存");	//13
		titles.add("是否启动安全库存");	//14
		titles.add("存放位置");	//15
		titles.add("返修数量");	//16
		titles.add("描述");	//17
		dataMap.put("titles", titles);
		List<PageData> varOList = repair_spare_partsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_SPARE_PARTS_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCREATOR"));	    //2
			vpd.put("var3", varOList.get(i).getString("CREATE_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("EQM_BASE_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("EQM_SUPPLIER"));	    //5
			vpd.put("var6", varOList.get(i).getString("SUPPLIER_DATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("USE_DATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("SPARE_NAME"));	    //8
			vpd.put("var9", varOList.get(i).getString("SPARE_TYPE"));	    //9
			vpd.put("var10", varOList.get(i).get("SPARE_AMOUNT").toString());	//10
			vpd.put("var11", varOList.get(i).getString("EQM_DEP"));	    //11
			vpd.put("var12", varOList.get(i).getString("EQM_STATION"));	    //12
			vpd.put("var13", varOList.get(i).get("EQM_SAFETY_STOCK").toString());	//13
			vpd.put("var14", varOList.get(i).getString("FIRING_WHETHER"));	    //14
			vpd.put("var15", varOList.get(i).getString("DEPOSIT_LOC"));	    //15
			vpd.put("var16", varOList.get(i).get("REWORK_QTY").toString());	//16
			vpd.put("var17", varOList.get(i).getString("FDES"));	    //17
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
