/**
 * 
 */
package org.yy.controller.pp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.StationTime;
import org.yy.service.app.AppRegistrationService;
import org.yy.service.fhoa.NoticeService;
import org.yy.service.fhoa.StaffService;
import org.yy.service.flow.NEWPLAN_BOMService;
import org.yy.service.km.CodingRulesService;
import org.yy.service.km.ProductionCalendarService;
import org.yy.service.km.StandardCapacityService;
import org.yy.service.mbase.MAT_AUXILIARYMxService;
import org.yy.service.mbase.MAT_BASICService;
import org.yy.service.mm.CallMaterialDetailsFLService;
import org.yy.service.mm.CallMaterialDetailsService;
import org.yy.service.mm.CallMaterialFLService;
import org.yy.service.mm.CallMaterialOperateService;
import org.yy.service.mm.CallMaterialService;
import org.yy.service.mm.MaterialRequirementService;
import org.yy.service.mm.StockService;
import org.yy.service.mom.WC_StationService;
import org.yy.service.mom.WorkStationPersonRelService;
import org.yy.service.pp.APSService;
import org.yy.service.pp.PlanningWorkOrderService;
import org.yy.service.pp.ProcessWorkOrderExampleService;
import org.yy.service.pp.SALESORDERDETAILService;
import org.yy.service.pp.SALESORDERService;
import org.yy.service.pp.WorkorderProcessIOExampleService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.service.project.manager.Cabinet_BOMService;
import org.yy.util.DateUtil;
import org.yy.util.JpushClientUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;
import org.yy.util.weixin.SendWeChatMessageMes;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.util.StringUtil;

/**
 * @author chen
 *
 */
@Controller
@RequestMapping("/aps")
public class APSController extends BaseController {
	@Autowired
	private PlanningWorkOrderService PlanningWorkOrderService;
	@Autowired
	private ProcessWorkOrderExampleService processWorkOrderExampleService;
	@Autowired
	private StandardCapacityService StandardCapacityService;
	@Autowired
	private ProductionCalendarService ProductionCalendarService;

	@Autowired
	private StaffService StaffService;
	@Autowired
	private WorkorderProcessIOExampleService WorkorderProcessIOExampleService;
	@Autowired
	private APSService apsService;
	@Autowired
	private SALESORDERDETAILService SALESORDERDETAILService;
	@Autowired
	private SALESORDERService SALESORDERService;
	@Autowired
	private StandardCapacityService standardcapacityService;
	@Autowired
	private MaterialRequirementService MaterialRequirementService;
	@Autowired
	private StockService StockService;
//	@Autowired
//	private ProcessDefinitionService ProcessDefinitionService;
	@Autowired
	private WC_StationService wc_StationController;
	@Autowired
	private MAT_BASICService mat_basicService;
	@Autowired
	private NEWPLAN_BOMService NEWPLAN_BOMService;
	@Autowired
	private ProcessWorkOrderExampleService ProcessWorkOrderExampleService;
	@Autowired
	private WorkStationPersonRelService WorkStationPersonRelService;
	@Autowired
	private AppRegistrationService AppRegistrationService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private CallMaterialService CallMaterialService;
	@Autowired
	private CallMaterialOperateService CallMaterialOperateService;
	@Autowired
	private CallMaterialDetailsService CallMaterialDetailsService;
	@Autowired
	private CodingRulesService CodingRulesService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;
	@Autowired
	private Cabinet_BOMService Cabinet_BOMService;
	@Autowired
	private MAT_AUXILIARYMxService mat_auxiliarymxService;
	@Autowired
	private CallMaterialFLService callmaterialflService;
	@Autowired
	private CallMaterialDetailsFLService callmaterialdetailsflService;

	/**
	 * 根据计划工单id 判断当前工单排程是否可以进行修改
	 * 
	 * @param PlanningWorkOrder_ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/isAbledUpdate")
	public Object isAbledUpdate() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PlanningWorkOrder_ID = pd.getString("PlanningWorkOrder_ID");
		if (Tools.isEmpty(PlanningWorkOrder_ID)) {
			throw new RuntimeException("PlanningWorkOrder_ID参数不存在");
		}
		pd.put("PlanningWorkOrder_ID", PlanningWorkOrder_ID);

		isAbleUpdateByPlanningWorkId(pd);
		map.put("data", true);
		map.put("result", errInfo);
		return map;
	}

	// 根据计划工单id 判断该工单是否可以排程
	private void isAbleUpdateByPlanningWorkId(PageData pd) throws Exception {
		// 查询计划工单状态
		PageData findById = PlanningWorkOrderService.findById(pd);
		if (null == findById) {
			throw new RuntimeException("计划工单不存在");
		}

		String ScheduleSchedule = findById.getString("ScheduleSchedule");
		String DistributionProgress = findById.getString("DistributionProgress");

		// 在排程中，如果当前计划工单的状态为（ 已排程 已下发）则不允许修改 提示当前状态下的计划工单不允许修改排程
		if ("已排程".equals(ScheduleSchedule) && "已下发".equals(DistributionProgress)) {
			throw new RuntimeException("当前状态已排程已下发，计划工单不允许修改排程");
		}

		// 在排程中，如果当前计划工单的状态为（ 已排程 未下发）则不允许修改 提示请先修改计划工单状态为未排程后再修改排程
		if ("已排程".equals(ScheduleSchedule) && "未下发".equals(DistributionProgress)) {
			throw new RuntimeException("请先修改计划工单状态为未排程后再修改排程");
		}
	}

	/**
	 * 根据计划工单id 判断当前是否满足手动排程的条件
	 * 
	 * @param PlanningWorkOrder_ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/satisfiedAPSManualByPlanningWorkOrderId")
	public Object satisfiedAPSManualByPlanningWorkOrderId() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PlanningWorkOrder_ID = pd.getString("PlanningWorkOrder_ID");
		if (Tools.isEmpty(PlanningWorkOrder_ID)) {
			throw new RuntimeException("PlanningWorkOrder_ID参数不存在");
		}
		// 1、判断该计划工单所绑定的工位、工序是否设置了标准产能 如果没有则不能参与排程
		pd.put("PlanningWorkOrderID", PlanningWorkOrder_ID);
		List<PageData> listByPlanningWorkOrderID = apsService.listByPlanningWorkOrderID(pd);
		if (CollectionUtil.isEmpty(listByPlanningWorkOrderID)) {
			throw new RuntimeException("不能进行排程，工艺工单工序实例数据不存在");
		}
		for (PageData pageData : listByPlanningWorkOrderID) {
			String FSTATION = pageData.getString("FStation");
			String WP = pageData.getString("WP");
			List<PageData> listByStationAndWP = StandardCapacityService.listByStationAndWP(FSTATION, WP);
			if (CollectionUtil.isEmpty(listByStationAndWP)) {
				throw new RuntimeException("不能进行排程，不存在该工单关联的工序的标准产能");
			}
			PageData pData = new PageData();
			pData.put("SuitableForStation", WP);

			List<PageData> listbyStation = ProductionCalendarService.listbyStation(pData);

			// 2、是否设置了工作时间
			if (CollectionUtil.isEmpty(listbyStation)) {
				throw new RuntimeException("不能进行排程，不存在该工单关联的工序的工作时间");
			}
		}

		// 3、返回结果
		map.put("data", true);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据计划工单id 判断当前是否满足自动排程的条件
	 * 
	 * @param PlanningWorkOrder_ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/satisfiedAPSAutoByPlanningWorkOrderId")
	public Object satisfiedAPSAutoByPlanningWorkOrderId() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String PlanningWorkOrder_ID = pd.getString("PlanningWorkOrder_ID");
		if (Tools.isEmpty(PlanningWorkOrder_ID)) {
			throw new RuntimeException("PlanningWorkOrder_ID参数不存在");
		}
		// 1、判断该计划工单所绑定的工位、工序是否设置了标准产能 如果没有则不能参与排程
		pd.put("PlanningWorkOrderID", PlanningWorkOrder_ID);
		List<PageData> listByPlanningWorkOrderID = apsService.listByPlanningWorkOrderID(pd);
		for (PageData pageData : listByPlanningWorkOrderID) {
			String FStation = pageData.getString("FStation");
			String WP = pageData.getString("WP");
			List<PageData> listByStationAndWP = StandardCapacityService.listByStationAndWP(FStation, WP);
			if (CollectionUtil.isEmpty(listByStationAndWP)) {
				throw new RuntimeException("不能进行排程，不存在该工单关联的工序的标准产能");
			}
			for (PageData scData : listByStationAndWP) {

				// 2、是否设置了标准产能值
				if (Tools.isEmpty(scData.getString("StandardCapacityValue"))) {
					throw new RuntimeException("不能进行排程，该数据所关联的标准产能值为空");
				}
			}
			pd.put("SuitableForStation", WP);
			List<PageData> listbyStation = ProductionCalendarService.listbyStation(pd);

			// 3、是否设置了工作时间
			if (CollectionUtil.isEmpty(listbyStation)) {
				throw new RuntimeException("不能进行排程，不存在该工单关联的工序的工作时间");
			}
		}
		map.put("data", true);
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping("/getAPSDetailByPlanningWorkOrderIds")
	@ResponseBody
	public Object getAPSDetailByPlanningWorkOrderIds() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		String[] split = DATA_IDS.split(",yl,");
		List<PageData> resultList = Lists.newArrayList();

		if (split.length > 0) {
			for (String planningWorkOrderId : split) {
				PageData pageData = new PageData();
				pageData.put("PlanningWorkOrder_ID", planningWorkOrderId);
				PageData findById = PlanningWorkOrderService.findById(pageData);
				if (null != findById) {
					resultList.add(findById);
				}
			}
		}
		map.put("varlist", resultList);
		map.put("result", errInfo);
		return map;

	}

	/**
	 * 根据计划工单状态获取排程的数据（ 未排程未下发 、已排程待下发、 已排程已下发、订单号、工单号、工序）
	 * 
	 * @param ScheduleSchedule
	 * @param DistributionProgress
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getAPSDataByStatus")
	@ResponseBody
	public Object getAPSDataByStatus(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		String ScheduleSchedule = pd.getString("ScheduleSchedule");
		if (Tools.isEmpty(ScheduleSchedule)) {
			throw new RuntimeException("ScheduleSchedule参数不存在");
		}
		String DistributionProgress = pd.getString("DistributionProgress");
		if (Tools.isEmpty(DistributionProgress)) {
			throw new RuntimeException("DistributionProgress参数不存在");
		}
		// 根据 排程和下发状态获取计划工单
		List<PageData> list = PlanningWorkOrderService.listAllByScheduleAndDistributionProgresslistPage(page);
		if (CollectionUtil.isEmpty(list)) {
			map.put("varlist", Lists.newArrayList());
			map.put("result", errInfo);
			map.put("page", page);
			map.put("apsCount", 0);
			return map;
		}
		for (PageData pageData : list) {
			String FPlanner = pageData.getString("FPlanner");
			String FPlannerStr = "";
			if (Tools.notEmpty(FPlanner)) {
				String[] split = FPlanner.split(",yl,");

				for (String str : split) {
					FPlannerStr += (str + ",");
				}
				FPlannerStr = FPlannerStr.substring(0, FPlannerStr.length() - 1);
			}
			pageData.put("FPlanner", FPlannerStr);
		}
		map.put("varlist", list);
		map.put("result", errInfo);
		map.put("page", page);
		return map;

	}

	/**
	 * 根据生产任务id获取投入物料列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getInputMaterialListByPwoeID")
	public Object getInputMaterialListByPwoeID() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String ProcessWorkOrderExample_ID = pd.getString("ProcessWorkOrderExample_ID");
		List<PageData> listByProcessWorkOrderExampleID = WorkorderProcessIOExampleService
				.listByProcessWorkOrderExampleID(ProcessWorkOrderExample_ID);
		List<PageData> collect = listByProcessWorkOrderExampleID.stream().filter(l -> "投入".equals(l.getString("TType")))
				.collect(Collectors.toList());
		List<PageData> inputDataList = Lists.newArrayList();
		if (CollectionUtil.isNotEmpty(collect)) {
			for (PageData pageData : collect) {
				String MaterialID = pageData.getString("MaterialID");
				PageData inputData = new PageData();
				inputData.put("MAT_BASIC_ID", MaterialID);
				PageData findById = new PageData();
				try {
					findById = mat_basicService.findById(inputData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String MAT_NAME = String.valueOf(findById.get("MAT_NAME"));
				String MAT_SPECS = String.valueOf(findById.get("MAT_SPECS"));
				String MAT_CODE = String.valueOf(findById.get("MAT_CODE"));
				inputData.put("MAT_NAME", MAT_NAME);
				inputData.put("MAT_CODE", MAT_CODE);
				inputData.put("MAT_SPECS", MAT_SPECS);
				Double PlannedQuantity = Double.valueOf(String.valueOf(pageData.get("PlannedQuantity")));
				inputData.put("PlannedQuantity", PlannedQuantity);
				inputDataList.add(inputData);
			}
			map.put("data", inputDataList);
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获取所有有效的员工（排程指定）
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getAllEffectivePerson")
	public Object getAllEffectivePerson() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> listAll = StaffService.getStaffList(pd);
		if (CollectionUtil.isEmpty(listAll)) {
			throw new RuntimeException("不存在员工，无法下发任务");
		}
		map.put("data", listAll);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 根据工位获取该工位下的所有员工
	 * 
	 * @param FStation
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getEffectivePersonByFStation")
	public Object getEffectivePersonByFStation() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FStation = pd.getString("FStation");
		if (Tools.isEmpty(FStation)) {
			throw new RuntimeException("FStation参数不存在");
		}
		pd.put("WorkstationID", FStation);
		List<PageData> listAll = WorkStationPersonRelService.listAll(pd);
		if (CollectionUtil.isEmpty(listAll)) {
			throw new RuntimeException("该工位下没有员工，无法下发任务");
		}
		map.put("data", listAll);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 执行手动排程动作 v2 管悦 20210803 修改计划员并且不改变状态
	 * 
	 * @param pageData
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/doAPSManual")
	public Object doAPSManual() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String paramList = pd.getString("paramList");
		if (Tools.isEmpty(paramList)) {
			throw new RuntimeException("参数没传");
		}
		List<PageData> paramListArray = JSON.parseArray(paramList, PageData.class);
		for (PageData param : paramListArray) {
			// 是否下发
			String ifIssue = param.getString("ifIssue");
			// 更改下发状态
			param.put("ScheduleSchedule", "已排程");
			if ("Y".equals(ifIssue)) {
				param.put("DistributionProgress", "已下发");
			} else {
				param.put("DistributionProgress", "待下发");
			}
			// 更新计划工单 排程和下发状态 执行更新操作返回成功 否则抛异常 排程失败
			// PlanningWorkOrderService.edit(param);
			String PlanningWorkOrderID = param.getString("PlanningWorkOrder_ID");
			PageData pdOr = new PageData();
			pdOr.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
			pdOr = PlanningWorkOrderService.findById(pdOr);
			pdOr.put("FPlanner", param.getString("FPlannerx"));
			pdOr.put("FPlannerID", param.getString("FPlannerIDx"));
			pdOr.put("PlannedBeginTime", param.getString("PlannedBeginTime"));
			// （从工单获取） 计划结束时间
			pdOr.put("PlannedEndTime", param.getString("PlannedEndTime"));
			PlanningWorkOrderService.edit(pdOr);
			List<PageData> listProcessWorkOrderExampleByPlanningWorkOrderID = PlanningWorkOrderService
					.getListProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);
			for (PageData pageData : listProcessWorkOrderExampleByPlanningWorkOrderID) {
				// （从工单获取） 计划开始时间
				pageData.put("PlannedBeginTime", param.getString("PlannedBeginTime"));
				// （从工单获取） 计划结束时间
				pageData.put("PlannedEndTime", param.getString("PlannedEndTime"));

				ProcessWorkOrderExampleService.edit(pageData);
			}
		}

		map.put("result", errInfo);
		return map;
	}

	/**
	 * 执行自动排程动作
	 * 
	 * @param pageData
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/doAPSAuto")
	public Object doAPSAuto() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String errInfo = "success";
		// 开始执行
		PageData pd = new PageData();
		pd = this.getPageData();
		String PlanningWorkOrder_ID = pd.getString("PlanningWorkOrderID");
		String resultProcessWorkOrderExampleList = pd.getString("WPAndExecutorIDList");
		List<PageData> parseArray = JSON.parseArray(resultProcessWorkOrderExampleList, PageData.class);
		if (Tools.isEmpty(PlanningWorkOrder_ID)) {
			throw new RuntimeException("PlanningWorkOrder_ID参数不存在");
		}
		pd.put("PlanningWorkOrder_ID", PlanningWorkOrder_ID);
		// 判断是否可以执行排程操作 注释上是为了 排程完成后仍可以修改
		// isAbleUpdateByPlanningWorkId(pd);
		// 如果排程类型为有限排程的话 物料不齐套 不让排程
		// 如果排程类型为无限排程的话 物料不齐套 也可以排程
		if ("有限排程".equals(pd.getString("apsType"))) {
			// 查看当前计划工单所需物料 是否齐套
			PageData ifCompleteSetOfMaterialsByPlanningWorkOrderID = this
					.ifCompleteSetOfMaterialsByPlanningWorkOrderID(PlanningWorkOrder_ID);
			if ("N".equals(ifCompleteSetOfMaterialsByPlanningWorkOrderID.getString("ifAllComplete"))) {
				throw new RuntimeException("查看当前计划工单所需物料 未齐套 不能进行排程");
			}
		}
		List<PageData> processWorkOrderExampleList = PlanningWorkOrderService
				.getListProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrder_ID);

		// 如果传过来的工序和执行人，查出来数据匹配，则更新执行人
		parseArray.forEach(p -> {
			ProcessWorkOrderExampleService.updateExecutorIDByWPAndPlanningWorkOrderID(PlanningWorkOrder_ID,
					p.getString("WP"), p.getString("ExecutorID"), p.getString("FStation"));
		});

		// 获取所有工序
		List<Map<String, Object>> wpAllList = Lists.newArrayList();
		for (PageData pageData : processWorkOrderExampleList) {
			Map<String, Object> map2 = Maps.newHashMap();
			String ProcessWorkOrderExample_ID = pageData.getString("ProcessWorkOrderExample_ID");
			String wpString = pageData.getString("WP");
			String SerialNum = pageData.getString("SerialNum");
			String FStation = pageData.getString("FStation");
			map2.put("ProcessWorkOrderExample_ID", ProcessWorkOrderExample_ID);
			map2.put("WP", wpString);
			map2.put("SerialNum", SerialNum);
			map2.put("FStation", FStation);
			// 根据工艺工单工序实例 获取 投入产出实例
			List<PageData> workorderProcessIOExampleList = apsService
					.getWorkorderProcessIOExampleListByProcessWorkOrderExampleID(ProcessWorkOrderExample_ID);
			if (CollectionUtil.isEmpty(workorderProcessIOExampleList)) {
				throw new RuntimeException("该工艺工单工序实例未配置投入产出");
			}
			// 如果说该工序下有产出则把 计划量带过来 没有则空着
			workorderProcessIOExampleList = workorderProcessIOExampleList.stream()
					.filter(w -> w.getString("TType").equals("产出")).collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(workorderProcessIOExampleList)) {
				String PlannedQuantity = workorderProcessIOExampleList.get(0).get("PlannedQuantity").toString();
				map2.put("PlannedQuantity", PlannedQuantity);
			}
			wpAllList.add(map2);
		}
		String timeStr = pd.getString("beginTime");

		// 循环工序
		for (Map<String, Object> map2 : wpAllList) {

			String updateProcessWorkOrderExampleBeginTime = "";
			String updateProcessWorkOrderExampleEndTime = "";
			String updateProcessWorkOrderExampleWorkHours = "";
			String updateProcessWorkOrderExampleWorkHoursUnit = "";

			// 计划量
			String PlannedQuantityStr = String.valueOf(map2.get("PlannedQuantity"));
			if (Tools.isEmpty(PlannedQuantityStr)) {
				throw new RuntimeException("计划量为空");
			}
			Double doublePlannedQuantity = Double.valueOf(PlannedQuantityStr);
			// 获取工序
			String WP = map2.get("WP").toString();
			// 获取工位
			String FStation = map2.get("FStation").toString();
			// 根据工序获取标准产能 根据工位获取工作时间
			PageData standardCapacityListByWP = standardcapacityService.getByWP(WP);
			if (null == standardCapacityListByWP) {
				throw new RuntimeException("工序" + WP + "，工位" + FStation + "未设置产能");
			}

			// 需要根据计划数量 和 标准产能 工位开始结束时间 计算 每个工位的所用时长
			// 标准产能值
			String StandardCapacityValueStr = standardCapacityListByWP.getString("StandardCapacityValue");
			// 标准产能单位
			String StandardCapacityUnitStr = standardCapacityListByWP.getString("StandardCapacityUnit");
			// 标准产能单位值
			String StandardCapacityUnitValueStr = StandardCapacityUnitStr.substring(0, 1);
			// 标准产能单位后缀
			String StandardCapacityUnitSuffixStr = StandardCapacityUnitStr.substring(1,
					StandardCapacityUnitStr.length());
			// 标准产能单位值 double 单位例如 1小时、1分钟、1天等单位 前面的那个数字 默认 1
			Double StandardCapacityUnitValue = Double.valueOf(StandardCapacityUnitValueStr);
			// 标准产能 double值 ，默认所有工位的产能值都是一样的
			Double StandardCapacityValue = Double.valueOf(StandardCapacityValueStr);
			if (Tools.isEmpty(PlannedQuantityStr)) {
				throw new RuntimeException("该工序没有配置产出值，若没有产出，则需要配置该工序的单位占用时间");
			}
			// 要生产的计划数量 double
			Double PlannedQuantity = Double.valueOf(PlannedQuantityStr);

			// 获取工位的工作时间 多个工序使用 ,yl, 分割
			String[] FStationSplit = FStation.split(",yl,");
			List<StationTime> StationTimeList = Lists.newArrayList();
			String beginTime = timeStr;
			for (int i = 0; i < FStationSplit.length; i++) {
				StationTime stationTime = new StationTime();
				String station = FStationSplit[i];
				PageData pData = new PageData();
				pData.put("SuitableForStation", station);
				// 根据工位获取生产日历的时间， 默认每个工位的工作时间为 00:00:00 - 23:59:59
				List<PageData> listbyStation = ProductionCalendarService.listbyStation(pData);
				if (CollectionUtil.isEmpty(listbyStation)) {
					throw new RuntimeException("该工位" + station + "没有设置生产时间");
				}
				// 当，工位设置了多个生产日历时，默认取第一个
				PageData pc = listbyStation.get(0);
				// 适用日期
				String Applicable = pc.getString("ApplicableDateValue");
				// 计算工位可以开始的时间 如果当前工位被别的工序占用的 要等到别的工序做完 才能安排时间
				beginTime = calcWPBeginTimeByStation(beginTime, station);

				stationTime.setApplicableDate(Applicable);
				stationTime.setStation(station);
				stationTime.setTime(sdf.parse(beginTime));
				StationTimeList.add(stationTime);
			}
			// 根据开始时间进行排序
			List<StationTime> resultStationTimeList = StationTimeList.stream()
					.sorted(Comparator.comparing(StationTime::getTime)).collect(Collectors.toList());

			// 多个工位的情况
			if (resultStationTimeList.size() > 1) {
				// 赋值工序的开始时间 ，为工位最早的时间
				updateProcessWorkOrderExampleBeginTime = sdf.format(resultStationTimeList.get(0).getTime());
				Double processCount = 0.0;
				// 判断当前是否都生产完了 标识
				boolean flag = false;
				long totaldiff = 0;
				for (int i = 0; i < resultStationTimeList.size(); i++) {
					Double currentProcessCount = processCount;

					long currentdiff = totaldiff;
					if (i == resultStationTimeList.size() - 1) {
						continue;
					}
					// 前一个时间和后一个时间比较做差 因为标准产能都相同 所以 计算生产量的时候，在此基础上要乘以 当时生产的工位个数
					Date timePre = resultStationTimeList.get(i).getTime();
					Date timeNext = resultStationTimeList.get(i + 1).getTime();

					long diff = timeNext.getTime() - timePre.getTime();
					if ("秒".equals(StandardCapacityUnitSuffixStr)) {
						// 时间差换成秒
						long sec = diff / (1000);
						// 计算产能
						Double secDouble = BigDecimal.valueOf(sec).doubleValue();
						processCount += secDouble * StandardCapacityValue * (i + 1) / StandardCapacityUnitValue;
					}
					if ("分钟".equals(StandardCapacityUnitSuffixStr)) {
						// 时间差换成分钟
						long min = diff / (1000 * 60);
						// 计算产能
						Double secDouble = BigDecimal.valueOf(min).doubleValue();
						processCount += secDouble * StandardCapacityValue * (i + 1) / StandardCapacityUnitValue;
					}
					if ("小时".equals(StandardCapacityUnitSuffixStr)) {
						// 时间差换成小时
						long hour = diff / (1000 * 60 * 60);
						// 计算产能
						Double secDouble = BigDecimal.valueOf(hour).doubleValue();
						processCount += secDouble * StandardCapacityValue * (i + 1) / StandardCapacityUnitValue;
					}
					if ("天".equals(StandardCapacityUnitSuffixStr)) {
						// 时间差换成天
						long days = diff / (1000 * 60 * 60 * 24);
						// 计算产能
						Double secDouble = BigDecimal.valueOf(days).doubleValue();
						processCount += secDouble * StandardCapacityValue * (i + 1) / StandardCapacityUnitValue;
					}
					totaldiff += diff;
					// 需求数量和产出数量对比，计算出最终计划时长 和最终的结束时间； 更新数据
					// 如果生产的数量大于计划量, 则停止排期，获取多生产的数量，当前结束时间、产能 ，往前推算出结束时间
					if (processCount >= doublePlannedQuantity) {
						flag = true;
						// 多的那部分产量
						Double count = processCount - currentProcessCount;
						// 多的产量除以产能和工位个数 = 多出来的时间
						Double perUnit = count / (i + 1) / StandardCapacityValue * StandardCapacityUnitValue;
						Date endTime = new Date();
						long perUnitTime = 0;
						if ("秒".equals(StandardCapacityUnitSuffixStr)) {
							// timeNext 往前推 这些秒 算结束时间
							perUnitTime = perUnit.intValue();
							endTime = DateUtils.addSeconds(timeNext, -perUnit.intValue());

						}
						if ("分钟".equals(StandardCapacityUnitSuffixStr)) {
							// timeNext 往前推 这些秒 算结束时间
							perUnitTime = perUnit.intValue();
							endTime = DateUtils.addSeconds(timeNext, -perUnit.intValue() * 60);
						}
						if ("小时".equals(StandardCapacityUnitSuffixStr)) {
							// timeNext 往前推 这些秒 算结束时间
							perUnitTime = perUnit.intValue() * 60 * 60;
							endTime = DateUtils.addSeconds(timeNext, -perUnit.intValue() * 60 * 60);
						}
						if ("天".equals(StandardCapacityUnitSuffixStr)) {
							// timeNext 往前推 这些秒 算结束时间
							perUnitTime = perUnit.intValue() * 60 * 60 * 60;
							endTime = DateUtils.addSeconds(timeNext, -perUnit.intValue() * 60 * 60 * 60);
						}
						currentdiff = totaldiff - perUnitTime;
						updateProcessWorkOrderExampleEndTime = sdf.format(endTime);
						// 如果这个时间不在 配置生产日历中
						updateProcessWorkOrderExampleWorkHours = String.valueOf(currentdiff / 1000);
						updateProcessWorkOrderExampleWorkHoursUnit = "秒";
						continue;
					}
				}
				if (!flag) {
					Date restTime = resultStationTimeList.get(resultStationTimeList.size() - 1).getTime();
					// 剩下的各个工位同时生产剩余数量
					Double restCount = doublePlannedQuantity - processCount;
					// 总共有多少个工位同时干
					int size = resultStationTimeList.size();
					Double perUnit = restCount / size / StandardCapacityValue * StandardCapacityUnitValue;
					if ("秒".equals(StandardCapacityUnitSuffixStr)) {
						// restTime 往后推 这些秒 算结束时间
						updateProcessWorkOrderExampleEndTime = sdf
								.format(DateUtils.addSeconds(restTime, perUnit.intValue()));
					}
					if ("分钟".equals(StandardCapacityUnitSuffixStr)) {
						// restTime 往后推 这些秒 算结束时间
						updateProcessWorkOrderExampleEndTime = sdf
								.format(DateUtils.addSeconds(restTime, perUnit.intValue() * 60));
					}
					if ("小时".equals(StandardCapacityUnitSuffixStr)) {
						// restTime 往后推 这些秒 算结束时间
						updateProcessWorkOrderExampleEndTime = sdf
								.format(DateUtils.addSeconds(restTime, perUnit.intValue() * 60 * 60));
					}
					if ("天".equals(StandardCapacityUnitSuffixStr)) {
						// restTime 往后推 这些秒 算结束时间
						updateProcessWorkOrderExampleEndTime = sdf
								.format(DateUtils.addSeconds(restTime, perUnit.intValue() * 60 * 60 * 60));
					}
					updateProcessWorkOrderExampleWorkHours = String
							.valueOf((sdf.parse(updateProcessWorkOrderExampleEndTime).getTime()
									- sdf.parse(updateProcessWorkOrderExampleBeginTime).getTime()) / 1000);
					updateProcessWorkOrderExampleWorkHoursUnit = "秒";
				}
			}
			// 一个工位的情况
			else {

				// 计划量 / 标准产能 = 所用时间 （单位）
				Double useTime = PlannedQuantity / StandardCapacityValue * StandardCapacityUnitValue;
				Date beginTime1 = resultStationTimeList.get(0).getTime();
				// 赋值工序的开始时间 计划开始时间为工位最早的时间
				updateProcessWorkOrderExampleBeginTime = sdf.format(beginTime1);

				// 计算结束时间 ；然后 给工序赋值计划开始时间、计划结束时间、时长、时长单位； 更新数据
				if ("秒".equals(StandardCapacityUnitSuffixStr)) {
					// restTime 往后推 这些秒 算结束时间
					updateProcessWorkOrderExampleEndTime = sdf
							.format(DateUtils.addSeconds(beginTime1, useTime.intValue()));
				}
				if ("分钟".equals(StandardCapacityUnitSuffixStr)) {
					// restTime 往后推 这些秒 算结束时间
					updateProcessWorkOrderExampleEndTime = sdf
							.format(DateUtils.addSeconds(beginTime1, useTime.intValue() * 60));
				}
				if ("小时".equals(StandardCapacityUnitSuffixStr)) {
					// restTime 往后推 这些秒 算结束时间
					updateProcessWorkOrderExampleEndTime = sdf
							.format(DateUtils.addSeconds(beginTime1, useTime.intValue() * 60 * 60));
				}
				if ("天".equals(StandardCapacityUnitSuffixStr)) {
					// restTime 往后推 这些秒 算结束时间
					updateProcessWorkOrderExampleEndTime = sdf
							.format(DateUtils.addSeconds(beginTime1, useTime.intValue() * 60 * 60 * 60));
				}
				updateProcessWorkOrderExampleWorkHoursUnit = "秒";
				updateProcessWorkOrderExampleWorkHours = String
						.valueOf((sdf.parse(updateProcessWorkOrderExampleEndTime).getTime()
								- sdf.parse(updateProcessWorkOrderExampleBeginTime).getTime()) / 1000);
			}

			map2.put("PlannedBeginTime", updateProcessWorkOrderExampleBeginTime);
			map2.put("PlannedEndTime", updateProcessWorkOrderExampleEndTime);
			map2.put("WorkHours", updateProcessWorkOrderExampleWorkHours);
			map2.put("WorkHoursUnit", updateProcessWorkOrderExampleWorkHoursUnit);
			// 更新工艺工单工序实例
			ProcessWorkOrderExampleService.updateByConditionMap(map2);
			// 给 timeStr 赋值当前计算好的时间 作为下一个工序的开始
			timeStr = updateProcessWorkOrderExampleEndTime;
		}

		// 是否下发
		String ifIssue = pd.getString("ifIssue");
		PageData pdData = new PageData();
		pdData.put("PlanningWorkOrder_ID", PlanningWorkOrder_ID);
		pdData.put("ScheduleSchedule", "已排程");
		if ("Y".equals(ifIssue)) {
			pdData.put("DistributionProgress", "已下发");
		} else {
			pdData.put("DistributionProgress", "待下发");
		}
		// 更新计划工单 排程和下发状态 执行更新操作返回成功 否则抛异常 排程失败
		PlanningWorkOrderService.updateWorkOrderDistributionProgress(pdData);
		map.put("result", errInfo);
		return map;
	}

	// 根据工位计算工序的开始时间
	private String calcWPBeginTimeByStation(String TimeStr, String FirstWPStations) throws ParseException {
		String[] FirstStationIDs = FirstWPStations.split(",yl,");
		for (String StationID : FirstStationIDs) {
			// ( 从工艺工单工序实例表中 查找 结束时间大于 参数 计划开始时间的。
			// 并且 该实例中工位 ,yl,拼接字符， 包含当前第一个工位的 )
			// 结束时间倒序排列
			List<PageData> listByTimeStrAndStationID = PlanningWorkOrderService.listByTimeStrAndStationID(TimeStr,
					StationID);
			// 如果不存在 则计划的开始时间就是 参数传过来的时间
			if (CollectionUtil.isEmpty(listByTimeStrAndStationID)) {
				continue;
			}
			// 如果存在 则 取结束时间最晚的一条的 结束时间 +1s 作为 此次计划的开始时间
			String preEndTime = listByTimeStrAndStationID.get(0).getString("PlannedEndTime");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parseEndTime = sdf.parse(preEndTime);
			// 结束时间加一秒
			Date preEndTimeAdd1Seconds = DateUtils.addSeconds(parseEndTime, 1);
			// 给计划开始时间参数赋值
			TimeStr = sdf.format(preEndTimeAdd1Seconds);
		}
		return TimeStr;
	}

	// 根据计划工单id获取当前物料是否齐套
	private PageData ifCompleteSetOfMaterialsByPlanningWorkOrderID(String PlanningWorkOrder_ID) throws Exception {
		if (Tools.isEmpty(PlanningWorkOrder_ID)) {
			throw new RuntimeException("PlanningWorkOrder_ID参数不存在");
		}
		PageData pgParam = new PageData();
		pgParam.put("PlanningWorkOrderID", PlanningWorkOrder_ID);
		List<PageData> materialRequirementList = MaterialRequirementService.getMaterialRequirementList(pgParam);
		if (CollectionUtil.isEmpty(materialRequirementList)) {
			throw new RuntimeException("该计划工单没有配置物料需求单");
		}

		/**
		 * 查看物料需求单中物料需求数量 和 当前车间库中存在的物料数量进行对比 如果不够，则提示不够，并且提示什么物料 需要采购多少个
		 * 
		 * 返回类型 ： ifAllComplete 整个计划工单是否都齐套了 PurchaseList 需要采购的对象 {
		 * "ifAllComplete":"Y"/"N",
		 * "PurchaseList":[{"MaterialID":"xxxxxx","Purchase":"5"},
		 * {"MaterialID":"xxxxxx","Purchase":"8"}] }
		 * 
		 */
		String ifAllComplete = "Y";
		List<Map<String, Object>> resultMapList = Lists.newArrayList();
		for (int i = 0; i < materialRequirementList.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			PageData mrData = materialRequirementList.get(i);
			String MaterialID = mrData.getString("MaterialID");
			String DemandCountStr = mrData.getString("DemandCount");
			Double DemandCount = Double.valueOf(DemandCountStr);
			String wareHouseType = "工厂库";
			PageData pg = new PageData();
			pg.put("ItemId", MaterialID);
			pg.put("FType", wareHouseType);
			List<PageData> actualCountByFTYPEAndItemID = StockService.getActualCountByFTYPEAndItemID(pg);
			// 如果该物料在工厂库没有 ，则下推采购单
			if (CollectionUtil.isEmpty(actualCountByFTYPEAndItemID)) {
				map.put("MaterialID", MaterialID);
				map.put("Purchase", DemandCount);
				resultMapList.add(map);
			}
			// 如果该物料在工厂库有 ，但是小于需求数量，则下推采购单。否则返回齐套
			if (CollectionUtil.isNotEmpty(actualCountByFTYPEAndItemID)) {
				PageData actualCountByFTYPEAndItemIDData = actualCountByFTYPEAndItemID.get(0);
				String ActualCountStr = actualCountByFTYPEAndItemIDData.getString("ActualCount");
				Double ActualCount = Double.valueOf(ActualCountStr);
				if (ActualCount < DemandCount) {
					map.put("MaterialID", MaterialID);
					map.put("Purchase", DemandCount - ActualCount);
					resultMapList.add(map);
				}
			}
		}
		// 如果有需要采购的物料的 物料齐套标志为 N
		if (CollectionUtil.isNotEmpty(resultMapList)) {
			ifAllComplete = "N";
		}
		PageData pageData = new PageData();
		pageData.put("ifAllComplete", ifAllComplete);
		pageData.put("PurchaseList", resultMapList);
		return pageData;
	}

	/**
	 * 下发
	 * 
	 * @param PlanningWorkOrderID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/issue")
	public Object issue(@RequestParam String PlanningWorkOrderID) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
		PageData findById = PlanningWorkOrderService.findById(pd);
		if (null != findById) {
			findById.put("ScheduleSchedule", "已排程");
			findById.put("DistributionProgress", "已下发");
			PlanningWorkOrderService.edit(findById);
		}
		// 先排程再下发然后执行
		// List<PageData> listProcessWorkOrderExampleByPlanningWorkOrderID =
		// PlanningWorkOrderService
		// .getListProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);
		// if
		// (CollectionUtil.isNotEmpty(listProcessWorkOrderExampleByPlanningWorkOrderID))
		// {
		// for (PageData pageData :
		// listProcessWorkOrderExampleByPlanningWorkOrderID) {
		// pageData.put("FStatus", "未执行");
		// ProcessWorkOrderExampleService.edit(pageData);
		//
		// }
		// }
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获取每个状态的排程任务数量
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getAPSCount")
	public Object getAPSCount() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd1 = new PageData();
		pd1.put("ScheduleSchedule", "未排程");
		pd1.put("DistributionProgress", "未下发");
		PageData count1 = PlanningWorkOrderService.getCountByScheduleAndDistributionProgress(pd1);
		PageData pd2 = new PageData();
		pd2.put("ScheduleSchedule", "已排程");
		pd2.put("DistributionProgress", "待下发");
		PageData count2 = PlanningWorkOrderService.getCountByScheduleAndDistributionProgress(pd2);
		PageData pd3 = new PageData();
		pd3.put("ScheduleSchedule", "已排程");
		pd3.put("DistributionProgress", "已下发");
		PageData count3 = PlanningWorkOrderService.getCountByScheduleAndDistributionProgress(pd3);
		map.put("count1", count1);
		map.put("count2", count2);
		map.put("count3", count3);
		map.put("result", errInfo);
		return map;

	}

	/**
	 * 自动排程前更新计划时间
	 * 
	 * @param pd （SUBPLAN_ID，MASTERPLAN_ID，BEGIN_NODE）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/updateTaskPlanningTime")
	public Object updateTaskPlanningTime() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		String[] split = DATA_IDS.split(",yl,");
		if (split.length > 0) {
			for (String planningWorkOrderId : split) {
				PageData pData = new PageData();
				pData.put("PlanningWorkOrder_ID", planningWorkOrderId);
				PageData subPlan = PlanningWorkOrderService.findById(pData);
				String MasterWorkOrder_ID = subPlan.getString("MasterWorkOrder_ID");
				pd.put("MASTERPLAN_ID", MasterWorkOrder_ID);
				pd.put("SUBPLAN_ID", planningWorkOrderId);
				// 默认开始节点
				pd.put("BEGIN_NODE", "1607049105657");
				getNodeA(pd);
			}
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 获取每一个子计划的一级节点
	 * 
	 * @param pd （SUBPLAN_ID，MASTERPLAN_ID，BEGIN_NODE）
	 * @return
	 * @throws Exception
	 */
	public Object getNodeA(PageData pd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		List<PageData> nextNodeList = NEWPLAN_BOMService.nextNodeList(pd);
		// 通过传来的上一节点node找下一节点nextNodeList 这里还需要完善left join 任务表
		for (int i = 0; i < nextNodeList.size(); i++) {
			PageData pd1 = nextNodeList.get(i);
			if (Tools.isEmpty(pd1.getString("NODE_ID"))) {
				continue;
			}
			String NODE_TYPE = pd1.getString("NODE_TYPE");
			if (!NODE_TYPE.equals("end round")) {
				pd.put("END_NODE", pd1.getString("NODE_ID"));
				pd.put("NODE_TYPE", pd1.getString("NODE_TYPE"));
				String beginTime = getNodeT(pd);// 开始日期
				// 修改节点对应任务的
				// update 任务表 set 开始时间=beginTime,结束时间=开始时间+工时 where 自己写
				PageData pData = new PageData();
				pData.put("NODE_ID", pd1.getString("NODE_ID"));
				String SUBPLAN_ID = pd.getString("SUBPLAN_ID");
				pData.put("PlanningWorkOrderID", SUBPLAN_ID);
				List<PageData> listAll = ProcessWorkOrderExampleService.listAll(pData);
				if (CollectionUtil.isNotEmpty(listAll)) {
					for (PageData pageData : listAll) {
						if (null == pageData.get("WorkHours")) {
							continue;
						}
						pageData.put("PlannedBeginTime", beginTime);
						String WorkHoursUnit = pageData.getString("WorkHoursUnit");
						String afterDayDate = DateUtil.getAfterDayDate(beginTime,
								String.valueOf(pageData.get("WorkHours")), WorkHoursUnit);
						pageData.put("PlannedEndTime", afterDayDate);
						ProcessWorkOrderExampleService.edit(pageData);
					}
				}
				pd.put("BEGIN_NODE", pd1.getString("NODE_ID"));
				getNodeA(pd);
			}
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 通过当前节点获取上级节点如果上级节点数量大于1则取节点对应任务工时最大的节点
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String getNodeT(PageData pd) throws Exception {
		String endTime = "";
		List<PageData> pds = NEWPLAN_BOMService.lastNodeList(pd);
		List<String> endTimeList = Lists.newArrayList();
		String SUBPLAN_ID = pd.getString("SUBPLAN_ID");
		PageData pData = new PageData();
		pData.put("PlanningWorkOrder_ID", SUBPLAN_ID);
		PageData subPlan = PlanningWorkOrderService.findById(pData);
		// 根据子计划工单的id 获取子计划工单的开始时间 赋值 给当前节点对应的 任务
		endTime = subPlan.getString("PlannedBeginTime");
		if (CollectionUtil.isNotEmpty(pds)) {
			for (PageData pd1 : pds) {
				if (Tools.isEmpty(pd1.getString("NODE_ID"))) {
					return endTime;
				}
				if (!pd1.getString("NODE_TYPE").equals("start round")) {
					pData.put("PlanningWorkOrderID", SUBPLAN_ID);
					String NODE_ID = pd1.getString("NODE_ID");
					pData.put("NODE_ID", NODE_ID);
					List<PageData> listAll = ProcessWorkOrderExampleService.listAll(pData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						if (null != listAll.get(0)) {
							endTime = listAll.get(0).getString("PlannedEndTime");
						}
					}
				}
				if (Tools.isEmpty(endTime)) {
					continue;
				}
				endTimeList.add(endTime);
			}
			if (CollectionUtil.isEmpty(endTimeList)) {
				return endTime;
			}
			endTimeList.sort((e1, e2) -> {
				if (DateUtil.str2Date(e1).getTime() > DateUtil.str2Date(e2).getTime()) {
					return 1;
				}
				return 0;
			});
			endTime = endTimeList.get(0);
		}
		return endTime;
	}

	/**
	 * 执行手动排程动作 v1 管悦 20210804 保存并下发排程 v2 管悦 20210820 给库房发备货通知，创建叫料申请并下发库房 v3 管悦
	 * 20210823 创建库房叫料申请单和分料组叫料任务
	 * 
	 * @param pageData
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/push")
	public Object push() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		String PlanningWorkOrderNumStrs = "";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FFTYPE", "生产任务");
		String paramList = pd.getString("paramList");
		if (Tools.isEmpty(paramList)) {
			throw new RuntimeException("参数没传");
		}
		List<PageData> paramListArray = JSON.parseArray(paramList, PageData.class);
		for (PageData param : paramListArray) {
			/*
			 * if(null ==param.getString("FPlannerx")||
			 * "".equals(param.getString("FPlannerx"))) { errInfo = "fail1"; break; }
			 * if(null ==param.getString("FPlannerIDx")||
			 * "".equals(param.getString("FPlannerIDx"))) { errInfo = "fail1"; break; }
			 */
			if (null == param.getString("PlannedBeginTime") || "".equals(param.getString("PlannedBeginTime"))) {
				errInfo = "fail1";
				break;
			}
			if (null == param.getString("PlannedEndTime") || "".equals(param.getString("PlannedEndTime"))) {
				errInfo = "fail1";
				break;
			}
		}
		if (errInfo.equals("success")) {
			// 创建库房叫料单
			for (PageData param : paramListArray) {
				PageData pdOrx = new PageData();
				String PlanningWorkOrderID = param.getString("PlanningWorkOrder_ID");
				pdOrx.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
				PageData subPlan = PlanningWorkOrderService.findById(pdOrx);
				PlanningWorkOrderNumStrs += "," + subPlan.getString("WorkOrderNum");
			}
			// 创建库房叫料单
			PlanningWorkOrderNumStrs = PlanningWorkOrderNumStrs.substring(1);
			// 创建叫料申请
			PageData pdCX = new PageData();
			pdCX.put("CallMaterial_ID", this.get32UUID()); // 主键
			pdCX.put("MakePersonID", Jurisdiction.getName());
			pdCX.put("MakeTime", Tools.date2Str(new Date()));
			pdCX.put("DocumentNum", (String) CodingRulesService.getRuleNumByRuleType("JLSQ"));
			pdCX.put("FState", "叫料");
			pdCX.put("FCallTime", Tools.date2Str(new Date()));
			pdCX.put("FCallMan", Jurisdiction.getName());
			pdCX.put("MakeTime", Tools.date2Str(new Date()));
			pdCX.put("PlanningWorkOrderNum", PlanningWorkOrderNumStrs);
			pdCX.put("NeedUseTime", Tools.date2Str(new Date()));
			CallMaterialService.save(pdCX);

			// 加入分料单明细
			PageData operateX = new PageData();
			operateX.put("CallMaterialOperate_ID", this.get32UUID());
			operateX.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
			operateX.put("OperatePerson", Jurisdiction.getName());
			operateX.put("OperateTime", Tools.date2Str(new Date()));
			operateX.put("FState", "叫料");
			CallMaterialOperateService.save(operateX);

			if (StringUtil.isNotEmpty(PlanningWorkOrderNumStrs)) {
				List<String> PlanningWorkOrderNumList = CollectionUtils.asList(PlanningWorkOrderNumStrs.split(","));
				List<PageData> allDetailList = Lists.newArrayList();
				for (String num : PlanningWorkOrderNumList) {
					String mat_id = "";
					PageData pdData = new PageData();
					pdData.put("WorkOrderNum", num);
					List<PageData> listAll = PlanningWorkOrderService.listAll(pdData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						mat_id = listAll.get(0).getString("OutputMaterialID");
						PageData matParam = new PageData();
						matParam.put("MAT_BASIC_ID", mat_id);
						PageData MAT_INFO = mat_basicService.findById(matParam);
						if (null != MAT_INFO) {
							PageData cadparam = new PageData();
							cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
							List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
							if (CollectionUtil.isNotEmpty(cadList)) {
								PageData bomParam = new PageData();
								bomParam.put("FFTYPE", "BOM");

								bomParam.put("Cabinet_Assembly_Detail_ID",
										cadList.get(0).getString("Cabinet_Assembly_Detail_ID"));
								List<PageData> cadBomList = Cabinet_BOMService.listAll(bomParam);

								String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
								String MAT_AUXILIARYMX_ID = "";
								PageData pg1 = new PageData();
								pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
								pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
								List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
										.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
								if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
									MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
											.getString("MAT_AUXILIARYMX_ID");
								}

								for (PageData bom : cadBomList) {
									PageData cmData = new PageData();
									cmData.put("CallMaterialDetails_ID", this.get32UUID()); // 主键
									cmData.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
									cmData.put("TType", "计划生成");
									cmData.put("OperatePersion", Jurisdiction.getName());
									cmData.put("OperateTime", Tools.date2Str(new Date()));

									cmData.put("Material_ID", bom.getString("MAT_BASIC_ID"));
									cmData.put("MaterialName", bom.getString("MAT_NAME"));

									cmData.put("Specification", bom.getString("MAT_SPECS"));
									cmData.put("DemandNum", bom.get("BOM_COUNT").toString());
									cmData.put("QuantityCount", bom.get("BOM_COUNT").toString());

									cmData.put("MaterialCode", bom.getString("MAT_CODE"));
									cmData.put("FUnitName", bom.getString("MAT_MAIN_UNIT"));

									cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
									cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

									allDetailList.add(cmData);

								}
							}
						}
					}
				}

				Map<String, PageData> result2 = new HashMap<String, PageData>();
				for (PageData cmData : allDetailList) {
					String Material_ID = cmData.get("Material_ID").toString();
					BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
					if (result2.containsKey(Material_ID)) {
						BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
						value = value.add(temp);
						result2.get(Material_ID).put("DemandNum", value);
						result2.get(Material_ID).put("QuantityCount", value);
						continue;
					}
					result2.put(Material_ID, cmData);
				}

				for (PageData cmDetailData : result2.values()) {
					CallMaterialDetailsService.save(cmDetailData);
				}
				PageData pdNotice1 = new PageData();

				// 跳转页面
				pdNotice1.put("AccessURL", "../../../views/mm/CallMaterial/cangku/CallMaterial_list.html");//
				pdNotice1.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice1.put("ReadPeople", ",");// 已读人默认空
				pdNotice1.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice1.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice1.put("TType", "消息推送");// 消息类型
				pdNotice1.put("FContent", "工单编号为" + PlanningWorkOrderNumStrs + "的计划工单即将用料，请关注（查看备货任务）");// 消息正文
				pdNotice1.put("FTitle", "备货通知");// 消息标题
				pdNotice1.put("LinkIf", "no");// 是否跳转页面
				pdNotice1.put("DataSources", "备货通知");// 数据来源
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
				String WXNR = "【备货通知】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice1.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
			}
			for (PageData param : paramListArray) {

				String PlanningWorkOrderID = param.getString("PlanningWorkOrder_ID");

				// 是否下发
				String ifIssue = param.getString("ifIssue");
				// 更改下发状态
				param.put("ScheduleSchedule", "已排程");
				param.put("DistributionProgress", "已下发");
				/*
				 * if ("Y".equals(ifIssue)) { param.put("DistributionProgress", "已下发"); } else {
				 * param.put("DistributionProgress", "待下发"); }
				 */
				// 更新计划工单 排程和下发状态 执行更新操作返回成功 否则抛异常 排程失败
				// PlanningWorkOrderService.edit(param);
				PageData pdOr = new PageData();
				pdOr.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
				pdOr = PlanningWorkOrderService.findById(pdOr);
				// pdOr.put("FPlanner", param.getString("FPlannerx"));
				// pdOr.put("FPlannerID", param.getString("FPlannerIDx"));
				pdOr.put("ScheduleSchedule", "已排程");
				pdOr.put("DistributionProgress", "已下发");
				pdOr.put("PlannedBeginTime", param.getString("PlannedBeginTime"));
				pdOr.put("PlannedEndTime", param.getString("PlannedEndTime"));
				pdOr.put("FPlanner", Jurisdiction.getName());
				pdOr.put("FPlannerID", Jurisdiction.getName());
				PlanningWorkOrderService.edit(pdOr);
				List<PageData> listProcessWorkOrderExampleByPlanningWorkOrderID = PlanningWorkOrderService
						.getListProcessWorkOrderExampleByPlanningWorkOrderID(PlanningWorkOrderID);
				for (PageData pageData : listProcessWorkOrderExampleByPlanningWorkOrderID) {
					// （从工单获取） 计划开始时间
					pageData.put("PlannedBeginTime", param.getString("PlannedBeginTime"));
					// （从工单获取） 计划结束时间
					pageData.put("PlannedEndTime", param.getString("PlannedEndTime"));

					ProcessWorkOrderExampleService.edit(pageData);
				}

				pd.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
				PageData subPlan = PlanningWorkOrderService.findById(pd);
				// PlanningWorkOrderNumStrs+=","+subPlan.getString("WorkOrderNum");
				// 创建叫料申请
				PageData pdC = new PageData();
				pdC.put("CallMaterialFL_ID", this.get32UUID()); // 主键
				pdC.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
				pdC.put("MakePersonID", Jurisdiction.getName());
				pdC.put("MakeTime", Tools.date2Str(new Date()));
				pdC.put("DocumentNum", (String) CodingRulesService.getRuleNumByRuleType("JLSQ"));
				pdC.put("FState", "待分料");
				pdC.put("FCallTime", Tools.date2Str(new Date()));
				pdC.put("FCallMan", Jurisdiction.getName());
				pdC.put("MakeTime", Tools.date2Str(new Date()));
				pdC.put("PlanningWorkOrderNum", subPlan.getString("WorkOrderNum"));
				pdC.put("NeedUseTime", Tools.date2Str(new Date()));
				pdC.put("FIsYL", "是");
				callmaterialflService.save(pdC);

				// 加入分料单明细
				PageData operate = new PageData();
				operate.put("CallMaterialOperate_ID", this.get32UUID());
				operate.put("OperatePerson", Jurisdiction.getName());
				operate.put("OperateTime", Tools.date2Str(new Date()));
				operate.put("FState", "分料任务");
				operate.put("CallMaterial_ID", pdC.getString("CallMaterial_ID"));

				CallMaterialOperateService.save(operate);

				String PlanningWorkOrderNumStr = subPlan.getString("WorkOrderNum");
				if (StringUtil.isNotEmpty(PlanningWorkOrderNumStr)) {
					List<String> PlanningWorkOrderNumList = CollectionUtils.asList(PlanningWorkOrderNumStr.split(","));
					List<PageData> allDetailList = Lists.newArrayList();
					for (String num : PlanningWorkOrderNumList) {
						String mat_id = "";
						PageData pdData = new PageData();
						pdData.put("WorkOrderNum", num);
						List<PageData> listAll = PlanningWorkOrderService.listAll(pdData);
						if (CollectionUtil.isNotEmpty(listAll)) {
							mat_id = listAll.get(0).getString("OutputMaterialID");
							PageData matParam = new PageData();
							matParam.put("MAT_BASIC_ID", mat_id);
							PageData MAT_INFO = mat_basicService.findById(matParam);
							if (null != MAT_INFO) {
								PageData cadparam = new PageData();
								cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
								List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
								if (CollectionUtil.isNotEmpty(cadList)) {
									PageData bomParam = new PageData();
									bomParam.put("FFTYPE", "BOM");

									bomParam.put("Cabinet_Assembly_Detail_ID",
											cadList.get(0).getString("Cabinet_Assembly_Detail_ID"));
									List<PageData> cadBomList = Cabinet_BOMService.listAll(bomParam);

									String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
									String MAT_AUXILIARYMX_ID = "";
									PageData pg1 = new PageData();
									pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
									pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
									List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
											.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
									if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
										MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
												.getString("MAT_AUXILIARYMX_ID");
									}

									for (PageData bom : cadBomList) {
										PageData cmData = new PageData();
										cmData.put("CallMaterialDetailsFL_ID", this.get32UUID()); // 主键
										cmData.put("CallMaterial_ID", pdC.getString("CallMaterial_ID"));
										cmData.put("CallMaterialFL_ID", pdC.getString("CallMaterialFL_ID"));
										cmData.put("TType", "计划生成");
										cmData.put("OperatePersion", Jurisdiction.getName());
										cmData.put("OperateTime", Tools.date2Str(new Date()));

										cmData.put("Material_ID", bom.getString("MAT_BASIC_ID"));
										cmData.put("MaterialName", bom.getString("MAT_NAME"));

										cmData.put("Specification", bom.getString("MAT_SPECS"));
										cmData.put("DemandNum", bom.get("BOM_COUNT").toString());
										cmData.put("QuantityCount", bom.get("BOM_COUNT").toString());

										cmData.put("MaterialCode", bom.getString("MAT_CODE"));
										cmData.put("FUnitName", bom.getString("MAT_MAIN_UNIT"));

										cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
										cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

										allDetailList.add(cmData);

									}
								}
							}
						}
					}

					Map<String, PageData> result2 = new HashMap<String, PageData>();
					for (PageData cmData : allDetailList) {
						String Material_ID = cmData.get("Material_ID").toString();
						BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
						if (result2.containsKey(Material_ID)) {
							BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
							value = value.add(temp);
							result2.get(Material_ID).put("DemandNum", value);
							result2.get(Material_ID).put("QuantityCount", value);
							continue;
						}
						result2.put(Material_ID, cmData);
					}

					for (PageData cmDetailData : result2.values()) {
						callmaterialdetailsflService.save(cmDetailData);
					}
				}
				if (null != subPlan) {
					/*
					 * findById.put("ScheduleSchedule", "已排程"); findById.put("DistributionProgress",
					 * "已下发"); PlanningWorkOrderService.edit(findById);
					 */

					subPlan.put("FStatus", "执行中");
					subPlan.put("DistributionProgress", "已下发");
					subPlan.put("ScheduleSchedule", "已排程");
					subPlan.put("ActualBeginTime", Tools.date2Str(new Date()));
					PlanningWorkOrderService.edit(subPlan);
					// PageData pageData2 = new PageData();
					// pageData2.put("MASTERPLAN_ID",
					// subPlan.getString("MasterWorkOrder_ID"));
					// pageData2.put("SUBPLAN_ID",
					// subPlan.getString("PlanningWorkOrder_ID"));
					// List<PageData> findStartTask =
					// SecondStageService.findStartTaskNoStandard(pageData2);
					// for (PageData taskFlow : findStartTask) {
					PageData po = new PageData();
					// po.put("NODE_ID", taskFlow.getString("NODE_ID"));
					po.put("PlanningWorkOrderID", subPlan.getString("PlanningWorkOrder_ID"));
					// 消息推送通知库房，备货通知：哪个订单即将用料，请关注（查看备货任务）
					PageData pdNotice1 = new PageData();

					// 跳转页面
					pdNotice1.put("AccessURL", "../../../views/mm/callmaterialfl/callmaterialfl_editAll.html");//
					pdNotice1.put("NOTICE_ID", this.get32UUID()); // 主键
					pdNotice1.put("ReadPeople", ",");// 已读人默认空
					pdNotice1.put("FIssuedID", Jurisdiction.getName()); // 发布人
					pdNotice1.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
					pdNotice1.put("TType", "消息推送");// 消息类型
					pdNotice1.put("FContent", "工单编号为" + subPlan.getString("WorkOrderNum") + "的计划工单待分料，请关注（查看备货任务）");// 消息正文
					pdNotice1.put("FTitle", "分料任务");// 消息标题
					pdNotice1.put("LinkIf", "no");// 是否跳转页面
					pdNotice1.put("DataSources", "分料任务");// 数据来源
					pdNotice1.put("ReceivingAuthority", ",FLZ,KF,");// 接收人
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
					String WXNR = "【分料任务】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
							+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice1.get("FContent");
					// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
					// "0");
					weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
					List<PageData> pwoeList = ProcessWorkOrderExampleService.listAll(po);
					for (PageData task : pwoeList) {
						task.put("FStatus", "待执行");
						ProcessWorkOrderExampleService.edit(task);

						// 生产工单下发-消息推送工位人员
						PageData findById = processWorkOrderExampleService.findById(task);
						String FFTYPE = pd.getString("FFTYPE");
						if (null != FFTYPE && FFTYPE.equals("生产任务")) {
							List<PageData> staffList = WorkStationPersonRelService.listAllByName(findById);
							String ReceivingAuthority = ",";
							for (PageData pdStaffx : staffList) {
								ReceivingAuthority += pdStaffx.getString("staffno");
								ReceivingAuthority = ReceivingAuthority + ",";
								// 手机app-极光推送
								String UserName = pdStaffx.getString("staffno");
								pd.put("UserName", UserName);
								PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
								if (pdf != null) {
									String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
									String notification_title = "工作通知";
									String msg_title = "生产任务下发";
									String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
											+ findById.getString("WPName") + "，产出物料为"
											+ findById.getString("ProcessIMaterielCode") + "的生产任务已下发";
									if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title,
											msg_title, msg_content, "") == 1) {
										// errInfo="success";
									} else {
										// errInfo="error";
									}
								}

							}
							// 消息推送
							PageData pdNotice = new PageData();

							// 跳转页面
							pdNotice.put("AccessURL", "../../../views/pp/taskRun/run_task_listAll.html");//
							pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
							pdNotice.put("ReadPeople", ",");// 已读人默认空
							pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
							pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
							pdNotice.put("TType", "消息推送");// 消息类型
							pdNotice.put("FContent",
									"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("WPName")
											+ "，产出物料为" + findById.getString("ProcessIMaterielCode") + "的生产任务已下发");// 消息正文
							pdNotice.put("FTitle", "生产任务下发");// 消息标题
							pdNotice.put("LinkIf", "no");// 是否跳转页面
							pdNotice.put("DataSources", "生产任务下发");// 数据来源
							pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
							pdNotice.put("Report_Key", "task");// 手机app
							pdNotice.put("Report_Value", "");// 手机app
							noticeService.save(pdNotice);
							SendWeChatMessageMes weChat1 = new SendWeChatMessageMes();
							PageData pd31 = new PageData();
							// String Name = Jurisdiction.getName();
							String Name1 = "管悦";
							pd3.put("NAME", Name);
							// PageData pd2 = usersService.getPhone(pd3);
							String phone1 = "";
							/*
							 * if(null!=pd2){ phone = pd2.getString("phone"); }else{ phone=""; }
							 */
							String WXNR1 = "【生产任务下发】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
									+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
							// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
							// "0");
							weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR1, "0");
						}

						// }
						// taskFlow.put("EXECUTE_STATE", "执行中");
						// NEWPLAN_BOMService.edit(taskFlow);

					}

				}

			}

		}

		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210804 下发 v2 管悦 20210820 给库房发备货通知，创建叫料申请并下发库房
	 * 
	 * @param PlanningWorkOrderID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/xiafa")
	public Object xiafa() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = this.getPageData();
		pd.put("FFTYPE", "生产任务");
		String IDS = pd.getString("IDS");
		String PlanningWorkOrderNumStrs = "";
		String[] strs = IDS.split(",yl,");
		for (String param : strs) {
			pd.put("PlanningWorkOrder_ID", param);
			PageData findById = PlanningWorkOrderService.findById(pd);
			if (null != findById) {
				if (null == findById.getString("FPlanner") || "".equals(findById.getString("FPlanner"))) {
					errInfo = "fail1";
					break;
				}
				if (null == findById.getString("FPlannerID") || "".equals(findById.getString("FPlannerID"))) {
					errInfo = "fail1";
					break;
				}
				if (null == findById.getString("PlannedBeginTime")
						|| "".equals(findById.getString("PlannedBeginTime"))) {
					errInfo = "fail1";
					break;
				}
				if (null == findById.getString("PlannedEndTime") || "".equals(findById.getString("PlannedEndTime"))) {
					errInfo = "fail1";
					break;
				}
			}
		}
		PageData pdCX = new PageData();
		if (errInfo.equals("success")) {
			// 创建库房叫料单
			for (String param : strs) {
				PageData pdOrx = new PageData();
				String PlanningWorkOrderID = param;
				pdOrx.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
				PageData subPlan = PlanningWorkOrderService.findById(pdOrx);
				PlanningWorkOrderNumStrs += "," + subPlan.getString("WorkOrderNum");
			}
			// 创建库房叫料单
			PlanningWorkOrderNumStrs = PlanningWorkOrderNumStrs.substring(1);
			// 创建叫料申请

			pdCX.put("CallMaterial_ID", this.get32UUID()); // 主键
			pdCX.put("MakePersonID", Jurisdiction.getName());
			pdCX.put("MakeTime", Tools.date2Str(new Date()));
			pdCX.put("DocumentNum", (String) CodingRulesService.getRuleNumByRuleType("JLSQ"));
			pdCX.put("FState", "叫料");
			pdCX.put("FCallTime", Tools.date2Str(new Date()));
			pdCX.put("FCallMan", Jurisdiction.getName());
			pdCX.put("MakeTime", Tools.date2Str(new Date()));
			pdCX.put("PlanningWorkOrderNum", PlanningWorkOrderNumStrs);
			pdCX.put("NeedUseTime", Tools.date2Str(new Date()));
			CallMaterialService.save(pdCX);

			// 加入分料单明细
			PageData operateX = new PageData();
			operateX.put("CallMaterialOperate_ID", this.get32UUID());
			operateX.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
			operateX.put("OperatePerson", Jurisdiction.getName());
			operateX.put("OperateTime", Tools.date2Str(new Date()));
			operateX.put("FState", "叫料");
			CallMaterialOperateService.save(operateX);

			if (StringUtil.isNotEmpty(PlanningWorkOrderNumStrs)) {
				List<String> PlanningWorkOrderNumList = CollectionUtils.asList(PlanningWorkOrderNumStrs.split(","));
				List<PageData> allDetailList = Lists.newArrayList();
				for (String num : PlanningWorkOrderNumList) {
					String mat_id = "";
					PageData pdData = new PageData();
					pdData.put("WorkOrderNum", num);
					List<PageData> listAll = PlanningWorkOrderService.listAll(pdData);
					if (CollectionUtil.isNotEmpty(listAll)) {
						mat_id = listAll.get(0).getString("OutputMaterialID");
						PageData matParam = new PageData();
						matParam.put("MAT_BASIC_ID", mat_id);
						PageData MAT_INFO = mat_basicService.findById(matParam);
						if (null != MAT_INFO) {
							PageData cadparam = new PageData();
							cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
							List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
							if (CollectionUtil.isNotEmpty(cadList)) {
								PageData bomParam = new PageData();
								bomParam.put("FFTYPE", "BOM");

								bomParam.put("Cabinet_Assembly_Detail_ID",
										cadList.get(0).getString("Cabinet_Assembly_Detail_ID"));
								List<PageData> cadBomList = Cabinet_BOMService.listAll(bomParam);

								String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
								String MAT_AUXILIARYMX_ID = "";
								PageData pg1 = new PageData();
								pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
								pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
								List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
										.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
								if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
									MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
											.getString("MAT_AUXILIARYMX_ID");
								}

								for (PageData bom : cadBomList) {
									PageData cmData = new PageData();
									cmData.put("CallMaterialDetails_ID", this.get32UUID()); // 主键
									cmData.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
									cmData.put("TType", "计划生成");
									cmData.put("OperatePersion", Jurisdiction.getName());
									cmData.put("OperateTime", Tools.date2Str(new Date()));

									cmData.put("Material_ID", bom.getString("MAT_BASIC_ID"));
									cmData.put("MaterialName", bom.getString("MAT_NAME"));

									cmData.put("Specification", bom.getString("MAT_SPECS"));
									cmData.put("DemandNum", bom.get("BOM_COUNT").toString());
									cmData.put("QuantityCount", bom.get("BOM_COUNT").toString());

									cmData.put("MaterialCode", bom.getString("MAT_CODE"));
									cmData.put("FUnitName", bom.getString("MAT_MAIN_UNIT"));

									cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
									cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

									allDetailList.add(cmData);

								}
							}
						}
					}
				}

				Map<String, PageData> result2 = new HashMap<String, PageData>();
				for (PageData cmData : allDetailList) {
					String Material_ID = cmData.get("Material_ID").toString();
					BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
					if (result2.containsKey(Material_ID)) {
						BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
						value = value.add(temp);
						result2.get(Material_ID).put("DemandNum", value);
						result2.get(Material_ID).put("QuantityCount", value);
						continue;
					}
					result2.put(Material_ID, cmData);
				}

				for (PageData cmDetailData : result2.values()) {
					CallMaterialDetailsService.save(cmDetailData);
				}
				PageData pdNotice1 = new PageData();

				// 跳转页面
				pdNotice1.put("AccessURL", "../../../views/mm/callmaterialfl/callmaterialfl_editAll.html");//
				pdNotice1.put("NOTICE_ID", this.get32UUID()); // 主键
				pdNotice1.put("ReadPeople", ",");// 已读人默认空
				pdNotice1.put("FIssuedID", Jurisdiction.getName()); // 发布人
				pdNotice1.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
				pdNotice1.put("TType", "消息推送");// 消息类型
				pdNotice1.put("FContent", "工单编号为" + PlanningWorkOrderNumStrs + "的计划工单即将用料，请关注（查看分料任务）");// 消息正文
				pdNotice1.put("FTitle", "分料任务");// 消息标题
				pdNotice1.put("LinkIf", "no");// 是否跳转页面
				pdNotice1.put("DataSources", "分料任务");// 数据来源
				pdNotice1.put("ReceivingAuthority", ",FLZ,KF,");// 接收人
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
				String WXNR = "【分料任务】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
						+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice1.get("FContent");
				// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
				// "0");
				weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
			}

		}

		if (errInfo.equals("success")) {
			for (String PlanningWorkOrderID : strs) {
				pd.put("PlanningWorkOrder_ID", PlanningWorkOrderID);
				PageData subPlan = PlanningWorkOrderService.findById(pd);
				if (null != subPlan) {
					/*
					 * findById.put("ScheduleSchedule", "已排程"); findById.put("DistributionProgress",
					 * "已下发"); PlanningWorkOrderService.edit(findById);
					 */

					subPlan.put("FStatus", "执行中");
					subPlan.put("DistributionProgress", "已下发");
					subPlan.put("ScheduleSchedule", "已排程");
					subPlan.put("ActualBeginTime", Tools.date2Str(new Date()));
					PlanningWorkOrderService.edit(subPlan);
					// PageData pageData2 = new PageData();
					// pageData2.put("MASTERPLAN_ID",
					// subPlan.getString("MasterWorkOrder_ID"));
					// pageData2.put("SUBPLAN_ID",
					// subPlan.getString("PlanningWorkOrder_ID"));
					// List<PageData> findStartTask =
					// SecondStageService.findStartTaskNoStandard(pageData2);
					// for (PageData taskFlow : findStartTask) {
					PageData po = new PageData();
					// po.put("NODE_ID", taskFlow.getString("NODE_ID"));
					// 消息推送通知库房，备货通知：哪个订单即将用料，请关注（查看备货任务）
					PageData pdNotice1 = new PageData();

					// 跳转页面
					pdNotice1.put("AccessURL", "../../../views/mm/CallMaterial/cangku/CallMaterial_list.html");//
					pdNotice1.put("NOTICE_ID", this.get32UUID()); // 主键
					pdNotice1.put("ReadPeople", ",");// 已读人默认空
					pdNotice1.put("FIssuedID", Jurisdiction.getName()); // 发布人
					pdNotice1.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
					pdNotice1.put("TType", "消息推送");// 消息类型
					pdNotice1.put("FContent", "工单编号为" + subPlan.getString("WorkOrderNum") + "的计划工单待分料，请关注（查看备货任务）");// 消息正文
					pdNotice1.put("FTitle", "备货通知");// 消息标题
					pdNotice1.put("LinkIf", "no");// 是否跳转页面
					pdNotice1.put("DataSources", "备货通知");// 数据来源
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
					String WXNR = "【备货通知】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
							+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice1.get("FContent");
					// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
					// "0");
					weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR, "0");
					po.put("PlanningWorkOrderID", subPlan.getString("PlanningWorkOrder_ID"));

					// 创建叫料申请
					PageData pdC = new PageData();
					pdC.put("CallMaterialFL_ID", this.get32UUID()); // 主键
					pdC.put("CallMaterial_ID", pdCX.getString("CallMaterial_ID"));
					pdC.put("MakeTime", Tools.date2Str(new Date()));
					pdC.put("DocumentNum", (String) CodingRulesService.getRuleNumByRuleType("JLSQ"));
					pdC.put("FState", "待分料");
					pdC.put("FCallTime", Tools.date2Str(new Date()));
					pdC.put("FCallMan", Jurisdiction.getName());
					pdC.put("MakeTime", Tools.date2Str(new Date()));
					pdC.put("PlanningWorkOrderNum", subPlan.getString("WorkOrderNum"));
					pdC.put("NeedUseTime", Tools.date2Str(new Date()));
					pdC.put("FIsYL", "是");
					callmaterialflService.save(pdC);

					// 加入操作记录
					PageData operate = new PageData();
					operate.put("CallMaterialOperate_ID", this.get32UUID());
					operate.put("CallMaterial_ID", pdC.getString("CallMaterial_ID"));
					operate.put("OperatePerson", Jurisdiction.getName());
					operate.put("OperateTime", Tools.date2Str(new Date()));
					operate.put("FState", "叫料任务");
					CallMaterialOperateService.save(operate);

					String PlanningWorkOrderNumStr = subPlan.getString("WorkOrderNum");
					if (StringUtil.isNotEmpty(PlanningWorkOrderNumStr)) {
						List<String> PlanningWorkOrderNumList = CollectionUtils
								.asList(PlanningWorkOrderNumStr.split(","));
						List<PageData> allDetailList = Lists.newArrayList();
						for (String num : PlanningWorkOrderNumList) {
							String mat_id = "";
							PageData pdData = new PageData();
							pdData.put("WorkOrderNum", num);
							List<PageData> listAll = PlanningWorkOrderService.listAll(pdData);
							if (CollectionUtil.isNotEmpty(listAll)) {
								mat_id = listAll.get(0).getString("OutputMaterialID");
								PageData matParam = new PageData();
								matParam.put("MAT_BASIC_ID", mat_id);
								PageData MAT_INFO = mat_basicService.findById(matParam);
								if (null != MAT_INFO) {
									PageData cadparam = new PageData();
									cadparam.put("Cabinet_No", MAT_INFO.getString("MAT_CODE"));
									List<PageData> cadList = Cabinet_Assembly_DetailService.listAll(cadparam);
									if (CollectionUtil.isNotEmpty(cadList)) {
										PageData bomParam = new PageData();
										bomParam.put("FFTYPE", "BOM");

										bomParam.put("Cabinet_Assembly_Detail_ID",
												cadList.get(0).getString("Cabinet_Assembly_Detail_ID"));
										List<PageData> cadBomList = Cabinet_BOMService.listAll(bomParam);

										String PPROJECT_CODE = cadList.get(0).getString("PPROJECT_CODE");
										String MAT_AUXILIARYMX_ID = "";
										PageData pg1 = new PageData();
										pg1.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
										pg1.put("MAT_AUXILIARYMX_CODE", PPROJECT_CODE);
										List<PageData> byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE = mat_auxiliarymxService
												.getByMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE(pg1);
										if (CollectionUtil.isNotEmpty(byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE)) {
											MAT_AUXILIARYMX_ID = byMAT_AUXILIARY_IDAndMAT_AUXILIARYMX_CODE.get(0)
													.getString("MAT_AUXILIARYMX_ID");
										}

										for (PageData bom : cadBomList) {
											PageData cmData = new PageData();
											cmData.put("CallMaterialDetailsFL_ID", this.get32UUID()); // 主键
											cmData.put("CallMaterial_ID", pdC.getString("CallMaterial_ID"));
											cmData.put("CallMaterialFL_ID", pdC.getString("CallMaterialFL_ID"));
											cmData.put("TType", "计划生成");
											cmData.put("OperatePersion", Jurisdiction.getName());
											cmData.put("OperateTime", Tools.date2Str(new Date()));

											cmData.put("Material_ID", bom.getString("MAT_BASIC_ID"));
											cmData.put("MaterialName", bom.getString("MAT_NAME"));

											cmData.put("Specification", bom.getString("MAT_SPECS"));
											cmData.put("DemandNum", bom.get("BOM_COUNT").toString());
											cmData.put("QuantityCount", bom.get("BOM_COUNT").toString());

											cmData.put("MaterialCode", bom.getString("MAT_CODE"));
											cmData.put("FUnitName", bom.getString("MAT_MAIN_UNIT"));

											cmData.put("MAT_AUXILIARY_ID", "54e6b1a185764c32bff189907a571a6d");
											cmData.put("MAT_AUXILIARYMX_ID", MAT_AUXILIARYMX_ID);

											allDetailList.add(cmData);

										}
									}
								}
							}
						}

						Map<String, PageData> result2 = new HashMap<String, PageData>();
						for (PageData cmData : allDetailList) {
							String Material_ID = cmData.get("Material_ID").toString();
							BigDecimal value = new BigDecimal(cmData.get("DemandNum").toString());
							if (result2.containsKey(Material_ID)) {
								BigDecimal temp = new BigDecimal(result2.get(Material_ID).get("DemandNum").toString());
								value = value.add(temp);
								result2.get(Material_ID).put("DemandNum", value);
								result2.get(Material_ID).put("QuantityCount", value);
								continue;
							}
							result2.put(Material_ID, cmData);
						}

						for (PageData cmDetailData : result2.values()) {
							callmaterialdetailsflService.save(cmDetailData);
						}
					}

					List<PageData> pwoeList = ProcessWorkOrderExampleService.listAll(po);
					for (PageData task : pwoeList) {
						task.put("FStatus", "待执行");
						ProcessWorkOrderExampleService.edit(task);

						// 生产工单下发-消息推送工位人员
						PageData findById = processWorkOrderExampleService.findById(task);
						String FFTYPE = pd.getString("FFTYPE");
						if (null != FFTYPE && FFTYPE.equals("生产任务")) {
							List<PageData> staffList = WorkStationPersonRelService.listAllByName(findById);
							String ReceivingAuthority = ",";
							for (PageData pdStaffx : staffList) {
								ReceivingAuthority += pdStaffx.getString("staffno");
								ReceivingAuthority = ReceivingAuthority + ",";
								// 手机app-极光推送
								String UserName = pdStaffx.getString("staffno");
								pd.put("UserName", UserName);
								PageData pdf = AppRegistrationService.findById(pd);// 根据用户名查询设备id
								if (pdf != null) {
									String registrationId = pdf.getString("Registration_ID");// 数据库设备ID
									String notification_title = "工作通知";
									String msg_title = "生产任务下发";
									String msg_content = "工单编号为" + findById.getString("WorkOrderNum") + "，工序为"
											+ findById.getString("WPName") + "，产出物料为"
											+ findById.getString("ProcessIMaterielCode") + "的生产任务已下发";
									if (JpushClientUtil.sendToRegistrationId(registrationId, notification_title,
											msg_title, msg_content, "") == 1) {
										// errInfo="success";
									} else {
										// errInfo="error";
									}
								}

							}
							// 消息推送
							PageData pdNotice = new PageData();

							// 跳转页面
							pdNotice.put("AccessURL", "../../../views/pp/taskRun/run_task_listAll.html");//
							pdNotice.put("NOTICE_ID", this.get32UUID()); // 主键
							pdNotice.put("ReadPeople", ",");// 已读人默认空
							pdNotice.put("FIssuedID", Jurisdiction.getName()); // 发布人
							pdNotice.put("ReleaseTime", DateUtil.date2Str(new Date())); // 发布时间
							pdNotice.put("TType", "消息推送");// 消息类型
							pdNotice.put("FContent",
									"工单编号为" + findById.getString("WorkOrderNum") + "，工序为" + findById.getString("WPName")
											+ "，产出物料为" + findById.getString("ProcessIMaterielCode") + "的生产任务已下发");// 消息正文
							pdNotice.put("FTitle", "生产任务下发");// 消息标题
							pdNotice.put("LinkIf", "no");// 是否跳转页面
							pdNotice.put("DataSources", "生产任务下发");// 数据来源
							pdNotice.put("ReceivingAuthority", ReceivingAuthority);// 接收人
							pdNotice.put("Report_Key", "task");// 手机app
							pdNotice.put("Report_Value", "");// 手机app
							noticeService.save(pdNotice);
							SendWeChatMessageMes weChat1 = new SendWeChatMessageMes();
							PageData pd31 = new PageData();
							// String Name = Jurisdiction.getName();
							String Name1 = "管悦";
							pd3.put("NAME", Name);
							// PageData pd2 = usersService.getPhone(pd3);
							String phone1 = "";
							/*
							 * if(null!=pd2){ phone = pd2.getString("phone"); }else{ phone=""; }
							 */
							String WXNR1 = "【生产任务下发】\r\n" + "发布人：" + Jurisdiction.getName() + "\r\n" + "发布时间："
									+ Tools.date2Str(new Date()) + "\r\n" + "消息内容：" + pdNotice.get("FContent");
							// weChat.sendWeChatMsgText(pdPhone.getString("PHONE"), "@all", "1000010", WXNR,
							// "0");
							weChat.sendWeChatMsgText("@all", "@all", "1000014", WXNR1, "0");

						}

						// }
						// taskFlow.put("EXECUTE_STATE", "执行中");
						// NEWPLAN_BOMService.edit(taskFlow);

					}

				}
			}
		}
		map.put("result", errInfo);
		return map;
	}

	/**
	 * v1 管悦 20210908 排程甘特图
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getPCList")
	@ResponseBody
	public Object getPCList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> resultList = Lists.newArrayList();
		resultList = PlanningWorkOrderService.getPCList(pd);
		map.put("varlist", resultList);
		map.put("result", errInfo);
		return map;

	}
}
