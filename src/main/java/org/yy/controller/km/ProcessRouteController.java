package org.yy.controller.km;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileDownload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelRead;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.flow.TECHNOLOGY_FLOWService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.ProcessRouteService;
import org.yy.service.km.WorkingProcedureDefectiveItemsService;
import org.yy.service.km.WorkingProcedureService;
import org.yy.service.mom.OperationRecordService;

/** 
 * 说明：工艺路线
 * 作者：YuanYes QQ356703572
 * 时间：2020-11-11
 * 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ProcessRoute")
public class ProcessRouteController extends BaseController {
	
	@Autowired
	private ProcessRouteService ProcessRouteService;
	
	@Autowired
	private WorkingProcedureService WorkingProcedureService;//工艺路线工序
	
	@Autowired
	private WorkingProcedureDefectiveItemsService WorkingProcedureDefectiveItemsService;//工艺路线工序次品项
	
	@Autowired
	private AttachmentSetService attachmentsetService;//附件
	
	@Autowired
	private StaffService staffService;//员工
	
	@Autowired
	private OperationRecordService operationrecordService;//操作记录
	
	@Autowired
	private CodingRulesService codingRulesService;// 编码规则接口
	
	@Autowired
	private BYTEARRAYService BYTEARRAYService;//流程图文件
	
	@Autowired
	private TECHNOLOGY_FLOWService TECHNOLOGY_FLOWService;//工艺工序节点
	
	/**复制工艺路线
	 * 复制流程图、工艺路线、工艺路线流程图节点、工艺工序、工艺工序附件、工艺工序次品项
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/copy")
	@ResponseBody
	public Object copy() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String currentTime = Tools.date2Str(new Date());
		String ProcessRoute_ID = pd.getString("ProcessRoute_ID");
		String newProcessRoute_ID = this.get32UUID();
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData ProcessRoute = ProcessRouteService.findById(pd);//获取工艺路线
		ProcessRoute.put("ProcessRouteID", ProcessRoute_ID);//关联工艺工序表查询隶属工艺工序
		List<PageData> nodeList = TECHNOLOGY_FLOWService.findByProcessRoute_ID(pd);//工艺路线流程图下各个节点及连线,ProcessRoute_ID
		//List<PageData> WorkingProcedureList = WorkingProcedureService.listAll(ProcessRoute);//工艺路线工序列表
		ProcessRoute.put("ProcessRoute_ID", newProcessRoute_ID);
		ProcessRoute.put("FNum", codingRulesService.getRuleNumByRuleType("ProcessRouteNO").toString());//根据规则类型获取规则号
		ProcessRoute.put("FName", ProcessRoute.getString("FName")+"_副本");//创建人，员工id
		ProcessRoute.put("FCreatePersonID", staffid);//创建人，员工id
		ProcessRoute.put("FCreateTime", currentTime);
		ProcessRoute.put("LastModifiedBy", staffid);//修改人，员工id
		ProcessRoute.put("LastModificationTime", currentTime);
		ProcessRoute.put("FStatus", "停用");
		ProcessRouteService.save(ProcessRoute);//保存工艺路线
		pd.put("PID", ProcessRoute_ID);
		pd.put("FTYPE", "工艺路线");
		PageData byteArray = BYTEARRAYService.findByPID(pd);//获取流程图
		byteArray.put("PID", newProcessRoute_ID);
		//byteArray.put("FTYPE", "工艺路线");//引用原流程图FTYPE
		//byteArray.put("RESOURCE_FILE_NAME", "工艺路线");//引用原流程图RESOURCE_FILE_NAME
		//byteArray.put("TEXT_BYTE_STREAM", "");//引用原流程图json
		byteArray.put("LAST_MODIFIED_TIME", currentTime);
		byteArray.put("LAST_MODIFIER", staffid);
		byteArray.put("GE_BYTEARRAY_FLOW_ID", UuidUtil.get32UUID());
		BYTEARRAYService.save(byteArray);//保存流程图
		for(PageData node:nodeList) {
			node.put("TECHNOLOGY_FLOW_ID", UuidUtil.get32UUID());
			node.put("NODE_ID", node.containsKey("NODE_ID")?node.getString("NODE_ID"):"");
			node.put("NODE_NAME", node.containsKey("NODE_NAME")?node.getString("NODE_NAME"):"");
			node.put("NODE_KIND", node.containsKey("NODE_KIND")?node.getString("NODE_KIND"):"");
			node.put("NODE_TYPE", node.containsKey("NODE_TYPE")?node.getString("NODE_TYPE"):"");
			node.put("PHASE_TYPE", node.containsKey("PHASE_TYPE")?node.getString("PHASE_TYPE"):"");
			node.put("JUMP_CONDITION", node.containsKey("JUMP_CONDITION")?node.getString("JUMP_CONDITION"):"");
			node.put("BEGIN_NODE", node.containsKey("BEGIN_NODE")?node.getString("BEGIN_NODE"):"");
			node.put("END_NODE", node.containsKey("END_NODE")?node.getString("END_NODE"):"");
			node.put("FPARTICIPATOR", node.containsKey("FPARTICIPATOR")?node.getString("FPARTICIPATOR"):"");
			node.put("FDES", node.containsKey("FDES")?node.getString("FDES"):"");
			node.put("FCREATOR", staffid);
			node.put("CREATE_TIME", currentTime);
			node.put("LAST_MODIFIER", staffid);
			node.put("LAST_MODIFIED_TIME", currentTime);
			node.put("ProcessRoute_ID", newProcessRoute_ID);
			TECHNOLOGY_FLOWService.save(node);
			if(node.containsKey("NODE_KIND") && "node".equals(node.getString("NODE_KIND"))
					 && !"开始".equals(node.getString("NODE_NAME")) && !"结束".equals(node.getString("NODE_NAME"))) {
				PageData temp = new PageData();
				temp.put("NODE_ID", node.getString("NODE_ID"));
				temp.put("ProcessRouteID", ProcessRoute_ID);
				PageData WorkingProcedure = WorkingProcedureService.findByProcessRouteIDAndNodeId(temp);
				if(null!=WorkingProcedure && WorkingProcedure.containsKey("WorkingProcedure_ID")) {
					//根据旧工艺工序id查询次品项，复制到新工艺工序下
					String WorkingProcedure_ID = WorkingProcedure.getString("WorkingProcedure_ID");
					//保存工艺工序
					String newWorkingProcedureId=this.get32UUID();
					WorkingProcedure.put("WorkingProcedure_ID", newWorkingProcedureId);
					WorkingProcedure.put("ProcessRouteID", newProcessRoute_ID);
					WorkingProcedure.put("FCreatePersonID", staffid);
					WorkingProcedure.put("FCreateTime", currentTime);
					WorkingProcedureService.save(WorkingProcedure);//保存工艺工序
					//保存附件 
					PageData pdFj=new PageData();
					pdFj.put("AssociationID", WorkingProcedure.getString("WorkingProcedure_ID"));
					List<PageData> findByAssId = attachmentsetService.findByAssId(pdFj);
					if(CollectionUtil.isNotEmpty(findByAssId)){
						pdFj=findByAssId.get(0);
						if(pdFj != null) {
							//附件集插入数据
							pdFj.put("DataSources", "工艺路线工序");//数据来源
							pdFj.put("AssociationIDTable", "KM_WorkingProcedure");//数据来源表名
							pdFj.put("AssociationID", newWorkingProcedureId);//数据来源表ID
							pdFj.put("FCreatePersonID", staffid);//STAFF_ID;
							pdFj.put("FCreateTime", currentTime);
							attachmentsetService.check(pdFj);
						}
					}
					
					PageData p = new PageData();
					p.put("WorkingProcedure_ID", WorkingProcedure_ID);
					List<PageData> DefectiveItems = WorkingProcedureDefectiveItemsService.listAll(p);//根据工艺工序id查询隶属次品项
					for(PageData DefectiveItem:DefectiveItems) {
						//保存隶属次品项
						DefectiveItem.put("WorkingProcedureDefectiveItems_ID", this.get32UUID());
						DefectiveItem.put("WorkingProcedure_ID", newWorkingProcedureId);
						WorkingProcedureDefectiveItemsService.save(DefectiveItem);
					}
				}
			}
		}
		operationrecordService.add("","工艺路线","复制",pd.getString("ProcessRoute_ID"),staffid,pd.toString());
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**添加
	 * 	添加工艺路线，添加流程图，添加开始结束节点
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	//@RequiresPermissions("ProcessRoute:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData numpd = new PageData();
		numpd = ProcessRouteService.findCount(pd);
		//PageData namepd = new PageData();
		//namepd = ProcessRouteService.findCountByFName(pd);
		String time = Tools.date2Str(new Date());
		if(numpd != null && Integer.parseInt(numpd.get("zs").toString()) > 0) {
			errInfo = "error";//编号重复
		//} else if (namepd != null && Integer.parseInt(namepd.get("zs").toString()) > 0) {
		//	errInfo = "error";//名称重复
		} else {
			//FNum = codingRulesService.getRuleNumByRuleType("ProcessRouteNO").toString();//根据规则类型获取规则号
			String ProcessRoute_ID = this.get32UUID();
			PageData staff = new PageData();
			staff.put("FNAME", Jurisdiction.getName());
			String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
			pd.put("ProcessRoute_ID", ProcessRoute_ID);				//工艺路线主键
			//String userid=staffService.getStaffId(pd).getString("STAFF_ID");
			pd.put("FCreatePersonID", staffid);			//创建人
			pd.put("FCreateTime", time);			//创建时间
			pd.put("LastModificationTime", time);	//修改时间
			pd.put("LastModifiedBy", staffid);			//修改人
			pd.put("FStatus", "停用");
			pd.put("FVersion", "1");
			ProcessRouteService.save(pd);
			PageData wpd = new PageData();
			wpd.put("PID", ProcessRoute_ID);				
			wpd.put("GE_BYTEARRAY_FLOW_ID", this.get32UUID());		//流程图表主键
			wpd.put("FTYPE", "工艺路线");		//流程图类型
			wpd.put("RESOURCE_FILE_NAME", "工艺路线");		//源文件名
			wpd.put("TEXT_BYTE_STREAM", "{\"title\":\"流程图\","
					+ "\"nodes\":{"
					+ "\"1607049105657\":"
					+ "{\"name\":\"开始\",\"left\":230,\"top\":60,\"type\":\"start round\",\"width\":26,\"height\":26,\"alt\":true},"
					+ "\"1607049107889\":"
					+ "{\"name\":\"结束\",\"left\":230,\"top\":530,\"type\":\"end round\",\"width\":26,\"height\":26,\"alt\":true}},"
					+ "\"lines\":{},\"areas\":{},\"initNum\":97}");		//流程图JSON
			wpd.put("LAST_MODIFIED_TIME", time);		//修改时间
			wpd.put("LAST_MODIFIER", staffid);		//修改人
			BYTEARRAYService.save(wpd);			//添加工艺流程图
			for(int i=0;i<2;i++) {
				PageData node = new PageData();
				if(i==0) {
					node.put("NODE_ID", "1607049105657");
					node.put("NODE_NAME", "开始");
					node.put("NODE_TYPE", "start round");//task/start round/tb/sl
				} else {
					node.put("NODE_ID", "1607049107889");
					node.put("NODE_NAME", "结束");
					node.put("NODE_TYPE", "end round");
				}
				node.put("NODE_KIND", "node");//node或者line
				node.put("PHASE_TYPE", "");
				node.put("JUMP_CONDITION", "");
				node.put("BEGIN_NODE", "");
				node.put("END_NODE", "");
				node.put("FPARTICIPATOR", "");
				node.put("FDES", "");
				node.put("FCREATOR", staffid);//
				node.put("CREATE_TIME", time);
				node.put("LAST_MODIFIER", staffid);//
				node.put("LAST_MODIFIED_TIME", time);
				node.put("ProcessRoute_ID", ProcessRoute_ID);
				node.put("TECHNOLOGY_FLOW_ID", UuidUtil.get32UUID());
				TECHNOLOGY_FLOWService.save(node);
			}
			/*PageData wpd = new PageData();
			wpd.put("ProcessRouteID", ProcessRoute_ID);				
			wpd.put("WorkingProcedure_ID", this.get32UUID());		//工艺路线工序主键
			wpd.put("SerialNum", "1");		//工艺路线工序主键
			WorkingProcedureService.save(wpd);						//添加工艺工序*/
			pd = ProcessRouteService.findById(pd);					//根据ID读取
			operationrecordService.add("","工艺路线","添加",pd.getString("ProcessRoute_ID"),staffid,"添加工艺路线");
		}
		map.put("ProcessRoute_ID", pd.get("ProcessRoute_ID"));
		map.put("result", errInfo);								//返回结果
		return map;
	}
	
	/**删除
	 * 	删除工艺路线，删除流程图，删除节点，删除工艺工序，删除工艺工序附件，删除工艺工序次品项，
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	//@RequiresPermissions("ProcessRoute:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			pd.put("ProcessRouteID", pd.getString("ProcessRoute_ID"));
			WorkingProcedureService.deleteByProcessRouteID(pd);//删除工艺路线工序及其附件和次品项
			TECHNOLOGY_FLOWService.delete(pd);// 根据工艺路线id及节点id(非必须)删除
			pd.put("FTYPE", "工艺路线");
			pd.put("PID", pd.getString("ProcessRoute_ID"));
			BYTEARRAYService.deleteByPidAndFTYPE(pd);//删除流程图，根据pid和ftype
			ProcessRouteService.delete(pd);//删除工艺路线
			operationrecordService.add("","工艺路线","删除",pd.getString("ProcessRoute_ID"),staffid,pd.toString());
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
	//@RequiresPermissions("ProcessRoute:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData numpd = new PageData();
		numpd = ProcessRouteService.findCount(pd);
		PageData namepd = new PageData();
		namepd = ProcessRouteService.findCountByFName(pd);
		PageData Cabinet_Typepd = new PageData();
		Cabinet_Typepd = ProcessRouteService.findCountByCabinetType(pd);
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		if(numpd != null && Integer.parseInt(numpd.get("zs").toString()) >0) {
			errInfo = "error";//编号重复
			map.put("msg", "编号重复,请修改");
		} else if (namepd != null && Integer.parseInt(namepd.get("zs").toString()) > 0) {
			errInfo = "error";//名称重复
			map.put("msg", "名称重复,请修改");
		}else if (Cabinet_Typepd != null && Integer.parseInt(Cabinet_Typepd.get("zs").toString()) > 0) {
			errInfo = "error";//柜体类型重复
			map.put("msg", "柜体类型重复,请修改");
		} else {
			pd.put("LastModifiedBy", staffid);			//修改人
			pd.put("LastModificationTime", Tools.date2Str(new Date()));
			pd.put("FVersion", Integer.parseInt(pd.get("FVersion").toString())+1);
			pd.put("FStatus", "停用");
			ProcessRouteService.edit(pd);
			operationrecordService.add("","工艺路线","修改",pd.getString("ProcessRoute_ID"),staffid,"修改工艺路线");
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改状态，停用、已发布
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editStatus")
	@ResponseBody
	public Object editStatus() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		pd = this.getPageData();
		//查询该工艺路线是否已被生产bom引用，如果已被引用，不允许停用
		if("未发布".equals(pd.getString("FStatus")) && 
				Integer.parseInt(ProcessRouteService.findCountProductionBomByProcessRouteId(pd).get("zs").toString())>0) {
			errInfo = "error";//存在引用该工艺路线的生产bom，不允许停用
		} else {
			ProcessRouteService.editStatus(pd);//修改状态
			operationrecordService.add("","工艺路线","修改状态",pd.getString("ProcessRoute_ID"),staffid,pd.toString());
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**全部列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	//@RequiresPermissions("ProcessRoute:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData> varList = ProcessRouteService.listAll(pd);	//列出ProcessRoute列表
		map.put("varList", varList);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	//@RequiresPermissions("ProcessRoute:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = ProcessRouteService.list(page);	//列出ProcessRoute列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面,同时查询出隶属工艺路线工序
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	//@RequiresPermissions("ProcessRoute:edit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ProcessRouteService.findById(pd);	//根据ID读取
		//查询对应隶属工序
		if(null!=pd && pd.containsKey("ProcessRoute_ID") && !"".equals(pd.getString("ProcessRoute_ID"))) {
			pd.put("ProcessRouteID", pd.getString("ProcessRoute_ID"));
			List<PageData> list = WorkingProcedureService.listAll(pd);
			pd.put("WorkingProcedureList", list);
		}
		map.put("pd", pd);
		map.put("result", errInfo);						//返回结果
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
		titles.add("编号");	//2
		titles.add("名称");	//3
		titles.add("有效期");	//4
		titles.add("状态");	//5
		titles.add("创建时间");	//6
		titles.add("创建人");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = ProcessRouteService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FNum"));	    //2
			vpd.put("var2", varOList.get(i).getString("FName"));	    //3
			vpd.put("var3", varOList.get(i).getString("TermOfValidity"));	    //4
			vpd.put("var4", varOList.get(i).getString("FStatus"));	    //5
			vpd.put("var5", varOList.get(i).getString("FCreateTime"));	    //6
			vpd.put("var6", varOList.get(i).getString("FCreatePersonID"));	    //7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	@ResponseBody
	public Object readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData staff = new PageData();
		staff.put("FNAME", Jurisdiction.getName());
		String staffid=staffService.getStaffId(staff).getString("STAFF_ID");
		PageData pd = new PageData();
		if (null != file && !file.isEmpty()) {
	        int realRowCount = 0;//真正有数据的行数
	        //得到工作空间
	        Workbook workbook = null;
	        try {
	            workbook = ObjectExcelRead.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        //得到sheet
	        Sheet sheet = ObjectExcelRead.getSheetByWorkbook(workbook, 0);
	        realRowCount = sheet.getPhysicalNumberOfRows();
	        String currentTime = Tools.date2Str(new Date());
	        for(int rowNum = 1;rowNum <= realRowCount;rowNum++) {
	        	Row row = sheet.getRow(rowNum);
	            if(ObjectExcelRead.isBlankRow(row)) {	//空行跳过
	            	break;
	            }
	            if(row.getRowNum() == -1) {
	                continue;
	            }else {
	                if(row.getRowNum() == 0) {//第一行表头跳过
	                    continue;
	                }
	            }
	            pd.put("ProcessRoute_ID", this.get32UUID());
	            pd.put("FNum", ObjectExcelRead.getCellValue(sheet, row, 0));//编码
	            pd.put("FName", ObjectExcelRead.getCellValue(sheet, row, 1));//名称
	            pd.put("FStatus", "创建");//状态
	            pd.put("TermOfValidity", ObjectExcelRead.getCellValue(sheet, row, 2)
	            		+"-"+ObjectExcelRead.getCellValue(sheet, row, 3));//有效期
	            pd.put("FCreateTime", currentTime);//创建时间
	            pd.put("FCreatePersonID", "");//创建人
	            pd.put("LastModificationTime", currentTime);//创建时间
	            ProcessRouteService.save(pd);
	        }
		}
		operationrecordService.add("","工艺路线","导入","",staffid,"");
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getProjectpath() + Const.FILEPATHFILE + "ProcessRoute.xlsx", "ProcessRoute.xlsx");
	}
	
	/**获取工艺路线列表-可搜索-前100条
	 * @author 管悦
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/getRouteList")
	@ResponseBody
	public Object getRouteList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//检索条件-客户名/客户编号
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData>	varList = ProcessRouteService.getRouteList(pd);	//列出Customer列表
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
}
