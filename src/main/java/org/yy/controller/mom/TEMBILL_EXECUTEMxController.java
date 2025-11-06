package org.yy.controller.mom;

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
import org.yy.service.mom.TEMBILL_EXECUTEMxService;

/** 
 * 说明：质量检测发布(明细)
 * 作者：YuanYe
 * 时间：2020-02-24
 * 
 */
@Controller
@RequestMapping("/tembill_executemx")
public class TEMBILL_EXECUTEMxController extends BaseController {
	
	@Autowired
	private TEMBILL_EXECUTEMxService tembill_executemxService;
	
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
		pd.put("TEMBILL_EXECUTEMX_ID", this.get32UUID());	//主键
		tembill_executemxService.save(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		tembill_executemxService.delete(pd);
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
		tembill_executemxService.edit(pd);
		map.put("result", errInfo);				//返回结果
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
		List<PageData>	varList = tembill_executemxService.list(page);	//列出TEMBILL_EXECUTEMx列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);									//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = tembill_executemxService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			tembill_executemxService.deleteAll(ArrayDATA_IDS);
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
		titles.add("标题");	//1
		titles.add("描述");	//2
		titles.add("是否有描述明细");	//3
		titles.add("描述类型");	//4
		titles.add("描述明细内容");	//5
		titles.add("描述明细内容反馈");	//6
		titles.add("有无其他描述");	//7
		titles.add("其他描述内容");	//8
		titles.add("右侧选项内容");	//9
		titles.add("右侧选项内容反馈");	//10
		titles.add("排序");	//11
		titles.add("最后操作人");	//12
		titles.add("最后操作时间");	//13
		titles.add("扩展字段1");	//14
		titles.add("扩展字段2");	//15
		titles.add("扩展字段3");	//16
		titles.add("扩展字段4");	//17
		titles.add("扩展字段5");	//18
		dataMap.put("titles", titles);
		List<PageData> varOList = tembill_executemxService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CAPTION"));	    //1
			vpd.put("var2", varOList.get(i).getString("FDESCRIBE"));	    //2
			vpd.put("var3", varOList.get(i).getString("DESCRIPTION_DETAILS"));	    //3
			vpd.put("var4", varOList.get(i).getString("DESCRIBE_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("DESCRIPTION_CONTENT"));	    //5
			vpd.put("var6", varOList.get(i).getString("DESCRIBE_CONTENT_BF"));	    //6
			vpd.put("var7", varOList.get(i).getString("OTHER_DESCRIPTION_TYPE"));	    //7
			vpd.put("var8", varOList.get(i).getString("OTHER_DESCRIPTION"));	    //8
			vpd.put("var9", varOList.get(i).getString("RIGHT_OPTION_CONTENT"));	    //9
			vpd.put("var10", varOList.get(i).getString("RIGHT_CONTENT_BF"));	    //10
			vpd.put("var11", varOList.get(i).get("SORT").toString());	//11
			vpd.put("var12", varOList.get(i).getString("FLASTUPDATEPEOPLE"));	    //12
			vpd.put("var13", varOList.get(i).getString("FLASTUPDATETIME"));	    //13
			vpd.put("var14", varOList.get(i).getString("FEXTEND1"));	    //14
			vpd.put("var15", varOList.get(i).getString("FEXTEND2"));	    //15
			vpd.put("var16", varOList.get(i).getString("FEXTEND3"));	    //16
			vpd.put("var17", varOList.get(i).getString("FEXTEND4"));	    //17
			vpd.put("var18", varOList.get(i).getString("FEXTEND5"));	    //18
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
