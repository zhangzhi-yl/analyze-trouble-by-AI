package org.yy.controller.km;

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
import org.yy.service.km.MaterialConsumeService;

/** 
 * 说明：物料消耗产出
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-10
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/MaterialConsume")
public class MaterialConsumeController extends BaseController {
	
	@Autowired
	private MaterialConsumeService MaterialConsumeService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("MaterialConsume:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MaterialConsume_ID", this.get32UUID());	//主键
		MaterialConsumeService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("MaterialConsume:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialConsumeService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("MaterialConsume:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MaterialConsumeService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("MaterialConsume:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = MaterialConsumeService.list(page);	//列出MaterialConsume列表
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
	@RequiresPermissions("MaterialConsume:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = MaterialConsumeService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("MaterialConsume:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			MaterialConsumeService.deleteAll(ArrayDATA_IDS);
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
		titles.add("消耗单据id");	//1
		titles.add("数据来源");	//2
		titles.add("物料id");	//3
		titles.add("物料条形码id");	//4
		titles.add("物料批次号");	//5
		titles.add("规格型号");	//6
		titles.add("物料辅助属性");	//7
		titles.add("物料辅助属性值");	//8
		titles.add("单位");	//9
		titles.add("消耗数量");	//10
		titles.add("关联工单id");	//11
		titles.add("操作人id");	//12
		titles.add("操作时间");	//13
		titles.add("类型(消耗/产出)");	//14
		titles.add("计划工单批号");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = MaterialConsumeService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ConsumptionDocumentID"));	    //1
			vpd.put("var2", varOList.get(i).getString("DataSources"));	    //2
			vpd.put("var3", varOList.get(i).getString("MaterialID"));	    //3
			vpd.put("var4", varOList.get(i).getString("MaterialBarCode"));	    //4
			vpd.put("var5", varOList.get(i).getString("MaterialBatchNum"));	    //5
			vpd.put("var6", varOList.get(i).getString("SpecificationsAndModels"));	    //6
			vpd.put("var7", varOList.get(i).getString("MaterialSProp"));	    //7
			vpd.put("var8", varOList.get(i).getString("MaterialSPropKey"));	    //8
			vpd.put("var9", varOList.get(i).getString("FUnit"));	    //9
			vpd.put("var10", varOList.get(i).get("ConsumptionQuantity").toString());	//10
			vpd.put("var11", varOList.get(i).getString("WorkOrderRel"));	    //11
			vpd.put("var12", varOList.get(i).getString("FOperatorID"));	    //12
			vpd.put("var13", varOList.get(i).getString("FOperateTime"));	    //13
			vpd.put("var14", varOList.get(i).getString("FType"));	    //14
			vpd.put("var15", varOList.get(i).getString("BatchNum"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
