package org.yy.controller.mdm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.yy.service.mdm.EQM_VULNERABLE_PARTSService;

/** 
 * 说明：设备易损件
 * 作者：YuanYe
 * 时间：2020-06-09
 * 
 */
@Controller
@RequestMapping("/eqm_vulnerable_parts")
public class EQM_VULNERABLE_PARTSController extends BaseController {
	
	@Autowired
	private EQM_VULNERABLE_PARTSService eqm_vulnerable_partsService;
	
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
		Integer day = Integer.parseInt(pd.getString("MAINTENANCE_CYCLE")); //获取易损件维护周期
		Date date = new Date();
		Calendar calendar =new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, day);	//进行计算，算出当前时间加上易损件周期后的下一次维护日期
		date = calendar.getTime();	//计算后的日期
		pd.put("VULNERABLE_PARTS_ID", this.get32UUID());	//主键设备易损件ID
		pd.put("CHECKIN_TIME", Tools.date2Str(date, "yyyy-MM-dd"));		//易损件维护日期
		/*pd.put("FEXTEND1", "");	//预留扩展1
		pd.put("FEXTEND2", "");	//预留扩展2
		pd.put("FEXTEND3", "");	//预留扩展3
		pd.put("FEXTEND4", "");	//预留扩展4*/		
		eqm_vulnerable_partsService.save(pd);
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
		eqm_vulnerable_partsService.delete(pd);
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
		PageData pd1 = new PageData();
		pd = this.getPageData();
		String VULNERABLE_PARTS_ID = pd.getString("VULNERABLE_PARTS_ID");
		pd1.put("VULNERABLE_PARTS_ID", VULNERABLE_PARTS_ID);
		pd1 = eqm_vulnerable_partsService.findById(pd1);		//通过ID获取修改前的记录
		Integer dayNew = Integer.parseInt(pd.getString("MAINTENANCE_CYCLE"));		//本次操作后填写的周期天数
		Integer dayOld = Integer.parseInt(pd1.getString("MAINTENANCE_CYCLE"));		//上一次操作填写的周期天数
		if(dayNew == dayOld){	//比较两次天数是否相同
			pd.put("CHECKIN_TIME", pd1.getString("CHECKIN_TIME"));		//如果相同，则易损件提醒日期不变
		}else{			//如果不同则从新计算
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(pd1.getString("CHECKIN_TIME"));	//获取上一次操作后的易损件提醒日期
			Calendar calendar =new GregorianCalendar();
			Integer day = dayNew - dayOld;		//计算出上一次操作填写的周期天数和本次操作后填写的周期天数的差值
			calendar.setTime(date);
			calendar.add(calendar.DATE, day);	//用上一次的提醒日期加上差值的天数计算新的提醒日期(正数则会往后延长相应天，负数则会往前追回相应天数天)
			date = calendar.getTime();			//计算后的日期
			pd.put("CHECKIN_TIME", Tools.date2Str(date, "yyyy-MM-dd"));		//将计算后的日期放进pd中
		}
		eqm_vulnerable_partsService.edit(pd);			//进行修改
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
		List<PageData>	varList = eqm_vulnerable_partsService.list(page);	//列出EQM_BASEMx列表
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
		pd = eqm_vulnerable_partsService.findById(pd);	//根据ID读取
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
			eqm_vulnerable_partsService.deleteAll(ArrayDATA_IDS);
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
		titles.add("设备易损件ID");	//1
		titles.add("所属设备");	//2
		titles.add("名称");	//3
		titles.add("型号规格");	//4
		titles.add("材质");	//5
		titles.add("数量");	//6
		titles.add("单位价格");	//7
		titles.add("备注");	//8
		/*titles.add("预留扩展1");	//9
		titles.add("预留扩展2");	//10
		titles.add("预留扩展3");	//11
		titles.add("预留扩展4");	//12
		titles.add("预留扩展5");	//13*/		
		titles.add("生产厂家");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_vulnerable_partsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("VULNERABLE_PARTS_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("EQM_BASE_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FSPECS"));	    //4
			vpd.put("var5", varOList.get(i).getString("FTEXTURE"));	    //5
			vpd.put("var6", varOList.get(i).get("FAMOUNT").toString());	//6
			vpd.put("var7", varOList.get(i).get("FPRICE").toString());	//7
			vpd.put("var8", varOList.get(i).getString("FREMARKS"));	    //8
			/*vpd.put("var9", varOList.get(i).getString("FEXTEND1"));	    //9
			vpd.put("var10", varOList.get(i).getString("FEXTEND2"));	    //10
			vpd.put("var11", varOList.get(i).getString("FEXTEND3"));	    //11
			vpd.put("var12", varOList.get(i).getString("FEXTEND4"));	    //12
			vpd.put("var13", varOList.get(i).getString("FEXTEND5"));	    //13*/			
			vpd.put("var14", varOList.get(i).getString("FMANUFACTURER"));	    //14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
