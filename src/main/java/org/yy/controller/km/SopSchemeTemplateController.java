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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.SopSchemeTemplateService;

/** 
 * 说明：SOP方案模板
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-18
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/SopSchemeTemplate")
public class SopSchemeTemplateController extends BaseController {
	
	@Autowired
	private SopSchemeTemplateService SopSchemeTemplateService;
	@Autowired
	private StaffService staffService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("SopSchemeTemplate:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = SopSchemeTemplateService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//单号重复
		}else {	
		pd.put("SopSchemeTemplate_ID", this.get32UUID());	//主键
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));//查询职员ID
		pd.put("FCreateTime", Tools.date2Str(new Date()));
		SopSchemeTemplateService.save(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("SopSchemeTemplate:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		SopSchemeTemplateService.delete(pd);
		SopSchemeTemplateService.deleteMx(pd);//关联删除明细
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("SopSchemeTemplate:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//单号验重
		PageData pdNum = SopSchemeTemplateService.getRepeatNum(pd);
		if(pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) >0) {
			errInfo = "fail1";//订单号重复
		}else {		
		SopSchemeTemplateService.edit(pd);
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("SopSchemeTemplate:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = SopSchemeTemplateService.list(page);	//列出SopSchemeTemplate列表
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
	@RequiresPermissions("SopSchemeTemplate:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = SopSchemeTemplateService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("SopSchemeTemplate:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			SopSchemeTemplateService.deleteAll(ArrayDATA_IDS);
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
		titles.add("编码");	//1
		titles.add("名称");	//2
		titles.add("类型（数据字典）");	//3
		titles.add("状态（启用，禁用）");	//4
		titles.add("用途描述");	//5
		titles.add("创建人");	//6
		titles.add("创建时间");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = SopSchemeTemplateService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNumber"));	    //1
			vpd.put("var2", varOList.get(i).getString("FName"));	    //2
			vpd.put("var3", varOList.get(i).getString("FType"));	    //3
			vpd.put("var4", varOList.get(i).getString("FState"));	    //4
			vpd.put("var5", varOList.get(i).getString("FUse"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCreatePersonID"));	    //6
			vpd.put("var7", varOList.get(i).getString("FCreateTime"));	    //7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**禁用/启用
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editState")
	@ResponseBody
	public Object editState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		SopSchemeTemplateService.editState(pd);
		map.put("result", errInfo);
		return map;
	}
}
