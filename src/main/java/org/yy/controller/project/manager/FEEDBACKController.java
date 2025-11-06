package org.yy.controller.project.manager;

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
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.project.manager.FEEDBACKService;

/** 
 * 说明：柜体信息反馈
 * 作者：YuanYes QQ356703572
 * 时间：2021-07-09
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/feedback")
public class FEEDBACKController extends BaseController {
	
	@Autowired
	private FEEDBACKService feedbackService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private StaffService StaffService;

	public String [] testRepeat(String [] arrStr) throws Exception{
		List<String> list = new ArrayList<>();
		for (int i=0; i<arrStr.length; i++) {
			if(!list.contains(arrStr[i])) {
				list.add(arrStr[i]);
			}
		}
		//返回一个包含所有对象的指定类型的数组
		String[] newArrStr =  list.toArray(new String[1]);
	    //System.out.println(Arrays.toString(newArrStr));
		return newArrStr;
	}
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("feedback:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FTYPE=pd.getString("FTYPE");
		PageData staffParam = new PageData();
		staffParam.put("FNAME", Jurisdiction.getName());
		PageData staffInfo = StaffService.getStaffId(staffParam);
		if(staffInfo != null) {
			pd.put("DNAME", staffInfo.getString("DNAME"));
		}
		if(FTYPE!=null && FTYPE.equals("生产")) {
			PageData pdMsg = feedbackService.getMsg(pd);	
			if(pdMsg !=null) {
				pd.put("FPROJECT_ID", pdMsg.getString("PROJECT_ID"));
				pd.put("FCABINENT_ID", pdMsg.getString("Cabinet_Assembly_Detail_ID"));
				pd.put("FRELATED_ID", pd.getString("PlanningWorkOrder_ID"));
				pd.put("AccessURL", "../../../views/projectManager/pro_plan/pro_plan_jssj.html?Cabinet_Assembly_Detail_ID="+pdMsg.getString("Cabinet_Assembly_Detail_ID"));// 
				String FBATCH="GTFK"+Tools.date2Str(new Date(),"yyyyMMddhhmmss");
				pd.put("FCREATOR", Jurisdiction.getName());
				pd.put("FCTIME", Tools.date2Str(new Date()));
				pd.put("FBATCH", FBATCH);//批次号
				String FRECEIVEMANS=pdMsg.getString("FRECEIVEMANS");
				if(FRECEIVEMANS!=null && !"".equals(FRECEIVEMANS)) {
					String ArrayDATA_IDS[] =FRECEIVEMANS.split(",");
					String ArrayDATA_IDSX[] =testRepeat(ArrayDATA_IDS);
					for(String FRECEIVEMAN : ArrayDATA_IDSX) {
						if(!FRECEIVEMAN.equals("")) {
							// 跳转页面
							pd.put("FRECEIVEMAN", FRECEIVEMAN); 
							pd.put("FEEDBACK_ID", this.get32UUID());	//主键
							feedbackService.save(pd);
						}
					}
				}
			}else {
				errInfo = "fail";
				map.put("result", errInfo);
				return map;
			}
		}else if(FTYPE!=null && FTYPE.equals("采购")) {
			PageData pdMsg = feedbackService.getMsg(pd);	
			if(pdMsg !=null) {
				pd.put("FPROJECT_ID", pdMsg.getString("PROJECT_ID"));
				pd.put("FRELATED_ID", pd.getString("PurchaseMaterialDetails_ID"));
				//pd.put("FCABINENT_ID", pdMsg.getString("Cabinet_Assembly_Detail_ID"));
				pd.put("AccessURL", "../../../views/projectManager/pro_plan/pro_plan_jssj.html?PROJECT_ID="+pdMsg.getString("PROJECT_ID"));// 
				String FBATCH="GTFK"+Tools.date2Str(new Date(),"yyyyMMddhhmmss");
				pd.put("FCREATOR", Jurisdiction.getName());
				pd.put("FCTIME", Tools.date2Str(new Date()));
				pd.put("FBATCH", FBATCH);//批次号
				String FRECEIVEMANS=pdMsg.getString("FRECEIVEMANS");
				if(FRECEIVEMANS!=null && !"".equals(FRECEIVEMANS)) {
					String ArrayDATA_IDS[] =FRECEIVEMANS.split(",");
					String ArrayDATA_IDSX[] =testRepeat(ArrayDATA_IDS);
					for(String FRECEIVEMAN : ArrayDATA_IDSX) {
						if(!FRECEIVEMAN.equals("")) {
							// 跳转页面
							pd.put("FEEDBACK_ID", this.get32UUID());	//主键
							pd.put("FRECEIVEMAN", FRECEIVEMAN); 
							feedbackService.save(pd);
						}
					}
				}
			}else {
				errInfo = "fail";
				map.put("result", errInfo);
				return map;
			}
		}
		
		
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("feedback:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		feedbackService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("feedback:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		feedbackService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("feedback:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("FCREATOR", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData>	varList = feedbackService.list(page);	//列出FEEDBACK列表
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
	//@RequiresPermissions("feedback:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = feedbackService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("feedback:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			feedbackService.deleteAll(ArrayDATA_IDS);
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
		titles.add("项目ID");	//1
		titles.add("柜体ID");	//2
		titles.add("类型");	//3
		titles.add("信息描述");	//4
		titles.add("提醒人");	//5
		titles.add("提醒人反馈");	//6
		titles.add("发布人");	//7
		titles.add("发布时间");	//8
		titles.add("提醒批次");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = feedbackService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FPROJECT_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FCABINENT_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FTYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("FDESCRIBE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FRECEIVEMAN"));	    //5
			vpd.put("var6", varOList.get(i).getString("FBACK"));	    //6
			vpd.put("var7", varOList.get(i).getString("FCREATOR"));	    //7
			vpd.put("var8", varOList.get(i).getString("FCTIME"));	    //8
			vpd.put("var9", varOList.get(i).getString("FBATCH"));	    //9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	/**v1 管悦 2021-07-09 获取描述信息
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getMsg")
	//@RequiresPermissions("feedback:edit")
	@ResponseBody
	public Object getMsg() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FTYPE=pd.getString("FTYPE");
		if(FTYPE!=null && FTYPE.equals("生产")) {
			pd = feedbackService.getMsg(pd);	
			if(pd !=null) {
				
			}else {
				errInfo = "fail";
			}
		}else if(FTYPE!=null && FTYPE.equals("采购")) {
			pd = feedbackService.getMsg(pd);	
			if(pd !=null) {
				
			}else {
				errInfo = "fail";
			}
		}
		
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
}
