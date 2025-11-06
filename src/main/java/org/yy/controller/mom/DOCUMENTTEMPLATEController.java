package org.yy.controller.mom;

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
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.yy.entity.PageData;
import org.yy.service.mom.DOCUMENTTEMPLATEService;
import org.yy.service.mom.DOCUMENTTEMPLATEMxService;

/** 
 * 说明：检验单主模板
 * 作者：YuanYe
 * 时间：2020-02-21
 * 
 */
@Controller
@RequestMapping("/documenttemplate")
public class DOCUMENTTEMPLATEController extends BaseController {
	
	@Autowired
	private DOCUMENTTEMPLATEService documenttemplateService;
	
	@Autowired
	private DOCUMENTTEMPLATEMxService documenttemplatemxService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("documenttemplate:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DOCUMENTTEMPLATE_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
 		documenttemplateService.save(pd);
		pd = documenttemplateService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("documenttemplate:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(Integer.parseInt(documenttemplatemxService.findCount(pd).get("zs").toString()) > 0){
				errInfo = "error";
			}else{
				documenttemplateService.delete(pd);
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
	//@RequiresPermissions("documenttemplate:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("VERSION", Double.parseDouble(pd.getString("VERSION").toString())+0.1);
		documenttemplateService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("documenttemplate:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String BELONGTYPE = pd.getString("BELONGTYPE");						//类型检索条件
		if(Tools.notEmpty(BELONGTYPE))pd.put("BELONGTYPE", BELONGTYPE.trim());
		page.setPd(pd);
		List<PageData> varList = documenttemplateService.list(page);	//列出DOCUMENTTEMPLATE列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去预览页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goSee")
		//@RequiresPermissions("documenttemplate:edit")
		@ResponseBody
		public Object goSee()throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData pdID = new PageData();
			pd = this.getPageData();
			pdID.put("DOCUMENTTEMPLATE_ID", pd.getString("DOCUMENTTEMPLATE_ID"));//主键ID
			pd = documenttemplateService.findById(pd);	//根据ID读取
			List<PageData> varList=documenttemplatemxService.listMxAll(pdID);
			map.put("pd", pd);
			map.put("varList", varList);
			map.put("result", errInfo);						//返回结果
			return map;
		}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("documenttemplate:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = documenttemplateService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	/**发布
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/issue")
	@ResponseBody
	public Object issue() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		if(null!=pd.getString("STATE")&&!"".equals(pd.getString("STATE"))) {//判断参数是否为空
			if("YES".equals(pd.getString("STATE"))) {//判断是否是发布
				pd.put("FRUN_STATE", "已发布");
			}else {//取消发布
				pd.put("FRUN_STATE", "未发布");
			}
		}
		documenttemplateService.editIssue(pd);//修改发布状态
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**模板名称根据工单关键字查询数据
	* @param
	* @throws Exception
	*/
	@RequestMapping(value="/getTemplate")
	@ResponseBody
	public Object getTemplate() throws Exception {
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
		List<PageData> varList=documenttemplateService.getTemplate(pd);
		ObjectMapper mapper = new ObjectMapper();  
		String listName = mapper.writeValueAsString(varList); 
		map.put("listName", listName);
		map.put("result", result);
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
		titles.add("模板名称");	//1
		titles.add("版本");	//2
		titles.add("日期");	//3
		titles.add("所属类别");	//4
		titles.add("模板是否在用");	//5
		titles.add("创建人");	//6
		titles.add("创建时间");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = documenttemplateService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("VERSION"));	    //2
			vpd.put("var3", varOList.get(i).getString("DAYTIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("BELONG_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("TEMPLATE_STATUS"));	    //5
			vpd.put("var6", varOList.get(i).getString("FCREATOR"));	    //6
			vpd.put("var7", varOList.get(i).getString("CREATE_TIME"));	    //7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
