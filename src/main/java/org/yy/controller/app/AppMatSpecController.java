package org.yy.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.SpringUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;

import org.yy.entity.PageData;
import org.yy.service.mbase.MAT_SPECService;
import org.yy.service.system.UsersService;

/** 
 * 说明：物料规格附表
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-25
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/MatSpec")
public class AppMatSpecController extends BaseController {
	
	@Autowired
	private MAT_SPECService MAT_SPECService;
	
	/**下拉列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	@ResponseBody
	public Object listAll(HttpServletResponse response) throws Exception{
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			List<PageData> varList = Lists.newArrayList();
			String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
			if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
			varList = MAT_SPECService.listAll(pd);	//列出MAT_SPEC列表
			Map<String, Object> map = new HashMap<>();
			map.put("pd", pd);
			map.put("varList", varList);
			return AppResult.success(map, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("MAT_SPEC:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MAT_SPEC_ID", this.get32UUID());	//主键
		MAT_SPECService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("MAT_SPEC:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MAT_SPECService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("MAT_SPEC:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		MAT_SPECService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("MAT_SPEC:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = MAT_SPECService.list(page);	//列出MAT_SPEC列表
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
	//@RequiresPermissions("MAT_SPEC:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = MAT_SPECService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("MAT_SPEC:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			MAT_SPECService.deleteAll(ArrayDATA_IDS);
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
		titles.add("物料id");	//1
		titles.add("规格单位id");	//2
		titles.add("规格数量");	//3
		titles.add("创建人");	//4
		titles.add("创建时间");	//5
		titles.add("最后修改人");	//6
		titles.add("最后修改时间");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = MAT_SPECService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MAT_BASIC_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("UNIT_INFO_ID"));	    //2
			vpd.put("var3", varOList.get(i).get("MAT_SPEC_QTY").toString());	//3
			vpd.put("var4", varOList.get(i).getString("FCreatePersonID"));	    //4
			vpd.put("var5", varOList.get(i).getString("FCreateTime"));	    //5
			vpd.put("var6", varOList.get(i).getString("LastModifiedBy"));	    //6
			vpd.put("var7", varOList.get(i).getString("LastModificationTime"));	    //7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
