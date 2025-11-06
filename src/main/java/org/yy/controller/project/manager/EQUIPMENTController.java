package org.yy.controller.project.manager;

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
import org.yy.service.project.manager.EQUIPMENTService;

/** 
 * 说明：项目设备
 * 作者：YuanYes QQ356703572
 * 时间：2020-09-01
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/equipment")
public class EQUIPMENTController extends BaseController {
	
	@Autowired
	private EQUIPMENTService equipmentService;
	
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
		pd.put("EQUIPMENT_ID", this.get32UUID());	//主键
		pd.put("ECREATE_TIME", Tools.date2Str(new Date()));
		pd.put("ECREATOR", Jurisdiction.getName());
		pd.put("ELAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pd.put("ELAST_MODIFIER", Jurisdiction.getName());
		//pd.put("EDEPARTMENT_ID", Jurisdiction.getDEPARTMENT_IDNEW());
		equipmentService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		//equipmentService.delete(pd);
		pd.put("EVISIBLE", "0");
		pd.put("FTYPE", "type1");
		equipmentService.upVisible(pd);//
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
		pd.put("ELAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		pd.put("ELAST_MODIFIER", Jurisdiction.getName());
		equipmentService.edit(pd);
		map.put("result", errInfo);
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
		List<PageData>	varList = equipmentService.list(page);	//列出EQUIPMENT列表
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
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = equipmentService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("equipment:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			equipmentService.deleteAll(ArrayDATA_IDS);
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
		titles.add("项目立项主键");	//1
		titles.add("项目编号");	//2
		titles.add("项目名称");	//3
		titles.add("设备编号");	//4
		titles.add("设备名称");	//5
		titles.add("合同签订日期");	//6
		titles.add("交货期");	//7
		titles.add("Type");	//8
		titles.add("台数");	//9
		titles.add("客户名称");	//10
		titles.add("j交货地点");	//11
		titles.add("设备合同金额");	//12
		titles.add("项目负责人");	//13
		titles.add("项目经理");	//14
		titles.add("备注");	//15
		titles.add("执行状态");	//16
		titles.add("是否删除");	//17
		titles.add("创建人");	//18
		titles.add("创建时间");	//19
		titles.add("最后修改人");	//20
		titles.add("最后修改时间");	//21
		dataMap.put("titles", titles);
		List<PageData> varOList = equipmentService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EPROJECT_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EPROJECT_CODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("EPROJECT_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("EEQM_NO"));	    //4
			vpd.put("var5", varOList.get(i).getString("EEQM_NAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("ESIGN_DATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("EDELIVERY_DATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("ETYPE"));	    //8
			vpd.put("var9", varOList.get(i).get("ESET_NUMBER").toString());	//9
			vpd.put("var10", varOList.get(i).getString("ECUSTOMER_NAME"));	    //10
			vpd.put("var11", varOList.get(i).getString("EDELIVERY_PLACE"));	    //11
			vpd.put("var12", varOList.get(i).get("ECONTRACT_AMOUNT").toString());	//12
			vpd.put("var13", varOList.get(i).getString("EPROJECT_PRINCIPAL"));	    //13
			vpd.put("var14", varOList.get(i).getString("EPROJECT_MANAGER"));	    //14
			vpd.put("var15", varOList.get(i).getString("ENOTE"));	    //15
			vpd.put("var16", varOList.get(i).getString("ERUN_STATE"));	    //16
			vpd.put("var17", varOList.get(i).getString("EVISIBLE"));	    //17
			vpd.put("var18", varOList.get(i).getString("ECREATOR"));	    //18
			vpd.put("var19", varOList.get(i).getString("ECREATE_TIME"));	    //19
			vpd.put("var20", varOList.get(i).getString("ELAST_MODIFIER"));	    //20
			vpd.put("var21", varOList.get(i).getString("ELAST_MODIFIED_TIME"));	    //21
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**修改执行状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editState")
	@ResponseBody
	public Object editState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pd1 = new PageData();
		pd = this.getPageData();
		equipmentService.editState(pd);
		map.put("result", errInfo);
		pd1 = equipmentService.getPROID(pd);
		pd.put("PRIJECT_ID", pd1.get("EPROJECT_ID"));//获取项目ID 范
		try{
			
		}catch(Exception e){
			System.out.println("调用新增设备费用管理方法失败");
		}
		return map;
	}
}
