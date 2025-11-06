package org.yy.controller.mdmc;

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
import org.yy.service.mdmc.EQM_MAINTENANCE_THREE_CARDService;
import org.yy.service.mdmc.EQM_MAINTENANCE_ITEM_MAINService;
import org.yy.service.mdmc.EQM_MAINTENANCE_ITEM_QUARTERService;
import org.yy.service.mdmc.EQM_MAINTENANCE_PLAN_QUARTERService;
import org.yy.service.mdmc.EQM_MAINTENANCE_THREE_CARDMXService;

/** 
 * 说明：三级保养卡主表
 * 作者：YuanYe
 * 时间：2020-06-23
 * 
 */
@Controller
@RequestMapping("/eqm_maintenance_three_card")
public class EQM_MAINTENANCE_THREE_CARDController extends BaseController {
	
	@Autowired
	private EQM_MAINTENANCE_THREE_CARDService eqm_maintenance_three_cardService;
	
	@Autowired
	private EQM_MAINTENANCE_THREE_CARDMXService eqm_maintenance_three_cardmxService;
	
	@Autowired
	private EQM_MAINTENANCE_PLAN_QUARTERService eqm_maintenance_plan_quarterService;
	
	@Autowired
	private EQM_MAINTENANCE_ITEM_MAINService eqm_maintenance_item_mainService;
	
	@Autowired
	private EQM_MAINTENANCE_ITEM_QUARTERService eqm_maintenance_item_quarterService;
	
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
		PageData savepd = new PageData();
		pd = this.getPageData();
		try {
			String EQM_MAINTENANCE_PLAN_QUARTER_ID=pd.getString("EQM_MAINTENANCE_PLAN_QUARTER_ID");//保养周计划主键
			String RESPONSIBLE_DEPARTMENT=pd.getString("RESPONSIBLE_DEPARTMENT");//责任部门
			String MAINTENANCE_MODE=pd.getString("MAINTENANCE_MODE");//保养方式
			String DUTY_OFFICER=pd.getString("DUTY_OFFICER");//责任人
			String MAINTENANCE_PERIOD=pd.getString("MAINTENANCE_PERIOD");//保养周期
			String OUTSIDE_FACTORY=pd.getString("OUTSIDE_FACTORY");//厂外实施单位
			String REMARK=pd.getString("REMARK");//备注
			Integer TIME=Integer.valueOf(pd.getString("TIME"));//次数
			
			PageData pdquarter=eqm_maintenance_plan_quarterService.findById(pd);
			
			String FEQM_ID=pdquarter.getString("FEQM_ID");//设备主键
			String FCLASS_ID=pdquarter.getString("FEQM_CLASS_ID");//设备类主键
			String SINGLE_NUMBER=pdquarter.getString("FRECEIPT_NUMBER");//本单号
			String MACHINE_NAME=pdquarter.getString("FEQM_NAME");//设备名称
			String MACHINE_NUMBER=pdquarter.getString("FEQM_CODE");//设备编号
			pd.put("FCLASS_ID", FCLASS_ID);
			
			PageData pdc=eqm_maintenance_item_mainService.findByFclassId(pd);
			List<PageData> pdquartermx=eqm_maintenance_item_quarterService.findByFatherId(pdc);
			
			savepd.put("EQM_MAINTENANCE_THREE_CARD_ID", this.get32UUID());	//主键
			savepd.put("EQM_MAINTENANCE_PLAN_QUARTER_ID", EQM_MAINTENANCE_PLAN_QUARTER_ID);
			savepd.put("EQM_MAINTENANCE_ITEM_MAIN_ID", pdc.getString("EQM_MAINTENANCE_ITEM_MAIN_ID"));
			savepd.put("MACHINE_NAME", MACHINE_NAME);
			savepd.put("MACHINE_NUMBER", MACHINE_NUMBER);
			savepd.put("RESPONSIBLE_DEPARTMENT", RESPONSIBLE_DEPARTMENT);
			savepd.put("MAINTENANCE_MODE", MAINTENANCE_MODE);
			
			savepd.put("DUTY_OFFICER", DUTY_OFFICER);
			savepd.put("MAINTENANCE_PERIOD", MAINTENANCE_PERIOD);
			savepd.put("OUTSIDE_FACTORY", OUTSIDE_FACTORY);
			
			savepd.put("REMARK", REMARK);
			savepd.put("FEQM_ID", FEQM_ID);
			savepd.put("FCLASS_ID", FCLASS_ID);
			savepd.put("SINGLE_NUMBER", SINGLE_NUMBER);
			savepd.put("TIME", TIME);
			savepd.put("FEXTEND1", "未完成");//状态
			savepd.put("FCREATOR", Jurisdiction.getUsername());//创建人
			savepd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
			savepd.put("LAST_MODIFIER", Jurisdiction.getUsername());//最后修改人
			savepd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));//最后修改时间
			eqm_maintenance_three_cardService.save(savepd);
			
			if(pdquartermx.size()>0){
				for (int i = 0; i < pdquartermx.size(); i++) {
					PageData pddate=pdquartermx.get(i);
					String FMAINTENANCE_ITEM=pddate.getString("FMAINTENANCE_ITEM");
					String EQM_MAINTENANCE_ITEM_QUARTER_ID=pddate.getString("EQM_MAINTENANCE_ITEM_QUARTER_ID");
					PageData pdsavemx=new PageData();
					pdsavemx.put("EQM_MAINTENANCE_THREE_CARDMX_ID", this.get32UUID());
					pdsavemx.put("EQM_MAINTENANCE_THREE_CARD_ID", savepd.getString("EQM_MAINTENANCE_THREE_CARD_ID"));
					pdsavemx.put("EQM_MAINTENANCE_PLAN_QUARTER_ID", EQM_MAINTENANCE_PLAN_QUARTER_ID);
					pdsavemx.put("EQM_MAINTENANCE_ITEM_MAIN_ID", pdc.getString("EQM_MAINTENANCE_ITEM_MAIN_ID"));
					pdsavemx.put("FEQM_ID", FEQM_ID);
					pdsavemx.put("FCLASS_ID", FCLASS_ID);
					pdsavemx.put("FMAINTENANCE_ITEM", FMAINTENANCE_ITEM);
					pdsavemx.put("EQM_MAINTENANCE_ITEM_QUARTER_ID", EQM_MAINTENANCE_ITEM_QUARTER_ID);
					pdsavemx.put("FCREATOR", Jurisdiction.getUsername());//创建人
					pdsavemx.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
					pdsavemx.put("LAST_MODIFIER", Jurisdiction.getUsername());//最后修改人
					pdsavemx.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));//最后修改时间
					eqm_maintenance_three_cardmxService.save(pdsavemx);
				}
			}
			List<PageData> list=eqm_maintenance_three_cardService.listAll(pd);
			if(list.size()>0){
				PageData pdedit=new PageData();
					Integer FMAINTENANCE_COUNT = list.size();
					String strSql="UPDATE MDMC_EQM_MAINTENANCE_PLAN_QUARTER SET FMAINTENANCE_COUNT"+TIME+" = "+FMAINTENANCE_COUNT+",FSTATE"+TIME+" = '已保养' where EQM_MAINTENANCE_PLAN_QUARTER_ID = '"+EQM_MAINTENANCE_PLAN_QUARTER_ID+"'";
					pdedit.put("strSql", strSql);
					eqm_maintenance_plan_quarterService.editState(pdedit);
			}
		} catch (Exception e) {
			errInfo="error";
			e.printStackTrace();
		}
		map.put("result", errInfo);						//返回结果
		map.put("pd", savepd);	
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
		try{
			String EQM_MAINTENANCE_PLAN_QUARTER_ID=pd.getString("EQM_MAINTENANCE_PLAN_QUARTER_ID");
			Integer TIME=Integer.valueOf(pd.getString("TIME"));//次数
			eqm_maintenance_three_cardmxService.deleteFather(pd);
			eqm_maintenance_three_cardService.delete(pd);
			List<PageData> list=eqm_maintenance_three_cardService.listAll(pd);
			if(list.size()>0){
				PageData pdedit=new PageData();
				Integer FMAINTENANCE_COUNT = list.size();
				String strSql="UPDATE MDMC_EQM_MAINTENANCE_PLAN_QUARTER SET FMAINTENANCE_COUNT"+TIME+" = "+FMAINTENANCE_COUNT+",FSTATE"+TIME+" = '已保养' where EQM_MAINTENANCE_PLAN_QUARTER_ID = '"+EQM_MAINTENANCE_PLAN_QUARTER_ID+"'";
				pdedit.put("strSql", strSql);
				eqm_maintenance_plan_quarterService.editState(pdedit);
				
			}else{
				PageData pdedit=new PageData();
				Integer FMAINTENANCE_COUNT = list.size();
				String strSql="UPDATE MDMC_EQM_MAINTENANCE_PLAN_QUARTER SET FMAINTENANCE_COUNT"+TIME+" = "+FMAINTENANCE_COUNT+",FSTATE"+TIME+" = '未保养' where EQM_MAINTENANCE_PLAN_QUARTER_ID = '"+EQM_MAINTENANCE_PLAN_QUARTER_ID+"'";
				pdedit.put("strSql", strSql);
				eqm_maintenance_plan_quarterService.editState(pdedit);
			}
		} catch(Exception e){
			errInfo = "error";
		}
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
		pd.put("LAST_MODIFIER", Jurisdiction.getUsername());//最后修改人
		pd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));//最后修改时间
		eqm_maintenance_three_cardService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	@RequestMapping(value="/editState")
	@ResponseBody
	public Object editState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_maintenance_three_cardService.editState(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**
	 * 修改状态： 未完成、审批中、审核完成、已完成
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editState1")
	@ResponseBody
	public Object editState1() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_maintenance_three_cardService.editState1(pd);
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
		List<PageData> varList = eqm_maintenance_three_cardService.list(page);	//列出EQM_MAINTENANCE_THREE_CARD列表
		if(varList.size()>0){
			for (int i=0;i<varList.size();i++) {
				PageData pd1=varList.get(i);
				List<PageData>	varList1=eqm_maintenance_three_cardmxService.listAll(pd1);
				varList.get(i).put("varList1", varList1);
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_maintenance_three_cardService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("保养季计划主键");	//1
		titles.add("设备主键");	//2
		titles.add("设备类主键");	//3
		titles.add("保养项主模板主键");	//4
		titles.add("本单号");	//5
		titles.add("次数");	//6
		titles.add("设备名称");	//7
		titles.add("设备编号");	//8
		titles.add("保养方式");	//9
		titles.add("责任部门");	//10
		titles.add("责任人");	//11
		titles.add("保养周期");	//12
		titles.add("厂外实施单位");	//13
		titles.add("备注");	//14
		titles.add("创建人");	//15
		titles.add("创建时间");	//16
		titles.add("最后修改人");	//17
		titles.add("最后修改时间");	//18
		titles.add("扩展字段1");	//19
		titles.add("扩展字段2");	//20
		titles.add("扩展字段3");	//21
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_maintenance_three_cardService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_MAINTENANCE_PLAN_QUARTER_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FEQM_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FCLASS_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("EQM_MAINTENANCE_ITEM_MAIN_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("SINGLE_NUMBER"));	    //5
			vpd.put("var6", varOList.get(i).getString("TIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("MACHINE_NAME"));	    //7
			vpd.put("var8", varOList.get(i).getString("MACHINE_NUMBER"));	    //8
			vpd.put("var9", varOList.get(i).getString("MAINTENANCE_MODE"));	    //9
			vpd.put("var10", varOList.get(i).getString("RESPONSIBLE_DEPARTMENT"));	    //10
			vpd.put("var11", varOList.get(i).getString("DUTY_OFFICER"));	    //11
			vpd.put("var12", varOList.get(i).getString("MAINTENANCE_PERIOD"));	    //12
			vpd.put("var13", varOList.get(i).getString("OUTSIDE_FACTORY"));	    //13
			vpd.put("var14", varOList.get(i).getString("REMARK"));	    //14
			vpd.put("var15", varOList.get(i).getString("FCREATOR"));	    //15
			vpd.put("var16", varOList.get(i).getString("CREATE_TIME"));	    //16
			vpd.put("var17", varOList.get(i).getString("LAST_MODIFIER"));	    //17
			vpd.put("var18", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //18
			vpd.put("var19", varOList.get(i).getString("FEXTEND1"));	    //19
			vpd.put("var20", varOList.get(i).getString("FEXTEND2"));	    //20
			vpd.put("var21", varOList.get(i).getString("FEXTEND3"));	    //21
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
