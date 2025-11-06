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
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.project.manager.Cabinet_BOM_changeService;

/** 
 * 说明：BOM变更记录
 * 作者：YuanYes QQ356703572
 * 时间：2021-08-26
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/cabinet_bom_change")
public class Cabinet_BOM_changeController extends BaseController {
	
	@Autowired
	private Cabinet_BOM_changeService cabinet_bom_changeService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("cabinet_bom_change:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("Cabinet_BOM_change_ID", this.get32UUID());	//主键
		cabinet_bom_changeService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("cabinet_bom_change:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		cabinet_bom_changeService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("cabinet_bom_change:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		cabinet_bom_changeService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("cabinet_bom_change:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = cabinet_bom_changeService.list(page);	//列出Cabinet_BOM_change列表
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
	@RequiresPermissions("cabinet_bom_change:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = cabinet_bom_changeService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("cabinet_bom_change:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			cabinet_bom_changeService.deleteAll(ArrayDATA_IDS);
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
		titles.add("柜体装配bom主键");	//1
		titles.add("物料id");	//2
		titles.add("物料名称");	//3
		titles.add("物料编码");	//4
		titles.add("物料分类");	//5
		titles.add("物料规格");	//6
		titles.add("物料主单位");	//7
		titles.add("物料品牌");	//8
		titles.add("bom数量");	//9
		titles.add("上限");	//10
		titles.add("下限");	//11
		titles.add("物料种类");	//12
		titles.add("详情ID");	//13
		titles.add("bom状态（正常的，变更的）");	//14
		titles.add("备注");	//15
		titles.add("是否下推");	//16
		titles.add("备注17");	//17
		titles.add("备注18");	//18
		titles.add("备注19");	//19
		titles.add("备注20");	//20
		titles.add("版本号");	//21
		titles.add("变更内容");	//22
		titles.add("变更记录ID");	//23
		dataMap.put("titles", titles);
		List<PageData> varOList = cabinet_bom_changeService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CABINET_BOM_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("MAT_BASIC_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("MAT_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("MAT_CODE"));	    //4
			vpd.put("var5", varOList.get(i).getString("MAT_CLASS"));	    //5
			vpd.put("var6", varOList.get(i).getString("MAT_SPECS"));	    //6
			vpd.put("var7", varOList.get(i).getString("MAT_MAIN_UNIT"));	    //7
			vpd.put("var8", varOList.get(i).getString("MAT_BRAND"));	    //8
			vpd.put("var9", varOList.get(i).getString("BOM_COUNT"));	    //9
			vpd.put("var10", varOList.get(i).getString("UP_LIMIT"));	    //10
			vpd.put("var11", varOList.get(i).getString("DOWM_LIMIT"));	    //11
			vpd.put("var12", varOList.get(i).getString("MAT_CATEGRAY"));	    //12
			vpd.put("var13", varOList.get(i).getString("Cabinet_Assembly_Detail_ID"));	    //13
			vpd.put("var14", varOList.get(i).getString("FSTATE"));	    //14
			vpd.put("var15", varOList.get(i).getString("REMARK"));	    //15
			vpd.put("var16", varOList.get(i).getString("If_Purchase"));	    //16
			vpd.put("var17", varOList.get(i).getString("FAudit_State"));	    //17
			vpd.put("var18", varOList.get(i).getString("Change_Duty"));	    //18
			vpd.put("var19", varOList.get(i).getString("FORDER"));	    //19
			vpd.put("var20", varOList.get(i).getString("CG_COUNT"));	    //20
			vpd.put("var21", varOList.get(i).getString("FVersion"));	    //21
			vpd.put("var22", varOList.get(i).getString("FChange"));	    //22
			vpd.put("var23", varOList.get(i).getString("FRelation_ID"));	    //23
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
