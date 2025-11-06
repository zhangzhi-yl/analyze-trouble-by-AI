package org.yy.controller.zm;

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
import org.yy.service.zm.SceneTimeService;

/** 
 * 说明：场景日期
 * 作者：YuanYe
 * 时间：2021-10-13
 * 
 */
@Controller
@RequestMapping("/scenetime")
public class SceneTimeController extends BaseController {
	
	@Autowired
	private SceneTimeService scenetimeService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
//	@RequiresPermissions("scenetime:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String sceneid = pd.getString("Scene_ID");
		if(null != sceneid && !"".equals(sceneid)) {
			pd.put("SCENETIME_ID", this.get32UUID());    //主键
			pd.put("Creator", Jurisdiction.getName());
			pd.put("CreateTime", Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
			scenetimeService.save(pd);
		}else {
			errInfo = "主表ID丢失";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
//	@RequiresPermissions("scenetime:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		scenetimeService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
//	@RequiresPermissions("scenetime:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("Modified", Jurisdiction.getName());
		pd.put("ModifiedTime",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		scenetimeService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
//	@RequiresPermissions("scenetime:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = scenetimeService.list(page);	//列出SceneTime列表
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
//	@RequiresPermissions("scenetime:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = scenetimeService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
//	@RequiresPermissions("scenetime:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			scenetimeService.deleteAll(ArrayDATA_IDS);
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
//	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("开始时间");	//1
		titles.add("结束时间");	//2
		titles.add("创建人");	//3
		titles.add("创建时间");	//4
		titles.add("修改人");	//5
		titles.add("最后修改时间");	//6
		titles.add("扩展一");	//7
		titles.add("扩展二");	//8
		titles.add("扩展三");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = scenetimeService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("StartTime"));	    //1
			vpd.put("var2", varOList.get(i).getString("EndTime"));	    //2
			vpd.put("var3", varOList.get(i).getString("Creator"));	    //3
			vpd.put("var4", varOList.get(i).getString("CreateTime"));	    //4
			vpd.put("var5", varOList.get(i).getString("Modified"));	    //5
			vpd.put("var6", varOList.get(i).getString("ModifiedTime"));	    //6
			vpd.put("var7", varOList.get(i).getString("Extend1"));	    //7
			vpd.put("var8", varOList.get(i).getString("Extend2"));	    //8
			vpd.put("var9", varOList.get(i).getString("Extend3"));	    //9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
