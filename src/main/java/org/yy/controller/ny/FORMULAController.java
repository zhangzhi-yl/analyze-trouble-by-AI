//package org.yy.controller.ny;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//
//import org.yy.controller.base.BaseController;
//import org.yy.entity.Page;
//import org.yy.util.DateUtil;
//import org.yy.util.ObjectExcelView;
//import org.yy.util.Tools;
//import org.yy.entity.PageData;
//import org.yy.service.ny.FORMULAService;
//
///**
// * 说明：公式
// * 作者：YuanYe
// * 时间：2021-11-20
// *
// */
//@Controller
//@RequestMapping("/formula")
//public class FORMULAController extends BaseController {
//
//	@Autowired
//	private FORMULAService formulaService;
//
//	/**保存
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/add")
////	@RequiresPermissions("formula:add")
//	@ResponseBody
//	public Object add() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		pd.put("FORMULA_ID", this.get32UUID());	//主键
//		formulaService.save(pd);
//		map.put("result", errInfo);
//		return map;
//	}
//
//	/**删除
//	 * @param out
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/delete")
////	@RequiresPermissions("formula:del")
//	@ResponseBody
//	public Object delete() throws Exception{
//		Map<String,String> map = new HashMap<String,String>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		formulaService.delete(pd);
//		map.put("result", errInfo);				//返回结果
//		return map;
//	}
//
//	/**修改
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/edit")
//	@RequiresPermissions("formula:edit")
//	@ResponseBody
//	public Object edit() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		formulaService.edit(pd);
//		map.put("result", errInfo);
//		return map;
//	}
//
//	/**列表
//	 * @param page
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/list")
//	@RequiresPermissions("formula:list")
//	@ResponseBody
//	public Object list(Page page) throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
//		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
//		page.setPd(pd);
//		List<PageData>	varList = formulaService.list(page);	//列出FORMULA列表
//		map.put("varList", varList);
//		map.put("page", page);
//		map.put("result", errInfo);
//		return map;
//	}
//
//	 /**去修改页面获取数据
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/goEdit")
//	@RequiresPermissions("formula:edit")
//	@ResponseBody
//	public Object goEdit() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		pd = formulaService.findById(pd);	//根据ID读取
//		map.put("pd", pd);
//		map.put("result", errInfo);
//		return map;
//	}
//
//	 /**批量删除
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/deleteAll")
//	@RequiresPermissions("formula:del")
//	@ResponseBody
//	public Object deleteAll() throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		String errInfo = "success";
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		String DATA_IDS = pd.getString("DATA_IDS");
//		if(Tools.notEmpty(DATA_IDS)){
//			String ArrayDATA_IDS[] = DATA_IDS.split(",");
//			formulaService.deleteAll(ArrayDATA_IDS);
//			errInfo = "success";
//		}else{
//			errInfo = "error";
//		}
//		map.put("result", errInfo);				//返回结果
//		return map;
//	}
//
//	 /**导出到excel
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/excel")
//	@RequiresPermissions("toExcel")
//	public ModelAndView exportExcel() throws Exception{
//		ModelAndView mv = new ModelAndView();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		Map<String,Object> dataMap = new HashMap<String,Object>();
//		List<String> titles = new ArrayList<String>();
//		titles.add("符号名");	//1
//		titles.add("计算名");	//2
//		dataMap.put("titles", titles);
//		List<PageData> varOList = formulaService.listAll(pd);
//		List<PageData> varList = new ArrayList<PageData>();
//		for(int i=0;i<varOList.size();i++){
//			PageData vpd = new PageData();
//			vpd.put("var1", varOList.get(i).getString("SYMBOLNAME"));	    //1
//			vpd.put("var2", varOList.get(i).getString("CALCULATIONNAME"));	    //2
//			varList.add(vpd);
//		}
//		dataMap.put("varList", varList);
//		ObjectExcelView erv = new ObjectExcelView();
//		mv = new ModelAndView(erv,dataMap);
//		return mv;
//	}
//
//}
