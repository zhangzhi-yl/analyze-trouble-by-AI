package org.yy.controller.pm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.DelFileUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.entity.PageData;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.pm.PM_MAINTAINService;
//import org.yy.service.pm.PM_MAINTAINMxService;

/** 
 * 说明：设备保养任务
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-29
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PM_MAINTAIN")
public class PM_MAINTAINController extends BaseController {
	
	@Autowired
	private PM_MAINTAINService PM_MAINTAINService;
	
	//@Autowired
	//private PM_MAINTAINMxService PM_MAINTAINmxService;
	
	@Autowired
	private AttachmentSetService attachmentsetService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("PM_MAINTAIN:add")
	@ResponseBody
	public Object add(
			@RequestParam(value = "RESERVE_ONE", required = false) MultipartFile file,
			@RequestParam(value="MAINTAIN_FILE",required=false) String path,
			@RequestParam(value="PM_MAINTAIN_ID",required=false) String PM_MAINTAIN_ID,
			@RequestParam(value="MAINTAIN_PLAN_NO",required=false) String MAINTAIN_PLAN_NO,
			@RequestParam(value="EQM_BASE_ID",required=false) String EQM_BASE_ID,
			@RequestParam(value="MAINTAIN_TYPE",required=false) String MAINTAIN_TYPE,
			@RequestParam(value="MAINTAIN_SOURCE",required=false) String MAINTAIN_SOURCE,
			@RequestParam(value="MAINTAIN_DEPT",required=false) String MAINTAIN_DEPT,
			@RequestParam(value="MAINTAIN_PRINCIPAL",required=false) String MAINTAIN_PRINCIPAL,
			@RequestParam(value="PLAN_MAINTAIN_TIME",required=false) String PLAN_MAINTAIN_TIME,
			@RequestParam(value="PRACTICAL_START_TIME",required=false) String PRACTICAL_START_TIME,
			@RequestParam(value="PRACTICAL_END_TIME",required=false) String PRACTICAL_END_TIME,
			@RequestParam(value="MAINTAIN_MANHOUR",required=false) String MAINTAIN_MANHOUR,
			@RequestParam(value="IF_LEFTOVER_PROBLEM",required=false) String IF_LEFTOVER_PROBLEM,
			@RequestParam(value="LEFTOVER_PROBLEM",required=false) String LEFTOVER_PROBLEM,
			@RequestParam(value="REMARK",required=false) String REMARK,
			@RequestParam(value="RUN_STATUS",required=false) String RUN_STATUS,
			@RequestParam(value="FCREATOR",required=false) String FCREATOR,
			@RequestParam(value="CREATE_TIME",required=false) String CREATE_TIME,
			@RequestParam(value="RESERVE_TWO",required=false) String RESERVE_TWO,
			@RequestParam(value="RESERVE_THREE",required=false) String RESERVE_THREE
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PM_MAINTAIN_ID", this.get32UUID());	//主键
		pd.put("FCREATOR", Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		pd.put("EQM_BASE_ID", EQM_BASE_ID);	
		pd.put("MAINTAIN_PLAN_NO", MAINTAIN_PLAN_NO);	
		pd.put("MAINTAIN_TYPE", MAINTAIN_TYPE);	
		pd.put("MAINTAIN_SOURCE", MAINTAIN_SOURCE);	
		pd.put("MAINTAIN_DEPT", MAINTAIN_DEPT);	
		pd.put("MAINTAIN_PRINCIPAL", MAINTAIN_PRINCIPAL);	
		pd.put("PLAN_MAINTAIN_TIME", PLAN_MAINTAIN_TIME);	
		pd.put("PRACTICAL_START_TIME", PRACTICAL_START_TIME);	
		pd.put("PRACTICAL_END_TIME", PRACTICAL_END_TIME);	
		pd.put("MAINTAIN_MANHOUR", MAINTAIN_MANHOUR);	
		pd.put("IF_LEFTOVER_PROBLEM", IF_LEFTOVER_PROBLEM);	
		pd.put("REMARK", REMARK);
		pd.put("RUN_STATUS", RUN_STATUS);
		pd.put("RESERVE_TWO", RESERVE_TWO);
		pd.put("RESERVE_THREE", RESERVE_THREE);
		//上传附件
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar calendar = Calendar.getInstance();
				String dateName = df.format(calendar.getTime());
				String ffile = DateUtil.getDays();
				String MAINTAIN_FILE = "";
				String PMRESERVE_ONE = "";
				if (null != file && !file.isEmpty()) {
					String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
					String fileNamereal = pd.getString("MAINTAIN_FILE").substring(0, pd.getString("MAINTAIN_FILE").indexOf(".")); // 文件上传路径
					MAINTAIN_FILE = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
					PMRESERVE_ONE = Const.FILEPATHFILE +DateUtil.getDays()+"/"+MAINTAIN_FILE;
					//附件集插入数据
					PageData pdFile=new PageData();
					pdFile.put("DataSources", "设备保养任务");
					pdFile.put("AssociationIDTable", "PM_MAINTAIN");
					pdFile.put("AssociationID", pd.getString("PM_MAINTAIN_ID"));
					pdFile.put("FName", MAINTAIN_FILE);
					pdFile.put("FUrl", PMRESERVE_ONE);
					pdFile.put("FExplanation", "");
					pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
					pdFile.put("FCreateTime", Tools.date2Str(new Date()));
					attachmentsetService.check(pdFile);
				}
				pd.put("RESERVE_ONE", PMRESERVE_ONE);	
				PM_MAINTAINService.save(pd);
		pd = PM_MAINTAINService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("PM_MAINTAIN:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			PM_MAINTAINService.delete(pd);
			/*if(Integer.parseInt(PM_MAINTAINmxService.findCount(pd).get("zs").toString()) > 0){
				errInfo = "error";
			}else{
				PM_MAINTAINService.delete(pd);
			}*/
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改运行状态
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/goIssue")
	@ResponseBody
	public Object goIssue() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			PM_MAINTAINService.editIssue(pd);
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
	//@RequiresPermissions("PM_MAINTAIN:edit")
	@ResponseBody
	public Object edit(
			@RequestParam(value = "RESERVE_ONE", required = false) MultipartFile file,
			@RequestParam(value="MAINTAIN_FILE",required=false) String path,
			@RequestParam(value="PM_MAINTAIN_ID",required=false) String PM_MAINTAIN_ID,
			@RequestParam(value="MAINTAIN_PLAN_NO",required=false) String MAINTAIN_PLAN_NO,
			@RequestParam(value="EQM_BASE_ID",required=false) String EQM_BASE_ID,
			@RequestParam(value="MAINTAIN_TYPE",required=false) String MAINTAIN_TYPE,
			@RequestParam(value="MAINTAIN_SOURCE",required=false) String MAINTAIN_SOURCE,
			@RequestParam(value="MAINTAIN_DEPT",required=false) String MAINTAIN_DEPT,
			@RequestParam(value="MAINTAIN_PRINCIPAL",required=false) String MAINTAIN_PRINCIPAL,
			@RequestParam(value="PLAN_MAINTAIN_TIME",required=false) String PLAN_MAINTAIN_TIME,
			@RequestParam(value="PRACTICAL_START_TIME",required=false) String PRACTICAL_START_TIME,
			@RequestParam(value="PRACTICAL_END_TIME",required=false) String PRACTICAL_END_TIME,
			@RequestParam(value="MAINTAIN_MANHOUR",required=false) String MAINTAIN_MANHOUR,
			@RequestParam(value="IF_LEFTOVER_PROBLEM",required=false) String IF_LEFTOVER_PROBLEM,
			@RequestParam(value="LEFTOVER_PROBLEM",required=false) String LEFTOVER_PROBLEM,
			@RequestParam(value="REMARK",required=false) String REMARK,
			@RequestParam(value="RUN_STATUS",required=false) String RUN_STATUS,
			@RequestParam(value="FCREATOR",required=false) String FCREATOR,
			@RequestParam(value="CREATE_TIME",required=false) String CREATE_TIME,
			@RequestParam(value="RESERVE_TWO",required=false) String RESERVE_TWO,
			@RequestParam(value="RESERVE_ONEEDIT",required=false) String RESERVE_ONEEDIT,
			@RequestParam(value="RESERVE_THREE",required=false) String RESERVE_THREE
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("PM_MAINTAIN_ID", PM_MAINTAIN_ID);	//主键
		pd.put("FCREATOR", FCREATOR);//创建人
		pd.put("CREATE_TIME", CREATE_TIME);//创建时间
		pd.put("EQM_BASE_ID", EQM_BASE_ID);	
		pd.put("MAINTAIN_PLAN_NO", MAINTAIN_PLAN_NO);	
		pd.put("MAINTAIN_TYPE", MAINTAIN_TYPE);	
		pd.put("MAINTAIN_SOURCE", MAINTAIN_SOURCE);	
		pd.put("MAINTAIN_DEPT", MAINTAIN_DEPT);	
		pd.put("MAINTAIN_PRINCIPAL", MAINTAIN_PRINCIPAL);	
		pd.put("PLAN_MAINTAIN_TIME", PLAN_MAINTAIN_TIME);	
		pd.put("PRACTICAL_START_TIME", PRACTICAL_START_TIME);	
		pd.put("PRACTICAL_END_TIME", PRACTICAL_END_TIME);	
		pd.put("MAINTAIN_MANHOUR", MAINTAIN_MANHOUR);	
		pd.put("IF_LEFTOVER_PROBLEM", IF_LEFTOVER_PROBLEM);	
		pd.put("REMARK", REMARK);
		pd.put("RUN_STATUS", RUN_STATUS);
		pd.put("RESERVE_TWO", RESERVE_TWO);
		pd.put("RESERVE_THREE", RESERVE_THREE);
		pd.put("RESERVE_ONE", RESERVE_ONEEDIT);
		//上传附件
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar calendar = Calendar.getInstance();
				String dateName = df.format(calendar.getTime());
				String ffile = DateUtil.getDays();
				String MAINTAIN_FILE = "";
				String PMRESERVE_ONE = "";
				PageData pdJ = new PageData();
				pdJ=PM_MAINTAINService.findById(pd);
				String MAINTAIN_FILEJ="";
				if(null!=pdJ) {
				   MAINTAIN_FILEJ=pdJ.getString("MAINTAIN_FILE");
				}
				if (null != file && !file.isEmpty()) {//上传附件
					if(!MAINTAIN_FILEJ.equals(pd.getString("MAINTAIN_FILE"))) {
						String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
						String fileNamereal = pd.getString("MAINTAIN_FILE").substring(0, pd.getString("MAINTAIN_FILE").indexOf(".")); // 文件上传路径
						MAINTAIN_FILE = FileUpload.fileUp(file, filePath, fileNamereal+dateName);// 执行上传
						PMRESERVE_ONE = Const.FILEPATHFILE +DateUtil.getDays()+"/"+MAINTAIN_FILE;
						//附件集插入数据
						PageData pdFile=new PageData();
						pdFile.put("DataSources", "设备保养任务");
						pdFile.put("AssociationIDTable", "PM_MAINTAIN");
						pdFile.put("AssociationID", pd.getString("PM_MAINTAIN_ID"));
						pdFile.put("FName", MAINTAIN_FILE);
						pdFile.put("FUrl", PMRESERVE_ONE);
						pdFile.put("FExplanation", "");
						pdFile.put("FCreatePersonID",Jurisdiction.getUsername());
						pdFile.put("FCreateTime", Tools.date2Str(new Date()));
						pd.put("RESERVE_ONE", PMRESERVE_ONE);
						attachmentsetService.check(pdFile);
					}
				}else {//没上传附件引用之前的附件信息
					pd.put("MAINTAIN_FILE", pdJ.getString("MAINTAIN_FILE"));//附件名称
					pd.put("RESERVE_ONE", pdJ.getString("RESERVE_ONE"));//附件名称
				}
				PM_MAINTAINService.edit(pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("PM_MAINTAIN:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = PM_MAINTAINService.list(page);	//列出PM_MAINTAIN列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("PM_MAINTAIN:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PM_MAINTAINService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	/**删除附件
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delFj")
	@ResponseBody
	public Object delFj() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PATH = pd.getString("PATH");	
		if(Tools.notEmpty(pd.getString("PATH").trim())){								//附件路径
			DelFileUtil.delFolder(PathUtil.getProjectpath() + pd.getString("PATH")); 	//删除硬盘中的附件
		}
		if(PATH != null){
			PM_MAINTAINService.delFj(pd);													//删除数据库中附件数据
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
		titles.add("设备保养ID");	//1
		titles.add("保养计划单");	//2
		titles.add("设备ID");	//3
		titles.add("保养类型");	//4
		titles.add("保养来源");	//5
		titles.add("保养负责部门");	//6
		titles.add("保养负责人");	//7
		titles.add("计划保养时间");	//8
		titles.add("实际开始时间");	//9
		titles.add("实际结束时间");	//10
		titles.add("实际保养工时");	//11
		titles.add("是否有遗留问题");	//12
		titles.add("遗留问题描述");	//13
		titles.add("备注");	//14
		titles.add("保养手册附件");	//15
		titles.add("运行状态");	//16
		titles.add("创建人");	//17
		titles.add("创建时间");	//18
		titles.add("预留1");	//19
		titles.add("预留2");	//20
		titles.add("预留3");	//21
		dataMap.put("titles", titles);
		List<PageData> varOList = PM_MAINTAINService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PM_MAINTAIN_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("MAINTAIN_PLAN_NO"));	    //2
			vpd.put("var3", varOList.get(i).getString("EQM_BASE_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("MAINTAIN_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("MAINTAIN_SOURCE"));	    //5
			vpd.put("var6", varOList.get(i).getString("MAINTAIN_DEPT"));	    //6
			vpd.put("var7", varOList.get(i).getString("MAINTAIN_PRINCIPAL"));	    //7
			vpd.put("var8", varOList.get(i).getString("PLAN_MAINTAIN_TIME"));	    //8
			vpd.put("var9", varOList.get(i).getString("PRACTICAL_START_TIME"));	    //9
			vpd.put("var10", varOList.get(i).getString("PRACTICAL_END_TIME"));	    //10
			vpd.put("var11", varOList.get(i).get("MAINTAIN_MANHOUR").toString());	//11
			vpd.put("var12", varOList.get(i).getString("IF_LEFTOVER_PROBLEM"));	    //12
			vpd.put("var13", varOList.get(i).getString("LEFTOVER_PROBLEM"));	    //13
			vpd.put("var14", varOList.get(i).getString("REMARK"));	    //14
			vpd.put("var15", varOList.get(i).getString("MAINTAIN_FILE"));	    //15
			vpd.put("var16", varOList.get(i).getString("RUN_STATUS"));	    //16
			vpd.put("var17", varOList.get(i).getString("FCREATOR"));	    //17
			vpd.put("var18", varOList.get(i).getString("CREATE_TIME"));	    //18
			vpd.put("var19", varOList.get(i).getString("RESERVE_ONE"));	    //19
			vpd.put("var20", varOList.get(i).getString("RESERVE_TWO"));	    //20
			vpd.put("var21", varOList.get(i).getString("RESERVE_THREE"));	    //21
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	
}
