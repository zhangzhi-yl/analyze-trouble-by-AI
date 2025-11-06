package org.yy.controller.fhoa;

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
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.yy.entity.PageData;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mom.WC_StationService;
import org.yy.service.mom.WorkStationPersonRelService;
import org.yy.service.system.UsersService;

/** 
 * 说明：通知公告
 * 作者：YuanYes QQ356703572
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
    private UsersService usersService;
	@Autowired
	private WorkStationPersonRelService workstationpersonrelService;
	@Autowired
	private WC_StationService wc_stationService;
		
	/**新增
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("notice:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdOp = new PageData();
		String name = Jurisdiction.getName();	//获取当前登录人
		pd.put("NOTICE_ID", this.get32UUID());							//主键
		pd.put("ReadPeople", ",");//已读人默认空
		pd.put("FIssuedID", Jurisdiction.getName());				//发布人
		pd.put("ReleaseTime", DateUtil.date2Str(new Date()));			//发布时间
		String ogj = pd.getString("TType");//接收类型
		String[] jieshou = pd.getString("ReceivingAuthority").split(",");//获取接收权限(接收人)
		String ReceivingAuthority = ",";
		String WorkStation = ",";
		if(ogj == "消息推送" || ogj.equals("消息推送")){//类型为消息推送时，将用户姓名转换成用户的登录账号
			for (String str : jieshou) {
				ReceivingAuthority += str;
				ReceivingAuthority  = ReceivingAuthority + ",";
			}
			pd.put("ReceivingAuthority", ReceivingAuthority);
		}else if(ogj == "企业通告" || ogj.equals("企业通告")){//类型为企业通告时，字段中存入all ，
			pd.put("ReceivingAuthority", "All");
		}else if(ogj == "站点通知" || ogj.equals("站点通知")){//类型为站点通知时，将站点下关联的人员存入到字段中
			for(String str : jieshou){
				pd.put("WorkstationID", str);
				List<PageData> varList = workstationpersonrelService.listAll(pd);//获取单个工作站下面的人员
				for(int i=0;i<varList.size();i++){//循环人员列表
					String USERNAME = toHanyuPinyin(varList.get(i).getString("staffname"));//将中文名转换成英文名
					int a = ReceivingAuthority.indexOf(USERNAME);
					if(a == -1 ){//判断人员是否重复
						ReceivingAuthority += USERNAME;
						ReceivingAuthority  = ReceivingAuthority + ",";
					}
				}
				//获取站点名称,循环存储
				pd.put("WC_STATION_ID", str);
				PageData STATIONPD = wc_stationService.findById(pd);
				 WorkStation += STATIONPD.get("FNAME").toString() + ",";
			}
			
			pd.put("ReceivingAuthority", ReceivingAuthority);
			pd.put("WorkStation", WorkStation);
		}
		noticeService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("notice:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		noticeService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("notice:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String ogj = pd.getString("ReceivingAuthority");	//修改接收权限字段样式
		if(ogj != "All" && !ogj.equals("All")){		//判断是否是单位通告
			String ReceivingAuthority = "," + ogj + ",";
			pd.put("ReceivingAuthority", ReceivingAuthority);
		}
		noticeService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("notice:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdO = new PageData();
		PageData pdL = new PageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String NAME = Jurisdiction.getUsername();					//登录用户	
			pd.put("USERNAME", NAME);
		page.setPd(pd);
		List<PageData>	varList = noticeService.list(page);	//列出Notice列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listRight")
	//@RequiresPermissions("notice:list")
	@ResponseBody
	public Object listRight(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdO = new PageData();
		PageData pdL = new PageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String NAME = Jurisdiction.getUsername();					//登录用户	
			pd.put("USERNAME", NAME);
		page.setPd(pd);
		List<PageData>	varList = noticeService.listRight(page);	//列出Notice列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**读取最新的一条
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getNewest")
	@ResponseBody
	public Object getNewest(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String USERNAME = Jurisdiction.getUsername();					//用户名
		String DEPARTMENT_ID = Jurisdiction.getDEPARTMENT_ID();
		if("".equals(DEPARTMENT_ID) || "无权".equals(DEPARTMENT_ID)){
			pd.put("DEPARTMENT_ID","");									//根据部门ID过滤
			if(!"admin".equals(USERNAME))pd.put("USERNAME", USERNAME);	//非admin用户时
		}else{
			pd.put("DEPARTMENT_ID", DEPARTMENT_ID);
			pd.put("ORUSERNAME", USERNAME);
		}
		page.setPd(pd);
		page.setShowCount(1);
		List<PageData>	varList = noticeService.list(page);	//列出Notice列表
		String notice = "", content = "",uname = "",ctime = "", noticeid = "1";
		if(varList.size() > 0) {
			noticeid = varList.get(0).getString("NOTICE_ID");	//ID
			notice = varList.get(0).getString("SYNOPSIS");		//简介
			content = varList.get(0).getString("CONTENT");		//内容
			uname = varList.get(0).getString("UNAME");			//发布人
			ctime = varList.get(0).getString("CTIME");			//创建时间
		}
		map.put("noticeid", noticeid);
		map.put("notice", notice);
		map.put("content", content);
		map.put("uname", uname);
		map.put("ctime", ctime);
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
		pd = noticeService.findById(pd);	//根据ID读取
		//点击查看修改已读字段
		String ReadPeople = pd.get("ReadPeople").toString();//获取现有已读人
		String USERNAME = Jurisdiction.getUsername();//获取当前登录人
		int a = ReadPeople.indexOf(USERNAME);
		if(a == -1){//判断当前登录人不在已读字段中
			ReadPeople = ReadPeople+USERNAME+",";
		}
		pd.put("ReadPeople", ReadPeople);
		noticeService.editRead(pd);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	//正则表达式去除指定头尾字符
	public static String trimBothEndsChars(String srcStr, String splitter) {
	    String regex = "^" + splitter + "*|" + splitter + "*$";
	    return srcStr.replaceAll(regex, "");
	}
	
	/**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goSee")
	@ResponseBody
	public Object goSee() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pdO = new PageData();
		PageData pdL = new PageData();
		pd = this.getPageData();
		pd = noticeService.findById(pd);	//根据ID读取
		String USERNAME = Jurisdiction.getName();	//登录姓名
		String NOTICE_ID = pd.getString("NOTICE_ID"); //获取ID
		String ReadPeople = pd.getString("ReadPeople");//获取当前已读人员
		
		if(ReadPeople != null && !ReadPeople.equals("")){
			String[] varList = ReadPeople.split(",");
			boolean flag = true;
			for(int i=0;i<varList.length;i++){
				String Name = varList[i];
				if(Name == USERNAME || Name.equals(USERNAME)){
					flag = false;
					break;
				}
			}
			if(flag == true){
				ReadPeople = ReadPeople+","+USERNAME;
			}
		}else{
			ReadPeople = USERNAME;
		}
		pdO.put("ReadPeople", ReadPeople);
		pdO.put("NOTICE_ID", NOTICE_ID);
		noticeService.editRead(pdO);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("notice:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			noticeService.deleteAll(ArrayDATA_IDS);
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
		titles.add("部门ID");	//1
		titles.add("用户名");	//2
		titles.add("发布人");	//3
		titles.add("发布时间");	//4
		titles.add("公告简介");	//5
		titles.add("公告内容");	//6
		titles.add("是否全显");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = noticeService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("DEPARTMENT_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("USERNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("UNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("CTIME"));	    //4
			vpd.put("var5", varOList.get(i).getString("SYNOPSIS"));	    //5
			vpd.put("var6", varOList.get(i).getString("CONTENT"));	    //6
			vpd.put("var7", varOList.get(i).getString("ISALL"));	    //7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	public String toHanyuPinyin(String ChineseLanguage){
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE); // 输出拼音全部小写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 不带声调
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V) ;
        try {
            for (int i=0; i<cl_chars.length; i++){
                if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")){// 如果字符是中文,则将中文转为汉语拼音
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                } else {// 如果字符不是中文,则不转换
                    hanyupinyin += cl_chars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
        	e.printStackTrace();
        }
        return hanyupinyin;
    }
	/**v1 管悦 20210907 修改已读状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editRead")
	//@RequiresPermissions("notice:edit")
	@ResponseBody
	public Object editRead() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = noticeService.findById(pd);	//根据ID读取
		PageData pdO = new PageData();
		String USERNAME = Jurisdiction.getUsername();	//登录姓名
		String NOTICE_ID = pd.getString("NOTICE_ID"); //获取ID
		String ReadPeople = pd.getString("ReadPeople");//获取当前已读人员
		
		if(ReadPeople != null && !ReadPeople.equals("")){
			String[] varList = ReadPeople.split(",");
			boolean flag = true;
			for(int i=0;i<varList.length;i++){
				String Name = varList[i];
				if(Name == USERNAME || Name.equals(USERNAME)){
					flag = false;
					break;
				}
			}
			if(flag == true){
				ReadPeople = ReadPeople+USERNAME+",";
			}
		}else{
			ReadPeople = USERNAME;
		}
		pdO.put("ReadPeople", ReadPeople);
		pdO.put("NOTICE_ID", NOTICE_ID);
		noticeService.editRead(pdO);
		map.put("result", errInfo);
		return map;
	}
}
