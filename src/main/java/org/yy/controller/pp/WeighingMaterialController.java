package org.yy.controller.pp;

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
import org.yy.service.fhoa.StaffService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.WeighingMaterialService;

/** 
 * 说明：称量任务物料明细
 * 作者：YuanYes QQ356703572
 * 时间：2021-01-30
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/WeighingMaterial")
public class WeighingMaterialController extends BaseController {
	
	@Autowired
	private WeighingMaterialService WeighingMaterialService;
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("WeighingMaterial:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		pd = this.getPageData();
		pd.put("WeighingMaterial_ID", this.get32UUID());	//主键
		pd.put("WeighingTime", Tools.date2Str(new Date()));	//称量时间
		pd.put("WeighingPersonnel", staffid);	//称量人员员工id
		WeighingMaterialService.save(pd);
		operationrecordService.add("","称量任务物料明细","添加","",staffid,"称量任务物料明细添加");
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("WeighingMaterial:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		WeighingMaterialService.delete(pd);
		operationrecordService.add("","称量任务物料明细","删除","",staffid,"称量任务物料明细删除");
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("WeighingMaterial:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		staff=staffService.getStaffId(staff);
		String staffid=null==staff?"":staff.getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		WeighingMaterialService.edit(pd);
		operationrecordService.add("","称量任务物料明细","修改","",staffid,"称量任务物料明细修改");
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("WeighingMaterial:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = WeighingMaterialService.list(page);	//列出WeighingMaterial列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**全部列表
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("WeighingMaterial:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = WeighingMaterialService.listAll(pd);	//列出WeighingMaterial列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("WeighingMaterial:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = WeighingMaterialService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("WeighingMaterial:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			WeighingMaterialService.deleteAll(ArrayDATA_IDS);
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
		//titles.add("计划工单ID");	//1
		titles.add("工单编码");		//2
		titles.add("锅次");			//3
		titles.add("工序");			//4
		titles.add("物料名称");		//5
		titles.add("净重");			//6
		titles.add("皮重");			//7
		titles.add("单位");			//8
		titles.add("称量时间");		//9
		titles.add("称量人员");		//10
		dataMap.put("titles", titles);
		List<PageData> varOList = WeighingMaterialService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			//vpd.put("var1", varOList.get(i).getString("WorkorderProcessIOExampleID"));	//1
			vpd.put("var1", varOList.get(i).getString("WorkOrderNum"));	    				//1
			vpd.put("var2", varOList.get(i).getString("WokNum"));	    					//2
			vpd.put("var3", varOList.get(i).getString("WP"));	    						//3
			vpd.put("var4", varOList.get(i).getString("MaterialName"));	    				//4
			vpd.put("var5", varOList.get(i).get("NetWeight").toString());					//5
			vpd.put("var6", varOList.get(i).get("Ftare").toString());						//6
			vpd.put("var7", varOList.get(i).getString("FUnit"));	    					//7
			vpd.put("var8", varOList.get(i).getString("WeighingTime"));	    				//8
			vpd.put("var9", varOList.get(i).getString("WeighingPersonnel"));	    		//9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
