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
import org.yy.service.mom.TEMBILL_EXECUTEService;
import org.yy.service.mom.TEMBILL_EXECUTETICKService;
import org.yy.service.run.RUN_FINAL_INSPECTService;
import org.yy.service.mdm.EQM_POINT_INSPECTService;
import org.yy.service.mom.DOCUMENTTEMPLATEMxService;
import org.yy.service.mom.POLLINGService;
import org.yy.service.mom.SPOT_CHECKService;
import org.yy.service.mom.TEMBILL_EXECUTEMxService;

/** 
 * 说明：质量检测发布
 * 作者：YuanYe
 * 时间：2020-02-24
 * 
 */
@Controller
@RequestMapping("/tembill_execute")
public class TEMBILL_EXECUTEController extends BaseController {
	
	@Autowired
	private TEMBILL_EXECUTEService tembill_executeService;
	
	@Autowired
	private TEMBILL_EXECUTEMxService tembill_executemxService;
	
	@Autowired
	private DOCUMENTTEMPLATEMxService documenttemplatemxService;
	@Autowired
	private TEMBILL_EXECUTETICKService tembill_executetickService;
	@Autowired
	private EQM_POINT_INSPECTService eqm_point_inspectService;//设备点巡检
	@Autowired
	private SPOT_CHECKService spot_checkService;//抽检
	@Autowired
	private POLLINGService pollingService;//巡检
	@Autowired
	private RUN_FINAL_INSPECTService run_final_inspectService;//终检
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("tembill_execute:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TEMBILL_EXECUTE_ID", this.get32UUID());	//主键
		pd.put("FCREATOR",Jurisdiction.getName());//创建人
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));//创建时间
		pd.put("FVERIFY_ODD", "JC"+Tools.date2Str(new Date(),"yyyyMMddHHmmss"));//检验单单据
		if(null!=pd.getString("DOCUMENTTEMPLATE_ID")&&!"".equals(pd.getString("DOCUMENTTEMPLATE_ID"))) {
			List<PageData> varList = documenttemplatemxService.listMxAll(pd);	//列出质量检测下所有明细
			if(varList.size()>0) {
				for(int i = 0; i < varList.size(); i++) {
					PageData TembillMxPd = new PageData();
					TembillMxPd.put("TEMBILL_EXECUTEMX_ID", this.get32UUID());//明细ID
                	TembillMxPd.put("TEMBILL_EXECUTE_ID", pd.getString("TEMBILL_EXECUTE_ID"));//主键ID
                	TembillMxPd.put("CAPTION", varList.get(i).getString("FNAME"));//标题
                	TembillMxPd.put("FDESCRIBE", varList.get(i).getString("FDESCRIBE"));//描述
                	TembillMxPd.put("DESCRIPTION_DETAILS", varList.get(i).getString("FTYPE1"));//有无描述明细(1无,2有)
                	TembillMxPd.put("DESCRIBE_TYPE", varList.get(i).getString("FTYPE"));//描述类型(1单,2多,3填,4描)
                	TembillMxPd.put("DESCRIPTION_CONTENT", varList.get(i).getString("FDESCRIBETERM"));//描述明细内容
                	TembillMxPd.put("DESCRIBE_CONTENT_BF", "");//描述明细内容反馈
                	TembillMxPd.put("RIGHT_OPTION_CONTENT", varList.get(i).getString("FRIGTHTERM"));//右侧选项内容
                	TembillMxPd.put("RIGHT_CONTENT_BF", "");//右侧选项内容反馈
                	TembillMxPd.put("OTHER_DESCRIPTION_TYPE", varList.get(i).getString("OTHER_DESCRIPTION_TYPE"));//有无其他描述(1无,2有)
                	TembillMxPd.put("OTHER_DESCRIPTION", "");//其他描述内容
                	TembillMxPd.put("SORT", varList.get(i).get("FORDER").toString());//排序
                	TembillMxPd.put("FSERIAL_NUM", varList.get(i).getString("FSERIAL_NUM")!=null?varList.get(i).getString("FSERIAL_NUM"):"");//编号
                	TembillMxPd.put("FINSEPCTION", varList.get(i).getString("FINSEPCTION")!=null?varList.get(i).getString("FINSEPCTION"):"");//检查方法
                	TembillMxPd.put("FLASTUPDATEPEOPLE",Jurisdiction.getName());//创建人
                	TembillMxPd.put("FLASTUPDATETIME", Tools.date2Str(new Date()));//创建时间
                	tembill_executemxService.save(TembillMxPd);
				}
			}
		}
		tembill_executeService.save(pd);
		pd = tembill_executeService.findById(pd);	//根据ID读取
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("tembill_execute:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
				tembill_executeService.delete(pd);
				tembill_executemxService.deleteId(pd);//根据主表ID删除数据
				tembill_executetickService.deleteId(pd);//根据主表ID删除数据
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
	//@RequiresPermissions("tembill_execute:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		tembill_executeService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改检验状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editState")
	//@RequiresPermissions("tembill_execute:edit")
	@ResponseBody
	public Object editState() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		tembill_executeService.editState(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**更新反馈内容
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/setFeedback")
	@ResponseBody
	public Object setFeedback() throws Exception{
		PageData pd = new PageData();
		PageData mxPd = new PageData();
		PageData tickPd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
        if(null!=pd.getString("TEMBILL_EXECUTEMX_ID")&&!"".equals(pd.getString("TEMBILL_EXECUTEMX_ID"))) {
        	mxPd.put("TEMBILL_EXECUTEMX_ID", pd.getString("TEMBILL_EXECUTEMX_ID"));
        	mxPd=tembill_executemxService.findById(mxPd);//查询明细数据
        	tickPd.put("TEMBILL_EXECUTE_ID", mxPd.getString("TEMBILL_EXECUTE_ID"));//主表id
        	tickPd.put("FTICK_TIME", Tools.date2Str(new Date()));//反馈时间
        	tickPd.put("FTICK_PERSON", Jurisdiction.getName());//反馈人
        	tickPd.put("FTICK_CAPTION", mxPd.getString("CAPTION"));//反馈标题
        	tickPd.put("FTICK_MATTER", pd.getString("BEAR"));//反馈内容
        	tickPd.put("TEMBILL_EXECUTETICK_ID", this.get32UUID());//主键id
        	tembill_executetickService.save(tickPd);
		}
		pd.put("FLASTUPDATEPEOPLE",Jurisdiction.getName());
		String FLASTUPDATETIME=Tools.date2Str(new Date());
		String chatType = "UPDATE MOM_TEMBILL_EXECUTEMX SET "+pd.getString("FIELD")+"='"+pd.getString("BEAR")+"',"+"FLASTUPDATEPEOPLE='"+pd.getString("FLASTUPDATEPEOPLE")+"',"+"FLASTUPDATETIME='"+FLASTUPDATETIME+"'"+" WHERE TEMBILL_EXECUTEMX_ID='"+pd.getString("TEMBILL_EXECUTEMX_ID")+"'";//拼写SQL语句
		//String chatType = "UPDATE MOM_TEMBILL_EXECUTEMX SET "+pd.getString("FIELD")+"='"+pd.getString("BEAR")+"' WHERE TEMBILL_EXECUTEMX_ID='"+pd.getString("TEMBILL_EXECUTEMX_ID")+"'";//拼写SQL语句
		pd.put("chatType", chatType);
		tembill_executemxService.setFeedback(pd);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("tembill_execute:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = tembill_executeService.list(page);	//列出TEMBILL_EXECUTE列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去操作页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goOperat")
		//@RequiresPermissions("tembill_execute:edit")
		@ResponseBody
		public Object goOperat()throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = tembill_executeService.findById(pd);	//根据ID读取
			List<PageData> varList = tembill_executemxService.listMxAll(pd);	//列出TEMBILL_EXECUTEMX列表
			map.put("pd", pd);
			map.put("varList", varList);
			map.put("result", errInfo);						//返回结果
			return map;
		}	
		
		/**设备点巡检去操作页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goEqmOperat")
		@ResponseBody
		public Object goEqmOperat()throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData eqmPd = new PageData();
			pd = this.getPageData();
			eqmPd.put("EQM_POINT_INSPECT_ID", pd.getString("EQM_POINT_INSPECT_ID"));
			pd = tembill_executeService.findById(pd);	//根据ID读取
			eqmPd=eqm_point_inspectService.findById(eqmPd);
			List<PageData> varList = tembill_executemxService.listMxAll(pd);	//列出TEMBILL_EXECUTEMX列表
			map.put("pd", pd);
			map.put("eqmPd", eqmPd);
			map.put("varList", varList);
			map.put("result", errInfo);						//返回结果
			return map;
		}
		
		/**抽检去操作页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goSpotOperat")
		@ResponseBody
		public Object goSpotOperat()throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData spotPd = new PageData();
			pd = this.getPageData();
			spotPd.put("SPOT_CHECK_ID", pd.getString("SPOT_CHECK_ID"));
			pd = tembill_executeService.findById(pd);	//根据ID读取
			spotPd=spot_checkService.findById(spotPd);
			List<PageData> varList = tembill_executemxService.listMxAll(pd);	//列出TEMBILL_EXECUTEMX列表
			map.put("pd", pd);
			map.put("spotPd", spotPd);
			map.put("varList", varList);
			map.put("result", errInfo);						//返回结果
			return map;
		}
	
		/**巡检去操作页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goPollOperat")
		@ResponseBody
		public Object goPollOperat()throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData pollPd = new PageData();
			pd = this.getPageData();
			pollPd.put("POLLING_ID", pd.getString("POLLING_ID"));
			pd = tembill_executeService.findById(pd);	//根据ID读取
			pollPd=pollingService.findById(pollPd);
			List<PageData> varList = tembill_executemxService.listMxAll(pd);	//列出TEMBILL_EXECUTEMX列表
			map.put("pd", pd);
			map.put("pollPd", pollPd);
			map.put("varList", varList);
			map.put("result", errInfo);						//返回结果
			return map;
		}
		
		/**终检去操作页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goFinalOperat")
		@ResponseBody
		public Object goFinalOperat()throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			String errInfo = "success";
			PageData pd = new PageData();
			PageData finalPd = new PageData();
			pd = this.getPageData();
			finalPd.put("RUN_FINAL_INSPECT_ID", pd.getString("RUN_FINAL_INSPECT_ID"));
			pd = tembill_executeService.findById(pd);	//根据ID读取
			finalPd=run_final_inspectService.findById(finalPd);
			List<PageData> varList = tembill_executemxService.listMxAll(pd);	//列出TEMBILL_EXECUTEMX列表
			map.put("pd", pd);
			map.put("finalPd", finalPd);
			map.put("varList", varList);
			map.put("result", errInfo);						//返回结果
			return map;
		}
		
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("tembill_execute:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = tembill_executeService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}	
	
	
	/**工单单号根据工单关键字查询数据
	* @param
	* @throws Exception
	*/
	@RequestMapping(value="/getPlan")
	@ResponseBody
	public Object getPlan() throws Exception {
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
		List<PageData> varList=tembill_executeService.getPlan(pd);
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
		titles.add("生产工单ID");	//1
		titles.add("生产工单单号");	//2
		titles.add("产品编码");	//3
		titles.add("产品名称");	//4
		titles.add("产线名称");	//5
		titles.add("班组");	//6
		titles.add("规格");	//7
		titles.add("单位");	//8
		titles.add("工单负责人");	//9
		titles.add("检测开始");	//10
		titles.add("检测结束");	//11
		titles.add("检测人");	//12
		titles.add("检测时间");	//13
		titles.add("操作人");	//14
		titles.add("操作时间");	//15
		titles.add("结果");	//16
		titles.add("描述");	//17
		titles.add("状态");	//18
		titles.add("模板名称");	//19
		titles.add("版本");	//20
		titles.add("日期");	//21
		titles.add("所属类别");	//22
		titles.add("创建人");	//23
		titles.add("创建时间");	//24
		titles.add("扩展字段1");	//25
		titles.add("扩展字段2");	//26
		titles.add("扩展字段3");	//27
		titles.add("扩展字段4");	//28
		titles.add("扩展字段5");	//29
		dataMap.put("titles", titles);
		List<PageData> varOList = tembill_executeService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PLAN_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("FBILLNO"));	    //2
			vpd.put("var3", varOList.get(i).getString("PRODUCT_NO"));	    //3
			vpd.put("var4", varOList.get(i).getString("PRODUCT_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("PLINE_NAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("FTEAM"));	    //6
			vpd.put("var7", varOList.get(i).getString("FSPECS"));	    //7
			vpd.put("var8", varOList.get(i).getString("FUNIT"));	    //8
			vpd.put("var9", varOList.get(i).getString("PLAN_PRINCIPAL"));	    //9
			vpd.put("var10", varOList.get(i).getString("FDETECT_START"));	    //10
			vpd.put("var11", varOList.get(i).getString("FDETECT_END"));	    //11
			vpd.put("var12", varOList.get(i).getString("FDETECTOR"));	    //12
			vpd.put("var13", varOList.get(i).getString("FDETECT_TIME"));	    //13
			vpd.put("var14", varOList.get(i).getString("FOPERATOR"));	    //14
			vpd.put("var15", varOList.get(i).getString("FOPERAT_TIME"));	    //15
			vpd.put("var16", varOList.get(i).getString("FEFFECT"));	    //16
			vpd.put("var17", varOList.get(i).getString("FDS"));	    //17
			vpd.put("var18", varOList.get(i).getString("FSTATE"));	    //18
			vpd.put("var19", varOList.get(i).getString("FNAME"));	    //19
			vpd.put("var20", varOList.get(i).getString("VERSION"));	    //20
			vpd.put("var21", varOList.get(i).getString("DAYTIME"));	    //21
			vpd.put("var22", varOList.get(i).getString("BELONG_TYPE"));	    //22
			vpd.put("var23", varOList.get(i).getString("FCREATOR"));	    //23
			vpd.put("var24", varOList.get(i).getString("CREATE_TIME"));	    //24
			vpd.put("var25", varOList.get(i).getString("FEXTEND1"));	    //25
			vpd.put("var26", varOList.get(i).getString("FEXTEND2"));	    //26
			vpd.put("var27", varOList.get(i).getString("FEXTEND3"));	    //27
			vpd.put("var28", varOList.get(i).getString("FEXTEND4"));	    //28
			vpd.put("var29", varOList.get(i).getString("FEXTEND5"));	    //29
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
