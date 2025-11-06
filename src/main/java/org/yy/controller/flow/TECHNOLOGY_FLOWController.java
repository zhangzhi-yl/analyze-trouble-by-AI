package org.yy.controller.flow;

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
import org.yy.util.UuidUtil;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.flow.TECHNOLOGY_FLOWService;
import org.yy.service.km.WorkingProcedureService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：工艺工序节点
 * 作者：YuanYes QQ356703572
 * 时间：2020-12-02
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/TECHNOLOGY_FLOW")
public class TECHNOLOGY_FLOWController extends BaseController {
	
	@Autowired
	private TECHNOLOGY_FLOWService TECHNOLOGY_FLOWService;
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private WorkingProcedureService WorkingProcedureService;//工艺路线工序
	
	@Autowired
	private BYTEARRAYService BYTEARRAYService;//流程图文件
	
	/**保存
	 * 添加节点数据和工艺工序数据，刷新流程图
	 * @param	pd.ProcessRoute_ID
	 * 			pd.
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("TECHNOLOGY_FLOW:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String ProcessRoute_ID = pd.getString("ProcessRoute_ID");
		String time = Tools.date2Str(new Date());
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData node=new PageData();
		node.put("NODE_ID", pd.getString("NODE_ID"));
		node.put("NODE_NAME", pd.getString("NODE_NAME"));
		node.put("NODE_KIND", pd.getString("NODE_KIND"));//node或者line
		node.put("NODE_TYPE", pd.getString("NODE_TYPE"));//task/start round/tb/sl
		node.put("PHASE_TYPE", "");
		node.put("JUMP_CONDITION", "");
		node.put("BEGIN_NODE", pd.getString("BEGIN_NODE"));
		node.put("END_NODE", pd.getString("END_NODE"));
		node.put("FPARTICIPATOR", "");
		node.put("FDES", "");
		node.put("FCREATOR", staffid);
		node.put("CREATE_TIME", time);
		node.put("LAST_MODIFIER", staffid);
		node.put("LAST_MODIFIED_TIME", time);
		node.put("ProcessRoute_ID", ProcessRoute_ID);
		node.put("TECHNOLOGY_FLOW_ID", UuidUtil.get32UUID());
		TECHNOLOGY_FLOWService.save(node);
		if("node".equals(pd.getString("NODE_KIND"))) {//如果是节点，则添加工艺工序
			PageData wpd = new PageData();
			wpd.put("ProcessRouteID", ProcessRoute_ID);				
			wpd.put("WorkingProcedure_ID", this.get32UUID());		//工艺路线工序主键
			pd.put("ProcessRouteID", ProcessRoute_ID);
			int i = Integer.parseInt(WorkingProcedureService.findMaxSerialNumByProcessRouteID(pd)
					.get("maxSerialNum").toString())+1;
			wpd.put("SerialNum", i);
			wpd.put("NODE_ID", pd.getString("NODE_ID"));
			wpd.put("FCreatePersonID", staffid);
			wpd.put("FCreateTime", time);
			wpd.put("FStatus", "");
			WorkingProcedureService.save(wpd);						//添加工艺工序
		}
		/*pd.put("PID", ProcessRoute_ID);
		PageData BYTEARRAYpd = BYTEARRAYService.findByPID(pd);	//查询流程图文件，准备更新流程图
		if(null!=BYTEARRAYpd && BYTEARRAYpd.containsKey("GE_BYTEARRAY_FLOW_ID")) {
			BYTEARRAYpd.put("TEXT_BYTE_STREAM", pd.get("TEXT_BYTE_STREAM"));
			BYTEARRAYpd.put("LAST_MODIFIED_TIME", time);
			BYTEARRAYpd.put("LAST_MODIFIER", "");
			BYTEARRAYService.edit(BYTEARRAYpd);//更新流程图
		}*/
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * 删除节点 和 数据 , 刷新流程图json 方法
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("TECHNOLOGY_FLOW:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ProcessRouteID", pd.getString("ProcessRoute_ID"));
		PageData wpd = WorkingProcedureService.findByProcessRouteIDAndNodeId(pd);
		if(null!=wpd)
		WorkingProcedureService.delete(wpd);	//删除工艺工序，及其所属附件、次品项
		TECHNOLOGY_FLOWService.delete(pd);		//根据工艺路线id和节点id删除节点
		/*pd.put("PID", pd.getString("ProcessRoute_ID"));
		PageData BYTEARRAYpd = BYTEARRAYService.findByPID(pd);	//查询流程图文件，准备更新流程图
		if(null!=BYTEARRAYpd && BYTEARRAYpd.containsKey("GE_BYTEARRAY_FLOW_ID")) {
			BYTEARRAYpd.put("TEXT_BYTE_STREAM", pd.get("TEXT_BYTE_STREAM"));
			BYTEARRAYpd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			BYTEARRAYpd.put("LAST_MODIFIER", "");
			BYTEARRAYService.edit(BYTEARRAYpd);//更新流程图
		}*/
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * 一般为修改了节点名称
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	//@RequiresPermissions("TECHNOLOGY_FLOW:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData nodepd = new PageData();
		List<PageData> list = TECHNOLOGY_FLOWService.findByProcessRoute_ID(pd);//根据工艺路线id和节点id查询
		if(!list.isEmpty() && null!=list.get(0)) {
			nodepd=list.get(0);
		}
		nodepd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
		nodepd.put("LAST_MODIFIER", staffid);
		nodepd.put("NODE_NAME", pd.getString("NODE_NAME"));
		TECHNOLOGY_FLOWService.edit(nodepd);
		/*pd.put("PID", pd.getString("ProcessRoute_ID"));
		PageData BYTEARRAYpd = BYTEARRAYService.findByPID(pd);	//查询流程图文件，准备更新流程图
		if(null!=BYTEARRAYpd && BYTEARRAYpd.containsKey("GE_BYTEARRAY_FLOW_ID")) {
			BYTEARRAYpd.put("TEXT_BYTE_STREAM", pd.get("TEXT_BYTE_STREAM"));
			BYTEARRAYpd.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			BYTEARRAYpd.put("LAST_MODIFIER", "");
			BYTEARRAYService.edit(BYTEARRAYpd);//更新流程图
		}*/
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("TECHNOLOGY_FLOW:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = TECHNOLOGY_FLOWService.list(page);	//列出TECHNOLOGY_FLOW列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	/**全部列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("TECHNOLOGY_FLOW:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = TECHNOLOGY_FLOWService.listAll(pd);	//列出TECHNOLOGY_FLOW列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("TECHNOLOGY_FLOW:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = TECHNOLOGY_FLOWService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/findByProcessRoute_ID")
	@ResponseBody
	public Object findByProcessRoute_ID() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> list = TECHNOLOGY_FLOWService.findByProcessRoute_ID(pd);//根据工艺路线id和节点id查询
		if(!list.isEmpty()) {
			map.put("pd", list.get(0));
		}
		map.put("result", errInfo);
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	//@RequiresPermissions("TECHNOLOGY_FLOW:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			TECHNOLOGY_FLOWService.deleteAll(ArrayDATA_IDS);
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
		titles.add("PH_节点ID");	//1
		titles.add("PH_节点名称");	//2
		titles.add("PH_节点种类");	//3
		titles.add("PH_节点类型");	//4
		titles.add("PH_步骤类型");	//5
		titles.add("PH_跳转条件");	//6
		titles.add("PH_开始节点");	//7
		titles.add("PH_结束节点");	//8
		titles.add("PH_参与者");	//9
		titles.add("PH_描述");	//10
		titles.add("PH_创建人");	//11
		titles.add("PH_创建时间");	//12
		titles.add("PH_最后变更人");	//13
		titles.add("PH_最后变更时间");	//14
		titles.add("工艺路线ID");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = TECHNOLOGY_FLOWService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("NODE_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("NODE_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("NODE_KIND"));	    //3
			vpd.put("var4", varOList.get(i).getString("NODE_TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("PHASE_TYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("JUMP_CONDITION"));	    //6
			vpd.put("var7", varOList.get(i).getString("BEGIN_NODE"));	    //7
			vpd.put("var8", varOList.get(i).getString("END_NODE"));	    //8
			vpd.put("var9", varOList.get(i).getString("FPARTICIPATOR"));	    //9
			vpd.put("var10", varOList.get(i).getString("FDES"));	    //10
			vpd.put("var11", varOList.get(i).getString("FCREATOR"));	    //11
			vpd.put("var12", varOList.get(i).getString("CREATE_TIME"));	    //12
			vpd.put("var13", varOList.get(i).getString("LAST_MODIFIER"));	    //13
			vpd.put("var14", varOList.get(i).getString("LAST_MODIFIED_TIME"));	    //14
			vpd.put("var15", varOList.get(i).getString("ProcessRoute_ID"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
}
