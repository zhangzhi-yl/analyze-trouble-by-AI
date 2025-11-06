package org.yy.controller.mom;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.mom.TeamCalendarService;

/** 
 * 说明：工厂日历管理
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/teamcalendar")
public class TeamCalendarController extends BaseController {
	
	@Autowired
	private TeamCalendarService teamcalendarService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("teamcalendar:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TEAM_CALENDAR_ID", this.get32UUID());	//日历ID
		teamcalendarService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("teamcalendar:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		teamcalendarService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("teamcalendar:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		teamcalendarService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("teamcalendar:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = teamcalendarService.list(page);	//列出TeamCalendar列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/dayList")
	@ResponseBody
	public Object dayList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "500";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			List<PageData> varList = teamcalendarService.dayList(pd);
			List<Map<String, Object>> varJsons=new ArrayList<Map<String, Object>>();
			for (int i = 0; i < varList.size(); i++) {
				PageData pd1=varList.get(i);
				Map<String, Object> mapjson = new HashMap<String, Object>();
				mapjson.put("title",pd1.getString("CLASSES_NAME")+":"+"<br>"+pd1.get("START_TIME") +"-"+ pd1.get("END_TIME"));
				mapjson.put("start", pd1.get("START_TIME"));
				mapjson.put("end", pd1.get("END_TIME"));
				mapjson.put("end", pd1.get("END_TIMEDAY"));
				if(pd1.get("START_TIMEDAY").equals(pd1.get("END_TIMEDAY"))) {
					mapjson.put("className", "label-grey");
				}else {
					mapjson.put("className", "label-purple");
				}
				mapjson.put("id", pd1.get("TEAM_CALENDAR_ID"));
				varJsons.add(mapjson);
			}
			if(varJsons.size()>0){
					map.put("strjson",varJsons);
					map.put("ftype",200);
			}else{
				map.put("ftype",500);
			}
			
			map.put("msg","ok");
			map.put("msgText","查询成功！");
		}catch (Exception e){
			result = "500";
			map.put("msg","no");
			map.put("msgText","未知错误，请联系管理员！");
		}finally{
			map.put("result", result);
		}
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("teamcalendar:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = teamcalendarService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("teamcalendar:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			teamcalendarService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**数据时间段重复检查
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/checkRepeat")
	@ResponseBody
	public Object checkRepeat() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String START_DATE = pd.getString("START_DATE");
		String END_DATE = pd.getString("END_DATE");
		String START_TIME = pd.getString("START_TIME");
		String END_TIME = pd.getString("END_TIME");
		pd.put("START_TIME", START_DATE+" "+START_TIME);
		pd.put("END_TIME", END_DATE+" "+END_TIME);
		JSONArray teamArr = JSONArray.fromObject(pd.getString("TEAMS_INFO"));
		int n=0;int m=0;
		for(int i=0;i<teamArr.size();i++) {
			String[] teamInfoArr = teamArr.get(i).toString().split("&");
			String teamId = teamInfoArr[0];
			pd.put("TEAM_ID", teamId);
			pd = teamcalendarService.checkRepeat(pd);	//判断时间段重复
			m = Integer.parseInt(pd.get("num").toString());
			if(m>0) {
				n++;
			}
		}
		pd.put("n", n);
		if(n>0) {
			errInfo = "error";
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 批量排班
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/shift")
	@ResponseBody
	public Object staffBinding() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData calendarPd = new PageData();
		pd = this.getPageData();
		//TEAMS_INFO格式:[“班组ID1&班组名称1”，“班组ID2&班组名称2”，。。。],
		//参照："[\"90dc4d85a9a14d68b826d45cf6eefc92&班组名称1\",\"3696b2ae6b4e43d3bebc4e78df754995&12\",
		//\"6e4a9d6d2cc7446c98dca2cd43f52a9f&2\",\"6e4a9d6d2cc7446c98dca2cd43f52888&班组2\"]"
		JSONArray teamArr = JSONArray.fromObject(pd.getString("TEAMS_INFO"));
		String FSHIFT = pd.getString("FSHIFT");
		String[] manHourArr = null;

			manHourArr = pd.getString("manHourStr").split(",");
		String START_DATE = pd.getString("START_DATE");
		String END_DATE = pd.getString("END_DATE");
		String START_TIME = pd.getString("START_TIME")+":00";
		String END_TIME = pd.getString("END_TIME")+":00";
		String AREA_ID = pd.getString("AREA_ID");
		String SHIFT_SYSTEM = pd.getString("SHIFT_SYSTEM");
		String START_TEAM = pd.getString("START_TEAM");
		String[] STARTTEAMARR = START_TEAM.split("&");
		String START_TEAM_ID = STARTTEAMARR[0];
		String START_TEAM_NAME = STARTTEAMARR[1];
		int manHourArrIndex=0;
		String name="";
		int startFlag=0;
		for(int i=0;i<teamArr.size();i++) {
			if(i>0) {name+="|";}
			name+= teamArr.get(i).toString().split("&")[1];
			if(START_TEAM_ID.equals(teamArr.get(i).toString().split("&")[0])){
				startFlag=i;
			}
		}
		int teamArrIndex=startFlag;
		String startDateTime = START_DATE+" "+START_TIME;
		List<PageData> list = new ArrayList<>();
		if("轮班".equals(FSHIFT)) {
			while(true) {
				if(manHourArrIndex>manHourArr.length-1){manHourArrIndex=0;}
				if(teamArrIndex>teamArr.size()-1){teamArrIndex=0;}
				calendarPd = new PageData();
				calendarPd.put("TEAM_CALENDAR_ID", this.get32UUID());
				calendarPd.put("START_DATE", START_DATE);
				calendarPd.put("END_DATE", END_DATE);
				String[] teamInfoArr = teamArr.get(teamArrIndex).toString().split("&");
				String teamId = teamInfoArr[0];
				String teamName = teamInfoArr[1];
				calendarPd.put("CLASSES_NAME", SHIFT_SYSTEM+teamName);
				calendarPd.put("START_TEAM", START_TEAM_NAME);
				calendarPd.put("TEAM_ID", teamId);
				startDateTime=getAddJTime(startDateTime, (Double.parseDouble(manHourArr[manHourArrIndex])*60));//计算得出结束时间
				calendarPd.put("START_TIME", getAddJTime(startDateTime, -(Double.parseDouble(manHourArr[manHourArrIndex])*60)));
				calendarPd.put("END_TIME", startDateTime);
				Date d1 = DateUtil.str2Date(getAddJTime(startDateTime, -(Double.parseDouble(manHourArr[manHourArrIndex])*60)));
				Date d2 = DateUtil.str2Date(startDateTime);
				if(!DateUtils.isSameDay(d1, d2)) {
					calendarPd.put("ACROSSDAY", "是");
				} else {
					calendarPd.put("ACROSSDAY", "否");
				}
				calendarPd.put("AREA_ID", AREA_ID);
				calendarPd.put("FSHIFT", FSHIFT);
				calendarPd.put("MAN_HOUR", teamArr.size()+"-"+pd.getString("manHourStr").replace(",","|"));
				calendarPd.put("TEAMS_INFO", teamArr.size()+"-"+name);
				calendarPd.put("TEAMS_INFO", teamArr.size()+"-"+name);
				list.add(calendarPd);
				int res=startDateTime.compareTo((END_DATE+" "+END_TIME));
				manHourArrIndex++;
				teamArrIndex++;
				if(res>=0){
					break;
				}
			}
		}else if("倒班".equals(FSHIFT)){
			while(true) {
				if(teamArrIndex>teamArr.size()-1){teamArrIndex=0;}
				calendarPd = new PageData();
				calendarPd.put("TEAM_CALENDAR_ID", this.get32UUID());
				calendarPd.put("START_DATE", START_DATE);
				calendarPd.put("END_DATE", END_DATE);
				String[] teamInfoArr = teamArr.get(teamArrIndex).toString().split("&");
				String teamId = teamInfoArr[0];
				String teamName = teamInfoArr[1];
				calendarPd.put("CLASSES_NAME", SHIFT_SYSTEM+teamName);
				calendarPd.put("START_TEAM", START_TEAM_NAME);
				calendarPd.put("TEAM_ID", teamId);
				
				startDateTime=getAddJTime(startDateTime, (Double.parseDouble(manHourArr[teamArrIndex])*60));//计算得出结束时间
				calendarPd.put("START_TIME", getAddJTime(startDateTime, -(Double.parseDouble(manHourArr[teamArrIndex])*60)));
				calendarPd.put("END_TIME", startDateTime);
				Date d1 = DateUtil.str2Date(getAddJTime(startDateTime, -(Double.parseDouble(manHourArr[teamArrIndex])*60)));
				Date d2 = DateUtil.str2Date(startDateTime);
				if(!DateUtils.isSameDay(d1, d2)) {
					calendarPd.put("ACROSSDAY", "是");
				} else {
					calendarPd.put("ACROSSDAY", "否");
				}
				calendarPd.put("AREA_ID", AREA_ID);
				calendarPd.put("FSHIFT", FSHIFT);
				calendarPd.put("MAN_HOUR", teamArr.size()+"-"+pd.getString("manHourStr").replace(",","|"));
				calendarPd.put("TEAMS_INFO", teamArr.size()+"-"+name);
				list.add(calendarPd);
				int res=startDateTime.compareTo((END_DATE+" "+END_TIME));
				teamArrIndex++;
				if(res>=0){
					break;
				}
			}
		}
		teamcalendarService.batchInsert(list);
		map.put("result", errInfo);				//返回结果
		return map;
	}

	/**
	 * 	返回sdate字符串所代表的时间相加fen分钟后的时间。
	 * @param sdate yyyy-MM-dd HH:mm:ss格式时间字符串
	 * @param fen 分钟数
	 * @return	返回在sdate时间基础上fen分钟后的时间
	 * @throws ParseException
	 */
	public static String getAddJTime(String sdate,double fen) throws ParseException {
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =sdf.parse(sdate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, (int)fen);
		String addJTimeString = sdf.format(calendar.getTime());
		return addJTimeString;
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
		titles.add("班次名称");	//1
		titles.add("开始日期");	//2
		titles.add("结束日期");	//3
		titles.add("跨天");	//4
		titles.add("所属车间");	//5
		titles.add("所属班制");	//6
		titles.add("开始时间");	//7
		titles.add("结束时间");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = teamcalendarService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CLASSES_NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("START_DATE"));	    //2
			vpd.put("var3", varOList.get(i).getString("END_DATE"));	    //3
			vpd.put("var4", varOList.get(i).getString("ACROSSDAY"));	    //4
			vpd.put("var5", varOList.get(i).getString("AREANAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("TEAMNAME"));	    //6
			vpd.put("var7", varOList.get(i).getString("START_TIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("END_TIME"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
