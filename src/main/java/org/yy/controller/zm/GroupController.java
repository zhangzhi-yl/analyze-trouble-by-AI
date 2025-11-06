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
import org.yy.util.DateUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.zm.GroupService;

/** 
 * 说明：分组管理
 * 作者：YuanYes QQ356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/group")
public class GroupController extends BaseController {
	
	@Autowired
	private GroupService groupService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("group:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("GROUP_ID", this.get32UUID());	//主键
		pd.put("Creator", Jurisdiction.getName());
		pd.put("CreateTime",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		pd.put("FStatus","0");
		groupService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("group:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		groupService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("group:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("Modified", Jurisdiction.getName());
		pd.put("ModifiedTime",Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		groupService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**校验名称重复
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/checkName")
	@ResponseBody
	public Object checkName() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FName = pd.getString("FName");						//关键词检索条件
		if(Tools.notEmpty(FName))pd.put("FName", FName.trim());
		List<PageData> eqList = groupService.listAll(pd);
		if(eqList.size() > 0){
			errInfo = "201";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}

	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editStatus")
	//@RequiresPermissions("group:edit")
	@ResponseBody
	public Object editStatus() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		groupService.editStatus(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("group:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = groupService.list(page);	//列出Group列表
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
	//@RequiresPermissions("group:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = groupService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("group:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			groupService.deleteAll(ArrayDATA_IDS);
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
		titles.add("名称");	//1
		titles.add("编码");	//2
		titles.add("车间");	//3
		titles.add("状态");	//4
		titles.add("备注");	//5
		titles.add("创建人");	//6
		titles.add("创建时间");	//7
		titles.add("修改人");	//8
		titles.add("最后修改时间");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = groupService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			String status= "";
			String FStatus = varOList.get(i).getString("FStatus");
			if("0".equals(FStatus)){
				status = "关闭";
			}else if("1".equals(FStatus)) {
				status = "开启";
			}
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FName"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCode"));	    //2
			vpd.put("var3", varOList.get(i).getString("AreaName"));	    //3
			vpd.put("var4", status);	    //4
			vpd.put("var5", varOList.get(i).getString("Remarks"));	    //5
			vpd.put("var6", varOList.get(i).getString("Creator"));	    //6
			vpd.put("var7", varOList.get(i).getString("CreateTime"));	    //7
			vpd.put("var8", varOList.get(i).getString("Modified"));	    //8
			vpd.put("var9", varOList.get(i).getString("ModifiedTime"));	    //9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
