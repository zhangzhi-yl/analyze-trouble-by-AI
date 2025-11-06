package org.yy.controller.mm;

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
import org.yy.service.mm.TakeStock_WinOrLoseService;

/** 
 * 说明：盘盈盘亏单
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-01
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/TakeStock_WinOrLose")
public class TakeStock_WinOrLoseController extends BaseController {
	
	@Autowired
	private TakeStock_WinOrLoseService TakeStock_WinOrLoseService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("TakeStock_WinOrLose:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TakeStock_WinOrLose_ID", this.get32UUID());	//主键
		TakeStock_WinOrLoseService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("TakeStock_WinOrLose:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		TakeStock_WinOrLoseService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("TakeStock_WinOrLose:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		TakeStock_WinOrLoseService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("TakeStock_WinOrLose:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = TakeStock_WinOrLoseService.list(page);	//列出TakeStock_WinOrLose列表
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
	//@RequiresPermissions("TakeStock_WinOrLose:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = TakeStock_WinOrLoseService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("TakeStock_WinOrLose:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			TakeStock_WinOrLoseService.deleteAll(ArrayDATA_IDS);
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
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("盘盈盘亏明细表ID");	//1
		titles.add("行号");	//2
		titles.add("盘点任务ID");	//3
		titles.add("盘点批次");	//4
		titles.add("物料ID");	//5
		titles.add("盘点结果");	//6
		titles.add("1  盘盈  0 盘亏");	//7
		titles.add("差异数量");	//8
		titles.add("差异单批号");	//9
		titles.add("是否处理 1 是 0 否");	//10
		titles.add("关联单号");	//11
		titles.add("是否行关闭  1 是 0 否");	//12
		titles.add("描述");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = TakeStock_WinOrLoseService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TakeStock_WinOrLose_ID"));	    //1
			vpd.put("var2", varOList.get(i).get("FEntryID").toString());	//2
			vpd.put("var3", varOList.get(i).getString("TakeStock_Task_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("TakeStock_Batch"));	    //4
			vpd.put("var5", varOList.get(i).getString("Material_ID"));	    //5
			vpd.put("var6", varOList.get(i).getString("TakeStock_Result"));	    //6
			vpd.put("var7", varOList.get(i).get("WinOrLose").toString());	//7
			vpd.put("var8", varOList.get(i).get("Difference_Count").toString());	//8
			vpd.put("var9", varOList.get(i).getString("DifferenceOrder_NUM"));	    //9
			vpd.put("var10", varOList.get(i).get("FOperator").toString());	//10
			vpd.put("var11", varOList.get(i).getString("Relevance_Order"));	    //11
			vpd.put("var12", varOList.get(i).get("IfFEntryClose").toString());	//12
			vpd.put("var13", varOList.get(i).getString("FExplanation"));	    //13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
