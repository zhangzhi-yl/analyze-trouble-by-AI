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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.project.manager.PRESALEPLANTWOService;

/** 
 * 说明：售前方案计划二级明细
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-20
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/presaleplantwo")
public class PRESALEPLANTWOController extends BaseController {
	
	@Autowired
	private PRESALEPLANTWOService presaleplantwoService;
	
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
		pd.put("PRESALEPLANTWO_ID", this.get32UUID());	//主键
		pd.put("FCREATETIME", Tools.date2Str(new Date()));
		pd.put("FFOUNDER", Jurisdiction.getName());
		pd.put("FFOUNDERACCOUNT", Jurisdiction.getName());
		presaleplantwoService.save(pd);
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
		presaleplantwoService.delete(pd);
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
		pd.put("FMODIFIERSTIME", Tools.date2Str(new Date()));
		pd.put("FMODIFIERS", Jurisdiction.getName());
		pd.put("FMODIFIERSACCOUNT", Jurisdiction.getName());
		presaleplantwoService.edit(pd);
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
		List<PageData>	varList = presaleplantwoService.list(page);	//列出PRESALEPLANTWO列表
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
	@RequiresPermissions("presaleplantwo:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = presaleplantwoService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
//	@RequestMapping(value="/deleteAll")
//	@RequiresPermissions("presaleplantwo:del")
//	@ResponseBody
//	public Object deleteAll() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();		
//		pd = this.getPageData();
//		String DATA_IDS = pd.getString("DATA_IDS");
//		if(Tools.notEmpty(DATA_IDS)){
//			String ArrayDATA_IDS[] = DATA_IDS.split(",");
//			presaleplantwoService.deleteAll(ArrayDATA_IDS);
//			errInfo = "success";
//		}else{
//			errInfo = "error";
//		}
//		map.put("result", errInfo);				//返回结果
//		return map;
//	}
	
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
		titles.add("序号");	//1
		titles.add("名称");	//2
		titles.add("订货号");	//3
		titles.add("技术描述");	//4
		titles.add("单位");	//5
		titles.add("数量");	//6
		titles.add("成本单价");	//7
		titles.add("金额总计");	//8
		titles.add("可替代型号");	//9
		titles.add("可用库存");	//10
		titles.add("仓库号");	//11
		titles.add("厂商");	//12
		titles.add("柜体名称");	//13
		titles.add("创建时间");	//14
		titles.add("创建人");	//15
		titles.add("创建人账号");	//16
		titles.add("修改人");	//17
		titles.add("修改人账号");	//18
		titles.add("修改时间");	//19
		titles.add("预留字段1");	//20
		titles.add("预留字段2");	//21
		titles.add("预留字段3");	//22
		titles.add("预留字段4");	//23
		titles.add("预留字段5");	//24
		titles.add("主表ID");	//25
		titles.add("一级明细ID");	//26
		dataMap.put("titles", titles);
		List<PageData> varOList = presaleplantwoService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNUMBER"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FORDERNUMBER"));	    //3
			vpd.put("var4", varOList.get(i).getString("FTECHNICALDESCRIPTION"));	    //4
			vpd.put("var5", varOList.get(i).getString("FUNIT"));	    //5
			vpd.put("var6", varOList.get(i).getString("FQUANTITY"));	    //6
			vpd.put("var7", varOList.get(i).getString("FUNITPRICE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FTOTALAMOUNT"));	    //8
			vpd.put("var9", varOList.get(i).getString("FALTERNATIVEMODEL"));	    //9
			vpd.put("var10", varOList.get(i).getString("FAVAILABLESTOCK"));	    //10
			vpd.put("var11", varOList.get(i).getString("FWAREHOUSENUMBER"));	    //11
			vpd.put("var12", varOList.get(i).getString("FMANUFACTURER"));	    //12
			vpd.put("var13", varOList.get(i).getString("FCABINETNAME"));	    //13
			vpd.put("var14", varOList.get(i).getString("FCREATETIME"));	    //14
			vpd.put("var15", varOList.get(i).getString("FFOUNDER"));	    //15
			vpd.put("var16", varOList.get(i).getString("FFOUNDERACCOUNT"));	    //16
			vpd.put("var17", varOList.get(i).getString("FMODIFIERS"));	    //17
			vpd.put("var18", varOList.get(i).getString("FMODIFIERSACCOUNT"));	    //18
			vpd.put("var19", varOList.get(i).getString("FMODIFIERSTIME"));	    //19
			vpd.put("var20", varOList.get(i).getString("FRESERVE1"));	    //20
			vpd.put("var21", varOList.get(i).getString("FRESERVE2"));	    //21
			vpd.put("var22", varOList.get(i).getString("FRESERVE3"));	    //22
			vpd.put("var23", varOList.get(i).getString("FRESERVE4"));	    //23
			vpd.put("var24", varOList.get(i).getString("FRESERVE5"));	    //24
			vpd.put("var25", varOList.get(i).getString("PRESALEPLAN_ID"));	    //25
			vpd.put("var26", varOList.get(i).getString("PRESALEPLANONE_ID"));	    //26
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
