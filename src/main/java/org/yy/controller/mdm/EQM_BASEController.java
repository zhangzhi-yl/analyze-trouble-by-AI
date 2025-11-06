package org.yy.controller.mdm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
//import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
//import org.yy.util.PathUtil;
import org.yy.util.Tools;
//import org.yy.util.TwoDimensionCode;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
//import org.yy.service.mdm.EQM_ACCIDENT_REPORTService;
import org.yy.service.mdm.EQM_ANNEXService;
import org.yy.service.mdm.EQM_AUTOINFOService;
import org.yy.service.mdm.EQM_BASEService;
import org.yy.service.mdm.EQM_BASE_PARAMETERService;
import org.yy.service.mdm.EQM_CULTURE_PLANService;
import org.yy.service.mdm.EQM_DEBUGService;
import org.yy.service.mdm.EQM_EXTENDService;
import org.yy.service.mdm.EQM_INSTALLService;
import org.yy.service.mdm.EQM_LEDGERService;
import org.yy.service.mdm.EQM_OVERHAULService;
import org.yy.service.mdm.EQM_RECORDService;
import org.yy.service.mdm.EQM_SPARE_PARTSService;
import org.yy.service.mdm.EQM_SPECIFICATIONService;
import org.yy.service.mdm.EQM_SUBSIDIARYService;
import org.yy.service.mdm.EQM_UNBOXCHECKService;
import org.yy.service.mdm.EQM_VERIFYService;
import org.yy.service.mdm.EQM_VULNERABLE_PARTSService;
//import org.yy.service.system.FHlogService;

/** 
 * 说明：设备基础资料
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/eqm_base")
public class EQM_BASEController extends BaseController {
	
	@Autowired
	private EQM_BASEService eqm_baseService;
	@Autowired
	private EQM_BASE_PARAMETERService eqm_base_parameterService;//设备基础参数设置
	@Autowired
	private EQM_ANNEXService eqm_annexService;//设备附件
	@Autowired
	private EQM_AUTOINFOService eqm_autoinfoService;//设备自动化资料
	@Autowired
	private EQM_SPARE_PARTSService eqm_spare_partsService;//设备备品备件
	@Autowired
	private EQM_SPECIFICATIONService eqm_specificationService;//设备规范明细资料
	@Autowired
	private EQM_EXTENDService eqm_extendService;//设备实体扩展
	@Autowired
	private EQM_RECORDService eqm_recordService;//设备档案
	@Autowired
	private EQM_LEDGERService eqm_ledgerService;//设备台账
	@Autowired
	private EQM_VERIFYService eqm_verifyService;//设备校验登记
	@Autowired
	private EQM_OVERHAULService eqm_overhaulService;//设备检修计划
	@Autowired
	private EQM_CULTURE_PLANService eqm_culture_planService;//保修保养计划
	@Autowired
	private StaffService staffService;
	@Autowired
	private EQM_SUBSIDIARYService eqm_subsidiaryService;//附属设备计量仪表
	@Autowired
	private EQM_VULNERABLE_PARTSService eqm_vulnerable_partsService;//易损件
	@Autowired
	private EQM_UNBOXCHECKService eqm_unboxcheckService;//开箱验收单
	@Autowired
	private EQM_INSTALLService eqm_installService;//设备安装记录
	@Autowired
	private EQM_DEBUGService eqm_debugService;//设备调试验收单
	@Autowired
	private MQTTConnect MQTTConnect;
//	@Autowired
//    private FHlogService FHLOG;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_base:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Date date = new Date();//时间
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		String errInfo = "success";
		PageData pd = new PageData();
		PageData parameterPd = new PageData();//保存设备基础参数设置用
		PageData unboxcheckPd = new PageData();//保存开箱验收单
		PageData installPd = new PageData();//保存设备安装记录
		PageData debugPd = new PageData();//保存设备调试验收单
		pd = this.getPageData();
		pd.put("EQM_BASE_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", dateFormat.format(date));//创建时间
		parameterPd.put("EQM_BASE_PARAMETER_ID", this.get32UUID());	//设备基础参数设置主键
		parameterPd.put("EQM_BASE_ID", pd.getString("EQM_BASE_ID"));	//设备基础资料主键
		parameterPd.put("FCREATOR",Jurisdiction.getName());//创建人
		parameterPd.put("CREATE_TIME", dateFormat.format(date));//创建时间
		unboxcheckPd.put("EQM_UNBOXCHECK_ID", this.get32UUID());
		unboxcheckPd.put("EQM_BASE_ID", pd.getString("EQM_BASE_ID"));
		unboxcheckPd.put("FNAME", pd.containsKey("FNAME")?pd.getString("FNAME"):"");
		unboxcheckPd.put("FSPECS", pd.containsKey("FSPECS")?pd.getString("FSPECS"):"");
		unboxcheckPd.put("FMANUFACTOR", pd.containsKey("FMANUFACTOR")?pd.getString("FMANUFACTOR"):"");
		installPd.put("EQM_INSTALL_ID", this.get32UUID());
		installPd.put("EQM_BASE_ID", pd.getString("EQM_BASE_ID"));
		installPd.put("FNAME", pd.containsKey("FNAME")?pd.getString("FNAME"):"");
		installPd.put("FSPECS", pd.containsKey("FSPECS")?pd.getString("FSPECS"):"");
		installPd.put("FMANUFACTOR", pd.containsKey("FMANUFACTOR")?pd.getString("FMANUFACTOR"):"");
		debugPd.put("EQM_DEBUG_ID", this.get32UUID());
		debugPd.put("EQM_BASE_ID", pd.getString("EQM_BASE_ID"));
		debugPd.put("FNAME", pd.containsKey("FNAME")?pd.getString("FNAME"):"");
		debugPd.put("FSPECS", pd.containsKey("FSPECS")?pd.getString("FSPECS"):"");
		debugPd.put("FMANUFACTOR", pd.containsKey("FMANUFACTOR")?pd.getString("FMANUFACTOR"):"");
		eqm_baseService.save(pd);
		eqm_base_parameterService.save(parameterPd);//保存设备基础参数设置，MDM_EQM_BASE_PARAMETER
		eqm_unboxcheckService.save(unboxcheckPd);//保存开箱验收单，MDM_EQM_UNBOXCHECK
		eqm_installService.save(installPd);//保存设备安装记录，MDM_EQM_INSTALL
		eqm_debugService.save(debugPd);//保存设备调试验收单，MDM_EQM_DEBUG
		List<PageData> list=new ArrayList<PageData>(); 
		list.add(pd);
//		FHLOG.save(Jurisdiction.getUsername(),"设备基础资料","MDM_EQM_BASE",
//				pd.getString("EQM_BASE_ID"),"新增："+list);
//		FHLOG.save(Jurisdiction.getUsername(),"设备基础参数设置","MDM_EQM_BASE_PARAMETER",
//				parameterPd.getString("EQM_BASE_PARAMETER_ID"),"添加设备基础资料时新增");
//		FHLOG.save(Jurisdiction.getUsername(),"设备开箱验收单","MDM_EQM_UNBOXCHECK",
//				unboxcheckPd.getString("AUTOCLAVE_CURING_ID"),"添加设备基础资料时新增");
//		FHLOG.save(Jurisdiction.getUsername(),"设备安装记录","MDM_EQM_INSTALL",
//				installPd.getString("EQM_INSTALL_ID"),"添加设备基础资料时新增");
//		FHLOG.save(Jurisdiction.getUsername(),"设备调试验收单","MDM_EQM_DEBUG",
//				debugPd.getString("EQM_DEBUG_ID"),"添加设备基础资料时新增");
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_base:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		
		try{
			eqm_baseService.delete(pd);
			eqm_base_parameterService.deleteBase(pd);//通过设备基础资料ID删除设备基础参数设置信息
			eqm_annexService.deleteBase(pd);//通过设备基础资料ID删除设备附件
			eqm_autoinfoService.deleteBase(pd);//通过设备基础资料ID删除设备自动化资料
			eqm_spare_partsService.deleteBase(pd);//通过设备基础资料ID删除设备备品备件
			eqm_specificationService.deleteBase(pd);//通过设备基础资料ID删除设备规范明细资料
			eqm_extendService.deleteBase(pd);//通过设备基础资料ID删除设备实体扩展
			eqm_recordService.deleteBase(pd);//通过设备基础资料ID删除设备档案
			eqm_ledgerService.deleteBase(pd);//通过设备基础资料ID删除设备台账
			eqm_verifyService.deleteBase(pd);//通过设备基础资料ID删除设备校验登记
			eqm_overhaulService.deleteBase(pd);//通过设备基础资料ID删除设备检修计划
			eqm_culture_planService.deleteBase(pd);//通过设备基础资料ID删除设备保修保养计划
			eqm_subsidiaryService.deleteBase(pd);//通过设备基础资料ID删除附属设备仪表
			eqm_vulnerable_partsService.deleteBase(pd);//通过设备基础资料ID删除设备易损件
			eqm_unboxcheckService.deleteBase(pd);//通过设备基础资料ID删除开箱验收单
			eqm_installService.deleteBase(pd);//通过设备基础资料ID删除设备安装记录
			eqm_debugService.deleteBase(pd);//通过设备基础资料ID删除设备调试验收单
		} catch(Exception e){
			errInfo = "error";
		}
//		FHLOG.save(Jurisdiction.getUsername(),"设备基础资料","MDM_EQM_BASE",
//				pd.getString("EQM_BASE_ID"),"删除");
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("eqm_base:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_baseService.edit(pd);
		List<PageData> list=new ArrayList<PageData>(); 
		list.add(pd);
//		FHLOG.save(Jurisdiction.getUsername(),"设备基础资料","MDM_EQM_BASE",
//				pd.getString("EQM_BASE_ID"),"修改："+list);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_base:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		String FTYPE = pd.getString("FTYPE");						//类型检索条件
		if(Tools.notEmpty(FTYPE))pd.put("FTYPE", FTYPE.trim());
		String FWORKCENTER = pd.getString("FWORKCENTER");						//工作中心检索条件
		if(Tools.notEmpty(FWORKCENTER))pd.put("FWORKCENTER", FWORKCENTER.trim());
		String RUN_STATE = pd.getString("RUN_STATE");						//运行状态检索条件
		if(Tools.notEmpty(RUN_STATE))pd.put("RUN_STATE", RUN_STATE.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_baseService.list(page);	//列出EQM_BASE列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list1")
	//@RequiresPermissions("eqm_base:list")
	@ResponseBody
	public Object list1(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String KEYWORDS1 = pd.getString("KEYWORDS1");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS1))pd.put("KEYWORDS1", KEYWORDS1.trim());
		String FTYPE = pd.getString("FTYPE");						//类型检索条件
		if(Tools.notEmpty(FTYPE))pd.put("FTYPE", FTYPE.trim());
		String FWORKCENTER = pd.getString("FWORKCENTER");						//工作中心检索条件
		if(Tools.notEmpty(FWORKCENTER))pd.put("FWORKCENTER", FWORKCENTER.trim());
		String RUN_STATE = pd.getString("RUN_STATE");						//运行状态检索条件
		if(Tools.notEmpty(RUN_STATE))pd.put("RUN_STATE", RUN_STATE.trim());
		page.setPd(pd);
		List<PageData>	varList = eqm_baseService.list(page);	//列出EQM_BASE列表
		JSONArray json = new JSONArray();
		for(int i=0;i<varList.size();i++){
			JSONObject jsonMainObject=new JSONObject();//主表
	    	JSONArray jsonMxArray=new JSONArray();//明细表
			PageData pd1 = varList.get(i);
			jsonMainObject
	    	.accumulate("id", pd1.get("FIDENTIFY"))//设备编码
	    	.accumulate("url", pd1.get("FWORKCENTER"))//图片路径
	    	.accumulate("name", pd1.get("FNAME"))//设备名称
	    	//.accumulate("FTYPE", pd1.get("FTYPE"))//设备类
	    	.accumulate("zhuti", pd1.get("SUBJECT_MATTER"))//主题
			.accumulate("zhutixie", "SUBJECT_MATTER_WRITE");//
	    	json.element(jsonMainObject);
			List<PageData> varListO = eqm_specificationService.listAll(pd1);
			for(int j=0;j<varListO.size();j++){
				JSONObject jsonMainMXObject=new JSONObject();//主表
				PageData pd2 = varListO.get(j);
				jsonMainMXObject
				.accumulate("shuiwen", pd2.get("FVALUES"))//列名
		    	.accumulate("names", pd2.get("KEY_NAME"))//参数名
		    	.accumulate("bianliangname", pd2.get("FDES"));//单位
				jsonMxArray=jsonMxArray.element(jsonMainMXObject);
			}
			jsonMainObject.accumulate("list",jsonMxArray);
		}
		String strData=json.toString().replaceAll("null","\"\"");
		MQTTConnect.pub("UPDATA/YL/1",strData);
		map.put("varList", strData);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("eqm_base:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String FTYPE = pd.getString("FTYPE");						//类型检索条件
		if(Tools.notEmpty(FTYPE))pd.put("FTYPE", FTYPE.trim());
		String FWORKCENTER = pd.getString("FWORKCENTER");						//工作中心检索条件
		if(Tools.notEmpty(FWORKCENTER))pd.put("FWORKCENTER", FWORKCENTER.trim());
		String RUN_STATE = pd.getString("RUN_STATE");						//运行状态检索条件
		if(Tools.notEmpty(RUN_STATE))pd.put("RUN_STATE", RUN_STATE.trim());
		
		List<PageData> varList = eqm_baseService.listAll(pd);	//列出EQM_BASE列表
//		FHLOG.save(Jurisdiction.getUsername(),"设备基础资料","MDM_EQM_BASE",
//				pd.getString("KEYWORDS"),"全部列表");
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	/**根据物料关键字查询数据
	* @param
	* @throws Exception
	*/
	@RequestMapping(value="/getBasic")
	//@RequiresPermissions("eqm_base:list")
	@ResponseBody
	public Object getBasic() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		String result = "success";
		PageData pd = new PageData();
		HttpServletRequest rt=this.getRequest();//从父类获取到request
		String keywork=rt.getParameter("query");
		if(keywork.equals(null)||keywork.equals("")){
			result="error";
			map.put("result", result);
			return map;
		}
		pd.put("inputName", keywork);
		List<PageData> varList=eqm_baseService.getBasic(pd);
		ObjectMapper mapper = new ObjectMapper();  
		String listName = mapper.writeValueAsString(varList); 
		map.put("listName", listName);
		map.put("result", result);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("eqm_base:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_baseService.findById(pd);	//根据ID读取
//		FHLOG.save(Jurisdiction.getUsername(),"设备基础资料","MDM_EQM_BASE",
//				pd.getString("EQM_BASE_ID"),"去修改页面获取数据");
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	/**通过设备标识获取数据
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/findByNumber")
	//@RequiresPermissions("eqm_base:edit")
	@ResponseBody
	public Object findByNumber() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_baseService.findByNumber(pd);	//根据ID读取
//		FHLOG.save(Jurisdiction.getUsername(),"设备基础资料","MDM_EQM_BASE",
//				pd.getString("FNUMBER"),"通过设备标识获取数据");
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("eqm_base:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			eqm_baseService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
//		FHLOG.save(Jurisdiction.getUsername(),"设备基础资料","MDM_EQM_BASE",
//				pd.getString("DATA_IDS"),"批量删除");
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**
	 *	生成二维码
	 * @param args
	 * @throws Exception
	 */
//	@RequestMapping(value="/createTwoDimensionCode")
//	@ResponseBody
//	public Object createTwoDimensionCode() throws Exception{
//		Map<String,String> map = new HashMap<String,String>();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		if(null!=pd.getString("EQM_BASE_ID")&&!"".equals(pd.getString("EQM_BASE_ID"))) {
//			pd=eqm_baseService.findById(pd);	//根据ID读取
//		}
//		String errInfo = "success", 
//		encoderImgId = this.get32UUID()+".png"; //encoderImgId此处二维码的图片名
//		String encoderContent = pd.getString("FIDENTIFY");				//内容
//		if(null == encoderContent){
//			errInfo = "error";
//		}else{
//			try {
//				String filePath = PathUtil.getProjectpath() + "uploadFiles/twoDimensionCode/" + encoderImgId;  //存放路径
//				TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");							//执行生成二维码
//			} catch (Exception e) {
//				errInfo = "error";
//			}
//		}
//		map.put("result", errInfo);						//返回结果
//		map.put("encoderImgId", encoderImgId);			//二维码图片名
//		return map;
//	}
	
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
		titles.add("标识");		//1
		titles.add("名称");		//2
		titles.add("描述");		//3
		titles.add("类型");		//4
		titles.add("工作中心");	//5
		titles.add("运行状态");	//6
		titles.add("品牌");		//7
		titles.add("规格型号");	//8
		titles.add("条码批号");	//9
		titles.add("厂家");		//10
		titles.add("创建人");		//11
		titles.add("创建时间");	//12
		titles.add("设备类别");	//13
		titles.add("最大产量");	//14
		titles.add("最小产量");	//15
		titles.add("单位");		//16
		titles.add("设备用途");	//17
		titles.add("外形尺寸");	//18
		titles.add("制造日期");	//19
		titles.add("出厂编号");	//20
		titles.add("使用部门");	//21
		titles.add("原值");		//22
		titles.add("出厂日期");	//23
		titles.add("设备重量");	//24
		titles.add("设备状况");	//25
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_baseService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FIDENTIFY"));	    	//1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    		//2
			vpd.put("var3", varOList.get(i).getString("FDES"));	    			//3
			vpd.put("var4", varOList.get(i).getString("FTYPE"));	    		//4
			vpd.put("var5", varOList.get(i).getString("WORKCENTER_NAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("RUN_STATE"));	    	//6
			vpd.put("var7", varOList.get(i).getString("FBRAND"));	    		//7
			vpd.put("var8", varOList.get(i).getString("FSPECS"));	    		//8
			vpd.put("var9", varOList.get(i).getString("BARCODE_BATCH"));	    //9
			vpd.put("var10", varOList.get(i).getString("FMANUFACTOR"));	    	//10
			vpd.put("var11", varOList.get(i).getString("FCREATOR"));	    	//11
			vpd.put("var12", varOList.get(i).getString("CREATE_TIME"));	    	//12
			vpd.put("var13", varOList.get(i).getString("CLASS_NAME"));	    	//13
			vpd.put("var14", varOList.get(i).get("FMAXYIEID").toString());	    	//14
			vpd.put("var15", varOList.get(i).get("FMINIYIEID").toString());	    	//15
			vpd.put("var16", varOList.get(i).getString("FUNIT"));	    		//16
			vpd.put("var17", varOList.get(i).getString("FPURPOSE"));	    	//17
			vpd.put("var18", varOList.get(i).getString("OVERALL_DIMENSION"));	//18
			vpd.put("var19", varOList.get(i).getString("MANUFACTURE_DATE"));	//19
			vpd.put("var20", varOList.get(i).getString("MANUFACTURE_NO"));	    //20
			vpd.put("var21", varOList.get(i).getString("DEPARTMENT_NAME"));	    //21
			vpd.put("var22", varOList.get(i).getString("ORIGINAL_VALUE"));	    //22
			vpd.put("var23", varOList.get(i).getString("EX_FACTORY_DATE"));	    //23
			vpd.put("var24", varOList.get(i).get("WEIGHT").toString());	    		//24
			vpd.put("var25", varOList.get(i).getString("STATUS_OF_EQUIPMENT"));	//25
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**检修待办列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/JXList")
	//@RequiresPermissions("eqm_base:list")
	@ResponseBody
	public Object JXList(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String FTYPE = pd.getString("FTYPE");						//类型检索条件
		if(Tools.notEmpty(FTYPE))pd.put("FTYPE", FTYPE.trim());
		String FWORKCENTER = pd.getString("FWORKCENTER");						//工作中心检索条件
		if(Tools.notEmpty(FWORKCENTER))pd.put("FWORKCENTER", FWORKCENTER.trim());
		String RUN_STATE = pd.getString("RUN_STATE");						//运行状态检索条件
		if(Tools.notEmpty(RUN_STATE))pd.put("RUN_STATE", RUN_STATE.trim());
		pd.put("USERNAME", Jurisdiction.getName());//当前登录人
		try {
			pd.put("DEPTNAME", staffService.getDEPTNAME(pd).getString("DNAME")!=null?staffService.getDEPTNAME(pd).getString("DNAME"):"");//当前登录人部门
		} catch (Exception e) {
			// TODO: handle exception
			pd.put("DEPTNAME", "");//当前登录人部门
		}
		page.setPd(pd);
		List<PageData>	varList = eqm_baseService.listJX(page);	//列出EQM_BASE列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	/**获取设备列表-可搜索-前100条
	 * @author 管悦
	 * @date 2021-01-15
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getEQMList")
	@ResponseBody
	public Object getEQMList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = eqm_baseService.getEQMList(pd);	//
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
