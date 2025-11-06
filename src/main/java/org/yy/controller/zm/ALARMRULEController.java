package org.yy.controller.zm;

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
import org.yy.service.zm.ALARMRULEService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;

/**
 * 说明：报警规则
 * 作者：YuanYe
 * 时间：2021-10-18
 *
 */
@Controller
@RequestMapping("/alarmrule")
public class ALARMRULEController extends BaseController {

	@Autowired
	private ALARMRULEService alarmruleService;

	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
//	@RequiresPermissions("alarmrule:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ALARMRULE_ID", this.get32UUID());	//主键
		pd.put("CREATOR", Jurisdiction.getName());	//主键
		pd.put("CREATE_TIME",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		alarmruleService.save(pd);
		map.put("result", errInfo);
		return map;
	}

	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
//	@RequiresPermissions("alarmrule:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		alarmruleService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}

	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
//	@RequiresPermissions("alarmrule:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("Modified", Jurisdiction.getName());
		pd.put("ModifiedTime",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		alarmruleService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
//	@RequiresPermissions("alarmrule:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS)) {
			pd.put("KEYWORDS", KEYWORDS.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = alarmruleService.list(page);	//列出ALARMRULE列表
		for (PageData var : varList){
			String ZM_PLC_NAME = var.getString("ZMPLCName");
			String NY_PLC_NAME = var.getString("PLCName");
			if("".equals(ZM_PLC_NAME) || null == ZM_PLC_NAME){
				var.put("PLCName",NY_PLC_NAME);
			}else {
				var.put("PLCName",ZM_PLC_NAME);
			}
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
//	@RequiresPermissions("alarmrule:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = alarmruleService.listAll(pd);	//列出ALARMRULE列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}

	/**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
//	@RequiresPermissions("alarmrule:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = alarmruleService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
//	@RequiresPermissions("alarmrule:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			alarmruleService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}

	/**
	 * 获取回路报警值
	 * @param
	 * @return
	 */
	@RequestMapping(value="/getLoopAlarm")
	@ResponseBody
	public Object getLoopAlarm() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData>	varList = alarmruleService.getLoopAlarm(pd);
		PageData alarm = new PageData();
		alarm.put("Ia",0);
		alarm.put("Ib",0);
		alarm.put("Ic",0);
		alarm.put("ua",0);
		alarm.put("ub",0);
		alarm.put("uc",0);
		alarm.put("epi",0);
		alarm.put("wflow",0);
		alarm.put("gflow",0);

		for (PageData var : varList){
			String EnergyType = var.getString("EnergyType");
			String ParamType = var.getString("ParamType");
			String ParamAttribute = var.getString("ParamAttribute");
			if("T_PatraElectric".equals(EnergyType) && "电流".equals(ParamType)){
				if("Ia".equals(ParamAttribute)){
					alarm.put("Ia",Double.parseDouble(var.get("THRESHOLD").toString()));
				}else if("Ib".equals(ParamAttribute)){
					alarm.put("Ib",Double.parseDouble(var.get("THRESHOLD").toString()));
				}else if("Ic".equals(ParamAttribute)){
					alarm.put("Ic",Double.parseDouble(var.get("THRESHOLD").toString()));
				}
			}else if("T_PatraElectric".equals(EnergyType) && "电压".equals(ParamType)){

				if("ua".equals(ParamAttribute)){
					alarm.put("ua",Double.parseDouble(var.get("THRESHOLD").toString()));
				}else if("ub".equals(ParamAttribute)){
					alarm.put("ub",Double.parseDouble(var.get("THRESHOLD").toString()));
				}else if("uc".equals(ParamAttribute)){
					alarm.put("uc",Double.parseDouble(var.get("THRESHOLD").toString()));
				}
			}else if("T_PatraElectric".equals(EnergyType) && ParamType.contains("能耗")){

				if("epi".equals(ParamAttribute)){
					alarm.put("epi",Double.parseDouble(var.get("THRESHOLD").toString()));
				}
			}else if("T_PatraWater".equals(EnergyType) && ParamType.contains("能耗")){
				if("flow".equals(ParamAttribute)){
					alarm.put("wflow",Double.parseDouble(var.get("THRESHOLD").toString()));
				}
			}else if("T_PatraGas".equals(EnergyType) && ParamType.contains("能耗")){
				if("flow".equals(ParamAttribute)){
					alarm.put("gflow",Double.parseDouble(var.get("THRESHOLD").toString()));
				}
			}
		}

		map.put("pd",alarm);
		map.put("result", errInfo);
		return map;
	}

	/**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
//	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("事件名称");	//1
		titles.add("条件");	//2
		titles.add("阀值");	//3
		titles.add("参数");
		titles.add("优先级");	//4
		titles.add("是否启用");	//5
		titles.add("区域位置");	//6
		titles.add("备注");	//7
		titles.add("创建时间");	//8
//		titles.add("");	//9
//		titles.add("");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = alarmruleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EVENT_NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("CONDITION"));	    //2
			vpd.put("var3", varOList.get(i).getString("THRESHOLD"));	    //3
			vpd.put("var4", varOList.get(i).getString("PLCName"));	    //3
			vpd.put("var5", varOList.get(i).getString("PRIORITY"));	    //4
			vpd.put("var6", varOList.get(i).getString("ISENABLE"));	    //5
			vpd.put("var7", varOList.get(i).getString("LOCATION"));	    //6
			vpd.put("var8", varOList.get(i).getString("REMARK"));	    //7
			vpd.put("var9", varOList.get(i).getString("CREATE_TIME"));	    //8
//			vpd.put("var9", varOList.get(i).getString(""));	    //9
//			vpd.put("var10", varOList.get(i).getString(""));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
