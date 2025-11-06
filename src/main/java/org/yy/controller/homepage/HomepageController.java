package org.yy.controller.homepage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.controller.base.BaseController;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.service.fhoa.StaffService;
import org.yy.service.homepage.HomepageService;
import org.yy.service.mom.WC_StationService;
import org.yy.service.project.manager.Cabinet_Assembly_DetailService;
import org.yy.util.Tools;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.util.StringUtil;

@Controller
@RequestMapping("/homepage")
public class HomepageController extends BaseController {
	@Autowired
	private HomepageService homepageService;
	@Autowired
	private StaffService StaffService;
	@Autowired
	private WC_StationService WC_StationService;
	@Autowired
	private Cabinet_Assembly_DetailService Cabinet_Assembly_DetailService;

	/**
	 * 执行完计划
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/finishedPlanlistPage")
	@ResponseBody
	public Object finishedPlanlistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.finishedPlanlistPage(page);
		for (PageData pageData : varList) {
			String yjbs = "正常范围";
			String ProcessIMaterielCode = pageData.getString("OutputMaterial");
			if (StringUtil.isNotEmpty(ProcessIMaterielCode)) {
				String[] split = ProcessIMaterielCode.split("/");
				if (null != split) {
					if (StringUtil.isNotEmpty(ProcessIMaterielCode)) {

						PageData tocmesParam = new PageData();
						tocmesParam.put("ProcessIMaterielCode", split[0]);
						List<PageData> mesTocStatus = Cabinet_Assembly_DetailService.mesTocStatus(tocmesParam);
						if (CollectionUtil.isNotEmpty(mesTocStatus)) {
							yjbs = mesTocStatus.get(0).getString("预警标识");
						}
					}
				}
			}
			pageData.put("yjbs", yjbs);
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 执行中计划
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/executingPlanlistPage")
	@ResponseBody
	public Object executingPlanlistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.executingPlanlistPage(page);
		for (PageData pageData : varList) {
			String yjbs = "正常范围";
			String ProcessIMaterielCode = pageData.getString("OutputMaterial");
			if (StringUtil.isNotEmpty(ProcessIMaterielCode)) {
				String[] split = ProcessIMaterielCode.split("/");
				if (null != split) {
					if (StringUtil.isNotEmpty(ProcessIMaterielCode)) {

						PageData tocmesParam = new PageData();
						tocmesParam.put("ProcessIMaterielCode", split[0]);
						List<PageData> mesTocStatus = Cabinet_Assembly_DetailService.mesTocStatus(tocmesParam);
						if (CollectionUtil.isNotEmpty(mesTocStatus)) {
							yjbs = mesTocStatus.get(0).getString("预警标识");
						}
					}
				}
			}
			pageData.put("yjbs", yjbs);
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 未执行计划
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/unexecutedPlanlistPage")
	@ResponseBody
	public Object unexecutedPlanlistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.unexecutedPlanlistPage(page);
		for (PageData pageData : varList) {
			String yjbs = "正常范围";
			String ProcessIMaterielCode = pageData.getString("OutputMaterial");
			if (StringUtil.isNotEmpty(ProcessIMaterielCode)) {
				String[] split = ProcessIMaterielCode.split("/");
				if (null != split) {
					if (StringUtil.isNotEmpty(ProcessIMaterielCode)) {

						PageData tocmesParam = new PageData();
						tocmesParam.put("ProcessIMaterielCode", split[0]);
						List<PageData> mesTocStatus = Cabinet_Assembly_DetailService.mesTocStatus(tocmesParam);
						if (CollectionUtil.isNotEmpty(mesTocStatus)) {
							yjbs = mesTocStatus.get(0).getString("预警标识");
						}
					}
				}
			}
			pageData.put("yjbs", yjbs);
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 生产任务下发提醒
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/issueRemindlistPage")
	@ResponseBody
	public Object issueRemindlistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.issueRemindlistPage(page);
		for (PageData pageData : varList) {
			if (null == pageData) {
				continue;
			}
			if ("1".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "生产任务");
			} else if ("2".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "返工任务");
			}

			String WPID = pageData.getString("WP");
			PageData pData = new PageData();
			pData.put("ProcessDefinition_ID", WPID);
			String WPName = pageData.getString("WPName");
			if (null != WPName) {
				pageData.put("WP", WPName);
			}
			String station = "";
			String FStationIDS = pageData.getString("FStation");
			if (Tools.isEmpty(FStationIDS)) {
				continue;
			}
			String[] FStationIDSsplit = FStationIDS.split(",yl,");
			if (!"".equals(FStationIDSsplit[0])) {
				for (String stationId : FStationIDSsplit) {
					PageData stationData = new PageData();
					stationData.put("WC_STATION_ID", stationId);
					PageData WC_STATION = WC_StationService.findById(stationData);
					if (null != WC_STATION) {
						station += "," + WC_STATION.getString("FNAME");
					}
				}
			}
			station = station.substring(1, station.length());
			pageData.put("FStation", station);
			// 执行人 多个
			String ExecutorIDParam = Tools.notEmpty(pageData.getString("ExecutorID")) ? pageData.getString("ExecutorID")
					: "";
			if (!"".equals(ExecutorIDParam)) {
				List<String> ExecutorIDList = Lists.newArrayList(ExecutorIDParam.split(",yl,"));
				if (CollectionUtil.isNotEmpty(ExecutorIDList)) {
					String ExecutorID1 = "";
					for (String e : ExecutorIDList) {
						PageData pageData1 = new PageData();
						pageData1.put("STAFF_ID", e);
						try {
							PageData staffInfo = StaffService.findById(pageData1);
							if (null != staffInfo) {
								ExecutorID1 += ',' + (staffInfo.getString("NAME"));
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if (Tools.notEmpty(ExecutorID1)) {
						pageData.put("ExecutorID", ExecutorID1.subSequence(1, ExecutorID1.length()));
					}

				}
			}
			String FCreatePersonID = pageData.getString("FCreatePersonID");
			if (Tools.notEmpty(FCreatePersonID)) {
				PageData pageData1 = new PageData();
				pageData1.put("STAFF_ID", FCreatePersonID);
				try {
					PageData staffInfo = StaffService.findById(pageData1);
					if (null != staffInfo) {
						FCreatePersonID = (staffInfo.getString("NAME"));
						pageData.put("FCreatePersonID", FCreatePersonID);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 配方核对完成提醒
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/checkDonelistPage")
	@ResponseBody
	public Object checkDonelistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.checkDonelistPage(page);
		for (PageData pageData : varList) {
			if (null == pageData) {
				continue;
			}
			if ("1".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "生产任务");
			} else if ("2".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "返工任务");
			}

			String WPID = pageData.getString("WP");
			PageData pData = new PageData();
			pData.put("ProcessDefinition_ID", WPID);
			String WPName = pageData.getString("WPName");
			if (null != WPName) {
				pageData.put("WP", WPName);
			}
			String station = "";
			String FStationIDS = pageData.getString("FStation");
			if (Tools.isEmpty(FStationIDS)) {
				continue;
			}
			String[] FStationIDSsplit = FStationIDS.split(",yl,");
			if (!"".equals(FStationIDSsplit[0])) {
				for (String stationId : FStationIDSsplit) {
					PageData stationData = new PageData();
					stationData.put("WC_STATION_ID", stationId);
					PageData WC_STATION = WC_StationService.findById(stationData);
					if (null != WC_STATION) {
						station += "," + WC_STATION.getString("FNAME");
					}
				}
			}
			station = station.substring(1, station.length());
			pageData.put("FStation", station);
			// 执行人 多个
			String ExecutorIDParam = Tools.notEmpty(pageData.getString("ExecutorID")) ? pageData.getString("ExecutorID")
					: "";
			if (!"".equals(ExecutorIDParam)) {
				List<String> ExecutorIDList = Lists.newArrayList(ExecutorIDParam.split(",yl,"));
				if (CollectionUtil.isNotEmpty(ExecutorIDList)) {
					String ExecutorID1 = "";
					for (String e : ExecutorIDList) {
						PageData pageData1 = new PageData();
						pageData1.put("STAFF_ID", e);
						try {
							PageData staffInfo = StaffService.findById(pageData1);
							if (null != staffInfo) {
								ExecutorID1 += ',' + (staffInfo.getString("NAME"));
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if (Tools.notEmpty(ExecutorID1)) {
						pageData.put("ExecutorID", ExecutorID1.subSequence(1, ExecutorID1.length()));
					}

				}
			}
			String FCreatePersonID = pageData.getString("FCreatePersonID");
			if (Tools.notEmpty(FCreatePersonID)) {
				PageData pageData1 = new PageData();
				pageData1.put("STAFF_ID", FCreatePersonID);
				try {
					PageData staffInfo = StaffService.findById(pageData1);
					if (null != staffInfo) {
						FCreatePersonID = (staffInfo.getString("NAME"));
						pageData.put("FCreatePersonID", FCreatePersonID);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 生产呼叫质检任务
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/callQIlistPage")
	@ResponseBody
	public Object callQIlistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.callQIlistPage(page);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 生产异常
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/processExceptionlistPage")
	@ResponseBody
	public Object processExceptionlistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.processExceptionlistPage(page);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 返工任务
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/reworkTasklistPage")
	@ResponseBody
	public Object reworkTasklistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.reworkTasklistPage(page);
		for (PageData pageData : varList) {
			if (null == pageData) {
				continue;
			}
			if ("1".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "生产任务");
			} else if ("2".equals(pageData.getString("TaskType"))) {
				pageData.put("TaskTypeName", "返工任务");
			}

			String WPID = pageData.getString("WP");
			PageData pData = new PageData();
			pData.put("ProcessDefinition_ID", WPID);
			String WPName = pageData.getString("WPName");
			if (null != WPName) {
				pageData.put("WP", WPName);
			}
			String station = "";
			String FStationIDS = pageData.getString("FStation");
			if (Tools.isEmpty(FStationIDS)) {
				continue;
			}
			String[] FStationIDSsplit = FStationIDS.split(",yl,");
			if (!"".equals(FStationIDSsplit[0])) {
				for (String stationId : FStationIDSsplit) {
					PageData stationData = new PageData();
					stationData.put("WC_STATION_ID", stationId);
					PageData WC_STATION = WC_StationService.findById(stationData);
					if (null != WC_STATION) {
						station += "," + WC_STATION.getString("FNAME");
					}
				}
			}
			station = station.substring(1, station.length());
			pageData.put("FStation", station);
			// 执行人 多个
			String ExecutorIDParam = Tools.notEmpty(pageData.getString("ExecutorID")) ? pageData.getString("ExecutorID")
					: "";
			if (!"".equals(ExecutorIDParam)) {
				List<String> ExecutorIDList = Lists.newArrayList(ExecutorIDParam.split(",yl,"));
				if (CollectionUtil.isNotEmpty(ExecutorIDList)) {
					String ExecutorID1 = "";
					for (String e : ExecutorIDList) {
						PageData pageData1 = new PageData();
						pageData1.put("STAFF_ID", e);
						try {
							PageData staffInfo = StaffService.findById(pageData1);
							if (null != staffInfo) {
								ExecutorID1 += ',' + (staffInfo.getString("NAME"));
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if (Tools.notEmpty(ExecutorID1)) {
						pageData.put("ExecutorID", ExecutorID1.subSequence(1, ExecutorID1.length()));
					}

				}
			}
			String FCreatePersonID = pageData.getString("FCreatePersonID");
			if (Tools.notEmpty(FCreatePersonID)) {
				PageData pageData1 = new PageData();
				pageData1.put("STAFF_ID", FCreatePersonID);
				try {
					PageData staffInfo = StaffService.findById(pageData1);
					if (null != staffInfo) {
						FCreatePersonID = (staffInfo.getString("NAME"));
						pageData.put("FCreatePersonID", FCreatePersonID);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}

	/**
	 * 计划延期提醒
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/planDelaylistPage")
	@ResponseBody
	public Object planDelaylistPage(Page page) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");
		if (Tools.notEmpty(KEYWORDS))
			pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = homepageService.planDelaylistPage(page);
		for (PageData pageData : varList) {

			Date PlannedEndTimeDate = Tools.str2Date(pageData.getString("PlannedEndTime"));
			Date now = new Date();
			long between = (now.getTime() - PlannedEndTimeDate.getTime()) / 1000;
			BigDecimal betweenBig = new BigDecimal(between);
			BigDecimal divide = betweenBig.divide(new BigDecimal(3600 * 24), 2, BigDecimal.ROUND_HALF_UP);
			pageData.put("DaySub", divide);
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
}
