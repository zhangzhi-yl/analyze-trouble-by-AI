package org.yy.controller.wt;

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
import org.yy.service.wt.TypelogsService;
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.wt.QuestionbanksService;

/** 
 * 说明：问题库
 * 作者：YuanYes QQ356703572
 * 时间：2021-09-03
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/questionbanks")
public class QuestionbanksController extends BaseController {
	
	@Autowired
	private QuestionbanksService questionbanksService;
	@Autowired
	private TypelogsService typelogsService;

	
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
		PageData pd1 = new PageData();
		PageData pd2 = new PageData();
		pd = this.getPageData();
		pd.put("QUESTIONBANK_ID", this.get32UUID());   //主键
		pd1=questionbanksService.getMax();

//		System.out.println(pd1.getString("QUESTION_NUMBER")+"111");
		if(pd1!=null){
			Integer QUESTION_NUMBER1=Integer.parseInt(pd1.getString("QUESTION_NUMBER"))+1;
			String QUESTION_NUMBER=String.valueOf(QUESTION_NUMBER1);
			String zero="00000000";
			String QUESTION_NUMBER2=zero+QUESTION_NUMBER;
			pd.put("QUESTION_NUMBER",QUESTION_NUMBER2.substring(QUESTION_NUMBER2.length()-9));}
		else{
			String QUESTION_NUMBER="000000001";
			pd.put("QUESTION_NUMBER",QUESTION_NUMBER);
		}
		pd.put("STATE","创建");
		pd.put("PROPOSER", Jurisdiction.getName());
		pd.put("RELEASE_TIME",Tools.date2Str(new Date()));
		questionbanksService.save(pd);
		pd2.put("TYPELOG_ID", this.get32UUID());
		pd2.put("QUESTIONBANK_ID", pd.getString("QUESTIONBANK_ID"));
		pd2.put("OPERATOR", Jurisdiction.getName());
		pd2.put("OPERATION_TIME",Tools.date2Str(new Date()));
		pd2.put("CURRENT_STATE","创建");
		typelogsService.save(pd2);
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
		questionbanksService.delete(pd);
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
		questionbanksService.edit(pd);
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

		pd.put("login",Jurisdiction.getName());
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = questionbanksService.list(page);	//列出Questionbanks列表
		map.put("varList", varList);
		map.put("page", page);


		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/list1")
	@ResponseBody
	public Object list1(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = questionbanksService.list1(page);	//列出Questionbanks列表
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
		pd = questionbanksService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping(value="/receiveEdit")
	@ResponseBody
	public Object receiveEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pd1 = new PageData();
		pd = this.getPageData();
		questionbanksService.receiveEdit(pd);
		pd1.put("TYPELOG_ID", this.get32UUID());
		pd1.put("QUESTIONBANK_ID", pd.getString("QUESTIONBANK_ID"));
		pd1.put("OPERATOR", Jurisdiction.getName());
		pd1.put("OPERATION_TIME",Tools.date2Str(new Date()));
		pd1.put("CURRENT_STATE","处理中");
		typelogsService.save(pd1);
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value="/CmpleteEdit")
	@ResponseBody
	public Object CmpleteEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData pd1 = new PageData();
		pd = this.getPageData();
		questionbanksService.CmpleteEdit(pd);
		pd1.put("TYPELOG_ID", this.get32UUID());
		pd1.put("QUESTIONBANK_ID", pd.getString("QUESTIONBANK_ID"));
		pd1.put("OPERATOR", Jurisdiction.getName());
		pd1.put("OPERATION_TIME",Tools.date2Str(new Date()));
		pd1.put("CURRENT_STATE","处理完成");
		typelogsService.save(pd1);
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
			questionbanksService.deleteAll(ArrayDATA_IDS);
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
		titles.add("问题编号");	//1
		titles.add("问题标题");	//2
		titles.add("影响范围");	//3
		titles.add("问题描述");	//4
		titles.add("问题来源");	//5
		titles.add("责任判定");	//6
		titles.add("严重度");	//7
		titles.add("责任人");	//8
		titles.add("提出人");	//9
		titles.add("发布时间");	//10
		titles.add("状态");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = questionbanksService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("QUESTION_NUMBER").toString());	//1
			vpd.put("var2", varOList.get(i).getString("QUESTION_TITLE"));	    //2
			vpd.put("var3", varOList.get(i).getString("SCOPE_OF_INFLUENCE"));	    //3
			vpd.put("var4", varOList.get(i).getString("PROBLEM_DESCRIPTION"));	    //4
			vpd.put("var5", varOList.get(i).getString("PROBLEM_SOURCE"));	    //5
			vpd.put("var6", varOList.get(i).getString("RESPONSIBILITY_JUDGMENT"));	    //6
			vpd.put("var7", varOList.get(i).getString("SEVERITY"));	    //7
			vpd.put("var8", varOList.get(i).getString("PERSON_LIABLE"));	    //8
			vpd.put("var9", varOList.get(i).getString("PROPOSER"));	    //9
			vpd.put("var10", varOList.get(i).getString("RELEASE_TIME"));	    //10
			vpd.put("var11", varOList.get(i).getString("STATE"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
