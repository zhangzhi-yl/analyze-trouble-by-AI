package org.yy.controller.app;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yy.controller.base.BaseController;
import org.yy.entity.AppResult;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.km.AttachmentSetService;
import org.yy.service.km.MaterialConsumeService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mbase.MAT_SPECService;
import org.yy.service.mm.MAKECODEService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.Unit_InfoService;
import org.yy.service.mom.WC_StationService;
import org.yy.service.mom.WorkStationPersonRelService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessRecordService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.pp.ProcessWorkOrderExample_SopStepService;
import org.yy.service.pp.WorkorderProcessIOExampleService;
import org.yy.service.system.DictionariesService;
import org.yy.util.Const;
import org.yy.util.DateUtil;
import org.yy.util.FileUpload;
import org.yy.util.PathUtil;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

/**
 * 手机端生产任务接口
 * 
 * @author CCG
 *
 */
@RestController
@RequestMapping("/appProduceTask")
public class AppProduceTaskController extends BaseController {
	@Autowired
	private WC_StationService WC_StationService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PlanningWorkOrderService planningWorkOrderService;
	@Autowired
	private WorkorderProcessIOExampleService workorderProcessIOExampleService;
	@Autowired
	private MAT_BASICService matBasicService;
	@Autowired
	private MaterialConsumeService MaterialConsumeService;
	@Autowired
	private ProcessWorkOrderExampleService processWorkOrderExampleService;
	@Autowired
	private AttachmentSetService attachmentsetService;
	@Autowired
	private ProcessRecordService processRecordService;
	@Autowired
	private StockService stockService;
	@Autowired
	private WC_StationService stationService;
	@Autowired
	private StockService StockService;
	@Autowired
	private Unit_InfoService Unit_InfoService;
	@Autowired
	private MAT_AUXILIARYMxService MAT_AUXILIARYMxService;
	@Autowired
	private MAKECODEService MAKECODEService;
	@Autowired
	private MAT_SPECService MAT_SPECService;
	@Autowired
	private ProcessWorkOrderExample_SopStepService pwoeSopStepService;
	@Autowired
	private DictionariesService DictionariesService;
	@Autowired
	private WorkStationPersonRelService WorkStationPersonRelService;
	/**
	 * 根据登录员工获取 任务列表 TaskType = 1 生产任务 TaskType = 2 返工任务
	 * 
	 * @param page
	 * @param response
	 * @return
	 */
	@RequestMapping("/listByStaffId")
	public Object listByStaffId(AppPage page) {
		try {
			PageData pd = new PageData();
			List<PageData> varList = Lists.newArrayList();
			pd = this.getPageData();
			String USERNAME = pd.getString("UserName");
			// 获取数据
			if (null != pd) {
				if (Tools.notEmpty(USERNAME)) {
					PageData staffParam = new PageData();
					staffParam.put("USERNAME", USERNAME);
					// 根据 user name 获取 staff id
					PageData staff = staffService.findById(staffParam);
					if (null != staff) {
						// 根据staff id 获取 生产任务 状态为未执行的
						String STAFF_ID = staff.getString("STAFF_ID");
						pd.put("STAFF_ID", STAFF_ID);
						// 默认 生产任务 如果传了 task type 2 为 返工任务
						String TaskType = "1";
						TaskType = Tools.notEmpty(pd.getString("TaskType")) ? pd.getString("TaskType") : TaskType;
						pd.put("TaskType", TaskType);

						String orderby = "TaskNum";
						if (Tools.notEmpty(pd.getString("orderby"))) {
							orderby = pd.getString("orderby");
						}
						String sort = "asc";
						if ("desc".equals(pd.getString("sort"))) {
							sort = "desc";
						}
					
						PageData stationPersonRel = new PageData();
						stationPersonRel.put("PersonId", STAFF_ID);
						List<PageData> WorkStationPersonRelList = WorkStationPersonRelService.listAll(stationPersonRel);
						List<String> stationIDList = Lists.newArrayList();
						for (PageData WorkStationPersonRel : WorkStationPersonRelList) {
							stationIDList.add(WorkStationPersonRel.getString("WorkstationID"));
						}
						if(CollectionUtils.isEmpty(stationIDList)){
							return AppResult.success(varList, page);
						}
						pd.put("stationIDList", stationIDList);
						page.setPd(pd);
						// 条件查询带分页排序的
						PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), orderby + " " + sort);
						PageInfo<PageData> pageInfo = new PageInfo<>(
								planningWorkOrderService.appTaskListByStaffId(page));
						varList = pageInfo.getList();

						for (PageData appProcessWorkOrderExampleDetailByPK : varList) {
							if (Tools.isEmpty(appProcessWorkOrderExampleDetailByPK.getString("OriginTaskID"))) {
								appProcessWorkOrderExampleDetailByPK.put("OriginTaskNum", "");
							} else {
								PageData pData = new PageData();
								pData.put("ProcessWorkOrderExample_ID",
										appProcessWorkOrderExampleDetailByPK.getString("OriginTaskID"));
								PageData orginPwoe = processWorkOrderExampleService.findById(pData);
								appProcessWorkOrderExampleDetailByPK.put("OriginTaskNum",
										orginPwoe.getString("TaskNum"));
							}
							if (Tools.isEmpty(appProcessWorkOrderExampleDetailByPK.getString("ActualBeginTime"))) {
								appProcessWorkOrderExampleDetailByPK.put("ActualBeginTime", "");
							}
							if (Tools.isEmpty(appProcessWorkOrderExampleDetailByPK.getString("ActualEndTime"))) {
								appProcessWorkOrderExampleDetailByPK.put("ActualEndTime", "");
							}
							if (Tools.isEmpty(appProcessWorkOrderExampleDetailByPK.getString("ProcessIMateriel"))) {
								appProcessWorkOrderExampleDetailByPK.put("ProcessIMateriel", "");
							} else {
								appProcessWorkOrderExampleDetailByPK.put("ProcessIMateriel",
										appProcessWorkOrderExampleDetailByPK.getString("ProcessIMaterielCode") + "|"
												+ appProcessWorkOrderExampleDetailByPK.getString("ProcessIMateriel"));
							}

							appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", 0);
							appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 0);
							appProcessWorkOrderExampleDetailByPK.put("PlannedQuantity", 0);
							List<PageData> listByProcessWorkOrderExampleID = workorderProcessIOExampleService
									.listByProcessWorkOrderExampleID(appProcessWorkOrderExampleDetailByPK
											.getString("ProcessWorkOrderExample_ID"));
							// 筛选出产出类型的数据
							List<PageData> collect = listByProcessWorkOrderExampleID.stream()
									.filter(t -> "产出".equals(t.getString("TType"))).collect(Collectors.toList());
							if (CollectionUtil.isNotEmpty(collect)) {
								for (PageData pageData : collect) {

									String PlannedQuantityStr = String.valueOf(pageData.get("PlannedQuantity"));
									BigDecimal PlannedQuantityBigDec = new BigDecimal(PlannedQuantityStr);

									// 根据物料消耗 表 获取当前生产了多少 做百分比
									PageData materialConsumeParam = new PageData();
									materialConsumeParam.put("ConsumptionDocumentID",
											pageData.getString("WorkorderProcessIOExample_ID"));
									materialConsumeParam.put("FType", "产出");
									materialConsumeParam.put("MaterialID", pageData.getString("MaterialID"));
									materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
									List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
									if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
										BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
										for (PageData pageDatas : MaterialConsumeList) {
											BigDecimal i = new BigDecimal(String.valueOf(pageDatas.get("ConsumptionQuantity")));
											ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
										}
										appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
										BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDec, 4,
												BigDecimal.ROUND_HALF_DOWN);
										BigDecimal multiply = divide.multiply(new BigDecimal("100"));
										if (multiply.doubleValue() >= 100.00) {
											appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 100);
										} else {
											String percent = new DecimalFormat("#.00").format(multiply);
											String index0 = percent.substring(0, 1);
											if (".".equals(index0)) {
												percent = "0".concat(percent);
											}
											appProcessWorkOrderExampleDetailByPK.put("TaskPercent", Double.valueOf(percent));
										}
									} else {

										appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", 0);

										appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 0);

									}
									appProcessWorkOrderExampleDetailByPK.put("PlannedQuantity", PlannedQuantityBigDec);
								
								}
							}
						}
						page.setTotalPage(pageInfo.getPages());
						page.setTotalResult(pageInfo.getTotal());
					}
				}
			}
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	
	/**
	 * 任务详情
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/appProcessWorkOrderExampleDetailByPK")
	@ResponseBody
	public Object appProcessWorkOrderExampleDetailByPK() {

		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String processWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
			PageData appProcessWorkOrderExampleDetailByPK = planningWorkOrderService
					.appProcessWorkOrderExampleDetailByPK(pd);
			if (null == appProcessWorkOrderExampleDetailByPK) {
				appProcessWorkOrderExampleDetailByPK = new PageData();
			}
			String PlanningWorkOrderID = appProcessWorkOrderExampleDetailByPK.getString("PlanningWorkOrderID");

			if (Tools.isEmpty(PlanningWorkOrderID)) {
				throw new RuntimeException("所属子计划工单为空");
			}
			pd.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
			PageData subPlan = planningWorkOrderService.findById(pd);
			if (null == subPlan) {
				throw new RuntimeException("所属子计划工单为空");
			}

			if ("暂停".equals(subPlan.getString("FStatus"))) {
				throw new RuntimeException("该任务所属计划被暂停，不能执行");
			}

			List<PageData> listByProcessWorkOrderExampleID = workorderProcessIOExampleService
					.listByProcessWorkOrderExampleID(processWorkOrderExample_ID);
			appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", 0);
			appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 0);
			appProcessWorkOrderExampleDetailByPK.put("PlannedQuantity", 0);
			// 按投入产出类型 获取不同的列表
			if (CollectionUtil.isNotEmpty(listByProcessWorkOrderExampleID)) {
				for (PageData pageData : listByProcessWorkOrderExampleID) {
					if ("产出".equals(pageData.getString("TType"))) {
						String PlannedQuantityStr = String.valueOf(pageData.get("PlannedQuantity"));
						BigDecimal PlannedQuantityBigDec = new BigDecimal(PlannedQuantityStr);

						// 根据物料消耗 表 获取当前生产了多少 做百分比
						PageData materialConsumeParam = new PageData();
						materialConsumeParam.put("ConsumptionDocumentID",
								pageData.getString("WorkorderProcessIOExample_ID"));
						materialConsumeParam.put("FType", "产出");
						materialConsumeParam.put("MaterialID", pageData.getString("MaterialID"));
						materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
						List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
						if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
							BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
							for (PageData pageDatas : MaterialConsumeList) {
								BigDecimal i = new BigDecimal(String.valueOf(pageDatas.get("ConsumptionQuantity")));
								ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
							}
							appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
							BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDec, 4,
									BigDecimal.ROUND_HALF_DOWN);
							BigDecimal multiply = divide.multiply(new BigDecimal("100"));
							if (multiply.doubleValue() > 100.00) {
								appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 100);
							} else {
								String percent = new DecimalFormat("#.00").format(multiply);
								String index0 = percent.substring(0, 1);
								if (".".equals(index0)) {
									percent = "0".concat(percent);
								}
								appProcessWorkOrderExampleDetailByPK.put("TaskPercent", Double.valueOf(percent));
							}
						} else {

							appProcessWorkOrderExampleDetailByPK.put("ConsumptionQuantity", 0);

							appProcessWorkOrderExampleDetailByPK.put("TaskPercent", 0);

						}
						appProcessWorkOrderExampleDetailByPK.put("PlannedQuantity", PlannedQuantityBigDec);
					}
				}

			}
			return AppResult.success(appProcessWorkOrderExampleDetailByPK, "获取成功", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 修改生产任务状态
	 * 
	 * @param response
	 * @param FStatus
	 * @param ProcessWorkOrderExample_ID
	 * @return
	 */
	@RequestMapping("/changeProcessWorkOrderExampleStatus")
	@ResponseBody
	public Object changeProcessWorkOrderExampleStatus() {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData appProcessWorkOrderExampleDetailByPK = processWorkOrderExampleService.findById(pd);
			if (null == appProcessWorkOrderExampleDetailByPK) {
				return AppResult.failed("任务没找到!");
			}
			String status = pd.getString("FStatus");
			if (Tools.notEmpty(status)) {
				appProcessWorkOrderExampleDetailByPK.put("FStatus", status);
				appProcessWorkOrderExampleDetailByPK.put("FRUN_STATE", status);
				appProcessWorkOrderExampleDetailByPK.put("FOPERATOR", pd.getString("STAFF_ID"));
				appProcessWorkOrderExampleDetailByPK.put("EXECUTE_TIME", Tools.date2Str(new Date()));
				appProcessWorkOrderExampleDetailByPK.put("TIME_STAMP", System.currentTimeMillis());

				if ("执行中".equals(status)) {
					appProcessWorkOrderExampleDetailByPK.put("ActualBeginTime", Tools.date2Str(new Date()));
				}
				if ("强制结束".equals(status)) {
					appProcessWorkOrderExampleDetailByPK.put("ActualEndTime", Tools.date2Str(new Date()));
				}

				processWorkOrderExampleService.edit(appProcessWorkOrderExampleDetailByPK);
			}

			return AppResult.success("success", "修改成功", "success");

		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();

			String USERNAME = pd.getString("UserName");
			String AssociationID = pd.getString("ProcessWorkOrderExample_ID");

			// 默认值
			String DataSources = Tools.notEmpty(pd.getString("DataSources")) ? pd.getString("DataSources") : "工艺工单工序实例";
			String AssociationIDTable = Tools.notEmpty(pd.getString("AssociationIDTable"))
					? pd.getString("AssociationIDTable") : "PP_ProcessWorkOrderExample";
			PageData pData = new PageData();
			pData.put("AssociationID", AssociationID);
			pData.put("DataSources", DataSources);
			pData.put("AssociationIDTable", AssociationIDTable);
			List<PageData> dataList = attachmentsetService.listAll(pData);
			// app端记录默认不能超过8张图片
			if (dataList.size() >= 8) {
				return AppResult.failed("已经上传过8张图片了，禁止上传");
			}

			pd.put("ATTACHMENTSET_ID", this.get32UUID()); // 主键
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			String dateName = df.format(calendar.getTime());
			String day = DateUtil.getDays();
			PageData pd1 = new PageData();
			String FFILENAME = "";

			// 根据 USERNAME 获取 staff info
			pd1.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pd1);
			String FPFFILEPATH = "";
			if (null != file && !file.isEmpty()) {
				String fileName = file.getOriginalFilename();
				String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + day; // 文件上传路径
				String fileNamereal = fileName.substring(0, fileName.indexOf(".")); // 文件上传路径
				FFILENAME = FileUpload.fileUp(file, filePath, fileNamereal + dateName);// 执行上传
				FPFFILEPATH = Const.FILEPATHFILE + DateUtil.getDays() + "/" + FFILENAME;
				String FExtension = FFILENAME.split("\\.")[1]; // 获取文件扩展名
				pd1.put("AttachmentSet_ID", this.get32UUID());
				pd1.put("FUrl", FPFFILEPATH); // 附件路径
				pd1.put("FName", fileName); // 附件名称
				pd1.put("DataSources", DataSources); // 数据源
				pd1.put("AssociationIDTable", AssociationIDTable); // 数据源表
				pd1.put("AssociationID", AssociationID); // 数据源ID
				pd1.put("FExplanation", ""); // 备注
				pd1.put("FStatus", "使用");
				pd1.put("TType", "");
				pd1.put("FExtension", FExtension);
				pd1.put("FCreatePersonID", staff.getString("STAFF_ID")); // 创建人ID
				pd1.put("FCreateTime", Tools.date2Str(new Date())); // 创建时间
				attachmentsetService.save(pd1);
			} else {
				return AppResult.failed("上传文件为空");
			}
			PageData pdData = new PageData();
			pdData.put("FUrl", FPFFILEPATH);
			pdData.put("AttachmentSet_ID", pd1.getString("AttachmentSet_ID"));
			return AppResult.success(FPFFILEPATH, "上传成功", "success");
		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 获取文件列表
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showFileList")
	@ResponseBody
	public Object showFileList() throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String AssociationID = pd.getString("ProcessWorkOrderExample_ID");
			// 默认值
			String DataSources = Tools.notEmpty(pd.getString("DataSources")) ? pd.getString("DataSources") : "工艺工单工序实例";
			String AssociationIDTable = Tools.notEmpty(pd.getString("AssociationIDTable"))
					? pd.getString("AssociationIDTable") : "PP_ProcessWorkOrderExample";
			PageData pData = new PageData();
			pData.put("AssociationID", AssociationID);
			pData.put("DataSources", DataSources);
			pData.put("AssociationIDTable", AssociationIDTable);
			// 获取上传文件列表
			List<PageData> dataList = attachmentsetService.listAll(pData);
			List<PageData> fileList = Lists.newArrayList();
			for (PageData d : dataList) {
				String fileUrl = d.getString("FUrl");
				PageData pData1 = new PageData();
				pData1.put("FUrl", fileUrl);
				pData1.put("AttachmentSet_ID", d.getString("AttachmentSet_ID"));
				fileList.add(pData1);
			}
			return AppResult.success(fileList, "获取成功", "success");
		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 删除照片
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteFile")
	@ResponseBody
	public Object deleteFile() throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String AttachmentSet_ID = pd.getString("AttachmentSet_ID");
			String[] idStrings = AttachmentSet_ID.split(",");
			attachmentsetService.deleteAll(idStrings);
			return AppResult.success("success", "删除成功", "success");
		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 根据 USERNAME 获取当前登录员工信息
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/getStaffByUserName")
	@ResponseBody
	public Object getStaffByUserName() {
		try {
			PageData pd = this.getPageData();
			String USERNAME = pd.getString("UserName");
			// 根据 USERNAME 获取 staff info
			PageData pd1 = new PageData();
			pd1.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pd1);
			if (null != staff) {
				pd1.put("StaffName", staff.get("NAME"));
				pd1.put("Time", Tools.date2Str(new Date()));
			}
			return AppResult.success(pd1, "获取成功", "success");
		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 生产任务过程记录-新增
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/processRecordAdd")
	@ResponseBody
	public Object processRecordAdd() {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("ProcessRecord_ID", this.get32UUID());
			pd.put("RecordTime", pd.getString("Time"));
			pd.put("RecordPerson", pd.getString("StaffName"));
			pd.put("RecordTerm", "新增记录");
			pd.put("RecordDescribe", pd.getString("RecordDescribe"));
			pd.put("ProcessWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
			pd.put("FType", "生产任务过程记录");
			pd.put("BatchNum", pd.getString("BatchNum"));
			processRecordService.save(pd);
			return AppResult.success("success", "记录成功", "success");

		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 生产任务过程记录-查询
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/processRecordList")
	@ResponseBody
	public Object processRecordList(AppPage page) {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("RecordTerm", "新增记录");
			pd.put("ProcessWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
			pd.put("FType", "生产任务过程记录");
			page.setPd(pd);
			// 生产任务过程记录查询
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), "RecordTime");
			List<PageData> applist = processRecordService.applist(page);
			PageInfo<PageData> pageInfo = new PageInfo<>(applist);
			List<PageData> varList = pageInfo.getList();
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);

		} catch (Exception e) {
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 扫码验证是否是该任务的物料
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/scanMaterial")
	@ResponseBody
	public Object scanMaterial() {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			if (Tools.isEmpty(code)) {
				throw new RuntimeException("二维码无法识别");
			}

			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				throw new RuntimeException("工位无法识别");
			}

			String oneThingsCode = "";
			String qrCode = "";
			String materialId = "";
			Double count = 0.0;
			String processWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
			PageData pwoeData = new PageData();
			pwoeData.put("ProcessWorkOrderExample_ID", processWorkOrderExample_ID);
			
			PageData findById = processWorkOrderExampleService.findById(pwoeData);
			if(null == findById){
				throw new RuntimeException("任务不存在");
			}
			// 验证锅次
			if("是".equals(findById.getString("WokIF"))){
				String WokNumParam = findById.getString("WokNum");
				String[] splits = code.split(",YL,");
				List<String> codeSplitLists = Lists.newArrayList(splits);
				String WokNum = codeSplitLists.get(codeSplitLists.size()-1);
				if(Tools.isEmpty(WokNum)){
					throw new RuntimeException("锅次号不存在");
				}
				if(!WokNum.equals(WokNumParam)){
					throw new RuntimeException("该物料不是本锅次的物料");
				}
			}

			// 唯一码 带出 物料码 和 序列号
			if ("W".equals(code.substring(0, 1))) {
				// 根据唯一码去库存中查找物料叫啥
				String[] split = code.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					oneThingsCode = codeSplitList.get(1);
					count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
					PageData pData = new PageData();
					// 一物码
					pData.put("OneThingCode", oneThingsCode);
					List<PageData> listAll = stockService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						materialId = listAll.get(0).getString("ItemID");
					}
					pd.put("MaterialCode", qrCode);
					pd.put("OneThingCode", oneThingsCode);
				}
			}
			// 类型码 带出 物料码
			if ("L".equals(code.substring(0, 1))) {
				// 根据类型码去库存中查找物料叫啥
				String[] split = code.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					qrCode = codeSplitList.get(1);
					count = Double.valueOf(String.valueOf(codeSplitList.get(2)));
					PageData pData = new PageData();
					// 类型码
					pData.put("QRCode", qrCode);
					List<PageData> listAll = stockService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						materialId = listAll.get(0).getString("ItemID");
					}
					pd.put("MaterialCode", qrCode);
					pd.put("OneThingCode", oneThingsCode);
				}
			}
			pd.put("MaterialID", materialId);

			// 查投入产出实例表 看该物料是否在当前任务下 允许投入
			PageData inputParam = new PageData();
			
			inputParam.put("processWorkOrderExample_ID", processWorkOrderExample_ID);
			inputParam.put("TType", "投入");
			List<String> basicIDs = Lists.newArrayList();
			basicIDs.add(materialId);
			inputParam.put("array", basicIDs);
			List<PageData> wpioeList = workorderProcessIOExampleService.listAll(inputParam);
			if (CollectionUtil.isEmpty(wpioeList)) {
				throw new RuntimeException("该物料不适用于该任务");
			}
			// 单位带过来
			PageData materialParam = new PageData();
			materialParam.put("MAT_BASIC_ID", materialId);
			PageData mat = matBasicService.findById(materialParam);
			String MaterialName = mat.getString("MAT_NAME");
			pd.put("MaterialName", MaterialName);
			pd.put("TType", "投入");
			String MAT_MAIN_UNIT = mat.getString("MAT_MAIN_UNIT");
			materialParam.put("UNIT_INFO_ID", MAT_MAIN_UNIT);
			PageData unitInfo = Unit_InfoService.findById(materialParam);
			if (null == unitInfo) {
				pd.put("UNIT", "默认单位");
			} else {
				pd.put("UNIT", unitInfo.getString("FNAME"));
			}
			pd.put("ProcessIMateriel", pd.getString("MaterialCode") + "|" + pd.getString("MaterialName"));
			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			String stockSum = "0";
			double stockSumDouble = 0.0;
			if (null != stationInfo) {
				String WareHouseId = stationInfo.getString("ConsumptionWarehouse");
				// 消耗仓库 根据仓库获取该物料的 库存 如果库存不足，提示
				PageData pdKC = new PageData();
				pdKC.put("WarehouseID", WareHouseId);
				pdKC.put("ItemID", materialId);
				PageData pdStockSum = StockService.getSum(pdKC);// 即时库存

				if (null == pdStockSum) {
					throw new RuntimeException("库存不足");
				}

				if (null != pdStockSum) {

					if ("0".equals(String.valueOf(pdStockSum.get("stockSum")))) {
						throw new RuntimeException("库存不足");
					}

					// 获取即时库存
					stockSum = pdStockSum.get("stockSum").toString();
					stockSumDouble = Double.valueOf(stockSum);
				}
			}
			pd.put("StockSumDouble", stockSumDouble);
			pd.put("count", count);

			// ============================begin=================================//

			if (CollectionUtil.isNotEmpty(wpioeList)) {
				PageData pageData = wpioeList.get(0);
				pd.put("TType", pageData.getString("TType"));
				pd.put("ByProduct", pageData.getString("ByProduct"));
				String PlannedQuantityStr = String.valueOf(pageData.get("PlannedQuantity"));
				BigDecimal PlannedQuantityBigDec = new BigDecimal(PlannedQuantityStr);
				pd.put("PlannedQuantity", PlannedQuantityBigDec);

				// 根据物料消耗 表 获取当前 消耗了多少 做百分比
				PageData materialConsumeParam = new PageData();
				materialConsumeParam.put("ConsumptionDocumentID", pageData.getString("WorkorderProcessIOExample_ID"));
				materialConsumeParam.put("FType", "消耗");
				materialConsumeParam.put("MaterialID", pageData.getString("MaterialID"));
				materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
				List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
				if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
					BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
					for (PageData pageDatas : MaterialConsumeList) {
						BigDecimal i = new BigDecimal(String.valueOf(pageDatas.get("ConsumptionQuantity")));
						ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
					}
					pd.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
					BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDec, 4,
							BigDecimal.ROUND_HALF_DOWN);
					BigDecimal multiply = divide.multiply(new BigDecimal("100"));
					BigDecimal subtract = PlannedQuantityBigDec.subtract(ConsumptionQuantityBigDec);
					if (subtract.doubleValue() < 0) {
						pd.put("Subtract", 0);
					} else {
						pd.put("Subtract", subtract.doubleValue());
					}
					// 计算投产百分比
					if (multiply.doubleValue() > 100.00) {
						pd.put("InputPercent", 100);
					} else {
						String percent = new DecimalFormat("#.00").format(multiply);
						String index0 = percent.substring(0, 1);
						if (".".equals(index0)) {
							percent = "0".concat(percent);
						}
						pd.put("InputPercent", Double.valueOf(percent));
					}
				} else {
					pd.put("ConsumptionQuantity", 0);
					pd.put("InputPercent", 0);
					pd.put("Subtract", 0);
				}
			} else {
				pd.put("ConsumptionQuantity", 0);
				pd.put("InputPercent", 0);
				pd.put("Subtract", 0);
			}

			// ============================end===================================//

			return AppResult.success(pd, "可以进行扫码投产,返回物料即时库存");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 插入物料消耗表
	 * 
	 * @param response
	 *            UserName code count WorkstationID
	 * @return
	 */
	@RequestMapping("/insertMaterialConsume")
	@ResponseBody
	public Object insertMaterialConsume() {

		try {
			PageData pd = new PageData();
			pd = this.getPageData();

			// 判断token是否过期
			String USERNAME = pd.getString("UserName");

			// 工位id
			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				return AppResult.failed("WC_STATION_ID 参数没找到!");
			}

			/*
			 * 1、根据二维码 和数量 新增消耗记录 2、影响车间库库存
			 * 3、如果该物料在物料投产表内，将其加载到当前操作位置（返回总体需求量和已投产数据）
			 */
			String code = pd.getString("code");
			if (Tools.isEmpty(code)) {
				throw new RuntimeException("二维码无法识别");
			}
			String count = pd.getString("count");

			String materialName = "";
			String oneThingsCode = "";
			String qrCode = "";
			String materialSProp = "2540539e45324232a50bde60ac2951d3";
			String materialSPropKey = "";
			String materialId = "";
			String specificationDesc = "";

			String fUnit = "";
			String fBatch = "";
			PageData inputData = new PageData();
			// 唯一码 带出 物料码 和 序列号
			if ("W".equals(code.substring(0, 1))) {
				// 根据唯一码去库存中查找物料叫啥
				String[] split = code.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					oneThingsCode = codeSplitList.get(1);
					PageData pData = new PageData();
					// 一物码
					pData.put("OneThingCode", oneThingsCode);
					List<PageData> listAll = stockService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						qrCode = listAll.get(0).getString("QRCode");
						materialName = listAll.get(0).getString("MAT_NAME");
						materialSProp = listAll.get(0).getString("MaterialSProp");
						materialSPropKey = listAll.get(0).getString("MaterialSPropKey");
						materialId = listAll.get(0).getString("ItemID");
						if (Tools.isEmpty(materialId)) {
							throw new RuntimeException("该物料在库存中不存在");
						}
						specificationDesc = listAll.get(0).get("SpecificationDesc").toString();
						fUnit = listAll.get(0).getString("FUnit");
						fBatch = listAll.get(0).getString("FBatch");
					}
				}
			}
			// 类型码 带出 物料码 和 序列号
			if ("L".equals(code.substring(0, 1))) {
				// 根据类型码去库存中查找物料叫啥
				String[] split = code.split(",YL,");
				if (split.length > 1) {
					List<String> codeSplitList = Lists.newArrayList(split);
					qrCode = codeSplitList.get(1);
					PageData pData = new PageData();
					// 类型码
					pData.put("QRCode", qrCode);
					List<PageData> listAll = stockService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						materialName = listAll.get(0).getString("MAT_NAME");
						materialSProp = listAll.get(0).getString("MaterialSProp");
						materialSPropKey = listAll.get(0).getString("MaterialSPropKey");
						materialId = listAll.get(0).getString("ItemID");
						if (Tools.isEmpty(materialId)) {
							throw new RuntimeException("该物料在库存中不存在");
						}
						specificationDesc = listAll.get(0).get("SpecificationDesc").toString();
						fUnit = listAll.get(0).getString("FUnit");
						fBatch = listAll.get(0).getString("FBatch");
					}
				}

			}
			if (Tools.isEmpty(materialId)) {
				throw new RuntimeException("该物料在库存中不存在");
			}
			// 根据 USERNAME 获取 staff info
			PageData pd1 = new PageData();
			pd1.put("USERNAME", USERNAME);
			PageData staff = staffService.findById(pd1);

			PageData findById = processWorkOrderExampleService.findById(pd);
			String BatchNum = findById.getString("BatchNum");
			String PlanningWorkOrderID = findById.getString("PlanningWorkOrderID");

			// 查投入产出实例表 看该物料是否在当前任务下 允许投入
			PageData wpioeParam = new PageData();
			wpioeParam.put("processWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
			List<String> array = Lists.newArrayList();
			array.add(materialId);
			wpioeParam.put("array", array);
			wpioeParam.put("TType", "投入");
			List<PageData> wpioeList = workorderProcessIOExampleService.listAll(wpioeParam);
			if (CollectionUtil.isEmpty(wpioeList)) {
				throw new RuntimeException("该物料不适用于该任务");
			}

			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			String stockSum = "0";
			if (null != stationInfo) {
				String WareHouseId = stationInfo.getString("ConsumptionWarehouse");
				// 消耗仓库 根据仓库获取该物料的 库存 如果库存不足，提示
				PageData pdKC = new PageData();
				pdKC.put("WarehouseID", WareHouseId);
				pdKC.put("ItemID", materialId);
				PageData pdStockSum = StockService.getSum(pdKC);// 即时库存

				if (null == pdStockSum) {
					throw new RuntimeException("库存不足");
				}

				if (null != pdStockSum) {

					if ("0.00".equals(pdStockSum.get("stockSum").toString())) {
						throw new RuntimeException("库存不足");
					}

					// 获取即时库存
					stockSum = pdStockSum.get("stockSum").toString();
					double stockSumDouble = Double.valueOf(stockSum);
					double countDouble = Double.valueOf(count);
					// 如果需求数量 大于了 即时库存数量 提示库存不足
					if (countDouble > stockSumDouble) {
						throw new RuntimeException("库存不足");
					}

					// 扣减库存
					PageData outParam = new PageData();
					outParam.put("num", countDouble);// 出库数量
					outParam.put("WarehouseID", WareHouseId);// 仓库id
					outParam.put("ItemID", materialId);// 物料id
					outParam.put("MaterialSPropKey", materialSPropKey);// 辅助属性值id
					StockService.outStock(outParam);
					String WorkorderProcessIOExample_ID = wpioeList.get(0).getString("WorkorderProcessIOExample_ID");

					// 插入到消耗产出表
					PageData materialConsume = new PageData();
					materialConsume.put("MaterialConsume_ID", this.get32UUID());
					materialConsume.put("ConsumptionDocumentID", WorkorderProcessIOExample_ID);
					materialConsume.put("FType", "消耗");
					materialConsume.put("DataSources", "PP_WorkorderProcessIOExample");
					materialConsume.put("MaterialID", materialId);
					materialConsume.put("MaterialName", materialName);
					materialConsume.put("MaterialBarCode", qrCode);
					materialConsume.put("MaterialBatchNum", fBatch);
					materialConsume.put("SpecificationsAndModels", count + " " + specificationDesc);
					materialConsume.put("MaterialSProp", materialSProp);
					materialConsume.put("MaterialSPropKey", materialSPropKey);
					materialConsume.put("FUnit", fUnit);
					materialConsume.put("ConsumptionQuantity", count);
					materialConsume.put("WorkOrderRel", PlanningWorkOrderID);
					materialConsume.put("FOperatorID", staff.getString("STAFF_ID"));
					materialConsume.put("FOperateTime", Tools.date2Str(new Date()));
					materialConsume.put("BatchNum", BatchNum);
					materialConsume.put("OneThingCode", oneThingsCode);
					MaterialConsumeService.save(materialConsume);
					inputData.put("ProcessIMateriel", qrCode + "|" + materialName);
					// 返回冒泡信息
					PageData materialConsumeParam = new PageData();
					materialConsumeParam.put("ConsumptionDocumentID", WorkorderProcessIOExample_ID);
					materialConsumeParam.put("FType", "消耗");
					materialConsumeParam.put("MaterialID", materialId);
					materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
					List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
					if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
						BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
						for (PageData pageData : MaterialConsumeList) {
							BigDecimal i = new BigDecimal(String.valueOf(pageData.get("ConsumptionQuantity")));
							ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
						}
						inputData.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
						inputData.put("PlannedQuantity", wpioeList.get(0).get("PlannedQuantity"));
						BigDecimal PlannedQuantityBigDecimal = new BigDecimal(
								String.valueOf(wpioeList.get(0).get("PlannedQuantity")));
						BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDecimal, 4,
								BigDecimal.ROUND_HALF_DOWN);
						BigDecimal multiply = divide.multiply(new BigDecimal("100"));
						if (multiply.doubleValue() > 100.0) {
							multiply = new BigDecimal("100");
						}
						BigDecimal subtract = PlannedQuantityBigDecimal.subtract(ConsumptionQuantityBigDec);
						if (subtract.doubleValue() < 0) {
							inputData.put("Subtract", 0);
						} else {
							inputData.put("Subtract", subtract.doubleValue());
						}
						inputData.put("InputPercent", multiply.doubleValue());
					} else {
						inputData.put("ConsumptionQuantity", 0);
						inputData.put("PlannedQuantity", 0);
						inputData.put("InputPercent", 0);
						inputData.put("Subtract", 0);
					}

				}
			}
			return AppResult.success(inputData, "物料消耗成功");

		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}

	}

	/**
	 * 投产列表筛选
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/inputListSearch")
	@ResponseBody
	public Object inputListSearch(AppPage page) {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String processWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
			PageData inputParam = new PageData();
			inputParam.put("processWorkOrderExample_ID", processWorkOrderExample_ID);
			inputParam.put("TType", "投入");
			// 关键词搜索
			if (Tools.notEmpty(pd.getString("KEYWORDS"))) {
				List<PageData> basicList = matBasicService.getMaterialList(pd);
				List<String> basicIDs = Lists.newArrayList();
				if (CollectionUtil.isNotEmpty(basicList)) {
					basicList.forEach(b -> {
						basicIDs.add(b.getString("MAT_BASIC_ID"));
					});
					inputParam.put("array", basicIDs);
				} else {
					basicIDs.add("&nbsp;");
					inputParam.put("array", basicIDs);
				}
			}
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount());
			List<PageData> listByProcessWorkOrderExampleID = workorderProcessIOExampleService.listAll(inputParam);
			PageInfo<PageData> pageInfo = new PageInfo<>(listByProcessWorkOrderExampleID);
			List<PageData> inputList = Lists.newArrayList();
			// 处理数据
			for (PageData pageData : pageInfo.getList()) {
				PageData inputData = new PageData();
				inputData.put("TType", pageData.getString("TType"));
				inputData.put("ByProduct", pageData.getString("ByProduct"));
				String PlannedQuantityStr = String.valueOf(pageData.get("PlannedQuantity"));
				BigDecimal PlannedQuantityBigDec = new BigDecimal(PlannedQuantityStr);
				inputData.put("PlannedQuantity", PlannedQuantityBigDec);
				inputData.put("MaterialID", pageData.getString("MaterialID"));
				PageData matBasicParam = new PageData();
				matBasicParam.put("MAT_BASIC_ID", pageData.getString("MaterialID"));
				PageData matBasic = matBasicService.findById(matBasicParam);
				if (null != matBasic) {
					inputData.put("MaterialName", matBasic.getString("MAT_NAME"));
					inputData.put("MaterialCode", matBasic.getString("MAT_CODE"));
					// 拼接参数
					inputData.put("ProcessIMateriel",
							matBasic.getString("MAT_CODE") + "|" + matBasic.getString("MAT_NAME"));
					// 单位带过来

					PageData materialParam = new PageData();
					String MAT_MAIN_UNIT = matBasic.getString("MAT_MAIN_UNIT");
					materialParam.put("UNIT_INFO_ID", MAT_MAIN_UNIT);
					PageData unitInfo = Unit_InfoService.findById(materialParam);
					if (null == unitInfo) {
						inputData.put("UNIT", "默认单位");
					} else {
						inputData.put("UNIT", unitInfo.getString("FNAME"));
					}
				} else {
					inputData.put("MaterialName", "");
					inputData.put("MaterialCode", "");
					inputData.put("ProcessIMateriel", "");
					inputData.put("UNIT", "默认单位");
				}

				inputData.put("MAT_NAME", pageData.getString("MAT_NAME"));
				inputData.put("MAT_CODE", pageData.getString("MAT_CODE"));
				inputData.put("MAT_SPECS", pageData.getString("MAT_SPECS"));
				inputData.put("MAT_AUXILIARY_ID", pageData.getString("MAT_AUXILIARY_ID"));
				inputData.put("fUnit", pageData.getString("fUnit"));
				inputData.put("IFOneCode", pageData.getString("IFOneCode"));
				
				
				// 根据物料消耗 表 获取当前 消耗了多少 做百分比
				PageData materialConsumeParam = new PageData();
				materialConsumeParam.put("ConsumptionDocumentID", pageData.getString("WorkorderProcessIOExample_ID"));
				materialConsumeParam.put("FType", "消耗");
				materialConsumeParam.put("MaterialID", pageData.getString("MaterialID"));
				materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
				List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
				if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
					BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
					for (PageData pageDatas : MaterialConsumeList) {
						BigDecimal i = new BigDecimal(String.valueOf(pageDatas.get("ConsumptionQuantity")));
						ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
					}
					inputData.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
					BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDec, 4,
							BigDecimal.ROUND_HALF_DOWN);
					BigDecimal multiply = divide.multiply(new BigDecimal("100"));
					BigDecimal subtract = PlannedQuantityBigDec.subtract(ConsumptionQuantityBigDec);
					if (subtract.doubleValue() < 0) {
						inputData.put("Subtract", 0);
					} else {
						inputData.put("Subtract", subtract.doubleValue());
					}
					if (multiply.doubleValue() > 100.00) {
						inputData.put("InputPercent", 100);
					} else {
						String percent = new DecimalFormat("#.00").format(multiply);
						String index0 = percent.substring(0, 1);
						if (".".equals(index0)) {
							percent = "0".concat(percent);
						}
						inputData.put("InputPercent", Double.valueOf(percent));
					}
				} else {
					inputData.put("ConsumptionQuantity", 0);
					inputData.put("InputPercent", 0);
					inputData.put("Subtract", 0);
				}

				inputList.add(inputData);
			}
			// 返回分页参数数据
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(inputList, page);

		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 产出列表
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/outputListSearch")
	@ResponseBody
	public Object outputListSearch(AppPage page) {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String processWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
			PageData outputParam = new PageData();
			outputParam.put("processWorkOrderExample_ID", processWorkOrderExample_ID);
			outputParam.put("TType", "产出");
			if (Tools.notEmpty(pd.getString("KEYWORDS"))) {
				List<PageData> basicList = matBasicService.getMaterialList(pd);
				List<String> basicIDs = Lists.newArrayList();
				if (CollectionUtil.isNotEmpty(basicList)) {
					basicList.forEach(b -> {
						basicIDs.add(b.getString("MAT_BASIC_ID"));
					});
					outputParam.put("array", basicIDs);
				} else {
					basicIDs.add("&nbsp;");
					outputParam.put("array", basicIDs);
				}
			}
			// 产出参数拼接
			page.setPd(pd);
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount());
			List<PageData> listByProcessWorkOrderExampleID = workorderProcessIOExampleService.listAll(outputParam);
			PageInfo<PageData> pageInfo = new PageInfo<>(listByProcessWorkOrderExampleID);
			List<PageData> outputList = Lists.newArrayList();
			for (PageData pageData : listByProcessWorkOrderExampleID) {

				PageData outputData = new PageData();
				outputData.put("TType", pageData.getString("TType"));
				outputData.put("ByProduct", pageData.getString("ByProduct"));
				String PlannedQuantityStr = String.valueOf(pageData.get("PlannedQuantity"));
				BigDecimal PlannedQuantityBigDec = new BigDecimal(PlannedQuantityStr);
				outputData.put("PlannedQuantity", PlannedQuantityBigDec);
				outputData.put("MaterialID", pageData.getString("MaterialID"));
				PageData matBasicParam = new PageData();
				matBasicParam.put("MAT_BASIC_ID", pageData.getString("MaterialID"));
				PageData matBasic = matBasicService.findById(matBasicParam);
				if (null != matBasic) {
					outputData.put("MaterialName", matBasic.getString("MAT_NAME"));
					outputData.put("MaterialCode", matBasic.getString("MAT_CODE"));
					outputData.put("ProcessIMateriel",
							matBasic.getString("MAT_CODE") + "|" + matBasic.getString("MAT_NAME"));
					// 单位带过来

					PageData materialParam = new PageData();
					String MAT_MAIN_UNIT = matBasic.getString("MAT_MAIN_UNIT");
					materialParam.put("UNIT_INFO_ID", MAT_MAIN_UNIT);
					PageData unitInfo = Unit_InfoService.findById(materialParam);
					if (null == unitInfo) {
						outputData.put("UNIT", "默认单位");
					} else {
						outputData.put("UNIT", unitInfo.getString("FNAME"));
					}

				} else {
					outputData.put("MaterialName", "");
					outputData.put("MaterialCode", "");
					outputData.put("ProcessIMateriel", "");
					outputData.put("UNIT", "默认单位");
				}

				// 根据物料消耗 表 获取当前生产了多少产出 了多少 做百分比
				PageData materialConsumeParam = new PageData();
				materialConsumeParam.put("ConsumptionDocumentID", pageData.getString("WorkorderProcessIOExample_ID"));
				materialConsumeParam.put("FType", "产出");
				materialConsumeParam.put("MaterialID", pageData.getString("MaterialID"));
				materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
				List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
				if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
					BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
					for (PageData pageDatas : MaterialConsumeList) {
						BigDecimal i = new BigDecimal(String.valueOf(pageDatas.get("ConsumptionQuantity")));
						ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
					}
					outputData.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
					BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDec, 4,
							BigDecimal.ROUND_HALF_DOWN);
					BigDecimal multiply = divide.multiply(new BigDecimal("100"));

					BigDecimal subtract = PlannedQuantityBigDec.subtract(ConsumptionQuantityBigDec);
					if (subtract.doubleValue() < 0) {
						outputData.put("Subtract", 0);
					} else {
						outputData.put("Subtract", subtract.doubleValue());
					}

					if (multiply.doubleValue() > 100.00) {
						outputData.put("OutputPercent", 100);
					} else {
						String percent = new DecimalFormat("#.00").format(multiply);
						String index0 = percent.substring(0, 1);
						if (".".equals(index0)) {
							percent = "0".concat(percent);
						}
						outputData.put("OutputPercent", Double.valueOf(percent));
					}
				} else {
					outputData.put("ConsumptionQuantity", 0);
					outputData.put("InputPercent", 0);
					outputData.put("Subtract", 0);
				}

				outputList.add(outputData);
			}
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(outputList, page);

		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 根据工位二维码获取工位数据
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping("/getWorkStationInfoByCode")
	@ResponseBody
	public Object getWorkStationInfoByCode() {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String code = pd.getString("code");
			if (Tools.isEmpty(code)) {
				throw new RuntimeException("该二维码不能识别");
			}
			String[] split = code.split("/");
			String stationName = split[0];
			String stationCode = split[1];
			PageData pdData = new PageData();
			// 根据工位码和工位名称 返回工位信息
			pdData.put("FCODE", stationCode);
			pdData.put("FNAME", stationName);
			List<PageData> listAll = stationService.listAll(pdData);
			if (CollectionUtil.isEmpty(listAll)) {
				throw new RuntimeException("根据二维码数据没有找到该工位");
			}
			pdData.put("WC_STATION_ID", listAll.get(0).get("WC_STATION_ID"));

			return AppResult.success(pdData, "获取工位信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 根据物料id获取该物料在该任务的投产列表
	 * 
	 * @param page
	 * @param response
	 * @return
	 */
	@RequestMapping("getConsumeListByMaterialId")
	@ResponseBody
	public Object getConsumeListByMaterialId(AppPage page) {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pData = new PageData();
			// 任务id 查 投入产出
			PageData wpioeParam = new PageData();
			wpioeParam.put("processWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
			List<String> array = Lists.newArrayList();
			array.add(pd.getString("MaterialID"));
			wpioeParam.put("array", array);
			List<PageData> wpioeList = workorderProcessIOExampleService.listAll(wpioeParam);
			if (CollectionUtil.isEmpty(wpioeList)) {
				throw new RuntimeException("该物料不适用于该任务");
			}
			String MaterialConsume_ID = wpioeList.get(0).getString("WorkorderProcessIOExample_ID");
			pData.put("ConsumptionDocumentID", MaterialConsume_ID);
			pData.put("MaterialID", pd.getString("MaterialID"));
			pData.put("FType", pd.getString("FType"));
			page.setPd(pData);
			// 查询投产列表
			PageHelper.startPage(page.getCurrentPage(), page.getShowCount(), "FOperateTime desc");
			PageInfo<PageData> pageInfo = new PageInfo<>(MaterialConsumeService.listAll(pData));
			List<PageData> varList = pageInfo.getList();
			for (PageData pageData : varList) {
				String FOperateTimeStr = pageData.getString("FOperateTime");
				FOperateTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Tools.str2Date(FOperateTimeStr));
				pageData.put("FOperateTime", FOperateTimeStr);
				// 处理操作人 和 拼接 物料名称和code
				String FOperatorID = pageData.getString("FOperatorID");
				PageData pdData = new PageData();
				pdData.put("STAFF_ID", FOperatorID);
				PageData staffInfo = staffService.findById(pdData);
				String NAME = staffInfo.getString("NAME");
				pageData.put("FOperatorName", NAME);
				pageData.put("ProcessIMateriel",
						pageData.getString("MaterialBarCode") + "|" + pageData.getString("MaterialName"));

				String FUnitID = pageData.getString("FUnit");
				PageData pData2 = new PageData();
				pData2.put("UNIT_INFO_ID", FUnitID);
				PageData unitInfo = Unit_InfoService.findById(pData2);
				pageData.put("FUnit", unitInfo.getString("FNAME"));

			}
			page.setTotalPage(pageInfo.getPages());
			page.setTotalResult(pageInfo.getTotal());
			return AppResult.success(varList, page);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 产出打码获取物料信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/outMatDetail")
	@ResponseBody
	public Object outMatDetail() throws Exception {

		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData pdCode = new PageData();
			BigDecimal FCode = new BigDecimal(0);
			PageData pdMx = new PageData();

			// 物料id
			String MAT_BASIC_ID = pd.getString("MAT_BASIC_ID");
			if (Tools.isEmpty(MAT_BASIC_ID)) {
				return AppResult.failed("物料id为空");
			}
			pdMx.put("MAT_BASIC_ID", MAT_BASIC_ID);

			// 子计划工单id
			String PlanningWorkOrderID = pd.getString("PlanningWorkOrderID");
			if (Tools.isEmpty(PlanningWorkOrderID)) {
				return AppResult.failed("子计划工单id为空");
			}
			pdMx.put("PlanningWorkOrderID", PlanningWorkOrderID);

			PageData pdData = new PageData();
			pdData.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
			PageData subPlan = planningWorkOrderService.findById(pdData);
			String SPropKeyName = subPlan.getString("MasterWorkOrderNum");
			pdData.put("MAT_AUXILIARY_ID", "2540539e45324232a50bde60ac2951d3");
			pdData.put("MAT_AUXILIARYMX_CODE", SPropKeyName);

			// 获取MasterWorkOrder WorkOrderNum 根据号码 和 SPROP 获取 auxMX Id 返回前台
			List<PageData> MAT_AUXILIARYMX_INFO = MAT_AUXILIARYMxService
					.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pdData);
			if (CollectionUtil.isNotEmpty(MAT_AUXILIARYMX_INFO)) {
				pdMx.put("SPropKeyID", MAT_AUXILIARYMX_INFO.get(0).get("MAT_AUXILIARYMX_ID"));
			}
			PageData matParam = new PageData();
			matParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
			pd = matBasicService.findById(matParam);

			if (null == pd) {
				return AppResult.failed("物料不存在");
			}

			String Ftype = "类型码";
			String ForOne = "否";
			if ("是".equals(pd.getString("UNIQUE_CODE_WHETHER"))) {
				Ftype = "唯一码";
				ForOne = "是";
			}
			pdMx.put("ForOne", ForOne);

			pd.put("Ftype", Ftype);

			// 类型码唯一码处理
			if ("类型码".equals(Ftype)) {
				pdCode = MAKECODEService.getCode(pd);
				String encode = "L,YL," + pd.getString("MAT_CODE") + ",YL," + pd.get("MAT_SPECS_QTY") + ",YL,"
						+ SPropKeyName;

				pdMx.put("Encode", encode);

				pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
				pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

				PageData unitMainParam = new PageData();
				unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
				PageData unitMain = Unit_InfoService.findById(unitMainParam);
				String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
				pdMx.put("MAT_SPECS_QTY", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT);

				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				String MAT_AUX_UNIT = "默认包装";
				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
				}
				pdMx.put("MAT_AUX_UNIT_ID", pd.get("MAT_MAIN_UNIT"));
				pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
				pdMx.put("SPropKey", SPropKeyName);
				pdMx.put("MakeTime", Tools.date2Str(new Date()));

				// 类型码 0
				pdMx.put("SerialNum", FCode);

			}
			if ("唯一码".equals(Ftype)) {
				pdCode = MAKECODEService.getCode(pd);
				FCode = new BigDecimal(pdCode.get("FCode").toString());

				FCode = FCode.add(new BigDecimal(1));
				String encode = "W,YL," + FCode + ",YL," + pd.get("MAT_SPECS_QTY") + ",YL," + SPropKeyName;
				pdMx.put("Encode", encode);

				pdMx.put("MAT_NAME", pd.get("MAT_NAME"));
				pdMx.put("MAT_CODE", pd.get("MAT_CODE"));

				PageData unitMainParam = new PageData();
				unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
				PageData unitMain = Unit_InfoService.findById(unitMainParam);
				String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
				pdMx.put("MAT_SPECS_QTY", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT);

				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", pd.get("MAT_AUXILIARY_UNIT"));
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				String MAT_AUX_UNIT = "默认包装";
				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
				}
				pdMx.put("MAT_AUX_UNIT_ID", pd.get("MAT_MAIN_UNIT"));
				pdMx.put("MAT_SPECS", pd.get("MAT_SPECS_QTY") + MAT_MAIN_UNIT + '/' + MAT_AUX_UNIT);
				pdMx.put("SPropKey", SPropKeyName);
				pdMx.put("MakeTime", Tools.date2Str(new Date()));

				// 唯一码有 序列号
				pdMx.put("SerialNum", FCode);

			}

			pd.put("FCode", FCode);
			MAKECODEService.editCode(pd);
			return AppResult.success(pdMx);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}

	}

	/***
	 * 插入物料产出
	 * 
	 * @param MAT_BASIC_ID
	 *            WC_STATION_ID MAT_NAME MAT_CODE MAT_NAME MAT_SPECS SPropKeyID
	 *            MAT_AUX_UNIT_ID MAT_SPECS_QTY PlanningWorkOrderID SerialNum
	 *            Count
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insertMaterialOutput")
	@ResponseBody
	public Object insertMaterialOutput() throws Exception {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			// 查投入产出实例表 看该物料是否在当前任务下 允许投入
			String materialId = pd.getString("MAT_BASIC_ID");
			PageData wpioeParam = new PageData();
			wpioeParam.put("processWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
			List<String> array = Lists.newArrayList();
			array.add(materialId);
			wpioeParam.put("array", array);
			wpioeParam.put("TType", "产出");
			List<PageData> wpioeList = workorderProcessIOExampleService.listAll(wpioeParam);
			if (CollectionUtil.isEmpty(wpioeList)) {
				throw new RuntimeException("该物料不适用于该任务");
			}

			// 工位id
			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				return AppResult.failed("WC_STATION_ID 参数没找到!");
			}
			PageData findById = processWorkOrderExampleService.findById(pd);
			String BatchNum = findById.getString("BatchNum");
			Object WorkorderProcessIOExample_ID = wpioeList.get(0).getString("WorkorderProcessIOExample_ID");
			Object materialName = pd.getString("MAT_NAME");
			Object qrCode = pd.getString("MAT_CODE");
			Object fBatch = pd.getString("MAT_NAME");
			Object specificationDesc = pd.getString("MAT_SPECS");
			Object materialSProp = "2540539e45324232a50bde60ac2951d3";
			Object materialSPropKey = pd.getString("SPropKeyID");
			Object fUnit = pd.getString("MAT_AUX_UNIT_ID");
			String MAT_SPECS_QTY = pd.getString("MAT_SPECS_QTY");
//			String MAT_SPECS_QTY = pd.getString("MAT_SPECS").split("/")[0].split(" ")[0];
			Object PlanningWorkOrderID = pd.getString("PlanningWorkOrderID");
			Object oneThingsCode = pd.getString("SerialNum");
			Integer count = Integer.valueOf(pd.getString("Count"));
			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			PageData pageData = new PageData();
			pageData.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
			PageData pwo = planningWorkOrderService.findById(pageData);
			if (null == pwo) {
				throw new RuntimeException("子计划工单没找到");
			}

			if (null != stationInfo) {
				for (int i = 0; i < count; i++) {
					// 插入到产出表
					PageData materialConsume = new PageData();
					materialConsume.put("MaterialConsume_ID", this.get32UUID());
					materialConsume.put("ConsumptionDocumentID", WorkorderProcessIOExample_ID);
					materialConsume.put("FType", "产出");
					materialConsume.put("DataSources", "PP_WorkorderProcessIOExample");
					materialConsume.put("MaterialID", materialId);
					materialConsume.put("MaterialName", materialName);
					materialConsume.put("MaterialBarCode", qrCode);
					materialConsume.put("MaterialBatchNum", fBatch);
					materialConsume.put("SpecificationsAndModels", specificationDesc);
					materialConsume.put("MaterialSProp", materialSProp);
					materialConsume.put("MaterialSPropKey", materialSPropKey);
					materialConsume.put("FUnit", fUnit);
					materialConsume.put("ConsumptionQuantity", Double.valueOf(MAT_SPECS_QTY));
					materialConsume.put("WorkOrderRel", PlanningWorkOrderID);
					materialConsume.put("FOperatorID", pd.getString("STAFF_ID"));
					materialConsume.put("FOperateTime", Tools.date2Str(new Date()));
					materialConsume.put("BatchNum", BatchNum);
					materialConsume.put("OneThingCode", oneThingsCode);
					materialConsume.put("OutType", "正常");
					MaterialConsumeService.save(materialConsume);
					// 根据工位id获取绑定的仓库和库位 产出物料加库存
					String WareHouseId = stationInfo.getString("ProduceWarehouse");
					if (Tools.isEmpty(WareHouseId)) {
						throw new RuntimeException("工位没有绑定默认仓库");
					}
					String LocationId = stationInfo.getString("ProduceLocation");
					if (Tools.isEmpty(LocationId)) {
						throw new RuntimeException("工位没有绑定默认库位");
					}
					// 增加库存
					PageData inParam = new PageData();
					inParam.put("Stock_ID", this.get32UUID());
					inParam.put("WarehouseID", WareHouseId);// 仓库id
					inParam.put("PositionID", LocationId);// 库位id
					inParam.put("ItemID", materialId);// 物料id
					inParam.put("StorageStatus", "工厂");// 存储状态(工厂,车间)
					inParam.put("QRCode", qrCode);// 物料二维码
					inParam.put("OneThingCode", oneThingsCode);// 一物码
					inParam.put("MaterialSProp", materialSProp);// 物料辅助属性
					inParam.put("MaterialSPropKey", materialSPropKey);// 物料辅助属性值
					inParam.put("SpecificationDesc", specificationDesc);// 规格
					inParam.put("ActualCount", MAT_SPECS_QTY);// 实际数量
					inParam.put("FBatch", BatchNum);// 批号
					inParam.put("FUnit", fUnit);// 单位
					inParam.put("ProductionBatch", BatchNum);// 生产批号

					inParam.put("UseCount", 0);// 使用数量
					inParam.put("UseIf", "NO");// 是否占用
					inParam.put("FLevel", 1);// 等级

					inParam.put("RunningState", "在用");// 运行状态
					inParam.put("FCreateTime", Tools.date2Str(new Date()));// 创建时间
					inParam.put("UsageTime", Tools.date2Str(new Date()));// 使用时间
					inParam.put("DateOfManufacture", Tools.date2Str(new Date()));// 生产日期

					inParam.put("LastModifiedTime", Tools.date2Str(new Date()));// 最后修改日期
					inParam.put("LastCountTime", Tools.date2Str(new Date()));// 上次盘点日期
					inParam.put("FStatus", "入库");// 状态

					inParam.put("ProjectNum", pwo.getString("WorkOrderNum"));// 计划工单编号
					inParam.put("CustomerID", pwo.getString("FCustomer"));// 客户

					StockService.inStock(inParam);
				}
			}
			return AppResult.success("产出提交成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}

	/**
	 * 获取物料规格下拉数据
	 * 
	 * @return
	 */
	@RequestMapping("/getMatSpecList")
	@ResponseBody
	public Object getMatSpecList() {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String MAT_BASIC_ID = pd.getString("MAT_BASIC_ID");
			PageData matParam = new PageData();
			matParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
			// 根据物料id获取物料基础信息
			pd = matBasicService.findById(matParam);
			String MAT_AUX_UNIT = "默认包装";
			// 获取主单位名称
			PageData unitMainParam = new PageData();
			unitMainParam.put("UNIT_INFO_ID", pd.get("MAT_MAIN_UNIT"));
			PageData unitMain = Unit_InfoService.findById(unitMainParam);

			String MAT_MAIN_UNIT = " " + String.valueOf(unitMain.get("FNAME"));
			PageData matspecParam = new PageData();
			matspecParam.put("MAT_BASIC_ID", MAT_BASIC_ID);
			List<PageData> listAll = MAT_SPECService.listAll(matspecParam);
			List<String> matSpecList = Lists.newArrayList();
			// 根据物料id获取所有辅单位信息 添加其他规格
			for (int i = 0; i < listAll.size(); i++) {
				String MAT_SPEC_QTY = listAll.get(i).get("MAT_SPEC_QTY").toString();
				String UNIT_INFO_ID = listAll.get(i).getString("UNIT_INFO_ID");
				PageData unitAuxParam = new PageData();
				unitAuxParam.put("UNIT_INFO_ID", UNIT_INFO_ID);
				PageData unitAux = Unit_InfoService.findById(unitAuxParam);

				if (null != unitAux) {
					MAT_AUX_UNIT = String.valueOf(unitAux.get("FNAME"));
					String matSpec = "";
					// 拼接返回值
					matSpec += MAT_SPEC_QTY + " " + MAT_MAIN_UNIT + "/" + MAT_AUX_UNIT;
					matSpecList.add(matSpec);
				}
			}
			// 返回列表
			return AppResult.success(matSpecList);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}
	}
	/**
	 * 根据任务主键获取SOP列表
	 * @return
	 */
	@RequestMapping("/appSOPList")
	@ResponseBody
	public Object appSOPList(AppPage page) {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData appProcessWorkOrderExampleDetailByPK = planningWorkOrderService
					.appProcessWorkOrderExampleDetailByPK(pd);
			if (null == appProcessWorkOrderExampleDetailByPK) {
				appProcessWorkOrderExampleDetailByPK = new PageData();
			}
			
			String SOP_ID = appProcessWorkOrderExampleDetailByPK.getString("SOP_ID");
			PageData soPageData = new PageData();
			soPageData.put("SopSchemeTemplate_ID", SOP_ID);
			soPageData.put("ProcessWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
			if(Tools.notEmpty(SOP_ID)){
				page.setPd(soPageData);
				PageInfo<PageData> pageInfo = new PageInfo<>(pwoeSopStepService.appListPage(page));
				List<PageData> sopList = pageInfo.getList();
				for (PageData sopDetail : sopList) {
					String FType = sopDetail.getString("FType");
					PageData dicPageData = new PageData();
					dicPageData.put("DICTIONARIES_ID", FType);
					PageData findById = DictionariesService.findById(dicPageData);
					if(null== findById){
						sopDetail.put("FType", "标准");
					}else{
						sopDetail.put("FType", findById.getString("NAME"));
					}
					if(Tools.notEmpty(sopDetail.getString("End_Time"))){
						sopDetail.put("FSTATUS", "DONE");
					}else{
						sopDetail.put("FSTATUS", "DOING");
					}
				}
				page.setTotalPage(pageInfo.getPages());
				page.setTotalResult(pageInfo.getTotal());
				return AppResult.success(sopList,page);
			}
			return AppResult.success(Lists.newArrayList());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}		
	}
	
	/**
	 * SOP步骤开始执行
	 * @return
	 */
	@RequestMapping("/appSopBegin")
	@ResponseBody
	public Object appSopBegin() {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.getString("ProcessWorkOrderExample_SopStep_ID");
			PageData sopDetail = pwoeSopStepService.findById(pd);
			sopDetail.put("Begin_Time", Tools.date2Str(new Date()));
			sopDetail.put("FOperator", pd.getString("STAFF_ID"));
			pwoeSopStepService.edit(sopDetail);
			return AppResult.success("SOP开始执行");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}		
	}
	/**
	 * SOP步骤结束执行
	 * @return
	 */
	@RequestMapping("/appSopEnd")
	@ResponseBody
	public Object appSopEnd() {
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.getString("ProcessWorkOrderExample_SopStep_ID");
			PageData sopDetail = pwoeSopStepService.findById(pd);
			if(Tools.isEmpty(sopDetail.getString("Begin_Time"))){
				throw new RuntimeException("该SOP尚未开始不能结束");
			}
			sopDetail.put("End_Time", Tools.date2Str(new Date()));
			sopDetail.put("FOperator", pd.getString("STAFF_ID"));
			sopDetail.put("OperationComment", pd.getString("OperationComment"));
			pwoeSopStepService.edit(sopDetail);
			return AppResult.success("SOP结束执行");
		} catch (Exception e) {
			e.printStackTrace();
			return AppResult.failed(e.getMessage());
		}		
	}
	
	
	/**
	 * 插入物料消耗表(不需要识别二维码的方法)
	 * 
	 * @param response
	 *            UserName code count WorkstationID
	 * @return
	 */
	@RequestMapping("/insertMaterialConsumeNoQRCode")
	@ResponseBody
	public Object insertMaterialConsumeNoQRCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			// 工位id
			String WC_STATION_ID = pd.getString("WC_STATION_ID");
			if (Tools.isEmpty(WC_STATION_ID)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "工位参数没找到!");
				return map;
			}

			String materialId = pd.getString("MAT_BASIC_ID");
			String materialName = pd.getString("MAT_NAME");
			
			String count = pd.getString("count");

			String oneThingsCode = pd.getString("oneThingsCode");

			String qrCode = pd.getString("MAT_CODE");
			
			String materialSProp = "2540539e45324232a50bde60ac2951d3";

			String materialSPropKey = "";
			String specificationDesc = pd.getString("MAT_SPECS");
			String fUnit =pd.getString("fUnit");
			String fBatch = "";
			PageData pd1111 = WC_StationService.findById(pd);
			// 如果不存在一物码 则就是 类型码的物料则
			if(StringUtil.isEmpty(oneThingsCode)){
				PageData pData = new PageData();
				// 类型码
				pData.put("QRCode", qrCode);
				if(null != pd1111){
					pData.put("PositionID",  pd1111.get("ConsumptionLocation"));
					pData.put("inputMat", "inputMat");
					}
				List<PageData> listAll = stockService.listAll(pData);
				if (CollectionUtil.isNotEmpty(listAll)) {
					materialName = listAll.get(0).getString("MAT_NAME");
					materialSProp = listAll.get(0).getString("MaterialSProp")==null?materialSProp:listAll.get(0).getString("MaterialSProp");
					materialSPropKey = listAll.get(0).getString("MaterialSPropKey")==null?"4486905b9e47428985c36c9d55d21ad7":listAll.get(0).getString("MaterialSPropKey");
					materialId = listAll.get(0).getString("ItemID");
					if (Tools.isEmpty(materialId)) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "该物料在库存中不存在");
						return map;
					}
					specificationDesc = listAll.get(0).get("SpecificationDesc").toString();
					fUnit = listAll.get(0).getString("FUnit");
					fBatch = listAll.get(0).getString("FBatch");
				}
			}else{
				PageData pData = new PageData();
				// 一物码
				pData.put("OneThingCode", oneThingsCode);
				if(null != pd1111){
					pData.put("PositionID",  pd1111.get("ConsumptionLocation"));
					pData.put("inputMat", "inputMat");
					}
				List<PageData> listAll = stockService.listAll(pData);
				if (CollectionUtil.isNotEmpty(listAll)) {
					qrCode = listAll.get(0).getString("QRCode");
					materialName = listAll.get(0).getString("MAT_NAME");
					materialSProp = listAll.get(0).getString("MaterialSProp");
					materialSPropKey = listAll.get(0).getString("MaterialSPropKey");
					materialId = listAll.get(0).getString("ItemID");
					if (Tools.isEmpty(materialId)) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "该物料在库存中不存在");
						return map;
					}
					specificationDesc = listAll.get(0).get("SpecificationDesc").toString();
					fUnit = listAll.get(0).getString("FUnit");
					fBatch = listAll.get(0).getString("FBatch");
				}else{
					errInfo = "error";
					map.put("result", errInfo);
					map.put("msg", "该物料在库存中不存在");
					return map;
				}
			}
	
			PageData inputData = new PageData();

			PageData findById = processWorkOrderExampleService.findById(pd);
			String BatchNum = findById.getString("BatchNum");
			String PlanningWorkOrderID = findById.getString("PlanningWorkOrderID");

			// 查投入产出实例表 看该物料是否在当前任务下 允许投入
			PageData wpioeParam = new PageData();
			wpioeParam.put("processWorkOrderExample_ID", pd.getString("ProcessWorkOrderExample_ID"));
			List<String> array = Lists.newArrayList();
			array.add(materialId);
			wpioeParam.put("array", array);
			wpioeParam.put("TType", "投入");
			List<PageData> wpioeList = workorderProcessIOExampleService.listAll(wpioeParam);
			if (CollectionUtil.isEmpty(wpioeList)) {
				errInfo = "error";
				map.put("result", errInfo);
				map.put("msg", "该物料不适用于该任务");
				return map;
			}

			// 根据传过来的仓库 库位 影响库存
			PageData stationParam = new PageData();
			stationParam.put("WC_STATION_ID", WC_STATION_ID);
			PageData stationInfo = stationService.findById(stationParam);
			String stockSum = "0";
			if (null != stationInfo) {
				String WareHouseId = stationInfo.getString("ConsumptionWarehouse");
				// 消耗仓库 根据仓库获取该物料的 库存 如果库存不足，提示
				PageData pdKC = new PageData();
				pdKC.put("WarehouseID", WareHouseId);
				pdKC.put("ItemID", materialId);
				PageData pdStockSum = StockService.getSum(pdKC);// 即时库存

				if (null == pdStockSum) {
					errInfo = "error";
					map.put("result", errInfo);
					map.put("msg", "库存不足");
					return map;
				}

				if (null != pdStockSum) {

					if ("0.00".equals(pdStockSum.get("stockSum").toString())) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "库存不足");
						return map;
						
					}

					// 获取即时库存
					stockSum = pdStockSum.get("stockSum").toString();
					double stockSumDouble = Double.valueOf(stockSum);
					double countDouble = Double.valueOf(count);
					// 如果需求数量 大于了 即时库存数量 提示库存不足
					if (countDouble > stockSumDouble) {
						errInfo = "error";
						map.put("result", errInfo);
						map.put("msg", "库存不足");
						return map;
					}

					// 扣减库存
					PageData outParam = new PageData();
					outParam.put("num", countDouble);// 出库数量
					outParam.put("WarehouseID", WareHouseId);// 仓库id
					outParam.put("ItemID", materialId);// 物料id
					outParam.put("MaterialSPropKey", materialSPropKey);// 辅助属性值id
					StockService.outStock(outParam);
					String WorkorderProcessIOExample_ID = wpioeList.get(0).getString("WorkorderProcessIOExample_ID");

					// 插入到消耗产出表
					PageData materialConsume = new PageData();
					materialConsume.put("MaterialConsume_ID", this.get32UUID());
					materialConsume.put("ConsumptionDocumentID", WorkorderProcessIOExample_ID);
					materialConsume.put("FType", "消耗");
					materialConsume.put("DataSources", "PP_WorkorderProcessIOExample");
					materialConsume.put("MaterialID", materialId);
					materialConsume.put("MaterialName", materialName);
					materialConsume.put("MaterialBarCode", qrCode);
					materialConsume.put("MaterialBatchNum", fBatch);
					materialConsume.put("SpecificationsAndModels", count + " " + specificationDesc);
					materialConsume.put("MaterialSProp", materialSProp);
					materialConsume.put("MaterialSPropKey", materialSPropKey);
					materialConsume.put("FUnit", fUnit);
					materialConsume.put("ConsumptionQuantity", count);
					materialConsume.put("WorkOrderRel", PlanningWorkOrderID);
					materialConsume.put("FOperatorID", pd.getString("STAFF_ID"));
					materialConsume.put("FOperateTime", Tools.date2Str(new Date()));
					materialConsume.put("BatchNum", BatchNum);
					if(StringUtil.isEmpty(oneThingsCode)){
						oneThingsCode = null;
					}
					materialConsume.put("OneThingCode", oneThingsCode);
					MaterialConsumeService.save(materialConsume);
					inputData.put("ProcessIMateriel", qrCode + "|" + materialName);
					// 返回冒泡信息
					PageData materialConsumeParam = new PageData();
					materialConsumeParam.put("ConsumptionDocumentID", WorkorderProcessIOExample_ID);
					materialConsumeParam.put("FType", "消耗");
					materialConsumeParam.put("MaterialID", materialId);
					materialConsumeParam.put("DataSources", "PP_WorkorderProcessIOExample");
					List<PageData> MaterialConsumeList = MaterialConsumeService.listAll(materialConsumeParam);
					if (CollectionUtil.isNotEmpty(MaterialConsumeList)) {
						BigDecimal ConsumptionQuantityBigDec = new BigDecimal(0);
						for (PageData pageData : MaterialConsumeList) {
							BigDecimal i = new BigDecimal(String.valueOf(pageData.get("ConsumptionQuantity")));
							ConsumptionQuantityBigDec = ConsumptionQuantityBigDec.add(i);
						}
						inputData.put("ConsumptionQuantity", ConsumptionQuantityBigDec);
						inputData.put("PlannedQuantity", wpioeList.get(0).get("PlannedQuantity"));
						BigDecimal PlannedQuantityBigDecimal = new BigDecimal(
								String.valueOf(wpioeList.get(0).get("PlannedQuantity")));
						BigDecimal divide = ConsumptionQuantityBigDec.divide(PlannedQuantityBigDecimal, 4,
								BigDecimal.ROUND_HALF_DOWN);
						BigDecimal multiply = divide.multiply(new BigDecimal("100"));
						if (multiply.doubleValue() > 100.0) {
							multiply = new BigDecimal("100");
						}
						BigDecimal subtract = PlannedQuantityBigDecimal.subtract(ConsumptionQuantityBigDec);
						if (subtract.doubleValue() < 0) {
							inputData.put("Subtract", 0);
						} else {
							inputData.put("Subtract", subtract.doubleValue());
						}
						inputData.put("InputPercent", multiply.doubleValue());
					} else {
						inputData.put("ConsumptionQuantity", 0);
						inputData.put("PlannedQuantity", 0);
						inputData.put("InputPercent", 0);
						inputData.put("Subtract", 0);
					}

				}
			}
			errInfo = "success";
			map.put("result", errInfo);
			map.put("msg", "物料消耗成功");
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "error";
			map.put("result", errInfo);
			map.put("msg", e.getMessage());
			return map;
		}

	}
	
	
}
