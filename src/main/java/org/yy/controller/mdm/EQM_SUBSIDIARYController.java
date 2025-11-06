package org.yy.controller.mdm;

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
import org.yy.service.mdm.EQM_SUBSIDIARYService;

/** 
 * 说明：附属设备及计量仪表
 * 作者：YuanYe
 * 时间：2020-06-09
 * 
 */
@Controller
@RequestMapping("/eqm_subsidiary")
public class EQM_SUBSIDIARYController extends BaseController {
	
	@Autowired
	private EQM_SUBSIDIARYService eqm_subsidiaryService;
	
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
		pd.put("EQM_SUBSIDIARY_ID", this.get32UUID());	//附属设备ID主键
		/*pd.put("FEXTEND1", "");	//预留扩展1
		pd.put("FEXTEND2", "");	//预留扩展2
		pd.put("FEXTEND3", "");	//预留扩展3
		pd.put("FEXTEND4", "");	//预留扩展4
		pd.put("FEXTEND5", "");	//预留扩展5*/
		eqm_subsidiaryService.save(pd);
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
		eqm_subsidiaryService.delete(pd);
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
		eqm_subsidiaryService.edit(pd);
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
		List<PageData>	varList = eqm_subsidiaryService.list(page);	//列出列表
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
		pd = eqm_subsidiaryService.findById(pd);	//根据ID读取
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
			eqm_subsidiaryService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备ID");	//1
		titles.add("所属设备名称");	//2
		titles.add("名称");	//3
		titles.add("型号");	//4
		titles.add("数量");	//5
		titles.add("厂家");	//6
		titles.add("用途");	//7
		titles.add("单位原值");	//8
		titles.add("备注");	//9
		/*titles.add("预留扩展1");	//10
		titles.add("预留扩展2");	//11
		titles.add("预留扩展3");	//12
		titles.add("预留扩展4");	//13
		titles.add("预留扩展5");	//14
*/		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_subsidiaryService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_SUBSIDIARY_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQM_BASE_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FCODE"));	    //4
			vpd.put("var5", varOList.get(i).get("FAMOUNT").toString());	//5
			vpd.put("var6", varOList.get(i).getString("FMANUFACTURER"));	    //6
			vpd.put("var7", varOList.get(i).getString("FPURPOSE"));	    //7
			vpd.put("var8", varOList.get(i).getString("UNIT_ORIGINAL_VALUE "));	    //8
			vpd.put("var9", varOList.get(i).getString("FREMARKS"));	    //9
			/*vpd.put("var10", varOList.get(i).getString("FEXTEND1"));	    //10
			vpd.put("var11", varOList.get(i).getString("FEXTEND2"));	    //11
			vpd.put("var12", varOList.get(i).getString("FEXTEND3"));	    //12
			vpd.put("var13", varOList.get(i).getString("FEXTEND4"));	    //13
			vpd.put("var14", varOList.get(i).getString("FEXTEND5"));	    //14
*/			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
