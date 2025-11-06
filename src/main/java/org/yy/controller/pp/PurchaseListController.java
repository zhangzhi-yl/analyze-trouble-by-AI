package org.yy.controller.pp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.DepartmentService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.mom.OperationRecordService;
import org.yy.service.pp.PurchaseListService;
import org.yy.service.pp.PurchaseList_CommentsService;
import org.yy.service.pp.PurchaseMaterialDetailsService;
import org.yy.service.project.manager.Cabinet_AssemblyService;
import org.yy.service.system.UsersService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileUpload;
import org.yy.util.Jurisdiction;
import org.yy.util.ObjectExcelView;
import org.yy.util.PathUtil;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;
import org.yy.util.weixin.SendWeChatMessageMes;

/**
 * 说明：采购订单 作者：YuanYes QQ356703572 时间：2020-11-09 官网：356703572@qq.com
 */
@Controller
@RequestMapping("/PurchaseList")
public class PurchaseListController extends BaseController {

	@Autowired
	private PurchaseListService PurchaseListService;
	@Autowired
	private OperationRecordService operationrecordService;
	@Autowired
	private PurchaseMaterialDetailsService PurchaseMaterialDetailsService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	@Autowired
	private MaterialRequirementService MaterialRequirementService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PurchaseList_CommentsService PurchaseList_CommentsService;// 审核意见
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private UsersService usersService;
	private Cabinet_AssemblyService Cabinet_AssemblyService;
	@Autowired
	private NoticeService noticeService;

	/**
	 * 保存
	 * 
	 * @author 管悦
	 * @date 2020-11-09
	 * @param FFILEPATH:文件、清单信息
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	// @RequiresPermissions("dtprojectfile:add")
	@ResponseBody
	public Object add(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			// 单号验重
			PageData pdNum = PurchaseListService.getRepeatNum(pd);
			if (pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) > 0) {
				errInfo = "fail1";// 单号重复
			} else {
				pd.put("FNAME", Jurisdiction.getName());
				String staffID = staffService.getStaffId(pd).getString("STAFF_ID");
				String purchaseList_id = this.get32UUID();
				pd.put("PurchaseList_ID", purchaseList_id); // 主键
				pd.put("FMakeBillsPersoID", staffID);// 查询职员ID
				// pd.put("FMakeBillsPersoID",
				// "c3e8a7d350cc43d9b9e87641947168b8");
				pd.put("FMakeBillsTime", Tools.date2Str(new Date()));
				pd.put("FStatus", "创建");
				pd.put("FCheckFlag", "N");
				PurchaseListService.save(pd);
				// 添加审批意见
				PageData cpd = new PageData();
				cpd.put("PurchaseList_Comments_ID", this.get32UUID()); // 主键
				cpd.put("BillID", purchaseList_id); // 操作人
				cpd.put("FStatus", "创建"); // 操作人
				cpd.put("FComments", "无"); // 操作人
				cpd.put("FOperator", staffID); // 操作人
				cpd.put("OperationTime", Tools.date2Str(new Date())); // 操作时间
				PurchaseList_CommentsService.save(cpd);
				// 上传附件
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar calendar = Calendar.getInstance();
				String dateName = df.format(calendar.getTime());
				String ffile = DateUtil.getDays();
				String FFILENAME = "";
				String FPFFILEPATH = "";
				if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
					String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
					String fileNamereal = pd.getString("FFILENAME").substring(0,
							pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
					FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal + dateName);// 执行上传
					FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
					// 附件集插入数据
					PageData pdFile = new PageData();
					pdFile.put("DataSources", "采购订单");
					pdFile.put("AssociationIDTable", "PP_PurchaseList");
					pdFile.put("AssociationID", pd.getString("PurchaseList_ID"));
					pdFile.put("FName", FFILENAME);
					pdFile.put("FUrl", FPFFILEPATH);
					pdFile.put("FExplanation", "");
					pdFile.put("FCreatePersonID", pd.getString("FMakeBillsPersoID"));
					pdFile.put("FCreateTime", Tools.date2Str(new Date()));
					attachmentsetService.check(pdFile);
				}
				map.put("PurchaseList_ID", purchaseList_id);
			}
			// 插入操作日志
			PageData pdOp = new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
			pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));// 操作人
			pdOp.put("FunctionType", "");// 功能类型
			pdOp.put("FunctionItem", "采购订单");// 功能项
			pdOp.put("OperationType", "新增");// 操作类型
			pdOp.put("Fdescribe", "");// 描述
			pdOp.put("DeleteTagID", pd.get("PurchaseList_ID"));// 删改数据ID
			operationrecordService.save(pdOp);
		} catch (Exception e) {
			errInfo = "error";
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 删除采购订单
	 * 
	 * @author 管悦
	 * @date 2020-11-09
	 * @param PurchaseList_ID:采购订单ID
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	// @RequiresPermissions("salesorder:del")
	@ResponseBody
	public Object delete() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseListService.delete(pd);// 删除采购订单
		pd.put("RowClose", "Y");
		pd.put("PurchaseID", pd.getString("PurchaseList_ID"));
		PurchaseMaterialDetailsService.deleteMxRelated(pd);// 关联行关闭采购订单明细
		pd.put("BillID", pd.getString("PurchaseList_ID"));
		PurchaseList_CommentsService.deleteByBillID(pd);// 删除对应单据的所有审核评论
		// 反写计划工单状态
		pd.put("PushDownPurchaseIF", "N");// 源单下推状态
		MaterialRequirementService.updateStateAll(pd);// 一键反写源单下推状态
		// 插入操作日志
		PageData pdOp = new PageData();
		pdOp.put("FNAME", Jurisdiction.getName());
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FOperatorID", staffService.getStaffId(pdOp).getString("STAFF_ID"));// 操作人
		// pdOp.put("FOperatorID", "c3e8a7d350cc43d9b9e87641947168b8");
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "采购订单");// 功能项
		pdOp.put("OperationType", "删除");// 操作类型
		pdOp.put("Fdescribe", "删除采购订单并行关闭清单明细");// 描述
		pdOp.put("DeleteTagID", pd.get("PurchaseList_ID"));// 删改数据ID
		operationrecordService.save(pdOp);
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 修改
	 * 
	 * @author 管悦
	 * @date 2020-11-11
	 * @param FFILEPATH:文件、清单信息、FTYPE:是否更换附件（Y、N）
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(@RequestParam(value = "FFILEPATH", required = false) MultipartFile FFILEPATH) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FTYPE = pd.getString("FTYPE");
		try {
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
			// 单号验重
			PageData pdNum = PurchaseListService.getRepeatNum(pd);
			if (pdNum != null && Integer.parseInt(pdNum.get("RepeatNum").toString()) > 0) {
				errInfo = "fail1";// 单号重复
			} else {
				PurchaseListService.edit(pd);
				if (FTYPE.equals("Y")) {
					// 上传附件
					DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					Calendar calendar = Calendar.getInstance();
					String dateName = df.format(calendar.getTime());
					String ffile = DateUtil.getDays();
					String FFILENAME = "";
					String FPFFILEPATH = "";
					if (null != FFILEPATH && !FFILEPATH.isEmpty()) {
						String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile; // 文件上传路径
						String fileNamereal = pd.getString("FFILENAME").substring(0,
								pd.getString("FFILENAME").indexOf(".")); // 文件上传路径
						FFILENAME = FileUpload.fileUp(FFILEPATH, filePath, fileNamereal + dateName);// 执行上传
						FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
						// 附件集插入数据
						PageData pdFile = new PageData();
						pdFile.put("DataSources", "采购订单");
						pdFile.put("AssociationIDTable", "PP_PurchaseList");
						pdFile.put("AssociationID", pd.getString("PurchaseList_ID"));
						pdFile.put("FName", FFILENAME);
						pdFile.put("FUrl", FPFFILEPATH);
						pdFile.put("FExplanation", "");
						pdFile.put("FCreatePersonID", pd.getString("FMakeBillsPersoID"));
						pdFile.put("FCreateTime", Tools.date2Str(new Date()));
						attachmentsetService.check(pdFile);
					}

				}
			}
			// 插入操作日志

			PageData pdOp = new PageData();
			pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
			pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
			pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));// 操作人
			pdOp.put("FunctionType", "");// 功能类型
			pdOp.put("FunctionItem", "采购订单");// 功能项
			pdOp.put("OperationType", "修改");// 操作类型
			pdOp.put("Fdescribe", "");// 描述
			pdOp.put("DeleteTagID", pd.get("PurchaseList_ID"));// 删改数据ID
			operationrecordService.save(pdOp);
		} catch (Exception e) {
			errInfo = "error";
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获取采购订单列表
	 * 
	 * @author 管悦
	 * @date 2020-11-09
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	// @RequiresPermissions("PurchaseList:list")
	@ResponseBody
	public Object list(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_FNum = pd.getString("KEYWORDS_FNum"); // 采购申请编号
		if (Tools.notEmpty(KEYWORDS_FNum))
			pd.put("KEYWORDS_FNum", KEYWORDS_FNum.trim());
		String KEYWORDS_FStatus = pd.getString("KEYWORDS_FStatus"); // 状态
		if (Tools.notEmpty(KEYWORDS_FStatus))
			pd.put("KEYWORDS_FStatus", KEYWORDS_FStatus.trim());
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum"); // 销售订单编号
		if (Tools.notEmpty(KEYWORDS_OrderNum))
			pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		String KEYWORDS_WorkOrderNum = pd.getString("KEYWORDS_WorkOrderNum"); // 计划工单编号
		if (Tools.notEmpty(KEYWORDS_WorkOrderNum))
			pd.put("KEYWORDS_WorkOrderNum", KEYWORDS_WorkOrderNum.trim());
		String KEYWORDS_Supplier = pd.getString("KEYWORDS_Supplier"); // 供应商编号/名称
		if (Tools.notEmpty(KEYWORDS_Supplier))
			pd.put("KEYWORDS_Supplier", KEYWORDS_Supplier.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum"); // 物料编号
		if (Tools.notEmpty(KEYWORDS_MaterialNum))
			pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName"); // 物料名称
		if (Tools.notEmpty(KEYWORDS_MaterialName))
			pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		String KEYWORDS_FMakeBillsTimeStart = pd.getString("KEYWORDS_FMakeBillsTimeStart"); // 创建开始时间
		if (Tools.notEmpty(KEYWORDS_FMakeBillsTimeStart))
			pd.put("KEYWORDS_FMakeBillsTimeStart", KEYWORDS_FMakeBillsTimeStart.trim());
		String KEYWORDS_FMakeBillsTimeEnd = pd.getString("KEYWORDS_FMakeBillsTimeEnd"); // 创建结束时间
		if (Tools.notEmpty(KEYWORDS_FMakeBillsTimeEnd))
			pd.put("KEYWORDS_FMakeBillsTimeEnd", KEYWORDS_FMakeBillsTimeEnd.trim());
		String KEYWORDS_DeMandTimeStart = pd.getString("KEYWORDS_DeMandTimeStart"); // 需求开始时间
		if (Tools.notEmpty(KEYWORDS_DeMandTimeStart))
			pd.put("KEYWORDS_DeMandTimeStart", KEYWORDS_DeMandTimeStart.trim());
		String KEYWORDS_DeMandTimeEnd = pd.getString("KEYWORDS_DeMandTimeEnd"); // 需求结束时间
		if (Tools.notEmpty(KEYWORDS_DeMandTimeEnd))
			pd.put("KEYWORDS_DeMandTimeEnd", KEYWORDS_DeMandTimeEnd.trim());
		String KEYWORDS_EstimatedTimeOfArrivalStart = pd.getString("KEYWORDS_EstimatedTimeOfArrivalStart"); // 预计到达开始时间
		if (Tools.notEmpty(KEYWORDS_EstimatedTimeOfArrivalStart))
			pd.put("KEYWORDS_EstimatedTimeOfArrivalStart", KEYWORDS_EstimatedTimeOfArrivalStart.trim());
		String KEYWORDS_EstimatedTimeOfArrivalEnd = pd.getString("KEYWORDS_EstimatedTimeOfArrivalEnd"); // 预计到达结束时间
		if (Tools.notEmpty(KEYWORDS_EstimatedTimeOfArrivalEnd))
			pd.put("KEYWORDS_EstimatedTimeOfArrivalEnd", KEYWORDS_EstimatedTimeOfArrivalEnd.trim());
		page.setPd(pd);
		List<PageData> varList = PurchaseListService.list(page); // 列出PurchaseList列表
		// 插入操作日志
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
		// pd.put("FMakeBillsPersoID", "c3e8a7d350cc43d9b9e87641947168b8");
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "采购订单");// 功能项
		pdOp.put("OperationType", "查询");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", "");// 删改数据ID
		operationrecordService.save(pdOp);
		map.put("userId", pd.getString("FMakeBillsPersoID"));
		map.put("CREATE_NAME", Jurisdiction.getName());
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 项目看板-采购待采购
	 * 
	 * @author 管悦
	 * @date 2020-11-09
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value = "/listXMCG")
	// @RequiresPermissions("PurchaseList:list")
	@ResponseBody
	public Object listXMCG(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS_FNum = pd.getString("KEYWORDS_FNum"); // 采购申请编号
		if (Tools.notEmpty(KEYWORDS_FNum))
			pd.put("KEYWORDS_FNum", KEYWORDS_FNum.trim());
		String KEYWORDS_FStatus = pd.getString("KEYWORDS_FStatus"); // 状态
		if (Tools.notEmpty(KEYWORDS_FStatus))
			pd.put("KEYWORDS_FStatus", KEYWORDS_FStatus.trim());
		String KEYWORDS_OrderNum = pd.getString("KEYWORDS_OrderNum"); // 销售订单编号
		if (Tools.notEmpty(KEYWORDS_OrderNum))
			pd.put("KEYWORDS_OrderNum", KEYWORDS_OrderNum.trim());
		String KEYWORDS_WorkOrderNum = pd.getString("KEYWORDS_WorkOrderNum"); // 计划工单编号
		if (Tools.notEmpty(KEYWORDS_WorkOrderNum))
			pd.put("KEYWORDS_WorkOrderNum", KEYWORDS_WorkOrderNum.trim());
		String KEYWORDS_Supplier = pd.getString("KEYWORDS_Supplier"); // 供应商编号/名称
		if (Tools.notEmpty(KEYWORDS_Supplier))
			pd.put("KEYWORDS_Supplier", KEYWORDS_Supplier.trim());
		String KEYWORDS_MaterialNum = pd.getString("KEYWORDS_MaterialNum"); // 物料编号
		if (Tools.notEmpty(KEYWORDS_MaterialNum))
			pd.put("KEYWORDS_MaterialNum", KEYWORDS_MaterialNum.trim());
		String KEYWORDS_MaterialName = pd.getString("KEYWORDS_MaterialName"); // 物料名称
		if (Tools.notEmpty(KEYWORDS_MaterialName))
			pd.put("KEYWORDS_MaterialName", KEYWORDS_MaterialName.trim());
		String KEYWORDS_FMakeBillsTimeStart = pd.getString("KEYWORDS_FMakeBillsTimeStart"); // 创建开始时间
		if (Tools.notEmpty(KEYWORDS_FMakeBillsTimeStart))
			pd.put("KEYWORDS_FMakeBillsTimeStart", KEYWORDS_FMakeBillsTimeStart.trim());
		String KEYWORDS_FMakeBillsTimeEnd = pd.getString("KEYWORDS_FMakeBillsTimeEnd"); // 创建结束时间
		if (Tools.notEmpty(KEYWORDS_FMakeBillsTimeEnd))
			pd.put("KEYWORDS_FMakeBillsTimeEnd", KEYWORDS_FMakeBillsTimeEnd.trim());
		String KEYWORDS_DeMandTimeStart = pd.getString("KEYWORDS_DeMandTimeStart"); // 需求开始时间
		if (Tools.notEmpty(KEYWORDS_DeMandTimeStart))
			pd.put("KEYWORDS_DeMandTimeStart", KEYWORDS_DeMandTimeStart.trim());
		String KEYWORDS_DeMandTimeEnd = pd.getString("KEYWORDS_DeMandTimeEnd"); // 需求结束时间
		if (Tools.notEmpty(KEYWORDS_DeMandTimeEnd))
			pd.put("KEYWORDS_DeMandTimeEnd", KEYWORDS_DeMandTimeEnd.trim());
		String KEYWORDS_EstimatedTimeOfArrivalStart = pd.getString("KEYWORDS_EstimatedTimeOfArrivalStart"); // 预计到达开始时间
		if (Tools.notEmpty(KEYWORDS_EstimatedTimeOfArrivalStart))
			pd.put("KEYWORDS_EstimatedTimeOfArrivalStart", KEYWORDS_EstimatedTimeOfArrivalStart.trim());
		String KEYWORDS_EstimatedTimeOfArrivalEnd = pd.getString("KEYWORDS_EstimatedTimeOfArrivalEnd"); // 预计到达结束时间
		if (Tools.notEmpty(KEYWORDS_EstimatedTimeOfArrivalEnd))
			pd.put("KEYWORDS_EstimatedTimeOfArrivalEnd", KEYWORDS_EstimatedTimeOfArrivalEnd.trim());
		pd.put("FNAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData> varList = PurchaseListService.listXMCG(page); // 列出PurchaseList列表
		// 插入操作日志

		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
		// pd.put("FMakeBillsPersoID", "c3e8a7d350cc43d9b9e87641947168b8");
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "采购订单");// 功能项
		pdOp.put("OperationType", "查询");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", "");// 删改数据ID
		operationrecordService.save(pdOp);
		map.put("userId", pd.getString("FMakeBillsPersoID"));
		map.put("CREATE_NAME", Jurisdiction.getName());
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据ID获取采购订单详情
	 * 
	 * @author 管悦
	 * @date 2020-11-09
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	// @RequiresPermissions("PurchaseList:edit")
	@ResponseBody
	public Object goEdit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = PurchaseListService.findById(pd); // 根据ID读取
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
	@RequiresPermissions("PurchaseList:del")
	@ResponseBody
	public Object deleteAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (Tools.notEmpty(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			PurchaseListService.deleteAll(ArrayDATA_IDS);
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
		titles.add("交货地点"); // 1
		titles.add("编号"); // 2
		titles.add("处理人ID"); // 3
		titles.add("供应商ID"); // 4
		titles.add("预计交货时间"); // 5
		titles.add("制单人"); // 6
		titles.add("制单时间"); // 7
		titles.add("备注"); // 8
		titles.add("状态"); // 9
		titles.add("审核人"); // 10
		titles.add("审核时间"); // 11
		titles.add("审核标志"); // 12
		titles.add("客户"); // 13
		titles.add("源单类型"); // 14
		titles.add("关联ID"); // 15
		dataMap.put("titles", titles);
		List<PageData> varOList = PurchaseListService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("DeliveryPlace")); // 1
			vpd.put("var2", varOList.get(i).getString("FNum")); // 2
			vpd.put("var3", varOList.get(i).getString("FOperatorID")); // 3
			vpd.put("var4", varOList.get(i).getString("SupplierID")); // 4
			vpd.put("var5", varOList.get(i).getString("EstimatedTimeOfArrival")); // 5
			vpd.put("var6", varOList.get(i).getString("FMakeBillsPersoID")); // 6
			vpd.put("var7", varOList.get(i).getString("FMakeBillsTime")); // 7
			vpd.put("var8", varOList.get(i).getString("FExplanation")); // 8
			vpd.put("var9", varOList.get(i).getString("FStatus")); // 9
			vpd.put("var10", varOList.get(i).getString("FCheckPersonID")); // 10
			vpd.put("var11", varOList.get(i).getString("FCheckTime")); // 11
			vpd.put("var12", varOList.get(i).getString("FCheckFlag")); // 12
			vpd.put("var13", varOList.get(i).getString("FCustomer")); // 13
			vpd.put("var14", varOList.get(i).getString("SourceOrderType")); // 14
			vpd.put("var15", varOList.get(i).getString("RelationID")); // 15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 审核或反审核采购订单
	 * 
	 * 四种状态： 1，创建：添加单据后 2，已申请：申请人自审 3，审批通过：领导审批 4，结束：申请人点击结束按钮，单据结束
	 * 
	 * @author 管悦
	 * @date 2020-11-09
	 * @param PurchaseList_ID:采购订单ID、FCheckFlag:审核标志（Y、N）
	 * @throws Exception
	 */
	@RequestMapping(value = "/editAudit")
	@ResponseBody
	public Object editAudit() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FCheckPersonID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
		pd.put("FCheckTime", Tools.date2Str(new Date()));
		String OperationType = "";
		if ("已申请".equals(pd.getString("FStatus"))) {
			OperationType = "自审完毕";
			pd.put("FComments", OperationType);
			pd.put("FCheckFlag", "N");
		}
		if ("创建".equals(pd.getString("FStatus"))) {
			OperationType = "反审核";
			pd.put("FComments", OperationType);
		}
		if ("审批通过".equals(pd.getString("FStatus")))
			OperationType = "领导审核";
		if ("结束".equals(pd.getString("FStatus"))) {
			OperationType = "结束";
			pd.put("FComments", OperationType);
		}
		// 保存审核意见
		PageData commentpd = new PageData();
		commentpd.put("PurchaseList_Comments_ID", UuidUtil.get32UUID());
		commentpd.put("BillID", pd.getString("PurchaseList_ID"));
		commentpd.put("FStatus", pd.getString("FStatus"));
		commentpd.put("FComments", pd.getString("FComments"));
		commentpd.put("FOperator", pd.getString("FCheckPersonID"));
		commentpd.put("OperationTime", pd.getString("FCheckTime"));
		PurchaseList_CommentsService.save(commentpd);
		PurchaseListService.editAudit(pd);
		operationrecordService.add("", "采购订单", OperationType, pd.getString("PurchaseList_ID"),
				pd.getString("FCheckPersonID"), "");
		map.put("result", errInfo); // 返回结果
		return map;
	}

	/**
	 * 结束
	 * 
	 * @author 管悦
	 * @date 2020-11-09
	 * @param PurchaseList_ID:采购订单主键、FStatus:状态
	 * @throws Exception
	 */
	@RequestMapping(value = "/over")
	// @RequiresPermissions("PurchaseList:edit")
	@ResponseBody
	public Object over() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		PurchaseListService.over(pd);
		// 插入操作日志
		pd.put("FNAME", Jurisdiction.getName());
		pd.put("FMakeBillsPersoID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
		// pd.put("FMakeBillsPersoID", "c3e8a7d350cc43d9b9e87641947168b8");
		PageData pdOp = new PageData();
		pdOp.put("OperationRecord_ID", this.get32UUID()); // 主键
		pdOp.put("FOperateTime", Tools.date2Str(new Date())); // 操作时间
		pdOp.put("FOperatorID", pd.get("FMakeBillsPersoID"));// 操作人
		pdOp.put("FunctionType", "");// 功能类型
		pdOp.put("FunctionItem", "采购订单");// 功能项
		pdOp.put("OperationType", "结束");// 操作类型
		pdOp.put("Fdescribe", "");// 描述
		pdOp.put("DeleteTagID", pd.get("PurchaseList_ID"));// 删改数据ID
		operationrecordService.save(pdOp);
		// 保存审核意见
		PageData commentpd = new PageData();
		commentpd.put("PurchaseList_Comments_ID", UuidUtil.get32UUID());
		commentpd.put("BillID", pd.getString("PurchaseList_ID"));
		commentpd.put("FStatus", pd.getString("FStatus"));
		commentpd.put("FComments", "完毕");
		commentpd.put("FOperator", pdOp.getString("FOperatorID"));
		commentpd.put("OperationTime", pdOp.getString("FOperateTime"));
		PurchaseList_CommentsService.save(commentpd);

		PageData pdx = PurchaseListService.findById(pd);
		PageData pdNotice1 = new PageData();

		// 跳转页面
		pdNotice1.put("AccessURL", "../../../views/mm/CallMaterial/cangku/CallMaterial_list.html");//
		pdNotice1.put("NOTICE_ID", this.get32UUID()); // 主键
		pdNotice1.put("ReadPeople", ",");// 已读人默认空
		pdNotice1.put("FIssuedID", Jurisdiction.getName()); // 发布人
		pdNotice1.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
		pdNotice1.put("TType", "消息推送");// 消息类型
		pdNotice1.put("FContent", "项目编号为" + pdx.getString("PPROJECT_CODE") + "，项目名称为" + pdx.getString("PPROJECT_NAME")
				+ "，采购编号为" + pdx.getString("FNum") + "的采购单已到货");// 消息正文
		pdNotice1.put("FTitle", "到货通知");// 消息标题
		pdNotice1.put("LinkIf", "no");// 是否跳转页面
		pdNotice1.put("DataSources", "到货通知");// 数据来源
		pdNotice1.put("ReceivingAuthority", ",KF,");// 接收人
		pdNotice1.put("Report_Key", "task");// 手机app
		pdNotice1.put("Report_Value", "");// 手机app
		noticeService.save(pdNotice1);
		SendWeChatMessageMes weChat = new SendWeChatMessageMes();
		PageData pd3 = new PageData();
		// String Name = Jurisdiction.getName();
		String Name = "管悦";
		pd3.put("NAME", Name);
		// PageData pd2 = usersService.getPhone(pd3);
		String phone = "";
		/*
		 * if(null!=pd2){ phone = pd2.getString("phone"); }else{ phone=""; }
		 */
		String WXNR = "【到货通知】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间：" + Tools.date2Str(new Date())
				+ "\r\n" + "消息内容：" + pdNotice1.get("FContent");
		// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
		// "0");
		weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获取采购订单编号列表-可搜索-前100条
	 * 
	 * @author 管悦
	 * @date 2020-11-15
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFNumList")
	@ResponseBody
	public Object getOrderNumList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		List<PageData> varList = PurchaseListService.getFNumList(pd);
		map.put("varList", varList);
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获得启动审核准备信息
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/setStarts")
	@ResponseBody
	public Object setStarts() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DEPARTMENT_ID", Jurisdiction.getDEPARTMENT_ID());
		PageData pdBack = new PageData();
		PageData pdDept = departmentService.findById(pd);
		pdBack.put("SrartKeyId", "KEY_PurchaseList");
		String headmans = pdDept.get("HEADMAN") == null ? "" : pdDept.getString("HEADMAN");
		String strs[] = headmans.split("、");
		for (int j = 0; j < strs.length; j++) {
			pd.put("NAME", strs[j]);
			PageData pdUser = usersService.findUser(pd);// 根据姓名查用户表
			if (pdUser != null) {
				pdBack.put("ProjectGL" + (j + 1), pdUser.get("USERNAME"));
			}
		}
		if (strs.length > 0) {
			pd.put("FNAME", Jurisdiction.getName());
			pd.put("FCheckPersonID", staffService.getStaffId(pd).getString("STAFF_ID"));// 查询职员ID
			pd.put("FCheckTime", Tools.date2Str(new Date()));
			pd.put("FCheckFlag", "N");
			pd.put("FStatus", "已申请");
			PurchaseListService.editAudit(pd);
		} else {
			errInfo = "fail1";
		}
		map.put("pd", pdBack);
		map.put("result", errInfo);
		return map;
	}
}
