package org.yy.controller.trouble;

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
import org.yy.service.trouble.TroubleLocalDefineService;
import org.yy.util.DateUtil;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.trouble.TroubleService;

/** 
 * 说明：隐患管理
 * 作者：YuanYes QQ356703572
 * 时间：2025-09-24
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/trouble")
public class TroubleController extends BaseController {
	
	@Autowired
	private TroubleService troubleService;
	@Autowired
	private TroubleLocalDefineService troublelocaldefineService;
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
		pd.put("TROUBLE_ID", this.get32UUID());	//主键
		troubleService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/testTrouble")
	@ResponseBody
	public Object testTrouble() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		troubleService.pushAlarmMsg();
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
		troubleService.delete(pd);
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
		troubleService.edit(pd);
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
		List<PageData>	varList = troubleService.list(page);	//列出Trouble列表
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
		pd = troubleService.findById(pd);	//根据ID读取
		pd.put("LOCAL_ID",pd.get("LOCAL_ID"));
		List<PageData> defines = troublelocaldefineService.listAll(pd);
		String[] define = new String[defines.size()];
		for(int i = 0; i < define.length; i++){
			define[i] = defines.get(i).get("DEFINE_NAME").toString();
		}
		pd.put("define", define);
		map.put("pd", pd);
		map.put("result", errInfo);
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
			troubleService.deleteAll(ArrayDATA_IDS);
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
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("隐患定义ID");	//1
		titles.add("隐患位置ID");	//2
		titles.add("图片路径");	//3
		titles.add("图片上传方式");	//4
		titles.add("状态");	//5
		titles.add("告警等级");	//6
		titles.add("分析开始时间");	//7
		titles.add("分析结束时间");	//8
		titles.add("结果描述");	//9
		titles.add("分析隐患后果");	//10
		titles.add("处置建议");	//11
		titles.add("结果状态");	//12
		titles.add("分析结果");	//13
		titles.add("消息推送");	//14
		titles.add("创建人");	//15
		titles.add("创建时间");	//16
		titles.add("更新人");	//17
		titles.add("更新时间");	//18
		titles.add("备注");	//19
		titles.add("分析定时");	//20
		titles.add("告警定时");	//21
		titles.add("告警时间");	//22
		titles.add("隐患定义名称");	//23
		titles.add("是否误报");	//24
		dataMap.put("titles", titles);
		List<PageData> varOList = troubleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("DEFINE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("LOCAL_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("IMG_URL"));	    //3
			vpd.put("var4", varOList.get(i).getString("IMG_UPLOAD_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("STATUS"));	    //5
			vpd.put("var6", varOList.get(i).getString("LEVEL"));	    //6
			vpd.put("var7", varOList.get(i).getString("ANALYSIS_START_TIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("ANALYSIS_END_TIME"));	    //8
			vpd.put("var9", varOList.get(i).getString("ANALYSIS_DESCRIB"));	    //9
			vpd.put("var10", varOList.get(i).getString("ANALYSIS_RESULT"));	    //10
			vpd.put("var11", varOList.get(i).getString("SUGGESTION"));	    //11
			vpd.put("var12", varOList.get(i).getString("RESULT_STATUS"));	    //12
			vpd.put("var13", varOList.get(i).getString("ANALYSIS_TEXT"));	    //13
			vpd.put("var14", varOList.get(i).getString("PUSH_MSG"));	    //14
			vpd.put("var15", varOList.get(i).getString("CREATE_BY"));	    //15
			vpd.put("var16", varOList.get(i).getString("CREATE_TIME"));	    //16
			vpd.put("var17", varOList.get(i).getString("UPDATE_BY"));	    //17
			vpd.put("var18", varOList.get(i).getString("UPDATE_TIME"));	    //18
			vpd.put("var19", varOList.get(i).getString("REMARK"));	    //19
			vpd.put("var20", varOList.get(i).getString("ANALYSIS_INTERVAL"));	    //20
			vpd.put("var21", varOList.get(i).getString("PUSH_INTERVAL"));	    //21
			vpd.put("var22", varOList.get(i).getString("REPORT_TIME"));	    //22
			vpd.put("var23", varOList.get(i).getString("DEFINE_NAMES"));	    //23
			vpd.put("var24", varOList.get(i).getString("IS_ERROR"));	    //24
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
