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
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.mom.SPOT_CHECKService;
import org.yy.service.mom.TEMBILL_EXECUTEService;

/** 
 * 说明：抽检单据
 * 作者：YuanYe
 * 时间：2020-03-17
 * 
 */
@Controller
@RequestMapping("/spot_check")
public class SPOT_CHECKController extends BaseController {
	
	@Autowired
	private SPOT_CHECKService spot_checkService;
	@Autowired
	private TEMBILL_EXECUTEService tembill_executeService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("spot_check:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SPOT_CHECK_ID", this.get32UUID());	//主键
		spot_checkService.save(pd);
		if(null!=pd.getString("DOCUMENTTEMPLATE_ID")&&!"".equals(pd.getString("DOCUMENTTEMPLATE_ID"))) {
			pd.put("IDNAME", "SPOT_CHECK_ID");//主键字段名
			pd.put("ID", pd.getString("SPOT_CHECK_ID"));//主键ID
			pd.put("FTABLE_NAME", "MOM_SPOT_CHECK");//表名
			pd.put("FIEID_NAME", "TEMBILL_EXECUTE_ID");//字段名
			tembill_executeService.saveTem(pd);
		}
		pd = spot_checkService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("spot_check:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		spot_checkService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("spot_check:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		spot_checkService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("spot_check:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = spot_checkService.list(page);	//列出SPOT_CHECK列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去新增页面获取数据
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goAdd")
		@ResponseBody
		public Object goAdd() throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("SPOT_CHECK_NUMBER", "CJ"+Tools.date2Str(new Date(),"yyyyMMddHHmmss"));//抽检单号
			pd.put("FDETECTOR", Jurisdiction.getName());//检测人
			pd.put("FDETECT_TIME", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm"));//检测时间
			pd.put("FOPERATOR", Jurisdiction.getName());//操作人
			pd.put("FOPERAT_TIME", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm"));//操作时间
			pd.put("FCREATOR", Jurisdiction.getName());//创建人
			pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
			pd.put("FSTATE", "待检");//状态
			map.put("pd", pd);
			map.put("result", errInfo);
			return map;
		}		

		/**修改检验状态
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
			spot_checkService.editState(pd);
			map.put("result", errInfo);				//返回结果
			return map;
		}
		
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("spot_check:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = spot_checkService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("spot_check:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			spot_checkService.deleteAll(ArrayDATA_IDS);
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
	//@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("生产工单ID");	//1
		titles.add("质量检测单ID");	//2
		titles.add("抽检单号");	//3
		titles.add("生产工单单号");	//4
		titles.add("产品编码");	//5
		titles.add("产品名称");	//6
		titles.add("产线名称");	//7
		titles.add("班组");	//8
		titles.add("规格");	//9
		titles.add("单位");	//10
		titles.add("工单负责人");	//11
		titles.add("检测开始");	//12
		titles.add("检测结束");	//13
		titles.add("检测人");	//14
		titles.add("检测时间");	//15
		titles.add("操作人");	//16
		titles.add("操作时间");	//17
		titles.add("结果");	//18
		titles.add("描述");	//19
		titles.add("状态");	//20
		titles.add("模板名称");	//21
		titles.add("所属类别");	//22
		titles.add("创建人");	//23
		titles.add("创建时间");	//24
		titles.add("扩展字段1");	//25
		titles.add("扩展字段2");	//26
		titles.add("扩展字段3");	//27
		titles.add("扩展字段4");	//28
		titles.add("扩展字段5");	//29
		titles.add("检测模板ID");	//30
		dataMap.put("titles", titles);
		List<PageData> varOList = spot_checkService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PLAN_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("TEMBILL_EXECUTE_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("SPOT_CHECK_NUMBER"));	    //3
			vpd.put("var4", varOList.get(i).getString("FBILLNO"));	    //4
			vpd.put("var5", varOList.get(i).getString("PRODUCT_NO"));	    //5
			vpd.put("var6", varOList.get(i).getString("PRODUCT_NAME"));	    //6
			vpd.put("var7", varOList.get(i).getString("PLINE_NAME"));	    //7
			vpd.put("var8", varOList.get(i).getString("FTEAM"));	    //8
			vpd.put("var9", varOList.get(i).getString("FSPECS"));	    //9
			vpd.put("var10", varOList.get(i).getString("FUNIT"));	    //10
			vpd.put("var11", varOList.get(i).getString("PLAN_PRINCIPAL"));	    //11
			vpd.put("var12", varOList.get(i).getString("FDETECT_START"));	    //12
			vpd.put("var13", varOList.get(i).getString("FDETECT_END"));	    //13
			vpd.put("var14", varOList.get(i).getString("FDETECTOR"));	    //14
			vpd.put("var15", varOList.get(i).getString("FDETECT_TIME"));	    //15
			vpd.put("var16", varOList.get(i).getString("FOPERATOR"));	    //16
			vpd.put("var17", varOList.get(i).getString("FOPERAT_TIME"));	    //17
			vpd.put("var18", varOList.get(i).getString("FEFFECT"));	    //18
			vpd.put("var19", varOList.get(i).getString("FDS"));	    //19
			vpd.put("var20", varOList.get(i).getString("FSTATE"));	    //20
			vpd.put("var21", varOList.get(i).getString("FNAME"));	    //21
			vpd.put("var22", varOList.get(i).getString("BELONG_TYPE"));	    //22
			vpd.put("var23", varOList.get(i).getString("FCREATOR"));	    //23
			vpd.put("var24", varOList.get(i).getString("CREATE_TIME"));	    //24
			vpd.put("var25", varOList.get(i).getString("FEXTEND1"));	    //25
			vpd.put("var26", varOList.get(i).getString("FEXTEND2"));	    //26
			vpd.put("var27", varOList.get(i).getString("FEXTEND3"));	    //27
			vpd.put("var28", varOList.get(i).getString("FEXTEND4"));	    //28
			vpd.put("var29", varOList.get(i).getString("FEXTEND5"));	    //29
			vpd.put("var30", varOList.get(i).getString("DOCUMENTTEMPLATE_ID"));	    //30
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
