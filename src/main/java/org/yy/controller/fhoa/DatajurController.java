package org.yy.controller.fhoa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;

import org.yy.controller.base.BaseController;
import org.yy.entity.PageData;
import org.yy.service.fhoa.DatajurService;
import org.yy.service.fhoa.DepartmentService;
import org.yy.util.Jurisdiction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 说明：组织数据权限表
 * 作者：YuanYe Q356703572
 * 官网：www.ylmom2s.com
 */
@Controller
@RequestMapping("/datajur")
public class DatajurController extends BaseController {
	
	@Autowired
	private DatajurService datajurService;
	@Autowired
	private DepartmentService departmentService;
	
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
		pd.put("DEPARTMENT_IDS", departmentService.getDEPARTMENT_IDS(pd.getString("DEPARTMENT_ID")));		//部门ID集
		datajurService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		String ZDEPARTMENT_ID = Jurisdiction.getDEPARTMENT_ID();
		ZDEPARTMENT_ID = "".equals(ZDEPARTMENT_ID)?"0":ZDEPARTMENT_ID;
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect(ZDEPARTMENT_ID,zdepartmentPdList));
		map.put("zTreeNodes", (null == arr ?"":"{\"treeNodes\":" + arr.toString() + "}"));
		pd = datajurService.findById(pd);	//根据ID读取
		pd = departmentService.findById(pd);//读取部门数据(用部门名称)
		map.put("pd", pd);
		map.put("result", errInfo);			//返回结果
		return map;
	}	
	
}
