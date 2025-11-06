package org.yy.controller.mom;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.yy.entity.PageData;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.mom.TeamStaffService;

/** 
 * 说明：班组人员管理
 * 作者：YuanYe
 * 时间：2020-01-14
 * 
 */
@Controller
@RequestMapping("/teamstaff")
public class TeamStaffController extends BaseController {
	
	@Autowired
	private TeamStaffService teamstaffService;
	
	@Autowired
	private DepartmentService departmentService;
	
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
		pd.put("TEAM_STAFF_ID", this.get32UUID());	//班组人员ID
		teamstaffService.save(pd);
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
		teamstaffService.delete(pd);
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
		teamstaffService.edit(pd);
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
		List<PageData>	varList = teamstaffService.list(page);	//列出TeamStaff列表
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
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		String ZDEPARTMENT_ID = Jurisdiction.getDEPARTMENT_ID();
		ZDEPARTMENT_ID = "".equals(ZDEPARTMENT_ID)?"0":ZDEPARTMENT_ID;
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect(ZDEPARTMENT_ID,zdepartmentPdList));
		map.put("zTreeNodes", (null == arr ?"":"{\"treeNodes\":" + arr.toString() + "}"));
		pd = teamstaffService.findById(pd);	//根据ID读取
		PageData dpd = new PageData();
		dpd = departmentService.findById(pd);
		String depname = null == dpd?"请选择":dpd.getString("NAME");
		map.put("depname", null == depname?"请选择":depname);
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	/**
	 * 绑定组员
	 * @return 绑定组员
	 * @throws Exception
	 */
	@RequestMapping(value="/staffBinding")
	@ResponseBody
	public Object staffBinding() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String staffArray = pd.getString("DATA_IDS");
		if(Tools.notEmpty(staffArray)){
			JSONArray array = JSONArray.fromObject(staffArray); //首先把字符串转成 JSONArray  对象
			for(int i=0;i<array.size();i++) {
				JSONObject json = array.getJSONObject(i);
				json.put("TEAM_STAFF_ID", this.get32UUID());	//为数据设置主键
			}
			ArrayList<PageData> list = (ArrayList<PageData>) JSONArray.toCollection(array, PageData.class);//jsonarray转成list
			teamstaffService.staffBinding(list);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
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
			teamstaffService.deleteAll(ArrayDATA_IDS);
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
		titles.add("岗位");	//2
		titles.add("部门");	//3
		titles.add("所属车间");	//4
		titles.add("联系电话");	//5
		titles.add("组长");	//6
		titles.add("组员");	//7
		titles.add("人员名称");	//8
		titles.add("人员编码");	//9
		titles.add("描述");	//10
		titles.add("所属班组");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = teamstaffService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("STATION"));	    //2
			vpd.put("var2", varOList.get(i).getString("DEPT"));	    //3
			vpd.put("var3", varOList.get(i).getString("AREA_ID"));	    //4
			vpd.put("var4", varOList.get(i).getString("FPHONE"));	    //5
			vpd.put("var5", varOList.get(i).getString("TEAM_LEADER"));	    //6
			vpd.put("var6", varOList.get(i).getString("TEAM_STAFF"));	    //7
			vpd.put("var7", varOList.get(i).getString("FNAME"));	    //8
			vpd.put("var8", varOList.get(i).getString("FCODE"));	    //9
			vpd.put("var9", varOList.get(i).getString("FDES"));	    //10
			vpd.put("var10", varOList.get(i).getString("TEAM_INFO_ID"));	    //11
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

}
