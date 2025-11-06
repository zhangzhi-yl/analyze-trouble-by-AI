package org.yy.controller.mbase;

import java.text.SimpleDateFormat;
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
import org.yy.service.mbase.MAT_QUALITYService;

/** 
 * 说明：质量资料
 * 作者：YuanYe
 * 时间：2020-01-07
 * 
 */
@Controller
@RequestMapping("/mat_quality")
public class MAT_QUALITYController extends BaseController {
	
	@Autowired
	private MAT_QUALITYService mat_qualityService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("mat_quality:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MAT_QUALITY_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
		mat_qualityService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("mat_quality:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		mat_qualityService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("mat_quality:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		mat_qualityService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("mat_quality:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = mat_qualityService.list(page);	//列出MAT_QUALITY列表
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
	//@RequiresPermissions("mat_quality:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = mat_qualityService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**去修改页面通过基础资料Id获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goSonEdit")
		//@RequiresPermissions("mat_quality:edit")
		@ResponseBody
		public Object goSonEdit() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = mat_qualityService.findBySonId(pd);	//根据基础资料ID读取
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("mat_quality:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			mat_qualityService.deleteAll(ArrayDATA_IDS);
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
		titles.add("基础资料ID");	//1
		titles.add("采购检验方式");	//2
		titles.add("产品检验方式");	//3
		titles.add("退库检验方式");	//4
		titles.add("库存检验方式");	//5
		titles.add("其他检验方式");	//6
		titles.add("抽样标准致命");	//7
		titles.add("抽样标准严重");	//8
		titles.add("抽样标准轻微");	//9
		titles.add("库存检验周期天");	//10
		titles.add("库存检验周期预警天");	//11
		titles.add("检验方案");	//12
		titles.add("检验员");	//13
		titles.add("扩展字段1");	//14
		titles.add("扩展字段2");	//15
		titles.add("扩展字段3");	//16
		titles.add("扩展字段4");	//17
		titles.add("扩展字段5");	//18
		titles.add("创建人");	//19
		titles.add("创建时间");	//20
		dataMap.put("titles", titles);
		List<PageData> varOList = mat_qualityService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MAT_BASIC_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("PURCHASE_TEST_MODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("PRODUCT_TEST_MODE"));	    //3
			vpd.put("var4", varOList.get(i).getString("CANCELLINGSTOCKS_TEST_MODE"));	    //4
			vpd.put("var5", varOList.get(i).getString("STOCKS_TEST_MODE"));	    //5
			vpd.put("var6", varOList.get(i).getString("OTHER_TEST_MODE"));	    //6
			vpd.put("var7", varOList.get(i).getString("SAMPLE_STANDARD_DEADLY"));	    //7
			vpd.put("var8", varOList.get(i).getString("SAMPLE_STANDARD_SEVERITY"));	    //8
			vpd.put("var9", varOList.get(i).getString("SAMPLE_STANDARD_SLIGHT"));	    //9
			vpd.put("var10", varOList.get(i).get("STOCKS_TEST_DAY").toString());	//10
			vpd.put("var11", varOList.get(i).get("STOCKS_WARNING_DAY").toString());	//11
			vpd.put("var12", varOList.get(i).getString("TEST_SCHEME"));	    //12
			vpd.put("var13", varOList.get(i).getString("FINSPECTOR"));	    //13
			vpd.put("var14", varOList.get(i).getString("FEXTEND1"));	    //14
			vpd.put("var15", varOList.get(i).getString("FEXTEND2"));	    //15
			vpd.put("var16", varOList.get(i).getString("FEXTEND3"));	    //16
			vpd.put("var17", varOList.get(i).getString("FEXTEND4"));	    //17
			vpd.put("var18", varOList.get(i).getString("FEXTEND5"));	    //18
			vpd.put("var19", varOList.get(i).getString("FCREATOR"));	    //19
			vpd.put("var20", varOList.get(i).getString("CREATE_TIME"));	    //20
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
