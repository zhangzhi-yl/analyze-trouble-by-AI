package org.yy.controller.km;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.flow.BYTEARRAYService;
import org.yy.service.flow.NEW_BOMService;
import org.yy.service.flow.TECHNOLOGY_FLOWService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.InputOutputService;
import org.yy.service.km.ProductionBOMService;
import org.yy.service.km.WorkingProcedureDefectiveItemsExampleService;
import org.yy.service.km.WorkingProcedureDefectiveItemsService;
import org.yy.service.km.WorkingProcedureExampleService;
import org.yy.service.km.WorkingProcedureService;
import org.yy.service.mom.OperationRecordService;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.Tools;

/**
 * 说明：生产BOM 作者：YuanYes QQ356703572 时间：2020-11-11 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/ProductionBOM")
public class ProductionBOMController extends BaseController {

	@Autowired
	private ProductionBOMService ProductionBOMService;
	@Autowired
	private CodingRulesService codingrulesService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private WorkingProcedureService WorkingProcedureService;
	@Autowired
	private WorkingProcedureExampleService WorkingProcedureExampleService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	@Autowired
	private InputOutputService InputOutputService;
	@Autowired
	private WorkingProcedureDefectiveItemsExampleService WorkingProcedureDefectiveItemsExampleService;
	@Autowired
	private WorkingProcedureDefectiveItemsService WorkingProcedureDefectiveItemsService;
	@Autowired
	private NEW_BOMService NEW_BOMService;
	@Autowired
	private BYTEARRAYService BYTEARRAYService;
	@Autowired
	private TECHNOLOGY_FLOWService TECHNOLOGY_FLOWService;
	@Autowired
	private StaffService staffService;

	/**
	 * 保存
	 * 
	 * @author 管悦
	 * @date 2020-11-12
	 * @param 投入产出信息、ProcessRouteID：模板工艺路线ID
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	// @RequiresPermissions("ProductionBOM:add")
	@ResponseBody
	public Object add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		// 单号验重
		PageData pdNum = ProductionBOMService.getRepeatNum(pd);
		if (pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) > 0) {
			errInfo = "fail1";// 单号重复
		} else {
			pd.put("ProductionBOM_ID", this.get32UUID()); // 主键
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
			// pd.put("FCreatePersonID", "c3e8a7d350cc43d9b9e87641947168b8");
			pd.put("FCreateTime", Tools.date2Str(new Date()));
			pd.put("FStatus", "未发布");
			ProductionBOMService.save(pd);
			// 实例化工艺工序表
			pd.put("ProcessRouteID", pd.getString("ProcessRouteRel"));
			List<PageData> varList = WorkingProcedureService.listAll(pd); // 列出WorkingProcedure列表
			for (PageData pdGX : varList) {
				pdGX.put("WorkingProcedureExample_ID", this.get32UUID()); // 主键
				pdGX.put("BOM_ID", pd.getString("ProductionBOM_ID")); // 生产BOMID
				pdGX.put("FCreatePersonID", "c3e8a7d350cc43d9b9e87641947168b8");
				pdGX.put("FCreateTime", Tools.date2Str(new Date()));
				pdGX.put("FStatus", "N");
				WorkingProcedureExampleService.save(pdGX);
				// 插入附件集
				PageData pdFj = new PageData();
				pdFj.put("AssociationID", pdGX.getString("WorkingProcedure_ID"));
				List<PageData> findByAssId = attachmentsetService.findByAssId(pdFj);	
				if(CollectionUtil.isNotEmpty(findByAssId)){
					pdFj = findByAssId.get(0);
					if(pdFj != null) {
					// 附件集插入数据
					pdFj.put("DataSources", "工艺工序实例");
					pdFj.put("AssociationIDTable", "KM_WorkingProcedureExample");
					pdFj.put("AssociationID", pdGX.getString("WorkingProcedureExample_ID"));
					pdFj.put("FCreatePersonID", pd.getString("FCreatePersonID"));
					pdFj.put("FCreateTime", Tools.date2Str(new Date()));
					attachmentsetService.check(pdFj);
					}
				}
				List<PageData> varListx = WorkingProcedureDefectiveItemsService.listAll(pdGX); // 列出次品项列表
				for (PageData pdx : varListx) {
					pdx.put("WorkingProcedureDefectiveItemsExample_ID", this.get32UUID()); // 主键
					pdx.put("WorkingProcedureExample_ID", pdGX.getString("WorkingProcedureExample_ID"));
					pdx.put("ProductionBOM_ID", pd.getString("ProductionBOM_ID")); // 生产BOMID
					WorkingProcedureDefectiveItemsExampleService.save(pdx);
				}
			}
			pd.put("ProcessRoute_ID", pd.getString("ProcessRouteRel"));
			List<PageData> flowList = TECHNOLOGY_FLOWService.listAll(pd); // 列出工艺路线流程列表
			for (PageData pdFlow : flowList) {
				pdFlow.put("NEW_BOM_FLOW_ID", this.get32UUID());
				pdFlow.put("ProductionBOM_ID", pd.getString("ProductionBOM_ID"));
				NEW_BOMService.save(pdFlow);
			}
			pd.put("PID", pd.getString("ProcessRouteRel"));
			PageData pdBYTEARRAY = BYTEARRAYService.findByPID(pd);
			pdBYTEARRAY.put("PID", pd.getString("ProductionBOM_ID"));
			pdBYTEARRAY.put("GE_BYTEARRAY_FLOW_ID", this.get32UUID());
			pdBYTEARRAY.put("FTYPE", "生产BOM");
			pdBYTEARRAY.put("FNAME", Jurisdiction.getName());
			pdBYTEARRAY.put("LAST_MODIFIER", staffService.getStaffId(pdBYTEARRAY).getString("STAFF_ID"));// 查询职员ID
			// pdBYTEARRAY.put("LAST_MODIFIER",
			// "c3e8a7d350cc43d9b9e87641947168b8");
			pdBYTEARRAY.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			BYTEARRAYService.save(pdBYTEARRAY);
			// 插入操作日志
			PageData pdOp = new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
			pdOp.put("FOperatorID", pd.get("FCreatePersonID"));// 操作人
			pdOp.put("FunctionType", "");// 功能类型
			pdOp.put("FunctionItem", "生产BOM");// 功能项
			pdOp.put("OperationType", "新增");// 操作类型
			pdOp.put("Fdescribe", "");// 描述
			pdOp.put("DeleteTagID", pd.get("ProductionBOM_ID"));
			operationrecordService.save(pdOp);
		}
		map.put("ProductionBOM_ID", pd.get("ProductionBOM_ID"));
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 保存
	 * 
	 * @author 管悦
	 * @date 2020-11-12
	 * @param 投入产出信息、ProcessRouteID：模板工艺路线ID
	 * @throws Exception
	 */
	@RequestMapping(value = "/goCopy")
	// @RequiresPermissions("ProductionBOM:add")
	@ResponseBody
	public Object goCopy() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String BOM_Num = pd.get("BOM_Num").toString();
		String ProductionBOM_ID = pd.get("ProductionBOM_ID").toString();
		pd= ProductionBOMService.findById(pd);
		pd.put("BOM_Num", BOM_Num);
		Integer version = Integer.parseInt(pd.get("FVersion").toString());
		version = version +1;
		// 单号验重
		PageData pdNum = ProductionBOMService.getRepeatNum(pd);
		if (pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) > 0) {
			errInfo = "fail1";// 单号重复
		} else {
			pd.put("ProductionBOM_ID", this.get32UUID()); // 主键
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
			// pd.put("FCreatePersonID", "c3e8a7d350cc43d9b9e87641947168b8");
			pd.put("FCreateTime", Tools.date2Str(new Date()));
			pd.put("FStatus", "未发布");
			pd.put("FVersion", version);
			ProductionBOMService.save(pd);
			// 实例化工艺工序表
			pd.put("ProcessRouteID", pd.getString("ProcessRouteRel"));
			List<PageData> varList = WorkingProcedureService.listAll(pd); // 列出WorkingProcedure列表
			for (PageData pdGX : varList) {
				pdGX.put("WorkingProcedureExample_ID", this.get32UUID()); // 主键
				pdGX.put("BOM_ID", pd.getString("ProductionBOM_ID")); // 生产BOMID
				pdGX.put("FCreatePersonID", "c3e8a7d350cc43d9b9e87641947168b8");
				pdGX.put("FCreateTime", Tools.date2Str(new Date()));
				pdGX.put("FStatus", "N");
				WorkingProcedureExampleService.save(pdGX);
				PageData pd111 = new PageData();
				pdGX.put("BOM_ID", ProductionBOM_ID);
				List<PageData> listExample = WorkingProcedureExampleService.listAllFlow(pdGX);
				pd111.put("WorkingProcedureExample_ID", listExample.get(0).get("WorkingProcedureExample_ID"));
				pd111.put("FTYPE", "投入");
				List<PageData>	varListInput = InputOutputService.listAll1(pd111);	//列出InputOutput列表
				for(int i=0;i<varListInput.size();i++){
					PageData input = varListInput.get(i);
					input.put("InputOutput_ID", this.get32UUID());
					input.put("WorkingProcedureExample_ID",pdGX.get("WorkingProcedureExample_ID"));
					InputOutputService.save(input);
				}
				pd111.put("FTYPE", "产出");
				List<PageData>	varListOutput = InputOutputService.listAll1(pd111);	//列出InputOutput列表
				for(int i=0;i<varListOutput.size();i++){
					PageData Output = varListOutput.get(i);
					Output.put("InputOutput_ID", this.get32UUID());
					Output.put("WorkingProcedureExample_ID",pdGX.get("WorkingProcedureExample_ID"));
					InputOutputService.save(Output);
				}
				
				
				// 插入附件集
				PageData pdFj = new PageData();
				pdFj.put("AssociationID", pdGX.getString("WorkingProcedure_ID"));
				List<PageData> findByAssId = attachmentsetService.findByAssId(pdFj);	
				if(CollectionUtil.isNotEmpty(findByAssId)){
					pdFj = findByAssId.get(0);
					if(pdFj != null) {
					// 附件集插入数据
					pdFj.put("DataSources", "工艺工序实例");
					pdFj.put("AssociationIDTable", "KM_WorkingProcedureExample");
					pdFj.put("AssociationID", pdGX.getString("WorkingProcedureExample_ID"));
					pdFj.put("FCreatePersonID", pd.getString("FCreatePersonID"));
					pdFj.put("FCreateTime", Tools.date2Str(new Date()));
					attachmentsetService.check(pdFj);
					}
				}
				List<PageData> varListx = WorkingProcedureDefectiveItemsService.listAll(pdGX); // 列出次品项列表
				for (PageData pdx : varListx) {
					pdx.put("WorkingProcedureDefectiveItemsExample_ID", this.get32UUID()); // 主键
					pdx.put("WorkingProcedureExample_ID", pdGX.getString("WorkingProcedureExample_ID"));
					pdx.put("ProductionBOM_ID", pd.getString("ProductionBOM_ID")); // 生产BOMID
					WorkingProcedureDefectiveItemsExampleService.save(pdx);
				}
			}
			pd.put("ProcessRoute_ID", pd.getString("ProcessRouteRel"));
			List<PageData> flowList = TECHNOLOGY_FLOWService.listAll(pd); // 列出工艺路线流程列表
			for (PageData pdFlow : flowList) {
				pdFlow.put("NEW_BOM_FLOW_ID", this.get32UUID());
				pdFlow.put("ProductionBOM_ID", pd.getString("ProductionBOM_ID"));
				NEW_BOMService.save(pdFlow);
			}
			pd.put("PID", pd.getString("ProcessRouteRel"));
			PageData pdBYTEARRAY = BYTEARRAYService.findByPID(pd);
			pdBYTEARRAY.put("PID", pd.getString("ProductionBOM_ID"));
			pdBYTEARRAY.put("GE_BYTEARRAY_FLOW_ID", this.get32UUID());
			pdBYTEARRAY.put("FTYPE", "生产BOM");
			pdBYTEARRAY.put("FNAME", Jurisdiction.getName());
			pdBYTEARRAY.put("LAST_MODIFIER", staffService.getStaffId(pdBYTEARRAY).getString("STAFF_ID"));// 查询职员ID
			// pdBYTEARRAY.put("LAST_MODIFIER",
			// "c3e8a7d350cc43d9b9e87641947168b8");
			pdBYTEARRAY.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
			BYTEARRAYService.save(pdBYTEARRAY);
		}
		map.put("ProductionBOM_ID", pd.get("ProductionBOM_ID"));
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	// @RequiresPermissions("ProductionBOM:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProductionBOMService.delete(pd);
		// 删除实例工序和投入产出
		pd.put("FStatus", "Y");
		WorkingProcedureExampleService.deleteByBomId(pd);
		InputOutputService.rowCloseByBomId(pd);
		// 删除次品项列表
		WorkingProcedureDefectiveItemsExampleService.deleteByBomId(pd);
		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		// pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "生产BOM");// 功能项
		pdOp.put("OperationType", "删除");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", pd.get("ProductionBOM_ID"));
		operationrecordService.save(pdOp);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 修改 FIsChange:是否更换BOM
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	// @RequiresPermissions("ProductionBOM:edit")
	@ResponseBody
	public Object edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FIsChange = pd.getString("FIsChange");
		// 单号验重
		PageData pdNum = ProductionBOMService.getRepeatNum(pd);
		if (pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) > 0) {
			errInfo = "fail1";// 单号重复
		} else {
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
			ProductionBOMService.edit(pd);
			if (FIsChange != null && FIsChange.equals("Y")) {// 更换工艺路线
				pd.put("BOM_ID", pd.getString("ProductionBOM_ID")); // 生产BOMID
				// 删除实例工序和投入产出
				pd.put("FStatus", "Y");
				WorkingProcedureExampleService.deleteByBomId(pd);
				InputOutputService.rowCloseByBomId(pd);
				// 删除次品项列表
				WorkingProcedureDefectiveItemsExampleService.deleteByBomId(pd);
				// 删除实例生产BOM流程列表
				NEW_BOMService.deleteByBomId(pd);
				// 删除流程图文件
				pd.put("PID", pd.getString("ProductionBOM_ID"));
				BYTEARRAYService.deleteByBomId(pd);
				// 实例化工艺工序表
				pd.put("ProcessRouteID", pd.getString("ProcessRouteRel"));
				List<PageData> varList = WorkingProcedureService.listAll(pd); // 列出WorkingProcedure列表
				for (PageData pdGX : varList) {
					pdGX.put("WorkingProcedureExample_ID", this.get32UUID()); // 主键
					pdGX.put("BOM_ID", pd.getString("ProductionBOM_ID")); // 生产BOMID
					pdGX.put("FNAME", Jurisdiction.getName());
					pdGX.put("FCreatePersonID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
					pdGX.put("FCreateTime", Tools.date2Str(new Date()));
					pdGX.put("FStatus", "N");
					WorkingProcedureExampleService.save(pdGX);
					// 插入附件集
					PageData pdFj = new PageData();
					pdFj.put("AssociationID", pdGX.getString("WorkingProcedure_ID"));
					List<PageData> findByAssId = attachmentsetService.findByAssId(pdFj);	
					if(CollectionUtil.isNotEmpty(findByAssId)){
						pdFj = findByAssId.get(0);
						if(pdFj != null) {
						// 附件集插入数据
						pdFj.put("DataSources", "工艺工序实例");
						pdFj.put("AssociationIDTable", "KM_WorkingProcedureExample");
						pdFj.put("AssociationID", pdGX.getString("WorkingProcedureExample_ID"));
						pdFj.put("FCreatePersonID", pd.getString("FCreatePersonID"));
						pdFj.put("FCreateTime", Tools.date2Str(new Date()));
						attachmentsetService.check(pdFj);
						}
					}
					List<PageData> varListx = WorkingProcedureDefectiveItemsService.listAll(pdGX); // 列出次品项列表
					for (PageData pdx : varListx) {
						pdx.put("WorkingProcedureDefectiveItemsExample_ID", this.get32UUID()); // 主键
						pdx.put("WorkingProcedureExample_ID", pdGX.getString("WorkingProcedureExample_ID"));
						pdx.put("ProductionBOM_ID", pd.getString("ProductionBOM_ID")); // 生产BOMID
						WorkingProcedureDefectiveItemsExampleService.save(pdx);
					}
				}
				pd.put("ProcessRoute_ID", pd.getString("ProcessRouteRel"));
				List<PageData> flowList = TECHNOLOGY_FLOWService.listAll(pd); // 列出工艺路线流程列表
				for (PageData pdFlow : flowList) {
					pdFlow.put("NEW_BOM_FLOW_ID", this.get32UUID());
					pdFlow.put("ProductionBOM_ID", pd.getString("ProductionBOM_ID"));
					NEW_BOMService.save(pdFlow);
				}
				pd.put("PID", pd.getString("ProcessRouteRel"));
				PageData pdBYTEARRAY = BYTEARRAYService.findByPID(pd);
				pdBYTEARRAY.put("GE_BYTEARRAY_FLOW_ID", this.get32UUID());
				pdBYTEARRAY.put("FTYPE", "生产BOM");
				pdBYTEARRAY.put("PID", pd.getString("ProductionBOM_ID"));
				pdBYTEARRAY.put("FNAME", Jurisdiction.getName());
				pdBYTEARRAY.put("LAST_MODIFIER", staffService.getStaffId(pdBYTEARRAY).getString("STAFF_ID"));// 查询职员ID
				// pdBYTEARRAY.put("LAST_MODIFIER",
				// "c3e8a7d350cc43d9b9e87641947168b8");
				pdBYTEARRAY.put("LAST_MODIFIED_TIME", Tools.date2Str(new Date()));
				BYTEARRAYService.save(pdBYTEARRAY);
			}
			// 插入操作日志
			PageData pdOp = new PageData();
			pdOp.put("FNAME", Jurisdiction.getName());
			pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
			pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
			pdOp.put("FunctionType", "");// 功能类型
			pdOp.put("FunctionItem", "生产BOM");// 功能项
			pdOp.put("OperationType", "修改");// 操作类型
			pdOp.put("Fdescribe", "");// 描述
			pdOp.put("DeleteTagID", pd.get("ProductionBOM_ID"));
			operationrecordService.save(pdOp);
		}
		ProductionBOMService.edit(pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获取生产BOM列表
	 * 
	 * @author 管悦
	 * @date 2020-11-12
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	// @RequiresPermissions("ProductionBOM:list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_MATCODE = pd.getString("KEYWORDS_MATCODE"); // 成品物料编号
		if (Tools.notEmpty(KEYWORDS_MATCODE))
			pd.put("KEYWORDS_MATCODE", KEYWORDS_MATCODE.trim());
		String KEYWORDS_MATNAME = pd.getString("KEYWORDS_MATNAME"); // 成品物料名称
		if (Tools.notEmpty(KEYWORDS_MATNAME))
			pd.put("KEYWORDS_MATNAME", KEYWORDS_MATNAME.trim());
		String KEYWORDS_FVersion = pd.getString("KEYWORDS_FVersion"); // 完整版本号
		if (Tools.notEmpty(KEYWORDS_FVersion))
			pd.put("KEYWORDS_FVersion", KEYWORDS_FVersion.trim());
		page.setPd(pd);
		List<PageData> varList = ProductionBOMService.list(page); // 列出ProductionBOM列表
		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "生产BOM");// 功能项
		pdOp.put("OperationType", "查询");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", "");// 删改数据ID
		operationrecordService.save(pdOp);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 去修改页面获取数据
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	// @RequiresPermissions("ProductionBOM:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = ProductionBOMService.findById(pd); // 根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 批量删除
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAll")
	@RequiresPermissions("ProductionBOM:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			ProductionBOMService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		} else {
			errInfo = "error";
		}
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("BOM编号"); // 1
		titles.add("成品物料编号"); // 2
		titles.add("规格描述"); // 3
		titles.add("数量"); // 4
		titles.add("单位"); // 5
		titles.add("版本号"); // 6
		titles.add("有效期"); // 7
		titles.add("物料清单版本号"); // 8
		titles.add("组件分配"); // 9
		titles.add("关联工艺路线"); // 10
		titles.add("备注"); // 11
		titles.add("创建人"); // 12
		titles.add("创建时间"); // 13
		titles.add("发布状态(未发布,已发布)"); // 14
		dataMap.put("titles", titles);
		List<PageData> varOList = ProductionBOMService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BOM_Num")); // 1
			vpd.put("var2", varOList.get(i).getString("FMNum")); // 2
			vpd.put("var3", varOList.get(i).getString("SpecificationsDesc")); // 3
			vpd.put("var4", varOList.get(i).get("FCount").toString()); // 4
			vpd.put("var5", varOList.get(i).getString("FUnit")); // 5
			vpd.put("var6", varOList.get(i).getString("FVersion")); // 6
			vpd.put("var7", varOList.get(i).getString("TermOfValidity")); // 7
			vpd.put("var8", varOList.get(i).getString("BOMVersion")); // 8
			vpd.put("var9", varOList.get(i).getString("ComponentAllocation")); // 9
			vpd.put("var10", varOList.get(i).getString("ProcessRouteRel")); // 10
			vpd.put("var11", varOList.get(i).getString("FExplanation")); // 11
			vpd.put("var12", varOList.get(i).getString("FCreatePersonID")); // 12
			vpd.put("var13", varOList.get(i).getString("FCreateTime")); // 13
			vpd.put("var14", varOList.get(i).getString("FStatus")); // 14
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 获取BOM列表-可搜索-前100条
	 * 
	 * @author 管悦
	 * @date 2020-11-06
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value = "/getBOMList")
	@ResponseBody
	public Object getBOMList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS"); // 检索条件-客户名/客户编号
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData> varList = ProductionBOMService.getBOMList(pd); //
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 发布/停用
	 * 
	 * @author 管悦
	 * @date 2020-11-12
	 * @param {
	 *            ProductionBOM_ID:生产BOM ID FStatus:发布状态 }
	 * @throws Exception
	 */
	@RequestMapping(value = "/release")
	@ResponseBody
	public Object release() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		ProductionBOMService.release(pd);
		// 插入操作日志

		PageData pdOp = new PageData();
		String FStatus = pd.getString("FStatus");
		if (FStatus.equals("已发布")) {
			pdOp.put("OperationType", "发布");// 操作类型
		} else {
			pdOp.put("OperationType", "停用");// 操作类型
		}
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "生产BOM");// 功能项
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", pd.get("PurchaseList_ID"));// 删改数据ID
		operationrecordService.save(pdOp);
		map.put("result", errInfo);
		return map;
	}
}
