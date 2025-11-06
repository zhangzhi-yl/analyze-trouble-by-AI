package org.yy.controller.mdm;

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
import org.yy.service.mdm.EQM_BASE_PARAMETERService;

/** 
 * 说明：设备基础参数设置
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_base_parameter")
public class EQM_BASE_PARAMETERController extends BaseController {
	
	@Autowired
	private EQM_BASE_PARAMETERService eqm_base_parameterService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_base_parameter:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_BASE_PARAMETER_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
		eqm_base_parameterService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_base_parameter:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_base_parameterService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_base_parameter:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_base_parameterService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_base_parameter:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_base_parameterService.list(page);	//列出EQM_BASE_PARAMETER列表
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
	//@RequiresPermissions("eqm_base_parameter:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_base_parameterService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**去修改页面通过设备基础资料Id获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goSonEdit")
		//@RequiresPermissions("eqm_base_parameter:edit")
		@ResponseBody
		public Object goSonEdit() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = eqm_base_parameterService.findBySonId(pd);	//根据ID读取
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("eqm_base_parameter:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_base_parameterService.deleteAll(ArrayDATA_IDS);
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
		titles.add("负责人");	//1
		titles.add("负责人电话");	//2
		titles.add("检修人");	//3
		titles.add("检修人电话");	//4
		titles.add("紧急联系人");	//5
		titles.add("紧急联系人电话");	//6
		titles.add("厂家联系人");	//7
		titles.add("厂家联系人电话");	//8
		titles.add("进场日期");	//9
		titles.add("是否保养定期提醒");	//10
		titles.add("定期保养天");	//11
		titles.add("是否点巡检定期提醒");	//12
		titles.add("点巡检天");	//13
		titles.add("扩展字段1");	//14
		titles.add("扩展字段2");	//15
		titles.add("扩展字段3");	//16
		titles.add("扩展字段4");	//17
		titles.add("扩展字段5");	//18
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_base_parameterService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FHEAD"));	    //1
			vpd.put("var2", varOList.get(i).getString("HEAD_PONE"));	    //2
			vpd.put("var3", varOList.get(i).getString("OVERHAUL_PERSON"));	    //3
			vpd.put("var4", varOList.get(i).getString("OVERHAUL_PERSON_PHONE"));	    //4
			vpd.put("var5", varOList.get(i).getString("URGENT_CONTACT"));	    //5
			vpd.put("var6", varOList.get(i).getString("URGENT_CONTACT_PHONE"));	    //6
			vpd.put("var7", varOList.get(i).getString("MANUFACTOR_CONTACT"));	    //7
			vpd.put("var8", varOList.get(i).getString("MANUFACTOR_CONTACT_PHONE"));	    //8
			vpd.put("var9", varOList.get(i).getString("ENTER_DATE"));	    //9
			vpd.put("var10", varOList.get(i).getString("RME_WHETHER"));	    //10
			vpd.put("var11", varOList.get(i).get("RME_DAY").toString());	//11
			vpd.put("var12", varOList.get(i).getString("RPIN_WHETHER"));	    //12
			vpd.put("var13", varOList.get(i).get("SIN_DAY").toString());	//13
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
