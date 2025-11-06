package org.yy.controller.qm;

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
import org.yy.service.qm.O_RECORD_QUALITYTASKService;

/** 
 * 说明：柜体质检执行操作记录
 * 作者：YuanYes QQ356703572
 * 时间：2021-07-21
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/o_record_qualitytask")
public class O_RECORD_QUALITYTASKController extends BaseController {
	
	@Autowired
	private O_RECORD_QUALITYTASKService o_record_qualitytaskService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("o_record_qualitytask:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("O_RECORD_QUALITYTASK_ID", this.get32UUID());	//主键
		String name = Jurisdiction.getName();
		pd.put("BEGIN_TIME",Tools.date2Str(new Date()));
		pd.put("OPERSON",name);
		o_record_qualitytaskService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/findByExampleID")
	@ResponseBody
	public Object findByExampleID() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = o_record_qualitytaskService.findByExampleID(pd);	//根据ID读取
		if(null == pd){
			map.put("msg", "暂无记录");
			map.put("result", "error");
			return map;
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("o_record_qualitytask:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		o_record_qualitytaskService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("o_record_qualitytask:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd =o_record_qualitytaskService.findById(pd);
			if(null == pd){
				throw new RuntimeException("计时未开始！");
			}
			pd.put("END_TIME",Tools.date2Str(new Date()));
			o_record_qualitytaskService.edit(pd);
			map.put("result", errInfo);
			return map;
		} catch (Exception e) {
			map.put("result", "exception");
			map.put("msg", e.getMessage());
			return map;
		}
		
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("o_record_qualitytask:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = o_record_qualitytaskService.list(page);	//列出O_RECORD_QUALITYTASK列表
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
	@RequiresPermissions("o_record_qualitytask:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = o_record_qualitytaskService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("o_record_qualitytask:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			o_record_qualitytaskService.deleteAll(ArrayDATA_IDS);
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
		titles.add("备注2");	//1
		titles.add("备注3");	//2
		titles.add("备注4");	//3
		titles.add("备注5");	//4
		titles.add("备注6");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = o_record_qualitytaskService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ASSOCIATED_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("BEGIN_TIME"));	    //2
			vpd.put("var3", varOList.get(i).getString("END_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("OPERSON"));	    //4
			vpd.put("var5", varOList.get(i).getString("FDESCRIBE"));	    //5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
