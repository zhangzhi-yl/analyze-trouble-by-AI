package org.yy.controller.mdm;

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
import org.yy.service.mdm.EQM_POINT_INSPECTService;
import org.yy.service.mom.DOCUMENTTEMPLATEService;
import org.yy.service.mom.TEMBILL_EXECUTEService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.mdm.EQM_BASEService;
import org.yy.service.mdm.EQM_POINT_INSPECTMxService;

/** 
 * 说明：设备点巡检
 * 作者：YuanYe
 * 时间：2020-02-19
 * 
 */
@Controller
@RequestMapping("/eqm_point_inspect")
public class EQM_POINT_INSPECTController extends BaseController {
	
	@Autowired
	private EQM_POINT_INSPECTService eqm_point_inspectService;
	
	@Autowired
	private EQM_POINT_INSPECTMxService eqm_point_inspectmxService;
	@Autowired
	private TEMBILL_EXECUTEService tembill_executeService;
	@Autowired
	private EQM_BASEService eqm_baseService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private DOCUMENTTEMPLATEService documenttemplateService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("eqm_point_inspect:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EQM_POINT_INSPECT_ID", this.get32UUID());	//主键
		pd.put("STATUS","创建");//执行状态
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		eqm_point_inspectService.save(pd);
		if(null!=pd.getString("DOCUMENTTEMPLATE_ID")&&!"".equals(pd.getString("DOCUMENTTEMPLATE_ID"))) {
			pd.put("IDNAME", "EQM_POINT_INSPECT_ID");//主键字段名
			pd.put("ID", pd.getString("EQM_POINT_INSPECT_ID"));//主键ID
			pd.put("FTABLE_NAME", "MDM_EQM_POINT_INSPECT");//表名
			pd.put("FIEID_NAME", "TEMBILL_EXECUTE_ID");//字段名
			tembill_executeService.saveTem(pd);
		}
		pd = eqm_point_inspectService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("eqm_point_inspect:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(Integer.parseInt(eqm_point_inspectmxService.findCount(pd).get("zs").toString()) > 0){
				errInfo = "error";
			}else{
				eqm_point_inspectService.delete(pd);
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
	//@RequiresPermissions("eqm_point_inspect:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_point_inspectService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	@RequestMapping(value="/editStatus")
	//@RequiresPermissions("eqm_point_inspect:edit")
	@ResponseBody
	public Object editStatus() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		eqm_point_inspectService.editStatus(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("eqm_point_inspect:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = eqm_point_inspectService.list(page);	//列出EQM_POINT_INSPECT列表
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
	//@RequiresPermissions("eqm_point_inspect:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = eqm_point_inspectService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	
	/**通过设备标识获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getScan")
	@ResponseBody
	public Object getScan()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{			
			pd = eqm_baseService.findByNumber(pd);	//根据设备标识读取
			if(pd==null) {
				result = "201";
			}
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("pd", pd);
			map.put("result", result);
		}		
		return map;
	}
	
	/**更新点检人
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editOperator")
	@ResponseBody
	public Object editOperator()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", pd.getString("FOPERATOR"));
		try{	
			PageData countpd = staffService.findByUser(pd);//获取人员是否存在
			if(Integer.parseInt(countpd.get("NAMENUM").toString())>0) {//判断人员表中是否存在输入值
				eqm_point_inspectService.editOperator(pd);	//更新点检人
			}else {//不存在返回无数据
				result = "201";
			}
			
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("pd", pd);
			map.put("result", result);
		}		
		return map;
	}
	
	/**更新确认人
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editIdentfied")
	@ResponseBody
	public Object editIdentfied()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("NAME", pd.getString("FIDENTIFIED"));
		try{	
			PageData countpd = staffService.findByUser(pd);//获取人员是否存在
			if(Integer.parseInt(countpd.get("NAMENUM").toString())>0) {//判断人员表中是否存在输入值
				eqm_point_inspectService.editIdentfied(pd);	//更新确认人
			}else {//不存在返回无数据
				result = "201";
			}
			
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("pd", pd);
			map.put("result", result);
		}		
		return map;
	}
	
	/**查询单据模板
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getEqmStencil")
	@ResponseBody
	public Object getEqmStencil()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "200";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{	
			pd=documenttemplateService.findByStencil(pd);//查询模板名称及ID
			if(null!=pd) {
				result = "200";
			}else {//不存在返回无数据
				result = "201";
			}
		}catch (Exception e){
			result = "500";
		}finally{
			map.put("pd", pd);
			map.put("result", result);
		}		
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
		titles.add("设备基础资料ID");	//1
		titles.add("设备名称");	//2
		titles.add("设备编号");	//3
		titles.add("设备类型");	//4
		titles.add("规格型号");	//5
		titles.add("工作中心");	//6
		titles.add("条码");	//7
		titles.add("品牌");	//8
		titles.add("创建人");	//9
		titles.add("创建时间");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = eqm_point_inspectService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("EQM_BASE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("FNUMBER"));	    //3
			vpd.put("var4", varOList.get(i).getString("FTYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FSPECS"));	    //5
			vpd.put("var6", varOList.get(i).getString("FWORKCENTER"));	    //6
			vpd.put("var7", varOList.get(i).getString("FBARCODE"));	    //7
			vpd.put("var8", varOList.get(i).getString("FBRAND"));	    //8
			vpd.put("var9", varOList.get(i).getString("FCREATOR"));	    //9
			vpd.put("var10", varOList.get(i).getString("CREATE_TIME"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
