package org.yy.controller.km;

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
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.LeaveApplicationService;
import org.yy.service.system.UsersService;

/** 
 * 说明：请假申请
 * 作者：YuanYes QQ356703572
 * 时间：2021-03-17
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/LeaveApplication")
public class LeaveApplicationController extends BaseController {
	
	@Autowired
	private LeaveApplicationService LeaveApplicationService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
    private UsersService usersService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("LeaveApplication:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("LeaveApplication_ID", this.get32UUID());	//主键
		pd.put("FNAME", Jurisdiction.getName());
		String staffID=staffService.getStaffId(pd).getString("STAFF_ID");
		pd.put("FCreatePerson", staffID);// 查询职员ID
		pd.put("FCreateTime", Tools.date2Str(new Date()));
		pd.put("RUN_STATUS", "创建");
		LeaveApplicationService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("LeaveApplication:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		LeaveApplicationService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("LeaveApplication:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		LeaveApplicationService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("LeaveApplication:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = LeaveApplicationService.list(page);	//列出LeaveApplication列表
		for(int i=0;i<varList.size();i++) {
			String FCopyMan=varList.get(i).getString("FCopyMan");
			if(FCopyMan != null && !"".equals(FCopyMan)) {
				PageData pdCopy=LeaveApplicationService.getCopyName(varList.get(i));
					if(pdCopy != null) {
						varList.get(i).put("FCopyName",pdCopy.getString("FCopyName"));
					}
			}
		}
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
	//@RequiresPermissions("LeaveApplication:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = LeaveApplicationService.findById(pd);	//根据ID读取
		String FCopyMan=pd.getString("FCopyMan");
		if(FCopyMan != null && !"".equals(FCopyMan)) {
			PageData pdCopy=LeaveApplicationService.getCopyName(pd);
				if(pdCopy != null) {
					pd.put("FCopyName",pdCopy.getString("FCopyName"));
				}
		}
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("LeaveApplication:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			LeaveApplicationService.deleteAll(ArrayDATA_IDS);
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
		titles.add("请假类型");	//1
		titles.add("单号");	//2
		titles.add("开始时间");	//3
		titles.add("结束时间");	//4
		titles.add("时长");	//5
		titles.add("请假事由");	//6
		titles.add("工作交接情况");	//7
		titles.add("备注");	//8
		titles.add("创建人");	//9
		titles.add("创建时间");	//10
		titles.add("状态");	//11
		titles.add("审核进度");	//12
		titles.add("提交审批时间");	//13
		titles.add("提交审批人");	//14
		titles.add("流程ID");	//15
		titles.add("抄送人");	//16
		dataMap.put("titles", titles);
		List<PageData> varOList = LeaveApplicationService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FType"));	    //1
			vpd.put("var2", varOList.get(i).getString("Fbillno"));	    //2
			vpd.put("var3", varOList.get(i).getString("FStartTime"));	    //3
			vpd.put("var4", varOList.get(i).getString("FEndTime"));	    //4
			vpd.put("var5", varOList.get(i).get("FHour").toString());	//5
			vpd.put("var6", varOList.get(i).getString("FReason"));	    //6
			vpd.put("var7", varOList.get(i).getString("FTransferCase"));	    //7
			vpd.put("var8", varOList.get(i).getString("FNOTE"));	    //8
			vpd.put("var9", varOList.get(i).getString("FCreatePerson"));	    //9
			vpd.put("var10", varOList.get(i).getString("FCreateTime"));	    //10
			vpd.put("var11", varOList.get(i).getString("RUN_STATUS"));	    //11
			vpd.put("var12", varOList.get(i).getString("GENERATE_PROGRESS"));	    //12
			vpd.put("var13", varOList.get(i).getString("FPostTime"));	    //13
			vpd.put("var14", varOList.get(i).getString("FPostMan"));	    //14
			vpd.put("var15", varOList.get(i).getString("PROC_INST_ID_"));	    //15
			vpd.put("var16", varOList.get(i).getString("FCopyMan"));	    //16
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**获得启动审核准备信息
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/setStarts")
	@ResponseBody
	public Object setStarts() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_ID());
		PageData pdBack = new PageData();
		PageData pdDept=departmentService.findById(pd);
		pdBack.put("SrartKeyId", "KEY_LeaveApplication");
		String headmans=pdDept.get("HEADMAN")==null?"":pdDept.getString("HEADMAN");
		String strs[]=headmans.split("、");
		for(int j=0;j<strs.length;j++) {				
			pd.put("NAME", strs[j]);
			PageData pdUser = usersService.findUser(pd);//根据姓名查用户表
			if(pdUser != null) {
				pdBack.put("ProjectGL"+(j+1), pdUser.get("USERNAME"));
			}
		}
		if(strs.length >0) {
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FPostMan", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
			pd.put("FPostTime", Tools.date2Str(new Date()));
			pd.put("RUN_STATUS", "审批中");
			LeaveApplicationService.editAudit(pd);
		}else {
			errInfo="fail1";
		}
		map.put("pd", pdBack);
		map.put("result", errInfo);
		return map;
	}	
}
